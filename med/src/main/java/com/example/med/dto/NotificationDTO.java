package com.example.med.dto;

import com.example.med.model.notification.Notification;
import com.example.med.model.notification.TypeNotification;
import com.example.med.model.notification.DestinataireType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDTO {
    private Long id;
    private TypeNotification type;
    private String titre;
    private String message;
    private Long destinataireId;
    private DestinataireType destinataireType;
    private String lien;
    private boolean lu;
    private LocalDateTime dateCreation;
    private LocalDateTime dateLecture;

    // Pour l'affichage dans le frontend
    private String typeLabel;
    private String tempsEcoule;

    public static NotificationDTO fromEntity(Notification notification) {
        return NotificationDTO.builder()
                .id(notification.getId())
                .type(notification.getType())
                .titre(notification.getTitre())
                .message(notification.getMessage())
                .destinataireId(notification.getDestinataireId())
                .destinataireType(notification.getDestinataireType())
                .lien(notification.getLien())
                .lu(notification.isLu())
                .dateCreation(notification.getDateCreation())
                .dateLecture(notification.getDateLecture())
                .typeLabel(notification.getType().getLabel())
                .tempsEcoule(calculerTempsEcoule(notification.getDateCreation()))
                .build();
    }

    private static String calculerTempsEcoule(LocalDateTime date) {
        if (date == null) return "";

        LocalDateTime now = LocalDateTime.now();
        long minutes = java.time.Duration.between(date, now).toMinutes();

        if (minutes < 1) return "A l'instant";
        if (minutes < 60) return minutes + " min";

        long hours = minutes / 60;
        if (hours < 24) return hours + " h";

        long days = hours / 24;
        if (days < 7) return days + " j";

        long weeks = days / 7;
        if (weeks < 4) return weeks + " sem";

        return date.toLocalDate().toString();
    }
}
