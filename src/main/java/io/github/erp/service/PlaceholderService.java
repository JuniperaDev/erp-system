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

import io.github.erp.service.dto.PlaceholderDTO;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.domain.Placeholder}.
 */
public interface PlaceholderService {
    /**
     * Save a placeholder.
     *
     * @param placeholderDTO the entity to save.
     * @return the persisted entity.
     */
    PlaceholderDTO save(PlaceholderDTO placeholderDTO);

    /**
     * Partially updates a placeholder.
     *
     * @param placeholderDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlaceholderDTO> partialUpdate(PlaceholderDTO placeholderDTO);

    /**
     * Get all the placeholders.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaceholderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" placeholder.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlaceholderDTO> findOne(Long id);

    /**
     * Delete the "id" placeholder.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the placeholder corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaceholderDTO> search(String query, Pageable pageable);
    
    /**
     * Attach placeholders to an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @param placeholders the placeholders to attach
     * @return the updated set of attached placeholders
     */
    Set<PlaceholderDTO> attachPlaceholders(Long entityId, String entityType, Set<PlaceholderDTO> placeholders);
    
    /**
     * Detach placeholders from an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @param placeholderIds the placeholder IDs to detach
     * @return the updated set of attached placeholders
     */
    Set<PlaceholderDTO> detachPlaceholders(Long entityId, String entityType, Set<Long> placeholderIds);
    
    /**
     * Get all placeholders attached to an entity.
     *
     * @param entityId the entity ID
     * @param entityType the entity type
     * @return the set of attached placeholders
     */
    Set<PlaceholderDTO> getAttachedPlaceholders(Long entityId, String entityType);
    
    /**
     * Find placeholders by token pattern.
     *
     * @param tokenPattern the token pattern to search
     * @return matching placeholders
     */
    Page<PlaceholderDTO> findByTokenPattern(String tokenPattern, Pageable pageable);
    
    /**
     * Bulk create placeholders from templates.
     *
     * @param templates the placeholder templates
     * @return created placeholders
     */
    Set<PlaceholderDTO> createFromTemplates(Set<PlaceholderDTO> templates);
}
