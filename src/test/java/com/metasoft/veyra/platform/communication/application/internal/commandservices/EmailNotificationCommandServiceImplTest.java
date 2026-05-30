package com.metasoft.veyra.platform.communication.application.internal.commandservices;

import com.metasoft.veyra.platform.communication.application.internal.outboundservices.sendgrid.SendGridGateway;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationCommandServiceImplTest {

    @Mock
    private SendGridGateway sendGridGateway;

    @InjectMocks
    private EmailNotificationCommandServiceImpl emailNotificationCommandService;

    @Test
    void shouldDelegatePlainEmailToGateway() {
        SendPlainEmailCommand command = new SendPlainEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "Welcome",
                "Hello from Veyra"
        );

        emailNotificationCommandService.handle(command);

        verify(sendGridGateway).sendPlain(command);
    }

    @Test
    void shouldDelegateHtmlEmailToGateway() {
        SendHtmlEmailCommand command = new SendHtmlEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "Welcome",
                "<p>Hello from Veyra</p>",
                "Hello from Veyra"
        );

        emailNotificationCommandService.handle(command);

        verify(sendGridGateway).sendHtml(command);
    }

    @Test
    void shouldDelegateTemplateEmailToGateway() {
        SendTemplateEmailCommand command = new SendTemplateEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "d-template-id",
                Map.of("firstName", "Ana")
        );

        emailNotificationCommandService.handle(command);

        verify(sendGridGateway).sendTemplate(command);
    }
}
