package io.github.erp.service;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.AssetProcurementLink;
import io.github.erp.domain.enumeration.ProcurementEntityType;
import io.github.erp.repository.AssetProcurementLinkRepository;
import io.github.erp.service.dto.AssetProcurementLinkDTO;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
class AssetProcurementLinkServiceIT {

    private static final Long DEFAULT_ASSET_ID = 1L;
    private static final Long DEFAULT_PROCUREMENT_ENTITY_ID = 1L;
    private static final ProcurementEntityType DEFAULT_PROCUREMENT_ENTITY_TYPE = ProcurementEntityType.PURCHASE_ORDER;
    private static final LocalDate DEFAULT_LINK_DATE = LocalDate.ofEpochDay(0L);

    @Autowired
    private AssetProcurementLinkRepository assetProcurementLinkRepository;

    @Autowired
    private AssetProcurementLinkService assetProcurementLinkService;

    @Autowired
    private EntityManager em;

    private AssetProcurementLink assetProcurementLink;

    public static AssetProcurementLink createEntity(EntityManager em) {
        AssetProcurementLink assetProcurementLink = new AssetProcurementLink()
            .assetId(DEFAULT_ASSET_ID)
            .procurementEntityId(DEFAULT_PROCUREMENT_ENTITY_ID)
            .procurementEntityType(DEFAULT_PROCUREMENT_ENTITY_TYPE)
            .linkDate(DEFAULT_LINK_DATE);
        return assetProcurementLink;
    }

    @BeforeEach
    public void initTest() {
        assetProcurementLink = createEntity(em);
    }

    @Test
    @Transactional
    void createAssetProcurementLink() {
        int databaseSizeBeforeCreate = assetProcurementLinkRepository.findAll().size();

        AssetProcurementLinkDTO assetProcurementLinkDTO = new AssetProcurementLinkDTO();
        assetProcurementLinkDTO.setAssetId(DEFAULT_ASSET_ID);
        assetProcurementLinkDTO.setProcurementEntityId(DEFAULT_PROCUREMENT_ENTITY_ID);
        assetProcurementLinkDTO.setProcurementEntityType(DEFAULT_PROCUREMENT_ENTITY_TYPE);
        assetProcurementLinkDTO.setLinkDate(DEFAULT_LINK_DATE);

        AssetProcurementLinkDTO result = assetProcurementLinkService.save(assetProcurementLinkDTO);

        List<AssetProcurementLink> assetProcurementLinkList = assetProcurementLinkRepository.findAll();
        assertThat(assetProcurementLinkList).hasSize(databaseSizeBeforeCreate + 1);
        AssetProcurementLink testAssetProcurementLink = assetProcurementLinkList.get(assetProcurementLinkList.size() - 1);
        assertThat(testAssetProcurementLink.getAssetId()).isEqualTo(DEFAULT_ASSET_ID);
        assertThat(testAssetProcurementLink.getProcurementEntityId()).isEqualTo(DEFAULT_PROCUREMENT_ENTITY_ID);
        assertThat(testAssetProcurementLink.getProcurementEntityType()).isEqualTo(DEFAULT_PROCUREMENT_ENTITY_TYPE);
        assertThat(testAssetProcurementLink.getLinkDate()).isEqualTo(DEFAULT_LINK_DATE);
    }

    @Test
    @Transactional
    void findByAssetId() {
        assetProcurementLinkRepository.saveAndFlush(assetProcurementLink);

        List<AssetProcurementLinkDTO> result = assetProcurementLinkService.findByAssetId(DEFAULT_ASSET_ID);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAssetId()).isEqualTo(DEFAULT_ASSET_ID);
    }
}
