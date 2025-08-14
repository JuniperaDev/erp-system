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

import io.github.erp.cqrs.lease.readmodel.LeaseReportReadModel;
import io.github.erp.cqrs.lease.service.LeaseReportCQRSService;
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
public class LeaseReportCQRSResource {

    private final Logger log = LoggerFactory.getLogger(LeaseReportCQRSResource.class);

    private final LeaseReportCQRSService leaseReportCQRSService;

    public LeaseReportCQRSResource(LeaseReportCQRSService leaseReportCQRSService) {
        this.leaseReportCQRSService = leaseReportCQRSService;
    }

    @GetMapping("/lease-reports")
    public ResponseEntity<List<LeaseReportReadModel>> getAllLeaseReports(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of LeaseReports");
        Page<LeaseReportReadModel> page = leaseReportCQRSService.findAllLeaseReports(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-reports/{id}")
    public ResponseEntity<LeaseReportReadModel> getLeaseReport(@PathVariable Long id) {
        log.debug("REST request to get LeaseReport : {}", id);
        Optional<LeaseReportReadModel> leaseReport = leaseReportCQRSService.findLeaseReport(id);
        return ResponseUtil.wrapOrNotFound(leaseReport);
    }

    @GetMapping("/lease-reports/by-date")
    public ResponseEntity<List<LeaseReportReadModel>> getLeaseReportsByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaseReports by date : {}", reportingDate);
        Page<LeaseReportReadModel> page = leaseReportCQRSService.findLeaseReportsByDate(reportingDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-reports/by-status")
    public ResponseEntity<List<LeaseReportReadModel>> getLeaseReportsByStatus(
        @RequestParam String leaseStatus,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaseReports by status : {}", leaseStatus);
        Page<LeaseReportReadModel> page = leaseReportCQRSService.findLeaseReportsByStatus(leaseStatus, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-reports/by-fiscal-year")
    public ResponseEntity<List<LeaseReportReadModel>> getLeaseReportsByFiscalYear(
        @RequestParam Integer fiscalYear,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaseReports by fiscal year : {}", fiscalYear);
        Page<LeaseReportReadModel> page = leaseReportCQRSService.findLeaseReportsByFiscalYear(fiscalYear, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-reports/by-date-range")
    public ResponseEntity<List<LeaseReportReadModel>> getLeaseReportsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get LeaseReports by date range : {} to {}", startDate, endDate);
        Page<LeaseReportReadModel> page = leaseReportCQRSService.findLeaseReportsByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/lease-reports/total-amount")
    public ResponseEntity<BigDecimal> getTotalLeaseAmountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total lease amount by date : {}", reportingDate);
        BigDecimal totalAmount = leaseReportCQRSService.getTotalLeaseAmountByDate(reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/lease-reports/total-amount-by-status")
    public ResponseEntity<BigDecimal> getTotalLeaseAmountByStatusAndDate(
        @RequestParam String leaseStatus,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total lease amount by status and date : {} on {}", leaseStatus, reportingDate);
        BigDecimal totalAmount = leaseReportCQRSService.getTotalLeaseAmountByStatusAndDate(leaseStatus, reportingDate);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/lease-reports/total-amount-by-fiscal-year")
    public ResponseEntity<BigDecimal> getTotalLeaseAmountByFiscalYear(
        @RequestParam Integer fiscalYear
    ) {
        log.debug("REST request to get total lease amount by fiscal year : {}", fiscalYear);
        BigDecimal totalAmount = leaseReportCQRSService.getTotalLeaseAmountByFiscalYear(fiscalYear);
        return ResponseEntity.ok().body(totalAmount);
    }

    @GetMapping("/lease-reports/count")
    public ResponseEntity<Long> getLeaseCountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get lease count by date : {}", reportingDate);
        Long count = leaseReportCQRSService.getLeaseCountByDate(reportingDate);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/lease-reports/reporting-dates")
    public ResponseEntity<List<LocalDate>> getDistinctReportingDates() {
        log.debug("REST request to get distinct reporting dates");
        List<LocalDate> dates = leaseReportCQRSService.getDistinctReportingDates();
        return ResponseEntity.ok().body(dates);
    }

    @GetMapping("/lease-reports/lease-statuses")
    public ResponseEntity<List<String>> getDistinctLeaseStatuses() {
        log.debug("REST request to get distinct lease statuses");
        List<String> statuses = leaseReportCQRSService.getDistinctLeaseStatuses();
        return ResponseEntity.ok().body(statuses);
    }

    @GetMapping("/lease-reports/by-lease/{leaseId}")
    public ResponseEntity<List<LeaseReportReadModel>> getLeaseReportsByLeaseId(@PathVariable Long leaseId) {
        log.debug("REST request to get LeaseReports by lease ID : {}", leaseId);
        List<LeaseReportReadModel> reports = leaseReportCQRSService.findLeaseReportsByLeaseId(leaseId);
        return ResponseEntity.ok().body(reports);
    }
}
