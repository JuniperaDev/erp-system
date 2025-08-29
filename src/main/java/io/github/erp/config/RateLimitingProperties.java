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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.rate-limiting")
public class RateLimitingProperties {

    private int standardRequestsPerHour = 1000;
    private int reportRequestsPerHour = 10;
    private int exportRequestsPerHour = 5;
    private int searchRequestsPerHour = 500;
    private boolean enabled = true;

    public int getStandardRequestsPerHour() {
        return standardRequestsPerHour;
    }

    public void setStandardRequestsPerHour(int standardRequestsPerHour) {
        this.standardRequestsPerHour = standardRequestsPerHour;
    }

    public int getReportRequestsPerHour() {
        return reportRequestsPerHour;
    }

    public void setReportRequestsPerHour(int reportRequestsPerHour) {
        this.reportRequestsPerHour = reportRequestsPerHour;
    }

    public int getExportRequestsPerHour() {
        return exportRequestsPerHour;
    }

    public void setExportRequestsPerHour(int exportRequestsPerHour) {
        this.exportRequestsPerHour = exportRequestsPerHour;
    }

    public int getSearchRequestsPerHour() {
        return searchRequestsPerHour;
    }

    public void setSearchRequestsPerHour(int searchRequestsPerHour) {
        this.searchRequestsPerHour = searchRequestsPerHour;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
