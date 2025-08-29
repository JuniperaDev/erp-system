package io.github.erp.web.rest;

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

import io.github.erp.IntegrationTest;
import io.github.erp.config.RateLimitingProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "application.rate-limiting.enabled=true",
    "application.rate-limiting.standard-requests-per-hour=1000",
    "application.rate-limiting.report-requests-per-hour=10",
    "application.rate-limiting.export-requests-per-hour=5",
    "application.rate-limiting.search-requests-per-hour=500"
})
class RateLimitingIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RateLimitingProperties rateLimitingProperties;

    @Test
    @WithMockUser
    void shouldIncludeRateLimitHeadersInResponse() throws Exception {
        mockMvc.perform(get("/api/v1/audit-trail/events"))
            .andExpect(status().isOk())
            .andExpect(header().exists("X-RateLimit-Limit"))
            .andExpect(header().exists("X-RateLimit-Remaining"))
            .andExpect(header().exists("X-RateLimit-Reset"));
    }

    @Test
    @WithMockUser
    void shouldAllowRequestsWithinRateLimit() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/api/v1/audit-trail/events"))
                .andExpect(status().isOk());
        }
    }

    @Test
    @WithMockUser
    void shouldApplyDifferentRateLimitsForDifferentEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/audit-trail/compliance-reports"))
            .andExpect(status().isOk())
            .andExpect(header().exists("X-RateLimit-Limit"));

        mockMvc.perform(get("/api/v1/audit-trail/export"))
            .andExpect(status().isOk())
            .andExpect(header().exists("X-RateLimit-Limit"));

        mockMvc.perform(get("/api/v1/audit-trail/events/search"))
            .andExpect(status().isOk())
            .andExpect(header().exists("X-RateLimit-Limit"));
    }
}
