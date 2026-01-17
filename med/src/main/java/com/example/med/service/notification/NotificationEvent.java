package com.example.med.service.notification;

import com.example.med.model.notification.TypeNotification;
import com.example.med.model.notification.DestinataireType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

/**
 * Evenement de notification transmis aux observers
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEvent {
    private TypeNotification type;
    private String titre;
    private String message;
    private Long destinataireId;      // null = broadcast
    private DestinataireType destinataireType;
    private String lien;
    private Map<String, Object> data; // Donnees supplementaires (optionnel)

    /**
     * Creer une notification pour les admins
     */
    public static NotificationEvent pourAdmin(TypeNotification type, String titre, String message, String lien) {
        return NotificationEvent.builder()
                .type(type)
                .titre(titre)
                .message(message)
                .destinataireType(DestinataireType.ADMIN)
                .destinataireId(null)
                .lien(lien)
                .build();
    }

    /**
     * Creer une notification pour un client
     */
    public static NotificationEvent pourClient(TypeNotification type, String titre, String message, Long clientId, String lien) {
        return NotificationEvent.builder()
                .type(type)
                .titre(titre)
                .message(message)
                .destinataireType(DestinataireType.CLIENT)
                .destinataireId(clientId)
                .lien(lien)
                .build();
    }

    /**
     * Creer une notification pour une societe
     */
    public static NotificationEvent pourSociete(TypeNotification type, String titre, String message, Long societeId, String lien) {
        return NotificationEvent.builder()
                .type(type)
                .titre(titre)
                .message(message)
                .destinataireType(DestinataireType.SOCIETE)
                .destinataireId(societeId)
                .lien(lien)
                .build();
    }
}
