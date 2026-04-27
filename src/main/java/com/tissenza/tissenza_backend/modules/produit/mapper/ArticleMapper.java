package com.tissenza.tissenza_backend.modules.produit.mapper;

import com.tissenza.tissenza_backend.modules.produit.dto.ArticleDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    /**
     * Convertit une entité Article en ArticleDTO
     */
    public ArticleDTO toDTO(Article article) {
        if (article == null) {
            return null;
        }

        ArticleDTO dto = new ArticleDTO();
        dto.setId(article.getId());
        dto.setProduitId(article.getProduit() != null ? article.getProduit().getId() : null);
        dto.setSku(article.getSku());
        dto.setPrix(article.getPrix());
        dto.setStockActuel(article.getStockActuel());
        dto.setAttributs(article.getAttributs());
        dto.setImage(article.getImage());

        return dto;
    }

    /**
     * Convertit un ArticleDTO en entité Article
     */
    public Article toEntity(ArticleDTO dto) {
        if (dto == null) {
            return null;
        }

        Article article = new Article();
        article.setId(dto.getId());
        article.setSku(dto.getSku());
        article.setPrix(dto.getPrix());
        article.setStockActuel(dto.getStockActuel());
        article.setAttributs(dto.getAttributs());
        article.setImage(dto.getImage());

        // Note: La relation produit doit être gérée séparément
        // car elle nécessite une récupération depuis la base de données

        return article;
    }

    /**
     * Convertit une liste d'entités Article en liste de ArticleDTO
     */
    public List<ArticleDTO> toDTOList(List<Article> articles) {
        if (articles == null) {
            return null;
        }

        return articles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de ArticleDTO en liste d'entités Article
     */
    public List<Article> toEntityList(List<ArticleDTO> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour une entité Article à partir d'un ArticleDTO
     */
    public void updateEntityFromDTO(ArticleDTO dto, Article article) {
        if (dto == null || article == null) {
            return;
        }

        article.setSku(dto.getSku());
        article.setPrix(dto.getPrix());
        article.setStockActuel(dto.getStockActuel());
        article.setAttributs(dto.getAttributs());
        article.setImage(dto.getImage());

        // Note: La relation produit doit être gérée séparément
    }
}
