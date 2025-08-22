#!/bin/bash

set -e

echo "Deploying ERP System Microservices to Kubernetes..."

echo "Applying ConfigMaps and Secrets..."
kubectl apply -f k8s/configmap.yaml

echo "Deploying JHipster Registry..."
kubectl apply -f k8s/jhipster-registry-deployment.yaml

echo "Waiting for JHipster Registry to be ready..."
kubectl wait --for=condition=available --timeout=300s deployment/jhipster-registry

echo "Deploying Asset Management Service..."
kubectl apply -f k8s/asset-management-service-deployment.yaml

echo "Deploying Financial Core Service..."
kubectl apply -f k8s/financial-core-service-deployment.yaml

echo "Deploying Lease Service..."
kubectl apply -f k8s/lease-service-deployment.yaml

echo "Deploying Depreciation Service..."
kubectl apply -f k8s/depreciation-service-deployment.yaml

echo "Deploying WIP Service..."
kubectl apply -f k8s/wip-service-deployment.yaml

echo "Deploying Reporting Service..."
kubectl apply -f k8s/reporting-service-deployment.yaml

echo "Applying Horizontal Pod Autoscalers..."
kubectl apply -f k8s/hpa.yaml

echo "Deployment completed successfully!"
echo "Use 'kubectl get pods' to check the status of your deployments"
echo "Use 'kubectl get services' to see the available services"
