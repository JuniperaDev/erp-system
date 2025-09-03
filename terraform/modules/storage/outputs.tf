output "storage_account_id" {
  description = "ID of the storage account"
  value       = azurerm_storage_account.main.id
}

output "storage_account_name" {
  description = "Name of the storage account"
  value       = azurerm_storage_account.main.name
}

output "storage_account_primary_endpoint" {
  description = "Primary blob endpoint of the storage account"
  value       = azurerm_storage_account.main.primary_blob_endpoint
}

output "storage_containers" {
  description = "Map of container names to their URLs"
  value = {
    documents  = "${azurerm_storage_account.main.primary_blob_endpoint}${azurerm_storage_container.documents.name}"
    reports    = "${azurerm_storage_account.main.primary_blob_endpoint}${azurerm_storage_container.reports.name}"
    backups    = "${azurerm_storage_account.main.primary_blob_endpoint}${azurerm_storage_container.backups.name}"
    audit_logs = "${azurerm_storage_account.main.primary_blob_endpoint}${azurerm_storage_container.audit_logs.name}"
  }
}

output "private_endpoint_id" {
  description = "ID of the storage private endpoint"
  value       = azurerm_private_endpoint.storage.id
}
