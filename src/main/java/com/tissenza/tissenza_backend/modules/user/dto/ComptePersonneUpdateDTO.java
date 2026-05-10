package com.tissenza.tissenza_backend.modules.user.dto;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComptePersonneUpdateDTO {
    
    // Informations du compte
    private String email;
    private String telephone;
    private String motDePasse;
    private Compte.Role role;
    private Compte.Statut statut;
    private Boolean isVerified;
    
    // Informations de la personne
    private String nom;
    private String prenom;
    private String adresse;
    private String ville;
    private String photoProfil;
}
