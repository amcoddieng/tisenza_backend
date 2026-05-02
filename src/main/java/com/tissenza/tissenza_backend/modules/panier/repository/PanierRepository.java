package com.tissenza.tissenza_backend.modules.panier.repository;

import com.tissenza.tissenza_backend.modules.panier.entity.Panier;
import com.tissenza.tissenza_backend.modules.panier.entity.Panier.PanierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {

    /**
     * Trouver les paniers par client
     */
    List<Panier> findByClientId(Long clientId);

    /**
     * Trouver le panier actif d'un client (status EN_ATTENTE)
     */
    Optional<Panier> findByClientIdAndStatus(Long clientId, PanierStatus status);

    /**
     * Trouver les paniers par statut
     */
    List<Panier> findByStatus(PanierStatus status);

    /**
     * Compter les paniers par client
     */
    @Query("SELECT COUNT(p) FROM Panier p WHERE p.client.id = :clientId")
    long countByClientId(@Param("clientId") Long clientId);

    /**
     * Trouver les paniers créés dans une période
     */
    @Query("SELECT p FROM Panier p WHERE p.dateCreation BETWEEN :startDate AND :endDate")
    List<Panier> findByDateCreationBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                          @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Trouver le dernier panier d'un client
     */
    @Query("SELECT p FROM Panier p WHERE p.client.id = :clientId ORDER BY p.dateCreation DESC")
    Optional<Panier> findLastPanierByClient(@Param("clientId") Long clientId);
}
