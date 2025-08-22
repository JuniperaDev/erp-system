package io.github.erp.financial.service.mapper;

import io.github.erp.financial.domain.JobSheet;
import io.github.erp.financial.service.dto.JobSheetDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link JobSheet} and its DTO {@link JobSheetDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JobSheetMapper extends EntityMapper<JobSheetDTO, JobSheet> {
    
    @Mapping(target = "id", source = "id")
    @Mapping(target = "serialNumber", source = "serialNumber")
    @Mapping(target = "jobSheetDate", source = "jobSheetDate")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "remarks", source = "remarks")
    JobSheetDTO toDto(JobSheet jobSheet);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "serialNumber", source = "serialNumber")
    @Mapping(target = "jobSheetDate", source = "jobSheetDate")
    @Mapping(target = "details", source = "details")
    @Mapping(target = "remarks", source = "remarks")
    JobSheet toEntity(JobSheetDTO jobSheetDTO);
}
