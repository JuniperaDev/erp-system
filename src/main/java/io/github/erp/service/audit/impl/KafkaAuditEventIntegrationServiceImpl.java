package io.github.erp.service.audit.impl;

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

import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import io.github.erp.service.KafkaAuditEventPublisher;
import io.github.erp.service.audit.AuditEventRoutingService;
import io.github.erp.service.audit.KafkaAuditEventIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KafkaAuditEventIntegrationServiceImpl implements KafkaAuditEventIntegrationService {

    private static final Logger log = LoggerFactory.getLogger(KafkaAuditEventIntegrationServiceImpl.class);

    private final KafkaAuditEventPublisher kafkaAuditEventPublisher;
    private final AuditEventRoutingService auditEventRoutingService;

    public KafkaAuditEventIntegrationServiceImpl(
            KafkaAuditEventPublisher kafkaAuditEventPublisher,
            AuditEventRoutingService auditEventRoutingService) {
        this.kafkaAuditEventPublisher = kafkaAuditEventPublisher;
        this.auditEventRoutingService = auditEventRoutingService;
    }

    @Override
    public void publishAuditEventToKafka(DomainEvent event) {
        try {
            if (isAuditEvent(event)) {
                publishAuditTrailEventToKafka((AuditTrailEvent) event);
            } else if (isComplianceEvent(event)) {
                publishComplianceEventToKafka((ComplianceAuditEvent) event);
            } else {
                log.debug("Event {} is not an audit or compliance event, skipping Kafka publishing", event.getEventId());
            }
        } catch (Exception e) {
            log.error("Failed to publish event to Kafka: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAuditTrailEventToKafka(AuditTrailEvent event) {
        try {
            if (auditEventRoutingService.shouldRouteToMultipleTopics(event)) {
                String[] categories = auditEventRoutingService.getMultipleCategories(event);
                for (String category : categories) {
                    kafkaAuditEventPublisher.publishAuditEvent(event, category);
                }
            } else {
                String category = auditEventRoutingService.determineEventCategory(event);
                kafkaAuditEventPublisher.publishAuditEvent(event, category);
            }
            log.debug("Successfully published audit trail event to Kafka: {}", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to publish audit trail event to Kafka: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishComplianceEventToKafka(ComplianceAuditEvent event) {
        try {
            String category = auditEventRoutingService.determineEventCategory(event);
            kafkaAuditEventPublisher.publishComplianceAuditEvent(event, category);
            log.debug("Successfully published compliance event to Kafka: {}", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to publish compliance event to Kafka: {}", event.getEventId(), e);
        }
    }

    @Override
    public boolean isAuditEvent(DomainEvent event) {
        return event instanceof AuditTrailEvent;
    }

    @Override
    public boolean isComplianceEvent(DomainEvent event) {
        return event instanceof ComplianceAuditEvent;
    }
}
