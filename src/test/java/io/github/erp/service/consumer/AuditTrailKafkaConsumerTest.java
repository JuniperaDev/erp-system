package io.github.erp.service.consumer;

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
import io.github.erp.service.EventSourcingAuditTrailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditTrailKafkaConsumerTest {

    @Mock
    private EventSourcingAuditTrailService eventSourcingAuditTrailService;

    @Mock
    private Acknowledgment acknowledgment;

    private AuditTrailKafkaConsumer auditTrailKafkaConsumer;

    @BeforeEach
    void setUp() {
        auditTrailKafkaConsumer = new AuditTrailKafkaConsumer(eventSourcingAuditTrailService);
    }

    @Test
    void shouldConsumeBusinessEvents() {
        AuditTrailEvent event = createTestEvent("CREATE");
        
        auditTrailKafkaConsumer.consumeBusinessEvents(event, "business-events-topic", 0, 123L, acknowledgment);
        
        verify(eventSourcingAuditTrailService).validateAndPersistEvent(event);
        verify(acknowledgment).acknowledge();
    }

    @Test
    void shouldConsumeSecurityEvents() {
        AuditTrailEvent event = createTestEvent("LOGIN");
        
        auditTrailKafkaConsumer.consumeSecurityEvents(event, "security-events-topic", 0, 123L, acknowledgment);
        
        verify(eventSourcingAuditTrailService).validateAndPersistEvent(event);
        verify(acknowledgment).acknowledge();
    }

    @Test
    void shouldConsumeSystemEvents() {
        AuditTrailEvent event = createTestEvent("SYSTEM_START");
        
        auditTrailKafkaConsumer.consumeSystemEvents(event, "system-events-topic", 0, 123L, acknowledgment);
        
        verify(eventSourcingAuditTrailService).validateAndPersistEvent(event);
        verify(acknowledgment).acknowledge();
    }

    @Test
    void shouldHandleProcessingError() {
        AuditTrailEvent event = createTestEvent("CREATE");
        doThrow(new RuntimeException("Processing error")).when(eventSourcingAuditTrailService).validateAndPersistEvent(any());
        
        try {
            auditTrailKafkaConsumer.consumeBusinessEvents(event, "business-events-topic", 0, 123L, acknowledgment);
        } catch (RuntimeException e) {
            verify(eventSourcingAuditTrailService).validateAndPersistEvent(event);
            verify(acknowledgment, never()).acknowledge();
        }
    }

    private AuditTrailEvent createTestEvent(String actionType) {
        return new AuditTrailEvent(
            UUID.randomUUID().toString(),
            "TEST_ENTITY",
            "test-user",
            actionType,
            "TestEntity",
            "{}",
            "{\"name\":\"test\"}",
            "192.168.1.1",
            "Mozilla/5.0",
            UUID.randomUUID()
        );
    }
}
