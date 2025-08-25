#!/bin/bash

set -e

echo "Starting ERP System Microservices locally with Docker Compose..."

./scripts/build-docker-images.sh

echo "Starting services with Docker Compose..."
cd src/main/docker/microservices
docker-compose up -d

echo "Microservices started successfully!"
echo "Services are available at:"
echo "- JHipster Registry: http://localhost:8761"
echo "- Asset Management Service: http://localhost:8082"
echo "- Financial Core Service: http://localhost:8083"
echo "- Lease Service: http://localhost:8084"
echo "- Depreciation Service: http://localhost:8085"
echo "- WIP Service: http://localhost:8086"
echo "- Reporting Service: http://localhost:8087"
echo ""
echo "Use 'docker-compose logs -f' to view logs"
echo "Use 'docker-compose down' to stop all services"
