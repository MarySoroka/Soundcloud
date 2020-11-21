package com.soundcloud.validation;

import java.util.Optional;

public interface BeanValidator {
    /**
     * method validate type, which has annotation
     * @param bean bean
     * @return if validation success , then return Optional.empty(), else option ValidationResult
     * @throws ValidatorException if we have exception during validation
     */
    Optional<ValidationResult> validate(Object bean) throws ValidatorException;
}
