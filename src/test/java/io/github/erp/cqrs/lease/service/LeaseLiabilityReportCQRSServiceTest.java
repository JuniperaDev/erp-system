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

import io.github.erp.cqrs.lease.readmodel.LeaseLiabilityReportReadModel;
import io.github.erp.cqrs.lease.repositories.LeaseLiabilityReportReadModelRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeaseLiabilityReportCQRSServiceTest {

    @Mock
    private LeaseLiabilityReportReadModelRepository leaseLiabilityReportReadModelRepository;

    private LeaseLiabilityReportCQRSService leaseLiabilityReportCQRSService;

    private LeaseLiabilityReportReadModel leaseLiabilityReportReadModel;
    private LocalDate reportingDate;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        leaseLiabilityReportCQRSService = new LeaseLiabilityReportCQRSService(leaseLiabilityReportReadModelRepository);
        
        reportingDate = LocalDate.of(2024, 12, 31);
        pageable = PageRequest.of(0, 20);
        
        leaseLiabilityReportReadModel = new LeaseLiabilityReportReadModel();
        leaseLiabilityReportReadModel.setId(1L);
        leaseLiabilityReportReadModel.setLeaseId(100L);
        leaseLiabilityReportReadModel.setLeaseContractNumber("LC-001");
        leaseLiabilityReportReadModel.setLeaseTitle("Office Building Lease");
        leaseLiabilityReportReadModel.setLesseeName("ABC Corporation");
        leaseLiabilityReportReadModel.setOpeningBalance(new BigDecimal("100000.00"));
        leaseLiabilityReportReadModel.setCashPayment(new BigDecimal("5000.00"));
        leaseLiabilityReportReadModel.setPrincipalPayment(new BigDecimal("4000.00"));
        leaseLiabilityReportReadModel.setInterestPayment(new BigDecimal("1000.00"));
        leaseLiabilityReportReadModel.setOutstandingBalance(new BigDecimal("96000.00"));
        leaseLiabilityReportReadModel.setReportingDate(reportingDate);
        leaseLiabilityReportReadModel.setFiscalYear(2024);
    }

    @Test
    void findAllLeaseLiabilityReports_ShouldReturnPageOfReports() {
        List<LeaseLiabilityReportReadModel> reports = Arrays.asList(leaseLiabilityReportReadModel);
        Page<LeaseLiabilityReportReadModel> page = new PageImpl<>(reports, pageable, 1);
        
        when(leaseLiabilityReportReadModelRepository.findAll(pageable)).thenReturn(page);
        
        Page<LeaseLiabilityReportReadModel> result = leaseLiabilityReportCQRSService.findAllLeaseLiabilityReports(pageable);
        
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getLeaseContractNumber()).isEqualTo("LC-001");
    }

    @Test
    void findLeaseLiabilityReport_ShouldReturnReport_WhenExists() {
        when(leaseLiabilityReportReadModelRepository.findById(1L)).thenReturn(Optional.of(leaseLiabilityReportReadModel));
        
        Optional<LeaseLiabilityReportReadModel> result = leaseLiabilityReportCQRSService.findLeaseLiabilityReport(1L);
        
        assertThat(result).isPresent();
        assertThat(result.get().getLeaseContractNumber()).isEqualTo("LC-001");
    }

    @Test
    void getTotalOpeningBalanceByDate_ShouldReturnTotalBalance() {
        BigDecimal expectedTotal = new BigDecimal("500000.00");
        
        when(leaseLiabilityReportReadModelRepository.getTotalOpeningBalanceByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = leaseLiabilityReportCQRSService.getTotalOpeningBalanceByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalCashPaymentByDate_ShouldReturnTotalPayment() {
        BigDecimal expectedTotal = new BigDecimal("25000.00");
        
        when(leaseLiabilityReportReadModelRepository.getTotalCashPaymentByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = leaseLiabilityReportCQRSService.getTotalCashPaymentByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalPrincipalPaymentByDate_ShouldReturnTotalPayment() {
        BigDecimal expectedTotal = new BigDecimal("20000.00");
        
        when(leaseLiabilityReportReadModelRepository.getTotalPrincipalPaymentByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = leaseLiabilityReportCQRSService.getTotalPrincipalPaymentByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalInterestPaymentByDate_ShouldReturnTotalPayment() {
        BigDecimal expectedTotal = new BigDecimal("5000.00");
        
        when(leaseLiabilityReportReadModelRepository.getTotalInterestPaymentByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = leaseLiabilityReportCQRSService.getTotalInterestPaymentByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getTotalOutstandingBalanceByDate_ShouldReturnTotalBalance() {
        BigDecimal expectedTotal = new BigDecimal("480000.00");
        
        when(leaseLiabilityReportReadModelRepository.getTotalOutstandingBalanceByReportingDate(reportingDate)).thenReturn(expectedTotal);
        
        BigDecimal result = leaseLiabilityReportCQRSService.getTotalOutstandingBalanceByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedTotal);
    }

    @Test
    void getLeaseLiabilityCountByDate_ShouldReturnCount() {
        Long expectedCount = 10L;
        
        when(leaseLiabilityReportReadModelRepository.countByReportingDate(reportingDate)).thenReturn(expectedCount);
        
        Long result = leaseLiabilityReportCQRSService.getLeaseLiabilityCountByDate(reportingDate);
        
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    void getDistinctReportingDates_ShouldReturnListOfDates() {
        List<LocalDate> expectedDates = Arrays.asList(
            LocalDate.of(2024, 12, 31),
            LocalDate.of(2024, 11, 30),
            LocalDate.of(2024, 10, 31)
        );
        
        when(leaseLiabilityReportReadModelRepository.findDistinctReportingDates()).thenReturn(expectedDates);
        
        List<LocalDate> result = leaseLiabilityReportCQRSService.getDistinctReportingDates();
        
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(expectedDates);
    }

    @Test
    void findLeaseLiabilityReportsByLeaseId_ShouldReturnListOfReports() {
        List<LeaseLiabilityReportReadModel> expectedReports = Arrays.asList(leaseLiabilityReportReadModel);
        
        when(leaseLiabilityReportReadModelRepository.findByLeaseId(100L)).thenReturn(expectedReports);
        
        List<LeaseLiabilityReportReadModel> result = leaseLiabilityReportCQRSService.findLeaseLiabilityReportsByLeaseId(100L);
        
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLeaseId()).isEqualTo(100L);
    }
}
