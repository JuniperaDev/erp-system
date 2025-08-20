package io.github.erp.service.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class MemoryMonitoringService {

    private static final Logger log = LoggerFactory.getLogger(MemoryMonitoringService.class);

    private final MeterRegistry meterRegistry;
    private final MemoryMXBean memoryMXBean;
    private final AtomicLong entityLoadCount = new AtomicLong(0);
    private final AtomicLong nPlusOneQueryCount = new AtomicLong(0);

    public MemoryMonitoringService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        
        registerMemoryGauges();
        registerEntityLoadingCounters();
    }

    private void registerMemoryGauges() {
        Gauge.builder("jvm.memory.heap.used.percentage")
            .description("Percentage of heap memory used")
            .register(meterRegistry, this, service -> {
                MemoryUsage heapUsage = service.memoryMXBean.getHeapMemoryUsage();
                return (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
            });

        Gauge.builder("jvm.memory.heap.used.mb")
            .description("Heap memory used in MB")
            .register(meterRegistry, this, service -> {
                MemoryUsage heapUsage = service.memoryMXBean.getHeapMemoryUsage();
                return heapUsage.getUsed() / (1024.0 * 1024.0);
            });

        Gauge.builder("jvm.memory.heap.max.mb")
            .description("Maximum heap memory in MB")
            .register(meterRegistry, this, service -> {
                MemoryUsage heapUsage = service.memoryMXBean.getHeapMemoryUsage();
                return heapUsage.getMax() / (1024.0 * 1024.0);
            });
    }

    private void registerEntityLoadingCounters() {
        Counter.builder("erp.entity.loads")
            .description("Number of entity loads")
            .register(meterRegistry);

        Counter.builder("erp.nplus1.queries")
            .description("Number of potential N+1 queries detected")
            .register(meterRegistry);

        Gauge.builder("erp.entity.loads.total")
            .description("Total entity loads since startup")
            .register(meterRegistry, entityLoadCount, AtomicLong::get);

        Gauge.builder("erp.nplus1.queries.total")
            .description("Total N+1 queries detected since startup")
            .register(meterRegistry, nPlusOneQueryCount, AtomicLong::get);
    }

    public void recordEntityLoad(String entityType) {
        entityLoadCount.incrementAndGet();
        meterRegistry.counter("erp.entity.loads", "type", entityType).increment();
        
        log.debug("Entity load recorded for type: {}", entityType);
    }

    public void recordNPlusOneQuery(String entityType, String relationship) {
        nPlusOneQueryCount.incrementAndGet();
        meterRegistry.counter("erp.nplus1.queries", "entity", entityType, "relationship", relationship).increment();
        
        log.warn("Potential N+1 query detected for entity: {} relationship: {}", entityType, relationship);
    }

    public Timer.Sample startEntityLoadTimer(String entityType) {
        return Timer.start(meterRegistry)
            .tag("entity.type", entityType);
    }

    public void stopEntityLoadTimer(Timer.Sample sample, String operation) {
        sample.stop(Timer.builder("erp.entity.load.duration")
            .description("Time taken to load entities")
            .tag("operation", operation)
            .register(meterRegistry));
    }

    public MemoryUsage getCurrentHeapUsage() {
        return memoryMXBean.getHeapMemoryUsage();
    }

    public double getHeapUsagePercentage() {
        MemoryUsage heapUsage = getCurrentHeapUsage();
        return (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
    }

    public void logMemoryUsage(String operation) {
        MemoryUsage heapUsage = getCurrentHeapUsage();
        double usagePercentage = getHeapUsagePercentage();
        
        log.info("Memory usage after {}: {:.2f}% ({} MB / {} MB)", 
            operation, 
            usagePercentage,
            heapUsage.getUsed() / (1024 * 1024),
            heapUsage.getMax() / (1024 * 1024));
    }
}
