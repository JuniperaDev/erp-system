package io.github.erp.service.elasticsearch.query;

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

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditQueryTemplates {

    public QueryBuilder buildComplianceQuery(String regulationType, Instant fromDate, Instant toDate) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        boolQuery.filter(QueryBuilders.termQuery("metadata.regulatoryFramework", regulationType));
        
        if (fromDate != null || toDate != null) {
            var rangeQuery = QueryBuilders.rangeQuery("occurredOn");
            if (fromDate != null) {
                rangeQuery.gte(fromDate.toEpochMilli());
            }
            if (toDate != null) {
                rangeQuery.lte(toDate.toEpochMilli());
            }
            boolQuery.filter(rangeQuery);
        }
        
        return boolQuery;
    }

    public QueryBuilder buildEntityAuditTrailQuery(String entityType, String entityId) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        boolQuery.filter(QueryBuilders.termQuery("aggregateType", entityType));
        boolQuery.filter(QueryBuilders.termQuery("aggregateId", entityId));
        
        return boolQuery;
    }

    public QueryBuilder buildUserActivityQuery(String userId, Instant fromDate, Instant toDate) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        boolQuery.filter(QueryBuilders.termQuery("userId", userId));
        
        if (fromDate != null || toDate != null) {
            var rangeQuery = QueryBuilders.rangeQuery("occurredOn");
            if (fromDate != null) {
                rangeQuery.gte(fromDate.toEpochMilli());
            }
            if (toDate != null) {
                rangeQuery.lte(toDate.toEpochMilli());
            }
            boolQuery.filter(rangeQuery);
        }
        
        return boolQuery;
    }

    public QueryBuilder buildSecurityEventQuery(String ipAddress, Instant fromDate, Instant toDate) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        if (ipAddress != null) {
            boolQuery.filter(QueryBuilders.termQuery("ipAddress", ipAddress));
        }
        
        boolQuery.should(QueryBuilders.termQuery("eventType", "LOGIN"));
        boolQuery.should(QueryBuilders.termQuery("eventType", "LOGOUT"));
        boolQuery.should(QueryBuilders.termQuery("eventType", "FAILED_LOGIN"));
        boolQuery.should(QueryBuilders.termQuery("eventType", "PERMISSION_DENIED"));
        boolQuery.minimumShouldMatch(1);
        
        if (fromDate != null || toDate != null) {
            var rangeQuery = QueryBuilders.rangeQuery("occurredOn");
            if (fromDate != null) {
                rangeQuery.gte(fromDate.toEpochMilli());
            }
            if (toDate != null) {
                rangeQuery.lte(toDate.toEpochMilli());
            }
            boolQuery.filter(rangeQuery);
        }
        
        return boolQuery;
    }

    public QueryBuilder buildDataChangeQuery(String entityType, String changeType, Instant fromDate, Instant toDate) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        
        if (entityType != null) {
            boolQuery.filter(QueryBuilders.termQuery("aggregateType", entityType));
        }
        
        if (changeType != null) {
            boolQuery.filter(QueryBuilders.termQuery("eventType", changeType));
        }
        
        if (fromDate != null || toDate != null) {
            var rangeQuery = QueryBuilders.rangeQuery("occurredOn");
            if (fromDate != null) {
                rangeQuery.gte(fromDate.toEpochMilli());
            }
            if (toDate != null) {
                rangeQuery.lte(toDate.toEpochMilli());
            }
            boolQuery.filter(rangeQuery);
        }
        
        return boolQuery;
    }

    public QueryBuilder buildFullTextSearchQuery(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return QueryBuilders.matchAllQuery();
        }
        
        return QueryBuilders.multiMatchQuery(searchText)
            .field("searchableText", 2.0f)
            .field("userAgent")
            .field("metadata.reason")
            .field("aggregateId")
            .field("userId")
            .type(org.elasticsearch.index.query.MultiMatchQueryBuilder.Type.BEST_FIELDS);
    }
}
