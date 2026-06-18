package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.communication.domain.exceptions.UserPushTokenNotFoundException;
import com.metasoft.veyra.platform.communication.domain.model.aggregates.UserPushToken;
import com.metasoft.veyra.platform.communication.domain.model.commands.RegisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.UnregisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.services.UserPushTokenCommandService;
import com.metasoft.veyra.platform.communication.infrastructure.persistence.jpa.repositories.UserPushTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPushTokenCommandServiceImpl implements UserPushTokenCommandService {

    private final UserPushTokenRepository userPushTokenRepository;
    private final ExternalIamService externalIamService;

    public UserPushTokenCommandServiceImpl(
            UserPushTokenRepository userPushTokenRepository,
            ExternalIamService externalIamService
    ) {
        this.userPushTokenRepository = userPushTokenRepository;
        this.externalIamService = externalIamService;
    }

    @Override
    @Transactional
    public UserPushToken handle(RegisterUserPushTokenCommand command) {
        externalIamService.ensureUserExists(command.userId());

        return userPushTokenRepository.findByToken(command.token())
                .map(existing -> updateExistingToken(existing, command))
                .orElseGet(() -> userPushTokenRepository.save(
                        new UserPushToken(command.userId(), command.token(), command.platform())
                ));
    }

    @Override
    @Transactional
    public void handle(UnregisterUserPushTokenCommand command) {
        externalIamService.ensureUserExists(command.userId());

        var token = userPushTokenRepository.findByUserIdAndToken(command.userId(), command.token())
                .orElseThrow(() -> new UserPushTokenNotFoundException(command.userId(), command.token()));

        userPushTokenRepository.delete(token);
    }

    private UserPushToken updateExistingToken(UserPushToken existing, RegisterUserPushTokenCommand command) {
        if (existing.getUserId().equals(command.userId())) {
            existing.refresh(command.platform());
        } else {
            existing.reassignTo(command.userId(), command.platform());
        }
        return userPushTokenRepository.save(existing);
    }
}
