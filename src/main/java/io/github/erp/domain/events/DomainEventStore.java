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

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DomainEventStore {
    
    DomainEvent store(DomainEvent event);
    
    Optional<DomainEvent> findByEventId(UUID eventId);
    
    List<DomainEvent> findByAggregateId(String aggregateId);
    
    List<DomainEvent> findByEventType(String eventType);
    
    List<DomainEvent> findUnprocessedEvents();
    
    List<DomainEvent> findEventsSince(Instant since);
    
    void markAsProcessed(UUID eventId);
    
    void incrementRetryCount(UUID eventId);
    
    List<DomainEvent> findEventsForReplay(String aggregateId, Instant fromTime);
}
