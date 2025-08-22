package io.github.erp.domain.events.asset;

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

import io.github.erp.domain.events.DomainEvent;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class DepreciationCalculatedEvent implements DomainEvent {

    private final UUID eventId;
    private final String aggregateId;
    private final Long assetId;
    private final String assetNumber;
    private final BigDecimal depreciationAmount;
    private final BigDecimal accumulatedDepreciation;
    private final BigDecimal netBookValue;
    private final String depreciationPeriod;
    private final LocalDate calculationDate;
    private final Instant occurredOn;
    private final Integer version;
    private final UUID correlationId;

    public DepreciationCalculatedEvent(
        String aggregateId,
        Long assetId,
        String assetNumber,
        BigDecimal depreciationAmount,
        BigDecimal accumulatedDepreciation,
        BigDecimal netBookValue,
        String depreciationPeriod,
        LocalDate calculationDate
    ) {
        this.eventId = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.assetId = assetId;
        this.assetNumber = assetNumber;
        this.depreciationAmount = depreciationAmount;
        this.accumulatedDepreciation = accumulatedDepreciation;
        this.netBookValue = netBookValue;
        this.depreciationPeriod = depreciationPeriod;
        this.calculationDate = calculationDate;
        this.occurredOn = Instant.now();
        this.version = 1;
        this.correlationId = UUID.randomUUID();
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return "DepreciationCalculatedEvent";
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
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return "DepreciationEntry";
    }

    public Long getAssetId() {
        return assetId;
    }

    public String getAssetNumber() {
        return assetNumber;
    }

    public BigDecimal getDepreciationAmount() {
        return depreciationAmount;
    }

    public BigDecimal getAccumulatedDepreciation() {
        return accumulatedDepreciation;
    }

    public BigDecimal getNetBookValue() {
        return netBookValue;
    }

    public String getDepreciationPeriod() {
        return depreciationPeriod;
    }

    public LocalDate getCalculationDate() {
        return calculationDate;
    }
}
