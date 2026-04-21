package com.tissenza.tissenza_backend.modules.paiement.dto;

import com.tissenza.tissenza_backend.modules.user.dto.CompteDTO;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * DTO pour l'entité UserMoyenPaiement
 */
public class UserMoyenPaiementDTO {
    
    private Long id;
    private Long userId;
    private String userEmail;
    private String userNom; // Nom de la personne associée au compte
    private Long moyenPaiementId;
    private String moyenPaiementNom;
    private String moyenPaiementPhoto;
    private Boolean actif;
    private String numero;
    private LocalDateTime createdAt;

    // Constructeurs
    public UserMoyenPaiementDTO() {}

    public UserMoyenPaiementDTO(Long userId, Long moyenPaiementId, Boolean actif) {
        this.userId = userId;
        this.moyenPaiementId = moyenPaiementId;
        this.actif = actif;
    }

    public UserMoyenPaiementDTO(Long userId, Long moyenPaiementId, Boolean actif, String numero) {
        this.userId = userId;
        this.moyenPaiementId = moyenPaiementId;
        this.actif = actif;
        this.numero = numero;
    }

    public UserMoyenPaiementDTO(Long id, Long userId, String userEmail, String userNom,
                               Long moyenPaiementId, String moyenPaiementNom, 
                               String moyenPaiementPhoto, Boolean actif, String numero, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userNom = userNom;
        this.moyenPaiementId = moyenPaiementId;
        this.moyenPaiementNom = moyenPaiementNom;
        this.moyenPaiementPhoto = moyenPaiementPhoto;
        this.actif = actif;
        this.numero = numero;
        this.createdAt = createdAt;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNom() {
        return userNom;
    }

    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }

    public Long getMoyenPaiementId() {
        return moyenPaiementId;
    }

    public void setMoyenPaiementId(Long moyenPaiementId) {
        this.moyenPaiementId = moyenPaiementId;
    }

    public String getMoyenPaiementNom() {
        return moyenPaiementNom;
    }

    public void setMoyenPaiementNom(String moyenPaiementNom) {
        this.moyenPaiementNom = moyenPaiementNom;
    }

    public String getMoyenPaiementPhoto() {
        return moyenPaiementPhoto;
    }

    public void setMoyenPaiementPhoto(String moyenPaiementPhoto) {
        this.moyenPaiementPhoto = moyenPaiementPhoto;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "UserMoyenPaiementDTO{" +
                "id=" + getId() +
                ", userId=" + getUserId() +
                ", userEmail='" + getUserEmail() + '\'' +
                ", userNom='" + getUserNom() + '\'' +
                ", moyenPaiementId=" + getMoyenPaiementId() +
                ", moyenPaiementNom='" + getMoyenPaiementNom() + '\'' +
                ", moyenPaiementPhoto='" + getMoyenPaiementPhoto() + '\'' +
                ", actif=" + getActif() +
                ", numero='" + getNumero() + '\'' +
                ", createdAt=" + getCreatedAt() +
                '}';
    }
}
