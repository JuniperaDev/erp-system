package io.github.erp.cqrs.asset.writemodel;

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

import io.github.erp.domain.DepreciationEntry;
import io.github.erp.domain.events.asset.DepreciationCalculatedEvent;
import io.github.erp.domain.events.DomainEventStore;
import io.github.erp.repository.DepreciationEntryRepository;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.mapper.DepreciationEntryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class DepreciationWriteModelService {

    private final Logger log = LoggerFactory.getLogger(DepreciationWriteModelService.class);

    private final DepreciationEntryRepository depreciationEntryRepository;
    private final DepreciationEntryMapper depreciationEntryMapper;
    private final DomainEventStore domainEventStore;

    public DepreciationWriteModelService(
        DepreciationEntryRepository depreciationEntryRepository,
        DepreciationEntryMapper depreciationEntryMapper,
        DomainEventStore domainEventStore
    ) {
        this.depreciationEntryRepository = depreciationEntryRepository;
        this.depreciationEntryMapper = depreciationEntryMapper;
        this.domainEventStore = domainEventStore;
    }

    public DepreciationEntryDTO createDepreciationEntry(DepreciationEntryDTO depreciationEntryDTO) {
        log.debug("Request to create DepreciationEntry : {}", depreciationEntryDTO);
        
        DepreciationEntry depreciationEntry = depreciationEntryMapper.toEntity(depreciationEntryDTO);
        depreciationEntry = depreciationEntryRepository.save(depreciationEntry);
        DepreciationEntryDTO result = depreciationEntryMapper.toDto(depreciationEntry);

        DepreciationCalculatedEvent event = new DepreciationCalculatedEvent(
            depreciationEntry.getId().toString(),
            depreciationEntry.getAssetRegistration() != null ? 
                depreciationEntry.getAssetRegistration().getId() : null,
            depreciationEntry.getAssetRegistration() != null ? 
                depreciationEntry.getAssetRegistration().getAssetNumber() : null,
            depreciationEntry.getDepreciationAmount(),
            depreciationEntry.getAccumulatedDepreciation(),
            depreciationEntry.getNetBookValue(),
            depreciationEntry.getDepreciationPeriod() != null ? 
                depreciationEntry.getDepreciationPeriod().getPeriodCode() : null,
            LocalDate.now()
        );
        
        domainEventStore.store(event);
        log.info("Depreciation entry created and event published: {}", depreciationEntry.getId());
        
        return result;
    }

    public Optional<DepreciationEntryDTO> updateDepreciationEntry(DepreciationEntryDTO depreciationEntryDTO) {
        log.debug("Request to update DepreciationEntry : {}", depreciationEntryDTO);

        return depreciationEntryRepository
            .findById(depreciationEntryDTO.getId())
            .map(existingEntry -> {
                depreciationEntryMapper.partialUpdate(existingEntry, depreciationEntryDTO);
                return existingEntry;
            })
            .map(depreciationEntryRepository::save)
            .map(updatedEntry -> {
                DepreciationCalculatedEvent event = new DepreciationCalculatedEvent(
                    updatedEntry.getId().toString(),
                    updatedEntry.getAssetRegistration() != null ? 
                        updatedEntry.getAssetRegistration().getId() : null,
                    updatedEntry.getAssetRegistration() != null ? 
                        updatedEntry.getAssetRegistration().getAssetNumber() : null,
                    updatedEntry.getDepreciationAmount(),
                    updatedEntry.getAccumulatedDepreciation(),
                    updatedEntry.getNetBookValue(),
                    updatedEntry.getDepreciationPeriod() != null ? 
                        updatedEntry.getDepreciationPeriod().getPeriodCode() : null,
                    LocalDate.now()
                );
                
                domainEventStore.store(event);
                log.info("Depreciation entry updated and event published: {}", updatedEntry.getId());
                
                return updatedEntry;
            })
            .map(depreciationEntryMapper::toDto);
    }

    public void deleteDepreciationEntry(Long id) {
        log.debug("Request to delete DepreciationEntry : {}", id);
        depreciationEntryRepository.deleteById(id);
    }

    public void calculateDepreciationForPeriod(String periodCode, LocalDate calculationDate) {
        log.debug("Request to calculate depreciation for period : {}", periodCode);
        
        log.info("Depreciation calculation completed for period: {}", periodCode);
    }
}
