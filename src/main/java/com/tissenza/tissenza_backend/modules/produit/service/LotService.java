package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.entity.Lot;
import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import com.tissenza.tissenza_backend.modules.produit.repository.ArticleRepository;
import com.tissenza.tissenza_backend.modules.produit.repository.LotRepository;
import com.tissenza.tissenza_backend.modules.produit.repository.HistoriqueStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class LotService {

    @Autowired
    private LotRepository lotRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private HistoriqueStockRepository historiqueStockRepository;

    /**
     * Créer un nouveau lot pour un article
     */
    public Lot createLot(Long articleId, Lot lot) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + articleId));

        // Génération automatique du numéro de lot si non fourni
        if (lot.getNumeroLot() == null || lot.getNumeroLot().isEmpty()) {
            lot.setNumeroLot(genererNumeroLot());
        }

        // Vérifier l'unicité du numéro de lot
        if (lotRepository.findByNumeroLot(lot.getNumeroLot()) != null) {
            throw new RuntimeException("Numéro de lot déjà utilisé: " + lot.getNumeroLot());
        }

        lot.setArticle(article);
        lot.setQuantiteRestante(lot.getQuantiteInitiale());
        
        Lot savedLot = lotRepository.save(lot);
        
        // Mettre à jour le stock de l'article
        updateArticleStock(articleId);
        
        return savedLot;
    }

    /**
     * Générer un numéro de lot unique
     */
    private String genererNumeroLot() {
        return "LOT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Mettre à jour le stock d'un article à partir de ses lots
     */
    private void updateArticleStock(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + articleId));
        
        Integer stockTotal = lotRepository.calculerStockTotalParArticle(articleId);
        article.setStockActuel(stockTotal);
        articleRepository.save(article);
    }

    /**
     * Lister tous les lots d'un article
     */
    public List<Lot> getLotsByArticle(Long articleId) {
        return lotRepository.findByArticleId(articleId);
    }

    /**
     * Lister les lots actifs d'un article
     */
    public List<Lot> getLotsActifsByArticle(Long articleId) {
        return lotRepository.findByArticleIdAndStatut(articleId, Lot.Statut.ACTIF);
    }

    /**
     * Trouver un lot par son ID
     */
    public Optional<Lot> getLotById(Long id) {
        return lotRepository.findById(id);
    }

    /**
     * Trouver un lot par son numéro
     */
    public Optional<Lot> getLotByNumero(String numeroLot) {
        return Optional.ofNullable(lotRepository.findByNumeroLot(numeroLot));
    }

    /**
     * Mettre à jour un lot
     */
    public Lot updateLot(Long id, Lot lotDetails) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lot non trouvé avec l'ID: " + id));

        lot.setQuantiteInitiale(lotDetails.getQuantiteInitiale());
        lot.setQuantiteRestante(lotDetails.getQuantiteRestante());
        lot.setDateFabrication(lotDetails.getDateFabrication());
        lot.setDateExpiration(lotDetails.getDateExpiration());
        lot.setPrixAchat(lotDetails.getPrixAchat());
        lot.setFournisseur(lotDetails.getFournisseur());
        lot.setEmplacement(lotDetails.getEmplacement());
        lot.setStatut(lotDetails.getStatut());

        Lot updatedLot = lotRepository.save(lot);
        updateArticleStock(lot.getArticle().getId());
        
        return updatedLot;
    }

    /**
     * Supprimer un lot
     */
    public void deleteLot(Long id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lot non trouvé avec l'ID: " + id));
        
        Long articleId = lot.getArticle().getId();
        lotRepository.delete(lot);
        updateArticleStock(articleId);
    }

    /**
     * Retirer du stock d'un lot
     */
    public Lot retirerStock(Long lotId, Integer quantite, String motif) {
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot non trouvé avec l'ID: " + lotId));

        lot.retirerQuantite(quantite);
        
        Lot updatedLot = lotRepository.save(lot);
        updateArticleStock(lot.getArticle().getId());
        
        // Créer une entrée dans l'historique des stocks
        creerHistoriqueStock(lot.getArticle(), quantite, HistoriqueStock.Type.SORTIE, motif);
        
        return updatedLot;
    }

    /**
     * Ajouter du stock à un lot
     */
    public Lot ajouterStock(Long lotId, Integer quantite, String motif) {
        Lot lot = lotRepository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot non trouvé avec l'ID: " + lotId));

        lot.ajouterQuantite(quantite);
        
        Lot updatedLot = lotRepository.save(lot);
        updateArticleStock(lot.getArticle().getId());
        
        // Créer une entrée dans l'historique des stocks
        creerHistoriqueStock(lot.getArticle(), quantite, HistoriqueStock.Type.ENTREE, motif);
        
        return updatedLot;
    }

    /**
     * Lister les lots proches de la péremption
     */
    public List<Lot> getLotsProchesPeremption() {
        LocalDate aujourdhui = LocalDate.now();
        LocalDate dans30Jours = aujourdhui.plusDays(30);
        return lotRepository.findLotsProchesPeremption(aujourdhui, dans30Jours);
    }

    /**
     * Lister les lots périmés
     */
    public List<Lot> getLotsPerimes() {
        return lotRepository.findLotsPerimes(LocalDate.now());
    }

    /**
     * Marquer les lots périmés comme tels
     */
    public void marquerLotsPerimes() {
        List<Lot> lotsPerimes = getLotsPerimes();
        for (Lot lot : lotsPerimes) {
            lot.setStatut(Lot.Statut.PERIME);
            lotRepository.save(lot);
        }
    }

    /**
     * Lister les lots épuisés
     */
    public List<Lot> getLotsEpuises() {
        return lotRepository.findByQuantiteRestanteLessThanAndStatutNot(1, Lot.Statut.EPUISE);
    }

    /**
     * Lister les lots par fournisseur
     */
    public List<Lot> getLotsByFournisseur(String fournisseur) {
        return lotRepository.findByFournisseurContainingIgnoreCase(fournisseur);
    }

    /**
     * Lister les lots par emplacement
     */
    public List<Lot> getLotsByEmplacement(String emplacement) {
        return lotRepository.findByEmplacementContainingIgnoreCase(emplacement);
    }

    /**
     * Lister les lots avec stock faible
     */
    public List<Lot> getLotsStockFaible(Integer seuil) {
        return lotRepository.findLotsStockFaible(seuil);
    }

    /**
     * Rechercher des lots par mot-clé
     */
    public List<Lot> searchLots(String keyword) {
        return lotRepository.searchByKeyword(keyword);
    }

    /**
     * Obtenir des statistiques sur les lots
     */
    public LotStatistics getStatistics() {
        Long totalLots = lotRepository.count();
        Long lotsActifs = lotRepository.countByStatut(Lot.Statut.ACTIF);
        Long lotsPerimes = lotRepository.countByStatut(Lot.Statut.PERIME);
        Long lotsEpuises = lotRepository.countByStatut(Lot.Statut.EPUISE);
        Long lotsBloques = lotRepository.countByStatut(Lot.Statut.BLOQUE);

        return new LotStatistics(totalLots, lotsActifs, lotsPerimes, lotsEpuises, lotsBloques);
    }

    /**
     * Créer une entrée dans l'historique des stocks
     */
    private void creerHistoriqueStock(Article article, Integer quantite, HistoriqueStock.Type type, String motif) {
        HistoriqueStock historique = new HistoriqueStock();
        historique.setArticle(article);
        historique.setQuantite(quantite);
        historique.setType(type);
        historique.setMotif(motif);
        
        historiqueStockRepository.save(historique);
    }

    /**
     * Classe pour les statistiques des lots
     */
    public static class LotStatistics {
        private final Long total;
        private final Long actifs;
        private final Long perimes;
        private final Long epuises;
        private final Long bloques;

        public LotStatistics(Long total, Long actifs, Long perimes, Long epuises, Long bloques) {
            this.total = total;
            this.actifs = actifs;
            this.perimes = perimes;
            this.epuises = epuises;
            this.bloques = bloques;
        }

        // Getters
        public Long getTotal() { return total; }
        public Long getActifs() { return actifs; }
        public Long getPerimes() { return perimes; }
        public Long getEpuises() { return epuises; }
        public Long getBloques() { return bloques; }
    }
}
