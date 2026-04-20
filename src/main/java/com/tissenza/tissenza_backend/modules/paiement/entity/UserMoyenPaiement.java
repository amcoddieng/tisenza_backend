package com.tissenza.tissenza_backend.modules.paiement.entity;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité de liaison Many-to-Many entre les utilisateurs et les moyens de paiement
 */
@Entity
@Table(name = "user_moyen_paiement",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "moyen_paiement_id"}))
public class UserMoyenPaiement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "L'utilisateur est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_moyen_paiement_user"))
    private Compte user;

    @NotNull(message = "Le moyen de paiement est obligatoire")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moyen_paiement_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_moyen_paiement_moyen"))
    private MoyenPaiement moyenPaiement;

    @Column(name = "actif", nullable = false)
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructeurs
    public UserMoyenPaiement() {}

    public UserMoyenPaiement(Compte user, MoyenPaiement moyenPaiement) {
        this.user = user;
        this.moyenPaiement = moyenPaiement;
        this.actif = true;
    }

    public UserMoyenPaiement(Compte user, MoyenPaiement moyenPaiement, Boolean actif) {
        this.user = user;
        this.moyenPaiement = moyenPaiement;
        this.actif = actif;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Compte getUser() {
        return user;
    }

    public void setUser(Compte user) {
        this.user = user;
    }

    public MoyenPaiement getMoyenPaiement() {
        return moyenPaiement;
    }

    public void setMoyenPaiement(MoyenPaiement moyenPaiement) {
        this.moyenPaiement = moyenPaiement;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Méthodes utilitaires
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserMoyenPaiement that = (UserMoyenPaiement) o;
        return Objects.equals(id, that.id) && 
               Objects.equals(user, that.user) && 
               Objects.equals(moyenPaiement, that.moyenPaiement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, moyenPaiement);
    }

    @Override
    public String toString() {
        return "UserMoyenPaiement{" +
                "id=" + id +
                ", user=" + (user != null ? user.getId() : null) +
                ", moyenPaiement=" + (moyenPaiement != null ? moyenPaiement.getId() : null) +
                ", actif=" + actif +
                ", createdAt=" + createdAt +
                '}';
    }
}
