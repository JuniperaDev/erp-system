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
import io.github.erp.web.rest.dto.AuditEventDTO;
import io.github.erp.web.rest.dto.AuditSearchRequestDTO;
import io.github.erp.web.rest.dto.AuditSearchResponseDTO;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

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
            
            AuditSearchResponseDTO result = new AuditSearchResponseDTO();
            result.setHits(new ArrayList<>());
            result.setTotalHits(0L);
            result.setAggregations(new HashMap<>());
            
            result.setTook((int) (System.currentTimeMillis() - startTime));
            
            log.debug("Advanced search completed in {} ms, found {} hits", 
                result.getTook(), result.getTotalHits());
            
            return result;
        } catch (Exception e) {
            log.error("Failed to perform advanced audit search", e);
            throw new RuntimeException("Failed to perform advanced audit search", e);
        }
    }

    private SearchRequest buildSearchRequest(AuditSearchRequestDTO request) {
        SearchRequest searchRequest = new SearchRequest("audit-events-*");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        QueryBuilder query = buildQuery(request);
        sourceBuilder.query(query);

        addSorting(sourceBuilder, request);
        addPagination(sourceBuilder, request);
        addAggregations(sourceBuilder, request);

        searchRequest.source(sourceBuilder);
        return searchRequest;
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

    private void addSorting(SearchSourceBuilder sourceBuilder, AuditSearchRequestDTO request) {
        if (request.getSort() != null && !request.getSort().isEmpty()) {
            for (AuditSearchRequestDTO.SortDTO sort : request.getSort()) {
                SortOrder order = "desc".equalsIgnoreCase(sort.getDirection()) 
                    ? SortOrder.DESC : SortOrder.ASC;
                sourceBuilder.sort(sort.getField(), order);
            }
        } else {
            sourceBuilder.sort("occurredOn", SortOrder.DESC);
        }
    }

    private void addPagination(SearchSourceBuilder sourceBuilder, AuditSearchRequestDTO request) {
        int page = request.getPage() != null ? request.getPage() : 0;
        int size = request.getSize() != null ? request.getSize() : 20;
        
        sourceBuilder.from(page * size);
        sourceBuilder.size(size);
    }

    private void addAggregations(SearchSourceBuilder sourceBuilder, AuditSearchRequestDTO request) {
        if (request.getAggregations() != null && !request.getAggregations().isEmpty()) {
            for (AuditSearchRequestDTO.AggregationDTO agg : request.getAggregations()) {
                AggregationBuilder aggregation = buildAggregation(agg);
                if (aggregation != null) {
                    sourceBuilder.aggregation(aggregation);
                }
            }
        }
    }

    private AggregationBuilder buildAggregation(AuditSearchRequestDTO.AggregationDTO agg) {
        switch (agg.getType().toLowerCase()) {
            case "terms":
                return AggregationBuilders.terms(agg.getField() + "_terms")
                    .field(agg.getField())
                    .size(100);
            case "date_histogram":
                return AggregationBuilders.dateHistogram(agg.getField() + "_histogram")
                    .field(agg.getField())
                    .calendarInterval(org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval.DAY);
            default:
                log.warn("Unsupported aggregation type: {}", agg.getType());
                return null;
        }
    }

    private AuditSearchResponseDTO buildSearchResponse(SearchResponse response) {
        AuditSearchResponseDTO result = new AuditSearchResponseDTO();
        
        List<AuditEventDTO> hits = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {
            AuditEventDTO dto = convertToAuditEventDTO(hit);
            hits.add(dto);
        }
        
        result.setHits(hits);
        result.setTotalHits(response.getHits().getTotalHits().value);
        
        Map<String, Object> aggregations = new HashMap<>();
        if (response.getAggregations() != null) {
            response.getAggregations().forEach(agg -> {
                aggregations.put(agg.getName(), agg);
            });
        }
        result.setAggregations(aggregations);
        
        return result;
    }

    private AuditEventDTO convertToAuditEventDTO(SearchHit hit) {
        Map<String, Object> source = hit.getSourceAsMap();
        AuditEventDTO dto = new AuditEventDTO();
        
        if (source.get("eventId") != null) {
            dto.setEventId(UUID.fromString((String) source.get("eventId")));
        }
        dto.setEventType((String) source.get("eventType"));
        dto.setAggregateType((String) source.get("aggregateType"));
        dto.setAggregateId((String) source.get("aggregateId"));
        dto.setUserId((String) source.get("userId"));
        dto.setIpAddress((String) source.get("ipAddress"));
        dto.setUserAgent((String) source.get("userAgent"));
        dto.setSessionId((String) source.get("sessionId"));
        
        if (source.get("version") != null) {
            dto.setVersion((Integer) source.get("version"));
        }
        
        if (source.get("correlationId") != null) {
            dto.setCorrelationId(UUID.fromString((String) source.get("correlationId")));
        }
        
        if (source.get("occurredOn") != null) {
            dto.setOccurredOn(Instant.ofEpochMilli((Long) source.get("occurredOn")));
        }
        
        return dto;
    }
}
