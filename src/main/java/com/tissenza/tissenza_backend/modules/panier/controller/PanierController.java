package com.tissenza.tissenza_backend.modules.panier.controller;

import com.tissenza.tissenza_backend.modules.panier.dto.PanierDTO;
import com.tissenza.tissenza_backend.modules.panier.dto.AjoutPanierRequest;
import com.tissenza.tissenza_backend.modules.panier.service.PanierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/paniers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestion des Paniers", description = "APIs pour la gestion des paniers d'achat")
public class PanierController {

    private final PanierService panierService;

    /**
     * Récupérer ou créer le panier actif d'un client
     */
    @GetMapping("/client/{clientId}/actif")
    @Operation(summary = "Récupérer le panier actif", description = "Récupère ou crée le panier actif (EN_ATTENTE) d'un client")
    public ResponseEntity<PanierDTO> getPanierActif(
            @Parameter(description = "ID du client") @PathVariable Long clientId) {
        
        try {
            PanierDTO panier = panierService.getOrCreatePanierActif(clientId);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du panier actif pour le client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Ajouter un article au panier
     */
    @PostMapping("/client/{clientId}/ajouter")
    @Operation(summary = "Ajouter un article au panier", description = "Ajoute un article au panier actif d'un client")
    public ResponseEntity<PanierDTO> ajouterArticle(
            @Parameter(description = "ID du client") @PathVariable Long clientId,
            @RequestBody AjoutPanierRequest request) {
        
        try {
            PanierDTO panier = panierService.ajouterArticleAuPanier(clientId, request);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            log.error("Erreur lors de l'ajout d'article au panier pour le client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Mettre à jour la quantité d'un article dans le panier
     */
    @PutMapping("/{panierId}/article/{articleId}/quantite")
    @Operation(summary = "Mettre à jour la quantité", description = "Met à jour la quantité d'un article dans le panier")
    public ResponseEntity<PanierDTO> mettreAJourQuantite(
            @Parameter(description = "ID du panier") @PathVariable Long panierId,
            @Parameter(description = "ID de l'article") @PathVariable Long articleId,
            @Parameter(description = "Nouvelle quantité") @RequestParam Integer quantite) {
        
        try {
            PanierDTO panier = panierService.mettreAJourQuantite(panierId, articleId, quantite);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de la quantité pour le panier {} et article {}: {}", 
                    panierId, articleId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Supprimer un article du panier
     */
    @DeleteMapping("/{panierId}/article/{articleId}")
    @Operation(summary = "Supprimer un article", description = "Supprime un article du panier")
    public ResponseEntity<PanierDTO> supprimerArticle(
            @Parameter(description = "ID du panier") @PathVariable Long panierId,
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        
        try {
            PanierDTO panier = panierService.supprimerArticleDuPanier(panierId, articleId);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de l'article pour le panier {} et article {}: {}", 
                    panierId, articleId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Vider le panier
     */
    @DeleteMapping("/{panierId}/vider")
    @Operation(summary = "Vider le panier", description = "Supprime tous les articles du panier")
    public ResponseEntity<Void> viderPanier(
            @Parameter(description = "ID du panier") @PathVariable Long panierId) {
        
        try {
            panierService.viderPanier(panierId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erreur lors du vidage du panier {}: {}", panierId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Valider le panier
     */
    @PostMapping("/{panierId}/valider")
    @Operation(summary = "Valider le panier", description = "Change le statut du panier à VALIDE")
    public ResponseEntity<PanierDTO> validerPanier(
            @Parameter(description = "ID du panier") @PathVariable Long panierId) {
        
        try {
            PanierDTO panier = panierService.validerPanier(panierId);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            log.error("Erreur lors de la validation du panier {}: {}", panierId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Récupérer un panier par ID
     */
    @GetMapping("/{panierId}")
    @Operation(summary = "Récupérer un panier", description = "Récupère les détails d'un panier spécifique")
    public ResponseEntity<PanierDTO> getPanierById(
            @Parameter(description = "ID du panier") @PathVariable Long panierId) {
        
        try {
            PanierDTO panier = panierService.getPanierById(panierId);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du panier {}: {}", panierId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Récupérer tous les paniers d'un client
     */
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Récupérer les paniers d'un client", description = "Récupère tous les paniers d'un client")
    public ResponseEntity<List<PanierDTO>> getPaniersByClient(
            @Parameter(description = "ID du client") @PathVariable Long clientId) {
        
        try {
            List<PanierDTO> paniers = panierService.getPaniersByClient(clientId);
            return ResponseEntity.ok(paniers);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des paniers pour le client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
