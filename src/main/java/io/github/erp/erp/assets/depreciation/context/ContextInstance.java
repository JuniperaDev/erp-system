package io.github.erp.erp.assets.depreciation.context;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContextInstance implements Serializable {

    private UUID depreciationJobCountUpContextId;
    private UUID depreciationJobCountDownContextId;
    private UUID depreciationBatchCountUpContextId;
    private UUID depreciationBatchCountDownContextId;
    private UUID messageCountContextId;
    private UUID depreciationAmountContextId;

    public static ContextInstanceBuilder builder() {
        return new ContextInstanceBuilder();
    }

    public static class ContextInstanceBuilder {
        private UUID depreciationJobCountUpContextId;
        private UUID depreciationJobCountDownContextId;
        private UUID depreciationBatchCountUpContextId;
        private UUID depreciationBatchCountDownContextId;
        private UUID messageCountContextId;
        private UUID depreciationAmountContextId;

        public ContextInstanceBuilder depreciationJobCountUpContextId(UUID depreciationJobCountUpContextId) {
            this.depreciationJobCountUpContextId = depreciationJobCountUpContextId;
            return this;
        }

        public ContextInstanceBuilder depreciationJobCountDownContextId(UUID depreciationJobCountDownContextId) {
            this.depreciationJobCountDownContextId = depreciationJobCountDownContextId;
            return this;
        }

        public ContextInstanceBuilder depreciationBatchCountUpContextId(UUID depreciationBatchCountUpContextId) {
            this.depreciationBatchCountUpContextId = depreciationBatchCountUpContextId;
            return this;
        }

        public ContextInstanceBuilder depreciationBatchCountDownContextId(UUID depreciationBatchCountDownContextId) {
            this.depreciationBatchCountDownContextId = depreciationBatchCountDownContextId;
            return this;
        }

        public ContextInstanceBuilder messageCountContextId(UUID messageCountContextId) {
            this.messageCountContextId = messageCountContextId;
            return this;
        }

        public ContextInstanceBuilder depreciationAmountContextId(UUID depreciationAmountContextId) {
            this.depreciationAmountContextId = depreciationAmountContextId;
            return this;
        }

        public ContextInstance build() {
            ContextInstance instance = new ContextInstance();
            instance.setDepreciationJobCountUpContextId(depreciationJobCountUpContextId);
            instance.setDepreciationJobCountDownContextId(depreciationJobCountDownContextId);
            instance.setDepreciationBatchCountUpContextId(depreciationBatchCountUpContextId);
            instance.setDepreciationBatchCountDownContextId(depreciationBatchCountDownContextId);
            instance.setMessageCountContextId(messageCountContextId);
            instance.setDepreciationAmountContextId(depreciationAmountContextId);
            return instance;
        }
    }

    public UUID getDepreciationJobCountUpContextId() { return depreciationJobCountUpContextId; }
    public void setDepreciationJobCountUpContextId(UUID depreciationJobCountUpContextId) { this.depreciationJobCountUpContextId = depreciationJobCountUpContextId; }
    
    public UUID getDepreciationJobCountDownContextId() { return depreciationJobCountDownContextId; }
    public void setDepreciationJobCountDownContextId(UUID depreciationJobCountDownContextId) { this.depreciationJobCountDownContextId = depreciationJobCountDownContextId; }
    
    public UUID getDepreciationBatchCountUpContextId() { return depreciationBatchCountUpContextId; }
    public void setDepreciationBatchCountUpContextId(UUID depreciationBatchCountUpContextId) { this.depreciationBatchCountUpContextId = depreciationBatchCountUpContextId; }
    
    public UUID getDepreciationBatchCountDownContextId() { return depreciationBatchCountDownContextId; }
    public void setDepreciationBatchCountDownContextId(UUID depreciationBatchCountDownContextId) { this.depreciationBatchCountDownContextId = depreciationBatchCountDownContextId; }
    
    public UUID getMessageCountContextId() { return messageCountContextId; }
    public void setMessageCountContextId(UUID messageCountContextId) { this.messageCountContextId = messageCountContextId; }
    
    public UUID getDepreciationAmountContextId() { return depreciationAmountContextId; }
    public void setDepreciationAmountContextId(UUID depreciationAmountContextId) { this.depreciationAmountContextId = depreciationAmountContextId; }
}
