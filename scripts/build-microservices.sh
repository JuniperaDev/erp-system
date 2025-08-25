#!/bin/bash

set -e

echo "Building ERP System Microservices..."

echo "Building main application..."
./mvnw clean package -DskipTests

mkdir -p target/microservices

echo "Building Asset Management Service..."
./mvnw clean package -DskipTests -Dspring.profiles.active=asset-management
cp target/*.jar target/asset-management-service-1.0.0.jar

echo "Building Financial Core Service..."
./mvnw clean package -DskipTests -Dspring.profiles.active=financial-core
cp target/*.jar target/financial-core-service-1.0.0.jar

echo "Building Lease Service..."
./mvnw clean package -DskipTests -Dspring.profiles.active=lease-service
cp target/*.jar target/lease-service-1.0.0.jar

echo "Building Depreciation Service..."
./mvnw clean package -DskipTests -Dspring.profiles.active=depreciation-service
cp target/*.jar target/depreciation-service-1.0.0.jar

echo "Building WIP Service..."
./mvnw clean package -DskipTests -Dspring.profiles.active=wip-service
cp target/*.jar target/wip-service-1.0.0.jar

echo "Building Reporting Service..."
./mvnw clean package -DskipTests -Dspring.profiles.active=reporting-service
cp target/*.jar target/reporting-service-1.0.0.jar

echo "All microservices built successfully!"
echo "JAR files are available in the target/ directory"
