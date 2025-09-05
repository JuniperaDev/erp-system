resource "azurerm_storage_account" "main" {
  name                     = "${replace(var.project_name, "-", "")}${var.environment}storage"
  resource_group_name      = var.resource_group_name
  location                 = var.location
  account_tier             = var.account_tier
  account_replication_type = var.replication_type
  account_kind             = "StorageV2"

  min_tls_version                 = "TLS1_2"
  allow_nested_items_to_be_public = false

  blob_properties {
    versioning_enabled  = var.enable_versioning
    change_feed_enabled = var.enable_change_feed
    delete_retention_policy {
      days = var.blob_retention_days
    }
    container_delete_retention_policy {
      days = var.container_retention_days
    }
  }

  network_rules {
    default_action             = "Deny"
    bypass                     = ["AzureServices"]
    virtual_network_subnet_ids = [var.aks_subnet_id]
  }

  tags = var.tags
}

resource "azurerm_storage_container" "documents" {
  name                  = "documents"
  storage_account_name  = azurerm_storage_account.main.name
  container_access_type = "private"
}

resource "azurerm_storage_container" "reports" {
  name                  = "reports"
  storage_account_name  = azurerm_storage_account.main.name
  container_access_type = "private"
}

resource "azurerm_storage_container" "backups" {
  name                  = "backups"
  storage_account_name  = azurerm_storage_account.main.name
  container_access_type = "private"
}

resource "azurerm_storage_container" "audit_logs" {
  name                  = "audit-logs"
  storage_account_name  = azurerm_storage_account.main.name
  container_access_type = "private"
}

resource "azurerm_private_endpoint" "storage" {
  name                = "${var.project_name}-${var.environment}-storage-pe"
  location            = var.location
  resource_group_name = var.resource_group_name
  subnet_id           = var.private_endpoint_subnet_id

  private_service_connection {
    name                           = "${var.project_name}-${var.environment}-storage-psc"
    private_connection_resource_id = azurerm_storage_account.main.id
    subresource_names              = ["blob"]
    is_manual_connection           = false
  }

  tags = var.tags
}

resource "azurerm_key_vault_secret" "storage_connection_string" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "storage-connection-string"
  value        = azurerm_storage_account.main.primary_connection_string
  key_vault_id = var.key_vault_id
  tags         = var.tags
}

resource "azurerm_key_vault_secret" "storage_account_name" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "storage-account-name"
  value        = azurerm_storage_account.main.name
  key_vault_id = var.key_vault_id
  tags         = var.tags
}

resource "azurerm_key_vault_secret" "storage_account_key" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "storage-account-key"
  value        = azurerm_storage_account.main.primary_access_key
  key_vault_id = var.key_vault_id
  tags         = var.tags
}

resource "azurerm_storage_management_policy" "main" {
  storage_account_id = azurerm_storage_account.main.id

  rule {
    name    = "documents_lifecycle"
    enabled = true
    filters {
      prefix_match = ["documents/"]
      blob_types   = ["blockBlob"]
    }
    actions {
      base_blob {
        tier_to_cool_after_days_since_modification_greater_than    = var.lifecycle_cool_after_days
        tier_to_archive_after_days_since_modification_greater_than = var.lifecycle_archive_after_days
        delete_after_days_since_modification_greater_than          = var.lifecycle_delete_after_days
      }
      snapshot {
        delete_after_days_since_creation_greater_than = var.lifecycle_snapshot_delete_days
      }
      version {
        delete_after_days_since_creation = var.lifecycle_version_delete_days
      }
    }
  }

  rule {
    name    = "reports_lifecycle"
    enabled = true
    filters {
      prefix_match = ["reports/"]
      blob_types   = ["blockBlob"]
    }
    actions {
      base_blob {
        tier_to_cool_after_days_since_modification_greater_than    = var.lifecycle_reports_cool_days
        tier_to_archive_after_days_since_modification_greater_than = var.lifecycle_reports_archive_days
        delete_after_days_since_modification_greater_than          = var.lifecycle_reports_delete_days
      }
    }
  }

  rule {
    name    = "audit_logs_lifecycle"
    enabled = true
    filters {
      prefix_match = ["audit-logs/"]
      blob_types   = ["blockBlob"]
    }
    actions {
      base_blob {
        tier_to_archive_after_days_since_modification_greater_than = var.lifecycle_audit_archive_days
      }
    }
  }

  rule {
    name    = "backups_lifecycle"
    enabled = true
    filters {
      prefix_match = ["backups/"]
      blob_types   = ["blockBlob"]
    }
    actions {
      base_blob {
        tier_to_cool_after_days_since_modification_greater_than    = var.lifecycle_backup_cool_days
        tier_to_archive_after_days_since_modification_greater_than = var.lifecycle_backup_archive_days
        delete_after_days_since_modification_greater_than          = var.lifecycle_backup_delete_days
      }
    }
  }
}
