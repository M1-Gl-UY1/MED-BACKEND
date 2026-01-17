package com.example.med.service.notification;

import com.example.med.model.notification.Notification;
import com.example.med.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PATTERN OBSERVER - ConcreteObserver
 *
 * Observer qui sauvegarde les notifications en base de donnees.
 * Priorite haute (1) pour etre execute en premier.
 */
@Component
public class DatabaseNotificationObserver implements NotificationObserver {

    private final NotificationRepository notificationRepository;

    @Autowired
    public DatabaseNotificationObserver(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void onNotification(NotificationEvent event) {
        Notification notification = Notification.builder()
                .type(event.getType())
                .titre(event.getTitre())
                .message(event.getMessage())
                .destinataireId(event.getDestinataireId())
                .destinataireType(event.getDestinataireType())
                .lien(event.getLien())
                .lu(false)
                .build();

        notificationRepository.save(notification);

        System.out.println("[DatabaseObserver] Notification sauvegardee: " + event.getTitre());
    }

    @Override
    public int getPriority() {
        return 1; // Haute priorite - sauvegarde en premier
    }
}
