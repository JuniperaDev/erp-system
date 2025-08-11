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

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.DetailedLeaseContract;
import io.github.erp.repository.DetailedLeaseContractRepository;
import io.github.erp.repository.search.DetailedLeaseContractSearchRepository;
import io.github.erp.service.criteria.DetailedLeaseContractCriteria;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
import io.github.erp.service.mapper.DetailedLeaseContractMapper;
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
 * Service for executing complex queries for {@link DetailedLeaseContract} entities in the database.
 * The main input is a {@link DetailedLeaseContractCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DetailedLeaseContractDTO} or a {@link Page} of {@link DetailedLeaseContractDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DetailedLeaseContractQueryService extends QueryService<DetailedLeaseContract> {

    private final Logger log = LoggerFactory.getLogger(DetailedLeaseContractQueryService.class);

    private final DetailedLeaseContractRepository detailedLeaseContractRepository;

    private final DetailedLeaseContractMapper detailedLeaseContractMapper;

    private final DetailedLeaseContractSearchRepository detailedLeaseContractSearchRepository;

    public DetailedLeaseContractQueryService(
        DetailedLeaseContractRepository detailedLeaseContractRepository,
        DetailedLeaseContractMapper detailedLeaseContractMapper,
        DetailedLeaseContractSearchRepository detailedLeaseContractSearchRepository
    ) {
        this.detailedLeaseContractRepository = detailedLeaseContractRepository;
        this.detailedLeaseContractMapper = detailedLeaseContractMapper;
        this.detailedLeaseContractSearchRepository = detailedLeaseContractSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DetailedLeaseContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DetailedLeaseContractDTO> findByCriteria(DetailedLeaseContractCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DetailedLeaseContract> specification = createSpecification(criteria);
        return detailedLeaseContractMapper.toDto(detailedLeaseContractRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DetailedLeaseContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DetailedLeaseContractDTO> findByCriteria(DetailedLeaseContractCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DetailedLeaseContract> specification = createSpecification(criteria);
        return detailedLeaseContractRepository.findAll(specification, page).map(detailedLeaseContractMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DetailedLeaseContractCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DetailedLeaseContract> specification = createSpecification(criteria);
        return detailedLeaseContractRepository.count(specification);
    }

    /**
     * Function to convert {@link DetailedLeaseContractCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DetailedLeaseContract> createSpecification(DetailedLeaseContractCriteria criteria) {
        Specification<DetailedLeaseContract> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DetailedLeaseContract_.id));
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBookingId(), DetailedLeaseContract_.bookingId));
            }
            if (criteria.getLeaseTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseTitle(), DetailedLeaseContract_.leaseTitle));
            }
            if (criteria.getShortTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortTitle(), DetailedLeaseContract_.shortTitle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DetailedLeaseContract_.description));
            }
            if (criteria.getInceptionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInceptionDate(), DetailedLeaseContract_.inceptionDate));
            }
            if (criteria.getCommencementDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCommencementDate(), DetailedLeaseContract_.commencementDate));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildSpecification(criteria.getSerialNumber(), DetailedLeaseContract_.serialNumber));
            }
            if (criteria.getSuperintendentServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperintendentServiceOutletId(),
                            root -> root.join(DetailedLeaseContract_.superintendentServiceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getMainDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMainDealerId(),
                            root -> root.join(DetailedLeaseContract_.mainDealer, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getFirstReportingPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFirstReportingPeriodId(),
                            root -> root.join(DetailedLeaseContract_.firstReportingPeriod, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getLastReportingPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastReportingPeriodId(),
                            root -> root.join(DetailedLeaseContract_.lastReportingPeriod, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractDocumentId(),
                            root -> root.join(DetailedLeaseContract_.leaseContractDocument, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractCalculationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractCalculationsId(),
                            root -> root.join(DetailedLeaseContract_.leaseContractCalculations, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getLeasePaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeasePaymentId(),
                            root -> root.join(DetailedLeaseContract_.leasePayments, JoinType.LEFT).get(LeasePayment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
