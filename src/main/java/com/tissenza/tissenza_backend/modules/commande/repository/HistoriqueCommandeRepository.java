package com.tissenza.tissenza_backend.modules.commande.repository;

import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.commande.entity.HistoriqueCommande;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueCommandeRepository extends JpaRepository<HistoriqueCommande, Long> {

    // Historique par commande
    List<HistoriqueCommande> findByCommandeIdOrderByCreatedAtDesc(Long commandeId);
    List<HistoriqueCommande> findByCommandeOrderByCreatedAtDesc(Commande commande);

    // Historique par utilisateur
    List<HistoriqueCommande> findByModifieParIdOrderByCreatedAtDesc(Long modifieParId);
    List<HistoriqueCommande> findByModifieParOrderByCreatedAtDesc(Compte modifiePar);

    // Changements de statut spécifiques
    List<HistoriqueCommande> findByAncienStatutAndNouveauStatut(
            Commande.StatutCommande ancienStatut, 
            Commande.StatutCommande nouveauStatut);

    // Historique par plage de dates
    @Query("SELECT h FROM HistoriqueCommande h WHERE h.createdAt BETWEEN :startDate AND :endDate")
    List<HistoriqueCommande> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    // Historique par commande et plage de dates
    @Query("SELECT h FROM HistoriqueCommande h WHERE h.commande.id = :commandeId AND h.createdAt BETWEEN :startDate AND :endDate")
    List<HistoriqueCommande> findByCommandeIdAndDateRange(@Param("commandeId") Long commandeId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);

    // Derniers changements de statut
    @Query("SELECT h FROM HistoriqueCommande h ORDER BY h.createdAt DESC")
    List<HistoriqueCommande> findLatestChangements();

    // Statistiques sur les changements de statut
    @Query("SELECT h.nouveauStatut, COUNT(h) FROM HistoriqueCommande h GROUP BY h.nouveauStatut")
    List<Object[]> getStatistiquesChangementsStatut();

    // Temps moyen entre changements de statut
    @Query("SELECT h1.nouveauStatut, AVG(FUNCTION('TIMESTAMPDIFF', 'SECOND', h1.createdAt, h2.createdAt)) " +
           "FROM HistoriqueCommande h1 " +
           "JOIN HistoriqueCommande h2 ON h1.commande = h2.commande AND h1.createdAt < h2.createdAt " +
           "GROUP BY h1.nouveauStatut")
    List<Object[]> getTempsMoyentParStatut();

    // Rechercher par motif
    @Query("SELECT h FROM HistoriqueCommande h WHERE h.motif LIKE %:keyword%")
    List<HistoriqueCommande> searchByMotif(@Param("keyword") String keyword);

    // Historique complet d'une commande avec détails
    @Query("SELECT h FROM HistoriqueCommande h " +
           "LEFT JOIN FETCH h.commande " +
           "LEFT JOIN FETCH h.modifiePar " +
           "WHERE h.commande.id = :commandeId " +
           "ORDER BY h.createdAt DESC")
    List<HistoriqueCommande> getHistoriqueCompletByCommande(@Param("commandeId") Long commandeId);
}
