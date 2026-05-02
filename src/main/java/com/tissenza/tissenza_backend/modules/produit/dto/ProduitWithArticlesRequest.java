package com.tissenza.tissenza_backend.modules.produit.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProduitWithArticlesRequest {
    private Long boutiqueId;
    private Long sousCategorieId;
    private String nom;
    private String description;
    private String statut;
    // Note: L'image du produit sera gérée avec MultipartFile dans le contrôleur
    private List<ArticleRequest> articles; // Liste des articles à créer
}
