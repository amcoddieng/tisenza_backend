package com.tissenza.tissenza_backend.modules.produit.mapper;

import com.tissenza.tissenza_backend.modules.produit.dto.ArticleResponseDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Article et ArticleResponseDTO
 * Évite le LazyInitializationException en extrayant uniquement les champs nécessaires
 */
@Component
public class ArticleResponseMapper {

    /**
     * Convertit une entité Article en ArticleResponseDTO
     * @param article l'entité à convertir
     * @return le DTO correspondant
     */
    public ArticleResponseDTO toDTO(Article article) {
        if (article == null) {
            return null;
        }

        ArticleResponseDTO dto = new ArticleResponseDTO();
        dto.setId(article.getId());
        dto.setSku(article.getSku());
        dto.setPrix(article.getPrix());
        dto.setStockActuel(article.getStockActuel());
        dto.setImage(article.getImage());
        dto.setAttributs(article.getAttributs());
        // L'entité Article n'a pas de champs createdAt/updatedAt
        // Ces champs sont gérés au niveau du Produit parent

        // Extraction sécurisée des informations du produit
        if (article.getProduit() != null) {
            dto.setProduitId(article.getProduit().getId());
            dto.setProduitNom(article.getProduit().getNom());
            dto.setProduitDescription(article.getProduit().getDescription());
            
            // Extraction des informations de la boutique via le produit
            if (article.getProduit().getBoutique() != null) {
                dto.setBoutiqueId(article.getProduit().getBoutique().getId());
                dto.setBoutiqueNom(article.getProduit().getBoutique().getNom());
            } else {
                dto.setBoutiqueId(null);
                dto.setBoutiqueNom("Boutique non disponible");
            }
        } else {
            dto.setProduitId(null);
            dto.setProduitNom("Produit non disponible");
            dto.setProduitDescription("Description non disponible");
            dto.setBoutiqueId(null);
            dto.setBoutiqueNom("Boutique non disponible");
        }

        return dto;
    }

    /**
     * Convertit un ArticleResponseDTO en entité Article
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Article toEntity(ArticleResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        Article article = new Article();
        article.setId(dto.getId());
        article.setSku(dto.getSku());
        article.setPrix(dto.getPrix());
        article.setStockActuel(dto.getStockActuel());
        article.setImage(dto.getImage());
        article.setAttributs(dto.getAttributs());
        
        // Note: Le produit doit être défini séparément via le service
        // pour éviter les problèmes de persistance
        
        return article;
    }

    /**
     * Convertit une liste d'articles en liste de DTOs
     * @param articles la liste d'entités
     * @return la liste de DTOs
     */
    public java.util.List<ArticleResponseDTO> toDTOList(java.util.List<Article> articles) {
        if (articles == null) {
            return new java.util.ArrayList<>();
        }

        return articles.stream()
                .map(this::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
