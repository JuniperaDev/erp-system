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

import io.github.erp.domain.events.AbstractDomainEvent;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "lease_liability_calculated_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeaseLiabilityCalculatedEvent extends AbstractDomainEvent {

    @Column(name = "lease_id")
    private String leaseId;

    @Column(name = "liability_amount", precision = 21, scale = 2)
    private BigDecimal liabilityAmount;

    @Column(name = "interest_rate", precision = 21, scale = 15)
    private BigDecimal interestRate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "lease_contract_id")
    private Long leaseContractId;

    @Column(name = "lease_contract_booking_id")
    private String leaseContractBookingId;

    @Column(name = "lease_contract_title")
    private String leaseContractTitle;

    @Column(name = "lease_amortization_calculation_id")
    private Long leaseAmortizationCalculationId;

    protected LeaseLiabilityCalculatedEvent() {
        super();
    }

    public LeaseLiabilityCalculatedEvent(String leaseLiabilityId, String leaseId, BigDecimal liabilityAmount,
                                       BigDecimal interestRate, LocalDate startDate, LocalDate endDate,
                                       Long leaseContractId, String leaseContractBookingId, String leaseContractTitle,
                                       Long leaseAmortizationCalculationId, UUID correlationId) {
        super(leaseLiabilityId, "LeaseLiability", correlationId);
        this.leaseId = leaseId;
        this.liabilityAmount = liabilityAmount;
        this.interestRate = interestRate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaseContractId = leaseContractId;
        this.leaseContractBookingId = leaseContractBookingId;
        this.leaseContractTitle = leaseContractTitle;
        this.leaseAmortizationCalculationId = leaseAmortizationCalculationId;
    }

    public String getLeaseId() {
        return leaseId;
    }

    public void setLeaseId(String leaseId) {
        this.leaseId = leaseId;
    }

    public BigDecimal getLiabilityAmount() {
        return liabilityAmount;
    }

    public void setLiabilityAmount(BigDecimal liabilityAmount) {
        this.liabilityAmount = liabilityAmount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getLeaseContractId() {
        return leaseContractId;
    }

    public void setLeaseContractId(Long leaseContractId) {
        this.leaseContractId = leaseContractId;
    }

    public String getLeaseContractBookingId() {
        return leaseContractBookingId;
    }

    public void setLeaseContractBookingId(String leaseContractBookingId) {
        this.leaseContractBookingId = leaseContractBookingId;
    }

    public String getLeaseContractTitle() {
        return leaseContractTitle;
    }

    public void setLeaseContractTitle(String leaseContractTitle) {
        this.leaseContractTitle = leaseContractTitle;
    }

    public Long getLeaseAmortizationCalculationId() {
        return leaseAmortizationCalculationId;
    }

    public void setLeaseAmortizationCalculationId(Long leaseAmortizationCalculationId) {
        this.leaseAmortizationCalculationId = leaseAmortizationCalculationId;
    }
}
