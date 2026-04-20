package com.tissenza.tissenza_backend.modules.auth.service;

import com.tissenza.tissenza_backend.modules.auth.dto.LoginRequest;
import com.tissenza.tissenza_backend.modules.auth.dto.LoginResponse;
import com.tissenza.tissenza_backend.modules.auth.dto.RegisterRequest;
import com.tissenza.tissenza_backend.modules.auth.dto.RegisterResponse;
import com.tissenza.tissenza_backend.modules.auth.security.JwtUtils;
import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.dto.CompteDTO;
import com.tissenza.tissenza_backend.modules.user.dto.CompteWithPersonneDTO;
import com.tissenza.tissenza_backend.modules.user.service.CompteService;
import com.tissenza.tissenza_backend.modules.user.service.PersonneService;
import com.tissenza.tissenza_backend.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CompteService compteService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final PersonneService personneService;

    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static final long LOCK_DURATION_HOURS = 24;

    public LoginResponse login(LoginRequest loginRequest) {
        log.info("Tentative de connexion pour l'email: {}", loginRequest.getEmail());
        
        Compte compte = compteService.getCompteByEmail(loginRequest.getEmail())
                .orElseThrow(() -> {
                    log.warn("Compte non trouvé pour l'email: {}", loginRequest.getEmail());
                    return new BusinessException("Email ou mot de passe incorrect", "INVALID_CREDENTIALS");
                });

        log.info("Compte trouvé: id={}, email={}, statut={}", compte.getId(), compte.getEmail(), compte.getStatut());

        // Vérifier si le compte est verrouillé
        if (compte.getStatut() == Compte.Statut.SUSPENDU) {
            log.warn("Compte suspendu: {}", loginRequest.getEmail());
            throw new BusinessException("Compte suspendu. Veuillez contacter l'administrateur.", "ACCOUNT_SUSPENDED");
        }

        if (compte.getStatut() == Compte.Statut.INACTIF) {
            log.warn("Compte inactif: {}", loginRequest.getEmail());
            throw new BusinessException("Compte inactif. Veuillez activer votre compte.", "ACCOUNT_INACTIVE");
        }

        try {
            log.info("Tentative d'authentification Spring Security pour: {}", loginRequest.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            log.info("Authentification réussie pour: {}", loginRequest.getEmail());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);
            
            // Mettre à jour la dernière connexion
            compteService.updateLastLogin(compte.getId());

            // Obtenir la personne associée
            Personne personne = compte.getPersonne();

            return new LoginResponse(
                    jwt,
                    "Bearer",
                    compte.getId(),
                    compte.getEmail(),
                    personne.getNom() + " " + personne.getPrenom(),
                    compte.getRole().toString(),
                    jwtUtils.getExpirationTime()
            );

        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            log.error("Mauvais identifiants pour {}: {}", loginRequest.getEmail(), e.getMessage());
            
            // Vérifier si l'email existe pour donner un message plus précis
            if (compteService.getCompteByEmail(loginRequest.getEmail()).isPresent()) {
                throw new BusinessException("Mot de passe incorrect pour l'email: " + loginRequest.getEmail(), "BAD_PASSWORD");
            } else {
                throw new BusinessException("Aucun compte trouvé avec l'email: " + loginRequest.getEmail(), "EMAIL_NOT_FOUND");
            }
        } catch (org.springframework.security.authentication.DisabledException e) {
            log.error("Compte désactivé pour {}: {}", loginRequest.getEmail(), e.getMessage());
            throw new BusinessException("Compte désactivé. Veuillez contacter l'administrateur.", "ACCOUNT_DISABLED");
        } catch (org.springframework.security.authentication.LockedException e) {
            log.error("Compte verrouillé pour {}: {}", loginRequest.getEmail(), e.getMessage());
            throw new BusinessException("Compte verrouillé. Veuillez contacter l'administrateur.", "ACCOUNT_LOCKED");
        } catch (Exception e) {
            log.error("Erreur inattendue lors de l'authentification pour {}: {} - {}", 
                    loginRequest.getEmail(), e.getClass().getSimpleName(), e.getMessage());
            throw new BusinessException("Erreur lors de l'authentification: " + e.getClass().getSimpleName(), "AUTH_ERROR");
        }
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        // Vérifier si l'email existe déjà
        if (compteService.existsByEmail(registerRequest.getEmail())) {
            throw new BusinessException("Email déjà utilisé", "EMAIL_ALREADY_EXISTS");
        }

        // Créer la personne
        Personne personne = new Personne();
        personne.setNom(registerRequest.getNom());
        personne.setPrenom(registerRequest.getPrenom());
        personne.setAdresse(registerRequest.getAdresse());
        personne.setVille(registerRequest.getVille());
        personne = personneService.createPersonne(personne);

        // Créer le compte
        CompteDTO compteDTO = new CompteDTO();
        compteDTO.setPersonneId(personne.getId());
        compteDTO.setEmail(registerRequest.getEmail());
        compteDTO.setMotDePasse(passwordEncoder.encode(registerRequest.getPassword()));
        compteDTO.setTelephone(registerRequest.getTelephone());
        compteDTO.setRole(registerRequest.getRole());
        compteDTO.setStatut(Compte.Statut.ACTIF);
        compteDTO.setIsVerified(false);
        
        Compte compte = compteService.createCompteFromDTO(compteDTO);

        return new RegisterResponse(
                compte.getId(),
                compte.getEmail(),
                personne.getNom() + " " + personne.getPrenom(),
                compte.getRole().toString(),
                "Compte créé avec succès"
        );
    }

    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        }
        throw new BusinessException("Utilisateur non authentifié", "USER_NOT_AUTHENTICATED");
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public CompteWithPersonneDTO getCurrentCompteWithPersonne() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            try {
                return compteService.getCompteWithPersonneByEmail(email);
            } catch (Exception e) {
                log.error("Erreur lors de la récupération du compte courant: {}", e.getMessage());
                throw new BusinessException("Erreur lors de la récupération des informations utilisateur", "USER_INFO_ERROR");
            }
        }
        throw new BusinessException("Utilisateur non authentifié", "USER_NOT_AUTHENTICATED");
    }

    public Compte getCurrentCompte() {
        String email = getCurrentUser().getUsername();
        return compteService.getCompteByEmail(email)
                .orElseThrow(() -> new BusinessException("Compte non trouvé", "COMPTE_NOT_FOUND"));
    }

    public Personne getCurrentPersonne() {
        Compte compte = getCurrentCompte();
        return compte.getPersonne();
    }
}
