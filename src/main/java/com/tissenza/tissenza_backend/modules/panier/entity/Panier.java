package com.tissenza.tissenza_backend.modules.panier.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "paniers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private com.tissenza.tissenza_backend.modules.user.entity.Personne client;

    @Column(name = "date_creation", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreation;

    @Column(name = "updated_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PanierStatus status;

    @Column(name = "total", precision = 10, scale = 2)
    private java.math.BigDecimal total = java.math.BigDecimal.ZERO;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PanierItem> items;

    public enum PanierStatus {
        EN_ATTENTE,
        VALIDE,
        ANNULE
    }

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (status == null) {
            status = PanierStatus.EN_ATTENTE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
