package com.tissenza.tissenza_backend.modules.produit.dto;

import java.math.BigDecimal;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO pour la création d'articles avec gestion des attributs JSON
 */
public class ArticleCreateDTO {
    @JsonProperty("produit_id")
    private Long produitId;
    private String sku;
    private BigDecimal prix;
    @JsonProperty("stock_actuel")
    private Integer stockActuel;
    
    private String image;
    private Map<String, Object> attributs;

    public ArticleCreateDTO() {}

    public Long getProduitId() { return produitId; }
    public void setProduitId(Long produitId) { this.produitId = produitId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public Integer getStockActuel() { return stockActuel; }
    public void setStockActuel(Integer stockActuel) { this.stockActuel = stockActuel; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Map<String, Object> getAttributs() { return attributs; }
    public void setAttributs(Map<String, Object> attributs) { this.attributs = attributs; }
}
