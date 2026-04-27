package com.tissenza.tissenza_backend.modules.boutique.mapper;

import com.tissenza.tissenza_backend.modules.boutique.dto.DocumentDTO;
import com.tissenza.tissenza_backend.modules.boutique.entity.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMapper {

    public DocumentDTO toDTO(Document document) {
        if (document == null) return null;
        
        DocumentDTO dto = new DocumentDTO();
        dto.setId(document.getId());
        dto.setPersonneId(document.getPersonne() != null ? document.getPersonne().getId() : null);
        dto.setType(document.getType());
        dto.setUrl(document.getUrl());
        dto.setValidated(document.getValidated());
        dto.setCreatedAt(document.getCreatedAt());
        
        return dto;
    }

    public Document toEntity(DocumentDTO dto) {
        if (dto == null) return null;
        
        Document document = new Document();
        document.setId(dto.getId());
        document.setType(dto.getType());
        document.setUrl(dto.getUrl());
        document.setValidated(dto.getValidated());
        document.setCreatedAt(dto.getCreatedAt());
        
        // Mapper le personneId vers l'entité Personne
        if (dto.getPersonneId() != null) {
            com.tissenza.tissenza_backend.modules.user.entity.Personne personne = new com.tissenza.tissenza_backend.modules.user.entity.Personne();
            personne.setId(dto.getPersonneId());
            document.setPersonne(personne);
        }
        
        return document;
    }

    public List<DocumentDTO> toDTOList(List<Document> documents) {
        if (documents == null) return null;
        
        return documents.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<Document> toEntityList(List<DocumentDTO> dtos) {
        if (dtos == null) return null;
        
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public void updateEntityFromDTO(DocumentDTO dto, Document document) {
        if (dto == null || document == null) return;
        
        document.setType(dto.getType());
        document.setUrl(dto.getUrl());
        document.setValidated(dto.getValidated());
    }
}
