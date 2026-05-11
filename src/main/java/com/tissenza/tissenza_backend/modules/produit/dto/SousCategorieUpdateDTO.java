package com.tissenza.tissenza_backend.modules.produit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO pour la mise à jour de sous-catégories
 */
public class SousCategorieUpdateDTO {
    private String nom;
    private String description;
    
    @JsonProperty("categorieId")
    private Long categorieId;

    public SousCategorieUpdateDTO() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }
}
