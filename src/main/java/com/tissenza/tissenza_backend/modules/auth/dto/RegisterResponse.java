package com.tissenza.tissenza_backend.modules.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

    private Long userId;
    private String email;
    private String username;
    private String role;
    private String message;
}
