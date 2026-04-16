package com.tissenza.tissenza_backend.modules.boutique.controller;

import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import com.tissenza.tissenza_backend.modules.boutique.service.BoutiqueService;
import com.tissenza.tissenza_backend.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boutiques")
@RequiredArgsConstructor
@Tag(name = "Boutique Management", description = "API pour la gestion des boutiques")
public class BoutiqueController {

    private final BoutiqueService boutiqueService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle boutique", description = "Crée une nouvelle boutique dans le système")
    public ResponseEntity<ApiResponse<Boutique>> createBoutique(@RequestBody Boutique boutique) {
        Boutique createdBoutique = boutiqueService.createBoutique(boutique);
        return new ResponseEntity<>(ApiResponse.success(createdBoutique, "Boutique créée avec succès"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une boutique par ID", description = "Retourne les détails d'une boutique spécifique")
    public ResponseEntity<ApiResponse<Boutique>> getBoutiqueById(
            @Parameter(description = "ID de la boutique à récupérer") @PathVariable Long id) {
        return boutiqueService.getBoutiqueById(id)
                .map(boutique -> ResponseEntity.ok(ApiResponse.success(boutique, "Boutique trouvée")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Boutique non trouvée")));
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les boutiques", description = "Retourne la liste de toutes les boutiques")
    public ResponseEntity<ApiResponse<List<Boutique>>> getAllBoutiques() {
        List<Boutique> boutiques = boutiqueService.getAllBoutiques();
        return ResponseEntity.ok(ApiResponse.success(boutiques, "Liste des boutiques récupérée"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une boutique", description = "Met à jour les informations d'une boutique existante")
    public ResponseEntity<Boutique> updateBoutique(
            @Parameter(description = "ID de la boutique à mettre à jour") @PathVariable Long id,
            @RequestBody Boutique boutiqueDetails) {
        try {
            Boutique updatedBoutique = boutiqueService.updateBoutique(id, boutiqueDetails);
            return ResponseEntity.ok(updatedBoutique);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une boutique", description = "Supprime une boutique du système")
    public ResponseEntity<Void> deleteBoutique(
            @Parameter(description = "ID de la boutique à supprimer") @PathVariable Long id) {
        boutiqueService.deleteBoutique(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/vendeur/{vendeurId}")
    @Operation(summary = "Récupérer les boutiques d'un vendeur", description = "Retourne la liste des boutiques par ID vendeur")
    public ResponseEntity<List<Boutique>> getBoutiquesByVendeurId(
            @Parameter(description = "ID du vendeur") @PathVariable Long vendeurId) {
        List<Boutique> boutiques = boutiqueService.getBoutiquesByVendeurId(vendeurId);
        return ResponseEntity.ok(boutiques);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer des boutiques par statut", description = "Retourne la liste des boutiques par statut")
    public ResponseEntity<List<Boutique>> getBoutiquesByStatut(
            @Parameter(description = "Statut à rechercher") @PathVariable Boutique.Statut statut) {
        List<Boutique> boutiques = boutiqueService.getBoutiquesByStatut(statut);
        return ResponseEntity.ok(boutiques);
    }

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher par nom", description = "Recherche des boutiques par nom (insensible à la casse)")
    public ResponseEntity<List<Boutique>> searchBoutiquesByNom(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<Boutique> boutiques = boutiqueService.searchBoutiquesByNom(nom);
        return ResponseEntity.ok(boutiques);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des boutiques par mot-clé dans nom ou description")
    public ResponseEntity<List<Boutique>> searchBoutiquesByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<Boutique> boutiques = boutiqueService.searchBoutiquesByKeyword(keyword);
        return ResponseEntity.ok(boutiques);
    }

    @GetMapping("/note/{noteMin}")
    @Operation(summary = "Récupérer des boutiques par note minimale", description = "Retourne les boutiques avec une note supérieure ou égale")
    public ResponseEntity<List<Boutique>> getBoutiquesByNoteMin(
            @Parameter(description = "Note minimale") @PathVariable Float noteMin) {
        List<Boutique> boutiques = boutiqueService.getBoutiquesByNoteMin(noteMin);
        return ResponseEntity.ok(boutiques);
    }

    @GetMapping("/stats/note/{statut}")
    @Operation(summary = "Note moyenne par statut", description = "Calcule la note moyenne des boutiques par statut")
    public ResponseEntity<Float> getAverageNoteByStatut(
            @Parameter(description = "Statut pour le calcul") @PathVariable Boutique.Statut statut) {
        Float average = boutiqueService.getAverageNoteByStatut(statut);
        return ResponseEntity.ok(average);
    }

    @GetMapping("/count/statut/{statut}")
    @Operation(summary = "Compter par statut", description = "Compte le nombre de boutiques par statut")
    public ResponseEntity<Long> countBoutiquesByStatut(
            @Parameter(description = "Statut pour le comptage") @PathVariable Boutique.Statut statut) {
        long count = boutiqueService.countBoutiquesByStatut(statut);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/statut")
    @Operation(summary = "Statistiques par statut", description = "Retourne les statistiques des boutiques par statut")
    public ResponseEntity<List<Object[]>> getStatisticsByStatut() {
        List<Object[]> stats = boutiqueService.getStatisticsByStatut();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/exists/vendeur/{vendeurId}")
    @Operation(summary = "Vérifier l'existence par vendeur", description = "Vérifie si une boutique existe pour un vendeur")
    public ResponseEntity<Boolean> existsByVendeurId(
            @Parameter(description = "ID du vendeur à vérifier") @PathVariable Long vendeurId) {
        boolean exists = boutiqueService.existsByVendeurId(vendeurId);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/{id}/validate")
    @Operation(summary = "Valider une boutique", description = "Change le statut d'une boutique à VALIDÉ")
    public ResponseEntity<Boutique> validateBoutique(
            @Parameter(description = "ID de la boutique à valider") @PathVariable Long id) {
        try {
            Boutique validatedBoutique = boutiqueService.validateBoutique(id);
            return ResponseEntity.ok(validatedBoutique);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/refuse")
    @Operation(summary = "Refuser une boutique", description = "Change le statut d'une boutique à REFUSÉ")
    public ResponseEntity<Boutique> refuseBoutique(
            @Parameter(description = "ID de la boutique à refuser") @PathVariable Long id) {
        try {
            Boutique refusedBoutique = boutiqueService.refuseBoutique(id);
            return ResponseEntity.ok(refusedBoutique);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/note")
    @Operation(summary = "Mettre à jour la note", description = "Met à jour la note d'une boutique")
    public ResponseEntity<Boutique> updateNote(
            @Parameter(description = "ID de la boutique") @PathVariable Long id,
            @Parameter(description = "Nouvelle note") @RequestParam Float note) {
        try {
            Boutique updatedBoutique = boutiqueService.updateNote(id, note);
            return ResponseEntity.ok(updatedBoutique);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
