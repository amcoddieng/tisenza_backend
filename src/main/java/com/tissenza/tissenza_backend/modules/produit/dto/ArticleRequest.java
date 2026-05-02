package com.tissenza.tissenza_backend.modules.produit.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class ArticleRequest {
    private String sku;
    private BigDecimal prix;
    private Integer stock;
    private Map<String, Object> attributs;
    // Note: Les images des articles seront gérées séparément si nécessaire
}
