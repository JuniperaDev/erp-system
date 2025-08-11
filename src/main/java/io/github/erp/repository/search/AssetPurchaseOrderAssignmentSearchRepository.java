package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.AssetPurchaseOrderAssignment;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

public interface AssetPurchaseOrderAssignmentSearchRepository extends ElasticsearchRepository<AssetPurchaseOrderAssignment, Long>, AssetPurchaseOrderAssignmentSearchRepositoryInternal {
}

interface AssetPurchaseOrderAssignmentSearchRepositoryInternal {
    Page<AssetPurchaseOrderAssignment> search(String query, Pageable pageable);
}

class AssetPurchaseOrderAssignmentSearchRepositoryInternalImpl implements AssetPurchaseOrderAssignmentSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AssetPurchaseOrderAssignmentSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<AssetPurchaseOrderAssignment> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<AssetPurchaseOrderAssignment> hits = elasticsearchTemplate
            .search(nativeSearchQuery, AssetPurchaseOrderAssignment.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
