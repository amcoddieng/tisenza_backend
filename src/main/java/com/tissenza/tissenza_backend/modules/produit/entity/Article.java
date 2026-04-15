package com.tissenza.tissenza_backend.modules.produit.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(name = "sku", unique = true, length = 100)
    private String sku;

    @Column(name = "prix", precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(name = "stock_actuel")
    private Integer stockActuel = 0;

    @Column(name = "attributs", columnDefinition = "JSON")
    private String attributs;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueStock> historiqueStock;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lot> lots;

    /**
     * Calcule le stock total à partir de tous les lots actifs
     */
    public Integer getStockTotal() {
        if (lots == null || lots.isEmpty()) {
            return stockActuel;
        }
        return lots.stream()
                .filter(lot -> lot.getStatut() == Lot.Statut.ACTIF)
                .mapToInt(Lot::getQuantiteRestante)
                .sum();
    }

    /**
     * Vérifie si l'article a des lots proches de la péremption
     */
    public boolean aLotsProchesPeremption() {
        if (lots == null || lots.isEmpty()) {
            return false;
        }
        return lots.stream()
                .anyMatch(Lot::estProchePeremption);
    }

    /**
     * Vérifie si l'article a des lots périmés
     */
    public boolean aLotsPerimes() {
        if (lots == null || lots.isEmpty()) {
            return false;
        }
        return lots.stream()
                .anyMatch(Lot::estPerime);
    }
}
