package com.tissenza.tissenza_backend.modules.produit.mapper;

import com.tissenza.tissenza_backend.modules.produit.dto.ProduitDTO;
import com.tissenza.tissenza_backend.modules.produit.dto.ProduitWithArticlesDTO;
import com.tissenza.tissenza_backend.modules.produit.dto.ArticleDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProduitMapper {

    /**
     * Convertit une entité Produit en ProduitDTO
     * Initialise les relations lazy avant la conversion
     */
    public ProduitDTO toDTO(Produit produit) {
        if (produit == null) {
            return null;
        }

        ProduitDTO dto = new ProduitDTO();
        dto.setId(produit.getId());
        dto.setNom(produit.getNom());
        dto.setDescription(produit.getDescription());
        dto.setImage(produit.getImage());
        dto.setStatut(produit.getStatut());
        dto.setCreatedAt(produit.getCreatedAt());

        // Retourner seulement les IDs pour éviter les boucles infinies et les lazy loading
        if (produit.getBoutique() != null) {
            dto.setBoutiqueId(produit.getBoutique().getId());
        }

        if (produit.getSousCategorie() != null) {
            dto.setSousCategorieId(produit.getSousCategorie().getId());
        }

        return dto;
    }

    /**
     * Convertit une liste de produits en liste de DTOs
     */
    public List<ProduitDTO> toDTOList(List<Produit> produits) {
        if (produits == null) {
            return null;
        }

        return produits.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit un ProduitDTO en entité Produit
     * Utilisé pour la création et la mise à jour
     */
    public Produit toEntity(ProduitDTO dto) {
        if (dto == null) {
            return null;
        }

        Produit produit = new Produit();
        produit.setId(dto.getId());
        produit.setNom(dto.getNom());
        produit.setDescription(dto.getDescription());
        produit.setImage(dto.getImage());
        produit.setStatut(dto.getStatut());
        produit.setCreatedAt(dto.getCreatedAt());

        // Note: Les relations avec boutique et sous-categorie doivent être gérées séparément
        // car elles nécessitent des requêtes à la base de données

        return produit;
    }

    /**
     * Convertit une liste de DTOs en liste d'entités
     */
    public List<Produit> toEntityList(List<ProduitDTO> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité Produit en ProduitWithArticlesDTO avec ses articles
     */
    public ProduitWithArticlesDTO toDTOWithArticles(Produit produit) {
        if (produit == null) {
            return null;
        }

        ProduitWithArticlesDTO dto = new ProduitWithArticlesDTO();
        dto.setId(produit.getId());
        dto.setNom(produit.getNom());
        dto.setDescription(produit.getDescription());
        dto.setImage(produit.getImage());
        dto.setStatut(produit.getStatut());
        dto.setCreatedAt(produit.getCreatedAt());

        // Mapper les relations
        if (produit.getBoutique() != null) {
            dto.setBoutiqueId(produit.getBoutique().getId());
        }
        if (produit.getSousCategorie() != null) {
            dto.setSousCategorieId(produit.getSousCategorie().getId());
        }

        // Mapper les articles
        if (produit.getArticles() != null) {
            List<ArticleDTO> articleDTOs = produit.getArticles().stream()
                    .map(this::mapArticleToDTO)
                    .collect(Collectors.toList());
            dto.setArticles(articleDTOs);
        }

        return dto;
    }

    /**
     * Convertit une entité Article en ArticleDTO
     */
    private ArticleDTO mapArticleToDTO(Article article) {
        if (article == null) {
            return null;
        }

        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setSku(article.getSku());
        dto.setPrix(article.getPrix());
        dto.setStockActuel(article.getStockActuel());
        dto.setAttributs(article.getAttributs());
        dto.setImage(article.getImage());

        if (article.getProduit() != null) {
            dto.setProduitId(article.getProduit().getId());
        }

        return dto;
    }
}
