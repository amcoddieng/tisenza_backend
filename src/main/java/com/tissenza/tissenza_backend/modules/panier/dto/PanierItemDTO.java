package com.tissenza.tissenza_backend.modules.panier.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierItemDTO {
    private Long id;
    private Long panierId;
    private Long articleId;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;
    private LocalDateTime date;
    
    // Informations sur l'article pour affichage
    private String articleSku;
    private String articleNom;
    private String articleImage;
}
