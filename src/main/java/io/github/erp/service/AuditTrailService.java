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

import java.time.Instant;
import java.util.Map;

/**
 * Service Interface for managing audit trails and change tracking.
 */
public interface AuditTrailService {
    
    /**
     * Log a business event for audit purposes.
     *
     * @param entityType the entity type
     * @param entityId the entity ID
     * @param eventType the event type (CREATE, UPDATE, DELETE, etc.)
     * @param changes the changes made
     * @param userId the user who made the change
     */
    void logBusinessEvent(String entityType, Long entityId, String eventType, 
                         Map<String, Object> changes, String userId);
    
    /**
     * Log document attachment/detachment events.
     *
     * @param entityType the entity type
     * @param entityId the entity ID
     * @param documentId the document ID
     * @param action ATTACH or DETACH
     * @param userId the user who performed the action
     */
    void logDocumentEvent(String entityType, Long entityId, Long documentId, 
                         String action, String userId);
    
    /**
     * Log placeholder management events.
     *
     * @param entityType the entity type
     * @param entityId the entity ID
     * @param placeholderId the placeholder ID
     * @param action ATTACH or DETACH
     * @param userId the user who performed the action
     */
    void logPlaceholderEvent(String entityType, Long entityId, Long placeholderId, 
                           String action, String userId);
    
    /**
     * Get audit trail for an entity.
     *
     * @param entityType the entity type
     * @param entityId the entity ID
     * @param fromDate the start date
     * @param toDate the end date
     * @return audit trail entries
     */
    Map<String, Object> getAuditTrail(String entityType, Long entityId, 
                                     Instant fromDate, Instant toDate);
    
    /**
     * Log cross-cutting concern operations.
     *
     * @param operation the operation performed
     * @param entityType the entity type
     * @param entityId the entity ID
     * @param details additional operation details
     * @param userId the user who performed the operation
     */
    void logCrossCuttingOperation(String operation, String entityType, Long entityId, 
                                 Map<String, Object> details, String userId);
}
