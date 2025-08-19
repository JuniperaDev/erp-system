package io.github.erp.internal.acl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

public abstract class AbstractTranslationACL<S, T> {
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final Validator validator;

    protected AbstractTranslationACL(Validator validator) {
        this.validator = validator;
    }

    public T translate(S source) {
        if (source == null) {
            log.warn("Attempted to translate null source object");
            return null;
        }

        try {
            validateSource(source);
            T result = performTranslation(source);
            validateResult(result);
            return result;
        } catch (Exception e) {
            log.error("Translation failed for source: {}", source, e);
            throw new TranslationException("Failed to translate object", e);
        }
    }

    protected abstract T performTranslation(S source);

    protected void validateSource(S source) {
        if (validator != null) {
            Set<ConstraintViolation<S>> violations = validator.validate(source);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder("Source validation failed: ");
                violations.forEach(v -> sb.append(v.getMessage()).append("; "));
                throw new ValidationException(sb.toString());
            }
        }
    }

    protected void validateResult(T result) {
        if (validator != null && result != null) {
            Set<ConstraintViolation<T>> violations = validator.validate(result);
            if (!violations.isEmpty()) {
                StringBuilder sb = new StringBuilder("Result validation failed: ");
                violations.forEach(v -> sb.append(v.getMessage()).append("; "));
                throw new ValidationException(sb.toString());
            }
        }
    }

    public static class TranslationException extends RuntimeException {
        public TranslationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}
