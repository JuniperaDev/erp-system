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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainEventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @Mock
    private KafkaTemplate<String, DomainEvent> kafkaTemplate;

    @Mock
    private DomainEventStore eventStore;

    private DomainEventPublisher domainEventPublisher;

    @BeforeEach
    void setUp() {
        domainEventPublisher = new DomainEventPublisher(applicationEventPublisher, kafkaTemplate, eventStore);
        ReflectionTestUtils.setField(domainEventPublisher, "domainEventsTopicName", "domain_events_topic");
    }

    @Test
    @DisplayName("Should publish domain event successfully")
    void shouldPublishDomainEventSuccessfully() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());
        
        when(eventStore.store(any(DomainEvent.class))).thenReturn(event);

        domainEventPublisher.publish(event);

        verify(eventStore).store(event);
        verify(applicationEventPublisher).publishEvent(event);
        verify(kafkaTemplate).send(eq("domain_events_topic"), eq("AST-001"), eq(event));
    }

    @Test
    @DisplayName("Should handle publishing errors gracefully")
    void shouldHandlePublishingErrorsGracefully() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());
        
        when(eventStore.store(any(DomainEvent.class))).thenThrow(new RuntimeException("Database error"));

        assertThrows(DomainEventPublishingException.class, () -> domainEventPublisher.publish(event));
    }

    @Test
    @DisplayName("Should store event before publishing")
    void shouldStoreEventBeforePublishing() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());
        
        when(eventStore.store(any(DomainEvent.class))).thenReturn(event);

        domainEventPublisher.publish(event);

        verify(eventStore).store(event);
        verify(applicationEventPublisher).publishEvent(event);
        verify(kafkaTemplate).send(any(String.class), any(String.class), any(DomainEvent.class));
    }
}
