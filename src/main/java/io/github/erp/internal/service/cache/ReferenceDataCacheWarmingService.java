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

import io.github.erp.erp.startUp.cache.AbstractStartupCacheUpdateService;
import io.github.erp.internal.ErpCacheProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service("referenceDataCacheWarmingService")
public class ReferenceDataCacheWarmingService extends AbstractStartupCacheUpdateService {

    private final Logger log = LoggerFactory.getLogger(ReferenceDataCacheWarmingService.class);

    public ReferenceDataCacheWarmingService(ErpCacheProperties cacheProperties) {
        super(cacheProperties);
    }

    @Override
    public void refreshCache() {
        log.info("Warming reference data caches...");
        log.info("Reference data cache warming completed");
    }

    @Override
    @CacheEvict(value = "referenceData", allEntries = true, cacheManager = "caffeineCacheManager")
    public void clearCache() {
        log.info("Clearing reference data cache");
    }

    @Scheduled(cron = "0 30 19 * * *") // Run every day at 19:30 (7:30 PM)
    public void refreshCacheAt1930Hours() {
        refreshCache();
    }
}
