package io.github.erp.financial.service.impl;

import io.github.erp.financial.domain.Settlement;
import io.github.erp.financial.repository.SettlementRepository;
import io.github.erp.financial.service.SettlementService;
import io.github.erp.financial.service.dto.SettlementDTO;
import io.github.erp.financial.service.mapper.SettlementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Settlement}.
 */
@Service
@Transactional
public class SettlementServiceImpl implements SettlementService {

    private final Logger log = LoggerFactory.getLogger(SettlementServiceImpl.class);

    private final SettlementRepository settlementRepository;

    private final SettlementMapper settlementMapper;

    public SettlementServiceImpl(SettlementRepository settlementRepository, SettlementMapper settlementMapper) {
        this.settlementRepository = settlementRepository;
        this.settlementMapper = settlementMapper;
    }

    @Override
    public SettlementDTO save(SettlementDTO settlementDTO) {
        log.debug("Request to save Settlement : {}", settlementDTO);
        Settlement settlement = settlementMapper.toEntity(settlementDTO);
        settlement = settlementRepository.save(settlement);
        return settlementMapper.toDto(settlement);
    }

    @Override
    public SettlementDTO update(SettlementDTO settlementDTO) {
        log.debug("Request to update Settlement : {}", settlementDTO);
        Settlement settlement = settlementMapper.toEntity(settlementDTO);
        settlement = settlementRepository.save(settlement);
        return settlementMapper.toDto(settlement);
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
    public Optional<SettlementDTO> findOne(Long id) {
        log.debug("Request to get Settlement : {}", id);
        return settlementRepository.findById(id).map(settlementMapper::toDto);
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
