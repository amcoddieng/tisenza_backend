package com.tissenza.tissenza_backend.modules.produit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotDTO {
    
    private Long id;
    private Long articleId;
    private String articleNom;
    private String numeroLot;
    private Integer quantiteInitiale;
    private Integer quantiteRestante;
    private LocalDate dateFabrication;
    private LocalDate dateExpiration;
    private Double prixAchat;
    private String fournisseur;
    private String emplacement;
    private String statut;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Champs calculés
    private boolean perime;
    private boolean prochePeremption;
    private boolean epuise;
    private Integer joursAvantPeremption;
    private Double tauxUtilisation;
    
    /**
     * Calcule si le lot est périmé
     */
    public boolean isPerime() {
        return dateExpiration != null && dateExpiration.isBefore(LocalDate.now());
    }
    
    /**
     * Calcule si le lot est proche de la péremption (30 jours)
     */
    public boolean isProchePeremption() {
        return dateExpiration != null && 
               dateExpiration.minusDays(30).isBefore(LocalDate.now()) && 
               !isPerime();
    }
    
    /**
     * Calcule si le lot est épuisé
     */
    public boolean isEpuise() {
        return quantiteRestante <= 0;
    }
    
    /**
     * Calcule le nombre de jours avant la péremption
     */
    public Integer getJoursAvantPeremption() {
        if (dateExpiration == null) {
            return null;
        }
        long jours = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dateExpiration);
        return (int) Math.max(0, jours);
    }
    
    /**
     * Calcule le taux d'utilisation du lot
     */
    public Double getTauxUtilisation() {
        if (quantiteInitiale == null || quantiteInitiale == 0) {
            return 0.0;
        }
        return ((double) (quantiteInitiale - quantiteRestante) / quantiteInitiale) * 100;
    }
}
