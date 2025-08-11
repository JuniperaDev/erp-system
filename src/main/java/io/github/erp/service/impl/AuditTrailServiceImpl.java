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

import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.service.AuditTrailService;
import io.github.erp.service.EventSourcingAuditTrailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing audit trails and change tracking.
 */
@Service
@Transactional
public class AuditTrailServiceImpl implements AuditTrailService {

    private final Logger log = LoggerFactory.getLogger(AuditTrailServiceImpl.class);
    private final Logger auditLog = LoggerFactory.getLogger("AUDIT");
    
    private final DomainEventStore eventStore;
    private final EventSourcingAuditTrailService eventSourcingAuditTrailService;

    public AuditTrailServiceImpl(DomainEventStore eventStore, 
                               EventSourcingAuditTrailService eventSourcingAuditTrailService) {
        this.eventStore = eventStore;
        this.eventSourcingAuditTrailService = eventSourcingAuditTrailService;
    }

    @Override
    public void logBusinessEvent(String entityType, Long entityId, String eventType, 
                                Map<String, Object> changes, String userId) {
        log.debug("Logging business event: {} for {} with id {} by user {}", 
                 eventType, entityType, entityId, userId);
        
        Map<String, Object> auditEntry = createAuditEntry(entityType, entityId, eventType, userId);
        auditEntry.put("changes", changes);
        
        auditLog.info("BUSINESS_EVENT: {}", auditEntry);
    }

    @Override
    public void logDocumentEvent(String entityType, Long entityId, Long documentId, 
                                String action, String userId) {
        log.debug("Logging document event: {} document {} for {} with id {} by user {}", 
                 action, documentId, entityType, entityId, userId);
        
        Map<String, Object> auditEntry = createAuditEntry(entityType, entityId, "DOCUMENT_" + action, userId);
        auditEntry.put("documentId", documentId);
        auditEntry.put("action", action);
        
        auditLog.info("DOCUMENT_EVENT: {}", auditEntry);
    }

    @Override
    public void logPlaceholderEvent(String entityType, Long entityId, Long placeholderId, 
                                   String action, String userId) {
        log.debug("Logging placeholder event: {} placeholder {} for {} with id {} by user {}", 
                 action, placeholderId, entityType, entityId, userId);
        
        Map<String, Object> auditEntry = createAuditEntry(entityType, entityId, "PLACEHOLDER_" + action, userId);
        auditEntry.put("placeholderId", placeholderId);
        auditEntry.put("action", action);
        
        auditLog.info("PLACEHOLDER_EVENT: {}", auditEntry);
    }

    @Override
    public Map<String, Object> getAuditTrail(String entityType, Long entityId, 
                                            Instant fromDate, Instant toDate) {
        log.debug("Retrieving audit trail for {} with id {} from {} to {}", 
                 entityType, entityId, fromDate, toDate);
        
        String entityIdStr = entityId != null ? entityId.toString() : null;
        List<DomainEvent> events = eventSourcingAuditTrailService.reconstructAuditTrail(
            entityType, entityIdStr, fromDate, toDate);
        
        Map<String, Object> auditTrail = new HashMap<>();
        auditTrail.put("entityType", entityType);
        auditTrail.put("entityId", entityId);
        auditTrail.put("fromDate", fromDate);
        auditTrail.put("toDate", toDate);
        auditTrail.put("eventCount", events.size());
        
        List<Map<String, Object>> auditEntries = events.stream()
            .map(this::mapEventToAuditEntry)
            .collect(Collectors.toList());
        auditTrail.put("auditEntries", auditEntries);
        
        return auditTrail;
    }

    @Override
    public void logCrossCuttingOperation(String operation, String entityType, Long entityId, 
                                        Map<String, Object> details, String userId) {
        log.debug("Logging cross-cutting operation: {} for {} with id {} by user {}", 
                 operation, entityType, entityId, userId);
        
        Map<String, Object> auditEntry = createAuditEntry(entityType, entityId, "CROSS_CUTTING_" + operation, userId);
        auditEntry.put("operation", operation);
        auditEntry.put("details", details);
        
        auditLog.info("CROSS_CUTTING_EVENT: {}", auditEntry);
    }

    private Map<String, Object> createAuditEntry(String entityType, Long entityId, String eventType, String userId) {
        Map<String, Object> auditEntry = new HashMap<>();
        auditEntry.put("timestamp", Instant.now());
        auditEntry.put("entityType", entityType);
        auditEntry.put("entityId", entityId);
        auditEntry.put("eventType", eventType);
        auditEntry.put("userId", userId);
        return auditEntry;
    }

    private Map<String, Object> mapEventToAuditEntry(DomainEvent event) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("eventId", event.getEventId());
        entry.put("eventType", event.getEventType());
        entry.put("aggregateType", event.getAggregateType());
        entry.put("aggregateId", event.getAggregateId());
        entry.put("timestamp", event.getOccurredOn());
        entry.put("version", event.getVersion());
        entry.put("correlationId", event.getCorrelationId());
        return entry;
    }
}
