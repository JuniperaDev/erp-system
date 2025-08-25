# ERP System Microservices Deployment Guide

## Overview

This document provides comprehensive guidance for deploying the ERP System as microservices following the bounded context architecture defined in Story D4.5. The system has been decomposed into 6 distinct microservices based on Domain-Driven Design principles.

## Microservices Architecture

### Bounded Contexts

1. **Asset Management Service** (Port: 8082)
   - Handles asset registration, categorization, and lifecycle management
   - Package: `io.github.erp.context.assets`, `io.github.erp.cqrs.asset`, `io.github.erp.erp.assets`

2. **Financial Core Service** (Port: 8083)
   - Manages settlements, payments, vendors, and core financial operations
   - Package: `io.github.erp.financial`, `io.github.erp.cqrs.financial`

3. **Lease Service** (Port: 8084)
   - IFRS16 lease accounting and compliance
   - Package: `io.github.erp.cqrs.lease`, `io.github.erp.erp.leases`

4. **Depreciation Service** (Port: 8085)
   - Asset depreciation calculations and net book value management
   - Package: `io.github.erp.erp.assets.depreciation`, `io.github.erp.erp.assets.nbv`

5. **Work-in-Progress Service** (Port: 8086)
   - Project cost accumulation and WIP asset management
   - Package: `io.github.erp.erp.wip`

6. **Reporting Service** (Port: 8087)
   - Cross-context reporting and analytics
   - Package: `io.github.erp.internal.report`, `io.github.erp.cqrs.*`

## Service Discovery and Configuration

### JHipster Registry
- **Port**: 8761
- **Purpose**: Service discovery and centralized configuration
- **Authentication**: admin/admin
- **Configuration**: Native file-based configuration from `central-server-config`

### Service Registration
Each microservice automatically registers with the JHipster Registry using Eureka client configuration:
```yaml
eureka:
  client:
    enabled: true
    service-url:
      defaultZone: http://admin:admin@jhipster-registry:8761/eureka
```

## Health Checks and Monitoring

### Spring Actuator Endpoints
All services expose the following management endpoints:
- `/management/health` - Health check
- `/management/health/liveness` - Kubernetes liveness probe
- `/management/health/readiness` - Kubernetes readiness probe
- `/management/metrics` - Application metrics
- `/management/prometheus` - Prometheus metrics export

### Prometheus Integration
Each service exports metrics to Prometheus on the `/management/prometheus` endpoint with service-specific tags.

## Deployment Options

### 1. Local Development with Docker Compose

#### Prerequisites
- Docker and Docker Compose installed
- Java 11+ and Maven for building

#### Quick Start
```bash
# Build all microservices and Docker images
./scripts/build-docker-images.sh

# Start all services
./scripts/start-microservices-local.sh
```

#### Services Access
- JHipster Registry: http://localhost:8761
- Asset Management: http://localhost:8082
- Financial Core: http://localhost:8083
- Lease Service: http://localhost:8084
- Depreciation Service: http://localhost:8085
- WIP Service: http://localhost:8086
- Reporting Service: http://localhost:8087

### 2. Kubernetes Deployment

#### Prerequisites
- Kubernetes cluster (local or cloud)
- kubectl configured
- Docker images built and available

#### Deployment Steps
```bash
# Build and push Docker images
./scripts/build-docker-images.sh

# Deploy to Kubernetes
./scripts/deploy-k8s.sh
```

#### Kubernetes Resources
- **Deployments**: Each microservice with 2 replicas
- **Services**: ClusterIP services for internal communication
- **ConfigMaps**: Database and service configuration
- **Secrets**: JWT tokens and database credentials
- **HPA**: Horizontal Pod Autoscaling based on CPU/memory

## Configuration Management

### Environment-Specific Configuration
Each microservice has dedicated configuration files:
- `src/main/resources/config/microservices/{service}-application.yml`

### Shared Configuration
Common settings managed through JHipster Registry:
- Database connections
- Security settings
- Kafka topics
- Service discovery

### Environment Variables
Key environment variables for deployment:
- `SPRING_DATASOURCE_URL`: Database connection string
- `EUREKA_CLIENT_SERVICE_URL_DEFAULTZONE`: Service registry URL
- `SECURITY_AUTHENTICATION_JWT_BASE64_SECRET`: JWT signing key
- `SPRING_PROFILES_ACTIVE`: Active Spring profiles

## Inter-Service Communication

### Synchronous Communication
- REST API calls between services
- Service discovery through Eureka
- Load balancing via Spring Cloud LoadBalancer

### Asynchronous Communication
- Kafka topics for event-driven communication
- Domain events for cross-context integration
- Event sourcing for audit trails

### Kafka Topics
- `asset_events_topic`: Asset lifecycle events
- `financial_events_topic`: Financial transaction events
- `lease_events_topic`: Lease accounting events
- `depreciation_batch_topic`: Depreciation processing
- `wip_events_topic`: Work-in-progress events
- `reporting_events_topic`: Reporting data updates

## Security

### JWT Authentication
- Shared JWT secret across all services
- Token validation at service boundaries
- Role-based access control

### Network Security
- Internal service communication via private networks
- TLS encryption for external endpoints
- Kubernetes network policies (recommended)

## Monitoring and Observability

### Metrics Collection
- Prometheus metrics from all services
- JVM, HTTP, and business metrics
- Custom metrics for domain-specific operations

### Health Monitoring
- Liveness and readiness probes
- Database connectivity checks
- Service dependency health

### Logging
- Centralized logging with structured format
- Correlation IDs for request tracing
- Service-specific log levels

## Scaling and Performance

### Horizontal Pod Autoscaling
- CPU-based scaling (70% threshold)
- Memory-based scaling (80% threshold)
- Min replicas: 2, Max replicas: 10

### Resource Limits
- Memory: 256Mi request, 512Mi limit
- CPU: 250m request, 500m limit
- Adjustable based on load testing

## Troubleshooting

### Common Issues

1. **Service Registration Failures**
   - Check JHipster Registry connectivity
   - Verify Eureka configuration
   - Check network connectivity

2. **Database Connection Issues**
   - Verify database URL and credentials
   - Check database service availability
   - Review connection pool settings

3. **Inter-Service Communication Failures**
   - Check service discovery registration
   - Verify network policies
   - Review load balancer configuration

### Debugging Commands
```bash
# Check service status
kubectl get pods
kubectl get services

# View service logs
kubectl logs -f deployment/asset-management-service

# Check service registration
curl http://localhost:8761/eureka/apps

# Health check
curl http://localhost:8082/management/health
```

## Migration from Monolith

### Phased Approach
1. Deploy microservices alongside monolith
2. Gradually migrate traffic to microservices
3. Implement data synchronization
4. Decommission monolith components

### Data Migration
- Shared database initially
- Gradual data separation by bounded context
- Event-driven synchronization during transition

## Development Workflow

### Building Services
```bash
# Build specific service
./mvnw clean package -Dspring.profiles.active=asset-management

# Build all services
./scripts/build-microservices.sh
```

### Testing
```bash
# Run tests for specific context
./mvnw test -Dtest="*Asset*"

# Integration testing
docker-compose -f src/main/docker/microservices/docker-compose.yml up -d
```

### CI/CD Integration
- Automated builds for each service
- Independent deployment pipelines
- Service-specific testing strategies

## Best Practices

### Service Design
- Single responsibility per service
- Database per service pattern
- API versioning for backward compatibility
- Circuit breaker pattern for resilience

### Configuration
- Externalized configuration
- Environment-specific settings
- Secret management
- Configuration validation

### Monitoring
- Comprehensive health checks
- Business metrics collection
- Distributed tracing
- Alerting on key metrics

## Support and Maintenance

### Documentation
- API documentation via Swagger/OpenAPI
- Service dependency mapping
- Runbook for operational procedures

### Backup and Recovery
- Database backup strategies
- Configuration backup
- Disaster recovery procedures

For additional support, refer to the bounded context documentation in `modernization/architecture/bounded-contexts/` and the microservices design document in `modernization/architecture/MICROSERVICES_DESIGN.md`.
