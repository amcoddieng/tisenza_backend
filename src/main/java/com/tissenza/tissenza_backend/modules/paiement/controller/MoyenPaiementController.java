package com.tissenza.tissenza_backend.modules.paiement.controller;

import com.tissenza.tissenza_backend.exception.ApiResponse;
import com.tissenza.tissenza_backend.modules.paiement.dto.MoyenPaiementDTO;
import com.tissenza.tissenza_backend.modules.paiement.dto.UserMoyenPaiementDTO;
import com.tissenza.tissenza_backend.modules.paiement.service.MoyenPaiementService;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.entity.Compte.Role;
import com.tissenza.tissenza_backend.service.CloudinaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Controller REST pour la gestion des moyens de paiement et des associations utilisateurs-moyens de paiement
 */
@RestController
@RequestMapping("/api/paiement")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class MoyenPaiementController {

    private final MoyenPaiementService moyenPaiementService;
    private final CloudinaryService cloudinaryService;

    // ============= ENDPOINTS POUR LES MOYENS DE PAIEMENT =============

    /**
     * Crée un nouveau moyen de paiement
     */
    @PostMapping("/moyens")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MoyenPaiementDTO>> createMoyenPaiement(
            @Valid @RequestBody MoyenPaiementDTO dto) {
        log.info("Requête POST /api/paiement/moyens - Création moyen de paiement: {}", dto.getNom());
        
        MoyenPaiementDTO created = moyenPaiementService.createMoyenPaiement(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Moyen de paiement créé avec succès"));
    }

    /**
     * Crée un moyen de paiement avec upload de photo
     */
    @PostMapping("/moyens/with-photo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MoyenPaiementDTO>> createMoyenPaiementWithPhoto(
            @RequestParam("nom") String nom,
            @RequestParam(value = "photo", required = false) MultipartFile photoFile) {
        
        try {
            // Upload la photo si fournie
            String photoUrl = null;
            if (photoFile != null && !photoFile.isEmpty()) {
                photoUrl = cloudinaryService.uploadImage(photoFile);
                log.info("Photo uploadée: {}", photoUrl);
            }
            
            // Créer le DTO avec l'URL de la photo
            MoyenPaiementDTO dto = new MoyenPaiementDTO();
            dto.setNom(nom);
            dto.setPhoto(photoUrl);
            
            MoyenPaiementDTO created = moyenPaiementService.createMoyenPaiement(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Moyen de paiement créé avec succès"));
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload de la photo: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de l'upload de la photo"));
        } catch (Exception e) {
            log.error("Erreur lors de la création du moyen de paiement: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Erreur lors de la création du moyen de paiement"));
        }
    }

    /**
     * Récupère tous les moyens de paiement
     */
    @GetMapping("/moyens")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<List<MoyenPaiementDTO>>> getAllMoyensPaiement() {
        log.info("Requête GET /api/paiement/moyens - Liste de tous les moyens de paiement");
        
        List<MoyenPaiementDTO> moyens = moyenPaiementService.getAllMoyensPaiement();
        return ResponseEntity.ok(ApiResponse.success(moyens, "Liste des moyens de paiement"));
    }

    /**
     * Récupère un moyen de paiement par son ID
     */
    @GetMapping("/moyens/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<MoyenPaiementDTO>> getMoyenPaiementById(@PathVariable Long id) {
        log.info("Requête GET /api/paiement/moyens/{} - Récupération moyen de paiement", id);
        
        return moyenPaiementService.getMoyenPaiementById(id)
                .map(moyen -> ResponseEntity.ok(ApiResponse.success(moyen, "Moyen de paiement trouvé")))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Moyen de paiement non trouvé avec l'ID: " + id)));
    }

    /**
     * Met à jour un moyen de paiement
     */
    @PutMapping("/moyens/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MoyenPaiementDTO>> updateMoyenPaiement(
            @PathVariable Long id, @Valid @RequestBody MoyenPaiementDTO dto) {
        log.info("Requête PUT /api/paiement/moyens/{} - Mise à jour moyen de paiement", id);
        
        try {
            MoyenPaiementDTO updated = moyenPaiementService.updateMoyenPaiement(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Moyen de paiement mis à jour avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Supprime un moyen de paiement
     */
    @DeleteMapping("/moyens/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteMoyenPaiement(@PathVariable Long id) {
        log.info("Requête DELETE /api/paiement/moyens/{} - Suppression moyen de paiement", id);
        
        try {
            moyenPaiementService.deleteMoyenPaiement(id);
            return ResponseEntity.ok(ApiResponse.<Void>success(null, "Moyen de paiement supprimé avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Recherche des moyens de paiement par mot-clé
     */
    @GetMapping("/moyens/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<List<MoyenPaiementDTO>>> searchMoyensPaiement(
            @RequestParam String keyword) {
        log.info("Requête GET /api/paiement/moyens/search?keyword={} - Recherche moyens de paiement", keyword);
        
        List<MoyenPaiementDTO> moyens = moyenPaiementService.searchMoyensPaiement(keyword);
        return ResponseEntity.ok(ApiResponse.success(moyens, "Résultats de recherche"));
    }

    // ============= ENDPOINTS POUR LES ASSOCIATIONS UTILISATEUR-MOYEN PAIEMENT =============

    /**
     * Associe un moyen de paiement à un utilisateur
     */
    @PostMapping("/associations")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<UserMoyenPaiementDTO>> associateMoyenPaiementToUser(
            @RequestParam Long userId,
            @RequestParam Long moyenPaiementId,
            @RequestParam(required = false, defaultValue = "true") Boolean actif,
            @RequestParam(required = false) String numero) {
        log.info("Requête POST /api/paiement/associations - Association utilisateur {} moyen {} numero {}", userId, moyenPaiementId, numero);
        
        try {
            UserMoyenPaiementDTO association = moyenPaiementService.associateMoyenPaiementToUser(userId, moyenPaiementId, actif, numero);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(association, "Association créée avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Récupère tous les moyens de paiement d'un utilisateur
     */
    @GetMapping("/associations/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<List<UserMoyenPaiementDTO>>> getMoyensPaiementByUser(
            @PathVariable Long userId) {
        log.info("Requête GET /api/paiement/associations/user/{} - Moyens de paiement utilisateur", userId);
        
        List<UserMoyenPaiementDTO> associations = moyenPaiementService.getMoyensPaiementByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(associations, "Moyens de paiement de l'utilisateur"));
    }

    /**
     * Récupère les moyens de paiement actifs d'un utilisateur
     */
    @GetMapping("/associations/user/{userId}/actifs")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<List<MoyenPaiementDTO>>> getMoyensPaiementActifsByUser(
            @PathVariable Long userId) {
        log.info("Requête GET /api/paiement/associations/user/{}/actifs - Moyens actifs utilisateur", userId);
        
        List<MoyenPaiementDTO> moyens = moyenPaiementService.getMoyensPaiementActifsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(moyens, "Moyens de paiement actifs de l'utilisateur"));
    }

    /**
     * Récupère tous les utilisateurs qui ont un moyen de paiement spécifique
     */
    @GetMapping("/associations/moyen/{moyenPaiementId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserMoyenPaiementDTO>>> getUsersByMoyenPaiement(
            @PathVariable Long moyenPaiementId) {
        log.info("Requête GET /api/paiement/associations/moyen/{} - Utilisateurs moyen paiement", moyenPaiementId);
        
        List<UserMoyenPaiementDTO> associations = moyenPaiementService.getUsersByMoyenPaiement(moyenPaiementId);
        return ResponseEntity.ok(ApiResponse.success(associations, "Utilisateurs ayant ce moyen de paiement"));
    }

    /**
     * Active un moyen de paiement pour un utilisateur
     */
    @PutMapping("/associations/{userId}/{moyenPaiementId}/activer")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<Void>> activerMoyenPaiementForUser(
            @PathVariable Long userId, @PathVariable Long moyenPaiementId) {
        log.info("Requête PUT /api/paiement/associations/{}/{}/activer - Activation moyen paiement", userId, moyenPaiementId);
        
        try {
            moyenPaiementService.activerMoyenPaiementForUser(userId, moyenPaiementId);
            return ResponseEntity.ok(ApiResponse.<Void>success(null, "Moyen de paiement activé avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Désactive un moyen de paiement pour un utilisateur
     */
    @PutMapping("/associations/{userId}/{moyenPaiementId}/desactiver")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<Void>> desactiverMoyenPaiementForUser(
            @PathVariable Long userId, @PathVariable Long moyenPaiementId) {
        log.info("Requête PUT /api/paiement/associations/{}/{}/desactiver - Désactivation moyen paiement", userId, moyenPaiementId);
        
        try {
            moyenPaiementService.desactiverMoyenPaiementForUser(userId, moyenPaiementId);
            return ResponseEntity.ok(ApiResponse.<Void>success(null, "Moyen de paiement désactivé avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Supprime une association utilisateur-moyen de paiement
     */
    @DeleteMapping("/associations/{userId}/{moyenPaiementId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<Void>> removeMoyenPaiementFromUser(
            @PathVariable Long userId, @PathVariable Long moyenPaiementId) {
        log.info("Requête DELETE /api/paiement/associations/{}/{}/ - Suppression association", userId, moyenPaiementId);
        
        try {
            moyenPaiementService.removeMoyenPaiementFromUser(userId, moyenPaiementId);
            return ResponseEntity.ok(ApiResponse.<Void>success(null, "Association supprimée avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * Vérifie si un utilisateur a un moyen de paiement spécifique actif
     */
    @GetMapping("/associations/{userId}/{moyenPaiementId}/check")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<Boolean>> checkUserHasMoyenPaiementActif(
            @PathVariable Long userId, @PathVariable Long moyenPaiementId) {
        log.info("Requête GET /api/paiement/associations/{}/{}/check - Vérification association", userId, moyenPaiementId);
        
        boolean hasMoyen = moyenPaiementService.userHasMoyenPaiementActif(userId, moyenPaiementId);
        return ResponseEntity.ok(ApiResponse.success(hasMoyen, "Vérification terminée"));
    }

    /**
     * Compte le nombre de moyens de paiement actifs pour un utilisateur
     */
    @GetMapping("/associations/user/{userId}/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    public ResponseEntity<ApiResponse<Long>> countMoyensPaiementActifsByUser(@PathVariable Long userId) {
        log.info("Requête GET /api/paiement/associations/user/{}/count - Compte moyens actifs", userId);
        
        long count = moyenPaiementService.countMoyensPaiementActifsByUser(userId);
        return ResponseEntity.ok(ApiResponse.success(count, "Nombre de moyens de paiement actifs"));
    }
}
