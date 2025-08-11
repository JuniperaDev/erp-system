package io.github.erp.domain.events.audit;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AuditEventHandler.class);
    private static final Logger auditLog = LoggerFactory.getLogger("AUDIT");

    @EventListener
    public void handleAuditTrailEvent(AuditTrailEvent event) {
        log.debug("Processing audit trail event: {} for entity: {}", 
                 event.getEventType(), event.getEntityName());
        
        auditLog.info("AUDIT_TRAIL: User {} performed {} on {} (ID: {}) - Old: {}, New: {}", 
                     event.getUserId(), event.getActionType(), event.getEntityName(), 
                     event.getAggregateId(), event.getOldValues(), event.getNewValues());
    }

    @EventListener
    public void handleEntityStateChangedEvent(EntityStateChangedEvent event) {
        log.debug("Processing entity state change event: {} for field: {}", 
                 event.getEventType(), event.getFieldName());
        
        auditLog.info("STATE_CHANGE: {} changed field {} from '{}' to '{}' on {} (ID: {}) - Reason: {}", 
                     event.getChangedBy(), event.getFieldName(), event.getOldValue(), 
                     event.getNewValue(), event.getAggregateType(), event.getAggregateId(), 
                     event.getChangeReason());
    }

    @EventListener
    public void handleComplianceAuditEvent(ComplianceAuditEvent event) {
        log.debug("Processing compliance audit event: {} for regulation: {}", 
                 event.getEventType(), event.getRegulationReference());
        
        auditLog.info("COMPLIANCE_AUDIT: {} audit for {} (ID: {}) - Status: {}, Risk: {}, Remediation Required: {}", 
                     event.getComplianceType(), event.getAggregateType(), event.getAggregateId(), 
                     event.getComplianceStatus(), event.getRiskLevel(), event.getRemediationRequired());
        
        if (Boolean.TRUE.equals(event.getRemediationRequired())) {
            log.warn("Compliance remediation required for {} (ID: {}) - Regulation: {}", 
                    event.getAggregateType(), event.getAggregateId(), event.getRegulationReference());
        }
    }

    @EventListener
    public void handleGenericDomainEvent(DomainEvent event) {
        if (!(event instanceof AuditTrailEvent) && 
            !(event instanceof EntityStateChangedEvent) && 
            !(event instanceof ComplianceAuditEvent)) {
            
            log.debug("Processing generic domain event for audit: {}", event.getEventType());
            
            auditLog.info("DOMAIN_EVENT: {} occurred on {} (ID: {}) at {}", 
                         event.getEventType(), event.getAggregateType(), 
                         event.getAggregateId(), event.getOccurredOn());
        }
    }
}
