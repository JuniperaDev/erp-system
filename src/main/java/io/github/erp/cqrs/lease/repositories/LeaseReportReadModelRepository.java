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

import io.github.erp.cqrs.lease.readmodel.LeaseReportReadModel;
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
public interface LeaseReportReadModelRepository 
    extends JpaRepository<LeaseReportReadModel, Long>, JpaSpecificationExecutor<LeaseReportReadModel> {

    Optional<LeaseReportReadModel> findByLeaseIdAndReportingDate(Long leaseId, LocalDate reportingDate);

    List<LeaseReportReadModel> findByLeaseId(Long leaseId);

    Page<LeaseReportReadModel> findByReportingDate(LocalDate reportingDate, Pageable pageable);

    Page<LeaseReportReadModel> findByLeaseStatus(String leaseStatus, Pageable pageable);

    Page<LeaseReportReadModel> findByFiscalYear(Integer fiscalYear, Pageable pageable);

    Page<LeaseReportReadModel> findByFiscalQuarter(String fiscalQuarter, Pageable pageable);

    @Query("SELECT lrrm FROM LeaseReportReadModel lrrm WHERE lrrm.reportingDate BETWEEN :startDate AND :endDate")
    Page<LeaseReportReadModel> findByReportingDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        Pageable pageable
    );

    @Query("SELECT lrrm FROM LeaseReportReadModel lrrm WHERE lrrm.leaseStatus = :leaseStatus AND lrrm.reportingDate = :reportingDate")
    Page<LeaseReportReadModel> findByLeaseStatusAndReportingDate(
        @Param("leaseStatus") String leaseStatus, 
        @Param("reportingDate") LocalDate reportingDate, 
        Pageable pageable
    );

    @Query("SELECT lrrm FROM LeaseReportReadModel lrrm WHERE lrrm.fiscalYear = :fiscalYear AND lrrm.leaseStatus = :leaseStatus")
    Page<LeaseReportReadModel> findByFiscalYearAndLeaseStatus(
        @Param("fiscalYear") Integer fiscalYear,
        @Param("leaseStatus") String leaseStatus, 
        Pageable pageable
    );

    @Query("SELECT SUM(lrrm.leaseAmount) FROM LeaseReportReadModel lrrm WHERE lrrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalLeaseAmountByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(lrrm.leaseAmount) FROM LeaseReportReadModel lrrm WHERE lrrm.leaseStatus = :leaseStatus AND lrrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalLeaseAmountByStatusAndReportingDate(
        @Param("leaseStatus") String leaseStatus, 
        @Param("reportingDate") LocalDate reportingDate
    );

    @Query("SELECT SUM(lrrm.leaseAmount) FROM LeaseReportReadModel lrrm WHERE lrrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalLeaseAmountByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT SUM(lrrm.leaseAmount) FROM LeaseReportReadModel lrrm WHERE lrrm.fiscalYear = :fiscalYear AND lrrm.leaseStatus = :leaseStatus")
    java.math.BigDecimal getTotalLeaseAmountByFiscalYearAndStatus(
        @Param("fiscalYear") Integer fiscalYear,
        @Param("leaseStatus") String leaseStatus
    );

    @Query("SELECT SUM(lrrm.leaseAmount) FROM LeaseReportReadModel lrrm WHERE lrrm.reportingDate BETWEEN :startDate AND :endDate")
    java.math.BigDecimal getTotalLeaseAmountByDateRange(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT SUM(lrrm.leaseAmount) FROM LeaseReportReadModel lrrm WHERE lrrm.reportingDate BETWEEN :startDate AND :endDate AND lrrm.leaseStatus = :leaseStatus")
    java.math.BigDecimal getTotalLeaseAmountByDateRangeAndStatus(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate,
        @Param("leaseStatus") String leaseStatus
    );

    @Query("SELECT SUM(lrrm.leaseAmount) FROM LeaseReportReadModel lrrm WHERE lrrm.reportingDate BETWEEN :startDate AND :endDate AND lrrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalLeaseAmountByDateRangeAndFiscalYear(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate,
        @Param("fiscalYear") Integer fiscalYear
    );

    @Query("SELECT COUNT(lrrm) FROM LeaseReportReadModel lrrm WHERE lrrm.reportingDate = :reportingDate")
    Long countByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT COUNT(lrrm) FROM LeaseReportReadModel lrrm WHERE lrrm.leaseStatus = :leaseStatus AND lrrm.reportingDate = :reportingDate")
    Long countByLeaseStatusAndReportingDate(
        @Param("leaseStatus") String leaseStatus, 
        @Param("reportingDate") LocalDate reportingDate
    );

    @Query("SELECT DISTINCT lrrm.reportingDate FROM LeaseReportReadModel lrrm ORDER BY lrrm.reportingDate DESC")
    List<LocalDate> findDistinctReportingDates();

    @Query("SELECT DISTINCT lrrm.leaseStatus FROM LeaseReportReadModel lrrm ORDER BY lrrm.leaseStatus")
    List<String> findDistinctLeaseStatuses();

    void deleteByLeaseId(Long leaseId);

    void deleteByLeaseIdAndReportingDate(Long leaseId, LocalDate reportingDate);
}
