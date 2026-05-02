package com.tissenza.tissenza_backend.modules.commande.mapper;

import com.tissenza.tissenza_backend.modules.commande.dto.CommandeDTO;
import com.tissenza.tissenza_backend.modules.commande.dto.DetailCommandeDTO;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.commande.entity.DetailCommande;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommandeMapper {

    /**
     * Convertit une entité Commande en CommandeDTO
     */
    public CommandeDTO toDTO(Commande commande) {
        if (commande == null) {
            return null;
        }

        CommandeDTO dto = new CommandeDTO();
        dto.setId(commande.getId());
        dto.setDate(commande.getDate());
        dto.setStatus(commande.getStatus());
        dto.setTotal(commande.getTotal());
        dto.setStatusPaiement(commande.getStatusPaiement());

        // Mapper les relations
        if (commande.getClient() != null) {
            dto.setClientId(commande.getClient().getId());
        }

        if (commande.getPanier() != null) {
            dto.setPanierId(commande.getPanier().getId());
        }

        // Mapper les détails
        if (commande.getDetails() != null) {
            List<DetailCommandeDTO> detailDTOs = commande.getDetails().stream()
                    .map(this::mapDetailCommandeToDTO)
                    .collect(Collectors.toList());
            dto.setDetails(detailDTOs);
        }

        return dto;
    }

    /**
     * Convertit une entité DetailCommande en DetailCommandeDTO
     */
    private DetailCommandeDTO mapDetailCommandeToDTO(DetailCommande detail) {
        if (detail == null) {
            return null;
        }

        DetailCommandeDTO dto = new DetailCommandeDTO();
        dto.setId(detail.getId());
        dto.setQuantite(detail.getQuantite());
        dto.setPrixUnitaire(detail.getPrixUnitaire());
        dto.setSousTotal(detail.getSousTotal());
        dto.setDate(detail.getDate());

        // Mapper les relations
        if (detail.getCommande() != null) {
            dto.setCommandeId(detail.getCommande().getId());
        }

        if (detail.getArticle() != null) {
            dto.setArticleId(detail.getArticle().getId());
            dto.setArticleSku(detail.getArticle().getSku());
            
            // Récupérer les infos du produit via l'article
            if (detail.getArticle().getProduit() != null) {
                dto.setArticleNom(detail.getArticle().getProduit().getNom());
                dto.setArticleImage(detail.getArticle().getProduit().getImage());
            }
        }

        return dto;
    }

    /**
     * Convertit une liste de Commande en liste de CommandeDTO
     */
    public List<CommandeDTO> toDTOList(List<Commande> commandes) {
        if (commandes == null) {
            return null;
        }

        return commandes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
