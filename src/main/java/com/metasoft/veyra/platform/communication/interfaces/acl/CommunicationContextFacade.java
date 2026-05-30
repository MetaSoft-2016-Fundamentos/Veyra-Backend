package com.metasoft.veyra.platform.communication.interfaces.acl;

import java.util.List;
import java.util.Map;

public interface CommunicationContextFacade {
    void sendPlainEmail(List<String> recipients, String subject, String plainContent);

    void sendHtmlEmail(List<String> recipients, String subject, String htmlContent, String plainContent);

    void sendTemplateEmail(List<String> recipients, String templateId, Map<String, Object> dynamicTemplateData);

    void sendPushNotification(String deviceToken, String title, String body);
}
