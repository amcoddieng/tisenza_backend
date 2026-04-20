package com.tissenza.tissenza_backend.modules.paiement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité MoyenPaiement
 */
public class MoyenPaiementDTO {
    
    private Long id;
    
    @NotBlank(message = "Le nom du moyen de paiement est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;
    
    private String photo;
    
    private LocalDateTime createdAt;

    // Constructeurs
    public MoyenPaiementDTO() {}

    public MoyenPaiementDTO(String nom, String photo) {
        this.nom = nom;
        this.photo = photo;
    }

    public MoyenPaiementDTO(Long id, String nom, String photo, LocalDateTime createdAt) {
        this.id = id;
        this.nom = nom;
        this.photo = photo;
        this.createdAt = createdAt;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "MoyenPaiementDTO{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", photo='" + getPhoto() + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
