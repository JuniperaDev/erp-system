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

import io.github.erp.cqrs.lease.readmodel.LeaseReportReadModel;
import io.github.erp.cqrs.lease.repositories.LeaseReportReadModelRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaseReportCQRSServiceTest {

    @Mock
    private LeaseReportReadModelRepository leaseReportReadModelRepository;

    private LeaseReportCQRSService leaseReportCQRSService;

    private LeaseReportReadModel leaseReportReadModel;
    private LocalDate reportingDate;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        leaseReportCQRSService = new LeaseReportCQRSService(leaseReportReadModelRepository);
        
        reportingDate = LocalDate.of(2024, 12, 31);
        pageable = PageRequest.of(0, 20);
        
        leaseReportReadModel = new LeaseReportReadModel();
        leaseReportReadModel.setId(1L);
        leaseReportReadModel.setLeaseId(100L);
        leaseReportReadModel.setLeaseContractNumber("LC-001");
        leaseReportReadModel.setLeaseTitle("Office Building Lease");
        leaseReportReadModel.setLesseeName("ABC Corporation");
        leaseReportReadModel.setLessorName("XYZ Properties");
        leaseReportReadModel.setLeaseAmount(new BigDecimal("50000.00"));
        leaseReportReadModel.setLeaseStatus("ACTIVE");
        leaseReportReadModel.setReportingDate(reportingDate);
        leaseReportReadModel.setFiscalYear(2024);
    }

    @Test
    void findAllLeaseReports_ShouldReturnPageOfLeaseReports() {
        List<LeaseReportReadModel> leaseReports = Arrays.asList(leaseReportReadModel);
        Page<LeaseReportReadModel> page = new PageImpl<>(leaseReports, pageable, 1);
        
        when(leaseReportReadModelRepository.findAll(pageable)).thenReturn(page);
        
        Page<LeaseReportReadModel> result = leaseReportCQRSService.findAllLeaseReports(pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLeaseContractNumber()).isEqualTo("LC-001");
    }

    @Test
    void findLeaseReport_ShouldReturnLeaseReport_WhenExists() {
        when(leaseReportReadModelRepository.findById(1L)).thenReturn(Optional.of(leaseReportReadModel));
        
        Optional<LeaseReportReadModel> result = leaseReportCQRSService.findLeaseReport(1L);
        
        assertThat(result).isPresent();
        assertThat(result.get().getLeaseContractNumber()).isEqualTo("LC-001");
    }

    @Test
    void findLeaseReport_ShouldReturnEmpty_WhenNotExists() {
        when(leaseReportReadModelRepository.findById(1L)).thenReturn(Optional.empty());
        
        Optional<LeaseReportReadModel> result = leaseReportCQRSService.findLeaseReport(1L);
        
        assertThat(result).isEmpty();
    }

    @Test
    void findLeaseReportsByDate_ShouldReturnPageOfLeaseReports() {
        List<LeaseReportReadModel> leaseReports = Arrays.asList(leaseReportReadModel);
        Page<LeaseReportReadModel> page = new PageImpl<>(leaseReports, pageable, 1);
        
        when(leaseReportReadModelRepository.findByReportingDate(reportingDate, pageable)).thenReturn(page);
        
        Page<LeaseReportReadModel> result = leaseReportCQRSService.findLeaseReportsByDate(reportingDate, pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getReportingDate()).isEqualTo(reportingDate);
    }

    @Test
    void findLeaseReportsByStatus_ShouldReturnPageOfLeaseReports() {
        List<LeaseReportReadModel> leaseReports = Arrays.asList(leaseReportReadModel);
        Page<LeaseReportReadModel> page = new PageImpl<>(leaseReports, pageable, 1);
        
        when(leaseReportReadModelRepository.findByLeaseStatus("ACTIVE", pageable)).thenReturn(page);
        
        Page<LeaseReportReadModel> result = leaseReportCQRSService.findLeaseReportsByStatus("ACTIVE", pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLeaseStatus()).isEqualTo("ACTIVE");
    }

    @Test
    void getTotalLeaseAmountByDate_ShouldReturnTotalAmount() {
        BigDecimal expectedTotal = new BigDecimal("150000.00");
        
        when(leaseReportReadModelRepository.getTotalLeaseAmountByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = leaseReportCQRSService.getTotalLeaseAmountByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getLeaseCountByDate_ShouldReturnCount() {
        Long expectedCount = 5L;
        
        when(leaseReportReadModelRepository.countByReportingDate(reportingDate)).thenReturn(expectedCount);
        
        Long result = leaseReportCQRSService.getLeaseCountByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    void getDistinctReportingDates_ShouldReturnListOfDates() {
        List<LocalDate> expectedDates = Arrays.asList(
            LocalDate.of(2024, 12, 31),
            LocalDate.of(2024, 11, 30),
            LocalDate.of(2024, 10, 31)
        );
        
        when(leaseReportReadModelRepository.findDistinctReportingDates()).thenReturn(expectedDates);
        
        List<LocalDate> result = leaseReportCQRSService.getDistinctReportingDates();
        
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedDates);
    }

    @Test
    void findLeaseReportsByLeaseId_ShouldReturnListOfLeaseReports() {
        List<LeaseReportReadModel> expectedReports = Arrays.asList(leaseReportReadModel);
        
        when(leaseReportReadModelRepository.findByLeaseId(100L)).thenReturn(expectedReports);
        
        List<LeaseReportReadModel> result = leaseReportCQRSService.findLeaseReportsByLeaseId(100L);
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLeaseId()).isEqualTo(100L);
    }
}
