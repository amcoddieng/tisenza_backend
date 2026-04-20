package com.tissenza.tissenza_backend.modules.paiement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant les moyens de paiement disponibles dans le système
 */
@Entity
@Table(name = "moyen_paiement")
public class MoyenPaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du moyen de paiement est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, length = 100, unique = true)
    private String nom;

    @Column(name = "photo", length = 255)
    private String photo;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relations Many-to-Many avec les utilisateurs
    @OneToMany(mappedBy = "moyenPaiement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserMoyenPaiement> userMoyenPaiements;

    // Constructeurs
    public MoyenPaiement() {}

    public MoyenPaiement(String nom, String photo) {
        this.nom = nom;
        this.photo = photo;
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

    public List<UserMoyenPaiement> getUserMoyenPaiements() {
        return userMoyenPaiements;
    }

    public void setUserMoyenPaiements(List<UserMoyenPaiement> userMoyenPaiements) {
        this.userMoyenPaiements = userMoyenPaiements;
    }

    // Méthodes utilitaires
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoyenPaiement that = (MoyenPaiement) o;
        return Objects.equals(id, that.id) && Objects.equals(nom, that.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom);
    }

    @Override
    public String toString() {
        return "MoyenPaiement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", photo='" + photo + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
