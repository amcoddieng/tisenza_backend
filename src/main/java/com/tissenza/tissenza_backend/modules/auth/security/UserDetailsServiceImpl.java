package com.tissenza.tissenza_backend.modules.auth.security;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import com.tissenza.tissenza_backend.modules.user.entity.Personne;
import com.tissenza.tissenza_backend.modules.user.repository.CompteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CompteRepository compteRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Tentative d'authentification pour l'email: {}", email);
        
        Compte compte = compteRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Compte non trouvé avec l'email: {}", email);
                    return new UsernameNotFoundException("Compte non trouvé avec l'email: " + email);
                });

        log.info("Compte trouvé: id={}, email={}, statut={}", compte.getId(), compte.getEmail(), compte.getStatut());

        // Vérifier si le compte est actif
        if (compte.getStatut() != Compte.Statut.ACTIF) {
            log.warn("Compte non actif: {} pour l'email: {}", compte.getStatut(), email);
            throw new UsernameNotFoundException("Compte non actif: " + email);
        }

        // Créer un UserDetails à partir du Compte
        Collection<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + compte.getRole().name())
        );

        Personne personne = compte.getPersonne();
        String username = personne.getNom() + " " + personne.getPrenom();

        log.info("Création UserDetails pour: {}, role: {}, password hash: {}", 
                email, compte.getRole(), compte.getMotDePasse() != null ? "present" : "null");

        return User.builder()
                .username(email)
                .password(compte.getMotDePasse())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(compte.getStatut() == Compte.Statut.SUSPENDU)
                .credentialsExpired(false)
                .disabled(compte.getStatut() == Compte.Statut.INACTIF || !compte.getIsVerified())
                .build();
    }
}
