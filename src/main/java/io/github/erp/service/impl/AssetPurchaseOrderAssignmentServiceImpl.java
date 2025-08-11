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

import io.github.erp.domain.AssetPurchaseOrderAssignment;
import io.github.erp.repository.AssetPurchaseOrderAssignmentRepository;
import io.github.erp.repository.search.AssetPurchaseOrderAssignmentSearchRepository;
import io.github.erp.service.AssetPurchaseOrderAssignmentService;
import io.github.erp.service.dto.AssetPurchaseOrderAssignmentDTO;
import io.github.erp.service.mapper.AssetPurchaseOrderAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetPurchaseOrderAssignment}.
 */
@Service
@Transactional
public class AssetPurchaseOrderAssignmentServiceImpl implements AssetPurchaseOrderAssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssetPurchaseOrderAssignmentServiceImpl.class);

    private final AssetPurchaseOrderAssignmentRepository assetPurchaseOrderAssignmentRepository;

    private final AssetPurchaseOrderAssignmentMapper assetPurchaseOrderAssignmentMapper;

    private final AssetPurchaseOrderAssignmentSearchRepository assetPurchaseOrderAssignmentSearchRepository;

    public AssetPurchaseOrderAssignmentServiceImpl(
        AssetPurchaseOrderAssignmentRepository assetPurchaseOrderAssignmentRepository,
        AssetPurchaseOrderAssignmentMapper assetPurchaseOrderAssignmentMapper,
        AssetPurchaseOrderAssignmentSearchRepository assetPurchaseOrderAssignmentSearchRepository
    ) {
        this.assetPurchaseOrderAssignmentRepository = assetPurchaseOrderAssignmentRepository;
        this.assetPurchaseOrderAssignmentMapper = assetPurchaseOrderAssignmentMapper;
        this.assetPurchaseOrderAssignmentSearchRepository = assetPurchaseOrderAssignmentSearchRepository;
    }

    @Override
    public AssetPurchaseOrderAssignmentDTO save(AssetPurchaseOrderAssignmentDTO assetPurchaseOrderAssignmentDTO) {
        log.debug("Request to save AssetPurchaseOrderAssignment : {}", assetPurchaseOrderAssignmentDTO);
        AssetPurchaseOrderAssignment assetPurchaseOrderAssignment = assetPurchaseOrderAssignmentMapper.toEntity(assetPurchaseOrderAssignmentDTO);
        assetPurchaseOrderAssignment = assetPurchaseOrderAssignmentRepository.save(assetPurchaseOrderAssignment);
        AssetPurchaseOrderAssignmentDTO result = assetPurchaseOrderAssignmentMapper.toDto(assetPurchaseOrderAssignment);
        assetPurchaseOrderAssignmentSearchRepository.save(assetPurchaseOrderAssignment);
        return result;
    }

    @Override
    public AssetPurchaseOrderAssignmentDTO update(AssetPurchaseOrderAssignmentDTO assetPurchaseOrderAssignmentDTO) {
        log.debug("Request to update AssetPurchaseOrderAssignment : {}", assetPurchaseOrderAssignmentDTO);
        AssetPurchaseOrderAssignment assetPurchaseOrderAssignment = assetPurchaseOrderAssignmentMapper.toEntity(assetPurchaseOrderAssignmentDTO);
        assetPurchaseOrderAssignment = assetPurchaseOrderAssignmentRepository.save(assetPurchaseOrderAssignment);
        AssetPurchaseOrderAssignmentDTO result = assetPurchaseOrderAssignmentMapper.toDto(assetPurchaseOrderAssignment);
        assetPurchaseOrderAssignmentSearchRepository.save(assetPurchaseOrderAssignment);
        return result;
    }

    @Override
    public Optional<AssetPurchaseOrderAssignmentDTO> partialUpdate(AssetPurchaseOrderAssignmentDTO assetPurchaseOrderAssignmentDTO) {
        log.debug("Request to partially update AssetPurchaseOrderAssignment : {}", assetPurchaseOrderAssignmentDTO);

        return assetPurchaseOrderAssignmentRepository
            .findById(assetPurchaseOrderAssignmentDTO.getId())
            .map(existingAssetPurchaseOrderAssignment -> {
                assetPurchaseOrderAssignmentMapper.partialUpdate(existingAssetPurchaseOrderAssignment, assetPurchaseOrderAssignmentDTO);

                return existingAssetPurchaseOrderAssignment;
            })
            .map(assetPurchaseOrderAssignmentRepository::save)
            .map(savedAssetPurchaseOrderAssignment -> {
                assetPurchaseOrderAssignmentSearchRepository.save(savedAssetPurchaseOrderAssignment);

                return savedAssetPurchaseOrderAssignment;
            })
            .map(assetPurchaseOrderAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetPurchaseOrderAssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetPurchaseOrderAssignments");
        return assetPurchaseOrderAssignmentRepository.findAll(pageable).map(assetPurchaseOrderAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetPurchaseOrderAssignmentDTO> findOne(Long id) {
        log.debug("Request to get AssetPurchaseOrderAssignment : {}", id);
        return assetPurchaseOrderAssignmentRepository.findById(id).map(assetPurchaseOrderAssignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetPurchaseOrderAssignment : {}", id);
        assetPurchaseOrderAssignmentRepository.deleteById(id);
        assetPurchaseOrderAssignmentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetPurchaseOrderAssignmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetPurchaseOrderAssignments for query {}", query);
        return assetPurchaseOrderAssignmentSearchRepository.search(query, pageable).map(assetPurchaseOrderAssignmentMapper::toDto);
    }
}
