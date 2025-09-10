package io.github.erp.service.consumer;

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

import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import io.github.erp.service.elasticsearch.document.AuditEventDocument;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticsearchIndexingConsumer {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexingConsumer.class);
    
    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ElasticsearchIndexingConsumer(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(
        topics = {
            "${spring.kafka.topics.audit-events.business-events.name:erp.audit.prod.business-events.v1}",
            "${spring.kafka.topics.audit-events.security-events.name:erp.audit.prod.security-events.v1}",
            "${spring.kafka.topics.audit-events.system-events.name:erp.audit.prod.system-events.v1}"
        },
        groupId = "erp-system-elasticsearch-indexing",
        containerFactory = "auditTrailEventKafkaListenerContainerFactory"
    )
    public void consumeAuditEventsForIndexing(
            @Payload AuditTrailEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Indexing audit event: {} from topic: {} partition: {} offset: {}", 
                    event.getEventId(), topic, partition, offset);
            
            indexAuditEvent(event, topic);
            
            log.debug("Successfully indexed audit event: {}", event.getEventId());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("Error indexing audit event: {} from topic: {}", 
                    event.getEventId(), topic, e);
        }
    }

    @KafkaListener(
        topics = "${spring.kafka.topics.audit-events.compliance-events.name:erp.audit.prod.compliance-events.v1}",
        groupId = "erp-system-elasticsearch-indexing",
        containerFactory = "complianceAuditEventKafkaListenerContainerFactory"
    )
    public void consumeComplianceEventsForIndexing(
            @Payload ComplianceAuditEvent event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {
        
        try {
            log.info("Indexing compliance event: {} from topic: {} partition: {} offset: {}", 
                    event.getEventId(), topic, partition, offset);
            
            indexComplianceEvent(event);
            
            log.debug("Successfully indexed compliance event: {}", event.getEventId());
            acknowledgment.acknowledge();
            
        } catch (Exception e) {
            log.error("Error indexing compliance event: {} from topic: {}", 
                    event.getEventId(), topic, e);
        }
    }

    private void indexAuditEvent(AuditTrailEvent event, String topic) {
        try {
            AuditEventDocument document = transformToDocument(event);
            
            String indexName = determineIndexName(topic, event.getOccurredOn());
            IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(event.getEventId().toString())
                .withObject(document)
                .build();
            
            elasticsearchTemplate.index(indexQuery, IndexCoordinates.of(indexName));
            
            log.debug("Successfully indexed audit event: {} to index: {}", 
                event.getEventId(), indexName);
                
        } catch (Exception e) {
            log.error("Failed to index audit event: {} from topic: {}", event.getEventId(), topic, e);
            throw new RuntimeException("Failed to index audit event", e);
        }
    }

    private void indexComplianceEvent(ComplianceAuditEvent event) {
        try {
            AuditEventDocument document = transformComplianceToDocument(event);
            
            String indexName = "compliance-events-" + 
                java.time.format.DateTimeFormatter.ofPattern("yyyy.MM").format(
                    event.getOccurredOn().atZone(java.time.ZoneOffset.UTC));
            
            IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(event.getEventId().toString())
                .withObject(document)
                .build();
            
            elasticsearchTemplate.index(indexQuery, IndexCoordinates.of(indexName));
            
            log.debug("Successfully indexed compliance event: {} to index: {}", 
                event.getEventId(), indexName);
                
        } catch (Exception e) {
            log.error("Failed to index compliance event: {} of type: {}", 
                event.getEventId(), event.getComplianceType(), e);
            throw new RuntimeException("Failed to index compliance event", e);
        }
    }

    private AuditEventDocument transformToDocument(AuditTrailEvent event) {
        AuditEventDocument document = new AuditEventDocument();
        document.setEventId(event.getEventId().toString());
        document.setEventType(event.getEventType());
        document.setAggregateType(event.getAggregateType());
        document.setAggregateId(event.getAggregateId());
        document.setOccurredOn(event.getOccurredOn());
        document.setVersion(event.getVersion());
        document.setCorrelationId(event.getCorrelationId() != null ? event.getCorrelationId().toString() : null);
        document.setUserId(event.getUserId());
        document.setIpAddress(event.getIpAddress());
        document.setUserAgent(event.getUserAgent());
        document.setProcessed(event.getProcessed());
        document.setRetryCount(event.getRetryCount());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("actionType", event.getActionType());
        metadata.put("entityName", event.getEntityName());
        metadata.put("oldValues", event.getOldValues());
        metadata.put("newValues", event.getNewValues());
        document.setMetadata(metadata);
        
        String searchableText = buildSearchableText(event);
        document.setSearchableText(searchableText);
        
        return document;
    }

    private AuditEventDocument transformComplianceToDocument(ComplianceAuditEvent event) {
        AuditEventDocument document = new AuditEventDocument();
        document.setEventId(event.getEventId().toString());
        document.setEventType(event.getEventType());
        document.setAggregateType(event.getAggregateType());
        document.setAggregateId(event.getAggregateId());
        document.setOccurredOn(event.getOccurredOn());
        document.setVersion(event.getVersion());
        document.setCorrelationId(event.getCorrelationId() != null ? event.getCorrelationId().toString() : null);
        document.setProcessed(event.getProcessed());
        document.setRetryCount(event.getRetryCount());
        
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("complianceType", event.getComplianceType());
        metadata.put("regulationReference", event.getRegulationReference());
        metadata.put("complianceStatus", event.getComplianceStatus());
        metadata.put("auditDetails", event.getAuditDetails());
        metadata.put("auditorId", event.getAuditorId());
        metadata.put("riskLevel", event.getRiskLevel());
        metadata.put("remediationRequired", event.getRemediationRequired());
        document.setMetadata(metadata);
        
        String searchableText = buildComplianceSearchableText(event);
        document.setSearchableText(searchableText);
        
        return document;
    }

    private String buildSearchableText(AuditTrailEvent event) {
        StringBuilder searchText = new StringBuilder();
        searchText.append(event.getEventType()).append(" ");
        searchText.append(event.getAggregateType()).append(" ");
        searchText.append(event.getAggregateId()).append(" ");
        if (event.getUserId() != null) {
            searchText.append(event.getUserId()).append(" ");
        }
        if (event.getUserAgent() != null) {
            searchText.append(event.getUserAgent()).append(" ");
        }
        if (event.getActionType() != null) {
            searchText.append(event.getActionType()).append(" ");
        }
        if (event.getEntityName() != null) {
            searchText.append(event.getEntityName()).append(" ");
        }
        return searchText.toString().trim();
    }

    private String buildComplianceSearchableText(ComplianceAuditEvent event) {
        StringBuilder searchText = new StringBuilder();
        searchText.append(event.getEventType()).append(" ");
        searchText.append(event.getAggregateType()).append(" ");
        searchText.append(event.getAggregateId()).append(" ");
        if (event.getComplianceType() != null) {
            searchText.append(event.getComplianceType()).append(" ");
        }
        if (event.getRegulationReference() != null) {
            searchText.append(event.getRegulationReference()).append(" ");
        }
        if (event.getComplianceStatus() != null) {
            searchText.append(event.getComplianceStatus()).append(" ");
        }
        if (event.getAuditorId() != null) {
            searchText.append(event.getAuditorId()).append(" ");
        }
        return searchText.toString().trim();
    }

    private String determineIndexName(String topic, Instant occurredOn) {
        String indexPrefix;
        if (topic.contains("business-events")) {
            indexPrefix = "audit-events";
        } else if (topic.contains("security-events")) {
            indexPrefix = "security-events";
        } else if (topic.contains("system-events")) {
            indexPrefix = "system-events";
        } else {
            indexPrefix = "audit-events";
        }
        
        String monthSuffix = java.time.format.DateTimeFormatter.ofPattern("yyyy.MM")
            .format(occurredOn.atZone(java.time.ZoneOffset.UTC));
        
        return indexPrefix + "-" + monthSuffix;
    }
}
