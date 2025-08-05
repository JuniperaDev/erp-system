package io.github.erp.erp.resources.leases;

import io.github.erp.internal.service.leases.InternalDetailedLeaseContractService;
import io.github.erp.repository.DetailedLeaseContractRepository;
import io.github.erp.service.DetailedLeaseContractQueryService;
import io.github.erp.service.criteria.DetailedLeaseContractCriteria;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * REST controller for managing DetailedLeaseContract via legacy IFRS16LeaseContract endpoints.
 * Maintains backward compatibility for existing API consumers.
 */
@RestController
@RequestMapping("/api/leases")
@Transactional
public class IFRS16LeaseContractResourceProdV1 {

    private final Logger log = LoggerFactory.getLogger(IFRS16LeaseContractResourceProdV1.class);

    private static final String ENTITY_NAME = "ifrs16LeaseContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalDetailedLeaseContractService internalDetailedLeaseContractService;

    private final DetailedLeaseContractRepository detailedLeaseContractRepository;

    private final DetailedLeaseContractQueryService detailedLeaseContractQueryService;

    public IFRS16LeaseContractResourceProdV1(
        InternalDetailedLeaseContractService internalDetailedLeaseContractService,
        DetailedLeaseContractRepository detailedLeaseContractRepository,
        DetailedLeaseContractQueryService detailedLeaseContractQueryService
    ) {
        this.internalDetailedLeaseContractService = internalDetailedLeaseContractService;
        this.detailedLeaseContractRepository = detailedLeaseContractRepository;
        this.detailedLeaseContractQueryService = detailedLeaseContractQueryService;
    }

    @PostMapping("/ifrs-16-lease-contracts")
    public ResponseEntity<DetailedLeaseContractDTO> createIFRS16LeaseContract(@Valid @RequestBody DetailedLeaseContractDTO detailedLeaseContractDTO) throws URISyntaxException {
        log.debug("REST request to save IFRS16LeaseContract : {}", detailedLeaseContractDTO);
        if (detailedLeaseContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new ifrs16LeaseContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailedLeaseContractDTO result = internalDetailedLeaseContractService.save(detailedLeaseContractDTO);
        return ResponseEntity
            .created(new URI("/api/leases/ifrs-16-lease-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PutMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<DetailedLeaseContractDTO> updateIFRS16LeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DetailedLeaseContractDTO detailedLeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IFRS16LeaseContract : {}, {}", id, detailedLeaseContractDTO);
        if (detailedLeaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailedLeaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailedLeaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailedLeaseContractDTO result = internalDetailedLeaseContractService.save(detailedLeaseContractDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detailedLeaseContractDTO.getId().toString()))
            .body(result);
    }

    @PatchMapping(value = "/ifrs-16-lease-contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailedLeaseContractDTO> partialUpdateIFRS16LeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DetailedLeaseContractDTO detailedLeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IFRS16LeaseContract partially : {}, {}", id, detailedLeaseContractDTO);
        if (detailedLeaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailedLeaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailedLeaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailedLeaseContractDTO> result = internalDetailedLeaseContractService.partialUpdate(detailedLeaseContractDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detailedLeaseContractDTO.getId().toString())
        );
    }

    @GetMapping("/ifrs-16-lease-contracts")
    public ResponseEntity<List<DetailedLeaseContractDTO>> getAllIFRS16LeaseContracts(
        DetailedLeaseContractCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get IFRS16LeaseContracts by criteria: {}", criteria);
        Page<DetailedLeaseContractDTO> page = detailedLeaseContractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/ifrs-16-lease-contracts/count")
    public ResponseEntity<Long> countIFRS16LeaseContracts(DetailedLeaseContractCriteria criteria) {
        log.debug("REST request to count IFRS16LeaseContracts by criteria: {}", criteria);
        return ResponseEntity.ok().body(detailedLeaseContractQueryService.countByCriteria(criteria));
    }

    @GetMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<DetailedLeaseContractDTO> getIFRS16LeaseContract(@PathVariable Long id) {
        log.debug("REST request to get IFRS16LeaseContract : {}", id);
        Optional<DetailedLeaseContractDTO> detailedLeaseContractDTO = internalDetailedLeaseContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailedLeaseContractDTO);
    }

    @DeleteMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<Void> deleteIFRS16LeaseContract(@PathVariable Long id) {
        log.debug("REST request to delete IFRS16LeaseContract : {}", id);
        internalDetailedLeaseContractService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/_search/ifrs-16-lease-contracts")
    public ResponseEntity<List<DetailedLeaseContractDTO>> searchIFRS16LeaseContracts(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of IFRS16LeaseContracts for query {}", query);
        Page<DetailedLeaseContractDTO> page = internalDetailedLeaseContractService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
