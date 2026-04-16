package com.tissenza.tissenza_backend.modules.boutique.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoutiqueCreationDTO {
    private Long vendeurId;
    private String nom;
    private String description;
    private String addresse;
    private String logo;
}
