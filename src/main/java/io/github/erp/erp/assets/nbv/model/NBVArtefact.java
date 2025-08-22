package io.github.erp.erp.assets.nbv.model;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NBVArtefact {

    private BigDecimal netBookValueAmount;

    private BigDecimal previousNetBookValueAmount;

    private Long elapsedMonths;

    private Long priorMonths;

    private BigDecimal usefulLifeYears;

    private LocalDate activePeriodStartDate;

    private LocalDate activePeriodEndDate;

    private LocalDate capitalizationDate;

    public BigDecimal getNetBookValueAmount() { return netBookValueAmount; }
    public void setNetBookValueAmount(BigDecimal netBookValueAmount) { this.netBookValueAmount = netBookValueAmount; }
    
    public BigDecimal getPreviousNetBookValueAmount() { return previousNetBookValueAmount; }
    public void setPreviousNetBookValueAmount(BigDecimal previousNetBookValueAmount) { this.previousNetBookValueAmount = previousNetBookValueAmount; }
    
    public Long getElapsedMonths() { return elapsedMonths; }
    public void setElapsedMonths(Long elapsedMonths) { this.elapsedMonths = elapsedMonths; }
    
    public Long getPriorMonths() { return priorMonths; }
    public void setPriorMonths(Long priorMonths) { this.priorMonths = priorMonths; }
    
    public BigDecimal getUsefulLifeYears() { return usefulLifeYears; }
    public void setUsefulLifeYears(BigDecimal usefulLifeYears) { this.usefulLifeYears = usefulLifeYears; }
    
    public LocalDate getActivePeriodStartDate() { return activePeriodStartDate; }
    public void setActivePeriodStartDate(LocalDate activePeriodStartDate) { this.activePeriodStartDate = activePeriodStartDate; }
    
    public LocalDate getActivePeriodEndDate() { return activePeriodEndDate; }
    public void setActivePeriodEndDate(LocalDate activePeriodEndDate) { this.activePeriodEndDate = activePeriodEndDate; }
    
    public LocalDate getCapitalizationDate() { return capitalizationDate; }
    public void setCapitalizationDate(LocalDate capitalizationDate) { this.capitalizationDate = capitalizationDate; }

    public static NBVArtefactBuilder builder() {
        return new NBVArtefactBuilder();
    }

    public static class NBVArtefactBuilder {
        private BigDecimal netBookValueAmount;
        private BigDecimal previousNetBookValueAmount;
        private Long elapsedMonths;
        private Long priorMonths;
        private BigDecimal usefulLifeYears;
        private LocalDate activePeriodStartDate;
        private LocalDate activePeriodEndDate;
        private LocalDate capitalizationDate;

        public NBVArtefactBuilder netBookValueAmount(BigDecimal netBookValueAmount) {
            this.netBookValueAmount = netBookValueAmount;
            return this;
        }

        public NBVArtefactBuilder previousNetBookValueAmount(BigDecimal previousNetBookValueAmount) {
            this.previousNetBookValueAmount = previousNetBookValueAmount;
            return this;
        }

        public NBVArtefactBuilder elapsedMonths(Long elapsedMonths) {
            this.elapsedMonths = elapsedMonths;
            return this;
        }

        public NBVArtefactBuilder priorMonths(Long priorMonths) {
            this.priorMonths = priorMonths;
            return this;
        }

        public NBVArtefactBuilder usefulLifeYears(BigDecimal usefulLifeYears) {
            this.usefulLifeYears = usefulLifeYears;
            return this;
        }

        public NBVArtefactBuilder activePeriodStartDate(LocalDate activePeriodStartDate) {
            this.activePeriodStartDate = activePeriodStartDate;
            return this;
        }

        public NBVArtefactBuilder activePeriodEndDate(LocalDate activePeriodEndDate) {
            this.activePeriodEndDate = activePeriodEndDate;
            return this;
        }

        public NBVArtefactBuilder capitalizationDate(LocalDate capitalizationDate) {
            this.capitalizationDate = capitalizationDate;
            return this;
        }

        public NBVArtefact build() {
            NBVArtefact artefact = new NBVArtefact();
            artefact.setNetBookValueAmount(netBookValueAmount);
            artefact.setPreviousNetBookValueAmount(previousNetBookValueAmount);
            artefact.setElapsedMonths(elapsedMonths);
            artefact.setPriorMonths(priorMonths);
            artefact.setUsefulLifeYears(usefulLifeYears);
            artefact.setActivePeriodStartDate(activePeriodStartDate);
            artefact.setActivePeriodEndDate(activePeriodEndDate);
            artefact.setCapitalizationDate(capitalizationDate);
            return artefact;
        }
    }
}
