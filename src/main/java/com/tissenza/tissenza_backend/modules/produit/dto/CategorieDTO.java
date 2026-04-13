package com.tissenza.tissenza_backend.modules.produit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDTO {
    private Long id;
    private String nom;
    private String description;
    private String image;
    private LocalDateTime createdAt;
}
