package com.tissenza.tissenza_backend.modules.boutique.repository;

import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoutiqueRepository extends JpaRepository<Boutique, Long> {

    Optional<Boutique> findByVendeur(Compte vendeur);

    List<Boutique> findByVendeurId(Long vendeurId);

    List<Boutique> findByStatut(Boutique.Statut statut);

    List<Boutique> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT b FROM Boutique b WHERE b.nom LIKE %:keyword% OR b.description LIKE %:keyword%")
    List<Boutique> searchByKeyword(@Param("keyword") String keyword);

    List<Boutique> findByNoteGreaterThanEqual(Float note);

    @Query("SELECT AVG(b.note) FROM Boutique b WHERE b.statut = :statut")
    Float getAverageNoteByStatut(@Param("statut") Boutique.Statut statut);

    @Query("SELECT COUNT(b) FROM Boutique b WHERE b.statut = :statut")
    long countByStatut(@Param("statut") Boutique.Statut statut);

    @Query("SELECT b.statut, COUNT(b) FROM Boutique b GROUP BY b.statut")
    List<Object[]> getStatisticsByStatut();

    boolean existsByVendeur(Compte vendeur);

    boolean existsByVendeurId(Long vendeurId);
}
