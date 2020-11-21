package com.soundcloud.validation;

import com.soundcloud.bean.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.*;

/**
 * Implementation of the FieldValidator for validating max length of the field
 */
@Bean(beanName = "maxLengthValidator")
public class MaxLengthFieldValidator implements FieldValidator {
    private static final Logger LOGGER = LogManager.getLogger(MaxLengthFieldValidator.class);
    @Override
    public Optional<ValidationMessage> validate(Object entity, Field field) throws ValidatorException {
        try {
            if (String.class.isAssignableFrom(field.getType())) {
                String fieldValue = (String) field.get(entity);
                MaxLength fieldAnnotation = field.getAnnotation(MaxLength.class);
                long maxLength = fieldAnnotation.value();
                if (fieldValue == null || fieldValue.length() > maxLength){
                   return Optional.of(new ValidationMessage(field.getName(), Collections.singletonList(VALIDATION_MAX_LENGTH_EXCEPTION.replace("0", String.valueOf(maxLength))),fieldValue));
                }
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(VALIDATION_FATAL_EXCEPTION.replace("0",field.getName()));
            throw new ValidatorException(VALIDATION_FATAL_EXCEPTION.replace("0",field.getName()));
        }
        return Optional.empty();
    }
}
