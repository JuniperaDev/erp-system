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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.service.EventSourcingAuditTrailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@Transactional
class EventSourcingAuditTrailIntegrationTest {

    @Autowired
    private DomainEventStore eventStore;

    @Autowired
    private DomainEventPublisher eventPublisher;

    @Autowired
    private EventSourcingAuditTrailService eventSourcingAuditTrailService;

    private AuditTrailEvent auditEvent;
    private EntityStateChangedEvent stateChangeEvent;
    private ComplianceAuditEvent complianceEvent;
    private Instant testStartTime;

    @BeforeEach
    void setUp() {
        testStartTime = Instant.now();
        
        auditEvent = new AuditTrailEvent(
            "entity-123",
            "TestEntity",
            "user-456",
            "CREATE",
            "TestEntity",
            null,
            "{\"name\":\"Test Entity\",\"value\":100}",
            "192.168.1.1",
            "Mozilla/5.0",
            UUID.randomUUID()
        );

        stateChangeEvent = new EntityStateChangedEvent(
            "entity-123",
            "TestEntity",
            "UPDATE",
            "value",
            "100",
            "200",
            "user-456",
            "Business requirement change",
            UUID.randomUUID()
        );

        complianceEvent = new ComplianceAuditEvent(
            "entity-123",
            "TestEntity",
            "IFRS16",
            "IFRS16.2.1",
            "COMPLIANT",
            "Entity meets IFRS16 requirements for lease accounting",
            "auditor-789",
            "LOW",
            false,
            UUID.randomUUID()
        );
    }

    @Test
    void shouldCreateAndRetrieveAuditTrailEvents() {
        long initialCount = eventStore.count();

        eventPublisher.publish(auditEvent);
        eventPublisher.publish(stateChangeEvent);
        eventPublisher.publish(complianceEvent);

        long finalCount = eventStore.count();
        assertThat(finalCount).isEqualTo(initialCount + 3);

        List<DomainEvent> auditTrail = eventSourcingAuditTrailService.reconstructAuditTrail(
            "TestEntity", "entity-123", testStartTime, Instant.now());

        assertThat(auditTrail).hasSize(3);
        assertThat(auditTrail).extracting(DomainEvent::getAggregateId).containsOnly("entity-123");
        assertThat(auditTrail).extracting(DomainEvent::getAggregateType).containsOnly("TestEntity");
    }

    @Test
    void shouldGenerateComplianceReport() {
        eventPublisher.publish(auditEvent);
        eventPublisher.publish(stateChangeEvent);
        eventPublisher.publish(complianceEvent);

        Map<String, Object> report = eventSourcingAuditTrailService.generateComplianceReport(
            testStartTime, Instant.now());

        assertThat(report).containsKey("reportPeriod");
        assertThat(report).containsKey("totalEvents");
        assertThat(report).containsKey("eventSummary");
        assertThat(report).containsKey("complianceEvents");
        assertThat(report).containsKey("entityTypeCounts");

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> complianceEvents = (List<Map<String, Object>>) report.get("complianceEvents");
        assertThat(complianceEvents).hasSizeGreaterThanOrEqualTo(3);

        @SuppressWarnings("unchecked")
        Map<String, Long> entityTypeCounts = (Map<String, Long>) report.get("entityTypeCounts");
        assertThat(entityTypeCounts).containsKey("TestEntity");
    }

    @Test
    void shouldReplayEventsForAggregate() {
        eventPublisher.publish(auditEvent);
        eventPublisher.publish(stateChangeEvent);

        List<DomainEvent> replayedEvents = eventSourcingAuditTrailService.replayEvents(
            "entity-123", testStartTime);

        assertThat(replayedEvents).hasSizeGreaterThanOrEqualTo(2);
        assertThat(replayedEvents).extracting(DomainEvent::getAggregateId).containsOnly("entity-123");
    }

    @Test
    void shouldFindRelatedEventsByCorrelationId() {
        UUID correlationId = UUID.randomUUID();
        
        AuditTrailEvent relatedEvent1 = new AuditTrailEvent(
            "entity-456",
            "RelatedEntity",
            "user-789",
            "CREATE",
            "RelatedEntity",
            null,
            "{\"name\":\"Related Entity\"}",
            "192.168.1.2",
            "Mozilla/5.0",
            correlationId
        );

        EntityStateChangedEvent relatedEvent2 = new EntityStateChangedEvent(
            "entity-789",
            "AnotherEntity",
            "UPDATE",
            "status",
            "PENDING",
            "ACTIVE",
            "user-789",
            "Related to entity-456 creation",
            correlationId
        );

        eventPublisher.publish(relatedEvent1);
        eventPublisher.publish(relatedEvent2);

        List<DomainEvent> relatedEvents = eventSourcingAuditTrailService.findRelatedEvents(correlationId);

        assertThat(relatedEvents).hasSize(2);
        assertThat(relatedEvents).extracting(DomainEvent::getCorrelationId).containsOnly(correlationId);
    }

    @Test
    void shouldGetAuditEventSummary() {
        eventPublisher.publish(auditEvent);
        eventPublisher.publish(stateChangeEvent);
        eventPublisher.publish(complianceEvent);

        Map<String, Long> summary = eventSourcingAuditTrailService.getAuditEventSummary(
            testStartTime, Instant.now());

        assertThat(summary).isNotEmpty();
        assertThat(summary).containsKey("AuditTrailEvent");
        assertThat(summary).containsKey("EntityStateChangedEvent");
        assertThat(summary).containsKey("ComplianceAuditEvent");
    }

    @Test
    void shouldReconstructEntityState() {
        eventPublisher.publish(auditEvent);
        eventPublisher.publish(stateChangeEvent);

        Map<String, Object> entityState = eventSourcingAuditTrailService.reconstructEntityState(
            "TestEntity", "entity-123", Instant.now());

        assertThat(entityState).containsKey("entityType");
        assertThat(entityState).containsKey("entityId");
        assertThat(entityState).containsKey("asOfDate");
        assertThat(entityState).containsKey("eventCount");
        assertThat(entityState).containsKey("eventHistory");

        assertThat(entityState.get("entityType")).isEqualTo("TestEntity");
        assertThat(entityState.get("entityId")).isEqualTo("entity-123");
        assertThat((Integer) entityState.get("eventCount")).isGreaterThanOrEqualTo(2);
    }

    @Test
    void shouldValidateEventIntegrity() {
        eventPublisher.publish(auditEvent);
        eventPublisher.publish(stateChangeEvent);

        boolean isValid = eventSourcingAuditTrailService.validateEventIntegrity("entity-123");

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldExportAuditTrail() {
        eventPublisher.publish(auditEvent);
        eventPublisher.publish(stateChangeEvent);

        Map<String, Object> export = eventSourcingAuditTrailService.exportAuditTrail(
            "TestEntity", "entity-123", testStartTime, Instant.now(), "JSON");

        assertThat(export).containsKey("exportMetadata");
        assertThat(export).containsKey("events");

        @SuppressWarnings("unchecked")
        Map<String, Object> metadata = (Map<String, Object>) export.get("exportMetadata");
        assertThat(metadata.get("entityType")).isEqualTo("TestEntity");
        assertThat(metadata.get("entityId")).isEqualTo("entity-123");
        assertThat(metadata.get("format")).isEqualTo("JSON");
        assertThat((Integer) metadata.get("eventCount")).isGreaterThanOrEqualTo(2);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> events = (List<Map<String, Object>>) export.get("events");
        assertThat(events).hasSizeGreaterThanOrEqualTo(2);
    }
}
