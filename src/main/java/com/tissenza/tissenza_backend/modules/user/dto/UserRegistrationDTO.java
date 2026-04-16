package com.tissenza.tissenza_backend.modules.user.dto;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    private String nom;
    private String prenom;
    private String adresse;
    private String photoProfil;
    private String ville;
    private String email;
    private String telephone;
    private String motDePasse;
    private Compte.Role role;
}
