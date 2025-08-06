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
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import io.github.erp.IntegrationTest;
import io.github.erp.domain.BusinessDocument;
import io.github.erp.domain.Dealer;
import io.github.erp.domain.FiscalMonth;
import io.github.erp.domain.DetailedLeaseContract;
import io.github.erp.domain.LeasePayment;
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.DetailedLeaseContractRepository;
import io.github.erp.repository.search.DetailedLeaseContractSearchRepository;
import io.github.erp.service.criteria.DetailedLeaseContractCriteria;
import io.github.erp.service.dto.DetailedLeaseContractDTO;
import io.github.erp.service.mapper.DetailedLeaseContractMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DetailedLeaseContractResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DetailedLeaseContractResourceIT {

    private static final String DEFAULT_BOOKING_ID = "AAAAAAAAAA";
    private static final String UPDATED_BOOKING_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LEASE_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_LEASE_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INCEPTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INCEPTION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_INCEPTION_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_COMMENCEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMMENCEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_COMMENCEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final UUID DEFAULT_SERIAL_NUMBER = UUID.randomUUID();
    private static final UUID UPDATED_SERIAL_NUMBER = UUID.randomUUID();

    private static final String ENTITY_API_URL = "/api/v2/detailed-lease-contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/v2/_search/detailed-lease-contracts";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailedLeaseContractRepository detailedLeaseContractRepository;

    @Autowired
    private DetailedLeaseContractMapper detailedLeaseContractMapper;

    /**
     * This repository is mocked in the io.github.erp.repository.search test package.
     *
     * @see io.github.erp.repository.search.DetailedLeaseContractSearchRepositoryMockConfiguration
     */
    @Autowired
    private DetailedLeaseContractSearchRepository mockDetailedLeaseContractSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailedLeaseContractMockMvc;

    private DetailedLeaseContract detailedLeaseContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailedLeaseContract createEntity(EntityManager em) {
        DetailedLeaseContract detailedLeaseContract = new DetailedLeaseContract()
            .bookingId(DEFAULT_BOOKING_ID)
            .leaseTitle(DEFAULT_LEASE_TITLE)
            .shortTitle(DEFAULT_SHORT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .inceptionDate(DEFAULT_INCEPTION_DATE)
            .commencementDate(DEFAULT_COMMENCEMENT_DATE)
            .serialNumber(DEFAULT_SERIAL_NUMBER);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        detailedLeaseContract.setSuperintendentServiceOutlet(serviceOutlet);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        detailedLeaseContract.setMainDealer(dealer);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        detailedLeaseContract.setFirstReportingPeriod(fiscalMonth);
        // Add required entity
        detailedLeaseContract.setLastReportingPeriod(fiscalMonth);
        return detailedLeaseContract;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailedLeaseContract createUpdatedEntity(EntityManager em) {
        DetailedLeaseContract detailedLeaseContract = new DetailedLeaseContract()
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .shortTitle(UPDATED_SHORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .inceptionDate(UPDATED_INCEPTION_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .serialNumber(UPDATED_SERIAL_NUMBER);
        // Add required entity
        ServiceOutlet serviceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            serviceOutlet = ServiceOutletResourceIT.createUpdatedEntity(em);
            em.persist(serviceOutlet);
            em.flush();
        } else {
            serviceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        detailedLeaseContract.setSuperintendentServiceOutlet(serviceOutlet);
        // Add required entity
        Dealer dealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            dealer = DealerResourceIT.createUpdatedEntity(em);
            em.persist(dealer);
            em.flush();
        } else {
            dealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        detailedLeaseContract.setMainDealer(dealer);
        // Add required entity
        FiscalMonth fiscalMonth;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            fiscalMonth = FiscalMonthResourceIT.createUpdatedEntity(em);
            em.persist(fiscalMonth);
            em.flush();
        } else {
            fiscalMonth = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        detailedLeaseContract.setFirstReportingPeriod(fiscalMonth);
        // Add required entity
        detailedLeaseContract.setLastReportingPeriod(fiscalMonth);
        return detailedLeaseContract;
    }

    @BeforeEach
    public void initTest() {
        detailedLeaseContract = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailedLeaseContract() throws Exception {
        int databaseSizeBeforeCreate = detailedLeaseContractRepository.findAll().size();
        // Create the DetailedLeaseContract
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);
        restDetailedLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeCreate + 1);
        DetailedLeaseContract testDetailedLeaseContract = detailedLeaseContractList.get(detailedLeaseContractList.size() - 1);
        assertThat(testDetailedLeaseContract.getBookingId()).isEqualTo(DEFAULT_BOOKING_ID);
        assertThat(testDetailedLeaseContract.getLeaseTitle()).isEqualTo(DEFAULT_LEASE_TITLE);
        assertThat(testDetailedLeaseContract.getShortTitle()).isEqualTo(DEFAULT_SHORT_TITLE);
        assertThat(testDetailedLeaseContract.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDetailedLeaseContract.getInceptionDate()).isEqualTo(DEFAULT_INCEPTION_DATE);
        assertThat(testDetailedLeaseContract.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testDetailedLeaseContract.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(1)).save(testDetailedLeaseContract);
    }

    @Test
    @Transactional
    void createDetailedLeaseContractWithExistingId() throws Exception {
        // Create the DetailedLeaseContract with an existing ID
        detailedLeaseContract.setId(1L);
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        int databaseSizeBeforeCreate = detailedLeaseContractRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailedLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeCreate);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(0)).save(detailedLeaseContract);
    }

    @Test
    @Transactional
    void checkBookingIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailedLeaseContractRepository.findAll().size();
        // set the field null
        detailedLeaseContract.setBookingId(null);

        // Create the DetailedLeaseContract, which fails.
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        restDetailedLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLeaseTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailedLeaseContractRepository.findAll().size();
        // set the field null
        detailedLeaseContract.setLeaseTitle(null);

        // Create the DetailedLeaseContract, which fails.
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        restDetailedLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInceptionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailedLeaseContractRepository.findAll().size();
        // set the field null
        detailedLeaseContract.setInceptionDate(null);

        // Create the DetailedLeaseContract, which fails.
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        restDetailedLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommencementDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = detailedLeaseContractRepository.findAll().size();
        // set the field null
        detailedLeaseContract.setCommencementDate(null);

        // Create the DetailedLeaseContract, which fails.
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        restDetailedLeaseContractMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContracts() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList
        restDetailedLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailedLeaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inceptionDate").value(hasItem(DEFAULT_INCEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())));
    }

    @Test
    @Transactional
    void getDetailedLeaseContract() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get the detailedLeaseContract
        restDetailedLeaseContractMockMvc
            .perform(get(ENTITY_API_URL_ID, detailedLeaseContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailedLeaseContract.getId().intValue()))
            .andExpect(jsonPath("$.bookingId").value(DEFAULT_BOOKING_ID))
            .andExpect(jsonPath("$.leaseTitle").value(DEFAULT_LEASE_TITLE))
            .andExpect(jsonPath("$.shortTitle").value(DEFAULT_SHORT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.inceptionDate").value(DEFAULT_INCEPTION_DATE.toString()))
            .andExpect(jsonPath("$.commencementDate").value(DEFAULT_COMMENCEMENT_DATE.toString()))
            .andExpect(jsonPath("$.serialNumber").value(DEFAULT_SERIAL_NUMBER.toString()));
    }

    @Test
    @Transactional
    void getDetailedLeaseContractsByIdFiltering() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        Long id = detailedLeaseContract.getId();

        defaultDetailedLeaseContractShouldBeFound("id.equals=" + id);
        defaultDetailedLeaseContractShouldNotBeFound("id.notEquals=" + id);

        defaultDetailedLeaseContractShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDetailedLeaseContractShouldNotBeFound("id.greaterThan=" + id);

        defaultDetailedLeaseContractShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDetailedLeaseContractShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByBookingIdIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where bookingId equals to DEFAULT_BOOKING_ID
        defaultDetailedLeaseContractShouldBeFound("bookingId.equals=" + DEFAULT_BOOKING_ID);

        // Get all the detailedLeaseContractList where bookingId equals to UPDATED_BOOKING_ID
        defaultDetailedLeaseContractShouldNotBeFound("bookingId.equals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByBookingIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where bookingId not equals to DEFAULT_BOOKING_ID
        defaultDetailedLeaseContractShouldNotBeFound("bookingId.notEquals=" + DEFAULT_BOOKING_ID);

        // Get all the detailedLeaseContractList where bookingId not equals to UPDATED_BOOKING_ID
        defaultDetailedLeaseContractShouldBeFound("bookingId.notEquals=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByBookingIdIsInShouldWork() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where bookingId in DEFAULT_BOOKING_ID or UPDATED_BOOKING_ID
        defaultDetailedLeaseContractShouldBeFound("bookingId.in=" + DEFAULT_BOOKING_ID + "," + UPDATED_BOOKING_ID);

        // Get all the detailedLeaseContractList where bookingId equals to UPDATED_BOOKING_ID
        defaultDetailedLeaseContractShouldNotBeFound("bookingId.in=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByBookingIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where bookingId is not null
        defaultDetailedLeaseContractShouldBeFound("bookingId.specified=true");

        // Get all the detailedLeaseContractList where bookingId is null
        defaultDetailedLeaseContractShouldNotBeFound("bookingId.specified=false");
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByBookingIdContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where bookingId contains DEFAULT_BOOKING_ID
        defaultDetailedLeaseContractShouldBeFound("bookingId.contains=" + DEFAULT_BOOKING_ID);

        // Get all the detailedLeaseContractList where bookingId contains UPDATED_BOOKING_ID
        defaultDetailedLeaseContractShouldNotBeFound("bookingId.contains=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByBookingIdNotContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where bookingId does not contain DEFAULT_BOOKING_ID
        defaultDetailedLeaseContractShouldNotBeFound("bookingId.doesNotContain=" + DEFAULT_BOOKING_ID);

        // Get all the detailedLeaseContractList where bookingId does not contain UPDATED_BOOKING_ID
        defaultDetailedLeaseContractShouldBeFound("bookingId.doesNotContain=" + UPDATED_BOOKING_ID);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where leaseTitle equals to DEFAULT_LEASE_TITLE
        defaultDetailedLeaseContractShouldBeFound("leaseTitle.equals=" + DEFAULT_LEASE_TITLE);

        // Get all the detailedLeaseContractList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("leaseTitle.equals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where leaseTitle not equals to DEFAULT_LEASE_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("leaseTitle.notEquals=" + DEFAULT_LEASE_TITLE);

        // Get all the detailedLeaseContractList where leaseTitle not equals to UPDATED_LEASE_TITLE
        defaultDetailedLeaseContractShouldBeFound("leaseTitle.notEquals=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseTitleIsInShouldWork() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where leaseTitle in DEFAULT_LEASE_TITLE or UPDATED_LEASE_TITLE
        defaultDetailedLeaseContractShouldBeFound("leaseTitle.in=" + DEFAULT_LEASE_TITLE + "," + UPDATED_LEASE_TITLE);

        // Get all the detailedLeaseContractList where leaseTitle equals to UPDATED_LEASE_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("leaseTitle.in=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where leaseTitle is not null
        defaultDetailedLeaseContractShouldBeFound("leaseTitle.specified=true");

        // Get all the detailedLeaseContractList where leaseTitle is null
        defaultDetailedLeaseContractShouldNotBeFound("leaseTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseTitleContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where leaseTitle contains DEFAULT_LEASE_TITLE
        defaultDetailedLeaseContractShouldBeFound("leaseTitle.contains=" + DEFAULT_LEASE_TITLE);

        // Get all the detailedLeaseContractList where leaseTitle contains UPDATED_LEASE_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("leaseTitle.contains=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseTitleNotContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where leaseTitle does not contain DEFAULT_LEASE_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("leaseTitle.doesNotContain=" + DEFAULT_LEASE_TITLE);

        // Get all the detailedLeaseContractList where leaseTitle does not contain UPDATED_LEASE_TITLE
        defaultDetailedLeaseContractShouldBeFound("leaseTitle.doesNotContain=" + UPDATED_LEASE_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByShortTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where shortTitle equals to DEFAULT_SHORT_TITLE
        defaultDetailedLeaseContractShouldBeFound("shortTitle.equals=" + DEFAULT_SHORT_TITLE);

        // Get all the detailedLeaseContractList where shortTitle equals to UPDATED_SHORT_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("shortTitle.equals=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByShortTitleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where shortTitle not equals to DEFAULT_SHORT_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("shortTitle.notEquals=" + DEFAULT_SHORT_TITLE);

        // Get all the detailedLeaseContractList where shortTitle not equals to UPDATED_SHORT_TITLE
        defaultDetailedLeaseContractShouldBeFound("shortTitle.notEquals=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByShortTitleIsInShouldWork() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where shortTitle in DEFAULT_SHORT_TITLE or UPDATED_SHORT_TITLE
        defaultDetailedLeaseContractShouldBeFound("shortTitle.in=" + DEFAULT_SHORT_TITLE + "," + UPDATED_SHORT_TITLE);

        // Get all the detailedLeaseContractList where shortTitle equals to UPDATED_SHORT_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("shortTitle.in=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByShortTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where shortTitle is not null
        defaultDetailedLeaseContractShouldBeFound("shortTitle.specified=true");

        // Get all the detailedLeaseContractList where shortTitle is null
        defaultDetailedLeaseContractShouldNotBeFound("shortTitle.specified=false");
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByShortTitleContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where shortTitle contains DEFAULT_SHORT_TITLE
        defaultDetailedLeaseContractShouldBeFound("shortTitle.contains=" + DEFAULT_SHORT_TITLE);

        // Get all the detailedLeaseContractList where shortTitle contains UPDATED_SHORT_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("shortTitle.contains=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByShortTitleNotContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where shortTitle does not contain DEFAULT_SHORT_TITLE
        defaultDetailedLeaseContractShouldNotBeFound("shortTitle.doesNotContain=" + DEFAULT_SHORT_TITLE);

        // Get all the detailedLeaseContractList where shortTitle does not contain UPDATED_SHORT_TITLE
        defaultDetailedLeaseContractShouldBeFound("shortTitle.doesNotContain=" + UPDATED_SHORT_TITLE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where description equals to DEFAULT_DESCRIPTION
        defaultDetailedLeaseContractShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the detailedLeaseContractList where description equals to UPDATED_DESCRIPTION
        defaultDetailedLeaseContractShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where description not equals to DEFAULT_DESCRIPTION
        defaultDetailedLeaseContractShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the detailedLeaseContractList where description not equals to UPDATED_DESCRIPTION
        defaultDetailedLeaseContractShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultDetailedLeaseContractShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the detailedLeaseContractList where description equals to UPDATED_DESCRIPTION
        defaultDetailedLeaseContractShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where description is not null
        defaultDetailedLeaseContractShouldBeFound("description.specified=true");

        // Get all the detailedLeaseContractList where description is null
        defaultDetailedLeaseContractShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where description contains DEFAULT_DESCRIPTION
        defaultDetailedLeaseContractShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the detailedLeaseContractList where description contains UPDATED_DESCRIPTION
        defaultDetailedLeaseContractShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where description does not contain DEFAULT_DESCRIPTION
        defaultDetailedLeaseContractShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the detailedLeaseContractList where description does not contain UPDATED_DESCRIPTION
        defaultDetailedLeaseContractShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate equals to DEFAULT_INCEPTION_DATE
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.equals=" + DEFAULT_INCEPTION_DATE);

        // Get all the detailedLeaseContractList where inceptionDate equals to UPDATED_INCEPTION_DATE
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.equals=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate not equals to DEFAULT_INCEPTION_DATE
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.notEquals=" + DEFAULT_INCEPTION_DATE);

        // Get all the detailedLeaseContractList where inceptionDate not equals to UPDATED_INCEPTION_DATE
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.notEquals=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsInShouldWork() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate in DEFAULT_INCEPTION_DATE or UPDATED_INCEPTION_DATE
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.in=" + DEFAULT_INCEPTION_DATE + "," + UPDATED_INCEPTION_DATE);

        // Get all the detailedLeaseContractList where inceptionDate equals to UPDATED_INCEPTION_DATE
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.in=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate is not null
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.specified=true");

        // Get all the detailedLeaseContractList where inceptionDate is null
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate is greater than or equal to DEFAULT_INCEPTION_DATE
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.greaterThanOrEqual=" + DEFAULT_INCEPTION_DATE);

        // Get all the detailedLeaseContractList where inceptionDate is greater than or equal to UPDATED_INCEPTION_DATE
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.greaterThanOrEqual=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate is less than or equal to DEFAULT_INCEPTION_DATE
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.lessThanOrEqual=" + DEFAULT_INCEPTION_DATE);

        // Get all the detailedLeaseContractList where inceptionDate is less than or equal to SMALLER_INCEPTION_DATE
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.lessThanOrEqual=" + SMALLER_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsLessThanSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate is less than DEFAULT_INCEPTION_DATE
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.lessThan=" + DEFAULT_INCEPTION_DATE);

        // Get all the detailedLeaseContractList where inceptionDate is less than UPDATED_INCEPTION_DATE
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.lessThan=" + UPDATED_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByInceptionDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where inceptionDate is greater than DEFAULT_INCEPTION_DATE
        defaultDetailedLeaseContractShouldNotBeFound("inceptionDate.greaterThan=" + DEFAULT_INCEPTION_DATE);

        // Get all the detailedLeaseContractList where inceptionDate is greater than SMALLER_INCEPTION_DATE
        defaultDetailedLeaseContractShouldBeFound("inceptionDate.greaterThan=" + SMALLER_INCEPTION_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate equals to DEFAULT_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldBeFound("commencementDate.equals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the detailedLeaseContractList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.equals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate not equals to DEFAULT_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.notEquals=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the detailedLeaseContractList where commencementDate not equals to UPDATED_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldBeFound("commencementDate.notEquals=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsInShouldWork() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate in DEFAULT_COMMENCEMENT_DATE or UPDATED_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldBeFound("commencementDate.in=" + DEFAULT_COMMENCEMENT_DATE + "," + UPDATED_COMMENCEMENT_DATE);

        // Get all the detailedLeaseContractList where commencementDate equals to UPDATED_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.in=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate is not null
        defaultDetailedLeaseContractShouldBeFound("commencementDate.specified=true");

        // Get all the detailedLeaseContractList where commencementDate is null
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate is greater than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldBeFound("commencementDate.greaterThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the detailedLeaseContractList where commencementDate is greater than or equal to UPDATED_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.greaterThanOrEqual=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate is less than or equal to DEFAULT_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldBeFound("commencementDate.lessThanOrEqual=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the detailedLeaseContractList where commencementDate is less than or equal to SMALLER_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.lessThanOrEqual=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate is less than DEFAULT_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.lessThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the detailedLeaseContractList where commencementDate is less than UPDATED_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldBeFound("commencementDate.lessThan=" + UPDATED_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByCommencementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where commencementDate is greater than DEFAULT_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldNotBeFound("commencementDate.greaterThan=" + DEFAULT_COMMENCEMENT_DATE);

        // Get all the detailedLeaseContractList where commencementDate is greater than SMALLER_COMMENCEMENT_DATE
        defaultDetailedLeaseContractShouldBeFound("commencementDate.greaterThan=" + SMALLER_COMMENCEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsBySerialNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where serialNumber equals to DEFAULT_SERIAL_NUMBER
        defaultDetailedLeaseContractShouldBeFound("serialNumber.equals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the detailedLeaseContractList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultDetailedLeaseContractShouldNotBeFound("serialNumber.equals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsBySerialNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where serialNumber not equals to DEFAULT_SERIAL_NUMBER
        defaultDetailedLeaseContractShouldNotBeFound("serialNumber.notEquals=" + DEFAULT_SERIAL_NUMBER);

        // Get all the detailedLeaseContractList where serialNumber not equals to UPDATED_SERIAL_NUMBER
        defaultDetailedLeaseContractShouldBeFound("serialNumber.notEquals=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsBySerialNumberIsInShouldWork() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where serialNumber in DEFAULT_SERIAL_NUMBER or UPDATED_SERIAL_NUMBER
        defaultDetailedLeaseContractShouldBeFound("serialNumber.in=" + DEFAULT_SERIAL_NUMBER + "," + UPDATED_SERIAL_NUMBER);

        // Get all the detailedLeaseContractList where serialNumber equals to UPDATED_SERIAL_NUMBER
        defaultDetailedLeaseContractShouldNotBeFound("serialNumber.in=" + UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsBySerialNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        // Get all the detailedLeaseContractList where serialNumber is not null
        defaultDetailedLeaseContractShouldBeFound("serialNumber.specified=true");

        // Get all the detailedLeaseContractList where serialNumber is null
        defaultDetailedLeaseContractShouldNotBeFound("serialNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsBySuperintendentServiceOutletIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        ServiceOutlet superintendentServiceOutlet;
        if (TestUtil.findAll(em, ServiceOutlet.class).isEmpty()) {
            superintendentServiceOutlet = ServiceOutletResourceIT.createEntity(em);
            em.persist(superintendentServiceOutlet);
            em.flush();
        } else {
            superintendentServiceOutlet = TestUtil.findAll(em, ServiceOutlet.class).get(0);
        }
        em.persist(superintendentServiceOutlet);
        em.flush();
        detailedLeaseContract.setSuperintendentServiceOutlet(superintendentServiceOutlet);
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Long superintendentServiceOutletId = superintendentServiceOutlet.getId();

        // Get all the detailedLeaseContractList where superintendentServiceOutlet equals to superintendentServiceOutletId
        defaultDetailedLeaseContractShouldBeFound("superintendentServiceOutletId.equals=" + superintendentServiceOutletId);

        // Get all the detailedLeaseContractList where superintendentServiceOutlet equals to (superintendentServiceOutletId + 1)
        defaultDetailedLeaseContractShouldNotBeFound("superintendentServiceOutletId.equals=" + (superintendentServiceOutletId + 1));
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByMainDealerIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Dealer mainDealer;
        if (TestUtil.findAll(em, Dealer.class).isEmpty()) {
            mainDealer = DealerResourceIT.createEntity(em);
            em.persist(mainDealer);
            em.flush();
        } else {
            mainDealer = TestUtil.findAll(em, Dealer.class).get(0);
        }
        em.persist(mainDealer);
        em.flush();
        detailedLeaseContract.setMainDealer(mainDealer);
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Long mainDealerId = mainDealer.getId();

        // Get all the detailedLeaseContractList where mainDealer equals to mainDealerId
        defaultDetailedLeaseContractShouldBeFound("mainDealerId.equals=" + mainDealerId);

        // Get all the detailedLeaseContractList where mainDealer equals to (mainDealerId + 1)
        defaultDetailedLeaseContractShouldNotBeFound("mainDealerId.equals=" + (mainDealerId + 1));
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByFirstReportingPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        FiscalMonth firstReportingPeriod;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            firstReportingPeriod = FiscalMonthResourceIT.createEntity(em);
            em.persist(firstReportingPeriod);
            em.flush();
        } else {
            firstReportingPeriod = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(firstReportingPeriod);
        em.flush();
        detailedLeaseContract.setFirstReportingPeriod(firstReportingPeriod);
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Long firstReportingPeriodId = firstReportingPeriod.getId();

        // Get all the detailedLeaseContractList where firstReportingPeriod equals to firstReportingPeriodId
        defaultDetailedLeaseContractShouldBeFound("firstReportingPeriodId.equals=" + firstReportingPeriodId);

        // Get all the detailedLeaseContractList where firstReportingPeriod equals to (firstReportingPeriodId + 1)
        defaultDetailedLeaseContractShouldNotBeFound("firstReportingPeriodId.equals=" + (firstReportingPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLastReportingPeriodIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        FiscalMonth lastReportingPeriod;
        if (TestUtil.findAll(em, FiscalMonth.class).isEmpty()) {
            lastReportingPeriod = FiscalMonthResourceIT.createEntity(em);
            em.persist(lastReportingPeriod);
            em.flush();
        } else {
            lastReportingPeriod = TestUtil.findAll(em, FiscalMonth.class).get(0);
        }
        em.persist(lastReportingPeriod);
        em.flush();
        detailedLeaseContract.setLastReportingPeriod(lastReportingPeriod);
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Long lastReportingPeriodId = lastReportingPeriod.getId();

        // Get all the detailedLeaseContractList where lastReportingPeriod equals to lastReportingPeriodId
        defaultDetailedLeaseContractShouldBeFound("lastReportingPeriodId.equals=" + lastReportingPeriodId);

        // Get all the detailedLeaseContractList where lastReportingPeriod equals to (lastReportingPeriodId + 1)
        defaultDetailedLeaseContractShouldNotBeFound("lastReportingPeriodId.equals=" + (lastReportingPeriodId + 1));
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseContractDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        BusinessDocument leaseContractDocument;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            leaseContractDocument = BusinessDocumentResourceIT.createEntity(em);
            em.persist(leaseContractDocument);
            em.flush();
        } else {
            leaseContractDocument = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(leaseContractDocument);
        em.flush();
        detailedLeaseContract.setLeaseContractDocument(leaseContractDocument);
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Long leaseContractDocumentId = leaseContractDocument.getId();

        // Get all the detailedLeaseContractList where leaseContractDocument equals to leaseContractDocumentId
        defaultDetailedLeaseContractShouldBeFound("leaseContractDocumentId.equals=" + leaseContractDocumentId);

        // Get all the detailedLeaseContractList where leaseContractDocument equals to (leaseContractDocumentId + 1)
        defaultDetailedLeaseContractShouldNotBeFound("leaseContractDocumentId.equals=" + (leaseContractDocumentId + 1));
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeaseContractCalculationsIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        BusinessDocument leaseContractCalculations;
        if (TestUtil.findAll(em, BusinessDocument.class).isEmpty()) {
            leaseContractCalculations = BusinessDocumentResourceIT.createEntity(em);
            em.persist(leaseContractCalculations);
            em.flush();
        } else {
            leaseContractCalculations = TestUtil.findAll(em, BusinessDocument.class).get(0);
        }
        em.persist(leaseContractCalculations);
        em.flush();
        detailedLeaseContract.setLeaseContractCalculations(leaseContractCalculations);
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Long leaseContractCalculationsId = leaseContractCalculations.getId();

        // Get all the detailedLeaseContractList where leaseContractCalculations equals to leaseContractCalculationsId
        defaultDetailedLeaseContractShouldBeFound("leaseContractCalculationsId.equals=" + leaseContractCalculationsId);

        // Get all the detailedLeaseContractList where leaseContractCalculations equals to (leaseContractCalculationsId + 1)
        defaultDetailedLeaseContractShouldNotBeFound("leaseContractCalculationsId.equals=" + (leaseContractCalculationsId + 1));
    }

    @Test
    @Transactional
    void getAllDetailedLeaseContractsByLeasePaymentIsEqualToSomething() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        LeasePayment leasePayment;
        if (TestUtil.findAll(em, LeasePayment.class).isEmpty()) {
            leasePayment = LeasePaymentResourceIT.createEntity(em);
            em.persist(leasePayment);
            em.flush();
        } else {
            leasePayment = TestUtil.findAll(em, LeasePayment.class).get(0);
        }
        em.persist(leasePayment);
        em.flush();
        detailedLeaseContract.addLeasePayment(leasePayment);
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        Long leasePaymentId = leasePayment.getId();

        // Get all the detailedLeaseContractList where leasePayment equals to leasePaymentId
        defaultDetailedLeaseContractShouldBeFound("leasePaymentId.equals=" + leasePaymentId);

        // Get all the detailedLeaseContractList where leasePayment equals to (leasePaymentId + 1)
        defaultDetailedLeaseContractShouldNotBeFound("leasePaymentId.equals=" + (leasePaymentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDetailedLeaseContractShouldBeFound(String filter) throws Exception {
        restDetailedLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailedLeaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inceptionDate").value(hasItem(DEFAULT_INCEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())));

        // Check, that the count call also returns 1
        restDetailedLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDetailedLeaseContractShouldNotBeFound(String filter) throws Exception {
        restDetailedLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDetailedLeaseContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDetailedLeaseContract() throws Exception {
        // Get the detailedLeaseContract
        restDetailedLeaseContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailedLeaseContract() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();

        // Update the detailedLeaseContract
        DetailedLeaseContract updatedDetailedLeaseContract = detailedLeaseContractRepository.findById(detailedLeaseContract.getId()).get();
        // Disconnect from session so that the updates on updatedDetailedLeaseContract are not directly saved in db
        em.detach(updatedDetailedLeaseContract);
        updatedDetailedLeaseContract
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .shortTitle(UPDATED_SHORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .inceptionDate(UPDATED_INCEPTION_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .serialNumber(UPDATED_SERIAL_NUMBER);
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(updatedDetailedLeaseContract);

        restDetailedLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailedLeaseContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isOk());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);
        DetailedLeaseContract testDetailedLeaseContract = detailedLeaseContractList.get(detailedLeaseContractList.size() - 1);
        assertThat(testDetailedLeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testDetailedLeaseContract.getLeaseTitle()).isEqualTo(UPDATED_LEASE_TITLE);
        assertThat(testDetailedLeaseContract.getShortTitle()).isEqualTo(UPDATED_SHORT_TITLE);
        assertThat(testDetailedLeaseContract.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDetailedLeaseContract.getInceptionDate()).isEqualTo(UPDATED_INCEPTION_DATE);
        assertThat(testDetailedLeaseContract.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testDetailedLeaseContract.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository).save(testDetailedLeaseContract);
    }

    @Test
    @Transactional
    void putNonExistingDetailedLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();
        detailedLeaseContract.setId(count.incrementAndGet());

        // Create the DetailedLeaseContract
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailedLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailedLeaseContractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(0)).save(detailedLeaseContract);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailedLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();
        detailedLeaseContract.setId(count.incrementAndGet());

        // Create the DetailedLeaseContract
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailedLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(0)).save(detailedLeaseContract);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailedLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();
        detailedLeaseContract.setId(count.incrementAndGet());

        // Create the DetailedLeaseContract
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailedLeaseContractMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(0)).save(detailedLeaseContract);
    }

    @Test
    @Transactional
    void partialUpdateDetailedLeaseContractWithPatch() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();

        // Update the detailedLeaseContract using partial update
        DetailedLeaseContract partialUpdatedDetailedLeaseContract = new DetailedLeaseContract();
        partialUpdatedDetailedLeaseContract.setId(detailedLeaseContract.getId());

        partialUpdatedDetailedLeaseContract.bookingId(UPDATED_BOOKING_ID);

        restDetailedLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailedLeaseContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailedLeaseContract))
            )
            .andExpect(status().isOk());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);
        DetailedLeaseContract testDetailedLeaseContract = detailedLeaseContractList.get(detailedLeaseContractList.size() - 1);
        assertThat(testDetailedLeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testDetailedLeaseContract.getLeaseTitle()).isEqualTo(DEFAULT_LEASE_TITLE);
        assertThat(testDetailedLeaseContract.getShortTitle()).isEqualTo(DEFAULT_SHORT_TITLE);
        assertThat(testDetailedLeaseContract.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDetailedLeaseContract.getInceptionDate()).isEqualTo(DEFAULT_INCEPTION_DATE);
        assertThat(testDetailedLeaseContract.getCommencementDate()).isEqualTo(DEFAULT_COMMENCEMENT_DATE);
        assertThat(testDetailedLeaseContract.getSerialNumber()).isEqualTo(DEFAULT_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateDetailedLeaseContractWithPatch() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();

        // Update the detailedLeaseContract using partial update
        DetailedLeaseContract partialUpdatedDetailedLeaseContract = new DetailedLeaseContract();
        partialUpdatedDetailedLeaseContract.setId(detailedLeaseContract.getId());

        partialUpdatedDetailedLeaseContract
            .bookingId(UPDATED_BOOKING_ID)
            .leaseTitle(UPDATED_LEASE_TITLE)
            .shortTitle(UPDATED_SHORT_TITLE)
            .description(UPDATED_DESCRIPTION)
            .inceptionDate(UPDATED_INCEPTION_DATE)
            .commencementDate(UPDATED_COMMENCEMENT_DATE)
            .serialNumber(UPDATED_SERIAL_NUMBER);

        restDetailedLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailedLeaseContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailedLeaseContract))
            )
            .andExpect(status().isOk());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);
        DetailedLeaseContract testDetailedLeaseContract = detailedLeaseContractList.get(detailedLeaseContractList.size() - 1);
        assertThat(testDetailedLeaseContract.getBookingId()).isEqualTo(UPDATED_BOOKING_ID);
        assertThat(testDetailedLeaseContract.getLeaseTitle()).isEqualTo(UPDATED_LEASE_TITLE);
        assertThat(testDetailedLeaseContract.getShortTitle()).isEqualTo(UPDATED_SHORT_TITLE);
        assertThat(testDetailedLeaseContract.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDetailedLeaseContract.getInceptionDate()).isEqualTo(UPDATED_INCEPTION_DATE);
        assertThat(testDetailedLeaseContract.getCommencementDate()).isEqualTo(UPDATED_COMMENCEMENT_DATE);
        assertThat(testDetailedLeaseContract.getSerialNumber()).isEqualTo(UPDATED_SERIAL_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingDetailedLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();
        detailedLeaseContract.setId(count.incrementAndGet());

        // Create the DetailedLeaseContract
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailedLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailedLeaseContractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(0)).save(detailedLeaseContract);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailedLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();
        detailedLeaseContract.setId(count.incrementAndGet());

        // Create the DetailedLeaseContract
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailedLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(0)).save(detailedLeaseContract);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailedLeaseContract() throws Exception {
        int databaseSizeBeforeUpdate = detailedLeaseContractRepository.findAll().size();
        detailedLeaseContract.setId(count.incrementAndGet());

        // Create the DetailedLeaseContract
        DetailedLeaseContractDTO detailedLeaseContractDTO = detailedLeaseContractMapper.toDto(detailedLeaseContract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailedLeaseContractMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailedLeaseContractDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailedLeaseContract in the database
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DetailedLeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(0)).save(detailedLeaseContract);
    }

    @Test
    @Transactional
    void deleteDetailedLeaseContract() throws Exception {
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);

        int databaseSizeBeforeDelete = detailedLeaseContractRepository.findAll().size();

        // Delete the iFRS16LeaseContract
        restDetailedLeaseContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailedLeaseContract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailedLeaseContract> detailedLeaseContractList = detailedLeaseContractRepository.findAll();
        assertThat(detailedLeaseContractList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the IFRS16LeaseContract in Elasticsearch
        verify(mockDetailedLeaseContractSearchRepository, times(1)).deleteById(detailedLeaseContract.getId());
    }

    @Test
    @Transactional
    void searchIFRS16LeaseContract() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        detailedLeaseContractRepository.saveAndFlush(detailedLeaseContract);
        when(mockDetailedLeaseContractSearchRepository.search("id:" + detailedLeaseContract.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(detailedLeaseContract), PageRequest.of(0, 1), 1));

        // Search the iFRS16LeaseContract
        restDetailedLeaseContractMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + detailedLeaseContract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailedLeaseContract.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(DEFAULT_BOOKING_ID)))
            .andExpect(jsonPath("$.[*].leaseTitle").value(hasItem(DEFAULT_LEASE_TITLE)))
            .andExpect(jsonPath("$.[*].shortTitle").value(hasItem(DEFAULT_SHORT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inceptionDate").value(hasItem(DEFAULT_INCEPTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].commencementDate").value(hasItem(DEFAULT_COMMENCEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].serialNumber").value(hasItem(DEFAULT_SERIAL_NUMBER.toString())));
    }
}
