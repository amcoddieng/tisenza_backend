package com.tissenza.tissenza_backend.modules.auth.controller;

import com.tissenza.tissenza_backend.exception.ApiResponse;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.service.CompteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth/diagnostic")
@RequiredArgsConstructor
@Slf4j
public class AuthDiagnosticController {

    private final CompteService compteService;

    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> checkEmail(@PathVariable String email) {
        Map<String, Object> result = new HashMap<>();
        
        Optional<Compte> compteOpt = compteService.getCompteByEmail(email);
        
        if (compteOpt.isPresent()) {
            Compte compte = compteOpt.get();
            result.put("found", true);
            result.put("compteId", compte.getId());
            result.put("email", compte.getEmail());
            result.put("statut", compte.getStatut().toString());
            result.put("role", compte.getRole().toString());
            result.put("isVerified", compte.getIsVerified());
            result.put("hasPassword", compte.getMotDePasse() != null && !compte.getMotDePasse().isEmpty());
            result.put("passwordLength", compte.getMotDePasse() != null ? compte.getMotDePasse().length() : 0);
            result.put("passwordHash", compte.getMotDePasse() != null ? compte.getMotDePasse().substring(0, Math.min(20, compte.getMotDePasse().length())) + "..." : "null");
            result.put("hasPersonne", compte.getPersonne() != null);
            
            if (compte.getPersonne() != null) {
                result.put("personneId", compte.getPersonne().getId());
                result.put("personneName", compte.getPersonne().getNom() + " " + compte.getPersonne().getPrenom());
            }
            
            log.info("Diagnostic pour email {}: {}", email, result);
        } else {
            result.put("found", false);
            result.put("message", "Aucun compte trouvé avec cet email");
            log.warn("Aucun compte trouvé pour l'email: {}", email);
        }
        
        return ResponseEntity.ok(ApiResponse.success(result, "Diagnostic complété"));
    }

    @GetMapping("/list-accounts")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listAccounts() {
        Map<String, Object> result = new HashMap<>();
        
        // Lister tous les comptes pour diagnostic
        try {
            List<Compte> allComptes = compteService.getAllComptes();
            result.put("totalAccounts", allComptes.size());
            
            var accountsInfo = allComptes.stream()
                    .map(compte -> {
                        Map<String, Object> info = new HashMap<>();
                        info.put("id", compte.getId());
                        info.put("email", compte.getEmail());
                        info.put("statut", compte.getStatut().toString());
                        info.put("role", compte.getRole().toString());
                        info.put("isVerified", compte.getIsVerified());
                        info.put("hasPassword", compte.getMotDePasse() != null);
                        info.put("hasPersonne", compte.getPersonne() != null);
                        return info;
                    })
                    .toList();
            
            result.put("accounts", accountsInfo);
            log.info("Liste des comptes: {} comptes trouvés", allComptes.size());
            
        } catch (Exception e) {
            log.error("Erreur lors de la liste des comptes: {}", e.getMessage());
            result.put("error", e.getMessage());
        }
        
        return ResponseEntity.ok(ApiResponse.success(result, "Liste des comptes"));
    }
}
