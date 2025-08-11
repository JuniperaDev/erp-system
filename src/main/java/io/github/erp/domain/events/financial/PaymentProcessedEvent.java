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
@DiscriminatorValue("PaymentProcessedEvent")
public class PaymentProcessedEvent extends AbstractDomainEvent {

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_amount", precision = 21, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "invoiced_amount", precision = 21, scale = 2)
    private BigDecimal invoicedAmount;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "settlement_currency")
    private String settlementCurrency;

    @Column(name = "description")
    private String description;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "purchase_order_number")
    private String purchaseOrderNumber;

    protected PaymentProcessedEvent() {
        super();
    }

    public PaymentProcessedEvent(String paymentId, String paymentNumber, BigDecimal paymentAmount, 
                               BigDecimal invoicedAmount, LocalDate paymentDate, String settlementCurrency, 
                               String description, String dealerName, String purchaseOrderNumber, 
                               UUID correlationId) {
        super(paymentId, "Payment", correlationId);
        this.paymentNumber = paymentNumber;
        this.paymentAmount = paymentAmount;
        this.invoicedAmount = invoicedAmount;
        this.paymentDate = paymentDate;
        this.settlementCurrency = settlementCurrency;
        this.description = description;
        this.dealerName = dealerName;
        this.purchaseOrderNumber = purchaseOrderNumber;
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

    public BigDecimal getInvoicedAmount() {
        return invoicedAmount;
    }

    public void setInvoicedAmount(BigDecimal invoicedAmount) {
        this.invoicedAmount = invoicedAmount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public void setSettlementCurrency(String settlementCurrency) {
        this.settlementCurrency = settlementCurrency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    @Override
    public String toString() {
        return "PaymentProcessedEvent{" +
            "paymentNumber='" + paymentNumber + '\'' +
            ", paymentAmount=" + paymentAmount +
            ", invoicedAmount=" + invoicedAmount +
            ", paymentDate=" + paymentDate +
            ", settlementCurrency='" + settlementCurrency + '\'' +
            ", description='" + description + '\'' +
            ", dealerName='" + dealerName + '\'' +
            ", purchaseOrderNumber='" + purchaseOrderNumber + '\'' +
            "} " + super.toString();
    }
}
