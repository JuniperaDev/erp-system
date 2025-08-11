package io.github.erp.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.AssetJobSheetAssignment;
import io.github.erp.repository.AssetJobSheetAssignmentRepository;
import io.github.erp.repository.search.AssetJobSheetAssignmentSearchRepository;
import io.github.erp.service.AssetJobSheetAssignmentService;
import io.github.erp.service.dto.AssetJobSheetAssignmentDTO;
import io.github.erp.service.mapper.AssetJobSheetAssignmentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssetJobSheetAssignmentServiceImpl implements AssetJobSheetAssignmentService {

    private final Logger log = LoggerFactory.getLogger(AssetJobSheetAssignmentServiceImpl.class);

    private final AssetJobSheetAssignmentRepository assetJobSheetAssignmentRepository;

    private final AssetJobSheetAssignmentMapper assetJobSheetAssignmentMapper;

    private final AssetJobSheetAssignmentSearchRepository assetJobSheetAssignmentSearchRepository;

    public AssetJobSheetAssignmentServiceImpl(
        AssetJobSheetAssignmentRepository assetJobSheetAssignmentRepository,
        AssetJobSheetAssignmentMapper assetJobSheetAssignmentMapper,
        AssetJobSheetAssignmentSearchRepository assetJobSheetAssignmentSearchRepository
    ) {
        this.assetJobSheetAssignmentRepository = assetJobSheetAssignmentRepository;
        this.assetJobSheetAssignmentMapper = assetJobSheetAssignmentMapper;
        this.assetJobSheetAssignmentSearchRepository = assetJobSheetAssignmentSearchRepository;
    }

    @Override
    public AssetJobSheetAssignmentDTO save(AssetJobSheetAssignmentDTO assetJobSheetAssignmentDTO) {
        log.debug("Request to save AssetJobSheetAssignment : {}", assetJobSheetAssignmentDTO);
        AssetJobSheetAssignment assetJobSheetAssignment = assetJobSheetAssignmentMapper.toEntity(assetJobSheetAssignmentDTO);
        assetJobSheetAssignment = assetJobSheetAssignmentRepository.save(assetJobSheetAssignment);
        AssetJobSheetAssignmentDTO result = assetJobSheetAssignmentMapper.toDto(assetJobSheetAssignment);
        assetJobSheetAssignmentSearchRepository.save(assetJobSheetAssignment);
        return result;
    }

    @Override
    public AssetJobSheetAssignmentDTO update(AssetJobSheetAssignmentDTO assetJobSheetAssignmentDTO) {
        log.debug("Request to update AssetJobSheetAssignment : {}", assetJobSheetAssignmentDTO);
        AssetJobSheetAssignment assetJobSheetAssignment = assetJobSheetAssignmentMapper.toEntity(assetJobSheetAssignmentDTO);
        assetJobSheetAssignment = assetJobSheetAssignmentRepository.save(assetJobSheetAssignment);
        AssetJobSheetAssignmentDTO result = assetJobSheetAssignmentMapper.toDto(assetJobSheetAssignment);
        assetJobSheetAssignmentSearchRepository.save(assetJobSheetAssignment);
        return result;
    }

    @Override
    public Optional<AssetJobSheetAssignmentDTO> partialUpdate(AssetJobSheetAssignmentDTO assetJobSheetAssignmentDTO) {
        log.debug("Request to partially update AssetJobSheetAssignment : {}", assetJobSheetAssignmentDTO);

        return assetJobSheetAssignmentRepository
            .findById(assetJobSheetAssignmentDTO.getId())
            .map(existingAssetJobSheetAssignment -> {
                assetJobSheetAssignmentMapper.partialUpdate(existingAssetJobSheetAssignment, assetJobSheetAssignmentDTO);

                return existingAssetJobSheetAssignment;
            })
            .map(assetJobSheetAssignmentRepository::save)
            .map(savedAssetJobSheetAssignment -> {
                assetJobSheetAssignmentSearchRepository.save(savedAssetJobSheetAssignment);

                return savedAssetJobSheetAssignment;
            })
            .map(assetJobSheetAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetJobSheetAssignmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AssetJobSheetAssignments");
        return assetJobSheetAssignmentRepository.findAll(pageable).map(assetJobSheetAssignmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AssetJobSheetAssignmentDTO> findOne(Long id) {
        log.debug("Request to get AssetJobSheetAssignment : {}", id);
        return assetJobSheetAssignmentRepository.findById(id).map(assetJobSheetAssignmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AssetJobSheetAssignment : {}", id);
        assetJobSheetAssignmentRepository.deleteById(id);
        assetJobSheetAssignmentSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AssetJobSheetAssignmentDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of AssetJobSheetAssignments for query {}", query);
        return assetJobSheetAssignmentSearchRepository.search(query, pageable).map(assetJobSheetAssignmentMapper::toDto);
    }
}
