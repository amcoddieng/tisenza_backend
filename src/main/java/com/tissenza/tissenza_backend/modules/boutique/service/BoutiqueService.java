package com.tissenza.tissenza_backend.modules.boutique.service;

import com.tissenza.tissenza_backend.modules.boutique.dto.BoutiqueDTO;
import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import com.tissenza.tissenza_backend.modules.boutique.mapper.BoutiqueMapper;
import com.tissenza.tissenza_backend.modules.boutique.repository.BoutiqueRepository;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoutiqueService {

    private final BoutiqueRepository boutiqueRepository;
    private final BoutiqueMapper boutiqueMapper;
    private final com.tissenza.tissenza_backend.modules.user.repository.CompteRepository compteRepository;

    /**
     * Crée une nouvelle boutique
     * @param boutiqueDTO les informations de la boutique
     * @return le DTO de la boutique créée
     */
    @Transactional
    public BoutiqueDTO createBoutique(BoutiqueDTO boutiqueDTO) {
        Boutique boutique = boutiqueMapper.toEntity(boutiqueDTO);
        
        // Récupération du vendeur si spécifié
        if (boutiqueDTO.getVendeurId() != null) {
            Compte vendeur = compteRepository.findById(boutiqueDTO.getVendeurId())
                    .orElseThrow(() -> new RuntimeException("Vendeur not found with id: " + boutiqueDTO.getVendeurId()));
            boutique.setVendeur(vendeur);
        }
        
        Boutique savedBoutique = boutiqueRepository.save(boutique);
        return boutiqueMapper.toDTO(savedBoutique);
    }

    /**
     * Récupère une boutique par son ID
     * @param id l'ID de la boutique
     * @return le DTO de la boutique ou null si non trouvée
     */
    @Transactional(readOnly = true)
    public Optional<BoutiqueDTO> getBoutiqueById(Long id) {
        return boutiqueRepository.findById(id)
                .map(boutiqueMapper::toDTO);
    }

    /**
     * Récupère toutes les boutiques
     * @return la liste des DTOs de boutiques
     */
    @Transactional(readOnly = true)
    public List<BoutiqueDTO> getAllBoutiques() {
        List<Boutique> boutiques = boutiqueRepository.findAll();
        return boutiqueMapper.toDTOList(boutiques);
    }

    /**
     * Met à jour une boutique existante
     * @param id l'ID de la boutique
     * @param boutiqueDTO les nouvelles informations
     * @return le DTO de la boutique mise à jour
     */
    @Transactional
    public BoutiqueDTO updateBoutique(Long id, BoutiqueDTO boutiqueDTO) {
        return boutiqueRepository.findById(id)
                .map(boutique -> {
                    boutique.setNom(boutiqueDTO.getNom());
                    boutique.setDescription(boutiqueDTO.getDescription());
                    boutique.setNote(boutiqueDTO.getNote());
                    boutique.setAddresse(boutiqueDTO.getAddresse());
                    boutique.setLogo(boutiqueDTO.getLogo());
                    boutique.setStatut(boutiqueDTO.getStatut());
                    
                    // Mise à jour du vendeur si spécifié
                    if (boutiqueDTO.getVendeurId() != null && boutique.getVendeur() != null && 
                        !boutiqueDTO.getVendeurId().equals(boutique.getVendeur().getId())) {
                        Compte nouveauVendeur = compteRepository.findById(boutiqueDTO.getVendeurId())
                                .orElseThrow(() -> new RuntimeException("Vendeur not found with id: " + boutiqueDTO.getVendeurId()));
                        boutique.setVendeur(nouveauVendeur);
                    }
                    
                    return boutiqueRepository.save(boutique);
                })
                .map(boutiqueMapper::toDTO)
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
