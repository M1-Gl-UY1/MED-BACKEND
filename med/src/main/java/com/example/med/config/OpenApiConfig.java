package com.example.med.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI/Swagger pour la documentation de l'API
 *
 * Documentation accessible à:
 * - Swagger UI: http://localhost:8085/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8085/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8085}")
    private String serverPort;

    @Bean
    public OpenAPI medOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MED Auto API")
                        .description("""
                                API REST pour la plateforme de vente de véhicules MED Auto.

                                ## Fonctionnalités principales

                                ### Catalogue
                                - **Véhicules**: CRUD complet, recherche full-text, filtres avancés
                                - **Options**: Gestion des options de véhicules
                                - **Images**: Upload et gestion des images

                                ### Commandes
                                - **Panier**: Gestion du panier d'achat
                                - **Commandes**: Création, validation, suivi
                                - **Documents**: Génération de factures et bons de commande

                                ### Utilisateurs
                                - **Clients**: Inscription, authentification, profil
                                - **Sociétés**: Comptes entreprise
                                - **Admins**: Administration

                                ### Notifications (WebSocket)
                                - Endpoint: `/ws`
                                - Topics: `/topic/notifications/admin`, `/topic/notifications/user/{id}`

                                ## Design Patterns implémentés
                                - Observer (notifications)
                                - Factory Method (commandes)
                                - Template Method (calcul TVA)
                                - Builder (documents PDF)
                                - Iterator (catalogue)
                                - Command (soldes)
                                - Decorator (promotions)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("MED Auto Support")
                                .email("contact@med-auto.cm")
                                .url("https://med-auto.cm"))
                        .license(new License()
                                .name("Propriétaire")
                                .url("https://med-auto.cm/licence")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Serveur de développement"),
                        new Server()
                                .url("https://med-backend.onrender.com")
                                .description("Serveur de production")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Token JWT obtenu via /api/clients/auth ou /api/societes/auth ou /api/admins/auth")));
    }
}
