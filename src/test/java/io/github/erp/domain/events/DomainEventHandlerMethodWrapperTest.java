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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DomainEventHandlerMethodWrapperTest {

    @Test
    @DisplayName("Should invoke handler method successfully")
    void shouldInvokeHandlerMethodSuccessfully() throws Exception {
        TestHandler handler = new TestHandler();
        Method method = TestHandler.class.getDeclaredMethod("handleEvent", DomainEvent.class);
        
        DomainEventHandlerMethodWrapper wrapper = new DomainEventHandlerMethodWrapper(
            handler, method, "TestEvent", 1);

        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        wrapper.handle(event);

        assertThat(handler.wasInvoked()).isTrue();
        assertThat(wrapper.getEventType()).isEqualTo("TestEvent");
        assertThat(wrapper.getOrder()).isEqualTo(1);
        assertThat(wrapper.getMethodName()).isEqualTo("handleEvent");
    }

    @Test
    @DisplayName("Should throw exception when handler method fails")
    void shouldThrowExceptionWhenHandlerMethodFails() throws Exception {
        TestHandler handler = new TestHandler();
        Method method = TestHandler.class.getDeclaredMethod("failingHandler", DomainEvent.class);
        
        DomainEventHandlerMethodWrapper wrapper = new DomainEventHandlerMethodWrapper(
            handler, method, "TestEvent", 1);

        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        assertThrows(RuntimeException.class, () -> wrapper.handle(event));
    }

    static class TestHandler {
        private boolean invoked = false;

        public void handleEvent(DomainEvent event) {
            this.invoked = true;
        }

        public void failingHandler(DomainEvent event) {
            throw new RuntimeException("Handler failed");
        }

        public boolean wasInvoked() {
            return invoked;
        }
    }
}
