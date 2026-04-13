package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import com.tissenza.tissenza_backend.modules.produit.service.SousCategorieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-categories")
@RequiredArgsConstructor
@Tag(name = "SousCategorie Management", description = "API pour la gestion des sous-catégories")
public class SousCategorieController {

    private final SousCategorieService sousCategorieService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle sous-catégorie", description = "Crée une nouvelle sous-catégorie dans le système")
    public ResponseEntity<SousCategorie> createSousCategorie(@RequestBody SousCategorie sousCategorie) {
        SousCategorie createdSousCategorie = sousCategorieService.createSousCategorie(sousCategorie);
        return new ResponseEntity<>(createdSousCategorie, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une sous-catégorie par ID", description = "Retourne les détails d'une sous-catégorie spécifique")
    public ResponseEntity<SousCategorie> getSousCategorieById(
            @Parameter(description = "ID de la sous-catégorie à récupérer") @PathVariable Long id) {
        return sousCategorieService.getSousCategorieById(id)
                .map(sousCategorie -> ResponseEntity.ok(sousCategorie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les sous-catégories", description = "Retourne la liste de toutes les sous-catégories")
    public ResponseEntity<List<SousCategorie>> getAllSousCategories() {
        List<SousCategorie> sousCategories = sousCategorieService.getAllSousCategories();
        return ResponseEntity.ok(sousCategories);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une sous-catégorie", description = "Met à jour les informations d'une sous-catégorie existante")
    public ResponseEntity<SousCategorie> updateSousCategorie(
            @Parameter(description = "ID de la sous-catégorie à mettre à jour") @PathVariable Long id,
            @RequestBody SousCategorie sousCategorieDetails) {
        try {
            SousCategorie updatedSousCategorie = sousCategorieService.updateSousCategorie(id, sousCategorieDetails);
            return ResponseEntity.ok(updatedSousCategorie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une sous-catégorie", description = "Supprime une sous-catégorie du système")
    public ResponseEntity<Void> deleteSousCategorie(
            @Parameter(description = "ID de la sous-catégorie à supprimer") @PathVariable Long id) {
        sousCategorieService.deleteSousCategorie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorie/{categorieId}")
    @Operation(summary = "Récupérer les sous-catégories d'une catégorie", description = "Retourne la liste des sous-catégories par ID catégorie")
    public ResponseEntity<List<SousCategorie>> getSousCategoriesByCategorieId(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId) {
        List<SousCategorie> sousCategories = sousCategorieService.getSousCategoriesByCategorieId(categorieId);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/categorie/{categorieId}/with-produits")
    @Operation(summary = "Récupérer les sous-catégories avec produits", description = "Retourne les sous-catégories d'une catégorie avec leurs produits")
    public ResponseEntity<List<SousCategorie>> getSousCategoriesByCategorieIdWithProduits(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId) {
        List<SousCategorie> sousCategories = sousCategorieService.getSousCategoriesByCategorieIdWithProduits(categorieId);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/nom/{nom}")
    @Operation(summary = "Récupérer une sous-catégorie par nom", description = "Retourne une sous-catégorie par son nom")
    public ResponseEntity<SousCategorie> getSousCategorieByNom(
            @Parameter(description = "Nom de la sous-catégorie") @PathVariable String nom) {
        return sousCategorieService.getSousCategorieByNom(nom)
                .map(sousCategorie -> ResponseEntity.ok(sousCategorie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher par nom", description = "Recherche des sous-catégories par nom (insensible à la casse)")
    public ResponseEntity<List<SousCategorie>> searchSousCategoriesByNom(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<SousCategorie> sousCategories = sousCategorieService.searchSousCategoriesByNom(nom);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des sous-catégories par mot-clé dans nom ou description")
    public ResponseEntity<List<SousCategorie>> searchSousCategoriesByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<SousCategorie> sousCategories = sousCategorieService.searchSousCategoriesByKeyword(keyword);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/count/categorie/{categorieId}")
    @Operation(summary = "Compter par catégorie", description = "Compte le nombre de sous-catégories par catégorie")
    public ResponseEntity<Long> countByCategorieId(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId) {
        long count = sousCategorieService.countByCategorieId(categorieId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Vérifier l'existence", description = "Vérifie si une sous-catégorie existe par ID")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de la sous-catégorie à vérifier") @PathVariable Long id) {
        boolean exists = sousCategorieService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/nom/{nom}")
    @Operation(summary = "Vérifier l'existence par nom", description = "Vérifie si une sous-catégorie existe par nom")
    public ResponseEntity<Boolean> existsByNom(
            @Parameter(description = "Nom de la sous-catégorie à vérifier") @PathVariable String nom) {
        boolean exists = sousCategorieService.existsByNom(nom);
        return ResponseEntity.ok(exists);
    }
}
