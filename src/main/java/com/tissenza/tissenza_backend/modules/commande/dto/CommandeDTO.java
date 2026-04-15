package com.tissenza.tissenza_backend.modules.commande.dto;

import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDTO {
    private Long id;
    private Long clientId;
    private String clientEmail;
    private String clientNom;
    private String numeroCommande;
    private BigDecimal montantTotal;
    private String statut;
    private String adresseLivraison;
    private String notes;
    private LocalDateTime dateLivraisonSouhaitee;
    private LocalDateTime dateLivraisonReelle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<LigneCommandeDTO> lignesCommande;

    // Constructeur depuis l'entité
    public CommandeDTO(Commande commande) {
        this.id = commande.getId();
        this.clientId = commande.getClient().getId();
        this.clientEmail = commande.getClient().getEmail();
        this.clientNom = commande.getClient().getPersonne().getNom() + " " + 
                        commande.getClient().getPersonne().getPrenom();
        this.numeroCommande = commande.getNumeroCommande();
        this.montantTotal = commande.getMontantTotal();
        this.statut = commande.getStatut().toString();
        this.adresseLivraison = commande.getAdresseLivraison();
        this.notes = commande.getNotes();
        this.dateLivraisonSouhaitee = commande.getDateLivraisonSouhaitee();
        this.dateLivraisonReelle = commande.getDateLivraisonReelle();
        this.createdAt = commande.getCreatedAt();
        this.updatedAt = commande.getUpdatedAt();
        
        if (commande.getLignesCommande() != null) {
            this.lignesCommande = commande.getLignesCommande().stream()
                    .map(LigneCommandeDTO::new)
                    .collect(Collectors.toList());
        }
    }

    // Méthodes utilitaires
    public boolean estEnAttente() {
        return "EN_ATTENTE".equals(statut);
    }

    public boolean estValidee() {
        return "VALIDEE".equals(statut);
    }

    public boolean estEnPreparation() {
        return "EN_PREPARATION".equals(statut);
    }

    public boolean estPrete() {
        return "PRETE".equals(statut);
    }

    public boolean estEnLivraison() {
        return "EN_LIVRAISON".equals(statut);
    }

    public boolean estLivree() {
        return "LIVREE".equals(statut);
    }

    public boolean estAnnulee() {
        return "ANNULEE".equals(statut);
    }

    public String getStatutAffichage() {
        switch (statut) {
            case "EN_ATTENTE": return "En attente de validation";
            case "VALIDEE": return "Validée";
            case "EN_PREPARATION": return "En préparation";
            case "PRETE": return "Prête pour livraison";
            case "EN_LIVRAISON": return "En cours de livraison";
            case "LIVREE": return "Livrée";
            case "ANNULEE": return "Annulée";
            default: return statut;
        }
    }
}
