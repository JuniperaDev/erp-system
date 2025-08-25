package io.github.erp.internal.service.cache;

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

import io.github.erp.domain.events.asset.AssetCreatedEvent;
import io.github.erp.domain.events.asset.AssetCategoryChangedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CacheInvalidationService {

    private final Logger log = LoggerFactory.getLogger(CacheInvalidationService.class);

    @EventListener
    @CacheEvict(value = {"assetRegistrations", "assetRegistrationIds"}, allEntries = true, cacheManager = "caffeineCacheManager")
    public void handleAssetCreated(AssetCreatedEvent event) {
        log.debug("Asset created event received for asset {}, clearing asset cache", event.getAssetRegistrationNumber());
    }

    @EventListener
    @CacheEvict(value = {"assetRegistrations", "assetRegistrationIds"}, allEntries = true, cacheManager = "caffeineCacheManager")
    public void handleAssetCategoryChanged(AssetCategoryChangedEvent event) {
        log.debug("Asset category changed event received for asset {}, clearing asset cache", event.getAssetRegistrationNumber());
    }

    @CacheEvict(value = {"assetRegistrations", "assetRegistrationIds", "transactionAccounts", "transactionAccountIds"}, allEntries = true, cacheManager = "caffeineCacheManager")
    public void clearAllCaches() {
        log.info("Clearing all Caffeine caches");
    }
}
