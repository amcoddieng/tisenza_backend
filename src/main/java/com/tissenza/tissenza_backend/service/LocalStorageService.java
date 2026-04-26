package com.tissenza.tissenza_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService {

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    private Path rootLocation;

    // Types de dossiers supportés
    public enum FileType {
        PROFIL("profil"),
        IMG_DOC("img_doc"),
        PRODUIT("produit"),
        ARTICLE("article");

        private final String folderName;

        FileType(String folderName) {
            this.folderName = folderName;
        }

        public String getFolderName() {
            return folderName;
        }
    }

    public LocalStorageService() {
        this.rootLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
            // Créer tous les sous-dossiers
            for (FileType type : FileType.values()) {
                Files.createDirectories(rootLocation.resolve(type.getFolderName()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String storeFile(MultipartFile file, FileType fileType) throws IOException {
        // Créer le répertoire s'il n'existe pas
        Path targetDirectory = rootLocation.resolve(fileType.getFolderName());
        try {
            Files.createDirectories(targetDirectory);
        } catch (IOException e) {
            throw new IOException("Could not create upload directory: " + targetDirectory, e);
        }

        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".") 
            ? originalFilename.substring(originalFilename.lastIndexOf(".")) 
            : "";
        
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = targetDirectory.resolve(uniqueFilename);

        // Copier le fichier
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Retourner le chemin relatif pour l'accès web
        return "/uploads/" + fileType.getFolderName() + "/" + uniqueFilename;
    }

    // Méthode pour compatibilité avec le code existant
    public String storeFile(MultipartFile file) throws IOException {
        return storeFile(file, FileType.PROFIL); // Par défaut, stocker dans profil
    }

    public void deleteFile(String filePath) throws IOException {
        if (filePath != null && filePath.startsWith("/uploads/")) {
            String relativePath = filePath.substring("/uploads/".length());
            Path fileToDelete = rootLocation.resolve(relativePath);
            Files.deleteIfExists(fileToDelete);
        }
    }

    public Path getRootLocation() {
        return rootLocation;
    }

    public Path getDirectory(FileType fileType) {
        return rootLocation.resolve(fileType.getFolderName());
    }
}
