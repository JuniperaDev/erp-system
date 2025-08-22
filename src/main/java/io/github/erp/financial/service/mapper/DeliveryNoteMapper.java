package io.github.erp.financial.service.mapper;

import io.github.erp.financial.domain.DeliveryNote;
import io.github.erp.financial.service.dto.DeliveryNoteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DeliveryNote} and its DTO {@link DeliveryNoteDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DeliveryNoteMapper extends EntityMapper<DeliveryNoteDTO, DeliveryNote> {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "deliveryNoteNumber", source = "deliveryNoteNumber")
    @Mapping(target = "documentDate", source = "documentDate")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "serialNumber", source = "serialNumber")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "remarks", source = "remarks")
    DeliveryNoteDTO toDto(DeliveryNote deliveryNote);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "deliveryNoteNumber", source = "deliveryNoteNumber")
    @Mapping(target = "documentDate", source = "documentDate")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "serialNumber", source = "serialNumber")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "remarks", source = "remarks")
    DeliveryNote toEntity(DeliveryNoteDTO deliveryNoteDTO);
}
