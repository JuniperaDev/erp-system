package io.github.erp.service.elasticsearch;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class AuditSearchMetrics {

    private static final Logger log = LoggerFactory.getLogger(AuditSearchMetrics.class);

    private final Timer searchLatencyTimer;
    private final Timer indexingLatencyTimer;
    private final Counter searchRequestCounter;
    private final Counter indexingRequestCounter;
    private final Counter searchErrorCounter;
    private final Counter indexingErrorCounter;

    @Autowired
    public AuditSearchMetrics(MeterRegistry meterRegistry) {
        this.searchLatencyTimer = Timer.builder("audit.search.latency")
            .description("Latency of audit search operations")
            .register(meterRegistry);

        this.indexingLatencyTimer = Timer.builder("audit.indexing.latency")
            .description("Latency of audit indexing operations")
            .register(meterRegistry);

        this.searchRequestCounter = Counter.builder("audit.search.requests")
            .description("Total number of audit search requests")
            .register(meterRegistry);

        this.indexingRequestCounter = Counter.builder("audit.indexing.requests")
            .description("Total number of audit indexing requests")
            .register(meterRegistry);

        this.searchErrorCounter = Counter.builder("audit.search.errors")
            .description("Total number of audit search errors")
            .register(meterRegistry);

        this.indexingErrorCounter = Counter.builder("audit.indexing.errors")
            .description("Total number of audit indexing errors")
            .register(meterRegistry);
    }

    public void recordSearchLatency(Duration duration) {
        searchLatencyTimer.record(duration.toNanos(), TimeUnit.NANOSECONDS);
        searchRequestCounter.increment();
        
        if (duration.toMillis() > 1000) {
            log.warn("Slow audit search detected: {} ms", duration.toMillis());
        }
        
        log.debug("Recorded audit search latency: {} ms", duration.toMillis());
    }

    public void recordIndexingLatency(Duration duration) {
        indexingLatencyTimer.record(duration.toNanos(), TimeUnit.NANOSECONDS);
        indexingRequestCounter.increment();
        
        if (duration.toMillis() > 5000) {
            log.warn("Slow audit indexing detected: {} ms", duration.toMillis());
        }
        
        log.debug("Recorded audit indexing latency: {} ms", duration.toMillis());
    }

    public void recordSearchError() {
        searchErrorCounter.increment();
        log.debug("Recorded audit search error");
    }

    public void recordIndexingError() {
        indexingErrorCounter.increment();
        log.debug("Recorded audit indexing error");
    }

    public Timer.Sample startSearchTimer() {
        return Timer.start();
    }

    public Timer.Sample startIndexingTimer() {
        return Timer.start();
    }

    public double getAverageSearchLatency() {
        return searchLatencyTimer.mean(TimeUnit.MILLISECONDS);
    }

    public double getAverageIndexingLatency() {
        return indexingLatencyTimer.mean(TimeUnit.MILLISECONDS);
    }

    public long getTotalSearchRequests() {
        return (long) searchRequestCounter.count();
    }

    public long getTotalIndexingRequests() {
        return (long) indexingRequestCounter.count();
    }

    public long getTotalSearchErrors() {
        return (long) searchErrorCounter.count();
    }

    public long getTotalIndexingErrors() {
        return (long) indexingErrorCounter.count();
    }

    public double getSearchErrorRate() {
        long totalRequests = getTotalSearchRequests();
        if (totalRequests == 0) {
            return 0.0;
        }
        return (double) getTotalSearchErrors() / totalRequests;
    }

    public double getIndexingErrorRate() {
        long totalRequests = getTotalIndexingRequests();
        if (totalRequests == 0) {
            return 0.0;
        }
        return (double) getTotalIndexingErrors() / totalRequests;
    }
}
