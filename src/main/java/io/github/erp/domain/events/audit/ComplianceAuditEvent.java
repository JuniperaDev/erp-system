package io.github.erp.domain.events.audit;

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

import io.github.erp.domain.events.AbstractDomainEvent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "compliance_audit_event")
public class ComplianceAuditEvent extends AbstractDomainEvent {

    @Column(name = "compliance_type")
    private String complianceType;

    @Column(name = "regulation_reference")
    private String regulationReference;

    @Column(name = "compliance_status")
    private String complianceStatus;

    @Column(name = "audit_details", columnDefinition = "TEXT")
    private String auditDetails;

    @Column(name = "auditor_id")
    private String auditorId;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "remediation_required")
    private Boolean remediationRequired;

    protected ComplianceAuditEvent() {
        super();
    }

    public ComplianceAuditEvent(String aggregateId, String aggregateType, String complianceType,
                              String regulationReference, String complianceStatus, String auditDetails,
                              String auditorId, String riskLevel, Boolean remediationRequired,
                              UUID correlationId) {
        super(aggregateId, aggregateType, correlationId);
        this.complianceType = complianceType;
        this.regulationReference = regulationReference;
        this.complianceStatus = complianceStatus;
        this.auditDetails = auditDetails;
        this.auditorId = auditorId;
        this.riskLevel = riskLevel;
        this.remediationRequired = remediationRequired;
    }

    public String getComplianceType() {
        return complianceType;
    }

    public void setComplianceType(String complianceType) {
        this.complianceType = complianceType;
    }

    public String getRegulationReference() {
        return regulationReference;
    }

    public void setRegulationReference(String regulationReference) {
        this.regulationReference = regulationReference;
    }

    public String getComplianceStatus() {
        return complianceStatus;
    }

    public void setComplianceStatus(String complianceStatus) {
        this.complianceStatus = complianceStatus;
    }

    public String getAuditDetails() {
        return auditDetails;
    }

    public void setAuditDetails(String auditDetails) {
        this.auditDetails = auditDetails;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public Boolean getRemediationRequired() {
        return remediationRequired;
    }

    public void setRemediationRequired(Boolean remediationRequired) {
        this.remediationRequired = remediationRequired;
    }

    @Override
    public String toString() {
        return "ComplianceAuditEvent{" +
            "complianceType='" + complianceType + '\'' +
            ", regulationReference='" + regulationReference + '\'' +
            ", complianceStatus='" + complianceStatus + '\'' +
            ", auditDetails='" + auditDetails + '\'' +
            ", auditorId='" + auditorId + '\'' +
            ", riskLevel='" + riskLevel + '\'' +
            ", remediationRequired=" + remediationRequired +
            "} " + super.toString();
    }
}
