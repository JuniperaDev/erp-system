package io.github.erp.internal.service.assets;

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
import io.github.erp.domain.events.asset.AssetCategoryChangedEvent;
import io.github.erp.domain.events.asset.AssetCreatedEvent;
import io.github.erp.internal.repository.InternalAssetRegistrationRepository;
import io.github.erp.internal.utilities.NextIntegerFiller;
import io.github.erp.repository.search.AssetRegistrationSearchRepository;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.AssetRegistrationListDTO;
import io.github.erp.service.impl.AssetRegistrationServiceImpl;
import io.github.erp.service.mapper.AssetRegistrationMapper;
import io.github.erp.service.monitoring.MemoryMonitoringService;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("internalAssetRegistrationServiceImpl")
@Transactional
public class InternalAssetRegistrationServiceImpl implements InternalAssetRegistrationService{


    private final Logger log = LoggerFactory.getLogger(AssetRegistrationServiceImpl.class);

    private final InternalAssetRegistrationRepository assetRegistrationRepository;

    private final AssetRegistrationMapper assetRegistrationMapper;

    private final AssetRegistrationSearchRepository assetRegistrationSearchRepository;

    private final InternalAssetRegistrationRepository internalAssetRegistrationRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final MemoryMonitoringService memoryMonitoringService;

    public InternalAssetRegistrationServiceImpl(InternalAssetRegistrationRepository assetRegistrationRepository, AssetRegistrationMapper assetRegistrationMapper, AssetRegistrationSearchRepository assetRegistrationSearchRepository, InternalAssetRegistrationRepository internalAssetRegistrationRepository, DomainEventPublisher domainEventPublisher, MemoryMonitoringService memoryMonitoringService) {
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationMapper = assetRegistrationMapper;
        this.assetRegistrationSearchRepository = assetRegistrationSearchRepository;
        this.internalAssetRegistrationRepository = internalAssetRegistrationRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.memoryMonitoringService = memoryMonitoringService;
    }

    @Override
    public AssetRegistrationDTO save(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to save AssetRegistration : {}", assetRegistrationDTO);
        boolean isNewAsset = assetRegistrationDTO.getId() == null;
        AssetRegistration assetRegistration = assetRegistrationMapper.toEntity(assetRegistrationDTO);
        assetRegistration = assetRegistrationRepository.save(assetRegistration);
        AssetRegistrationDTO result = assetRegistrationMapper.toDto(assetRegistration);
        assetRegistrationSearchRepository.save(assetRegistration);
        
        if (isNewAsset) {
            AssetCreatedEvent event = new AssetCreatedEvent(
                assetRegistration.getId().toString(),
                assetRegistration.getAssetNumber(),
                assetRegistration.getAssetDetails(),
                assetRegistration.getAssetCost(),
                assetRegistration.getAssetCategory().getId(),
                UUID.randomUUID()
            );
            domainEventPublisher.publish(event);
        }
        
        return result;
    }

    @Override
    public Optional<AssetRegistrationDTO> partialUpdate(AssetRegistrationDTO assetRegistrationDTO) {
        log.debug("Request to partially update AssetRegistration : {}", assetRegistrationDTO);

        return assetRegistrationRepository
            .findById(assetRegistrationDTO.getId())
            .map(existingAssetRegistration -> {
                Long previousCategoryId = existingAssetRegistration.getAssetCategory() != null ? 
                    existingAssetRegistration.getAssetCategory().getId() : null;
                String previousCategoryName = existingAssetRegistration.getAssetCategory() != null ? 
                    existingAssetRegistration.getAssetCategory().getAssetCategoryName() : null;

                assetRegistrationMapper.partialUpdate(existingAssetRegistration, assetRegistrationDTO);

                Long newCategoryId = existingAssetRegistration.getAssetCategory() != null ? 
                    existingAssetRegistration.getAssetCategory().getId() : null;
                String newCategoryName = existingAssetRegistration.getAssetCategory() != null ? 
                    existingAssetRegistration.getAssetCategory().getAssetCategoryName() : null;

                if (previousCategoryId != null && newCategoryId != null && 
                    !previousCategoryId.equals(newCategoryId)) {
                    AssetCategoryChangedEvent event = new AssetCategoryChangedEvent(
                        existingAssetRegistration.getId().toString(),
                        existingAssetRegistration.getAssetNumber(),
                        previousCategoryId,
                        newCategoryId,
                        previousCategoryName,
                        newCategoryName,
                        UUID.randomUUID()
                    );
                    domainEventPublisher.publish(event);
                }

                return existingAssetRegistration;
            })
            .map(assetRegistrationRepository::save)
            .map(savedAssetRegistration -> {
                assetRegistrationSearchRepository.save(savedAssetRegistration);

                return savedAssetRegistration;
            })
            .map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetRegistrations");
        Timer.Sample sample = memoryMonitoringService.startEntityLoadTimer("AssetRegistration");
        
        try {
            Page<AssetRegistrationDTO> result = assetRegistrationRepository.findAllLazy(pageable).map(assetRegistrationMapper::toDto);
            memoryMonitoringService.recordEntityLoad("AssetRegistration");
            return result;
        } finally {
            memoryMonitoringService.stopEntityLoadTimer(sample, "findAll", "AssetRegistration");
        }
    }

    @Transactional(readOnly = true)
    public Page<AssetRegistrationListDTO> findAllAsProjection(Pageable pageable) {
        log.debug("Request to get all AssetRegistrations as projection");
        Timer.Sample sample = memoryMonitoringService.startEntityLoadTimer("AssetRegistrationProjection");
        
        try {
            Page<AssetRegistrationListDTO> result = assetRegistrationRepository.findAllAsProjection(pageable);
            memoryMonitoringService.recordEntityLoad("AssetRegistrationProjection");
            return result;
        } finally {
            memoryMonitoringService.stopEntityLoadTimer(sample, "findAllAsProjection", "AssetRegistration");
        }
    }

    public Page<AssetRegistrationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assetRegistrationRepository.findAllWithEagerRelationships(pageable).map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetRegistrationDTO> findOne(Long id) {
        log.debug("Request to get AssetRegistration : {}", id);
        Timer.Sample sample = memoryMonitoringService.startEntityLoadTimer("AssetRegistration");
        
        try {
            Optional<AssetRegistrationDTO> result = assetRegistrationRepository.findOneWithEagerRelationships(id).map(assetRegistrationMapper::toDto);
            memoryMonitoringService.recordEntityLoad("AssetRegistration");
            return result;
        } finally {
            memoryMonitoringService.stopEntityLoadTimer(sample, "findOne", "AssetRegistration");
        }
    }

    @Transactional(readOnly = true)
    public Optional<AssetRegistrationDTO> findOneLazy(Long id) {
        log.debug("Request to get AssetRegistration lazily : {}", id);
        Timer.Sample sample = memoryMonitoringService.startEntityLoadTimer("AssetRegistrationLazy");
        
        try {
            Optional<AssetRegistrationDTO> result = assetRegistrationRepository.findOneLazy(id).map(assetRegistrationMapper::toDto);
            memoryMonitoringService.recordEntityLoad("AssetRegistrationLazy");
            return result;
        } finally {
            memoryMonitoringService.stopEntityLoadTimer(sample, "findOneLazy", "AssetRegistration");
        }
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetRegistration : {}", id);
        assetRegistrationRepository.deleteById(id);
        assetRegistrationSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetRegistrationDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetRegistrations for query {}", query);
        return assetRegistrationSearchRepository.search(query, pageable).map(assetRegistrationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AssetRegistrationDTO> findByCapitalizationDateBefore(LocalDate capitalizationDate) {

        return internalAssetRegistrationRepository.findAllByCapitalizationDateLessThanEqual(capitalizationDate)
            .stream().map(assetRegistrationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Long calculateNextAssetNumber() {
        log.debug("Request to get next asset number");
        return NextIntegerFiller.fillNext(assetRegistrationRepository.findAllAssetNumbers());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllIds() {
        log.debug("Request to get list of ids");
        return assetRegistrationRepository.findAllIds();
    }

    /**
     * List of asset ids of assets related to asset-general-adjustment
     *
     * @return List of ids
     */
    @Override
    public List<Long> findAdjacentAssetIds() {

        return assetRegistrationRepository.findAdjacentAssetIds();
    }

    /**
     * List of asset ids of assets related to asset-general-adjustment
     *
     * @return List of ids
     */
    @Override
    public List<Long> findDisposedAssetIds() {

        return assetRegistrationRepository.findDisposedAssetIds();
    }

    /**
     * List of asset ids of assets related to asset-write-off
     *
     * @return List of ids
     */
    @Override
    public List<Long> findWrittenOffAssetIds() {

        return assetRegistrationRepository.findWrittenOffAssetIds();
    }
}
