package com.tissenza.tissenza_backend.modules.commande.repository;

import com.tissenza.tissenza_backend.modules.commande.entity.DetailCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DetailCommandeRepository extends JpaRepository<DetailCommande, Long> {

    /**
     * Trouver les détails d'une commande
     */
    List<DetailCommande> findByCommandeId(Long commandeId);

    /**
     * Trouver les détails par article
     */
    List<DetailCommande> findByArticleId(Long articleId);

    /**
     * Compter les détails dans une commande
     */
    @Query("SELECT COUNT(dc) FROM DetailCommande dc WHERE dc.commande.id = :commandeId")
    long countByCommandeId(@Param("commandeId") Long commandeId);

    /**
     * Calculer le total d'une commande
     */
    @Query("SELECT SUM(dc.sousTotal) FROM DetailCommande dc WHERE dc.commande.id = :commandeId")
    BigDecimal calculateTotalByCommandeId(@Param("commandeId") Long commandeId);

    /**
     * Trouver les articles les plus vendus
     */
    @Query("SELECT dc.article.id, SUM(dc.quantite) as totalVendu FROM DetailCommande dc " +
           "GROUP BY dc.article.id ORDER BY totalVendu DESC")
    List<Object[]> findTopSellingArticles();

    /**
     * Trouver les détails créés dans une période
     */
    @Query("SELECT dc FROM DetailCommande dc WHERE dc.date BETWEEN :startDate AND :endDate")
    List<DetailCommande> findByDateBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                          @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Supprimer tous les détails d'une commande
     */
    void deleteByCommandeId(Long commandeId);

    /**
     * Trouver les détails avec quantité supérieure à un seuil
     */
    @Query("SELECT dc FROM DetailCommande dc WHERE dc.quantite > :minQuantite")
    List<DetailCommande> findByQuantiteGreaterThan(@Param("minQuantite") Integer minQuantite);
}
