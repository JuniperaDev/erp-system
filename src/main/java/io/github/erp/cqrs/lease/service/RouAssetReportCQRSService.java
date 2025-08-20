package io.github.erp.cqrs.lease.service;

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
import io.github.erp.cqrs.lease.repositories.RouAssetReportReadModelRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RouAssetReportCQRSService {

    private final RouAssetReportReadModelRepository rouAssetReportReadModelRepository;

    public RouAssetReportCQRSService(RouAssetReportReadModelRepository rouAssetReportReadModelRepository) {
        this.rouAssetReportReadModelRepository = rouAssetReportReadModelRepository;
    }

    public Page<RouAssetReportReadModel> findAllRouAssetReports(Pageable pageable) {
        return rouAssetReportReadModelRepository.findAll(pageable);
    }

    public Optional<RouAssetReportReadModel> findRouAssetReport(Long id) {
        return rouAssetReportReadModelRepository.findById(id);
    }

    public Page<RouAssetReportReadModel> findRouAssetReportsByDate(LocalDate reportingDate, Pageable pageable) {
        return rouAssetReportReadModelRepository.findByReportingDate(reportingDate, pageable);
    }

    public Page<RouAssetReportReadModel> findRouAssetReportsByCategory(String assetCategoryName, Pageable pageable) {
        return rouAssetReportReadModelRepository.findByAssetCategoryName(assetCategoryName, pageable);
    }

    public Page<RouAssetReportReadModel> findRouAssetReportsByFiscalYear(Integer fiscalYear, Pageable pageable) {
        return rouAssetReportReadModelRepository.findByFiscalYear(fiscalYear, pageable);
    }

    public Page<RouAssetReportReadModel> findRouAssetReportsByFiscalQuarter(String fiscalQuarter, Pageable pageable) {
        return rouAssetReportReadModelRepository.findByFiscalQuarter(fiscalQuarter, pageable);
    }

    public Page<RouAssetReportReadModel> findRouAssetReportsByDateRange(
        LocalDate startDate, 
        LocalDate endDate, 
        Pageable pageable
    ) {
        return rouAssetReportReadModelRepository.findByReportingDateBetween(startDate, endDate, pageable);
    }

    public Page<RouAssetReportReadModel> findRouAssetReportsByCategoryAndDate(
        String assetCategoryName, 
        LocalDate reportingDate, 
        Pageable pageable
    ) {
        return rouAssetReportReadModelRepository.findByAssetCategoryNameAndReportingDate(assetCategoryName, reportingDate, pageable);
    }

    public Page<RouAssetReportReadModel> findRouAssetReportsByFiscalYearAndCategory(
        Integer fiscalYear,
        String assetCategoryName, 
        Pageable pageable
    ) {
        return rouAssetReportReadModelRepository.findByFiscalYearAndAssetCategoryName(fiscalYear, assetCategoryName, pageable);
    }

    public BigDecimal getTotalDepreciationAmountByDate(LocalDate reportingDate) {
        return rouAssetReportReadModelRepository.getTotalDepreciationAmountByReportingDate(reportingDate);
    }

    public BigDecimal getTotalOutstandingAmountByDate(LocalDate reportingDate) {
        return rouAssetReportReadModelRepository.getTotalOutstandingAmountByReportingDate(reportingDate);
    }

    public BigDecimal getTotalAccumulatedDepreciationByDate(LocalDate reportingDate) {
        return rouAssetReportReadModelRepository.getTotalAccumulatedDepreciationByReportingDate(reportingDate);
    }

    public BigDecimal getTotalNetBookValueByDate(LocalDate reportingDate) {
        return rouAssetReportReadModelRepository.getTotalNetBookValueByReportingDate(reportingDate);
    }

    public BigDecimal getTotalDepreciationAmountByCategoryAndDate(String assetCategoryName, LocalDate reportingDate) {
        return rouAssetReportReadModelRepository.getTotalDepreciationAmountByCategoryAndReportingDate(assetCategoryName, reportingDate);
    }

    public BigDecimal getTotalNetBookValueByCategoryAndDate(String assetCategoryName, LocalDate reportingDate) {
        return rouAssetReportReadModelRepository.getTotalNetBookValueByCategoryAndReportingDate(assetCategoryName, reportingDate);
    }

    public BigDecimal getTotalDepreciationAmountByFiscalYear(Integer fiscalYear) {
        return rouAssetReportReadModelRepository.getTotalDepreciationAmountByFiscalYear(fiscalYear);
    }

    public BigDecimal getTotalNetBookValueByFiscalYear(Integer fiscalYear) {
        return rouAssetReportReadModelRepository.getTotalNetBookValueByFiscalYear(fiscalYear);
    }

    public Long getRouAssetCountByDate(LocalDate reportingDate) {
        return rouAssetReportReadModelRepository.countByReportingDate(reportingDate);
    }

    public List<LocalDate> getDistinctReportingDates() {
        return rouAssetReportReadModelRepository.findDistinctReportingDates();
    }

    public List<String> getDistinctAssetCategoryNames() {
        return rouAssetReportReadModelRepository.findDistinctAssetCategoryNames();
    }

    public List<RouAssetReportReadModel> findRouAssetReportsByRouAssetIdentifier(UUID rouAssetIdentifier) {
        return rouAssetReportReadModelRepository.findByRouAssetIdentifier(rouAssetIdentifier);
    }

    public List<RouAssetReportReadModel> findRouAssetReportsByLeaseContractId(Long leaseContractId) {
        return rouAssetReportReadModelRepository.findByLeaseContractId(leaseContractId);
    }
}
