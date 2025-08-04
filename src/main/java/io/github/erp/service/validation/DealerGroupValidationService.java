package io.github.erp.service.validation;

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

import io.github.erp.domain.DealerGroup;
import io.github.erp.repository.DealerGroupRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * Service for validating DealerGroup hierarchy to prevent circular dependencies.
 */
@Service
public class DealerGroupValidationService {

    private static final int MAX_HIERARCHY_DEPTH = 5;

    private final DealerGroupRepository dealerGroupRepository;

    public DealerGroupValidationService(DealerGroupRepository dealerGroupRepository) {
        this.dealerGroupRepository = dealerGroupRepository;
    }

    /**
     * Validates that setting the parent group would not create a circular dependency.
     *
     * @param dealerGroupId the ID of the dealer group
     * @param parentGroupId the ID of the proposed parent group
     * @return true if the relationship is valid, false if it would create a circular dependency
     */
    public boolean isValidParentRelationship(Long dealerGroupId, Long parentGroupId) {
        if (dealerGroupId == null || parentGroupId == null) {
            return true;
        }

        if (dealerGroupId.equals(parentGroupId)) {
            return false;
        }

        Set<Long> visitedGroups = new HashSet<>();
        return !wouldCreateCircularDependency(dealerGroupId, parentGroupId, visitedGroups);
    }

    /**
     * Validates that the hierarchy depth does not exceed the maximum allowed depth.
     *
     * @param dealerGroupId the ID of the dealer group
     * @param parentGroupId the ID of the proposed parent group
     * @return true if the depth is within limits, false otherwise
     */
    public boolean isValidHierarchyDepth(Long dealerGroupId, Long parentGroupId) {
        if (parentGroupId == null) {
            return true;
        }

        int depth = calculateHierarchyDepth(parentGroupId);
        return depth < MAX_HIERARCHY_DEPTH;
    }

    private boolean wouldCreateCircularDependency(Long dealerGroupId, Long parentGroupId, Set<Long> visitedGroups) {
        if (visitedGroups.contains(parentGroupId)) {
            return true;
        }

        visitedGroups.add(parentGroupId);

        DealerGroup parentGroup = dealerGroupRepository.findById(parentGroupId).orElse(null);
        if (parentGroup == null || parentGroup.getParentGroup() == null) {
            return false;
        }

        Long grandParentId = parentGroup.getParentGroup().getId();
        if (dealerGroupId.equals(grandParentId)) {
            return true;
        }

        return wouldCreateCircularDependency(dealerGroupId, grandParentId, visitedGroups);
    }

    private int calculateHierarchyDepth(Long parentGroupId) {
        int depth = 0;
        Long currentParentId = parentGroupId;

        while (currentParentId != null && depth < MAX_HIERARCHY_DEPTH + 1) {
            DealerGroup parentGroup = dealerGroupRepository.findById(currentParentId).orElse(null);
            if (parentGroup == null || parentGroup.getParentGroup() == null) {
                break;
            }
            currentParentId = parentGroup.getParentGroup().getId();
            depth++;
        }

        return depth;
    }
}
