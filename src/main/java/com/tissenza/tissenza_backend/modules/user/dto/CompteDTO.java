package com.tissenza.tissenza_backend.modules.user.dto;

import com.tissenza.tissenza_backend.modules.user.entity.Compte;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
