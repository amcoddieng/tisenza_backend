package com.tissenza.tissenza_backend.modules.boutique.controller;

import com.tissenza.tissenza_backend.modules.boutique.dto.DocumentDTO;
import com.tissenza.tissenza_backend.modules.boutique.entity.Document;
import com.tissenza.tissenza_backend.modules.boutique.service.DocumentService;
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
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Document Management", description = "API pour la gestion des documents")
public class DocumentController {

    private final DocumentService documentService;
    private final LocalStorageService localStorageService;

    @PostMapping
    @Operation(summary = "Créer un nouveau document", description = "Crée un nouveau document dans le système")
    public ResponseEntity<DocumentDTO> createDocument(@RequestBody DocumentDTO documentDTO) {
        DocumentDTO createdDocument = documentService.createDocumentDTO(documentDTO);
        return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
    }

    /**
     * Créer un document avec upload de fichier
     */
    @PostMapping("/with-file")
    @Operation(summary = "Créer un document avec fichier", description = "Crée un nouveau document avec stockage de fichier en local")
    public ResponseEntity<DocumentDTO> createDocumentWithFile(
            @RequestParam("personneId") Long personneId,
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Stocker le fichier en local
            String fileUrl = localStorageService.storeFile(file, LocalStorageService.FileType.IMG_DOC);
            log.info("Fichier stocké en local: {}", fileUrl);
            
            // Créer le DTO du document avec l'URL du fichier
            DocumentDTO documentDTO = new DocumentDTO();
            documentDTO.setPersonneId(personneId);
            documentDTO.setType(com.tissenza.tissenza_backend.modules.boutique.entity.Document.Type.valueOf(type));
            documentDTO.setUrl(fileUrl);
            documentDTO.setValidated(false);
            
            DocumentDTO createdDocument = documentService.createDocumentDTO(documentDTO);
            return new ResponseEntity<>(createdDocument, HttpStatus.CREATED);
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload du fichier: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Erreur lors de la création du document: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un document par ID", description = "Retourne les détails d'un document spécifique")
    public ResponseEntity<DocumentDTO> getDocumentById(
            @Parameter(description = "ID du document à récupérer") @PathVariable Long id) {
        return documentService.getDocumentByIdDTO(id)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les documents", description = "Retourne la liste de tous les documents")
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<DocumentDTO> documents = documentService.getAllDocumentsDTO();
        return ResponseEntity.ok(documents);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un document", description = "Met à jour les informations d'un document existant")
    public ResponseEntity<DocumentDTO> updateDocument(
            @Parameter(description = "ID du document à mettre à jour") @PathVariable Long id,
            @RequestBody DocumentDTO documentDTO) {
        try {
            DocumentDTO updatedDocument = documentService.updateDocumentDTO(id, documentDTO);
            return ResponseEntity.ok(updatedDocument);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mettre à jour le fichier d'un document
     */
    @PostMapping("/{id}/file")
    @Operation(summary = "Mettre à jour le fichier", description = "Stocke et met à jour le fichier d'un document existant en local")
    public ResponseEntity<Document> updateFile(
            @Parameter(description = "ID du document") @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Stocker le fichier en local
            String fileUrl = localStorageService.storeFile(file, LocalStorageService.FileType.IMG_DOC);
            log.info("Fichier stocké en local: {}", fileUrl);
            
            // Mettre à jour le document avec la nouvelle URL
            Document documentDetails = new Document();
            documentDetails.setUrl(fileUrl);
            
            Document updatedDocument = documentService.updateDocument(id, documentDetails);
            return ResponseEntity.ok(updatedDocument);
            
        } catch (IOException e) {
            log.error("Erreur lors de l'upload du fichier: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du fichier: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un document", description = "Supprime un document du système")
    public ResponseEntity<Void> deleteDocument(
            @Parameter(description = "ID du document à supprimer") @PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/personne/{personneId}")
    @Operation(summary = "Récupérer les documents d'une personne", description = "Retourne la liste des documents par ID personne")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByPersonneId(
            @Parameter(description = "ID de la personne") @PathVariable Long personneId) {
        List<DocumentDTO> documents = documentService.getDocumentsByPersonneIdDTO(personneId);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/personne/{personneId}/type/{type}")
    @Operation(summary = "Récupérer un document par personne et type", description = "Retourne un document spécifique par personne et type")
    public ResponseEntity<DocumentDTO> getDocumentByPersonneIdAndType(
            @Parameter(description = "ID de la personne") @PathVariable Long personneId,
            @Parameter(description = "Type de document") @PathVariable Document.Type type) {
        return documentService.getDocumentByPersonneIdAndTypeDTO(personneId, type)
                .map(document -> ResponseEntity.ok(document))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Récupérer des documents par type", description = "Retourne la liste des documents par type")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByType(
            @Parameter(description = "Type de document") @PathVariable Document.Type type) {
        List<DocumentDTO> documents = documentService.getDocumentsByTypeDTO(type);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/validated/{validated}")
    @Operation(summary = "Récupérer des documents par validation", description = "Retourne la liste des documents par statut de validation")
    public ResponseEntity<List<DocumentDTO>> getDocumentsByValidation(
            @Parameter(description = "Statut de validation") @PathVariable Boolean validated) {
        List<DocumentDTO> documents = documentService.getDocumentsByValidationDTO(validated);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/personne/{personneId}/validated/{validated}")
    @Operation(summary = "Récupérer les documents d'une personne par validation", description = "Retourne les documents d'une personne par statut de validation")
    public ResponseEntity<List<Document>> getDocumentsByPersonneAndValidation(
            @Parameter(description = "ID de la personne") @PathVariable Long personneId,
            @Parameter(description = "Statut de validation") @PathVariable Boolean validated) {
        List<Document> documents = documentService.getDocumentsByPersonneId(personneId);
        List<Document> filtered = documents.stream()
                .filter(doc -> doc.getValidated().equals(validated))
                .toList();
        return ResponseEntity.ok(filtered);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des documents par mot-clé dans l'URL")
    public ResponseEntity<List<DocumentDTO>> searchDocumentsByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<DocumentDTO> documents = documentService.searchDocumentsByKeywordDTO(keyword);
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/count/type/{type}")
    @Operation(summary = "Compter par type", description = "Compte le nombre de documents par type")
    public ResponseEntity<Long> countDocumentsByType(
            @Parameter(description = "Type pour le comptage") @PathVariable Document.Type type) {
        long count = documentService.countDocumentsByType(type);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/validated/{validated}")
    @Operation(summary = "Compter par validation", description = "Compte le nombre de documents par statut de validation")
    public ResponseEntity<Long> countDocumentsByValidation(
            @Parameter(description = "Statut de validation pour le comptage") @PathVariable Boolean validated) {
        long count = documentService.countDocumentsByValidation(validated);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/type")
    @Operation(summary = "Statistiques par type", description = "Retourne les statistiques des documents par type")
    public ResponseEntity<List<Object[]>> getStatisticsByType() {
        List<Object[]> stats = documentService.getStatisticsByType();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/stats/validation")
    @Operation(summary = "Statistiques par validation", description = "Retourne les statistiques des documents par validation")
    public ResponseEntity<List<Object[]>> getStatisticsByValidation() {
        List<Object[]> stats = documentService.getStatisticsByValidation();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/exists/personne/{personneId}/type/{type}")
    @Operation(summary = "Vérifier l'existence par personne et type", description = "Vérifie si un document existe pour une personne et un type")
    public ResponseEntity<Boolean> existsByPersonneAndType(
            @Parameter(description = "ID de la personne") @PathVariable Long personneId,
            @Parameter(description = "Type de document") @PathVariable Document.Type type) {
        boolean exists = documentService.getDocumentByPersonneIdAndType(personneId, type).isPresent();
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/{id}/validate")
    @Operation(summary = "Valider un document", description = "Marque un document comme validé")
    public ResponseEntity<DocumentDTO> validateDocument(
            @Parameter(description = "ID du document à valider") @PathVariable Long id) {
        try {
            DocumentDTO validatedDocument = documentService.validateDocumentDTO(id);
            return ResponseEntity.ok(validatedDocument);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Rejeter un document", description = "Marque un document comme non validé")
    public ResponseEntity<DocumentDTO> rejectDocument(
            @Parameter(description = "ID du document à rejeter") @PathVariable Long id) {
        try {
            DocumentDTO rejectedDocument = documentService.rejectDocumentDTO(id);
            return ResponseEntity.ok(rejectedDocument);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/url")
    @Operation(summary = "Mettre à jour l'URL", description = "Met à jour l'URL d'un document")
    public ResponseEntity<DocumentDTO> updateDocumentUrl(
            @Parameter(description = "ID du document") @PathVariable Long id,
            @Parameter(description = "Nouvelle URL") @RequestParam String url) {
        try {
            DocumentDTO updatedDocument = documentService.updateDocumentUrlDTO(id, url);
            return ResponseEntity.ok(updatedDocument);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
