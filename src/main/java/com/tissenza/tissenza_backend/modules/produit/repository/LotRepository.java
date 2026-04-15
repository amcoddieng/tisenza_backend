package com.tissenza.tissenza_backend.modules.produit.repository;

import com.tissenza.tissenza_backend.modules.produit.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    /**
     * Trouver tous les lots d'un article
     */
    List<Lot> findByArticleId(Long articleId);

    /**
     * Trouver les lots actifs d'un article
     */
    List<Lot> findByArticleIdAndStatut(Long articleId, Lot.Statut statut);

    /**
     * Trouver un lot par son numéro unique
     */
    Lot findByNumeroLot(String numeroLot);

    /**
     * Trouver les lots proches de la péremption
     */
    @Query("SELECT l FROM Lot l WHERE l.dateExpiration BETWEEN :debut AND :fin AND l.statut = 'ACTIF'")
    List<Lot> findLotsProchesPeremption(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);

    /**
     * Trouver les lots périmés
     */
    @Query("SELECT l FROM Lot l WHERE l.dateExpiration < :date AND l.statut != 'PERIME'")
    List<Lot> findLotsPerimes(@Param("date") LocalDate date);

    /**
     * Trouver les lots épuisés
     */
    List<Lot> findByQuantiteRestanteLessThanAndStatutNot(Integer quantite, Lot.Statut statut);

    /**
     * Trouver les lots par fournisseur
     */
    List<Lot> findByFournisseurContainingIgnoreCase(String fournisseur);

    /**
     * Trouver les lots par emplacement
     */
    List<Lot> findByEmplacementContainingIgnoreCase(String emplacement);

    /**
     * Calculer le stock total d'un article
     */
    @Query("SELECT COALESCE(SUM(l.quantiteRestante), 0) FROM Lot l WHERE l.article.id = :articleId AND l.statut = 'ACTIF'")
    Integer calculerStockTotalParArticle(@Param("articleId") Long articleId);

    /**
     * Trouver les lots arrivant à expiration dans les X prochains jours
     */
    @Query("SELECT l FROM Lot l WHERE l.dateExpiration BETWEEN :aujourdhui AND :dateLimite AND l.statut = 'ACTIF'")
    List<Lot> findLotsExpirantAvant(@Param("aujourdhui") LocalDate aujourdhui, @Param("dateLimite") LocalDate dateLimite);

    /**
     * Compter les lots par statut
     */
    @Query("SELECT COUNT(l) FROM Lot l WHERE l.statut = :statut")
    Long countByStatut(@Param("statut") Lot.Statut statut);

    /**
     * Trouver les lots avec stock faible
     */
    @Query("SELECT l FROM Lot l WHERE l.quantiteRestante <= :seuil AND l.statut = 'ACTIF'")
    List<Lot> findLotsStockFaible(@Param("seuil") Integer seuil);

    /**
     * Rechercher des lots par mot-clé (numéro, fournisseur, emplacement)
     */
    @Query("SELECT l FROM Lot l WHERE l.numeroLot LIKE %:keyword% OR l.fournisseur LIKE %:keyword% OR l.emplacement LIKE %:keyword%")
    List<Lot> searchByKeyword(@Param("keyword") String keyword);
}
