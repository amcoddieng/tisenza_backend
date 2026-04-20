package com.tissenza.tissenza_backend.modules.auth.controller;

import com.tissenza.tissenza_backend.exception.ApiResponse;
import com.tissenza.tissenza_backend.modules.auth.dto.LoginRequest;
import com.tissenza.tissenza_backend.modules.auth.dto.LoginResponse;
import com.tissenza.tissenza_backend.modules.auth.dto.RegisterRequest;
import com.tissenza.tissenza_backend.modules.auth.dto.RegisterResponse;
import com.tissenza.tissenza_backend.modules.auth.service.AuthService;
import com.tissenza.tissenza_backend.modules.user.dto.CompteWithPersonneDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API pour l'authentification et la gestion des comptes")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne un token JWT")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Parameter(description = "Informations de connexion") @Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(ApiResponse.success(response, "Connexion réussie"));
    }

    @PostMapping("/register")
    @Operation(summary = "Inscription utilisateur", description = "Crée un nouveau compte utilisateur")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Parameter(description = "Informations d'inscription") @Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = authService.register(registerRequest);
        return new ResponseEntity<>(ApiResponse.success(response, "Compte créé avec succès"), HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    @Operation(summary = "Déconnexion", description = "Déconnecte l'utilisateur actuel")
    public ResponseEntity<ApiResponse<Void>> logout() {
        authService.logout();
        return ResponseEntity.ok(ApiResponse.success(null, "Déconnexion réussie"));
    }

    @GetMapping("/me")
    @Operation(summary = "Informations utilisateur actuel", description = "Retourne les informations de l'utilisateur connecté")
    public ResponseEntity<ApiResponse<CompteWithPersonneDTO>> getCurrentUser() {
        CompteWithPersonneDTO currentCompte = authService.getCurrentCompteWithPersonne();
        return ResponseEntity.ok(ApiResponse.success(currentCompte, "Informations utilisateur récupérées"));
    }
}
