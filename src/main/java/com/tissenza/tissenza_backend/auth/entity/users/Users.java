package com.tissenza.tissenza_backend.auth.entity.users;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "telephone", unique = false, nullable = false)
    private String telephone;
    @Column(name = "nom_complet")
    private String nom_complet;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "role")
    private String role;
    @Column(name = "statut")
    private String statut;
    @Column(name = "otp_code")
    private String otp_code;
    @Column(name = "otp_expiration_at")
    private Timestamp otp_expiration_at;
    @Column(name = "otp_tentatives")
    private int otp_tentatives;
    @Column(name = "biometrie_active")
    private boolean biometrie_active;
    @Column(name = "motif_suspension")
    private String motif_suspension;
    @Column(name = "created_at")
    private Timestamp created_at;
    @Column(name = "updated_at")
    private Timestamp updated_at;
    @Column(name = "last_login_at")
    private Timestamp last_login_at;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNom_complet() {
        return nom_complet;
    }

    public void setNom_complet(String nom_complet) {
        this.nom_complet = nom_complet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getOtp_code() {
        return otp_code;
    }

    public void setOtp_code(String otp_code) {
        this.otp_code = otp_code;
    }

    public Timestamp getOtp_expiration_at() {
        return otp_expiration_at;
    }

    public void setOtp_expiration_at(Timestamp otp_expiration_at) {
        this.otp_expiration_at = otp_expiration_at;
    }

    public int getOtp_tentatives() {
        return otp_tentatives;
    }

    public void setOtp_tentatives(int otp_tentatives) {
        this.otp_tentatives = otp_tentatives;
    }

    public boolean isBiometrie_active() {
        return biometrie_active;
    }

    public void setBiometrie_active(boolean biometrie_active) {
        this.biometrie_active = biometrie_active;
    }

    public String getMotif_suspension() {
        return motif_suspension;
    }

    public void setMotif_suspension(String motif_suspension) {
        this.motif_suspension = motif_suspension;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Timestamp getLast_login_at() {
        return last_login_at;
    }

    public void setLast_login_at(Timestamp last_login_at) {
        this.last_login_at = last_login_at;
    }
}
