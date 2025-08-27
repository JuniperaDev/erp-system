data "azurerm_client_config" "current" {}

resource "azurerm_key_vault" "main" {
  name                       = "${var.project_name}-${var.environment}-kv"
  location                   = var.location
  resource_group_name        = var.resource_group_name
  tenant_id                  = data.azurerm_client_config.current.tenant_id
  sku_name                   = var.key_vault_sku
  soft_delete_retention_days = var.soft_delete_retention_days
  purge_protection_enabled   = var.enable_purge_protection

  network_acls {
    default_action             = "Deny"
    bypass                     = "AzureServices"
    virtual_network_subnet_ids = [var.aks_subnet_id]
  }

  tags = var.tags
}

resource "azurerm_key_vault_access_policy" "aks" {
  count        = var.aks_identity_principal_id != null ? 1 : 0
  key_vault_id = azurerm_key_vault.main.id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = var.aks_identity_principal_id

  secret_permissions = [
    "Get",
    "List"
  ]
}

resource "azurerm_key_vault_access_policy" "current" {
  key_vault_id = azurerm_key_vault.main.id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = data.azurerm_client_config.current.object_id

  secret_permissions = [
    "Get",
    "List",
    "Set",
    "Delete",
    "Recover",
    "Backup",
    "Restore",
    "Purge"
  ]

  key_permissions = [
    "Get",
    "List",
    "Create",
    "Delete",
    "Recover",
    "Backup",
    "Restore",
    "Purge",
    "Encrypt",
    "Decrypt",
    "Sign",
    "Verify"
  ]

  certificate_permissions = [
    "Get",
    "List",
    "Create",
    "Delete",
    "Recover",
    "Backup",
    "Restore",
    "Purge",
    "Import"
  ]
}

resource "azurerm_key_vault_access_policy" "admin_groups" {
  for_each     = toset(var.admin_group_object_ids)
  key_vault_id = azurerm_key_vault.main.id
  tenant_id    = data.azurerm_client_config.current.tenant_id
  object_id    = each.value

  secret_permissions = [
    "Get",
    "List",
    "Set",
    "Delete",
    "Recover",
    "Backup",
    "Restore"
  ]

  key_permissions = [
    "Get",
    "List",
    "Create",
    "Delete",
    "Recover",
    "Backup",
    "Restore",
    "Encrypt",
    "Decrypt",
    "Sign",
    "Verify"
  ]
}

resource "random_password" "jwt_secret" {
  length  = 64
  special = true
}

resource "azurerm_key_vault_secret" "jwt_secret" {
  name         = "jwt-secret"
  value        = base64encode(random_password.jwt_secret.result)
  key_vault_id = azurerm_key_vault.main.id
  tags         = var.tags

  depends_on = [azurerm_key_vault_access_policy.current]
}

resource "azurerm_private_endpoint" "key_vault" {
  name                = "${var.project_name}-${var.environment}-kv-pe"
  location            = var.location
  resource_group_name = var.resource_group_name
  subnet_id           = var.private_endpoint_subnet_id

  private_service_connection {
    name                           = "${var.project_name}-${var.environment}-kv-psc"
    private_connection_resource_id = azurerm_key_vault.main.id
    subresource_names              = ["vault"]
    is_manual_connection           = false
  }

  tags = var.tags
}

resource "azurerm_network_security_rule" "deny_all_inbound" {
  count                       = var.create_security_rules ? 1 : 0
  name                        = "DenyAllInbound"
  priority                    = 4096
  direction                   = "Inbound"
  access                      = "Deny"
  protocol                    = "*"
  source_port_range           = "*"
  destination_port_range      = "*"
  source_address_prefix       = "*"
  destination_address_prefix  = "*"
  resource_group_name         = var.resource_group_name
  network_security_group_name = var.network_security_group_name
}

resource "azurerm_network_security_rule" "allow_https_inbound" {
  count                       = var.create_security_rules ? 1 : 0
  name                        = "AllowHTTPSInbound"
  priority                    = 1000
  direction                   = "Inbound"
  access                      = "Allow"
  protocol                    = "Tcp"
  source_port_range           = "*"
  destination_port_range      = "443"
  source_address_prefix       = "*"
  destination_address_prefix  = "*"
  resource_group_name         = var.resource_group_name
  network_security_group_name = var.network_security_group_name
}

resource "azurerm_resource_group_policy_assignment" "require_tags" {
  count                = var.enable_policy_assignments ? 1 : 0
  name                 = "require-tags"
  resource_group_id    = var.resource_group_id
  policy_definition_id = "/providers/Microsoft.Authorization/policyDefinitions/1e30110a-5ceb-460c-a204-c1c3969c6d62"
  description          = "Require specified tags on resources"

  parameters = jsonencode({
    tagNames = {
      value = ["Environment", "Project", "ManagedBy"]
    }
  })
}

resource "azurerm_resource_group_policy_assignment" "allowed_locations" {
  count                = var.enable_policy_assignments ? 1 : 0
  name                 = "allowed-locations"
  resource_group_id    = var.resource_group_id
  policy_definition_id = "/providers/Microsoft.Authorization/policyDefinitions/e56962a6-4747-49cd-b67b-bf8b01975c4c"
  description          = "Restrict resource locations"

  parameters = jsonencode({
    listOfAllowedLocations = {
      value = var.allowed_locations
    }
  })
}
