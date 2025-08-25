package io.github.erp.context.assets.service;

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
import io.github.erp.context.assets.domain.AssetRegistration;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.asset.AssetCreatedEvent;
import io.github.erp.domain.events.asset.AssetCategoryChangedEvent;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service("contextInternalAssetRegistrationServiceImpl")
@Transactional
public class InternalAssetRegistrationServiceImpl {

    private final Logger log = LoggerFactory.getLogger(InternalAssetRegistrationServiceImpl.class);

    private final InternalAssetRegistrationRepository assetRegistrationRepository;
    private final AssetRegistrationMapper assetRegistrationMapper;
    private final AssetRegistrationSearchRepository assetRegistrationSearchRepository;
    private final DomainEventPublisher domainEventPublisher;

    public InternalAssetRegistrationServiceImpl(
        InternalAssetRegistrationRepository assetRegistrationRepository,
        AssetRegistrationMapper assetRegistrationMapper,
        AssetRegistrationSearchRepository assetRegistrationSearchRepository,
        DomainEventPublisher domainEventPublisher
    ) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetRegistrationSearchRepository = assetRegistrationSearchRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public AssetRegistrationDTO save(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to save AssetRegistration : {}", assetRegistrationDTO);
        
        AssetRegistration assetRegistration = assetRegistrationMapper.toEntity(assetRegistrationDTO);
        boolean isNewAsset = assetRegistration.getId() == null;
        Long previousCategoryId = null;
        
        if (!isNewAsset) {
            Optional<AssetRegistration> existingAsset = assetRegistrationRepository.findById(assetRegistration.getId());
            if (existingAsset.isPresent() && existingAsset.get().getAssetCategory() != null) {
                previousCategoryId = existingAsset.get().getAssetCategory().getId();
            }
        }
        
        assetRegistration = assetRegistrationRepository.save(assetRegistration);
        AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);
        assetRegistrationSearchRepository.save(assetRegistration);
        
        if (isNewAsset) {
            publishAssetCreatedEvent(assetRegistration);
        } else if (previousCategoryId != null && 
                   assetRegistration.getAssetCategory() != null &&
                   !previousCategoryId.equals(assetRegistration.getAssetCategory().getId())) {
            publishAssetCategoryChangedEvent(assetRegistration, previousCategoryId);
        }
        
        return result;
    }

    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetRegistrations");
        return assetRegistrationRepository.findAll(pageable).map(assetRegistrationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AssetRegistrationDTO> findOne(Long id) {
        log.debug("Request to get AssetRegistration : {}", id);
        return assetRegistrationRepository.findById(id).map(assetRegistrationMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete AssetRegistration : {}", id);
        assetRegistrationRepository.deleteById(id);
        assetRegistrationSearchRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetRegistrations for query {}", query);
        return assetRegistrationSearchRepository.search(query, pageable).map(assetRegistrationMapper::toDto);
    }

    private void publishAssetCreatedEvent(AssetRegistration assetRegistration) {
        try {
            AssetCreatedEvent event = new AssetCreatedEvent(
                assetRegistration.getId().toString(),
                assetRegistration.getAssetNumber(),
                assetRegistration.getAssetDetails(),
                assetRegistration.getAssetCost(),
                assetRegistration.getAssetCategory() != null ? assetRegistration.getAssetCategory().getId() : null,
                UUID.randomUUID()
            );
            domainEventPublisher.publish(event);
            log.info("Published AssetCreatedEvent for asset: {}", assetRegistration.getId());
        } catch (Exception e) {
            log.error("Failed to publish AssetCreatedEvent for asset: {}", assetRegistration.getId(), e);
        }
    }

    private void publishAssetCategoryChangedEvent(AssetRegistration assetRegistration, Long previousCategoryId) {
        try {
            AssetCategoryChangedEvent event = new AssetCategoryChangedEvent(
                assetRegistration.getId().toString(),
                assetRegistration.getAssetNumber(),
                previousCategoryId,
                assetRegistration.getAssetCategory() != null ? assetRegistration.getAssetCategory().getId() : null,
                null, // previousCategoryName - would need to fetch from database
                assetRegistration.getAssetCategory() != null ? assetRegistration.getAssetCategory().getAssetCategoryName() : null,
                UUID.randomUUID()
            );
            domainEventPublisher.publish(event);
            log.info("Published AssetCategoryChangedEvent for asset: {}", assetRegistration.getId());
        } catch (Exception e) {
            log.error("Failed to publish AssetCategoryChangedEvent for asset: {}", assetRegistration.getId(), e);
        }
    }
}
