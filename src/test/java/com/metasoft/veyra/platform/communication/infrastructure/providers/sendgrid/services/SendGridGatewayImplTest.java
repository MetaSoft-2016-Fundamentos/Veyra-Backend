package com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.services;

import com.metasoft.veyra.platform.communication.domain.exceptions.CommunicationProviderNotConfiguredException;
import com.metasoft.veyra.platform.communication.domain.exceptions.SendGridIntegrationException;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.configuration.SendGridSettings;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SendGridGatewayImplTest {

    @Mock
    private SendGrid sendGrid;

    private final SendGridSettings sendGridSettings = new SendGridSettings(
            "test-api-key",
            "noreply@veyra.com",
            "Veyra"
    );

    @Test
    void shouldSendPlainEmailWhenSendGridAcceptsRequest() throws IOException {
        SendGridGatewayImpl gateway = new SendGridGatewayImpl(sendGridSettings, sendGrid);
        when(sendGrid.api(any(Request.class))).thenReturn(successResponse(202));

        SendPlainEmailCommand command = new SendPlainEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "Welcome",
                "Hello from Veyra"
        );

        gateway.sendPlain(command);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(requestCaptor.capture());
        assertEquals("mail/send", requestCaptor.getValue().getEndpoint());
        assertTrue(requestCaptor.getValue().getBody().contains("Hello from Veyra"));
    }

    @Test
    void shouldSendHtmlEmailWhenSendGridAcceptsRequest() throws IOException {
        SendGridGatewayImpl gateway = new SendGridGatewayImpl(sendGridSettings, sendGrid);
        when(sendGrid.api(any(Request.class))).thenReturn(successResponse(202));

        SendHtmlEmailCommand command = new SendHtmlEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "Welcome",
                "<p>Hello from Veyra</p>",
                "Hello from Veyra"
        );

        gateway.sendHtml(command);

        verify(sendGrid).api(any(Request.class));
    }

    @Test
    void shouldSendTemplateEmailWhenSendGridAcceptsRequest() throws IOException {
        SendGridGatewayImpl gateway = new SendGridGatewayImpl(sendGridSettings, sendGrid);
        when(sendGrid.api(any(Request.class))).thenReturn(successResponse(202));

        SendTemplateEmailCommand command = new SendTemplateEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "d-template-id",
                Map.of("firstName", "Ana")
        );

        gateway.sendTemplate(command);

        ArgumentCaptor<Request> requestCaptor = ArgumentCaptor.forClass(Request.class);
        verify(sendGrid).api(requestCaptor.capture());
        assertTrue(requestCaptor.getValue().getBody().contains("d-template-id"));
    }

    @Test
    void shouldThrowWhenSendGridApiKeyIsMissing() {
        SendGridGatewayImpl gateway = new SendGridGatewayImpl(
                new SendGridSettings("", "noreply@veyra.com", "Veyra"),
                sendGrid
        );

        SendPlainEmailCommand command = new SendPlainEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "Welcome",
                "Hello from Veyra"
        );

        assertThrows(CommunicationProviderNotConfiguredException.class, () -> gateway.sendPlain(command));
    }

    @Test
    void shouldThrowWhenSendGridRejectsRequest() throws IOException {
        SendGridGatewayImpl gateway = new SendGridGatewayImpl(sendGridSettings, sendGrid);
        when(sendGrid.api(any(Request.class))).thenReturn(successResponse(400));

        SendPlainEmailCommand command = new SendPlainEmailCommand(
                new EmailRecipients(List.of("user@example.com")),
                "Welcome",
                "Hello from Veyra"
        );

        assertThrows(SendGridIntegrationException.class, () -> gateway.sendPlain(command));
    }

    private Response successResponse(int statusCode) {
        Response response = new Response();
        response.setStatusCode(statusCode);
        response.setBody("{}");
        return response;
    }
}
