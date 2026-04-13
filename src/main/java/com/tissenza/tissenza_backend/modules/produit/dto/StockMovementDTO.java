package com.tissenza.tissenza_backend.modules.produit.dto;

import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementDTO {
    private Long articleId;
    private Integer quantite;
    private HistoriqueStock.Type type;
    private String motif;
}
