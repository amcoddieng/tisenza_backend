package com.tissenza.tissenza_backend.modules.user.dto;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Compte
 * Résout le problème de LazyInitializationException en n'exposant que les champs nécessaires
 */
public class CompteResponseDTO {
    private Long id;
    private String email;
    private String telephone;
    private Compte.Role role;
    private Compte.Statut statut;
    private Boolean isVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    
    // Informations de la personne associée (évite le lazy loading)
    private Long personneId;
    private String personneNom;
    private String personnePrenom;
    private String personnePhoto;
    private String personneVille;

    public CompteResponseDTO() {}

    public CompteResponseDTO(Long id, String email, String telephone, Compte.Role role, 
                           Compte.Statut statut, Boolean isVerified, LocalDateTime lastLogin, 
                           LocalDateTime createdAt, Long personneId, String personneNom, 
                           String personnePrenom, String personnePhoto, String personneVille) {
        this.id = id;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.statut = statut;
        this.isVerified = isVerified;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.personneId = personneId;
        this.personneNom = personneNom;
        this.personnePrenom = personnePrenom;
        this.personnePhoto = personnePhoto;
        this.personneVille = personneVille;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public Compte.Role getRole() { return role; }
    public void setRole(Compte.Role role) { this.role = role; }

    public Compte.Statut getStatut() { return statut; }
    public void setStatut(Compte.Statut statut) { this.statut = statut; }

    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Long getPersonneId() { return personneId; }
    public void setPersonneId(Long personneId) { this.personneId = personneId; }

    public String getPersonneNom() { return personneNom; }
    public void setPersonneNom(String personneNom) { this.personneNom = personneNom; }

    public String getPersonnePrenom() { return personnePrenom; }
    public void setPersonnePrenom(String personnePrenom) { this.personnePrenom = personnePrenom; }

    public String getPersonnePhoto() { return personnePhoto; }
    public void setPersonnePhoto(String personnePhoto) { this.personnePhoto = personnePhoto; }

    public String getPersonneVille() { return personneVille; }
    public void setPersonneVille(String personneVille) { this.personneVille = personneVille; }

    @Override
    public String toString() {
        return "CompteResponseDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", role=" + role +
                ", statut=" + statut +
                ", isVerified=" + isVerified +
                ", lastLogin=" + lastLogin +
                ", createdAt=" + createdAt +
                ", personneId=" + personneId +
                ", personneNom='" + personneNom + '\'' +
                ", personnePrenom='" + personnePrenom + '\'' +
                ", personnePhoto='" + personnePhoto + '\'' +
                ", personneVille='" + personneVille + '\'' +
                '}';
    }
}
