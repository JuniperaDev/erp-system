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
@Table(name = "lease_payment_made_event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LeasePaymentMadeEvent extends AbstractDomainEvent {

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "lease_contract_id")
    private Long leaseContractId;

    @Column(name = "lease_contract_booking_id")
    private String leaseContractBookingId;

    @Column(name = "lease_contract_title")
    private String leaseContractTitle;

    protected LeasePaymentMadeEvent() {
        super();
    }

    public LeasePaymentMadeEvent(String leasePaymentId, BigDecimal paymentAmount, LocalDate paymentDate,
                               Long leaseContractId, String leaseContractBookingId, String leaseContractTitle,
                               UUID correlationId) {
        super(leasePaymentId, "LeasePayment", correlationId);
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.leaseContractId = leaseContractId;
        this.leaseContractBookingId = leaseContractBookingId;
        this.leaseContractTitle = leaseContractTitle;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
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
}
