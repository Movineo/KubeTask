package com.movine.taskmanagementsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory broker for the /topic and /queue prefixes
        config.enableSimpleBroker("/topic", "/queue");

        // Set the prefix for messages that are bound for @MessageMapping methods
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register a WebSocket endpoint that the clients will use to connect
        registry.addEndpoint("/ws")
                // Allow all origins for simplicity (in production, you'd want to restrict this)
                .setAllowedOrigins("*")
                // Enable SockJS fallback options for browsers that don't support WebSockets natively
                .withSockJS();
    }
}