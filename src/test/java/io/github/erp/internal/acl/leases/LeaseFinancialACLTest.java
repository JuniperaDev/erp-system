package io.github.erp.internal.acl.leases;

import io.github.erp.internal.acl.dto.LeasePaymentData;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
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
class LeaseFinancialACLTest {

    @Mock
    private Validator validator;

    private LeaseFinancialACL acl;

    @BeforeEach
    void setUp() {
        acl = new LeaseFinancialACL(validator);
    }

    @Test
    void shouldTranslateLeasePaymentToSettlement() {
        LeasePaymentDTO leasePayment = createTestLeasePayment();
        when(validator.validate(any())).thenReturn(java.util.Collections.emptySet());

        Optional<SettlementDTO> result = acl.translateLeasePaymentToSettlement(leasePayment);

        assertThat(result).isPresent();
        SettlementDTO settlement = result.get();
        assertThat(settlement.getPaymentAmount()).isEqualTo(leasePayment.getPaymentAmount());
        assertThat(settlement.getPaymentDate()).isEqualTo(leasePayment.getPaymentDate());
        assertThat(settlement.getDescription()).contains("Lease payment for contract");
    }

    @Test
    void shouldExtractLeasePaymentData() {
        LeasePaymentDTO leasePayment = createTestLeasePayment();

        Optional<LeasePaymentData> result = acl.extractLeasePaymentData(leasePayment);

        assertThat(result).isPresent();
        LeasePaymentData data = result.get();
        assertThat(data.getPaymentAmount()).isEqualTo(leasePayment.getPaymentAmount());
        assertThat(data.getPaymentDate()).isEqualTo(leasePayment.getPaymentDate());
        assertThat(data.getPaymentType()).isEqualTo("LEASE_PAYMENT");
    }

    @Test
    void shouldValidateLeasePaymentForFinancialIntegration() {
        LeasePaymentDTO leasePayment = createTestLeasePayment();

        boolean isValid = acl.validateLeasePaymentForFinancialIntegration(leasePayment);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldGenerateSettlementReference() {
        LeasePaymentDTO leasePayment = createTestLeasePayment();

        Optional<String> reference = acl.generateSettlementReference(leasePayment);

        assertThat(reference).isPresent();
        assertThat(reference.get()).startsWith("LEASE-");
        assertThat(reference.get()).contains("LEASE-001");
    }

    private LeasePaymentDTO createTestLeasePayment() {
        LeasePaymentDTO leasePayment = new LeasePaymentDTO();
        leasePayment.setId(1L);
        leasePayment.setPaymentAmount(BigDecimal.valueOf(5000));
        leasePayment.setPaymentDate(LocalDate.now());

        DetailedLeaseContractDTO contract = new DetailedLeaseContractDTO();
        contract.setId(1L);
        contract.setBookingId("LEASE-001");
        contract.setLeaseTitle("Test Lease Contract");
        leasePayment.setLeaseContract(contract);

        return leasePayment;
    }
}
