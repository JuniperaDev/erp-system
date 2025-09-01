package io.github.erp.config;

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

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RateLimitingInterceptor.class);

    private final RateLimitingProperties rateLimitingProperties;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public RateLimitingInterceptor(RateLimitingProperties rateLimitingProperties) {
        this.rateLimitingProperties = rateLimitingProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!rateLimitingProperties.isEnabled()) {
            return true;
        }

        String userKey = getUserKey(request);
        String requestPath = request.getRequestURI();
        
        Bucket bucket = getBucketForUser(userKey, requestPath);
        
        if (bucket.tryConsume(1)) {
            addRateLimitHeaders(response, bucket, requestPath);
            return true;
        } else {
            handleRateLimitExceeded(response, bucket, requestPath);
            return false;
        }
    }

    private String getUserKey(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            return authentication.getName();
        }
        return request.getRemoteAddr();
    }

    private Bucket getBucketForUser(String userKey, String requestPath) {
        String bucketKey = userKey + ":" + getEndpointType(requestPath);
        return buckets.computeIfAbsent(bucketKey, key -> createBucket(requestPath));
    }

    private String getEndpointType(String requestPath) {
        if (requestPath.contains("/compliance-reports")) {
            return "REPORT";
        } else if (requestPath.contains("/export")) {
            return "EXPORT";
        } else if (requestPath.contains("/events/search")) {
            return "SEARCH";
        } else {
            return "STANDARD";
        }
    }

    private Bucket createBucket(String requestPath) {
        int requestsPerHour = getRequestsPerHour(requestPath);
        
        Bandwidth bandwidth = Bandwidth.classic(requestsPerHour, Refill.intervally(requestsPerHour, Duration.ofHours(1)));
        return Bucket4j.builder()
            .addLimit(bandwidth)
            .build();
    }

    private int getRequestsPerHour(String requestPath) {
        if (requestPath.contains("/compliance-reports")) {
            return rateLimitingProperties.getReportRequestsPerHour();
        } else if (requestPath.contains("/export")) {
            return rateLimitingProperties.getExportRequestsPerHour();
        } else if (requestPath.contains("/events/search")) {
            return rateLimitingProperties.getSearchRequestsPerHour();
        } else {
            return rateLimitingProperties.getStandardRequestsPerHour();
        }
    }

    private void addRateLimitHeaders(HttpServletResponse response, Bucket bucket, String requestPath) {
        long availableTokens = bucket.getAvailableTokens();
        long capacity = getRequestsPerHour(requestPath);
        
        response.setHeader("X-RateLimit-Limit", String.valueOf(capacity));
        response.setHeader("X-RateLimit-Remaining", String.valueOf(availableTokens));
        response.setHeader("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() + Duration.ofHours(1).toMillis()));
    }

    private void handleRateLimitExceeded(HttpServletResponse response, Bucket bucket, String requestPath) throws Exception {
        long capacity = getRequestsPerHour(requestPath);
        
        response.setStatus(429);
        response.setHeader("Retry-After", "3600");
        response.setHeader("X-RateLimit-Limit", String.valueOf(capacity));
        response.setHeader("X-RateLimit-Remaining", "0");
        response.setHeader("X-RateLimit-Reset", String.valueOf(System.currentTimeMillis() + Duration.ofHours(1).toMillis()));
        response.setContentType("application/json");
        
        String errorResponse = "{\"error\":\"Rate limit exceeded\",\"message\":\"Too many requests. Please try again later.\"}";
        response.getWriter().write(errorResponse);
        
        log.warn("Rate limit exceeded for request");
    }
}
