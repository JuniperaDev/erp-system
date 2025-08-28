package io.github.erp.service.validation;

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

import com.networknt.schema.ValidationMessage;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationResult {

    private final boolean valid;
    private final List<String> errors;
    private final String errorMessage;

    private ValidationResult(boolean valid, List<String> errors, String errorMessage) {
        this.valid = valid;
        this.errors = errors != null ? errors : Collections.emptyList();
        this.errorMessage = errorMessage;
    }

    public static ValidationResult valid() {
        return new ValidationResult(true, Collections.emptyList(), null);
    }

    public static ValidationResult invalid(Set<ValidationMessage> validationMessages) {
        List<String> errors = validationMessages.stream()
            .map(ValidationMessage::getMessage)
            .collect(Collectors.toList());
        return new ValidationResult(false, errors, null);
    }

    public static ValidationResult error(String errorMessage) {
        return new ValidationResult(false, Collections.emptyList(), errorMessage);
    }

    public boolean isValid() {
        return valid;
    }

    public List<String> getErrors() {
        return errors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean hasErrors() {
        return !errors.isEmpty() || errorMessage != null;
    }

    public String getFormattedErrors() {
        if (errorMessage != null) {
            return errorMessage;
        }
        return String.join("; ", errors);
    }

    @Override
    public String toString() {
        return "ValidationResult{" +
            "valid=" + valid +
            ", errors=" + errors +
            ", errorMessage='" + errorMessage + '\'' +
            '}';
    }
}
