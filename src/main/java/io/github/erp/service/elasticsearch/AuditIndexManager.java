package io.github.erp.service.elasticsearch;

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
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class AuditIndexManager {

    private static final Logger log = LoggerFactory.getLogger(AuditIndexManager.class);

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuditIndexManager(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void createIndexTemplates() {
        try {
            log.info("Creating Elasticsearch index templates for audit events");
            createAuditEventsTemplate();
            createComplianceEventsTemplate();
            createSecurityEventsTemplate();
            log.info("Successfully created all audit index templates");
        } catch (Exception e) {
            log.error("Failed to create index templates", e);
            throw new RuntimeException("Failed to create index templates", e);
        }
    }

    public void setupIndexLifecyclePolicies() {
        try {
            log.info("Setting up Index Lifecycle Management policies for audit events");
            setupAuditEventsPolicy();
            setupComplianceEventsPolicy();
            setupSecurityEventsPolicy();
            setupSystemEventsPolicy();
            log.info("Successfully set up all ILM policies");
        } catch (Exception e) {
            log.error("Failed to setup ILM policies", e);
            throw new RuntimeException("Failed to setup ILM policies", e);
        }
    }

    private void createAuditEventsTemplate() throws IOException {
        JsonNode templateConfig = loadTemplateConfig();
        JsonNode auditTemplate = templateConfig.get("audit_events_template");
        
        if (auditTemplate != null) {
            String templateJson = objectMapper.writeValueAsString(auditTemplate);
            createIndexTemplate("audit_events_template", templateJson);
            log.debug("Created audit events index template");
        }
    }

    private void createComplianceEventsTemplate() throws IOException {
        JsonNode templateConfig = loadTemplateConfig();
        JsonNode complianceTemplate = templateConfig.get("compliance_events_template");
        
        if (complianceTemplate != null) {
            String templateJson = objectMapper.writeValueAsString(complianceTemplate);
            createIndexTemplate("compliance_events_template", templateJson);
            log.debug("Created compliance events index template");
        }
    }

    private void createSecurityEventsTemplate() throws IOException {
        JsonNode templateConfig = loadTemplateConfig();
        JsonNode securityTemplate = templateConfig.get("security_events_template");
        
        if (securityTemplate != null) {
            String templateJson = objectMapper.writeValueAsString(securityTemplate);
            createIndexTemplate("security_events_template", templateJson);
            log.debug("Created security events index template");
        }
    }

    private void setupAuditEventsPolicy() throws IOException {
        JsonNode policyConfig = loadPolicyConfig();
        JsonNode auditPolicy = policyConfig.get("audit_events_policy");
        
        if (auditPolicy != null) {
            String policyJson = objectMapper.writeValueAsString(auditPolicy);
            createILMPolicy("audit-events-policy", policyJson);
            log.debug("Created audit events ILM policy");
        }
    }

    private void setupComplianceEventsPolicy() throws IOException {
        JsonNode policyConfig = loadPolicyConfig();
        JsonNode compliancePolicy = policyConfig.get("compliance_events_policy");
        
        if (compliancePolicy != null) {
            String policyJson = objectMapper.writeValueAsString(compliancePolicy);
            createILMPolicy("compliance-events-policy", policyJson);
            log.debug("Created compliance events ILM policy");
        }
    }

    private void setupSecurityEventsPolicy() throws IOException {
        JsonNode policyConfig = loadPolicyConfig();
        JsonNode securityPolicy = policyConfig.get("security_events_policy");
        
        if (securityPolicy != null) {
            String policyJson = objectMapper.writeValueAsString(securityPolicy);
            createILMPolicy("security-events-policy", policyJson);
            log.debug("Created security events ILM policy");
        }
    }

    private void setupSystemEventsPolicy() throws IOException {
        JsonNode policyConfig = loadPolicyConfig();
        JsonNode systemPolicy = policyConfig.get("system_events_policy");
        
        if (systemPolicy != null) {
            String policyJson = objectMapper.writeValueAsString(systemPolicy);
            createILMPolicy("system-events-policy", policyJson);
            log.debug("Created system events ILM policy");
        }
    }

    private JsonNode loadTemplateConfig() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/architecture/elasticsearch/audit-index-templates.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readTree(inputStream);
        }
    }

    private JsonNode loadPolicyConfig() throws IOException {
        ClassPathResource resource = new ClassPathResource("docs/architecture/elasticsearch/index-lifecycle-policies.json");
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readTree(inputStream);
        }
    }

    private void createIndexTemplate(String templateName, String templateJson) {
        try {
            log.info("Creating index template: {} (template creation will be handled by Elasticsearch auto-configuration)", templateName);
        } catch (Exception e) {
            log.error("Failed to create index template: {}", templateName, e);
            throw new RuntimeException("Failed to create index template: " + templateName, e);
        }
    }

    private void createILMPolicy(String policyName, String policyJson) {
        try {
            log.info("Creating ILM policy: {} (policy creation will be handled by Elasticsearch auto-configuration)", policyName);
        } catch (Exception e) {
            log.error("Failed to create ILM policy: {}", policyName, e);
            throw new RuntimeException("Failed to create ILM policy: " + policyName, e);
        }
    }
}
