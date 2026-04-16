package com.tissenza.tissenza_backend.modules.commande.service;

import com.tissenza.tissenza_backend.modules.commande.entity.*;
import com.tissenza.tissenza_backend.modules.commande.repository.*;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.service.ArticleService;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.service.CompteService;
import com.tissenza.tissenza_backend.modules.user.repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    @Autowired
    private HistoriqueCommandeRepository historiqueCommandeRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private CompteService compteService;

    @Autowired
    private CompteRepository compteRepository;

    /**
     * Créer une nouvelle commande
     */
    public Commande creerCommande(Long clientId, List<LigneCommandeRequest> lignesCommande, 
                                 String adresseLivraison, String notes, LocalDateTime dateLivraisonSouhaitee) {
        
        // Vérifier le client
        Compte client = trouverCompteParId(clientId);
        
        // Créer la commande
        Commande commande = new Commande();
        commande.setClient(client);
        commande.setNumeroCommande(genererNumeroCommande());
        commande.setAdresseLivraison(adresseLivraison);
        commande.setNotes(notes);
        commande.setDateLivraisonSouhaitee(dateLivraisonSouhaitee);
        commande.setStatut(Commande.StatutCommande.EN_ATTENTE);
        
        // Sauvegarder la commande
        commande = commandeRepository.save(commande);
        
        // Créer les lignes de commande
        BigDecimal montantTotal = BigDecimal.ZERO;
        for (LigneCommandeRequest request : lignesCommande) {
            LigneCommande ligne = creerLigneCommande(commande, request);
            montantTotal = montantTotal.add(ligne.getMontantTotal());
        }
        
        commande.setMontantTotal(montantTotal);
        commande = commandeRepository.save(commande);
        
        // Enregistrer l'historique
        enregistrerHistorique(commande, null, Commande.StatutCommande.EN_ATTENTE, 
                            client, "Création de la commande");
        
        return commande;
    }

    /**
     * Valider une commande
     */
    public Commande validerCommande(Long commandeId, Long valideurId, String motif) {
        Commande commande = trouverCommandeParId(commandeId);
        Compte valideur = trouverCompteParId(valideurId);
        
        changerStatutCommande(commande, Commande.StatutCommande.VALIDEE, 
                             valideur, motif);
        
        // Réserver le stock
        reserverStock(commande);
        
        return commande;
    }

    /**
     * Mettre une commande en préparation
     */
    public Commande mettreEnPreparation(Long commandeId, Long prepareurId, String motif) {
        Commande commande = trouverCommandeParId(commandeId);
        Compte prepareur = trouverCompteParId(prepareurId);
        
        changerStatutCommande(commande, Commande.StatutCommande.EN_PREPARATION, 
                             prepareur, motif);
        
        return commande;
    }

    /**
     * Marquer une commande comme prête
     */
    public Commande marquerCommePrete(Long commandeId, Long prepareurId, String motif) {
        Commande commande = trouverCommandeParId(commandeId);
        Compte prepareur = trouverCompteParId(prepareurId);
        
        changerStatutCommande(commande, Commande.StatutCommande.PRETE, 
                             prepareur, motif);
        
        return commande;
    }

    /**
     * Mettre une commande en livraison
     */
    public Commande mettreEnLivraison(Long commandeId, Long livreurId, String motif) {
        Commande commande = trouverCommandeParId(commandeId);
        Compte livreur = trouverCompteParId(livreurId);
        
        changerStatutCommande(commande, Commande.StatutCommande.EN_LIVRAISON, 
                             livreur, motif);
        
        return commande;
    }

    /**
     * Livrer une commande
     */
    public Commande livrerCommande(Long commandeId, Long livreurId, String motif) {
        Commande commande = trouverCommandeParId(commandeId);
        Compte livreur = trouverCompteParId(livreurId);
        
        changerStatutCommande(commande, Commande.StatutCommande.LIVREE, 
                             livreur, motif);
        
        commande.setDateLivraisonReelle(LocalDateTime.now());
        
        // Confirmer la sortie du stock
        confirmerSortieStock(commande);
        
        return commandeRepository.save(commande);
    }

    /**
     * Annuler une commande
     */
    public Commande annulerCommande(Long commandeId, Long annuleurId, String motif) {
        Commande commande = trouverCommandeParId(commandeId);
        Compte annuleur = trouverCompteParId(annuleurId);
        
        // Vérifier si l'annulation est possible
        if (!peutEtreAnnulee(commande)) {
            throw new RuntimeException("La commande ne peut pas être annulée à ce stade");
        }
        
        changerStatutCommande(commande, Commande.StatutCommande.ANNULEE, 
                             annuleur, motif);
        
        // Libérer le stock réservé
        libererStockReserve(commande);
        
        return commande;
    }

    /**
     * Obtenir une commande par son ID
     */
    public Commande getCommandeById(Long id) {
        return trouverCommandeParId(id);
    }

    /**
     * Obtenir une commande par son numéro
     */
    public Commande getCommandeByNumero(String numeroCommande) {
        return commandeRepository.findByNumeroCommande(numeroCommande)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec le numéro: " + numeroCommande));
    }

    /**
     * Lister les commandes d'un client
     */
    public List<Commande> getCommandesByClient(Long clientId) {
        return commandeRepository.findByClientIdOrderByCreatedAtDesc(clientId);
    }

    /**
     * Lister les commandes par statut
     */
    public List<Commande> getCommandesByStatut(Commande.StatutCommande statut) {
        return commandeRepository.findByStatutOrderByCreatedAtAsc(statut);
    }

    /**
     * Lister les commandes en retard
     */
    public List<Commande> getCommandesEnRetard() {
        return commandeRepository.findCommandesEnRetard(LocalDateTime.now());
    }

    /**
     * Obtenir les statistiques des commandes
     */
    public CommandeStatistics getStatistiquesCommandes() {
        long totalCommandes = commandeRepository.count();
        long commandesEnAttente = commandeRepository.countByStatut(Commande.StatutCommande.EN_ATTENTE);
        long commandesValidees = commandeRepository.countByStatut(Commande.StatutCommande.VALIDEE);
        long commandesEnPreparation = commandeRepository.countByStatut(Commande.StatutCommande.EN_PREPARATION);
        long commandesLivrees = commandeRepository.countByStatut(Commande.StatutCommande.LIVREE);
        long commandesAnnulees = commandeRepository.countByStatut(Commande.StatutCommande.ANNULEE);
        
        BigDecimal caTotal = commandeRepository.sumMontantTotalByStatut(Commande.StatutCommande.LIVREE);
        
        return new CommandeStatistics(totalCommandes, commandesEnAttente, commandesValidees,
                                    commandesEnPreparation, commandesLivrees, commandesAnnulees, caTotal);
    }

    // Méthodes privées

    private LigneCommande creerLigneCommande(Commande commande, LigneCommandeRequest request) {
        Article article = articleService.getArticleById(request.getArticleId())
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + request.getArticleId()));
        
        // Vérifier la disponibilité du stock
        if (article.getStockActuel() < request.getQuantite()) {
            throw new RuntimeException("Stock insuffisant pour l'article: " + article.getProduit().getNom());
        }
        
        LigneCommande ligne = new LigneCommande();
        ligne.setCommande(commande);
        ligne.setArticle(article);
        ligne.setQuantite(request.getQuantite());
        ligne.setPrixUnitaire(request.getPrixUnitaire() != null ? request.getPrixUnitaire() : article.getPrix());
        ligne.setRemise(request.getRemise() != null ? request.getRemise() : BigDecimal.ZERO);
        ligne.setPrixAchatUnitaire(request.getPrixAchatUnitaire());
        
        // Calculer le montant total
        ligne.calculerMontantTotal();
        
        return ligneCommandeRepository.save(ligne);
    }

    private void changerStatutCommande(Commande commande, Commande.StatutCommande nouveauStatut,
                                     Compte modifiePar, String motif) {
        Commande.StatutCommande ancienStatut = commande.getStatut();
        commande.setStatut(nouveauStatut);
        commandeRepository.save(commande);
        
        enregistrerHistorique(commande, ancienStatut, nouveauStatut, modifiePar, motif);
    }

    private void enregistrerHistorique(Commande commande, Commande.StatutCommande ancienStatut,
                                     Commande.StatutCommande nouveauStatut, Compte modifiePar, String motif) {
        HistoriqueCommande historique = new HistoriqueCommande(commande, ancienStatut, 
                                                              nouveauStatut, modifiePar, motif);
        historiqueCommandeRepository.save(historique);
    }

    private void reserverStock(Commande commande) {
        for (LigneCommande ligne : commande.getLignesCommande()) {
            articleService.removeStock(ligne.getArticle().getId(), ligne.getQuantite(), 
                                       "Réservation pour commande " + commande.getNumeroCommande());
        }
    }

    private void confirmerSortieStock(Commande commande) {
        // Le stock a déjà été retiré lors de la réservation
        // On pourrait ici ajouter une logique de confirmation si nécessaire
    }

    private void libererStockReserve(Commande commande) {
        for (LigneCommande ligne : commande.getLignesCommande()) {
            articleService.addStock(ligne.getArticle().getId(), ligne.getQuantite(), 
                                      "Annulation commande " + commande.getNumeroCommande());
        }
    }

    private boolean peutEtreAnnulee(Commande commande) {
        return commande.getStatut() == Commande.StatutCommande.EN_ATTENTE ||
               commande.getStatut() == Commande.StatutCommande.VALIDEE;
    }

    private String genererNumeroCommande() {
        return "CMD-" + LocalDateTime.now().toString().replace(":", "").substring(0, 14) + 
               "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private Commande trouverCommandeParId(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + id));
    }

    private Compte trouverCompteParId(Long id) {
        return compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec l'ID: " + id));
    }

    // DTO pour la création de ligne de commande
    public static class LigneCommandeRequest {
        private Long articleId;
        private Integer quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal remise;
        private BigDecimal prixAchatUnitaire;

        // Getters et Setters
        public Long getArticleId() { return articleId; }
        public void setArticleId(Long articleId) { this.articleId = articleId; }
        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
        public BigDecimal getPrixUnitaire() { return prixUnitaire; }
        public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }
        public BigDecimal getRemise() { return remise; }
        public void setRemise(BigDecimal remise) { this.remise = remise; }
        public BigDecimal getPrixAchatUnitaire() { return prixAchatUnitaire; }
        public void setPrixAchatUnitaire(BigDecimal prixAchatUnitaire) { this.prixAchatUnitaire = prixAchatUnitaire; }
    }

    // Classe pour les statistiques
    public static class CommandeStatistics {
        private final long totalCommandes;
        private final long commandesEnAttente;
        private final long commandesValidees;
        private final long commandesEnPreparation;
        private final long commandesLivrees;
        private final long commandesAnnulees;
        private final BigDecimal chiffreAffaires;

        public CommandeStatistics(long totalCommandes, long commandesEnAttente, long commandesValidees,
                                long commandesEnPreparation, long commandesLivrees, long commandesAnnulees,
                                BigDecimal chiffreAffaires) {
            this.totalCommandes = totalCommandes;
            this.commandesEnAttente = commandesEnAttente;
            this.commandesValidees = commandesValidees;
            this.commandesEnPreparation = commandesEnPreparation;
            this.commandesLivrees = commandesLivrees;
            this.commandesAnnulees = commandesAnnulees;
            this.chiffreAffaires = chiffreAffaires != null ? chiffreAffaires : BigDecimal.ZERO;
        }

        // Getters
        public long getTotalCommandes() { return totalCommandes; }
        public long getCommandesEnAttente() { return commandesEnAttente; }
        public long getCommandesValidees() { return commandesValidees; }
        public long getCommandesEnPreparation() { return commandesEnPreparation; }
        public long getCommandesLivrees() { return commandesLivrees; }
        public long getCommandesAnnulees() { return commandesAnnulees; }
        public BigDecimal getChiffreAffaires() { return chiffreAffaires; }
    }
}
