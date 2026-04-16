package com.tissenza.tissenza_backend.modules.produit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SousCategorieDTO {
    private Long id;
    private Long categorieId;
    private String nom;
    private String description;
    private LocalDateTime createdAt;
}
