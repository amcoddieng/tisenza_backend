package com.tissenza.tissenza_backend.modules.commande.repository;

import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande.CommandeStatus;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande.StatusPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    /**
     * Trouver les commandes par client
     */
    List<Commande> findByClientId(Long clientId);

    /**
     * Trouver les commandes par statut
     */
    List<Commande> findByStatus(CommandeStatus status);

    /**
     * Trouver les commandes par statut de paiement
     */
    List<Commande> findByStatusPaiement(StatusPaiement statusPaiement);

    /**
     * Trouver les commandes par client et statut
     */
    List<Commande> findByClientIdAndStatus(Long clientId, CommandeStatus status);

    /**
     * Trouver une commande par panier
     */
    Optional<Commande> findByPanierId(Long panierId);

    /**
     * Compter les commandes par client
     */
    @Query("SELECT COUNT(c) FROM Commande c WHERE c.client.id = :clientId")
    long countByClientId(@Param("clientId") Long clientId);

    /**
     * Trouver les commandes créées dans une période
     */
    @Query("SELECT c FROM Commande c WHERE c.date BETWEEN :startDate AND :endDate")
    List<Commande> findByDateBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                    @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Calculer le chiffre d'affaires total
     */
    @Query("SELECT SUM(c.total) FROM Commande c WHERE c.status = :status")
    java.math.BigDecimal calculateTotalRevenueByStatus(@Param("status") CommandeStatus status);

    /**
     * Trouver les commandes avec montant total supérieur à un seuil
     */
    @Query("SELECT c FROM Commande c WHERE c.total > :minTotal")
    List<Commande> findByTotalGreaterThan(@Param("minTotal") java.math.BigDecimal minTotal);

    /**
     * Trouver la dernière commande d'un client
     */
    @Query("SELECT c FROM Commande c WHERE c.client.id = :clientId ORDER BY c.date DESC")
    Optional<Commande> findLastCommandeByClient(@Param("clientId") Long clientId);
}
