package com.tissenza.tissenza_backend.modules.user.service;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.repository.CompteRepository;
import com.tissenza.tissenza_backend.exception.BusinessException;
import com.tissenza.tissenza_backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompteService {

    private final CompteRepository compteRepository;

    public Compte createCompte(Compte compte) {
        // Vérifier si l'email existe déjà
        if (compteRepository.existsByEmail(compte.getEmail())) {
            throw new BusinessException("Email déjà utilisé: " + compte.getEmail(), "EMAIL_ALREADY_EXISTS");
        }
        
        // Vérifier si le téléphone existe déjà
        if (compte.getTelephone() != null && compteRepository.existsByTelephone(compte.getTelephone())) {
            throw new BusinessException("Téléphone déjà utilisé: " + compte.getTelephone(), "TELEPHONE_ALREADY_EXISTS");
        }
        
        return compteRepository.save(compte);
    }

    public Optional<Compte> getCompteById(Long id) {
        return compteRepository.findById(id);
    }

    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    public Compte updateCompte(Long id, Compte compteDetails) {
        return compteRepository.findById(id)
                .map(compte -> {
                    // Vérifier si l'email est utilisé par un autre compte
                    if (!compte.getEmail().equals(compteDetails.getEmail()) && 
                        compteRepository.existsByEmail(compteDetails.getEmail())) {
                        throw new BusinessException("Email déjà utilisé: " + compteDetails.getEmail(), "EMAIL_ALREADY_EXISTS");
                    }
                    
                    // Vérifier si le téléphone est utilisé par un autre compte
                    if (compteDetails.getTelephone() != null && 
                        !compteDetails.getTelephone().equals(compte.getTelephone()) && 
                        compteRepository.existsByTelephone(compteDetails.getTelephone())) {
                        throw new BusinessException("Téléphone déjà utilisé: " + compteDetails.getTelephone(), "TELEPHONE_ALREADY_EXISTS");
                    }
                    
                    compte.setEmail(compteDetails.getEmail());
                    compte.setTelephone(compteDetails.getTelephone());
                    compte.setMotDePasse(compteDetails.getMotDePasse());
                    compte.setRole(compteDetails.getRole());
                    compte.setStatut(compteDetails.getStatut());
                    compte.setIsVerified(compteDetails.getIsVerified());
                    return compteRepository.save(compte);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Compte", id.toString()));
    }

    public void deleteCompte(Long id) {
        compteRepository.deleteById(id);
    }

    public Optional<Compte> getCompteByEmail(String email) {
        return compteRepository.findByEmail(email);
    }

    public Optional<Compte> getCompteByTelephone(String telephone) {
        return compteRepository.findByTelephone(telephone);
    }

    public boolean existsByEmail(String email) {
        return compteRepository.existsByEmail(email);
    }

    public boolean existsByTelephone(String telephone) {
        return compteRepository.existsByTelephone(telephone);
    }

    public List<Compte> getComptesByRole(Compte.Role role) {
        return compteRepository.findByRole(role);
    }

    public List<Compte> getComptesByStatut(Compte.Statut statut) {
        return compteRepository.findByStatut(statut);
    }

    public List<Compte> getComptesByRoleAndStatut(Compte.Role role, Compte.Statut statut) {
        return compteRepository.findByRoleAndStatut(role, statut);
    }

    public List<Compte> getVerifiedComptes() {
        return compteRepository.findByIsVerified(true);
    }

    public List<Compte> getUnverifiedComptes() {
        return compteRepository.findByIsVerified(false);
    }

    public List<Compte> searchByKeyword(String keyword) {
        return compteRepository.searchByKeyword(keyword);
    }

    public long countByRole(Compte.Role role) {
        return compteRepository.countByRole(role);
    }

    public long countByStatut(Compte.Statut statut) {
        return compteRepository.countByStatut(statut);
    }

    public long countVerifiedUsers() {
        return compteRepository.countVerifiedUsers();
    }

    public Compte updateLastLogin(Long id) {
        return compteRepository.findById(id)
                .map(compte -> {
                    compte.setLastLogin(LocalDateTime.now());
                    return compteRepository.save(compte);
                })
                .orElseThrow(() -> new RuntimeException("Compte not found with id: " + id));
    }

    public Compte verifyCompte(Long id) {
        return compteRepository.findById(id)
                .map(compte -> {
                    compte.setIsVerified(true);
                    return compteRepository.save(compte);
                })
                .orElseThrow(() -> new RuntimeException("Compte not found with id: " + id));
    }

    public Compte changeStatut(Long id, Compte.Statut newStatut) {
        return compteRepository.findById(id)
                .map(compte -> {
                    compte.setStatut(newStatut);
                    return compteRepository.save(compte);
                })
                .orElseThrow(() -> new RuntimeException("Compte not found with id: " + id));
    }
}
