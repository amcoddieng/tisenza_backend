package com.tissenza.tissenza_backend.modules.commande.dto;

import com.tissenza.tissenza_backend.modules.commande.entity.LigneCommande;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeDTO {
    private Long id;
    private Long commandeId;
    private Long articleId;
    private String articleNom;
    private String articleSku;
    private String articleImage;
    private Long lotId;
    private String lotNumero;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal remise;
    private BigDecimal montantTotal;
    private BigDecimal prixAchatUnitaire;
    private BigDecimal marge;
    private BigDecimal tauxMarge;
    private LocalDateTime createdAt;

    // Constructeur depuis l'entité
    public LigneCommandeDTO(LigneCommande ligne) {
        this.id = ligne.getId();
        this.commandeId = ligne.getCommande().getId();
        this.articleId = ligne.getArticle().getId();
        this.articleNom = ligne.getArticle().getProduit().getNom();
        this.articleSku = ligne.getArticle().getSku();
        this.articleImage = ligne.getArticle().getImage();
        this.quantite = ligne.getQuantite();
        this.prixUnitaire = ligne.getPrixUnitaire();
        this.remise = ligne.getRemise();
        this.montantTotal = ligne.getMontantTotal();
        this.prixAchatUnitaire = ligne.getPrixAchatUnitaire();
        this.createdAt = ligne.getCreatedAt();
        
        if (ligne.getLot() != null) {
            this.lotId = ligne.getLot().getId();
            this.lotNumero = ligne.getLot().getNumeroLot();
        }
        
        // Calculer la marge si le prix d'achat est disponible
        if (ligne.getPrixAchatUnitaire() != null) {
            this.marge = ligne.getMarge();
            this.tauxMarge = ligne.getTauxMarge();
        }
    }

    // Méthodes utilitaires
    public BigDecimal getSousTotal() {
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public BigDecimal getMontantRemise() {
        return remise;
    }

    public boolean aRemise() {
        return remise != null && remise.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean aMarge() {
        return marge != null && marge.compareTo(BigDecimal.ZERO) > 0;
    }

    public String getTauxMargePourcentage() {
        if (tauxMarge != null) {
            return tauxMarge.setScale(2, RoundingMode.HALF_UP) + "%";
        }
        return "0%";
    }
}
