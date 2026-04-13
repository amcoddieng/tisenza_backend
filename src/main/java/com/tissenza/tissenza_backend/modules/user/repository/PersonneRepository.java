package com.tissenza.tissenza_backend.modules.user.repository;

import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {

    Optional<Personne> findByNom(String nom);

    Optional<Personne> findByPrenom(String prenom);

    List<Personne> findByNomContainingIgnoreCase(String nom);

    List<Personne> findByPrenomContainingIgnoreCase(String prenom);

    List<Personne> findByVilleIgnoreCase(String ville);

    @Query("SELECT p FROM Personne p WHERE p.nom LIKE %:keyword% OR p.prenom LIKE %:keyword% OR p.ville LIKE %:keyword%")
    List<Personne> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(p) FROM Personne p WHERE p.ville = :ville")
    long countByVille(@Param("ville") String ville);
}
