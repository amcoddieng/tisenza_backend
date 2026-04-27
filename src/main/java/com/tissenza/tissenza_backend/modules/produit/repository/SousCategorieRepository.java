package com.tissenza.tissenza_backend.modules.produit.repository;

import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long> {

    List<SousCategorie> findByCategorieId(Long categorieId);

    Optional<SousCategorie> findByNom(String nom);

    List<SousCategorie> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT sc FROM SousCategorie sc WHERE sc.nom LIKE %:keyword% OR sc.description LIKE %:keyword%")
    List<SousCategorie> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(sc) FROM SousCategorie sc WHERE sc.categorie.id = :categorieId")
    long countByCategorieId(@Param("categorieId") Long categorieId);

    @Query("SELECT sc FROM SousCategorie sc LEFT JOIN FETCH sc.produits WHERE sc.categorie.id = :categorieId")
    List<SousCategorie> findByCategorieIdWithProduits(@Param("categorieId") Long categorieId);

    List<SousCategorie> findByCategorieIdAndNomContainingIgnoreCase(Long categorieId, String nom);
}
