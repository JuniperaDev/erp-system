package io.github.erp.service.audit;

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
import io.github.erp.service.audit.impl.AuditEventRoutingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuditEventRoutingServiceTest {

    private AuditEventRoutingService auditEventRoutingService;

    @BeforeEach
    void setUp() {
        auditEventRoutingService = new AuditEventRoutingServiceImpl();
    }

    @Test
    void shouldDetermineBusinessCategory() {
        AuditTrailEvent event = createTestEvent("CREATE");
        
        String category = auditEventRoutingService.determineEventCategory(event);
        
        assertThat(category).isEqualTo("BUSINESS");
    }

    @Test
    void shouldDetermineSecurityCategory() {
        AuditTrailEvent event = createTestEvent("LOGIN");
        
        String category = auditEventRoutingService.determineEventCategory(event);
        
        assertThat(category).isEqualTo("SECURITY");
    }

    @Test
    void shouldDetermineSystemCategory() {
        AuditTrailEvent event = createTestEvent("SYSTEM_START");
        
        String category = auditEventRoutingService.determineEventCategory(event);
        
        assertThat(category).isEqualTo("SYSTEM");
    }

    @Test
    void shouldDetermineComplianceCategory() {
        ComplianceAuditEvent event = createTestComplianceEvent();
        
        String category = auditEventRoutingService.determineEventCategory(event);
        
        assertThat(category).isEqualTo("COMPLIANCE");
    }

    @Test
    void shouldIdentifyMultiTopicEvents() {
        AuditTrailEvent event = createTestEvent("DELETE");
        
        boolean shouldRoute = auditEventRoutingService.shouldRouteToMultipleTopics(event);
        
        assertThat(shouldRoute).isTrue();
    }

    @Test
    void shouldGetMultipleCategories() {
        AuditTrailEvent event = createTestEvent("DELETE");
        
        String[] categories = auditEventRoutingService.getMultipleCategories(event);
        
        assertThat(categories).containsExactlyInAnyOrder("BUSINESS", "SECURITY");
    }

    @Test
    void shouldIdentifyHighPriorityEvent() {
        AuditTrailEvent event = createTestEvent("DELETE");
        
        boolean isHighPriority = auditEventRoutingService.isHighPriorityEvent(event);
        
        assertThat(isHighPriority).isTrue();
    }

    @Test
    void shouldIdentifyHighPriorityComplianceEvent() {
        ComplianceAuditEvent event = createTestComplianceEvent();
        event.setRiskLevel("HIGH");
        
        boolean isHighPriority = auditEventRoutingService.isHighPriorityEvent(event);
        
        assertThat(isHighPriority).isTrue();
    }

    private AuditTrailEvent createTestEvent(String actionType) {
        AuditTrailEvent event = new AuditTrailEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(UUID.randomUUID().toString());
        event.setAggregateType("TEST_ENTITY");
        event.setActionType(actionType);
        event.setEventTimestamp(Instant.now());
        return event;
    }

    private ComplianceAuditEvent createTestComplianceEvent() {
        ComplianceAuditEvent event = new ComplianceAuditEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(UUID.randomUUID().toString());
        event.setAggregateType("COMPLIANCE_ENTITY");
        event.setEventTimestamp(Instant.now());
        event.setComplianceType("SOX");
        event.setRiskLevel("LOW");
        event.setRemediationRequired(false);
        return event;
    }
}
