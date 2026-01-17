package com.example.med.service.notification;

import com.example.med.model.notification.DestinataireType;
import com.example.med.model.utilisateur.Client;
import com.example.med.model.utilisateur.Societe;
import com.example.med.model.utilisateur.Utilisateur;
import com.example.med.repository.UtilisateurRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * PATTERN OBSERVER - ConcreteObserver
 *
 * Observer qui envoie les notifications par email.
 * Priorite basse (100) - execute apres la base de donnees et WebSocket.
 */
@Component
public class EmailNotificationObserver implements NotificationObserver {

    private final JavaMailSender mailSender;
    private final UtilisateurRepository utilisateurRepository;

    @Value("${app.mail.from:tiomelajorel@gmail.com}")
    private String fromEmail;

    @Value("${app.mail.from-name:MED Automobiles}")
    private String fromName;

    @Autowired
    public EmailNotificationObserver(JavaMailSender mailSender, UtilisateurRepository utilisateurRepository) {
        this.mailSender = mailSender;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    @Async
    public void onNotification(NotificationEvent event) {
        // Ne pas envoyer d'email aux admins (ils ont deja la notif in-app)
        if (event.getDestinataireType() == DestinataireType.ADMIN) {
            System.out.println("[EmailObserver] Notification admin - pas d'email envoye");
            return;
        }

        // Recuperer l'email du destinataire
        if (event.getDestinataireId() == null) {
            System.out.println("[EmailObserver] Pas de destinataire specifie");
            return;
        }

        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(event.getDestinataireId());
        if (utilisateurOpt.isEmpty()) {
            System.out.println("[EmailObserver] Utilisateur non trouve: " + event.getDestinataireId());
            return;
        }

        Utilisateur utilisateur = utilisateurOpt.get();
        String email = getEmail(utilisateur);
        String nom = getNom(utilisateur);

        if (email == null || email.isBlank()) {
            System.out.println("[EmailObserver] Email non disponible pour l'utilisateur: " + event.getDestinataireId());
            return;
        }

        // Envoyer l'email
        try {
            sendEmail(email, nom, event);
            System.out.println("[EmailObserver] Email envoye a " + email + ": " + event.getTitre());
        } catch (Exception e) {
            System.err.println("[EmailObserver] Erreur envoi email a " + email + ": " + e.getMessage());
        }
    }

    private void sendEmail(String toEmail, String toName, NotificationEvent event) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail, fromName);
        helper.setTo(toEmail);
        helper.setSubject(event.getTitre());

        String htmlContent = buildEmailHtml(toName, event);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String buildEmailHtml(String nom, NotificationEvent event) {
        String lien = event.getLien() != null ? event.getLien() : "#";
        String typeLabel = event.getType() != null ? event.getType().getLabel() : "Notification";

        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: #1a56db; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }
                    .content { background: #f9fafb; padding: 30px; border: 1px solid #e5e7eb; }
                    .footer { background: #f3f4f6; padding: 15px; text-align: center; font-size: 12px; color: #6b7280; border-radius: 0 0 8px 8px; }
                    .badge { display: inline-block; background: #dbeafe; color: #1e40af; padding: 4px 12px; border-radius: 20px; font-size: 12px; margin-bottom: 15px; }
                    .btn { display: inline-block; background: #1a56db; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px; margin-top: 20px; }
                    .btn:hover { background: #1e40af; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1 style="margin: 0;">MED Automobiles</h1>
                    </div>
                    <div class="content">
                        <span class="badge">%s</span>
                        <h2 style="margin-top: 0;">%s</h2>
                        <p>Bonjour %s,</p>
                        <p>%s</p>
                        <a href="%s" class="btn">Voir les details</a>
                    </div>
                    <div class="footer">
                        <p>Cet email a ete envoye par MED Automobiles.</p>
                        <p>Ne repondez pas a cet email.</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(typeLabel, event.getTitre(), nom, event.getMessage(), lien);
    }

    private String getEmail(Utilisateur utilisateur) {
        if (utilisateur instanceof Client client) {
            return client.getEmail();
        } else if (utilisateur instanceof Societe societe) {
            return societe.getEmail();
        }
        return null;
    }

    private String getNom(Utilisateur utilisateur) {
        if (utilisateur instanceof Client client) {
            String prenom = client.getPrenom() != null ? client.getPrenom() + " " : "";
            return prenom + client.getNom();
        } else if (utilisateur instanceof Societe societe) {
            return societe.getNom();
        }
        return utilisateur.getNom();
    }

    @Override
    public int getPriority() {
        return 100; // Priorite basse - execute en dernier
    }
}
