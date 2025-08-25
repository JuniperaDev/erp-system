#!/bin/bash

set -e

echo "Building Docker images for ERP System Microservices..."

./scripts/build-microservices.sh

echo "Building Asset Management Service Docker image..."
docker build -f src/main/docker/microservices/Dockerfile.asset-management -t ghacupha/erp-asset-management-service:latest .

echo "Building Financial Core Service Docker image..."
docker build -f src/main/docker/microservices/Dockerfile.financial-core -t ghacupha/erp-financial-core-service:latest .

echo "Building Lease Service Docker image..."
docker build -f src/main/docker/microservices/Dockerfile.lease-service -t ghacupha/erp-lease-service:latest .

echo "Building Depreciation Service Docker image..."
docker build -f src/main/docker/microservices/Dockerfile.depreciation-service -t ghacupha/erp-depreciation-service:latest .

echo "Building WIP Service Docker image..."
docker build -f src/main/docker/microservices/Dockerfile.wip-service -t ghacupha/erp-wip-service:latest .

echo "Building Reporting Service Docker image..."
docker build -f src/main/docker/microservices/Dockerfile.reporting-service -t ghacupha/erp-reporting-service:latest .

echo "All Docker images built successfully!"
echo "Use 'docker images | grep ghacupha/erp-' to see the built images"
