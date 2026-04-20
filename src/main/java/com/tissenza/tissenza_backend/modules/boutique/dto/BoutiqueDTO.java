package com.tissenza.tissenza_backend.modules.boutique.dto;

import com.tissenza.tissenza_backend.modules.boutique.entity.Boutique;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité Boutique
 * Résout le problème de LazyInitializationException en n'exposant que les champs nécessaires
 */
public class BoutiqueDTO {
    private Long id;
    private String nom;
    private String description;
    private String addresse;
    private String logo;
    private Float note;
    private Boutique.Statut statut;
    private LocalDateTime createdAt;
    
    // Informations du vendeur (évite le lazy loading)
    private Long vendeurId;
    private String vendeurEmail;
    private String vendeurNom; // nom de la personne associée au compte vendeur
    
    // Pour accepter le format JSON avec vendeur imbriqué
    private VendeurInfo vendeur;
    
    public BoutiqueDTO() {}
    
    public BoutiqueDTO(Long id, String nom, String description, String addresse, String logo, 
                    Float note, Boutique.Statut statut, LocalDateTime createdAt, 
                    Long vendeurId, String vendeurEmail, String vendeurNom) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.addresse = addresse;
        this.logo = logo;
        this.note = note;
        this.statut = statut;
        this.createdAt = createdAt;
        this.vendeurId = vendeurId;
        this.vendeurEmail = vendeurEmail;
        this.vendeurNom = vendeurNom;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAddresse() { return addresse; }
    public void setAddresse(String addresse) { this.addresse = addresse; }
    
    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }
    
    public Float getNote() { return note; }
    public void setNote(Float note) { this.note = note; }
    
    public Boutique.Statut getStatut() { return statut; }
    public void setStatut(Boutique.Statut statut) { this.statut = statut; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Long getVendeurId() { return vendeurId; }
    public void setVendeurId(Long vendeurId) { this.vendeurId = vendeurId; }
    
    public String getVendeurEmail() { return vendeurEmail; }
    public void setVendeurEmail(String vendeurEmail) { this.vendeurEmail = vendeurEmail; }
    
    public String getVendeurNom() { return vendeurNom; }
    public void setVendeurNom(String vendeurNom) { this.vendeurNom = vendeurNom; }
    
    public VendeurInfo getVendeur() { return vendeur; }
    public void setVendeur(VendeurInfo vendeur) { 
        this.vendeur = vendeur;
        // Extraire automatiquement l'ID si disponible
        if (vendeur != null && vendeur.getId() != null) {
            this.vendeurId = vendeur.getId();
        }
    }
    
    @Override
    public String toString() {
        return "BoutiqueDTO{" +
                "id=" + getId() +
                ", nom='" + getNom() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", addresse='" + getAddresse() + '\'' +
                ", logo='" + getLogo() + '\'' +
                ", note=" + getNote() +
                ", statut=" + getStatut() +
                ", createdAt=" + getCreatedAt() +
                ", vendeurId=" + getVendeurId() +
                ", vendeurEmail='" + getVendeurEmail() + '\'' +
                ", vendeurNom='" + getVendeurNom() + '\'' +
                '}';
    }
}
