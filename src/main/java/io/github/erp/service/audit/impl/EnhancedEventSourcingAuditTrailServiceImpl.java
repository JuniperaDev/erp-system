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
import io.github.erp.service.EventSourcingAuditTrailService;
import io.github.erp.service.audit.EnhancedEventSourcingAuditTrailService;
import io.github.erp.service.audit.KafkaAuditEventIntegrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EnhancedEventSourcingAuditTrailServiceImpl implements EnhancedEventSourcingAuditTrailService {

    private static final Logger log = LoggerFactory.getLogger(EnhancedEventSourcingAuditTrailServiceImpl.class);

    private final EventSourcingAuditTrailService eventSourcingAuditTrailService;
    private final KafkaAuditEventIntegrationService kafkaAuditEventIntegrationService;

    public EnhancedEventSourcingAuditTrailServiceImpl(
            EventSourcingAuditTrailService eventSourcingAuditTrailService,
            KafkaAuditEventIntegrationService kafkaAuditEventIntegrationService) {
        this.eventSourcingAuditTrailService = eventSourcingAuditTrailService;
        this.kafkaAuditEventIntegrationService = kafkaAuditEventIntegrationService;
    }

    @Override
    @Transactional
    public AuditTrailEvent validatePersistAndPublishEvent(AuditTrailEvent event) {
        log.info("Validating, persisting and publishing audit trail event: {}", event.getEventId());
        
        DomainEvent persistedEvent = eventSourcingAuditTrailService.validateAndPersistEvent(event);
        
        kafkaAuditEventIntegrationService.publishAuditEventToKafka(persistedEvent);
        
        return (AuditTrailEvent) persistedEvent;
    }

    @Override
    @Transactional
    public ComplianceAuditEvent validatePersistAndPublishComplianceEvent(ComplianceAuditEvent event) {
        log.info("Validating, persisting and publishing compliance audit event: {}", event.getEventId());
        
        DomainEvent persistedEvent = eventSourcingAuditTrailService.validateAndPersistEvent(event);
        
        kafkaAuditEventIntegrationService.publishAuditEventToKafka(persistedEvent);
        
        return (ComplianceAuditEvent) persistedEvent;
    }
}
