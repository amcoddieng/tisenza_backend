package com.tissenza.tissenza_backend.modules.produit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {
    private Long id;
    private Long produitId;
    private String sku;
    private BigDecimal prix;
    private Integer stockActuel;
    private String attributs;
    private String image;
}
