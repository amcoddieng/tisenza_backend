package com.tissenza.tissenza_backend.auth.controller;

import com.tissenza.tissenza_backend.auth.dto.AdminDTO;
import com.tissenza.tissenza_backend.auth.service.AdminService;
import com.tissenza.tissenza_backend.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;

    private <T> ApiResponse<T> okResponse(String message, T data) {
        ApiResponse<T> response = ApiResponse.success(data);
        response.setMessage(message);
        return response;
    }

    private <T> ApiResponse<T> errorResponse(String message) {
        return ApiResponse.error(message);
    }
    
    /**
     * GET - Récupérer tous les administrateurs
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AdminDTO>>> getAllAdmins() {
        List<AdminDTO> admins = adminService.getAllAdmins();
        return ResponseEntity.ok(okResponse("Liste des administrateurs récupérée avec succès", admins));
    }
    
    /**
     * GET - Récupérer un administrateur par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminDTO>> getAdminById(@PathVariable Long id) {
        Optional<AdminDTO> admin = adminService.getAdminById(id);
        
        if (admin.isPresent()) {
            return ResponseEntity.ok(okResponse("Administrateur récupéré avec succès", admin.get()));
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse("Administrateur non trouvé"));
    }
    
    /**
     * GET - Récupérer un administrateur par email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<AdminDTO>> getAdminByEmail(@PathVariable String email) {
        Optional<AdminDTO> admin = adminService.getAdminByEmail(email);
        
        if (admin.isPresent()) {
            adminService.updateLastAction(admin.get().getId());
            return ResponseEntity.ok(okResponse("Administrateur récupéré avec succès", admin.get()));
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse("Administrateur non trouvé"));
    }
    
    /**
     * GET - Récupérer les administrateurs actifs
     */
    @GetMapping("/status/active")
    public ResponseEntity<ApiResponse<List<AdminDTO>>> getActiveAdmins() {
        List<AdminDTO> admins = adminService.getActiveAdmins();
        return ResponseEntity.ok(okResponse("Liste des administrateurs actifs récupérée avec succès", admins));
    }
    
    /**
     * GET - Récupérer les administrateurs inactifs
     */
    @GetMapping("/status/inactive")
    public ResponseEntity<ApiResponse<List<AdminDTO>>> getInactiveAdmins() {
        List<AdminDTO> admins = adminService.getInactiveAdmins();
        return ResponseEntity.ok(okResponse("Liste des administrateurs inactifs récupérée avec succès", admins));
    }
    
    /**
     * GET - Récupérer les administrateurs avec 2FA activé
     */
    @GetMapping("/2fa/enabled")
    public ResponseEntity<ApiResponse<List<AdminDTO>>> getAdminsWithTwoFactorEnabled() {
        List<AdminDTO> admins = adminService.getAdminsWithTwoFactorEnabled();
        return ResponseEntity.ok(okResponse("Liste des administrateurs avec 2FA activé", admins));
    }
    
    /**
     * POST - Créer un nouvel administrateur
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AdminDTO>> createAdmin(@RequestBody AdminDTO adminDTO) {
        
        if (adminService.emailExists(adminDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse("Cet email est déjà utilisé"));
        }
        
        if (adminService.telephoneExists(adminDTO.getTelephone())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(errorResponse("Ce téléphone est déjà utilisé"));
        }
        
        AdminDTO createdAdmin = adminService.createAdmin(adminDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(okResponse("Administrateur créé avec succès", createdAdmin));
    }
    
    /**
     * PUT - Mettre à jour un administrateur
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminDTO>> updateAdmin(
            @PathVariable Long id,
            @RequestBody AdminDTO adminDTO) {
        
        try {
            AdminDTO updatedAdmin = adminService.updateAdmin(id, adminDTO);
            return ResponseEntity.ok(okResponse("Administrateur mis à jour avec succès", updatedAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }
    
    /**
     * PATCH - Activer/désactiver 2FA
     */
    @PatchMapping("/{id}/2fa")
    public ResponseEntity<ApiResponse<AdminDTO>> toggleTwoFactorAuth(
            @PathVariable Long id,
            @RequestParam(required = false) String secret) {
        
        try {
            AdminDTO updatedAdmin = adminService.toggleTwoFactorAuth(id, secret);
            return ResponseEntity.ok(okResponse("2FA togglé avec succès", updatedAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }
    
    /**
     * PATCH - Mettre à jour les permissions
     */
    @PatchMapping("/{id}/permissions")
    public ResponseEntity<ApiResponse<AdminDTO>> updatePermissions(
            @PathVariable Long id,
            @RequestParam String permissions) {
        
        try {
            AdminDTO updatedAdmin = adminService.updatePermissions(id, permissions);
            return ResponseEntity.ok(okResponse("Permissions mises à jour avec succès", updatedAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }
    
    /**
     * PATCH - Suspendre un administrateur
     */
    @PatchMapping("/{id}/suspend")
    public ResponseEntity<ApiResponse<AdminDTO>> suspendAdmin(
            @PathVariable Long id,
            @RequestParam String motif) {
        
        try {
            AdminDTO suspendedAdmin = adminService.suspendAdmin(id, motif);
            return ResponseEntity.ok(okResponse("Administrateur suspendu avec succès", suspendedAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }
    
    /**
     * PATCH - Réactiver un administrateur
     */
    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<ApiResponse<AdminDTO>> reactivateAdmin(@PathVariable Long id) {
        
        try {
            AdminDTO reactivatedAdmin = adminService.reactivateAdmin(id);
            return ResponseEntity.ok(okResponse("Administrateur réactivé avec succès", reactivatedAdmin));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }
    
    /**
     * DELETE - Supprimer un administrateur
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(@PathVariable Long id) {
        
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok(okResponse("Administrateur supprimé avec succès", null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(errorResponse(e.getMessage()));
        }
    }
}
