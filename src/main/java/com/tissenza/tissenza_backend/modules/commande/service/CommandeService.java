package com.tissenza.tissenza_backend.modules.commande.service;

import com.tissenza.tissenza_backend.modules.commande.dto.CommandeDTO;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande.CommandeStatus;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande.StatusPaiement;
import com.tissenza.tissenza_backend.modules.commande.entity.DetailCommande;
import com.tissenza.tissenza_backend.modules.commande.mapper.CommandeMapper;
import com.tissenza.tissenza_backend.modules.commande.repository.CommandeRepository;
import com.tissenza.tissenza_backend.modules.commande.repository.DetailCommandeRepository;
import com.tissenza.tissenza_backend.modules.panier.entity.Panier;
import com.tissenza.tissenza_backend.modules.panier.entity.PanierItem;
import com.tissenza.tissenza_backend.modules.panier.repository.PanierRepository;
import com.tissenza.tissenza_backend.modules.panier.service.PanierService;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.repository.PersonneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final DetailCommandeRepository detailCommandeRepository;
    private final CommandeMapper commandeMapper;
    private final PanierRepository panierRepository;
    private final PanierService panierService;
    private final PersonneRepository personneRepository;

    /**
     * Créer une commande à partir d'un panier
     */
    @Transactional
    public CommandeDTO creerCommandeDepuisPanier(Long panierId) {
        // Récupérer le panier
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));

        // Vérifier que le panier n'est pas déjà validé
        if (panier.getStatus() != com.tissenza.tissenza_backend.modules.panier.entity.Panier.PanierStatus.EN_ATTENTE) {
            throw new RuntimeException("Le panier n'est pas en attente et ne peut être transformé en commande");
        }

        // Vérifier que le panier contient des articles
        if (panier.getItems() == null || panier.getItems().isEmpty()) {
            throw new RuntimeException("Le panier est vide");
        }

        // Créer la commande
        Commande commande = new Commande();
        commande.setClient(panier.getClient());
        commande.setPanier(panier);
        commande.setTotal(panier.getTotal());
        commande.setStatus(CommandeStatus.EN_ATTENTE);
        commande.setStatusPaiement(StatusPaiement.EN_ATTENTE);

        commande = commandeRepository.save(commande);
        log.info("Commande créée à partir du panier {}: {}", panierId, commande.getId());

        // Créer les détails de commande
        for (PanierItem panierItem : panier.getItems()) {
            DetailCommande detail = new DetailCommande();
            detail.setCommande(commande);
            detail.setArticle(panierItem.getArticle());
            detail.setQuantite(panierItem.getQuantite());
            detail.setPrixUnitaire(panierItem.getPrixUnitaire());
            // Le sous-total sera calculé automatiquement dans @PrePersist
            
            detailCommandeRepository.save(detail);
            log.info("Détail de commande créé pour l'article {}: quantité={}, prix={}", 
                    panierItem.getArticle().getSku(), panierItem.getQuantite(), panierItem.getPrixUnitaire());
        }

        // Valider le panier
        panierService.validerPanier(panierId);

        return commandeMapper.toDTO(commande);
    }

    /**
     * Mettre à jour le statut d'une commande
     */
    @Transactional
    public CommandeDTO mettreAJourStatut(Long commandeId, CommandeStatus nouveauStatut) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + commandeId));

        commande.setStatus(nouveauStatut);
        commandeRepository.save(commande);
        
        log.info("Statut de la commande {} mis à jour: {}", commandeId, nouveauStatut);
        return commandeMapper.toDTO(commande);
    }

    /**
     * Mettre à jour le statut de paiement d'une commande
     */
    @Transactional
    public CommandeDTO mettreAJourStatutPaiement(Long commandeId, StatusPaiement nouveauStatutPaiement) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + commandeId));

        commande.setStatusPaiement(nouveauStatutPaiement);
        commandeRepository.save(commande);
        
        log.info("Statut de paiement de la commande {} mis à jour: {}", commandeId, nouveauStatutPaiement);
        return commandeMapper.toDTO(commande);
    }

    /**
     * Annuler une commande
     */
    @Transactional
    public CommandeDTO annulerCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + commandeId));

        // Vérifier que la commande peut être annulée
        if (commande.getStatus() == CommandeStatus.LIVREE) {
            throw new RuntimeException("Une commande livrée ne peut être annulée");
        }

        commande.setStatus(CommandeStatus.ANNULEE);
        commandeRepository.save(commande);
        
        log.info("Commande annulée: {}", commandeId);
        return commandeMapper.toDTO(commande);
    }

    /**
     * Récupérer une commande par ID
     */
    @Transactional(readOnly = true)
    public CommandeDTO getCommandeById(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + commandeId));
        return commandeMapper.toDTO(commande);
    }

    /**
     * Récupérer toutes les commandes d'un client
     */
    @Transactional(readOnly = true)
    public List<CommandeDTO> getCommandesByClient(Long clientId) {
        List<Commande> commandes = commandeRepository.findByClientId(clientId);
        return commandeMapper.toDTOList(commandes);
    }

    /**
     * Récupérer les commandes par statut
     */
    @Transactional(readOnly = true)
    public List<CommandeDTO> getCommandesByStatut(CommandeStatus statut) {
        List<Commande> commandes = commandeRepository.findByStatus(statut);
        return commandeMapper.toDTOList(commandes);
    }

    /**
     * Récupérer les commandes par statut de paiement
     */
    @Transactional(readOnly = true)
    public List<CommandeDTO> getCommandesByStatutPaiement(StatusPaiement statutPaiement) {
        List<Commande> commandes = commandeRepository.findByStatusPaiement(statutPaiement);
        return commandeMapper.toDTOList(commandes);
    }

    /**
     * Calculer le chiffre d'affaires total
     */
    @Transactional(readOnly = true)
    public BigDecimal calculerChiffreAffairesTotal() {
        BigDecimal chiffreAffaires = commandeRepository.calculateTotalRevenueByStatus(CommandeStatus.LIVREE);
        return chiffreAffaires != null ? chiffreAffaires : BigDecimal.ZERO;
    }

    /**
     * Récupérer les détails d'une commande
     */
    @Transactional(readOnly = true)
    public List<DetailCommande> getDetailsCommande(Long commandeId) {
        return detailCommandeRepository.findByCommandeId(commandeId);
    }

    /**
     * Supprimer une commande (administrateur uniquement)
     */
    @Transactional
    public void supprimerCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + commandeId));

        // Supprimer les détails d'abord
        detailCommandeRepository.deleteByCommandeId(commandeId);
        
        // Supprimer la commande
        commandeRepository.delete(commande);
        
        log.info("Commande supprimée: {}", commandeId);
    }
}
