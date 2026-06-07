package com.metasoft.veyra.platform.shared.infrastructure.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP-over-WebSocket configuration.
 * <p>
 *     Clients connect to {@code ws://host/ws?token=eyJ...} and subscribe to
 *     {@code /topic/conversations/{conversationId}} to receive real-time messages.
 *     The JWT is validated during the HTTP→WS upgrade by {@link JwtHandshakeInterceptor}.
 * </p>
 * <p>
 *     Subscription destinations: {@code /topic/**} (broadcast, server → all subscribers)<br>
 *     Application destinations: {@code /app/**} (client → server, not used — clients send via REST)
 * </p>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final JwtHandshakeInterceptor jwtHandshakeInterceptor;

    public WebSocketConfiguration(JwtHandshakeInterceptor jwtHandshakeInterceptor) {
        this.jwtHandshakeInterceptor = jwtHandshakeInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .addInterceptors(jwtHandshakeInterceptor)
                .withSockJS();          // SockJS fallback for clients that don't support native WebSocket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // In-memory broker handles /topic/** subscriptions
        registry.enableSimpleBroker("/topic");
        // Prefix for messages sent from clients via STOMP (not currently used — REST is the send path)
        registry.setApplicationDestinationPrefixes("/app");
    }
}
