package com.tissenza.tissenza_backend.modules.produit.dto;

import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueStockDTO {
    private Long id;
    private Long articleId;
    private Integer quantite;
    private HistoriqueStock.Type type;
    private String motif;
    private LocalDateTime createdAt;
}
