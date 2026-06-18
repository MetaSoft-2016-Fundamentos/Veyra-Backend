package com.metasoft.veyra.platform.communication.interfaces.acl;

import com.metasoft.veyra.platform.communication.domain.model.valueobjects.PushPlatform;
import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailTemplate;

import java.util.List;
import java.util.Map;

public interface CommunicationContextFacade {
    void sendPlainEmail(List<String> recipients, String subject, String plainContent);

    void sendHtmlEmail(List<String> recipients, String subject, String htmlContent, String plainContent);

    void sendTemplateEmail(List<String> recipients, String templateId, Map<String, Object> dynamicTemplateData);

    void sendRenderedTemplateEmail(List<String> recipients, String subject, EmailTemplate template, Map<String, String> variables);

    void sendPushNotification(String deviceToken, String title, String body);

    void registerUserPushToken(Long userId, String token, PushPlatform platform);

    void unregisterUserPushToken(Long userId, String token);

    Long sendPushNotificationToUser(Long userId, String title, String body);

    void markNotificationAsRead(Long userId, Long notificationId);

    /**
     * Creates a DIRECT conversation between two users, or returns the existing one
     * if it already exists (idempotent).
     *
     * @param userIdA the first participant's user ID
     * @param userIdB the second participant's user ID
     * @return the conversation ID (new or existing)
     */
    Long getOrCreateDirectConversation(Long userIdA, Long userIdB);
}
