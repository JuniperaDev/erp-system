package io.github.erp.internal.model;

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
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationStatus {

    private String build;

    private String version;

    private String profile;

    private String branch;

    public ApplicationStatus() {}

    public ApplicationStatus(String build, String version, String profile, String branch) {
        this.build = build;
        this.version = version;
        this.profile = profile;
        this.branch = branch;
    }

    public static ApplicationStatusBuilder builder() {
        return new ApplicationStatusBuilder();
    }

    public static class ApplicationStatusBuilder {
        private String build;
        private String version;
        private String profile;
        private String branch;

        public ApplicationStatusBuilder build(String build) {
            this.build = build;
            return this;
        }

        public ApplicationStatusBuilder version(String version) {
            this.version = version;
            return this;
        }

        public ApplicationStatusBuilder profile(String profile) {
            this.profile = profile;
            return this;
        }

        public ApplicationStatusBuilder branch(String branch) {
            this.branch = branch;
            return this;
        }

        public ApplicationStatus build() {
            return new ApplicationStatus(build, version, profile, branch);
        }
    }

    public String getBuild() { return build; }
    public void setBuild(String build) { this.build = build; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public String getProfile() { return profile; }
    public void setProfile(String profile) { this.profile = profile; }
    
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
}
