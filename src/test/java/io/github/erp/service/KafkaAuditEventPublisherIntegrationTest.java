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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.topics.audit-events.business-events.name=test-business-events",
    "spring.kafka.topics.audit-events.compliance-events.name=test-compliance-events",
    "spring.kafka.topics.audit-events.security-events.name=test-security-events",
    "spring.kafka.topics.audit-events.system-events.name=test-system-events"
})
class KafkaAuditEventPublisherIntegrationTest {

    @Autowired
    private KafkaAuditEventPublisher kafkaAuditEventPublisher;

    @Test
    void shouldPublishBusinessEvent() throws Exception {
        AuditTrailEvent event = createTestAuditEvent("CREATE", "BUSINESS_ENTITY");
        
        CompletableFuture<Void> future = kafkaAuditEventPublisher.publishBusinessEvent(event);
        
        future.get();
        assertThat(future).isCompleted();
    }

    @Test
    void shouldPublishComplianceEvent() throws Exception {
        ComplianceAuditEvent event = createTestComplianceEvent();
        
        CompletableFuture<Void> future = kafkaAuditEventPublisher.publishComplianceEvent(event);
        
        future.get();
        assertThat(future).isCompleted();
    }

    @Test
    void shouldPublishSecurityEvent() throws Exception {
        AuditTrailEvent event = createTestAuditEvent("LOGIN", "USER");
        
        CompletableFuture<Void> future = kafkaAuditEventPublisher.publishSecurityEvent(event);
        
        future.get();
        assertThat(future).isCompleted();
    }

    @Test
    void shouldPublishSystemEvent() throws Exception {
        AuditTrailEvent event = createTestAuditEvent("SYSTEM_START", "SYSTEM");
        
        CompletableFuture<Void> future = kafkaAuditEventPublisher.publishSystemEvent(event);
        
        future.get();
        assertThat(future).isCompleted();
    }

    @Test
    void shouldRouteEventByCategory() throws Exception {
        AuditTrailEvent event = createTestAuditEvent("CREATE", "BUSINESS_ENTITY");
        
        CompletableFuture<Void> future = kafkaAuditEventPublisher.publishAuditEvent(event, "BUSINESS");
        
        future.get();
        assertThat(future).isCompleted();
    }

    private AuditTrailEvent createTestAuditEvent(String actionType, String aggregateType) {
        AuditTrailEvent event = new AuditTrailEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(UUID.randomUUID().toString());
        event.setAggregateType(aggregateType);
        event.setActionType(actionType);
        event.setEventTimestamp(Instant.now());
        event.setUserId("test-user");
        event.setEntityName("TestEntity");
        return event;
    }

    private ComplianceAuditEvent createTestComplianceEvent() {
        ComplianceAuditEvent event = new ComplianceAuditEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(UUID.randomUUID().toString());
        event.setAggregateType("COMPLIANCE_ENTITY");
        event.setEventTimestamp(Instant.now());
        event.setComplianceType("SOX");
        event.setRegulationReference("SOX-404");
        event.setComplianceStatus("COMPLIANT");
        event.setRiskLevel("LOW");
        event.setRemediationRequired(false);
        return event;
    }
}
