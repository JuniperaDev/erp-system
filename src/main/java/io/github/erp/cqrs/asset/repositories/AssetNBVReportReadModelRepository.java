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

import io.github.erp.cqrs.asset.readmodel.AssetNBVReportReadModel;
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
public interface AssetNBVReportReadModelRepository 
    extends JpaRepository<AssetNBVReportReadModel, Long>, JpaSpecificationExecutor<AssetNBVReportReadModel> {

    Optional<AssetNBVReportReadModel> findByAssetIdAndReportingDate(Long assetId, LocalDate reportingDate);

    List<AssetNBVReportReadModel> findByAssetId(Long assetId);

    Page<AssetNBVReportReadModel> findByReportingDate(LocalDate reportingDate, Pageable pageable);

    Page<AssetNBVReportReadModel> findByCategoryName(String categoryName, Pageable pageable);

    Page<AssetNBVReportReadModel> findByServiceOutletCode(String serviceOutletCode, Pageable pageable);

    Page<AssetNBVReportReadModel> findByFiscalYear(Integer fiscalYear, Pageable pageable);

    Page<AssetNBVReportReadModel> findByFiscalQuarter(String fiscalQuarter, Pageable pageable);

    @Query("SELECT anrm FROM AssetNBVReportReadModel anrm WHERE anrm.reportingDate BETWEEN :startDate AND :endDate")
    Page<AssetNBVReportReadModel> findByReportingDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        Pageable pageable
    );

    @Query("SELECT anrm FROM AssetNBVReportReadModel anrm WHERE anrm.categoryName = :categoryName AND anrm.reportingDate = :reportingDate")
    Page<AssetNBVReportReadModel> findByCategoryNameAndReportingDate(
        @Param("categoryName") String categoryName, 
        @Param("reportingDate") LocalDate reportingDate, 
        Pageable pageable
    );

    @Query("SELECT anrm FROM AssetNBVReportReadModel anrm WHERE anrm.fiscalYear = :fiscalYear AND anrm.categoryName = :categoryName")
    Page<AssetNBVReportReadModel> findByFiscalYearAndCategoryName(
        @Param("fiscalYear") Integer fiscalYear,
        @Param("categoryName") String categoryName, 
        Pageable pageable
    );

    @Query("SELECT SUM(anrm.netBookValue) FROM AssetNBVReportReadModel anrm WHERE anrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalNBVByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(anrm.netBookValue) FROM AssetNBVReportReadModel anrm WHERE anrm.categoryName = :categoryName AND anrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalNBVByCategoryAndReportingDate(
        @Param("categoryName") String categoryName, 
        @Param("reportingDate") LocalDate reportingDate
    );

    @Query("SELECT SUM(anrm.historicalCost) FROM AssetNBVReportReadModel anrm WHERE anrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalHistoricalCostByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT COUNT(anrm) FROM AssetNBVReportReadModel anrm WHERE anrm.reportingDate = :reportingDate")
    Long countByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT DISTINCT anrm.reportingDate FROM AssetNBVReportReadModel anrm ORDER BY anrm.reportingDate DESC")
    List<LocalDate> findDistinctReportingDates();

    void deleteByAssetId(Long assetId);

    void deleteByAssetIdAndReportingDate(Long assetId, LocalDate reportingDate);
}
