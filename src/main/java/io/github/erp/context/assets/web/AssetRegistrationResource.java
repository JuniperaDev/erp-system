package io.github.erp.context.assets.web;

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

import io.github.erp.context.assets.service.InternalAssetRegistrationServiceImpl;
import io.github.erp.service.dto.AssetRegistrationDTO;
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

@RestController("contextAssetRegistrationResource")
@RequestMapping("/api/v2/asset-management")
public class AssetRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(AssetRegistrationResource.class);

    private static final String ENTITY_NAME = "assetRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalAssetRegistrationServiceImpl assetRegistrationService;

    public AssetRegistrationResource(InternalAssetRegistrationServiceImpl assetRegistrationService) {
        this.assetRegistrationService = assetRegistrationService;
    }

    @PostMapping("/asset-registrations")
    public ResponseEntity<AssetRegistrationDTO> createAssetRegistration(@Valid @RequestBody AssetRegistrationDTO assetRegistrationDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetRegistration : {}", assetRegistrationDTO);
        if (assetRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetRegistrationDTO result = assetRegistrationService.save(assetRegistrationDTO);
        return ResponseEntity
            .created(new URI("/api/v2/asset-management/asset-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/asset-registrations/{id}")
    public ResponseEntity<AssetRegistrationDTO> updateAssetRegistration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetRegistrationDTO assetRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetRegistration : {}, {}", id, assetRegistrationDTO);
        if (assetRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        AssetRegistrationDTO result = assetRegistrationService.save(assetRegistrationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetRegistrationDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/asset-registrations")
    public ResponseEntity<List<AssetRegistrationDTO>> getAllAssetRegistrations(
        Pageable pageable
    ) {
        log.debug("REST request to get a page of AssetRegistrations");
        Page<AssetRegistrationDTO> page = assetRegistrationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-registrations/{id}")
    public ResponseEntity<AssetRegistrationDTO> getAssetRegistration(@PathVariable Long id) {
        log.debug("REST request to get AssetRegistration : {}", id);
        Optional<AssetRegistrationDTO> assetRegistrationDTO = assetRegistrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetRegistrationDTO);
    }

    @DeleteMapping("/asset-registrations/{id}")
    public ResponseEntity<Void> deleteAssetRegistration(@PathVariable Long id) {
        log.debug("REST request to delete AssetRegistration : {}", id);
        assetRegistrationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/asset-registrations/_search")
    public ResponseEntity<List<AssetRegistrationDTO>> searchAssetRegistrations(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of AssetRegistrations for query {}", query);
        Page<AssetRegistrationDTO> page = assetRegistrationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
