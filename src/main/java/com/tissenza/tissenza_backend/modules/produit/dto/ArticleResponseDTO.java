package com.tissenza.tissenza_backend.modules.produit.dto;

import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Article
 * Résout le problème de LazyInitializationException en n'exposant que les champs nécessaires
 */
public class ArticleResponseDTO {
    private Long id;
    private String sku;
    private BigDecimal prix;
    private Integer stockActuel;
    private String image;
    private String attributs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Informations du produit (évite le lazy loading)
    private Long produitId;
    private String produitNom;
    private String produitDescription;
    
    // Informations de la boutique (évite le lazy loading)
    private Long boutiqueId;
    private String boutiqueNom;

    public ArticleResponseDTO() {}

    public ArticleResponseDTO(Long id, String sku, BigDecimal prix, Integer stockActuel, 
                           String image, String attributs, LocalDateTime createdAt, 
                           LocalDateTime updatedAt, Long produitId, String produitNom, 
                           String produitDescription, Long boutiqueId, String boutiqueNom) {
        this.id = id;
        this.sku = sku;
        this.prix = prix;
        this.stockActuel = stockActuel;
        this.image = image;
        this.attributs = attributs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.produitId = produitId;
        this.produitNom = produitNom;
        this.produitDescription = produitDescription;
        this.boutiqueId = boutiqueId;
        this.boutiqueNom = boutiqueNom;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public Integer getStockActuel() { return stockActuel; }
    public void setStockActuel(Integer stockActuel) { this.stockActuel = stockActuel; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getAttributs() { return attributs; }
    public void setAttributs(String attributs) { this.attributs = attributs; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getProduitId() { return produitId; }
    public void setProduitId(Long produitId) { this.produitId = produitId; }

    public String getProduitNom() { return produitNom; }
    public void setProduitNom(String produitNom) { this.produitNom = produitNom; }

    public String getProduitDescription() { return produitDescription; }
    public void setProduitDescription(String produitDescription) { this.produitDescription = produitDescription; }

    public Long getBoutiqueId() { return boutiqueId; }
    public void setBoutiqueId(Long boutiqueId) { this.boutiqueId = boutiqueId; }

    public String getBoutiqueNom() { return boutiqueNom; }
    public void setBoutiqueNom(String boutiqueNom) { this.boutiqueNom = boutiqueNom; }

    @Override
    public String toString() {
        return "ArticleResponseDTO{" +
                "id=" + id +
                ", sku='" + sku + '\'' +
                ", prix=" + prix +
                ", stockActuel=" + stockActuel +
                ", image='" + image + '\'' +
                ", attributs='" + attributs + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", produitId=" + produitId +
                ", produitNom='" + produitNom + '\'' +
                ", produitDescription='" + produitDescription + '\'' +
                ", boutiqueId=" + boutiqueId +
                ", boutiqueNom='" + boutiqueNom + '\'' +
                '}';
    }
}
