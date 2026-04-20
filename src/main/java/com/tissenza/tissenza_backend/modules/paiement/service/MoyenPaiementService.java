package com.tissenza.tissenza_backend.modules.paiement.service;

import com.tissenza.tissenza_backend.modules.paiement.dto.MoyenPaiementDTO;
import com.tissenza.tissenza_backend.modules.paiement.dto.UserMoyenPaiementDTO;
import com.tissenza.tissenza_backend.modules.paiement.entity.MoyenPaiement;
import com.tissenza.tissenza_backend.modules.paiement.entity.UserMoyenPaiement;
import com.tissenza.tissenza_backend.modules.paiement.mapper.PaiementMapper;
import com.tissenza.tissenza_backend.modules.paiement.repository.MoyenPaiementRepository;
import com.tissenza.tissenza_backend.modules.paiement.repository.UserMoyenPaiementRepository;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.repository.CompteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des moyens de paiement et des associations utilisateurs-moyens de paiement
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MoyenPaiementService {

    private final MoyenPaiementRepository moyenPaiementRepository;
    private final UserMoyenPaiementRepository userMoyenPaiementRepository;
    private final CompteRepository compteRepository;
    private final PaiementMapper paiementMapper;

    // ============= GESTION DES MOYENS DE PAIEMENT =============

    /**
     * Crée un nouveau moyen de paiement
     */
    @Transactional
    public MoyenPaiementDTO createMoyenPaiement(MoyenPaiementDTO dto) {
        log.info("Création d'un nouveau moyen de paiement: {}", dto.getNom());
        
        // Vérifier si le nom existe déjà
        if (moyenPaiementRepository.existsByNom(dto.getNom())) {
            throw new IllegalArgumentException("Un moyen de paiement avec ce nom existe déjà: " + dto.getNom());
        }
        
        MoyenPaiement moyenPaiement = paiementMapper.toEntity(dto);
        MoyenPaiement saved = moyenPaiementRepository.save(moyenPaiement);
        
        return paiementMapper.toDTO(saved);
    }

    /**
     * Récupère un moyen de paiement par son ID
     */
    @Transactional(readOnly = true)
    public Optional<MoyenPaiementDTO> getMoyenPaiementById(Long id) {
        return moyenPaiementRepository.findById(id)
                .map(paiementMapper::toDTO);
    }

    /**
     * Récupère tous les moyens de paiement
     */
    @Transactional(readOnly = true)
    public List<MoyenPaiementDTO> getAllMoyensPaiement() {
        List<MoyenPaiement> moyens = moyenPaiementRepository.findAll();
        return paiementMapper.toMoyenPaiementDTOList(moyens);
    }

    /**
     * Met à jour un moyen de paiement
     */
    @Transactional
    public MoyenPaiementDTO updateMoyenPaiement(Long id, MoyenPaiementDTO dto) {
        log.info("Mise à jour du moyen de paiement ID: {}", id);
        
        return moyenPaiementRepository.findById(id)
                .map(existing -> {
                    // Vérifier si le nouveau nom est déjà utilisé par un autre moyen
                    if (!existing.getNom().equals(dto.getNom()) && 
                        moyenPaiementRepository.existsByNom(dto.getNom())) {
                        throw new IllegalArgumentException("Un moyen de paiement avec ce nom existe déjà: " + dto.getNom());
                    }
                    
                    existing.setNom(dto.getNom());
                    existing.setPhoto(dto.getPhoto());
                    
                    return paiementMapper.toDTO(moyenPaiementRepository.save(existing));
                })
                .orElseThrow(() -> new RuntimeException("Moyen de paiement non trouvé avec l'ID: " + id));
    }

    /**
     * Supprime un moyen de paiement
     */
    @Transactional
    public void deleteMoyenPaiement(Long id) {
        log.info("Suppression du moyen de paiement ID: {}", id);
        
        if (!moyenPaiementRepository.existsById(id)) {
            throw new RuntimeException("Moyen de paiement non trouvé avec l'ID: " + id);
        }
        
        // Supprimer toutes les associations utilisateur-moyen de paiement
        userMoyenPaiementRepository.deleteByMoyenPaiementId(id);
        
        // Supprimer le moyen de paiement
        moyenPaiementRepository.deleteById(id);
    }

    /**
     * Recherche des moyens de paiement par mot-clé
     */
    @Transactional(readOnly = true)
    public List<MoyenPaiementDTO> searchMoyensPaiement(String keyword) {
        List<MoyenPaiement> moyens = moyenPaiementRepository.searchByKeyword(keyword);
        return paiementMapper.toMoyenPaiementDTOList(moyens);
    }

    // ============= GESTION DES ASSOCIATIONS UTILISATEUR-MOYEN PAIEMENT =============

    /**
     * Associe un moyen de paiement à un utilisateur
     */
    @Transactional
    public UserMoyenPaiementDTO associateMoyenPaiementToUser(Long userId, Long moyenPaiementId, Boolean actif) {
        log.info("Association du moyen de paiement {} à l'utilisateur {}", moyenPaiementId, userId);
        
        // Vérifier que l'utilisateur existe
        Compte user = compteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID: " + userId));
        
        // Vérifier que le moyen de paiement existe
        MoyenPaiement moyenPaiement = moyenPaiementRepository.findById(moyenPaiementId)
                .orElseThrow(() -> new RuntimeException("Moyen de paiement non trouvé avec l'ID: " + moyenPaiementId));
        
        // Vérifier si l'association existe déjà
        if (userMoyenPaiementRepository.existsByUserIdAndMoyenPaiementId(userId, moyenPaiementId)) {
            throw new IllegalArgumentException("Cette association existe déjà");
        }
        
        // Créer l'association
        UserMoyenPaiement association = new UserMoyenPaiement(user, moyenPaiement, actif != null ? actif : true);
        UserMoyenPaiement saved = userMoyenPaiementRepository.save(association);
        
        return paiementMapper.toDTO(saved);
    }

    /**
     * Désactive un moyen de paiement pour un utilisateur
     */
    @Transactional
    public void desactiverMoyenPaiementForUser(Long userId, Long moyenPaiementId) {
        log.info("Désactivation du moyen de paiement {} pour l'utilisateur {}", moyenPaiementId, userId);
        
        int updated = userMoyenPaiementRepository.desactiverMoyenPaiement(userId, moyenPaiementId);
        if (updated == 0) {
            throw new RuntimeException("Association non trouvée entre l'utilisateur " + userId + 
                                     " et le moyen de paiement " + moyenPaiementId);
        }
    }

    /**
     * Active un moyen de paiement pour un utilisateur
     */
    @Transactional
    public void activerMoyenPaiementForUser(Long userId, Long moyenPaiementId) {
        log.info("Activation du moyen de paiement {} pour l'utilisateur {}", moyenPaiementId, userId);
        
        int updated = userMoyenPaiementRepository.activerMoyenPaiement(userId, moyenPaiementId);
        if (updated == 0) {
            throw new RuntimeException("Association non trouvée entre l'utilisateur " + userId + 
                                     " et le moyen de paiement " + moyenPaiementId);
        }
    }

    /**
     * Supprime une association utilisateur-moyen de paiement
     */
    @Transactional
    public void removeMoyenPaiementFromUser(Long userId, Long moyenPaiementId) {
        log.info("Suppression de l'association entre utilisateur {} et moyen de paiement {}", userId, moyenPaiementId);
        
        Optional<UserMoyenPaiement> association = userMoyenPaiementRepository
                .findByUserIdAndMoyenPaiementId(userId, moyenPaiementId);
        
        if (association.isEmpty()) {
            throw new RuntimeException("Association non trouvée entre l'utilisateur " + userId + 
                                     " et le moyen de paiement " + moyenPaiementId);
        }
        
        userMoyenPaiementRepository.delete(association.get());
    }

    /**
     * Récupère tous les moyens de paiement d'un utilisateur
     */
    @Transactional(readOnly = true)
    public List<UserMoyenPaiementDTO> getMoyensPaiementByUser(Long userId) {
        List<UserMoyenPaiement> associations = userMoyenPaiementRepository.findByUserId(userId);
        return paiementMapper.toUserMoyenPaiementDTOList(associations);
    }

    /**
     * Récupère les moyens de paiement actifs d'un utilisateur
     */
    @Transactional(readOnly = true)
    public List<MoyenPaiementDTO> getMoyensPaiementActifsByUser(Long userId) {
        List<MoyenPaiement> moyens = moyenPaiementRepository.findActifsByUserId(userId);
        return paiementMapper.toMoyenPaiementDTOList(moyens);
    }

    /**
     * Récupère tous les utilisateurs qui ont un moyen de paiement spécifique
     */
    @Transactional(readOnly = true)
    public List<UserMoyenPaiementDTO> getUsersByMoyenPaiement(Long moyenPaiementId) {
        List<UserMoyenPaiement> associations = userMoyenPaiementRepository.findByMoyenPaiementId(moyenPaiementId);
        return paiementMapper.toUserMoyenPaiementDTOList(associations);
    }

    /**
     * Vérifie si un utilisateur a un moyen de paiement spécifique actif
     */
    @Transactional(readOnly = true)
    public boolean userHasMoyenPaiementActif(Long userId, Long moyenPaiementId) {
        return moyenPaiementRepository.existsActifForUser(userId, moyenPaiementId);
    }

    /**
     * Compte le nombre de moyens de paiement actifs pour un utilisateur
     */
    @Transactional(readOnly = true)
    public long countMoyensPaiementActifsByUser(Long userId) {
        return userMoyenPaiementRepository.countActifsByUserId(userId);
    }
}
