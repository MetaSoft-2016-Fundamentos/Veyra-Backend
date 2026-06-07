package com.metasoft.veyra.platform.shared.infrastructure.websocket;

import com.metasoft.veyra.platform.iam.infrastructure.tokens.jwt.BearerTokenService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

/**
 * Validates JWT during the WebSocket handshake.
 * <p>
 *     Reads the token from the {@code token} query parameter
 *     (e.g. {@code ws://host/ws?token=eyJ...}).
 *     Rejects the handshake (returns false) when the token is missing or invalid,
 *     so unauthenticated clients cannot open a WebSocket connection.
 * </p>
 */
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final BearerTokenService bearerTokenService;

    public JwtHandshakeInterceptor(BearerTokenService bearerTokenService) {
        this.bearerTokenService = bearerTokenService;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {
        List<String> tokenParams = UriComponentsBuilder
                .fromUri(request.getURI())
                .build()
                .getQueryParams()
                .get("token");

        if (tokenParams == null || tokenParams.isEmpty()) {
            return false;
        }

        String token = tokenParams.get(0);

        // Strip "Bearer " prefix if the client sends it that way
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (!bearerTokenService.validateToken(token)) {
            return false;
        }

        // Store the validated username so controllers/handlers can use it
        attributes.put("username", bearerTokenService.getUsernameFromToken(token));
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        // No post-handshake action needed
    }
}
