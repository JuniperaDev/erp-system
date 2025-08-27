# Azure Terraform Deployment Guide

This document provides a comprehensive guide for deploying the ERP System microservices to Microsoft Azure using Terraform as Infrastructure as Code.

## Overview

The Azure deployment strategy leverages managed Azure services to provide a scalable, secure, and cost-effective infrastructure for the ERP System microservices. The infrastructure is defined using Terraform modules and supports multiple environments (dev, staging, prod).

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

## Migration from Existing Infrastructure

### Phase 1: Infrastructure Setup (Weeks 1-2)
- Deploy Azure infrastructure using Terraform
- Configure networking and security
- Set up monitoring and alerting

### Phase 2: Database Migration (Weeks 3-4)
- Export data from existing PostgreSQL
- Import to Azure Database for PostgreSQL
- Validate data integrity and performance

### Phase 3: Application Deployment (Weeks 5-6)
- Deploy microservices to AKS
- Configure Event Hubs integration
- Test inter-service communication

### Phase 4: Cutover and Optimization (Weeks 7-8)
- Switch DNS to Azure Application Gateway
- Monitor performance and optimize
- Decommission old infrastructure

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
