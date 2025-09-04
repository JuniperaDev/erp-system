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

import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ReportingConsumer {

    private static final Logger log = LoggerFactory.getLogger(ReportingConsumer.class);

    @KafkaListener(
        topics = {
            "${spring.kafka.topics.audit-events.business-events.name:erp.audit.prod.business-events.v1}",
            "${spring.kafka.topics.audit-events.security-events.name:erp.audit.prod.security-events.v1}",
            "${spring.kafka.topics.audit-events.system-events.name:erp.audit.prod.system-events.v1}"
        },
        groupId = "erp-system-audit-reporting",
        containerFactory = "auditTrailEventKafkaListenerContainerFactory"
    )
    public void consumeAuditEventsForReporting(
            @Payload AuditTrailEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Processing audit event for reporting: {} from topic: {} partition: {} offset: {}", 
                    event.getEventId(), topic, partition, offset);
            
            generateAuditReport(event, topic);
            
            log.debug("Successfully processed audit event for reporting: {}", event.getEventId());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing audit event for reporting: {} from topic: {}", 
                    event.getEventId(), topic, e);
            throw e;
        }
    }

    @KafkaListener(
        topics = "${spring.kafka.topics.audit-events.compliance-events.name:erp.audit.prod.compliance-events.v1}",
        groupId = "erp-system-compliance-reporting",
        containerFactory = "complianceAuditEventKafkaListenerContainerFactory"
    )
    public void consumeComplianceEventsForReporting(
            @Payload ComplianceAuditEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Processing compliance event for reporting: {} from topic: {} partition: {} offset: {}", 
                    event.getEventId(), topic, partition, offset);
            
            generateComplianceReport(event);
            
            log.debug("Successfully processed compliance event for reporting: {}", event.getEventId());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("Error processing compliance event for reporting: {} from topic: {}", 
                    event.getEventId(), topic, e);
            throw e;
        }
    }

    private void generateAuditReport(AuditTrailEvent event, String topic) {
        log.info("Generating audit report for event: {} from topic: {}", event.getEventId(), topic);
    }

    private void generateComplianceReport(ComplianceAuditEvent event) {
        log.info("Generating compliance report for event: {} - Type: {} Status: {}", 
                event.getEventId(), event.getComplianceType(), event.getComplianceStatus());
    }
}
