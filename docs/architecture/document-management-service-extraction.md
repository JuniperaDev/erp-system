# Document Management Service Extraction Architecture

## Epic 1.1: Document Management Service Extraction
### Story 1.1.1: Design Document Management Service Architecture

**Version:** 1.0  
**Date:** August 11, 2025  
**Author:** Devin AI  
**Status:** Design Phase  

## Executive Summary

This document outlines the architecture design for extracting the document management functionality from the monolithic ERP system into a standalone microservice. The design maintains backward compatibility while enabling independent scaling, deployment, and maintenance of document management capabilities.

## Current State Analysis

### Existing Components

#### Core Interfaces and Implementations
- **FileStorageService Interface** (`io.github.erp.internal.files.FileStorageService`)
  - Provides abstraction for file system operations
  - Supports multiple storage implementations
  - Includes integrity verification (MD5/SHA-512 checksums)

- **Storage Service Implementations**
  - `ReportsFSStorageService`: Handles report file storage
  - `BusinessDocumentFSStorageService`: Manages business document storage
  - Both implement filesystem-based storage with configurable directories

#### Domain Entities
- **FileUpload** (`io.github.erp.domain.FileUpload`)
  - Core entity for file upload metadata
  - Includes file content (BLOB), type, processing status
  - Supports placeholders for categorization

- **BusinessDocument** (`io.github.erp.domain.BusinessDocument`)
  - Enterprise document management entity
  - Includes security clearance, audit trail, checksum verification
  - Links to ApplicationUser, Dealer, and security models

- **FileType** (`io.github.erp.domain.FileType`)
  - File type classification and templates
  - Supports multiple file medium types and model types

#### REST Endpoints
- **FileUploadResource** (`/api/file-uploads`)
  - Full CRUD operations for file uploads
  - Search functionality with Elasticsearch integration
  - Pagination and filtering support

- **BusinessDocumentResource** (`/api/business-documents`)
  - Complete business document lifecycle management
  - Advanced querying and search capabilities

#### Configuration
- **BusinessDocumentProperties**: Configurable storage directory
- **ReportsProperties**: Report-specific storage configuration
- **FileUtils**: Utility functions for checksum calculation and file operations

### Integration Points
- **Security Integration**: ApplicationUser, SecurityClearance, Dealer entities
- **Search Integration**: Elasticsearch for document indexing
- **Audit Integration**: Creation/modification tracking
- **Business Process Integration**: Links to settlements, reports, and workflows

## Target Architecture Design

### Service Boundaries

#### Document Management Service
**Responsibilities:**
- File upload, download, and storage management
- Document metadata management
- File type classification and validation
- Search and indexing capabilities
- Security and access control
- Audit trail maintenance

**Not Responsible For:**
- User authentication (delegated to Identity Service)
- Business process orchestration
- Report generation logic
- Financial calculations

### Microservice Architecture

#### Service Structure
```
document-management-service/
├── src/main/java/io/github/erp/docmgmt/
│   ├── api/                    # REST API controllers
│   ├── domain/                 # Domain entities
│   ├── service/                # Business logic services
│   ├── repository/             # Data access layer
│   ├── config/                 # Configuration classes
│   ├── security/               # Security configuration
│   └── integration/            # External service integration
├── src/main/resources/
│   ├── config/                 # Application configuration
│   ├── db/migration/           # Database migration scripts
│   └── application.yml         # Service configuration
└── docs/                       # Service documentation
```

#### Core Components

##### API Layer
- **DocumentController**: Document lifecycle management
- **FileUploadController**: File upload/download operations
- **FileTypeController**: File type management
- **SearchController**: Document search and indexing

##### Service Layer
- **DocumentService**: Core document operations
- **FileStorageService**: File storage abstraction (extracted)
- **SecurityService**: Access control and permissions
- **SearchService**: Document indexing and search
- **AuditService**: Change tracking and audit trail

##### Domain Layer
- **Document**: Core document entity
- **FileMetadata**: File metadata and properties
- **DocumentType**: Document classification
- **AccessPermission**: Security and access control
- **AuditEntry**: Audit trail records

##### Infrastructure Layer
- **FileSystemStorageAdapter**: Local filesystem storage
- **CloudStorageAdapter**: Cloud storage integration (future)
- **SearchIndexAdapter**: Elasticsearch integration
- **SecurityAdapter**: External security service integration

### API Design

#### REST Endpoints

##### Document Management
```
POST   /api/v1/documents                    # Create document
GET    /api/v1/documents                    # List documents
GET    /api/v1/documents/{id}               # Get document
PUT    /api/v1/documents/{id}               # Update document
DELETE /api/v1/documents/{id}               # Delete document
PATCH  /api/v1/documents/{id}               # Partial update
```

##### File Operations
```
POST   /api/v1/documents/{id}/upload        # Upload file content
GET    /api/v1/documents/{id}/download      # Download file content
GET    /api/v1/documents/{id}/metadata      # Get file metadata
POST   /api/v1/documents/{id}/verify        # Verify file integrity
```

##### Search and Discovery
```
GET    /api/v1/documents/search             # Search documents
GET    /api/v1/documents/types              # List document types
POST   /api/v1/documents/bulk               # Bulk operations
```

##### Backward Compatibility (v0 endpoints)
```
POST   /api/file-uploads                    # Legacy file upload
GET    /api/file-uploads                    # Legacy file listing
POST   /api/business-documents              # Legacy business documents
GET    /api/business-documents              # Legacy business document listing
```

#### Event Schema
```json
{
  "eventType": "DocumentCreated|DocumentUpdated|DocumentDeleted",
  "documentId": "uuid",
  "userId": "string",
  "timestamp": "iso8601",
  "metadata": {
    "documentType": "string",
    "size": "number",
    "checksum": "string"
  }
}
```

### Data Model

#### Document Entity
```sql
CREATE TABLE documents (
    id BIGSERIAL PRIMARY KEY,
    document_title VARCHAR(255) NOT NULL,
    description TEXT,
    document_serial UUID NOT NULL UNIQUE,
    file_path VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size BIGINT NOT NULL,
    checksum VARCHAR(128) NOT NULL,
    algorithm_id BIGINT NOT NULL,
    security_clearance_id BIGINT NOT NULL,
    created_by BIGINT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_modified_by BIGINT,
    last_modified_date TIMESTAMP,
    version INTEGER DEFAULT 1,
    is_deleted BOOLEAN DEFAULT FALSE
);
```

#### File Metadata Entity
```sql
CREATE TABLE file_metadata (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    storage_path VARCHAR(500) NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    encoding VARCHAR(50),
    upload_token VARCHAR(255),
    processing_status VARCHAR(50) DEFAULT 'PENDING',
    error_message TEXT,
    FOREIGN KEY (document_id) REFERENCES documents(id)
);
```

### Integration Patterns

#### Service-to-Service Communication
- **Synchronous**: REST APIs for real-time operations
- **Asynchronous**: Event-driven for audit and notifications
- **Circuit Breaker**: Resilience patterns for external dependencies

#### Security Integration
- **JWT Token Validation**: Integrate with existing authentication
- **Role-Based Access Control**: Maintain existing security clearance model
- **API Gateway**: Centralized security and routing

#### Data Consistency
- **Saga Pattern**: For distributed transactions
- **Event Sourcing**: For audit trail requirements
- **CQRS**: Separate read/write models for performance

### Database Migration Strategy

#### Phase 1: Dual Write Pattern
1. Create new document management database schema
2. Implement dual write to both old and new schemas
3. Migrate existing data in batches
4. Validate data consistency

#### Phase 2: Read Migration
1. Switch read operations to new service
2. Maintain write operations to both systems
3. Monitor performance and data consistency
4. Rollback capability maintained

#### Phase 3: Complete Migration
1. Switch all operations to new service
2. Decommission old document management code
3. Clean up legacy database tables
4. Update documentation and monitoring

#### Zero-Downtime Requirements
- **Blue-Green Deployment**: Parallel service deployment
- **Feature Flags**: Gradual rollout control
- **Database Versioning**: Backward-compatible schema changes
- **Rollback Procedures**: Automated rollback on failure detection

### Configuration Management

#### Service Configuration
```yaml
document-management:
  storage:
    type: filesystem # filesystem, s3, azure
    base-path: /var/lib/erp/documents
    max-file-size: 100MB
    allowed-types: [pdf, doc, docx, xls, xlsx, txt]
  
  security:
    jwt:
      secret: ${JWT_SECRET}
      expiration: 3600
    clearance:
      required: true
      default-level: PUBLIC
  
  search:
    elasticsearch:
      hosts: ${ELASTICSEARCH_HOSTS}
      index-name: documents
      batch-size: 100
  
  integration:
    erp-core:
      base-url: ${ERP_CORE_URL}
      timeout: 30s
    audit-service:
      base-url: ${AUDIT_SERVICE_URL}
      async: true
```

### Health Check and Monitoring

#### Health Endpoints
```
GET /actuator/health              # Overall service health
GET /actuator/health/storage      # Storage system health
GET /actuator/health/database     # Database connectivity
GET /actuator/health/search       # Search service health
```

#### Metrics
- **Business Metrics**: Document upload/download rates, storage usage
- **Technical Metrics**: Response times, error rates, throughput
- **Security Metrics**: Authentication failures, access violations

#### Logging Strategy
- **Structured Logging**: JSON format for log aggregation
- **Correlation IDs**: Request tracing across services
- **Security Logging**: Access attempts and security events
- **Performance Logging**: Slow query and operation tracking

## Implementation Roadmap

### Phase 1: Foundation (Weeks 1-2)
- [ ] Create service project structure
- [ ] Implement core domain entities
- [ ] Set up database schema and migrations
- [ ] Implement basic CRUD operations
- [ ] Create health check endpoints

### Phase 2: Core Functionality (Weeks 3-4)
- [ ] Implement file storage abstraction
- [ ] Add file upload/download capabilities
- [ ] Implement security integration
- [ ] Add search functionality
- [ ] Create audit trail system

### Phase 3: Integration (Weeks 5-6)
- [ ] Implement backward compatibility APIs
- [ ] Add event publishing for integration
- [ ] Implement circuit breaker patterns
- [ ] Add monitoring and metrics
- [ ] Performance optimization

### Phase 4: Migration (Weeks 7-8)
- [ ] Implement dual write pattern
- [ ] Data migration scripts and validation
- [ ] Load testing and performance validation
- [ ] Production deployment preparation
- [ ] Documentation and training

### Dependencies and Risks

#### Dependencies
- **Infrastructure**: Kubernetes cluster, database, storage
- **Security Service**: JWT validation and user management
- **Search Service**: Elasticsearch cluster
- **Monitoring**: Prometheus, Grafana, logging infrastructure

#### Risk Assessment
| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| Data Loss During Migration | High | Low | Comprehensive backup, validation scripts |
| Performance Degradation | Medium | Medium | Load testing, performance monitoring |
| Security Vulnerabilities | High | Low | Security review, penetration testing |
| Integration Failures | Medium | Medium | Circuit breakers, fallback mechanisms |

### Testing Strategy

#### Unit Testing
- **Coverage Target**: 90% code coverage
- **Test Framework**: JUnit 5, Mockito
- **Test Categories**: Domain logic, service layer, utilities

#### Integration Testing
- **Database Testing**: Testcontainers for database integration
- **API Testing**: REST Assured for endpoint testing
- **Security Testing**: Authentication and authorization flows

#### End-to-End Testing
- **User Journey Testing**: Complete document lifecycle
- **Performance Testing**: Load and stress testing
- **Security Testing**: Penetration testing and vulnerability scanning

#### Migration Testing
- **Data Integrity**: Validation of migrated data
- **Performance Impact**: Before/after performance comparison
- **Rollback Testing**: Validation of rollback procedures

## Production-Readiness Gates

### Gate 1: Code Quality
- [ ] Code review process established
- [ ] Static analysis tools configured (SonarQube)
- [ ] Code coverage targets met (90%)
- [ ] Documentation standards followed
- [ ] Coding standards compliance verified

### Gate 2: Testing Excellence
- [ ] Unit test coverage ≥ 90%
- [ ] Integration tests for all major flows
- [ ] End-to-end test automation
- [ ] Performance test baselines established
- [ ] Security test suite implemented

### Gate 3: Security Compliance
- [ ] Security review completed
- [ ] Vulnerability scanning passed
- [ ] Authentication/authorization implemented
- [ ] Data encryption at rest and in transit
- [ ] Security logging and monitoring

### Gate 4: Performance Standards
- [ ] Response time SLAs defined and met
- [ ] Throughput requirements validated
- [ ] Resource utilization optimized
- [ ] Scalability testing completed
- [ ] Performance monitoring implemented

### Gate 5: Operational Readiness
- [ ] Deployment automation configured
- [ ] Monitoring and alerting setup
- [ ] Logging and observability implemented
- [ ] Disaster recovery procedures documented
- [ ] Runbook and troubleshooting guides created

## Conclusion

This architecture design provides a comprehensive approach to extracting document management functionality from the monolithic ERP system while maintaining backward compatibility and ensuring production readiness. The phased implementation approach minimizes risk while delivering value incrementally.

The design leverages existing patterns and infrastructure while introducing modern microservice practices for improved scalability, maintainability, and operational excellence.
