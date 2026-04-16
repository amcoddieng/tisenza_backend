package com.tissenza.tissenza_backend.modules.commande.repository;

import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    // Recherche par numéro de commande
    Optional<Commande> findByNumeroCommande(String numeroCommande);

    // Commandes par client
    List<Commande> findByClientId(Long clientId);
    List<Commande> findByClientIdOrderByCreatedAtDesc(Long clientId);
    List<Commande> findByClient(Compte client);

    // Commandes par statut
    List<Commande> findByStatut(Commande.StatutCommande statut);

    // Commandes par client et statut
    List<Commande> findByClientIdAndStatut(Long clientId, Commande.StatutCommande statut);

    // Commandes par plage de dates
    @Query("SELECT c FROM Commande c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Commande> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    // Commandes par client et plage de dates
    @Query("SELECT c FROM Commande c WHERE c.client.id = :clientId AND c.createdAt BETWEEN :startDate AND :endDate")
    List<Commande> findByClientIdAndDateRange(@Param("clientId") Long clientId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    // Commandes par statut et plage de dates
    @Query("SELECT c FROM Commande c WHERE c.statut = :statut AND c.createdAt BETWEEN :startDate AND :endDate")
    List<Commande> findByStatutAndDateRange(@Param("statut") Commande.StatutCommande statut,
                                           @Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate);

    // Commandes en attente de validation
    List<Commande> findByStatutOrderByCreatedAtAsc(Commande.StatutCommande statut);

    // Commandes par date de livraison souhaitée
    @Query("SELECT c FROM Commande c WHERE c.dateLivraisonSouhaitee BETWEEN :startDate AND :endDate")
    List<Commande> findByDateLivraisonSouhaiteeRange(@Param("startDate") LocalDateTime startDate,
                                                    @Param("endDate") LocalDateTime endDate);

    // Commandes en retard (non livrées après la date souhaitée)
    @Query("SELECT c FROM Commande c WHERE c.dateLivraisonSouhaitee < :now AND c.statut NOT IN ('LIVREE', 'ANNULEE')")
    List<Commande> findCommandesEnRetard(@Param("now") LocalDateTime now);

    // Statistiques
    @Query("SELECT COUNT(c) FROM Commande c WHERE c.statut = :statut")
    long countByStatut(@Param("statut") Commande.StatutCommande statut);

    @Query("SELECT COUNT(c) FROM Commande c WHERE c.client.id = :clientId")
    long countByClientId(@Param("clientId") Long clientId);

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.statut = :statut")
    BigDecimal sumMontantTotalByStatut(@Param("statut") Commande.StatutCommande statut);

    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.client.id = :clientId AND c.statut = :statut")
    BigDecimal sumMontantTotalByClientIdAndStatut(@Param("clientId") Long clientId,
                                                 @Param("statut") Commande.StatutCommande statut);

    // Chiffre d'affaires par période
    @Query("SELECT SUM(c.montantTotal) FROM Commande c WHERE c.createdAt BETWEEN :startDate AND :endDate AND c.statut = 'LIVREE'")
    BigDecimal getChiffreAffairesByPeriode(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    // Dernières commandes
    @Query("SELECT c FROM Commande c ORDER BY c.createdAt DESC")
    List<Commande> findLatestCommandes();

    @Query("SELECT c FROM Commande c WHERE c.client.id = :clientId ORDER BY c.createdAt DESC")
    List<Commande> findLatestCommandesByClient(@Param("clientId") Long clientId);

    // Recherche par notes
    @Query("SELECT c FROM Commande c WHERE c.notes LIKE %:keyword%")
    List<Commande> searchByNotes(@Param("keyword") String keyword);
}
