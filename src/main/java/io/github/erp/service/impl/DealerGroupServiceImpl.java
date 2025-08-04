<<<<<<< HEAD
package io.github.erp.service.impl;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.DealerGroup;
import io.github.erp.repository.DealerGroupRepository;
import io.github.erp.repository.search.DealerGroupSearchRepository;
import io.github.erp.service.DealerGroupService;
import io.github.erp.service.dto.DealerGroupDTO;
import io.github.erp.service.mapper.DealerGroupMapper;
import io.github.erp.service.validation.DealerGroupValidationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DealerGroup}.
 */
@Service
@Transactional
public class DealerGroupServiceImpl implements DealerGroupService {

    private final Logger log = LoggerFactory.getLogger(DealerGroupServiceImpl.class);

    private final DealerGroupRepository dealerGroupRepository;

    private final DealerGroupMapper dealerGroupMapper;

    private final DealerGroupSearchRepository dealerGroupSearchRepository;

    private final DealerGroupValidationService validationService;

    public DealerGroupServiceImpl(
        DealerGroupRepository dealerGroupRepository,
        DealerGroupMapper dealerGroupMapper,
        DealerGroupSearchRepository dealerGroupSearchRepository,
        DealerGroupValidationService validationService
    ) {
        this.dealerGroupRepository = dealerGroupRepository;
        this.dealerGroupMapper = dealerGroupMapper;
        this.dealerGroupSearchRepository = dealerGroupSearchRepository;
        this.validationService = validationService;
    }

    @Override
    public DealerGroupDTO save(DealerGroupDTO dealerGroupDTO) {
        log.debug("Request to save DealerGroup : {}", dealerGroupDTO);
        
        if (dealerGroupDTO.getParentGroup() != null) {
            if (!validationService.isValidParentRelationship(dealerGroupDTO.getId(), dealerGroupDTO.getParentGroup().getId())) {
                throw new IllegalArgumentException("Setting this parent group would create a circular dependency");
            }
            
            if (!validationService.isValidHierarchyDepth(dealerGroupDTO.getId(), dealerGroupDTO.getParentGroup().getId())) {
                throw new IllegalArgumentException("Hierarchy depth would exceed maximum allowed depth of 5 levels");
            }
        }
        
        DealerGroup dealerGroup = dealerGroupMapper.toEntity(dealerGroupDTO);
        dealerGroup = dealerGroupRepository.save(dealerGroup);
        DealerGroupDTO result = dealerGroupMapper.toDto(dealerGroup);
        dealerGroupSearchRepository.save(dealerGroup);
        return result;
    }

    @Override
    public Optional<DealerGroupDTO> partialUpdate(DealerGroupDTO dealerGroupDTO) {
        log.debug("Request to partially update DealerGroup : {}", dealerGroupDTO);

        if (dealerGroupDTO.getParentGroup() != null) {
            if (!validationService.isValidParentRelationship(dealerGroupDTO.getId(), dealerGroupDTO.getParentGroup().getId())) {
                throw new IllegalArgumentException("Setting this parent group would create a circular dependency");
            }
            
            if (!validationService.isValidHierarchyDepth(dealerGroupDTO.getId(), dealerGroupDTO.getParentGroup().getId())) {
                throw new IllegalArgumentException("Hierarchy depth would exceed maximum allowed depth of 5 levels");
            }
        }

        return dealerGroupRepository
            .findById(dealerGroupDTO.getId())
            .map(existingDealerGroup -> {
                dealerGroupMapper.partialUpdate(existingDealerGroup, dealerGroupDTO);

                return existingDealerGroup;
            })
            .map(dealerGroupRepository::save)
            .map(savedDealerGroup -> {
                dealerGroupSearchRepository.save(savedDealerGroup);

                return savedDealerGroup;
            })
            .map(dealerGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DealerGroups");
        return dealerGroupRepository.findAll(pageable).map(dealerGroupMapper::toDto);
    }

    public Page<DealerGroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dealerGroupRepository.findAllWithEagerRelationships(pageable).map(dealerGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DealerGroupDTO> findOne(Long id) {
        log.debug("Request to get DealerGroup : {}", id);
        return dealerGroupRepository.findOneWithEagerRelationships(id).map(dealerGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DealerGroup : {}", id);
        dealerGroupRepository.deleteById(id);
        dealerGroupSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DealerGroups for query {}", query);
        return dealerGroupSearchRepository.search(query, pageable).map(dealerGroupMapper::toDto);
    }
}
||||||| parent of 210929d30f (feat: Fix dealer self-references by creating DealerGroup entity)
=======
package io.github.erp.service.impl;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.DealerGroup;
import io.github.erp.repository.DealerGroupRepository;
import io.github.erp.repository.search.DealerGroupSearchRepository;
import io.github.erp.service.DealerGroupService;
import io.github.erp.service.dto.DealerGroupDTO;
import io.github.erp.service.mapper.DealerGroupMapper;
import io.github.erp.service.validation.DealerGroupValidationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DealerGroup}.
 */
@Service
@Transactional
public class DealerGroupServiceImpl implements DealerGroupService {

    private final Logger log = LoggerFactory.getLogger(DealerGroupServiceImpl.class);

    private final DealerGroupRepository dealerGroupRepository;

    private final DealerGroupMapper dealerGroupMapper;

    private final DealerGroupSearchRepository dealerGroupSearchRepository;

    public DealerGroupServiceImpl(
        DealerGroupRepository dealerGroupRepository,
        DealerGroupMapper dealerGroupMapper,
        DealerGroupSearchRepository dealerGroupSearchRepository
    ) {
        this.dealerGroupRepository = dealerGroupRepository;
        this.dealerGroupMapper = dealerGroupMapper;
        this.dealerGroupSearchRepository = dealerGroupSearchRepository;
    }

    @Override
    public DealerGroupDTO save(DealerGroupDTO dealerGroupDTO) {
        log.debug("Request to save DealerGroup : {}", dealerGroupDTO);
        
        if (dealerGroupDTO.getParentGroup() != null) {
            DealerGroupValidationService validationService = new DealerGroupValidationService(dealerGroupRepository);
            
            if (!validationService.isValidParentRelationship(dealerGroupDTO.getId(), dealerGroupDTO.getParentGroup().getId())) {
                throw new IllegalArgumentException("Setting this parent group would create a circular dependency");
            }
            
            if (!validationService.isValidHierarchyDepth(dealerGroupDTO.getId(), dealerGroupDTO.getParentGroup().getId())) {
                throw new IllegalArgumentException("Hierarchy depth would exceed maximum allowed depth of 5 levels");
            }
        }
        
        DealerGroup dealerGroup = dealerGroupMapper.toEntity(dealerGroupDTO);
        dealerGroup = dealerGroupRepository.save(dealerGroup);
        DealerGroupDTO result = dealerGroupMapper.toDto(dealerGroup);
        dealerGroupSearchRepository.save(dealerGroup);
        return result;
    }

    @Override
    public Optional<DealerGroupDTO> partialUpdate(DealerGroupDTO dealerGroupDTO) {
        log.debug("Request to partially update DealerGroup : {}", dealerGroupDTO);

        return dealerGroupRepository
            .findById(dealerGroupDTO.getId())
            .map(existingDealerGroup -> {
                dealerGroupMapper.partialUpdate(existingDealerGroup, dealerGroupDTO);

                return existingDealerGroup;
            })
            .map(dealerGroupRepository::save)
            .map(savedDealerGroup -> {
                dealerGroupSearchRepository.save(savedDealerGroup);

                return savedDealerGroup;
            })
            .map(dealerGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerGroupDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DealerGroups");
        return dealerGroupRepository.findAll(pageable).map(dealerGroupMapper::toDto);
    }

    public Page<DealerGroupDTO> findAllWithEagerRelationships(Pageable pageable) {
        return dealerGroupRepository.findAllWithEagerRelationships(pageable).map(dealerGroupMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DealerGroupDTO> findOne(Long id) {
        log.debug("Request to get DealerGroup : {}", id);
        return dealerGroupRepository.findOneWithEagerRelationships(id).map(dealerGroupMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DealerGroup : {}", id);
        dealerGroupRepository.deleteById(id);
        dealerGroupSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DealerGroupDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DealerGroups for query {}", query);
        return dealerGroupSearchRepository.search(query, pageable).map(dealerGroupMapper::toDto);
    }
}
>>>>>>> 210929d30f (feat: Fix dealer self-references by creating DealerGroup entity)
