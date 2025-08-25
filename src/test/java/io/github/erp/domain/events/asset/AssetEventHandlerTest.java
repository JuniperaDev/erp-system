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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class AssetEventHandlerTest {

    private LegacyAssetEventHandler assetEventHandler;

    @BeforeEach
    void setUp() {
        assetEventHandler = new LegacyAssetEventHandler();
    }

    @Test
    @DisplayName("Should handle AssetCreatedEvent correctly")
    void shouldHandleAssetCreatedEventCorrectly() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        assertDoesNotThrow(() -> assetEventHandler.handleAssetCreated(event));
    }

    @Test
    @DisplayName("Should handle AssetCategoryChangedEvent correctly")
    void shouldHandleAssetCategoryChangedEventCorrectly() {
        AssetCategoryChangedEvent event = new AssetCategoryChangedEvent(
            "AST-002", "AST-002", 1L, 2L, "Furniture", "Equipment", UUID.randomUUID());

        assertDoesNotThrow(() -> assetEventHandler.handleAssetCategoryChanged(event));
    }

    @Test
    @DisplayName("Should handle AssetDisposedEvent correctly")
    void shouldHandleAssetDisposedEventCorrectly() {
        AssetDisposedEvent event = new AssetDisposedEvent(
            "AST-003", "AST-003", "Disposed Asset", LocalDate.now(),
            BigDecimal.valueOf(5000), BigDecimal.valueOf(3000), "DISP-001", UUID.randomUUID());

        assertDoesNotThrow(() -> assetEventHandler.handleAssetDisposed(event));
    }

    @Test
    @DisplayName("Should handle AssetRevaluedEvent correctly")
    void shouldHandleAssetRevaluedEventCorrectly() {
        AssetRevaluedEvent event = new AssetRevaluedEvent(
            "AST-004", "AST-004", "Revalued Asset", LocalDate.now(),
            BigDecimal.valueOf(10000), BigDecimal.valueOf(12000), "REV-001", UUID.randomUUID());

        assertDoesNotThrow(() -> assetEventHandler.handleAssetRevalued(event));
    }
}
