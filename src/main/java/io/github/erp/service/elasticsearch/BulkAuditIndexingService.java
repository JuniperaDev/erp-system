package io.github.erp.service.elasticsearch;

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

import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.service.elasticsearch.document.AuditEventDocument;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BulkAuditIndexingService {

    private static final Logger log = LoggerFactory.getLogger(BulkAuditIndexingService.class);
    private static final int BATCH_SIZE = 1000;

    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public BulkAuditIndexingService(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.objectMapper = new ObjectMapper();
    }

    public void bulkIndexHistoricalData(List<AuditTrailEvent> events) {
        if (events == null || events.isEmpty()) {
            log.info("No events to index");
            return;
        }

        log.info("Starting bulk indexing of {} audit events", events.size());
        long startTime = System.currentTimeMillis();
        int totalIndexed = 0;
        int totalFailed = 0;

        try {
            for (int i = 0; i < events.size(); i += BATCH_SIZE) {
                int endIndex = Math.min(i + BATCH_SIZE, events.size());
                List<AuditTrailEvent> batch = events.subList(i, endIndex);
                
                BulkIndexResult result = indexBatch(batch);
                totalIndexed += result.getSuccessCount();
                totalFailed += result.getFailureCount();
                
                log.debug("Processed batch {}-{}: {} indexed, {} failed", 
                    i + 1, endIndex, result.getSuccessCount(), result.getFailureCount());
            }

            long duration = System.currentTimeMillis() - startTime;
            log.info("Bulk indexing completed: {} indexed, {} failed in {} ms", 
                totalIndexed, totalFailed, duration);

        } catch (Exception e) {
            log.error("Bulk indexing failed", e);
            throw new RuntimeException("Bulk indexing failed", e);
        }
    }

    private BulkIndexResult indexBatch(List<AuditTrailEvent> events) {
        try {
            List<IndexQuery> indexQueries = new ArrayList<>();
            
            for (AuditTrailEvent event : events) {
                AuditEventDocument document = transformToDocument(event);
                String indexName = determineIndexName(event.getOccurredOn());
                
                IndexQuery indexQuery = new IndexQueryBuilder()
                    .withId(event.getEventId().toString())
                    .withObject(document)
                    .build();
                
                indexQueries.add(indexQuery);
            }

            elasticsearchTemplate.bulkIndex(indexQueries, IndexCoordinates.of(determineIndexName(events.get(0).getOccurredOn())));
            
            log.info("Successfully bulk indexed {} events", events.size());
            return new BulkIndexResult(events.size(), 0);
            
        } catch (Exception e) {
            log.error("Failed to index batch of {} events", events.size(), e);
            return new BulkIndexResult(0, events.size());
        }
    }

    private AuditEventDocument transformToDocument(AuditTrailEvent event) {
        AuditEventDocument document = new AuditEventDocument();
        document.setEventId(event.getEventId().toString());
        document.setEventType(event.getEventType());
        document.setAggregateType(event.getAggregateType());
        document.setAggregateId(event.getAggregateId());
        document.setOccurredOn(event.getOccurredOn());
        document.setVersion(event.getVersion());
        document.setCorrelationId(event.getCorrelationId() != null ? event.getCorrelationId().toString() : null);
        document.setUserId(event.getUserId());
        document.setIpAddress(event.getIpAddress());
        document.setUserAgent(event.getUserAgent());
        document.setProcessed(event.getProcessed());
        document.setRetryCount(event.getRetryCount());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("actionType", event.getActionType());
        metadata.put("entityName", event.getEntityName());
        metadata.put("oldValues", event.getOldValues());
        metadata.put("newValues", event.getNewValues());
        document.setMetadata(metadata);
        
        String searchableText = buildSearchableText(event);
        document.setSearchableText(searchableText);
        
        return document;
    }

    private String buildSearchableText(AuditTrailEvent event) {
        StringBuilder searchText = new StringBuilder();
        searchText.append(event.getEventType()).append(" ");
        searchText.append(event.getAggregateType()).append(" ");
        searchText.append(event.getAggregateId()).append(" ");
        if (event.getUserId() != null) {
            searchText.append(event.getUserId()).append(" ");
        }
        if (event.getUserAgent() != null) {
            searchText.append(event.getUserAgent()).append(" ");
        }
        if (event.getActionType() != null) {
            searchText.append(event.getActionType()).append(" ");
        }
        if (event.getEntityName() != null) {
            searchText.append(event.getEntityName()).append(" ");
        }
        return searchText.toString().trim();
    }

    private String determineIndexName(Instant occurredOn) {
        String monthSuffix = java.time.format.DateTimeFormatter.ofPattern("yyyy.MM")
            .format(occurredOn.atZone(java.time.ZoneOffset.UTC));
        return "audit-events-" + monthSuffix;
    }

    private static class BulkIndexResult {
        private final int successCount;
        private final int failureCount;

        public BulkIndexResult(int successCount, int failureCount) {
            this.successCount = successCount;
            this.failureCount = failureCount;
        }

        public int getSuccessCount() {
            return successCount;
        }

        public int getFailureCount() {
            return failureCount;
        }
    }
}
