package io.github.erp.repository.search;

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

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.web.rest.dto.AuditSearchRequestDTO;
import io.github.erp.web.rest.dto.AuditSearchResponseDTO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AuditTrailEventSearchRepository
    extends ElasticsearchRepository<AuditTrailEvent, Long>, AuditTrailEventSearchRepositoryInternal {}

interface AuditTrailEventSearchRepositoryInternal {
    Page<AuditTrailEvent> search(String query, Pageable pageable);
    AuditSearchResponseDTO advancedSearch(AuditSearchRequestDTO request);
    Map<String, Object> searchWithAggregations(AuditSearchRequestDTO request);
}

class AuditTrailEventSearchRepositoryInternalImpl implements AuditTrailEventSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AuditTrailEventSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<AuditTrailEvent> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<AuditTrailEvent> hits = elasticsearchTemplate
            .search(nativeSearchQuery, AuditTrailEvent.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }

    @Override
    public AuditSearchResponseDTO advancedSearch(AuditSearchRequestDTO request) {
        return null;
    }

    @Override
    public Map<String, Object> searchWithAggregations(AuditSearchRequestDTO request) {
        return null;
    }
}
