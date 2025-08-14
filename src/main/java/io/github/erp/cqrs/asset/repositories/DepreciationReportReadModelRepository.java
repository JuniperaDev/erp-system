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

import io.github.erp.cqrs.asset.readmodel.DepreciationReportReadModel;
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
public interface DepreciationReportReadModelRepository 
    extends JpaRepository<DepreciationReportReadModel, Long>, JpaSpecificationExecutor<DepreciationReportReadModel> {

    Optional<DepreciationReportReadModel> findByAssetIdAndDepreciationPeriod(Long assetId, String depreciationPeriod);

    List<DepreciationReportReadModel> findByAssetId(Long assetId);

    Page<DepreciationReportReadModel> findByDepreciationPeriod(String depreciationPeriod, Pageable pageable);

    Page<DepreciationReportReadModel> findByCategoryName(String categoryName, Pageable pageable);

    Page<DepreciationReportReadModel> findByFiscalYear(Integer fiscalYear, Pageable pageable);

    Page<DepreciationReportReadModel> findByFiscalQuarter(String fiscalQuarter, Pageable pageable);

    @Query("SELECT drm FROM DepreciationReportReadModel drm WHERE drm.depreciationDate BETWEEN :startDate AND :endDate")
    Page<DepreciationReportReadModel> findByDepreciationDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        Pageable pageable
    );

    @Query("SELECT drm FROM DepreciationReportReadModel drm WHERE drm.categoryName = :categoryName AND drm.depreciationPeriod = :depreciationPeriod")
    Page<DepreciationReportReadModel> findByCategoryNameAndDepreciationPeriod(
        @Param("categoryName") String categoryName, 
        @Param("depreciationPeriod") String depreciationPeriod, 
        Pageable pageable
    );

    @Query("SELECT drm FROM DepreciationReportReadModel drm WHERE drm.fiscalYear = :fiscalYear AND drm.categoryName = :categoryName")
    Page<DepreciationReportReadModel> findByFiscalYearAndCategoryName(
        @Param("fiscalYear") Integer fiscalYear,
        @Param("categoryName") String categoryName, 
        Pageable pageable
    );

    @Query("SELECT SUM(drm.depreciationAmount) FROM DepreciationReportReadModel drm WHERE drm.depreciationPeriod = :depreciationPeriod")
    java.math.BigDecimal getTotalDepreciationByPeriod(@Param("depreciationPeriod") String depreciationPeriod);

    @Query("SELECT SUM(drm.depreciationAmount) FROM DepreciationReportReadModel drm WHERE drm.categoryName = :categoryName AND drm.depreciationPeriod = :depreciationPeriod")
    java.math.BigDecimal getTotalDepreciationByCategoryAndPeriod(
        @Param("categoryName") String categoryName, 
        @Param("depreciationPeriod") String depreciationPeriod
    );

    @Query("SELECT SUM(drm.netBookValue) FROM DepreciationReportReadModel drm WHERE drm.depreciationPeriod = :depreciationPeriod")
    java.math.BigDecimal getTotalNBVByPeriod(@Param("depreciationPeriod") String depreciationPeriod);

    @Query("SELECT COUNT(drm) FROM DepreciationReportReadModel drm WHERE drm.depreciationPeriod = :depreciationPeriod")
    Long countByDepreciationPeriod(@Param("depreciationPeriod") String depreciationPeriod);

    @Query("SELECT DISTINCT drm.depreciationPeriod FROM DepreciationReportReadModel drm ORDER BY drm.depreciationPeriod DESC")
    List<String> findDistinctDepreciationPeriods();

    void deleteByAssetId(Long assetId);

    void deleteByAssetIdAndDepreciationPeriod(Long assetId, String depreciationPeriod);
}
