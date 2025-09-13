output "namespace_id" {
  description = "ID of the Event Hub namespace"
  value       = azurerm_eventhub_namespace.main.id
}

output "namespace_name" {
  description = "Name of the Event Hub namespace"
  value       = azurerm_eventhub_namespace.main.name
}

output "namespace_hostname" {
  description = "Hostname of the Event Hub namespace"
  value       = "${azurerm_eventhub_namespace.main.name}.servicebus.windows.net"
}

output "kafka_bootstrap_servers" {
  description = "Kafka bootstrap servers for Event Hub"
  value       = "${azurerm_eventhub_namespace.main.name}.servicebus.windows.net:9093"
}

output "connection_string" {
  description = "Primary connection string for Event Hub"
  value       = azurerm_eventhub_namespace_authorization_rule.microservices.primary_connection_string
  sensitive   = true
}

output "event_hubs" {
  description = "Map of Event Hub names to their IDs"
  value = {
    asset_events       = azurerm_eventhub.asset_events.name
    financial_events   = azurerm_eventhub.financial_events.name
    lease_events       = azurerm_eventhub.lease_events.name
    depreciation_batch = azurerm_eventhub.depreciation_batch.name
    wip_events         = azurerm_eventhub.wip_events.name
    reporting_events   = azurerm_eventhub.reporting_events.name
  }
}
