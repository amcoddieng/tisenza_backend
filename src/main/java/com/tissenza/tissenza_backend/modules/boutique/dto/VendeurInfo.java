package com.tissenza.tissenza_backend.modules.boutique.dto;

/**
 * Classe interne pour représenter les informations du vendeur dans le JSON
 * Permet d'accepter le format: {"vendeur": {"id": 1}}
 */
public class VendeurInfo {
    private Long id;
    
    public VendeurInfo() {}
    
    public VendeurInfo(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
}
