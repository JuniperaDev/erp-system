package io.github.erp.service.audit.impl;

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

import io.github.erp.domain.events.audit.AuditTrailEvent;
import io.github.erp.domain.events.audit.ComplianceAuditEvent;
import io.github.erp.service.audit.AuditEventRoutingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuditEventRoutingServiceImpl implements AuditEventRoutingService {

    private static final Logger log = LoggerFactory.getLogger(AuditEventRoutingServiceImpl.class);

    private static final Set<String> SECURITY_ACTION_TYPES = new HashSet<>(Arrays.asList(
        "LOGIN", "LOGOUT", "AUTHENTICATION_FAILURE", "AUTHORIZATION_FAILURE", 
        "PASSWORD_CHANGE", "ROLE_CHANGE", "PERMISSION_CHANGE", "ACCESS_DENIED",
        "SECURITY_VIOLATION", "SUSPICIOUS_ACTIVITY", "DATA_BREACH", "UNAUTHORIZED_ACCESS"
    ));

    private static final Set<String> SYSTEM_ACTION_TYPES = new HashSet<>(Arrays.asList(
        "SYSTEM_START", "SYSTEM_STOP", "CONFIGURATION_CHANGE", "BACKUP_CREATED",
        "BACKUP_RESTORED", "MAINTENANCE_START", "MAINTENANCE_END", "ERROR_OCCURRED",
        "PERFORMANCE_ALERT", "CAPACITY_WARNING", "SERVICE_UNAVAILABLE", "HEALTH_CHECK"
    ));

    private static final Set<String> HIGH_PRIORITY_ACTIONS = new HashSet<>(Arrays.asList(
        "DELETE", "AUTHENTICATION_FAILURE", "AUTHORIZATION_FAILURE", "SECURITY_VIOLATION",
        "DATA_BREACH", "UNAUTHORIZED_ACCESS", "SYSTEM_STOP", "ERROR_OCCURRED", "SERVICE_UNAVAILABLE"
    ));

    private static final Set<String> HIGH_PRIORITY_COMPLIANCE_TYPES = new HashSet<>(Arrays.asList(
        "SOX", "GDPR", "PCI_DSS", "HIPAA", "IFRS16"
    ));

    private static final Set<String> MULTI_TOPIC_ACTIONS = new HashSet<>(Arrays.asList(
        "DELETE", "ROLE_CHANGE", "PERMISSION_CHANGE", "CONFIGURATION_CHANGE"
    ));

    @Override
    public String determineEventCategory(AuditTrailEvent event) {
        String actionType = event.getActionType();
        
        if (actionType == null) {
            log.warn("Audit event {} has null action type, defaulting to BUSINESS category", event.getEventId());
            return "BUSINESS";
        }

        if (SECURITY_ACTION_TYPES.contains(actionType.toUpperCase())) {
            return "SECURITY";
        }

        if (SYSTEM_ACTION_TYPES.contains(actionType.toUpperCase())) {
            return "SYSTEM";
        }

        return "BUSINESS";
    }

    @Override
    public String determineEventCategory(ComplianceAuditEvent event) {
        return "COMPLIANCE";
    }

    @Override
    public boolean shouldRouteToMultipleTopics(AuditTrailEvent event) {
        String actionType = event.getActionType();
        
        if (actionType == null) {
            return false;
        }

        return MULTI_TOPIC_ACTIONS.contains(actionType.toUpperCase());
    }

    @Override
    public String[] getMultipleCategories(AuditTrailEvent event) {
        String actionType = event.getActionType();
        
        if (actionType == null) {
            return new String[]{"BUSINESS"};
        }

        String primaryCategory = determineEventCategory(event);
        
        switch (actionType.toUpperCase()) {
            case "DELETE":
                return new String[]{"BUSINESS", "SECURITY"};
            case "ROLE_CHANGE":
            case "PERMISSION_CHANGE":
                return new String[]{"BUSINESS", "SECURITY"};
            case "CONFIGURATION_CHANGE":
                return new String[]{"BUSINESS", "SYSTEM"};
            default:
                return new String[]{primaryCategory};
        }
    }

    @Override
    public boolean isHighPriorityEvent(AuditTrailEvent event) {
        String actionType = event.getActionType();
        
        if (actionType == null) {
            return false;
        }

        return HIGH_PRIORITY_ACTIONS.contains(actionType.toUpperCase());
    }

    @Override
    public boolean isHighPriorityEvent(ComplianceAuditEvent event) {
        String complianceType = event.getComplianceType();
        String riskLevel = event.getRiskLevel();
        Boolean remediationRequired = event.getRemediationRequired();
        
        if (complianceType != null && HIGH_PRIORITY_COMPLIANCE_TYPES.contains(complianceType.toUpperCase())) {
            return true;
        }

        if (riskLevel != null && ("HIGH".equalsIgnoreCase(riskLevel) || "CRITICAL".equalsIgnoreCase(riskLevel))) {
            return true;
        }

        return Boolean.TRUE.equals(remediationRequired);
    }
}
