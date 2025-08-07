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

import io.github.erp.domain.DetailedLeaseContract;
import io.github.erp.repository.DetailedLeaseContractRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Simple integration test for DetailedLeaseContract with PostgreSQL Testcontainers.
 */
@DataJpaTest
@ActiveProfiles("testcontainers")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class DetailedLeaseContractSimpleIT {

    @Autowired
    private DetailedLeaseContractRepository detailedLeaseContractRepository;

    @Test
    @Transactional
    void testDatabaseConnectionAndBasicOperations() throws Exception {
        assertThat(detailedLeaseContractRepository).isNotNull();
        
        long initialCount = detailedLeaseContractRepository.count();
        
        DetailedLeaseContract testContract = new DetailedLeaseContract();
        testContract.setBookingId("DB-TEST-001");
        testContract.setLeaseTitle("Database Test Contract");
        testContract.setInceptionDate(LocalDate.of(2024, 1, 1));
        testContract.setCommencementDate(LocalDate.of(2024, 2, 1));
        testContract.setSerialNumber(UUID.randomUUID());
        
        assertThat(testContract.getBookingId()).isEqualTo("DB-TEST-001");
        assertThat(testContract.getLeaseTitle()).isEqualTo("Database Test Contract");
        assertThat(testContract.getInceptionDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(testContract.getCommencementDate()).isEqualTo(LocalDate.of(2024, 2, 1));
        assertThat(testContract.getSerialNumber()).isNotNull();
    }

    @Test
    @Transactional
    void testDatabaseMigrationWorksCorrectly() throws Exception {
        assertThat(detailedLeaseContractRepository).isNotNull();
        
        long initialCount = detailedLeaseContractRepository.count();
        
        DetailedLeaseContract contract1 = new DetailedLeaseContract();
        contract1.setBookingId("MIGRATION-TEST-001");
        contract1.setLeaseTitle("Migration Test Contract 1");
        contract1.setShortTitle("MIG-001");
        contract1.setInceptionDate(LocalDate.of(2024, 1, 1));
        contract1.setCommencementDate(LocalDate.of(2024, 2, 1));
        contract1.setSerialNumber(UUID.randomUUID());
        
        DetailedLeaseContract contract2 = new DetailedLeaseContract();
        contract2.setBookingId("MIGRATION-TEST-002");
        contract2.setLeaseTitle("Migration Test Contract 2");
        contract2.setShortTitle("MIG-002");
        contract2.setInceptionDate(LocalDate.of(2024, 1, 1));
        contract2.setCommencementDate(LocalDate.of(2024, 2, 1));
        contract2.setSerialNumber(UUID.randomUUID());
        
        detailedLeaseContractRepository.saveAndFlush(contract1);
        detailedLeaseContractRepository.saveAndFlush(contract2);
        
        assertThat(detailedLeaseContractRepository.count()).isEqualTo(initialCount + 2);
        
        assertThat(detailedLeaseContractRepository.findAll())
            .extracting(DetailedLeaseContract::getBookingId)
            .contains("MIGRATION-TEST-001", "MIGRATION-TEST-002");
    }
}
