package com.tissenza.tissenza_backend.modules.paiement.repository;

import com.tissenza.tissenza_backend.modules.paiement.entity.MoyenPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité MoyenPaiement
 */
@Repository
public interface MoyenPaiementRepository extends JpaRepository<MoyenPaiement, Long> {

    /**
     * Trouve un moyen de paiement par son nom
     */
    Optional<MoyenPaiement> findByNom(String nom);

    /**
     * Vérifie si un moyen de paiement existe par son nom
     */
    boolean existsByNom(String nom);

    /**
     * Trouve tous les moyens de paiement actifs pour un utilisateur
     */
    @Query("SELECT mp FROM MoyenPaiement mp " +
           "JOIN UserMoyenPaiement ump ON mp.id = ump.moyenPaiement.id " +
           "WHERE ump.user.id = :userId AND ump.actif = true")
    List<MoyenPaiement> findActifsByUserId(@Param("userId") Long userId);

    /**
     * Trouve tous les moyens de paiement pour un utilisateur
     */
    @Query("SELECT mp FROM MoyenPaiement mp " +
           "JOIN UserMoyenPaiement ump ON mp.id = ump.moyenPaiement.id " +
           "WHERE ump.user.id = :userId")
    List<MoyenPaiement> findByUserId(@Param("userId") Long userId);

    /**
     * Compte le nombre de moyens de paiement actifs pour un utilisateur
     */
    @Query("SELECT COUNT(mp) FROM MoyenPaiement mp " +
           "JOIN UserMoyenPaiement ump ON mp.id = ump.moyenPaiement.id " +
           "WHERE ump.user.id = :userId AND ump.actif = true")
    long countActifsByUserId(@Param("userId") Long userId);

    /**
     * Vérifie si un utilisateur a un moyen de paiement spécifique actif
     */
    @Query("SELECT CASE WHEN COUNT(ump) > 0 THEN true ELSE false END " +
           "FROM UserMoyenPaiement ump " +
           "WHERE ump.user.id = :userId AND ump.moyenPaiement.id = :moyenPaiementId AND ump.actif = true")
    boolean existsActifForUser(@Param("userId") Long userId, @Param("moyenPaiementId") Long moyenPaiementId);

    /**
     * Recherche des moyens de paiement par mot-clé
     */
    @Query("SELECT mp FROM MoyenPaiement mp " +
           "WHERE LOWER(mp.nom) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<MoyenPaiement> searchByKeyword(@Param("keyword") String keyword);
}
