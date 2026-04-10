package com.tissenza.tissenza_backend.auth.entity.admin;

import java.sql.Timestamp;

import com.tissenza.tissenza_backend.auth.entity.users.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// Ajouter des annotations JPA
@Entity
@Table(name = "administrateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Administrateur extends Users {

    @Column(name = "deux_fa_secret")
    private String deuxFaSecret;
    
    @Column(name = "deux_fa_active")
    private boolean deuxFaActive;
    
    @Column(name = "permissions", columnDefinition = "jsonb")
    private String permissions;
    
    @Column(name = "last_action_at")
    private Timestamp lastActionAt;

    /**
     * Constructeur avec paramètres de base
     */
    public Administrateur(String telephone, String nom_complet, String email, String role) {
        super();
        this.setTelephone(telephone);
        this.setNom_complet(nom_complet);
        this.setEmail(email);
        this.setRole(role);
    }
    
}
