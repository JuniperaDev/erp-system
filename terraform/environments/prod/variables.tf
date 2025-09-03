variable "project_name" {
  description = "Name of the project"
  type        = string
  default     = "erp-system"
}

variable "location" {
  description = "Azure region for resources"
  type        = string
  default     = "East US 2"
}

variable "database_password" {
  description = "Password for the PostgreSQL database administrator"
  type        = string
  sensitive   = true
}

variable "admin_group_object_ids" {
  description = "Object IDs of Azure AD groups that should have admin access"
  type        = list(string)
  default     = []
}

variable "alert_email_addresses" {
  description = "List of email addresses for monitoring alerts"
  type        = list(string)
  default     = []
}
