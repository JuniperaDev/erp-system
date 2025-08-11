package io.github.erp.web.rest;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.DetailedLeaseContractRepository;
import io.github.erp.service.DetailedLeaseContractQueryService;
import io.github.erp.service.DetailedLeaseContractService;
import io.github.erp.service.criteria.DetailedLeaseContractCriteria;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.DetailedLeaseContract}.
 */
@RestController
@RequestMapping("/api/v2")
public class DetailedLeaseContractResource {

    private final Logger log = LoggerFactory.getLogger(DetailedLeaseContractResource.class);

    private static final String ENTITY_NAME = "detailedLeaseContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailedLeaseContractService detailedLeaseContractService;

    private final DetailedLeaseContractRepository detailedLeaseContractRepository;

    private final DetailedLeaseContractQueryService detailedLeaseContractQueryService;

    public DetailedLeaseContractResource(
        DetailedLeaseContractService detailedLeaseContractService,
        DetailedLeaseContractRepository detailedLeaseContractRepository,
        DetailedLeaseContractQueryService detailedLeaseContractQueryService
    ) {
        this.detailedLeaseContractService = detailedLeaseContractService;
        this.detailedLeaseContractRepository = detailedLeaseContractRepository;
        this.detailedLeaseContractQueryService = detailedLeaseContractQueryService;
    }

    /**
     * {@code POST  /detailed-lease-contracts} : Create a new DetailedLeaseContract.
     *
     * @param detailedLeaseContractDTO the detailedLeaseContractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailedLeaseContractDTO, or with status {@code 400 (Bad Request)} if the DetailedLeaseContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detailed-lease-contracts")
    public ResponseEntity<DetailedLeaseContractDTO> createDetailedLeaseContract(
        @Valid @RequestBody DetailedLeaseContractDTO detailedLeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DetailedLeaseContract : {}", detailedLeaseContractDTO);
        if (detailedLeaseContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new DetailedLeaseContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailedLeaseContractDTO result = detailedLeaseContractService.save(detailedLeaseContractDTO);
        return ResponseEntity
            .created(new URI("/api/v2/detailed-lease-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detailed-lease-contracts/:id} : Updates an existing DetailedLeaseContract.
     *
     * @param id the id of the DetailedLeaseContractDTO to save.
     * @param detailedLeaseContractDTO the DetailedLeaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated DetailedLeaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the DetailedLeaseContractDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the DetailedLeaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detailed-lease-contracts/{id}")
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
     * {@code PATCH  /detailed-lease-contracts/:id} : Partial updates given fields of an existing DetailedLeaseContract, field will ignore if it is null
     *
     * @param id the id of the DetailedLeaseContractDTO to save.
     * @param detailedLeaseContractDTO the DetailedLeaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated DetailedLeaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the DetailedLeaseContractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the DetailedLeaseContractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the DetailedLeaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detailed-lease-contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
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
     * {@code GET  /detailed-lease-contracts} : get all the DetailedLeaseContracts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of DetailedLeaseContracts in body.
     */
    @GetMapping("/detailed-lease-contracts")
    public ResponseEntity<List<DetailedLeaseContractDTO>> getAllDetailedLeaseContracts(
        DetailedLeaseContractCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get DetailedLeaseContracts by criteria: {}", criteria);
        Page<DetailedLeaseContractDTO> page = detailedLeaseContractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /detailed-lease-contracts/count} : count all the DetailedLeaseContracts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/detailed-lease-contracts/count")
    public ResponseEntity<Long> countDetailedLeaseContracts(DetailedLeaseContractCriteria criteria) {
        log.debug("REST request to count DetailedLeaseContracts by criteria: {}", criteria);
        return ResponseEntity.ok().body(detailedLeaseContractQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /detailed-lease-contracts/:id} : get the "id" DetailedLeaseContract.
     *
     * @param id the id of the DetailedLeaseContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the DetailedLeaseContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detailed-lease-contracts/{id}")
    public ResponseEntity<DetailedLeaseContractDTO> getDetailedLeaseContract(@PathVariable Long id) {
        log.debug("REST request to get DetailedLeaseContract : {}", id);
        Optional<DetailedLeaseContractDTO> detailedLeaseContractDTO = detailedLeaseContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailedLeaseContractDTO);
    }

    /**
     * {@code DELETE  /detailed-lease-contracts/:id} : delete the "id" DetailedLeaseContract.
     *
     * @param id the id of the DetailedLeaseContractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detailed-lease-contracts/{id}")
    public ResponseEntity<Void> deleteDetailedLeaseContract(@PathVariable Long id) {
        log.debug("REST request to delete DetailedLeaseContract : {}", id);
        detailedLeaseContractService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/detailed-lease-contracts?query=:query} : search for the DetailedLeaseContract corresponding
     * to the query.
     *
     * @param query the query of the DetailedLeaseContract search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/detailed-lease-contracts")
    public ResponseEntity<List<DetailedLeaseContractDTO>> searchDetailedLeaseContracts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DetailedLeaseContracts for query {}", query);
        Page<DetailedLeaseContractDTO> page = detailedLeaseContractService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
