package com.tissenza.tissenza_backend.modules.produit.repository;

import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {

    List<Produit> findByBoutiqueId(Long boutiqueId);

    List<Produit> findBySousCategorieId(Long sousCategorieId);

    List<Produit> findByStatut(Produit.Statut statut);

    Optional<Produit> findByNom(String nom);

    List<Produit> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT p FROM Produit p WHERE p.nom LIKE %:keyword% OR p.description LIKE %:keyword%")
    List<Produit> searchByKeyword(@Param("keyword") String keyword);

    List<Produit> findByBoutiqueIdAndStatut(Long boutiqueId, Produit.Statut statut);

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.boutique.id = :boutiqueId")
    long countByBoutiqueId(@Param("boutiqueId") Long boutiqueId);

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.sousCategorie.id = :sousCategorieId")
    long countBySousCategorieId(@Param("sousCategorieId") Long sousCategorieId);

    @Query("SELECT COUNT(p) FROM Produit p WHERE p.statut = :statut")
    long countByStatut(@Param("statut") Produit.Statut statut);

    @Query("SELECT p.statut, COUNT(p) FROM Produit p GROUP BY p.statut")
    List<Object[]> getStatisticsByStatut();

    @Query("SELECT p FROM Produit p LEFT JOIN FETCH p.articles WHERE p.id = :id")
    Optional<Produit> findByIdWithArticles(@Param("id") Long id);
}
