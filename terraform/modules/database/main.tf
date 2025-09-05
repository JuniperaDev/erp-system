resource "azurerm_postgresql_flexible_server" "main" {
  name                   = "${var.project_name}-${var.environment}-postgres"
  resource_group_name    = var.resource_group_name
  location               = var.location
  version                = var.postgres_version
  delegated_subnet_id    = var.database_subnet_id
  private_dns_zone_id    = var.private_dns_zone_id
  administrator_login    = var.admin_username
  administrator_password = var.admin_password
  zone                   = var.availability_zone

  storage_mb                   = var.storage_mb
  sku_name                     = var.sku_name
  backup_retention_days        = var.backup_retention_days
  geo_redundant_backup_enabled = var.enable_geo_redundant_backup

  high_availability {
    mode                      = var.enable_high_availability ? "ZoneRedundant" : "Disabled"
    standby_availability_zone = var.enable_high_availability ? var.standby_availability_zone : null
  }

  maintenance_window {
    day_of_week  = var.maintenance_window.day_of_week
    start_hour   = var.maintenance_window.start_hour
    start_minute = var.maintenance_window.start_minute
  }

  tags = var.tags

  depends_on = [var.private_dns_zone_id]
}

resource "azurerm_postgresql_flexible_server_database" "erp_system" {
  name      = "erpSystem"
  server_id = azurerm_postgresql_flexible_server.main.id
  collation = "en_US.utf8"
  charset   = "utf8"
}

resource "azurerm_postgresql_flexible_server_configuration" "log_statement" {
  name      = "log_statement"
  server_id = azurerm_postgresql_flexible_server.main.id
  value     = "all"
}

resource "azurerm_postgresql_flexible_server_configuration" "log_min_duration_statement" {
  name      = "log_min_duration_statement"
  server_id = azurerm_postgresql_flexible_server.main.id
  value     = "1000"
}

resource "azurerm_postgresql_flexible_server_configuration" "shared_preload_libraries" {
  name      = "shared_preload_libraries"
  server_id = azurerm_postgresql_flexible_server.main.id
  value     = "pg_stat_statements"
}

resource "azurerm_postgresql_flexible_server_firewall_rule" "aks_subnet" {
  count            = var.enable_firewall_rules ? 1 : 0
  name             = "AllowAKSSubnet"
  server_id        = azurerm_postgresql_flexible_server.main.id
  start_ip_address = cidrhost(var.aks_subnet_cidr, 0)
  end_ip_address   = cidrhost(var.aks_subnet_cidr, -1)
}

resource "azurerm_key_vault_secret" "database_url" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "database-url"
  value        = "jdbc:postgresql://${azurerm_postgresql_flexible_server.main.fqdn}:5432/${azurerm_postgresql_flexible_server_database.erp_system.name}?sslmode=require"
  key_vault_id = var.key_vault_id
  tags         = var.tags
}

resource "azurerm_key_vault_secret" "database_password" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "database-password"
  value        = var.admin_password
  key_vault_id = var.key_vault_id
  tags         = var.tags
}
