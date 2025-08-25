# Microservices Architecture Decision: Single JAR vs Separate JARs

## Context

For Story D4.5 (Prepare for Microservices Deployment), we needed to decide between two architectural approaches for deploying microservices:

1. **Single JAR with Spring Profiles**: Use one JAR file (`erp-system-*.jar`) with different Spring profiles to activate specific bounded contexts
2. **Separate JARs**: Build individual JAR files for each microservice bounded context

## Decision

We have chosen the **Single JAR with Spring Profiles** approach for the initial microservices deployment.

## Rationale

### Advantages of Single JAR Approach

1. **Maintains Existing Build Pipeline**
   - Leverages existing Maven build configuration and CI/CD infrastructure
   - No need to restructure the entire build system immediately
   - Reduces risk of breaking existing deployment processes

2. **Gradual Migration Strategy**
   - Enables incremental transition from monolith to microservices
   - Allows testing microservices patterns without full architectural overhaul
   - Provides fallback option to monolithic deployment if needed

3. **Shared Libraries and Dependencies**
   - Eliminates code duplication across microservices
   - Maintains consistency in shared domain models and utilities
   - Reduces overall artifact size compared to multiple JARs with duplicated dependencies

4. **Simplified Dependency Management**
   - Single Maven POM manages all dependencies
   - Consistent versioning across all services
   - Easier security updates and dependency upgrades

5. **Development Efficiency**
   - Faster build times (single compilation unit)
   - Simplified local development and testing
   - Easier debugging across service boundaries

### Spring Profile Configuration

Each microservice is activated using specific Spring profiles:

- `asset-management`: Asset Management Service (Port 8082)
- `financial-core`: Financial Core Service (Port 8083)
- `lease-service`: IFRS16 Leasing Service (Port 8084)
- `depreciation-service`: Depreciation Service (Port 8085)
- `wip-service`: Work-in-Progress Service (Port 8086)
- `reporting-service`: Reporting Service (Port 8087)

### Docker Configuration

Each microservice runs in its own Docker container with:
```dockerfile
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active={profile-name} -jar /app.jar" ]
```

## Trade-offs and Considerations

### Disadvantages of Single JAR Approach

1. **Larger Container Images**: Each container includes the full application JAR
2. **Potential Resource Overhead**: Services load unused code and dependencies
3. **Coupling Risk**: Shared codebase may create hidden dependencies between services
4. **Scaling Granularity**: Cannot optimize individual service JARs for specific requirements

### Future Migration Path

This approach provides a foundation for future migration to separate JARs:

1. **Phase 1** (Current): Single JAR with Spring profiles
2. **Phase 2**: Extract shared libraries to separate modules
3. **Phase 3**: Build separate JAR files for each bounded context
4. **Phase 4**: Optimize individual service dependencies and configurations

## Implementation Details

### Service Discovery
- Uses JHipster Registry (Eureka) for service registration and discovery
- Each service registers with its profile-specific name and port

### Health Checks
- Spring Boot Actuator provides `/actuator/health` endpoints
- Each service exposes health checks on its designated port

### Configuration Management
- Distributed configuration via JHipster Registry
- Profile-specific configuration files in `src/main/resources/config/microservices/`

### Monitoring and Observability
- Micrometer metrics integration
- Distributed tracing capabilities
- Centralized logging configuration

## Validation Criteria

This architectural decision will be validated based on:

1. **Deployment Success**: All 6 microservices deploy and start successfully
2. **Service Discovery**: Services register and discover each other correctly
3. **Health Monitoring**: Health checks respond appropriately
4. **Resource Efficiency**: Acceptable memory and CPU usage
5. **Development Velocity**: Maintains or improves development speed

## Review and Evolution

This decision will be reviewed after:
- 3 months of production operation
- Performance and resource utilization analysis
- Developer feedback on maintainability
- Business requirements for independent service scaling

The architecture can evolve toward separate JARs if the trade-offs justify the additional complexity.
