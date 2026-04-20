package com.tissenza.tissenza_backend.modules.user.repository;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    Optional<Compte> findByEmail(String email);

    Optional<Compte> findByTelephone(String telephone);

    boolean existsByEmail(String email);

    boolean existsByTelephone(String telephone);

    List<Compte> findByRole(Compte.Role role);

    List<Compte> findByStatut(Compte.Statut statut);

    List<Compte> findByRoleAndStatut(Compte.Role role, Compte.Statut statut);

    List<Compte> findByIsVerified(Boolean isVerified);

    @Query("SELECT c FROM Compte c WHERE c.email LIKE %:keyword% OR c.telephone LIKE %:keyword%")
    List<Compte> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(c) FROM Compte c WHERE c.role = :role")
    long countByRole(@Param("role") Compte.Role role);

    @Query("SELECT COUNT(c) FROM Compte c WHERE c.statut = :statut")
    long countByStatut(@Param("statut") Compte.Statut statut);

    @Query("SELECT COUNT(c) FROM Compte c WHERE c.isVerified = true")
    long countVerifiedUsers();

    // JOIN FETCH queries pour éviter LazyInitializationException
    @Query("SELECT c FROM Compte c LEFT JOIN FETCH c.personne WHERE c.id = :id")
    Optional<Compte> findByIdWithPersonne(@Param("id") Long id);

    @Query("SELECT c FROM Compte c LEFT JOIN FETCH c.personne WHERE c.email = :email")
    Optional<Compte> findByEmailWithPersonne(@Param("email") String email);

    @Query("SELECT c FROM Compte c LEFT JOIN FETCH c.personne")
    List<Compte> findAllWithPersonne();
}
