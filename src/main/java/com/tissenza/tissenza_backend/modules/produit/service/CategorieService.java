package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.dto.CategorieDTO;
import com.tissenza.tissenza_backend.modules.produit.entity.Categorie;
import com.tissenza.tissenza_backend.modules.produit.mapper.CategorieMapper;
import com.tissenza.tissenza_backend.modules.produit.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategorieService {

    private final CategorieRepository categorieRepository;
    private final CategorieMapper categorieMapper;

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Optional<Categorie> getCategorieById(Long id) {
        return categorieRepository.findById(id);
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    /**
     * Récupère toutes les catégories et les convertit en DTO
     * Cette méthode gère les relations lazy pour éviter la LazyInitializationException
     */
    @Transactional(readOnly = true)
    public List<CategorieDTO> getAllCategoriesDTO() {
        try {
            List<Categorie> categories = categorieRepository.findAll();
            log.debug("Récupération de {} catégories", categories.size());
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<CategorieDTO> categoriesDTO = categorieMapper.toCategorieDTOList(categories);
            log.debug("Conversion de {} catégories en DTO réussie", categoriesDTO.size());
            
            return categoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des catégories DTO", e);
            throw new RuntimeException("Impossible de récupérer les catégories", e);
        }
    }

    /**
     * Récupère toutes les catégories avec leurs sous-catégories et les convertit en DTO
     * Initialise explicitement les relations lazy
     */
    @Transactional(readOnly = true)
    public List<CategorieDTO> getAllCategoriesWithSousCategoriesDTO() {
        try {
            List<Categorie> categories = categorieRepository.findAllWithSousCategories();
            log.debug("Récupération de {} catégories avec sous-catégories", categories.size());
            
            // Les sous-catégories sont déjà initialisées par la requête personnalisée
            List<CategorieDTO> categoriesDTO = categorieMapper.toCategorieDTOList(categories);
            log.debug("Conversion de {} catégories avec sous-catégories en DTO réussie", categoriesDTO.size());
            
            return categoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des catégories avec sous-catégories DTO", e);
            throw new RuntimeException("Impossible de récupérer les catégories avec sous-catégories", e);
        }
    }

    /**
     * Récupère une catégorie par ID et la convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<CategorieDTO> getCategorieByIdDTO(Long id) {
        try {
            Optional<Categorie> categorie = categorieRepository.findById(id);
            if (categorie.isPresent()) {
                CategorieDTO dto = categorieMapper.toCategorieDTO(categorie.get());
                log.debug("Conversion de la catégorie ID {} en DTO réussie", id);
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la catégorie DTO par ID: {}", id, e);
            throw new RuntimeException("Impossible de récupérer la catégorie", e);
        }
    }

    public Categorie updateCategorie(Long id, Categorie categorieDetails) {
        return categorieRepository.findById(id)
                .map(categorie -> {
                    categorie.setNom(categorieDetails.getNom());
                    categorie.setDescription(categorieDetails.getDescription());
                    categorie.setImage(categorieDetails.getImage());
                    return categorieRepository.save(categorie);
                })
                .orElseThrow(() -> new RuntimeException("Categorie not found with id: " + id));
    }

    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }

    public Optional<Categorie> getCategorieByNom(String nom) {
        return categorieRepository.findByNom(nom);
    }

    public List<Categorie> searchCategoriesByNom(String nom) {
        return categorieRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Categorie> searchCategoriesByKeyword(String keyword) {
        return categorieRepository.searchByKeyword(keyword);
    }

    /**
     * Recherche des catégories par nom et les convertit en DTO
     * Initialise explicitement les relations lazy
     */
    @Transactional(readOnly = true)
    public List<CategorieDTO> searchCategoriesByNomDTO(String nom) {
        try {
            List<Categorie> categories = categorieRepository.findByNomContainingIgnoreCase(nom);
            log.debug("Recherche de {} catégories avec le nom: {}", categories.size(), nom);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<CategorieDTO> categoriesDTO = categorieMapper.toCategorieDTOList(categories);
            log.debug("Conversion de {} catégories en DTO réussie", categoriesDTO.size());
            
            return categoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des catégories DTO par nom: {}", nom, e);
            throw new RuntimeException("Impossible de rechercher les catégories", e);
        }
    }

    /**
     * Recherche des catégories par mot-clé et les convertit en DTO
     */
    @Transactional(readOnly = true)
    public List<CategorieDTO> searchCategoriesByKeywordDTO(String keyword) {
        try {
            List<Categorie> categories = categorieRepository.searchByKeyword(keyword);
            log.debug("Recherche de {} catégories avec le mot-clé: {}", categories.size(), keyword);
            
            // Conversion en DTO dans la transaction pour éviter les problèmes de lazy loading
            List<CategorieDTO> categoriesDTO = categorieMapper.toCategorieDTOList(categories);
            log.debug("Conversion de {} catégories en DTO réussie", categoriesDTO.size());
            
            return categoriesDTO;
        } catch (Exception e) {
            log.error("Erreur lors de la recherche des catégories DTO par mot-clé: {}", keyword, e);
            throw new RuntimeException("Impossible de rechercher les catégories", e);
        }
    }

    /**
     * Récupère une catégorie par nom et la convertit en DTO
     */
    @Transactional(readOnly = true)
    public Optional<CategorieDTO> getCategorieByNomDTO(String nom) {
        try {
            Optional<Categorie> categorie = categorieRepository.findByNom(nom);
            if (categorie.isPresent()) {
                CategorieDTO dto = categorieMapper.toCategorieDTO(categorie.get());
                log.debug("Conversion de la catégorie '{}' en DTO réussie", nom);
                return Optional.of(dto);
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la catégorie DTO par nom: {}", nom, e);
            throw new RuntimeException("Impossible de récupérer la catégorie", e);
        }
    }

    public long countCategories() {
        return categorieRepository.countCategories();
    }

    public List<Categorie> getAllCategoriesWithSousCategories() {
        return categorieRepository.findAllWithSousCategories();
    }

    public boolean existsById(Long id) {
        return categorieRepository.existsById(id);
    }

    public boolean existsByNom(String nom) {
        return categorieRepository.findByNom(nom).isPresent();
    }
}
