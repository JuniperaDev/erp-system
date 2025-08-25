package io.github.erp.context.assets.events;

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

import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.DomainEventHandler;
import io.github.erp.domain.events.DomainEventHandlerMethodAnnotation;
import io.github.erp.domain.events.asset.AssetCreatedEvent;
import io.github.erp.domain.events.asset.AssetCategoryChangedEvent;
import io.github.erp.domain.events.asset.AssetDisposedEvent;
import io.github.erp.domain.events.asset.AssetRevaluedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@DomainEventHandler
public class AssetEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetEventHandler.class);

    @DomainEventHandlerMethodAnnotation(eventType = "AssetCreatedEvent", order = 1)
    public void handleAssetCreated(DomainEvent event) {
        if (event instanceof AssetCreatedEvent) {
            AssetCreatedEvent assetEvent = (AssetCreatedEvent) event;
            log.info("Processing asset created event for asset: {} with registration: {}", 
                    assetEvent.getAggregateId(), assetEvent.getAssetRegistrationNumber());
            
            processAssetCreation(assetEvent);
            notifyDepreciationService(assetEvent);
            notifyFinancialService(assetEvent);
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "AssetCategoryChangedEvent", order = 1)
    public void handleAssetCategoryChanged(DomainEvent event) {
        if (event instanceof AssetCategoryChangedEvent) {
            AssetCategoryChangedEvent categoryEvent = (AssetCategoryChangedEvent) event;
            log.info("Processing asset category changed event for asset: {} from category {} to {}", 
                    categoryEvent.getAggregateId(), categoryEvent.getPreviousCategoryName(), 
                    categoryEvent.getNewCategoryName());
            
            processAssetCategoryChange(categoryEvent);
            notifyDepreciationService(categoryEvent);
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "AssetDisposedEvent", order = 1)
    public void handleAssetDisposed(DomainEvent event) {
        if (event instanceof AssetDisposedEvent) {
            AssetDisposedEvent disposalEvent = (AssetDisposedEvent) event;
            log.info("Processing asset disposed event for asset: {} on date: {}", 
                    disposalEvent.getAggregateId(), disposalEvent.getDisposalDate());
            
            processAssetDisposal(disposalEvent);
            notifyDepreciationService(disposalEvent);
            notifyFinancialService(disposalEvent);
        }
    }

    @DomainEventHandlerMethodAnnotation(eventType = "AssetRevaluedEvent", order = 1)
    public void handleAssetRevalued(DomainEvent event) {
        if (event instanceof AssetRevaluedEvent) {
            AssetRevaluedEvent revaluationEvent = (AssetRevaluedEvent) event;
            log.info("Processing asset revalued event for asset: {} on date: {}", 
                    revaluationEvent.getAggregateId(), revaluationEvent.getRevaluationDate());
            
            processAssetRevaluation(revaluationEvent);
            notifyDepreciationService(revaluationEvent);
            notifyFinancialService(revaluationEvent);
        }
    }

    private void processAssetCreation(AssetCreatedEvent event) {
        try {
            log.info("Processing asset creation for asset: {} with registration: {}", 
                    event.getAggregateId(), event.getAssetRegistrationNumber());
            
            updateAssetIndexes(event.getAggregateId());
            initializeAssetTracking(event);
            
            log.info("Asset creation processing completed for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to process asset creation for: {}", event.getAggregateId(), e);
        }
    }

    private void processAssetCategoryChange(AssetCategoryChangedEvent event) {
        try {
            log.info("Processing asset category change for asset: {} from {} to {}", 
                    event.getAggregateId(), event.getPreviousCategoryName(), event.getNewCategoryName());
            
            updateDepreciationSchedule(event.getAggregateId());
            recalculateAssetMetrics(event);
            
            log.info("Asset category change processing completed for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to process asset category change for: {}", event.getAggregateId(), e);
        }
    }

    private void processAssetDisposal(AssetDisposedEvent event) {
        try {
            log.info("Processing asset disposal for asset: {} on date: {}", 
                    event.getAggregateId(), event.getDisposalDate());
            
            finalizeDepreciation(event.getAggregateId(), event.getDisposalDate());
            updateAssetStatus(event.getAggregateId(), "DISPOSED");
            calculateDisposalGainLoss(event);
            
            log.info("Asset disposal processing completed for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to process asset disposal for: {}", event.getAggregateId(), e);
        }
    }

    private void processAssetRevaluation(AssetRevaluedEvent event) {
        try {
            log.info("Processing asset revaluation for asset: {} on date: {}", 
                    event.getAggregateId(), event.getRevaluationDate());
            
            updateAssetValuation(event.getAggregateId(), event.getRevaluedAmount());
            recalculateDepreciationSchedule(event);
            updateFinancialReports(event);
            
            log.info("Asset revaluation processing completed for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to process asset revaluation for: {}", event.getAggregateId(), e);
        }
    }

    private void notifyDepreciationService(DomainEvent event) {
        try {
            log.info("Notifying depreciation service for event: {} on asset: {}", 
                    event.getEventType(), event.getAggregateId());
            
            publishDepreciationEvent(event);
            
        } catch (Exception e) {
            log.error("Failed to notify depreciation service for event: {} on asset: {}", 
                    event.getEventType(), event.getAggregateId(), e);
        }
    }

    private void notifyFinancialService(DomainEvent event) {
        try {
            log.info("Notifying financial service for event: {} on asset: {}", 
                    event.getEventType(), event.getAggregateId());
            
            publishFinancialEvent(event);
            
        } catch (Exception e) {
            log.error("Failed to notify financial service for event: {} on asset: {}", 
                    event.getEventType(), event.getAggregateId(), e);
        }
    }

    private void updateAssetIndexes(String assetId) {
        try {
            log.debug("Updating search indexes for asset: {}", assetId);
        } catch (Exception e) {
            log.error("Failed to update asset indexes for: {}", assetId, e);
        }
    }

    private void initializeAssetTracking(AssetCreatedEvent event) {
        try {
            log.debug("Initializing asset tracking for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to initialize asset tracking for: {}", event.getAggregateId(), e);
        }
    }

    private void updateDepreciationSchedule(String assetId) {
        try {
            log.debug("Updating depreciation schedule for asset: {}", assetId);
        } catch (Exception e) {
            log.error("Failed to update depreciation schedule for: {}", assetId, e);
        }
    }

    private void recalculateAssetMetrics(AssetCategoryChangedEvent event) {
        try {
            log.debug("Recalculating asset metrics for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to recalculate asset metrics for: {}", event.getAggregateId(), e);
        }
    }

    private void finalizeDepreciation(String assetId, java.time.LocalDate disposalDate) {
        try {
            log.debug("Finalizing depreciation for asset: {} on date: {}", assetId, disposalDate);
        } catch (Exception e) {
            log.error("Failed to finalize depreciation for: {}", assetId, e);
        }
    }

    private void updateAssetStatus(String assetId, String status) {
        try {
            log.debug("Updating asset status for: {} to: {}", assetId, status);
        } catch (Exception e) {
            log.error("Failed to update asset status for: {}", assetId, e);
        }
    }

    private void calculateDisposalGainLoss(AssetDisposedEvent event) {
        try {
            log.debug("Calculating disposal gain/loss for: {}", event.getAggregateId());
            if (event.getNetBookValue() != null && event.getDisposalProceeds() != null) {
                java.math.BigDecimal gainLoss = event.getDisposalProceeds().subtract(event.getNetBookValue());
                log.info("Disposal gain/loss calculated for asset {}: {}", event.getAggregateId(), gainLoss);
            }
        } catch (Exception e) {
            log.error("Failed to calculate disposal gain/loss for: {}", event.getAggregateId(), e);
        }
    }

    private void updateAssetValuation(String assetId, java.math.BigDecimal revaluedAmount) {
        try {
            log.debug("Updating asset valuation for: {} to: {}", assetId, revaluedAmount);
        } catch (Exception e) {
            log.error("Failed to update asset valuation for: {}", assetId, e);
        }
    }

    private void recalculateDepreciationSchedule(AssetRevaluedEvent event) {
        try {
            log.debug("Recalculating depreciation schedule for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to recalculate depreciation schedule for: {}", event.getAggregateId(), e);
        }
    }

    private void updateFinancialReports(AssetRevaluedEvent event) {
        try {
            log.debug("Updating financial reports for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to update financial reports for: {}", event.getAggregateId(), e);
        }
    }

    private void publishDepreciationEvent(DomainEvent event) {
        try {
            log.debug("Publishing depreciation event for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to publish depreciation event for: {}", event.getAggregateId(), e);
        }
    }

    private void publishFinancialEvent(DomainEvent event) {
        try {
            log.debug("Publishing financial event for: {}", event.getAggregateId());
        } catch (Exception e) {
            log.error("Failed to publish financial event for: {}", event.getAggregateId(), e);
        }
    }
}
