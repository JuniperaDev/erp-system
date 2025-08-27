# ERP System Azure Infrastructure

This directory contains Terraform configurations for deploying the ERP System microservices infrastructure on Microsoft Azure.

## Directory Structure

```
terraform/
├── environments/           # Environment-specific configurations
│   ├── dev/               # Development environment
│   ├── staging/           # Staging environment
│   └── prod/              # Production environment
├── modules/               # Reusable Terraform modules
│   ├── networking/        # VNet, subnets, NSGs, private DNS
│   ├── aks/              # Azure Kubernetes Service
│   ├── database/         # Azure Database for PostgreSQL
│   ├── messaging/        # Azure Event Hubs
│   ├── storage/          # Azure Blob Storage
│   ├── monitoring/       # Azure Monitor, Application Insights
│   └── security/         # Azure Key Vault, policies
└── shared/               # Shared configurations
    ├── providers.tf      # Provider configurations
    ├── variables.tf      # Common variables
    └── outputs.tf        # Common outputs
```

## Quick Start

### Prerequisites

1. **Azure CLI** - [Install Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli)
2. **Terraform** - [Install Terraform](https://learn.hashicorp.com/tutorials/terraform/install-cli) (>= 1.0)
3. **Azure Subscription** - With Contributor permissions

### Authentication

```bash
# Login to Azure
az login

# Set the subscription (if you have multiple)
az account set --subscription "your-subscription-id"
```

### Deploy Development Environment

```bash
# Navigate to dev environment
cd environments/dev

# Copy and customize variables
cp terraform.tfvars.example terraform.tfvars
# Edit terraform.tfvars with your values

# Initialize Terraform
terraform init

# Plan the deployment
terraform plan

# Apply the infrastructure
terraform apply
```

### Access the AKS Cluster

```bash
# Get cluster credentials
az aks get-credentials \
  --resource-group $(terraform output -raw resource_group_name) \
  --name $(terraform output -raw aks_cluster_name)

# Verify access
kubectl get nodes
```

## Environment Configurations

### Development (`environments/dev/`)

**Purpose**: Development and testing
**Characteristics**:
- Minimal resource allocation for cost savings
- Single availability zone
- Basic monitoring
- 7-day backup retention

**Estimated Monthly Cost**: ~$400

### Staging (`environments/staging/`)

**Purpose**: Pre-production testing and validation
**Characteristics**:
- Production-like configuration with smaller scale
- High availability database
- Enhanced monitoring
- 14-day backup retention

**Estimated Monthly Cost**: ~$800

### Production (`environments/prod/`)

**Purpose**: Live production workloads
**Characteristics**:
- Full production scale and redundancy
- Multi-zone deployment
- Comprehensive monitoring and alerting
- 35-day backup retention with geo-redundancy

**Estimated Monthly Cost**: ~$1,702

## Module Documentation

### Networking Module

Creates the foundational network infrastructure including:
- Virtual Network with segmented subnets
- Network Security Groups with security rules
- Private DNS zones for service discovery
- Private endpoints for secure connectivity

**Key Resources**:
- `azurerm_virtual_network`
- `azurerm_subnet` (AKS, Database, Application Gateway)
- `azurerm_network_security_group`
- `azurerm_private_dns_zone`

### AKS Module

Deploys a production-ready Kubernetes cluster with:
- Auto-scaling node pools (2-10 nodes)
- Azure Container Registry integration
- Azure AD RBAC integration
- Monitoring and logging integration

**Key Resources**:
- `azurerm_kubernetes_cluster`
- `azurerm_container_registry`
- `azurerm_user_assigned_identity`

### Database Module

Provisions PostgreSQL database infrastructure:
- Azure Database for PostgreSQL Flexible Server
- High availability configuration (production)
- Automated backups and point-in-time recovery
- Private endpoint connectivity

**Key Resources**:
- `azurerm_postgresql_flexible_server`
- `azurerm_postgresql_flexible_server_database`
- `azurerm_postgresql_flexible_server_configuration`

### Messaging Module

Sets up event streaming with Azure Event Hubs:
- Kafka-compatible Event Hubs namespace
- Individual Event Hubs for microservice topics
- Consumer groups for each service
- Network access restrictions

**Key Resources**:
- `azurerm_eventhub_namespace`
- `azurerm_eventhub` (6 topics for microservices)
- `azurerm_eventhub_consumer_group`

### Storage Module

Creates blob storage for application data:
- Storage account with private endpoints
- Containers for different data types
- Versioning and soft delete policies
- Network access restrictions

**Key Resources**:
- `azurerm_storage_account`
- `azurerm_storage_container`
- `azurerm_private_endpoint`

### Monitoring Module

Implements comprehensive observability:
- Log Analytics workspace for centralized logging
- Application Insights for application monitoring
- Metric alerts for proactive monitoring
- Action groups for alert notifications

**Key Resources**:
- `azurerm_log_analytics_workspace`
- `azurerm_application_insights`
- `azurerm_monitor_metric_alert`
- `azurerm_monitor_action_group`

### Security Module

Manages security and compliance:
- Azure Key Vault for secrets management
- Managed identities for service authentication
- Azure Policy assignments for governance
- Network security configurations

**Key Resources**:
- `azurerm_key_vault`
- `azurerm_key_vault_access_policy`
- `azurerm_resource_group_policy_assignment`

## Configuration Variables

### Required Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `database_password` | PostgreSQL admin password | `SecurePassword123!` |
| `admin_group_object_ids` | Azure AD admin groups | `["guid1", "guid2"]` |
| `alert_email_addresses` | Email addresses for alerts | `["admin@company.com"]` |

### Optional Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `project_name` | `erp-system` | Project name prefix |
| `location` | `East US 2` | Azure region |

## Outputs

Each environment provides the following outputs:

| Output | Description |
|--------|-------------|
| `resource_group_name` | Name of the resource group |
| `aks_cluster_name` | Name of the AKS cluster |
| `database_server_fqdn` | PostgreSQL server FQDN |
| `key_vault_name` | Name of the Key Vault |
| `storage_account_name` | Name of the storage account |

## Security Best Practices

### Network Security
- All Azure services use private endpoints
- Network Security Groups restrict inter-subnet traffic
- No public IP addresses for backend services

### Identity and Access
- Managed identities for service-to-service authentication
- Azure AD integration for human access
- Principle of least privilege for all access policies

### Data Protection
- Encryption at rest for all data stores
- TLS 1.2+ for all network communications
- Regular backup testing and validation

## Cost Management

### Optimization Strategies
- Use Azure Reserved Instances for predictable workloads
- Configure auto-scaling to match demand
- Implement lifecycle policies for storage
- Regular cost reviews and optimization

### Cost Monitoring
- Azure Cost Management alerts configured
- Monthly cost reports generated
- Resource tagging for cost allocation

## Troubleshooting

### Common Issues

**Terraform State Lock**
```bash
# If state is locked, force unlock (use carefully)
terraform force-unlock <lock-id>
```

**AKS Access Issues**
```bash
# Reset AKS credentials
az aks get-credentials --resource-group <rg> --name <cluster> --overwrite-existing
```

**Key Vault Access Denied**
```bash
# Check access policies
az keyvault show --name <vault-name> --query "properties.accessPolicies"
```

### Useful Commands

```bash
# Check Terraform state
terraform state list
terraform state show <resource>

# Validate configuration
terraform validate
terraform fmt -check

# Import existing resources
terraform import <resource_type>.<name> <azure_resource_id>
```

## Maintenance

### Regular Tasks
- **Weekly**: Review monitoring alerts and metrics
- **Monthly**: Apply security updates to AKS nodes
- **Quarterly**: Review and optimize costs
- **Annually**: Review and update disaster recovery procedures

### Updates
- Keep Terraform providers updated
- Monitor Azure service updates and deprecations
- Test infrastructure changes in development first

## Support

For issues with this infrastructure:

1. **Terraform Issues**: Check the [Terraform Azure Provider documentation](https://registry.terraform.io/providers/hashicorp/azurerm/latest/docs)
2. **Azure Issues**: Refer to [Azure documentation](https://docs.microsoft.com/en-us/azure/)
3. **Application Issues**: Contact the development team

## Contributing

When making changes to the infrastructure:

1. Create a feature branch
2. Test changes in the development environment
3. Update documentation as needed
4. Submit a pull request for review

## License

This infrastructure code is part of the ERP System project and follows the same licensing terms.
