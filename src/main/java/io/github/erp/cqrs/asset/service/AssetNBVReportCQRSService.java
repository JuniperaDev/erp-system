package io.github.erp.cqrs.asset.service;

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
import io.github.erp.cqrs.asset.repositories.AssetNBVReportReadModelRepository;
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
public class AssetNBVReportCQRSService {

    private final AssetNBVReportReadModelRepository assetNBVReportReadModelRepository;

    public AssetNBVReportCQRSService(AssetNBVReportReadModelRepository assetNBVReportReadModelRepository) {
        this.assetNBVReportReadModelRepository = assetNBVReportReadModelRepository;
    }

    public Page<AssetNBVReportReadModel> findAllAssetNBVReports(Pageable pageable) {
        return assetNBVReportReadModelRepository.findAll(pageable);
    }

    public Optional<AssetNBVReportReadModel> findAssetNBVReport(Long id) {
        return assetNBVReportReadModelRepository.findById(id);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByDate(LocalDate reportingDate, Pageable pageable) {
        return assetNBVReportReadModelRepository.findByReportingDate(reportingDate, pageable);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByCategory(String categoryName, Pageable pageable) {
        return assetNBVReportReadModelRepository.findByCategoryName(categoryName, pageable);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByServiceOutlet(String serviceOutletCode, Pageable pageable) {
        return assetNBVReportReadModelRepository.findByServiceOutletCode(serviceOutletCode, pageable);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByFiscalYear(Integer fiscalYear, Pageable pageable) {
        return assetNBVReportReadModelRepository.findByFiscalYear(fiscalYear, pageable);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByFiscalQuarter(String fiscalQuarter, Pageable pageable) {
        return assetNBVReportReadModelRepository.findByFiscalQuarter(fiscalQuarter, pageable);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByDateRange(
        LocalDate startDate, 
        LocalDate endDate, 
        Pageable pageable
    ) {
        return assetNBVReportReadModelRepository.findByReportingDateBetween(startDate, endDate, pageable);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByCategoryAndDate(
        String categoryName, 
        LocalDate reportingDate, 
        Pageable pageable
    ) {
        return assetNBVReportReadModelRepository.findByCategoryNameAndReportingDate(categoryName, reportingDate, pageable);
    }

    public Page<AssetNBVReportReadModel> findAssetNBVReportsByFiscalYearAndCategory(
        Integer fiscalYear,
        String categoryName, 
        Pageable pageable
    ) {
        return assetNBVReportReadModelRepository.findByFiscalYearAndCategoryName(fiscalYear, categoryName, pageable);
    }

    public BigDecimal getTotalNBVByDate(LocalDate reportingDate) {
        return assetNBVReportReadModelRepository.getTotalNBVByReportingDate(reportingDate);
    }

    public BigDecimal getTotalNBVByCategoryAndDate(String categoryName, LocalDate reportingDate) {
        return assetNBVReportReadModelRepository.getTotalNBVByCategoryAndReportingDate(categoryName, reportingDate);
    }

    public BigDecimal getTotalHistoricalCostByDate(LocalDate reportingDate) {
        return assetNBVReportReadModelRepository.getTotalHistoricalCostByReportingDate(reportingDate);
    }

    public Long getAssetCountByDate(LocalDate reportingDate) {
        return assetNBVReportReadModelRepository.countByReportingDate(reportingDate);
    }

    public List<LocalDate> getDistinctReportingDates() {
        return assetNBVReportReadModelRepository.findDistinctReportingDates();
    }

    public List<AssetNBVReportReadModel> findAssetNBVReportsByAssetId(Long assetId) {
        return assetNBVReportReadModelRepository.findByAssetId(assetId);
    }
}
