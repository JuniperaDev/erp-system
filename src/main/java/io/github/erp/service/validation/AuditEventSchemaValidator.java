package io.github.erp.service.validation;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.github.erp.domain.events.audit.AuditTrailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

@Component
public class AuditEventSchemaValidator {

    private static final Logger log = LoggerFactory.getLogger(AuditEventSchemaValidator.class);
    private static final String SCHEMA_PATH = "schemas/audit-event-schema.json";

    private final ObjectMapper objectMapper;
    private JsonSchema auditEventSchema;

    public AuditEventSchemaValidator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void initializeSchema() {
        try {
            ClassPathResource schemaResource = new ClassPathResource(SCHEMA_PATH);
            if (!schemaResource.exists()) {
                log.warn("Audit event schema not found at classpath: {}", SCHEMA_PATH);
                return;
            }

            try (InputStream schemaStream = schemaResource.getInputStream()) {
                JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
                JsonNode schemaNode = objectMapper.readTree(schemaStream);
                this.auditEventSchema = factory.getSchema(schemaNode);
                log.info("Audit event schema loaded successfully from: {}", SCHEMA_PATH);
            }
        } catch (IOException e) {
            log.error("Failed to load audit event schema from: {}", SCHEMA_PATH, e);
        }
    }

    public ValidationResult validate(AuditTrailEvent event) {
        if (auditEventSchema == null) {
            log.warn("Audit event schema not initialized, skipping validation");
            return ValidationResult.valid();
        }

        try {
            JsonNode eventNode = objectMapper.valueToTree(event);
            Set<ValidationMessage> validationMessages = auditEventSchema.validate(eventNode);

            if (validationMessages.isEmpty()) {
                log.debug("Audit event validation passed for event: {}", event.getEventId());
                return ValidationResult.valid();
            } else {
                log.warn("Audit event validation failed for event: {} with {} errors", 
                    event.getEventId(), validationMessages.size());
                return ValidationResult.invalid(validationMessages);
            }
        } catch (Exception e) {
            log.error("Error during audit event validation for event: {}", event.getEventId(), e);
            return ValidationResult.error(e.getMessage());
        }
    }

    public ValidationResult validateJson(JsonNode eventJson) {
        if (auditEventSchema == null) {
            log.warn("Audit event schema not initialized, skipping validation");
            return ValidationResult.valid();
        }

        try {
            Set<ValidationMessage> validationMessages = auditEventSchema.validate(eventJson);

            if (validationMessages.isEmpty()) {
                log.debug("Audit event JSON validation passed");
                return ValidationResult.valid();
            } else {
                log.warn("Audit event JSON validation failed with {} errors", validationMessages.size());
                return ValidationResult.invalid(validationMessages);
            }
        } catch (Exception e) {
            log.error("Error during audit event JSON validation", e);
            return ValidationResult.error(e.getMessage());
        }
    }

    public boolean isSchemaLoaded() {
        return auditEventSchema != null;
    }
}
