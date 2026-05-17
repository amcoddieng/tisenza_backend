package com.tissenza.tissenza_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Service pour la gestion des fichiers locaux
 */
@Service
@Slf4j
public class FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * Sauvegarde une image dans le dossier local
     * @param file Fichier image à sauvegarder
     * @param subFolder Sous-dossier (ex: "article", "produit", "profil")
     * @return Chemin relatif de l'image sauvegardée
     * @throws IOException En cas d'erreur lors de la sauvegarde
     */
    public String saveImage(MultipartFile file, String subFolder) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier image ne peut pas être vide");
        }

        // Vérifier le type de fichier
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Le fichier doit être une image");
        }

        try {
            // Créer le dossier de destination s'il n'existe pas
            Path targetDir = Paths.get(uploadDir, subFolder);
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
                log.info("Dossier créé: {}", targetDir);
            }

            // Générer un nom de fichier unique
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Sauvegarder le fichier
            Path targetPath = targetDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            String relativePath = subFolder + "/" + uniqueFilename;
            log.info("Image sauvegardée localement: {}", relativePath);
            return relativePath;

        } catch (IOException e) {
            log.error("Erreur lors de la sauvegarde de l'image: {}", e.getMessage());
            throw new IOException("Impossible de sauvegarder l'image localement", e);
        }
    }

    /**
     * Sauvegarde une image d'article
     * @param file Fichier image à sauvegarder
     * @return Chemin relatif de l'image sauvegardée
     * @throws IOException En cas d'erreur lors de la sauvegarde
     */
    public String saveArticleImage(MultipartFile file) throws IOException {
        return saveImage(file, "article");
    }

    /**
     * Supprime un fichier local
     * @param relativePath Chemin relatif du fichier à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean deleteFile(String relativePath) {
        try {
            if (relativePath == null || relativePath.isEmpty()) {
                return false;
            }

            Path filePath = Paths.get(uploadDir, relativePath);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Fichier supprimé: {}", relativePath);
                return true;
            } else {
                log.warn("Fichier non trouvé: {}", relativePath);
                return false;
            }
        } catch (IOException e) {
            log.error("Erreur lors de la suppression du fichier {}: {}", relativePath, e.getMessage());
            return false;
        }
    }

    /**
     * Récupère le chemin complet d'un fichier
     * @param relativePath Chemin relatif du fichier
     * @return Chemin complet du fichier
     */
    public Path getFullPath(String relativePath) {
        return Paths.get(uploadDir, relativePath);
    }
}
