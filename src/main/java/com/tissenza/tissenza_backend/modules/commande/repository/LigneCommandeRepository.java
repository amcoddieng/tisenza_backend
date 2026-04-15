package com.tissenza.tissenza_backend.modules.commande.repository;

import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.commande.entity.LigneCommande;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {

    // Lignes de commande par commande
    List<LigneCommande> findByCommandeId(Long commandeId);
    List<LigneCommande> findByCommande(Commande commande);

    // Lignes de commande par article
    List<LigneCommande> findByArticleId(Long articleId);
    List<LigneCommande> findByArticle(Article article);

    // Articles les plus commandés
    @Query("SELECT lc.article, SUM(lc.quantite) as totalQuantite " +
           "FROM LigneCommande lc " +
           "GROUP BY lc.article " +
           "ORDER BY totalQuantite DESC")
    List<Object[]> findArticlesLesPlusCommandes();

    // Articles les plus commandés par période
    @Query("SELECT lc.article, SUM(lc.quantite) as totalQuantite " +
           "FROM LigneCommande lc " +
           "WHERE lc.commande.createdAt BETWEEN :startDate AND :endDate " +
           "GROUP BY lc.article " +
           "ORDER BY totalQuantite DESC")
    List<Object[]> findArticlesLesPlusCommandesByPeriode(@Param("startDate") java.time.LocalDateTime startDate,
                                                        @Param("endDate") java.time.LocalDateTime endDate);

    // Chiffre d'affaires par article
    @Query("SELECT lc.article, SUM(lc.montantTotal) as totalCA " +
           "FROM LigneCommande lc " +
           "WHERE lc.commande.statut = 'LIVREE' " +
           "GROUP BY lc.article " +
           "ORDER BY totalCA DESC")
    List<Object[]> getChiffreAffairesParArticle();

    // Marge totale par commande
    @Query("SELECT lc.commande, SUM(lc.montantTotal - (lc.prixAchatUnitaire * lc.quantite)) as margeTotale " +
           "FROM LigneCommande lc " +
           "WHERE lc.prixAchatUnitaire IS NOT NULL " +
           "GROUP BY lc.commande")
    List<Object[]> getMargeTotaleParCommande();

    // Statistiques sur les ventes d'un article
    @Query("SELECT COUNT(DISTINCT lc.commande), SUM(lc.quantite), SUM(lc.montantTotal) " +
           "FROM LigneCommande lc " +
           "WHERE lc.article.id = :articleId AND lc.commande.statut = 'LIVREE'")
    Object[] getStatistiquesVentesArticle(@Param("articleId") Long articleId);

    // Lignes de commande avec prix d'achat (pour calcul des marges)
    @Query("SELECT lc FROM LigneCommande lc WHERE lc.prixAchatUnitaire IS NOT NULL")
    List<LigneCommande> findLignesAvecPrixAchat();

    // Vérifier si un article est dans des commandes actives
    @Query("SELECT COUNT(lc) FROM LigneCommande lc " +
           "WHERE lc.article.id = :articleId AND lc.commande.statut NOT IN ('LIVREE', 'ANNULEE')")
    long countByArticleIdInCommandesActives(@Param("articleId") Long articleId);
}
