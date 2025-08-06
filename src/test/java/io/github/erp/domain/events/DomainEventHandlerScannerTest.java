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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainEventHandlerScannerTest {

    @Mock
    private DomainEventProcessor domainEventProcessor;

    private DomainEventHandlerScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new DomainEventHandlerScanner(domainEventProcessor);
    }

    @Test
    @DisplayName("Should register handler methods automatically")
    void shouldRegisterHandlerMethodsAutomatically() {
        TestEventHandler handler = new TestEventHandler();

        scanner.postProcessAfterInitialization(handler, "testHandler");

        verify(domainEventProcessor).registerHandler(eq("TestEvent"), any(DomainEventHandlerMethodWrapper.class));
    }

    @Test
    @DisplayName("Should ignore beans without DomainEventHandler annotation")
    void shouldIgnoreBeansWithoutAnnotation() {
        Object regularBean = new Object();

        scanner.postProcessAfterInitialization(regularBean, "regularBean");

        verifyNoInteractions(domainEventProcessor);
    }

    @DomainEventHandler
    static class TestEventHandler {
        
        @DomainEventHandlerMethodAnnotation(eventType = "TestEvent")
        public void handleTestEvent(DomainEvent event) {
        }
    }
}
