package io.github.erp.internal.acl.reporting;

import io.github.erp.internal.acl.dto.ReportingData;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CrossContextReportingACL implements ReportingDataHarmonizationACL {

    @Override
    public Optional<ReportingData> harmonizeAssetData(Object assetEntity) {
        if (!(assetEntity instanceof AssetRegistrationDTO)) {
            return Optional.empty();
        }
        
        AssetRegistrationDTO asset = (AssetRegistrationDTO) assetEntity;
        
        Map<String, BigDecimal> metrics = new HashMap<>();
        metrics.put("assetCost", asset.getAssetCost());
        metrics.put("historicalCost", asset.getHistoricalCost());
        
        Map<String, String> attributes = new HashMap<>();
        attributes.put("assetNumber", asset.getAssetNumber());
        attributes.put("assetTag", asset.getAssetTag());
        attributes.put("assetDetails", asset.getAssetDetails());
        
        return Optional.of(ReportingData.builder()
            .entityId(asset.getId().toString())
            .entityType("ASSET")
            .reportingDate(LocalDate.now())
            .financialMetrics(metrics)
            .attributes(attributes)
            .reportingPeriod(getCurrentReportingPeriod())
            .build());
    }

    @Override
    public Optional<ReportingData> harmonizeFinancialData(Object financialEntity) {
        if (!(financialEntity instanceof SettlementDTO)) {
            return Optional.empty();
        }
        
        SettlementDTO settlement = (SettlementDTO) financialEntity;
        
        Map<String, BigDecimal> metrics = new HashMap<>();
        metrics.put("paymentAmount", settlement.getPaymentAmount());
        
        Map<String, String> attributes = new HashMap<>();
        attributes.put("paymentNumber", settlement.getPaymentNumber());
        attributes.put("description", settlement.getDescription());
        
        return Optional.of(ReportingData.builder()
            .entityId(settlement.getId().toString())
            .entityType("SETTLEMENT")
            .reportingDate(settlement.getPaymentDate())
            .financialMetrics(metrics)
            .attributes(attributes)
            .reportingPeriod(getCurrentReportingPeriod())
            .build());
    }

    @Override
    public Optional<ReportingData> harmonizeLeaseData(Object leaseEntity) {
        if (!(leaseEntity instanceof LeasePaymentDTO)) {
            return Optional.empty();
        }
        
        LeasePaymentDTO leasePayment = (LeasePaymentDTO) leaseEntity;
        
        Map<String, BigDecimal> metrics = new HashMap<>();
        metrics.put("paymentAmount", leasePayment.getPaymentAmount());
        
        Map<String, String> attributes = new HashMap<>();
        attributes.put("leaseContractId", leasePayment.getLeaseContract() != null ? 
            leasePayment.getLeaseContract().getId().toString() : "UNKNOWN");
        
        return Optional.of(ReportingData.builder()
            .entityId(leasePayment.getId().toString())
            .entityType("LEASE_PAYMENT")
            .reportingDate(leasePayment.getPaymentDate())
            .financialMetrics(metrics)
            .attributes(attributes)
            .reportingPeriod(getCurrentReportingPeriod())
            .build());
    }

    @Override
    public Optional<ReportingData> harmonizeWIPData(Object wipEntity) {
        if (!(wipEntity instanceof WorkInProgressRegistrationDTO)) {
            return Optional.empty();
        }
        
        WorkInProgressRegistrationDTO wip = (WorkInProgressRegistrationDTO) wipEntity;
        
        Map<String, BigDecimal> metrics = new HashMap<>();
        metrics.put("instalmentAmount", wip.getInstalmentAmount());
        metrics.put("levelOfCompletion", BigDecimal.valueOf(wip.getLevelOfCompletion() != null ? 
            wip.getLevelOfCompletion() : 0.0));
        
        Map<String, String> attributes = new HashMap<>();
        attributes.put("sequenceNumber", wip.getSequenceNumber());
        attributes.put("particulars", wip.getParticulars());
        attributes.put("completed", wip.getCompleted() != null ? wip.getCompleted().toString() : "false");
        
        return Optional.of(ReportingData.builder()
            .entityId(wip.getId().toString())
            .entityType("WIP")
            .reportingDate(wip.getInstalmentDate())
            .financialMetrics(metrics)
            .attributes(attributes)
            .reportingPeriod(getCurrentReportingPeriod())
            .build());
    }

    @Override
    public List<ReportingData> aggregateContextData(List<Object> entities) {
        return entities.stream()
            .map(this::harmonizeEntityData)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    @Override
    public boolean validateReportingData(ReportingData data) {
        return data != null &&
               data.getEntityId() != null &&
               data.getEntityType() != null &&
               data.getReportingDate() != null;
    }

    private Optional<ReportingData> harmonizeEntityData(Object entity) {
        if (entity instanceof AssetRegistrationDTO) {
            return harmonizeAssetData(entity);
        } else if (entity instanceof SettlementDTO) {
            return harmonizeFinancialData(entity);
        } else if (entity instanceof LeasePaymentDTO) {
            return harmonizeLeaseData(entity);
        } else if (entity instanceof WorkInProgressRegistrationDTO) {
            return harmonizeWIPData(entity);
        }
        return Optional.empty();
    }

    private String getCurrentReportingPeriod() {
        LocalDate now = LocalDate.now();
        return now.getYear() + "-Q" + ((now.getMonthValue() - 1) / 3 + 1);
    }
}
