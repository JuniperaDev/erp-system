package io.github.erp.domain.events.asset;

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
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.DomainEventStore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class AssetDomainEventsIntegrationTest {

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private DomainEventStore eventStore;

    @Test
    @Transactional
    @DisplayName("Should publish and store AssetCreatedEvent successfully")
    void shouldPublishAndStoreAssetCreatedEventSuccessfully() {
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
    @DisplayName("Should publish and store AssetCategoryChangedEvent successfully")
    void shouldPublishAndStoreAssetCategoryChangedEventSuccessfully() {
        AssetCategoryChangedEvent event = new AssetCategoryChangedEvent(
            "AST-002", "AST-002", 1L, 2L, "Furniture", "Equipment", UUID.randomUUID());

        domainEventPublisher.publish(event);

        var storedEvent = eventStore.findByEventId(event.getEventId());
        assertThat(storedEvent).isPresent();
        assertThat(storedEvent.get().getEventType()).isEqualTo("AssetCategoryChangedEvent");
        assertThat(storedEvent.get().getAggregateId()).isEqualTo("AST-002");
    }

    @Test
    @Transactional
    @DisplayName("Should publish and store AssetDisposedEvent successfully")
    void shouldPublishAndStoreAssetDisposedEventSuccessfully() {
        AssetDisposedEvent event = new AssetDisposedEvent(
            "AST-003", "AST-003", "Disposed Asset", LocalDate.now(),
            BigDecimal.valueOf(5000), BigDecimal.valueOf(3000), "DISP-001", UUID.randomUUID());

        domainEventPublisher.publish(event);

        var storedEvent = eventStore.findByEventId(event.getEventId());
        assertThat(storedEvent).isPresent();
        assertThat(storedEvent.get().getEventType()).isEqualTo("AssetDisposedEvent");
        assertThat(storedEvent.get().getAggregateId()).isEqualTo("AST-003");
    }

    @Test
    @Transactional
    @DisplayName("Should publish and store AssetRevaluedEvent successfully")
    void shouldPublishAndStoreAssetRevaluedEventSuccessfully() {
        AssetRevaluedEvent event = new AssetRevaluedEvent(
            "AST-004", "AST-004", "Revalued Asset", LocalDate.now(),
            BigDecimal.valueOf(10000), BigDecimal.valueOf(12000), "REV-001", UUID.randomUUID());

        domainEventPublisher.publish(event);

        var storedEvent = eventStore.findByEventId(event.getEventId());
        assertThat(storedEvent).isPresent();
        assertThat(storedEvent.get().getEventType()).isEqualTo("AssetRevaluedEvent");
        assertThat(storedEvent.get().getAggregateId()).isEqualTo("AST-004");
    }

    @Test
    @Transactional
    @DisplayName("Should handle multiple asset events in sequence")
    void shouldHandleMultipleAssetEventsInSequence() {
        AssetCreatedEvent createdEvent = new AssetCreatedEvent(
            "AST-005", "AST-005", "Multi-Event Asset", 
            BigDecimal.valueOf(20000), 1L, UUID.randomUUID());

        AssetCategoryChangedEvent categoryEvent = new AssetCategoryChangedEvent(
            "AST-005", "AST-005", 1L, 2L, "Furniture", "Equipment", UUID.randomUUID());

        AssetRevaluedEvent revaluedEvent = new AssetRevaluedEvent(
            "AST-005", "AST-005", "Multi-Event Asset", LocalDate.now(),
            BigDecimal.valueOf(20000), BigDecimal.valueOf(25000), "REV-002", UUID.randomUUID());

        domainEventPublisher.publish(createdEvent);
        domainEventPublisher.publish(categoryEvent);
        domainEventPublisher.publish(revaluedEvent);

        var events = eventStore.findByAggregateId("AST-005");
        assertThat(events).hasSize(3);
        assertThat(events).extracting("eventType")
            .containsExactlyInAnyOrder("AssetCreatedEvent", "AssetCategoryChangedEvent", "AssetRevaluedEvent");
    }
}
