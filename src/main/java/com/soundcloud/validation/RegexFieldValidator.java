package com.soundcloud.validation;

import com.soundcloud.bean.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.soundcloud.application.ApplicationConstants.VALIDATION_FATAL_EXCEPTION;
import static com.soundcloud.application.ApplicationConstants.VALIDATION_REGEX_EXCEPTION;
/**
 * Implementation of the FieldValidator for validating regex of the field
 */
@Bean(beanName = "emailValidator")
public class RegexFieldValidator implements FieldValidator {
    private static final Logger LOGGER = LogManager.getLogger(RegexFieldValidator.class);

    @Override
    public Optional<ValidationMessage> validate(Object entity, Field field) throws ValidatorException {
        try {
            if (String.class.isAssignableFrom(field.getType())) {
                String fieldValue = (String) field.get(entity);
                Regex annotation = field.getAnnotation(Regex.class);
                String regex = annotation.regex();
                Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                if (fieldValue == null || !pattern.matcher(fieldValue).find()) {
                    String exception = VALIDATION_REGEX_EXCEPTION.replace("1", field.getName());
                    exception = exception.replace("0", fieldValue);
                    return Optional.of(new ValidationMessage(field.getName(), Collections.singletonList(exception), fieldValue));
                }
            }
        } catch (IllegalAccessException e) {
            LOGGER.error(VALIDATION_FATAL_EXCEPTION.replace("0", field.getName()));
            throw new ValidatorException(VALIDATION_FATAL_EXCEPTION.replace("0", field.getName()));
        }
        return Optional.empty();
    }
}
