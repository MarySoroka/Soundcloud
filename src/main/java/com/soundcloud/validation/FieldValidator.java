package com.soundcloud.validation;

import java.lang.reflect.Field;
import java.util.Optional;

public interface FieldValidator {
    /**
     * method validate field
     * @param entity entity, that has field
     * @param field field
     * @return  * @return if validation success , then return Optional.empty(), else option ValidationResult
     * @throws ValidatorException if we have exception during validation
     * @throws IllegalAccessException if we have exception during validation
     */
    Optional<ValidationMessage> validate(Object entity, Field field) throws IllegalAccessException, ValidatorException;
}
