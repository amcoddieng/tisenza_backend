package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.dto.ProduitDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import com.tissenza.tissenza_backend.modules.produit.mapper.ProduitMapper;
import com.tissenza.tissenza_backend.modules.produit.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    public Produit createProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    /**
     * Crée un produit et le convertit en DTO
     */
    @Transactional
    public ProduitDTO createProduitDTO(Produit produit) {
        try {
            Produit createdProduit = produitRepository.save(produit);
            log.debug("Création du produit avec ID: {}", createdProduit.getId());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ProduitDTO produitDTO = produitMapper.toDTO(createdProduit);
            log.debug("Conversion du produit en DTO réussie");
            
            return produitDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la création du produit DTO", e);
            throw new RuntimeException("Impossible de créer le produit", e);
        }
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public Optional<Produit> getProduitByIdWithArticles(Long id) {
        return produitRepository.findByIdWithArticles(id);
    }

    /**
     * Récupère un produit avec ses articles et le convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<ProduitDTO> getProduitByIdWithArticlesDTO(Long id) {
        try {
            return produitRepository.findByIdWithArticles(id)
                    .map(produit -> {
                        log.debug("Récupération du produit avec articles ID: {}", id);
                        return produitMapper.toDTO(produit);
                    });
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du produit DTO avec articles: {}", id, e);
            throw new RuntimeException("Impossible de récupérer le produit", e);
        }
    }

    /**
     * Récupère un produit par nom et le convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<ProduitDTO> getProduitByNomDTO(String nom) {
        try {
            Optional<Produit> produit = produitRepository.findByNom(nom);
            if (produit.isPresent()) {
                ProduitDTO dto = produitMapper.toDTO(produit.get());
                log.debug("Conversion du produit '{}' en DTO réussie", nom);
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du produit DTO par nom: {}", nom, e);
            throw new RuntimeException("Impossible de récupérer le produit", e);
        }
    }

    /**
     * Récupère les produits d'une sous-catégorie et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> getProduitsBySousCategorieIdDTO(Long sousCategorieId) {
        try {
            List<Produit> produits = produitRepository.findBySousCategorieId(sousCategorieId);
            log.debug("Récupération de {} produits pour la sous-catégorie ID {}", produits.size(), sousCategorieId);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion de {} produits en DTO réussie", produitsDTO.size());
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des produits DTO par sous-catégorie ID: {}", sousCategorieId, e);
            throw new RuntimeException("Impossible de récupérer les produits", e);
        }
    }

    /**
     * Récupère les produits par statut et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> getProduitsByStatutDTO(Produit.Statut statut) {
        try {
            List<Produit> produits = produitRepository.findByStatut(statut);
            log.debug("Récupération de {} produits avec le statut {}", produits.size(), statut);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion de {} produits en DTO réussie", produitsDTO.size());
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des produits DTO par statut: {}", statut, e);
            throw new RuntimeException("Impossible de récupérer les produits", e);
        }
    }

    /**
     * Recherche des produits par critères multiples (boutique + sous-catégorie + nom) et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> searchProduitsByMultipleCriteriaDTO(Long boutiqueId, Long sousCategorieId, String nom) {
        try {
            List<Produit> produits = produitRepository.searchByMultipleCriteria(boutiqueId, sousCategorieId, nom);
            log.debug("Recherche de {} produits avec boutiqueId={}, sousCategorieId={}, nom={}", 
                     produits.size(), boutiqueId, sousCategorieId, nom);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion de {} produits en DTO réussie", produitsDTO.size());
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des produits DTO par critères multiples: boutiqueId={}, sousCategorieId={}, nom={}", 
                     boutiqueId, sousCategorieId, nom, e);
            throw new RuntimeException("Impossible de rechercher les produits", e);
        }
    }

    /**
     * Recherche des produits par boutique, sous-catégorie et nom (LIKE) et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> searchProduitsByBoutiqueSousCategorieAndNomDTO(Long boutiqueId, Long sousCategorieId, String nom) {
        try {
            List<Produit> produits = produitRepository.searchByBoutiqueSousCategorieAndNom(boutiqueId, sousCategorieId, nom);
            log.debug("Recherche de {} produits pour boutiqueId={}, sousCategorieId={}, nom={}", 
                     produits.size(), boutiqueId, sousCategorieId, nom);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion de {} produits en DTO réussie", produitsDTO.size());
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des produits DTO par boutique, sous-catégorie et nom: boutiqueId={}, sousCategorieId={}, nom={}", 
                     boutiqueId, sousCategorieId, nom, e);
            throw new RuntimeException("Impossible de rechercher les produits", e);
        }
    }

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Produit updateProduit(Long id, Produit produitDetails) {
        return produitRepository.findById(id)
                .map(produit -> {
                    produit.setBoutique(produitDetails.getBoutique());
                    produit.setSousCategorie(produitDetails.getSousCategorie());
                    produit.setNom(produitDetails.getNom());
                    produit.setDescription(produitDetails.getDescription());
                    produit.setStatut(produitDetails.getStatut());
                    produit.setImage(produitDetails.getImage());
                    return produitRepository.save(produit);
                })
                .orElseThrow(() -> new RuntimeException("Produit not found with id: " + id));
    }

    /**
     * Met à jour un produit et le convertit en DTO
     */
    @Transactional
    public ProduitDTO updateProduitDTO(Long id, Produit produitDetails) {
        try {
            Produit updatedProduit = updateProduit(id, produitDetails);
            log.debug("Mise à jour du produit avec ID: {}", updatedProduit.getId());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ProduitDTO produitDTO = produitMapper.toDTO(updatedProduit);
            log.debug("Conversion du produit en DTO réussie");
            
            return produitDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du produit DTO: {}", id, e);
            throw new RuntimeException("Impossible de mettre à jour le produit", e);
        }
    }

    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> getProduitsByBoutiqueId(Long boutiqueId) {
        return produitRepository.findByBoutiqueId(boutiqueId);
    }

    /**
     * Récupère les produits d'une boutique et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> getProduitsByBoutiqueIdDTO(Long boutiqueId) {
        try {
            List<Produit> produits = produitRepository.findByBoutiqueId(boutiqueId);
            log.debug("Récupération de {} produits pour la boutique {}", produits.size(), boutiqueId);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion des produits en DTO réussie");
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des produits DTO de la boutique: {}", boutiqueId, e);
            throw new RuntimeException("Impossible de récupérer les produits", e);
        }
    }

    public List<Produit> getProduitsBySousCategorieId(Long sousCategorieId) {
        return produitRepository.findBySousCategorieId(sousCategorieId);
    }

    public List<Produit> getProduitsByStatut(Produit.Statut statut) {
        return produitRepository.findByStatut(statut);
    }

    public Optional<Produit> getProduitByNom(String nom) {
        return produitRepository.findByNom(nom);
    }

    public List<Produit> searchProduitsByNom(String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Produit> searchProduitsByKeyword(String keyword) {
        return produitRepository.searchByKeyword(keyword);
    }

    /**
     * Récupère tous les produits et les convertit en DTO
     * Cette méthode gère les relations lazy pour éviter la LazyInitializationException
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> getAllProduitsDTO() {
        try {
            List<Produit> produits = produitRepository.findAll();
            log.debug("Récupération de {} produits", produits.size());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion de {} produits en DTO réussie", produitsDTO.size());
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des produits DTO", e);
            throw new RuntimeException("Impossible de récupérer les produits", e);
        }
    }

    /**
     * Recherche des produits par nom et les convertit en DTO
     * Initialise explicitement les relations lazy
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> searchProduitsByNomDTO(String nom) {
        try {
            List<Produit> produits = produitRepository.findByNomContainingIgnoreCase(nom);
            log.debug("Recherche de {} produits avec le nom: {}", produits.size(), nom);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion de {} produits en DTO réussie", produitsDTO.size());
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des produits DTO par nom: {}", nom, e);
            throw new RuntimeException("Impossible de rechercher les produits", e);
        }
    }

    /**
     * Recherche des produits par mot-clé et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> searchProduitsByKeywordDTO(String keyword) {
        try {
            List<Produit> produits = produitRepository.searchByKeyword(keyword);
            log.debug("Recherche de {} produits avec le mot-clé: {}", produits.size(), keyword);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion de {} produits en DTO réussie", produitsDTO.size());
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des produits DTO par mot-clé: {}", keyword, e);
            throw new RuntimeException("Impossible de rechercher les produits", e);
        }
    }

    /**
     * Récupère un produit par ID et le convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<ProduitDTO> getProduitByIdDTO(Long id) {
        try {
            Optional<Produit> produit = produitRepository.findById(id);
            if (produit.isPresent()) {
                ProduitDTO dto = produitMapper.toDTO(produit.get());
                log.debug("Conversion du produit ID {} en DTO réussie", id);
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération du produit DTO par ID: {}", id, e);
            throw new RuntimeException("Impossible de récupérer le produit", e);
        }
    }

    public List<Produit> getProduitsByBoutiqueIdAndStatut(Long boutiqueId, Produit.Statut statut) {
        return produitRepository.findByBoutiqueIdAndStatut(boutiqueId, statut);
    }

    /**
     * Récupère les produits d'une boutique par statut et les convertit en DTOs
     */
    @Transactional(readOnly = true)
    public List<ProduitDTO> getProduitsByBoutiqueIdAndStatutDTO(Long boutiqueId, Produit.Statut statut) {
        try {
            List<Produit> produits = produitRepository.findByBoutiqueIdAndStatut(boutiqueId, statut);
            log.debug("Récupération de {} produits pour la boutique {} avec statut {}", produits.size(), boutiqueId, statut);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<ProduitDTO> produitsDTO = produitMapper.toDTOList(produits);
            log.debug("Conversion des produits en DTO réussie");
            
            return produitsDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des produits DTO de la boutique: {} avec statut: {}", boutiqueId, statut, e);
            throw new RuntimeException("Impossible de récupérer les produits", e);
        }
    }

    public long countByBoutiqueId(Long boutiqueId) {
        return produitRepository.countByBoutiqueId(boutiqueId);
    }

    public long countBySousCategorieId(Long sousCategorieId) {
        return produitRepository.countBySousCategorieId(sousCategorieId);
    }

    public long countByStatut(Produit.Statut statut) {
        return produitRepository.countByStatut(statut);
    }

    public List<Object[]> getStatisticsByStatut() {
        return produitRepository.getStatisticsByStatut();
    }

    public boolean existsById(Long id) {
        return produitRepository.existsById(id);
    }

    public boolean existsByNom(String nom) {
        return produitRepository.findByNom(nom).isPresent();
    }

    public Produit activateProduit(Long id) {
        return produitRepository.findById(id)
                .map(produit -> {
                    produit.setStatut(Produit.Statut.ACTIF);
                    return produitRepository.save(produit);
                })
                .orElseThrow(() -> new RuntimeException("Produit not found with id: " + id));
    }

    /**
     * Active un produit et le convertit en DTO
     */
    @Transactional
    public ProduitDTO activateProduitDTO(Long id) {
        try {
            Produit activatedProduit = activateProduit(id);
            log.debug("Activation du produit avec ID: {}", id);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ProduitDTO produitDTO = produitMapper.toDTO(activatedProduit);
            log.debug("Conversion du produit en DTO réussie");
            
            return produitDTO;
        } catch (Exception e) {
            log.error("Erreur lors de l'activation du produit DTO: {}", id, e);
            throw new RuntimeException("Impossible d'activer le produit", e);
        }
    }

    public Produit deactivateProduit(Long id) {
        return produitRepository.findById(id)
                .map(produit -> {
                    produit.setStatut(Produit.Statut.INACTIF);
                    return produitRepository.save(produit);
                })
                .orElseThrow(() -> new RuntimeException("Produit not found with id: " + id));
    }

    /**
     * Désactive un produit et le convertit en DTO
     */
    @Transactional
    public ProduitDTO deactivateProduitDTO(Long id) {
        try {
            Produit deactivatedProduit = deactivateProduit(id);
            log.debug("Désactivation du produit avec ID: {}", id);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            ProduitDTO produitDTO = produitMapper.toDTO(deactivatedProduit);
            log.debug("Conversion du produit en DTO réussie");
            
            return produitDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la désactivation du produit DTO: {}", id, e);
            throw new RuntimeException("Impossible de désactiver le produit", e);
        }
    }
}
