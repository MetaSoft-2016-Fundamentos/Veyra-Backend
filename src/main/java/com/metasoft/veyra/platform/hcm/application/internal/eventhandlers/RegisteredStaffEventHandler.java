package com.metasoft.veyra.platform.hcm.application.internal.eventhandlers;

import com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl.ExternalCommunicationService;
import com.metasoft.veyra.platform.hcm.application.internal.outboundservices.acl.ExternalIamService;
import com.metasoft.veyra.platform.hcm.domain.model.events.RegisteredStaffEvent;
import com.metasoft.veyra.platform.hcm.infrastructure.persistence.jpa.repositories.StaffRepository;
import com.metasoft.veyra.platform.hcm.domain.model.valueobjects.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisteredStaffEventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisteredStaffEventHandler.class);

    private final ExternalIamService externalIamService;
    private final ExternalCommunicationService externalCommunicationService;
    private final StaffRepository staffRepository;

    public RegisteredStaffEventHandler(
            ExternalIamService externalIamService,
            ExternalCommunicationService externalCommunicationService,
            StaffRepository staffRepository) {
        this.externalIamService = externalIamService;
        this.externalCommunicationService = externalCommunicationService;
        this.staffRepository = staffRepository;
    }

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void on(RegisteredStaffEvent event) {
        LOGGER.info("Handling RegisteredStaffEvent for email: {}", event.getEmail());

        // 1. Crear la cuenta en IAM y obtener el token de activación
        var activationToken = externalIamService.createStaffAccount(event.getEmail());

        // 2. Obtener el UserId de IAM y asignarlo al Staff
        var iamUserId = externalIamService.fetchUserByUsername(event.getEmail());
        if (iamUserId != null) {
            staffRepository.findById(event.getStaffId()).ifPresent(staff -> {
                staff.setUserId(iamUserId);
                staffRepository.save(staff);
            });
        }

        // 3. Enviar el email de activación
        externalCommunicationService.sendStaffActivationEmail(event.getEmail(), event.getFirstName() + " " + event.getLastName(), activationToken);

        LOGGER.info("Staff account created and activation email sent to: {}", event.getEmail());
    }
}
