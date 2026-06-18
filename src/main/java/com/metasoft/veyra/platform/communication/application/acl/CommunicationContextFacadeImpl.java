package com.metasoft.veyra.platform.communication.application.acl;

import com.metasoft.veyra.platform.communication.domain.model.commands.MarkNotificationAsReadCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.RegisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPushNotificationToUserCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendRenderedTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.UnregisterUserPushTokenCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.EmailRecipients;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;
import com.metasoft.veyra.platform.communication.domain.model.commands.CreateConversationCommand;
import com.metasoft.veyra.platform.communication.domain.model.valueobjects.ConversationType;
import com.metasoft.veyra.platform.communication.domain.services.ConversationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.EmailNotificationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.PushNotificationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.UserNotificationCommandService;
import com.metasoft.veyra.platform.communication.domain.services.UserPushTokenCommandService;
import com.metasoft.veyra.platform.communication.interfaces.acl.CommunicationContextFacade;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailTemplate;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CommunicationContextFacadeImpl implements CommunicationContextFacade {

    private final EmailNotificationCommandService emailNotificationCommandService;
    private final PushNotificationCommandService pushNotificationCommandService;
    private final UserPushTokenCommandService userPushTokenCommandService;
    private final UserNotificationCommandService userNotificationCommandService;
    private final ConversationCommandService conversationCommandService;

    public CommunicationContextFacadeImpl(
            EmailNotificationCommandService emailNotificationCommandService,
            PushNotificationCommandService pushNotificationCommandService,
            UserPushTokenCommandService userPushTokenCommandService,
            UserNotificationCommandService userNotificationCommandService,
            ConversationCommandService conversationCommandService
    ) {
        this.emailNotificationCommandService = emailNotificationCommandService;
        this.pushNotificationCommandService = pushNotificationCommandService;
        this.userPushTokenCommandService = userPushTokenCommandService;
        this.userNotificationCommandService = userNotificationCommandService;
        this.conversationCommandService = conversationCommandService;
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

    @Override
    public void registerUserPushToken(Long userId, String token, PushPlatform platform) {
        userPushTokenCommandService.handle(new RegisterUserPushTokenCommand(userId, token, platform));
    }

    @Override
    public void unregisterUserPushToken(Long userId, String token) {
        userPushTokenCommandService.handle(new UnregisterUserPushTokenCommand(userId, token));
    }

    @Override
    public Long sendPushNotificationToUser(Long userId, String title, String body) {
        return pushNotificationCommandService.handle(new SendPushNotificationToUserCommand(userId, title, body))
                .notificationId();
    }

    @Override
    public void markNotificationAsRead(Long userId, Long notificationId) {
        userNotificationCommandService.handle(new MarkNotificationAsReadCommand(userId, notificationId));
    }

    @Override
    public Long getOrCreateDirectConversation(Long userIdA, Long userIdB) {
        var result = conversationCommandService.handle(
                new CreateConversationCommand(List.of(userIdA, userIdB), ConversationType.DIRECT, null));
        return result.conversationId();
    }

    @Override
    public void sendRenderedTemplateEmail(List<String> recipients, String subject, EmailTemplate template, Map<String, String> variables) {
        emailNotificationCommandService.handle(
                new SendRenderedTemplateEmailCommand(new EmailRecipients(recipients), subject, template, variables)
        );
    }
}
