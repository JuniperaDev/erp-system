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

import io.github.erp.domain.AssetPaymentInvoiceAssignment;
import io.github.erp.repository.AssetPaymentInvoiceAssignmentRepository;
import io.github.erp.repository.search.AssetPaymentInvoiceAssignmentSearchRepository;
import io.github.erp.service.AssetPaymentInvoiceAssignmentService;
import io.github.erp.service.dto.AssetPaymentInvoiceAssignmentDTO;
import io.github.erp.service.mapper.AssetPaymentInvoiceAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetPaymentInvoiceAssignment}.
 */
@Service
@Transactional
public class AssetPaymentInvoiceAssignmentServiceImpl implements AssetPaymentInvoiceAssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssetPaymentInvoiceAssignmentServiceImpl.class);

    private final AssetPaymentInvoiceAssignmentRepository assetPaymentInvoiceAssignmentRepository;

    private final AssetPaymentInvoiceAssignmentMapper assetPaymentInvoiceAssignmentMapper;

    private final AssetPaymentInvoiceAssignmentSearchRepository assetPaymentInvoiceAssignmentSearchRepository;

    public AssetPaymentInvoiceAssignmentServiceImpl(
        AssetPaymentInvoiceAssignmentRepository assetPaymentInvoiceAssignmentRepository,
        AssetPaymentInvoiceAssignmentMapper assetPaymentInvoiceAssignmentMapper,
        AssetPaymentInvoiceAssignmentSearchRepository assetPaymentInvoiceAssignmentSearchRepository
    ) {
        this.assetPaymentInvoiceAssignmentRepository = assetPaymentInvoiceAssignmentRepository;
        this.assetPaymentInvoiceAssignmentMapper = assetPaymentInvoiceAssignmentMapper;
        this.assetPaymentInvoiceAssignmentSearchRepository = assetPaymentInvoiceAssignmentSearchRepository;
    }

    @Override
    public AssetPaymentInvoiceAssignmentDTO save(AssetPaymentInvoiceAssignmentDTO assetPaymentInvoiceAssignmentDTO) {
        log.debug("Request to save AssetPaymentInvoiceAssignment : {}", assetPaymentInvoiceAssignmentDTO);
        AssetPaymentInvoiceAssignment assetPaymentInvoiceAssignment = assetPaymentInvoiceAssignmentMapper.toEntity(assetPaymentInvoiceAssignmentDTO);
        assetPaymentInvoiceAssignment = assetPaymentInvoiceAssignmentRepository.save(assetPaymentInvoiceAssignment);
        AssetPaymentInvoiceAssignmentDTO result = assetPaymentInvoiceAssignmentMapper.toDto(assetPaymentInvoiceAssignment);
        assetPaymentInvoiceAssignmentSearchRepository.save(assetPaymentInvoiceAssignment);
        return result;
    }

    @Override
    public AssetPaymentInvoiceAssignmentDTO update(AssetPaymentInvoiceAssignmentDTO assetPaymentInvoiceAssignmentDTO) {
        log.debug("Request to update AssetPaymentInvoiceAssignment : {}", assetPaymentInvoiceAssignmentDTO);
        AssetPaymentInvoiceAssignment assetPaymentInvoiceAssignment = assetPaymentInvoiceAssignmentMapper.toEntity(assetPaymentInvoiceAssignmentDTO);
        assetPaymentInvoiceAssignment = assetPaymentInvoiceAssignmentRepository.save(assetPaymentInvoiceAssignment);
        AssetPaymentInvoiceAssignmentDTO result = assetPaymentInvoiceAssignmentMapper.toDto(assetPaymentInvoiceAssignment);
        assetPaymentInvoiceAssignmentSearchRepository.save(assetPaymentInvoiceAssignment);
        return result;
    }

    @Override
    public Optional<AssetPaymentInvoiceAssignmentDTO> partialUpdate(AssetPaymentInvoiceAssignmentDTO assetPaymentInvoiceAssignmentDTO) {
        log.debug("Request to partially update AssetPaymentInvoiceAssignment : {}", assetPaymentInvoiceAssignmentDTO);

        return assetPaymentInvoiceAssignmentRepository
            .findById(assetPaymentInvoiceAssignmentDTO.getId())
            .map(existingAssetPaymentInvoiceAssignment -> {
                assetPaymentInvoiceAssignmentMapper.partialUpdate(existingAssetPaymentInvoiceAssignment, assetPaymentInvoiceAssignmentDTO);

                return existingAssetPaymentInvoiceAssignment;
            })
            .map(assetPaymentInvoiceAssignmentRepository::save)
            .map(savedAssetPaymentInvoiceAssignment -> {
                assetPaymentInvoiceAssignmentSearchRepository.save(savedAssetPaymentInvoiceAssignment);

                return savedAssetPaymentInvoiceAssignment;
            })
            .map(assetPaymentInvoiceAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetPaymentInvoiceAssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetPaymentInvoiceAssignments");
        return assetPaymentInvoiceAssignmentRepository.findAll(pageable).map(assetPaymentInvoiceAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetPaymentInvoiceAssignmentDTO> findOne(Long id) {
        log.debug("Request to get AssetPaymentInvoiceAssignment : {}", id);
        return assetPaymentInvoiceAssignmentRepository.findById(id).map(assetPaymentInvoiceAssignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetPaymentInvoiceAssignment : {}", id);
        assetPaymentInvoiceAssignmentRepository.deleteById(id);
        assetPaymentInvoiceAssignmentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetPaymentInvoiceAssignmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetPaymentInvoiceAssignments for query {}", query);
        return assetPaymentInvoiceAssignmentSearchRepository.search(query, pageable).map(assetPaymentInvoiceAssignmentMapper::toDto);
    }
}
