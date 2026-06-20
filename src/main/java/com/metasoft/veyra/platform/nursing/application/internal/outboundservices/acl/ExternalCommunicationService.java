package com.metasoft.veyra.platform.nursing.application.internal.outboundservices.acl;
import com.metasoft.veyra.platform.communication.interfaces.acl.CommunicationContextFacade;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
@Service("ExternalCommunicationServiceNursing")
public class ExternalCommunicationService {
    private final CommunicationContextFacade communicationContextFacade;
    @Value("${veyra.frontend.url}")
    private String frontendUrl;

    public ExternalCommunicationService(CommunicationContextFacade communicationContextFacade) {
        this.communicationContextFacade = communicationContextFacade;
    }

    public void sendRelativeActivationEmail(String email, String firstName, String activationToken) {
        var activationLink = frontendUrl + "/iam/set-password?token=" + activationToken;
        communicationContextFacade.sendRenderedTemplateEmail(
                List.of(email),
                "Activa tu cuenta en Veyra",
                EmailTemplate.SET_PASSWORD,
                Map.of(
                        "firstName", firstName,
                        "actionUrl", activationLink,
                        "expiresIn", "24 horas",
                        "year", String.valueOf(java.time.Year.now().getValue()),
                        "recipientEmail", email
                )
        );
    }
}