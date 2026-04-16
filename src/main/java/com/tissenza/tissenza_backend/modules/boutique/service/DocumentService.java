package com.tissenza.tissenza_backend.modules.boutique.service;

import com.tissenza.tissenza_backend.modules.boutique.entity.Document;
import com.tissenza.tissenza_backend.modules.boutique.repository.DocumentRepository;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;

    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document updateDocument(Long id, Document documentDetails) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setUrl(documentDetails.getUrl());
                    document.setValidated(documentDetails.getValidated());
                    return documentRepository.save(document);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public List<Document> getDocumentsByPersonne(Personne personne) {
        return documentRepository.findByPersonne(personne);
    }

    public List<Document> getDocumentsByPersonneId(Long personneId) {
        return documentRepository.findByPersonneId(personneId);
    }

    public Optional<Document> getDocumentByPersonneAndType(Personne personne, Document.Type type) {
        return documentRepository.findByPersonneAndType(personne, type);
    }

    public Optional<Document> getDocumentByPersonneIdAndType(Long personneId, Document.Type type) {
        return documentRepository.findByPersonneIdAndType(personneId, type);
    }

    public List<Document> getDocumentsByType(Document.Type type) {
        return documentRepository.findByType(type);
    }

    public List<Document> getDocumentsByValidation(Boolean validated) {
        return documentRepository.findByValidated(validated);
    }

    public List<Document> getDocumentsByPersonneAndValidation(Personne personne, Boolean validated) {
        return documentRepository.findByPersonneAndValidated(personne, validated);
    }

    public List<Document> searchDocumentsByKeyword(String keyword) {
        return documentRepository.searchByKeyword(keyword);
    }

    public long countDocumentsByType(Document.Type type) {
        return documentRepository.countByType(type);
    }

    public long countDocumentsByValidation(Boolean validated) {
        return documentRepository.countByValidated(validated);
    }

    public List<Object[]> getStatisticsByType() {
        return documentRepository.getStatisticsByType();
    }

    public List<Object[]> getStatisticsByValidation() {
        return documentRepository.getStatisticsByValidation();
    }

    public boolean existsByPersonneAndType(Personne personne, Document.Type type) {
        return documentRepository.existsByPersonneAndType(personne, type);
    }

    public Document validateDocument(Long id) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setValidated(true);
                    return documentRepository.save(document);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    public Document rejectDocument(Long id) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setValidated(false);
                    return documentRepository.save(document);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }

    public Document updateDocumentUrl(Long id, String url) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setUrl(url);
                    return documentRepository.save(document);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }
}
