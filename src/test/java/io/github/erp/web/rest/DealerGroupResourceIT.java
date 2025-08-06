package io.github.erp.web.rest;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.DealerGroup;
import io.github.erp.repository.DealerGroupRepository;
import io.github.erp.repository.search.DealerGroupSearchRepository;
import io.github.erp.service.criteria.DealerGroupCriteria;
import io.github.erp.service.dto.DealerGroupDTO;
import io.github.erp.service.mapper.DealerGroupMapper;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DealerGroupResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebMvc
@WithMockUser
class DealerGroupResourceIT {

    private static final String DEFAULT_GROUP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dealer-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/dealer-groups";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DealerGroupRepository dealerGroupRepository;

    @Autowired
    private DealerGroupMapper dealerGroupMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DealerGroupSearchRepositoryMockConfiguration
     */
    @Autowired
    private DealerGroupSearchRepository mockDealerGroupSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDealerGroupMockMvc;

    private DealerGroup dealerGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DealerGroup createEntity(EntityManager em) {
        DealerGroup dealerGroup = new DealerGroup()
            .groupName(DEFAULT_GROUP_NAME)
            .description(DEFAULT_DESCRIPTION)
            .notes(DEFAULT_NOTES);
        return dealerGroup;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DealerGroup createUpdatedEntity(EntityManager em) {
        DealerGroup dealerGroup = new DealerGroup()
            .groupName(UPDATED_GROUP_NAME)
            .description(UPDATED_DESCRIPTION)
            .notes(UPDATED_NOTES);
        return dealerGroup;
    }

    @BeforeEach
    public void initTest() {
        dealerGroup = createEntity(em);
    }

    @Test
    @Transactional
    void createDealerGroup() throws Exception {
        int databaseSizeBeforeCreate = dealerGroupRepository.findAll().size();
        DealerGroupDTO dealerGroupDTO = dealerGroupMapper.toDto(dealerGroup);
        restDealerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerGroupDTO))
            )
            .andExpect(status().isCreated());

        List<DealerGroup> dealerGroupList = dealerGroupRepository.findAll();
        assertThat(dealerGroupList).hasSize(databaseSizeBeforeCreate + 1);
        DealerGroup testDealerGroup = dealerGroupList.get(dealerGroupList.size() - 1);
        assertThat(testDealerGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
        assertThat(testDealerGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDealerGroup.getNotes()).isEqualTo(DEFAULT_NOTES);

        verify(mockDealerGroupSearchRepository, times(1)).save(testDealerGroup);
    }

    @Test
    @Transactional
    void createDealerGroupWithExistingId() throws Exception {
        dealerGroup.setId(1L);
        DealerGroupDTO dealerGroupDTO = dealerGroupMapper.toDto(dealerGroup);

        int databaseSizeBeforeCreate = dealerGroupRepository.findAll().size();

        restDealerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<DealerGroup> dealerGroupList = dealerGroupRepository.findAll();
        assertThat(dealerGroupList).hasSize(databaseSizeBeforeCreate);

        verify(mockDealerGroupSearchRepository, times(0)).save(dealerGroup);
    }

    @Test
    @Transactional
    void checkGroupNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = dealerGroupRepository.findAll().size();
        dealerGroup.setGroupName(null);

        DealerGroupDTO dealerGroupDTO = dealerGroupMapper.toDto(dealerGroup);

        restDealerGroupMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(dealerGroupDTO))
            )
            .andExpect(status().isBadRequest());

        List<DealerGroup> dealerGroupList = dealerGroupRepository.findAll();
        assertThat(dealerGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDealerGroups() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);

        restDealerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealerGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    @Test
    @Transactional
    void getDealerGroup() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);

        restDealerGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, dealerGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dealerGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()));
    }

    @Test
    @Transactional
    void getDealerGroupsByIdFiltering() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);

        Long id = dealerGroup.getId();

        defaultDealerGroupShouldBeFound("id.equals=" + id);
        defaultDealerGroupShouldNotBeFound("id.notEquals=" + id);

        defaultDealerGroupShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDealerGroupShouldNotBeFound("id.greaterThan=" + id);

        defaultDealerGroupShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDealerGroupShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDealerGroupsByGroupNameIsEqualToSomething() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);

        defaultDealerGroupShouldBeFound("groupName.equals=" + DEFAULT_GROUP_NAME);

        defaultDealerGroupShouldNotBeFound("groupName.equals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void getAllDealerGroupsByGroupNameIsNotEqualToSomething() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);

        defaultDealerGroupShouldNotBeFound("groupName.notEquals=" + DEFAULT_GROUP_NAME);

        defaultDealerGroupShouldBeFound("groupName.notEquals=" + UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    void getNonExistingDealerGroup() throws Exception {
        restDealerGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDealerGroup() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);

        int databaseSizeBeforeUpdate = dealerGroupRepository.findAll().size();

        DealerGroup updatedDealerGroup = dealerGroupRepository.findById(dealerGroup.getId()).get();
        em.detach(updatedDealerGroup);
        updatedDealerGroup.groupName(UPDATED_GROUP_NAME).description(UPDATED_DESCRIPTION).notes(UPDATED_NOTES);
        DealerGroupDTO dealerGroupDTO = dealerGroupMapper.toDto(updatedDealerGroup);

        restDealerGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dealerGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(dealerGroupDTO))
            )
            .andExpect(status().isOk());

        List<DealerGroup> dealerGroupList = dealerGroupRepository.findAll();
        assertThat(dealerGroupList).hasSize(databaseSizeBeforeUpdate);
        DealerGroup testDealerGroup = dealerGroupList.get(dealerGroupList.size() - 1);
        assertThat(testDealerGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
        assertThat(testDealerGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDealerGroup.getNotes()).isEqualTo(UPDATED_NOTES);

        verify(mockDealerGroupSearchRepository).save(testDealerGroup);
    }

    @Test
    @Transactional
    void deleteDealerGroup() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);

        int databaseSizeBeforeDelete = dealerGroupRepository.findAll().size();

        restDealerGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, dealerGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        List<DealerGroup> dealerGroupList = dealerGroupRepository.findAll();
        assertThat(dealerGroupList).hasSize(databaseSizeBeforeDelete - 1);

        verify(mockDealerGroupSearchRepository, times(1)).deleteById(dealerGroup.getId());
    }

    @Test
    @Transactional
    void searchDealerGroup() throws Exception {
        dealerGroupRepository.saveAndFlush(dealerGroup);
        when(mockDealerGroupSearchRepository.search("id:" + dealerGroup.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dealerGroup), PageRequest.of(0, 1), 1));

        restDealerGroupMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + dealerGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealerGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDealerGroupShouldBeFound(String filter) throws Exception {
        restDealerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dealerGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())));

        restDealerGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDealerGroupShouldNotBeFound(String filter) throws Exception {
        restDealerGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        restDealerGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }
}
