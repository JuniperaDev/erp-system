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

import io.github.erp.repository.DealerGroupRepository;
import io.github.erp.service.DealerGroupQueryService;
import io.github.erp.service.DealerGroupService;
import io.github.erp.service.criteria.DealerGroupCriteria;
import io.github.erp.service.dto.DealerGroupDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DealerGroup}.
 */
@RestController
@RequestMapping("/api")
public class DealerGroupResource {

    private final Logger log = LoggerFactory.getLogger(DealerGroupResource.class);

    private static final String ENTITY_NAME = "dealersDealerGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DealerGroupService dealerGroupService;

    private final DealerGroupRepository dealerGroupRepository;

    private final DealerGroupQueryService dealerGroupQueryService;

    public DealerGroupResource(
        DealerGroupService dealerGroupService,
        DealerGroupRepository dealerGroupRepository,
        DealerGroupQueryService dealerGroupQueryService
    ) {
        this.dealerGroupService = dealerGroupService;
        this.dealerGroupRepository = dealerGroupRepository;
        this.dealerGroupQueryService = dealerGroupQueryService;
    }

    /**
     * {@code POST  /dealer-groups} : Create a new dealerGroup.
     *
     * @param dealerGroupDTO the dealerGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dealerGroupDTO, or with status {@code 400 (Bad Request)} if the dealerGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dealer-groups")
    public ResponseEntity<DealerGroupDTO> createDealerGroup(@Valid @RequestBody DealerGroupDTO dealerGroupDTO) throws URISyntaxException {
        log.debug("REST request to save DealerGroup : {}", dealerGroupDTO);
        if (dealerGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new dealerGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DealerGroupDTO result = dealerGroupService.save(dealerGroupDTO);
        return ResponseEntity
            .created(new URI("/api/dealer-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dealer-groups/:id} : Updates an existing dealerGroup.
     *
     * @param id the id of the dealerGroupDTO to save.
     * @param dealerGroupDTO the dealerGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealerGroupDTO,
     * or with status {@code 400 (Bad Request)} if the dealerGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dealerGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dealer-groups/{id}")
    public ResponseEntity<DealerGroupDTO> updateDealerGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DealerGroupDTO dealerGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DealerGroup : {}, {}", id, dealerGroupDTO);
        if (dealerGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealerGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealerGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DealerGroupDTO result = dealerGroupService.save(dealerGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dealerGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dealer-groups/:id} : Partial updates given fields of an existing dealerGroup, field will ignore if it is null
     *
     * @param id the id of the dealerGroupDTO to save.
     * @param dealerGroupDTO the dealerGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dealerGroupDTO,
     * or with status {@code 400 (Bad Request)} if the dealerGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dealerGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dealerGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dealer-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DealerGroupDTO> partialUpdateDealerGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DealerGroupDTO dealerGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DealerGroup partially : {}, {}", id, dealerGroupDTO);
        if (dealerGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dealerGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dealerGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DealerGroupDTO> result = dealerGroupService.partialUpdate(dealerGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dealerGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dealer-groups} : get all the dealerGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dealerGroups in body.
     */
    @GetMapping("/dealer-groups")
    public ResponseEntity<List<DealerGroupDTO>> getAllDealerGroups(DealerGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DealerGroups by criteria: {}", criteria);
        Page<DealerGroupDTO> page = dealerGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dealer-groups/count} : count all the dealerGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dealer-groups/count")
    public ResponseEntity<Long> countDealerGroups(DealerGroupCriteria criteria) {
        log.debug("REST request to count DealerGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(dealerGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dealer-groups/:id} : get the "id" dealerGroup.
     *
     * @param id the id of the dealerGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dealerGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dealer-groups/{id}")
    public ResponseEntity<DealerGroupDTO> getDealerGroup(@PathVariable Long id) {
        log.debug("REST request to get DealerGroup : {}", id);
        Optional<DealerGroupDTO> dealerGroupDTO = dealerGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dealerGroupDTO);
    }

    /**
     * {@code DELETE  /dealer-groups/:id} : delete the "id" dealerGroup.
     *
     * @param id the id of the dealerGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dealer-groups/{id}")
    public ResponseEntity<Void> deleteDealerGroup(@PathVariable Long id) {
        log.debug("REST request to delete DealerGroup : {}", id);
        dealerGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/dealer-groups?query=:query} : search for the dealerGroup corresponding
     * to the query.
     *
     * @param query the query of the dealerGroup search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/dealer-groups")
    public ResponseEntity<List<DealerGroupDTO>> searchDealerGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DealerGroups for query {}", query);
        Page<DealerGroupDTO> page = dealerGroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
