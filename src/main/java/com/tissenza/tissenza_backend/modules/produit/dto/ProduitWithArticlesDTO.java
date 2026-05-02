package com.tissenza.tissenza_backend.modules.produit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitWithArticlesDTO {
    private Long id;
    private Long boutiqueId;
    private Long sousCategorieId;
    private String nom;
    private String description;
    private String image;
    private com.tissenza.tissenza_backend.modules.produit.entity.Produit.Statut statut;
    private LocalDateTime createdAt;
    private List<ArticleDTO> articles;
}
