package com.tissenza.tissenza_backend.modules.user.controller;

import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.service.PersonneService;
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
@RequestMapping("/api/personnes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Personne Management", description = "API pour la gestion des personnes")
public class PersonneController {

    private final PersonneService personneService;
    private final LocalStorageService localStorageService;

    @PostMapping
    @Operation(summary = "Créer une nouvelle personne", description = "Crée une nouvelle personne dans le système")
    public ResponseEntity<ApiResponse<Personne>> createPersonne(@RequestBody Personne personne) {
        Personne createdPersonne = personneService.createPersonne(personne);
        return new ResponseEntity<>(ApiResponse.success(createdPersonne, "Personne créée avec succès"), HttpStatus.CREATED);
    }

    /**
     * Mettre à jour la photo de profil d'une personne
     */
    @PostMapping("/{id}/photo-profil")
    @Operation(summary = "Mettre à jour la photo de profil", description = "Upload et met à jour la photo de profil d'une personne")
    public ResponseEntity<ApiResponse<Personne>> updatePhotoProfil(
            @Parameter(description = "ID de la personne") @PathVariable Long id,
            @RequestParam("photoProfil") MultipartFile photoFile) {
        
        try {
            // Upload la photo localement dans le dossier profil
            String photoUrl = localStorageService.storeFile(photoFile, LocalStorageService.FileType.PROFIL);
            log.info("Photo de profil uploadée: {}", photoUrl);
            
            // Mettre à jour uniquement la photo de profil
            Personne updatedPersonne = personneService.updatePhotoProfil(id, photoUrl);
            return ResponseEntity.ok(ApiResponse.success(updatedPersonne, "Photo de profil mise à jour avec succès"));
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload de la photo de profil: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de l'upload de la photo de profil"));
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de la photo de profil: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la mise à jour de la photo de profil"));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une personne par ID", description = "Retourne les détails d'une personne spécifique")
    public ResponseEntity<ApiResponse<Personne>> getPersonneById(
            @Parameter(description = "ID de la personne à récupérer") @PathVariable Long id) {
        return personneService.getPersonneById(id)
                .map(personne -> ResponseEntity.ok(ApiResponse.success(personne, "Personne trouvée")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Personne non trouvée")));
    }

    @GetMapping
    @Operation(summary = "Récupérer toutes les personnes", description = "Retourne la liste de toutes les personnes")
    public ResponseEntity<ApiResponse<List<Personne>>> getAllPersonnes() {
        List<Personne> personnes = personneService.getAllPersonnes();
        return ResponseEntity.ok(ApiResponse.success(personnes, "Liste des personnes récupérée"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une personne", description = "Met à jour les informations d'une personne existante")
    public ResponseEntity<ApiResponse<Personne>> updatePersonne(
            @Parameter(description = "ID de la personne à mettre à jour") @PathVariable Long id,
            @RequestBody Personne personneDetails) {
        try {
            Personne updatedPersonne = personneService.updatePersonne(id, personneDetails);
            return ResponseEntity.ok(ApiResponse.success(updatedPersonne, "Personne mise à jour avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Personne non trouvée"));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une personne", description = "Supprime une personne du système")
    public ResponseEntity<ApiResponse<Void>> deletePersonne(
            @Parameter(description = "ID de la personne à supprimer") @PathVariable Long id) {
        personneService.deletePersonne(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Personne supprimée avec succès"));
    }

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher par nom", description = "Recherche des personnes par nom (insensible à la casse)")
    public ResponseEntity<ApiResponse<List<Personne>>> searchByNom(
            @Parameter(description = "Nom à rechercher") @RequestParam String nom) {
        List<Personne> personnes = personneService.searchByNom(nom);
        return ResponseEntity.ok(ApiResponse.success(personnes, "Résultats de recherche pour: " + nom));
    }

    @GetMapping("/search/prenom")
    @Operation(summary = "Rechercher par prénom", description = "Recherche des personnes par prénom (insensible à la casse)")
    public ResponseEntity<ApiResponse<List<Personne>>> searchByPrenom(
            @Parameter(description = "Prénom à rechercher") @RequestParam String prenom) {
        List<Personne> personnes = personneService.searchByPrenom(prenom);
        return ResponseEntity.ok(ApiResponse.success(personnes, "Résultats de recherche pour: " + prenom));
    }

    @GetMapping("/search/ville")
    @Operation(summary = "Rechercher par ville", description = "Recherche des personnes par ville")
    public ResponseEntity<ApiResponse<List<Personne>>> searchByVille(
            @Parameter(description = "Ville à rechercher") @RequestParam String ville) {
        List<Personne> personnes = personneService.searchByVille(ville);
        return ResponseEntity.ok(ApiResponse.success(personnes, "Personnes trouvées à: " + ville));
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des personnes par mot-clé dans nom, prénom ou ville")
    public ResponseEntity<ApiResponse<List<Personne>>> searchByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<Personne> personnes = personneService.searchByKeyword(keyword);
        return ResponseEntity.ok(ApiResponse.success(personnes, "Résultats de recherche pour: " + keyword));
    }

    @GetMapping("/count/ville")
    @Operation(summary = "Compter par ville", description = "Compte le nombre de personnes par ville")
    public ResponseEntity<Long> countByVille(
            @Parameter(description = "Ville pour le comptage") @RequestParam String ville) {
        long count = personneService.countByVille(ville);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Vérifier l'existence", description = "Vérifie si une personne existe par ID")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de la personne à vérifier") @PathVariable Long id) {
        boolean exists = personneService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
