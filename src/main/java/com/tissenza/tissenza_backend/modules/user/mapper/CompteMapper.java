package com.tissenza.tissenza_backend.modules.user.mapper;

import com.tissenza.tissenza_backend.modules.user.dto.CompteResponseDTO;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Compte et CompteResponseDTO
 * Évite le LazyInitializationException en extrayant uniquement les champs nécessaires
 */
@Component
public class CompteMapper {

    /**
     * Convertit une entité Compte en CompteResponseDTO
     * @param compte l'entité à convertir
     * @return le DTO correspondant
     */
    public CompteResponseDTO toDTO(Compte compte) {
        if (compte == null) {
            return null;
        }

        CompteResponseDTO dto = new CompteResponseDTO();
        dto.setId(compte.getId());
        dto.setEmail(compte.getEmail());
        dto.setTelephone(compte.getTelephone());
        dto.setRole(compte.getRole());
        dto.setStatut(compte.getStatut());
        dto.setIsVerified(compte.getIsVerified());
        dto.setLastLogin(compte.getLastLogin());
        dto.setCreatedAt(compte.getCreatedAt());

        // Extraction sécurisée des informations de la personne
        if (compte.getPersonne() != null) {
            dto.setPersonneId(compte.getPersonne().getId());
            dto.setPersonneNom(compte.getPersonne().getNom());
            dto.setPersonnePrenom(compte.getPersonne().getPrenom());
            dto.setPersonnePhoto(compte.getPersonne().getPhotoProfil());
            dto.setPersonneVille(compte.getPersonne().getVille());
        } else {
            dto.setPersonneId(null);
            dto.setPersonneNom("Nom non disponible");
            dto.setPersonnePrenom("Prénom non disponible");
            dto.setPersonnePhoto(null);
            dto.setPersonneVille("Ville non disponible");
        }

        return dto;
    }

    /**
     * Convertit un CompteResponseDTO en entité Compte
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Compte toEntity(CompteResponseDTO dto) {
        if (dto == null) {
            return null;
        }

        Compte compte = new Compte();
        compte.setId(dto.getId());
        compte.setEmail(dto.getEmail());
        compte.setTelephone(dto.getTelephone());
        compte.setRole(dto.getRole());
        compte.setStatut(dto.getStatut());
        compte.setIsVerified(dto.getIsVerified());
        compte.setLastLogin(dto.getLastLogin());
        
        // Note: La personne doit être définie séparément via le service
        // pour éviter les problèmes de persistance
        
        return compte;
    }

    /**
     * Convertit une liste de comptes en liste de DTOs
     * @param comptes la liste d'entités
     * @return la liste de DTOs
     */
    public java.util.List<CompteResponseDTO> toDTOList(java.util.List<Compte> comptes) {
        if (comptes == null) {
            return new java.util.ArrayList<>();
        }

        return comptes.stream()
                .map(this::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
