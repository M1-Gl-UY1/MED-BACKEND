package com.example.med.controller;

import com.example.med.dto.NotificationDTO;
import com.example.med.service.notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST pour la gestion des notifications
 *
 * Endpoints:
 * - GET /api/notifications/admin : toutes les notifications admin
 * - GET /api/notifications/admin/unread : notifications non lues admin
 * - GET /api/notifications/admin/count : nombre de non lues admin
 * - PUT /api/notifications/{id}/read : marquer comme lue
 * - PUT /api/notifications/admin/read-all : marquer toutes comme lues
 * - DELETE /api/notifications/{id} : supprimer une notification
 */
@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Recuperer toutes les notifications pour admin
     */
    @GetMapping("/admin")
    public ResponseEntity<List<NotificationDTO>> getNotificationsAdmin() {
        return ResponseEntity.ok(notificationService.getNotificationsAdmin());
    }

    /**
     * Recuperer les notifications non lues pour admin
     */
    @GetMapping("/admin/unread")
    public ResponseEntity<List<NotificationDTO>> getNotificationsNonLuesAdmin() {
        return ResponseEntity.ok(notificationService.getNotificationsNonLuesAdmin());
    }

    /**
     * Compter les notifications non lues pour admin
     */
    @GetMapping("/admin/count")
    public ResponseEntity<Map<String, Long>> countNotificationsNonLuesAdmin() {
        Map<String, Long> response = new HashMap<>();
        response.put("count", notificationService.countNotificationsNonLuesAdmin());
        return ResponseEntity.ok(response);
    }

    /**
     * Marquer une notification comme lue
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> marquerCommeLue(@PathVariable Long id) {
        notificationService.marquerCommeLue(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Marquer toutes les notifications admin comme lues
     */
    @PutMapping("/admin/read-all")
    public ResponseEntity<Void> marquerToutesLuesAdmin() {
        notificationService.marquerToutesLuesAdmin();
        return ResponseEntity.ok().build();
    }

    /**
     * Supprimer une notification
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable Long id) {
        notificationService.supprimer(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Endpoint de test pour envoyer une notification
     * A utiliser uniquement en developpement
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, String>> testNotification() {
        notificationService.notifierNouvelleCommande(
                "CMD-TEST-001",
                "Client Test",
                1500000
        );
        Map<String, String> response = new HashMap<>();
        response.put("message", "Notification de test envoyee");
        return ResponseEntity.ok(response);
    }
}
