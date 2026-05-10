package com.tissenza.tissenza_backend.modules.produit.dto;

import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Produit
 * Résout le problème de LazyInitializationException en n'exposant que les champs nécessaires
 */
public class ProduitResponseDTO {
    private Long id;
    private String nom;
    private String description;
    private String image;
    private Produit.Statut statut;
    private LocalDateTime createdAt;
    
    // Informations de la boutique (évite le lazy loading)
    private Long boutiqueId;
    private String boutiqueNom;
    
    // Informations de la sous-catégorie (évite le lazy loading)
    private Long sousCategorieId;
    private String sousCategorieNom;
    private Long categorieId;
    private String categorieNom;

    public ProduitResponseDTO() {}

    public ProduitResponseDTO(Long id, String nom, String description, String image, 
                             Produit.Statut statut, LocalDateTime createdAt, Long boutiqueId, 
                             String boutiqueNom, Long sousCategorieId, String sousCategorieNom,
                             Long categorieId, String categorieNom) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.statut = statut;
        this.createdAt = createdAt;
        this.boutiqueId = boutiqueId;
        this.boutiqueNom = boutiqueNom;
        this.sousCategorieId = sousCategorieId;
        this.sousCategorieNom = sousCategorieNom;
        this.categorieId = categorieId;
        this.categorieNom = categorieNom;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Produit.Statut getStatut() { return statut; }
    public void setStatut(Produit.Statut statut) { this.statut = statut; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getBoutiqueId() { return boutiqueId; }
    public void setBoutiqueId(Long boutiqueId) { this.boutiqueId = boutiqueId; }

    public String getBoutiqueNom() { return boutiqueNom; }
    public void setBoutiqueNom(String boutiqueNom) { this.boutiqueNom = boutiqueNom; }

    public Long getSousCategorieId() { return sousCategorieId; }
    public void setSousCategorieId(Long sousCategorieId) { this.sousCategorieId = sousCategorieId; }

    public String getSousCategorieNom() { return sousCategorieNom; }
    public void setSousCategorieNom(String sousCategorieNom) { this.sousCategorieNom = sousCategorieNom; }

    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }

    public String getCategorieNom() { return categorieNom; }
    public void setCategorieNom(String categorieNom) { this.categorieNom = categorieNom; }

    @Override
    public String toString() {
        return "ProduitResponseDTO{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", statut=" + statut +
                ", createdAt=" + createdAt +
                ", boutiqueId=" + boutiqueId +
                ", boutiqueNom='" + boutiqueNom + '\'' +
                ", sousCategorieId=" + sousCategorieId +
                ", sousCategorieNom='" + sousCategorieNom + '\'' +
                ", categorieId=" + categorieId +
                ", categorieNom='" + categorieNom + '\'' +
                '}';
    }
}
