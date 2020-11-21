package com.soundcloud.validation;

import java.util.List;

public class ValidationMessage {
    private final String field;
    private final List<String> errors;
    private final String value;

    public ValidationMessage(String field, List<String> errors, String value) {
        this.field = field;
        this.errors = errors;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getField() {
        return field;
    }

    public List<String> getErrors() {
        return errors;
    }
}
