package com.tissenza.tissenza_backend.modules.produit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @Column(name = "numero_lot", unique = true, length = 50)
    private String numeroLot;

    @Column(name = "quantite_initiale", nullable = false)
    private Integer quantiteInitiale;

    @Column(name = "quantite_restante", nullable = false)
    private Integer quantiteRestante;

    @Column(name = "date_fabrication")
    private LocalDate dateFabrication;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "prix_achat")
    private Double prixAchat;

    @Column(name = "fournisseur", length = 150)
    private String fournisseur;

    @Column(name = "emplacement", length = 100)
    private String emplacement;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, columnDefinition = "VARCHAR(20)")
    private Statut statut = Statut.ACTIF;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Statut {
        ACTIF, PERIME, EPUISE, BLOQUE
    }

    /**
     * Vérifie si le lot est périmé
     */
    public boolean estPerime() {
        return dateExpiration != null && dateExpiration.isBefore(LocalDate.now());
    }

    /**
     * Vérifie si le lot est proche de la péremption (30 jours)
     */
    public boolean estProchePeremption() {
        return dateExpiration != null && 
               dateExpiration.minusDays(30).isBefore(LocalDate.now()) && 
               !estPerime();
    }

    /**
     * Vérifie si le lot est épuisé
     */
    public boolean estEpuise() {
        return quantiteRestante <= 0;
    }

    /**
     * Retire une quantité du lot
     */
    public void retirerQuantite(Integer quantite) {
        if (quantite > quantiteRestante) {
            throw new IllegalArgumentException("Quantité supérieure au stock disponible");
        }
        this.quantiteRestante -= quantite;
        if (this.quantiteRestante == 0) {
            this.statut = Statut.EPUISE;
        }
    }

    /**
     * Ajoute une quantité au lot (retour/ajustement)
     */
    public void ajouterQuantite(Integer quantite) {
        this.quantiteRestante += quantite;
        if (this.quantiteRestante > 0 && this.statut == Statut.EPUISE) {
            this.statut = Statut.ACTIF;
        }
    }
}
