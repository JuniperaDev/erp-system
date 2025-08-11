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

import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.service.impl.AuditTrailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
class AuditTrailServiceTest {

    private AuditTrailService auditTrailService;

    @BeforeEach
    void setUp() {
        DomainEventStore mockEventStore = Mockito.mock(DomainEventStore.class);
        EventSourcingAuditTrailService mockEventSourcingService = Mockito.mock(EventSourcingAuditTrailService.class);
        auditTrailService = new AuditTrailServiceImpl(mockEventStore, mockEventSourcingService);
    }

    @Test
    void shouldLogBusinessEvent() {
        String entityType = "AssetWarranty";
        Long entityId = 1L;
        String eventType = "UPDATE";
        Map<String, Object> changes = new HashMap<>();
        changes.put("description", "Updated warranty description");
        String userId = "testUser";

        auditTrailService.logBusinessEvent(entityType, entityId, eventType, changes, userId);
    }

    @Test
    void shouldLogDocumentEvent() {
        String entityType = "AssetWarranty";
        Long entityId = 1L;
        Long documentId = 2L;
        String action = "ATTACH";
        String userId = "testUser";

        auditTrailService.logDocumentEvent(entityType, entityId, documentId, action, userId);
    }

    @Test
    void shouldLogPlaceholderEvent() {
        String entityType = "AssetWarranty";
        Long entityId = 1L;
        Long placeholderId = 3L;
        String action = "ATTACH";
        String userId = "testUser";

        auditTrailService.logPlaceholderEvent(entityType, entityId, placeholderId, action, userId);
    }


    @Test
    void shouldGetAuditTrail() {
        String entityType = "AssetWarranty";
        Long entityId = 1L;
        Instant fromDate = Instant.now().minusSeconds(3600);
        Instant toDate = Instant.now();

        Map<String, Object> auditTrail = auditTrailService.getAuditTrail(entityType, entityId, fromDate, toDate);

        assertThat(auditTrail).isNotNull();
        assertThat(auditTrail.get("entityType")).isEqualTo(entityType);
        assertThat(auditTrail.get("entityId")).isEqualTo(entityId);
        assertThat(auditTrail.get("fromDate")).isEqualTo(fromDate);
        assertThat(auditTrail.get("toDate")).isEqualTo(toDate);
        assertThat(auditTrail.get("message")).isNotNull();
    }

    @Test
    void shouldHandleNullParameters() {
        assertThatCode(() -> auditTrailService.logBusinessEvent(null, null, null, null, null))
            .doesNotThrowAnyException();
        
        assertThatCode(() -> auditTrailService.logDocumentEvent(null, null, null, null, null))
            .doesNotThrowAnyException();
        
        assertThatCode(() -> auditTrailService.logPlaceholderEvent(null, null, null, null, null))
            .doesNotThrowAnyException();
    }

    @Test
    void shouldLogCrossCuttingOperation() {
        String operation = "BULK_UPDATE";
        String entityType = "AssetWarranty";
        Long entityId = 1L;
        Map<String, Object> details = new HashMap<>();
        details.put("operation", "bulk placeholder attachment");
        details.put("count", 5);
        String userId = "testUser";

        auditTrailService.logCrossCuttingOperation(operation, entityType, entityId, details, userId);
    }
}
