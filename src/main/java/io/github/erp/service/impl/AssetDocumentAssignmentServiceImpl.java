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

import io.github.erp.domain.AssetDocumentAssignment;
import io.github.erp.repository.AssetDocumentAssignmentRepository;
import io.github.erp.repository.search.AssetDocumentAssignmentSearchRepository;
import io.github.erp.service.AssetDocumentAssignmentService;
import io.github.erp.service.dto.AssetDocumentAssignmentDTO;
import io.github.erp.service.mapper.AssetDocumentAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetDocumentAssignment}.
 */
@Service
@Transactional
public class AssetDocumentAssignmentServiceImpl implements AssetDocumentAssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssetDocumentAssignmentServiceImpl.class);

    private final AssetDocumentAssignmentRepository assetDocumentAssignmentRepository;

    private final AssetDocumentAssignmentMapper assetDocumentAssignmentMapper;

    private final AssetDocumentAssignmentSearchRepository assetDocumentAssignmentSearchRepository;

    public AssetDocumentAssignmentServiceImpl(
        AssetDocumentAssignmentRepository assetDocumentAssignmentRepository,
        AssetDocumentAssignmentMapper assetDocumentAssignmentMapper,
        @Autowired(required = false) AssetDocumentAssignmentSearchRepository assetDocumentAssignmentSearchRepository
    ) {
        this.assetDocumentAssignmentRepository = assetDocumentAssignmentRepository;
        this.assetDocumentAssignmentMapper = assetDocumentAssignmentMapper;
        this.assetDocumentAssignmentSearchRepository = assetDocumentAssignmentSearchRepository;
    }

    @Override
    public AssetDocumentAssignmentDTO save(AssetDocumentAssignmentDTO assetDocumentAssignmentDTO) {
        log.debug("Request to save AssetDocumentAssignment : {}", assetDocumentAssignmentDTO);
        AssetDocumentAssignment assetDocumentAssignment = assetDocumentAssignmentMapper.toEntity(assetDocumentAssignmentDTO);
        assetDocumentAssignment = assetDocumentAssignmentRepository.save(assetDocumentAssignment);
        AssetDocumentAssignmentDTO result = assetDocumentAssignmentMapper.toDto(assetDocumentAssignment);
        if (assetDocumentAssignmentSearchRepository != null) {
            assetDocumentAssignmentSearchRepository.save(assetDocumentAssignment);
        }
        return result;
    }

    @Override
    public AssetDocumentAssignmentDTO update(AssetDocumentAssignmentDTO assetDocumentAssignmentDTO) {
        log.debug("Request to update AssetDocumentAssignment : {}", assetDocumentAssignmentDTO);
        AssetDocumentAssignment assetDocumentAssignment = assetDocumentAssignmentMapper.toEntity(assetDocumentAssignmentDTO);
        assetDocumentAssignment = assetDocumentAssignmentRepository.save(assetDocumentAssignment);
        AssetDocumentAssignmentDTO result = assetDocumentAssignmentMapper.toDto(assetDocumentAssignment);
        if (assetDocumentAssignmentSearchRepository != null) {
            assetDocumentAssignmentSearchRepository.save(assetDocumentAssignment);
        }
        return result;
    }

    @Override
    public Optional<AssetDocumentAssignmentDTO> partialUpdate(AssetDocumentAssignmentDTO assetDocumentAssignmentDTO) {
        log.debug("Request to partially update AssetDocumentAssignment : {}", assetDocumentAssignmentDTO);

        return assetDocumentAssignmentRepository
            .findById(assetDocumentAssignmentDTO.getId())
            .map(existingAssetDocumentAssignment -> {
                assetDocumentAssignmentMapper.partialUpdate(existingAssetDocumentAssignment, assetDocumentAssignmentDTO);

                return existingAssetDocumentAssignment;
            })
            .map(assetDocumentAssignmentRepository::save)
            .map(savedAssetDocumentAssignment -> {
                if (assetDocumentAssignmentSearchRepository != null) {
                    assetDocumentAssignmentSearchRepository.save(savedAssetDocumentAssignment);
                }

                return savedAssetDocumentAssignment;
            })
            .map(assetDocumentAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDocumentAssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetDocumentAssignments");
        return assetDocumentAssignmentRepository.findAll(pageable).map(assetDocumentAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetDocumentAssignmentDTO> findOne(Long id) {
        log.debug("Request to get AssetDocumentAssignment : {}", id);
        return assetDocumentAssignmentRepository.findById(id).map(assetDocumentAssignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetDocumentAssignment : {}", id);
        assetDocumentAssignmentRepository.deleteById(id);
        if (assetDocumentAssignmentSearchRepository != null) {
            assetDocumentAssignmentSearchRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetDocumentAssignmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetDocumentAssignments for query {}", query);
        if (assetDocumentAssignmentSearchRepository != null) {
            return assetDocumentAssignmentSearchRepository.search(query, pageable).map(assetDocumentAssignmentMapper::toDto);
        }
        return assetDocumentAssignmentRepository.findAll(pageable).map(assetDocumentAssignmentMapper::toDto);
    }
}
