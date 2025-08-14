package io.github.erp.cqrs.lease.repositories;

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

import io.github.erp.cqrs.lease.readmodel.RouAssetReportReadModel;
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
import java.util.UUID;

@Repository
public interface RouAssetReportReadModelRepository 
    extends JpaRepository<RouAssetReportReadModel, Long>, JpaSpecificationExecutor<RouAssetReportReadModel> {

    Optional<RouAssetReportReadModel> findByRouAssetIdentifierAndReportingDate(UUID rouAssetIdentifier, LocalDate reportingDate);

    List<RouAssetReportReadModel> findByRouAssetIdentifier(UUID rouAssetIdentifier);

    List<RouAssetReportReadModel> findByLeaseContractId(Long leaseContractId);

    Page<RouAssetReportReadModel> findByReportingDate(LocalDate reportingDate, Pageable pageable);

    Page<RouAssetReportReadModel> findByAssetCategoryName(String assetCategoryName, Pageable pageable);

    Page<RouAssetReportReadModel> findByFiscalYear(Integer fiscalYear, Pageable pageable);

    Page<RouAssetReportReadModel> findByFiscalQuarter(String fiscalQuarter, Pageable pageable);

    @Query("SELECT rarrm FROM RouAssetReportReadModel rarrm WHERE rarrm.reportingDate BETWEEN :startDate AND :endDate")
    Page<RouAssetReportReadModel> findByReportingDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        Pageable pageable
    );

    @Query("SELECT rarrm FROM RouAssetReportReadModel rarrm WHERE rarrm.assetCategoryName = :assetCategoryName AND rarrm.reportingDate = :reportingDate")
    Page<RouAssetReportReadModel> findByAssetCategoryNameAndReportingDate(
        @Param("assetCategoryName") String assetCategoryName, 
        @Param("reportingDate") LocalDate reportingDate, 
        Pageable pageable
    );

    @Query("SELECT rarrm FROM RouAssetReportReadModel rarrm WHERE rarrm.fiscalYear = :fiscalYear AND rarrm.assetCategoryName = :assetCategoryName")
    Page<RouAssetReportReadModel> findByFiscalYearAndAssetCategoryName(
        @Param("fiscalYear") Integer fiscalYear,
        @Param("assetCategoryName") String assetCategoryName, 
        Pageable pageable
    );

    @Query("SELECT SUM(rarrm.depreciationAmount) FROM RouAssetReportReadModel rarrm WHERE rarrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalDepreciationAmountByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(rarrm.outstandingAmount) FROM RouAssetReportReadModel rarrm WHERE rarrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalOutstandingAmountByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(rarrm.accumulatedDepreciation) FROM RouAssetReportReadModel rarrm WHERE rarrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalAccumulatedDepreciationByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(rarrm.netBookValue) FROM RouAssetReportReadModel rarrm WHERE rarrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalNetBookValueByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(rarrm.depreciationAmount) FROM RouAssetReportReadModel rarrm WHERE rarrm.assetCategoryName = :assetCategoryName AND rarrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalDepreciationAmountByCategoryAndReportingDate(
        @Param("assetCategoryName") String assetCategoryName, 
        @Param("reportingDate") LocalDate reportingDate
    );

    @Query("SELECT SUM(rarrm.netBookValue) FROM RouAssetReportReadModel rarrm WHERE rarrm.assetCategoryName = :assetCategoryName AND rarrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalNetBookValueByCategoryAndReportingDate(
        @Param("assetCategoryName") String assetCategoryName, 
        @Param("reportingDate") LocalDate reportingDate
    );

    @Query("SELECT SUM(rarrm.depreciationAmount) FROM RouAssetReportReadModel rarrm WHERE rarrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalDepreciationAmountByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT SUM(rarrm.netBookValue) FROM RouAssetReportReadModel rarrm WHERE rarrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalNetBookValueByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT COUNT(rarrm) FROM RouAssetReportReadModel rarrm WHERE rarrm.reportingDate = :reportingDate")
    Long countByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT DISTINCT rarrm.reportingDate FROM RouAssetReportReadModel rarrm ORDER BY rarrm.reportingDate DESC")
    List<LocalDate> findDistinctReportingDates();

    @Query("SELECT DISTINCT rarrm.assetCategoryName FROM RouAssetReportReadModel rarrm ORDER BY rarrm.assetCategoryName")
    List<String> findDistinctAssetCategoryNames();

    void deleteByRouAssetIdentifier(UUID rouAssetIdentifier);

    void deleteByLeaseContractId(Long leaseContractId);

    void deleteByRouAssetIdentifierAndReportingDate(UUID rouAssetIdentifier, LocalDate reportingDate);
}
