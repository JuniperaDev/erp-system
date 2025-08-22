package io.github.erp.domain;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.erp.context.assets.domain.AssetRegistration;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Intermediate entity for AssetRegistration-JobSheet relationship.
 */
@Entity
@Table(name = "asset_job_sheet_assignment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assetjobsheetassignment")
public class AssetJobSheetAssignment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    @Field(type = FieldType.Long)
    private Long id;

    @NotNull
    @Column(name = "assignment_date", nullable = false)
    @Field(type = FieldType.Date)
    private LocalDate assignmentDate;

    @Column(name = "assignment_status")
    @Field(type = FieldType.Keyword)
    private String assignmentStatus;

    @Column(name = "work_type")
    @Field(type = FieldType.Keyword)
    private String workType;

    @Column(name = "notes")
    @Field(type = FieldType.Text)
    private String notes;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "assetJobSheetAssignments" }, allowSetters = true)
    private AssetRegistration assetRegistration;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "assetJobSheetAssignments" }, allowSetters = true)
    private JobSheet jobSheet;

    public Long getId() {
        return this.id;
    }

    public AssetJobSheetAssignment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAssignmentDate() {
        return this.assignmentDate;
    }

    public AssetJobSheetAssignment assignmentDate(LocalDate assignmentDate) {
        this.setAssignmentDate(assignmentDate);
        return this;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getAssignmentStatus() {
        return this.assignmentStatus;
    }

    public AssetJobSheetAssignment assignmentStatus(String assignmentStatus) {
        this.setAssignmentStatus(assignmentStatus);
        return this;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public String getWorkType() {
        return this.workType;
    }

    public AssetJobSheetAssignment workType(String workType) {
        this.setWorkType(workType);
        return this;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getNotes() {
        return this.notes;
    }

    public AssetJobSheetAssignment notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AssetRegistration getAssetRegistration() {
        return this.assetRegistration;
    }

    public void setAssetRegistration(AssetRegistration assetRegistration) {
        this.assetRegistration = assetRegistration;
    }

    public AssetJobSheetAssignment assetRegistration(AssetRegistration assetRegistration) {
        this.setAssetRegistration(assetRegistration);
        return this;
    }

    public JobSheet getJobSheet() {
        return this.jobSheet;
    }

    public void setJobSheet(JobSheet jobSheet) {
        this.jobSheet = jobSheet;
    }

    public AssetJobSheetAssignment jobSheet(JobSheet jobSheet) {
        this.setJobSheet(jobSheet);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssetJobSheetAssignment)) {
            return false;
        }
        return id != null && id.equals(((AssetJobSheetAssignment) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "AssetJobSheetAssignment{" +
            "id=" + getId() +
            ", assignmentDate='" + getAssignmentDate() + "'" +
            ", assignmentStatus='" + getAssignmentStatus() + "'" +
            ", workType='" + getWorkType() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
