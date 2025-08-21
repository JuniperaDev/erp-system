package io.github.erp.internal.acl.assets;

import io.github.erp.IntegrationTest;
import io.github.erp.context.assets.acl.SettlementToAssetAcquisitionACL;
import io.github.erp.internal.acl.dto.AssetAcquisitionData;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.SettlementService;
import io.github.erp.service.AssetCategoryService;
import io.github.erp.service.DealerService;
import io.github.erp.service.SettlementCurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class SettlementToAssetAcquisitionACLIntegrationTest {

    @Autowired
    private SettlementToAssetAcquisitionACL settlementToAssetAcquisitionACL;

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private AssetRegistrationService assetRegistrationService;

    @Autowired
    private AssetCategoryService assetCategoryService;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private SettlementCurrencyService settlementCurrencyService;

    @Test
    @Transactional
    void shouldCompleteEndToEndSettlementToAssetWorkflow() {
        DealerDTO dealer = createTestDealer();
        DealerDTO savedDealer = dealerService.save(dealer);

        SettlementCurrencyDTO currency = createTestCurrency();
        SettlementCurrencyDTO savedCurrency = settlementCurrencyService.save(currency);

        AssetCategoryDTO category = createTestAssetCategory();
        AssetCategoryDTO savedCategory = assetCategoryService.save(category);

        SettlementDTO settlement = createTestSettlement(savedDealer, savedCurrency);
        SettlementDTO savedSettlement = settlementService.save(settlement);

        Optional<AssetAcquisitionData> acquisitionData = 
            settlementToAssetAcquisitionACL.translateSettlementToAssetAcquisition(savedSettlement);

        assertThat(acquisitionData).isPresent();
        AssetAcquisitionData data = acquisitionData.get();

        AssetRegistrationDTO assetRegistration = new AssetRegistrationDTO();
        assetRegistration.setAssetCost(data.getAcquisitionCost());
        assetRegistration.setAcquiringTransactionId(savedSettlement.getId());
        assetRegistration.setAssetNumber("AST-INT-" + System.currentTimeMillis());
        assetRegistration.setAssetTag("TAG-INT-" + System.currentTimeMillis());
        assetRegistration.setAssetDetails("Integration test asset from settlement");
        assetRegistration.setAssetCategory(savedCategory);
        assetRegistration.setDealer(savedDealer);
        assetRegistration.setSettlementCurrency(savedCurrency);

        AssetRegistrationDTO savedAsset = assetRegistrationService.save(assetRegistration);

        assertThat(savedAsset.getAcquiringTransactionId()).isEqualTo(savedSettlement.getId());
        assertThat(savedAsset.getAssetCost()).isEqualTo(savedSettlement.getPaymentAmount());
        assertThat(savedAsset.getAssetNumber()).isNotNull();
        assertThat(savedAsset.getDealer().getId()).isEqualTo(savedDealer.getId());
        assertThat(savedAsset.getSettlementCurrency().getId()).isEqualTo(savedCurrency.getId());
    }

    @Test
    @Transactional
    void shouldValidateSettlementForAssetAcquisition() {
        DealerDTO dealer = createTestDealer();
        DealerDTO savedDealer = dealerService.save(dealer);

        SettlementCurrencyDTO currency = createTestCurrency();
        SettlementCurrencyDTO savedCurrency = settlementCurrencyService.save(currency);

        SettlementDTO settlement = createTestSettlement(savedDealer, savedCurrency);
        SettlementDTO savedSettlement = settlementService.save(settlement);

        boolean isValid = settlementToAssetAcquisitionACL.validateSettlementForAssetAcquisition(savedSettlement);

        assertThat(isValid).isTrue();
    }

    @Test
    @Transactional
    void shouldGenerateAssetNumberFromSettlement() {
        DealerDTO dealer = createTestDealer();
        DealerDTO savedDealer = dealerService.save(dealer);

        SettlementCurrencyDTO currency = createTestCurrency();
        SettlementCurrencyDTO savedCurrency = settlementCurrencyService.save(currency);

        SettlementDTO settlement = createTestSettlement(savedDealer, savedCurrency);
        SettlementDTO savedSettlement = settlementService.save(settlement);

        Optional<String> assetNumber = settlementToAssetAcquisitionACL.generateAssetNumber(savedSettlement);

        assertThat(assetNumber).isPresent();
        assertThat(assetNumber.get()).contains("AST-PAY-");
    }

    private SettlementDTO createTestSettlement(DealerDTO dealer, SettlementCurrencyDTO currency) {
        SettlementDTO settlement = new SettlementDTO();
        settlement.setPaymentNumber("PAY-INT-" + System.currentTimeMillis());
        settlement.setPaymentAmount(BigDecimal.valueOf(25000));
        settlement.setPaymentDate(LocalDate.now());
        settlement.setDescription("Integration test settlement for asset acquisition");
        settlement.setBiller(dealer);
        settlement.setSettlementCurrency(currency);
        return settlement;
    }

    private DealerDTO createTestDealer() {
        DealerDTO dealer = new DealerDTO();
        dealer.setDealerName("Integration Test Vendor " + System.currentTimeMillis());
        dealer.setTaxNumber("TAX-" + System.currentTimeMillis());
        dealer.setIdentificationDocumentNumber("ID-" + System.currentTimeMillis());
        return dealer;
    }

    private SettlementCurrencyDTO createTestCurrency() {
        SettlementCurrencyDTO currency = new SettlementCurrencyDTO();
        currency.setIso4217CurrencyCode("USD");
        currency.setCurrencyName("US Dollar");
        currency.setCountry("United States");
        return currency;
    }

    private AssetCategoryDTO createTestAssetCategory() {
        AssetCategoryDTO category = new AssetCategoryDTO();
        category.setAssetCategoryName("Integration Test Category " + System.currentTimeMillis());
        category.setDescription("Test category for integration testing");
        return category;
    }
}
