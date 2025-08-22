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

import io.github.erp.context.assets.domain.AssetRegistration;
import io.github.erp.domain.events.AssetAcquiredEvent;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AssetRegistration}.
 */
@Service
@Transactional
public class AssetRegistrationServiceImpl implements AssetRegistrationService {

    private final Logger log = LoggerFactory.getLogger(AssetRegistrationServiceImpl.class);

    private final AssetRegistrationRepository assetRegistrationRepository;

    private final AssetRegistrationMapper assetRegistrationMapper;

    private final AssetRegistrationSearchRepository assetRegistrationSearchRepository;
    
    private final ApplicationEventPublisher eventPublisher;

    public AssetRegistrationServiceImpl(
        AssetRegistrationRepository assetRegistrationRepository,
        AssetRegistrationMapper assetRegistrationMapper,
        @Autowired(required = false) AssetRegistrationSearchRepository assetRegistrationSearchRepository,
        ApplicationEventPublisher eventPublisher
    ) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetRegistrationSearchRepository = assetRegistrationSearchRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public AssetRegistrationDTO save(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to save AssetRegistration : {}", assetRegistrationDTO);
        AssetRegistration assetRegistration = assetRegistrationMapper.toEntity(assetRegistrationDTO);
        assetRegistration = assetRegistrationRepository.save(assetRegistration);
        AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);
        if (assetRegistrationSearchRepository != null) {
            assetRegistrationSearchRepository.save(assetRegistration);
        }
        
        if (assetRegistration.getAcquiringTransactionId() != null) {
            eventPublisher.publishEvent(new AssetAcquiredEvent(
                this,
                assetRegistration.getId(),
                assetRegistration.getAcquiringTransactionId(),
                assetRegistration.getCapitalizationDate()
            ));
        }
        
        return result;
    }

    @Override
    public Optional<AssetRegistrationDTO> partialUpdate(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to partially update AssetRegistration : {}", assetRegistrationDTO);

        return assetRegistrationRepository
            .findById(assetRegistrationDTO.getId())
            .map(existingAssetRegistration -> {
                assetRegistrationMapper.partialUpdate(existingAssetRegistration, assetRegistrationDTO);

                return existingAssetRegistration;
            })
            .map(assetRegistrationRepository::save)
            .map(savedAssetRegistration -> {
                if (assetRegistrationSearchRepository != null) {
                    assetRegistrationSearchRepository.save(savedAssetRegistration);
                }

                if (savedAssetRegistration.getAcquiringTransactionId() != null) {
                    eventPublisher.publishEvent(new AssetAcquiredEvent(
                        this,
                        savedAssetRegistration.getId(),
                        savedAssetRegistration.getAcquiringTransactionId(),
                        savedAssetRegistration.getCapitalizationDate()
                    ));
                }

                return savedAssetRegistration;
            })
            .map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetRegistrations");
        return assetRegistrationRepository.findAll(pageable).map(assetRegistrationMapper::toDto);
    }

    public Page<AssetRegistrationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetRegistrationRepository.findAllWithEagerRelationships(pageable).map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetRegistrationDTO> findOne(Long id) {
        log.debug("Request to get AssetRegistration : {}", id);
        return assetRegistrationRepository.findOneWithEagerRelationships(id).map(assetRegistrationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetRegistration : {}", id);
        assetRegistrationRepository.deleteById(id);
        if (assetRegistrationSearchRepository != null) {
            assetRegistrationSearchRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetRegistrations for query {}", query);
        if (assetRegistrationSearchRepository != null) {
            return assetRegistrationSearchRepository.search(query, pageable).map(assetRegistrationMapper::toDto);
        }
        return assetRegistrationRepository.findAll(pageable).map(assetRegistrationMapper::toDto);
    }
}
