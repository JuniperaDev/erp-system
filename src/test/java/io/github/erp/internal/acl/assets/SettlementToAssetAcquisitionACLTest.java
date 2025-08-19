package io.github.erp.internal.acl.assets;

import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import io.github.erp.service.dto.SettlementDTO;
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
class SettlementToAssetAcquisitionACLTest {

    @Mock
    private Validator validator;

    private SettlementToAssetAcquisitionACL acl;

    @BeforeEach
    void setUp() {
        acl = new SettlementToAssetAcquisitionACL(validator);
    }

    @Test
    void shouldTranslateSettlementToAssetAcquisition() {
        SettlementDTO settlement = createTestSettlement();
        when(validator.validate(any())).thenReturn(java.util.Collections.emptySet());

        Optional<AssetAcquisitionData> result = acl.translateSettlementToAssetAcquisition(settlement);

        assertThat(result).isPresent();
        AssetAcquisitionData data = result.get();
        assertThat(data.getAcquisitionCost()).isEqualTo(settlement.getPaymentAmount());
        assertThat(data.getAcquisitionDate()).isEqualTo(settlement.getPaymentDate());
        assertThat(data.getVendorName()).isEqualTo("Test Vendor");
        assertThat(data.getCurrencyCode()).isEqualTo("USD");
    }

    @Test
    void shouldReturnEmptyWhenSettlementIsNull() {
        Optional<AssetAcquisitionData> result = acl.translateSettlementToAssetAcquisition(null);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldValidateSettlementForAssetAcquisition() {
        SettlementDTO settlement = createTestSettlement();

        boolean isValid = acl.validateSettlementForAssetAcquisition(settlement);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalseForInvalidSettlement() {
        SettlementDTO settlement = new SettlementDTO();

        boolean isValid = acl.validateSettlementForAssetAcquisition(settlement);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldGenerateAssetNumber() {
        SettlementDTO settlement = createTestSettlement();

        Optional<String> assetNumber = acl.generateAssetNumber(settlement);

        assertThat(assetNumber).isPresent();
        assertThat(assetNumber.get()).isEqualTo("AST-PAY-001");
    }

    @Test
    void shouldExtractVendorInformation() {
        SettlementDTO settlement = createTestSettlement();

        Optional<String> vendorInfo = acl.extractVendorInformation(settlement);

        assertThat(vendorInfo).isPresent();
        assertThat(vendorInfo.get()).isEqualTo("Test Vendor");
    }

    private SettlementDTO createTestSettlement() {
        SettlementDTO settlement = new SettlementDTO();
        settlement.setId(1L);
        settlement.setPaymentNumber("PAY-001");
        settlement.setPaymentAmount(BigDecimal.valueOf(10000));
        settlement.setPaymentDate(LocalDate.now());
        settlement.setDescription("Test settlement");

        DealerDTO dealer = new DealerDTO();
        dealer.setId(1L);
        dealer.setDealerName("Test Vendor");
        settlement.setBiller(dealer);

        SettlementCurrencyDTO currency = new SettlementCurrencyDTO();
        currency.setIso4217CurrencyCode("USD");
        settlement.setSettlementCurrency(currency);

        return settlement;
    }
}
