package io.github.erp.erp.assets.nbv.model;

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
import io.github.erp.erp.assets.depreciation.context.ContextInstance;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NBVBatchMessage implements Serializable {

    private boolean processed;
    private int processedMessagesCount;
    private int numberOfBatches;
    private BigDecimal initialCost;
    private UUID messageCorrelationId;
    private long jobId;
    private long batchId;
    private int batchSize;
    private List<Long> assetIds;
    private LocalDateTime createdAt;
    private int startIndex;
    private int endIndex;
    private boolean isLastBatch;
    private int enqueuedCount;
    private int sequenceNumber;
    private int totalItems;
    private ContextInstance contextInstance;

    public static NBVBatchMessageBuilder builder() {
        return new NBVBatchMessageBuilder();
    }

    public static class NBVBatchMessageBuilder {
        private boolean processed;
        private int processedMessagesCount;
        private int numberOfBatches;
        private BigDecimal initialCost;
        private UUID messageCorrelationId;
        private long jobId;
        private long batchId;
        private int batchSize;
        private List<Long> assetIds;
        private LocalDateTime createdAt;
        private int startIndex;
        private int endIndex;
        private boolean isLastBatch;
        private int enqueuedCount;
        private int sequenceNumber;
        private int totalItems;
        private ContextInstance contextInstance;

        public NBVBatchMessageBuilder processed(boolean processed) {
            this.processed = processed;
            return this;
        }

        public NBVBatchMessageBuilder processedMessagesCount(int processedMessagesCount) {
            this.processedMessagesCount = processedMessagesCount;
            return this;
        }

        public NBVBatchMessageBuilder numberOfBatches(int numberOfBatches) {
            this.numberOfBatches = numberOfBatches;
            return this;
        }

        public NBVBatchMessageBuilder initialCost(BigDecimal initialCost) {
            this.initialCost = initialCost;
            return this;
        }

        public NBVBatchMessageBuilder messageCorrelationId(UUID messageCorrelationId) {
            this.messageCorrelationId = messageCorrelationId;
            return this;
        }

        public NBVBatchMessageBuilder jobId(long jobId) {
            this.jobId = jobId;
            return this;
        }

        public NBVBatchMessageBuilder batchId(long batchId) {
            this.batchId = batchId;
            return this;
        }

        public NBVBatchMessageBuilder batchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public NBVBatchMessageBuilder assetIds(List<Long> assetIds) {
            this.assetIds = assetIds;
            return this;
        }

        public NBVBatchMessageBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NBVBatchMessageBuilder startIndex(int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public NBVBatchMessageBuilder endIndex(int endIndex) {
            this.endIndex = endIndex;
            return this;
        }

        public NBVBatchMessageBuilder isLastBatch(boolean isLastBatch) {
            this.isLastBatch = isLastBatch;
            return this;
        }

        public NBVBatchMessageBuilder enqueuedCount(int enqueuedCount) {
            this.enqueuedCount = enqueuedCount;
            return this;
        }

        public NBVBatchMessageBuilder sequenceNumber(int sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public NBVBatchMessageBuilder totalItems(int totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public NBVBatchMessageBuilder contextInstance(ContextInstance contextInstance) {
            this.contextInstance = contextInstance;
            return this;
        }

        public NBVBatchMessage build() {
            NBVBatchMessage message = new NBVBatchMessage();
            message.setProcessed(processed);
            message.setProcessedMessagesCount(processedMessagesCount);
            message.setNumberOfBatches(numberOfBatches);
            message.setInitialCost(initialCost);
            message.setMessageCorrelationId(messageCorrelationId);
            message.setJobId(jobId);
            message.setBatchId(batchId);
            message.setBatchSize(batchSize);
            message.setAssetIds(assetIds);
            message.setCreatedAt(createdAt);
            message.setStartIndex(startIndex);
            message.setEndIndex(endIndex);
            message.setLastBatch(isLastBatch);
            message.setEnqueuedCount(enqueuedCount);
            message.setSequenceNumber(sequenceNumber);
            message.setTotalItems(totalItems);
            message.setContextInstance(contextInstance);
            return message;
        }
    }

    public boolean isProcessed() { return processed; }
    public void setProcessed(boolean processed) { this.processed = processed; }
    
    public int getProcessedMessagesCount() { return processedMessagesCount; }
    public void setProcessedMessagesCount(int processedMessagesCount) { this.processedMessagesCount = processedMessagesCount; }
    
    public int getNumberOfBatches() { return numberOfBatches; }
    public void setNumberOfBatches(int numberOfBatches) { this.numberOfBatches = numberOfBatches; }
    
    public BigDecimal getInitialCost() { return initialCost; }
    public void setInitialCost(BigDecimal initialCost) { this.initialCost = initialCost; }
    
    public UUID getMessageCorrelationId() { return messageCorrelationId; }
    public void setMessageCorrelationId(UUID messageCorrelationId) { this.messageCorrelationId = messageCorrelationId; }
    
    public long getJobId() { return jobId; }
    public void setJobId(long jobId) { this.jobId = jobId; }
    
    public long getBatchId() { return batchId; }
    public void setBatchId(long batchId) { this.batchId = batchId; }
    
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
    
    public List<Long> getAssetIds() { return assetIds; }
    public void setAssetIds(List<Long> assetIds) { this.assetIds = assetIds; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public int getStartIndex() { return startIndex; }
    public void setStartIndex(int startIndex) { this.startIndex = startIndex; }
    
    public int getEndIndex() { return endIndex; }
    public void setEndIndex(int endIndex) { this.endIndex = endIndex; }
    
    public boolean isLastBatch() { return isLastBatch; }
    public void setLastBatch(boolean lastBatch) { isLastBatch = lastBatch; }
    
    public int getEnqueuedCount() { return enqueuedCount; }
    public void setEnqueuedCount(int enqueuedCount) { this.enqueuedCount = enqueuedCount; }
    
    public int getSequenceNumber() { return sequenceNumber; }
    public void setSequenceNumber(int sequenceNumber) { this.sequenceNumber = sequenceNumber; }
    
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    
    public ContextInstance getContextInstance() { return contextInstance; }
    public void setContextInstance(ContextInstance contextInstance) { this.contextInstance = contextInstance; }
}
