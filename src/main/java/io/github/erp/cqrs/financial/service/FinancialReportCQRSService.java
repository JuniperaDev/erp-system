package io.github.erp.cqrs.financial.service;

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
import io.github.erp.cqrs.financial.repositories.FinancialReportReadModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FinancialReportCQRSService {

    private final Logger log = LoggerFactory.getLogger(FinancialReportCQRSService.class);

    private final FinancialReportReadModelRepository financialReportReadModelRepository;

    public FinancialReportCQRSService(FinancialReportReadModelRepository financialReportReadModelRepository) {
        this.financialReportReadModelRepository = financialReportReadModelRepository;
    }

    public Page<FinancialReportReadModel> findAllFinancialReports(Pageable pageable) {
        log.debug("Request to get all FinancialReports");
        return financialReportReadModelRepository.findAll(pageable);
    }

    public Optional<FinancialReportReadModel> findFinancialReport(Long id) {
        log.debug("Request to get FinancialReport : {}", id);
        return financialReportReadModelRepository.findById(id);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByDate(LocalDate reportingDate, Pageable pageable) {
        log.debug("Request to get FinancialReports by date : {}", reportingDate);
        return financialReportReadModelRepository.findByReportingDate(reportingDate, pageable);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByTransactionType(String transactionType, Pageable pageable) {
        log.debug("Request to get FinancialReports by transaction type : {}", transactionType);
        return financialReportReadModelRepository.findByTransactionType(transactionType, pageable);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByFiscalYear(Integer fiscalYear, Pageable pageable) {
        log.debug("Request to get FinancialReports by fiscal year : {}", fiscalYear);
        return financialReportReadModelRepository.findByFiscalYear(fiscalYear, pageable);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByFiscalMonth(Integer fiscalMonth, Pageable pageable) {
        log.debug("Request to get FinancialReports by fiscal month : {}", fiscalMonth);
        return financialReportReadModelRepository.findByFiscalMonth(fiscalMonth, pageable);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByDealer(String dealerName, Pageable pageable) {
        log.debug("Request to get FinancialReports by dealer : {}", dealerName);
        return financialReportReadModelRepository.findByDealerName(dealerName, pageable);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByCurrency(String currencyCode, Pageable pageable) {
        log.debug("Request to get FinancialReports by currency : {}", currencyCode);
        return financialReportReadModelRepository.findByCurrencyCode(currencyCode, pageable);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get FinancialReports by date range : {} to {}", startDate, endDate);
        return financialReportReadModelRepository.findByTransactionDateBetween(startDate, endDate, pageable);
    }

    public Page<FinancialReportReadModel> findFinancialReportsByReportingDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        log.debug("Request to get FinancialReports by reporting date range : {} to {}", startDate, endDate);
        return financialReportReadModelRepository.findByReportingDateBetween(startDate, endDate, pageable);
    }

    public BigDecimal getTotalTransactionAmountByDate(LocalDate reportingDate) {
        log.debug("Request to get total transaction amount by date : {}", reportingDate);
        BigDecimal total = financialReportReadModelRepository.getTotalTransactionAmountByDate(reportingDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalSettlementAmountByDate(LocalDate reportingDate) {
        log.debug("Request to get total settlement amount by date : {}", reportingDate);
        BigDecimal total = financialReportReadModelRepository.getTotalSettlementAmountByDate(reportingDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalOutstandingAmountByDate(LocalDate reportingDate) {
        log.debug("Request to get total outstanding amount by date : {}", reportingDate);
        BigDecimal total = financialReportReadModelRepository.getTotalOutstandingAmountByDate(reportingDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalTransactionAmountByTypeAndDate(String transactionType, LocalDate reportingDate) {
        log.debug("Request to get total transaction amount by type and date : {} on {}", transactionType, reportingDate);
        BigDecimal total = financialReportReadModelRepository.getTotalTransactionAmountByTypeAndDate(transactionType, reportingDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalTransactionAmountByDealerAndDate(String dealerName, LocalDate reportingDate) {
        log.debug("Request to get total transaction amount by dealer and date : {} on {}", dealerName, reportingDate);
        BigDecimal total = financialReportReadModelRepository.getTotalTransactionAmountByDealerAndDate(dealerName, reportingDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getTotalTransactionAmountByCurrencyAndDate(String currencyCode, LocalDate reportingDate) {
        log.debug("Request to get total transaction amount by currency and date : {} on {}", currencyCode, reportingDate);
        BigDecimal total = financialReportReadModelRepository.getTotalTransactionAmountByCurrencyAndDate(currencyCode, reportingDate);
        return total != null ? total : BigDecimal.ZERO;
    }

    public Long getTransactionCountByDate(LocalDate reportingDate) {
        log.debug("Request to get transaction count by date : {}", reportingDate);
        return financialReportReadModelRepository.getTransactionCountByDate(reportingDate);
    }

    public Long getTransactionCountByTypeAndDate(String transactionType, LocalDate reportingDate) {
        log.debug("Request to get transaction count by type and date : {} on {}", transactionType, reportingDate);
        return financialReportReadModelRepository.getTransactionCountByTypeAndDate(transactionType, reportingDate);
    }

    public List<LocalDate> getDistinctReportingDates() {
        log.debug("Request to get distinct reporting dates");
        return financialReportReadModelRepository.getDistinctReportingDates();
    }

    public List<String> getDistinctTransactionTypes() {
        log.debug("Request to get distinct transaction types");
        return financialReportReadModelRepository.getDistinctTransactionTypes();
    }

    public List<String> getDistinctDealerNames() {
        log.debug("Request to get distinct dealer names");
        return financialReportReadModelRepository.getDistinctDealerNames();
    }

    public List<String> getDistinctCurrencyCodes() {
        log.debug("Request to get distinct currency codes");
        return financialReportReadModelRepository.getDistinctCurrencyCodes();
    }

    public List<FinancialReportReadModel> findFinancialReportsByTransactionId(Long transactionId) {
        log.debug("Request to get FinancialReports by transaction ID : {}", transactionId);
        return financialReportReadModelRepository.findByTransactionId(transactionId);
    }

    public List<FinancialReportReadModel> findFinancialReportsByTransactionNumber(String transactionNumber) {
        log.debug("Request to get FinancialReports by transaction number : {}", transactionNumber);
        return financialReportReadModelRepository.findByTransactionNumber(transactionNumber);
    }
}
