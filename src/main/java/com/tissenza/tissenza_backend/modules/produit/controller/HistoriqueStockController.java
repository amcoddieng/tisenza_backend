package com.tissenza.tissenza_backend.modules.produit.controller;

import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import com.tissenza.tissenza_backend.modules.produit.service.HistoriqueStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/historique-stock")
@RequiredArgsConstructor
@Tag(name = "HistoriqueStock Management", description = "API pour la gestion de l'historique des stocks")
public class HistoriqueStockController {

    private final HistoriqueStockService historiqueStockService;

    @PostMapping
    @Operation(summary = "Créer un nouvel historique de stock", description = "Crée un nouvel enregistrement d'historique de stock")
    public ResponseEntity<HistoriqueStock> createHistoriqueStock(@RequestBody HistoriqueStock historiqueStock) {
        HistoriqueStock createdHistorique = historiqueStockService.createHistoriqueStock(historiqueStock);
        return new ResponseEntity<>(createdHistorique, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un historique par ID", description = "Retourne les détails d'un historique spécifique")
    public ResponseEntity<HistoriqueStock> getHistoriqueStockById(
            @Parameter(description = "ID de l'historique à récupérer") @PathVariable Long id) {
        return historiqueStockService.getHistoriqueStockById(id)
                .map(historique -> ResponseEntity.ok(historique))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les historiques", description = "Retourne la liste de tous les historiques de stock")
    public ResponseEntity<List<HistoriqueStock>> getAllHistoriqueStock() {
        List<HistoriqueStock> historiques = historiqueStockService.getAllHistoriqueStock();
        return ResponseEntity.ok(historiques);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un historique", description = "Supprime un historique de stock du système")
    public ResponseEntity<Void> deleteHistoriqueStock(
            @Parameter(description = "ID de l'historique à supprimer") @PathVariable Long id) {
        historiqueStockService.deleteHistoriqueStock(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/article/{articleId}")
    @Operation(summary = "Récupérer les historiques d'un article", description = "Retourne la liste des historiques par ID article")
    public ResponseEntity<List<HistoriqueStock>> getHistoriqueStockByArticleId(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        List<HistoriqueStock> historiques = historiqueStockService.getHistoriqueStockByArticleId(articleId);
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Récupérer les historiques par type", description = "Retourne la liste des historiques par type (ENTREE/SORTIE)")
    public ResponseEntity<List<HistoriqueStock>> getHistoriqueStockByType(
            @Parameter(description = "Type de mouvement") @PathVariable HistoriqueStock.Type type) {
        List<HistoriqueStock> historiques = historiqueStockService.getHistoriqueStockByType(type);
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/article/{articleId}/type/{type}")
    @Operation(summary = "Récupérer les historiques par article et type", description = "Retourne les historiques par article et type")
    public ResponseEntity<List<HistoriqueStock>> getHistoriqueStockByArticleIdAndType(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId,
            @Parameter(description = "Type de mouvement") @PathVariable HistoriqueStock.Type type) {
        List<HistoriqueStock> historiques = historiqueStockService.getHistoriqueStockByArticleIdAndType(articleId, type);
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Récupérer les historiques par plage de dates", description = "Retourne les historiques dans une plage de dates")
    public ResponseEntity<List<HistoriqueStock>> getHistoriqueStockByDateRange(
            @Parameter(description = "Date de début") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Date de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<HistoriqueStock> historiques = historiqueStockService.getHistoriqueStockByDateRange(startDate, endDate);
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/article/{articleId}/date-range")
    @Operation(summary = "Récupérer les historiques par article et plage de dates", description = "Retourne les historiques d'un article dans une plage de dates")
    public ResponseEntity<List<HistoriqueStock>> getHistoriqueStockByArticleIdAndDateRange(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId,
            @Parameter(description = "Date de début") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Date de fin") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<HistoriqueStock> historiques = historiqueStockService.getHistoriqueStockByArticleIdAndDateRange(articleId, startDate, endDate);
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/search/motif")
    @Operation(summary = "Rechercher par motif", description = "Recherche des historiques par motif")
    public ResponseEntity<List<HistoriqueStock>> searchHistoriqueStockByMotif(
            @Parameter(description = "Motif à rechercher") @RequestParam String keyword) {
        List<HistoriqueStock> historiques = historiqueStockService.searchHistoriqueStockByMotif(keyword);
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/count/article/{articleId}")
    @Operation(summary = "Compter par article", description = "Compte le nombre d'historiques par article")
    public ResponseEntity<Long> countByArticleId(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        long count = historiqueStockService.countByArticleId(articleId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/type/{type}")
    @Operation(summary = "Compter par type", description = "Compte le nombre d'historiques par type")
    public ResponseEntity<Long> countByType(
            @Parameter(description = "Type de mouvement") @PathVariable HistoriqueStock.Type type) {
        long count = historiqueStockService.countByType(type);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/stats/type")
    @Operation(summary = "Statistiques par type", description = "Retourne les statistiques des historiques par type")
    public ResponseEntity<List<Object[]>> getStatisticsByType() {
        List<Object[]> stats = historiqueStockService.getStatisticsByType();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/total/entree/{articleId}")
    @Operation(summary = "Total des entrées par article", description = "Retourne la quantité totale des entrées pour un article")
    public ResponseEntity<Integer> getTotalEntreeByArticle(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        Integer total = historiqueStockService.getTotalEntreeByArticle(articleId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/total/sortie/{articleId}")
    @Operation(summary = "Total des sorties par article", description = "Retourne la quantité totale des sorties pour un article")
    public ResponseEntity<Integer> getTotalSortieByArticle(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        Integer total = historiqueStockService.getTotalSortieByArticle(articleId);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/latest")
    @Operation(summary = "Derniers mouvements", description = "Retourne les derniers mouvements de stock")
    public ResponseEntity<List<HistoriqueStock>> getLatestMovements() {
        List<HistoriqueStock> historiques = historiqueStockService.getLatestMovements();
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/latest/article/{articleId}")
    @Operation(summary = "Derniers mouvements par article", description = "Retourne les derniers mouvements de stock pour un article")
    public ResponseEntity<List<HistoriqueStock>> getLatestMovementsByArticle(
            @Parameter(description = "ID de l'article") @PathVariable Long articleId) {
        List<HistoriqueStock> historiques = historiqueStockService.getLatestMovementsByArticle(articleId);
        return ResponseEntity.ok(historiques);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Vérifier l'existence", description = "Vérifie si un historique existe par ID")
    public ResponseEntity<Boolean> existsById(
            @Parameter(description = "ID de l'historique à vérifier") @PathVariable Long id) {
        boolean exists = historiqueStockService.existsById(id);
        return ResponseEntity.ok(exists);
    }
}
