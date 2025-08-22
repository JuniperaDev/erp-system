package io.github.erp.cqrs.asset.eventhandlers;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.erp.cqrs.asset.readmodel.AssetReportReadModel;
import io.github.erp.cqrs.asset.repositories.AssetReportReadModelRepository;
import io.github.erp.domain.AssetCategory;
import io.github.erp.domain.AssetRegistration;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.domain.events.asset.AssetCreatedEvent;
import io.github.erp.repository.AssetRegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetReportEventHandlerTest {

    @Mock
    private AssetReportReadModelRepository assetReportReadModelRepository;

    @Mock
    private AssetRegistrationRepository assetRegistrationRepository;

    private AssetReportEventHandler assetReportEventHandler;

    private AssetRegistration assetRegistration;
    private AssetCategory assetCategory;
    private Dealer dealer;
    private ServiceOutlet serviceOutlet;

    @BeforeEach
    void setUp() {
        assetReportEventHandler = new AssetReportEventHandler(
            assetReportReadModelRepository,
            assetRegistrationRepository
        );

        assetCategory = new AssetCategory();
        assetCategory.setId(1L);
        assetCategory.setAssetCategoryName("Equipment");

        dealer = new Dealer();
        dealer.setId(1L);
        dealer.setDealerName("Test Dealer");

        serviceOutlet = new ServiceOutlet();
        serviceOutlet.setId(1L);
        serviceOutlet.setOutletCode("OUT-001");

        assetRegistration = new AssetRegistration();
        assetRegistration.setId(100L);
        assetRegistration.setAssetNumber("AST-001");
        assetRegistration.setAssetTag("TAG-001");
        assetRegistration.setAssetDetails("Test Asset Details");
        assetRegistration.setAssetCost(BigDecimal.valueOf(10000));
        assetRegistration.setCapitalizationDate(LocalDate.now());
        assetRegistration.setAssetCategory(assetCategory);
        assetRegistration.setDealer(dealer);
        assetRegistration.setMainServiceOutlet(serviceOutlet);
    }

    @Test
    void shouldHandleAssetCreatedEvent() {
        AssetCreatedEvent event = new AssetCreatedEvent(
            "100",
            "AST-001",
            "TAG-001",
            "Test Asset Details",
            BigDecimal.valueOf(10000),
            LocalDate.now(),
            UUID.randomUUID()
        );

        when(assetRegistrationRepository.findById(100L)).thenReturn(Optional.of(assetRegistration));

        assetReportEventHandler.handleAssetCreated(event);

        ArgumentCaptor<AssetReportReadModel> captor = ArgumentCaptor.forClass(AssetReportReadModel.class);
        verify(assetReportReadModelRepository).save(captor.capture());

        AssetReportReadModel savedReadModel = captor.getValue();
        assertThat(savedReadModel.getAssetId()).isEqualTo(100L);
        assertThat(savedReadModel.getAssetNumber()).isEqualTo("AST-001");
        assertThat(savedReadModel.getAssetDescription()).isEqualTo("Test Asset Details");
        assertThat(savedReadModel.getAssetCost()).isEqualTo(BigDecimal.valueOf(10000));
        assertThat(savedReadModel.getCategoryName()).isEqualTo("Equipment");
        assertThat(savedReadModel.getDealerName()).isEqualTo("Test Dealer");
        assertThat(savedReadModel.getServiceOutletCode()).isEqualTo("OUT-001");
    }
}
