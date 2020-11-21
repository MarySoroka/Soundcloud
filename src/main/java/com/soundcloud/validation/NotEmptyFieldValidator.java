package com.soundcloud.validation;

import com.soundcloud.bean.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.VALIDATION_IS_EMPTY_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.VALIDATION_FATAL_EXCEPTION;
/**
 * Implementation of the FieldValidator for validating if field is not empty
 */
@Bean(beanName = "notEmptyValidator")
public class NotEmptyFieldValidator implements FieldValidator {
    private static final Logger LOGGER = LogManager.getLogger(NotEmptyFieldValidator.class);
    @Override
    public Optional<ValidationMessage> validate(Object entity, Field field) throws ValidatorException {
        try {
            if (Collection.class.isAssignableFrom(field.getType())) {
                Collection<?> fieldValue = (Collection<?>) field.get(entity);
                if (fieldValue == null || fieldValue.isEmpty()) {
                    return Optional.of(new ValidationMessage(field.getName(), Collections.singletonList(VALIDATION_IS_EMPTY_EXCEPTION),fieldValue.toString()));
                }
            } else if (String.class.isAssignableFrom(field.getType())) {
                String fieldValue = (String) field.get(entity);
                if (fieldValue == null || fieldValue.isEmpty()) {
                    return Optional.of(new ValidationMessage(field.getName(), Collections.singletonList(VALIDATION_IS_EMPTY_EXCEPTION),fieldValue));
                }
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(VALIDATION_FATAL_EXCEPTION.replace("0",field.getName()));
            throw new ValidatorException(VALIDATION_FATAL_EXCEPTION.replace("0",field.getName()));
        }
        return Optional.empty();
    }
}
