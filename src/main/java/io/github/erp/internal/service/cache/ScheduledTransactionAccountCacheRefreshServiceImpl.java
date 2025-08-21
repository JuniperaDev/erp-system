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
import io.github.erp.internal.service.ledgers.InternalTransactionAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service("scheduledTransactionAccountCacheRefreshService")
public class ScheduledTransactionAccountCacheRefreshServiceImpl extends AbstractStartupCacheUpdateService implements ScheduledCacheRefreshService {

    private final InternalTransactionAccountService internalDomainService;

    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager caffeineCacheManager;

    public ScheduledTransactionAccountCacheRefreshServiceImpl(ErpCacheProperties cacheProperties, InternalTransactionAccountService internalDomainService) {
        super(cacheProperties);
        this.internalDomainService = internalDomainService;
    }

    public void refreshCache() {
        if (caffeineCacheManager.getCache("transactionAccounts") != null) {
            caffeineCacheManager.getCache("transactionAccounts").clear();
        }
        if (caffeineCacheManager.getCache("transactionAccountIds") != null) {
            caffeineCacheManager.getCache("transactionAccountIds").clear();
        }
        
        // Fetch all IDs to warm both caches
        List<Long> ids = internalDomainService.findAllIds();
        for (Long id : ids) {
            internalDomainService.findOne(id); // Warms both Hazelcast and Caffeine
        }
    }

    @Scheduled(cron = "0 0 19 * * *") // Run every day at 19:00 (7:00 PM)
    public void refreshCacheAt1900Hours() {
        refreshCache();
    }

    public void refreshDefinedCacheItems(List<Long> ids) {
        // Fetch all IDs or a subset of IDs
        for (Long id : ids) {
            internalDomainService.findOne(id);
        }
    }

    @CacheEvict(cacheNames = "transactionAccounts", allEntries = true)
    public void clearCache() {
        if (caffeineCacheManager.getCache("transactionAccounts") != null) {
            caffeineCacheManager.getCache("transactionAccounts").clear();
        }
        if (caffeineCacheManager.getCache("transactionAccountIds") != null) {
            caffeineCacheManager.getCache("transactionAccountIds").clear();
        }
    }
}
