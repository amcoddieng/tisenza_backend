package com.tissenza.tissenza_backend.controller;

import com.tissenza.tissenza_backend.exception.ApiResponse;
import com.tissenza.tissenza_backend.service.CloudinaryService;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Controller pour la gestion des images avec Cloudinary
 */
@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Image Management", description = "API pour la gestion des images avec Cloudinary")
public class ImageController {

    private final CloudinaryService cloudinaryService;

    /**
     * Upload une image sur Cloudinary
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    @Operation(summary = "Uploader une image", description = "Upload une image sur Cloudinary et retourne l'URL publique")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @Parameter(description = "Fichier image à uploader", required = true)
            @RequestParam("file") MultipartFile file) {
        
        try {
            String imageUrl = cloudinaryService.uploadImage(file);
            
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            response.put("message", "Image uploadée avec succès");
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(response, "Image uploadée avec succès"));
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IOException e) {
            log.error("Erreur lors de l'upload de l'image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de l'upload de l'image"));
        }
    }

    /**
     * Supprime une image de Cloudinary
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR')")
    @Operation(summary = "Supprimer une image", description = "Supprime une image de Cloudinary par son URL")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteImage(
            @Parameter(description = "URL de l'image à supprimer", required = true)
            @RequestParam("imageUrl") String imageUrl) {
        
        try {
            String publicId = cloudinaryService.extractPublicId(imageUrl);
            
            if (publicId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.error("URL d'image Cloudinary invalide"));
            }
            
            boolean deleted = cloudinaryService.deleteImage(publicId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("deleted", deleted);
            response.put("publicId", publicId);
            response.put("imageUrl", imageUrl);
            
            if (deleted) {
                return ResponseEntity.ok()
                        .body(ApiResponse.success(response, "Image supprimée avec succès"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Image non trouvée ou déjà supprimée"));
            }
            
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'image: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erreur lors de la suppression de l'image"));
        }
    }

    /**
     * Vérifie si une URL est une URL Cloudinary valide
     */
    @GetMapping("/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEUR', 'CLIENT')")
    @Operation(summary = "Valider une URL Cloudinary", description = "Vérifie si une URL est une URL Cloudinary valide")
    public ResponseEntity<ApiResponse<Map<String, Object>>> validateCloudinaryUrl(
            @Parameter(description = "URL à valider", required = true)
            @RequestParam("imageUrl") String imageUrl) {
        
        boolean isValid = cloudinaryService.isCloudinaryUrl(imageUrl);
        String publicId = isValid ? cloudinaryService.extractPublicId(imageUrl) : null;
        
        Map<String, Object> response = new HashMap<>();
        response.put("isValid", isValid);
        response.put("isCloudinary", isValid);
        response.put("publicId", publicId);
        
        return ResponseEntity.ok()
                .body(ApiResponse.success(response, "Validation de l'URL terminée"));
    }
}
