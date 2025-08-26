package io.github.erp.docmgmt.service;

import io.github.erp.docmgmt.domain.*;
import io.github.erp.docmgmt.domain.Document;
import io.github.erp.docmgmt.repository.DocumentRepository;
import io.github.erp.docmgmt.repository.search.DocumentSearchRepository;
import io.github.erp.docmgmt.service.criteria.DocumentCriteria;
import io.github.erp.docmgmt.service.dto.DocumentDTO;
import io.github.erp.docmgmt.service.mapper.DocumentMapper;
import io.github.erp.domain.*;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class DocumentQueryService extends QueryService<Document> {

    private final Logger log = LoggerFactory.getLogger(DocumentQueryService.class);

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    private final DocumentSearchRepository documentSearchRepository;

    public DocumentQueryService(
        DocumentRepository documentRepository,
        DocumentMapper documentMapper,
        DocumentSearchRepository documentSearchRepository
    ) {
        this.documentRepository = documentRepository;
        this.documentMapper = documentMapper;
        this.documentSearchRepository = documentSearchRepository;
    }

    @Transactional(readOnly = true)
    public List<DocumentDTO> findByCriteria(DocumentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentMapper.toDto(documentRepository.findAll(specification));
    }

    @Transactional(readOnly = true)
    public Page<DocumentDTO> findByCriteria(DocumentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.findAll(specification, page).map(documentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public long countByCriteria(DocumentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Document> specification = createSpecification(criteria);
        return documentRepository.count(specification);
    }

    protected Specification<Document> createSpecification(DocumentCriteria criteria) {
        Specification<Document> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Document_.id));
            }
            if (criteria.getDocumentTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDocumentTitle(), Document_.documentTitle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Document_.description));
            }
            if (criteria.getContentType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContentType(), Document_.contentType));
            }
            if (criteria.getFileSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFileSize(), Document_.fileSize));
            }
            if (criteria.getChecksum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChecksum(), Document_.checksum));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Document_.createdDate));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Document_.lastModifiedDate));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(Document_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastModifiedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastModifiedById(),
                            root -> root.join(Document_.lastModifiedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getOriginatingDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOriginatingDepartmentId(),
                            root -> root.join(Document_.originatingDepartment, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getChecksumAlgorithmId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getChecksumAlgorithmId(),
                            root -> root.join(Document_.checksumAlgorithm, JoinType.LEFT).get(Algorithm_.id)
                        )
                    );
            }
            if (criteria.getSecurityClearanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityClearanceId(),
                            root -> root.join(Document_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
