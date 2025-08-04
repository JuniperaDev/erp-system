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

import io.github.erp.domain.*;
import io.github.erp.domain.DealerGroup;
import io.github.erp.repository.DealerGroupRepository;
import io.github.erp.service.criteria.DealerGroupCriteria;
import io.github.erp.service.dto.DealerGroupDTO;
import io.github.erp.service.mapper.DealerGroupMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link DealerGroup} entities in the database.
 * The main input is a {@link DealerGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DealerGroupDTO} or a {@link Page} of {@link DealerGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DealerGroupQueryService extends QueryService<DealerGroup> {

    private final Logger log = LoggerFactory.getLogger(DealerGroupQueryService.class);

    private final DealerGroupRepository dealerGroupRepository;

    private final DealerGroupMapper dealerGroupMapper;

    public DealerGroupQueryService(DealerGroupRepository dealerGroupRepository, DealerGroupMapper dealerGroupMapper) {
        this.dealerGroupRepository = dealerGroupRepository;
        this.dealerGroupMapper = dealerGroupMapper;
    }

    /**
     * Return a {@link List} of {@link DealerGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DealerGroupDTO> findByCriteria(DealerGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DealerGroup> specification = createSpecification(criteria);
        return dealerGroupMapper.toDto(dealerGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DealerGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DealerGroupDTO> findByCriteria(DealerGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DealerGroup> specification = createSpecification(criteria);
        return dealerGroupRepository.findAll(specification, page).map(dealerGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DealerGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DealerGroup> specification = createSpecification(criteria);
        return dealerGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link DealerGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DealerGroup> createSpecification(DealerGroupCriteria criteria) {
        Specification<DealerGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DealerGroup_.id));
            }
            if (criteria.getGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupName(), DealerGroup_.groupName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DealerGroup_.description));
            }
            if (criteria.getParentGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentGroupId(),
                            root -> root.join(DealerGroup_.parentGroup, JoinType.LEFT).get(DealerGroup_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(DealerGroup_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
