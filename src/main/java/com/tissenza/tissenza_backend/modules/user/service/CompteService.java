package com.tissenza.tissenza_backend.modules.user.service;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.dto.CompteDTO;
import com.tissenza.tissenza_backend.modules.user.dto.CompteWithPersonneDTO;
import com.tissenza.tissenza_backend.modules.user.dto.PersonneSimpleDTO;
import com.tissenza.tissenza_backend.modules.user.dto.PersonneDTO;
import com.tissenza.tissenza_backend.modules.user.dto.ComptePersonneUpdateDTO;
import com.tissenza.tissenza_backend.modules.user.repository.CompteRepository;
import com.tissenza.tissenza_backend.modules.user.repository.PersonneRepository;
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
    private final PersonneRepository personneRepository;

    public Compte createCompteFromDTO(CompteDTO compteDTO) {
        // Validate personne_id
        if (compteDTO.getPersonneId() == null) {
            throw new BusinessException("personne_id est obligatoire", "PERSONNE_ID_REQUIRED");
        }
        
        // Fetch Personne entity
        Personne personne = personneRepository.findById(compteDTO.getPersonneId())
                .orElseThrow(() -> new ResourceNotFoundException("Personne", compteDTO.getPersonneId().toString()));
        
        // Create Compte entity from DTO
        Compte compte = new Compte();
        compte.setPersonne(personne);
        compte.setEmail(compteDTO.getEmail());
        compte.setTelephone(compteDTO.getTelephone());
        compte.setMotDePasse(compteDTO.getMotDePasse());
        compte.setRole(compteDTO.getRole());
        compte.setStatut(compteDTO.getStatut() != null ? compteDTO.getStatut() : Compte.Statut.ACTIF);
        compte.setIsVerified(compteDTO.getIsVerified() != null ? compteDTO.getIsVerified() : false);
        
        return createCompte(compte);
    }

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
                .orElseThrow(() -> new ResourceNotFoundException("Compte", id.toString()));
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

    // Méthodes DTO pour éviter LazyInitializationException
    @Transactional(readOnly = true)
    public CompteWithPersonneDTO getCompteWithPersonneById(Long id) {
        Compte compte = compteRepository.findByIdWithPersonne(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte", id.toString()));
        
        PersonneSimpleDTO personneDTO = PersonneSimpleDTO.fromEntity(compte.getPersonne());
        return CompteWithPersonneDTO.fromEntities(compte, personneDTO);
    }

    @Transactional(readOnly = true)
    public CompteWithPersonneDTO getCompteWithPersonneByEmail(String email) {
        Compte compte = compteRepository.findByEmailWithPersonne(email)
                .orElseThrow(() -> new ResourceNotFoundException("Compte", email));
        
        PersonneSimpleDTO personneDTO = PersonneSimpleDTO.fromEntity(compte.getPersonne());
        return CompteWithPersonneDTO.fromEntities(compte, personneDTO);
    }

    @Transactional(readOnly = true)
    public List<CompteWithPersonneDTO> getAllComptesWithPersonne() {
        List<Compte> comptes = compteRepository.findAllWithPersonne();
        return comptes.stream()
                .map(compte -> {
                    PersonneSimpleDTO personneDTO = PersonneSimpleDTO.fromEntity(compte.getPersonne());
                    return CompteWithPersonneDTO.fromEntities(compte, personneDTO);
                })
                .toList();
    }

    @Transactional
    public CompteWithPersonneDTO updateCompteAndPersonne(Long compteId, ComptePersonneUpdateDTO updateDTO) {
        // Récupérer le compte avec sa personne
        Compte compte = compteRepository.findById(compteId)
                .orElseThrow(() -> new ResourceNotFoundException("Compte", compteId.toString()));
        
        Personne personne = compte.getPersonne();
        
        // Mettre à jour les informations du compte (seulement si non null)
        if (updateDTO.getEmail() != null) {
            compte.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getTelephone() != null) {
            compte.setTelephone(updateDTO.getTelephone());
        }
        if (updateDTO.getMotDePasse() != null) {
            compte.setMotDePasse(updateDTO.getMotDePasse());
        }
        if (updateDTO.getRole() != null) {
            compte.setRole(updateDTO.getRole());
        }
        if (updateDTO.getStatut() != null) {
            compte.setStatut(updateDTO.getStatut());
        }
        if (updateDTO.getIsVerified() != null) {
            setIsVerified(compte, updateDTO.getIsVerified());
        }
        
        // Mettre à jour les informations de la personne (seulement si non null)
        if (updateDTO.getNom() != null) {
            personne.setNom(updateDTO.getNom());
        }
        if (updateDTO.getPrenom() != null) {
            personne.setPrenom(updateDTO.getPrenom());
        }
        if (updateDTO.getAdresse() != null) {
            personne.setAdresse(updateDTO.getAdresse());
        }
        if (updateDTO.getVille() != null) {
            personne.setVille(updateDTO.getVille());
        }
        if (updateDTO.getPhotoProfil() != null) {
            personne.setPhotoProfil(updateDTO.getPhotoProfil());
        }
        
        // Mettre à jour les timestamps
        personne.setUpdatedAt(LocalDateTime.now());
        
        // Sauvegarder les entités
        personneRepository.save(personne);
        Compte updatedCompte = compteRepository.save(compte);
        
        // Retourner le DTO combiné
        PersonneSimpleDTO personneDTO = PersonneSimpleDTO.fromEntity(updatedCompte.getPersonne());
        return CompteWithPersonneDTO.fromEntities(updatedCompte, personneDTO);
    }

    private void setIsVerified(Compte compte, boolean verified) {
        // Utiliser réflexion pour accéder au champ isVerified s'il existe
        try {
            java.lang.reflect.Field isVerifiedField = Compte.class.getDeclaredField("isVerified");
            isVerifiedField.setAccessible(true);
            isVerifiedField.set(compte, verified);
        } catch (Exception e) {
            // Ignorer si le champ n'existe pas
        }
    }
}
