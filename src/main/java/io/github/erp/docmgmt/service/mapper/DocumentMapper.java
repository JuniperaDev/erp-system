package io.github.erp.docmgmt.service.mapper;

import io.github.erp.docmgmt.domain.Document;
import io.github.erp.docmgmt.service.dto.DocumentDTO;
import io.github.erp.service.mapper.EntityMapper;
import io.github.erp.service.mapper.ApplicationUserMapper;
import io.github.erp.service.mapper.DealerMapper;
import io.github.erp.service.mapper.AlgorithmMapper;
import io.github.erp.service.mapper.SecurityClearanceMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {ApplicationUserMapper.class, DealerMapper.class, AlgorithmMapper.class, SecurityClearanceMapper.class})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByLogin", source = "createdBy.applicationIdentity")
    @Mapping(target = "lastModifiedById", source = "lastModifiedBy.id")
    @Mapping(target = "lastModifiedByLogin", source = "lastModifiedBy.applicationIdentity")
    @Mapping(target = "originatingDepartmentId", source = "originatingDepartment.id")
    @Mapping(target = "originatingDepartmentDealerName", source = "originatingDepartment.dealerName")
    @Mapping(target = "checksumAlgorithmId", source = "checksumAlgorithm.id")
    @Mapping(target = "checksumAlgorithmName", source = "checksumAlgorithm.name")
    @Mapping(target = "securityClearanceId", source = "securityClearance.id")
    @Mapping(target = "securityClearanceClearanceLevel", source = "securityClearance.clearanceLevel")
    DocumentDTO toDto(Document s);

    Document toEntity(DocumentDTO documentDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Document partialUpdate(DocumentDTO documentDTO, @MappingTarget Document document);

    default Document fromId(Long id) {
        if (id == null) {
            return null;
        }
        Document document = new Document();
        document.setId(id);
        return document;
    }
}
