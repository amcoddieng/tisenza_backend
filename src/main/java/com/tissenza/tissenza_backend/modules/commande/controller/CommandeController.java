package com.tissenza.tissenza_backend.modules.commande.controller;

import com.tissenza.tissenza_backend.modules.commande.dto.CommandeDTO;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande.CommandeStatus;
import com.tissenza.tissenza_backend.modules.commande.entity.Commande.StatusPaiement;
import com.tissenza.tissenza_backend.modules.commande.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestion des Commandes", description = "APIs pour la gestion des commandes")
public class CommandeController {

    private final CommandeService commandeService;

    /**
     * Créer une commande à partir d'un panier
     */
    @PostMapping("/creer/{panierId}")
    @Operation(summary = "Créer une commande", description = "Crée une commande à partir d'un panier validé")
    public ResponseEntity<CommandeDTO> creerCommande(
            @Parameter(description = "ID du panier") @PathVariable Long panierId) {
        
        try {
            CommandeDTO commande = commandeService.creerCommandeDepuisPanier(panierId);
            return new ResponseEntity<>(commande, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Erreur lors de la création de la commande depuis le panier {}: {}", panierId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Récupérer une commande par ID
     */
    @GetMapping("/{commandeId}")
    @Operation(summary = "Récupérer une commande", description = "Récupère les détails d'une commande spécifique")
    public ResponseEntity<CommandeDTO> getCommandeById(
            @Parameter(description = "ID de la commande") @PathVariable Long commandeId) {
        
        try {
            CommandeDTO commande = commandeService.getCommandeById(commandeId);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération de la commande {}: {}", commandeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Récupérer toutes les commandes d'un client
     */
    @GetMapping("/client/{clientId}")
    @Operation(summary = "Récupérer les commandes d'un client", description = "Récupère toutes les commandes d'un client")
    public ResponseEntity<List<CommandeDTO>> getCommandesByClient(
            @Parameter(description = "ID du client") @PathVariable Long clientId) {
        
        try {
            List<CommandeDTO> commandes = commandeService.getCommandesByClient(clientId);
            return ResponseEntity.ok(commandes);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des commandes pour le client {}: {}", clientId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer les commandes par statut
     */
    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les commandes par statut", description = "Récupère les commandes filtrées par statut")
    public ResponseEntity<List<CommandeDTO>> getCommandesByStatut(
            @Parameter(description = "Statut de la commande") @PathVariable CommandeStatus statut) {
        
        try {
            List<CommandeDTO> commandes = commandeService.getCommandesByStatut(statut);
            return ResponseEntity.ok(commandes);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des commandes par statut {}: {}", statut, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupérer les commandes par statut de paiement
     */
    @GetMapping("/paiement/{statutPaiement}")
    @Operation(summary = "Récupérer les commandes par statut de paiement", description = "Récupère les commandes filtrées par statut de paiement")
    public ResponseEntity<List<CommandeDTO>> getCommandesByStatutPaiement(
            @Parameter(description = "Statut de paiement") @PathVariable StatusPaiement statutPaiement) {
        
        try {
            List<CommandeDTO> commandes = commandeService.getCommandesByStatutPaiement(statutPaiement);
            return ResponseEntity.ok(commandes);
        } catch (Exception e) {
            log.error("Erreur lors de la récupération des commandes par statut de paiement {}: {}", statutPaiement, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Mettre à jour le statut d'une commande
     */
    @PutMapping("/{commandeId}/statut")
    @Operation(summary = "Mettre à jour le statut", description = "Met à jour le statut d'une commande")
    public ResponseEntity<CommandeDTO> mettreAJourStatut(
            @Parameter(description = "ID de la commande") @PathVariable Long commandeId,
            @Parameter(description = "Nouveau statut") @RequestParam CommandeStatus statut) {
        
        try {
            CommandeDTO commande = commandeService.mettreAJourStatut(commandeId, statut);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du statut de la commande {}: {}", commandeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Mettre à jour le statut de paiement d'une commande
     */
    @PutMapping("/{commandeId}/paiement")
    @Operation(summary = "Mettre à jour le statut de paiement", description = "Met à jour le statut de paiement d'une commande")
    public ResponseEntity<CommandeDTO> mettreAJourStatutPaiement(
            @Parameter(description = "ID de la commande") @PathVariable Long commandeId,
            @Parameter(description = "Nouveau statut de paiement") @RequestParam StatusPaiement statutPaiement) {
        
        try {
            CommandeDTO commande = commandeService.mettreAJourStatutPaiement(commandeId, statutPaiement);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            log.error("Erreur lors de la mise à jour du statut de paiement de la commande {}: {}", commandeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Annuler une commande
     */
    @PostMapping("/{commandeId}/annuler")
    @Operation(summary = "Annuler une commande", description = "Annule une commande (si elle n'est pas encore livrée)")
    public ResponseEntity<CommandeDTO> annulerCommande(
            @Parameter(description = "ID de la commande") @PathVariable Long commandeId) {
        
        try {
            CommandeDTO commande = commandeService.annulerCommande(commandeId);
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            log.error("Erreur lors de l'annulation de la commande {}: {}", commandeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Calculer le chiffre d'affaires total
     */
    @GetMapping("/chiffre-affaires")
    @Operation(summary = "Calculer le chiffre d'affaires", description = "Calcule le chiffre d'affaires total des commandes livrées")
    public ResponseEntity<BigDecimal> getChiffreAffaires() {
        
        try {
            BigDecimal chiffreAffaires = commandeService.calculerChiffreAffairesTotal();
            return ResponseEntity.ok(chiffreAffaires);
        } catch (Exception e) {
            log.error("Erreur lors du calcul du chiffre d'affaires: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Supprimer une commande (administrateur)
     */
    @DeleteMapping("/{commandeId}")
    @Operation(summary = "Supprimer une commande", description = "Supprime une commande (réservé aux administrateurs)")
    public ResponseEntity<Void> supprimerCommande(
            @Parameter(description = "ID de la commande") @PathVariable Long commandeId) {
        
        try {
            commandeService.supprimerCommande(commandeId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de la commande {}: {}", commandeId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
