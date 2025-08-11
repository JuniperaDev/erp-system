package io.github.erp.service;

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

import io.github.erp.domain.events.DomainEvent;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface EventSourcingAuditTrailService {
    
    List<DomainEvent> reconstructAuditTrail(String entityType, String entityId, Instant fromDate, Instant toDate);
    
    Map<String, Object> generateComplianceReport(Instant fromDate, Instant toDate);
    
    List<DomainEvent> replayEvents(String aggregateId, Instant fromTime);
    
    List<DomainEvent> findRelatedEvents(UUID correlationId);
    
    Map<String, Long> getAuditEventSummary(Instant fromDate, Instant toDate);
    
    Map<String, Object> reconstructEntityState(String entityType, String entityId, Instant asOfDate);
    
    List<Map<String, Object>> getAuditTrailTimeline(String entityType, String entityId, Instant fromDate, Instant toDate);
    
    boolean validateEventIntegrity(String aggregateId);
    
    Map<String, Object> exportAuditTrail(String entityType, String entityId, Instant fromDate, Instant toDate, String format);
}
