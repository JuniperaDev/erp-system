output "server_id" {
  description = "ID of the PostgreSQL server"
  value       = azurerm_postgresql_flexible_server.main.id
}

output "server_name" {
  description = "Name of the PostgreSQL server"
  value       = azurerm_postgresql_flexible_server.main.name
}

output "server_fqdn" {
  description = "FQDN of the PostgreSQL server"
  value       = azurerm_postgresql_flexible_server.main.fqdn
}

output "database_name" {
  description = "Name of the ERP database"
  value       = azurerm_postgresql_flexible_server_database.erp_system.name
}

output "connection_string" {
  description = "JDBC connection string for the database"
  value       = "jdbc:postgresql://${azurerm_postgresql_flexible_server.main.fqdn}:5432/${azurerm_postgresql_flexible_server_database.erp_system.name}?sslmode=require"
  sensitive   = true
}

output "admin_username" {
  description = "Administrator username"
  value       = azurerm_postgresql_flexible_server.main.administrator_login
}
