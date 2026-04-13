package com.tissenza.tissenza_backend.modules.produit.repository;

import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueStockRepository extends JpaRepository<HistoriqueStock, Long> {

    List<HistoriqueStock> findByArticleId(Long articleId);

    List<HistoriqueStock> findByType(HistoriqueStock.Type type);

    List<HistoriqueStock> findByArticleIdAndType(Long articleId, HistoriqueStock.Type type);

    @Query("SELECT h FROM HistoriqueStock h WHERE h.createdAt BETWEEN :startDate AND :endDate")
    List<HistoriqueStock> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);

    @Query("SELECT h FROM HistoriqueStock h WHERE h.article.id = :articleId AND h.createdAt BETWEEN :startDate AND :endDate")
    List<HistoriqueStock> findByArticleIdAndDateRange(@Param("articleId") Long articleId,
                                                      @Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    @Query("SELECT h FROM HistoriqueStock h WHERE h.motif LIKE %:keyword%")
    List<HistoriqueStock> searchByMotif(@Param("keyword") String keyword);

    @Query("SELECT COUNT(h) FROM HistoriqueStock h WHERE h.article.id = :articleId")
    long countByArticleId(@Param("articleId") Long articleId);

    @Query("SELECT COUNT(h) FROM HistoriqueStock h WHERE h.type = :type")
    long countByType(@Param("type") HistoriqueStock.Type type);

    @Query("SELECT h.type, COUNT(h) FROM HistoriqueStock h GROUP BY h.type")
    List<Object[]> getStatisticsByType();

    @Query("SELECT SUM(h.quantite) FROM HistoriqueStock h WHERE h.article.id = :articleId AND h.type = 'ENTREE'")
    Integer getTotalEntreeByArticle(@Param("articleId") Long articleId);

    @Query("SELECT SUM(h.quantite) FROM HistoriqueStock h WHERE h.article.id = :articleId AND h.type = 'SORTIE'")
    Integer getTotalSortieByArticle(@Param("articleId") Long articleId);

    @Query("SELECT h FROM HistoriqueStock h ORDER BY h.createdAt DESC")
    List<HistoriqueStock> findLatestMovements();

    @Query("SELECT h FROM HistoriqueStock h WHERE h.article.id = :articleId ORDER BY h.createdAt DESC")
    List<HistoriqueStock> findLatestMovementsByArticle(@Param("articleId") Long articleId);
}
