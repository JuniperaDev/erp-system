package io.github.erp.cqrs.asset.repositories;

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

import io.github.erp.cqrs.asset.readmodel.AssetReportReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AssetReportReadModelRepository 
    extends JpaRepository<AssetReportReadModel, Long>, JpaSpecificationExecutor<AssetReportReadModel> {

    Optional<AssetReportReadModel> findByAssetIdAndReportingDate(Long assetId, LocalDate reportingDate);

    List<AssetReportReadModel> findByAssetId(Long assetId);

    Page<AssetReportReadModel> findByReportingDate(LocalDate reportingDate, Pageable pageable);

    Page<AssetReportReadModel> findByCategoryName(String categoryName, Pageable pageable);

    Page<AssetReportReadModel> findByDealerName(String dealerName, Pageable pageable);

    Page<AssetReportReadModel> findByServiceOutletCode(String serviceOutletCode, Pageable pageable);

    @Query("SELECT arm FROM AssetReportReadModel arm WHERE arm.reportingDate BETWEEN :startDate AND :endDate")
    Page<AssetReportReadModel> findByReportingDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        Pageable pageable
    );

    @Query("SELECT arm FROM AssetReportReadModel arm WHERE arm.categoryName = :categoryName AND arm.reportingDate = :reportingDate")
    Page<AssetReportReadModel> findByCategoryNameAndReportingDate(
        @Param("categoryName") String categoryName, 
        @Param("reportingDate") LocalDate reportingDate, 
        Pageable pageable
    );

    @Query("SELECT arm FROM AssetReportReadModel arm WHERE arm.dealerName = :dealerName AND arm.reportingDate BETWEEN :startDate AND :endDate")
    Page<AssetReportReadModel> findByDealerNameAndReportingDateBetween(
        @Param("dealerName") String dealerName,
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        Pageable pageable
    );

    @Query("SELECT SUM(arm.currentNBV) FROM AssetReportReadModel arm WHERE arm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalNBVByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(arm.currentNBV) FROM AssetReportReadModel arm WHERE arm.categoryName = :categoryName AND arm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalNBVByCategoryAndReportingDate(
        @Param("categoryName") String categoryName, 
        @Param("reportingDate") LocalDate reportingDate
    );

    @Query("SELECT COUNT(arm) FROM AssetReportReadModel arm WHERE arm.reportingDate = :reportingDate")
    Long countByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    void deleteByAssetId(Long assetId);

    void deleteByAssetIdAndReportingDate(Long assetId, LocalDate reportingDate);
}
