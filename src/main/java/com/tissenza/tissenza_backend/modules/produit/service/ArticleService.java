package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import com.tissenza.tissenza_backend.modules.produit.repository.ArticleRepository;
import com.tissenza.tissenza_backend.modules.produit.repository.HistoriqueStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final HistoriqueStockRepository historiqueStockRepository;

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article updateArticle(Long id, Article articleDetails) {
        return articleRepository.findById(id)
                .map(article -> {
                    article.setProduit(articleDetails.getProduit());
                    article.setSku(articleDetails.getSku());
                    article.setPrix(articleDetails.getPrix());
                    article.setStockActuel(articleDetails.getStockActuel());
                    article.setAttributs(articleDetails.getAttributs());
                    article.setImage(articleDetails.getImage());
                    return articleRepository.save(article);
                })
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public List<Article> getArticlesByProduitId(Long produitId) {
        return articleRepository.findByProduitId(produitId);
    }

    public Optional<Article> getArticleBySku(String sku) {
        return articleRepository.findBySku(sku);
    }

    public boolean existsBySku(String sku) {
        return articleRepository.existsBySku(sku);
    }

    public List<Article> getArticlesWithStockGreaterThan(Integer stock) {
        return articleRepository.findByStockActuelGreaterThan(stock);
    }

    public List<Article> getArticlesWithStockLessThan(Integer stock) {
        return articleRepository.findByStockActuelLessThan(stock);
    }

    public List<Article> getOutOfStockArticles() {
        return articleRepository.findOutOfStock();
    }

    public List<Article> getArticlesByPriceRange(BigDecimal min, BigDecimal max) {
        return articleRepository.findByPriceRange(min, max);
    }

    public List<Article> searchArticlesByKeyword(String keyword) {
        return articleRepository.searchByKeyword(keyword);
    }

    public long countByProduitId(Long produitId) {
        return articleRepository.countByProduitId(produitId);
    }

    public long countOutOfStock() {
        return articleRepository.countOutOfStock();
    }

    public long countInStock() {
        return articleRepository.countInStock();
    }

    public BigDecimal getMinPriceByProduit(Long produitId) {
        return articleRepository.getMinPriceByProduit(produitId);
    }

    public BigDecimal getMaxPriceByProduit(Long produitId) {
        return articleRepository.getMaxPriceByProduit(produitId);
    }

    public Integer getTotalStockByProduit(Long produitId) {
        return articleRepository.getTotalStockByProduit(produitId);
    }

    public Article updateStock(Long id, Integer newStock) {
        return articleRepository.findById(id)
                .map(article -> {
                    Integer oldStock = article.getStockActuel();
                    article.setStockActuel(newStock);
                    
                    // Créer un historique de mouvement de stock
                    HistoriqueStock historique = new HistoriqueStock();
                    historique.setArticle(article);
                    historique.setQuantite(newStock - oldStock);
                    historique.setType((newStock - oldStock) > 0 ? HistoriqueStock.Type.ENTREE : HistoriqueStock.Type.SORTIE);
                    historique.setMotif("Mise à jour manuelle du stock");
                    historiqueStockRepository.save(historique);
                    
                    return articleRepository.save(article);
                })
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }

    public Article addStock(Long id, Integer quantity, String motif) {
        return articleRepository.findById(id)
                .map(article -> {
                    article.setStockActuel(article.getStockActuel() + quantity);
                    
                    // Créer un historique d'entrée de stock
                    HistoriqueStock historique = new HistoriqueStock();
                    historique.setArticle(article);
                    historique.setQuantite(quantity);
                    historique.setType(HistoriqueStock.Type.ENTREE);
                    historique.setMotif(motif != null ? motif : "Entrée de stock");
                    historiqueStockRepository.save(historique);
                    
                    return articleRepository.save(article);
                })
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }

    public Article removeStock(Long id, Integer quantity, String motif) {
        return articleRepository.findById(id)
                .map(article -> {
                    if (article.getStockActuel() < quantity) {
                        throw new RuntimeException("Stock insuffisant pour cet article");
                    }
                    article.setStockActuel(article.getStockActuel() - quantity);
                    
                    // Créer un historique de sortie de stock
                    HistoriqueStock historique = new HistoriqueStock();
                    historique.setArticle(article);
                    historique.setQuantite(quantity);
                    historique.setType(HistoriqueStock.Type.SORTIE);
                    historique.setMotif(motif != null ? motif : "Sortie de stock");
                    historiqueStockRepository.save(historique);
                    
                    return articleRepository.save(article);
                })
                .orElseThrow(() -> new RuntimeException("Article not found with id: " + id));
    }

    public boolean existsById(Long id) {
        return articleRepository.existsById(id);
    }
}
