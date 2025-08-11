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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DomainEventStoreImpl implements DomainEventStore {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public DomainEvent store(DomainEvent event) {
        if (event instanceof AbstractDomainEvent) {
            entityManager.persist(event);
            return event;
        }
        throw new IllegalArgumentException("Event must extend AbstractDomainEvent for persistence");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DomainEvent> findByEventId(UUID eventId) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.eventId = :eventId", AbstractDomainEvent.class);
        query.setParameter("eventId", eventId);
        
        List<AbstractDomainEvent> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findByAggregateId(String aggregateId) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.aggregateId = :aggregateId ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        query.setParameter("aggregateId", aggregateId);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findByEventType(String eventType) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.eventType = :eventType ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        query.setParameter("eventType", eventType);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findUnprocessedEvents() {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.processed = false ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findEventsSince(Instant since) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.occurredOn >= :since ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        query.setParameter("since", since);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    public void markAsProcessed(UUID eventId) {
        entityManager.createQuery(
            "UPDATE AbstractDomainEvent e SET e.processed = true WHERE e.eventId = :eventId")
            .setParameter("eventId", eventId)
            .executeUpdate();
    }

    @Override
    public void incrementRetryCount(UUID eventId) {
        entityManager.createQuery(
            "UPDATE AbstractDomainEvent e SET e.retryCount = e.retryCount + 1 WHERE e.eventId = :eventId")
            .setParameter("eventId", eventId)
            .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findEventsForReplay(String aggregateId, Instant fromTime) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.aggregateId = :aggregateId AND e.occurredOn >= :fromTime ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        query.setParameter("aggregateId", aggregateId);
        query.setParameter("fromTime", fromTime);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findAuditTrailEvents(String entityType, String entityId, Instant fromDate, Instant toDate) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.aggregateType = :entityType AND e.aggregateId = :entityId " +
            "AND e.occurredOn >= :fromDate AND e.occurredOn <= :toDate ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        query.setParameter("entityType", entityType);
        query.setParameter("entityId", entityId);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findComplianceAuditEvents(Instant fromDate, Instant toDate) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.occurredOn >= :fromDate AND e.occurredOn <= :toDate " +
            "AND (e.eventType LIKE '%Created%' OR e.eventType LIKE '%Updated%' OR e.eventType LIKE '%Deleted%' " +
            "OR e.eventType LIKE '%Processed%' OR e.eventType LIKE '%Settled%') ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomainEvent> findEventsByCorrelationId(UUID correlationId) {
        TypedQuery<AbstractDomainEvent> query = entityManager.createQuery(
            "SELECT e FROM AbstractDomainEvent e WHERE e.correlationId = :correlationId ORDER BY e.occurredOn", 
            AbstractDomainEvent.class);
        query.setParameter("correlationId", correlationId);
        return (List<DomainEvent>) (List<?>) query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getAuditEventSummary(Instant fromDate, Instant toDate) {
        TypedQuery<Object[]> query = entityManager.createQuery(
            "SELECT e.eventType, COUNT(e) FROM AbstractDomainEvent e WHERE e.occurredOn >= :fromDate " +
            "AND e.occurredOn <= :toDate GROUP BY e.eventType", Object[].class);
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        
        Map<String, Long> summary = new HashMap<>();
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
            summary.put((String) result[0], (Long) result[1]);
        }
        return summary;
    }

    @Override
    @Transactional(readOnly = true)
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM AbstractDomainEvent e", Long.class);
        return query.getSingleResult();
    }
}
