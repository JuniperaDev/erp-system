package io.github.erp.docmgmt.service.mapper;

import io.github.erp.docmgmt.domain.Document;
import io.github.erp.docmgmt.service.dto.DocumentDTO;
import io.github.erp.service.mapper.EntityMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {

    @Mapping(target = "createdById", source = "createdBy.id")
    @Mapping(target = "createdByLogin", source = "createdBy.login")
    @Mapping(target = "lastModifiedById", source = "lastModifiedBy.id")
    @Mapping(target = "lastModifiedByLogin", source = "lastModifiedBy.login")
    @Mapping(target = "originatingDepartmentId", source = "originatingDepartment.id")
    @Mapping(target = "originatingDepartmentDealerName", source = "originatingDepartment.dealerName")
    @Mapping(target = "checksumAlgorithmId", source = "checksumAlgorithm.id")
    @Mapping(target = "checksumAlgorithmName", source = "checksumAlgorithm.name")
    @Mapping(target = "securityClearanceId", source = "securityClearance.id")
    @Mapping(target = "securityClearanceClearanceLevel", source = "securityClearance.clearanceLevel")
    DocumentDTO toDto(Document s);

    @Mapping(target = "createdBy", source = "createdById", qualifiedByName = "applicationUserId")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedById", qualifiedByName = "applicationUserId")
    @Mapping(target = "originatingDepartment", source = "originatingDepartmentId", qualifiedByName = "dealerId")
    @Mapping(target = "checksumAlgorithm", source = "checksumAlgorithmId", qualifiedByName = "algorithmId")
    @Mapping(target = "securityClearance", source = "securityClearanceId", qualifiedByName = "securityClearanceId")
    Document toEntity(DocumentDTO documentDTO);

    default Document fromId(Long id) {
        if (id == null) {
            return null;
        }
        Document document = new Document();
        document.setId(id);
        return document;
    }
}
