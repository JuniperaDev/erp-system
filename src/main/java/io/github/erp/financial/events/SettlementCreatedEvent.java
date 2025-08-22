package io.github.erp.financial.events;

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

}
