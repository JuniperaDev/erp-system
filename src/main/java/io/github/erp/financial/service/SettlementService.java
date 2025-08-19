package io.github.erp.financial.service;

import io.github.erp.financial.service.dto.SettlementDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link io.github.erp.financial.domain.Settlement}.
 */
public interface SettlementService {
    /**
     * Save a settlement.
     *
     * @param settlementDTO the entity to save.
     * @return the persisted entity.
     */
    SettlementDTO save(SettlementDTO settlementDTO);

    /**
     * Updates a settlement.
     *
     * @param settlementDTO the entity to update.
     * @return the persisted entity.
     */
    SettlementDTO update(SettlementDTO settlementDTO);

    /**
     * Partially updates a settlement.
     *
     * @param settlementDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SettlementDTO> partialUpdate(SettlementDTO settlementDTO);

    /**
     * Get all the settlements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementDTO> findAll(Pageable pageable);

    /**
     * Get the "id" settlement.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SettlementDTO> findOne(Long id);

    /**
     * Delete the "id" settlement.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the settlement corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SettlementDTO> search(String query, Pageable pageable);
}
