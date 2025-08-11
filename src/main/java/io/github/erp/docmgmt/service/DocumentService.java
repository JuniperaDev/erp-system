package io.github.erp.docmgmt.service;

import io.github.erp.docmgmt.service.dto.DocumentDTO;
import io.github.erp.docmgmt.service.criteria.DocumentCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DocumentService {

    DocumentDTO save(DocumentDTO documentDTO);

    Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO);

    Page<DocumentDTO> findAll(Pageable pageable);

    Page<DocumentDTO> findByCriteria(DocumentCriteria criteria, Pageable pageable);

    Long countByCriteria(DocumentCriteria criteria);

    Optional<DocumentDTO> findOne(Long id);

    void delete(Long id);

    Page<DocumentDTO> search(String query, Pageable pageable);
}
