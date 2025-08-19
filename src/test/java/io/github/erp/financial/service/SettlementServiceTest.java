package io.github.erp.financial.service;

import io.github.erp.financial.domain.Settlement;
import io.github.erp.financial.repository.SettlementRepository;
import io.github.erp.financial.service.dto.SettlementDTO;
import io.github.erp.financial.service.impl.SettlementServiceImpl;
import io.github.erp.financial.service.mapper.SettlementMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private SettlementMapper settlementMapper;

    private SettlementService settlementService;

    private Settlement settlement;
    private SettlementDTO settlementDTO;

    @BeforeEach
    void setUp() {
        settlementService = new SettlementServiceImpl(settlementRepository, settlementMapper);

        settlement = new Settlement();
        settlement.setId(1L);
        settlement.setPaymentNumber("PAY-001");
        settlement.setPaymentAmount(BigDecimal.valueOf(1000.00));
        settlement.setPaymentDate(LocalDate.now());

        settlementDTO = new SettlementDTO();
        settlementDTO.setId(1L);
        settlementDTO.setPaymentNumber("PAY-001");
        settlementDTO.setPaymentAmount(BigDecimal.valueOf(1000.00));
        settlementDTO.setPaymentDate(LocalDate.now());
    }

    @Test
    void shouldSaveSettlement() {
        when(settlementMapper.toEntity(settlementDTO)).thenReturn(settlement);
        when(settlementRepository.save(settlement)).thenReturn(settlement);
        when(settlementMapper.toDto(settlement)).thenReturn(settlementDTO);

        SettlementDTO result = settlementService.save(settlementDTO);

        assertThat(result).isEqualTo(settlementDTO);
        verify(settlementRepository).save(settlement);
    }

    @Test
    void shouldFindAllSettlements() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Settlement> settlementPage = new PageImpl<>(Arrays.asList(settlement));
        when(settlementRepository.findAll(pageable)).thenReturn(settlementPage);
        when(settlementMapper.toDto(settlement)).thenReturn(settlementDTO);

        Page<SettlementDTO> result = settlementService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(settlementDTO);
    }

    @Test
    void shouldFindSettlementById() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));
        when(settlementMapper.toDto(settlement)).thenReturn(settlementDTO);

        Optional<SettlementDTO> result = settlementService.findOne(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(settlementDTO);
    }

    @Test
    void shouldDeleteSettlement() {
        settlementService.delete(1L);

        verify(settlementRepository).deleteById(1L);
    }
}
