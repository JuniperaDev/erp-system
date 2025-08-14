package io.github.erp.cqrs.lease.web.rest;

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
import io.github.erp.cqrs.lease.service.LeaseLiabilityReportCQRSService;
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
public class LeaseLiabilityReportCQRSResource {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityReportCQRSResource.class);

    private final LeaseLiabilityReportCQRSService leaseLiabilityReportCQRSService;

    public LeaseLiabilityReportCQRSResource(LeaseLiabilityReportCQRSService leaseLiabilityReportCQRSService) {
        this.leaseLiabilityReportCQRSService = leaseLiabilityReportCQRSService;
    }

    @GetMapping("/lease-liability-reports")
    public ResponseEntity<List<LeaseLiabilityReportReadModel>> getAllLeaseLiabilityReports(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of LeaseLiabilityReports");
        Page<LeaseLiabilityReportReadModel> page = leaseLiabilityReportCQRSService.findAllLeaseLiabilityReports(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-liability-reports/{id}")
    public ResponseEntity<LeaseLiabilityReportReadModel> getLeaseLiabilityReport(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityReport : {}", id);
        Optional<LeaseLiabilityReportReadModel> leaseLiabilityReport = leaseLiabilityReportCQRSService.findLeaseLiabilityReport(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityReport);
    }

    @GetMapping("/lease-liability-reports/by-date")
    public ResponseEntity<List<LeaseLiabilityReportReadModel>> getLeaseLiabilityReportsByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityReports by date : {}", reportingDate);
        Page<LeaseLiabilityReportReadModel> page = leaseLiabilityReportCQRSService.findLeaseLiabilityReportsByDate(reportingDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-liability-reports/by-fiscal-year")
    public ResponseEntity<List<LeaseLiabilityReportReadModel>> getLeaseLiabilityReportsByFiscalYear(
        @RequestParam Integer fiscalYear,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityReports by fiscal year : {}", fiscalYear);
        Page<LeaseLiabilityReportReadModel> page = leaseLiabilityReportCQRSService.findLeaseLiabilityReportsByFiscalYear(fiscalYear, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-liability-reports/by-date-range")
    public ResponseEntity<List<LeaseLiabilityReportReadModel>> getLeaseLiabilityReportsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityReports by date range : {} to {}", startDate, endDate);
        Page<LeaseLiabilityReportReadModel> page = leaseLiabilityReportCQRSService.findLeaseLiabilityReportsByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-liability-reports/total-opening-balance")
    public ResponseEntity<BigDecimal> getTotalOpeningBalanceByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total opening balance by date : {}", reportingDate);
        BigDecimal totalBalance = leaseLiabilityReportCQRSService.getTotalOpeningBalanceByDate(reportingDate);
        return ResponseEntity.ok().body(totalBalance);
    }

    @GetMapping("/lease-liability-reports/total-cash-payment")
    public ResponseEntity<BigDecimal> getTotalCashPaymentByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total cash payment by date : {}", reportingDate);
        BigDecimal totalPayment = leaseLiabilityReportCQRSService.getTotalCashPaymentByDate(reportingDate);
        return ResponseEntity.ok().body(totalPayment);
    }

    @GetMapping("/lease-liability-reports/total-principal-payment")
    public ResponseEntity<BigDecimal> getTotalPrincipalPaymentByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total principal payment by date : {}", reportingDate);
        BigDecimal totalPayment = leaseLiabilityReportCQRSService.getTotalPrincipalPaymentByDate(reportingDate);
        return ResponseEntity.ok().body(totalPayment);
    }

    @GetMapping("/lease-liability-reports/total-interest-payment")
    public ResponseEntity<BigDecimal> getTotalInterestPaymentByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total interest payment by date : {}", reportingDate);
        BigDecimal totalPayment = leaseLiabilityReportCQRSService.getTotalInterestPaymentByDate(reportingDate);
        return ResponseEntity.ok().body(totalPayment);
    }

    @GetMapping("/lease-liability-reports/total-outstanding-balance")
    public ResponseEntity<BigDecimal> getTotalOutstandingBalanceByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total outstanding balance by date : {}", reportingDate);
        BigDecimal totalBalance = leaseLiabilityReportCQRSService.getTotalOutstandingBalanceByDate(reportingDate);
        return ResponseEntity.ok().body(totalBalance);
    }

    @GetMapping("/lease-liability-reports/count")
    public ResponseEntity<Long> getLeaseLiabilityCountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get lease liability count by date : {}", reportingDate);
        Long count = leaseLiabilityReportCQRSService.getLeaseLiabilityCountByDate(reportingDate);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/lease-liability-reports/reporting-dates")
    public ResponseEntity<List<LocalDate>> getDistinctReportingDates() {
        log.debug("REST request to get distinct reporting dates");
        List<LocalDate> dates = leaseLiabilityReportCQRSService.getDistinctReportingDates();
        return ResponseEntity.ok().body(dates);
    }

    @GetMapping("/lease-liability-reports/by-lease/{leaseId}")
    public ResponseEntity<List<LeaseLiabilityReportReadModel>> getLeaseLiabilityReportsByLeaseId(@PathVariable Long leaseId) {
        log.debug("REST request to get LeaseLiabilityReports by lease ID : {}", leaseId);
        List<LeaseLiabilityReportReadModel> reports = leaseLiabilityReportCQRSService.findLeaseLiabilityReportsByLeaseId(leaseId);
        return ResponseEntity.ok().body(reports);
    }
}
