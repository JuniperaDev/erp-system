package io.github.erp.service.mapper;

import io.github.erp.domain.AssetProcurementLink;
import io.github.erp.service.dto.AssetProcurementLinkDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface AssetProcurementLinkMapper extends EntityMapper<AssetProcurementLinkDTO, AssetProcurementLink> {}
