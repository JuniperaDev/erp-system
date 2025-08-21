package io.github.erp.web.rest;

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

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import io.github.erp.internal.service.cache.CacheInvalidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/cache")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class CacheMonitoringResource {

    private final Logger log = LoggerFactory.getLogger(CacheMonitoringResource.class);

    private final CacheManager caffeineCacheManager;
    private final CacheInvalidationService cacheInvalidationService;

    public CacheMonitoringResource(
        @Qualifier("caffeineCacheManager") CacheManager caffeineCacheManager,
        CacheInvalidationService cacheInvalidationService
    ) {
        this.caffeineCacheManager = caffeineCacheManager;
        this.cacheInvalidationService = cacheInvalidationService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        log.debug("REST request to get cache statistics");
        
        Map<String, Object> stats = new HashMap<>();
        
        for (String cacheName : caffeineCacheManager.getCacheNames()) {
            Cache cache = caffeineCacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache) {
                CaffeineCache caffeineCache = (CaffeineCache) cache;
                CacheStats cacheStats = caffeineCache.getNativeCache().stats();
                
                Map<String, Object> cacheStatMap = new HashMap<>();
                cacheStatMap.put("hitCount", cacheStats.hitCount());
                cacheStatMap.put("missCount", cacheStats.missCount());
                cacheStatMap.put("hitRate", cacheStats.hitRate());
                cacheStatMap.put("evictionCount", cacheStats.evictionCount());
                cacheStatMap.put("requestCount", cacheStats.requestCount());
                
                stats.put(cacheName, cacheStatMap);
            }
        }
        
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/clear/{cacheName}")
    public ResponseEntity<Void> clearCache(@PathVariable String cacheName) {
        log.debug("REST request to clear cache: {}", cacheName);
        
        Cache cache = caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/clear-all")
    public ResponseEntity<Void> clearAllCaches() {
        log.debug("REST request to clear all caches");
        cacheInvalidationService.clearAllCaches();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/names")
    public ResponseEntity<Iterable<String>> getCacheNames() {
        log.debug("REST request to get cache names");
        return ResponseEntity.ok(caffeineCacheManager.getCacheNames());
    }
}
