package io.github.erp.service.impl;

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

import io.github.erp.config.AuditKafkaConfiguration;
import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import io.github.erp.service.KafkaAuditEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class KafkaAuditEventPublisherImpl implements KafkaAuditEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(KafkaAuditEventPublisherImpl.class);

    private final KafkaTemplate<String, AuditTrailEvent> auditTrailEventKafkaTemplate;
    private final KafkaTemplate<String, ComplianceAuditEvent> complianceAuditEventKafkaTemplate;
    private final AuditKafkaConfiguration auditKafkaConfiguration;

    public KafkaAuditEventPublisherImpl(
            KafkaTemplate<String, AuditTrailEvent> auditTrailEventKafkaTemplate,
            KafkaTemplate<String, ComplianceAuditEvent> complianceAuditEventKafkaTemplate,
            AuditKafkaConfiguration auditKafkaConfiguration) {
        this.auditTrailEventKafkaTemplate = auditTrailEventKafkaTemplate;
        this.complianceAuditEventKafkaTemplate = complianceAuditEventKafkaTemplate;
        this.auditKafkaConfiguration = auditKafkaConfiguration;
    }

    @Override
    public CompletableFuture<Void> publishBusinessEvent(AuditTrailEvent event) {
        return publishToTopic(event, auditKafkaConfiguration.getBusinessEventsTopicName(), "BUSINESS");
    }

    @Override
    public CompletableFuture<Void> publishComplianceEvent(ComplianceAuditEvent event) {
        return publishComplianceToTopic(event, auditKafkaConfiguration.getComplianceEventsTopicName(), "COMPLIANCE");
    }

    @Override
    public CompletableFuture<Void> publishSecurityEvent(AuditTrailEvent event) {
        return publishToTopic(event, auditKafkaConfiguration.getSecurityEventsTopicName(), "SECURITY");
    }

    @Override
    public CompletableFuture<Void> publishSystemEvent(AuditTrailEvent event) {
        return publishToTopic(event, auditKafkaConfiguration.getSystemEventsTopicName(), "SYSTEM");
    }

    @Override
    public CompletableFuture<Void> publishAuditEvent(AuditTrailEvent event, String eventCategory) {
        switch (eventCategory.toUpperCase()) {
            case "BUSINESS":
                return publishBusinessEvent(event);
            case "SECURITY":
                return publishSecurityEvent(event);
            case "SYSTEM":
                return publishSystemEvent(event);
            default:
                log.warn("Unknown event category: {}. Publishing to business events topic.", eventCategory);
                return publishBusinessEvent(event);
        }
    }

    @Override
    public CompletableFuture<Void> publishComplianceAuditEvent(ComplianceAuditEvent event, String eventCategory) {
        if ("COMPLIANCE".equalsIgnoreCase(eventCategory)) {
            return publishComplianceEvent(event);
        } else {
            log.warn("Invalid event category for compliance event: {}. Publishing to compliance events topic.", eventCategory);
            return publishComplianceEvent(event);
        }
    }

    private CompletableFuture<Void> publishToTopic(AuditTrailEvent event, String topicName, String eventType) {
        try {
            String partitionKey = generatePartitionKey(event);
            
            log.info("Publishing {} audit event: {} for entity: {} to topic: {}", 
                    eventType, event.getEventId(), event.getAggregateId(), topicName);

            CompletableFuture<SendResult<String, AuditTrailEvent>> future = 
                auditTrailEventKafkaTemplate.send(topicName, partitionKey, event);

            return future.handle((result, throwable) -> {
                if (throwable != null) {
                    log.error("Failed to publish {} audit event: {} to topic: {}", 
                            eventType, event.getEventId(), topicName, throwable);
                    throw new RuntimeException("Failed to publish audit event", throwable);
                } else {
                    log.debug("Successfully published {} audit event: {} to topic: {} at offset: {}", 
                            eventType, event.getEventId(), topicName, 
                            result.getRecordMetadata().offset());
                    return null;
                }
            });

        } catch (Exception e) {
            log.error("Error publishing {} audit event: {} to topic: {}", 
                    eventType, event.getEventId(), topicName, e);
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    private CompletableFuture<Void> publishComplianceToTopic(ComplianceAuditEvent event, String topicName, String eventType) {
        try {
            String partitionKey = generateCompliancePartitionKey(event);
            
            log.info("Publishing {} audit event: {} for entity: {} to topic: {}", 
                    eventType, event.getEventId(), event.getAggregateId(), topicName);

            CompletableFuture<SendResult<String, ComplianceAuditEvent>> future = 
                complianceAuditEventKafkaTemplate.send(topicName, partitionKey, event);

            return future.handle((result, throwable) -> {
                if (throwable != null) {
                    log.error("Failed to publish {} audit event: {} to topic: {}", 
                            eventType, event.getEventId(), topicName, throwable);
                    throw new RuntimeException("Failed to publish compliance audit event", throwable);
                } else {
                    log.debug("Successfully published {} audit event: {} to topic: {} at offset: {}", 
                            eventType, event.getEventId(), topicName, 
                            result.getRecordMetadata().offset());
                    return null;
                }
            });

        } catch (Exception e) {
            log.error("Error publishing {} audit event: {} to topic: {}", 
                    eventType, event.getEventId(), topicName, e);
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }

    private String generatePartitionKey(AuditTrailEvent event) {
        return event.getAggregateType() + ":" + event.getAggregateId();
    }

    private String generateCompliancePartitionKey(ComplianceAuditEvent event) {
        return event.getComplianceType() + ":" + event.getAggregateType() + ":" + event.getAggregateId();
    }
}
