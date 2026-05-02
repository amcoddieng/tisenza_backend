package com.tissenza.tissenza_backend.modules.commande.dto;

import com.tissenza.tissenza_backend.modules.commande.entity.Commande.CommandeStatus;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande.StatusPaiement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDTO {
    private Long id;
    private Long clientId;
    private Long panierId;
    private LocalDateTime date;
    private CommandeStatus status;
    private BigDecimal total;
    private StatusPaiement statusPaiement;
    private List<DetailCommandeDTO> details;
}
