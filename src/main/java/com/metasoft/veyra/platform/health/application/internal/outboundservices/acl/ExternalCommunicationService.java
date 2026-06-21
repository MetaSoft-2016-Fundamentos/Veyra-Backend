package com.metasoft.veyra.platform.health.application.internal.outboundservices.acl;

import com.metasoft.veyra.platform.communication.interfaces.acl.CommunicationContextFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("ExternalCommunicationServiceHealth")
public class ExternalCommunicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalCommunicationService.class);

    private final CommunicationContextFacade communicationContextFacade;
    private final ExternalNursingService externalNursingService;
    private final ExternalHcmService externalHcmService;

    public ExternalCommunicationService(
            CommunicationContextFacade communicationContextFacade,
            ExternalNursingService externalNursingService,
            ExternalHcmService externalHcmService) {
        this.communicationContextFacade = communicationContextFacade;
        this.externalNursingService = externalNursingService;
        this.externalHcmService = externalHcmService;
    }

    public void sendAnomalyAlert(Long residentId, String severity, String details) {
        var staffMemberIdOpt = externalNursingService.fetchStaffMemberIdByResidentId(residentId);
        if (staffMemberIdOpt.isEmpty()) {
            LOGGER.warn("No responsible staff assigned for resident {} - skipping push notification", residentId);
            return;
        }

        var userIdOpt = externalHcmService.fetchUserIdByStaffId(staffMemberIdOpt.get());
        if (userIdOpt.isEmpty()) {
            LOGGER.warn("No user linked to staff {} for resident {} - skipping push notification",
                    staffMemberIdOpt.get(), residentId);
            return;
        }

        var title = "Alerta de signos vitales [" + severity + "]";
        communicationContextFacade.sendPushNotificationToUser(userIdOpt.get(), title, details);
        LOGGER.info("Push notification sent to userId {} for resident {} with severity {}",
                userIdOpt.get(), residentId, severity);
    }
}
