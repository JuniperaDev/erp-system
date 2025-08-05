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

import io.github.erp.ErpSystemApp;
import io.github.erp.domain.events.asset.AssetCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ErpSystemApp.class)
@TestPropertySource(properties = {
    "spring.liquibase.contexts=test"
})
class DomainEventIntegrationTest {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private DomainEventStore eventStore;

    @Test
    @Transactional
    void shouldPublishAndStoreEventSuccessfully() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Integration Test Asset", 
            BigDecimal.valueOf(15000), 1L, UUID.randomUUID());

        domainEventPublisher.publish(event);

        var storedEvent = eventStore.findByEventId(event.getEventId());
        assertThat(storedEvent).isPresent();
        assertThat(storedEvent.get().getEventType()).isEqualTo("AssetCreatedEvent");
        assertThat(storedEvent.get().getAggregateId()).isEqualTo("AST-001");
    }

    @Test
    @Transactional
    void shouldFindEventsByAggregateId() {
        AssetCreatedEvent event1 = new AssetCreatedEvent(
            "AST-002", "AST-002", "Test Asset 1", 
            BigDecimal.valueOf(10000), 1L, UUID.randomUUID());
        
        AssetCreatedEvent event2 = new AssetCreatedEvent(
            "AST-002", "AST-002-V2", "Test Asset 2", 
            BigDecimal.valueOf(12000), 1L, UUID.randomUUID());

        domainEventPublisher.publish(event1);
        domainEventPublisher.publish(event2);

        var events = eventStore.findByAggregateId("AST-002");
        assertThat(events).hasSize(2);
    }

    @Test
    @Transactional
    void shouldMarkEventAsProcessed() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-003", "AST-003", "Test Asset", 
            BigDecimal.valueOf(8000), 1L, UUID.randomUUID());

        domainEventPublisher.publish(event);
        eventStore.markAsProcessed(event.getEventId());

        var storedEvent = eventStore.findByEventId(event.getEventId());
        assertThat(storedEvent).isPresent();
    }
}
