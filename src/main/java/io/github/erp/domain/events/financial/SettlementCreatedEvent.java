package io.github.erp.domain.events.financial;

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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@DiscriminatorValue("SettlementCreatedEvent")
public class SettlementCreatedEvent extends AbstractDomainEvent {

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "settlement_currency_code")
    private String settlementCurrencyCode;

    @Column(name = "description")
    private String description;

    @Column(name = "biller_name")
    private String billerName;

    protected SettlementCreatedEvent() {
        super();
    }

    public SettlementCreatedEvent(String settlementId, String paymentNumber, BigDecimal paymentAmount, 
                                LocalDate paymentDate, String settlementCurrencyCode, String description, 
                                String billerName, UUID correlationId) {
        super(settlementId, "Settlement", correlationId);
        this.paymentNumber = paymentNumber;
        this.paymentAmount = paymentAmount;
        this.paymentDate = paymentDate;
        this.settlementCurrencyCode = settlementCurrencyCode;
        this.description = description;
        this.billerName = billerName;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
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

    public String getSettlementCurrencyCode() {
        return settlementCurrencyCode;
    }

    public void setSettlementCurrencyCode(String settlementCurrencyCode) {
        this.settlementCurrencyCode = settlementCurrencyCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBillerName() {
        return billerName;
    }

    public void setBillerName(String billerName) {
        this.billerName = billerName;
    }

    @Override
    public String toString() {
        return "SettlementCreatedEvent{" +
            "paymentNumber='" + paymentNumber + '\'' +
            ", paymentAmount=" + paymentAmount +
            ", paymentDate=" + paymentDate +
            ", settlementCurrencyCode='" + settlementCurrencyCode + '\'' +
            ", description='" + description + '\'' +
            ", billerName='" + billerName + '\'' +
            "} " + super.toString();
    }
}
