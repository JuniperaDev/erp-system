package io.github.erp.internal.acl.depreciation;

import io.github.erp.internal.acl.dto.DepreciationData;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.dto.AssetRegistrationDTO;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.dto.DepreciationMethodDTO;
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
class AssetToDepreciationACLTest {

    @Mock
    private Validator validator;

    private AssetToDepreciationACL acl;

    @BeforeEach
    void setUp() {
        acl = new AssetToDepreciationACL(validator);
    }

    @Test
    void shouldTranslateAssetToDepreciationData() {
        AssetRegistrationDTO asset = createTestAsset();
        when(validator.validate(any())).thenReturn(java.util.Collections.emptySet());

        Optional<DepreciationData> result = acl.translateAssetToDepreciationData(asset);

        assertThat(result).isPresent();
        DepreciationData data = result.get();
        assertThat(data.getAssetId()).isEqualTo(asset.getId().toString());
        assertThat(data.getDepreciationMethod()).isEqualTo("STRAIGHT_LINE");
        assertThat(data.getDepreciationAmount()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldCreateDepreciationEntry() {
        AssetRegistrationDTO asset = createTestAsset();
        DepreciationData depreciationData = DepreciationData.builder()
            .assetId(asset.getId().toString())
            .depreciationAmount(BigDecimal.valueOf(2000))
            .depreciationDate(LocalDate.now())
            .depreciationMethod("STRAIGHT_LINE")
            .netBookValue(BigDecimal.valueOf(8000))
            .fiscalPeriod("2024-01")
            .build();

        Optional<DepreciationEntryDTO> result = acl.createDepreciationEntry(asset, depreciationData);

        assertThat(result).isPresent();
        DepreciationEntryDTO entry = result.get();
        assertThat(entry.getDepreciationAmount()).isEqualTo(depreciationData.getDepreciationAmount());
        assertThat(entry.getAssetNumber()).isEqualTo(asset.getId());
    }

    @Test
    void shouldValidateAssetForDepreciation() {
        AssetRegistrationDTO asset = createTestAsset();

        boolean isValid = acl.validateAssetForDepreciation(asset);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldDetermineDepreciationMethod() {
        AssetRegistrationDTO asset = createTestAsset();

        Optional<String> method = acl.determineDepreciationMethod(asset);

        assertThat(method).isPresent();
        assertThat(method.get()).isEqualTo("STRAIGHT_LINE");
    }

    private AssetRegistrationDTO createTestAsset() {
        AssetRegistrationDTO asset = new AssetRegistrationDTO();
        asset.setId(1L);
        asset.setAssetNumber("AST-001");
        asset.setAssetTag("TAG-001");
        asset.setAssetCost(BigDecimal.valueOf(10000));
        asset.setCapitalizationDate(LocalDate.now().minusMonths(1));

        AssetCategoryDTO category = new AssetCategoryDTO();
        category.setId(1L);
        category.setAssetCategoryName("Test Category");

        DepreciationMethodDTO depreciationMethod = new DepreciationMethodDTO();
        depreciationMethod.setDepreciationMethodName("STRAIGHT_LINE");
        category.setDepreciationMethod(depreciationMethod);

        asset.setAssetCategory(category);

        return asset;
    }
}
