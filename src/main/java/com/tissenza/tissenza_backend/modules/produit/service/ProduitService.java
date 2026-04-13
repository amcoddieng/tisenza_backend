package com.tissenza.tissenza_backend.modules.produit.service;

import com.tissenza.tissenza_backend.modules.produit.entity.Produit;
import com.tissenza.tissenza_backend.modules.produit.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProduitService {

    private final ProduitRepository produitRepository;

    public Produit createProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public Optional<Produit> getProduitByIdWithArticles(Long id) {
        return produitRepository.findByIdWithArticles(id);
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
                    produit.setImage(produitDetails.getImage());
                    produit.setStatut(produitDetails.getStatut());
                    return produitRepository.save(produit);
                })
                .orElseThrow(() -> new RuntimeException("Produit not found with id: " + id));
    }

    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    public List<Produit> getProduitsByBoutiqueId(Long boutiqueId) {
        return produitRepository.findByBoutiqueId(boutiqueId);
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

    public List<Produit> getProduitsByBoutiqueIdAndStatut(Long boutiqueId, Produit.Statut statut) {
        return produitRepository.findByBoutiqueIdAndStatut(boutiqueId, statut);
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

    public Produit deactivateProduit(Long id) {
        return produitRepository.findById(id)
                .map(produit -> {
                    produit.setStatut(Produit.Statut.INACTIF);
                    return produitRepository.save(produit);
                })
                .orElseThrow(() -> new RuntimeException("Produit not found with id: " + id));
    }
}
