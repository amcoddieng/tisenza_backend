package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.dto.SousCategorieDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import com.tissenza.tissenza_backend.modules.produit.service.SousCategorieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sous-categories")
@RequiredArgsConstructor
@Tag(name = "SousCategorie Management", description = "API pour la gestion des sous-catégories")
public class SousCategorieController {

    private final SousCategorieService sousCategorieService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Créer une nouvelle sous-catégorie", description = "Crée une nouvelle sous-catégorie dans le système")
    public ResponseEntity<SousCategorie> createSousCategorie(@RequestBody SousCategorie sousCategorie) {
        SousCategorie createdSousCategorie = sousCategorieService.createSousCategorie(sousCategorie);
        return new ResponseEntity<>(createdSousCategorie, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer une sous-catégorie par ID", description = "Retourne les détails d'une sous-catégorie spécifique en DTO")
    public ResponseEntity<SousCategorieDTO> getSousCategorieById(
            @Parameter(description = "ID de la sous-catégorie à récupérer") @PathVariable Long id) {
        return sousCategorieService.getSousCategorieByIdDTO(id)
                .map(sousCategorie -> ResponseEntity.ok(sousCategorie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer toutes les sous-catégories", description = "Retourne la liste de toutes les sous-catégories en DTO")
    public ResponseEntity<List<SousCategorieDTO>> getAllSousCategories() {
        List<SousCategorieDTO> sousCategories = sousCategorieService.getAllSousCategoriesDTO();
        return ResponseEntity.ok(sousCategories);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Supprimer une sous-catégorie", description = "Supprime une sous-catégorie du système")
    public ResponseEntity<Void> deleteSousCategorie(
            @Parameter(description = "ID de la sous-catégorie à supprimer") @PathVariable Long id) {
        sousCategorieService.deleteSousCategorie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorie/{categorieId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer les sous-catégories d'une catégorie", description = "Retourne la liste des sous-catégories par ID catégorie en DTO")
    public ResponseEntity<List<SousCategorieDTO>> getSousCategoriesByCategorieId(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.getSousCategoriesByCategorieIdDTO(categorieId);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/categorie/{categorieId}/with-produits")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer les sous-catégories avec produits", description = "Retourne les sous-catégories d'une catégorie avec leurs produits en DTO")
    public ResponseEntity<List<SousCategorieDTO>> getSousCategoriesByCategorieIdWithProduits(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.getSousCategoriesByCategorieIdWithProduitsDTO(categorieId);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/nom/{nom}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer une sous-catégorie par nom", description = "Retourne une sous-catégorie par son nom en DTO")
    public ResponseEntity<SousCategorieDTO> getSousCategorieByNom(
            @Parameter(description = "Nom de la sous-catégorie") @PathVariable String nom) {
        return sousCategorieService.getSousCategorieByNomDTO(nom)
                .map(sousCategorie -> ResponseEntity.ok(sousCategorie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/nom")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher par nom", description = "Recherche des sous-catégories par nom (insensible à la casse) en DTO")
    public ResponseEntity<List<SousCategorieDTO>> searchSousCategoriesByNom(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.searchSousCategoriesByNomDTO(nom);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des sous-catégories par mot-clé dans nom ou description en DTO")
    public ResponseEntity<List<SousCategorieDTO>> searchSousCategoriesByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.searchSousCategoriesByKeywordDTO(keyword);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/count/categorie/{categorieId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Compter par catégorie", description = "Compte le nombre de sous-catégories par catégorie")
    public ResponseEntity<Long> countByCategorieId(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId) {
        long count = sousCategorieService.countByCategorieId(categorieId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Vérifier l'existence", description = "Vérifie si une sous-catégorie existe par ID")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de la sous-catégorie à vérifier") @PathVariable Long id) {
        boolean exists = sousCategorieService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/nom/{nom}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Vérifier l'existence par nom", description = "Vérifie si une sous-catégorie existe par nom")
    public ResponseEntity<Boolean> existsByNom(
            @Parameter(description = "Nom de la sous-catégorie à vérifier") @PathVariable String nom) {
        boolean exists = sousCategorieService.existsByNom(nom);
        return ResponseEntity.ok(exists);
    }

    // ========== NOUVELLES APIS AVEC INFORMATIONS DE CATÉGORIE ==========

    @GetMapping("/{id}/with-categorie")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer une sous-catégorie avec catégorie", description = "Retourne une sous-catégorie avec les informations de sa catégorie")
    public ResponseEntity<SousCategorieDTO> getSousCategorieByIdWithCategorie(
            @Parameter(description = "ID de la sous-catégorie à récupérer") @PathVariable Long id) {
        return sousCategorieService.getSousCategorieByIdWithCategorieDTO(id)
                .map(sousCategorie -> ResponseEntity.ok(sousCategorie))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/with-categorie")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Récupérer toutes les sous-catégories avec catégorie", description = "Retourne la liste de toutes les sous-catégories avec leurs informations de catégorie")
    public ResponseEntity<List<SousCategorieDTO>> getAllSousCategoriesWithCategorie() {
        List<SousCategorieDTO> sousCategories = sousCategorieService.getAllSousCategoriesWithCategorieDTO();
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/search/nom/with-categorie")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher par nom avec catégorie", description = "Recherche des sous-catégories par nom avec informations de catégorie")
    public ResponseEntity<List<SousCategorieDTO>> searchSousCategoriesByNomWithCategorie(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.searchSousCategoriesByNomWithCategorieDTO(nom);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/search/with-categorie")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Recherche par mot-clé avec catégorie", description = "Recherche des sous-catégories par mot-clé avec informations de catégorie")
    public ResponseEntity<List<SousCategorieDTO>> searchSousCategoriesByKeywordWithCategorie(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.searchSousCategoriesByKeywordWithCategorieDTO(keyword);
        return ResponseEntity.ok(sousCategories);
    }

    // ========== NOUVELLES APIS DE RECHERCHE PAR ID CATÉGORIE ==========

    @GetMapping("/categorie/{categorieId}/with-categorie-info")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher par ID catégorie avec infos", description = "Recherche des sous-catégories par ID catégorie avec informations de catégorie")
    public ResponseEntity<List<SousCategorieDTO>> searchSousCategoriesByCategorieIdWithCategorie(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.searchSousCategoriesByCategorieIdWithCategorieDTO(categorieId);
        return ResponseEntity.ok(sousCategories);
    }

    @GetMapping("/categorie/{categorieId}/search/nom")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Rechercher par catégorie et nom", description = "Recherche des sous-catégories par ID catégorie et nom (LIKE)")
    public ResponseEntity<List<SousCategorieDTO>> searchSousCategoriesByCategorieIdAndNom(
            @Parameter(description = "ID de la catégorie") @PathVariable Long categorieId,
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<SousCategorieDTO> sousCategories = sousCategorieService.searchSousCategoriesByCategorieIdAndNomWithCategorieDTO(categorieId, nom);
        return ResponseEntity.ok(sousCategories);
    }
}
