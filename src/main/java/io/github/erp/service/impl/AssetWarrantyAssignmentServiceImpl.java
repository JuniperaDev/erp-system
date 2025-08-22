package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.AssetWarrantyAssignment;
import io.github.erp.repository.AssetWarrantyAssignmentRepository;
import io.github.erp.repository.search.AssetWarrantyAssignmentSearchRepository;
import io.github.erp.service.AssetWarrantyAssignmentService;
import io.github.erp.service.dto.AssetWarrantyAssignmentDTO;
import io.github.erp.service.mapper.AssetWarrantyAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssetWarrantyAssignmentServiceImpl implements AssetWarrantyAssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssetWarrantyAssignmentServiceImpl.class);

    private final AssetWarrantyAssignmentRepository assetWarrantyAssignmentRepository;

    private final AssetWarrantyAssignmentMapper assetWarrantyAssignmentMapper;

    private final AssetWarrantyAssignmentSearchRepository assetWarrantyAssignmentSearchRepository;

    public AssetWarrantyAssignmentServiceImpl(
        AssetWarrantyAssignmentRepository assetWarrantyAssignmentRepository,
        AssetWarrantyAssignmentMapper assetWarrantyAssignmentMapper,
        @Autowired(required = false) AssetWarrantyAssignmentSearchRepository assetWarrantyAssignmentSearchRepository
    ) {
        this.assetWarrantyAssignmentRepository = assetWarrantyAssignmentRepository;
        this.assetWarrantyAssignmentMapper = assetWarrantyAssignmentMapper;
        this.assetWarrantyAssignmentSearchRepository = assetWarrantyAssignmentSearchRepository;
    }

    @Override
    public AssetWarrantyAssignmentDTO save(AssetWarrantyAssignmentDTO assetWarrantyAssignmentDTO) {
        log.debug("Request to save AssetWarrantyAssignment : {}", assetWarrantyAssignmentDTO);
        AssetWarrantyAssignment assetWarrantyAssignment = assetWarrantyAssignmentMapper.toEntity(assetWarrantyAssignmentDTO);
        assetWarrantyAssignment = assetWarrantyAssignmentRepository.save(assetWarrantyAssignment);
        AssetWarrantyAssignmentDTO result = assetWarrantyAssignmentMapper.toDto(assetWarrantyAssignment);
        if (assetWarrantyAssignmentSearchRepository != null) {
            assetWarrantyAssignmentSearchRepository.save(assetWarrantyAssignment);
        }
        return result;
    }

    @Override
    public AssetWarrantyAssignmentDTO update(AssetWarrantyAssignmentDTO assetWarrantyAssignmentDTO) {
        log.debug("Request to update AssetWarrantyAssignment : {}", assetWarrantyAssignmentDTO);
        AssetWarrantyAssignment assetWarrantyAssignment = assetWarrantyAssignmentMapper.toEntity(assetWarrantyAssignmentDTO);
        assetWarrantyAssignment = assetWarrantyAssignmentRepository.save(assetWarrantyAssignment);
        AssetWarrantyAssignmentDTO result = assetWarrantyAssignmentMapper.toDto(assetWarrantyAssignment);
        if (assetWarrantyAssignmentSearchRepository != null) {
            assetWarrantyAssignmentSearchRepository.save(assetWarrantyAssignment);
        }
        return result;
    }

    @Override
    public Optional<AssetWarrantyAssignmentDTO> partialUpdate(AssetWarrantyAssignmentDTO assetWarrantyAssignmentDTO) {
        log.debug("Request to partially update AssetWarrantyAssignment : {}", assetWarrantyAssignmentDTO);

        return assetWarrantyAssignmentRepository
            .findById(assetWarrantyAssignmentDTO.getId())
            .map(existingAssetWarrantyAssignment -> {
                assetWarrantyAssignmentMapper.partialUpdate(existingAssetWarrantyAssignment, assetWarrantyAssignmentDTO);

                return existingAssetWarrantyAssignment;
            })
            .map(assetWarrantyAssignmentRepository::save)
            .map(savedAssetWarrantyAssignment -> {
                if (assetWarrantyAssignmentSearchRepository != null) {
                    assetWarrantyAssignmentSearchRepository.save(savedAssetWarrantyAssignment);
                }

                return savedAssetWarrantyAssignment;
            })
            .map(assetWarrantyAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetWarrantyAssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetWarrantyAssignments");
        return assetWarrantyAssignmentRepository.findAll(pageable).map(assetWarrantyAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetWarrantyAssignmentDTO> findOne(Long id) {
        log.debug("Request to get AssetWarrantyAssignment : {}", id);
        return assetWarrantyAssignmentRepository.findById(id).map(assetWarrantyAssignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetWarrantyAssignment : {}", id);
        assetWarrantyAssignmentRepository.deleteById(id);
        if (assetWarrantyAssignmentSearchRepository != null) {
            assetWarrantyAssignmentSearchRepository.deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetWarrantyAssignmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetWarrantyAssignments for query {}", query);
        if (assetWarrantyAssignmentSearchRepository != null) {
            return assetWarrantyAssignmentSearchRepository.search(query, pageable).map(assetWarrantyAssignmentMapper::toDto);
        }
        return assetWarrantyAssignmentRepository.findAll(pageable).map(assetWarrantyAssignmentMapper::toDto);
    }
}
