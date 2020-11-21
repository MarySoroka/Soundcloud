package com.soundcloud.validation;

import com.soundcloud.bean.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.*;
/**
 * Implementation of the FieldValidator for validating min length of the field
 */
@Bean(beanName = "minLengthValidator")
public class MinLengthFieldValidator implements FieldValidator {
    private static final Logger LOGGER = LogManager.getLogger(MinLengthFieldValidator.class);
    @Override
    public Optional<ValidationMessage> validate(Object entity, Field field) throws ValidatorException {
        try {
            if (String.class.isAssignableFrom(field.getType())) {
                String fieldValue = (String) field.get(entity);
                MinLength fieldAnnotation = field.getAnnotation(MinLength.class);
                long minLength = fieldAnnotation.value();
                if (fieldValue == null || fieldValue.length() < minLength){
                    return Optional.of(new ValidationMessage(field.getName(), Collections.singletonList(VALIDATION_MIN_LENGTH_EXCEPTION.replace("0", String.valueOf(minLength))),fieldValue));
                }
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(VALIDATION_FATAL_EXCEPTION.replace("0",field.getName()));
            throw new ValidatorException(VALIDATION_FATAL_EXCEPTION.replace("0",field.getName()));
        }
        return Optional.empty();
    }
}
