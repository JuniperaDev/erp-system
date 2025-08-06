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
        }
    }

    private void processAssetCreation(AssetCreatedEvent event) {
        log.info("Asset creation processing completed for: {}", event.getAggregateId());
    }
}
