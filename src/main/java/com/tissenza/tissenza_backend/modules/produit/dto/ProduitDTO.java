package com.tissenza.tissenza_backend.modules.produit.dto;

import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDTO {
    private Long id;
    private Long boutiqueId;
    private Long sousCategorieId;
    private String nom;
    private String description;
    private String image;
    private Produit.Statut statut;
    private LocalDateTime createdAt;
}
