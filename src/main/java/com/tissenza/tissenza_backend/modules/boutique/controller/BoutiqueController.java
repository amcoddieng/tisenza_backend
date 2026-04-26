package com.tissenza.tissenza_backend.modules.boutique.controller;

import com.tissenza.tissenza_backend.modules.boutique.dto.BoutiqueDTO;
import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import com.tissenza.tissenza_backend.modules.boutique.service.BoutiqueService;
import com.tissenza.tissenza_backend.exception.ApiResponse;
import com.tissenza.tissenza_backend.service.LocalStorageService;
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
import java.util.List;

@RestController
@RequestMapping("/api/boutiques")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Boutique Management", description = "API pour la gestion des boutiques")
public class BoutiqueController {

    private final BoutiqueService boutiqueService;
    private final LocalStorageService localStorageService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle boutique", description = "Crée une nouvelle boutique dans le système")
    public ResponseEntity<ApiResponse<BoutiqueDTO>> createBoutique(@RequestBody BoutiqueDTO boutiqueDTO) {
        BoutiqueDTO createdBoutique = boutiqueService.createBoutique(boutiqueDTO);
        return new ResponseEntity<>(ApiResponse.success(createdBoutique, "Boutique créée avec succès"), HttpStatus.CREATED);
    }

    /**
     * Créer une boutique avec upload de logo
     */
    @PostMapping("/with-logo")
    @Operation(summary = "Créer une boutique avec logo", description = "Crée une nouvelle boutique avec upload de logo en local")
    public ResponseEntity<ApiResponse<BoutiqueDTO>> createBoutiqueWithLogo(
            @RequestParam("vendeurId") Long vendeurId,
            @RequestParam("nom") String nom,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "addresse", required = false) String addresse,
            @RequestParam(value = "statut", required = false, defaultValue = "EN_ATTENTE") String statut,
            @RequestParam(value = "note", required = false) Float note,
            @RequestParam(value = "logo", required = false) MultipartFile logoFile) {
        
        try {
            // Upload le logo si fourni
            String logoUrl = null;
            if (logoFile != null && !logoFile.isEmpty()) {
                logoUrl = localStorageService.storeFile(logoFile, LocalStorageService.FileType.PRODUIT);
                log.info("Logo uploadé: {}", logoUrl);
            }
            
            // Créer le DTO avec l'URL du logo
            BoutiqueDTO boutiqueDTO = new BoutiqueDTO();
            boutiqueDTO.setVendeurId(vendeurId);
            boutiqueDTO.setNom(nom);
            boutiqueDTO.setDescription(description);
            boutiqueDTO.setAddresse(addresse);
            boutiqueDTO.setStatut(Boutique.Statut.valueOf(statut));
            boutiqueDTO.setNote(note);
            boutiqueDTO.setLogo(logoUrl);
            
            BoutiqueDTO createdBoutique = boutiqueService.createBoutique(boutiqueDTO);
            return new ResponseEntity<>(ApiResponse.success(createdBoutique, "Boutique créée avec succès"), HttpStatus.CREATED);
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload du logo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de l'upload du logo"));
        } catch (Exception e) {
            log.error("Erreur lors de la création de la boutique: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la création de la boutique"));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une boutique par ID", description = "Retourne les détails d'une boutique spécifique")
    public ResponseEntity<ApiResponse<BoutiqueDTO>> getBoutiqueById(
            @Parameter(description = "ID de la boutique à récupérer") @PathVariable Long id) {
        return boutiqueService.getBoutiqueById(id)
                .map(boutique -> ResponseEntity.ok(ApiResponse.success(boutique, "Boutique trouvée")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Boutique non trouvée")));
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les boutiques", description = "Retourne la liste de toutes les boutiques")
    public ResponseEntity<ApiResponse<List<BoutiqueDTO>>> getAllBoutiques() {
        List<BoutiqueDTO> boutiques = boutiqueService.getAllBoutiques();
        return ResponseEntity.ok(ApiResponse.success(boutiques, "Liste des boutiques récupérée"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une boutique", description = "Met à jour les informations d'une boutique existante")
    public ResponseEntity<ApiResponse<BoutiqueDTO>> updateBoutique(
            @Parameter(description = "ID de la boutique à mettre à jour") @PathVariable Long id,
            @RequestBody BoutiqueDTO boutiqueDetails) {
        try {
            BoutiqueDTO updatedBoutique = boutiqueService.updateBoutique(id, boutiqueDetails);
            return ResponseEntity.ok(ApiResponse.success(updatedBoutique, "Boutique mise à jour avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Boutique non trouvée"));
        }
    }

    /**
     * Mettre à jour le logo d'une boutique
     */
    @PostMapping("/{id}/logo")
    @Operation(summary = "Mettre à jour le logo", description = "Upload et met à jour le logo d'une boutique existante")
    public ResponseEntity<ApiResponse<BoutiqueDTO>> updateLogo(
            @Parameter(description = "ID de la boutique") @PathVariable Long id,
            @RequestParam("logo") MultipartFile logoFile) {
        
        try {
            // Upload le logo localement
            String logoUrl = localStorageService.storeFile(logoFile, LocalStorageService.FileType.PRODUIT);
            log.info("Logo uploadé: {}", logoUrl);
            
            // Mettre à jour la boutique avec la nouvelle URL
            BoutiqueDTO boutiqueDetails = new BoutiqueDTO();
            boutiqueDetails.setLogo(logoUrl);
            
            BoutiqueDTO updatedBoutique = boutiqueService.updateBoutique(id, boutiqueDetails);
            return ResponseEntity.ok(ApiResponse.success(updatedBoutique, "Logo mis à jour avec succès"));
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload du logo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de l'upload du logo"));
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du logo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la mise à jour du logo"));
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

    
    @PutMapping("/{id}/note")
    @Operation(summary = "Mettre à jour la note", description = "Met à jour la note d'une boutique")
    public ResponseEntity<ApiResponse<Boutique>> updateNote(
            @Parameter(description = "ID de la boutique") @PathVariable Long id,
            @Parameter(description = "Nouvelle note") @RequestParam Float note) {
        try {
            Boutique updatedBoutique = boutiqueService.updateNote(id, note);
            return ResponseEntity.ok(ApiResponse.success(updatedBoutique, "Note mise à jour avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Boutique non trouvée"));
        }
    }

    /**
     * Mettre à jour les informations générales d'une boutique (sauf logo, statut et note)
     */
    @PutMapping("/{id}/infos")
    @Operation(summary = "Mettre à jour les informations", description = "Met à jour les informations générales d'une boutique (nom, description, adresse)")
    public ResponseEntity<ApiResponse<BoutiqueDTO>> updateInfos(
            @Parameter(description = "ID de la boutique") @PathVariable Long id,
            @Parameter(description = "Nouveau nom") @RequestParam(required = false) String nom,
            @Parameter(description = "Nouvelle description") @RequestParam(required = false) String description,
            @Parameter(description = "Nouvelle adresse") @RequestParam(required = false) String addresse) {
        try {
            BoutiqueDTO updatedBoutique = boutiqueService.updateInfos(id, nom, description, addresse);
            return ResponseEntity.ok(ApiResponse.success(updatedBoutique, "Informations mises à jour avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Boutique non trouvée"));
        }
    }

    /**
     * Mettre à jour le statut d'une boutique
     */
    @PutMapping("/{id}/statut")
    @Operation(summary = "Mettre à jour le statut", description = "Met à jour le statut d'une boutique")
    public ResponseEntity<ApiResponse<Boutique>> updateStatut(
            @Parameter(description = "ID de la boutique") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam String statut) {
        try {
            Boutique.Statut newStatut = Boutique.Statut.valueOf(statut.toUpperCase());
            Boutique updatedBoutique = boutiqueService.updateStatut(id, newStatut);
            return ResponseEntity.ok(ApiResponse.success(updatedBoutique, "Statut mis à jour avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Statut invalide. Valeurs possibles: EN_ATTENTE, VALIDE, REFUSE, SUSPENDU"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Boutique non trouvée"));
        }
    }
}
