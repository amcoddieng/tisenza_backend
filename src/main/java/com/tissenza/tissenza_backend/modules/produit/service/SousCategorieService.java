package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.entity.SousCategorie;
import com.tissenza.tissenza_backend.modules.produit.repository.SousCategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SousCategorieService {

    private final SousCategorieRepository sousCategorieRepository;

    public SousCategorie createSousCategorie(SousCategorie sousCategorie) {
        return sousCategorieRepository.save(sousCategorie);
    }

    public Optional<SousCategorie> getSousCategorieById(Long id) {
        return sousCategorieRepository.findById(id);
    }

    public List<SousCategorie> getAllSousCategories() {
        return sousCategorieRepository.findAll();
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
