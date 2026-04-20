package com.tissenza.tissenza_backend.modules.paiement.repository;

import com.tissenza.tissenza_backend.modules.paiement.entity.UserMoyenPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité UserMoyenPaiement
 */
@Repository
public interface UserMoyenPaiementRepository extends JpaRepository<UserMoyenPaiement, Long> {

    /**
     * Trouve une association utilisateur-moyen de paiement
     */
    Optional<UserMoyenPaiement> findByUserIdAndMoyenPaiementId(Long userId, Long moyenPaiementId);

    /**
     * Vérifie si une association utilisateur-moyen de paiement existe
     */
    boolean existsByUserIdAndMoyenPaiementId(Long userId, Long moyenPaiementId);

    /**
     * Trouve tous les moyens de paiement d'un utilisateur
     */
    List<UserMoyenPaiement> findByUserId(Long userId);

    /**
     * Trouve tous les moyens de paiement actifs d'un utilisateur
     */
    List<UserMoyenPaiement> findByUserIdAndActifTrue(Long userId);

    /**
     * Trouve tous les utilisateurs qui ont un moyen de paiement spécifique
     */
    List<UserMoyenPaiement> findByMoyenPaiementId(Long moyenPaiementId);

    /**
     * Trouve tous les utilisateurs qui ont un moyen de paiement spécifique actif
     */
    List<UserMoyenPaiement> findByMoyenPaiementIdAndActifTrue(Long moyenPaiementId);

    /**
     * Désactiver un moyen de paiement pour un utilisateur
     */
    @Modifying
    @Query("UPDATE UserMoyenPaiement ump SET ump.actif = false " +
           "WHERE ump.user.id = :userId AND ump.moyenPaiement.id = :moyenPaiementId")
    int desactiverMoyenPaiement(@Param("userId") Long userId, @Param("moyenPaiementId") Long moyenPaiementId);

    /**
     * Activer un moyen de paiement pour un utilisateur
     */
    @Modifying
    @Query("UPDATE UserMoyenPaiement ump SET ump.actif = true " +
           "WHERE ump.user.id = :userId AND ump.moyenPaiement.id = :moyenPaiementId")
    int activerMoyenPaiement(@Param("userId") Long userId, @Param("moyenPaiementId") Long moyenPaiementId);

    /**
     * Compter le nombre de moyens de paiement actifs pour un utilisateur
     */
    @Query("SELECT COUNT(ump) FROM UserMoyenPaiement ump " +
           "WHERE ump.user.id = :userId AND ump.actif = true")
    long countActifsByUserId(@Param("userId") Long userId);

    /**
     * Supprimer toutes les associations d'un utilisateur
     */
    @Modifying
    @Query("DELETE FROM UserMoyenPaiement ump WHERE ump.user.id = :userId")
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * Supprimer toutes les associations d'un moyen de paiement
     */
    @Modifying
    @Query("DELETE FROM UserMoyenPaiement ump WHERE ump.moyenPaiement.id = :moyenPaiementId")
    int deleteByMoyenPaiementId(@Param("moyenPaiementId") Long moyenPaiementId);
}
