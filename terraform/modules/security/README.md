# Security Module

This module creates comprehensive security infrastructure for the ERP system, including Key Vault, access policies, network security rules, and Azure Policy assignments.

## Resources Created

- Azure Key Vault with network restrictions
- Access policies for AKS and admin groups
- JWT secret generation and storage
- Private endpoint for Key Vault
- Network security rules
- Azure Policy assignments for compliance

## Usage

```hcl
module "security" {
  source = "../../modules/security"

  project_name                 = "erp-system"
  environment                  = "dev"
  location                     = "East US 2"
  resource_group_name          = "erp-system-dev-rg"
  resource_group_id            = module.networking.resource_group_id
  aks_subnet_id                = module.networking.aks_subnet_id
  private_endpoint_subnet_id   = module.networking.database_subnet_id
  tags                         = local.common_tags

  key_vault_sku               = "standard"
  soft_delete_retention_days  = 7
  enable_purge_protection     = false
  admin_group_object_ids      = ["group-id-1", "group-id-2"]
  enable_policy_assignments   = false
}
```

## Security Features

### Key Vault Security
- Network access restricted to AKS subnet
- Soft delete protection enabled
- Purge protection for production environments
- Private endpoint for secure connectivity
- RBAC-based access control

### Access Control
- Managed identity integration for AKS
- Azure AD group-based admin access
- Principle of least privilege
- Separate permissions for secrets, keys, and certificates

### Network Security
- Network Security Group rules
- Private endpoint connectivity
- Deny-all default rules with explicit allows
- HTTPS-only communication enforced

### Compliance
- Azure Policy assignments for governance
- Required tags enforcement
- Location restrictions
- Resource compliance monitoring

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project_name | Name of the project | `string` | n/a | yes |
| environment | Environment name | `string` | n/a | yes |
| location | Azure region | `string` | n/a | yes |
| resource_group_name | Name of the resource group | `string` | n/a | yes |
| resource_group_id | ID of the resource group | `string` | n/a | yes |
| aks_subnet_id | ID of the AKS subnet | `string` | n/a | yes |
| private_endpoint_subnet_id | ID of the subnet for private endpoints | `string` | n/a | yes |
| aks_identity_principal_id | Principal ID of the AKS identity | `string` | `null` | no |
| network_security_group_name | Name of the NSG to add rules to | `string` | `null` | no |
| tags | Tags to apply to resources | `map(string)` | `{}` | no |
| key_vault_sku | SKU for Key Vault | `string` | `"standard"` | no |
| soft_delete_retention_days | Soft delete retention period | `number` | `90` | no |
| enable_purge_protection | Enable purge protection | `bool` | `true` | no |
| admin_group_object_ids | Object IDs of admin Azure AD groups | `list(string)` | `[]` | no |
| create_security_rules | Create additional security rules | `bool` | `false` | no |
| enable_policy_assignments | Enable Azure Policy assignments | `bool` | `true` | no |
| allowed_locations | Allowed Azure regions | `list(string)` | `["East US 2", "West US 2"]` | no |

## Outputs

| Name | Description |
|------|-------------|
| key_vault_id | ID of the Key Vault |
| key_vault_name | Name of the Key Vault |
| key_vault_uri | URI of the Key Vault |
| jwt_secret_name | Name of the JWT secret in Key Vault |

## Secrets Management

### Auto-Generated Secrets
- **jwt-secret**: Base64-encoded JWT signing key for authentication

### Integration Secrets
Other modules automatically store their connection strings and keys:
- Database connection strings
- Storage account keys
- Event Hub connection strings
- Application Insights instrumentation keys

## Access Policies

### AKS Cluster Access
- **Secrets**: Get, List (read-only access for application secrets)

### Admin Groups Access
- **Secrets**: Get, List, Set, Delete, Recover, Backup, Restore
- **Keys**: Get, List, Create, Delete, Recover, Backup, Restore, Encrypt, Decrypt, Sign, Verify
- **Certificates**: Get, List, Create, Delete, Recover, Backup, Restore, Import

### Current User/Service Principal
- **Full access** to all Key Vault operations for Terraform management

## Compliance Policies

### Required Tags Policy
Enforces the presence of required tags on all resources:
- Environment
- Project
- ManagedBy

### Allowed Locations Policy
Restricts resource deployment to approved Azure regions for data residency compliance.

## Requirements

- Terraform >= 1.0
- AzureRM provider ~> 3.0
- Azure AD groups configured for admin access
- Virtual network with subnets configured
