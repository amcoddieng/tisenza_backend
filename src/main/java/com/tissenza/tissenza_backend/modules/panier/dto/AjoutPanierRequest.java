package com.tissenza.tissenza_backend.modules.panier.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AjoutPanierRequest {
    private Long articleId;
    private Integer quantite;
    private BigDecimal prixUnitaire;
}
