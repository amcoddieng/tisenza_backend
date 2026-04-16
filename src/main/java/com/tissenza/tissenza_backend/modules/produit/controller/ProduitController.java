package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import com.tissenza.tissenza_backend.modules.produit.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Tag(name = "Produit Management", description = "API pour la gestion des produits")
public class ProduitController {

    private final ProduitService produitService;

    @PostMapping
    @Operation(summary = "Créer un nouveau produit", description = "Crée un nouveau produit dans le système")
    public ResponseEntity<Produit> createProduit(@RequestBody Produit produit) {
        Produit createdProduit = produitService.createProduit(produit);
        return new ResponseEntity<>(createdProduit, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par ID", description = "Retourne les détails d'un produit spécifique")
    public ResponseEntity<Produit> getProduitById(
            @Parameter(description = "ID du produit à récupérer") @PathVariable Long id) {
        return produitService.getProduitById(id)
                .map(produit -> ResponseEntity.ok(produit))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/with-articles")
    @Operation(summary = "Récupérer un produit avec ses articles", description = "Retourne un produit avec ses articles chargés")
    public ResponseEntity<Produit> getProduitByIdWithArticles(
            @Parameter(description = "ID du produit à récupérer") @PathVariable Long id) {
        return produitService.getProduitByIdWithArticles(id)
                .map(produit -> ResponseEntity.ok(produit))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les produits", description = "Retourne la liste de tous les produits")
    public ResponseEntity<List<Produit>> getAllProduits() {
        List<Produit> produits = produitService.getAllProduits();
        return ResponseEntity.ok(produits);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit", description = "Met à jour les informations d'un produit existant")
    public ResponseEntity<Produit> updateProduit(
            @Parameter(description = "ID du produit à mettre à jour") @PathVariable Long id,
            @RequestBody Produit produitDetails) {
        try {
            Produit updatedProduit = produitService.updateProduit(id, produitDetails);
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
    public ResponseEntity<List<Produit>> getProduitsByBoutiqueId(
            @Parameter(description = "ID de la boutique") @PathVariable Long boutiqueId) {
        List<Produit> produits = produitService.getProduitsByBoutiqueId(boutiqueId);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/sous-categorie/{sousCategorieId}")
    @Operation(summary = "Récupérer les produits d'une sous-catégorie", description = "Retourne la liste des produits par ID sous-catégorie")
    public ResponseEntity<List<Produit>> getProduitsBySousCategorieId(
            @Parameter(description = "ID de la sous-catégorie") @PathVariable Long sousCategorieId) {
        List<Produit> produits = produitService.getProduitsBySousCategorieId(sousCategorieId);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer des produits par statut", description = "Retourne la liste des produits par statut")
    public ResponseEntity<List<Produit>> getProduitsByStatut(
            @Parameter(description = "Statut à rechercher") @PathVariable Produit.Statut statut) {
        List<Produit> produits = produitService.getProduitsByStatut(statut);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/nom/{nom}")
    @Operation(summary = "Récupérer un produit par nom", description = "Retourne un produit par son nom")
    public ResponseEntity<Produit> getProduitByNom(
            @Parameter(description = "Nom du produit") @PathVariable String nom) {
        return produitService.getProduitByNom(nom)
                .map(produit -> ResponseEntity.ok(produit))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher par nom", description = "Recherche des produits par nom (insensible à la casse)")
    public ResponseEntity<List<Produit>> searchProduitsByNom(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<Produit> produits = produitService.searchProduitsByNom(nom);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des produits par mot-clé dans nom ou description")
    public ResponseEntity<List<Produit>> searchProduitsByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<Produit> produits = produitService.searchProduitsByKeyword(keyword);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/boutique/{boutiqueId}/statut/{statut}")
    @Operation(summary = "Récupérer des produits par boutique et statut", description = "Retourne les produits par boutique et statut")
    public ResponseEntity<List<Produit>> getProduitsByBoutiqueIdAndStatut(
            @Parameter(description = "ID de la boutique") @PathVariable Long boutiqueId,
            @Parameter(description = "Statut à rechercher") @PathVariable Produit.Statut statut) {
        List<Produit> produits = produitService.getProduitsByBoutiqueIdAndStatut(boutiqueId, statut);
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
    public ResponseEntity<Produit> activateProduit(
            @Parameter(description = "ID du produit à activer") @PathVariable Long id) {
        try {
            Produit activatedProduit = produitService.activateProduit(id);
            return ResponseEntity.ok(activatedProduit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver un produit", description = "Change le statut d'un produit à INACTIF")
    public ResponseEntity<Produit> deactivateProduit(
            @Parameter(description = "ID du produit à désactiver") @PathVariable Long id) {
        try {
            Produit deactivatedProduit = produitService.deactivateProduit(id);
            return ResponseEntity.ok(deactivatedProduit);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
