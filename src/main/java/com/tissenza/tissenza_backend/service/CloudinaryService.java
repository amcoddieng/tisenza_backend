package com.tissenza.tissenza_backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * Service pour la gestion des images avec Cloudinary
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload une image sur Cloudinary
     * @param file Fichier image à uploader
     * @return URL publique de l'image uploadée
     * @throws IOException En cas d'erreur lors de l'upload
     */
    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier image ne peut pas être vide");
        }

        // Vérifier le type de fichier
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Le fichier doit être une image");
        }

        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                    "resource_type", "auto",
                    "folder", "tissenza",
                    "quality", "auto",
                    "fetch_format", "auto"
                )
            );

            String publicUrl = (String) uploadResult.get("secure_url");
            log.info("Image uploadée avec succès: {}", publicUrl);
            return publicUrl;

        } catch (Exception e) {
            log.error("Erreur lors de l'upload de l'image: {}", e.getMessage());
            throw new IOException("Impossible d'uploader l'image sur Cloudinary", e);
        }
    }

    /**
     * Supprime une image de Cloudinary
     * @param publicId ID public de l'image à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteImage(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            boolean success = "ok".equals(resultStatus);
            
            if (success) {
                log.info("Image supprimée avec succès: {}", publicId);
            } else {
                log.warn("Échec de la suppression de l'image: {}", publicId);
            }
            
            return success;
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'image {}: {}", publicId, e.getMessage());
            return false;
        }
    }

    /**
     * Extrait le public ID d'une URL Cloudinary
     * @param imageUrl URL de l'image Cloudinary
     * @return Public ID de l'image
     */
    public String extractPublicId(String imageUrl) {
        if (imageUrl == null || !imageUrl.contains("cloudinary")) {
            return null;
        }

        try {
            // Exemple d'URL: https://res.cloudinary.com/drrgj1x2a/image/upload/v1234567890/tissenza/xyz123.jpg
            String[] parts = imageUrl.split("/");
            
            // Trouver l'index de "upload"
            int uploadIndex = -1;
            for (int i = 0; i < parts.length; i++) {
                if ("upload".equals(parts[i])) {
                    uploadIndex = i;
                    break;
                }
            }
            
            if (uploadIndex != -1 && uploadIndex + 2 < parts.length) {
                // Le public ID inclut le dossier et le nom du fichier sans extension
                String fileNameWithExtension = parts[uploadIndex + 2];
                String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
                return "tissenza/" + fileName;
            }
            
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction du public ID: {}", e.getMessage());
        }
        
        return null;
    }

    /**
     * Vérifie si une URL est une URL Cloudinary valide
     * @param imageUrl URL à vérifier
     * @return true si c'est une URL Cloudinary, false sinon
     */
    public boolean isCloudinaryUrl(String imageUrl) {
        return imageUrl != null && imageUrl.contains("cloudinary.com");
    }
}
