package com.tissenza.tissenza_backend.modules.panier.mapper;

import com.tissenza.tissenza_backend.modules.panier.dto.PanierDTO;
import com.tissenza.tissenza_backend.modules.panier.dto.PanierItemDTO;
import com.tissenza.tissenza_backend.modules.panier.entity.Panier;
import com.tissenza.tissenza_backend.modules.panier.entity.PanierItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PanierMapper {

    /**
     * Convertit une entité Panier en PanierDTO
     */
    public PanierDTO toDTO(Panier panier) {
        if (panier == null) {
            return null;
        }

        PanierDTO dto = new PanierDTO();
        dto.setId(panier.getId());
        dto.setDateCreation(panier.getDateCreation());
        dto.setUpdatedDate(panier.getUpdatedDate());
        dto.setStatus(panier.getStatus());
        dto.setTotal(panier.getTotal());

        // Mapper les relations
        if (panier.getClient() != null) {
            dto.setClientId(panier.getClient().getId());
        }

        // Mapper les items
        if (panier.getItems() != null) {
            List<PanierItemDTO> itemDTOs = panier.getItems().stream()
                    .map(this::mapPanierItemToDTO)
                    .collect(Collectors.toList());
            dto.setItems(itemDTOs);
        }

        return dto;
    }

    /**
     * Convertit une entité PanierItem en PanierItemDTO
     */
    private PanierItemDTO mapPanierItemToDTO(PanierItem item) {
        if (item == null) {
            return null;
        }

        PanierItemDTO dto = new PanierItemDTO();
        dto.setId(item.getId());
        dto.setQuantite(item.getQuantite());
        dto.setPrixUnitaire(item.getPrixUnitaire());
        dto.setSousTotal(item.getSousTotal());
        dto.setDate(item.getDate());

        // Mapper les relations
        if (item.getPanier() != null) {
            dto.setPanierId(item.getPanier().getId());
        }

        if (item.getArticle() != null) {
            dto.setArticleId(item.getArticle().getId());
            dto.setArticleSku(item.getArticle().getSku());
            
            // Récupérer les infos du produit via l'article
            if (item.getArticle().getProduit() != null) {
                dto.setArticleNom(item.getArticle().getProduit().getNom());
                dto.setArticleImage(item.getArticle().getProduit().getImage());
            }
        }

        return dto;
    }

    /**
     * Convertit une liste de Panier en liste de PanierDTO
     */
    public List<PanierDTO> toDTOList(List<Panier> paniers) {
        if (paniers == null) {
            return null;
        }

        return paniers.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
