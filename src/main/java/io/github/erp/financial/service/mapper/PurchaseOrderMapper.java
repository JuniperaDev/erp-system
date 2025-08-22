package io.github.erp.financial.service.mapper;

import io.github.erp.financial.domain.PurchaseOrder;
import io.github.erp.financial.service.dto.PurchaseOrderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PurchaseOrder} and its DTO {@link PurchaseOrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PurchaseOrderMapper extends EntityMapper<PurchaseOrderDTO, PurchaseOrder> {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "purchaseOrderNumber", source = "purchaseOrderNumber")
    @Mapping(target = "purchaseOrderDate", source = "purchaseOrderDate")
    @Mapping(target = "purchaseOrderAmount", source = "purchaseOrderAmount")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "fileUploadToken", source = "fileUploadToken")
    @Mapping(target = "compilationToken", source = "compilationToken")
    @Mapping(target = "remarks", source = "remarks")
    PurchaseOrderDTO toDto(PurchaseOrder purchaseOrder);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "purchaseOrderNumber", source = "purchaseOrderNumber")
    @Mapping(target = "purchaseOrderDate", source = "purchaseOrderDate")
    @Mapping(target = "purchaseOrderAmount", source = "purchaseOrderAmount")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "notes", source = "notes")
    @Mapping(target = "fileUploadToken", source = "fileUploadToken")
    @Mapping(target = "compilationToken", source = "compilationToken")
    @Mapping(target = "remarks", source = "remarks")
    PurchaseOrder toEntity(PurchaseOrderDTO purchaseOrderDTO);
}
