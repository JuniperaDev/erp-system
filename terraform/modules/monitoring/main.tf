resource "azurerm_log_analytics_workspace" "main" {
  name                = "${var.project_name}-${var.environment}-logs"
  location            = var.location
  resource_group_name = var.resource_group_name
  sku                 = var.log_analytics_sku
  retention_in_days   = var.log_retention_days
  tags                = var.tags
}

resource "azurerm_application_insights" "main" {
  name                = "${var.project_name}-${var.environment}-insights"
  location            = var.location
  resource_group_name = var.resource_group_name
  workspace_id        = azurerm_log_analytics_workspace.main.id
  application_type    = "web"
  tags                = var.tags
}

resource "azurerm_monitor_action_group" "critical" {
  name                = "${var.project_name}-${var.environment}-critical-alerts"
  resource_group_name = var.resource_group_name
  short_name          = "critical"
  tags                = var.tags

  dynamic "email_receiver" {
    for_each = var.alert_email_addresses
    content {
      name          = "email-${email_receiver.key}"
      email_address = email_receiver.value
    }
  }
}

resource "azurerm_monitor_action_group" "warning" {
  name                = "${var.project_name}-${var.environment}-warning-alerts"
  resource_group_name = var.resource_group_name
  short_name          = "warning"
  tags                = var.tags

  dynamic "email_receiver" {
    for_each = var.alert_email_addresses
    content {
      name          = "email-${email_receiver.key}"
      email_address = email_receiver.value
    }
  }
}

resource "azurerm_monitor_metric_alert" "aks_cpu_high" {
  count               = var.aks_cluster_id != null ? 1 : 0
  name                = "${var.project_name}-${var.environment}-aks-cpu-high"
  resource_group_name = var.resource_group_name
  scopes              = [var.aks_cluster_id]
  description         = "AKS cluster CPU usage is high"
  severity            = 2
  frequency           = "PT1M"
  window_size         = "PT5M"
  tags                = var.tags

  criteria {
    metric_namespace = "Microsoft.ContainerService/managedClusters"
    metric_name      = "node_cpu_usage_percentage"
    aggregation      = "Average"
    operator         = "GreaterThan"
    threshold        = 80
  }

  action {
    action_group_id = azurerm_monitor_action_group.warning.id
  }
}

resource "azurerm_monitor_metric_alert" "aks_memory_high" {
  count               = var.aks_cluster_id != null ? 1 : 0
  name                = "${var.project_name}-${var.environment}-aks-memory-high"
  resource_group_name = var.resource_group_name
  scopes              = [var.aks_cluster_id]
  description         = "AKS cluster memory usage is high"
  severity            = 2
  frequency           = "PT1M"
  window_size         = "PT5M"
  tags                = var.tags

  criteria {
    metric_namespace = "Microsoft.ContainerService/managedClusters"
    metric_name      = "node_memory_working_set_percentage"
    aggregation      = "Average"
    operator         = "GreaterThan"
    threshold        = 85
  }

  action {
    action_group_id = azurerm_monitor_action_group.warning.id
  }
}

resource "azurerm_monitor_metric_alert" "database_cpu_high" {
  count               = var.database_server_id != null ? 1 : 0
  name                = "${var.project_name}-${var.environment}-db-cpu-high"
  resource_group_name = var.resource_group_name
  scopes              = [var.database_server_id]
  description         = "Database CPU usage is high"
  severity            = 2
  frequency           = "PT1M"
  window_size         = "PT5M"
  tags                = var.tags

  criteria {
    metric_namespace = "Microsoft.DBforPostgreSQL/flexibleServers"
    metric_name      = "cpu_percent"
    aggregation      = "Average"
    operator         = "GreaterThan"
    threshold        = 80
  }

  action {
    action_group_id = azurerm_monitor_action_group.warning.id
  }
}

resource "azurerm_monitor_metric_alert" "database_connections_high" {
  count               = var.database_server_id != null ? 1 : 0
  name                = "${var.project_name}-${var.environment}-db-connections-high"
  resource_group_name = var.resource_group_name
  scopes              = [var.database_server_id]
  description         = "Database connection count is high"
  severity            = 1
  frequency           = "PT1M"
  window_size         = "PT5M"
  tags                = var.tags

  criteria {
    metric_namespace = "Microsoft.DBforPostgreSQL/flexibleServers"
    metric_name      = "active_connections"
    aggregation      = "Average"
    operator         = "GreaterThan"
    threshold        = 80
  }

  action {
    action_group_id = azurerm_monitor_action_group.critical.id
  }
}

resource "azurerm_application_insights_web_test" "health_check" {
  count                   = var.application_gateway_fqdn != null ? 1 : 0
  name                    = "${var.project_name}-${var.environment}-health-check"
  location                = var.location
  resource_group_name     = var.resource_group_name
  application_insights_id = azurerm_application_insights.main.id
  kind                    = "ping"
  frequency               = 300
  timeout                 = 30
  enabled                 = true
  geo_locations           = ["us-tx-sn1-azr", "us-il-ch1-azr"]
  tags                    = var.tags

  configuration = <<XML
<WebTest Name="${var.project_name}-${var.environment}-health-check" Id="ABD48585-0831-40CB-9069-682A25A54A9B" Enabled="True" CssProjectStructure="" CssIteration="" Timeout="30" WorkItemIds="" xmlns="http://microsoft.com/schemas/VisualStudio/TeamTest/2010" Description="" CredentialUserName="" CredentialPassword="" PreAuthenticate="True" Proxy="default" StopOnError="False" RecordedResultFile="" ResultsLocale="">
  <Items>
    <Request Method="GET" Guid="a5f10126-e4cd-570d-961c-cea43999a200" Version="1.1" Url="https://${var.application_gateway_fqdn}/actuator/health" ThinkTime="0" Timeout="30" ParseDependentRequests="False" FollowRedirects="True" RecordResult="True" Cache="False" ResponseTimeGoal="0" Encoding="utf-8" ExpectedHttpStatusCode="200" ExpectedResponseUrl="" ReportingName="" IgnoreHttpStatusCode="False" />
  </Items>
</WebTest>
XML
}

resource "azurerm_key_vault_secret" "application_insights_key" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "application-insights-instrumentation-key"
  value        = azurerm_application_insights.main.instrumentation_key
  key_vault_id = var.key_vault_id
  tags         = var.tags
}

resource "azurerm_key_vault_secret" "application_insights_connection_string" {
  count        = var.key_vault_id != null ? 1 : 0
  name         = "application-insights-connection-string"
  value        = azurerm_application_insights.main.connection_string
  key_vault_id = var.key_vault_id
  tags         = var.tags
}
