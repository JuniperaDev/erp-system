package io.github.erp.service.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AssetRegistrationListDTOTest {

    @Test
    void shouldCreateAssetRegistrationListDTO() {
        AssetRegistrationListDTO dto = new AssetRegistrationListDTO(
            1L, "ASSET001", "TAG001", "Test Asset", 
            BigDecimal.valueOf(10000), LocalDate.now(),
            "Computer Equipment", "Test Dealer", "USD"
        );

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getAssetNumber()).isEqualTo("ASSET001");
        assertThat(dto.getAssetTag()).isEqualTo("TAG001");
        assertThat(dto.getAssetDetails()).isEqualTo("Test Asset");
        assertThat(dto.getAssetCost()).isEqualTo(BigDecimal.valueOf(10000));
        assertThat(dto.getAssetCategoryName()).isEqualTo("Computer Equipment");
        assertThat(dto.getDealerName()).isEqualTo("Test Dealer");
        assertThat(dto.getSettlementCurrencyCode()).isEqualTo("USD");
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        AssetRegistrationListDTO dto1 = new AssetRegistrationListDTO();
        dto1.setId(1L);
        
        AssetRegistrationListDTO dto2 = new AssetRegistrationListDTO();
        dto2.setId(1L);
        
        AssetRegistrationListDTO dto3 = new AssetRegistrationListDTO();
        dto3.setId(2L);

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
    }

    @Test
    void shouldImplementToString() {
        AssetRegistrationListDTO dto = new AssetRegistrationListDTO(
            1L, "ASSET001", "TAG001", "Test Asset", 
            BigDecimal.valueOf(10000), LocalDate.now(),
            "Computer Equipment", "Test Dealer", "USD"
        );

        String toString = dto.toString();
        assertThat(toString).contains("AssetRegistrationListDTO");
        assertThat(toString).contains("id=1");
        assertThat(toString).contains("assetNumber='ASSET001'");
    }
}
