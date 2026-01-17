package com.example.med.service.notification;

import com.example.med.dto.NotificationDTO;
import com.example.med.model.notification.DestinataireType;
import com.example.med.model.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * PATTERN OBSERVER - ConcreteObserver
 *
 * Observer qui envoie les notifications en temps reel via WebSocket.
 * Priorite moyenne (50).
 */
@Component
public class WebSocketNotificationObserver implements NotificationObserver {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketNotificationObserver(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onNotification(NotificationEvent event) {
        // Creer un DTO pour l'envoi
        NotificationDTO dto = NotificationDTO.builder()
                .type(event.getType())
                .titre(event.getTitre())
                .message(event.getMessage())
                .destinataireId(event.getDestinataireId())
                .destinataireType(event.getDestinataireType())
                .lien(event.getLien())
                .lu(false)
                .dateCreation(LocalDateTime.now())
                .typeLabel(event.getType().getLabel())
                .tempsEcoule("A l'instant")
                .build();

        // Envoyer selon le type de destinataire
        if (event.getDestinataireType() == DestinataireType.ADMIN) {
            // Broadcast a tous les admins connectes
            messagingTemplate.convertAndSend("/topic/notifications/admin", dto);
            System.out.println("[WebSocketObserver] Notification envoyee aux admins: " + event.getTitre());
        } else if (event.getDestinataireId() != null) {
            // Envoyer a un utilisateur specifique
            String destination = "/topic/notifications/user/" + event.getDestinataireId();
            messagingTemplate.convertAndSend(destination, dto);
            System.out.println("[WebSocketObserver] Notification envoyee a l'utilisateur " + event.getDestinataireId() + ": " + event.getTitre());
        }
    }

    @Override
    public int getPriority() {
        return 50; // Priorite moyenne
    }
}
