# Networking Module

This module creates the core networking infrastructure for the ERP system on Azure, including Virtual Network, subnets, Network Security Groups, and Application Gateway with WAF.

## Resources Created

- Resource Group
- Virtual Network with multiple subnets
- Network Security Groups with security rules
- Private DNS Zone for PostgreSQL
- Application Gateway with Web Application Firewall (WAF)
- Public IP for Application Gateway

## Usage

```hcl
module "networking" {
  source = "../../modules/networking"

  project_name        = "erp-system"
  environment         = "dev"
  location            = "East US 2"
  resource_group_name = "erp-system-dev-rg"
  tags                = {
    Environment = "dev"
    Project     = "erp-system"
    ManagedBy   = "Terraform"
  }

  vnet_address_space             = "10.0.0.0/16"
  aks_subnet_address_prefix      = "10.0.1.0/24"
  database_subnet_address_prefix = "10.0.2.0/24"
  appgw_subnet_address_prefix    = "10.0.3.0/24"
  
  appgw_sku_name  = "WAF_v2"
  appgw_sku_tier  = "WAF_v2"
  appgw_capacity  = 2
  waf_mode        = "Prevention"
}
```

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project_name | Name of the project | `string` | n/a | yes |
| environment | Environment name | `string` | n/a | yes |
| location | Azure region | `string` | n/a | yes |
| resource_group_name | Name of the resource group | `string` | n/a | yes |
| tags | Tags to apply to resources | `map(string)` | `{}` | no |
| vnet_address_space | Address space for the virtual network | `string` | `"10.0.0.0/16"` | no |
| aks_subnet_address_prefix | Address prefix for AKS subnet | `string` | `"10.0.1.0/24"` | no |
| database_subnet_address_prefix | Address prefix for database subnet | `string` | `"10.0.2.0/24"` | no |
| appgw_subnet_address_prefix | Address prefix for Application Gateway subnet | `string` | `"10.0.3.0/24"` | no |
| appgw_sku_name | SKU name for Application Gateway | `string` | `"WAF_v2"` | no |
| appgw_sku_tier | SKU tier for Application Gateway | `string` | `"WAF_v2"` | no |
| appgw_capacity | Capacity for Application Gateway | `number` | `2` | no |
| waf_mode | WAF mode for Application Gateway | `string` | `"Prevention"` | no |

## Outputs

| Name | Description |
|------|-------------|
| resource_group_name | Name of the resource group |
| resource_group_id | ID of the resource group |
| vnet_id | ID of the virtual network |
| vnet_name | Name of the virtual network |
| aks_subnet_id | ID of the AKS subnet |
| database_subnet_id | ID of the database subnet |
| appgw_subnet_id | ID of the Application Gateway subnet |
| private_dns_zone_id | ID of the private DNS zone for PostgreSQL |
| private_dns_zone_name | Name of the private DNS zone for PostgreSQL |
| application_gateway_id | ID of the Application Gateway |
| application_gateway_public_ip | Public IP address of the Application Gateway |
| application_gateway_fqdn | FQDN of the Application Gateway |

## Security Features

- Network Security Groups with restrictive rules
- Private DNS zones for internal name resolution
- Application Gateway with WAF v2 protection
- OWASP rule set for web application security
- Subnet delegation for PostgreSQL Flexible Server

## Requirements

- Terraform >= 1.0
- AzureRM provider ~> 3.0
