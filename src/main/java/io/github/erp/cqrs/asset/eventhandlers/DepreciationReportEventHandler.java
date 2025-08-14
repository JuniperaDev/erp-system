package io.github.erp.cqrs.asset.eventhandlers;

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

import io.github.erp.cqrs.asset.readmodel.DepreciationReportReadModel;
import io.github.erp.cqrs.asset.repositories.DepreciationReportReadModelRepository;
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.domain.events.DomainEvent;
import io.github.erp.domain.events.DomainEventHandler;
import io.github.erp.domain.events.DomainEventHandlerMethodAnnotation;
import io.github.erp.domain.events.asset.DepreciationCalculatedEvent;
import io.github.erp.repository.DepreciationEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@DomainEventHandler
public class DepreciationReportEventHandler {

    private static final Logger log = LoggerFactory.getLogger(DepreciationReportEventHandler.class);

    private final DepreciationReportReadModelRepository depreciationReportReadModelRepository;
    private final DepreciationEntryRepository depreciationEntryRepository;

    public DepreciationReportEventHandler(
        DepreciationReportReadModelRepository depreciationReportReadModelRepository,
        DepreciationEntryRepository depreciationEntryRepository
    ) {
        this.depreciationReportReadModelRepository = depreciationReportReadModelRepository;
        this.depreciationEntryRepository = depreciationEntryRepository;
    }

    @DomainEventHandlerMethodAnnotation(eventType = "DepreciationCalculatedEvent", order = 2)
    @Transactional
    public void handleDepreciationCalculated(DomainEvent event) {
        if (event instanceof DepreciationCalculatedEvent) {
            DepreciationCalculatedEvent depreciationEvent = (DepreciationCalculatedEvent) event;
            log.info("Processing depreciation calculated event for read model: {}", depreciationEvent.getAggregateId());
            
            try {
                createOrUpdateDepreciationReportReadModel(depreciationEvent);
                log.info("Successfully processed depreciation report read model for: {}", depreciationEvent.getAggregateId());
            } catch (Exception e) {
                log.error("Failed to process depreciation report read model for: {}", depreciationEvent.getAggregateId(), e);
            }
        }
    }

    private void createOrUpdateDepreciationReportReadModel(DepreciationCalculatedEvent event) {
        Long depreciationEntryId = Long.parseLong(event.getAggregateId());
        Optional<DepreciationEntry> depreciationEntryOptional = depreciationEntryRepository.findById(depreciationEntryId);
        
        if (depreciationEntryOptional.isPresent()) {
            DepreciationEntry depreciationEntry = depreciationEntryOptional.get();
            
            Optional<DepreciationReportReadModel> existingReadModel = 
                depreciationReportReadModelRepository.findByAssetIdAndDepreciationPeriod(
                    event.getAssetId(), 
                    event.getDepreciationPeriod()
                );

            DepreciationReportReadModel readModel;
            if (existingReadModel.isPresent()) {
                readModel = existingReadModel.get();
                updateDepreciationReportReadModel(readModel, depreciationEntry, event);
            } else {
                readModel = createNewDepreciationReportReadModel(depreciationEntry, event);
            }

            depreciationReportReadModelRepository.save(readModel);
        }
    }

    private DepreciationReportReadModel createNewDepreciationReportReadModel(
        DepreciationEntry depreciationEntry, 
        DepreciationCalculatedEvent event
    ) {
        DepreciationReportReadModel readModel = new DepreciationReportReadModel()
            .assetId(event.getAssetId())
            .assetNumber(event.getAssetNumber())
            .depreciationAmount(event.getDepreciationAmount())
            .accumulatedDepreciation(event.getAccumulatedDepreciation())
            .netBookValue(event.getNetBookValue())
            .depreciationPeriod(event.getDepreciationPeriod())
            .depreciationDate(event.getCalculationDate())
            .lastUpdated(LocalDate.now());

        if (depreciationEntry.getAssetRegistration() != null) {
            readModel.assetTag(depreciationEntry.getAssetRegistration().getAssetTag())
                     .assetDescription(depreciationEntry.getAssetRegistration().getAssetDetails());

            if (depreciationEntry.getAssetRegistration().getAssetCategory() != null) {
                readModel.categoryName(depreciationEntry.getAssetRegistration().getAssetCategory().getAssetCategoryName())
                         .categoryId(depreciationEntry.getAssetRegistration().getAssetCategory().getId());
            }

            if (depreciationEntry.getAssetRegistration().getMainServiceOutlet() != null) {
                readModel.serviceOutletCode(depreciationEntry.getAssetRegistration().getMainServiceOutlet().getOutletCode())
                         .serviceOutletId(depreciationEntry.getAssetRegistration().getMainServiceOutlet().getId());
            }

            if (depreciationEntry.getAssetRegistration().getDealer() != null) {
                readModel.dealerName(depreciationEntry.getAssetRegistration().getDealer().getDealerName())
                         .dealerId(depreciationEntry.getAssetRegistration().getDealer().getId());
            }
        }

        if (depreciationEntry.getDepreciationPeriod() != null && 
            depreciationEntry.getDepreciationPeriod().getFiscalMonth() != null) {
            readModel.fiscalMonthCode(depreciationEntry.getDepreciationPeriod().getFiscalMonth().getFiscalMonthCode())
                     .fiscalYear(depreciationEntry.getDepreciationPeriod().getFiscalMonth().getFiscalYear().getFiscalYearCode());
        }

        return readModel;
    }

    private void updateDepreciationReportReadModel(
        DepreciationReportReadModel readModel, 
        DepreciationEntry depreciationEntry, 
        DepreciationCalculatedEvent event
    ) {
        readModel.depreciationAmount(event.getDepreciationAmount())
                 .accumulatedDepreciation(event.getAccumulatedDepreciation())
                 .netBookValue(event.getNetBookValue())
                 .depreciationDate(event.getCalculationDate())
                 .lastUpdated(LocalDate.now());

        if (depreciationEntry.getAssetRegistration() != null) {
            readModel.assetTag(depreciationEntry.getAssetRegistration().getAssetTag())
                     .assetDescription(depreciationEntry.getAssetRegistration().getAssetDetails());
        }
    }
}
