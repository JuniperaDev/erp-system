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

import io.github.erp.cqrs.asset.readmodel.DepreciationReportReadModel;
import io.github.erp.cqrs.asset.service.DepreciationReportCQRSService;
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
public class DepreciationReportCQRSResource {

    private final Logger log = LoggerFactory.getLogger(DepreciationReportCQRSResource.class);

    private final DepreciationReportCQRSService depreciationReportCQRSService;

    public DepreciationReportCQRSResource(DepreciationReportCQRSService depreciationReportCQRSService) {
        this.depreciationReportCQRSService = depreciationReportCQRSService;
    }

    @GetMapping("/depreciation-reports")
    public ResponseEntity<List<DepreciationReportReadModel>> getAllDepreciationReports(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of DepreciationReports");
        Page<DepreciationReportReadModel> page = depreciationReportCQRSService.findAllDepreciationReports(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/depreciation-reports/{id}")
    public ResponseEntity<DepreciationReportReadModel> getDepreciationReport(@PathVariable Long id) {
        log.debug("REST request to get DepreciationReport : {}", id);
        Optional<DepreciationReportReadModel> depreciationReport = depreciationReportCQRSService.findDepreciationReport(id);
        return ResponseUtil.wrapOrNotFound(depreciationReport);
    }

    @GetMapping("/depreciation-reports/by-period")
    public ResponseEntity<List<DepreciationReportReadModel>> getDepreciationReportsByPeriod(
        @RequestParam String depreciationPeriod,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DepreciationReports by period : {}", depreciationPeriod);
        Page<DepreciationReportReadModel> page = depreciationReportCQRSService.findDepreciationReportsByPeriod(depreciationPeriod, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/depreciation-reports/by-category")
    public ResponseEntity<List<DepreciationReportReadModel>> getDepreciationReportsByCategory(
        @RequestParam String categoryName,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DepreciationReports by category : {}", categoryName);
        Page<DepreciationReportReadModel> page = depreciationReportCQRSService.findDepreciationReportsByCategory(categoryName, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/depreciation-reports/by-fiscal-year")
    public ResponseEntity<List<DepreciationReportReadModel>> getDepreciationReportsByFiscalYear(
        @RequestParam Integer fiscalYear,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DepreciationReports by fiscal year : {}", fiscalYear);
        Page<DepreciationReportReadModel> page = depreciationReportCQRSService.findDepreciationReportsByFiscalYear(fiscalYear, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/depreciation-reports/by-date-range")
    public ResponseEntity<List<DepreciationReportReadModel>> getDepreciationReportsByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get DepreciationReports by date range : {} to {}", startDate, endDate);
        Page<DepreciationReportReadModel> page = depreciationReportCQRSService.findDepreciationReportsByDateRange(startDate, endDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/depreciation-reports/total-depreciation")
    public ResponseEntity<BigDecimal> getTotalDepreciationByPeriod(
        @RequestParam String depreciationPeriod
    ) {
        log.debug("REST request to get total depreciation by period : {}", depreciationPeriod);
        BigDecimal totalDepreciation = depreciationReportCQRSService.getTotalDepreciationByPeriod(depreciationPeriod);
        return ResponseEntity.ok().body(totalDepreciation);
    }

    @GetMapping("/depreciation-reports/total-depreciation-by-category")
    public ResponseEntity<BigDecimal> getTotalDepreciationByCategoryAndPeriod(
        @RequestParam String categoryName,
        @RequestParam String depreciationPeriod
    ) {
        log.debug("REST request to get total depreciation by category and period : {} in {}", categoryName, depreciationPeriod);
        BigDecimal totalDepreciation = depreciationReportCQRSService.getTotalDepreciationByCategoryAndPeriod(categoryName, depreciationPeriod);
        return ResponseEntity.ok().body(totalDepreciation);
    }

    @GetMapping("/depreciation-reports/total-nbv")
    public ResponseEntity<BigDecimal> getTotalNBVByPeriod(
        @RequestParam String depreciationPeriod
    ) {
        log.debug("REST request to get total NBV by period : {}", depreciationPeriod);
        BigDecimal totalNBV = depreciationReportCQRSService.getTotalNBVByPeriod(depreciationPeriod);
        return ResponseEntity.ok().body(totalNBV);
    }

    @GetMapping("/depreciation-reports/count")
    public ResponseEntity<Long> getDepreciationCountByPeriod(
        @RequestParam String depreciationPeriod
    ) {
        log.debug("REST request to get depreciation count by period : {}", depreciationPeriod);
        Long count = depreciationReportCQRSService.getDepreciationCountByPeriod(depreciationPeriod);
        return ResponseEntity.ok().body(count);
    }

    @GetMapping("/depreciation-reports/periods")
    public ResponseEntity<List<String>> getDistinctDepreciationPeriods() {
        log.debug("REST request to get distinct depreciation periods");
        List<String> periods = depreciationReportCQRSService.getDistinctDepreciationPeriods();
        return ResponseEntity.ok().body(periods);
    }

    @GetMapping("/depreciation-reports/by-asset/{assetId}")
    public ResponseEntity<List<DepreciationReportReadModel>> getDepreciationReportsByAssetId(@PathVariable Long assetId) {
        log.debug("REST request to get DepreciationReports by asset ID : {}", assetId);
        List<DepreciationReportReadModel> reports = depreciationReportCQRSService.findDepreciationReportsByAssetId(assetId);
        return ResponseEntity.ok().body(reports);
    }
}
