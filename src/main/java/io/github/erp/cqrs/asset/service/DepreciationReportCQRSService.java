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

import io.github.erp.cqrs.asset.readmodel.DepreciationReportReadModel;
import io.github.erp.cqrs.asset.repositories.DepreciationReportReadModelRepository;
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
public class DepreciationReportCQRSService {

    private final DepreciationReportReadModelRepository depreciationReportReadModelRepository;

    public DepreciationReportCQRSService(DepreciationReportReadModelRepository depreciationReportReadModelRepository) {
        this.depreciationReportReadModelRepository = depreciationReportReadModelRepository;
    }

    public Page<DepreciationReportReadModel> findAllDepreciationReports(Pageable pageable) {
        return depreciationReportReadModelRepository.findAll(pageable);
    }

    public Optional<DepreciationReportReadModel> findDepreciationReport(Long id) {
        return depreciationReportReadModelRepository.findById(id);
    }

    public Page<DepreciationReportReadModel> findDepreciationReportsByPeriod(String depreciationPeriod, Pageable pageable) {
        return depreciationReportReadModelRepository.findByDepreciationPeriod(depreciationPeriod, pageable);
    }

    public Page<DepreciationReportReadModel> findDepreciationReportsByCategory(String categoryName, Pageable pageable) {
        return depreciationReportReadModelRepository.findByCategoryName(categoryName, pageable);
    }

    public Page<DepreciationReportReadModel> findDepreciationReportsByFiscalYear(Integer fiscalYear, Pageable pageable) {
        return depreciationReportReadModelRepository.findByFiscalYear(fiscalYear, pageable);
    }

    public Page<DepreciationReportReadModel> findDepreciationReportsByFiscalQuarter(String fiscalQuarter, Pageable pageable) {
        return depreciationReportReadModelRepository.findByFiscalQuarter(fiscalQuarter, pageable);
    }

    public Page<DepreciationReportReadModel> findDepreciationReportsByDateRange(
        LocalDate startDate, 
        LocalDate endDate, 
        Pageable pageable
    ) {
        return depreciationReportReadModelRepository.findByDepreciationDateBetween(startDate, endDate, pageable);
    }

    public Page<DepreciationReportReadModel> findDepreciationReportsByCategoryAndPeriod(
        String categoryName, 
        String depreciationPeriod, 
        Pageable pageable
    ) {
        return depreciationReportReadModelRepository.findByCategoryNameAndDepreciationPeriod(categoryName, depreciationPeriod, pageable);
    }

    public Page<DepreciationReportReadModel> findDepreciationReportsByFiscalYearAndCategory(
        Integer fiscalYear,
        String categoryName, 
        Pageable pageable
    ) {
        return depreciationReportReadModelRepository.findByFiscalYearAndCategoryName(fiscalYear, categoryName, pageable);
    }

    public BigDecimal getTotalDepreciationByPeriod(String depreciationPeriod) {
        return depreciationReportReadModelRepository.getTotalDepreciationByPeriod(depreciationPeriod);
    }

    public BigDecimal getTotalDepreciationByCategoryAndPeriod(String categoryName, String depreciationPeriod) {
        return depreciationReportReadModelRepository.getTotalDepreciationByCategoryAndPeriod(categoryName, depreciationPeriod);
    }

    public BigDecimal getTotalNBVByPeriod(String depreciationPeriod) {
        return depreciationReportReadModelRepository.getTotalNBVByPeriod(depreciationPeriod);
    }

    public Long getDepreciationCountByPeriod(String depreciationPeriod) {
        return depreciationReportReadModelRepository.countByDepreciationPeriod(depreciationPeriod);
    }

    public List<String> getDistinctDepreciationPeriods() {
        return depreciationReportReadModelRepository.findDistinctDepreciationPeriods();
    }

    public List<DepreciationReportReadModel> findDepreciationReportsByAssetId(Long assetId) {
        return depreciationReportReadModelRepository.findByAssetId(assetId);
    }
}
