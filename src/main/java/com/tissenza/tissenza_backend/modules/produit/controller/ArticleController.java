package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Tag(name = "Article Management", description = "API pour la gestion des articles")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    @Operation(summary = "Créer un nouvel article", description = "Crée un nouvel article dans le système")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle = articleService.createArticle(article);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un article par ID", description = "Retourne les détails d'un article spécifique")
    public ResponseEntity<Article> getArticleById(
            @Parameter(description = "ID de l'article à récupérer") @PathVariable Long id) {
        return articleService.getArticleById(id)
                .map(article -> ResponseEntity.ok(article))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les articles", description = "Retourne la liste de tous les articles")
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un article", description = "Met à jour les informations d'un article existant")
    public ResponseEntity<Article> updateArticle(
            @Parameter(description = "ID de l'article à mettre à jour") @PathVariable Long id,
            @RequestBody Article articleDetails) {
        try {
            Article updatedArticle = articleService.updateArticle(id, articleDetails);
            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un article", description = "Supprime un article du système")
    public ResponseEntity<Void> deleteArticle(
            @Parameter(description = "ID de l'article à supprimer") @PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/produit/{produitId}")
    @Operation(summary = "Récupérer les articles d'un produit", description = "Retourne la liste des articles par ID produit")
    public ResponseEntity<List<Article>> getArticlesByProduitId(
            @Parameter(description = "ID du produit") @PathVariable Long produitId) {
        List<Article> articles = articleService.getArticlesByProduitId(produitId);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/sku/{sku}")
    @Operation(summary = "Récupérer un article par SKU", description = "Retourne un article par son SKU")
    public ResponseEntity<Article> getArticleBySku(
            @Parameter(description = "SKU de l'article") @PathVariable String sku) {
        return articleService.getArticleBySku(sku)
                .map(article -> ResponseEntity.ok(article))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/stock/greater/{stock}")
    @Operation(summary = "Récupérer les articles avec stock supérieur", description = "Retourne les articles avec un stock supérieur à la valeur donnée")
    public ResponseEntity<List<Article>> getArticlesWithStockGreaterThan(
            @Parameter(description = "Stock minimum") @PathVariable Integer stock) {
        List<Article> articles = articleService.getArticlesWithStockGreaterThan(stock);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/stock/less/{stock}")
    @Operation(summary = "Récupérer les articles avec stock inférieur", description = "Retourne les articles avec un stock inférieur à la valeur donnée")
    public ResponseEntity<List<Article>> getArticlesWithStockLessThan(
            @Parameter(description = "Stock maximum") @PathVariable Integer stock) {
        List<Article> articles = articleService.getArticlesWithStockLessThan(stock);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Récupérer les articles en rupture de stock", description = "Retourne la liste des articles avec stock à 0")
    public ResponseEntity<List<Article>> getOutOfStockArticles() {
        List<Article> articles = articleService.getOutOfStockArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Récupérer les articles par plage de prix", description = "Retourne les articles dans une plage de prix")
    public ResponseEntity<List<Article>> getArticlesByPriceRange(
            @Parameter(description = "Prix minimum") @RequestParam BigDecimal min,
            @Parameter(description = "Prix maximum") @RequestParam BigDecimal max) {
        List<Article> articles = articleService.getArticlesByPriceRange(min, max);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des articles par mot-clé dans SKU ou attributs")
    public ResponseEntity<List<Article>> searchArticlesByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<Article> articles = articleService.searchArticlesByKeyword(keyword);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/count/produit/{produitId}")
    @Operation(summary = "Compter par produit", description = "Compte le nombre d'articles par produit")
    public ResponseEntity<Long> countByProduitId(
            @Parameter(description = "ID du produit") @PathVariable Long produitId) {
        long count = articleService.countByProduitId(produitId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/out-of-stock")
    @Operation(summary = "Compter les articles en rupture", description = "Compte le nombre d'articles en rupture de stock")
    public ResponseEntity<Long> countOutOfStock() {
        long count = articleService.countOutOfStock();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/in-stock")
    @Operation(summary = "Compter les articles en stock", description = "Compte le nombre d'articles en stock")
    public ResponseEntity<Long> countInStock() {
        long count = articleService.countInStock();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/price/min/{produitId}")
    @Operation(summary = "Prix minimum par produit", description = "Retourne le prix minimum des articles d'un produit")
    public ResponseEntity<BigDecimal> getMinPriceByProduit(
            @Parameter(description = "ID du produit") @PathVariable Long produitId) {
        BigDecimal minPrice = articleService.getMinPriceByProduit(produitId);
        return ResponseEntity.ok(minPrice);
    }

    @GetMapping("/price/max/{produitId}")
    @Operation(summary = "Prix maximum par produit", description = "Retourne le prix maximum des articles d'un produit")
    public ResponseEntity<BigDecimal> getMaxPriceByProduit(
            @Parameter(description = "ID du produit") @PathVariable Long produitId) {
        BigDecimal maxPrice = articleService.getMaxPriceByProduit(produitId);
        return ResponseEntity.ok(maxPrice);
    }

    @GetMapping("/stock/total/{produitId}")
    @Operation(summary = "Stock total par produit", description = "Retourne le stock total des articles d'un produit")
    public ResponseEntity<Integer> getTotalStockByProduit(
            @Parameter(description = "ID du produit") @PathVariable Long produitId) {
        Integer totalStock = articleService.getTotalStockByProduit(produitId);
        return ResponseEntity.ok(totalStock);
    }

    @GetMapping("/exists/sku/{sku}")
    @Operation(summary = "Vérifier l'existence par SKU", description = "Vérifie si un article existe par SKU")
    public ResponseEntity<Boolean> existsBySku(
            @Parameter(description = "SKU de l'article à vérifier") @PathVariable String sku) {
        boolean exists = articleService.existsBySku(sku);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Vérifier l'existence", description = "Vérifie si un article existe par ID")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de l'article à vérifier") @PathVariable Long id) {
        boolean exists = articleService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/{id}/stock")
    @Operation(summary = "Mettre à jour le stock", description = "Met à jour le stock d'un article")
    public ResponseEntity<Article> updateStock(
            @Parameter(description = "ID de l'article") @PathVariable Long id,
            @Parameter(description = "Nouveau stock") @RequestParam Integer newStock) {
        try {
            Article updatedArticle = articleService.updateStock(id, newStock);
            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/stock/add")
    @Operation(summary = "Ajouter du stock", description = "Ajoute une quantité au stock d'un article")
    public ResponseEntity<Article> addStock(
            @Parameter(description = "ID de l'article") @PathVariable Long id,
            @Parameter(description = "Quantité à ajouter") @RequestParam Integer quantity,
            @Parameter(description = "Motif du mouvement") @RequestParam(required = false) String motif) {
        try {
            Article updatedArticle = articleService.addStock(id, quantity, motif);
            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/stock/remove")
    @Operation(summary = "Retirer du stock", description = "Retire une quantité du stock d'un article")
    public ResponseEntity<Article> removeStock(
            @Parameter(description = "ID de l'article") @PathVariable Long id,
            @Parameter(description = "Quantité à retirer") @RequestParam Integer quantity,
            @Parameter(description = "Motif du mouvement") @RequestParam(required = false) String motif) {
        try {
            Article updatedArticle = articleService.removeStock(id, quantity, motif);
            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
