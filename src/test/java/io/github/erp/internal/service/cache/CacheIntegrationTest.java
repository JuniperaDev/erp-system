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

import io.github.erp.IntegrationTest;
import io.github.erp.internal.service.assets.InternalAssetRegistrationService;
import io.github.erp.internal.service.ledgers.InternalTransactionAccountService;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.TransactionAccountDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@SpringJUnitConfig
@Transactional
class CacheIntegrationTest {

    @Autowired
    private InternalAssetRegistrationService assetRegistrationService;

    @Autowired
    private InternalTransactionAccountService transactionAccountService;

    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager caffeineCacheManager;

    @Test
    void testAssetRegistrationCaching() {
        Cache assetCache = caffeineCacheManager.getCache("assetRegistrations");
        assertThat(assetCache).isNotNull();

        assetCache.clear();

        Optional<AssetRegistrationDTO> asset = assetRegistrationService.findOne(1L);
        if (asset.isPresent()) {
            Cache.ValueWrapper cachedValue = assetCache.get(1L);
            assertThat(cachedValue).isNotNull();
            assertThat(cachedValue.get()).isEqualTo(asset.get());
        }
    }

    @Test
    void testTransactionAccountCaching() {
        Cache accountCache = caffeineCacheManager.getCache("transactionAccounts");
        assertThat(accountCache).isNotNull();

        accountCache.clear();

        Optional<TransactionAccountDTO> account = transactionAccountService.findOne(1L);
        if (account.isPresent()) {
            Cache.ValueWrapper cachedValue = accountCache.get(1L);
            assertThat(cachedValue).isNotNull();
            assertThat(cachedValue.get()).isEqualTo(account.get());
        }
    }

    @Test
    void testCacheEvictionOnSave() {
        Cache assetCache = caffeineCacheManager.getCache("assetRegistrations");
        assertThat(assetCache).isNotNull();

        assetCache.clear();

        Optional<AssetRegistrationDTO> asset = assetRegistrationService.findOne(1L);
        if (asset.isPresent()) {
            AssetRegistrationDTO assetDto = asset.get();
            assetDto.setAssetDetails("Updated details");
            
            assetRegistrationService.save(assetDto);
            
            Cache.ValueWrapper cachedValue = assetCache.get(1L);
            assertThat(cachedValue).isNull();
        }
    }
}
