package io.github.erp.erp.assets.depreciation.model;

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

/**
 * includes list of asset ids that are to be depreciated in a single batch, and other
 * important batch details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepreciationBatchMessage implements Serializable {

    private UUID messageCorrelationId;
    private String jobId;
    private String batchId;
    private int batchSize;
    private List<String> assetIds;
    private BigDecimal initialCost;
    private LocalDateTime createdAt;
    private int startIndex;
    private int endIndex;
    private boolean isLastBatch;
    private int enqueuedCount;
    private int sequenceNumber;
    private int totalItems;
    private ContextInstance contextInstance;
    private boolean processed;
    private int processedMessagesCount;
    private int numberOfBatches;

    public static DepreciationBatchMessageBuilder builder() {
        return new DepreciationBatchMessageBuilder();
    }

    public static class DepreciationBatchMessageBuilder {
        private UUID messageCorrelationId;
        private String jobId;
        private String batchId;
        private int batchSize;
        private List<String> assetIds;
        private BigDecimal initialCost;
        private LocalDateTime createdAt;
        private int startIndex;
        private int endIndex;
        private boolean isLastBatch;
        private int enqueuedCount;
        private int sequenceNumber;
        private int totalItems;
        private ContextInstance contextInstance;
        private boolean processed;
        private int processedMessagesCount;
        private int numberOfBatches;

        public DepreciationBatchMessageBuilder messageCorrelationId(UUID messageCorrelationId) {
            this.messageCorrelationId = messageCorrelationId;
            return this;
        }

        public DepreciationBatchMessageBuilder jobId(String jobId) {
            this.jobId = jobId;
            return this;
        }

        public DepreciationBatchMessageBuilder batchId(String batchId) {
            this.batchId = batchId;
            return this;
        }

        public DepreciationBatchMessageBuilder batchSize(int batchSize) {
            this.batchSize = batchSize;
            return this;
        }

        public DepreciationBatchMessageBuilder assetIds(List<String> assetIds) {
            this.assetIds = assetIds;
            return this;
        }

        public DepreciationBatchMessageBuilder initialCost(BigDecimal initialCost) {
            this.initialCost = initialCost;
            return this;
        }

        public DepreciationBatchMessageBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DepreciationBatchMessageBuilder startIndex(int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public DepreciationBatchMessageBuilder endIndex(int endIndex) {
            this.endIndex = endIndex;
            return this;
        }

        public DepreciationBatchMessageBuilder isLastBatch(boolean isLastBatch) {
            this.isLastBatch = isLastBatch;
            return this;
        }

        public DepreciationBatchMessageBuilder enqueuedCount(int enqueuedCount) {
            this.enqueuedCount = enqueuedCount;
            return this;
        }

        public DepreciationBatchMessageBuilder sequenceNumber(int sequenceNumber) {
            this.sequenceNumber = sequenceNumber;
            return this;
        }

        public DepreciationBatchMessageBuilder totalItems(int totalItems) {
            this.totalItems = totalItems;
            return this;
        }

        public DepreciationBatchMessageBuilder contextInstance(ContextInstance contextInstance) {
            this.contextInstance = contextInstance;
            return this;
        }

        public DepreciationBatchMessageBuilder processed(boolean processed) {
            this.processed = processed;
            return this;
        }

        public DepreciationBatchMessageBuilder processedMessagesCount(int processedMessagesCount) {
            this.processedMessagesCount = processedMessagesCount;
            return this;
        }

        public DepreciationBatchMessageBuilder numberOfBatches(int numberOfBatches) {
            this.numberOfBatches = numberOfBatches;
            return this;
        }

        public DepreciationBatchMessage build() {
            DepreciationBatchMessage message = new DepreciationBatchMessage();
            message.setMessageCorrelationId(messageCorrelationId);
            message.setJobId(jobId);
            message.setBatchId(batchId);
            message.setBatchSize(batchSize);
            message.setAssetIds(assetIds);
            message.setInitialCost(initialCost);
            message.setCreatedAt(createdAt);
            message.setStartIndex(startIndex);
            message.setEndIndex(endIndex);
            message.setLastBatch(isLastBatch);
            message.setEnqueuedCount(enqueuedCount);
            message.setSequenceNumber(sequenceNumber);
            message.setTotalItems(totalItems);
            message.setContextInstance(contextInstance);
            message.setProcessed(processed);
            message.setProcessedMessagesCount(processedMessagesCount);
            message.setNumberOfBatches(numberOfBatches);
            return message;
        }
    }

    public UUID getMessageCorrelationId() { return messageCorrelationId; }
    public void setMessageCorrelationId(UUID messageCorrelationId) { this.messageCorrelationId = messageCorrelationId; }
    
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }
    
    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }
    
    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }
    
    public List<String> getAssetIds() { return assetIds; }
    public void setAssetIds(List<String> assetIds) { this.assetIds = assetIds; }
    
    public BigDecimal getInitialCost() { return initialCost; }
    public void setInitialCost(BigDecimal initialCost) { this.initialCost = initialCost; }
    
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
    
    public boolean isProcessed() { return processed; }
    public void setProcessed(boolean processed) { this.processed = processed; }
    
    public int getProcessedMessagesCount() { return processedMessagesCount; }
    public void setProcessedMessagesCount(int processedMessagesCount) { this.processedMessagesCount = processedMessagesCount; }
    
    public int getNumberOfBatches() { return numberOfBatches; }
    public void setNumberOfBatches(int numberOfBatches) { this.numberOfBatches = numberOfBatches; }
}
