package com.tissenza.tissenza_backend.modules.commande.entity;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Compte client;

    @Column(name = "numero_commande", unique = true, nullable = false, length = 50)
    private String numeroCommande;

    @Column(name = "montant_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, columnDefinition = "VARCHAR(20)")
    private StatutCommande statut = StatutCommande.EN_ATTENTE;

    @Column(name = "adresse_livraison", columnDefinition = "TEXT")
    private String adresseLivraison;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "date_livraison_souhaitee")
    private LocalDateTime dateLivraisonSouhaitee;

    @Column(name = "date_livraison_reelle")
    private LocalDateTime dateLivraisonReelle;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LigneCommande> lignesCommande;

    // Méthodes utilitaires
    public void calculerMontantTotal() {
        if (lignesCommande != null && !lignesCommande.isEmpty()) {
            montantTotal = lignesCommande.stream()
                    .map(LigneCommande::getMontantTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    public boolean estValidee() {
        return statut == StatutCommande.VALIDEE;
    }

    public boolean estEnPreparation() {
        return statut == StatutCommande.EN_PREPARATION;
    }

    public boolean estLivre() {
        return statut == StatutCommande.LIVREE;
    }

    public boolean estAnnulee() {
        return statut == StatutCommande.ANNULEE;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum StatutCommande {
        EN_ATTENTE,      // Commande créée, en attente de validation
        VALIDEE,         // Commande validée par le vendeur
        EN_PREPARATION,  // Commande en cours de préparation
        PRETE,           // Commande prête pour livraison
        EN_LIVRAISON,    // Commande en cours de livraison
        LIVREE,          // Commande livrée
        ANNULEE          // Commande annulée
    }
}
