package io.github.erp.internal.acl.assets;

import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.internal.acl.dto.ProjectCostData;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.dto.ServiceOutletDTO;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WIPToAssetRegistrationACLTest {

    @Mock
    private Validator validator;

    private WIPToAssetRegistrationACL acl;

    @BeforeEach
    void setUp() {
        acl = new WIPToAssetRegistrationACL(validator);
    }

    @Test
    void shouldTranslateWIPToAssetRegistration() {
        WorkInProgressRegistrationDTO wip = createTestWIP();
        when(validator.validate(any())).thenReturn(java.util.Collections.emptySet());

        Optional<AssetAcquisitionData> result = acl.translateWIPToAssetRegistration(wip);

        assertThat(result).isPresent();
        AssetAcquisitionData data = result.get();
        assertThat(data.getAcquisitionCost()).isEqualTo(wip.getInstalmentAmount());
        assertThat(data.getAcquisitionDate()).isEqualTo(wip.getInstalmentDate());
        assertThat(data.getDescription()).isEqualTo(wip.getParticulars());
    }

    @Test
    void shouldExtractProjectCostData() {
        WorkInProgressRegistrationDTO wip = createTestWIP();

        Optional<ProjectCostData> result = acl.extractProjectCostData(wip);

        assertThat(result).isPresent();
        ProjectCostData data = result.get();
        assertThat(data.getProjectCode()).isEqualTo(wip.getSequenceNumber());
        assertThat(data.getTotalCost()).isEqualTo(wip.getInstalmentAmount());
        assertThat(data.getCompletionPercentage()).isEqualTo(BigDecimal.valueOf(100.0));
    }

    @Test
    void shouldValidateWIPForAssetTransfer() {
        WorkInProgressRegistrationDTO wip = createTestWIP();

        boolean isValid = acl.validateWIPForAssetTransfer(wip);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseForIncompleteWIP() {
        WorkInProgressRegistrationDTO wip = createTestWIP();
        wip.setCompleted(false);
        wip.setLevelOfCompletion(50.0);

        boolean isValid = acl.validateWIPForAssetTransfer(wip);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldGenerateAssetNumberFromWIP() {
        WorkInProgressRegistrationDTO wip = createTestWIP();

        Optional<String> assetNumber = acl.generateAssetNumberFromWIP(wip);

        assertThat(assetNumber).isPresent();
        assertThat(assetNumber.get()).isEqualTo("AST-WIP-WIP-001");
    }

    private WorkInProgressRegistrationDTO createTestWIP() {
        WorkInProgressRegistrationDTO wip = new WorkInProgressRegistrationDTO();
        wip.setId(1L);
        wip.setSequenceNumber("WIP-001");
        wip.setParticulars("Test WIP project");
        wip.setInstalmentAmount(BigDecimal.valueOf(50000));
        wip.setInstalmentDate(LocalDate.now());
        wip.setLevelOfCompletion(100.0);
        wip.setCompleted(true);

        DealerDTO dealer = new DealerDTO();
        dealer.setId(1L);
        dealer.setDealerName("Test Contractor");
        wip.setDealer(dealer);

        ServiceOutletDTO outlet = new ServiceOutletDTO();
        outlet.setOutletCode("CC-001");
        wip.setOutletCode(outlet);

        SettlementCurrencyDTO currency = new SettlementCurrencyDTO();
        currency.setIso4217CurrencyCode("USD");
        wip.setSettlementCurrency(currency);

        return wip;
    }
}
