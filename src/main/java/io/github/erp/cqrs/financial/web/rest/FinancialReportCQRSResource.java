package io.github.erp.cqrs.financial.web.rest;

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
import io.github.erp.cqrs.financial.service.FinancialReportCQRSService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cqrs")
public class FinancialReportCQRSResource {

    private final Logger log = LoggerFactory.getLogger(FinancialReportCQRSResource.class);

    private final FinancialReportCQRSService financialReportCQRSService;

    public FinancialReportCQRSResource(FinancialReportCQRSService financialReportCQRSService) {
        this.financialReportCQRSService = financialReportCQRSService;
    }

    @GetMapping("/financial-reports")
    public ResponseEntity<List<FinancialReportReadModel>> getAllFinancialReports(
        Pageable pageable
    ) {
        log.debug("REST request to get a page of FinancialReports");
        Page<FinancialReportReadModel> page = financialReportCQRSService.findAllFinancialReports(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/{id}")
    public ResponseEntity<FinancialReportReadModel> getFinancialReport(@PathVariable Long id) {
        log.debug("REST request to get FinancialReport : {}", id);
        Optional<FinancialReportReadModel> financialReport = financialReportCQRSService.findFinancialReport(id);
        return ResponseUtil.wrapOrNotFound(financialReport);
    }

    @GetMapping("/financial-reports/by-date")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by date : {}", reportingDate);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByDate(reportingDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/by-transaction-type")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByTransactionType(
        @RequestParam String transactionType,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by transaction type : {}", transactionType);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByTransactionType(transactionType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/by-fiscal-year")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByFiscalYear(
        @RequestParam Integer fiscalYear,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by fiscal year : {}", fiscalYear);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByFiscalYear(fiscalYear, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/by-fiscal-month")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByFiscalMonth(
        @RequestParam Integer fiscalMonth,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by fiscal month : {}", fiscalMonth);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByFiscalMonth(fiscalMonth, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/by-dealer")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByDealer(
        @RequestParam String dealerName,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by dealer : {}", dealerName);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByDealer(dealerName, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/by-currency")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByCurrency(
        @RequestParam String currencyCode,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by currency : {}", currencyCode);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByCurrency(currencyCode, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/by-date-range")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by date range : {} to {}", startDate, endDate);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/by-reporting-date-range")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByReportingDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialReports by reporting date range : {} to {}", startDate, endDate);
        Page<FinancialReportReadModel> page = financialReportCQRSService.findFinancialReportsByReportingDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/financial-reports/total-transaction-amount")
    public ResponseEntity<BigDecimal> getTotalTransactionAmountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total transaction amount by date : {}", reportingDate);
        BigDecimal totalAmount = financialReportCQRSService.getTotalTransactionAmountByDate(reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/financial-reports/total-settlement-amount")
    public ResponseEntity<BigDecimal> getTotalSettlementAmountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total settlement amount by date : {}", reportingDate);
        BigDecimal totalAmount = financialReportCQRSService.getTotalSettlementAmountByDate(reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/financial-reports/total-outstanding-amount")
    public ResponseEntity<BigDecimal> getTotalOutstandingAmountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total outstanding amount by date : {}", reportingDate);
        BigDecimal totalAmount = financialReportCQRSService.getTotalOutstandingAmountByDate(reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/financial-reports/total-transaction-amount-by-type")
    public ResponseEntity<BigDecimal> getTotalTransactionAmountByTypeAndDate(
        @RequestParam String transactionType,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total transaction amount by type and date : {} on {}", transactionType, reportingDate);
        BigDecimal totalAmount = financialReportCQRSService.getTotalTransactionAmountByTypeAndDate(transactionType, reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/financial-reports/total-transaction-amount-by-dealer")
    public ResponseEntity<BigDecimal> getTotalTransactionAmountByDealerAndDate(
        @RequestParam String dealerName,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total transaction amount by dealer and date : {} on {}", dealerName, reportingDate);
        BigDecimal totalAmount = financialReportCQRSService.getTotalTransactionAmountByDealerAndDate(dealerName, reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/financial-reports/total-transaction-amount-by-currency")
    public ResponseEntity<BigDecimal> getTotalTransactionAmountByCurrencyAndDate(
        @RequestParam String currencyCode,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total transaction amount by currency and date : {} on {}", currencyCode, reportingDate);
        BigDecimal totalAmount = financialReportCQRSService.getTotalTransactionAmountByCurrencyAndDate(currencyCode, reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/financial-reports/transaction-count")
    public ResponseEntity<Long> getTransactionCountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get transaction count by date : {}", reportingDate);
        Long count = financialReportCQRSService.getTransactionCountByDate(reportingDate);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/financial-reports/transaction-count-by-type")
    public ResponseEntity<Long> getTransactionCountByTypeAndDate(
        @RequestParam String transactionType,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get transaction count by type and date : {} on {}", transactionType, reportingDate);
        Long count = financialReportCQRSService.getTransactionCountByTypeAndDate(transactionType, reportingDate);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/financial-reports/reporting-dates")
    public ResponseEntity<List<LocalDate>> getDistinctReportingDates() {
        log.debug("REST request to get distinct reporting dates");
        List<LocalDate> dates = financialReportCQRSService.getDistinctReportingDates();
        return ResponseEntity.ok().body(dates);
    }

    @GetMapping("/financial-reports/transaction-types")
    public ResponseEntity<List<String>> getDistinctTransactionTypes() {
        log.debug("REST request to get distinct transaction types");
        List<String> types = financialReportCQRSService.getDistinctTransactionTypes();
        return ResponseEntity.ok().body(types);
    }

    @GetMapping("/financial-reports/dealer-names")
    public ResponseEntity<List<String>> getDistinctDealerNames() {
        log.debug("REST request to get distinct dealer names");
        List<String> dealers = financialReportCQRSService.getDistinctDealerNames();
        return ResponseEntity.ok().body(dealers);
    }

    @GetMapping("/financial-reports/currency-codes")
    public ResponseEntity<List<String>> getDistinctCurrencyCodes() {
        log.debug("REST request to get distinct currency codes");
        List<String> currencies = financialReportCQRSService.getDistinctCurrencyCodes();
        return ResponseEntity.ok().body(currencies);
    }

    @GetMapping("/financial-reports/by-transaction/{transactionId}")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByTransactionId(@PathVariable Long transactionId) {
        log.debug("REST request to get FinancialReports by transaction ID : {}", transactionId);
        List<FinancialReportReadModel> reports = financialReportCQRSService.findFinancialReportsByTransactionId(transactionId);
        return ResponseEntity.ok().body(reports);
    }

    @GetMapping("/financial-reports/by-transaction-number")
    public ResponseEntity<List<FinancialReportReadModel>> getFinancialReportsByTransactionNumber(
        @RequestParam String transactionNumber
    ) {
        log.debug("REST request to get FinancialReports by transaction number : {}", transactionNumber);
        List<FinancialReportReadModel> reports = financialReportCQRSService.findFinancialReportsByTransactionNumber(transactionNumber);
        return ResponseEntity.ok().body(reports);
    }
}
