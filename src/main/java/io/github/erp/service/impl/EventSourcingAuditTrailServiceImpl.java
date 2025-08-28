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
import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import io.github.erp.service.EventSourcingAuditTrailService;
import io.github.erp.service.validation.AuditEventSchemaValidator;
import io.github.erp.service.validation.ComplianceAuditEventSchemaValidator;
import io.github.erp.service.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventSourcingAuditTrailServiceImpl implements EventSourcingAuditTrailService {

    private final Logger log = LoggerFactory.getLogger(EventSourcingAuditTrailServiceImpl.class);
    
    private final DomainEventStore eventStore;
    private final AuditEventSchemaValidator auditEventValidator;
    private final ComplianceAuditEventSchemaValidator complianceAuditEventValidator;

    public EventSourcingAuditTrailServiceImpl(DomainEventStore eventStore,
                                            AuditEventSchemaValidator auditEventValidator,
                                            ComplianceAuditEventSchemaValidator complianceAuditEventValidator) {
        this.eventStore = eventStore;
        this.auditEventValidator = auditEventValidator;
        this.complianceAuditEventValidator = complianceAuditEventValidator;
    }

    @Override
    @Transactional
    public DomainEvent validateAndPersistEvent(DomainEvent event) {
        log.debug("Validating and persisting event: {}", event.getEventId());
        
        if (event instanceof AuditTrailEvent) {
            ValidationResult result = auditEventValidator.validate((AuditTrailEvent) event);
            if (!result.isValid()) {
                log.warn("Audit trail event validation failed for event {}: {}", 
                    event.getEventId(), result.getFormattedErrors());
                throw new IllegalArgumentException("Event validation failed: " + result.getFormattedErrors());
            }
        } else if (event instanceof ComplianceAuditEvent) {
            ValidationResult result = complianceAuditEventValidator.validate((ComplianceAuditEvent) event);
            if (!result.isValid()) {
                log.warn("Compliance audit event validation failed for event {}: {}", 
                    event.getEventId(), result.getFormattedErrors());
                throw new IllegalArgumentException("Event validation failed: " + result.getFormattedErrors());
            }
        }
        
        return eventStore.store(event);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> reconstructAuditTrail(String entityType, String entityId, Instant fromDate, Instant toDate) {
        log.debug("Reconstructing audit trail for {} with id {} from {} to {}", 
                 entityType, entityId, fromDate, toDate);
        
        return eventStore.findAuditTrailEvents(entityType, entityId, fromDate, toDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> generateComplianceReport(Instant fromDate, Instant toDate) {
        log.debug("Generating compliance report from {} to {}", fromDate, toDate);
        
        List<DomainEvent> complianceEvents = eventStore.findComplianceAuditEvents(fromDate, toDate);
        Map<String, Long> eventSummary = eventStore.getAuditEventSummary(fromDate, toDate);
        
        Map<String, Object> report = new HashMap<>();
        report.put("reportPeriod", Map.of("from", fromDate, "to", toDate));
        report.put("totalEvents", complianceEvents.size());
        report.put("eventSummary", eventSummary);
        report.put("complianceEvents", complianceEvents.stream()
            .map(this::mapEventToReportEntry)
            .collect(Collectors.toList()));
        
        Map<String, Long> entityTypeCounts = complianceEvents.stream()
            .collect(Collectors.groupingBy(
                DomainEvent::getAggregateType,
                Collectors.counting()
            ));
        report.put("entityTypeCounts", entityTypeCounts);
        
        return report;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> replayEvents(String aggregateId, Instant fromTime) {
        log.debug("Replaying events for aggregate {} from {}", aggregateId, fromTime);
        
        return eventStore.findEventsForReplay(aggregateId, fromTime);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findRelatedEvents(UUID correlationId) {
        log.debug("Finding related events for correlation id {}", correlationId);
        
        return eventStore.findEventsByCorrelationId(correlationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getAuditEventSummary(Instant fromDate, Instant toDate) {
        log.debug("Getting audit event summary from {} to {}", fromDate, toDate);
        
        return eventStore.getAuditEventSummary(fromDate, toDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> reconstructEntityState(String entityType, String entityId, Instant asOfDate) {
        log.debug("Reconstructing entity state for {} with id {} as of {}", 
                 entityType, entityId, asOfDate);
        
        List<DomainEvent> events = eventStore.findAuditTrailEvents(entityType, entityId, Instant.EPOCH, asOfDate);
        
        Map<String, Object> entityState = new HashMap<>();
        entityState.put("entityType", entityType);
        entityState.put("entityId", entityId);
        entityState.put("asOfDate", asOfDate);
        entityState.put("eventCount", events.size());
        
        if (!events.isEmpty()) {
            DomainEvent lastEvent = events.get(events.size() - 1);
            entityState.put("lastEventType", lastEvent.getEventType());
            entityState.put("lastEventDate", lastEvent.getOccurredOn());
            entityState.put("version", lastEvent.getVersion());
        }
        
        List<Map<String, Object>> eventHistory = events.stream()
            .map(this::mapEventToStateEntry)
            .collect(Collectors.toList());
        entityState.put("eventHistory", eventHistory);
        
        return entityState;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAuditTrailTimeline(String entityType, String entityId, Instant fromDate, Instant toDate) {
        log.debug("Getting audit trail timeline for {} with id {} from {} to {}", 
                 entityType, entityId, fromDate, toDate);
        
        List<DomainEvent> events = eventStore.findAuditTrailEvents(entityType, entityId, fromDate, toDate);
        
        return events.stream()
            .map(this::mapEventToTimelineEntry)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateEventIntegrity(String aggregateId) {
        log.debug("Validating event integrity for aggregate {}", aggregateId);
        
        List<DomainEvent> events = eventStore.findByAggregateId(aggregateId);
        
        if (events.isEmpty()) {
            return true;
        }
        
        for (int i = 1; i < events.size(); i++) {
            DomainEvent current = events.get(i);
            DomainEvent previous = events.get(i - 1);
            
            if (current.getOccurredOn().isBefore(previous.getOccurredOn())) {
                log.warn("Event ordering violation detected for aggregate {}: event {} occurred before {}", 
                        aggregateId, current.getEventId(), previous.getEventId());
                return false;
            }
        }
        
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> exportAuditTrail(String entityType, String entityId, Instant fromDate, Instant toDate, String format) {
        log.debug("Exporting audit trail for {} with id {} from {} to {} in format {}", 
                 entityType, entityId, fromDate, toDate, format);
        
        List<DomainEvent> events = eventStore.findAuditTrailEvents(entityType, entityId, fromDate, toDate);
        
        Map<String, Object> export = new HashMap<>();
        export.put("exportMetadata", Map.of(
            "entityType", entityType,
            "entityId", entityId,
            "fromDate", fromDate,
            "toDate", toDate,
            "format", format,
            "exportedAt", Instant.now(),
            "eventCount", events.size()
        ));
        
        List<Map<String, Object>> exportedEvents = events.stream()
            .map(this::mapEventToExportEntry)
            .collect(Collectors.toList());
        export.put("events", exportedEvents);
        
        return export;
    }

    private Map<String, Object> mapEventToReportEntry(DomainEvent event) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("eventId", event.getEventId());
        entry.put("eventType", event.getEventType());
        entry.put("aggregateType", event.getAggregateType());
        entry.put("aggregateId", event.getAggregateId());
        entry.put("occurredOn", event.getOccurredOn());
        entry.put("version", event.getVersion());
        entry.put("correlationId", event.getCorrelationId());
        return entry;
    }

    private Map<String, Object> mapEventToStateEntry(DomainEvent event) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("eventType", event.getEventType());
        entry.put("occurredOn", event.getOccurredOn());
        entry.put("version", event.getVersion());
        entry.put("eventId", event.getEventId());
        return entry;
    }

    private Map<String, Object> mapEventToTimelineEntry(DomainEvent event) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("timestamp", event.getOccurredOn());
        entry.put("eventType", event.getEventType());
        entry.put("eventId", event.getEventId());
        entry.put("version", event.getVersion());
        entry.put("correlationId", event.getCorrelationId());
        return entry;
    }

    private Map<String, Object> mapEventToExportEntry(DomainEvent event) {
        Map<String, Object> entry = new HashMap<>();
        entry.put("eventId", event.getEventId());
        entry.put("eventType", event.getEventType());
        entry.put("aggregateType", event.getAggregateType());
        entry.put("aggregateId", event.getAggregateId());
        entry.put("occurredOn", event.getOccurredOn());
        entry.put("version", event.getVersion());
        entry.put("correlationId", event.getCorrelationId());
        return entry;
    }
}
