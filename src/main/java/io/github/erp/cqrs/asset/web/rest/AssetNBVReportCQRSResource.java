package io.github.erp.cqrs.asset.web.rest;

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
import io.github.erp.cqrs.asset.service.AssetNBVReportCQRSService;
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
public class AssetNBVReportCQRSResource {

    private final Logger log = LoggerFactory.getLogger(AssetNBVReportCQRSResource.class);

    private final AssetNBVReportCQRSService assetNBVReportCQRSService;

    public AssetNBVReportCQRSResource(AssetNBVReportCQRSService assetNBVReportCQRSService) {
        this.assetNBVReportCQRSService = assetNBVReportCQRSService;
    }

    @GetMapping("/asset-nbv-reports")
    public ResponseEntity<List<AssetNBVReportReadModel>> getAllAssetNBVReports(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AssetNBVReports");
        Page<AssetNBVReportReadModel> page = assetNBVReportCQRSService.findAllAssetNBVReports(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-nbv-reports/{id}")
    public ResponseEntity<AssetNBVReportReadModel> getAssetNBVReport(@PathVariable Long id) {
        log.debug("REST request to get AssetNBVReport : {}", id);
        Optional<AssetNBVReportReadModel> assetNBVReport = assetNBVReportCQRSService.findAssetNBVReport(id);
        return ResponseUtil.wrapOrNotFound(assetNBVReport);
    }

    @GetMapping("/asset-nbv-reports/by-date")
    public ResponseEntity<List<AssetNBVReportReadModel>> getAssetNBVReportsByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetNBVReports by date : {}", reportingDate);
        Page<AssetNBVReportReadModel> page = assetNBVReportCQRSService.findAssetNBVReportsByDate(reportingDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-nbv-reports/by-category")
    public ResponseEntity<List<AssetNBVReportReadModel>> getAssetNBVReportsByCategory(
        @RequestParam String categoryName,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetNBVReports by category : {}", categoryName);
        Page<AssetNBVReportReadModel> page = assetNBVReportCQRSService.findAssetNBVReportsByCategory(categoryName, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-nbv-reports/by-fiscal-year")
    public ResponseEntity<List<AssetNBVReportReadModel>> getAssetNBVReportsByFiscalYear(
        @RequestParam Integer fiscalYear,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetNBVReports by fiscal year : {}", fiscalYear);
        Page<AssetNBVReportReadModel> page = assetNBVReportCQRSService.findAssetNBVReportsByFiscalYear(fiscalYear, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-nbv-reports/by-date-range")
    public ResponseEntity<List<AssetNBVReportReadModel>> getAssetNBVReportsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetNBVReports by date range : {} to {}", startDate, endDate);
        Page<AssetNBVReportReadModel> page = assetNBVReportCQRSService.findAssetNBVReportsByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-nbv-reports/total-nbv")
    public ResponseEntity<BigDecimal> getTotalNBVByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total NBV by date : {}", reportingDate);
        BigDecimal totalNBV = assetNBVReportCQRSService.getTotalNBVByDate(reportingDate);
        return ResponseEntity.ok().body(totalNBV);
    }

    @GetMapping("/asset-nbv-reports/total-nbv-by-category")
    public ResponseEntity<BigDecimal> getTotalNBVByCategoryAndDate(
        @RequestParam String categoryName,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total NBV by category and date : {} on {}", categoryName, reportingDate);
        BigDecimal totalNBV = assetNBVReportCQRSService.getTotalNBVByCategoryAndDate(categoryName, reportingDate);
        return ResponseEntity.ok().body(totalNBV);
    }

    @GetMapping("/asset-nbv-reports/total-historical-cost")
    public ResponseEntity<BigDecimal> getTotalHistoricalCostByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total historical cost by date : {}", reportingDate);
        BigDecimal totalCost = assetNBVReportCQRSService.getTotalHistoricalCostByDate(reportingDate);
        return ResponseEntity.ok().body(totalCost);
    }

    @GetMapping("/asset-nbv-reports/count")
    public ResponseEntity<Long> getAssetCountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get asset count by date : {}", reportingDate);
        Long count = assetNBVReportCQRSService.getAssetCountByDate(reportingDate);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/asset-nbv-reports/reporting-dates")
    public ResponseEntity<List<LocalDate>> getDistinctReportingDates() {
        log.debug("REST request to get distinct reporting dates");
        List<LocalDate> dates = assetNBVReportCQRSService.getDistinctReportingDates();
        return ResponseEntity.ok().body(dates);
    }

    @GetMapping("/asset-nbv-reports/by-asset/{assetId}")
    public ResponseEntity<List<AssetNBVReportReadModel>> getAssetNBVReportsByAssetId(@PathVariable Long assetId) {
        log.debug("REST request to get AssetNBVReports by asset ID : {}", assetId);
        List<AssetNBVReportReadModel> reports = assetNBVReportCQRSService.findAssetNBVReportsByAssetId(assetId);
        return ResponseEntity.ok().body(reports);
    }
}
