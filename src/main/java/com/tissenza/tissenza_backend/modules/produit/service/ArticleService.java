package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.dto.ArticleDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.entity.HistoriqueStock;
import com.tissenza.tissenza_backend.modules.produit.mapper.ArticleMapper;
import com.tissenza.tissenza_backend.modules.produit.repository.ArticleRepository;
import com.tissenza.tissenza_backend.modules.produit.repository.HistoriqueStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final HistoriqueStockRepository historiqueStockRepository;
    private final ArticleMapper articleMapper;

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

    // ========== MÉTHODES DTO POUR ÉVITER LAZY INITIALIZATION EXCEPTION ==========

    /**
     * Récupère un article par ID et le convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> getArticleByIdDTO(Long id) {
        try {
            return articleRepository.findById(id)
                    .map(articleMapper::toDTO);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'article DTO par ID: {}", id, e);
            return Optional.empty();
        }
    }

    /**
     * Récupère tous les articles et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> getAllArticlesDTO() {
        try {
            List<Article> articles = articleRepository.findAll();
            log.debug("Récupération de {} articles", articles.size());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ArticleDTO> articlesDTO = articleMapper.toDTOList(articles);
            log.debug("Conversion de {} articles en DTO réussie", articlesDTO.size());
            
            return articlesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des articles DTO", e);
            throw new RuntimeException("Impossible de récupérer les articles", e);
        }
    }

    /**
     * Récupère les articles par ID produit et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByProduitIdDTO(Long produitId) {
        try {
            List<Article> articles = articleRepository.findByProduitId(produitId);
            log.debug("Récupération de {} articles pour le produit {}", articles.size(), produitId);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ArticleDTO> articlesDTO = articleMapper.toDTOList(articles);
            log.debug("Conversion de {} articles en DTO réussie", articlesDTO.size());
            
            return articlesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des articles DTO par produit ID: {}", produitId, e);
            throw new RuntimeException("Impossible de récupérer les articles", e);
        }
    }

    /**
     * Recherche des articles par mot-clé et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> searchArticlesByKeywordDTO(String keyword) {
        try {
            List<Article> articles = articleRepository.searchByKeyword(keyword);
            log.debug("Recherche de {} articles avec le mot-clé '{}'", articles.size(), keyword);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ArticleDTO> articlesDTO = articleMapper.toDTOList(articles);
            log.debug("Conversion de {} articles en DTO réussie", articlesDTO.size());
            
            return articlesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des articles DTO par mot-clé: {}", keyword, e);
            throw new RuntimeException("Impossible de rechercher les articles", e);
        }
    }

    /**
     * Récupère les articles en rupture de stock et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> getOutOfStockArticlesDTO() {
        try {
            List<Article> articles = articleRepository.findOutOfStock();
            log.debug("Récupération de {} articles en rupture de stock", articles.size());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ArticleDTO> articlesDTO = articleMapper.toDTOList(articles);
            log.debug("Conversion de {} articles en DTO réussie", articlesDTO.size());
            
            return articlesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des articles DTO en rupture de stock", e);
            throw new RuntimeException("Impossible de récupérer les articles", e);
        }
    }

    /**
     * Récupère les articles par plage de prix et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesByPriceRangeDTO(BigDecimal min, BigDecimal max) {
        try {
            List<Article> articles = articleRepository.findByPriceRange(min, max);
            log.debug("Récupération de {} articles dans la plage de prix {} - {}", articles.size(), min, max);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ArticleDTO> articlesDTO = articleMapper.toDTOList(articles);
            log.debug("Conversion de {} articles en DTO réussie", articlesDTO.size());
            
            return articlesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des articles DTO par plage de prix: {} - {}", min, max, e);
            throw new RuntimeException("Impossible de récupérer les articles", e);
        }
    }

    /**
     * Récupère les articles avec stock supérieur à une valeur et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesWithStockGreaterThanDTO(Integer stock) {
        try {
            List<Article> articles = articleRepository.findByStockActuelGreaterThan(stock);
            log.debug("Récupération de {} articles avec stock > {}", articles.size(), stock);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ArticleDTO> articlesDTO = articleMapper.toDTOList(articles);
            log.debug("Conversion de {} articles en DTO réussie", articlesDTO.size());
            
            return articlesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des articles DTO avec stock > {}", stock, e);
            throw new RuntimeException("Impossible de récupérer les articles", e);
        }
    }

    /**
     * Récupère les articles avec stock inférieur à une valeur et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ArticleDTO> getArticlesWithStockLessThanDTO(Integer stock) {
        try {
            List<Article> articles = articleRepository.findByStockActuelLessThan(stock);
            log.debug("Récupération de {} articles avec stock < {}", articles.size(), stock);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ArticleDTO> articlesDTO = articleMapper.toDTOList(articles);
            log.debug("Conversion de {} articles en DTO réussie", articlesDTO.size());
            
            return articlesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des articles DTO avec stock < {}", stock, e);
            throw new RuntimeException("Impossible de récupérer les articles", e);
        }
    }

    /**
     * Crée un article et le convertit en DTO
     */
    @Transactional
    public ArticleDTO createArticleDTO(Article article) {
        try {
            Article createdArticle = articleRepository.save(article);
            log.debug("Création de l'article avec ID: {}", createdArticle.getId());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ArticleDTO articleDTO = articleMapper.toDTO(createdArticle);
            log.debug("Conversion de l'article en DTO réussie");
            
            return articleDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la création de l'article DTO", e);
            throw new RuntimeException("Impossible de créer l'article", e);
        }
    }

    /**
     * Met à jour un article et le convertit en DTO
     */
    @Transactional
    public ArticleDTO updateArticleDTO(Long id, Article articleDetails) {
        try {
            Article updatedArticle = updateArticle(id, articleDetails);
            log.debug("Mise à jour de l'article avec ID: {}", updatedArticle.getId());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ArticleDTO articleDTO = articleMapper.toDTO(updatedArticle);
            log.debug("Conversion de l'article en DTO réussie");
            
            return articleDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour de l'article DTO: {}", id, e);
            throw new RuntimeException("Impossible de mettre à jour l'article", e);
        }
    }

    /**
     * Récupère un article par SKU et le convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<ArticleDTO> getArticleBySkuDTO(String sku) {
        try {
            return articleRepository.findBySku(sku)
                    .map(article -> {
                        log.debug("Récupération de l'article avec SKU: {}", sku);
                        return articleMapper.toDTO(article);
                    });
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de l'article DTO par SKU: {}", sku, e);
            throw new RuntimeException("Impossible de récupérer l'article", e);
        }
    }

    /**
     * Met à jour le stock d'un article et le convertit en DTO
     */
    @Transactional
    public ArticleDTO updateStockDTO(Long id, Integer newStock) {
        try {
            Article updatedArticle = updateStock(id, newStock);
            log.debug("Mise à jour du stock de l'article {} à {}", id, newStock);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ArticleDTO articleDTO = articleMapper.toDTO(updatedArticle);
            log.debug("Conversion de l'article en DTO réussie");
            
            return articleDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du stock DTO de l'article: {}", id, e);
            throw new RuntimeException("Impossible de mettre à jour le stock", e);
        }
    }

    /**
     * Ajoute du stock à un article et le convertit en DTO
     */
    @Transactional
    public ArticleDTO addStockDTO(Long id, Integer quantity, String motif) {
        try {
            Article updatedArticle = addStock(id, quantity, motif);
            log.debug("Ajout de {} au stock de l'article {}", quantity, id);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ArticleDTO articleDTO = articleMapper.toDTO(updatedArticle);
            log.debug("Conversion de l'article en DTO réussie");
            
            return articleDTO;
        } catch (Exception e) {
            log.error("Erreur lors de l'ajout de stock DTO de l'article: {}", id, e);
            throw new RuntimeException("Impossible d'ajouter du stock", e);
        }
    }

    /**
     * Retire du stock à un article et le convertit en DTO
     */
    @Transactional
    public ArticleDTO removeStockDTO(Long id, Integer quantity, String motif) {
        try {
            Article updatedArticle = removeStock(id, quantity, motif);
            log.debug("Retrait de {} du stock de l'article {}", quantity, id);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ArticleDTO articleDTO = articleMapper.toDTO(updatedArticle);
            log.debug("Conversion de l'article en DTO réussie");
            
            return articleDTO;
        } catch (Exception e) {
            log.error("Erreur lors du retrait de stock DTO de l'article: {}", id, e);
            throw new RuntimeException("Impossible de retirer du stock", e);
        }
    }
}
