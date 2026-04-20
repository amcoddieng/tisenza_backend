package com.tissenza.tissenza_backend.modules.boutique.mapper;

import com.tissenza.tissenza_backend.modules.boutique.dto.BoutiqueDTO;
import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir entre Boutique et BoutiqueDTO
 * Évite le LazyInitializationException en extrayant uniquement les champs nécessaires
 */
@Component
public class BoutiqueMapper {

    /**
     * Convertit une entité Boutique en BoutiqueDTO
     * @param boutique l'entité à convertir
     * @return le DTO correspondant
     */
    public BoutiqueDTO toDTO(Boutique boutique) {
        if (boutique == null) {
            return null;
        }

        BoutiqueDTO dto = new BoutiqueDTO();
        dto.setId(boutique.getId());
        dto.setNom(boutique.getNom());
        dto.setDescription(boutique.getDescription());
        dto.setAddresse(boutique.getAddresse());
        dto.setLogo(boutique.getLogo());
        dto.setNote(boutique.getNote());
        dto.setStatut(boutique.getStatut());
        dto.setCreatedAt(boutique.getCreatedAt());

        // Extraction sécurisée des informations du vendeur
        if (boutique.getVendeur() != null) {
            dto.setVendeurId(boutique.getVendeur().getId());
            dto.setVendeurEmail(boutique.getVendeur().getEmail());
            
            // Récupération du nom de la personne associée au vendeur
            if (boutique.getVendeur().getPersonne() != null) {
                dto.setVendeurNom(boutique.getVendeur().getPersonne().getNom() + " " + 
                                   boutique.getVendeur().getPersonne().getPrenom());
            } else {
                dto.setVendeurNom("Nom non disponible");
            }
        } else {
            dto.setVendeurId(null);
            dto.setVendeurEmail("Email non disponible");
            dto.setVendeurNom("Vendeur non disponible");
        }

        return dto;
    }

    /**
     * Convertit un BoutiqueDTO en entité Boutique
     * @param dto le DTO à convertir
     * @return l'entité correspondante
     */
    public Boutique toEntity(BoutiqueDTO dto) {
        if (dto == null) {
            return null;
        }

        Boutique boutique = new Boutique();
        boutique.setId(dto.getId());
        boutique.setNom(dto.getNom());
        boutique.setDescription(dto.getDescription());
        boutique.setAddresse(dto.getAddresse());
        boutique.setLogo(dto.getLogo());
        boutique.setNote(dto.getNote());
        
        // Définir un statut par défaut si non fourni
        if (dto.getStatut() != null) {
            boutique.setStatut(dto.getStatut());
        } else {
            boutique.setStatut(Boutique.Statut.EN_ATTENTE);
        }
        
        // Note: Le vendeur doit être défini séparément via le service
        // pour éviter les problèmes de persistance
        
        return boutique;
    }

    /**
     * Convertit une liste de boutiques en liste de DTOs
     * @param boutiques la liste d'entités
     * @return la liste de DTOs
     */
    public java.util.List<BoutiqueDTO> toDTOList(java.util.List<Boutique> boutiques) {
        if (boutiques == null) {
            return new java.util.ArrayList<>();
        }

        return boutiques.stream()
                .map(this::toDTO)
                .collect(java.util.stream.Collectors.toList());
    }
}
