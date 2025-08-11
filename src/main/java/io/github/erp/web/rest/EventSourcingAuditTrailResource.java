package io.github.erp.web.rest;

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
import io.github.erp.service.EventSourcingAuditTrailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/audit-trail")
public class EventSourcingAuditTrailResource {

    private final Logger log = LoggerFactory.getLogger(EventSourcingAuditTrailResource.class);

    private final EventSourcingAuditTrailService eventSourcingAuditTrailService;

    public EventSourcingAuditTrailResource(EventSourcingAuditTrailService eventSourcingAuditTrailService) {
        this.eventSourcingAuditTrailService = eventSourcingAuditTrailService;
    }

    @GetMapping("/reconstruct/{entityType}/{entityId}")
    public ResponseEntity<List<DomainEvent>> reconstructAuditTrail(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate) {
        
        log.debug("REST request to reconstruct audit trail for {} with id {} from {} to {}", 
                 entityType, entityId, fromDate, toDate);
        
        List<DomainEvent> auditTrail = eventSourcingAuditTrailService.reconstructAuditTrail(
            entityType, entityId, fromDate, toDate);
        
        return ResponseEntity.ok(auditTrail);
    }

    @GetMapping("/compliance-report")
    public ResponseEntity<Map<String, Object>> generateComplianceReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate) {
        
        log.debug("REST request to generate compliance report from {} to {}", fromDate, toDate);
        
        Map<String, Object> report = eventSourcingAuditTrailService.generateComplianceReport(fromDate, toDate);
        
        return ResponseEntity.ok(report);
    }

    @GetMapping("/replay/{aggregateId}")
    public ResponseEntity<List<DomainEvent>> replayEvents(
            @PathVariable String aggregateId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromTime) {
        
        log.debug("REST request to replay events for aggregate {} from {}", aggregateId, fromTime);
        
        List<DomainEvent> events = eventSourcingAuditTrailService.replayEvents(aggregateId, fromTime);
        
        return ResponseEntity.ok(events);
    }

    @GetMapping("/related-events/{correlationId}")
    public ResponseEntity<List<DomainEvent>> findRelatedEvents(@PathVariable UUID correlationId) {
        
        log.debug("REST request to find related events for correlation id {}", correlationId);
        
        List<DomainEvent> events = eventSourcingAuditTrailService.findRelatedEvents(correlationId);
        
        return ResponseEntity.ok(events);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getAuditEventSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate) {
        
        log.debug("REST request to get audit event summary from {} to {}", fromDate, toDate);
        
        Map<String, Long> summary = eventSourcingAuditTrailService.getAuditEventSummary(fromDate, toDate);
        
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/entity-state/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> reconstructEntityState(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant asOfDate) {
        
        log.debug("REST request to reconstruct entity state for {} with id {} as of {}", 
                 entityType, entityId, asOfDate);
        
        Map<String, Object> entityState = eventSourcingAuditTrailService.reconstructEntityState(
            entityType, entityId, asOfDate);
        
        return ResponseEntity.ok(entityState);
    }

    @GetMapping("/timeline/{entityType}/{entityId}")
    public ResponseEntity<List<Map<String, Object>>> getAuditTrailTimeline(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate) {
        
        log.debug("REST request to get audit trail timeline for {} with id {} from {} to {}", 
                 entityType, entityId, fromDate, toDate);
        
        List<Map<String, Object>> timeline = eventSourcingAuditTrailService.getAuditTrailTimeline(
            entityType, entityId, fromDate, toDate);
        
        return ResponseEntity.ok(timeline);
    }

    @GetMapping("/validate/{aggregateId}")
    public ResponseEntity<Map<String, Object>> validateEventIntegrity(@PathVariable String aggregateId) {
        
        log.debug("REST request to validate event integrity for aggregate {}", aggregateId);
        
        boolean isValid = eventSourcingAuditTrailService.validateEventIntegrity(aggregateId);
        
        Map<String, Object> result = Map.of(
            "aggregateId", aggregateId,
            "isValid", isValid,
            "validatedAt", Instant.now()
        );
        
        return ResponseEntity.ok(result);
    }

    @GetMapping("/export/{entityType}/{entityId}")
    public ResponseEntity<Map<String, Object>> exportAuditTrail(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
            @RequestParam(defaultValue = "JSON") String format) {
        
        log.debug("REST request to export audit trail for {} with id {} from {} to {} in format {}", 
                 entityType, entityId, fromDate, toDate, format);
        
        Map<String, Object> export = eventSourcingAuditTrailService.exportAuditTrail(
            entityType, entityId, fromDate, toDate, format);
        
        return ResponseEntity.ok(export);
    }
}
