package com.tissenza.tissenza_backend.modules.commande.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private com.tissenza.tissenza_backend.modules.user.entity.Personne client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panier_id", nullable = false)
    private com.tissenza.tissenza_backend.modules.panier.entity.Panier panier;

    @Column(name = "date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommandeStatus status;

    @Column(name = "total", precision = 10, scale = 2, nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_paiement")
    private StatusPaiement statusPaiement;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetailCommande> details;

    public enum CommandeStatus {
        EN_ATTENTE,
        CONFIRMEE,
        EN_PREPARATION,
        ENVOYEE,
        LIVREE,
        ANNULEE
    }

    public enum StatusPaiement {
        EN_ATTENTE,
        PAYEE,
        REMBOURSEE,
        ECHOUE
    }

    @PrePersist
    protected void onCreate() {
        date = LocalDateTime.now();
        if (status == null) {
            status = CommandeStatus.EN_ATTENTE;
        }
        if (statusPaiement == null) {
            statusPaiement = StatusPaiement.EN_ATTENTE;
        }
    }
}
