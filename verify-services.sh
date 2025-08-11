#!/bin/bash

echo "=== ERP System Service Verification ==="
echo

echo "1. Checking Docker services..."
POSTGRES_RUNNING=$(docker ps --filter "name=postgresql" --format "table {{.Names}}\t{{.Status}}" | grep -v NAMES)
ELASTICSEARCH_RUNNING=$(docker ps --filter "name=elasticsearch" --format "table {{.Names}}\t{{.Status}}" | grep -v NAMES)

if [ -n "$POSTGRES_RUNNING" ]; then
    echo "✅ PostgreSQL: $POSTGRES_RUNNING"
else
    echo "❌ PostgreSQL: Not running"
    echo "   Start with: docker-compose -f src/main/docker/postgresql.yml up -d"
fi

if [ -n "$ELASTICSEARCH_RUNNING" ]; then
    echo "✅ Elasticsearch: $ELASTICSEARCH_RUNNING"
else
    echo "❌ Elasticsearch: Not running"
    echo "   Start with: docker-compose -f src/main/docker/elasticsearch.yml up -d"
fi

echo

echo "2. Testing PostgreSQL connectivity..."
if docker exec -it docker-erpsystem-postgresql-1 psql -U erpSystem -c "SELECT current_user, current_database();" 2>/dev/null | grep -q erpSystem; then
    echo "✅ PostgreSQL: Connected successfully as erpSystem user"
    
    if docker exec -it docker-erpsystem-postgresql-1 psql -U erpSystem -l 2>/dev/null | grep -q erp_system_dev; then
        echo "✅ PostgreSQL: Database erp_system_dev exists"
    else
        echo "❌ PostgreSQL: Database erp_system_dev does not exist"
        echo "   Create with: docker exec -it docker-erpsystem-postgresql-1 psql -U erpSystem -c \"CREATE DATABASE erp_system_dev;\""
    fi
else
    echo "❌ PostgreSQL: Connection failed"
    echo "   Check if container is running and user exists"
fi

echo

echo "3. Testing Elasticsearch connectivity..."
if curl -s http://localhost:9200/_cluster/health | grep -q '"status"'; then
    echo "✅ Elasticsearch: Accessible on port 9200"
    CLUSTER_STATUS=$(curl -s http://localhost:9200/_cluster/health | grep -o '"status":"[^"]*"' | cut -d'"' -f4)
    echo "   Cluster status: $CLUSTER_STATUS"
else
    echo "❌ Elasticsearch: Not accessible on port 9200"
    echo "   Check if container is running and port is exposed"
fi

echo

echo "4. Checking environment variables..."
echo "   ERP_DOCUMENTS_MAX_FILE_SIZE: ${ERP_DOCUMENTS_MAX_FILE_SIZE:-'NOT SET (will use default 10MB)'}"
echo "   PG_DATABASE_DEV_USER: ${PG_DATABASE_DEV_USER:-'NOT SET (will use default erpSystem)'}"
echo "   SPRING_DATA_JEST_URI: ${SPRING_DATA_JEST_URI:-'NOT SET (will use default http://localhost:9200)'}"
echo "   ERP_INDEX_ENABLED: ${ERP_INDEX_ENABLED:-'NOT SET (will use default true)'}"
echo "   ERP_INDEX_REBUILD_ENABLED: ${ERP_INDEX_REBUILD_ENABLED:-'NOT SET (will use default false)'}"
echo "   UPLOADS_SIZE: ${UPLOADS_SIZE:-'NOT SET (will use default 1000)'}"

if [ -n "$PG_DATABASE_DEV_USER" ] && [ "$PG_DATABASE_DEV_USER" != "erpSystem" ]; then
    echo "⚠️  WARNING: PG_DATABASE_DEV_USER is set to '$PG_DATABASE_DEV_USER' but should be 'erpSystem' or unset"
    echo "   Run: unset PG_DATABASE_DEV_USER"
fi

echo

echo "5. Summary:"
ALL_GOOD=true

if [ -z "$POSTGRES_RUNNING" ]; then
    ALL_GOOD=false
fi

if [ -z "$ELASTICSEARCH_RUNNING" ]; then
    ALL_GOOD=false
fi

if ! docker exec -it docker-erpsystem-postgresql-1 psql -U erpSystem -c "SELECT 1;" 2>/dev/null | grep -q "1 row"; then
    ALL_GOOD=false
fi

if ! curl -s http://localhost:9200/_cluster/health | grep -q '"status"'; then
    ALL_GOOD=false
fi

if [ "$ALL_GOOD" = true ]; then
    echo "✅ All services are ready! You can start the application with: ./mvnw"
else
    echo "❌ Some services are not ready. Please fix the issues above before starting the application."
fi

echo
echo "=== End of Service Verification ==="
