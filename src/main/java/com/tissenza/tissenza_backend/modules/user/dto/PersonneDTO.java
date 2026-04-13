package com.tissenza.tissenza_backend.modules.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonneDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String adresse;
    private String photoProfil;
    private String ville;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
