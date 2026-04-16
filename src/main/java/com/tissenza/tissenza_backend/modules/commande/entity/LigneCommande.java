package com.tissenza.tissenza_backend.modules.commande.entity;

import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.entity.Lot;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "lignes_commande")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private Lot lot; // Optionnel : pour traçabilité du lot spécifique

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "prix_unitaire", nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(name = "remise", precision = 5, scale = 2)
    private BigDecimal remise = BigDecimal.ZERO;

    @Column(name = "montant_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @Column(name = "prix_achat_unitaire", precision = 10, scale = 2)
    private BigDecimal prixAchatUnitaire; // Pour calculer la marge

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Méthodes utilitaires
    @PrePersist
    @PreUpdate
    public void calculerMontantTotal() {
        BigDecimal sousTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        this.montantTotal = sousTotal.subtract(remise);
    }

    public BigDecimal getMarge() {
        if (prixAchatUnitaire != null) {
            BigDecimal coutTotal = prixAchatUnitaire.multiply(BigDecimal.valueOf(quantite));
            return montantTotal.subtract(coutTotal);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getMargeUnitaire() {
        if (prixAchatUnitaire != null) {
            return prixUnitaire.subtract(prixAchatUnitaire);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTauxMarge() {
        if (prixAchatUnitaire != null && prixAchatUnitaire.compareTo(BigDecimal.ZERO) > 0) {
            return getMargeUnitaire()
                    .divide(prixAchatUnitaire, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }
        return BigDecimal.ZERO;
    }
}
