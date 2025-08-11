package io.github.erp.repository;

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

import io.github.erp.domain.AssetDocumentAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AssetDocumentAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AssetDocumentAssignmentRepository extends JpaRepository<AssetDocumentAssignment, Long>, JpaSpecificationExecutor<AssetDocumentAssignment> {
    
    @Query("SELECT ada FROM AssetDocumentAssignment ada WHERE ada.assetRegistration.id = :assetRegistrationId")
    List<AssetDocumentAssignment> findByAssetRegistrationId(@Param("assetRegistrationId") Long assetRegistrationId);
    
    @Query("SELECT ada FROM AssetDocumentAssignment ada WHERE ada.businessDocument.id = :businessDocumentId")
    List<AssetDocumentAssignment> findByBusinessDocumentId(@Param("businessDocumentId") Long businessDocumentId);
    
    @Query("SELECT ada FROM AssetDocumentAssignment ada WHERE ada.assignmentStatus = :status")
    List<AssetDocumentAssignment> findByAssignmentStatus(@Param("status") String status);
    
    @Query("SELECT ada FROM AssetDocumentAssignment ada WHERE ada.documentType = :documentType")
    List<AssetDocumentAssignment> findByDocumentType(@Param("documentType") String documentType);
}
