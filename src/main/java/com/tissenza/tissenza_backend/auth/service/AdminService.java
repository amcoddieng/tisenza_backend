package com.tissenza.tissenza_backend.auth.service;

import com.tissenza.tissenza_backend.auth.dto.AdminDTO;
import com.tissenza.tissenza_backend.auth.entity.admin.Administrateur;
import com.tissenza.tissenza_backend.auth.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    
    private final AdminRepository adminRepository;
    
    /**
     * Créer un nouvel administrateur
     */
    public AdminDTO createAdmin(AdminDTO adminDTO) {
        Administrateur admin = mapToEntity(adminDTO);
        admin.setCreated_at(new Timestamp(System.currentTimeMillis()));
        admin.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        admin.setStatut("ACTIF");
        
        Administrateur savedAdmin = adminRepository.save(admin);
        return mapToDTO(savedAdmin);
    }
    
    /**
     * Récupérer un administrateur par ID
     */
    public Optional<AdminDTO> getAdminById(Long id) {
        return adminRepository.findById(id).map(this::mapToDTO);
    }
    
    /**
     * Récupérer un administrateur par email
     */
    public Optional<AdminDTO> getAdminByEmail(String email) {
        return adminRepository.findByEmail(email).map(this::mapToDTO);
    }
    
    /**
     * Récupérer un administrateur par téléphone
     */
    public Optional<AdminDTO> getAdminByTelephone(String telephone) {
        return adminRepository.findByTelephone(telephone).map(this::mapToDTO);
    }
    
    /**
     * Récupérer tous les administrateurs
     */
    public List<AdminDTO> getAllAdmins() {
        return adminRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer tous les administrateurs actifs
     */
    public List<AdminDTO> getActiveAdmins() {
        return adminRepository.findByStatut("ACTIF")
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer tous les administrateurs inactifs
     */
    public List<AdminDTO> getInactiveAdmins() {
        return adminRepository.findByStatut("INACTIF")
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupérer tous les administrateurs avec 2FA activé
     */
    public List<AdminDTO> getAdminsWithTwoFactorEnabled() {
        return adminRepository.findWithTwoFactorEnabled()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Mettre à jour un administrateur
     */
    public AdminDTO updateAdmin(Long id, AdminDTO adminDTO) {
        Optional<Administrateur> existingAdmin = adminRepository.findById(id);
        
        if (existingAdmin.isPresent()) {
            Administrateur admin = existingAdmin.get();
            
            if (adminDTO.getTelephone() != null) {
                admin.setTelephone(adminDTO.getTelephone());
            }
            if (adminDTO.getNom_complet() != null) {
                admin.setNom_complet(adminDTO.getNom_complet());
            }
            if (adminDTO.getEmail() != null) {
                admin.setEmail(adminDTO.getEmail());
            }
            if (adminDTO.getRole() != null) {
                admin.setRole(adminDTO.getRole());
            }
            if (adminDTO.getStatut() != null) {
                admin.setStatut(adminDTO.getStatut());
            }
            if (adminDTO.getDeuxFaSecret() != null) {
                admin.setDeuxFaSecret(adminDTO.getDeuxFaSecret());
            }
            admin.setDeuxFaActive(adminDTO.isDeuxFaActive());
            if (adminDTO.getPermissions() != null) {
                admin.setPermissions(adminDTO.getPermissions());
            }
            
            admin.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            
            Administrateur updatedAdmin = adminRepository.save(admin);
            return mapToDTO(updatedAdmin);
        }
        
        throw new RuntimeException("Administrateur non trouvé avec l'ID: " + id);
    }
    
    /**
     * Activer/désactiver 2FA pour un administrateur
     */
    public AdminDTO toggleTwoFactorAuth(Long id, String secret) {
        Optional<Administrateur> existingAdmin = adminRepository.findById(id);
        
        if (existingAdmin.isPresent()) {
            Administrateur admin = existingAdmin.get();
            admin.setDeuxFaSecret(secret);
            admin.setDeuxFaActive(!admin.isDeuxFaActive());
            admin.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            
            Administrateur updatedAdmin = adminRepository.save(admin);
            return mapToDTO(updatedAdmin);
        }
        
        throw new RuntimeException("Administrateur non trouvé avec l'ID: " + id);
    }
    
    /**
     * Mettre à jour les permissions d'un administrateur
     */
    public AdminDTO updatePermissions(Long id, String permissions) {
        Optional<Administrateur> existingAdmin = adminRepository.findById(id);
        
        if (existingAdmin.isPresent()) {
            Administrateur admin = existingAdmin.get();
            admin.setPermissions(permissions);
            admin.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            admin.setLastActionAt(new Timestamp(System.currentTimeMillis()));
            
            Administrateur updatedAdmin = adminRepository.save(admin);
            return mapToDTO(updatedAdmin);
        }
        
        throw new RuntimeException("Administrateur non trouvé avec l'ID: " + id);
    }
    
    /**
     * Suspendre un administrateur
     */
    public AdminDTO suspendAdmin(Long id, String motif) {
        Optional<Administrateur> existingAdmin = adminRepository.findById(id);
        
        if (existingAdmin.isPresent()) {
            Administrateur admin = existingAdmin.get();
            admin.setStatut("SUSPENDU");
            admin.setMotif_suspension(motif);
            admin.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            
            Administrateur updatedAdmin = adminRepository.save(admin);
            return mapToDTO(updatedAdmin);
        }
        
        throw new RuntimeException("Administrateur non trouvé avec l'ID: " + id);
    }
    
    /**
     * Réactiver un administrateur
     */
    public AdminDTO reactivateAdmin(Long id) {
        Optional<Administrateur> existingAdmin = adminRepository.findById(id);
        
        if (existingAdmin.isPresent()) {
            Administrateur admin = existingAdmin.get();
            admin.setStatut("ACTIF");
            admin.setMotif_suspension(null);
            admin.setUpdated_at(new Timestamp(System.currentTimeMillis()));
            
            Administrateur updatedAdmin = adminRepository.save(admin);
            return mapToDTO(updatedAdmin);
        }
        
        throw new RuntimeException("Administrateur non trouvé avec l'ID: " + id);
    }
    
    /**
     * Supprimer un administrateur
     */
    public void deleteAdmin(Long id) {
        if (adminRepository.existsById(id)) {
            adminRepository.deleteById(id);
        } else {
            throw new RuntimeException("Administrateur non trouvé avec l'ID: " + id);
        }
    }
    
    /**
     * Vérifier si un email existe
     */
    public boolean emailExists(String email) {
        return adminRepository.existsByEmail(email);
    }
    
    /**
     * Vérifier si un téléphone existe
     */
    public boolean telephoneExists(String telephone) {
        return adminRepository.existsByTelephone(telephone);
    }
    
    /**
     * Mettre à jour la dernière action
     */
    public void updateLastAction(Long id) {
        Optional<Administrateur> existingAdmin = adminRepository.findById(id);
        
        if (existingAdmin.isPresent()) {
            Administrateur admin = existingAdmin.get();
            admin.setLastActionAt(new Timestamp(System.currentTimeMillis()));
            admin.setLast_login_at(new Timestamp(System.currentTimeMillis()));
            adminRepository.save(admin);
        }
    }
    
    /**
     * Mapper l'entité vers DTO
     */
    private AdminDTO mapToDTO(Administrateur admin) {
        return AdminDTO.builder()
                .id(admin.getId())
                .telephone(admin.getTelephone())
                .nom_complet(admin.getNom_complet())
                .email(admin.getEmail())
                .role(admin.getRole())
                .statut(admin.getStatut())
                .biometrie_active(admin.isBiometrie_active())
                .motif_suspension(admin.getMotif_suspension())
                .created_at(admin.getCreated_at())
                .updated_at(admin.getUpdated_at())
                .last_login_at(admin.getLast_login_at())
                .deuxFaSecret(admin.getDeuxFaSecret())
                .deuxFaActive(admin.isDeuxFaActive())
                .permissions(admin.getPermissions())
                .lastActionAt(admin.getLastActionAt())
                .build();
    }
    
    /**
     * Mapper le DTO vers entité
     */
    private Administrateur mapToEntity(AdminDTO adminDTO) {
        Administrateur admin = new Administrateur();
        admin.setTelephone(adminDTO.getTelephone());
        admin.setNom_complet(adminDTO.getNom_complet());
        admin.setEmail(adminDTO.getEmail());
        admin.setRole(adminDTO.getRole());
        admin.setStatut(adminDTO.getStatut());
        admin.setBiometrie_active(adminDTO.isBiometrie_active());
        admin.setMotif_suspension(adminDTO.getMotif_suspension());
        admin.setDeuxFaSecret(adminDTO.getDeuxFaSecret());
        admin.setDeuxFaActive(adminDTO.isDeuxFaActive());
        admin.setPermissions(adminDTO.getPermissions());
        return admin;
    }
}
