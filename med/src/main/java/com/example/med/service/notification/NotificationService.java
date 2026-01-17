package com.example.med.service.notification;

import com.example.med.dto.NotificationDTO;
import com.example.med.model.notification.DestinataireType;
import com.example.med.model.notification.Notification;
import com.example.med.model.notification.TypeNotification;
import com.example.med.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PATTERN OBSERVER - Subject
 *
 * Service de notification qui gere les observers et emet les notifications.
 * Spring injecte automatiquement tous les beans implementant NotificationObserver.
 */
@Service
public class NotificationService {

    private final List<NotificationObserver> observers;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(List<NotificationObserver> observers, NotificationRepository notificationRepository) {
        // Trier les observers par priorite
        this.observers = observers.stream()
                .sorted(Comparator.comparingInt(NotificationObserver::getPriority))
                .collect(Collectors.toList());
        this.notificationRepository = notificationRepository;

        System.out.println("[NotificationService] " + observers.size() + " observers enregistres");
    }

    /**
     * Notifie tous les observers d'un evenement
     */
    public void notifier(NotificationEvent event) {
        for (NotificationObserver observer : observers) {
            try {
                observer.onNotification(event);
            } catch (Exception e) {
                System.err.println("[NotificationService] Erreur dans l'observer " +
                        observer.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    // ==================== Methodes utilitaires pour creer des notifications ====================

    /**
     * Notifier les admins d'une nouvelle commande
     */
    public void notifierNouvelleCommande(String reference, String clientNom, double montant) {
        notifier(NotificationEvent.pourAdmin(
                TypeNotification.NOUVELLE_COMMANDE,
                "Nouvelle commande #" + reference,
                "Commande de " + clientNom + " pour " + String.format("%,.0f", montant) + " XAF",
                "/commandes"
        ));
    }

    /**
     * Notifier un client du changement de statut de sa commande
     */
    public void notifierStatutCommande(Long clientId, DestinataireType clientType, String reference, String nouveauStatut) {
        NotificationEvent event = NotificationEvent.builder()
                .type(TypeNotification.COMMANDE_VALIDEE)
                .titre("Commande " + nouveauStatut)
                .message("Votre commande #" + reference + " est maintenant " + nouveauStatut)
                .destinataireId(clientId)
                .destinataireType(clientType)
                .lien("/mes-commandes")
                .build();
        notifier(event);
    }

    /**
     * Notifier les admins d'un stock faible
     */
    public void notifierStockFaible(String vehiculeNom, int quantite) {
        notifier(NotificationEvent.pourAdmin(
                TypeNotification.STOCK_FAIBLE,
                "Stock faible: " + vehiculeNom,
                "Il ne reste que " + quantite + " unite(s) en stock",
                "/stock"
        ));
    }

    /**
     * Notifier les admins d'une nouvelle inscription
     */
    public void notifierNouvelleInscription(String nom, String type) {
        notifier(NotificationEvent.pourAdmin(
                TypeNotification.NOUVELLE_INSCRIPTION,
                "Nouvelle inscription",
                nom + " (" + type + ") vient de s'inscrire",
                "/clients"
        ));
    }

    // ==================== Methodes CRUD pour les notifications ====================

    /**
     * Recuperer toutes les notifications pour admin
     */
    public List<NotificationDTO> getNotificationsAdmin() {
        return notificationRepository.findAllForAdmin().stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Recuperer les notifications non lues pour admin
     */
    public List<NotificationDTO> getNotificationsNonLuesAdmin() {
        return notificationRepository.findUnreadForAdmin().stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Compter les notifications non lues pour admin
     */
    public long countNotificationsNonLuesAdmin() {
        return notificationRepository.countUnreadForAdmin();
    }

    /**
     * Marquer une notification comme lue
     */
    @Transactional
    public void marquerCommeLue(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setLu(true);
            notification.setDateLecture(LocalDateTime.now());
            notificationRepository.save(notification);
        });
    }

    /**
     * Marquer toutes les notifications admin comme lues
     */
    @Transactional
    public void marquerToutesLuesAdmin() {
        List<Notification> nonLues = notificationRepository.findUnreadForAdmin();
        LocalDateTime now = LocalDateTime.now();
        nonLues.forEach(n -> {
            n.setLu(true);
            n.setDateLecture(now);
        });
        notificationRepository.saveAll(nonLues);
    }

    /**
     * Supprimer une notification
     */
    @Transactional
    public void supprimer(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    /**
     * Recuperer les notifications pour un utilisateur (client/societe)
     */
    public List<NotificationDTO> getNotificationsUtilisateur(Long userId, DestinataireType type) {
        return notificationRepository.findByDestinataire(userId, type).stream()
                .map(NotificationDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Compter les notifications non lues pour un utilisateur
     */
    public long countNotificationsNonLuesUtilisateur(Long userId, DestinataireType type) {
        return notificationRepository.countUnreadByDestinataire(userId, type);
    }
}
