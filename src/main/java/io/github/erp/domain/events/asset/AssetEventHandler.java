package io.github.erp.domain.events.asset;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        log.info("Asset creation processing completed for: {}", event.getAggregateId());
    }

    private void processAssetCategoryChange(AssetCategoryChangedEvent event) {
        log.info("Asset category change processing completed for: {}", event.getAggregateId());
    }

    private void processAssetDisposal(AssetDisposedEvent event) {
        log.info("Asset disposal processing completed for: {}", event.getAggregateId());
    }

    private void processAssetRevaluation(AssetRevaluedEvent event) {
        log.info("Asset revaluation processing completed for: {}", event.getAggregateId());
    }

    private void notifyDepreciationService(DomainEvent event) {
        log.info("Notifying depreciation service for event: {} on asset: {}", 
                event.getEventType(), event.getAggregateId());
    }

    private void notifyFinancialService(DomainEvent event) {
        log.info("Notifying financial service for event: {} on asset: {}", 
                event.getEventType(), event.getAggregateId());
    }
}
