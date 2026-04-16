package com.tissenza.tissenza_backend.modules.boutique.entity;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "boutiques")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Boutique {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendeur_id", nullable = false)
    private Compte vendeur;

    @Column(name = "nom", length = 150)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "note")
    private Float note;

    @Column(name = "addresse", columnDefinition = "TEXT")
    private String addresse;

    @Column(name = "logo", columnDefinition = "TEXT")
    private String logo;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, columnDefinition = "VARCHAR(20)")
    private Statut statut = Statut.EN_ATTENTE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum Statut {
        EN_ATTENTE, VALIDE, REFUSE
    }
}
