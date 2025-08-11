package io.github.erp.docmgmt.service.impl;

import io.github.erp.docmgmt.domain.Document;
import io.github.erp.docmgmt.repository.DocumentRepository;
import io.github.erp.docmgmt.repository.search.DocumentSearchRepository;
import io.github.erp.docmgmt.service.DocumentService;
import io.github.erp.docmgmt.service.dto.DocumentDTO;
import io.github.erp.docmgmt.service.mapper.DocumentMapper;
import io.github.erp.docmgmt.service.criteria.DocumentCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service("documentService")
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;
    private final DocumentSearchRepository documentSearchRepository;

    public DocumentServiceImpl(
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        DocumentSearchRepository documentSearchRepository
    ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.documentSearchRepository = documentSearchRepository;
    }

    @Override
    public DocumentDTO save(DocumentDTO documentDTO) {
        log.debug("Request to save Document : {}", documentDTO);
        Document document = documentMapper.toEntity(documentDTO);
        document = documentRepository.save(document);
        DocumentDTO result = documentMapper.toDto(document);
        documentSearchRepository.save(document);
        return result;
    }

    @Override
    public Optional<DocumentDTO> partialUpdate(DocumentDTO documentDTO) {
        log.debug("Request to partially update Document : {}", documentDTO);

        return documentRepository
            .findById(documentDTO.getId())
            .map(existingDocument -> {
                documentMapper.partialUpdate(existingDocument, documentDTO);
                return existingDocument;
            })
            .map(documentRepository::save)
            .map(document -> {
                documentSearchRepository.save(document);
                return documentMapper.toDto(document);
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Documents");
        return documentRepository.findAll(pageable).map(documentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByCriteria(DocumentCriteria criteria, Pageable pageable) {
        log.debug("Request to get Documents by criteria: {}", criteria);
        return documentRepository.findAll(pageable).map(documentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByCriteria(DocumentCriteria criteria) {
        log.debug("Request to count Documents by criteria: {}", criteria);
        return documentRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DocumentDTO> findOne(Long id) {
        log.debug("Request to get Document : {}", id);
        return documentRepository.findById(id).map(documentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Document : {}", id);
        documentRepository.deleteById(id);
        documentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search Documents for query {}", query);
        return documentSearchRepository.search(query, pageable).map(documentMapper::toDto);
    }
}
