package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.entity.Categorie;
import com.tissenza.tissenza_backend.modules.produit.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategorieService {

    private final CategorieRepository categorieRepository;

    public Categorie createCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public Optional<Categorie> getCategorieById(Long id) {
        return categorieRepository.findById(id);
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
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
