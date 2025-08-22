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

import io.github.erp.cqrs.asset.readmodel.AssetReportReadModel;
import io.github.erp.cqrs.asset.readmodel.AssetNBVReportReadModel;
import io.github.erp.cqrs.asset.repositories.AssetReportReadModelRepository;
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
public class AssetReportCQRSService {

    private final AssetReportReadModelRepository assetReportReadModelRepository;

    public AssetReportCQRSService(AssetReportReadModelRepository assetReportReadModelRepository) {
        this.assetReportReadModelRepository = assetReportReadModelRepository;
    }

    public Page<AssetReportReadModel> findAllAssetReports(Pageable pageable) {
        return assetReportReadModelRepository.findAll(pageable);
    }

    public Optional<AssetReportReadModel> findAssetReport(Long id) {
        return assetReportReadModelRepository.findById(id);
    }

    public Page<AssetReportReadModel> findAssetReportsByDate(LocalDate reportingDate, Pageable pageable) {
        return assetReportReadModelRepository.findByReportingDate(reportingDate, pageable);
    }

    public Page<AssetReportReadModel> findAssetReportsByCategory(String categoryName, Pageable pageable) {
        return assetReportReadModelRepository.findByCategoryName(categoryName, pageable);
    }

    public Page<AssetReportReadModel> findAssetReportsByDealer(String dealerName, Pageable pageable) {
        return assetReportReadModelRepository.findByDealerName(dealerName, pageable);
    }

    public Page<AssetReportReadModel> findAssetReportsByServiceOutlet(String serviceOutletCode, Pageable pageable) {
        return assetReportReadModelRepository.findByServiceOutletCode(serviceOutletCode, pageable);
    }

    public Page<AssetReportReadModel> findAssetReportsByDateRange(
        LocalDate startDate, 
        LocalDate endDate, 
        Pageable pageable
    ) {
        return assetReportReadModelRepository.findByReportingDateBetween(startDate, endDate, pageable);
    }

    public Page<AssetReportReadModel> findAssetReportsByCategoryAndDate(
        String categoryName, 
        LocalDate reportingDate, 
        Pageable pageable
    ) {
        return assetReportReadModelRepository.findByCategoryNameAndReportingDate(categoryName, reportingDate, pageable);
    }

    public Page<AssetReportReadModel> findAssetReportsByDealerAndDateRange(
        String dealerName,
        LocalDate startDate, 
        LocalDate endDate, 
        Pageable pageable
    ) {
        return assetReportReadModelRepository.findByDealerNameAndReportingDateBetween(dealerName, startDate, endDate, pageable);
    }

    public BigDecimal getTotalNBVByDate(LocalDate reportingDate) {
        return assetReportReadModelRepository.getTotalNBVByReportingDate(reportingDate);
    }

    public BigDecimal getTotalNBVByCategoryAndDate(String categoryName, LocalDate reportingDate) {
        return assetReportReadModelRepository.getTotalNBVByCategoryAndReportingDate(categoryName, reportingDate);
    }

    public Long getAssetCountByDate(LocalDate reportingDate) {
        return assetReportReadModelRepository.countByReportingDate(reportingDate);
    }

    public List<AssetReportReadModel> findAssetReportsByAssetId(Long assetId) {
        return assetReportReadModelRepository.findByAssetId(assetId);
    }
}
