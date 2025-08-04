package io.github.erp.erp.resources.leases;

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
import io.github.erp.repository.DetailedLeaseContractRepository;
import io.github.erp.service.DetailedLeaseContractQueryService;
import io.github.erp.service.DetailedLeaseContractService;
import io.github.erp.service.criteria.DetailedLeaseContractCriteria;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.DetailedLeaseContract}.
 */
@RestController
@RequestMapping("/api/leases")
public class DetailedLeaseContractResourceProd {

    private final Logger log = LoggerFactory.getLogger(DetailedLeaseContractResourceProd.class);

    private static final String ENTITY_NAME = "detailedLeaseContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailedLeaseContractService detailedLeaseContractService;

    private final DetailedLeaseContractRepository detailedLeaseContractRepository;

    private final DetailedLeaseContractQueryService detailedLeaseContractQueryService;

    public DetailedLeaseContractResourceProd(
        DetailedLeaseContractService detailedLeaseContractService,
        DetailedLeaseContractRepository detailedLeaseContractRepository,
        DetailedLeaseContractQueryService detailedLeaseContractQueryService
    ) {
        this.detailedLeaseContractService = detailedLeaseContractService;
        this.detailedLeaseContractRepository = detailedLeaseContractRepository;
        this.detailedLeaseContractQueryService = detailedLeaseContractQueryService;
    }

    /**
     * {@code POST  /ifrs-16-lease-contracts} : Create a new DetailedLeaseContract.
     *
     * @param detailedLeaseContractDTO the DetailedLeaseContractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new DetailedLeaseContractDTO, or with status {@code 400 (Bad Request)} if the DetailedLeaseContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ifrs-16-lease-contracts")
    public ResponseEntity<DetailedLeaseContractDTO> createDetailedLeaseContract(
        @Valid @RequestBody DetailedLeaseContractDTO detailedLeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DetailedLeaseContract : {}", detailedLeaseContractDTO);
        if (detailedLeaseContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new DetailedLeaseContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailedLeaseContractDTO result = detailedLeaseContractService.save(detailedLeaseContractDTO);
        return ResponseEntity
            .created(new URI("/api/ifrs-16-lease-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ifrs-16-lease-contracts/:id} : Updates an existing DetailedLeaseContract.
     *
     * @param id the id of the DetailedLeaseContractDTO to save.
     * @param detailedLeaseContractDTO the DetailedLeaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated DetailedLeaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the DetailedLeaseContractDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the DetailedLeaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<DetailedLeaseContractDTO> updateDetailedLeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailedLeaseContractDTO detailedLeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DetailedLeaseContract : {}, {}", id, detailedLeaseContractDTO);
        if (detailedLeaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailedLeaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailedLeaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailedLeaseContractDTO result = detailedLeaseContractService.save(detailedLeaseContractDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailedLeaseContractDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ifrs-16-lease-contracts/:id} : Partial updates given fields of an existing DetailedLeaseContract, field will ignore if it is null
     *
     * @param id the id of the DetailedLeaseContractDTO to save.
     * @param detailedLeaseContractDTO the DetailedLeaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated DetailedLeaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the DetailedLeaseContractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the DetailedLeaseContractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the DetailedLeaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ifrs-16-lease-contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailedLeaseContractDTO> partialUpdateDetailedLeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailedLeaseContractDTO detailedLeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailedLeaseContract partially : {}, {}", id, detailedLeaseContractDTO);
        if (detailedLeaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailedLeaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailedLeaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailedLeaseContractDTO> result = detailedLeaseContractService.partialUpdate(detailedLeaseContractDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, detailedLeaseContractDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ifrs-16-lease-contracts} : get all the iFRS16LeaseContracts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iFRS16LeaseContracts in body.
     */
    @GetMapping("/ifrs-16-lease-contracts")
    public ResponseEntity<List<IFRS16LeaseContractDTO>> getAllIFRS16LeaseContracts(
        IFRS16LeaseContractCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get IFRS16LeaseContracts by criteria: {}", criteria);
        Page<IFRS16LeaseContractDTO> page = iFRS16LeaseContractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ifrs-16-lease-contracts/count} : count all the iFRS16LeaseContracts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ifrs-16-lease-contracts/count")
    public ResponseEntity<Long> countIFRS16LeaseContracts(IFRS16LeaseContractCriteria criteria) {
        log.debug("REST request to count IFRS16LeaseContracts by criteria: {}", criteria);
        return ResponseEntity.ok().body(iFRS16LeaseContractQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ifrs-16-lease-contracts/:id} : get the "id" iFRS16LeaseContract.
     *
     * @param id the id of the iFRS16LeaseContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iFRS16LeaseContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<IFRS16LeaseContractDTO> getIFRS16LeaseContract(@PathVariable Long id) {
        log.debug("REST request to get IFRS16LeaseContract : {}", id);
        Optional<IFRS16LeaseContractDTO> iFRS16LeaseContractDTO = iFRS16LeaseContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iFRS16LeaseContractDTO);
    }

    /**
     * {@code DELETE  /ifrs-16-lease-contracts/:id} : delete the "id" iFRS16LeaseContract.
     *
     * @param id the id of the iFRS16LeaseContractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<Void> deleteIFRS16LeaseContract(@PathVariable Long id) {
        log.debug("REST request to delete IFRS16LeaseContract : {}", id);
        iFRS16LeaseContractService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ifrs-16-lease-contracts?query=:query} : search for the iFRS16LeaseContract corresponding
     * to the query.
     *
     * @param query the query of the iFRS16LeaseContract search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ifrs-16-lease-contracts")
    public ResponseEntity<List<IFRS16LeaseContractDTO>> searchIFRS16LeaseContracts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IFRS16LeaseContracts for query {}", query);
        Page<IFRS16LeaseContractDTO> page = iFRS16LeaseContractService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
