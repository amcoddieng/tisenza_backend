package com.tissenza.tissenza_backend.modules.user.dto;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import java.time.LocalDateTime;

public class CompteDTO {
    private Long id;
    private Long personneId;
    private String email;
    private String motDePasse;
    private String telephone;
    private Compte.Role role;
    private Compte.Statut statut;
    private Boolean isVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    
    public CompteDTO() {}
    
    public CompteDTO(Long id, Long personneId, String email, String motDePasse, String telephone, Compte.Role role, Compte.Statut statut, Boolean isVerified, LocalDateTime lastLogin, LocalDateTime createdAt) {
        this.id = id;
        this.personneId = personneId;
        this.email = email;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.role = role;
        this.statut = statut;
        this.isVerified = isVerified;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPersonneId() { return personneId; }
    public void setPersonneId(Long personneId) { this.personneId = personneId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    
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
}
