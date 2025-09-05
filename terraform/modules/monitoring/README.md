# Monitoring Module

This module creates comprehensive monitoring and alerting infrastructure for the ERP system using Azure Monitor, Log Analytics, and Application Insights.

## Resources Created

- Log Analytics Workspace
- Application Insights
- Monitor Action Groups for alerts
- Metric alerts for AKS and database
- Application Insights web tests for health monitoring
- Key Vault secrets for instrumentation keys

## Usage

```hcl
module "monitoring" {
  source = "../../modules/monitoring"

  project_name               = "erp-system"
  environment                = "dev"
  location                   = "East US 2"
  resource_group_name        = "erp-system-dev-rg"
  key_vault_id               = module.security.key_vault_id
  aks_cluster_id             = module.aks.cluster_id
  database_server_id         = module.database.server_id
  application_gateway_fqdn   = module.networking.application_gateway_fqdn
  tags                       = local.common_tags

  log_analytics_sku      = "PerGB2018"
  log_retention_days     = 30
  alert_email_addresses  = ["admin@company.com"]
}
```

## Monitoring Coverage

### AKS Cluster Monitoring
- CPU usage alerts (threshold: 80%)
- Memory usage alerts (threshold: 85%)
- Node health monitoring
- Container insights integration

### Database Monitoring
- CPU usage alerts (threshold: 80%)
- Connection count alerts (threshold: 80 connections)
- Query performance monitoring
- Backup status monitoring

### Application Gateway Monitoring
- Health check web tests
- Response time monitoring
- Availability monitoring from multiple regions

## Alert Severity Levels

- **Critical (Severity 1)**: Database connection issues, service unavailability
- **Warning (Severity 2)**: High resource usage, performance degradation

## Inputs

| Name | Description | Type | Default | Required |
|------|-------------|------|---------|:--------:|
| project_name | Name of the project | `string` | n/a | yes |
| environment | Environment name | `string` | n/a | yes |
| location | Azure region | `string` | n/a | yes |
| resource_group_name | Name of the resource group | `string` | n/a | yes |
| key_vault_id | ID of the Key Vault | `string` | `null` | no |
| aks_cluster_id | ID of the AKS cluster | `string` | `null` | no |
| database_server_id | ID of the database server | `string` | `null` | no |
| application_gateway_fqdn | FQDN of the application gateway | `string` | `null` | no |
| tags | Tags to apply to resources | `map(string)` | `{}` | no |
| log_analytics_sku | SKU for Log Analytics workspace | `string` | `"PerGB2018"` | no |
| log_retention_days | Log retention period in days | `number` | `30` | no |
| alert_email_addresses | Email addresses for alerts | `list(string)` | `[]` | no |

## Outputs

| Name | Description |
|------|-------------|
| log_analytics_workspace_id | ID of the Log Analytics workspace |
| log_analytics_workspace_name | Name of the Log Analytics workspace |
| application_insights_id | ID of Application Insights |
| application_insights_key | Instrumentation key for Application Insights |
| application_insights_connection_string | Connection string for Application Insights |

## Alert Configuration

### Action Groups
- **Critical**: Immediate email notifications for critical issues
- **Warning**: Email notifications for performance issues

### Metric Alerts
- AKS CPU and memory usage monitoring
- Database performance and connection monitoring
- Automated scaling recommendations

### Web Tests
- Health endpoint monitoring from multiple Azure regions
- Response time and availability tracking
- Synthetic transaction monitoring

## Integration Features

- Container insights for AKS monitoring
- Application performance monitoring (APM)
- Custom metrics and dashboards
- Log aggregation and analysis
- Automated alerting and notifications

## Requirements

- Terraform >= 1.0
- AzureRM provider ~> 3.0
- Email addresses configured for alert notifications
- Application Gateway with health endpoints
