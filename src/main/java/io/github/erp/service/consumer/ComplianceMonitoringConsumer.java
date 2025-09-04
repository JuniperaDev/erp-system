package io.github.erp.service.consumer;

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

import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import io.github.erp.service.EventSourcingAuditTrailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ComplianceMonitoringConsumer {

    private static final Logger log = LoggerFactory.getLogger(ComplianceMonitoringConsumer.class);

    private final EventSourcingAuditTrailService eventSourcingAuditTrailService;

    public ComplianceMonitoringConsumer(EventSourcingAuditTrailService eventSourcingAuditTrailService) {
        this.eventSourcingAuditTrailService = eventSourcingAuditTrailService;
    }

    @KafkaListener(
        topics = "${spring.kafka.topics.audit-events.compliance-events.name:erp.audit.prod.compliance-events.v1}",
        groupId = "erp-system-compliance-monitoring",
        containerFactory = "complianceAuditEventKafkaListenerContainerFactory"
    )
    public void consumeComplianceEvents(
            @Payload ComplianceAuditEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Consuming compliance audit event: {} from topic: {} partition: {} offset: {}", 
                    event.getEventId(), topic, partition, offset);
            
            eventSourcingAuditTrailService.validateAndPersistEvent(event);
            
            if (isHighRiskComplianceEvent(event)) {
                log.warn("High-risk compliance event detected: {} - Type: {} Risk Level: {}", 
                        event.getEventId(), event.getComplianceType(), event.getRiskLevel());
                processHighRiskEvent(event);
            }
            
            log.debug("Successfully processed compliance audit event: {}", event.getEventId());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing compliance audit event: {} from topic: {}", 
                    event.getEventId(), topic, e);
            throw e;
        }
    }

    private boolean isHighRiskComplianceEvent(ComplianceAuditEvent event) {
        String riskLevel = event.getRiskLevel();
        Boolean remediationRequired = event.getRemediationRequired();
        
        return ("HIGH".equalsIgnoreCase(riskLevel) || "CRITICAL".equalsIgnoreCase(riskLevel)) ||
               Boolean.TRUE.equals(remediationRequired);
    }

    private void processHighRiskEvent(ComplianceAuditEvent event) {
        log.info("Processing high-risk compliance event: {} - initiating compliance workflow", 
                event.getEventId());
    }
}
