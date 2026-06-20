package com.metasoft.veyra.platform.nursing.application.internal.eventhandlers;

import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalCommunicationService;
import com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.nursing.domain.model.commands.AssignUserToRelativeCommand;
import com.metasoft.veyra.platform.nursing.domain.model.events.RegisteredRelativeEvent;
import com.metasoft.veyra.platform.nursing.domain.services.RelativeCommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisteredRelativeEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisteredRelativeEventHandler.class);

    private final ExternalIamService externalIamService;
    private final ExternalCommunicationService externalCommunicationService;
    private final RelativeCommandService relativeCommandService;

    public RegisteredRelativeEventHandler(
            ExternalIamService externalIamService,
            ExternalCommunicationService externalCommunicationService,
            RelativeCommandService relativeCommandService) {
        this.externalIamService = externalIamService;
        this.externalCommunicationService = externalCommunicationService;
        this.relativeCommandService = relativeCommandService;
    }

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(RegisteredRelativeEvent event) {
        LOGGER.info("Handling RegisteredRelativeEvent for email: {}", event.getEmail());

        var activationToken = externalIamService.createRelativeAccount(event.getEmail());

        var userId = externalIamService.fetchUserByUsername(event.getEmail());
        relativeCommandService.handle(new AssignUserToRelativeCommand(event.getEmail(), userId.userId()));

        externalCommunicationService.sendRelativeActivationEmail(event.getEmail(),event.getFirstName()
                , activationToken);

        LOGGER.info("Relative account created and activation email sent to: {}", event.getEmail());
    }
}