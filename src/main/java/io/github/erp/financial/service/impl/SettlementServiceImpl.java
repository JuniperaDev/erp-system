package io.github.erp.financial.service.impl;

import io.github.erp.financial.service.SettlementService;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.repository.SettlementRepository;
import io.github.erp.service.mapper.SettlementMapper;
import io.github.erp.domain.events.DomainEventPublisher;
import io.github.erp.domain.events.financial.SettlementCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service("financialSettlementServiceImpl")
@Transactional
public class SettlementServiceImpl implements SettlementService {

    private final Logger log = LoggerFactory.getLogger(SettlementServiceImpl.class);

    private final SettlementRepository settlementRepository;
    private final SettlementMapper settlementMapper;
    private final DomainEventPublisher domainEventPublisher;

    public SettlementServiceImpl(
        SettlementRepository settlementRepository,
        SettlementMapper settlementMapper,
        DomainEventPublisher domainEventPublisher
    ) {
        this.settlementRepository = settlementRepository;
        this.settlementMapper = settlementMapper;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    public SettlementDTO save(SettlementDTO settlementDTO) {
        log.debug("Request to save Settlement : {}", settlementDTO);
        var settlement = settlementMapper.toEntity(settlementDTO);
        settlement = settlementRepository.save(settlement);
        SettlementDTO result = settlementMapper.toDto(settlement);
        
        SettlementCreatedEvent event = new SettlementCreatedEvent(
            settlement.getId().toString(),
            settlement.getPaymentNumber(),
            settlement.getPaymentAmount(),
            settlement.getPaymentDate(),
            settlement.getSettlementCurrency() != null ? settlement.getSettlementCurrency().getIso4217CurrencyCode() : "USD",
            settlement.getDescription(),
            settlement.getBiller() != null ? settlement.getBiller().getDealerName() : null,
            java.util.UUID.randomUUID()
        );
        domainEventPublisher.publish(event);
        
        return result;
    }

    @Override
    public Optional<SettlementDTO> partialUpdate(SettlementDTO settlementDTO) {
        log.debug("Request to partially update Settlement : {}", settlementDTO);
        return settlementRepository
            .findById(settlementDTO.getId())
            .map(existingSettlement -> {
                settlementMapper.partialUpdate(existingSettlement, settlementDTO);
                return existingSettlement;
            })
            .map(settlementRepository::save)
            .map(settlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Settlements");
        return settlementRepository.findAll(pageable).map(settlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementDTO> findAllWithEagerRelationships(Pageable pageable) {
        return settlementRepository.findAllWithEagerRelationships(pageable).map(settlementMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SettlementDTO> findOne(Long id) {
        log.debug("Request to get Settlement : {}", id);
        return settlementRepository.findOneWithEagerRelationships(id).map(settlementMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Settlement : {}", id);
        settlementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettlementDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Settlements for query {}", query);
        return settlementRepository.findAll(pageable).map(settlementMapper::toDto);
    }
}
