package com.tissenza.tissenza_backend.modules.user.dto;

import java.time.LocalDateTime;

public class PersonneSimpleDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String photoProfil;
    private String ville;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public PersonneSimpleDTO() {}
    
    public PersonneSimpleDTO(Long id, String nom, String prenom, String adresse, String photoProfil, String ville, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.photoProfil = photoProfil;
        this.ville = ville;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
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
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Constructeur pour créer à partir d'une entité Personne
    public static PersonneSimpleDTO fromEntity(com.tissenza.tissenza_backend.modules.user.entity.Personne personne) {
        PersonneSimpleDTO dto = new PersonneSimpleDTO();
        dto.setId(personne.getId());
        dto.setNom(personne.getNom());
        dto.setPrenom(personne.getPrenom());
        dto.setAdresse(personne.getAdresse());
        dto.setPhotoProfil(personne.getPhotoProfil());
        dto.setVille(personne.getVille());
        dto.setCreatedAt(personne.getCreatedAt());
        dto.setUpdatedAt(personne.getUpdatedAt());
        return dto;
    }
}
