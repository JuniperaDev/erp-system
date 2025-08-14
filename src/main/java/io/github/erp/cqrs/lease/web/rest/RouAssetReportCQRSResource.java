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

import io.github.erp.cqrs.lease.readmodel.RouAssetReportReadModel;
import io.github.erp.cqrs.lease.service.RouAssetReportCQRSService;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/cqrs")
public class RouAssetReportCQRSResource {

    private final Logger log = LoggerFactory.getLogger(RouAssetReportCQRSResource.class);

    private final RouAssetReportCQRSService rouAssetReportCQRSService;

    public RouAssetReportCQRSResource(RouAssetReportCQRSService rouAssetReportCQRSService) {
        this.rouAssetReportCQRSService = rouAssetReportCQRSService;
    }

    @GetMapping("/rou-asset-reports")
    public ResponseEntity<List<RouAssetReportReadModel>> getAllRouAssetReports(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of RouAssetReports");
        Page<RouAssetReportReadModel> page = rouAssetReportCQRSService.findAllRouAssetReports(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/rou-asset-reports/{id}")
    public ResponseEntity<RouAssetReportReadModel> getRouAssetReport(@PathVariable Long id) {
        log.debug("REST request to get RouAssetReport : {}", id);
        Optional<RouAssetReportReadModel> rouAssetReport = rouAssetReportCQRSService.findRouAssetReport(id);
        return ResponseUtil.wrapOrNotFound(rouAssetReport);
    }

    @GetMapping("/rou-asset-reports/by-date")
    public ResponseEntity<List<RouAssetReportReadModel>> getRouAssetReportsByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RouAssetReports by date : {}", reportingDate);
        Page<RouAssetReportReadModel> page = rouAssetReportCQRSService.findRouAssetReportsByDate(reportingDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/rou-asset-reports/by-category")
    public ResponseEntity<List<RouAssetReportReadModel>> getRouAssetReportsByCategory(
        @RequestParam String assetCategoryName,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RouAssetReports by category : {}", assetCategoryName);
        Page<RouAssetReportReadModel> page = rouAssetReportCQRSService.findRouAssetReportsByCategory(assetCategoryName, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/rou-asset-reports/by-fiscal-year")
    public ResponseEntity<List<RouAssetReportReadModel>> getRouAssetReportsByFiscalYear(
        @RequestParam Integer fiscalYear,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RouAssetReports by fiscal year : {}", fiscalYear);
        Page<RouAssetReportReadModel> page = rouAssetReportCQRSService.findRouAssetReportsByFiscalYear(fiscalYear, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/rou-asset-reports/by-date-range")
    public ResponseEntity<List<RouAssetReportReadModel>> getRouAssetReportsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get RouAssetReports by date range : {} to {}", startDate, endDate);
        Page<RouAssetReportReadModel> page = rouAssetReportCQRSService.findRouAssetReportsByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/rou-asset-reports/total-depreciation")
    public ResponseEntity<BigDecimal> getTotalDepreciationAmountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total depreciation amount by date : {}", reportingDate);
        BigDecimal totalDepreciation = rouAssetReportCQRSService.getTotalDepreciationAmountByDate(reportingDate);
        return ResponseEntity.ok().body(totalDepreciation);
    }

    @GetMapping("/rou-asset-reports/total-outstanding")
    public ResponseEntity<BigDecimal> getTotalOutstandingAmountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total outstanding amount by date : {}", reportingDate);
        BigDecimal totalOutstanding = rouAssetReportCQRSService.getTotalOutstandingAmountByDate(reportingDate);
        return ResponseEntity.ok().body(totalOutstanding);
    }

    @GetMapping("/rou-asset-reports/total-nbv")
    public ResponseEntity<BigDecimal> getTotalNetBookValueByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total net book value by date : {}", reportingDate);
        BigDecimal totalNBV = rouAssetReportCQRSService.getTotalNetBookValueByDate(reportingDate);
        return ResponseEntity.ok().body(totalNBV);
    }

    @GetMapping("/rou-asset-reports/total-nbv-by-category")
    public ResponseEntity<BigDecimal> getTotalNetBookValueByCategoryAndDate(
        @RequestParam String assetCategoryName,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total NBV by category and date : {} on {}", assetCategoryName, reportingDate);
        BigDecimal totalNBV = rouAssetReportCQRSService.getTotalNetBookValueByCategoryAndDate(assetCategoryName, reportingDate);
        return ResponseEntity.ok().body(totalNBV);
    }

    @GetMapping("/rou-asset-reports/count")
    public ResponseEntity<Long> getRouAssetCountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get ROU asset count by date : {}", reportingDate);
        Long count = rouAssetReportCQRSService.getRouAssetCountByDate(reportingDate);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/rou-asset-reports/reporting-dates")
    public ResponseEntity<List<LocalDate>> getDistinctReportingDates() {
        log.debug("REST request to get distinct reporting dates");
        List<LocalDate> dates = rouAssetReportCQRSService.getDistinctReportingDates();
        return ResponseEntity.ok().body(dates);
    }

    @GetMapping("/rou-asset-reports/asset-categories")
    public ResponseEntity<List<String>> getDistinctAssetCategoryNames() {
        log.debug("REST request to get distinct asset category names");
        List<String> categories = rouAssetReportCQRSService.getDistinctAssetCategoryNames();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/rou-asset-reports/by-rou-asset/{rouAssetIdentifier}")
    public ResponseEntity<List<RouAssetReportReadModel>> getRouAssetReportsByRouAssetIdentifier(@PathVariable UUID rouAssetIdentifier) {
        log.debug("REST request to get RouAssetReports by ROU asset identifier : {}", rouAssetIdentifier);
        List<RouAssetReportReadModel> reports = rouAssetReportCQRSService.findRouAssetReportsByRouAssetIdentifier(rouAssetIdentifier);
        return ResponseEntity.ok().body(reports);
    }

    @GetMapping("/rou-asset-reports/by-lease-contract/{leaseContractId}")
    public ResponseEntity<List<RouAssetReportReadModel>> getRouAssetReportsByLeaseContractId(@PathVariable Long leaseContractId) {
        log.debug("REST request to get RouAssetReports by lease contract ID : {}", leaseContractId);
        List<RouAssetReportReadModel> reports = rouAssetReportCQRSService.findRouAssetReportsByLeaseContractId(leaseContractId);
        return ResponseEntity.ok().body(reports);
    }
}
