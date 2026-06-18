package com.metasoft.veyra.platform.shared.infrastructure.templates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.metasoft.veyra.platform.shared.domain.model.valueobjects.EmailTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@Slf4j
public class EmailTemplateRenderer {

    private static final String TEMPLATE_BASE_PATH = "templates/email/";

    public String render(EmailTemplate template, Map<String, String> variables) {
        String templateName = template.name().toLowerCase().replace('_', '-') + ".html";
        String path = TEMPLATE_BASE_PATH + templateName;

        ClassPathResource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            throw new IllegalArgumentException("Email template not found: " + path);
        }

        try {
            String content = resource.getContentAsString(StandardCharsets.UTF_8);
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue() != null ? entry.getValue() : "");
            }
            return content;
        } catch (IOException exception) {
            log.error("Failed to load email template (template={})", templateName);
            throw new IllegalStateException("Failed to load email template: " + templateName, exception);
        }
    }
}
