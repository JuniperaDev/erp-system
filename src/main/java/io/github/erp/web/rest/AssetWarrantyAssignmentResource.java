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

import io.github.erp.repository.AssetWarrantyAssignmentRepository;
import io.github.erp.service.AssetWarrantyAssignmentService;
import io.github.erp.service.dto.AssetWarrantyAssignmentDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.AssetWarrantyAssignment}.
 */
@RestController
@RequestMapping("/api")
public class AssetWarrantyAssignmentResource {

    private final Logger log = LoggerFactory.getLogger(AssetWarrantyAssignmentResource.class);

    private static final String ENTITY_NAME = "assetWarrantyAssignment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetWarrantyAssignmentService assetWarrantyAssignmentService;

    private final AssetWarrantyAssignmentRepository assetWarrantyAssignmentRepository;

    public AssetWarrantyAssignmentResource(
        AssetWarrantyAssignmentService assetWarrantyAssignmentService,
        AssetWarrantyAssignmentRepository assetWarrantyAssignmentRepository
    ) {
        this.assetWarrantyAssignmentService = assetWarrantyAssignmentService;
        this.assetWarrantyAssignmentRepository = assetWarrantyAssignmentRepository;
    }

    /**
     * {@code POST  /asset-warranty-assignments} : Create a new assetWarrantyAssignment.
     *
     * @param assetWarrantyAssignmentDTO the assetWarrantyAssignmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetWarrantyAssignmentDTO, or with status {@code 400 (Bad Request)} if the assetWarrantyAssignment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-warranty-assignments")
    public ResponseEntity<AssetWarrantyAssignmentDTO> createAssetWarrantyAssignment(
        @Valid @RequestBody AssetWarrantyAssignmentDTO assetWarrantyAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AssetWarrantyAssignment : {}", assetWarrantyAssignmentDTO);
        if (assetWarrantyAssignmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetWarrantyAssignment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetWarrantyAssignmentDTO result = assetWarrantyAssignmentService.save(assetWarrantyAssignmentDTO);
        return ResponseEntity
            .created(new URI("/api/asset-warranty-assignments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-warranty-assignments/:id} : Updates an existing assetWarrantyAssignment.
     *
     * @param id the id of the assetWarrantyAssignmentDTO to save.
     * @param assetWarrantyAssignmentDTO the assetWarrantyAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetWarrantyAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the assetWarrantyAssignmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetWarrantyAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-warranty-assignments/{id}")
    public ResponseEntity<AssetWarrantyAssignmentDTO> updateAssetWarrantyAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetWarrantyAssignmentDTO assetWarrantyAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetWarrantyAssignment : {}, {}", id, assetWarrantyAssignmentDTO);
        if (assetWarrantyAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetWarrantyAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetWarrantyAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetWarrantyAssignmentDTO result = assetWarrantyAssignmentService.update(assetWarrantyAssignmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetWarrantyAssignmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-warranty-assignments/:id} : Partial updates given fields of an existing assetWarrantyAssignment, field will ignore if it is null
     *
     * @param id the id of the assetWarrantyAssignmentDTO to save.
     * @param assetWarrantyAssignmentDTO the assetWarrantyAssignmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetWarrantyAssignmentDTO,
     * or with status {@code 400 (Bad Request)} if the assetWarrantyAssignmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetWarrantyAssignmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetWarrantyAssignmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-warranty-assignments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetWarrantyAssignmentDTO> partialUpdateAssetWarrantyAssignment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetWarrantyAssignmentDTO assetWarrantyAssignmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetWarrantyAssignment partially : {}, {}", id, assetWarrantyAssignmentDTO);
        if (assetWarrantyAssignmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetWarrantyAssignmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetWarrantyAssignmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetWarrantyAssignmentDTO> result = assetWarrantyAssignmentService.partialUpdate(assetWarrantyAssignmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetWarrantyAssignmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-warranty-assignments} : get all the assetWarrantyAssignments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetWarrantyAssignments in body.
     */
    @GetMapping("/asset-warranty-assignments")
    public ResponseEntity<List<AssetWarrantyAssignmentDTO>> getAllAssetWarrantyAssignments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AssetWarrantyAssignments");
        Page<AssetWarrantyAssignmentDTO> page = assetWarrantyAssignmentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-warranty-assignments/:id} : get the "id" assetWarrantyAssignment.
     *
     * @param id the id of the assetWarrantyAssignmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetWarrantyAssignmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-warranty-assignments/{id}")
    public ResponseEntity<AssetWarrantyAssignmentDTO> getAssetWarrantyAssignment(@PathVariable Long id) {
        log.debug("REST request to get AssetWarrantyAssignment : {}", id);
        Optional<AssetWarrantyAssignmentDTO> assetWarrantyAssignmentDTO = assetWarrantyAssignmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetWarrantyAssignmentDTO);
    }

    /**
     * {@code DELETE  /asset-warranty-assignments/:id} : delete the "id" assetWarrantyAssignment.
     *
     * @param id the id of the assetWarrantyAssignmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @DeleteMapping("/asset-warranty-assignments/{id}")
    public ResponseEntity<Void> deleteAssetWarrantyAssignment(@PathVariable Long id) {
        log.debug("REST request to delete AssetWarrantyAssignment : {}", id);
        assetWarrantyAssignmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-warranty-assignments?query=:query} : search for the assetWarrantyAssignment corresponding
     * to the query.
     *
     * @param query the query of the assetWarrantyAssignment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-warranty-assignments")
    public ResponseEntity<List<AssetWarrantyAssignmentDTO>> searchAssetWarrantyAssignments(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of AssetWarrantyAssignments for query {}", query);
        Page<AssetWarrantyAssignmentDTO> page = assetWarrantyAssignmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
