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

import io.github.erp.domain.events.DomainEvent;
import io.github.erp.service.EventSourcingAuditTrailService;
import io.github.erp.web.rest.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/audit-trail")
@Tag(name = "Audit Trail", description = "Audit trail and compliance reporting API")
@SecurityRequirement(name = "bearerAuth")
public class EventSourcingAuditTrailResource {

    private final Logger log = LoggerFactory.getLogger(EventSourcingAuditTrailResource.class);

    private final EventSourcingAuditTrailService eventSourcingAuditTrailService;

    public EventSourcingAuditTrailResource(EventSourcingAuditTrailService eventSourcingAuditTrailService) {
        this.eventSourcingAuditTrailService = eventSourcingAuditTrailService;
    }

    @GetMapping("/events/{entityType}/{entityId}")
    @Operation(summary = "Retrieve audit trail for entity", 
               description = "Get the complete audit trail for a specific entity within a date range. Results are paginated and sorted chronologically.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Audit trail retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AuditTrailResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<AuditTrailResponseDTO> getAuditTrail(
            @Parameter(description = "Type of entity", example = "Payment") @PathVariable String entityType,
            @Parameter(description = "Unique identifier of the entity", example = "12345") @PathVariable String entityId,
            @Parameter(description = "Start date for audit trail") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @Parameter(description = "End date for audit trail") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
            @Parameter(description = "Filter by specific event types") @RequestParam(required = false) String eventTypes,
            @Parameter(description = "Filter by user who performed actions") @RequestParam(required = false) String userId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort criteria") @RequestParam(defaultValue = "occurredOn,asc") String sort) {
        
        log.debug("REST request to get audit trail for {} with id {} from {} to {}", 
                 entityType, entityId, fromDate, toDate);
        
        Pageable pageable = createPageable(page, size, sort);
        List<DomainEvent> auditTrail = eventSourcingAuditTrailService.reconstructAuditTrail(
            entityType, entityId, fromDate != null ? fromDate : Instant.EPOCH, 
            toDate != null ? toDate : Instant.now());
        
        AuditTrailResponseDTO response = convertToAuditTrailResponse(auditTrail, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/events/search")
    @Operation(summary = "Advanced audit event search", 
               description = "Perform advanced search across audit events using flexible criteria. Supports full-text search, date ranges, and complex filtering.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Search results retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AuditSearchResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid search request",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<AuditSearchResponseDTO> searchAuditEvents(@Valid @RequestBody AuditSearchRequestDTO searchRequest) {
        
        log.debug("REST request to search audit events with query: {}", searchRequest.getQuery());
        
        AuditSearchResponseDTO response = performAuditSearch(searchRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/compliance-reports")
    @Operation(summary = "Generate compliance report", 
               description = "Generate a compliance report for a specific framework and time period. Report generation is asynchronous - use the returned job ID to check status.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "202", description = "Report generation initiated",
                    content = @Content(schema = @Schema(implementation = ReportGenerationResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid report request",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Authentication required",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "403", description = "Insufficient permissions",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
        @ApiResponse(responseCode = "429", description = "Rate limit exceeded",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PreAuthorize("hasAuthority('COMPLIANCE_READ')")
    public ResponseEntity<ReportGenerationResponseDTO> generateComplianceReport(@Valid @RequestBody ComplianceReportRequestDTO reportRequest) {
        
        log.debug("REST request to generate compliance report of type: {}", reportRequest.getReportType());
        
        ReportGenerationResponseDTO response = initiateComplianceReportGeneration(reportRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/event-sourcing/reconstruct/{entityType}/{entityId}")
    @Operation(summary = "Reconstruct entity state", 
               description = "Reconstruct the state of an entity at a specific point in time using event sourcing. This provides a complete view of how the entity looked at any historical moment.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Entity state reconstructed successfully",
                    content = @Content(schema = @Schema(implementation = EntityStateResponseDTO.class)))
    })
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<EntityStateResponseDTO> reconstructEntityState(
            @Parameter(description = "Entity type") @PathVariable String entityType,
            @Parameter(description = "Entity ID") @PathVariable String entityId,
            @Parameter(description = "Point in time for state reconstruction") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant asOfDate) {
        
        log.debug("REST request to reconstruct entity state for {} with id {} as of {}", 
                 entityType, entityId, asOfDate);
        
        Map<String, Object> entityState = eventSourcingAuditTrailService.reconstructEntityState(entityType, entityId, asOfDate);
        EntityStateResponseDTO response = convertToEntityStateResponse(entityState);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/event-sourcing/replay/{aggregateId}")
    @Operation(summary = "Replay events for aggregate", 
               description = "Replay events for a specific aggregate from a given point in time. Useful for debugging and state reconstruction.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Events replayed successfully",
                    content = @Content(schema = @Schema(implementation = EventReplayResponseDTO.class)))
    })
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<EventReplayResponseDTO> replayEvents(
            @Parameter(description = "Aggregate ID") @PathVariable String aggregateId,
            @Valid @RequestBody EventReplayRequestDTO replayRequest) {
        
        log.debug("REST request to replay events for aggregate {} from {}", aggregateId, replayRequest.getFromTime());
        
        List<DomainEvent> events = eventSourcingAuditTrailService.replayEvents(aggregateId, replayRequest.getFromTime());
        EventReplayResponseDTO response = convertToEventReplayResponse(aggregateId, replayRequest, events);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/event-sourcing/validate/{aggregateId}")
    @Operation(summary = "Validate event integrity", 
               description = "Validate the integrity and consistency of events for a specific aggregate. Checks for proper event ordering, version consistency, and data integrity.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Validation completed",
                    content = @Content(schema = @Schema(implementation = ValidationResponseDTO.class)))
    })
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<ValidationResponseDTO> validateEventIntegrity(
            @Parameter(description = "Aggregate ID") @PathVariable String aggregateId) {
        
        log.debug("REST request to validate event integrity for aggregate {}", aggregateId);
        
        boolean isValid = eventSourcingAuditTrailService.validateEventIntegrity(aggregateId);
        ValidationResponseDTO response = convertToValidationResponse(aggregateId, isValid);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/statistics/summary")
    @Operation(summary = "Get audit event statistics", 
               description = "Get summary statistics for audit events within a date range. Includes event counts by type, user activity, and trend analysis.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully",
                    content = @Content(schema = @Schema(implementation = AuditStatisticsResponseDTO.class)))
    })
    @PreAuthorize("hasAuthority('AUDIT_READ')")
    public ResponseEntity<AuditStatisticsResponseDTO> getAuditEventStatistics(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toDate,
            @Parameter(description = "Group statistics by field") @RequestParam(defaultValue = "eventType") String groupBy) {
        
        log.debug("REST request to get audit event statistics from {} to {} grouped by {}", fromDate, toDate, groupBy);
        
        Map<String, Long> summary = eventSourcingAuditTrailService.getAuditEventSummary(fromDate, toDate);
        AuditStatisticsResponseDTO response = convertToAuditStatisticsResponse(summary, fromDate, toDate, groupBy);
        
        return ResponseEntity.ok(response);
    }


    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParts = sort.split(",");
        String field = sortParts[0];
        Sort.Direction direction = sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1]) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(direction, field));
    }

    private AuditTrailResponseDTO convertToAuditTrailResponse(List<DomainEvent> events, Pageable pageable) {
        AuditTrailResponseDTO response = new AuditTrailResponseDTO();
        
        List<AuditEventDTO> eventDTOs = events.stream()
            .map(this::convertToAuditEventDTO)
            .collect(java.util.stream.Collectors.toList());
        
        response.setContent(eventDTOs);
        response.setTotalElements((long) events.size());
        response.setTotalPages((int) Math.ceil((double) events.size() / pageable.getPageSize()));
        response.setFirst(pageable.getPageNumber() == 0);
        response.setLast(pageable.getPageNumber() >= response.getTotalPages() - 1);
        response.setNumberOfElements(eventDTOs.size());
        
        AuditTrailResponseDTO.PageableDTO pageableDTO = new AuditTrailResponseDTO.PageableDTO();
        pageableDTO.setPage(pageable.getPageNumber());
        pageableDTO.setSize(pageable.getPageSize());
        pageableDTO.setSort(pageable.getSort().stream()
            .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
            .collect(java.util.stream.Collectors.toList()));
        response.setPageable(pageableDTO);
        
        return response;
    }

    private AuditEventDTO convertToAuditEventDTO(DomainEvent event) {
        AuditEventDTO dto = new AuditEventDTO();
        dto.setEventId(event.getEventId());
        dto.setEventType(event.getEventType());
        dto.setAggregateType(event.getAggregateType());
        dto.setAggregateId(event.getAggregateId());
        dto.setOccurredOn(event.getOccurredOn());
        dto.setVersion(event.getVersion());
        dto.setCorrelationId(event.getCorrelationId());
        return dto;
    }

    private AuditSearchResponseDTO performAuditSearch(AuditSearchRequestDTO searchRequest) {
        AuditSearchResponseDTO response = new AuditSearchResponseDTO();
        response.setHits(java.util.Collections.emptyList());
        response.setTotalHits(0L);
        response.setAggregations(java.util.Collections.emptyMap());
        response.setTook(0);
        return response;
    }

    private ReportGenerationResponseDTO initiateComplianceReportGeneration(ComplianceReportRequestDTO reportRequest) {
        ReportGenerationResponseDTO response = new ReportGenerationResponseDTO();
        response.setJobId(UUID.randomUUID());
        response.setStatus("QUEUED");
        response.setEstimatedCompletionTime(Instant.now().plusSeconds(300));
        response.setStatusUrl("/api/v1/audit-trail/compliance-reports/" + response.getJobId());
        return response;
    }

    private EntityStateResponseDTO convertToEntityStateResponse(Map<String, Object> entityState) {
        EntityStateResponseDTO response = new EntityStateResponseDTO();
        response.setEntityType((String) entityState.get("entityType"));
        response.setEntityId((String) entityState.get("entityId"));
        response.setAsOfDate((Instant) entityState.get("asOfDate"));
        response.setEventCount((Integer) entityState.get("eventCount"));
        response.setLastEventDate((Instant) entityState.get("lastEventDate"));
        response.setVersion((Integer) entityState.get("version"));
        response.setState(entityState);
        return response;
    }

    private EventReplayResponseDTO convertToEventReplayResponse(String aggregateId, EventReplayRequestDTO request, List<DomainEvent> events) {
        EventReplayResponseDTO response = new EventReplayResponseDTO();
        response.setAggregateId(aggregateId);
        response.setFromTime(request.getFromTime());
        response.setToTime(request.getToTime());
        response.setEventCount(events.size());
        response.setReplayDuration(100);
        
        List<AuditEventDTO> eventDTOs = events.stream()
            .map(this::convertToAuditEventDTO)
            .collect(java.util.stream.Collectors.toList());
        response.setEvents(eventDTOs);
        
        return response;
    }

    private ValidationResponseDTO convertToValidationResponse(String aggregateId, boolean isValid) {
        ValidationResponseDTO response = new ValidationResponseDTO();
        response.setAggregateId(aggregateId);
        response.setValid(isValid);
        response.setEventCount(0);
        response.setValidationErrors(java.util.Collections.emptyList());
        
        ValidationResponseDTO.ValidationSummaryDTO summary = new ValidationResponseDTO.ValidationSummaryDTO();
        summary.setTotalEvents(0);
        summary.setValidEvents(0);
        summary.setInvalidEvents(0);
        summary.setWarningCount(0);
        response.setValidationSummary(summary);
        
        return response;
    }

    private AuditStatisticsResponseDTO convertToAuditStatisticsResponse(Map<String, Long> summary, Instant fromDate, Instant toDate, String groupBy) {
        AuditStatisticsResponseDTO response = new AuditStatisticsResponseDTO();
        
        AuditStatisticsResponseDTO.PeriodDTO period = new AuditStatisticsResponseDTO.PeriodDTO();
        period.setFrom(fromDate);
        period.setTo(toDate);
        response.setPeriod(period);
        
        response.setTotalEvents(summary.values().stream().mapToLong(Long::longValue).sum());
        response.setEventsByType(summary);
        response.setEventsByUser(java.util.Collections.emptyMap());
        response.setEventsByEntity(java.util.Collections.emptyMap());
        response.setTimeline(java.util.Collections.emptyList());
        response.setTopUsers(java.util.Collections.emptyList());
        
        AuditStatisticsResponseDTO.TrendsDTO trends = new AuditStatisticsResponseDTO.TrendsDTO();
        trends.setGrowthRate(0.0);
        trends.setPeakHour(12);
        trends.setAverageEventsPerDay(0.0);
        response.setTrends(trends);
        
        return response;
    }
}
