package com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid;

import com.metasoft.veyra.platform.communication.domain.model.commands.SendHtmlEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendPlainEmailCommand;
import com.metasoft.veyra.platform.communication.domain.model.commands.SendTemplateEmailCommand;
import com.metasoft.veyra.platform.communication.infrastructure.providers.sendgrid.configuration.SendGridSettings;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.util.List;

public final class SendGridMailBuilder {

    private SendGridMailBuilder() {
    }

    public static Mail buildPlainMail(SendGridSettings settings, SendPlainEmailCommand command) {
        Mail mail = createBaseMail(settings, command.recipients().values());
        mail.setSubject(command.subject());
        mail.addContent(new Content("text/plain", command.plainContent()));
        return mail;
    }

    public static Mail buildHtmlMail(SendGridSettings settings, SendHtmlEmailCommand command) {
        Mail mail = createBaseMail(settings, command.recipients().values());
        mail.setSubject(command.subject());
        if (command.plainContent() != null && !command.plainContent().isBlank()) {
            mail.addContent(new Content("text/plain", command.plainContent()));
        }
        mail.addContent(new Content("text/html", command.htmlContent()));
        return mail;
    }

    public static Mail buildTemplateMail(SendGridSettings settings, SendTemplateEmailCommand command) {
        Mail mail = createBaseMail(settings, command.recipients().values());
        mail.setTemplateId(command.templateId());
        Personalization personalization = mail.getPersonalization().getFirst();
        command.dynamicTemplateData().forEach(personalization::addDynamicTemplateData);
        return mail;
    }

    private static Mail createBaseMail(SendGridSettings settings, List<String> recipients) {
        Mail mail = new Mail();
        mail.setFrom(new Email(settings.fromEmail(), settings.fromName()));
        Personalization personalization = new Personalization();
        recipients.forEach(recipient -> personalization.addTo(new Email(recipient)));
        mail.addPersonalization(personalization);
        return mail;
    }
}
