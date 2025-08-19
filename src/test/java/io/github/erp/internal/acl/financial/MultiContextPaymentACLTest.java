package io.github.erp.internal.acl.financial;

import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.dto.DealerDTO;
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
class MultiContextPaymentACLTest {

    @Mock
    private Validator validator;

    private MultiContextPaymentACL acl;

    @BeforeEach
    void setUp() {
        acl = new MultiContextPaymentACL(validator);
    }

    @Test
    void shouldHarmonizePaymentData() {
        SettlementDTO payment = createTestPayment();
        when(validator.validate(any())).thenReturn(java.util.Collections.emptySet());

        Optional<SettlementDTO> result = acl.harmonizePaymentData(payment);

        assertThat(result).isPresent();
        SettlementDTO harmonized = result.get();
        assertThat(harmonized.getPaymentNumber()).startsWith("PAY-");
        assertThat(harmonized.getPaymentAmount()).isEqualTo(payment.getPaymentAmount());
        assertThat(harmonized.getDescription()).isEqualTo(payment.getDescription().trim());
    }

    @Test
    void shouldValidatePaymentIntegrity() {
        SettlementDTO payment = createTestPayment();

        boolean isValid = acl.validatePaymentIntegrity(payment);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldStandardizePaymentReference() {
        SettlementDTO payment = createTestPayment();
        payment.setPaymentNumber("test-payment-001");

        Optional<String> standardized = acl.standardizePaymentReference(payment);

        assertThat(standardized).isPresent();
        assertThat(standardized.get()).isEqualTo("PAY-TEST-PAYMENT-001");
    }

    @Test
    void shouldExtractPaymentCategory() {
        SettlementDTO payment = createTestPayment();

        Optional<String> category = acl.extractPaymentCategory(payment);

        assertThat(category).isPresent();
        assertThat(category.get()).isEqualTo("Test Category");
    }

    private SettlementDTO createTestPayment() {
        SettlementDTO payment = new SettlementDTO();
        payment.setId(1L);
        payment.setPaymentNumber("PAY-001");
        payment.setPaymentAmount(BigDecimal.valueOf(1000));
        payment.setPaymentDate(LocalDate.now());
        payment.setDescription("  Test payment description  ");

        SettlementCurrencyDTO currency = new SettlementCurrencyDTO();
        currency.setIso4217CurrencyCode("USD");
        payment.setSettlementCurrency(currency);

        DealerDTO biller = new DealerDTO();
        biller.setId(1L);
        biller.setDealerName("Test Biller");
        payment.setBiller(biller);

        PaymentCategoryDTO category = new PaymentCategoryDTO();
        category.setCategoryName("Test Category");
        payment.setPaymentCategory(category);

        return payment;
    }
}
