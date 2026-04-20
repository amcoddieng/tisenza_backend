package com.tissenza.tissenza_backend.modules.user.controller;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.dto.CompteDTO;
import com.tissenza.tissenza_backend.modules.user.dto.CompteWithPersonneDTO;
import com.tissenza.tissenza_backend.modules.user.service.CompteService;
import com.tissenza.tissenza_backend.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@Tag(name = "Compte Management", description = "API pour la gestion des comptes utilisateurs")
public class CompteController {

    private final CompteService compteService;

    @PostMapping
    @Operation(summary = "Créer un nouveau compte", description = "Crée un nouveau compte utilisateur")
    public ResponseEntity<ApiResponse<Compte>> createCompte(@RequestBody CompteDTO compteDTO) {
        Compte createdCompte = compteService.createCompteFromDTO(compteDTO);
        return new ResponseEntity<>(ApiResponse.success(createdCompte, "Compte créé avec succès"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un compte par ID", description = "Retourne les détails d'un compte spécifique")
    public ResponseEntity<Compte> getCompteById(
            @Parameter(description = "ID du compte à récupérer") @PathVariable Long id) {
        return compteService.getCompteById(id)
                .map(compte -> ResponseEntity.ok(compte))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @Operation(summary = "Récupérer tous les comptes", description = "Retourne la liste de tous les comptes")
    public ResponseEntity<List<Compte>> getAllComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        return ResponseEntity.ok(comptes);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un compte", description = "Met à jour les informations d'un compte existant")
    public ResponseEntity<Compte> updateCompte(
            @Parameter(description = "ID du compte à mettre à jour") @PathVariable Long id,
            @RequestBody Compte compteDetails) {
        try {
            Compte updatedCompte = compteService.updateCompte(id, compteDetails);
            return ResponseEntity.ok(updatedCompte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un compte", description = "Supprime un compte du système")
    public ResponseEntity<Void> deleteCompte(
            @Parameter(description = "ID du compte à supprimer") @PathVariable Long id) {
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Récupérer un compte par email", description = "Retourne un compte par son email")
    public ResponseEntity<Compte> getCompteByEmail(
            @Parameter(description = "Email du compte à récupérer") @PathVariable String email) {
        return compteService.getCompteByEmail(email)
                .map(compte -> ResponseEntity.ok(compte))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/telephone/{telephone}")
    @Operation(summary = "Récupérer un compte par téléphone", description = "Retourne un compte par son numéro de téléphone")
    public ResponseEntity<Compte> getCompteByTelephone(
            @Parameter(description = "Téléphone du compte à récupérer") @PathVariable String telephone) {
        return compteService.getCompteByTelephone(telephone)
                .map(compte -> ResponseEntity.ok(compte))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/role/{role}")
    @Operation(summary = "Récupérer des comptes par rôle", description = "Retourne la liste des comptes par rôle")
    public ResponseEntity<List<Compte>> getComptesByRole(
            @Parameter(description = "Rôle à rechercher") @PathVariable Compte.Role role) {
        List<Compte> comptes = compteService.getComptesByRole(role);
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer des comptes par statut", description = "Retourne la liste des comptes par statut")
    public ResponseEntity<List<Compte>> getComptesByStatut(
            @Parameter(description = "Statut à rechercher") @PathVariable Compte.Statut statut) {
        List<Compte> comptes = compteService.getComptesByStatut(statut);
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/role/{role}/statut/{statut}")
    @Operation(summary = "Récupérer des comptes par rôle et statut", description = "Retourne la liste des comptes par rôle et statut")
    public ResponseEntity<List<Compte>> getComptesByRoleAndStatut(
            @Parameter(description = "Rôle à rechercher") @PathVariable Compte.Role role,
            @Parameter(description = "Statut à rechercher") @PathVariable Compte.Statut statut) {
        List<Compte> comptes = compteService.getComptesByRoleAndStatut(role, statut);
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/verified")
    @Operation(summary = "Récupérer les comptes vérifiés", description = "Retourne la liste des comptes vérifiés")
    public ResponseEntity<List<Compte>> getVerifiedComptes() {
        List<Compte> comptes = compteService.getVerifiedComptes();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/unverified")
    @Operation(summary = "Récupérer les comptes non vérifiés", description = "Retourne la liste des comptes non vérifiés")
    public ResponseEntity<List<Compte>> getUnverifiedComptes() {
        List<Compte> comptes = compteService.getUnverifiedComptes();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche par mot-clé", description = "Recherche des comptes par mot-clé dans email ou téléphone")
    public ResponseEntity<List<Compte>> searchByKeyword(
            @Parameter(description = "Mot-clé de recherche") @RequestParam String keyword) {
        List<Compte> comptes = compteService.searchByKeyword(keyword);
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/count/role/{role}")
    @Operation(summary = "Compter par rôle", description = "Compte le nombre de comptes par rôle")
    public ResponseEntity<Long> countByRole(
            @Parameter(description = "Rôle pour le comptage") @PathVariable Compte.Role role) {
        long count = compteService.countByRole(role);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/statut/{statut}")
    @Operation(summary = "Compter par statut", description = "Compte le nombre de comptes par statut")
    public ResponseEntity<Long> countByStatut(
            @Parameter(description = "Statut pour le comptage") @PathVariable Compte.Statut statut) {
        long count = compteService.countByStatut(statut);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/verified")
    @Operation(summary = "Compter les utilisateurs vérifiés", description = "Compte le nombre d'utilisateurs vérifiés")
    public ResponseEntity<Long> countVerifiedUsers() {
        long count = compteService.countVerifiedUsers();
        return ResponseEntity.ok(count);
    }

    @PutMapping("/{id}/login")
    @Operation(summary = "Mettre à jour la dernière connexion", description = "Met à jour la date de dernière connexion")
    public ResponseEntity<Compte> updateLastLogin(
            @Parameter(description = "ID du compte à mettre à jour") @PathVariable Long id) {
        try {
            Compte updatedCompte = compteService.updateLastLogin(id);
            return ResponseEntity.ok(updatedCompte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/verify")
    @Operation(summary = "Vérifier un compte", description = "Marque un compte comme vérifié")
    public ResponseEntity<Compte> verifyCompte(
            @Parameter(description = "ID du compte à vérifier") @PathVariable Long id) {
        try {
            Compte verifiedCompte = compteService.verifyCompte(id);
            return ResponseEntity.ok(verifiedCompte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/statut")
    @Operation(summary = "Changer le statut d'un compte", description = "Change le statut d'un compte")
    public ResponseEntity<Compte> changeStatut(
            @Parameter(description = "ID du compte à modifier") @PathVariable Long id,
            @Parameter(description = "Nouveau statut") @RequestParam Compte.Statut newStatut) {
        try {
            Compte updatedCompte = compteService.changeStatut(id, newStatut);
            return ResponseEntity.ok(updatedCompte);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Vérifier l'existence par email", description = "Vérifie si un email existe déjà")
    public ResponseEntity<Boolean> existsByEmail(
            @Parameter(description = "Email à vérifier") @PathVariable String email) {
        boolean exists = compteService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/exists/telephone/{telephone}")
    @Operation(summary = "Vérifier l'existence par téléphone", description = "Vérifie si un numéro de téléphone existe déjà")
    public ResponseEntity<Boolean> existsByTelephone(
            @Parameter(description = "Téléphone à vérifier") @PathVariable String telephone) {
        boolean exists = compteService.existsByTelephone(telephone);
        return ResponseEntity.ok(exists);
    }

    // Endpoints DTO pour éviter LazyInitializationException
    @GetMapping("/{id}/with-personne")
    @Operation(summary = "Récupérer un compte avec sa personne", description = "Retourne un compte avec les informations de sa personne associée")
    public ResponseEntity<ApiResponse<CompteWithPersonneDTO>> getCompteWithPersonne(
            @Parameter(description = "ID du compte à récupérer") @PathVariable Long id) {
        try {
            CompteWithPersonneDTO compteDTO = compteService.getCompteWithPersonneById(id);
            return ResponseEntity.ok(ApiResponse.success(compteDTO, "Compte récupéré avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Compte non trouvé"));
        }
    }

    @GetMapping("/with-personne")
    @Operation(summary = "Lister tous les comptes avec leurs personnes", description = "Retourne tous les comptes avec les informations de leurs personnes associées")
    public ResponseEntity<ApiResponse<List<CompteWithPersonneDTO>>> getAllComptesWithPersonne() {
        List<CompteWithPersonneDTO> comptes = compteService.getAllComptesWithPersonne();
        return ResponseEntity.ok(ApiResponse.success(comptes, "Liste des comptes récupérée avec succès"));
    }

    @GetMapping("/email/{email}/with-personne")
    @Operation(summary = "Récupérer un compte par email avec sa personne", description = "Retourne un compte par email avec les informations de sa personne associée")
    public ResponseEntity<ApiResponse<CompteWithPersonneDTO>> getCompteByEmailWithPersonne(
            @Parameter(description = "Email du compte à récupérer") @PathVariable String email) {
        try {
            CompteWithPersonneDTO compteDTO = compteService.getCompteWithPersonneByEmail(email);
            return ResponseEntity.ok(ApiResponse.success(compteDTO, "Compte récupéré avec succès"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Compte non trouvé"));
        }
    }
}
