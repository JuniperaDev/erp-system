package io.github.erp.config;

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

import io.github.erp.service.elasticsearch.AuditIndexManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@Configuration
@ConditionalOnProperty(name = "spring.elasticsearch.audit.enabled", havingValue = "true", matchIfMissing = true)
public class ElasticsearchAuditConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchAuditConfiguration.class);

    @Autowired
    private AuditIndexManager auditIndexManager;

    @Bean
    @ConditionalOnMissingBean
    public AuditIndexManager auditIndexManager(ElasticsearchRestTemplate template) {
        return new AuditIndexManager(template);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setupAuditIndices() {
        try {
            log.info("Setting up Elasticsearch audit indices and policies");
            auditIndexManager.createIndexTemplates();
            auditIndexManager.setupIndexLifecyclePolicies();
            log.info("Successfully set up Elasticsearch audit infrastructure");
        } catch (Exception e) {
            log.warn("Failed to setup audit indices - Elasticsearch may not be available: {}", e.getMessage());
        }
    }
}
