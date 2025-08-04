package io.github.erp.service.dto;

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

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link io.github.erp.domain.AssetJobSheetAssignment} entity.
 */
public class AssetJobSheetAssignmentDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate assignmentDate;

    private String assignmentStatus;

    private String workType;

    private String notes;

    private Long assetRegistrationId;

    private String assetRegistrationAssetNumber;

    private Long jobSheetId;

    private String jobSheetSerialNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getAssignmentStatus() {
        return assignmentStatus;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getAssetRegistrationId() {
        return assetRegistrationId;
    }

    public void setAssetRegistrationId(Long assetRegistrationId) {
        this.assetRegistrationId = assetRegistrationId;
    }

    public String getAssetRegistrationAssetNumber() {
        return assetRegistrationAssetNumber;
    }

    public void setAssetRegistrationAssetNumber(String assetRegistrationAssetNumber) {
        this.assetRegistrationAssetNumber = assetRegistrationAssetNumber;
    }

    public Long getJobSheetId() {
        return jobSheetId;
    }

    public void setJobSheetId(Long jobSheetId) {
        this.jobSheetId = jobSheetId;
    }

    public String getJobSheetSerialNumber() {
        return jobSheetSerialNumber;
    }

    public void setJobSheetSerialNumber(String jobSheetSerialNumber) {
        this.jobSheetSerialNumber = jobSheetSerialNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetJobSheetAssignmentDTO)) {
            return false;
        }

        AssetJobSheetAssignmentDTO assetJobSheetAssignmentDTO = (AssetJobSheetAssignmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assetJobSheetAssignmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "AssetJobSheetAssignmentDTO{" +
            "id=" + getId() +
            ", assignmentDate='" + getAssignmentDate() + "'" +
            ", assignmentStatus='" + getAssignmentStatus() + "'" +
            ", workType='" + getWorkType() + "'" +
            ", notes='" + getNotes() + "'" +
            ", assetRegistrationId=" + getAssetRegistrationId() +
            ", jobSheetId=" + getJobSheetId() +
            "}";
    }
}
