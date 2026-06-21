package iam.integration;

import com.metasoft.veyra.platform.iam.infrastructure.tokens.jwt.services.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    private static final String TEST_SECRET = "veyra-test-secret-key-super-segura-2024!!";
    private static final int    TEST_EXPIRATION_DAYS = 1;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl();
        ReflectionTestUtils.setField(tokenService, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(tokenService, "expirationDays", TEST_EXPIRATION_DAYS);
    }

    @Test
    void shouldGenerateValidJwtTokenForAuthenticatedUser() {
        // Arrange
        String username = "maria.test";

        // Act
        String token = tokenService.generateToken(username);

        // Assert
        assertNotNull(token, "The JWT token should not be null");
        assertFalse(token.isBlank(), "The JWT token should not be blank");
        assertTrue(tokenService.validateToken(token),
                "The generated token must pass signature and expiration validation");
        assertEquals(username, tokenService.getUsernameFromToken(token),
                "The username extracted from the token must match the original one");
    }

    @Test
    void shouldRejectInvalidOrTamperedToken() {
        // Arrange
        String tamperedToken = "eyJhbGciOiJIUzI1NiJ9" +
                ".eyJzdWIiOiJoYWNrZXIifQ" +
                ".invalid_signature_here";

        // Act
        boolean isValid = tokenService.validateToken(tamperedToken);

        // Assert
        assertFalse(isValid, "A token with an invalid signature must be rejected");
    }

    @Test
    void shouldRejectBlankToken() {
        assertFalse(tokenService.validateToken(""), "A blank token must be rejected");
    }

    @Test
    void shouldExtractCorrectUsernameFromToken() {
        // Arrange
        String username = "juan.operador";
        String token = tokenService.generateToken(username);

        // Act
        String extracted = tokenService.getUsernameFromToken(token);

        // Assert
        assertEquals(username, extracted, "Extracted username must match the initial username");
    }
}