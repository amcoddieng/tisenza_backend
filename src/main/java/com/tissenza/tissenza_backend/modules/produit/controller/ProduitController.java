package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.dto.ProduitDTO;
import com.tissenza.tissenza_backend.modules.produit.dto.ProduitWithArticlesDTO;
import com.tissenza.tissenza_backend.modules.produit.dto.ProduitWithArticlesRequest;
import com.tissenza.tissenza_backend.modules.produit.dto.ArticleRequest;
import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import com.tissenza.tissenza_backend.modules.produit.service.ProduitService;
import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import com.tissenza.tissenza_backend.service.LocalStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Produit Management", description = "API pour la gestion des produits")
public class ProduitController {

    private final ProduitService produitService;
    private final LocalStorageService localStorageService;

    @PostMapping
    @Operation(summary = "Créer un nouveau produit", description = "Crée un nouveau produit dans le système")
    public ResponseEntity<ProduitDTO> createProduit(@RequestBody Produit produit) {
        ProduitDTO createdProduit = produitService.createProduitDTO(produit);
        return new ResponseEntity<>(createdProduit, HttpStatus.CREATED);
    }

    /**
     * Créer un produit avec upload d'image
     */
    @PostMapping("/with-image")
    @Operation(summary = "Créer un produit avec image", description = "Crée un nouveau produit avec stockage d'image en local")
    public ResponseEntity<ProduitDTO> createProduitWithImage(
            @RequestParam("boutiqueId") Long boutiqueId,
            @RequestParam("sousCategorieId") Long sousCategorieId,
            @RequestParam("nom") String nom,
            @RequestParam("description") String description,
            @RequestParam("statut") String statut,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        
        try {
            // Stocker l'image en local si fournie
            String imageUrl = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imageUrl = localStorageService.storeFile(imageFile, LocalStorageService.FileType.PRODUIT);
                log.info("Image stockée en local: {}", imageUrl);
            }
            
            // Créer le produit avec l'URL de l'image
            Produit produit = new Produit();
            
            // Créer les objets de relation avec les IDs
            Boutique boutique = new Boutique();
            boutique.setId(boutiqueId);
            produit.setBoutique(boutique);
            
            SousCategorie sousCategorie = new SousCategorie();
            sousCategorie.setId(sousCategorieId);
            produit.setSousCategorie(sousCategorie);
            
            produit.setNom(nom);
            produit.setDescription(description);
            produit.setStatut(Produit.Statut.valueOf(statut));
            produit.setImage(imageUrl);
            
            ProduitDTO createdProduit = produitService.createProduitDTO(produit);
            return new ResponseEntity<>(createdProduit, HttpStatus.CREATED);
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload de l'image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Erreur lors de la création du produit: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par ID", description = "Retourne les détails d'un produit spécifique")
    public ResponseEntity<ProduitDTO> getProduitById(
            @Parameter(description = "ID du produit à récupérer") @PathVariable Long id) {
        return produitService.getProduitByIdDTO(id)
                .map(produit -> ResponseEntity.ok(produit))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-articles")
    @Operation(summary = "Récupérer un produit avec ses articles", description = "Retourne un produit avec ses articles chargés")
    public ResponseEntity<ProduitDTO> getProduitByIdWithArticles(
            @Parameter(description = "ID du produit à récupérer") @PathVariable Long id) {
        return produitService.getProduitByIdWithArticlesDTO(id)
                .map(produit -> ResponseEntity.ok(produit))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les produits", description = "Retourne la liste de tous les produits")
    public ResponseEntity<List<ProduitDTO>> getAllProduits() {
        List<ProduitDTO> produits = produitService.getAllProduitsDTO();
        return ResponseEntity.ok(produits);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit", description = "Met à jour les informations d'un produit existant")
    public ResponseEntity<ProduitDTO> updateProduit(
            @Parameter(description = "ID du produit à mettre à jour") @PathVariable Long id,
            @RequestBody Produit produitDetails) {
        try {
            ProduitDTO updatedProduit = produitService.updateProduitDTO(id, produitDetails);
            return ResponseEntity.ok(updatedProduit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit", description = "Supprime un produit du système")
    public ResponseEntity<Void> deleteProduit(
            @Parameter(description = "ID du produit à supprimer") @PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/boutique/{boutiqueId}")
    @Operation(summary = "Récupérer les produits d'une boutique", description = "Retourne la liste des produits par ID boutique")
    public ResponseEntity<List<ProduitDTO>> getProduitsByBoutiqueId(
            @Parameter(description = "ID de la boutique") @PathVariable Long boutiqueId) {
        List<ProduitDTO> produits = produitService.getProduitsByBoutiqueIdDTO(boutiqueId);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/sous-categorie/{sousCategorieId}")
    @Operation(summary = "Récupérer les produits d'une sous-catégorie", description = "Retourne la liste des produits par ID sous-catégorie en DTO")
    public ResponseEntity<List<ProduitDTO>> getProduitsBySousCategorieId(
            @Parameter(description = "ID de la sous-catégorie") @PathVariable Long sousCategorieId) {
        List<ProduitDTO> produits = produitService.getProduitsBySousCategorieIdDTO(sousCategorieId);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer des produits par statut", description = "Retourne la liste des produits par statut en DTO")
    public ResponseEntity<List<ProduitDTO>> getProduitsByStatut(
            @Parameter(description = "Statut à rechercher") @PathVariable Produit.Statut statut) {
        List<ProduitDTO> produits = produitService.getProduitsByStatutDTO(statut);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/nom/{nom}")
    @Operation(summary = "Récupérer un produit par nom", description = "Retourne un produit par son nom en DTO")
    public ResponseEntity<ProduitDTO> getProduitByNom(
            @Parameter(description = "Nom du produit") @PathVariable String nom) {
        return produitService.getProduitByNomDTO(nom)
                .map(produit -> ResponseEntity.ok(produit))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher par nom", description = "Recherche des produits par nom (insensible à la casse) en DTO")
    public ResponseEntity<List<ProduitDTO>> searchProduitsByNom(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<ProduitDTO> produits = produitService.searchProduitsByNomDTO(nom);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des produits par mot-clé dans nom ou description en DTO")
    public ResponseEntity<List<ProduitDTO>> searchProduitsByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<ProduitDTO> produits = produitService.searchProduitsByKeywordDTO(keyword);
        return ResponseEntity.ok(produits);
    }

    // ========== NOUVELLES APIS DE RECHERCHE COMBINÉE ==========

    @GetMapping("/search/combined")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Recherche combinée", description = "Recherche des produits par boutique, sous-catégorie et nom (paramètres optionnels)")
    public ResponseEntity<List<ProduitDTO>> searchProduitsByMultipleCriteria(
            @Parameter(description = "ID de la boutique (optionnel)") @RequestParam(required = false) Long boutiqueId,
            @Parameter(description = "ID de la sous-catégorie (optionnel)") @RequestParam(required = false) Long sousCategorieId,
            @Parameter(description = "Nom à rechercher (optionnel)") @RequestParam(required = false) String nom) {
        List<ProduitDTO> produits = produitService.searchProduitsByMultipleCriteriaDTO(boutiqueId, sousCategorieId, nom);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/search/boutique-souscategorie-nom")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Recherche boutique + sous-catégorie + nom", description = "Recherche des produits par boutique, sous-catégorie et nom (LIKE) - tous obligatoires")
    public ResponseEntity<List<ProduitDTO>> searchProduitsByBoutiqueSousCategorieAndNom(
            @Parameter(description = "ID de la boutique") @RequestParam Long boutiqueId,
            @Parameter(description = "ID de la sous-catégorie") @RequestParam Long sousCategorieId,
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<ProduitDTO> produits = produitService.searchProduitsByBoutiqueSousCategorieAndNomDTO(boutiqueId, sousCategorieId, nom);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/boutique/{boutiqueId}/statut/{statut}")
    @Operation(summary = "Récupérer des produits par boutique et statut", description = "Retourne les produits par boutique et statut")
    public ResponseEntity<List<ProduitDTO>> getProduitsByBoutiqueIdAndStatut(
            @Parameter(description = "ID de la boutique") @PathVariable Long boutiqueId,
            @Parameter(description = "Statut à rechercher") @PathVariable Produit.Statut statut) {
        List<ProduitDTO> produits = produitService.getProduitsByBoutiqueIdAndStatutDTO(boutiqueId, statut);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/count/boutique/{boutiqueId}")
    @Operation(summary = "Compter par boutique", description = "Compte le nombre de produits par boutique")
    public ResponseEntity<Long> countByBoutiqueId(
            @Parameter(description = "ID de la boutique") @PathVariable Long boutiqueId) {
        long count = produitService.countByBoutiqueId(boutiqueId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/sous-categorie/{sousCategorieId}")
    @Operation(summary = "Compter par sous-catégorie", description = "Compte le nombre de produits par sous-catégorie")
    public ResponseEntity<Long> countBySousCategorieId(
            @Parameter(description = "ID de la sous-catégorie") @PathVariable Long sousCategorieId) {
        long count = produitService.countBySousCategorieId(sousCategorieId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/statut/{statut}")
    @Operation(summary = "Compter par statut", description = "Compte le nombre de produits par statut")
    public ResponseEntity<Long> countByStatut(
            @Parameter(description = "Statut pour le comptage") @PathVariable Produit.Statut statut) {
        long count = produitService.countByStatut(statut);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/statut")
    @Operation(summary = "Statistiques par statut", description = "Retourne les statistiques des produits par statut")
    public ResponseEntity<List<Object[]>> getStatisticsByStatut() {
        List<Object[]> stats = produitService.getStatisticsByStatut();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Vérifier l'existence", description = "Vérifie si un produit existe par ID")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID du produit à vérifier") @PathVariable Long id) {
        boolean exists = produitService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/nom/{nom}")
    @Operation(summary = "Vérifier l'existence par nom", description = "Vérifie si un produit existe par nom")
    public ResponseEntity<Boolean> existsByNom(
            @Parameter(description = "Nom du produit à vérifier") @PathVariable String nom) {
        boolean exists = produitService.existsByNom(nom);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activer un produit", description = "Change le statut d'un produit à ACTIF")
    public ResponseEntity<ProduitDTO> activateProduit(
            @Parameter(description = "ID du produit à activer") @PathVariable Long id) {
        try {
            ProduitDTO activatedProduit = produitService.activateProduitDTO(id);
            return ResponseEntity.ok(activatedProduit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver un produit", description = "Change le statut d'un produit à INACTIF")
    public ResponseEntity<ProduitDTO> deactivateProduit(
            @Parameter(description = "ID du produit à désactiver") @PathVariable Long id) {
        try {
            ProduitDTO deactivatedProduit = produitService.deactivateProduitDTO(id);
            return ResponseEntity.ok(deactivatedProduit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Créer un produit avec plusieurs articles en une seule transaction
     */
    @PostMapping("/with-articles")
    @Operation(summary = "Créer un produit avec plusieurs articles", description = "Crée un nouveau produit et plusieurs articles en une seule transaction")
    public ResponseEntity<ProduitWithArticlesDTO> createProduitWithArticles(
            @RequestParam("boutiqueId") Long boutiqueId,
            @RequestParam("sousCategorieId") Long sousCategorieId,
            @RequestParam("nom") String nom,
            @RequestParam("description") String description,
            @RequestParam("statut") String statut,
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            @RequestParam("articles") String articlesJson) {
        
        try {
            log.info("Requête de création de produit avec image et articles");
            
            // Parser le JSON des articles
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.List<ArticleRequest> articles = mapper.readValue(articlesJson, 
                mapper.getTypeFactory().constructCollectionType(java.util.List.class, ArticleRequest.class));
            
            // Créer la requête
            ProduitWithArticlesRequest request = new ProduitWithArticlesRequest();
            request.setBoutiqueId(boutiqueId);
            request.setSousCategorieId(sousCategorieId);
            request.setNom(nom);
            request.setDescription(description);
            request.setStatut(statut);
            request.setArticles(articles);
            
            ProduitWithArticlesDTO createdProduit = produitService.createProduitWithArticles(request, imageFile);
            return new ResponseEntity<>(createdProduit, HttpStatus.CREATED);
            
        } catch (Exception e) {
            log.error("Erreur lors de la création du produit avec articles: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
