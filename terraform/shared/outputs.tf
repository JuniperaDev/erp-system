output "resource_group_name" {
  description = "Name of the resource group"
  value       = var.resource_group_name
}

output "location" {
  description = "Azure region"
  value       = var.location
}

output "environment" {
  description = "Environment name"
  value       = var.environment
}

output "project_name" {
  description = "Project name"
  value       = var.project_name
}

output "common_tags" {
  description = "Common tags applied to resources"
  value       = merge(var.tags, { Environment = var.environment })
}
