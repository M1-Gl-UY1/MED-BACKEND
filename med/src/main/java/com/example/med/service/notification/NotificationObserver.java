package com.example.med.service.notification;

/**
 * PATTERN OBSERVER - Interface Observer
 *
 * Interface pour tous les observers de notifications.
 * Chaque observer reagit aux evenements de notification a sa maniere.
 */
public interface NotificationObserver {

    /**
     * Methode appelee quand une notification est emise
     * @param event L'evenement de notification
     */
    void onNotification(NotificationEvent event);

    /**
     * Retourne l'ordre de priorite de l'observer
     * Les observers avec une priorite plus basse sont executes en premier
     */
    default int getPriority() {
        return 100;
    }
}
