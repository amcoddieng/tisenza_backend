package com.tissenza.tissenza_backend.exception;

import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    private final List<String> errors;
    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(message);
        this.errors = List.of(message);
        this.fieldErrors = null;
    }

    public ValidationException(List<String> errors) {
        super("Erreurs de validation: " + String.join(", ", errors));
        this.errors = errors;
        this.fieldErrors = null;
    }

    public ValidationException(Map<String, String> fieldErrors) {
        super("Erreurs de validation des champs");
        this.errors = null;
        this.fieldErrors = fieldErrors;
    }

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
        this.fieldErrors = null;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
