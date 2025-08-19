package io.github.erp.context.assets.service;

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
import io.github.erp.context.assets.domain.AssetDisposal;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.asset.AssetDisposedEvent;
import io.github.erp.internal.repository.InternalAssetDisposalRepository;
import io.github.erp.repository.search.AssetDisposalSearchRepository;
import io.github.erp.service.dto.AssetDisposalDTO;
import io.github.erp.service.mapper.AssetDisposalMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class InternalAssetDisposalServiceImpl {

    private final Logger log = LoggerFactory.getLogger(InternalAssetDisposalServiceImpl.class);

    private final InternalAssetDisposalRepository assetDisposalRepository;
    private final AssetDisposalMapper assetDisposalMapper;
    private final AssetDisposalSearchRepository assetDisposalSearchRepository;
    private final DomainEventPublisher domainEventPublisher;

    public InternalAssetDisposalServiceImpl(
        InternalAssetDisposalRepository assetDisposalRepository,
        AssetDisposalMapper assetDisposalMapper,
        AssetDisposalSearchRepository assetDisposalSearchRepository,
        DomainEventPublisher domainEventPublisher
    ) {
        this.assetDisposalRepository = assetDisposalRepository;
        this.assetDisposalMapper = assetDisposalMapper;
        this.assetDisposalSearchRepository = assetDisposalSearchRepository;
        this.domainEventPublisher = domainEventPublisher;
    }

    public AssetDisposalDTO save(AssetDisposalDTO assetDisposalDTO) {
        log.debug("Request to save AssetDisposal : {}", assetDisposalDTO);
        
        AssetDisposal assetDisposal = assetDisposalMapper.toEntity(assetDisposalDTO);
        boolean isNewDisposal = assetDisposal.getId() == null;
        
        assetDisposal = assetDisposalRepository.save(assetDisposal);
        AssetDisposalDTO result = assetDisposalMapper.toDto(assetDisposal);
        assetDisposalSearchRepository.save(assetDisposal);
        
        if (isNewDisposal) {
            publishAssetDisposedEvent(assetDisposal);
        }
        
        return result;
    }

    @Transactional(readOnly = true)
    public Page<AssetDisposalDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetDisposals");
        return assetDisposalRepository.findAll(pageable).map(assetDisposalMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<AssetDisposalDTO> findOne(Long id) {
        log.debug("Request to get AssetDisposal : {}", id);
        return assetDisposalRepository.findById(id).map(assetDisposalMapper::toDto);
    }

    public void delete(Long id) {
        log.debug("Request to delete AssetDisposal : {}", id);
        assetDisposalRepository.deleteById(id);
        assetDisposalSearchRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<AssetDisposalDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetDisposals for query {}", query);
        return assetDisposalSearchRepository.search(query, pageable).map(assetDisposalMapper::toDto);
    }

    private void publishAssetDisposedEvent(AssetDisposal assetDisposal) {
        try {
            AssetDisposedEvent event = new AssetDisposedEvent(
                assetDisposal.getAssetDisposed() != null ? assetDisposal.getAssetDisposed().getId().toString() : null,
                assetDisposal.getAssetDisposed() != null ? assetDisposal.getAssetDisposed().getAssetNumber() : null,
                assetDisposal.getAssetDisposed() != null ? assetDisposal.getAssetDisposed().getAssetDetails() : null,
                assetDisposal.getDisposalDate(),
                assetDisposal.getNetBookValue(),
                assetDisposal.getNetBookValue(),
                assetDisposal.getAssetDisposalReference() != null ? assetDisposal.getAssetDisposalReference().toString() : null,
                UUID.randomUUID()
            );
            domainEventPublisher.publish(event);
            log.info("Published AssetDisposedEvent for disposal: {}", assetDisposal.getId());
        } catch (Exception e) {
            log.error("Failed to publish AssetDisposedEvent for disposal: {}", assetDisposal.getId(), e);
        }
    }
}
