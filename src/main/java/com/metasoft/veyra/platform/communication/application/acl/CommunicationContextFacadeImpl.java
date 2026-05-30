package com.metasoft.veyra.platform.communication.application.acl;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.communication.domain.services.EmailNotificationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.PushNotificationCommandService;
import com.metasoft.veyra.platform.communication.interfaces.acl.CommunicationContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommunicationContextFacadeImpl implements CommunicationContextFacade {

    private final EmailNotificationCommandService emailNotificationCommandService;
    private final PushNotificationCommandService pushNotificationCommandService;

    public CommunicationContextFacadeImpl(
            EmailNotificationCommandService emailNotificationCommandService,
            PushNotificationCommandService pushNotificationCommandService
    ) {
        this.emailNotificationCommandService = emailNotificationCommandService;
        this.pushNotificationCommandService = pushNotificationCommandService;
    }

    @Override
    public void sendPlainEmail(List<String> recipients, String subject, String plainContent) {
        emailNotificationCommandService.handle(
                new SendPlainEmailCommand(new EmailRecipients(recipients), subject, plainContent)
        );
    }

    @Override
    public void sendHtmlEmail(List<String> recipients, String subject, String htmlContent, String plainContent) {
        emailNotificationCommandService.handle(
                new SendHtmlEmailCommand(new EmailRecipients(recipients), subject, htmlContent, plainContent)
        );
    }

    @Override
    public void sendTemplateEmail(List<String> recipients, String templateId, Map<String, Object> dynamicTemplateData) {
        emailNotificationCommandService.handle(
                new SendTemplateEmailCommand(new EmailRecipients(recipients), templateId, dynamicTemplateData)
        );
    }

    @Override
    public void sendPushNotification(String deviceToken, String title, String body) {
        pushNotificationCommandService.handle(new SendPushNotificationCommand(deviceToken, title, body));
    }
}
