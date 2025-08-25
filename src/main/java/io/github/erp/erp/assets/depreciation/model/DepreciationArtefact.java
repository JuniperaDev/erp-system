package io.github.erp.erp.assets.depreciation.model;

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
public class DepreciationArtefact {

    private BigDecimal depreciationAmount;

    private Long elapsedMonths;

    private Long priorMonths;

    private BigDecimal usefulLifeYears;

    private BigDecimal nbvBeforeDepreciation;

    private BigDecimal nbv;

    private LocalDate depreciationPeriodStartDate;

    private LocalDate depreciationPeriodEndDate;

    private LocalDate capitalizationDate;

    public static DepreciationArtefactBuilder builder() {
        return new DepreciationArtefactBuilder();
    }

    public static class DepreciationArtefactBuilder {
        private BigDecimal depreciationAmount;
        private Long elapsedMonths;
        private Long priorMonths;
        private BigDecimal usefulLifeYears;
        private BigDecimal nbvBeforeDepreciation;
        private BigDecimal nbv;
        private LocalDate depreciationPeriodStartDate;
        private LocalDate depreciationPeriodEndDate;
        private LocalDate capitalizationDate;

        public DepreciationArtefactBuilder depreciationAmount(BigDecimal depreciationAmount) {
            this.depreciationAmount = depreciationAmount;
            return this;
        }

        public DepreciationArtefactBuilder elapsedMonths(Long elapsedMonths) {
            this.elapsedMonths = elapsedMonths;
            return this;
        }

        public DepreciationArtefactBuilder priorMonths(Long priorMonths) {
            this.priorMonths = priorMonths;
            return this;
        }

        public DepreciationArtefactBuilder usefulLifeYears(BigDecimal usefulLifeYears) {
            this.usefulLifeYears = usefulLifeYears;
            return this;
        }

        public DepreciationArtefactBuilder nbvBeforeDepreciation(BigDecimal nbvBeforeDepreciation) {
            this.nbvBeforeDepreciation = nbvBeforeDepreciation;
            return this;
        }

        public DepreciationArtefactBuilder nbv(BigDecimal nbv) {
            this.nbv = nbv;
            return this;
        }

        public DepreciationArtefactBuilder depreciationPeriodStartDate(LocalDate depreciationPeriodStartDate) {
            this.depreciationPeriodStartDate = depreciationPeriodStartDate;
            return this;
        }

        public DepreciationArtefactBuilder depreciationPeriodEndDate(LocalDate depreciationPeriodEndDate) {
            this.depreciationPeriodEndDate = depreciationPeriodEndDate;
            return this;
        }

        public DepreciationArtefactBuilder capitalizationDate(LocalDate capitalizationDate) {
            this.capitalizationDate = capitalizationDate;
            return this;
        }

        public DepreciationArtefact build() {
            DepreciationArtefact artefact = new DepreciationArtefact();
            artefact.setDepreciationAmount(depreciationAmount);
            artefact.setElapsedMonths(elapsedMonths);
            artefact.setPriorMonths(priorMonths);
            artefact.setUsefulLifeYears(usefulLifeYears);
            artefact.setNbvBeforeDepreciation(nbvBeforeDepreciation);
            artefact.setNbv(nbv);
            artefact.setDepreciationPeriodStartDate(depreciationPeriodStartDate);
            artefact.setDepreciationPeriodEndDate(depreciationPeriodEndDate);
            artefact.setCapitalizationDate(capitalizationDate);
            return artefact;
        }
    }

    public BigDecimal getDepreciationAmount() { return depreciationAmount; }
    public void setDepreciationAmount(BigDecimal depreciationAmount) { this.depreciationAmount = depreciationAmount; }
    
    public Long getElapsedMonths() { return elapsedMonths; }
    public void setElapsedMonths(Long elapsedMonths) { this.elapsedMonths = elapsedMonths; }
    
    public Long getPriorMonths() { return priorMonths; }
    public void setPriorMonths(Long priorMonths) { this.priorMonths = priorMonths; }
    
    public BigDecimal getUsefulLifeYears() { return usefulLifeYears; }
    public void setUsefulLifeYears(BigDecimal usefulLifeYears) { this.usefulLifeYears = usefulLifeYears; }
    
    public BigDecimal getNbvBeforeDepreciation() { return nbvBeforeDepreciation; }
    public void setNbvBeforeDepreciation(BigDecimal nbvBeforeDepreciation) { this.nbvBeforeDepreciation = nbvBeforeDepreciation; }
    
    public BigDecimal getNbv() { return nbv; }
    public void setNbv(BigDecimal nbv) { this.nbv = nbv; }
    
    public LocalDate getDepreciationPeriodStartDate() { return depreciationPeriodStartDate; }
    public void setDepreciationPeriodStartDate(LocalDate depreciationPeriodStartDate) { this.depreciationPeriodStartDate = depreciationPeriodStartDate; }
    
    public LocalDate getDepreciationPeriodEndDate() { return depreciationPeriodEndDate; }
    public void setDepreciationPeriodEndDate(LocalDate depreciationPeriodEndDate) { this.depreciationPeriodEndDate = depreciationPeriodEndDate; }
    
    public LocalDate getCapitalizationDate() { return capitalizationDate; }
    public void setCapitalizationDate(LocalDate capitalizationDate) { this.capitalizationDate = capitalizationDate; }
}
