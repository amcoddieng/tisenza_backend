package com.tissenza.tissenza_backend.modules.commande.entity;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @Enumerated(EnumType.STRING)
    @Column(name = "ancien_statut", columnDefinition = "VARCHAR(20)")
    private Commande.StatutCommande ancienStatut;

    @Enumerated(EnumType.STRING)
    @Column(name = "nouveau_statut", nullable = false, columnDefinition = "VARCHAR(20)")
    private Commande.StatutCommande nouveauStatut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifie_par")
    private Compte modifiePar; // Qui a effectué le changement

    @Column(name = "motif", length = 255)
    private String motif; // Raison du changement de statut

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Constructeur pratique pour les changements de statut
    public HistoriqueCommande(Commande commande, 
                             Commande.StatutCommande ancienStatut,
                             Commande.StatutCommande nouveauStatut,
                             Compte modifiePar,
                             String motif) {
        this.commande = commande;
        this.ancienStatut = ancienStatut;
        this.nouveauStatut = nouveauStatut;
        this.modifiePar = modifiePar;
        this.motif = motif;
    }
}
