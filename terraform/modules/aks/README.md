# AKS Module

This module creates an Azure Kubernetes Service (AKS) cluster with Azure Container Registry (ACR) and proper security configurations for the ERP system.

## Resources Created

- AKS Cluster with managed identity
- Azure Container Registry (ACR)
- User-assigned identity for AKS
- Role assignments for network and ACR access
- Optional system node pool for critical workloads

## Usage

```hcl
module "aks" {
  source = "../../modules/aks"

  project_name               = "erp-system"
  environment                = "dev"
  location                   = "East US 2"
  resource_group_name        = "erp-system-dev-rg"
  vnet_id                    = module.networking.vnet_id
  aks_subnet_id              = module.networking.aks_subnet_id
  log_analytics_workspace_id = module.monitoring.log_analytics_workspace_id
  tags                       = local.common_tags

  kubernetes_version      = "1.28"
  node_count              = 2
  min_node_count          = 1
  max_node_count          = 5
  vm_size                 = "Standard_D2s_v3"
  acr_sku                 = "Basic"
  admin_group_object_ids  = var.admin_group_object_ids
  enable_system_node_pool = false
}
```

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project_name | Name of the project | `string` | n/a | yes |
| environment | Environment name | `string` | n/a | yes |
| location | Azure region | `string` | n/a | yes |
| resource_group_name | Name of the resource group | `string` | n/a | yes |
| vnet_id | ID of the virtual network | `string` | n/a | yes |
| aks_subnet_id | ID of the AKS subnet | `string` | n/a | yes |
| log_analytics_workspace_id | ID of the Log Analytics workspace | `string` | n/a | yes |
| tags | Tags to apply to resources | `map(string)` | `{}` | no |
| kubernetes_version | Kubernetes version | `string` | `"1.28"` | no |
| node_count | Number of nodes in the default node pool | `number` | `3` | no |
| min_node_count | Minimum number of nodes | `number` | `1` | no |
| max_node_count | Maximum number of nodes | `number` | `10` | no |
| vm_size | VM size for nodes | `string` | `"Standard_D2s_v3"` | no |
| acr_sku | SKU for Azure Container Registry | `string` | `"Standard"` | no |
| admin_group_object_ids | Object IDs of Azure AD groups for admin access | `list(string)` | `[]` | no |
| enable_system_node_pool | Enable dedicated system node pool | `bool` | `false` | no |
| system_node_vm_size | VM size for system nodes | `string` | `"Standard_D2s_v3"` | no |
| system_node_count | Number of system nodes | `number` | `2` | no |

## Outputs

| Name | Description |
|------|-------------|
| cluster_id | ID of the AKS cluster |
| cluster_name | Name of the AKS cluster |
| cluster_fqdn | FQDN of the AKS cluster |
| acr_id | ID of the Azure Container Registry |
| acr_login_server | Login server URL of the ACR |
| identity_principal_id | Principal ID of the AKS managed identity |

## Security Features

- Azure AD integration with RBAC
- Network policies enabled
- Microsoft Defender for Containers
- Log Analytics integration for monitoring
- Managed identity for secure access
- Private container registry integration

## Requirements

- Terraform >= 1.0
- AzureRM provider ~> 3.0
- Azure AD groups configured for admin access
