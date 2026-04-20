package com.tissenza.tissenza_backend.modules.user.dto;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import java.time.LocalDateTime;

public class CompteWithPersonneDTO {
    private Long id;
    private String email;
    private String telephone;
    private Compte.Role role;
    private Compte.Statut statut;
    private Boolean isVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    
    // Informations de la personne
    private Long personneId;
    private String nom;
    private String prenom;
    private String adresse;
    private String photoProfil;
    private String ville;
    private LocalDateTime personneCreatedAt;
    private LocalDateTime personneUpdatedAt;
    
    public CompteWithPersonneDTO() {}
    
    public CompteWithPersonneDTO(Long id, String email, String telephone, Compte.Role role, Compte.Statut statut, Boolean isVerified, LocalDateTime lastLogin, LocalDateTime createdAt, Long personneId, String nom, String prenom, String adresse, String photoProfil, String ville, LocalDateTime personneCreatedAt, LocalDateTime personneUpdatedAt) {
        this.id = id;
        this.email = email;
        this.telephone = telephone;
        this.role = role;
        this.statut = statut;
        this.isVerified = isVerified;
        this.lastLogin = lastLogin;
        this.createdAt = createdAt;
        this.personneId = personneId;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.photoProfil = photoProfil;
        this.ville = ville;
        this.personneCreatedAt = personneCreatedAt;
        this.personneUpdatedAt = personneUpdatedAt;
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
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public String getPhotoProfil() { return photoProfil; }
    public void setPhotoProfil(String photoProfil) { this.photoProfil = photoProfil; }
    
    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }
    
    public LocalDateTime getPersonneCreatedAt() { return personneCreatedAt; }
    public void setPersonneCreatedAt(LocalDateTime personneCreatedAt) { this.personneCreatedAt = personneCreatedAt; }
    
    public LocalDateTime getPersonneUpdatedAt() { return personneUpdatedAt; }
    public void setPersonneUpdatedAt(LocalDateTime personneUpdatedAt) { this.personneUpdatedAt = personneUpdatedAt; }
    
    // Constructeur pour créer à partir d'un Compte et Personne
    public static CompteWithPersonneDTO fromEntities(Compte compte, PersonneSimpleDTO personne) {
        CompteWithPersonneDTO dto = new CompteWithPersonneDTO();
        dto.setId(compte.getId());
        dto.setEmail(compte.getEmail());
        dto.setTelephone(compte.getTelephone());
        dto.setRole(compte.getRole());
        dto.setStatut(compte.getStatut());
        dto.setIsVerified(compte.getIsVerified());
        dto.setLastLogin(compte.getLastLogin());
        dto.setCreatedAt(compte.getCreatedAt());
        
        if (personne != null) {
            dto.setPersonneId(personne.getId());
            dto.setNom(personne.getNom());
            dto.setPrenom(personne.getPrenom());
            dto.setAdresse(personne.getAdresse());
            dto.setPhotoProfil(personne.getPhotoProfil());
            dto.setVille(personne.getVille());
            dto.setPersonneCreatedAt(personne.getCreatedAt());
            dto.setPersonneUpdatedAt(personne.getUpdatedAt());
        }
        
        return dto;
    }
}
