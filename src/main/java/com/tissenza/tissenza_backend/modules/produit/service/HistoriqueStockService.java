package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import com.tissenza.tissenza_backend.modules.produit.repository.HistoriqueStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoriqueStockService {

    private final HistoriqueStockRepository historiqueStockRepository;

    public HistoriqueStock createHistoriqueStock(HistoriqueStock historiqueStock) {
        return historiqueStockRepository.save(historiqueStock);
    }

    public Optional<HistoriqueStock> getHistoriqueStockById(Long id) {
        return historiqueStockRepository.findById(id);
    }

    public List<HistoriqueStock> getAllHistoriqueStock() {
        return historiqueStockRepository.findAll();
    }

    public void deleteHistoriqueStock(Long id) {
        historiqueStockRepository.deleteById(id);
    }

    public List<HistoriqueStock> getHistoriqueStockByArticleId(Long articleId) {
        return historiqueStockRepository.findByArticleId(articleId);
    }

    public List<HistoriqueStock> getHistoriqueStockByType(HistoriqueStock.Type type) {
        return historiqueStockRepository.findByType(type);
    }

    public List<HistoriqueStock> getHistoriqueStockByArticleIdAndType(Long articleId, HistoriqueStock.Type type) {
        return historiqueStockRepository.findByArticleIdAndType(articleId, type);
    }

    public List<HistoriqueStock> getHistoriqueStockByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return historiqueStockRepository.findByDateRange(startDate, endDate);
    }

    public List<HistoriqueStock> getHistoriqueStockByArticleIdAndDateRange(Long articleId, LocalDateTime startDate, LocalDateTime endDate) {
        return historiqueStockRepository.findByArticleIdAndDateRange(articleId, startDate, endDate);
    }

    public List<HistoriqueStock> searchHistoriqueStockByMotif(String keyword) {
        return historiqueStockRepository.searchByMotif(keyword);
    }

    public long countByArticleId(Long articleId) {
        return historiqueStockRepository.countByArticleId(articleId);
    }

    public long countByType(HistoriqueStock.Type type) {
        return historiqueStockRepository.countByType(type);
    }

    public List<Object[]> getStatisticsByType() {
        return historiqueStockRepository.getStatisticsByType();
    }

    public Integer getTotalEntreeByArticle(Long articleId) {
        return historiqueStockRepository.getTotalEntreeByArticle(articleId);
    }

    public Integer getTotalSortieByArticle(Long articleId) {
        return historiqueStockRepository.getTotalSortieByArticle(articleId);
    }

    public List<HistoriqueStock> getLatestMovements() {
        return historiqueStockRepository.findLatestMovements();
    }

    public List<HistoriqueStock> getLatestMovementsByArticle(Long articleId) {
        return historiqueStockRepository.findLatestMovementsByArticle(articleId);
    }

    public boolean existsById(Long id) {
        return historiqueStockRepository.existsById(id);
    }
}
