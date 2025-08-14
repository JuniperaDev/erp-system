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

import io.github.erp.cqrs.asset.readmodel.AssetReportReadModel;
import io.github.erp.cqrs.asset.service.AssetReportCQRSService;
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
public class AssetReportCQRSResource {

    private final Logger log = LoggerFactory.getLogger(AssetReportCQRSResource.class);

    private final AssetReportCQRSService assetReportCQRSService;

    public AssetReportCQRSResource(AssetReportCQRSService assetReportCQRSService) {
        this.assetReportCQRSService = assetReportCQRSService;
    }

    @GetMapping("/asset-reports")
    public ResponseEntity<List<AssetReportReadModel>> getAllAssetReports(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AssetReports");
        Page<AssetReportReadModel> page = assetReportCQRSService.findAllAssetReports(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-reports/{id}")
    public ResponseEntity<AssetReportReadModel> getAssetReport(@PathVariable Long id) {
        log.debug("REST request to get AssetReport : {}", id);
        Optional<AssetReportReadModel> assetReport = assetReportCQRSService.findAssetReport(id);
        return ResponseUtil.wrapOrNotFound(assetReport);
    }

    @GetMapping("/asset-reports/by-date")
    public ResponseEntity<List<AssetReportReadModel>> getAssetReportsByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetReports by date : {}", reportingDate);
        Page<AssetReportReadModel> page = assetReportCQRSService.findAssetReportsByDate(reportingDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-reports/by-category")
    public ResponseEntity<List<AssetReportReadModel>> getAssetReportsByCategory(
        @RequestParam String categoryName,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetReports by category : {}", categoryName);
        Page<AssetReportReadModel> page = assetReportCQRSService.findAssetReportsByCategory(categoryName, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-reports/by-dealer")
    public ResponseEntity<List<AssetReportReadModel>> getAssetReportsByDealer(
        @RequestParam String dealerName,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetReports by dealer : {}", dealerName);
        Page<AssetReportReadModel> page = assetReportCQRSService.findAssetReportsByDealer(dealerName, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-reports/by-date-range")
    public ResponseEntity<List<AssetReportReadModel>> getAssetReportsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get AssetReports by date range : {} to {}", startDate, endDate);
        Page<AssetReportReadModel> page = assetReportCQRSService.findAssetReportsByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-reports/total-nbv")
    public ResponseEntity<BigDecimal> getTotalNBVByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total NBV by date : {}", reportingDate);
        BigDecimal totalNBV = assetReportCQRSService.getTotalNBVByDate(reportingDate);
        return ResponseEntity.ok().body(totalNBV);
    }

    @GetMapping("/asset-reports/total-nbv-by-category")
    public ResponseEntity<BigDecimal> getTotalNBVByCategoryAndDate(
        @RequestParam String categoryName,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get total NBV by category and date : {} on {}", categoryName, reportingDate);
        BigDecimal totalNBV = assetReportCQRSService.getTotalNBVByCategoryAndDate(categoryName, reportingDate);
        return ResponseEntity.ok().body(totalNBV);
    }

    @GetMapping("/asset-reports/count")
    public ResponseEntity<Long> getAssetCountByDate(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reportingDate
    ) {
        log.debug("REST request to get asset count by date : {}", reportingDate);
        Long count = assetReportCQRSService.getAssetCountByDate(reportingDate);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/asset-reports/by-asset/{assetId}")
    public ResponseEntity<List<AssetReportReadModel>> getAssetReportsByAssetId(@PathVariable Long assetId) {
        log.debug("REST request to get AssetReports by asset ID : {}", assetId);
        List<AssetReportReadModel> reports = assetReportCQRSService.findAssetReportsByAssetId(assetId);
        return ResponseEntity.ok().body(reports);
    }
}
