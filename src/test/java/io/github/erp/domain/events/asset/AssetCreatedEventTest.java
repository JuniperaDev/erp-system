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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AssetCreatedEventTest {

    @Test
    @DisplayName("Should create asset created event with all properties")
    void shouldCreateAssetCreatedEventWithAllProperties() {
        UUID correlationId = UUID.randomUUID();
        String assetId = "AST-001";
        String registrationNumber = "AST-001";
        String description = "Test Asset";
        BigDecimal cost = BigDecimal.valueOf(10000);
        Long categoryId = 1L;

        AssetCreatedEvent event = new AssetCreatedEvent(
            assetId, registrationNumber, description, cost, categoryId, correlationId);

        assertThat(event.getAggregateId()).isEqualTo(assetId);
        assertThat(event.getAggregateType()).isEqualTo("Asset");
        assertThat(event.getCorrelationId()).isEqualTo(correlationId);
        assertThat(event.getAssetRegistrationNumber()).isEqualTo(registrationNumber);
        assertThat(event.getAssetDescription()).isEqualTo(description);
        assertThat(event.getAssetCost()).isEqualTo(cost);
        assertThat(event.getAssetCategoryId()).isEqualTo(categoryId);
        assertThat(event.getEventType()).isEqualTo("AssetCreatedEvent");
        assertThat(event.getVersion()).isEqualTo(1);
        assertThat(event.getEventId()).isNotNull();
        assertThat(event.getOccurredOn()).isNotNull();
    }

    @Test
    @DisplayName("Should have unique event ID for each instance")
    void shouldHaveUniqueEventIdForEachInstance() {
        AssetCreatedEvent event1 = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset 1", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());
        
        AssetCreatedEvent event2 = new AssetCreatedEvent(
            "AST-002", "AST-002", "Test Asset 2", BigDecimal.valueOf(20000), 2L, UUID.randomUUID());

        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }

    @Test
    @DisplayName("Should set processed to false by default")
    void shouldSetProcessedToFalseByDefault() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        assertThat(event.getProcessed()).isFalse();
        assertThat(event.getRetryCount()).isEqualTo(0);
    }
}
