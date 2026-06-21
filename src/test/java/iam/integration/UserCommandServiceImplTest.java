package iam.integration;

import com.metasoft.veyra.platform.iam.application.internal.commandservices.UserCommandServiceImpl;
import com.metasoft.veyra.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.metasoft.veyra.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.metasoft.veyra.platform.iam.domain.model.aggregates.User;
import com.metasoft.veyra.platform.iam.domain.model.commands.SignInCommand;
import com.metasoft.veyra.platform.iam.domain.model.commands.SignUpCommand;
import com.metasoft.veyra.platform.iam.domain.model.entities.Role;
import com.metasoft.veyra.platform.iam.domain.model.valueobjects.Roles;
import com.metasoft.veyra.platform.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.metasoft.veyra.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserCommandServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private HashingService hashingService;
    @Mock private TokenService tokenService;
    @Mock private RoleRepository roleRepository;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    private Role defaultRole;
    private User storedUser;

    @BeforeEach
    void setUp() {
        defaultRole = new Role(Roles.ROLE_USER);
        storedUser  = new User("maria.test", "$2a$hashed_password", List.of(defaultRole));
    }

    // ── SIGN-UP ────────────────────────────────────────────────────────────────

    @Test
    void shouldRegisterNewUserSuccessfully() {
        // Arrange
        SignUpCommand command = new SignUpCommand("maria.test", "rawPass", List.of(defaultRole));

        when(userRepository.existsByUsername("maria.test")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_USER)).thenReturn(Optional.of(defaultRole));
        when(hashingService.encode("rawPass")).thenReturn("$2a$hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(storedUser);
        when(userRepository.findByUsername("maria.test")).thenReturn(Optional.of(storedUser));

        // Act
        Optional<User> result = userCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent(), "Should return the newly created user");
        assertEquals("maria.test", result.get().getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Arrange
        SignUpCommand command = new SignUpCommand("maria.test", "anyPass", List.of(defaultRole));
        when(userRepository.existsByUsername("maria.test")).thenReturn(true);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userCommandService.handle(command));

        assertEquals("Username already exists", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    // ── SIGN-IN / JWT ──────────────────────────────────────────────────────────

    @Test
    void shouldReturnJwtTokenWhenCredentialsAreValid() {
        // Arrange
        SignInCommand command = new SignInCommand("maria.test", "rawPass");

        when(userRepository.findByUsername("maria.test")).thenReturn(Optional.of(storedUser));
        when(hashingService.matches("rawPass", storedUser.getPassword())).thenReturn(true);
        when(tokenService.generateToken("maria.test")).thenReturn("eyJhbGciOiJIUzI1NiJ9.mockToken");

        // Act
        var result = userCommandService.handle(command);

        // Assert
        assertTrue(result.isPresent(), "Should return a pair containing user and token");
        assertEquals("eyJhbGciOiJIUzI1NiJ9.mockToken", result.get().getRight(),
                "The generated JWT must match the service token");
        assertSame(storedUser, result.get().getLeft(),
                "Should return the same found User object");
        verify(tokenService).generateToken("maria.test");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {
        // Arrange
        SignInCommand command = new SignInCommand("maria.test", "wrongPass");

        when(userRepository.findByUsername("maria.test")).thenReturn(Optional.of(storedUser));
        when(hashingService.matches("wrongPass", storedUser.getPassword())).thenReturn(false);

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userCommandService.handle(command));

        assertEquals("Invalid password", ex.getMessage());
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        SignInCommand command = new SignInCommand("unknown.user", "anyPass");
        when(userRepository.findByUsername("unknown.user")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userCommandService.handle(command));

        assertEquals("User not found", ex.getMessage());
        verify(tokenService, never()).generateToken(any());
    }
}