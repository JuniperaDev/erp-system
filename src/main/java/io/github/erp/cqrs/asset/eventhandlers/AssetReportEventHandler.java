package io.github.erp.cqrs.asset.eventhandlers;

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

import io.github.erp.cqrs.asset.readmodel.AssetReportReadModel;
import io.github.erp.cqrs.asset.readmodel.AssetNBVReportReadModel;
import io.github.erp.cqrs.asset.repositories.AssetReportReadModelRepository;
import io.github.erp.context.assets.domain.AssetRegistration;
import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.DomainEventHandler;
import io.github.erp.domain.events.DomainEventHandlerMethodAnnotation;
import io.github.erp.domain.events.asset.AssetCreatedEvent;
import io.github.erp.domain.events.asset.AssetCategoryChangedEvent;
import io.github.erp.domain.events.asset.AssetDisposedEvent;
import io.github.erp.domain.events.asset.AssetRevaluedEvent;
import io.github.erp.repository.AssetRegistrationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
@DomainEventHandler
public class AssetReportEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetReportEventHandler.class);

    private final AssetReportReadModelRepository assetReportReadModelRepository;
    private final AssetRegistrationRepository assetRegistrationRepository;

    public AssetReportEventHandler(
        AssetReportReadModelRepository assetReportReadModelRepository,
        AssetRegistrationRepository assetRegistrationRepository
    ) {
        this.assetReportReadModelRepository = assetReportReadModelRepository;
        this.assetRegistrationRepository = assetRegistrationRepository;
    }

    @DomainEventHandlerMethodAnnotation(eventType = "AssetCreatedEvent", order = 3)
    @Transactional
    public void handleAssetCreated(DomainEvent event) {
        if (event instanceof AssetCreatedEvent) {
            AssetCreatedEvent assetEvent = (AssetCreatedEvent) event;
            log.info("Processing asset created event for read model: {}", assetEvent.getAggregateId());
            
            try {
                createAssetReportReadModel(assetEvent);
                log.info("Successfully created asset report read model for: {}", assetEvent.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to create asset report read model for: {}", assetEvent.getAggregateId(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "AssetCategoryChangedEvent", order = 3)
    @Transactional
    public void handleAssetCategoryChanged(DomainEvent event) {
        if (event instanceof AssetCategoryChangedEvent) {
            AssetCategoryChangedEvent categoryEvent = (AssetCategoryChangedEvent) event;
            log.info("Processing asset category changed event for read model: {}", categoryEvent.getAggregateId());
            
            try {
                updateAssetReportReadModelCategory(categoryEvent);
                log.info("Successfully updated asset report read model category for: {}", categoryEvent.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to update asset report read model category for: {}", categoryEvent.getAggregateId(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "AssetDisposedEvent", order = 3)
    @Transactional
    public void handleAssetDisposed(DomainEvent event) {
        if (event instanceof AssetDisposedEvent) {
            AssetDisposedEvent disposalEvent = (AssetDisposedEvent) event;
            log.info("Processing asset disposed event for read model: {}", disposalEvent.getAggregateId());
            
            try {
                removeAssetReportReadModel(disposalEvent);
                log.info("Successfully removed asset report read model for: {}", disposalEvent.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to remove asset report read model for: {}", disposalEvent.getAggregateId(), e);
            }
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "AssetRevaluedEvent", order = 3)
    @Transactional
    public void handleAssetRevalued(DomainEvent event) {
        if (event instanceof AssetRevaluedEvent) {
            AssetRevaluedEvent revaluationEvent = (AssetRevaluedEvent) event;
            log.info("Processing asset revalued event for read model: {}", revaluationEvent.getAggregateId());
            
            try {
                updateAssetReportReadModelValue(revaluationEvent);
                log.info("Successfully updated asset report read model value for: {}", revaluationEvent.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to update asset report read model value for: {}", revaluationEvent.getAggregateId(), e);
            }
        }
    }

    private void createAssetReportReadModel(AssetCreatedEvent event) {
        Long assetId = Long.parseLong(event.getAggregateId());
        Optional<AssetRegistration> assetOptional = assetRegistrationRepository.findById(assetId);
        
        if (assetOptional.isPresent()) {
            AssetRegistration asset = assetOptional.get();
            
            AssetReportReadModel readModel = new AssetReportReadModel()
                .assetId(asset.getId())
                .assetNumber(event.getAssetRegistrationNumber())
                .assetTag(asset.getAssetTag())
                .assetDescription(event.getAssetDescription())
                .assetCost(event.getAssetCost())
                .currentNBV(event.getAssetCost())
                .capitalizationDate(asset.getCapitalizationDate())
                .reportingDate(LocalDate.now())
                .totalDepreciation(BigDecimal.ZERO)
                .lastUpdated(LocalDate.now());

            if (asset.getAssetCategory() != null) {
                readModel.categoryName(asset.getAssetCategory().getAssetCategoryName())
                         .categoryId(asset.getAssetCategory().getId());
            }

            if (asset.getMainServiceOutlet() != null) {
                readModel.serviceOutletCode(asset.getMainServiceOutlet().getOutletCode())
                         .serviceOutletId(asset.getMainServiceOutlet().getId());
            }

            if (asset.getDealer() != null) {
                readModel.dealerName(asset.getDealer().getDealerName())
                         .dealerId(asset.getDealer().getId());
            }

            assetReportReadModelRepository.save(readModel);
        }
    }

    private void updateAssetReportReadModelCategory(AssetCategoryChangedEvent event) {
        Long assetId = Long.parseLong(event.getAggregateId());
        assetReportReadModelRepository.findByAssetId(assetId)
            .forEach(readModel -> {
                readModel.categoryName(event.getNewCategoryName())
                         .categoryId(event.getNewCategoryId())
                         .lastUpdated(LocalDate.now());
                assetReportReadModelRepository.save(readModel);
            });
    }

    private void removeAssetReportReadModel(AssetDisposedEvent event) {
        Long assetId = Long.parseLong(event.getAggregateId());
        assetReportReadModelRepository.deleteByAssetId(assetId);
    }

    private void updateAssetReportReadModelValue(AssetRevaluedEvent event) {
        Long assetId = Long.parseLong(event.getAggregateId());
        assetReportReadModelRepository.findByAssetId(assetId)
            .forEach(readModel -> {
                readModel.assetCost(event.getRevaluedAmount())
                         .currentNBV(event.getRevaluedAmount())
                         .lastUpdated(LocalDate.now());
                assetReportReadModelRepository.save(readModel);
            });
    }
}
