terraform {
  required_version = ">= 1.0"
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "~> 3.0"
    }
    azuread = {
      source  = "hashicorp/azuread"
      version = "~> 2.0"
    }
  }
}

provider "azurerm" {
  features {
    key_vault {
      purge_soft_delete_on_destroy    = true
      recover_soft_deleted_key_vaults = true
    }
    resource_group {
      prevent_deletion_if_contains_resources = false
    }
  }
}

provider "azuread" {}

locals {
  environment = "staging"
  common_tags = {
    Environment = local.environment
    Project     = var.project_name
    ManagedBy   = "Terraform"
    CostCenter  = "Staging"
  }
}

module "monitoring" {
  source = "../../modules/monitoring"

  project_name        = var.project_name
  environment         = local.environment
  location            = var.location
  resource_group_name = "${var.project_name}-${local.environment}-rg"
  tags                = local.common_tags

  log_analytics_sku     = "PerGB2018"
  log_retention_days    = 60
  alert_email_addresses = var.alert_email_addresses
}

module "networking" {
  source = "../../modules/networking"

  project_name        = var.project_name
  environment         = local.environment
  location            = var.location
  resource_group_name = "${var.project_name}-${local.environment}-rg"
  tags                = local.common_tags

  vnet_address_space             = "10.2.0.0/16"
  aks_subnet_address_prefix      = "10.2.1.0/24"
  database_subnet_address_prefix = "10.2.2.0/24"
  appgw_subnet_address_prefix    = "10.2.3.0/24"
}

module "security" {
  source = "../../modules/security"

  project_name               = var.project_name
  environment                = local.environment
  location                   = var.location
  resource_group_name        = module.networking.resource_group_name
  resource_group_id          = module.networking.resource_group_id
  aks_subnet_id              = module.networking.aks_subnet_id
  private_endpoint_subnet_id = module.networking.database_subnet_id
  tags                       = local.common_tags

  key_vault_sku              = "standard"
  soft_delete_retention_days = 30
  enable_purge_protection    = false
  admin_group_object_ids     = var.admin_group_object_ids
  enable_policy_assignments  = true
}

module "aks" {
  source = "../../modules/aks"

  project_name               = var.project_name
  environment                = local.environment
  location                   = var.location
  resource_group_name        = module.networking.resource_group_name
  vnet_id                    = module.networking.vnet_id
  aks_subnet_id              = module.networking.aks_subnet_id
  log_analytics_workspace_id = module.monitoring.log_analytics_workspace_id
  tags                       = local.common_tags

  kubernetes_version      = "1.28"
  node_count              = 3
  min_node_count          = 2
  max_node_count          = 8
  vm_size                 = "Standard_D2s_v3"
  acr_sku                 = "Standard"
  admin_group_object_ids  = var.admin_group_object_ids
  enable_system_node_pool = false
}

module "database" {
  source = "../../modules/database"

  project_name        = var.project_name
  environment         = local.environment
  location            = var.location
  resource_group_name = module.networking.resource_group_name
  database_subnet_id  = module.networking.database_subnet_id
  private_dns_zone_id = module.networking.private_dns_zone_id
  key_vault_id        = module.security.key_vault_id
  tags                = local.common_tags

  postgres_version            = "14"
  admin_username              = "erpadmin"
  admin_password              = var.database_password
  sku_name                    = "GP_Standard_D2s_v3"
  storage_mb                  = 65536 # 64 GB
  backup_retention_days       = 14
  enable_geo_redundant_backup = false
  enable_high_availability    = true
  availability_zone           = "1"
  standby_availability_zone   = "2"
  enable_firewall_rules       = false
  aks_subnet_cidr             = "10.2.1.0/24"

  depends_on = [module.security]
}

module "messaging" {
  source = "../../modules/messaging"

  project_name        = var.project_name
  environment         = local.environment
  location            = var.location
  resource_group_name = module.networking.resource_group_name
  aks_subnet_id       = module.networking.aks_subnet_id
  key_vault_id        = module.security.key_vault_id
  tags                = local.common_tags

  sku                      = "Standard"
  capacity                 = 2
  auto_inflate_enabled     = true
  maximum_throughput_units = 10
  partition_count          = 4
  message_retention        = 7

  depends_on = [module.security]
}

module "storage" {
  source = "../../modules/storage"

  project_name               = var.project_name
  environment                = local.environment
  location                   = var.location
  resource_group_name        = module.networking.resource_group_name
  aks_subnet_id              = module.networking.aks_subnet_id
  private_endpoint_subnet_id = module.networking.database_subnet_id
  key_vault_id               = module.security.key_vault_id
  tags                       = local.common_tags

  account_tier             = "Standard"
  replication_type         = "LRS"
  enable_versioning        = true
  enable_change_feed       = true
  blob_retention_days      = 30
  container_retention_days = 30

  depends_on = [module.security]
}

module "monitoring_update" {
  source = "../../modules/monitoring"

  project_name             = var.project_name
  environment              = local.environment
  location                 = var.location
  resource_group_name      = module.networking.resource_group_name
  key_vault_id             = module.security.key_vault_id
  aks_cluster_id           = module.aks.cluster_id
  database_server_id       = module.database.server_id
  application_gateway_fqdn = module.networking.application_gateway_fqdn
  tags                     = local.common_tags

  log_analytics_sku     = "PerGB2018"
  log_retention_days    = 60
  alert_email_addresses = var.alert_email_addresses

  depends_on = [module.aks, module.database, module.security, module.networking]
}
