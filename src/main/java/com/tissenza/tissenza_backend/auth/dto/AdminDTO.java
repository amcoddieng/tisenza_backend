package com.tissenza.tissenza_backend.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDTO {
    
    private Long id;
    
    private String telephone;
    
    private String nom_complet;
    
    private String email;
    
    private String role;
    
    private String statut;
    
    private String otp_code;
    
    private Timestamp otp_expiration_at;
    
    private int otp_tentatives;
    
    private boolean biometrie_active;
    
    private String motif_suspension;
    
    private Timestamp created_at;
    
    private Timestamp updated_at;
    
    private Timestamp last_login_at;
    
    private String deuxFaSecret;
    
    private boolean deuxFaActive;
    
    private String permissions;
    
    private Timestamp lastActionAt;
}
