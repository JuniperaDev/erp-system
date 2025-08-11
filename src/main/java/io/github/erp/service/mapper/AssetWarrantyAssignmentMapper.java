package io.github.erp.service.mapper;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.erp.domain.AssetWarrantyAssignment;
import io.github.erp.service.dto.AssetWarrantyAssignmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssetWarrantyAssignment} and its DTO {@link AssetWarrantyAssignmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { AssetRegistrationMapper.class, AssetWarrantyMapper.class })
public interface AssetWarrantyAssignmentMapper extends EntityMapper<AssetWarrantyAssignmentDTO, AssetWarrantyAssignment> {
    @Mapping(target = "assetRegistrationId", source = "assetRegistration.id")
    @Mapping(target = "assetRegistrationAssetNumber", source = "assetRegistration.assetNumber")
    @Mapping(target = "assetWarrantyId", source = "assetWarranty.id")
    @Mapping(target = "assetWarrantyDescription", source = "assetWarranty.description")
    AssetWarrantyAssignmentDTO toDto(AssetWarrantyAssignment s);

    @Mapping(target = "assetRegistration", source = "assetRegistrationId", qualifiedByName = "assetRegistrationId")
    @Mapping(target = "assetWarranty", source = "assetWarrantyId", qualifiedByName = "assetWarrantyId")
    AssetWarrantyAssignment toEntity(AssetWarrantyAssignmentDTO assetWarrantyAssignmentDTO);
}
