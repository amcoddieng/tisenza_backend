package com.tissenza.tissenza_backend.modules.boutique.service;

import com.tissenza.tissenza_backend.modules.boutique.dto.DocumentDTO;
import com.tissenza.tissenza_backend.modules.boutique.entity.Document;
import com.tissenza.tissenza_backend.modules.boutique.mapper.DocumentMapper;
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
    private final DocumentMapper documentMapper;

    public Document createDocument(Document document) {
        return documentRepository.save(document);
    }

    @Transactional
    public DocumentDTO createDocumentDTO(DocumentDTO documentDTO) {
        Document document = documentMapper.toEntity(documentDTO);
        Document savedDocument = documentRepository.save(document);
        return documentMapper.toDTO(savedDocument);
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<DocumentDTO> getDocumentByIdDTO(Long id) {
        return documentRepository.findById(id)
                .map(documentMapper::toDTO);
    }

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> getAllDocumentsDTO() {
        List<Document> documents = documentRepository.findAll();
        return documentMapper.toDTOList(documents);
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

    @Transactional
    public DocumentDTO updateDocumentDTO(Long id, DocumentDTO documentDTO) {
        return documentRepository.findById(id)
                .map(document -> {
                    documentMapper.updateEntityFromDTO(documentDTO, document);
                    Document updatedDocument = documentRepository.save(document);
                    return documentMapper.toDTO(updatedDocument);
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

    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByPersonneIdDTO(Long personneId) {
        List<Document> documents = documentRepository.findByPersonneId(personneId);
        return documentMapper.toDTOList(documents);
    }

    public Optional<Document> getDocumentByPersonneAndType(Personne personne, Document.Type type) {
        return documentRepository.findByPersonneAndType(personne, type);
    }

    public Optional<Document> getDocumentByPersonneIdAndType(Long personneId, Document.Type type) {
        return documentRepository.findByPersonneIdAndType(personneId, type);
    }

    @Transactional(readOnly = true)
    public Optional<DocumentDTO> getDocumentByPersonneIdAndTypeDTO(Long personneId, Document.Type type) {
        return documentRepository.findByPersonneIdAndType(personneId, type)
                .map(documentMapper::toDTO);
    }

    public List<Document> getDocumentsByType(Document.Type type) {
        return documentRepository.findByType(type);
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByTypeDTO(Document.Type type) {
        List<Document> documents = documentRepository.findByType(type);
        return documentMapper.toDTOList(documents);
    }

    public List<Document> getDocumentsByValidation(Boolean validated) {
        return documentRepository.findByValidated(validated);
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> getDocumentsByValidationDTO(Boolean validated) {
        List<Document> documents = documentRepository.findByValidated(validated);
        return documentMapper.toDTOList(documents);
    }

    public List<Document> getDocumentsByPersonneAndValidation(Personne personne, Boolean validated) {
        return documentRepository.findByPersonneAndValidated(personne, validated);
    }

    public List<Document> searchDocumentsByKeyword(String keyword) {
        return documentRepository.searchByKeyword(keyword);
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> searchDocumentsByKeywordDTO(String keyword) {
        List<Document> documents = documentRepository.searchByKeyword(keyword);
        return documentMapper.toDTOList(documents);
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

    @Transactional
    public DocumentDTO validateDocumentDTO(Long id) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setValidated(true);
                    Document updatedDocument = documentRepository.save(document);
                    return documentMapper.toDTO(updatedDocument);
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

    @Transactional
    public DocumentDTO rejectDocumentDTO(Long id) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setValidated(false);
                    Document updatedDocument = documentRepository.save(document);
                    return documentMapper.toDTO(updatedDocument);
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

    @Transactional
    public DocumentDTO updateDocumentUrlDTO(Long id, String url) {
        return documentRepository.findById(id)
                .map(document -> {
                    document.setUrl(url);
                    Document updatedDocument = documentRepository.save(document);
                    return documentMapper.toDTO(updatedDocument);
                })
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
    }
}
