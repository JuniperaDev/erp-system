# Database Module

This module creates an Azure Database for PostgreSQL Flexible Server with high availability, backup, and security configurations for the ERP system.

## Resources Created

- PostgreSQL Flexible Server
- Database for ERP system
- Server configurations for logging and performance
- Firewall rules for AKS subnet access
- Key Vault secrets for connection strings

## Usage

```hcl
module "database" {
  source = "../../modules/database"

  project_name           = "erp-system"
  environment            = "dev"
  location               = "East US 2"
  resource_group_name    = "erp-system-dev-rg"
  database_subnet_id     = module.networking.database_subnet_id
  private_dns_zone_id    = module.networking.private_dns_zone_id
  key_vault_id           = module.security.key_vault_id
  tags                   = local.common_tags

  postgres_version              = "14"
  admin_username                = "erpadmin"
  admin_password                = var.database_password
  sku_name                      = "B_Standard_B1ms"
  storage_mb                    = 32768
  backup_retention_days         = 7
  enable_geo_redundant_backup   = false
  enable_high_availability      = false
  aks_subnet_cidr               = "10.0.1.0/24"
}
```

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project_name | Name of the project | `string` | n/a | yes |
| environment | Environment name | `string` | n/a | yes |
| location | Azure region | `string` | n/a | yes |
| resource_group_name | Name of the resource group | `string` | n/a | yes |
| database_subnet_id | ID of the database subnet | `string` | n/a | yes |
| private_dns_zone_id | ID of the private DNS zone | `string` | n/a | yes |
| key_vault_id | ID of the Key Vault | `string` | `null` | no |
| tags | Tags to apply to resources | `map(string)` | `{}` | no |
| postgres_version | PostgreSQL version | `string` | `"14"` | no |
| admin_username | Administrator username | `string` | `"erpadmin"` | no |
| admin_password | Administrator password | `string` | n/a | yes |
| sku_name | SKU name for the server | `string` | `"B_Standard_B1ms"` | no |
| storage_mb | Storage size in MB | `number` | `32768` | no |
| backup_retention_days | Backup retention period in days | `number` | `7` | no |
| enable_geo_redundant_backup | Enable geo-redundant backup | `bool` | `false` | no |
| enable_high_availability | Enable high availability | `bool` | `false` | no |
| availability_zone | Availability zone for the server | `string` | `"1"` | no |
| standby_availability_zone | Standby availability zone | `string` | `"2"` | no |
| enable_firewall_rules | Enable firewall rules | `bool` | `true` | no |
| aks_subnet_cidr | CIDR block of the AKS subnet | `string` | n/a | yes |

## Outputs

| Name | Description |
|------|-------------|
| server_id | ID of the PostgreSQL server |
| server_name | Name of the PostgreSQL server |
| server_fqdn | FQDN of the PostgreSQL server |
| database_name | Name of the ERP database |

## Security Features

- Private subnet deployment with DNS integration
- Firewall rules restricting access to AKS subnet
- SSL/TLS encryption enforced
- Key Vault integration for secrets management
- Audit logging enabled
- Performance monitoring configured

## High Availability

- Zone-redundant high availability option
- Automated backups with configurable retention
- Geo-redundant backup support for production
- Point-in-time recovery capabilities

## Requirements

- Terraform >= 1.0
- AzureRM provider ~> 3.0
- Private DNS zone configured
- Key Vault for secrets storage
