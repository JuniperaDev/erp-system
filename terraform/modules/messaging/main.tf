resource "azurerm_eventhub_namespace" "main" {
  name                = "${var.project_name}-${var.environment}-eventhub"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = var.sku
  capacity            = var.capacity
  
  auto_inflate_enabled     = var.auto_inflate_enabled
  maximum_throughput_units = var.auto_inflate_enabled ? var.maximum_throughput_units : null

  network_rulesets {
    default_action                 = "Deny"
    trusted_service_access_enabled = true

    virtual_network_rule {
      subnet_id                                       = var.aks_subnet_id
      ignore_missing_virtual_network_service_endpoint = false
    }
  }

  tags = var.tags
}

resource "azurerm_eventhub" "asset_events" {
  name                = "asset-events-topic"
  namespace_name      = azurerm_eventhub_namespace.main.name
  resource_group_name = var.resource_group_name
  partition_count     = var.partition_count
  message_retention   = var.message_retention
}

resource "azurerm_eventhub" "financial_events" {
  name                = "financial-events-topic"
  namespace_name      = azurerm_eventhub_namespace.main.name
  resource_group_name = var.resource_group_name
  partition_count     = var.partition_count
  message_retention   = var.message_retention
}

resource "azurerm_eventhub" "lease_events" {
  name                = "lease-events-topic"
  namespace_name      = azurerm_eventhub_namespace.main.name
  resource_group_name = var.resource_group_name
  partition_count     = var.partition_count
  message_retention   = var.message_retention
}

resource "azurerm_eventhub" "depreciation_batch" {
  name                = "depreciation-batch-topic"
  namespace_name      = azurerm_eventhub_namespace.main.name
  resource_group_name = var.resource_group_name
  partition_count     = var.partition_count
  message_retention   = var.message_retention
}

resource "azurerm_eventhub" "wip_events" {
  name                = "wip-events-topic"
  namespace_name      = azurerm_eventhub_namespace.main.name
  resource_group_name = var.resource_group_name
  partition_count     = var.partition_count
  message_retention   = var.message_retention
}

resource "azurerm_eventhub" "reporting_events" {
  name                = "reporting-events-topic"
  namespace_name      = azurerm_eventhub_namespace.main.name
  resource_group_name = var.resource_group_name
  partition_count     = var.partition_count
  message_retention   = var.message_retention
}

resource "azurerm_eventhub_consumer_group" "asset_service" {
  for_each = toset([
    "asset-events-topic",
    "financial-events-topic",
    "depreciation-batch-topic"
  ])
  
  name                = "asset-service"
  namespace_name      = azurerm_eventhub_namespace.main.name
  eventhub_name       = each.value
  resource_group_name = var.resource_group_name
}

resource "azurerm_eventhub_consumer_group" "financial_service" {
  for_each = toset([
    "asset-events-topic",
    "financial-events-topic",
    "lease-events-topic"
  ])
  
  name                = "financial-service"
  namespace_name      = azurerm_eventhub_namespace.main.name
  eventhub_name       = each.value
  resource_group_name = var.resource_group_name
}

resource "azurerm_eventhub_consumer_group" "lease_service" {
  for_each = toset([
    "lease-events-topic",
    "financial-events-topic",
    "asset-events-topic"
  ])
  
  name                = "lease-service"
  namespace_name      = azurerm_eventhub_namespace.main.name
  eventhub_name       = each.value
  resource_group_name = var.resource_group_name
}

resource "azurerm_eventhub_consumer_group" "depreciation_service" {
  for_each = toset([
    "depreciation-batch-topic",
    "asset-events-topic"
  ])
  
  name                = "depreciation-service"
  namespace_name      = azurerm_eventhub_namespace.main.name
  eventhub_name       = each.value
  resource_group_name = var.resource_group_name
}

resource "azurerm_eventhub_consumer_group" "wip_service" {
  for_each = toset([
    "wip-events-topic",
    "asset-events-topic",
    "financial-events-topic"
  ])
  
  name                = "wip-service"
  namespace_name      = azurerm_eventhub_namespace.main.name
  eventhub_name       = each.value
  resource_group_name = var.resource_group_name
}

resource "azurerm_eventhub_consumer_group" "reporting_service" {
  for_each = toset([
    "reporting-events-topic",
    "asset-events-topic",
    "financial-events-topic",
    "lease-events-topic",
    "depreciation-batch-topic",
    "wip-events-topic"
  ])
  
  name                = "reporting-service"
  namespace_name      = azurerm_eventhub_namespace.main.name
  eventhub_name       = each.value
  resource_group_name = var.resource_group_name
}

resource "azurerm_eventhub_namespace_authorization_rule" "microservices" {
  name                = "microservices-access"
  namespace_name      = azurerm_eventhub_namespace.main.name
  resource_group_name = var.resource_group_name
  listen              = true
  send                = true
  manage              = false
}

resource "azurerm_key_vault_secret" "eventhub_connection_string" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "eventhub-connection-string"
  value        = azurerm_eventhub_namespace_authorization_rule.microservices.primary_connection_string
  key_vault_id = var.key_vault_id
  tags         = var.tags
}

resource "azurerm_key_vault_secret" "kafka_bootstrap_servers" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "kafka-bootstrap-servers"
  value        = "${azurerm_eventhub_namespace.main.name}.servicebus.windows.net:9093"
  key_vault_id = var.key_vault_id
  tags         = var.tags
}
