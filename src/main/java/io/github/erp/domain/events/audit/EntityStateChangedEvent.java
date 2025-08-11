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
@Table(name = "entity_state_changed_event")
public class EntityStateChangedEvent extends AbstractDomainEvent {

    @Column(name = "change_type")
    private String changeType;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "change_reason")
    private String changeReason;

    protected EntityStateChangedEvent() {
        super();
    }

    public EntityStateChangedEvent(String aggregateId, String aggregateType, String changeType,
                                 String fieldName, String oldValue, String newValue, String changedBy,
                                 String changeReason, UUID correlationId) {
        super(aggregateId, aggregateType, correlationId);
        this.changeType = changeType;
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.changeReason = changeReason;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    @Override
    public String toString() {
        return "EntityStateChangedEvent{" +
            "changeType='" + changeType + '\'' +
            ", fieldName='" + fieldName + '\'' +
            ", oldValue='" + oldValue + '\'' +
            ", newValue='" + newValue + '\'' +
            ", changedBy='" + changedBy + '\'' +
            ", changeReason='" + changeReason + '\'' +
            "} " + super.toString();
    }
}
