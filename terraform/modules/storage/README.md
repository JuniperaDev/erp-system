# Storage Module

This module creates Azure Storage Account with blob containers, lifecycle management policies, and security configurations for the ERP system.

## Resources Created

- Storage Account with security hardening
- Blob containers for different data types
- Lifecycle management policies for cost optimization
- Private endpoint for secure access
- Key Vault secrets for access keys

## Usage

```hcl
module "storage" {
  source = "../../modules/storage"

  project_name               = "erp-system"
  environment                = "dev"
  location                   = "East US 2"
  resource_group_name        = "erp-system-dev-rg"
  aks_subnet_id              = module.networking.aks_subnet_id
  private_endpoint_subnet_id = module.networking.database_subnet_id
  key_vault_id               = module.security.key_vault_id
  tags                       = local.common_tags

  account_tier              = "Standard"
  replication_type          = "LRS"
  enable_versioning         = false
  enable_change_feed        = false
  blob_retention_days       = 7
  container_retention_days  = 7
}
```

## Containers Created

- `documents` - Document storage for the ERP system
- `reports` - Generated reports and analytics
- `backups` - Database and application backups
- `audit-logs` - Audit trail and compliance logs

## Lifecycle Management

The module implements intelligent lifecycle policies for cost optimization:

### Documents Container
- Move to Cool tier after 30 days (configurable)
- Move to Archive tier after 90 days (configurable)
- Delete after 7 years for compliance (configurable)

### Reports Container
- Move to Cool tier after 7 days
- Move to Archive tier after 30 days
- Delete after 1 year

### Audit Logs Container
- Move to Archive tier after 90 days
- Retained indefinitely for compliance

### Backups Container
- Move to Cool tier after 7 days
- Move to Archive tier after 30 days
- Delete after 3 years

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project_name | Name of the project | `string` | n/a | yes |
| environment | Environment name | `string` | n/a | yes |
| location | Azure region | `string` | n/a | yes |
| resource_group_name | Name of the resource group | `string` | n/a | yes |
| aks_subnet_id | ID of the AKS subnet | `string` | n/a | yes |
| private_endpoint_subnet_id | ID of the subnet for private endpoints | `string` | n/a | yes |
| key_vault_id | ID of the Key Vault | `string` | `null` | no |
| tags | Tags to apply to resources | `map(string)` | `{}` | no |
| account_tier | Storage account tier | `string` | `"Standard"` | no |
| replication_type | Storage account replication type | `string` | `"LRS"` | no |
| enable_versioning | Enable blob versioning | `bool` | `true` | no |
| enable_change_feed | Enable blob change feed | `bool` | `true` | no |
| blob_retention_days | Blob soft delete retention period | `number` | `30` | no |
| container_retention_days | Container soft delete retention period | `number` | `30` | no |

### Lifecycle Management Variables

| Name | Description | Type | Default |
|------|-------------|------|---------|
| lifecycle_cool_after_days | Days to move blobs to cool tier | `number` | `30` |
| lifecycle_archive_after_days | Days to move blobs to archive tier | `number` | `90` |
| lifecycle_delete_after_days | Days to delete blobs | `number` | `2555` |
| lifecycle_reports_cool_days | Days to move reports to cool tier | `number` | `7` |
| lifecycle_reports_archive_days | Days to move reports to archive tier | `number` | `30` |
| lifecycle_reports_delete_days | Days to delete reports | `number` | `365` |
| lifecycle_audit_archive_days | Days to move audit logs to archive | `number` | `90` |
| lifecycle_backup_cool_days | Days to move backups to cool tier | `number` | `7` |
| lifecycle_backup_archive_days | Days to move backups to archive tier | `number` | `30` |
| lifecycle_backup_delete_days | Days to delete backups | `number` | `1095` |

## Outputs

| Name | Description |
|------|-------------|
| storage_account_id | ID of the storage account |
| storage_account_name | Name of the storage account |
| primary_connection_string | Primary connection string |
| primary_access_key | Primary access key |

## Security Features

- TLS 1.2 minimum version enforced
- Public blob access disabled
- Network rules restricting access to AKS subnet
- Private endpoint for secure connectivity
- Blob versioning and change feed for audit trails
- Soft delete protection for containers and blobs

## Cost Optimization

- Intelligent tiering with lifecycle policies
- Environment-specific replication strategies
- Automated cleanup of old data
- Optimized retention periods by data type

## Requirements

- Terraform >= 1.0
- AzureRM provider ~> 3.0
- Virtual network with subnets configured
- Key Vault for secrets storage
