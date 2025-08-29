package io.github.erp.service;

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
import io.github.erp.domain.events.financial.SettlementCreatedEvent;
import io.github.erp.service.impl.EventSourcingAuditTrailServiceImpl;
import io.github.erp.service.validation.AuditEventSchemaValidator;
import io.github.erp.service.validation.ComplianceAuditEventSchemaValidator;
import io.github.erp.service.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventSourcingAuditTrailServiceTest {

    @Mock
    private DomainEventStore eventStore;

    @Mock
    private AuditEventSchemaValidator auditEventSchemaValidator;

    @Mock
    private ComplianceAuditEventSchemaValidator complianceAuditEventSchemaValidator;

    private EventSourcingAuditTrailService eventSourcingAuditTrailService;

    private SettlementCreatedEvent testEvent;
    private Instant fromDate;
    private Instant toDate;

    @BeforeEach
    void setUp() {
        ValidationResult validResult = ValidationResult.valid();
        lenient().when(auditEventSchemaValidator.validate(any())).thenReturn(validResult);
        lenient().when(complianceAuditEventSchemaValidator.validate(any())).thenReturn(validResult);
        
        eventSourcingAuditTrailService = new EventSourcingAuditTrailServiceImpl(
            eventStore, auditEventSchemaValidator, complianceAuditEventSchemaValidator);
        
        fromDate = Instant.now().minusSeconds(3600);
        toDate = Instant.now();
        
        testEvent = new SettlementCreatedEvent(
            "settlement-123",
            "PAY-001",
            BigDecimal.valueOf(1000.00),
            LocalDate.now(),
            "USD",
            "Test settlement",
            "Test Biller",
            UUID.randomUUID()
        );
    }

    @Test
    void shouldReconstructAuditTrail() {
        String entityType = "Settlement";
        String entityId = "settlement-123";
        List<DomainEvent> expectedEvents = Arrays.asList(testEvent);
        
        when(eventStore.findAuditTrailEvents(entityType, entityId, fromDate, toDate))
            .thenReturn(expectedEvents);
        
        List<DomainEvent> result = eventSourcingAuditTrailService.reconstructAuditTrail(
            entityType, entityId, fromDate, toDate);
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testEvent);
    }

    @Test
    void shouldGenerateComplianceReport() {
        List<DomainEvent> complianceEvents = Arrays.asList(testEvent);
        Map<String, Long> eventSummary = Map.of("SettlementCreatedEvent", 1L);
        
        when(eventStore.findComplianceAuditEvents(fromDate, toDate))
            .thenReturn(complianceEvents);
        when(eventStore.getAuditEventSummary(fromDate, toDate))
            .thenReturn(eventSummary);
        
        Map<String, Object> report = eventSourcingAuditTrailService.generateComplianceReport(fromDate, toDate);
        
        assertThat(report).containsKey("reportPeriod");
        assertThat(report).containsKey("totalEvents");
        assertThat(report).containsKey("eventSummary");
        assertThat(report).containsKey("complianceEvents");
        assertThat(report).containsKey("entityTypeCounts");
        
        assertThat((Integer) report.get("totalEvents")).isEqualTo(1);
    }

    @Test
    void shouldReplayEvents() {
        String aggregateId = "settlement-123";
        List<DomainEvent> expectedEvents = Arrays.asList(testEvent);
        
        when(eventStore.findEventsForReplay(aggregateId, fromDate))
            .thenReturn(expectedEvents);
        
        List<DomainEvent> result = eventSourcingAuditTrailService.replayEvents(aggregateId, fromDate);
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testEvent);
    }

    @Test
    void shouldFindRelatedEvents() {
        UUID correlationId = UUID.randomUUID();
        List<DomainEvent> expectedEvents = Arrays.asList(testEvent);
        
        when(eventStore.findEventsByCorrelationId(correlationId))
            .thenReturn(expectedEvents);
        
        List<DomainEvent> result = eventSourcingAuditTrailService.findRelatedEvents(correlationId);
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(testEvent);
    }

    @Test
    void shouldGetAuditEventSummary() {
        Map<String, Long> expectedSummary = Map.of("SettlementCreatedEvent", 5L, "PaymentProcessedEvent", 3L);
        
        when(eventStore.getAuditEventSummary(fromDate, toDate))
            .thenReturn(expectedSummary);
        
        Map<String, Long> result = eventSourcingAuditTrailService.getAuditEventSummary(fromDate, toDate);
        
        assertThat(result).hasSize(2);
        assertThat(result.get("SettlementCreatedEvent")).isEqualTo(5L);
        assertThat(result.get("PaymentProcessedEvent")).isEqualTo(3L);
    }

    @Test
    void shouldReconstructEntityState() {
        String entityType = "Settlement";
        String entityId = "settlement-123";
        Instant asOfDate = Instant.now();
        List<DomainEvent> events = Arrays.asList(testEvent);
        
        when(eventStore.findAuditTrailEvents(eq(entityType), eq(entityId), any(Instant.class), eq(asOfDate)))
            .thenReturn(events);
        
        Map<String, Object> result = eventSourcingAuditTrailService.reconstructEntityState(
            entityType, entityId, asOfDate);
        
        assertThat(result).containsKey("entityType");
        assertThat(result).containsKey("entityId");
        assertThat(result).containsKey("asOfDate");
        assertThat(result).containsKey("eventCount");
        assertThat(result).containsKey("eventHistory");
        
        assertThat(result.get("entityType")).isEqualTo(entityType);
        assertThat(result.get("entityId")).isEqualTo(entityId);
        assertThat((Integer) result.get("eventCount")).isEqualTo(1);
    }

    @Test
    void shouldValidateEventIntegrity() {
        String aggregateId = "settlement-123";
        List<DomainEvent> events = Arrays.asList(testEvent);
        
        when(eventStore.findByAggregateId(aggregateId))
            .thenReturn(events);
        
        boolean result = eventSourcingAuditTrailService.validateEventIntegrity(aggregateId);
        
        assertThat(result).isTrue();
    }

    @Test
    void shouldExportAuditTrail() {
        String entityType = "Settlement";
        String entityId = "settlement-123";
        String format = "JSON";
        List<DomainEvent> events = Arrays.asList(testEvent);
        
        when(eventStore.findAuditTrailEvents(entityType, entityId, fromDate, toDate))
            .thenReturn(events);
        
        Map<String, Object> result = eventSourcingAuditTrailService.exportAuditTrail(
            entityType, entityId, fromDate, toDate, format);
        
        assertThat(result).containsKey("exportMetadata");
        assertThat(result).containsKey("events");
        
        @SuppressWarnings("unchecked")
        Map<String, Object> metadata = (Map<String, Object>) result.get("exportMetadata");
        assertThat(metadata.get("entityType")).isEqualTo(entityType);
        assertThat(metadata.get("entityId")).isEqualTo(entityId);
        assertThat(metadata.get("format")).isEqualTo(format);
        assertThat((Integer) metadata.get("eventCount")).isEqualTo(1);
    }
}
