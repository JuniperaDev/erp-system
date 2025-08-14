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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouAssetReportCQRSServiceTest {

    @Mock
    private RouAssetReportReadModelRepository rouAssetReportReadModelRepository;

    private RouAssetReportCQRSService rouAssetReportCQRSService;

    private RouAssetReportReadModel rouAssetReportReadModel;
    private LocalDate reportingDate;
    private Pageable pageable;
    private UUID rouAssetIdentifier;

    @BeforeEach
    void setUp() {
        rouAssetReportCQRSService = new RouAssetReportCQRSService(rouAssetReportReadModelRepository);
        
        reportingDate = LocalDate.of(2024, 12, 31);
        pageable = PageRequest.of(0, 20);
        rouAssetIdentifier = UUID.randomUUID();
        
        rouAssetReportReadModel = new RouAssetReportReadModel();
        rouAssetReportReadModel.setId(1L);
        rouAssetReportReadModel.setRouAssetIdentifier(rouAssetIdentifier);
        rouAssetReportReadModel.setLeaseContractId(100L);
        rouAssetReportReadModel.setLeaseContractNumber("LC-001");
        rouAssetReportReadModel.setLeaseTitle("Office Building Lease");
        rouAssetReportReadModel.setAssetCategoryName("Buildings");
        rouAssetReportReadModel.setDepreciationAmount(new BigDecimal("2000.00"));
        rouAssetReportReadModel.setOutstandingAmount(new BigDecimal("48000.00"));
        rouAssetReportReadModel.setAccumulatedDepreciation(new BigDecimal("12000.00"));
        rouAssetReportReadModel.setNetBookValue(new BigDecimal("48000.00"));
        rouAssetReportReadModel.setReportingDate(reportingDate);
        rouAssetReportReadModel.setFiscalYear(2024);
    }

    @Test
    void findAllRouAssetReports_ShouldReturnPageOfReports() {
        List<RouAssetReportReadModel> reports = Arrays.asList(rouAssetReportReadModel);
        Page<RouAssetReportReadModel> page = new PageImpl<>(reports, pageable, 1);
        
        when(rouAssetReportReadModelRepository.findAll(pageable)).thenReturn(page);
        
        Page<RouAssetReportReadModel> result = rouAssetReportCQRSService.findAllRouAssetReports(pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLeaseContractNumber()).isEqualTo("LC-001");
    }

    @Test
    void findRouAssetReport_ShouldReturnReport_WhenExists() {
        when(rouAssetReportReadModelRepository.findById(1L)).thenReturn(Optional.of(rouAssetReportReadModel));
        
        Optional<RouAssetReportReadModel> result = rouAssetReportCQRSService.findRouAssetReport(1L);
        
        assertThat(result).isPresent();
        assertThat(result.get().getLeaseContractNumber()).isEqualTo("LC-001");
    }

    @Test
    void getTotalDepreciationAmountByDate_ShouldReturnTotalAmount() {
        BigDecimal expectedTotal = new BigDecimal("10000.00");
        
        when(rouAssetReportReadModelRepository.getTotalDepreciationAmountByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = rouAssetReportCQRSService.getTotalDepreciationAmountByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalNetBookValueByDate_ShouldReturnTotalValue() {
        BigDecimal expectedTotal = new BigDecimal("240000.00");
        
        when(rouAssetReportReadModelRepository.getTotalNetBookValueByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = rouAssetReportCQRSService.getTotalNetBookValueByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalNetBookValueByCategoryAndDate_ShouldReturnTotalValue() {
        BigDecimal expectedTotal = new BigDecimal("120000.00");
        
        when(rouAssetReportReadModelRepository.getTotalNetBookValueByCategoryAndReportingDate("Buildings", reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = rouAssetReportCQRSService.getTotalNetBookValueByCategoryAndDate("Buildings", reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getRouAssetCountByDate_ShouldReturnCount() {
        Long expectedCount = 15L;
        
        when(rouAssetReportReadModelRepository.countByReportingDate(reportingDate)).thenReturn(expectedCount);
        
        Long result = rouAssetReportCQRSService.getRouAssetCountByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    void getDistinctReportingDates_ShouldReturnListOfDates() {
        List<LocalDate> expectedDates = Arrays.asList(
            LocalDate.of(2024, 12, 31),
            LocalDate.of(2024, 11, 30),
            LocalDate.of(2024, 10, 31)
        );
        
        when(rouAssetReportReadModelRepository.findDistinctReportingDates()).thenReturn(expectedDates);
        
        List<LocalDate> result = rouAssetReportCQRSService.getDistinctReportingDates();
        
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedDates);
    }

    @Test
    void getDistinctAssetCategoryNames_ShouldReturnListOfCategories() {
        List<String> expectedCategories = Arrays.asList("Buildings", "Equipment", "Vehicles");
        
        when(rouAssetReportReadModelRepository.findDistinctAssetCategoryNames()).thenReturn(expectedCategories);
        
        List<String> result = rouAssetReportCQRSService.getDistinctAssetCategoryNames();
        
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedCategories);
    }

    @Test
    void findRouAssetReportsByRouAssetIdentifier_ShouldReturnListOfReports() {
        List<RouAssetReportReadModel> expectedReports = Arrays.asList(rouAssetReportReadModel);
        
        when(rouAssetReportReadModelRepository.findByRouAssetIdentifier(rouAssetIdentifier)).thenReturn(expectedReports);
        
        List<RouAssetReportReadModel> result = rouAssetReportCQRSService.findRouAssetReportsByRouAssetIdentifier(rouAssetIdentifier);
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRouAssetIdentifier()).isEqualTo(rouAssetIdentifier);
    }

    @Test
    void findRouAssetReportsByLeaseContractId_ShouldReturnListOfReports() {
        List<RouAssetReportReadModel> expectedReports = Arrays.asList(rouAssetReportReadModel);
        
        when(rouAssetReportReadModelRepository.findByLeaseContractId(100L)).thenReturn(expectedReports);
        
        List<RouAssetReportReadModel> result = rouAssetReportCQRSService.findRouAssetReportsByLeaseContractId(100L);
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLeaseContractId()).isEqualTo(100L);
    }
}
