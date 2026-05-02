package com.tissenza.tissenza_backend.modules.panier.dto;

import com.tissenza.tissenza_backend.modules.panier.entity.Panier.PanierStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierDTO {
    private Long id;
    private Long clientId;
    private LocalDateTime dateCreation;
    private LocalDateTime updatedDate;
    private PanierStatus status;
    private BigDecimal total;
    private List<PanierItemDTO> items;
}
