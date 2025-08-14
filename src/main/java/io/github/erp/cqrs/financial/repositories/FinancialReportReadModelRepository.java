package io.github.erp.cqrs.financial.repositories;

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

import io.github.erp.cqrs.financial.readmodel.FinancialReportReadModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinancialReportReadModelRepository extends JpaRepository<FinancialReportReadModel, Long> {

    Page<FinancialReportReadModel> findByReportingDate(LocalDate reportingDate, Pageable pageable);

    Page<FinancialReportReadModel> findByTransactionType(String transactionType, Pageable pageable);

    Page<FinancialReportReadModel> findByFiscalYear(Integer fiscalYear, Pageable pageable);

    Page<FinancialReportReadModel> findByFiscalMonth(Integer fiscalMonth, Pageable pageable);

    Page<FinancialReportReadModel> findByDealerName(String dealerName, Pageable pageable);

    Page<FinancialReportReadModel> findByCurrencyCode(String currencyCode, Pageable pageable);

    Page<FinancialReportReadModel> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<FinancialReportReadModel> findByReportingDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT SUM(f.transactionAmount) FROM FinancialReportReadModel f WHERE f.reportingDate = :reportingDate")
    BigDecimal getTotalTransactionAmountByDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(f.settlementAmount) FROM FinancialReportReadModel f WHERE f.reportingDate = :reportingDate")
    BigDecimal getTotalSettlementAmountByDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(f.outstandingAmount) FROM FinancialReportReadModel f WHERE f.reportingDate = :reportingDate")
    BigDecimal getTotalOutstandingAmountByDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(f.transactionAmount) FROM FinancialReportReadModel f WHERE f.transactionType = :transactionType AND f.reportingDate = :reportingDate")
    BigDecimal getTotalTransactionAmountByTypeAndDate(@Param("transactionType") String transactionType, @Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(f.transactionAmount) FROM FinancialReportReadModel f WHERE f.dealerName = :dealerName AND f.reportingDate = :reportingDate")
    BigDecimal getTotalTransactionAmountByDealerAndDate(@Param("dealerName") String dealerName, @Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT SUM(f.transactionAmount) FROM FinancialReportReadModel f WHERE f.currencyCode = :currencyCode AND f.reportingDate = :reportingDate")
    BigDecimal getTotalTransactionAmountByCurrencyAndDate(@Param("currencyCode") String currencyCode, @Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT COUNT(f) FROM FinancialReportReadModel f WHERE f.reportingDate = :reportingDate")
    Long getTransactionCountByDate(@Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT COUNT(f) FROM FinancialReportReadModel f WHERE f.transactionType = :transactionType AND f.reportingDate = :reportingDate")
    Long getTransactionCountByTypeAndDate(@Param("transactionType") String transactionType, @Param("reportingDate") LocalDate reportingDate);

    @Query("SELECT DISTINCT f.reportingDate FROM FinancialReportReadModel f ORDER BY f.reportingDate DESC")
    List<LocalDate> getDistinctReportingDates();

    @Query("SELECT DISTINCT f.transactionType FROM FinancialReportReadModel f ORDER BY f.transactionType")
    List<String> getDistinctTransactionTypes();

    @Query("SELECT DISTINCT f.dealerName FROM FinancialReportReadModel f WHERE f.dealerName IS NOT NULL ORDER BY f.dealerName")
    List<String> getDistinctDealerNames();

    @Query("SELECT DISTINCT f.currencyCode FROM FinancialReportReadModel f WHERE f.currencyCode IS NOT NULL ORDER BY f.currencyCode")
    List<String> getDistinctCurrencyCodes();

    List<FinancialReportReadModel> findByTransactionId(Long transactionId);

    List<FinancialReportReadModel> findByTransactionNumber(String transactionNumber);
}
