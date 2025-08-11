package io.github.erp.domain.events.lease;

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

import io.github.erp.IntegrationTest;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.internal.service.leases.InternalDetailedLeaseContractService;
import io.github.erp.internal.service.leases.InternalLeasePaymentService;
import io.github.erp.internal.service.leases.InternalLeaseLiabilityService;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.LeaseLiabilityDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@IntegrationTest
class LeaseEventIntegrationTest {

    @Autowired
    private InternalDetailedLeaseContractService detailedLeaseContractService;

    @Autowired
    private InternalLeasePaymentService leasePaymentService;

    @Autowired
    private InternalLeaseLiabilityService leaseLiabilityService;

    @SpyBean
    private DomainEventPublisher domainEventPublisher;

    @Test
    @Transactional
    void shouldPublishLeaseContractCreatedEvent() {
        DetailedLeaseContractDTO leaseContract = new DetailedLeaseContractDTO();
        leaseContract.setBookingId("TEST-BOOKING-001");
        leaseContract.setLeaseTitle("Test Lease Contract");
        leaseContract.setShortTitle("Test Lease");
        leaseContract.setInceptionDate(LocalDate.now());
        leaseContract.setCommencementDate(LocalDate.now().plusDays(30));
        leaseContract.setSerialNumber(UUID.randomUUID());

        detailedLeaseContractService.save(leaseContract);

        verify(domainEventPublisher, times(1)).publishEvent(any(LeaseContractCreatedEvent.class));
    }

    @Test
    @Transactional
    void shouldPublishLeasePaymentMadeEvent() {
        LeasePaymentDTO leasePayment = new LeasePaymentDTO();
        leasePayment.setPaymentAmount(BigDecimal.valueOf(1000.00));
        leasePayment.setPaymentDate(LocalDate.now());

        leasePaymentService.save(leasePayment);

        verify(domainEventPublisher, times(1)).publishEvent(any(LeasePaymentMadeEvent.class));
    }

    @Test
    @Transactional
    void shouldPublishLeaseLiabilityCalculatedEvent() {
        LeaseLiabilityDTO leaseLiability = new LeaseLiabilityDTO();
        leaseLiability.setLeaseId("TEST-LEASE-001");
        leaseLiability.setLiabilityAmount(BigDecimal.valueOf(50000.00));
        leaseLiability.setInterestRate(BigDecimal.valueOf(0.05));
        leaseLiability.setStartDate(LocalDate.now());
        leaseLiability.setEndDate(LocalDate.now().plusYears(5));

        leaseLiabilityService.save(leaseLiability);

        verify(domainEventPublisher, times(1)).publishEvent(any(LeaseLiabilityCalculatedEvent.class));
    }
}
