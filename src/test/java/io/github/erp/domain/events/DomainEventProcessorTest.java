package io.github.erp.domain.events;

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

import io.github.erp.domain.events.asset.AssetCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainEventProcessorTest {

    @Mock
    private DomainEventStore eventStore;

    @Mock
    private DomainEventErrorHandler errorHandler;

    @Mock
    private Acknowledgment acknowledgment;

    private DomainEventProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new DomainEventProcessor(eventStore, errorHandler);
    }

    @Test
    @DisplayName("Should process local event successfully")
    void shouldProcessLocalEventSuccessfully() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        processor.handleLocalEvent(event);

        verify(eventStore).markAsProcessed(event.getEventId());
    }

    @Test
    @DisplayName("Should process distributed event and acknowledge")
    void shouldProcessDistributedEventAndAcknowledge() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        processor.handleDistributedEvent(event, acknowledgment);

        verify(eventStore).markAsProcessed(event.getEventId());
        verify(acknowledgment).acknowledge();
    }

    @Test
    @DisplayName("Should handle error during distributed event processing")
    void shouldHandleErrorDuringDistributedEventProcessing() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        doThrow(new RuntimeException("Processing error")).when(eventStore).markAsProcessed(any());

        processor.handleDistributedEvent(event, acknowledgment);

        verify(errorHandler).handleError(eq(event), any(RuntimeException.class));
        verify(acknowledgment, never()).acknowledge();
    }

    @Test
    @DisplayName("Should register event handler")
    void shouldRegisterEventHandler() throws Exception {
        Object handler = new Object();
        DomainEventHandlerMethodWrapper handlerMethod = new DomainEventHandlerMethodWrapper(
            handler, handler.getClass().getMethod("toString"), "AssetCreatedEvent", 1);

        processor.registerHandler("AssetCreatedEvent", handlerMethod);

        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        processor.handleLocalEvent(event);

        verify(eventStore).markAsProcessed(event.getEventId());
    }

    @Test
    @DisplayName("Should make processEvent method public for retry mechanism")
    void shouldMakeProcessEventPublicForRetryMechanism() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        processor.processEvent(event);

        verify(eventStore).markAsProcessed(event.getEventId());
    }
}
