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

import io.github.erp.repository.search.AuditTrailEventSearchRepository;
import io.github.erp.service.elasticsearch.document.AuditEventDocument;
import io.github.erp.web.rest.dto.AuditEventDTO;
import io.github.erp.web.rest.dto.AuditSearchRequestDTO;
import io.github.erp.web.rest.dto.AuditSearchResponseDTO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ElasticsearchAuditSearchService {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchAuditSearchService.class);

    private final AuditTrailEventSearchRepository searchRepository;
    private final ElasticsearchRestTemplate elasticsearchTemplate;

    @Autowired
    public ElasticsearchAuditSearchService(
        AuditTrailEventSearchRepository searchRepository,
        ElasticsearchRestTemplate elasticsearchTemplate
    ) {
        this.searchRepository = searchRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public AuditSearchResponseDTO performAdvancedSearch(AuditSearchRequestDTO request) {
        try {
            long startTime = System.currentTimeMillis();
            
            log.debug("Performing advanced audit search with query: {}", request.getQuery());
            
            NativeSearchQuery nativeSearchQuery = buildNativeSearchQuery(request);
            List<AuditEventDocument> documents = elasticsearchTemplate
                .search(nativeSearchQuery, AuditEventDocument.class)
                .map(hit -> hit.getContent())
                .stream()
                .collect(java.util.stream.Collectors.toList());
            
            AuditSearchResponseDTO result = buildSearchResponseFromDocuments(documents);
            result.setTook((int) (System.currentTimeMillis() - startTime));
            
            log.debug("Advanced search completed in {} ms, found {} hits", 
                result.getTook(), result.getTotalHits());
            
            return result;
        } catch (Exception e) {
            log.error("Failed to perform advanced audit search", e);
            throw new RuntimeException("Failed to perform advanced audit search", e);
        }
    }

    private NativeSearchQuery buildNativeSearchQuery(AuditSearchRequestDTO request) {
        QueryBuilder query = buildQuery(request);
        
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(query);
        nativeSearchQuery.setPageable(buildPageable(request));
        
        return nativeSearchQuery;
    }

    private QueryBuilder buildQuery(AuditSearchRequestDTO request) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (request.getQuery() != null && !request.getQuery().trim().isEmpty()) {
            boolQuery.must(QueryBuilders.queryStringQuery(request.getQuery())
                .field("searchableText")
                .field("userAgent")
                .field("metadata.reason"));
        }

        if (request.getFilters() != null) {
            addFilters(boolQuery, request.getFilters());
        }

        if (!boolQuery.hasClauses()) {
            return QueryBuilders.matchAllQuery();
        }

        return boolQuery;
    }

    private void addFilters(BoolQueryBuilder boolQuery, AuditSearchRequestDTO.FiltersDTO filters) {
        if (filters.getEntityTypes() != null && !filters.getEntityTypes().isEmpty()) {
            boolQuery.filter(QueryBuilders.termsQuery("aggregateType", filters.getEntityTypes()));
        }

        if (filters.getEventTypes() != null && !filters.getEventTypes().isEmpty()) {
            boolQuery.filter(QueryBuilders.termsQuery("eventType", filters.getEventTypes()));
        }

        if (filters.getUserIds() != null && !filters.getUserIds().isEmpty()) {
            boolQuery.filter(QueryBuilders.termsQuery("userId", filters.getUserIds()));
        }

        if (filters.getIpAddresses() != null && !filters.getIpAddresses().isEmpty()) {
            boolQuery.filter(QueryBuilders.termsQuery("ipAddress", filters.getIpAddresses()));
        }

        if (filters.getDateRange() != null) {
            addDateRangeFilter(boolQuery, filters.getDateRange());
        }
    }

    private void addDateRangeFilter(BoolQueryBuilder boolQuery, AuditSearchRequestDTO.DateRangeDTO dateRange) {
        if (dateRange.getFrom() != null || dateRange.getTo() != null) {
            var rangeQuery = QueryBuilders.rangeQuery("occurredOn");
            
            if (dateRange.getFrom() != null) {
                rangeQuery.gte(dateRange.getFrom().toEpochMilli());
            }
            
            if (dateRange.getTo() != null) {
                rangeQuery.lte(dateRange.getTo().toEpochMilli());
            }
            
            boolQuery.filter(rangeQuery);
        }
    }

    private org.springframework.data.domain.Pageable buildPageable(AuditSearchRequestDTO request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;
        
        org.springframework.data.domain.Sort sort = buildSort(request);
        
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }
    
    private org.springframework.data.domain.Sort buildSort(AuditSearchRequestDTO request) {
        if (request.getSort() != null && !request.getSort().isEmpty()) {
            org.springframework.data.domain.Sort.Order[] orders = request.getSort().stream()
                .map(sortDto -> {
                    org.springframework.data.domain.Sort.Direction direction = 
                        "desc".equalsIgnoreCase(sortDto.getDirection()) 
                            ? org.springframework.data.domain.Sort.Direction.DESC 
                            : org.springframework.data.domain.Sort.Direction.ASC;
                    return new org.springframework.data.domain.Sort.Order(direction, sortDto.getField());
                })
                .toArray(org.springframework.data.domain.Sort.Order[]::new);
            return org.springframework.data.domain.Sort.by(orders);
        } else {
            return org.springframework.data.domain.Sort.by(
                org.springframework.data.domain.Sort.Direction.DESC, "occurredOn");
        }
    }


    private AuditSearchResponseDTO buildSearchResponseFromDocuments(List<AuditEventDocument> documents) {
        AuditSearchResponseDTO result = new AuditSearchResponseDTO();
        
        List<AuditEventDTO> hits = new ArrayList<>();
        for (AuditEventDocument document : documents) {
            AuditEventDTO dto = convertDocumentToAuditEventDTO(document);
            hits.add(dto);
        }
        
        result.setHits(hits);
        result.setTotalHits((long) documents.size());
        result.setAggregations(new HashMap<>());
        
        return result;
    }

    private AuditEventDTO convertDocumentToAuditEventDTO(AuditEventDocument document) {
        AuditEventDTO dto = new AuditEventDTO();
        
        if (document.getEventId() != null) {
            dto.setEventId(UUID.fromString(document.getEventId()));
        }
        dto.setEventType(document.getEventType());
        dto.setAggregateType(document.getAggregateType());
        dto.setAggregateId(document.getAggregateId());
        dto.setUserId(document.getUserId());
        dto.setIpAddress(document.getIpAddress());
        dto.setUserAgent(document.getUserAgent());
        dto.setSessionId(document.getSessionId());
        dto.setVersion(document.getVersion());
        
        if (document.getCorrelationId() != null) {
            dto.setCorrelationId(UUID.fromString(document.getCorrelationId()));
        }
        
        dto.setOccurredOn(document.getOccurredOn());
        
        return dto;
    }
}
