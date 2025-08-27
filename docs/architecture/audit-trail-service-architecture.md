# Audit Trail Service Architecture Design
## Story 1.2.1: Design Audit Trail Service Architecture

### Executive Summary

This document provides a comprehensive architectural design for the Audit Trail Service based on the existing implementation in the ERP System. The design leverages the mature audit trail infrastructure already present in the codebase while formalizing specifications for event schema, API contracts, Kafka integration, Elasticsearch search, data retention policies, and service integration patterns.

### Current Architecture Analysis

#### 1. Existing Audit Trail Components

##### 1.1 Core Service Interfaces

**AuditTrailService Interface**
- Location: `src/main/java/io/github/erp/service/AuditTrailService.java`
- Purpose: Primary interface for audit trail operations
- Key Methods:
  - `logBusinessEvent()` - Logs business entity changes with user context
  - `logDocumentEvent()` - Tracks document attachment/detachment operations
  - `logPlaceholderEvent()` - Manages placeholder entity associations
  - `getAuditTrail()` - Retrieves audit history for entities
  - `logCrossCuttingOperation()` - Captures cross-cutting concern operations

**EventSourcingAuditTrailService Interface**
- Location: `src/main/java/io/github/erp/service/EventSourcingAuditTrailService.java`
- Purpose: Advanced event sourcing capabilities for audit trails
- Key Methods:
  - `reconstructAuditTrail()` - Rebuilds audit history from events
  - `generateComplianceReport()` - Creates regulatory compliance reports
  - `replayEvents()` - Replays events for aggregate reconstruction
  - `findRelatedEvents()` - Correlates events across transactions
  - `reconstructEntityState()` - Point-in-time entity state reconstruction
  - `validateEventIntegrity()` - Ensures event sequence integrity
  - `exportAuditTrail()` - Exports audit data in various formats

##### 1.2 Domain Event Infrastructure

**AuditTrailEvent Entity**
- Location: `src/main/java/io/github/erp/domain/events/audit/AuditTrailEvent.java`
- Purpose: Core audit event entity with comprehensive metadata
- Key Fields:
  - `userId` - User performing the action
  - `actionType` - Type of operation (CREATE, UPDATE, DELETE, etc.)
  - `entityName` - Target entity type
  - `oldValues` - Previous state (JSON)
  - `newValues` - New state (JSON)
  - `ipAddress` - Client IP address
  - `userAgent` - Client user agent
  - Inherits: `eventId`, `aggregateId`, `aggregateType`, `occurredOn`, `version`, `correlationId`

**ComplianceAuditEvent Entity**
- Location: `src/main/java/io/github/erp/domain/events/audit/ComplianceAuditEvent.java`
- Purpose: Specialized audit events for regulatory compliance
- Key Fields:
  - `complianceType` - Type of compliance requirement
  - `regulationReference` - Specific regulation citation
  - `complianceStatus` - Compliance state (COMPLIANT, NON_COMPLIANT, PENDING)
  - `auditDetails` - Detailed compliance information
  - `auditorId` - Compliance auditor identifier
  - `riskLevel` - Risk assessment level
  - `remediationRequired` - Flag for required remediation actions

**DomainEventStore Interface**
- Location: `src/main/java/io/github/erp/domain/events/DomainEventStore.java`
- Purpose: Event persistence and retrieval operations
- Audit-Specific Methods:
  - `findAuditTrailEvents()` - Query audit events by entity and date range
  - `findComplianceAuditEvents()` - Retrieve compliance-specific events
  - `getAuditEventSummary()` - Generate event statistics
  - `findEventsByCorrelationId()` - Correlate related events

##### 1.3 REST API Layer

**EventSourcingAuditTrailResource**
- Location: `src/main/java/io/github/erp/web/rest/EventSourcingAuditTrailResource.java`
- Base Path: `/api/audit-trail`
- Key Endpoints:
  - `GET /reconstruct/{entityType}/{entityId}` - Reconstruct audit trail
  - `GET /compliance-report` - Generate compliance reports
  - `GET /replay/{aggregateId}` - Replay events for aggregate
  - `GET /related-events/{correlationId}` - Find correlated events
  - `GET /summary` - Get audit event summary statistics
  - `GET /entity-state/{entityType}/{entityId}` - Point-in-time state reconstruction
  - `GET /timeline/{entityType}/{entityId}` - Audit trail timeline view
  - `GET /validate/{aggregateId}` - Validate event integrity
  - `GET /export/{entityType}/{entityId}` - Export audit trail data

#### 2. Current Implementation Strengths

1. **Comprehensive Event Sourcing**: Full event sourcing implementation with event replay and state reconstruction
2. **Rich Metadata**: Detailed audit events with user context, IP tracking, and correlation IDs
3. **Compliance Focus**: Dedicated compliance audit events with regulatory references
4. **REST API Coverage**: Complete REST API for audit trail access and reporting
5. **Event Integrity**: Built-in event validation and integrity checking
6. **Flexible Querying**: Multiple query patterns for different audit scenarios
7. **Export Capabilities**: Support for exporting audit data in various formats

#### 3. Architecture Gaps Identified

Based on GitHub Issue #17 requirements, the following gaps need to be addressed:

1. **Formal Event Schema Documentation**: JSON schema specifications needed
2. **Kafka Integration Formalization**: Event streaming architecture needs documentation
3. **Elasticsearch Integration**: Search capabilities need formal specification
4. **Data Retention Policies**: Formal retention and archiving strategy required
5. **API Documentation**: OpenAPI/Swagger specifications needed
6. **Service Integration Contracts**: Formal integration patterns documentation
7. **Compliance Reporting Templates**: Standardized report formats needed

### Proposed Architecture Enhancements

#### 4. Event Schema and Metadata Structure

##### 4.1 JSON Schema Specifications

**Base Audit Event Schema**
```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "type": "object",
  "title": "AuditEvent",
  "properties": {
    "eventId": {
      "type": "string",
      "format": "uuid",
      "description": "Unique event identifier"
    },
    "eventType": {
      "type": "string",
      "enum": ["CREATED", "UPDATED", "DELETED", "ACCESSED", "PROCESSED"],
      "description": "Type of audit event"
    },
    "aggregateType": {
      "type": "string",
      "description": "Entity type being audited"
    },
    "aggregateId": {
      "type": "string",
      "description": "Entity instance identifier"
    },
    "occurredOn": {
      "type": "string",
      "format": "date-time",
      "description": "Event timestamp"
    },
    "version": {
      "type": "integer",
      "minimum": 1,
      "description": "Event version for ordering"
    },
    "correlationId": {
      "type": "string",
      "format": "uuid",
      "description": "Transaction correlation identifier"
    },
    "userId": {
      "type": "string",
      "description": "User performing the action"
    },
    "sessionId": {
      "type": "string",
      "description": "User session identifier"
    },
    "ipAddress": {
      "type": "string",
      "format": "ipv4",
      "description": "Client IP address"
    },
    "userAgent": {
      "type": "string",
      "description": "Client user agent"
    },
    "changes": {
      "type": "object",
      "properties": {
        "oldValues": {
          "type": "object",
          "description": "Previous entity state"
        },
        "newValues": {
          "type": "object",
          "description": "New entity state"
        }
      }
    }
  },
  "required": ["eventId", "eventType", "aggregateType", "aggregateId", "occurredOn", "userId"]
}
```

**Compliance Audit Event Schema Extension**
```json
{
  "allOf": [
    {"$ref": "#/definitions/AuditEvent"},
    {
      "type": "object",
      "properties": {
        "complianceType": {
          "type": "string",
          "enum": ["SOX", "GDPR", "IFRS16", "BASEL_III", "CUSTOM"],
          "description": "Compliance framework"
        },
        "regulationReference": {
          "type": "string",
          "description": "Specific regulation citation"
        },
        "complianceStatus": {
          "type": "string",
          "enum": ["COMPLIANT", "NON_COMPLIANT", "PENDING", "EXCEPTION"],
          "description": "Compliance assessment result"
        },
        "riskLevel": {
          "type": "string",
          "enum": ["LOW", "MEDIUM", "HIGH", "CRITICAL"],
          "description": "Risk assessment level"
        },
        "remediationRequired": {
          "type": "boolean",
          "description": "Whether remediation is required"
        },
        "auditorId": {
          "type": "string",
          "description": "Compliance auditor identifier"
        }
      },
      "required": ["complianceType", "complianceStatus"]
    }
  ]
}
```

##### 4.2 Event Classification Taxonomy

**Business Events**
- Entity lifecycle events (CREATE, UPDATE, DELETE)
- Business process events (APPROVED, REJECTED, PROCESSED)
- Document management events (ATTACHED, DETACHED, VIEWED)
- Placeholder management events (LINKED, UNLINKED)

**Compliance Events**
- Regulatory compliance assessments
- Audit trail access events
- Data retention policy executions
- Compliance report generations

**Security Events**
- Authentication events
- Authorization failures
- Data access events
- System configuration changes

**System Events**
- Service health events
- Performance monitoring events
- Error and exception events
- Integration events

#### 5. API Specifications

##### 5.1 OpenAPI Specification Structure

```yaml
openapi: 3.0.3
info:
  title: Audit Trail Service API
  description: Comprehensive audit trail and compliance reporting API
  version: 1.0.0
  contact:
    name: ERP System Team
    email: mailnjeru@gmail.com

servers:
  - url: /api/v1/audit-trail
    description: Production API
  - url: /api/v2/audit-trail
    description: Next generation API (future)

security:
  - bearerAuth: []

paths:
  /events/{entityType}/{entityId}:
    get:
      summary: Retrieve audit trail for entity
      parameters:
        - name: entityType
          in: path
          required: true
          schema:
            type: string
        - name: entityId
          in: path
          required: true
          schema:
            type: string
        - name: fromDate
          in: query
          schema:
            type: string
            format: date-time
        - name: toDate
          in: query
          schema:
            type: string
            format: date-time
        - name: page
          in: query
          schema:
            type: integer
            default: 0
        - name: size
          in: query
          schema:
            type: integer
            default: 20
            maximum: 100
      responses:
        '200':
          description: Audit trail retrieved successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/AuditTrailResponse'
        '400':
          $ref: '#/components/responses/BadRequest'
        '401':
          $ref: '#/components/responses/Unauthorized'
        '403':
          $ref: '#/components/responses/Forbidden'
        '404':
          $ref: '#/components/responses/NotFound'

  /compliance-reports:
    post:
      summary: Generate compliance report
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ComplianceReportRequest'
      responses:
        '202':
          description: Report generation initiated
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ReportGenerationResponse'

components:
  securitySchemes:
    bearerAuth:
      type: http
      scheme: bearer
      bearerFormat: JWT

  schemas:
    AuditTrailResponse:
      type: object
      properties:
        content:
          type: array
          items:
            $ref: '#/components/schemas/AuditEvent'
        pageable:
          $ref: '#/components/schemas/Pageable'
        totalElements:
          type: integer
        totalPages:
          type: integer

    ComplianceReportRequest:
      type: object
      properties:
        reportType:
          type: string
          enum: [SOX, GDPR, IFRS16, CUSTOM]
        fromDate:
          type: string
          format: date-time
        toDate:
          type: string
          format: date-time
        entityTypes:
          type: array
          items:
            type: string
        format:
          type: string
          enum: [PDF, CSV, JSON, XML]
          default: PDF
      required: [reportType, fromDate, toDate]

  responses:
    BadRequest:
      description: Invalid request parameters
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/ErrorResponse'
    
    Unauthorized:
      description: Authentication required
      
    Forbidden:
      description: Insufficient permissions
      
    NotFound:
      description: Resource not found
```

##### 5.2 Rate Limiting and Security

**Rate Limiting Strategy**
- Standard endpoints: 1000 requests/hour per user
- Report generation: 10 requests/hour per user
- Export endpoints: 5 requests/hour per user
- Search endpoints: 500 requests/hour per user

**Authentication and Authorization**
- JWT-based authentication required for all endpoints
- Role-based access control (RBAC) implementation
- Audit trail access requires AUDIT_READ permission
- Compliance reports require COMPLIANCE_READ permission
- Export operations require AUDIT_EXPORT permission

#### 6. Kafka Topic Structure and Event Streaming

##### 6.1 Topic Design

**Topic Naming Convention**
```
erp.audit.{environment}.{event-category}.{version}

Examples:
- erp.audit.prod.business-events.v1
- erp.audit.prod.compliance-events.v1
- erp.audit.prod.security-events.v1
- erp.audit.prod.system-events.v1
```

**Partitioning Strategy**
- Partition by entity type hash for load distribution
- Maintain event ordering within entity type
- Default: 12 partitions per topic for scalability

**Topic Configuration**
```yaml
topics:
  business-events:
    partitions: 12
    replication-factor: 3
    retention.ms: 2592000000  # 30 days
    cleanup.policy: delete
    compression.type: lz4
    
  compliance-events:
    partitions: 6
    replication-factor: 3
    retention.ms: 31536000000  # 1 year
    cleanup.policy: delete
    compression.type: lz4
    
  security-events:
    partitions: 6
    replication-factor: 3
    retention.ms: 7776000000  # 90 days
    cleanup.policy: delete
    compression.type: lz4
```

##### 6.2 Event Serialization

**Avro Schema for Audit Events**
```json
{
  "type": "record",
  "name": "AuditEvent",
  "namespace": "io.github.erp.events.audit",
  "fields": [
    {"name": "eventId", "type": "string"},
    {"name": "eventType", "type": "string"},
    {"name": "aggregateType", "type": "string"},
    {"name": "aggregateId", "type": "string"},
    {"name": "occurredOn", "type": "long", "logicalType": "timestamp-millis"},
    {"name": "version", "type": "int"},
    {"name": "correlationId", "type": ["null", "string"], "default": null},
    {"name": "userId", "type": "string"},
    {"name": "sessionId", "type": ["null", "string"], "default": null},
    {"name": "ipAddress", "type": ["null", "string"], "default": null},
    {"name": "userAgent", "type": ["null", "string"], "default": null},
    {"name": "changes", "type": ["null", "string"], "default": null}
  ]
}
```

##### 6.3 Consumer Group Strategy

**Audit Trail Reconstruction Consumer**
- Group ID: `audit-trail-reconstruction`
- Purpose: Rebuild audit trails from events
- Parallelism: 6 consumers

**Compliance Monitoring Consumer**
- Group ID: `compliance-monitoring`
- Purpose: Real-time compliance assessment
- Parallelism: 3 consumers

**Elasticsearch Indexing Consumer**
- Group ID: `elasticsearch-indexing`
- Purpose: Index events for search
- Parallelism: 4 consumers

**Archival Consumer**
- Group ID: `audit-archival`
- Purpose: Archive events to cold storage
- Parallelism: 2 consumers

#### 7. Elasticsearch Integration

##### 7.1 Index Mapping Design

**Audit Events Index Template**
```json
{
  "index_patterns": ["audit-events-*"],
  "template": {
    "settings": {
      "number_of_shards": 3,
      "number_of_replicas": 1,
      "refresh_interval": "30s",
      "index.lifecycle.name": "audit-events-policy",
      "index.lifecycle.rollover_alias": "audit-events"
    },
    "mappings": {
      "properties": {
        "eventId": {
          "type": "keyword"
        },
        "eventType": {
          "type": "keyword"
        },
        "aggregateType": {
          "type": "keyword"
        },
        "aggregateId": {
          "type": "keyword"
        },
        "occurredOn": {
          "type": "date",
          "format": "strict_date_optional_time||epoch_millis"
        },
        "userId": {
          "type": "keyword"
        },
        "ipAddress": {
          "type": "ip"
        },
        "changes": {
          "type": "object",
          "enabled": false
        },
        "searchableText": {
          "type": "text",
          "analyzer": "standard"
        }
      }
    }
  }
}
```

##### 7.2 Index Lifecycle Management

**ILM Policy**
```json
{
  "policy": {
    "phases": {
      "hot": {
        "actions": {
          "rollover": {
            "max_size": "10GB",
            "max_age": "7d"
          }
        }
      },
      "warm": {
        "min_age": "7d",
        "actions": {
          "allocate": {
            "number_of_replicas": 0
          }
        }
      },
      "cold": {
        "min_age": "30d",
        "actions": {
          "allocate": {
            "number_of_replicas": 0
          }
        }
      },
      "delete": {
        "min_age": "2555d"
      }
    }
  }
}
```

##### 7.3 Search Query Patterns

**Common Search Scenarios**
1. Entity audit history
2. User activity tracking
3. Compliance event monitoring
4. Security incident investigation
5. Performance analysis
6. Error pattern detection

**Example Query Templates**
```json
{
  "entity_audit_history": {
    "query": {
      "bool": {
        "must": [
          {"term": {"aggregateType": "{{entity_type}}"}},
          {"term": {"aggregateId": "{{entity_id}}"}},
          {"range": {"occurredOn": {"gte": "{{from_date}}", "lte": "{{to_date}}"}}}
        ]
      }
    },
    "sort": [{"occurredOn": {"order": "asc"}}]
  },
  
  "user_activity": {
    "query": {
      "bool": {
        "must": [
          {"term": {"userId": "{{user_id}}"}},
          {"range": {"occurredOn": {"gte": "{{from_date}}", "lte": "{{to_date}}"}}}
        ]
      }
    },
    "aggs": {
      "activity_by_type": {
        "terms": {"field": "eventType"}
      }
    }
  }
}
```

#### 8. Data Retention and Archiving Strategy

##### 8.1 Retention Periods by Event Type

**Business Events**
- Hot storage: 30 days
- Warm storage: 90 days
- Cold storage: 7 years
- Archive: Indefinite (compressed)

**Compliance Events**
- Hot storage: 90 days
- Warm storage: 1 year
- Cold storage: 10 years
- Archive: Indefinite (regulatory requirement)

**Security Events**
- Hot storage: 30 days
- Warm storage: 90 days
- Cold storage: 3 years
- Archive: 7 years

**System Events**
- Hot storage: 7 days
- Warm storage: 30 days
- Cold storage: 1 year
- Archive: 3 years

##### 8.2 Archival Storage Strategy

**Cold Storage Implementation**
- AWS S3 Glacier for long-term archival
- Data compression using LZ4 algorithm
- Encrypted storage with AES-256
- Metadata indexing for retrieval

**Archive Format**
```json
{
  "archiveMetadata": {
    "archiveId": "uuid",
    "createdAt": "timestamp",
    "eventCount": "integer",
    "compressionRatio": "float",
    "checksumSHA256": "string"
  },
  "events": [
    "compressed_event_data"
  ]
}
```

##### 8.3 Data Purging Policies

**Automated Purging Schedule**
- Daily: System events older than retention period
- Weekly: Business events older than retention period
- Monthly: Security events older than retention period
- Quarterly: Compliance events (with legal review)

**Purging Safeguards**
- Legal hold check before purging
- Backup verification before deletion
- Audit log of purging activities
- Recovery procedures for accidental deletion

#### 9. Compliance Reporting Capabilities

##### 9.1 Standard Report Templates

**SOX Compliance Report**
- Financial transaction audit trails
- Access control changes
- System configuration modifications
- User privilege escalations
- Data integrity validations

**GDPR Compliance Report**
- Personal data access events
- Data modification history
- Consent management activities
- Data deletion confirmations
- Cross-border data transfers

**IFRS16 Compliance Report**
- Lease accounting transactions
- Asset valuation changes
- Depreciation calculations
- Financial statement impacts
- Disclosure requirements

##### 9.2 Report Generation Framework

**Report Configuration Schema**
```json
{
  "reportTemplate": {
    "id": "string",
    "name": "string",
    "description": "string",
    "complianceFramework": "string",
    "filters": {
      "entityTypes": ["string"],
      "eventTypes": ["string"],
      "dateRange": {
        "from": "date",
        "to": "date"
      },
      "customFilters": {}
    },
    "sections": [
      {
        "title": "string",
        "query": "elasticsearch_query",
        "visualization": "table|chart|summary"
      }
    ],
    "outputFormats": ["PDF", "CSV", "JSON", "XML"]
  }
}
```

##### 9.3 Real-time Compliance Monitoring

**Compliance Rules Engine**
- Rule-based compliance checking
- Real-time violation detection
- Automated alert generation
- Escalation procedures
- Remediation tracking

**Monitoring Dashboards**
- Compliance status overview
- Violation trend analysis
- Risk assessment metrics
- Remediation progress tracking
- Regulatory deadline monitoring

#### 10. Service Integration Contracts

##### 10.1 Event Publishing Contracts

**Service Integration Pattern**
```java
@Component
public class AuditEventPublisher {
    
    @EventListener
    public void handleEntityCreated(EntityCreatedEvent event) {
        AuditTrailEvent auditEvent = AuditTrailEvent.builder()
            .aggregateType(event.getEntityType())
            .aggregateId(event.getEntityId())
            .eventType("CREATED")
            .userId(SecurityUtils.getCurrentUserId())
            .changes(buildChanges(null, event.getEntity()))
            .build();
            
        auditTrailService.publishEvent(auditEvent);
    }
}
```

**Integration Requirements**
- All services must publish audit events for CRUD operations
- Event correlation IDs must be propagated across service calls
- Circuit breaker pattern for audit service calls
- Fallback logging when audit service unavailable
- Retry mechanism with exponential backoff

##### 10.2 Cross-Service Audit Correlation

**Correlation Strategy**
- Generate correlation ID at API gateway
- Propagate correlation ID through all service calls
- Include correlation ID in all audit events
- Enable end-to-end transaction tracking

**Implementation Pattern**
```java
@RestController
public class BusinessController {
    
    @PostMapping("/business-operation")
    public ResponseEntity<?> performOperation(@RequestBody OperationRequest request) {
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);
        
        try {
            // Business logic with audit events
            return ResponseEntity.ok(result);
        } finally {
            MDC.clear();
        }
    }
}
```

##### 10.3 Integration Testing Strategy

**Test Categories**
1. Unit tests for audit event generation
2. Integration tests for service-to-service audit propagation
3. End-to-end tests for complete audit trails
4. Performance tests for audit overhead
5. Failure scenario tests for circuit breaker behavior

**Test Data Management**
- Synthetic test data generation
- Test environment isolation
- Audit event cleanup procedures
- Performance baseline establishment

### Implementation Complexity Assessment

#### 11. Effort Estimation

**Component Complexity Analysis**

| Component | Complexity | Effort (Story Points) | Dependencies |
|-----------|------------|----------------------|--------------|
| Event Schema Documentation | Low | 3 | None |
| API Specifications | Medium | 5 | Event Schema |
| Kafka Integration | Medium | 8 | Event Schema, API |
| Elasticsearch Integration | High | 13 | Kafka, Event Schema |
| Data Retention Policies | Medium | 8 | All components |
| Compliance Reporting | High | 13 | Elasticsearch, API |
| Service Integration | Medium | 8 | Event Schema, API |
| Testing & Documentation | Medium | 5 | All components |

**Total Estimated Effort: 63 Story Points**

#### 12. Sub-Issue Breakdown Recommendation

**Recommendation: Break into 4 Sub-Issues**

Given the complexity and dependencies, the following breakdown is recommended:

**Sub-Issue 1.2.1.1: Event Schema and API Specifications (8 points)**
- Formalize event schema documentation
- Create OpenAPI specifications
- Define service integration contracts
- Dependencies: None
- Duration: 1 sprint

**Sub-Issue 1.2.1.2: Kafka Event Streaming Integration (13 points)**
- Design topic structure and partitioning
- Implement event serialization
- Configure consumer groups
- Dependencies: Sub-issue 1.2.1.1
- Duration: 1 sprint

**Sub-Issue 1.2.1.3: Elasticsearch Search and Indexing (21 points)**
- Design index mappings and lifecycle
- Implement search query patterns
- Configure index management
- Dependencies: Sub-issue 1.2.1.2
- Duration: 2 sprints

**Sub-Issue 1.2.1.4: Data Retention and Compliance Reporting (21 points)**
- Implement retention policies
- Create compliance report templates
- Configure archival processes
- Dependencies: Sub-issue 1.2.1.3
- Duration: 2 sprints

### Architecture Decision Records (ADRs)

#### ADR-001: Event Sourcing as Primary Audit Strategy

**Status**: Accepted

**Context**: Need comprehensive audit trail with point-in-time reconstruction capabilities.

**Decision**: Use event sourcing pattern with domain events as the primary audit mechanism.

**Consequences**:
- Pros: Complete audit history, event replay capability, immutable audit trail
- Cons: Storage overhead, complexity in event schema evolution

#### ADR-002: Kafka for Event Streaming

**Status**: Accepted

**Context**: Need scalable, reliable event streaming for audit events.

**Decision**: Use Apache Kafka for audit event streaming with topic-based partitioning.

**Consequences**:
- Pros: High throughput, durability, scalability
- Cons: Operational complexity, additional infrastructure

#### ADR-003: Elasticsearch for Audit Search

**Status**: Accepted

**Context**: Need fast, flexible search capabilities for audit data.

**Decision**: Use Elasticsearch with index lifecycle management for audit search.

**Consequences**:
- Pros: Fast search, flexible queries, aggregations
- Cons: Additional infrastructure, data duplication

#### ADR-004: Multi-tier Data Retention

**Status**: Accepted

**Context**: Balance between accessibility and storage costs for audit data.

**Decision**: Implement hot/warm/cold/archive tier strategy with automated lifecycle management.

**Consequences**:
- Pros: Cost optimization, compliance with retention requirements
- Cons: Complexity in data retrieval from cold storage

### Deployment Architecture

#### 13. Component Deployment Strategy

**Microservice Deployment**
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: audit-trail-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: audit-trail-service
  template:
    metadata:
      labels:
        app: audit-trail-service
    spec:
      containers:
      - name: audit-trail-service
        image: erp/audit-trail-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: KAFKA_BROKERS
          value: "kafka-cluster:9092"
        - name: ELASTICSEARCH_URL
          value: "http://elasticsearch:9200"
        resources:
          requests:
            memory: "512Mi"
            cpu: "250m"
          limits:
            memory: "1Gi"
            cpu: "500m"
```

**Infrastructure Requirements**
- Kubernetes cluster with 3+ nodes
- Kafka cluster with 3 brokers
- Elasticsearch cluster with 3 nodes
- PostgreSQL database for event store
- Redis for caching
- S3-compatible storage for archival

#### 14. Monitoring and Observability

**Key Metrics**
- Audit event ingestion rate
- Event processing latency
- Search query performance
- Compliance report generation time
- Storage utilization by tier
- Error rates and retry counts

**Alerting Rules**
- Audit event ingestion failures
- High search query latency
- Compliance violation detection
- Storage capacity thresholds
- Service health degradation

### Security and Compliance Matrix

#### 15. Security Controls

| Control | Implementation | Compliance Framework |
|---------|----------------|---------------------|
| Data Encryption | AES-256 at rest, TLS 1.3 in transit | SOX, GDPR, IFRS16 |
| Access Control | RBAC with JWT authentication | SOX, GDPR |
| Audit Logging | Comprehensive audit trail | All frameworks |
| Data Integrity | SHA-256 checksums, event validation | SOX, IFRS16 |
| Data Retention | Automated lifecycle management | GDPR, SOX |
| Incident Response | Automated alerting and escalation | All frameworks |

#### 16. Compliance Mapping

**SOX Requirements**
- ✅ Financial transaction audit trails
- ✅ Access control monitoring
- ✅ System change tracking
- ✅ Data integrity validation
- ✅ Segregation of duties enforcement

**GDPR Requirements**
- ✅ Personal data processing logs
- ✅ Consent management tracking
- ✅ Data subject access rights
- ✅ Data deletion confirmations
- ✅ Cross-border transfer monitoring

**IFRS16 Requirements**
- ✅ Lease accounting audit trails
- ✅ Asset valuation tracking
- ✅ Depreciation calculation logs
- ✅ Financial statement impact tracking
- ✅ Disclosure requirement compliance

### Implementation Roadmap

#### Phase 1: Foundation (Sprint 1)
- Event schema documentation
- API specifications
- Service integration contracts
- Basic testing framework

#### Phase 2: Streaming (Sprint 2)
- Kafka topic configuration
- Event serialization implementation
- Consumer group setup
- Stream processing logic

#### Phase 3: Search (Sprints 3-4)
- Elasticsearch index design
- Search API implementation
- Query optimization
- Performance tuning

#### Phase 4: Compliance (Sprints 5-6)
- Retention policy implementation
- Compliance report templates
- Archival process automation
- Monitoring and alerting

### Conclusion

The proposed audit trail service architecture builds upon the existing comprehensive implementation while formalizing specifications and filling identified gaps. The design ensures regulatory compliance, scalability, and maintainability while leveraging proven patterns from the current codebase.

The recommendation to break this into 4 sub-issues allows for incremental delivery and proper dependency management, ensuring each component can be thoroughly tested and validated before proceeding to the next phase.

The architecture supports the microservices migration strategy outlined in the ERP modernization plan while maintaining backward compatibility with existing audit trail functionality.
