package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.dto.LotDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Lot;
import com.tissenza.tissenza_backend.modules.produit.service.LotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lots")
@Tag(name = "Gestion des Lots", description = "API pour la gestion des lots et stocks")
public class LotController {

    @Autowired
    private LotService lotService;

    /**
     * Créer un nouveau lot pour un article
     */
    @PostMapping("/article/{articleId}")
    @Operation(summary = "Créer un lot", description = "Crée un nouveau lot pour un article spécifique")
    public ResponseEntity<LotDTO> createLot(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId,
            @RequestBody Lot lot) {
        Lot createdLot = lotService.createLot(articleId, lot);
        return new ResponseEntity<>(convertToDTO(createdLot), HttpStatus.CREATED);
    }

    /**
     * Lister tous les lots d'un article
     */
    @GetMapping("/article/{articleId}")
    @Operation(summary = "Lister les lots d'un article", description = "Retourne tous les lots d'un article spécifique")
    public ResponseEntity<List<LotDTO>> getLotsByArticle(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        List<Lot> lots = lotService.getLotsByArticle(articleId);
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Lister les lots actifs d'un article
     */
    @GetMapping("/article/{articleId}/actifs")
    @Operation(summary = "Lister les lots actifs d'un article", description = "Retourne les lots actifs d'un article spécifique")
    public ResponseEntity<List<LotDTO>> getLotsActifsByArticle(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        List<Lot> lots = lotService.getLotsActifsByArticle(articleId);
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Trouver un lot par son ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Trouver un lot", description = "Retourne un lot par son ID")
    public ResponseEntity<LotDTO> getLotById(
            @Parameter(description = "ID du lot") @PathVariable Long id) {
        Optional<Lot> lot = lotService.getLotById(id);
        if (lot.isPresent()) {
            return ResponseEntity.ok(convertToDTO(lot.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Trouver un lot par son numéro
     */
    @GetMapping("/numero/{numeroLot}")
    @Operation(summary = "Trouver un lot par numéro", description = "Retourne un lot par son numéro unique")
    public ResponseEntity<LotDTO> getLotByNumero(
            @Parameter(description = "Numéro du lot") @PathVariable String numeroLot) {
        Optional<Lot> lot = lotService.getLotByNumero(numeroLot);
        if (lot.isPresent()) {
            return ResponseEntity.ok(convertToDTO(lot.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Mettre à jour un lot
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un lot", description = "Met à jour les informations d'un lot")
    public ResponseEntity<LotDTO> updateLot(
            @Parameter(description = "ID du lot") @PathVariable Long id,
            @RequestBody Lot lotDetails) {
        try {
            Lot updatedLot = lotService.updateLot(id, lotDetails);
            return ResponseEntity.ok(convertToDTO(updatedLot));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Supprimer un lot
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un lot", description = "Supprime un lot de la base de données")
    public ResponseEntity<Void> deleteLot(
            @Parameter(description = "ID du lot") @PathVariable Long id) {
        try {
            lotService.deleteLot(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Retirer du stock d'un lot
     */
    @PutMapping("/{id}/retirer-stock")
    @Operation(summary = "Retirer du stock", description = "Retire une quantité du stock d'un lot")
    public ResponseEntity<LotDTO> retirerStock(
            @Parameter(description = "ID du lot") @PathVariable Long id,
            @Parameter(description = "Quantité à retirer") @RequestParam Integer quantite,
            @Parameter(description = "Motif du retrait") @RequestParam(required = false) String motif) {
        try {
            Lot updatedLot = lotService.retirerStock(id, quantite, motif);
            return ResponseEntity.ok(convertToDTO(updatedLot));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Ajouter du stock à un lot
     */
    @PutMapping("/{id}/ajouter-stock")
    @Operation(summary = "Ajouter du stock", description = "Ajoute une quantité au stock d'un lot")
    public ResponseEntity<LotDTO> ajouterStock(
            @Parameter(description = "ID du lot") @PathVariable Long id,
            @Parameter(description = "Quantité à ajouter") @RequestParam Integer quantite,
            @Parameter(description = "Motif de l'ajout") @RequestParam(required = false) String motif) {
        try {
            Lot updatedLot = lotService.ajouterStock(id, quantite, motif);
            return ResponseEntity.ok(convertToDTO(updatedLot));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lister les lots proches de la péremption
     */
    @GetMapping("/peremption/proches")
    @Operation(summary = "Lots proches de la péremption", description = "Retourne les lots qui expirent dans les 30 prochains jours")
    public ResponseEntity<List<LotDTO>> getLotsProchesPeremption() {
        List<Lot> lots = lotService.getLotsProchesPeremption();
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Lister les lots périmés
     */
    @GetMapping("/perimes")
    @Operation(summary = "Lots périmés", description = "Retourne les lots périmés")
    public ResponseEntity<List<LotDTO>> getLotsPerimes() {
        List<Lot> lots = lotService.getLotsPerimes();
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Marquer les lots périmés comme tels
     */
    @PostMapping("/marquer-perimes")
    @Operation(summary = "Marquer les lots périmés", description = "Marque automatiquement les lots périmés")
    public ResponseEntity<Void> marquerLotsPerimes() {
        lotService.marquerLotsPerimes();
        return ResponseEntity.ok().build();
    }

    /**
     * Lister les lots épuisés
     */
    @GetMapping("/epuises")
    @Operation(summary = "Lots épuisés", description = "Retourne les lots épuisés")
    public ResponseEntity<List<LotDTO>> getLotsEpuises() {
        List<Lot> lots = lotService.getLotsEpuises();
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Lister les lots par fournisseur
     */
    @GetMapping("/fournisseur/{fournisseur}")
    @Operation(summary = "Lots par fournisseur", description = "Retourne les lots d'un fournisseur spécifique")
    public ResponseEntity<List<LotDTO>> getLotsByFournisseur(
            @Parameter(description = "Nom du fournisseur") @PathVariable String fournisseur) {
        List<Lot> lots = lotService.getLotsByFournisseur(fournisseur);
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Lister les lots par emplacement
     */
    @GetMapping("/emplacement/{emplacement}")
    @Operation(summary = "Lots par emplacement", description = "Retourne les lots d'un emplacement spécifique")
    public ResponseEntity<List<LotDTO>> getLotsByEmplacement(
            @Parameter(description = "Emplacement") @PathVariable String emplacement) {
        List<Lot> lots = lotService.getLotsByEmplacement(emplacement);
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Lister les lots avec stock faible
     */
    @GetMapping("/stock-faible")
    @Operation(summary = "Lots stock faible", description = "Retourne les lots avec stock inférieur au seuil")
    public ResponseEntity<List<LotDTO>> getLotsStockFaible(
            @Parameter(description = "Seuil de stock") @RequestParam(defaultValue = "10") Integer seuil) {
        List<Lot> lots = lotService.getLotsStockFaible(seuil);
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Rechercher des lots par mot-clé
     */
    @GetMapping("/search")
    @Operation(summary = "Rechercher des lots", description = "Recherche des lots par mot-clé")
    public ResponseEntity<List<LotDTO>> searchLots(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<Lot> lots = lotService.searchLots(keyword);
        List<LotDTO> lotDTOs = lots.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lotDTOs);
    }

    /**
     * Obtenir des statistiques sur les lots
     */
    @GetMapping("/statistiques")
    @Operation(summary = "Statistiques des lots", description = "Retourne des statistiques sur les lots")
    public ResponseEntity<LotService.LotStatistics> getStatistics() {
        LotService.LotStatistics stats = lotService.getStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Convertir une entité Lot en LotDTO
     */
    private LotDTO convertToDTO(Lot lot) {
        LotDTO dto = new LotDTO();
        dto.setId(lot.getId());
        dto.setArticleId(lot.getArticle().getId());
        dto.setArticleNom(lot.getArticle().getProduit().getNom());
        dto.setNumeroLot(lot.getNumeroLot());
        dto.setQuantiteInitiale(lot.getQuantiteInitiale());
        dto.setQuantiteRestante(lot.getQuantiteRestante());
        dto.setDateFabrication(lot.getDateFabrication());
        dto.setDateExpiration(lot.getDateExpiration());
        dto.setPrixAchat(lot.getPrixAchat());
        dto.setFournisseur(lot.getFournisseur());
        dto.setEmplacement(lot.getEmplacement());
        dto.setStatut(lot.getStatut().toString());
        dto.setCreatedAt(lot.getCreatedAt());
        dto.setUpdatedAt(lot.getUpdatedAt());
        
        // Champs calculés
        dto.setPerime(lot.estPerime());
        dto.setProchePeremption(lot.estProchePeremption());
        dto.setEpuise(lot.estEpuise());
        dto.setJoursAvantPeremption(calculateJoursAvantPeremption(lot.getDateExpiration()));
        dto.setTauxUtilisation(calculateTauxUtilisation(lot.getQuantiteInitiale(), lot.getQuantiteRestante()));
        
        return dto;
    }

    private Integer calculateJoursAvantPeremption(java.time.LocalDate dateExpiration) {
        if (dateExpiration == null) {
            return null;
        }
        long jours = java.time.temporal.ChronoUnit.DAYS.between(java.time.LocalDate.now(), dateExpiration);
        return (int) Math.max(0, jours);
    }

    private Double calculateTauxUtilisation(Integer quantiteInitiale, Integer quantiteRestante) {
        if (quantiteInitiale == null || quantiteInitiale == 0) {
            return 0.0;
        }
        return ((double) (quantiteInitiale - quantiteRestante) / quantiteInitiale) * 100;
    }
}
