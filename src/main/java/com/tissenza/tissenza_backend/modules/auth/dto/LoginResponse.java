package com.tissenza.tissenza_backend.modules.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private String type;
    private Long userId;
    private String email;
    private String username;
    private String role;
    private String photoProfil;
    private Long expiresIn;
    
    public LoginResponse(String token, Long userId, String email, String username, String role, String photoProfil, Long expiresIn) {
        this.token = token;
        this.type = "Bearer";
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.role = role;
        this.photoProfil = photoProfil;
        this.expiresIn = expiresIn;
    }
}
