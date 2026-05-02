package com.tissenza.tissenza_backend.modules.panier.service;

import com.tissenza.tissenza_backend.modules.panier.dto.PanierDTO;
import com.tissenza.tissenza_backend.modules.panier.dto.AjoutPanierRequest;
import com.tissenza.tissenza_backend.modules.panier.entity.Panier;
import com.tissenza.tissenza_backend.modules.panier.entity.PanierItem;
import com.tissenza.tissenza_backend.modules.panier.entity.Panier.PanierStatus;
import com.tissenza.tissenza_backend.modules.panier.mapper.PanierMapper;
import com.tissenza.tissenza_backend.modules.panier.repository.PanierRepository;
import com.tissenza.tissenza_backend.modules.panier.repository.PanierItemRepository;
import com.tissenza.tissenza_backend.modules.produit.entity.Article;
import com.tissenza.tissenza_backend.modules.produit.repository.ArticleRepository;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.repository.PersonneRepository;
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
public class PanierService {

    private final PanierRepository panierRepository;
    private final PanierItemRepository panierItemRepository;
    private final PanierMapper panierMapper;
    private final ArticleRepository articleRepository;
    private final PersonneRepository personneRepository;

    /**
     * Créer ou récupérer le panier actif d'un client
     */
    @Transactional
    public PanierDTO getOrCreatePanierActif(Long clientId) {
        // Vérifier si le client existe
        Personne client = personneRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + clientId));

        // Chercher un panier existant avec status EN_ATTENTE
        Optional<Panier> panierExistant = panierRepository.findByClientIdAndStatus(clientId, PanierStatus.EN_ATTENTE);

        Panier panier;
        if (panierExistant.isPresent()) {
            panier = panierExistant.get();
            log.info("Panier existant trouvé pour le client {}: {}", clientId, panier.getId());
        } else {
            // Créer un nouveau panier
            panier = new Panier();
            panier.setClient(client);
            panier.setStatus(PanierStatus.EN_ATTENTE);
            panier = panierRepository.save(panier);
            log.info("Nouveau panier créé pour le client {}: {}", clientId, panier.getId());
        }

        return panierMapper.toDTO(panier);
    }

    /**
     * Ajouter un article au panier
     */
    @Transactional
    public PanierDTO ajouterArticleAuPanier(Long clientId, AjoutPanierRequest request) {
        // Récupérer ou créer le panier actif
        Panier panier = panierRepository.findByClientIdAndStatus(clientId, PanierStatus.EN_ATTENTE)
                .orElseGet(() -> {
                    Personne client = personneRepository.findById(clientId)
                            .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + clientId));
                    Panier nouveauPanier = new Panier();
                    nouveauPanier.setClient(client);
                    nouveauPanier.setStatus(PanierStatus.EN_ATTENTE);
                    return panierRepository.save(nouveauPanier);
                });

        // Vérifier si l'article existe
        Article article = articleRepository.findById(request.getArticleId())
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec l'ID: " + request.getArticleId()));

        // Vérifier le stock
        if (article.getStockActuel() < request.getQuantite()) {
            throw new RuntimeException("Stock insuffisant pour l'article: " + article.getSku());
        }

        // Chercher si l'article est déjà dans le panier
        Optional<PanierItem> itemExistant = panierItemRepository.findByPanierIdAndArticleId(panier.getId(), request.getArticleId());

        PanierItem item;
        if (itemExistant.isPresent()) {
            // Mettre à jour la quantité
            item = itemExistant.get();
            int nouvelleQuantite = item.getQuantite() + request.getQuantite();
            
            // Vérifier le stock à nouveau
            if (article.getStockActuel() < nouvelleQuantite) {
                throw new RuntimeException("Stock insuffisant pour l'article: " + article.getSku());
            }
            
            item.setQuantite(nouvelleQuantite);
            log.info("Quantité mise à jour pour l'article {} dans le panier {}: {}", 
                    article.getSku(), panier.getId(), nouvelleQuantite);
        } else {
            // Créer un nouvel item
            item = new PanierItem();
            item.setPanier(panier);
            item.setArticle(article);
            item.setQuantite(request.getQuantite());
            item.setPrixUnitaire(request.getPrixUnitaire());
            log.info("Nouvel article ajouté au panier {}: {} (quantité: {})", 
                    panier.getId(), article.getSku(), request.getQuantite());
        }

        panierItemRepository.save(item);

        // Mettre à jour le total du panier
        updatePanierTotal(panier);

        return panierMapper.toDTO(panier);
    }

    /**
     * Mettre à jour la quantité d'un article dans le panier
     */
    @Transactional
    public PanierDTO mettreAJourQuantite(Long panierId, Long articleId, Integer nouvelleQuantite) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));

        PanierItem item = panierItemRepository.findByPanierIdAndArticleId(panierId, articleId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé dans le panier"));

        // Vérifier le stock
        if (item.getArticle().getStockActuel() < nouvelleQuantite) {
            throw new RuntimeException("Stock insuffisant pour l'article: " + item.getArticle().getSku());
        }

        if (nouvelleQuantite <= 0) {
            // Supprimer l'item si la quantité est 0 ou négative
            panierItemRepository.delete(item);
            log.info("Article supprimé du panier {}: {}", item.getArticle().getSku(), panierId);
        } else {
            item.setQuantite(nouvelleQuantite);
            panierItemRepository.save(item);
            log.info("Quantité mise à jour pour l'article {} dans le panier {}: {}", 
                    item.getArticle().getSku(), panierId, nouvelleQuantite);
        }

        // Mettre à jour le total du panier
        updatePanierTotal(panier);

        return panierMapper.toDTO(panier);
    }

    /**
     * Supprimer un article du panier
     */
    @Transactional
    public PanierDTO supprimerArticleDuPanier(Long panierId, Long articleId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));

        PanierItem item = panierItemRepository.findByPanierIdAndArticleId(panierId, articleId)
                .orElseThrow(() -> new RuntimeException("Article non trouvé dans le panier"));

        panierItemRepository.delete(item);
        log.info("Article supprimé du panier {}: {}", item.getArticle().getSku(), panierId);

        // Mettre à jour le total du panier
        updatePanierTotal(panier);

        return panierMapper.toDTO(panier);
    }

    /**
     * Vider le panier
     */
    @Transactional
    public void viderPanier(Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));

        panierItemRepository.deleteByPanierId(panierId);
        panier.setTotal(BigDecimal.ZERO);
        panierRepository.save(panier);
        
        log.info("Panier vidé: {}", panierId);
    }

    /**
     * Valider le panier (changer le statut à VALIDE)
     */
    @Transactional
    public PanierDTO validerPanier(Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));

        panier.setStatus(PanierStatus.VALIDE);
        panierRepository.save(panier);
        
        log.info("Panier validé: {}", panierId);
        return panierMapper.toDTO(panier);
    }

    /**
     * Récupérer un panier par ID
     */
    @Transactional(readOnly = true)
    public PanierDTO getPanierById(Long panierId) {
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new RuntimeException("Panier non trouvé avec l'ID: " + panierId));
        return panierMapper.toDTO(panier);
    }

    /**
     * Récupérer tous les paniers d'un client
     */
    @Transactional(readOnly = true)
    public List<PanierDTO> getPaniersByClient(Long clientId) {
        List<Panier> paniers = panierRepository.findByClientId(clientId);
        return panierMapper.toDTOList(paniers);
    }

    /**
     * Mettre à jour le total du panier
     */
    private void updatePanierTotal(Panier panier) {
        BigDecimal total = panierItemRepository.calculateTotalByPanierId(panier.getId());
        panier.setTotal(total != null ? total : BigDecimal.ZERO);
        panierRepository.save(panier);
    }
}
