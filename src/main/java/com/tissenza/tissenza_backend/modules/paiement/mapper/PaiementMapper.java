package com.tissenza.tissenza_backend.modules.paiement.mapper;

import com.tissenza.tissenza_backend.modules.paiement.dto.MoyenPaiementDTO;
import com.tissenza.tissenza_backend.modules.paiement.dto.UserMoyenPaiementDTO;
import com.tissenza.tissenza_backend.modules.paiement.entity.MoyenPaiement;
import com.tissenza.tissenza_backend.modules.paiement.entity.UserMoyenPaiement;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper pour les entités du module paiement
 */
@Component
public class PaiementMapper {

    /**
     * Convertit un MoyenPaiement en MoyenPaiementDTO
     */
    public MoyenPaiementDTO toDTO(MoyenPaiement moyenPaiement) {
        if (moyenPaiement == null) {
            return null;
        }

        MoyenPaiementDTO dto = new MoyenPaiementDTO();
        dto.setId(moyenPaiement.getId());
        dto.setNom(moyenPaiement.getNom());
        dto.setPhoto(moyenPaiement.getPhoto());
        dto.setCreatedAt(moyenPaiement.getCreatedAt());

        return dto;
    }

    /**
     * Convertit un MoyenPaiementDTO en MoyenPaiement
     */
    public MoyenPaiement toEntity(MoyenPaiementDTO dto) {
        if (dto == null) {
            return null;
        }

        MoyenPaiement moyenPaiement = new MoyenPaiement();
        moyenPaiement.setId(dto.getId());
        moyenPaiement.setNom(dto.getNom());
        moyenPaiement.setPhoto(dto.getPhoto());
        moyenPaiement.setCreatedAt(dto.getCreatedAt());

        return moyenPaiement;
    }

    /**
     * Convertit une liste de MoyenPaiement en liste de MoyenPaiementDTO
     */
    public List<MoyenPaiementDTO> toMoyenPaiementDTOList(List<MoyenPaiement> moyenPaiements) {
        if (moyenPaiements == null) {
            return List.of();
        }
        return moyenPaiements.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit un UserMoyenPaiement en UserMoyenPaiementDTO
     */
    public UserMoyenPaiementDTO toDTO(UserMoyenPaiement userMoyenPaiement) {
        if (userMoyenPaiement == null) {
            return null;
        }

        UserMoyenPaiementDTO dto = new UserMoyenPaiementDTO();
        dto.setId(userMoyenPaiement.getId());
        dto.setActif(userMoyenPaiement.getActif());
        dto.setNumero(userMoyenPaiement.getNumero());
        dto.setCreatedAt(userMoyenPaiement.getCreatedAt());

        // Informations sur l'utilisateur
        Compte user = userMoyenPaiement.getUser();
        if (user != null) {
            dto.setUserId(user.getId());
            dto.setUserEmail(user.getEmail());
            
            // Récupérer le nom de la personne associée
            Personne personne = user.getPersonne();
            if (personne != null) {
                dto.setUserNom(personne.getNom() + " " + personne.getPrenom());
            }
        }

        // Informations sur le moyen de paiement
        MoyenPaiement moyenPaiement = userMoyenPaiement.getMoyenPaiement();
        if (moyenPaiement != null) {
            dto.setMoyenPaiementId(moyenPaiement.getId());
            dto.setMoyenPaiementNom(moyenPaiement.getNom());
            dto.setMoyenPaiementPhoto(moyenPaiement.getPhoto());
        }

        return dto;
    }

    /**
     * Convertit un UserMoyenPaiementDTO en UserMoyenPaiement
     */
    public UserMoyenPaiement toEntity(UserMoyenPaiementDTO dto) {
        if (dto == null) {
            return null;
        }

        UserMoyenPaiement userMoyenPaiement = new UserMoyenPaiement();
        userMoyenPaiement.setId(dto.getId());
        userMoyenPaiement.setActif(dto.getActif());
        userMoyenPaiement.setNumero(dto.getNumero());
        userMoyenPaiement.setCreatedAt(dto.getCreatedAt());

        // Note: Les relations user et moyenPaiement doivent être définies séparément
        // dans le service pour éviter les problèmes de persistance

        return userMoyenPaiement;
    }

    /**
     * Convertit une liste de UserMoyenPaiement en liste de UserMoyenPaiementDTO
     */
    public List<UserMoyenPaiementDTO> toUserMoyenPaiementDTOList(List<UserMoyenPaiement> userMoyenPaiements) {
        if (userMoyenPaiements == null) {
            return List.of();
        }
        return userMoyenPaiements.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Crée un UserMoyenPaiement à partir des IDs
     */
    public UserMoyenPaiement createAssociation(Long userId, Long moyenPaiementId, Boolean actif) {
        UserMoyenPaiement association = new UserMoyenPaiement();
        
        // Créer des objets partiels avec seulement les IDs
        Compte user = new Compte();
        user.setId(userId);
        
        MoyenPaiement moyenPaiement = new MoyenPaiement();
        moyenPaiement.setId(moyenPaiementId);
        
        association.setUser(user);
        association.setMoyenPaiement(moyenPaiement);
        association.setActif(actif != null ? actif : true);
        
        return association;
    }
}
