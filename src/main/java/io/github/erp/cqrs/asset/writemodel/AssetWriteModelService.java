package io.github.erp.cqrs.asset.writemodel;

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

import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.events.asset.AssetCreatedEvent;
import io.github.erp.domain.events.asset.AssetCategoryChangedEvent;
import io.github.erp.domain.events.asset.AssetDisposedEvent;
import io.github.erp.domain.events.asset.AssetRevaluedEvent;
import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class AssetWriteModelService {

    private final Logger log = LoggerFactory.getLogger(AssetWriteModelService.class);

    private final AssetRegistrationRepository assetRegistrationRepository;
    private final AssetRegistrationMapper assetRegistrationMapper;
    private final DomainEventStore domainEventStore;

    public AssetWriteModelService(
        AssetRegistrationRepository assetRegistrationRepository,
        AssetRegistrationMapper assetRegistrationMapper,
        DomainEventStore domainEventStore
    ) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.domainEventStore = domainEventStore;
    }

    public AssetRegistrationDTO createAsset(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to create Asset : {}", assetRegistrationDTO);
        
        AssetRegistration assetRegistration = assetRegistrationMapper.toEntity(assetRegistrationDTO);
        assetRegistration = assetRegistrationRepository.save(assetRegistration);
        AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);

        AssetCreatedEvent event = new AssetCreatedEvent(
            assetRegistration.getId().toString(),
            assetRegistration.getAssetNumber(),
            assetRegistration.getAssetTag(),
            assetRegistration.getAssetDetails(),
            assetRegistration.getAssetCost(),
            assetRegistration.getCapitalizationDate(),
            java.util.UUID.randomUUID()
        );
        
        domainEventStore.store(event);
        log.info("Asset created and event published: {}", assetRegistration.getId());
        
        return result;
    }

    public Optional<AssetRegistrationDTO> updateAsset(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to update Asset : {}", assetRegistrationDTO);

        return assetRegistrationRepository
            .findById(assetRegistrationDTO.getId())
            .map(existingAsset -> {
                Long previousCategoryId = existingAsset.getAssetCategory() != null ? 
                    existingAsset.getAssetCategory().getId() : null;
                String previousCategoryName = existingAsset.getAssetCategory() != null ? 
                    existingAsset.getAssetCategory().getAssetCategoryName() : null;

                assetRegistrationMapper.partialUpdate(existingAsset, assetRegistrationDTO);
                return existingAsset;
            })
            .map(assetRegistrationRepository::save)
            .map(updatedAsset -> {
                Long newCategoryId = updatedAsset.getAssetCategory() != null ? 
                    updatedAsset.getAssetCategory().getId() : null;
                String newCategoryName = updatedAsset.getAssetCategory() != null ? 
                    updatedAsset.getAssetCategory().getAssetCategoryName() : null;

                if (previousCategoryId != null && newCategoryId != null && 
                    !previousCategoryId.equals(newCategoryId)) {
                    
                    AssetCategoryChangedEvent event = new AssetCategoryChangedEvent(
                        updatedAsset.getId().toString(),
                        updatedAsset.getAssetNumber(),
                        previousCategoryId,
                        newCategoryId,
                        previousCategoryName,
                        newCategoryName,
                        java.util.UUID.randomUUID()
                    );
                    
                    domainEventStore.store(event);
                    log.info("Asset category changed and event published: {}", updatedAsset.getId());
                }

                return updatedAsset;
            })
            .map(assetRegistrationMapper::toDto);
    }

    public void deleteAsset(Long id) {
        log.debug("Request to delete Asset : {}", id);
        
        Optional<AssetRegistration> assetOptional = assetRegistrationRepository.findById(id);
        if (assetOptional.isPresent()) {
            AssetRegistration asset = assetOptional.get();
            
            AssetDisposedEvent event = new AssetDisposedEvent(
                asset.getId().toString(),
                asset.getAssetNumber(),
                asset.getAssetDetails(),
                LocalDate.now(),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "DISPOSAL",
                java.util.UUID.randomUUID()
            );
            
            domainEventStore.store(event);
            log.info("Asset disposal event published: {}", asset.getId());
        }
        
        assetRegistrationRepository.deleteById(id);
    }

    public void revalueAsset(Long assetId, BigDecimal newValue, LocalDate revaluationDate) {
        log.debug("Request to revalue Asset : {} to value : {}", assetId, newValue);
        
        Optional<AssetRegistration> assetOptional = assetRegistrationRepository.findById(assetId);
        if (assetOptional.isPresent()) {
            AssetRegistration asset = assetOptional.get();
            BigDecimal previousValue = asset.getAssetCost();
            
            asset.setAssetCost(newValue);
            assetRegistrationRepository.save(asset);
            
            AssetRevaluedEvent event = new AssetRevaluedEvent(
                asset.getId().toString(),
                asset.getAssetNumber(),
                asset.getAssetDetails(),
                revaluationDate,
                previousValue,
                newValue,
                "REVALUATION",
                java.util.UUID.randomUUID()
            );
            
            domainEventStore.store(event);
            log.info("Asset revaluation event published: {}", asset.getId());
        }
    }
}
