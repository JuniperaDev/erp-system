package io.github.erp.domain.events.financial;

import io.github.erp.domain.events.AbstractDomainEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Domain event for invoice processing.
 */
public class InvoiceProcessedEvent extends AbstractDomainEvent {
    
    private final String invoiceId;
    private final String invoiceNumber;
    private final BigDecimal invoiceAmount;
    private final LocalDate invoiceDate;

    public InvoiceProcessedEvent(String invoiceId, String invoiceNumber, BigDecimal invoiceAmount, LocalDate invoiceDate) {
        super(invoiceId, "Invoice", UUID.randomUUID());
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
    }

    @Override
    public String getEventType() {
        return "InvoiceProcessed";
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    @Override
    public String toString() {
        return "InvoiceProcessedEvent{" +
            "invoiceId='" + invoiceId + '\'' +
            ", invoiceNumber='" + invoiceNumber + '\'' +
            ", invoiceAmount=" + invoiceAmount +
            ", invoiceDate=" + invoiceDate +
            ", occurredOn=" + getOccurredOn() +
            '}';
    }
}
