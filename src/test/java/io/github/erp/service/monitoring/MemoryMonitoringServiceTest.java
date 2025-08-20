package io.github.erp.service.monitoring;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryMonitoringServiceTest {

    private MemoryMonitoringService memoryMonitoringService;
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        memoryMonitoringService = new MemoryMonitoringService(meterRegistry);
    }

    @Test
    void shouldRecordEntityLoad() {
        memoryMonitoringService.recordEntityLoad("AssetRegistration");
        
        assertThat(meterRegistry.counter("erp.entity.loads", "type", "AssetRegistration").count()).isEqualTo(1.0);
        assertThat(meterRegistry.gauge("erp.entity.loads.total").value()).isEqualTo(1.0);
    }

    @Test
    void shouldRecordNPlusOneQuery() {
        memoryMonitoringService.recordNPlusOneQuery("AssetRegistration", "placeholders");
        
        assertThat(meterRegistry.counter("erp.nplus1.queries", "entity", "AssetRegistration", "relationship", "placeholders").count()).isEqualTo(1.0);
        assertThat(meterRegistry.gauge("erp.nplus1.queries.total").value()).isEqualTo(1.0);
    }

    @Test
    void shouldProvideMemoryUsageInformation() {
        double heapUsagePercentage = memoryMonitoringService.getHeapUsagePercentage();
        
        assertThat(heapUsagePercentage).isGreaterThanOrEqualTo(0.0);
        assertThat(heapUsagePercentage).isLessThanOrEqualTo(100.0);
    }

    @Test
    void shouldRegisterMemoryGauges() {
        assertThat(meterRegistry.gauge("jvm.memory.heap.used.percentage")).isNotNull();
        assertThat(meterRegistry.gauge("jvm.memory.heap.used.mb")).isNotNull();
        assertThat(meterRegistry.gauge("jvm.memory.heap.max.mb")).isNotNull();
    }
}
