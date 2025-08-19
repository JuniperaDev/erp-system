package io.github.erp.internal.acl.reporting;

import io.github.erp.internal.acl.dto.ReportingData;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CrossContextReportingACLTest {

    private CrossContextReportingACL acl;

    @BeforeEach
    void setUp() {
        acl = new CrossContextReportingACL();
    }

    @Test
    void shouldHarmonizeAssetData() {
        AssetRegistrationDTO asset = createTestAsset();

        Optional<ReportingData> result = acl.harmonizeAssetData(asset);

        assertThat(result).isPresent();
        ReportingData data = result.get();
        assertThat(data.getEntityType()).isEqualTo("ASSET");
        assertThat(data.getEntityId()).isEqualTo(asset.getId().toString());
        assertThat(data.getFinancialMetrics()).containsKey("assetCost");
        assertThat(data.getAttributes()).containsKey("assetNumber");
    }

    @Test
    void shouldHarmonizeFinancialData() {
        SettlementDTO settlement = createTestSettlement();

        Optional<ReportingData> result = acl.harmonizeFinancialData(settlement);

        assertThat(result).isPresent();
        ReportingData data = result.get();
        assertThat(data.getEntityType()).isEqualTo("SETTLEMENT");
        assertThat(data.getEntityId()).isEqualTo(settlement.getId().toString());
        assertThat(data.getFinancialMetrics()).containsKey("paymentAmount");
    }

    @Test
    void shouldHarmonizeLeaseData() {
        LeasePaymentDTO leasePayment = createTestLeasePayment();

        Optional<ReportingData> result = acl.harmonizeLeaseData(leasePayment);

        assertThat(result).isPresent();
        ReportingData data = result.get();
        assertThat(data.getEntityType()).isEqualTo("LEASE_PAYMENT");
        assertThat(data.getFinancialMetrics()).containsKey("paymentAmount");
    }

    @Test
    void shouldHarmonizeWIPData() {
        WorkInProgressRegistrationDTO wip = createTestWIP();

        Optional<ReportingData> result = acl.harmonizeWIPData(wip);

        assertThat(result).isPresent();
        ReportingData data = result.get();
        assertThat(data.getEntityType()).isEqualTo("WIP");
        assertThat(data.getFinancialMetrics()).containsKey("instalmentAmount");
        assertThat(data.getFinancialMetrics()).containsKey("levelOfCompletion");
    }

    @Test
    void shouldAggregateContextData() {
        List<Object> entities = Arrays.asList(
            createTestAsset(),
            createTestSettlement(),
            createTestLeasePayment(),
            createTestWIP()
        );

        List<ReportingData> result = acl.aggregateContextData(entities);

        assertThat(result).hasSize(4);
        assertThat(result.stream().map(ReportingData::getEntityType))
            .containsExactlyInAnyOrder("ASSET", "SETTLEMENT", "LEASE_PAYMENT", "WIP");
    }

    @Test
    void shouldValidateReportingData() {
        ReportingData validData = ReportingData.builder()
            .entityId("1")
            .entityType("TEST")
            .reportingDate(LocalDate.now())
            .build();

        boolean isValid = acl.validateReportingData(validData);

        assertThat(isValid).isTrue();
    }

    private AssetRegistrationDTO createTestAsset() {
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setId(1L);
        asset.setAssetNumber("AST-001");
        asset.setAssetCost(BigDecimal.valueOf(10000));
        return asset;
    }

    private SettlementDTO createTestSettlement() {
        SettlementDTO settlement = new SettlementDTO();
        settlement.setId(2L);
        settlement.setPaymentNumber("PAY-001");
        settlement.setPaymentAmount(BigDecimal.valueOf(5000));
        settlement.setPaymentDate(LocalDate.now());
        return settlement;
    }

    private LeasePaymentDTO createTestLeasePayment() {
        LeasePaymentDTO leasePayment = new LeasePaymentDTO();
        leasePayment.setId(3L);
        leasePayment.setPaymentAmount(BigDecimal.valueOf(3000));
        leasePayment.setPaymentDate(LocalDate.now());
        return leasePayment;
    }

    private WorkInProgressRegistrationDTO createTestWIP() {
        WorkInProgressRegistrationDTO wip = new WorkInProgressRegistrationDTO();
        wip.setId(4L);
        wip.setSequenceNumber("WIP-001");
        wip.setInstalmentAmount(BigDecimal.valueOf(7000));
        wip.setLevelOfCompletion(75.0);
        wip.setInstalmentDate(LocalDate.now());
        return wip;
    }
}
