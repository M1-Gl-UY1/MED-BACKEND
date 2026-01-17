package com.example.med.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuration WebSocket pour les notifications en temps reel
 *
 * Endpoints:
 * - /ws : endpoint de connexion WebSocket
 *
 * Topics:
 * - /topic/notifications/admin : notifications pour les admins
 * - /topic/notifications/user/{userId} : notifications pour un utilisateur specifique
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefixe pour les destinations de type "topic" (broadcast)
        config.enableSimpleBroker("/topic", "/queue");
        // Prefixe pour les messages envoyes par le client vers le serveur
        config.setApplicationDestinationPrefixes("/app");
        // Prefixe pour les messages destines a un utilisateur specifique
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint WebSocket avec SockJS fallback
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // Endpoint sans SockJS (pour les clients natifs WebSocket)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}
