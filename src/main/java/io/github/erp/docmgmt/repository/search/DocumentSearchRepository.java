package io.github.erp.docmgmt.repository.search;

import io.github.erp.docmgmt.domain.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface DocumentSearchRepository extends ElasticsearchRepository<Document, Long> {
    Page<Document> search(String query, Pageable pageable);
}
