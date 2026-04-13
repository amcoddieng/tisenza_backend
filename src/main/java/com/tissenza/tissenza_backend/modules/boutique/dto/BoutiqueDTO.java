package com.tissenza.tissenza_backend.modules.boutique.dto;

import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoutiqueDTO {
    private Long id;
    private Long vendeurId;
    private String nom;
    private String description;
    private Float note;
    private String addresse;
    private String logo;
    private Boutique.Statut statut;
    private LocalDateTime createdAt;
}
