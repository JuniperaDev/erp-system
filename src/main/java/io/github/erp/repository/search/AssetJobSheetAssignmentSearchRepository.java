package io.github.erp.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import io.github.erp.domain.AssetJobSheetAssignment;
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

public interface AssetJobSheetAssignmentSearchRepository extends ElasticsearchRepository<AssetJobSheetAssignment, Long>, AssetJobSheetAssignmentSearchRepositoryInternal {
}

interface AssetJobSheetAssignmentSearchRepositoryInternal {
    Page<AssetJobSheetAssignment> search(String query, Pageable pageable);
}

class AssetJobSheetAssignmentSearchRepositoryInternalImpl implements AssetJobSheetAssignmentSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AssetJobSheetAssignmentSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<AssetJobSheetAssignment> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<AssetJobSheetAssignment> hits = elasticsearchTemplate
            .search(nativeSearchQuery, AssetJobSheetAssignment.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
