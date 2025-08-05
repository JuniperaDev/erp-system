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

import io.github.erp.service.dto.BusinessDocumentDTO;
import java.util.Set;

/**
 * Service Interface for managing document attachments across entities.
 */
public interface DocumentAttachmentService {
    
    /**
     * Attach documents to an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @param documents the documents to attach
     * @return the updated set of attached documents
     */
    Set<BusinessDocumentDTO> attachDocuments(Long entityId, String entityType, Set<BusinessDocumentDTO> documents);
    
    /**
     * Detach documents from an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @param documentIds the document IDs to detach
     * @return the updated set of attached documents
     */
    Set<BusinessDocumentDTO> detachDocuments(Long entityId, String entityType, Set<Long> documentIds);
    
    /**
     * Get all documents attached to an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @return the set of attached documents
     */
    Set<BusinessDocumentDTO> getAttachedDocuments(Long entityId, String entityType);
    
    /**
     * Validate document attachment permissions.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @param documentId the document ID
     * @return true if attachment is allowed
     */
    boolean canAttachDocument(Long entityId, String entityType, Long documentId);
    
    /**
     * Add a single document attachment to an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @param document the document to attach
     * @return the attached document
     */
    BusinessDocumentDTO addDocumentAttachment(Long entityId, String entityType, BusinessDocumentDTO document);
    
    /**
     * Remove a single document attachment from an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @param documentId the document ID to remove
     * @return true if removal was successful
     */
    boolean removeDocumentAttachment(Long entityId, String entityType, Long documentId);
}
