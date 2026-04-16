package com.tissenza.tissenza_backend.modules.commande.controller;

import com.tissenza.tissenza_backend.modules.commande.entity.Commande;
import com.tissenza.tissenza_backend.modules.commande.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@CrossOrigin(origins = "*")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    /**
     * Créer une nouvelle commande
     */
    @PostMapping
    public ResponseEntity<Commande> creerCommande(@RequestBody CreerCommandeRequest request) {
        try {
            Commande commande = commandeService.creerCommande(
                    request.getClientId(),
                    request.getLignesCommande(),
                    request.getAdresseLivraison(),
                    request.getNotes(),
                    request.getDateLivraisonSouhaitee()
            );
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtenir une commande par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable Long id) {
        try {
            Commande commande = commandeService.getCommandeById(id);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtenir une commande par son numéro
     */
    @GetMapping("/numero/{numeroCommande}")
    public ResponseEntity<Commande> getCommandeByNumero(@PathVariable String numeroCommande) {
        try {
            Commande commande = commandeService.getCommandeByNumero(numeroCommande);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Lister les commandes d'un client
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Commande>> getCommandesByClient(@PathVariable Long clientId) {
        List<Commande> commandes = commandeService.getCommandesByClient(clientId);
        return ResponseEntity.ok(commandes);
    }

    /**
     * Lister les commandes par statut
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<Commande>> getCommandesByStatut(@PathVariable String statut) {
        try {
            Commande.StatutCommande statutCommande = Commande.StatutCommande.valueOf(statut.toUpperCase());
            List<Commande> commandes = commandeService.getCommandesByStatut(statutCommande);
            return ResponseEntity.ok(commandes);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Lister les commandes en retard
     */
    @GetMapping("/en-retard")
    public ResponseEntity<List<Commande>> getCommandesEnRetard() {
        List<Commande> commandes = commandeService.getCommandesEnRetard();
        return ResponseEntity.ok(commandes);
    }

    /**
     * Valider une commande
     */
    @PutMapping("/{id}/valider")
    public ResponseEntity<Commande> validerCommande(@PathVariable Long id, 
                                                   @RequestBody ChangementStatutRequest request) {
        try {
            Commande commande = commandeService.validerCommande(id, request.getModifieParId(), request.getMotif());
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Mettre une commande en préparation
     */
    @PutMapping("/{id}/preparation")
    public ResponseEntity<Commande> mettreEnPreparation(@PathVariable Long id, 
                                                       @RequestBody ChangementStatutRequest request) {
        try {
            Commande commande = commandeService.mettreEnPreparation(id, request.getModifieParId(), request.getMotif());
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Marquer une commande comme prête
     */
    @PutMapping("/{id}/prete")
    public ResponseEntity<Commande> marquerCommePrete(@PathVariable Long id, 
                                                     @RequestBody ChangementStatutRequest request) {
        try {
            Commande commande = commandeService.marquerCommePrete(id, request.getModifieParId(), request.getMotif());
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Mettre une commande en livraison
     */
    @PutMapping("/{id}/livraison")
    public ResponseEntity<Commande> mettreEnLivraison(@PathVariable Long id, 
                                                     @RequestBody ChangementStatutRequest request) {
        try {
            Commande commande = commandeService.mettreEnLivraison(id, request.getModifieParId(), request.getMotif());
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Livrer une commande
     */
    @PutMapping("/{id}/livrer")
    public ResponseEntity<Commande> livrerCommande(@PathVariable Long id, 
                                                  @RequestBody ChangementStatutRequest request) {
        try {
            Commande commande = commandeService.livrerCommande(id, request.getModifieParId(), request.getMotif());
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Annuler une commande
     */
    @PutMapping("/{id}/annuler")
    public ResponseEntity<Commande> annulerCommande(@PathVariable Long id, 
                                                   @RequestBody ChangementStatutRequest request) {
        try {
            Commande commande = commandeService.annulerCommande(id, request.getModifieParId(), request.getMotif());
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtenir les statistiques des commandes
     */
    @GetMapping("/statistiques")
    public ResponseEntity<CommandeService.CommandeStatistics> getStatistiquesCommandes() {
        CommandeService.CommandeStatistics statistiques = commandeService.getStatistiquesCommandes();
        return ResponseEntity.ok(statistiques);
    }

    // DTOs pour les requêtes

    public static class CreerCommandeRequest {
        private Long clientId;
        private List<CommandeService.LigneCommandeRequest> lignesCommande;
        private String adresseLivraison;
        private String notes;
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        private LocalDateTime dateLivraisonSouhaitee;

        // Getters et Setters
        public Long getClientId() { return clientId; }
        public void setClientId(Long clientId) { this.clientId = clientId; }
        public List<CommandeService.LigneCommandeRequest> getLignesCommande() { return lignesCommande; }
        public void setLignesCommande(List<CommandeService.LigneCommandeRequest> lignesCommande) { this.lignesCommande = lignesCommande; }
        public String getAdresseLivraison() { return adresseLivraison; }
        public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }
        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }
        public LocalDateTime getDateLivraisonSouhaitee() { return dateLivraisonSouhaitee; }
        public void setDateLivraisonSouhaitee(LocalDateTime dateLivraisonSouhaitee) { this.dateLivraisonSouhaitee = dateLivraisonSouhaitee; }
    }

    public static class ChangementStatutRequest {
        private Long modifieParId;
        private String motif;

        // Getters et Setters
        public Long getModifieParId() { return modifieParId; }
        public void setModifieParId(Long modifieParId) { this.modifieParId = modifieParId; }
        public String getMotif() { return motif; }
        public void setMotif(String motif) { this.motif = motif; }
    }
}
