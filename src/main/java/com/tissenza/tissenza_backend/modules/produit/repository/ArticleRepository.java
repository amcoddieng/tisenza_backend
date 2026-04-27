package com.tissenza.tissenza_backend.modules.produit.repository;

import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByProduitId(Long produitId);

    Optional<Article> findBySku(String sku);

    boolean existsBySku(String sku);

    List<Article> findByStockActuelGreaterThan(Integer stock);

    List<Article> findByStockActuelLessThan(Integer stock);

    @Query("SELECT a FROM Article a WHERE a.stockActuel = 0")
    List<Article> findOutOfStock();

    @Query("SELECT a FROM Article a WHERE a.prix BETWEEN :min AND :max")
    List<Article> findByPriceRange(@Param("min") BigDecimal min, @Param("max") BigDecimal max);

    @Query("SELECT a FROM Article a WHERE a.sku LIKE %:keyword% OR CAST(a.attributs AS text) LIKE %:keyword%")
    List<Article> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.produit.id = :produitId")
    long countByProduitId(@Param("produitId") Long produitId);

    @Query("SELECT COUNT(a) FROM Article a WHERE a.stockActuel = 0")
    long countOutOfStock();

    @Query("SELECT COUNT(a) FROM Article a WHERE a.stockActuel > 0")
    long countInStock();

    @Query("SELECT MIN(a.prix) FROM Article a WHERE a.produit.id = :produitId")
    BigDecimal getMinPriceByProduit(@Param("produitId") Long produitId);

    @Query("SELECT MAX(a.prix) FROM Article a WHERE a.produit.id = :produitId")
    BigDecimal getMaxPriceByProduit(@Param("produitId") Long produitId);

    @Query("SELECT SUM(a.stockActuel) FROM Article a WHERE a.produit.id = :produitId")
    Integer getTotalStockByProduit(@Param("produitId") Long produitId);
}
