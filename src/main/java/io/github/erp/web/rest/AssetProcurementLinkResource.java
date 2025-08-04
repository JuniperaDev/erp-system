package io.github.erp.web.rest;

import io.github.erp.repository.AssetProcurementLinkRepository;
import io.github.erp.service.AssetProcurementLinkService;
import io.github.erp.service.dto.AssetProcurementLinkDTO;
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

@RestController
@RequestMapping("/api")
public class AssetProcurementLinkResource {

    private final Logger log = LoggerFactory.getLogger(AssetProcurementLinkResource.class);

    private static final String ENTITY_NAME = "assetProcurementLink";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetProcurementLinkService assetProcurementLinkService;

    private final AssetProcurementLinkRepository assetProcurementLinkRepository;

    public AssetProcurementLinkResource(
        AssetProcurementLinkService assetProcurementLinkService,
        AssetProcurementLinkRepository assetProcurementLinkRepository
    ) {
        this.assetProcurementLinkService = assetProcurementLinkService;
        this.assetProcurementLinkRepository = assetProcurementLinkRepository;
    }

    @PostMapping("/asset-procurement-links")
    public ResponseEntity<AssetProcurementLinkDTO> createAssetProcurementLink(@Valid @RequestBody AssetProcurementLinkDTO assetProcurementLinkDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetProcurementLink : {}", assetProcurementLinkDTO);
        if (assetProcurementLinkDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetProcurementLink cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetProcurementLinkDTO result = assetProcurementLinkService.save(assetProcurementLinkDTO);
        return ResponseEntity
            .created(new URI("/api/asset-procurement-links/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/asset-procurement-links/{id}")
    public ResponseEntity<AssetProcurementLinkDTO> updateAssetProcurementLink(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetProcurementLinkDTO assetProcurementLinkDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetProcurementLink : {}, {}", id, assetProcurementLinkDTO);
        if (assetProcurementLinkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetProcurementLinkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetProcurementLinkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetProcurementLinkDTO result = assetProcurementLinkService.save(assetProcurementLinkDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetProcurementLinkDTO.getId().toString()))
            .body(result);
    }

    @GetMapping("/asset-procurement-links")
    public ResponseEntity<List<AssetProcurementLinkDTO>> getAllAssetProcurementLinks(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of AssetProcurementLinks");
        Page<AssetProcurementLinkDTO> page = assetProcurementLinkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/asset-procurement-links/{id}")
    public ResponseEntity<AssetProcurementLinkDTO> getAssetProcurementLink(@PathVariable Long id) {
        log.debug("REST request to get AssetProcurementLink : {}", id);
        Optional<AssetProcurementLinkDTO> assetProcurementLinkDTO = assetProcurementLinkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetProcurementLinkDTO);
    }

    @DeleteMapping("/asset-procurement-links/{id}")
    public ResponseEntity<Void> deleteAssetProcurementLink(@PathVariable Long id) {
        log.debug("REST request to delete AssetProcurementLink : {}", id);
        assetProcurementLinkService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/asset-procurement-links/by-asset/{assetId}")
    public ResponseEntity<List<AssetProcurementLinkDTO>> getAssetProcurementLinksByAsset(@PathVariable Long assetId) {
        log.debug("REST request to get AssetProcurementLinks by asset : {}", assetId);
        List<AssetProcurementLinkDTO> links = assetProcurementLinkService.findByAssetId(assetId);
        return ResponseEntity.ok().body(links);
    }
}
