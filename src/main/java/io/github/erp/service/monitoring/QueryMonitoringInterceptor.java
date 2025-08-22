package io.github.erp.service.monitoring;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class QueryMonitoringInterceptor extends EmptyInterceptor {

    private static final Logger log = LoggerFactory.getLogger(QueryMonitoringInterceptor.class);
    
    private final ThreadLocal<AtomicInteger> queryCount = ThreadLocal.withInitial(() -> new AtomicInteger(0));
    private final ThreadLocal<String> currentOperation = new ThreadLocal<>();
    
    @Autowired
    private MemoryMonitoringService memoryMonitoringService;

    @Override
    public String onPrepareStatement(String sql) {
        int count = queryCount.get().incrementAndGet();
        String operation = currentOperation.get();
        
        if (count > 10 && operation != null) {
            log.warn("Potential N+1 query detected. Query count: {} for operation: {}", count, operation);
            if (memoryMonitoringService != null) {
                memoryMonitoringService.recordNPlusOneQuery("unknown", operation);
            }
        }
        
        log.debug("SQL Query #{}: {}", count, sql);
        return super.onPrepareStatement(sql);
    }

    public void startOperation(String operation) {
        currentOperation.set(operation);
        queryCount.get().set(0);
        log.debug("Started monitoring operation: {}", operation);
    }

    public void endOperation() {
        int totalQueries = queryCount.get().get();
        String operation = currentOperation.get();
        
        if (operation != null) {
            log.debug("Completed operation: {} with {} queries", operation, totalQueries);
        }
        
        currentOperation.remove();
        queryCount.remove();
    }

    public int getCurrentQueryCount() {
        return queryCount.get().get();
    }
}
