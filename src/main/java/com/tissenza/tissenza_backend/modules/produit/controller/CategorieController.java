package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.entity.Categorie;
import com.tissenza.tissenza_backend.modules.produit.service.CategorieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categorie Management", description = "API pour la gestion des catégories")
public class CategorieController {

    private final CategorieService categorieService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle catégorie", description = "Crée une nouvelle catégorie dans le système")
    public ResponseEntity<Categorie> createCategorie(@RequestBody Categorie categorie) {
        Categorie createdCategorie = categorieService.createCategorie(categorie);
        return new ResponseEntity<>(createdCategorie, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une catégorie par ID", description = "Retourne les détails d'une catégorie spécifique")
    public ResponseEntity<Categorie> getCategorieById(
            @Parameter(description = "ID de la catégorie à récupérer") @PathVariable Long id) {
        return categorieService.getCategorieById(id)
                .map(categorie -> ResponseEntity.ok(categorie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les catégories", description = "Retourne la liste de toutes les catégories")
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = categorieService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/with-sous-categories")
    @Operation(summary = "Récupérer les catégories avec sous-catégories", description = "Retourne les catégories avec leurs sous-catégories chargées")
    public ResponseEntity<List<Categorie>> getAllCategoriesWithSousCategories() {
        List<Categorie> categories = categorieService.getAllCategoriesWithSousCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une catégorie", description = "Met à jour les informations d'une catégorie existante")
    public ResponseEntity<Categorie> updateCategorie(
            @Parameter(description = "ID de la catégorie à mettre à jour") @PathVariable Long id,
            @RequestBody Categorie categorieDetails) {
        try {
            Categorie updatedCategorie = categorieService.updateCategorie(id, categorieDetails);
            return ResponseEntity.ok(updatedCategorie);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une catégorie", description = "Supprime une catégorie du système")
    public ResponseEntity<Void> deleteCategorie(
            @Parameter(description = "ID de la catégorie à supprimer") @PathVariable Long id) {
        categorieService.deleteCategorie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nom/{nom}")
    @Operation(summary = "Récupérer une catégorie par nom", description = "Retourne une catégorie par son nom")
    public ResponseEntity<Categorie> getCategorieByNom(
            @Parameter(description = "Nom de la catégorie") @PathVariable String nom) {
        return categorieService.getCategorieByNom(nom)
                .map(categorie -> ResponseEntity.ok(categorie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher par nom", description = "Recherche des catégories par nom (insensible à la casse)")
    public ResponseEntity<List<Categorie>> searchCategoriesByNom(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<Categorie> categories = categorieService.searchCategoriesByNom(nom);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des catégories par mot-clé dans nom ou description")
    public ResponseEntity<List<Categorie>> searchCategoriesByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<Categorie> categories = categorieService.searchCategoriesByKeyword(keyword);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/count")
    @Operation(summary = "Compter les catégories", description = "Retourne le nombre total de catégories")
    public ResponseEntity<Long> countCategories() {
        long count = categorieService.countCategories();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Vérifier l'existence", description = "Vérifie si une catégorie existe par ID")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de la catégorie à vérifier") @PathVariable Long id) {
        boolean exists = categorieService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/nom/{nom}")
    @Operation(summary = "Vérifier l'existence par nom", description = "Vérifie si une catégorie existe par nom")
    public ResponseEntity<Boolean> existsByNom(
            @Parameter(description = "Nom de la catégorie à vérifier") @PathVariable String nom) {
        boolean exists = categorieService.existsByNom(nom);
        return ResponseEntity.ok(exists);
    }
}
