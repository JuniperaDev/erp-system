resource "azurerm_user_assigned_identity" "aks" {
  name                = "${var.project_name}-${var.environment}-aks-identity"
  location            = var.location
  resource_group_name = var.resource_group_name
  tags                = var.tags
}

resource "azurerm_role_assignment" "aks_network_contributor" {
  scope                = var.vnet_id
  role_definition_name = "Network Contributor"
  principal_id         = azurerm_user_assigned_identity.aks.principal_id
}

resource "azurerm_container_registry" "main" {
  name                = "${replace(var.project_name, "-", "")}${var.environment}acr"
  resource_group_name = var.resource_group_name
  location            = var.location
  sku                 = var.acr_sku
  admin_enabled       = false
  tags                = var.tags

  identity {
    type = "SystemAssigned"
  }
}

resource "azurerm_kubernetes_cluster" "main" {
  name                = "${var.project_name}-${var.environment}-aks"
  location            = var.location
  resource_group_name = var.resource_group_name
  dns_prefix          = "${var.project_name}-${var.environment}"
  kubernetes_version  = var.kubernetes_version
  tags                = var.tags

  default_node_pool {
    name                = "default"
    node_count          = var.node_count
    vm_size             = var.vm_size
    vnet_subnet_id      = var.aks_subnet_id
    enable_auto_scaling = true
    min_count           = var.min_node_count
    max_count           = var.max_node_count
    os_disk_size_gb     = 100
    type                = "VirtualMachineScaleSets"

    upgrade_settings {
      max_surge = "10%"
    }
  }

  identity {
    type         = "UserAssigned"
    identity_ids = [azurerm_user_assigned_identity.aks.id]
  }

  network_profile {
    network_plugin    = "azure"
    network_policy    = "azure"
    load_balancer_sku = "standard"
    service_cidr      = "172.16.0.0/16"
    dns_service_ip    = "172.16.0.10"
  }

  azure_active_directory_role_based_access_control {
    managed                = true
    admin_group_object_ids = var.admin_group_object_ids
    azure_rbac_enabled     = true
  }

  oms_agent {
    log_analytics_workspace_id = var.log_analytics_workspace_id
  }

  microsoft_defender {
    log_analytics_workspace_id = var.log_analytics_workspace_id
  }

  lifecycle {
    ignore_changes = [
      default_node_pool[0].node_count
    ]
  }
}

resource "azurerm_role_assignment" "aks_acr_pull" {
  scope                = azurerm_container_registry.main.id
  role_definition_name = "AcrPull"
  principal_id         = azurerm_kubernetes_cluster.main.kubelet_identity[0].object_id
}

resource "azurerm_kubernetes_cluster_node_pool" "system" {
  count                 = var.enable_system_node_pool ? 1 : 0
  name                  = "system"
  kubernetes_cluster_id = azurerm_kubernetes_cluster.main.id
  vm_size               = var.system_node_vm_size
  node_count            = var.system_node_count
  vnet_subnet_id        = var.aks_subnet_id
  enable_auto_scaling   = true
  min_count             = 1
  max_count             = 3
  os_disk_size_gb       = 100
  mode                  = "System"

  node_taints = [
    "CriticalAddonsOnly=true:NoSchedule"
  ]

  upgrade_settings {
    max_surge = "10%"
  }

  tags = var.tags
}
