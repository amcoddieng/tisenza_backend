package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.dto.SousCategorieDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import com.tissenza.tissenza_backend.modules.produit.mapper.CategorieMapper;
import com.tissenza.tissenza_backend.modules.produit.repository.SousCategorieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SousCategorieService {

    private final SousCategorieRepository sousCategorieRepository;
    private final CategorieMapper categorieMapper;

    public SousCategorie createSousCategorie(SousCategorie sousCategorie) {
        return sousCategorieRepository.save(sousCategorie);
    }

    public Optional<SousCategorie> getSousCategorieById(Long id) {
        return sousCategorieRepository.findById(id);
    }

    public List<SousCategorie> getAllSousCategories() {
        return sousCategorieRepository.findAll();
    }

    /**
     * Récupère toutes les sous-catégories et les convertit en DTO
     * Cette méthode gère les relations lazy pour éviter la LazyInitializationException
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> getAllSousCategoriesDTO() {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findAll();
            log.debug("Récupération de {} sous-catégories", sousCategories.size());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<SousCategorieDTO> sousCategoriesDTO = categorieMapper.toSousCategorieDTOList(sousCategories);
            log.debug("Conversion de {} sous-catégories en DTO réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des sous-catégories DTO", e);
            throw new RuntimeException("Impossible de récupérer les sous-catégories", e);
        }
    }

    /**
     * Récupère les sous-catégories d'une catégorie et les convertit en DTO
     * Initialise explicitement les relations lazy
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> getSousCategoriesByCategorieIdDTO(Long categorieId) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findByCategorieId(categorieId);
            log.debug("Récupération de {} sous-catégories pour la catégorie ID {}", sousCategories.size(), categorieId);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<SousCategorieDTO> sousCategoriesDTO = categorieMapper.toSousCategorieDTOList(sousCategories);
            log.debug("Conversion de {} sous-catégories en DTO réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des sous-catégories DTO par catégorie ID: {}", categorieId, e);
            throw new RuntimeException("Impossible de récupérer les sous-catégories", e);
        }
    }

    /**
     * Récupère les sous-catégories d'une catégorie avec produits et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> getSousCategoriesByCategorieIdWithProduitsDTO(Long categorieId) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findByCategorieIdWithProduits(categorieId);
            log.debug("Récupération de {} sous-catégories avec produits pour la catégorie ID {}", sousCategories.size(), categorieId);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<SousCategorieDTO> sousCategoriesDTO = categorieMapper.toSousCategorieDTOList(sousCategories);
            log.debug("Conversion de {} sous-catégories avec produits en DTO réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des sous-catégories avec produits DTO par catégorie ID: {}", categorieId, e);
            throw new RuntimeException("Impossible de récupérer les sous-catégories avec produits", e);
        }
    }

    public SousCategorie updateSousCategorie(Long id, SousCategorie sousCategorieDetails) {
        return sousCategorieRepository.findById(id)
                .map(sousCategorie -> {
                    sousCategorie.setCategorie(sousCategorieDetails.getCategorie());
                    sousCategorie.setNom(sousCategorieDetails.getNom());
                    sousCategorie.setDescription(sousCategorieDetails.getDescription());
                    return sousCategorieRepository.save(sousCategorie);
                })
                .orElseThrow(() -> new RuntimeException("SousCategorie not found with id: " + id));
    }

    public void deleteSousCategorie(Long id) {
        sousCategorieRepository.deleteById(id);
    }

    public List<SousCategorie> getSousCategoriesByCategorieId(Long categorieId) {
        return sousCategorieRepository.findByCategorieId(categorieId);
    }

    public Optional<SousCategorie> getSousCategorieByNom(String nom) {
        return sousCategorieRepository.findByNom(nom);
    }

    public List<SousCategorie> searchSousCategoriesByNom(String nom) {
        return sousCategorieRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<SousCategorie> searchSousCategoriesByKeyword(String keyword) {
        return sousCategorieRepository.searchByKeyword(keyword);
    }

    /**
     * Récupère une sous-catégorie par nom et la convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<SousCategorieDTO> getSousCategorieByNomDTO(String nom) {
        try {
            Optional<SousCategorie> sousCategorie = sousCategorieRepository.findByNom(nom);
            if (sousCategorie.isPresent()) {
                SousCategorieDTO dto = categorieMapper.toSousCategorieDTO(sousCategorie.get());
                log.debug("Conversion de la sous-catégorie '{}' en DTO réussie", nom);
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la sous-catégorie DTO par nom: {}", nom, e);
            throw new RuntimeException("Impossible de récupérer la sous-catégorie", e);
        }
    }

    /**
     * Recherche des sous-catégories par nom et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> searchSousCategoriesByNomDTO(String nom) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findByNomContainingIgnoreCase(nom);
            log.debug("Recherche de {} sous-catégories avec le nom: {}", sousCategories.size(), nom);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<SousCategorieDTO> sousCategoriesDTO = categorieMapper.toSousCategorieDTOList(sousCategories);
            log.debug("Conversion de {} sous-catégories en DTO réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des sous-catégories DTO par nom: {}", nom, e);
            throw new RuntimeException("Impossible de rechercher les sous-catégories", e);
        }
    }

    /**
     * Recherche des sous-catégories par mot-clé et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> searchSousCategoriesByKeywordDTO(String keyword) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.searchByKeyword(keyword);
            log.debug("Recherche de {} sous-catégories avec le mot-clé: {}", sousCategories.size(), keyword);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<SousCategorieDTO> sousCategoriesDTO = categorieMapper.toSousCategorieDTOList(sousCategories);
            log.debug("Conversion de {} sous-catégories en DTO réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des sous-catégories DTO par mot-clé: {}", keyword, e);
            throw new RuntimeException("Impossible de rechercher les sous-catégories", e);
        }
    }

    /**
     * Récupère une sous-catégorie par ID et la convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<SousCategorieDTO> getSousCategorieByIdDTO(Long id) {
        try {
            Optional<SousCategorie> sousCategorie = sousCategorieRepository.findById(id);
            if (sousCategorie.isPresent()) {
                SousCategorieDTO dto = categorieMapper.toSousCategorieDTO(sousCategorie.get());
                log.debug("Conversion de la sous-catégorie ID {} en DTO réussie", id);
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la sous-catégorie DTO par ID: {}", id, e);
            throw new RuntimeException("Impossible de récupérer la sous-catégorie", e);
        }
    }

    /**
     * Récupère une sous-catégorie par ID et la convertit en DTO avec informations de catégorie
     */
    @Transactional(readOnly = true)
    public Optional<SousCategorieDTO> getSousCategorieByIdWithCategorieDTO(Long id) {
        try {
            Optional<SousCategorie> sousCategorie = sousCategorieRepository.findById(id);
            if (sousCategorie.isPresent()) {
                SousCategorieDTO dto = categorieMapper.toSousCategorieDTO(sousCategorie.get(), true);
                log.debug("Conversion de la sous-catégorie ID {} en DTO avec catégorie réussie", id);
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la sous-catégorie DTO avec catégorie par ID: {}", id, e);
            throw new RuntimeException("Impossible de récupérer la sous-catégorie", e);
        }
    }

    /**
     * Récupère toutes les sous-catégories et les convertit en DTO avec informations de catégorie
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> getAllSousCategoriesWithCategorieDTO() {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findAll();
            log.debug("Récupération de {} sous-catégories", sousCategories.size());
            
            // Conversion en DTO avec informations de catégorie dans la transaction
            List<SousCategorieDTO> sousCategoriesDTO = sousCategories.stream()
                    .map(sc -> categorieMapper.toSousCategorieDTO(sc, true))
                    .collect(java.util.stream.Collectors.toList());
            log.debug("Conversion de {} sous-catégories en DTO avec catégorie réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des sous-catégories DTO avec catégorie", e);
            throw new RuntimeException("Impossible de récupérer les sous-catégories", e);
        }
    }

    /**
     * Recherche des sous-catégories par nom et les convertit en DTO avec informations de catégorie
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> searchSousCategoriesByNomWithCategorieDTO(String nom) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findByNomContainingIgnoreCase(nom);
            log.debug("Recherche de {} sous-catégories avec le nom: {}", sousCategories.size(), nom);
            
            // Conversion en DTO avec informations de catégorie dans la transaction
            List<SousCategorieDTO> sousCategoriesDTO = sousCategories.stream()
                    .map(sc -> categorieMapper.toSousCategorieDTO(sc, true))
                    .collect(java.util.stream.Collectors.toList());
            log.debug("Conversion de {} sous-catégories en DTO avec catégorie réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des sous-catégories DTO avec catégorie par nom: {}", nom, e);
            throw new RuntimeException("Impossible de rechercher les sous-catégories", e);
        }
    }

    /**
     * Recherche des sous-catégories par mot-clé et les convertit en DTO avec informations de catégorie
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> searchSousCategoriesByKeywordWithCategorieDTO(String keyword) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.searchByKeyword(keyword);
            log.debug("Recherche de {} sous-catégories avec le mot-clé: {}", sousCategories.size(), keyword);
            
            // Conversion en DTO avec informations de catégorie dans la transaction
            List<SousCategorieDTO> sousCategoriesDTO = sousCategories.stream()
                    .map(sc -> categorieMapper.toSousCategorieDTO(sc, true))
                    .collect(java.util.stream.Collectors.toList());
            log.debug("Conversion de {} sous-catégories en DTO avec catégorie réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des sous-catégories DTO avec catégorie par mot-clé: {}", keyword, e);
            throw new RuntimeException("Impossible de rechercher les sous-catégories", e);
        }
    }

    /**
     * Recherche des sous-catégories par ID de catégorie et les convertit en DTO avec informations de catégorie
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> searchSousCategoriesByCategorieIdWithCategorieDTO(Long categorieId) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findByCategorieId(categorieId);
            log.debug("Recherche de {} sous-catégories pour la catégorie ID: {}", sousCategories.size(), categorieId);
            
            // Conversion en DTO avec informations de catégorie dans la transaction
            List<SousCategorieDTO> sousCategoriesDTO = sousCategories.stream()
                    .map(sc -> categorieMapper.toSousCategorieDTO(sc, true))
                    .collect(java.util.stream.Collectors.toList());
            log.debug("Conversion de {} sous-catégories en DTO avec catégorie réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des sous-catégories DTO avec catégorie par ID catégorie: {}", categorieId, e);
            throw new RuntimeException("Impossible de rechercher les sous-catégories", e);
        }
    }

    /**
     * Recherche des sous-catégories par ID de catégorie et mot-clé dans le nom
     */
    @Transactional(readOnly = true)
    public List<SousCategorieDTO> searchSousCategoriesByCategorieIdAndNomWithCategorieDTO(Long categorieId, String nom) {
        try {
            List<SousCategorie> sousCategories = sousCategorieRepository.findByCategorieIdAndNomContainingIgnoreCase(categorieId, nom);
            log.debug("Recherche de {} sous-catégories pour la catégorie ID {} avec nom: {}", sousCategories.size(), categorieId, nom);
            
            // Conversion en DTO avec informations de catégorie dans la transaction
            List<SousCategorieDTO> sousCategoriesDTO = sousCategories.stream()
                    .map(sc -> categorieMapper.toSousCategorieDTO(sc, true))
                    .collect(java.util.stream.Collectors.toList());
            log.debug("Conversion de {} sous-catégories en DTO avec catégorie réussie", sousCategoriesDTO.size());
            
            return sousCategoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des sous-catégories DTO avec catégorie par ID catégorie et nom: {} {}", categorieId, nom, e);
            throw new RuntimeException("Impossible de rechercher les sous-catégories", e);
        }
    }

    public long countByCategorieId(Long categorieId) {
        return sousCategorieRepository.countByCategorieId(categorieId);
    }

    public List<SousCategorie> getSousCategoriesByCategorieIdWithProduits(Long categorieId) {
        return sousCategorieRepository.findByCategorieIdWithProduits(categorieId);
    }

    public boolean existsById(Long id) {
        return sousCategorieRepository.existsById(id);
    }

    public boolean existsByNom(String nom) {
        return sousCategorieRepository.findByNom(nom).isPresent();
    }
}
