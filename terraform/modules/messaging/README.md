# Messaging Module

This module creates Azure Event Hubs namespace and event hubs for inter-service communication in the ERP system, with consumer groups for each microservice.

## Resources Created

- Event Hubs Namespace
- Event Hubs for each service domain
- Consumer groups for microservice subscriptions
- Authorization rules for access control
- Key Vault secrets for connection strings

## Usage

```hcl
module "messaging" {
  source = "../../modules/messaging"

  project_name        = "erp-system"
  environment         = "dev"
  location            = "East US 2"
  resource_group_name = "erp-system-dev-rg"
  aks_subnet_id       = module.networking.aks_subnet_id
  key_vault_id        = module.security.key_vault_id
  tags                = local.common_tags

  sku                        = "Basic"
  capacity                   = 1
  auto_inflate_enabled       = false
  maximum_throughput_units   = 1
  partition_count            = 2
  message_retention          = 1
}
```

## Event Hubs Created

- `asset-events-topic` - Asset management events
- `financial-events-topic` - Financial transaction events
- `lease-events-topic` - Lease management events
- `depreciation-batch-topic` - Batch depreciation processing
- `wip-events-topic` - Work-in-progress events
- `reporting-events-topic` - Reporting and analytics events

## Consumer Groups

Each microservice has dedicated consumer groups for the event hubs they subscribe to:

- **asset-service**: asset-events, financial-events, depreciation-batch
- **financial-service**: asset-events, financial-events, lease-events
- **lease-service**: lease-events, financial-events, asset-events
- **depreciation-service**: depreciation-batch, asset-events
- **wip-service**: wip-events, asset-events, financial-events
- **reporting-service**: All event hubs for comprehensive reporting

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project_name | Name of the project | `string` | n/a | yes |
| environment | Environment name | `string` | n/a | yes |
| location | Azure region | `string` | n/a | yes |
| resource_group_name | Name of the resource group | `string` | n/a | yes |
| aks_subnet_id | ID of the AKS subnet | `string` | n/a | yes |
| key_vault_id | ID of the Key Vault | `string` | `null` | no |
| tags | Tags to apply to resources | `map(string)` | `{}` | no |
| sku | Event Hubs namespace SKU | `string` | `"Standard"` | no |
| capacity | Event Hubs namespace capacity | `number` | `1` | no |
| auto_inflate_enabled | Enable auto-inflate | `bool` | `false` | no |
| maximum_throughput_units | Maximum throughput units | `number` | `20` | no |
| partition_count | Number of partitions per event hub | `number` | `4` | no |
| message_retention | Message retention in days | `number` | `7` | no |

## Outputs

| Name | Description |
|------|-------------|
| namespace_id | ID of the Event Hubs namespace |
| namespace_name | Name of the Event Hubs namespace |
| connection_string | Primary connection string |
| kafka_endpoint | Kafka-compatible endpoint |

## Security Features

- Network rules restricting access to AKS subnet
- Trusted service access enabled
- Authorization rules with minimal permissions
- Key Vault integration for connection strings
- Kafka protocol support for Spring Boot integration

## Requirements

- Terraform >= 1.0
- AzureRM provider ~> 3.0
- Virtual network with AKS subnet
- Key Vault for secrets storage
