---
name: Story 0.1.3 - Migrate Kubernetes Configurations to Azure
about: Epic 0.1 Azure Infrastructure Deployment - Story 0.1.3
title: "Story 0.1.3: Migrate Kubernetes Configurations to Azure"
labels: ["story", "high"]
assignees: []
---

## Story Information

**Epic:** 0.1 Azure Infrastructure Deployment  
**Story:** 0.1.3 Migrate Kubernetes Configurations to Azure  
**Epic Phase:** Phase 0 (Foundation)  
**Business Domain:** Infrastructure & DevOps  
**Story Points:** 8  
**Priority:** High  

## User Story

**As a** DevOps engineer and development team  
**I want** to migrate existing Kubernetes configurations to work with Azure services and integrate with Azure Key Vault for secrets management  
**So that** all 6 ERP microservices can deploy successfully on Azure Kubernetes Service with proper security, configuration, and service connectivity  

## Acceptance Criteria

### Azure Service Integration
- [ ] **ConfigMap Migration**: Update existing ConfigMaps to use Azure service endpoints (PostgreSQL, Event Hubs, Blob Storage)
- [ ] **Secret Management**: Implement Azure Key Vault CSI driver integration for secure secrets injection
- [ ] **Service Deployments**: Create Azure-specific deployment manifests for all 6 microservices
- [ ] **Auto-scaling Configuration**: Migrate HPA configurations to work with Azure Container Insights metrics

### Production-Readiness Gates
- [ ] **Gate 1 - Configuration Standards**: Kubernetes manifest best practices and Azure integration patterns
- [ ] **Gate 2 - Security Excellence**: Key Vault integration, managed identity, and network policies
- [ ] **Gate 3 - Service Connectivity**: All microservices can connect to Azure PostgreSQL, Event Hubs, and storage
- [ ] **Gate 4 - Monitoring Integration**: Azure Monitor and Application Insights integration functional
- [ ] **Gate 5 - Deployment Automation**: Automated deployment pipeline with validation

### Microservice Configurations
- [ ] **Asset Management Service**: Azure-specific deployment with Event Hubs and PostgreSQL connectivity
- [ ] **Financial Core Service**: Integration with financial events topic and database access
- [ ] **Lease Service**: IFRS16 lease events integration and compliance logging
- [ ] **Depreciation Service**: Batch processing configuration with depreciation topic
- [ ] **WIP Service**: Work-in-progress events and asset integration
- [ ] **Reporting Service**: Multi-topic consumer configuration and storage access

### Azure Key Vault Integration
- [ ] **Secret Provider Class**: CSI driver configuration for Key Vault access
- [ ] **Managed Identity**: User-assigned identity for AKS cluster and microservices
- [ ] **Secret Injection**: Database passwords, connection strings, and JWT secrets from Key Vault
- [ ] **Certificate Management**: SSL certificates and service-to-service authentication

## Technical Requirements

### Kubernetes Manifest Updates
- Update existing ConfigMaps to reference Azure service endpoints
- Create Azure-specific Secret Provider Class for Key Vault integration
- Modify deployment manifests to use managed identity and Key Vault secrets
- Update HPA configurations to use Azure Container Insights metrics

### Azure Service Endpoints
- **Database**: Azure Database for PostgreSQL Flexible Server connection strings
- **Messaging**: Azure Event Hubs Kafka-compatible endpoints for all 6 topics
- **Storage**: Azure Blob Storage endpoints for documents, reports, backups, audit logs
- **Monitoring**: Application Insights connection strings for observability

### Security Configuration
- Managed Identity integration for secure Azure service access
- Key Vault CSI driver for secrets injection without hardcoded values
- Network policies for secure pod-to-pod and pod-to-service communication
- RBAC configuration for service account permissions

### Service Discovery
- JHipster Registry deployment on AKS for service discovery
- Eureka client configuration for microservice registration
- Load balancing and service mesh integration considerations

## Definition of Done

### Implementation
- [ ] Azure-specific ConfigMaps created with all service endpoints
- [ ] Secret Provider Class implemented for Key Vault integration
- [ ] All 6 microservice deployments updated for Azure compatibility
- [ ] HPA configurations migrated to use Azure metrics
- [ ] JHipster Registry deployment configured for AKS

### Validation
- [ ] All Kubernetes manifests validate successfully with kubectl
- [ ] Secret injection from Key Vault works correctly
- [ ] Microservices can connect to PostgreSQL, Event Hubs, and storage
- [ ] Auto-scaling triggers properly with Azure Container Insights
- [ ] Service discovery and inter-service communication functional

### Quality Assurance
- [ ] Kubernetes best practices validated (resource limits, health checks, labels)
- [ ] Security review for managed identity and Key Vault access
- [ ] Configuration review for Azure service integration
- [ ] Performance validation with resource requests and limits
- [ ] Operational readiness with monitoring and logging

### Documentation
- [ ] Azure Kubernetes deployment guide with step-by-step instructions
- [ ] Secret management documentation for Key Vault integration
- [ ] Troubleshooting guide for common deployment issues
- [ ] Service configuration reference for each microservice
- [ ] Migration guide from existing Kubernetes to Azure

## Dependencies

### Infrastructure
- Azure Kubernetes Service cluster deployed and configured
- Azure Key Vault with secrets and access policies configured
- Azure Database for PostgreSQL with connection details
- Azure Event Hubs namespace with topics created
- Azure Blob Storage with containers configured

### Services
- Managed Identity created and assigned to AKS cluster
- Key Vault CSI driver installed on AKS cluster
- Azure Container Insights enabled for monitoring
- Application Insights configured for observability

### Team
- Kubernetes configuration review by DevOps team
- Security validation for Key Vault integration
- Database connectivity testing by development team
- Monitoring setup validation by operations team

## Risk Assessment

### High-Risk Items
- **Secret Injection Failures**: Key Vault CSI driver configuration issues
- **Service Connectivity**: Network connectivity to Azure services
- **Configuration Drift**: Differences between existing and Azure configurations
- **Performance Impact**: Resource sizing and scaling behavior changes

### Mitigation Strategies
- Comprehensive testing of Key Vault integration in development environment
- Network connectivity validation and troubleshooting procedures
- Side-by-side configuration comparison and validation
- Performance testing and monitoring during migration

## Success Metrics

### Technical Metrics
- All 6 microservices deploy successfully on AKS
- 100% secret injection success rate from Key Vault
- Zero configuration-related deployment failures
- All health checks and readiness probes functional

### Business Metrics
- Seamless migration from existing Kubernetes to Azure
- No service downtime during configuration migration
- Improved security posture with Key Vault integration
- Enhanced monitoring and observability capabilities

## Notes

This story bridges the gap between infrastructure deployment and application deployment by ensuring all Kubernetes configurations work seamlessly with Azure services.

The migration maintains backward compatibility while leveraging Azure-native security and monitoring capabilities for improved operational excellence.

---

**Link to Devin run:** https://app.devin.ai/sessions/44c86532ffc3445fadc21bc292826c20  
**Requested by:** @rcandidosilva (Rodrigo Silva)
