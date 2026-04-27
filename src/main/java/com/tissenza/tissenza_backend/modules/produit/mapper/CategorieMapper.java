package com.tissenza.tissenza_backend.modules.produit.mapper;

import com.tissenza.tissenza_backend.modules.produit.dto.CategorieDTO;
import com.tissenza.tissenza_backend.modules.produit.dto.SousCategorieDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Categorie;
import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategorieMapper {

    /**
     * Convertit une entité Categorie en CategorieDTO
     * Initialise les relations lazy avant la conversion
     */
    public CategorieDTO toCategorieDTO(Categorie categorie) {
        if (categorie == null) {
            return null;
        }

        CategorieDTO dto = new CategorieDTO();
        dto.setId(categorie.getId());
        dto.setNom(categorie.getNom());
        dto.setDescription(categorie.getDescription());
        dto.setImage(categorie.getImage());
        dto.setCreatedAt(categorie.getCreatedAt());

        // Conversion des sous-catégories si elles sont chargées
        if (categorie.getSousCategories() != null) {
            List<SousCategorieDTO> sousCategoriesDTO = categorie.getSousCategories()
                    .stream()
                    .map(this::toSousCategorieDTO)
                    .collect(Collectors.toList());
            dto.setSousCategories(sousCategoriesDTO);
        }

        return dto;
    }

    /**
     * Convertit une entité SousCategorie en SousCategorieDTO
     * Évite la boucle infinie en ne retournant que l'ID de la catégorie parente
     */
    public SousCategorieDTO toSousCategorieDTO(SousCategorie sousCategorie) {
        return toSousCategorieDTO(sousCategorie, false);
    }

    /**
     * Convertit une entité SousCategorie en SousCategorieDTO
     * @param sousCategorie l'entité à convertir
     * @param includeCategorieInfo si true, inclut les informations de la catégorie
     */
    public SousCategorieDTO toSousCategorieDTO(SousCategorie sousCategorie, boolean includeCategorieInfo) {
        if (sousCategorie == null) {
            return null;
        }

        SousCategorieDTO dto = new SousCategorieDTO();
        dto.setId(sousCategorie.getId());
        dto.setNom(sousCategorie.getNom());
        dto.setDescription(sousCategorie.getDescription());
        dto.setCreatedAt(sousCategorie.getCreatedAt());

        // Retourner l'ID de la catégorie
        if (sousCategorie.getCategorie() != null) {
            dto.setCategorieId(sousCategorie.getCategorie().getId());
            
            // Inclure les informations de la catégorie si demandé
            if (includeCategorieInfo) {
                dto.setCategorieNom(sousCategorie.getCategorie().getNom());
                dto.setCategorieDescription(sousCategorie.getCategorie().getDescription());
            }
        }

        return dto;
    }

    /**
     * Convertit une liste de catégories en liste de DTOs
     */
    public List<CategorieDTO> toCategorieDTOList(List<Categorie> categories) {
        if (categories == null) {
            return null;
        }

        return categories.stream()
                .map(this::toCategorieDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une liste de sous-catégories en liste de DTOs
     */
    public List<SousCategorieDTO> toSousCategorieDTOList(List<SousCategorie> sousCategories) {
        if (sousCategories == null) {
            return null;
        }

        return sousCategories.stream()
                .map(this::toSousCategorieDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertit un CategorieDTO en entité Categorie
     * Utilisé pour la création et la mise à jour
     */
    public Categorie toEntity(CategorieDTO dto) {
        if (dto == null) {
            return null;
        }

        Categorie categorie = new Categorie();
        categorie.setId(dto.getId());
        categorie.setNom(dto.getNom());
        categorie.setDescription(dto.getDescription());
        categorie.setImage(dto.getImage());
        categorie.setCreatedAt(dto.getCreatedAt());

        return categorie;
    }

    /**
     * Convertit un SousCategorieDTO en entité SousCategorie
     * Utilisé pour la création et la mise à jour
     */
    public SousCategorie toEntity(SousCategorieDTO dto) {
        if (dto == null) {
            return null;
        }

        SousCategorie sousCategorie = new SousCategorie();
        sousCategorie.setId(dto.getId());
        sousCategorie.setNom(dto.getNom());
        sousCategorie.setDescription(dto.getDescription());
        sousCategorie.setCreatedAt(dto.getCreatedAt());

        // Note: La relation avec la catégorie doit être gérée séparément
        // car elle nécessite une requête à la base de données

        return sousCategorie;
    }
}
