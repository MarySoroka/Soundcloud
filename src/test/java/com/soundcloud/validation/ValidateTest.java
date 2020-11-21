package com.soundcloud.validation;

import com.soundcloud.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RunWith(JUnit4.class)
public class ValidateTest {

    private BeanValidator beanValidator;

    @Before
    public void init() {
        Map<Class<? extends Annotation>, FieldValidator> validatorMap = new HashMap<>();
        validatorMap.put(MaxLength.class, new MaxLengthFieldValidator());
        validatorMap.put(MinLength.class, new MinLengthFieldValidator());
        validatorMap.put(NotEmpty.class, new NotEmptyFieldValidator());
        validatorMap.put(Regex.class, new RegexFieldValidator());
        beanValidator = new BeanValidatorImpl(validatorMap);
    }

    @Test
    public void shouldGetValidationMessageOnEmailField() throws ValidatorException {

        User user = new User();
        user.setLogin("maryFlashy");
        user.setPassword("12345678");
        user.setEmail("mar@.com");

        Optional<ValidationResult> result = beanValidator.validate(user);
        Assert.assertTrue(result.isPresent());
        List<ValidationMessage> validationMessages = result.get().getMessageList();
        Assert.assertEquals(1, validationMessages.size());
        Assert.assertEquals("email",validationMessages.get(0).getField());
    }

    @Test
    public void shouldGetValidationMessageOnLoginField() throws ValidatorException {

        User user = new User();
        user.setLogin("mary");
        user.setPassword("12345678");
        user.setEmail("mar@gmail.com");

        Optional<ValidationResult> result = beanValidator.validate(user);
        Assert.assertTrue(result.isPresent());
        List<ValidationMessage> validationMessages = result.get().getMessageList();
        Assert.assertEquals(1, validationMessages.size());
        Assert.assertEquals("login",validationMessages.get(0).getField());

    }

}
