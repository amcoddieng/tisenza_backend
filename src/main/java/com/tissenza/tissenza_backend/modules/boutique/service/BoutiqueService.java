package com.tissenza.tissenza_backend.modules.boutique.service;

import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import com.tissenza.tissenza_backend.modules.boutique.repository.BoutiqueRepository;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoutiqueService {

    private final BoutiqueRepository boutiqueRepository;

    public Boutique createBoutique(Boutique boutique) {
        return boutiqueRepository.save(boutique);
    }

    public Optional<Boutique> getBoutiqueById(Long id) {
        return boutiqueRepository.findById(id);
    }

    public List<Boutique> getAllBoutiques() {
        return boutiqueRepository.findAll();
    }

    public Boutique updateBoutique(Long id, Boutique boutiqueDetails) {
        return boutiqueRepository.findById(id)
                .map(boutique -> {
                    boutique.setNom(boutiqueDetails.getNom());
                    boutique.setDescription(boutiqueDetails.getDescription());
                    boutique.setNote(boutiqueDetails.getNote());
                    boutique.setAddresse(boutiqueDetails.getAddresse());
                    boutique.setLogo(boutiqueDetails.getLogo());
                    boutique.setStatut(boutiqueDetails.getStatut());
                    return boutiqueRepository.save(boutique);
                })
                .orElseThrow(() -> new RuntimeException("Boutique not found with id: " + id));
    }

    public void deleteBoutique(Long id) {
        boutiqueRepository.deleteById(id);
    }

    public Optional<Boutique> getBoutiqueByVendeur(Compte vendeur) {
        return boutiqueRepository.findByVendeur(vendeur);
    }

    public List<Boutique> getBoutiquesByVendeurId(Long vendeurId) {
        return boutiqueRepository.findByVendeurId(vendeurId);
    }

    public List<Boutique> getBoutiquesByStatut(Boutique.Statut statut) {
        return boutiqueRepository.findByStatut(statut);
    }

    public List<Boutique> searchBoutiquesByNom(String nom) {
        return boutiqueRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Boutique> searchBoutiquesByKeyword(String keyword) {
        return boutiqueRepository.searchByKeyword(keyword);
    }

    public List<Boutique> getBoutiquesByNoteMin(Float noteMin) {
        return boutiqueRepository.findByNoteGreaterThanEqual(noteMin);
    }

    public Float getAverageNoteByStatut(Boutique.Statut statut) {
        return boutiqueRepository.getAverageNoteByStatut(statut);
    }

    public long countBoutiquesByStatut(Boutique.Statut statut) {
        return boutiqueRepository.countByStatut(statut);
    }

    public List<Object[]> getStatisticsByStatut() {
        return boutiqueRepository.getStatisticsByStatut();
    }

    public boolean existsByVendeur(Compte vendeur) {
        return boutiqueRepository.existsByVendeur(vendeur);
    }

    public boolean existsByVendeurId(Long vendeurId) {
        return boutiqueRepository.existsByVendeurId(vendeurId);
    }

    public Boutique validateBoutique(Long id) {
        return boutiqueRepository.findById(id)
                .map(boutique -> {
                    boutique.setStatut(Boutique.Statut.VALIDE);
                    return boutiqueRepository.save(boutique);
                })
                .orElseThrow(() -> new RuntimeException("Boutique not found with id: " + id));
    }

    public Boutique refuseBoutique(Long id) {
        return boutiqueRepository.findById(id)
                .map(boutique -> {
                    boutique.setStatut(Boutique.Statut.REFUSE);
                    return boutiqueRepository.save(boutique);
                })
                .orElseThrow(() -> new RuntimeException("Boutique not found with id: " + id));
    }

    public Boutique updateNote(Long id, Float note) {
        return boutiqueRepository.findById(id)
                .map(boutique -> {
                    boutique.setNote(note);
                    return boutiqueRepository.save(boutique);
                })
                .orElseThrow(() -> new RuntimeException("Boutique not found with id: " + id));
    }
}
