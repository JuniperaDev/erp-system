package io.github.erp.service.validation;

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

import io.github.erp.domain.enumeration.ProcurementEntityType;
import java.util.Arrays;
import org.springframework.stereotype.Service;

/**
 * Service for validating AssetProcurementLink entity types and relationships.
 */
@Service
public class AssetProcurementLinkValidationService {

    /**
     * Validates that the procurement entity type is valid.
     *
     * @param procurementEntityType the procurement entity type string
     * @return true if the type is valid, false otherwise
     */
    public boolean isValidProcurementEntityType(ProcurementEntityType procurementEntityType) {
        return procurementEntityType != null;
    }

    public boolean isValidProcurementEntityType(String procurementEntityType) {
        if (procurementEntityType == null || procurementEntityType.trim().isEmpty()) {
            return false;
        }

        return Arrays.stream(ProcurementEntityType.values())
            .anyMatch(type -> type.name().equals(procurementEntityType.trim().toUpperCase()));
    }

    /**
     * Validates that the procurement entity ID is not null and positive.
     *
     * @param procurementEntityId the procurement entity ID
     * @return true if the ID is valid, false otherwise
     */
    public boolean isValidProcurementEntityId(Long procurementEntityId) {
        return procurementEntityId != null && procurementEntityId > 0;
    }

    /**
     * Validates that the asset ID is not null and positive.
     *
     * @param assetId the asset ID
     * @return true if the ID is valid, false otherwise
     */
    public boolean isValidAssetId(Long assetId) {
        return assetId != null && assetId > 0;
    }
}
