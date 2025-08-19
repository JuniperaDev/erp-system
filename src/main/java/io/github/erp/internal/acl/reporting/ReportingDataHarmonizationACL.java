package io.github.erp.internal.acl.reporting;

import io.github.erp.internal.acl.dto.ReportingData;

import java.util.List;
import java.util.Optional;

public interface ReportingDataHarmonizationACL {
    
    Optional<ReportingData> harmonizeAssetData(Object assetEntity);
    
    Optional<ReportingData> harmonizeFinancialData(Object financialEntity);
    
    Optional<ReportingData> harmonizeLeaseData(Object leaseEntity);
    
    Optional<ReportingData> harmonizeWIPData(Object wipEntity);
    
    List<ReportingData> aggregateContextData(List<Object> entities);
    
    boolean validateReportingData(ReportingData data);
}
