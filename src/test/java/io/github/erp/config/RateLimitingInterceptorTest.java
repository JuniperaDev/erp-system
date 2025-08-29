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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateLimitingInterceptorTest {

    @Mock
    private RateLimitingProperties rateLimitingProperties;

    private RateLimitingInterceptor rateLimitingInterceptor;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        when(rateLimitingProperties.isEnabled()).thenReturn(true);
        when(rateLimitingProperties.getStandardRequestsPerHour()).thenReturn(1000);
        when(rateLimitingProperties.getReportRequestsPerHour()).thenReturn(10);
        when(rateLimitingProperties.getExportRequestsPerHour()).thenReturn(5);
        when(rateLimitingProperties.getSearchRequestsPerHour()).thenReturn(500);

        rateLimitingInterceptor = new RateLimitingInterceptor(rateLimitingProperties);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("testuser", "password")
        );
    }

    @Test
    void shouldAllowRequestWhenRateLimitingDisabled() throws Exception {
        when(rateLimitingProperties.isEnabled()).thenReturn(false);
        
        request.setRequestURI("/api/v1/audit-trail/events");
        
        boolean result = rateLimitingInterceptor.preHandle(request, response, null);
        
        assertThat(result).isTrue();
    }

    @Test
    void shouldAllowRequestWithinStandardRateLimit() throws Exception {
        request.setRequestURI("/api/v1/audit-trail/events");
        
        boolean result = rateLimitingInterceptor.preHandle(request, response, null);
        
        assertThat(result).isTrue();
        assertThat(response.getHeader("X-RateLimit-Limit")).isNotNull();
        assertThat(response.getHeader("X-RateLimit-Remaining")).isNotNull();
        assertThat(response.getHeader("X-RateLimit-Reset")).isNotNull();
    }

    @Test
    void shouldAllowRequestWithinReportRateLimit() throws Exception {
        request.setRequestURI("/api/v1/audit-trail/compliance-reports");
        
        boolean result = rateLimitingInterceptor.preHandle(request, response, null);
        
        assertThat(result).isTrue();
        assertThat(response.getHeader("X-RateLimit-Limit")).isNotNull();
    }

    @Test
    void shouldAllowRequestWithinExportRateLimit() throws Exception {
        request.setRequestURI("/api/v1/audit-trail/export");
        
        boolean result = rateLimitingInterceptor.preHandle(request, response, null);
        
        assertThat(result).isTrue();
        assertThat(response.getHeader("X-RateLimit-Limit")).isNotNull();
    }

    @Test
    void shouldAllowRequestWithinSearchRateLimit() throws Exception {
        request.setRequestURI("/api/v1/audit-trail/events/search");
        
        boolean result = rateLimitingInterceptor.preHandle(request, response, null);
        
        assertThat(result).isTrue();
        assertThat(response.getHeader("X-RateLimit-Limit")).isNotNull();
    }

    @Test
    void shouldBlockRequestWhenExportRateLimitExceeded() throws Exception {
        when(rateLimitingProperties.getExportRequestsPerHour()).thenReturn(1);
        rateLimitingInterceptor = new RateLimitingInterceptor(rateLimitingProperties);
        
        request.setRequestURI("/api/v1/audit-trail/export");
        
        rateLimitingInterceptor.preHandle(request, response, null);
        
        MockHttpServletResponse secondResponse = new MockHttpServletResponse();
        boolean result = rateLimitingInterceptor.preHandle(request, secondResponse, null);
        
        assertThat(result).isFalse();
        assertThat(secondResponse.getStatus()).isEqualTo(429);
        assertThat(secondResponse.getHeader("Retry-After")).isEqualTo("3600");
    }

    @Test
    void shouldUseRemoteAddressWhenUserNotAuthenticated() throws Exception {
        SecurityContextHolder.clearContext();
        request.setRemoteAddr("192.168.1.1");
        request.setRequestURI("/api/v1/audit-trail/events");
        
        boolean result = rateLimitingInterceptor.preHandle(request, response, null);
        
        assertThat(result).isTrue();
    }
}
