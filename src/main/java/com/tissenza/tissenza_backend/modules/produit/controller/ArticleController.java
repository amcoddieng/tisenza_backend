package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import com.tissenza.tissenza_backend.modules.produit.service.ArticleService;
import com.tissenza.tissenza_backend.service.CloudinaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Article Management", description = "API pour la gestion des articles")
public class ArticleController {

    private final ArticleService articleService;
    private final CloudinaryService cloudinaryService;

    @PostMapping
    @Operation(summary = "Créer un nouvel article", description = "Crée un nouvel article dans le système")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle = articleService.createArticle(article);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    /**
     * Créer un article avec upload d'image
     */
    @PostMapping("/with-image")
    @Operation(summary = "Créer un article avec image", description = "Crée un nouvel article avec upload d'image sur Cloudinary")
    public ResponseEntity<Article> createArticleWithImage(
            @RequestParam("produitId") Long produitId,
            @RequestParam("sku") String sku,
            @RequestParam("prix") BigDecimal prix,
            @RequestParam(value = "stockActuel", required = false, defaultValue = "0") Integer stockActuel,
            @RequestParam(value = "attributs", required = false) String attributs,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        try {
            // Upload l'image si fournie
            String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(imageFile);
                log.info("Image uploadée: {}", imageUrl);
            }
            
            // Créer l'article avec l'URL de l'image
            Article article = new Article();
            
            // Créer l'objet de relation avec le produit
            Produit produit = new Produit();
            produit.setId(produitId);
            article.setProduit(produit);
            
            article.setSku(sku);
            article.setPrix(prix);
            article.setStockActuel(stockActuel);
            article.setAttributs(attributs);
            article.setImage(imageUrl);
            
            Article createdArticle = articleService.createArticle(article);
            return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload de l'image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'article: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
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

    /**
     * Mettre à jour l'image d'un article
     */
    @PostMapping("/{id}/image")
    @Operation(summary = "Mettre à jour l'image", description = "Upload et met à jour l'image d'un article existant")
    public ResponseEntity<Article> updateImage(
            @Parameter(description = "ID de l'article") @PathVariable Long id,
            @RequestParam("image") MultipartFile imageFile) {
        
        try {
            // Upload l'image sur Cloudinary
            String imageUrl = cloudinaryService.uploadImage(imageFile);
            log.info("Image uploadée: {}", imageUrl);
            
            // Mettre à jour l'article avec la nouvelle URL
            Article articleDetails = new Article();
            articleDetails.setImage(imageUrl);
            
            Article updatedArticle = articleService.updateArticle(id, articleDetails);
            return ResponseEntity.ok(updatedArticle);
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload de l'image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de l'image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
