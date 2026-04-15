package com.tissenza.tissenza_backend.modules.commande.dto;

import com.tissenza.tissenza_backend.modules.commande.entity.HistoriqueCommande;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueCommandeDTO {
    private Long id;
    private Long commandeId;
    private String commandeNumero;
    private String ancienStatut;
    private String nouveauStatut;
    private Long modifieParId;
    private String modifieParNom;
    private String modifieParEmail;
    private String motif;
    private LocalDateTime createdAt;

    // Constructeur depuis l'entité
    public HistoriqueCommandeDTO(HistoriqueCommande historique) {
        this.id = historique.getId();
        this.commandeId = historique.getCommande().getId();
        this.commandeNumero = historique.getCommande().getNumeroCommande();
        this.ancienStatut = historique.getAncienStatut() != null ? 
                           historique.getAncienStatut().toString() : null;
        this.nouveauStatut = historique.getNouveauStatut().toString();
        this.motif = historique.getMotif();
        this.createdAt = historique.getCreatedAt();
        
        if (historique.getModifiePar() != null) {
            this.modifieParId = historique.getModifiePar().getId();
            this.modifieParNom = historique.getModifiePar().getPersonne().getNom() + " " + 
                               historique.getModifiePar().getPersonne().getPrenom();
            this.modifieParEmail = historique.getModifiePar().getEmail();
        }
    }

    // Méthodes utilitaires
    public String getAncienStatutAffichage() {
        return getStatutAffichage(ancienStatut);
    }

    public String getNouveauStatutAffichage() {
        return getStatutAffichage(nouveauStatut);
    }

    public String getChangementStatut() {
        if (ancienStatut == null) {
            return "Création → " + getNouveauStatutAffichage();
        }
        return getAncienStatutAffichage() + " → " + getNouveauStatutAffichage();
    }

    private String getStatutAffichage(String statut) {
        if (statut == null) return null;
        
        switch (statut) {
            case "EN_ATTENTE": return "En attente";
            case "VALIDEE": return "Validée";
            case "EN_PREPARATION": return "En préparation";
            case "PRETE": return "Prête";
            case "EN_LIVRAISON": return "En livraison";
            case "LIVREE": return "Livrée";
            case "ANNULEE": return "Annulée";
            default: return statut;
        }
    }

    public boolean estCreation() {
        return ancienStatut == null;
    }

    public boolean estAnnulation() {
        return "ANNULEE".equals(nouveauStatut);
    }

    public boolean estLivraison() {
        return "LIVREE".equals(nouveauStatut);
    }
}
