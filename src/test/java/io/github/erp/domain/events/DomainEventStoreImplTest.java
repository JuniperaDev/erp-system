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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainEventStoreImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<AbstractDomainEvent> query;

    private DomainEventStoreImpl eventStore;

    @BeforeEach
    void setUp() {
        eventStore = new DomainEventStoreImpl();
        eventStore.entityManager = entityManager;
    }

    @Test
    @DisplayName("Should store domain event successfully")
    void shouldStoreDomainEventSuccessfully() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        DomainEvent result = eventStore.store(event);

        verify(entityManager).persist(event);
        assertThat(result).isEqualTo(event);
    }

    @Test
    @DisplayName("Should throw exception for non-AbstractDomainEvent")
    void shouldThrowExceptionForNonAbstractDomainEvent() {
        DomainEvent event = mock(DomainEvent.class);

        assertThrows(IllegalArgumentException.class, () -> eventStore.store(event));
    }

    @Test
    @DisplayName("Should find event by event ID")
    void shouldFindEventByEventId() {
        UUID eventId = UUID.randomUUID();
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        when(entityManager.createQuery(anyString(), eq(AbstractDomainEvent.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(event));

        Optional<DomainEvent> result = eventStore.findByEventId(eventId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(event);
    }

    @Test
    @DisplayName("Should find events by aggregate ID")
    void shouldFindEventsByAggregateId() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "AST-001", "AST-001", "Test Asset", BigDecimal.valueOf(10000), 1L, UUID.randomUUID());

        when(entityManager.createQuery(anyString(), eq(AbstractDomainEvent.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Arrays.asList(event));

        List<DomainEvent> result = eventStore.findByAggregateId("AST-001");

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(event);
    }

    @Test
    @DisplayName("Should mark event as processed")
    void shouldMarkEventAsProcessed() {
        UUID eventId = UUID.randomUUID();
        javax.persistence.Query mockQuery = mock(javax.persistence.Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);

        eventStore.markAsProcessed(eventId);

        verify(entityManager).createQuery(contains("UPDATE"));
        verify(mockQuery).setParameter("eventId", eventId);
        verify(mockQuery).executeUpdate();
    }

    @Test
    @DisplayName("Should increment retry count")
    void shouldIncrementRetryCount() {
        UUID eventId = UUID.randomUUID();
        javax.persistence.Query mockQuery = mock(javax.persistence.Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);

        eventStore.incrementRetryCount(eventId);

        verify(entityManager).createQuery(contains("UPDATE"));
        verify(mockQuery).setParameter("eventId", eventId);
        verify(mockQuery).executeUpdate();
    }
}
