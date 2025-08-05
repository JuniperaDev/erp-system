package io.github.erp.internal.service.leases;

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
import io.github.erp.domain.DetailedLeaseContract;
import io.github.erp.internal.repository.InternalDetailedLeaseContractRepository;
import io.github.erp.repository.DetailedLeaseContractRepository;
import io.github.erp.repository.search.DetailedLeaseContractSearchRepository;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
import io.github.erp.service.mapper.DetailedLeaseContractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link DetailedLeaseContract}.
 */
@Service
@Transactional
public class InternalDetailedLeaseContractServiceImpl implements InternalDetailedLeaseContractService {

    private final Logger log = LoggerFactory.getLogger(InternalDetailedLeaseContractServiceImpl.class);

    private final InternalDetailedLeaseContractRepository detailedLeaseContractRepository;

    private final DetailedLeaseContractMapper detailedLeaseContractMapper;

    private final DetailedLeaseContractSearchRepository detailedLeaseContractSearchRepository;

    public InternalDetailedLeaseContractServiceImpl(
        InternalDetailedLeaseContractRepository detailedLeaseContractRepository,
        DetailedLeaseContractMapper detailedLeaseContractMapper,
        DetailedLeaseContractSearchRepository detailedLeaseContractSearchRepository
    ) {
        this.detailedLeaseContractRepository = detailedLeaseContractRepository;
        this.detailedLeaseContractMapper = detailedLeaseContractMapper;
        this.detailedLeaseContractSearchRepository = detailedLeaseContractSearchRepository;
    }

    @Override
    public DetailedLeaseContractDTO save(DetailedLeaseContractDTO detailedLeaseContractDTO) {
        log.debug("Request to save DetailedLeaseContract : {}", detailedLeaseContractDTO);
        DetailedLeaseContract detailedLeaseContract = detailedLeaseContractMapper.toEntity(detailedLeaseContractDTO);
        detailedLeaseContract = detailedLeaseContractRepository.save(detailedLeaseContract);
        DetailedLeaseContractDTO result = detailedLeaseContractMapper.toDto(detailedLeaseContract);
        detailedLeaseContractSearchRepository.save(detailedLeaseContract);
        return result;
    }

    @Override
    public Optional<DetailedLeaseContractDTO> partialUpdate(DetailedLeaseContractDTO detailedLeaseContractDTO) {
        log.debug("Request to partially update DetailedLeaseContract : {}", detailedLeaseContractDTO);

        return detailedLeaseContractRepository
            .findById(detailedLeaseContractDTO.getId())
            .map(existingDetailedLeaseContract -> {
                detailedLeaseContractMapper.partialUpdate(existingDetailedLeaseContract, detailedLeaseContractDTO);

                return existingDetailedLeaseContract;
            })
            .map(detailedLeaseContractRepository::save)
            .map(savedDetailedLeaseContract -> {
                detailedLeaseContractSearchRepository.save(savedDetailedLeaseContract);

                return savedDetailedLeaseContract;
            })
            .map(detailedLeaseContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetailedLeaseContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DetailedLeaseContracts");
        return detailedLeaseContractRepository.findAll(pageable).map(detailedLeaseContractMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetailedLeaseContractDTO> findOne(Long id) {
        log.debug("Request to get DetailedLeaseContract : {}", id);
        return detailedLeaseContractRepository.findById(id).map(detailedLeaseContractMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DetailedLeaseContract : {}", id);
        detailedLeaseContractRepository.deleteById(id);
        detailedLeaseContractSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DetailedLeaseContractDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of DetailedLeaseContracts for query {}", query);
        return detailedLeaseContractSearchRepository.search(query, pageable).map(detailedLeaseContractMapper::toDto);
    }
}
