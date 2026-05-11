package com.tissenza.tissenza_backend.modules.produit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO pour la création de sous-catégories
 */
public class SousCategorieCreateDTO {
    private String nom;
    private String description;
    
    @JsonProperty("categorieId")
    private Long categorieId;

    public SousCategorieCreateDTO() {}

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCategorieId() { return categorieId; }
    public void setCategorieId(Long categorieId) { this.categorieId = categorieId; }
}
