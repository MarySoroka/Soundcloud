package com.soundcloud.validation;

import com.soundcloud.application.ApplicationConstants;
import com.soundcloud.bean.Bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

import static com.soundcloud.application.ApplicationConstants.VALIDATION_FATAL_EXCEPTION;

/**
 * Class, that implements BeanValidator
 */
@Bean(beanName = "beanValidator")
public class BeanValidatorImpl implements BeanValidator {
    private final Map<Class<? extends Annotation>, FieldValidator> fieldValidator;
    public BeanValidatorImpl(Map<Class<? extends Annotation>, FieldValidator> fieldValidator) {
        this.fieldValidator = fieldValidator;
    }


    @Override
    public Optional<ValidationResult> validate(Object bean) throws ValidatorException {
        ValidationResult validationResult = new ValidationResult();
        try {
            if (bean == null || !bean.getClass().isAnnotationPresent(Validate.class)) {
                throw new ValidatorException(ApplicationConstants.VALIDATION_ANNOTATION_NOT_FOUND_EXCEPTION);
            }
            for (Field beanField :
                    bean.getClass().getDeclaredFields()) {
                beanField.setAccessible(true);
                Annotation[] fieldAnnotations = beanField.getDeclaredAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    if (fieldValidator.containsKey(fieldAnnotation.annotationType())) {
                        FieldValidator validator = this.fieldValidator.get(fieldAnnotation.annotationType());
                        Optional<ValidationMessage> validateMessage = validator.validate(bean, beanField);
                        validateMessage.ifPresent(validationResult::addMessage);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new ValidatorException(VALIDATION_FATAL_EXCEPTION);
        }
        if (validationResult.getMessageList().isEmpty()){
            return Optional.empty();
        }
        return Optional.of(validationResult);
    }
}
