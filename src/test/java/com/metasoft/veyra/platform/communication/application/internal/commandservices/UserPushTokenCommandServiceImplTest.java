package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserNotFoundForPushException;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserPushTokenNotFoundException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import com.metasoft.veyra.platform.communication.domain.model.commands.RegisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.UnregisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserPushTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPushTokenCommandServiceImplTest {

    @Mock
    private UserPushTokenRepository userPushTokenRepository;

    @Mock
    private ExternalIamService externalIamService;

    @InjectMocks
    private UserPushTokenCommandServiceImpl userPushTokenCommandService;

    @Test
    void shouldRegisterNewTokenWhenItDoesNotExist() {
        RegisterUserPushTokenCommand command = new RegisterUserPushTokenCommand(1L, "token-abc", PushPlatform.ANDROID);
        when(userPushTokenRepository.findByToken("token-abc")).thenReturn(Optional.empty());
        when(userPushTokenRepository.save(any(UserPushToken.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userPushTokenCommandService.handle(command);

        verify(externalIamService).ensureUserExists(1L);
        ArgumentCaptor<UserPushToken> captor = ArgumentCaptor.forClass(UserPushToken.class);
        verify(userPushTokenRepository).save(captor.capture());
        assertEquals(1L, captor.getValue().getUserId());
        assertEquals("token-abc", captor.getValue().getToken());
        assertEquals(PushPlatform.ANDROID, captor.getValue().getPlatform());
    }

    @Test
    void shouldRefreshTokenWhenItAlreadyBelongsToUser() {
        RegisterUserPushTokenCommand command = new RegisterUserPushTokenCommand(1L, "token-abc", PushPlatform.WEB);
        UserPushToken existing = new UserPushToken(1L, "token-abc", PushPlatform.ANDROID);
        when(userPushTokenRepository.findByToken("token-abc")).thenReturn(Optional.of(existing));
        when(userPushTokenRepository.save(existing)).thenReturn(existing);

        userPushTokenCommandService.handle(command);

        assertEquals(PushPlatform.WEB, existing.getPlatform());
        verify(userPushTokenRepository).save(existing);
    }

    @Test
    void shouldReassignTokenWhenItBelongsToAnotherUser() {
        RegisterUserPushTokenCommand command = new RegisterUserPushTokenCommand(2L, "token-abc", PushPlatform.IOS);
        UserPushToken existing = new UserPushToken(1L, "token-abc", PushPlatform.ANDROID);
        when(userPushTokenRepository.findByToken("token-abc")).thenReturn(Optional.of(existing));
        when(userPushTokenRepository.save(existing)).thenReturn(existing);

        userPushTokenCommandService.handle(command);

        assertEquals(2L, existing.getUserId());
        assertEquals(PushPlatform.IOS, existing.getPlatform());
    }

    @Test
    void shouldThrowWhenUserDoesNotExistOnRegister() {
        RegisterUserPushTokenCommand command = new RegisterUserPushTokenCommand(99L, "token-abc", PushPlatform.WEB);
        doThrow(new UserNotFoundForPushException(99L)).when(externalIamService).ensureUserExists(99L);

        assertThrows(UserNotFoundForPushException.class, () -> userPushTokenCommandService.handle(command));
        verify(userPushTokenRepository, never()).save(any());
    }

    @Test
    void shouldUnregisterExistingToken() {
        UnregisterUserPushTokenCommand command = new UnregisterUserPushTokenCommand(1L, "token-abc");
        UserPushToken existing = new UserPushToken(1L, "token-abc", PushPlatform.ANDROID);
        when(userPushTokenRepository.findByUserIdAndToken(1L, "token-abc")).thenReturn(Optional.of(existing));

        userPushTokenCommandService.handle(command);

        verify(userPushTokenRepository).delete(existing);
    }

    @Test
    void shouldThrowWhenTokenNotFoundOnUnregister() {
        UnregisterUserPushTokenCommand command = new UnregisterUserPushTokenCommand(1L, "missing-token");
        when(userPushTokenRepository.findByUserIdAndToken(1L, "missing-token")).thenReturn(Optional.empty());

        assertThrows(UserPushTokenNotFoundException.class, () -> userPushTokenCommandService.handle(command));
    }
}
