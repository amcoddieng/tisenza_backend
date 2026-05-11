package com.tissenza.tissenza_backend.modules.produit.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @JdbcTypeCode(SqlTypes.JSON)
    private String attributs;
    
    // Getter et Setter pour gérer la conversion JSON
    public String getAttributs() {
        return attributs;
    }
    
    public void setAttributs(String attributs) {
        this.attributs = attributs;
    }
    
    // Méthode utilitaire pour définir les attributs depuis un objet
    public void setAttributsFromObject(java.util.Map<String, Object> attributsMap) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            this.attributs = mapper.writeValueAsString(attributsMap);
        } catch (Exception e) {
            this.attributs = "{}";
        }
    }

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoriqueStock> historiqueStock;
}
