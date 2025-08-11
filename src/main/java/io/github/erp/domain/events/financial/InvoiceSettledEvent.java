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
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "invoice_settled_event")
public class InvoiceSettledEvent extends AbstractDomainEvent {

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "invoice_amount", precision = 21, scale = 2)
    private BigDecimal invoiceAmount;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "currency")
    private String currency;

    @Column(name = "payment_reference")
    private String paymentReference;

    @Column(name = "dealer_name")
    private String dealerName;

    @Column(name = "settlement_amount", precision = 21, scale = 2)
    private BigDecimal settlementAmount;

    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    protected InvoiceSettledEvent() {
        super();
    }

    public InvoiceSettledEvent(String invoiceId, String invoiceNumber, BigDecimal invoiceAmount, 
                             LocalDate invoiceDate, String currency, String paymentReference, 
                             String dealerName, BigDecimal settlementAmount, LocalDate settlementDate, 
                             UUID correlationId) {
        super(invoiceId, "Invoice", correlationId);
        this.invoiceNumber = invoiceNumber;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
        this.currency = currency;
        this.paymentReference = paymentReference;
        this.dealerName = dealerName;
        this.settlementAmount = settlementAmount;
        this.settlementDate = settlementDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(BigDecimal settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    @Override
    public String toString() {
        return "InvoiceSettledEvent{" +
            "invoiceNumber='" + invoiceNumber + '\'' +
            ", invoiceAmount=" + invoiceAmount +
            ", invoiceDate=" + invoiceDate +
            ", currency='" + currency + '\'' +
            ", paymentReference='" + paymentReference + '\'' +
            ", dealerName='" + dealerName + '\'' +
            ", settlementAmount=" + settlementAmount +
            ", settlementDate=" + settlementDate +
            "} " + super.toString();
    }
}
