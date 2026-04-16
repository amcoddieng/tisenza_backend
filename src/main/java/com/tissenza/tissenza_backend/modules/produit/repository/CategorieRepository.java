package com.tissenza.tissenza_backend.modules.produit.repository;

import com.tissenza.tissenza_backend.modules.produit.entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    Optional<Categorie> findByNom(String nom);

    List<Categorie> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT c FROM Categorie c WHERE c.nom LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Categorie> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(c) FROM Categorie c")
    long countCategories();

    @Query("SELECT c FROM Categorie c LEFT JOIN FETCH c.sousCategories")
    List<Categorie> findAllWithSousCategories();
}
