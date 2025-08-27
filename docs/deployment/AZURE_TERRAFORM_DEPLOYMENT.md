# Azure Terraform Deployment Guide

This document provides a comprehensive guide for deploying the ERP System microservices to Microsoft Azure using Terraform as Infrastructure as Code.

## Overview

The Azure deployment strategy leverages managed Azure services to provide a scalable, secure, and cost-effective infrastructure for the ERP System microservices. The infrastructure is defined using Terraform modules and supports multiple environments (dev, staging, prod).

## Production-Readiness Gates

This Azure infrastructure deployment follows a comprehensive production-readiness framework with five critical gates that must be satisfied before proceeding to production deployment:

### Gate 1 - Infrastructure Standards
- **Terraform Best Practices**: All modules follow HashiCorp's recommended patterns and naming conventions
- **Module Structure**: Consistent input/output definitions across all 7 infrastructure modules
- **Documentation Standards**: Complete module specifications with usage examples and troubleshooting guides
- **Code Quality**: Terraform validation, formatting, and security scanning integrated into CI/CD pipeline

### Gate 2 - Security Compliance
- **Azure Security Baseline**: Implementation follows Microsoft's Azure Security Benchmark guidelines
- **Key Vault Integration**: All secrets, certificates, and sensitive configuration managed through Azure Key Vault
- **Network Isolation**: Private endpoints for all Azure services with no public internet exposure for backend services
- **Identity Management**: Managed identities for service-to-service authentication with least privilege access

### Gate 3 - Cost Optimization
- **Resource Sizing Strategy**: Environment-specific resource allocation optimized for workload requirements
- **Cost Monitoring Implementation**: Azure Cost Management alerts and budget controls configured
- **Auto-scaling Configuration**: Dynamic scaling policies to optimize costs while maintaining performance
- **Reserved Instance Strategy**: Recommendations for predictable workloads to reduce operational costs

### Gate 4 - Operational Excellence
- **Monitoring Strategy**: Comprehensive observability with Azure Monitor, Application Insights, and custom dashboards
- **Logging Implementation**: Centralized log aggregation with retention policies and compliance requirements
- **Alerting Framework**: Proactive alerting for infrastructure health, performance, and security events
- **Backup Strategies**: Automated backup procedures with tested recovery processes and RTO/RPO targets

### Gate 5 - Deployment Automation
- **CI/CD Integration**: Automated infrastructure deployment with validation and rollback capabilities
- **Infrastructure Validation**: Automated testing of deployed infrastructure components and connectivity
- **Environment Promotion**: Controlled promotion process from development through staging to production
- **Change Management**: Infrastructure changes tracked with approval workflows and audit trails

## Architecture

### Azure Services Mapping

| Component | Azure Service | Purpose |
|-----------|---------------|---------|
| Container Orchestration | Azure Kubernetes Service (AKS) | Host microservices containers |
| Database | Azure Database for PostgreSQL Flexible Server | Primary data store |
| Messaging | Azure Event Hubs | Kafka-compatible event streaming |
| API Gateway | Azure Application Gateway | Load balancing and SSL termination |
| Storage | Azure Blob Storage | Document and file storage |
| Caching | Azure Cache for Redis | Application caching |
| Monitoring | Azure Monitor + Application Insights | Observability and alerting |
| Security | Azure Key Vault + Managed Identity | Secrets management |
| Container Registry | Azure Container Registry | Docker image storage |

### Network Architecture

```
Azure Virtual Network (10.x.0.0/16)
├── AKS Subnet (10.x.1.0/24)
│   ├── Microservices Pods
│   └── JHipster Registry
├── Database Subnet (10.x.2.0/24)
│   └── PostgreSQL Flexible Server
└── Application Gateway Subnet (10.x.3.0/24)
    └── Application Gateway
```

## Infrastructure Modules

### 1. Networking Module (`modules/networking/`)

Creates the foundational network infrastructure:
- Virtual Network with appropriate subnets
- Network Security Groups with security rules
- Private DNS zones for PostgreSQL
- Private endpoints for secure connectivity

### 2. AKS Module (`modules/aks/`)

Deploys the Kubernetes cluster:
- AKS cluster with auto-scaling node pools
- Azure Container Registry integration
- Azure AD integration for RBAC
- Log Analytics integration for monitoring

### 3. Database Module (`modules/database/`)

Provisions the PostgreSQL database:
- Azure Database for PostgreSQL Flexible Server
- High availability configuration (production)
- Automated backups and point-in-time recovery
- Private endpoint connectivity

### 4. Messaging Module (`modules/messaging/`)

Sets up event streaming infrastructure:
- Azure Event Hubs namespace
- Individual Event Hubs for each microservice topic:
  - `asset-events-topic`
  - `financial-events-topic`
  - `lease-events-topic`
  - `depreciation-batch-topic`
  - `wip-events-topic`
  - `reporting-events-topic`
- Consumer groups for each microservice

### 5. Storage Module (`modules/storage/`)

Creates blob storage for documents and files:
- Storage account with private endpoints
- Containers for documents, reports, backups, and audit logs
- Versioning and soft delete policies

### 6. Monitoring Module (`modules/monitoring/`)

Implements observability stack:
- Log Analytics workspace
- Application Insights for application monitoring
- Metric alerts for infrastructure and applications
- Action groups for alert notifications

### 7. Security Module (`modules/security/`)

Manages security and compliance:
- Azure Key Vault for secrets management
- Managed identities for secure service authentication
- Azure Policy assignments for governance
- Network security rules

## Environment Configurations

### Development Environment

- **Resource Sizing**: Smaller instances for cost optimization
- **High Availability**: Single zone deployment
- **Backup Retention**: 7 days
- **Monitoring**: Basic alerting
- **Estimated Cost**: ~$400/month

### Staging Environment

- **Resource Sizing**: Production-like but smaller scale
- **High Availability**: Zone-redundant database
- **Backup Retention**: 14 days
- **Monitoring**: Enhanced alerting
- **Estimated Cost**: ~$800/month

### Production Environment

- **Resource Sizing**: Full production scale
- **High Availability**: Multi-zone deployment
- **Backup Retention**: 35 days with geo-redundancy
- **Monitoring**: Comprehensive alerting and dashboards
- **Estimated Cost**: ~$1,702/month

## Deployment Process

### Prerequisites

1. **Azure CLI**: Install and authenticate with Azure
2. **Terraform**: Version >= 1.0
3. **kubectl**: For Kubernetes management
4. **Azure Permissions**: Contributor role on target subscription

### Step 1: Initialize Terraform

```bash
cd terraform/environments/dev
terraform init
```

### Step 2: Configure Variables

Copy and customize the terraform.tfvars file:

```bash
cp terraform.tfvars.example terraform.tfvars
# Edit terraform.tfvars with your specific values
```

Required variables:
- `database_password`: Secure password for PostgreSQL
- `admin_group_object_ids`: Azure AD groups for admin access
- `alert_email_addresses`: Email addresses for monitoring alerts

### Step 3: Plan and Apply Infrastructure

```bash
# Review the deployment plan
terraform plan

# Apply the infrastructure
terraform apply
```

### Step 4: Configure AKS Access

```bash
# Get AKS credentials
az aks get-credentials --resource-group $(terraform output -raw resource_group_name) \
                       --name $(terraform output -raw aks_cluster_name)

# Verify connectivity
kubectl get nodes
```

### Step 5: Install Azure Key Vault CSI Driver

```bash
# Add the Azure Key Vault CSI driver
helm repo add csi-secrets-store-provider-azure https://azure.github.io/secrets-store-csi-driver-provider-azure/charts
helm install csi-secrets-store-provider-azure/csi-secrets-store-provider-azure --generate-name
```

### Step 6: Deploy Microservices

```bash
# Apply Azure-specific Kubernetes manifests
kubectl apply -f ../../k8s/azure/
```

## Microservices Configuration

### Database Connectivity

Each microservice connects to Azure Database for PostgreSQL using:
- **Connection String**: Retrieved from Azure Key Vault
- **SSL Mode**: Required for security
- **Connection Pooling**: Configured for optimal performance

### Event Streaming

Microservices use Azure Event Hubs for inter-service communication:
- **Protocol**: Kafka-compatible SASL_SSL
- **Authentication**: Connection string from Key Vault
- **Topics**: Dedicated Event Hub for each business domain

### Storage Integration

Document and file storage uses Azure Blob Storage:
- **Authentication**: Managed Identity or storage account key
- **Containers**: Organized by content type (documents, reports, etc.)
- **Access**: Private endpoints for security

## Security Considerations

### Network Security

- **Private Endpoints**: All Azure services use private connectivity
- **Network Security Groups**: Restrict traffic between subnets
- **Azure Firewall**: Optional for additional network protection

### Identity and Access Management

- **Managed Identity**: Services authenticate using Azure AD identities
- **RBAC**: Role-based access control for AKS and Azure resources
- **Key Vault**: Centralized secrets management

### Data Protection

- **Encryption at Rest**: All data encrypted using Azure-managed keys
- **Encryption in Transit**: TLS 1.2+ for all communications
- **Backup Encryption**: Database backups encrypted automatically

## Monitoring and Alerting

### Application Monitoring

- **Application Insights**: Distributed tracing and performance monitoring
- **Custom Metrics**: Business-specific metrics from microservices
- **Log Aggregation**: Centralized logging in Log Analytics

### Infrastructure Monitoring

- **Azure Monitor**: Infrastructure metrics and alerts
- **Resource Health**: Automatic health checks for Azure services
- **Cost Management**: Budget alerts and cost optimization recommendations

### Alert Configuration

Critical alerts are configured for:
- High CPU/memory usage (>80%)
- Database connection failures
- Application errors and exceptions
- Infrastructure service outages

## Cost Optimization

### Resource Sizing

- **Auto-scaling**: HPA configured for 2-10 replicas based on CPU/memory
- **Node Pools**: Separate pools for system and application workloads
- **Reserved Instances**: Use for predictable workloads in production

### Storage Optimization

- **Lifecycle Policies**: Automatic tiering of blob storage
- **Backup Retention**: Optimized retention periods per environment
- **Compression**: Enable for log data and backups

## Disaster Recovery

### Backup Strategy

- **Database**: Automated backups with point-in-time recovery
- **Application Data**: Regular snapshots of persistent volumes
- **Configuration**: Infrastructure as Code for rapid rebuilding

### High Availability

- **Multi-Zone**: Production deployment across availability zones
- **Load Balancing**: Application Gateway distributes traffic
- **Health Checks**: Automatic failover for unhealthy instances

## Troubleshooting

### Common Issues

1. **AKS Node Issues**
   ```bash
   kubectl get nodes
   kubectl describe node <node-name>
   ```

2. **Database Connectivity**
   ```bash
   kubectl logs <pod-name> | grep -i database
   ```

3. **Key Vault Access**
   ```bash
   kubectl describe secretproviderclass erp-system-secrets
   ```

### Useful Commands

```bash
# Check Terraform state
terraform state list

# View AKS cluster info
az aks show --resource-group <rg-name> --name <cluster-name>

# Monitor Event Hubs
az eventhubs eventhub show --resource-group <rg-name> --namespace-name <namespace>

# Check Key Vault secrets
az keyvault secret list --vault-name <vault-name>
```

## CI/CD Integration Strategy

The Azure infrastructure deployment supports both Azure DevOps and GitHub Actions for continuous integration and deployment workflows.

### GitHub Actions Integration

#### Infrastructure Validation Pipeline
```yaml
# .github/workflows/terraform-ci.yml
name: Terraform CI/CD
on:
  pull_request:
    paths: ['terraform/**']
  push:
    branches: [main]
    paths: ['terraform/**']

jobs:
  validate:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: hashicorp/setup-terraform@v2
      - name: Terraform Format Check
        run: terraform fmt -check -recursive
      - name: Terraform Validate
        run: |
          cd terraform/environments/dev
          terraform init -backend=false
          terraform validate
```

#### Multi-Environment Deployment
- **Development**: Automatic deployment on merge to main branch
- **Staging**: Manual approval required with environment-specific validation
- **Production**: Multi-stage approval with security and compliance checks

### Azure DevOps Integration

#### Pipeline Structure
```yaml
# azure-pipelines.yml
trigger:
  branches:
    include: [main]
  paths:
    include: ['terraform/*']

stages:
  - stage: Validate
    jobs:
      - job: TerraformValidate
        steps:
          - task: TerraformInstaller@0
          - task: TerraformTaskV3@3
            inputs:
              command: 'validate'
              workingDirectory: 'terraform/environments/dev'
```

#### Security Integration
- **Azure Security Center**: Automated security scanning of infrastructure configurations
- **Policy Compliance**: Azure Policy validation integrated into deployment pipeline
- **Secret Scanning**: Detection of hardcoded secrets or credentials in Terraform code

### Deployment Automation Features

#### Infrastructure Testing
- **Connectivity Tests**: Automated validation of network connectivity between services
- **Security Tests**: Verification of private endpoints and network security group rules
- **Performance Tests**: Basic load testing of deployed infrastructure components

#### Rollback Procedures
- **State Backup**: Automatic Terraform state backup before each deployment
- **Blue-Green Deployment**: Support for zero-downtime infrastructure updates
- **Automated Rollback**: Triggered rollback on deployment failure or validation errors

## Migration from Existing Infrastructure

### Enhanced Migration Strategy for Zero-Downtime Requirements

#### Phase 1: Infrastructure Foundation (Weeks 1-2)
- **Week 1**: Deploy Azure infrastructure using Terraform with parallel environment approach
- **Week 2**: Configure networking, security, and monitoring with validation checkpoints
- **Validation Checkpoint**: All Azure services operational and connectivity tests passing

#### Phase 2: Database Migration with High Availability (Weeks 3-4)
- **Week 3**: Set up Azure Database for PostgreSQL with replication from existing database
- **Week 4**: Perform data synchronization and integrity validation with minimal downtime window
- **Validation Checkpoint**: Data consistency verified and performance benchmarks met

#### Phase 3: Application Deployment and Testing (Weeks 5-6)
- **Week 5**: Deploy microservices to AKS with blue-green deployment strategy
- **Week 6**: Configure Event Hubs integration and comprehensive inter-service communication testing
- **Validation Checkpoint**: All microservices operational with full functionality verified

#### Phase 4: Production Cutover and Optimization (Weeks 7-8)
- **Week 7**: Implement DNS cutover to Azure Application Gateway with traffic splitting
- **Week 8**: Monitor performance, optimize configurations, and decommission old infrastructure
- **Validation Checkpoint**: Production workloads stable with performance targets achieved

### Zero-Downtime Migration Procedures

#### Blue-Green Deployment Strategy
1. **Blue Environment**: Current production infrastructure
2. **Green Environment**: New Azure infrastructure deployed in parallel
3. **Traffic Splitting**: Gradual traffic migration using DNS and load balancer configuration
4. **Validation**: Real-time monitoring and automated rollback triggers
5. **Cutover**: Complete traffic migration after validation success

#### Rollback Strategy
- **Immediate Rollback**: DNS-based traffic redirection within 5 minutes
- **Data Rollback**: Point-in-time recovery for database changes
- **Configuration Rollback**: Terraform state rollback with automated procedures
- **Validation**: Automated health checks and performance monitoring

#### Migration Validation Checkpoints
- **Infrastructure Health**: All Azure services operational and properly configured
- **Network Connectivity**: Private endpoints and security groups functioning correctly
- **Data Integrity**: Database migration completed with zero data loss
- **Application Functionality**: All microservices operational with full feature parity
- **Performance Benchmarks**: Response times and throughput meeting or exceeding current metrics
- **Security Compliance**: All security controls operational and audit logs functional

## Support and Maintenance

### Regular Tasks

- **Security Updates**: Apply AKS and node updates monthly
- **Backup Verification**: Test restore procedures quarterly
- **Cost Review**: Monthly cost optimization review
- **Performance Tuning**: Quarterly performance analysis

### Escalation Procedures

1. **Infrastructure Issues**: Azure Support (if applicable)
2. **Application Issues**: Development team
3. **Security Incidents**: Security team and Azure Security Center

For additional support, refer to the Azure documentation and contact the DevOps team.
