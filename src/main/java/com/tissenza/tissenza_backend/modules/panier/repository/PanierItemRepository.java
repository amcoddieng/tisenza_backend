package com.tissenza.tissenza_backend.modules.panier.repository;

import com.tissenza.tissenza_backend.modules.panier.entity.PanierItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PanierItemRepository extends JpaRepository<PanierItem, Long> {

    /**
     * Trouver les items d'un panier
     */
    List<PanierItem> findByPanierId(Long panierId);

    /**
     * Trouver un item spécifique dans un panier
     */
    Optional<PanierItem> findByPanierIdAndArticleId(Long panierId, Long articleId);

    /**
     * Compter les items dans un panier
     */
    @Query("SELECT COUNT(pi) FROM PanierItem pi WHERE pi.panier.id = :panierId")
    long countByPanierId(@Param("panierId") Long panierId);

    /**
     * Trouver les items par article
     */
    List<PanierItem> findByArticleId(Long articleId);

    /**
     * Supprimer tous les items d'un panier
     */
    void deleteByPanierId(Long panierId);

    /**
     * Calculer le total d'un panier
     */
    @Query("SELECT SUM(pi.prixUnitaire * pi.quantite) FROM PanierItem pi WHERE pi.panier.id = :panierId")
    java.math.BigDecimal calculateTotalByPanierId(@Param("panierId") Long panierId);

    /**
     * Trouver les items créés dans une période
     */
    @Query("SELECT pi FROM PanierItem pi WHERE pi.date BETWEEN :startDate AND :endDate")
    List<PanierItem> findByDateBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                      @Param("endDate") java.time.LocalDateTime endDate);
}
