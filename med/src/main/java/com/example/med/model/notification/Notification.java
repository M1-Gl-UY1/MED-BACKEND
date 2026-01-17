package com.example.med.model.notification;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

/**
 * Entite Notification
 * Stocke les notifications envoyees aux utilisateurs (admins et clients)
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeNotification type;

    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String message;

    // ID du destinataire (null = broadcast a tous les admins)
    private Long destinataireId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DestinataireType destinataireType;

    // Lien vers la ressource concernee (ex: /commandes/123)
    private String lien;

    @Column(nullable = false)
    private boolean lu = false;

    @Column(nullable = false)
    private LocalDateTime dateCreation;

    private LocalDateTime dateLecture;

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}
