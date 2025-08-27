output "key_vault_id" {
  description = "ID of the Key Vault"
  value       = azurerm_key_vault.main.id
}

output "key_vault_name" {
  description = "Name of the Key Vault"
  value       = azurerm_key_vault.main.name
}

output "key_vault_uri" {
  description = "URI of the Key Vault"
  value       = azurerm_key_vault.main.vault_uri
}

output "jwt_secret_name" {
  description = "Name of the JWT secret in Key Vault"
  value       = azurerm_key_vault_secret.jwt_secret.name
}

output "private_endpoint_id" {
  description = "ID of the Key Vault private endpoint"
  value       = azurerm_private_endpoint.key_vault.id
}
