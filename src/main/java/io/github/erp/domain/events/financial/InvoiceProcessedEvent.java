package io.github.erp.domain.events.financial;

import io.github.erp.domain.events.DomainEvent;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain event for invoice processing.
 */
public class InvoiceProcessedEvent implements DomainEvent {
    
    private final UUID eventId;
    private final String invoiceId;
    private final String invoiceNumber;
    private final BigDecimal invoiceAmount;
    private final LocalDate invoiceDate;
    private final Instant occurredOn;
    private final UUID correlationId;
    private final Integer version;

    public InvoiceProcessedEvent(String invoiceId, String invoiceNumber, BigDecimal invoiceAmount, LocalDate invoiceDate) {
        this.eventId = UUID.randomUUID();
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceAmount = invoiceAmount;
        this.invoiceDate = invoiceDate;
        this.occurredOn = Instant.now();
        this.correlationId = UUID.randomUUID();
        this.version = 1;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return "InvoiceProcessed";
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    @Override
    public UUID getCorrelationId() {
        return correlationId;
    }

    @Override
    public String getAggregateId() {
        return invoiceId;
    }

    @Override
    public String getAggregateType() {
        return "Invoice";
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
            ", occurredOn=" + occurredOn +
            '}';
    }
}
