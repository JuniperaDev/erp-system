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

import io.github.erp.cqrs.lease.readmodel.LeaseLiabilityReportReadModel;
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
public interface LeaseLiabilityReportReadModelRepository 
    extends JpaRepository<LeaseLiabilityReportReadModel, Long>, JpaSpecificationExecutor<LeaseLiabilityReportReadModel> {

    Optional<LeaseLiabilityReportReadModel> findByLeaseIdAndReportingDate(Long leaseId, LocalDate reportingDate);

    List<LeaseLiabilityReportReadModel> findByLeaseId(Long leaseId);

    Page<LeaseLiabilityReportReadModel> findByReportingDate(LocalDate reportingDate, Pageable pageable);

    Page<LeaseLiabilityReportReadModel> findByFiscalYear(Integer fiscalYear, Pageable pageable);

    Page<LeaseLiabilityReportReadModel> findByFiscalQuarter(String fiscalQuarter, Pageable pageable);

    @Query("SELECT llrrm FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.reportingDate BETWEEN :startDate AND :endDate")
    Page<LeaseLiabilityReportReadModel> findByReportingDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        Pageable pageable
    );

    @Query("SELECT llrrm FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.fiscalYear = :fiscalYear AND llrrm.reportingDate = :reportingDate")
    Page<LeaseLiabilityReportReadModel> findByFiscalYearAndReportingDate(
        @Param("fiscalYear") Integer fiscalYear,
        @Param("reportingDate") LocalDate reportingDate, 
        Pageable pageable
    );

    @Query("SELECT SUM(llrrm.openingBalance) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalOpeningBalanceByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(llrrm.cashPayment) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalCashPaymentByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(llrrm.principalPayment) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalPrincipalPaymentByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(llrrm.interestPayment) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalInterestPaymentByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(llrrm.outstandingBalance) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.reportingDate = :reportingDate")
    java.math.BigDecimal getTotalOutstandingBalanceByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(llrrm.openingBalance) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalOpeningBalanceByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT SUM(llrrm.cashPayment) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalCashPaymentByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT SUM(llrrm.principalPayment) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalPrincipalPaymentByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT SUM(llrrm.interestPayment) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalInterestPaymentByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT SUM(llrrm.outstandingBalance) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.fiscalYear = :fiscalYear")
    java.math.BigDecimal getTotalOutstandingBalanceByFiscalYear(@Param("fiscalYear") Integer fiscalYear);

    @Query("SELECT COUNT(llrrm) FROM LeaseLiabilityReportReadModel llrrm WHERE llrrm.reportingDate = :reportingDate")
    Long countByReportingDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT DISTINCT llrrm.reportingDate FROM LeaseLiabilityReportReadModel llrrm ORDER BY llrrm.reportingDate DESC")
    List<LocalDate> findDistinctReportingDates();

    void deleteByLeaseId(Long leaseId);

    void deleteByLeaseIdAndReportingDate(Long leaseId, LocalDate reportingDate);
}
