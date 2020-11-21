package com.soundcloud.subscription;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class SubscriptionFieldTest{
    @Test
    public void getByNameSubscriptionField_whenDefault_IsEmpty() {
        String type = "DEFAULT";
        Optional<SubscriptionField> subscriptionField = SubscriptionField.of(type);
        assertFalse(subscriptionField.isPresent());
    }
    @Test
    public void getByNameSubscriptionField_whenId_IsNotEmpty() {
        String type = "SUBSCRIPTION_ID";
        Optional<SubscriptionField> subscriptionField = SubscriptionField.of(type);
        assertTrue(subscriptionField.isPresent());
    }
    @Test
    public void getByNameSubscriptionField_whenDate_IsNotEmpty() {
        String type = "SUBSCRIPTION_DATE";
        Optional<SubscriptionField> subscriptionField = SubscriptionField.of(type);
        assertTrue(subscriptionField.isPresent());
    }
    @Test
    public void getByNameSubscriptionField_whenUserId_IsNotEmpty() {
        String type = "SUBSCRIPTION_USER_ID";
        Optional<SubscriptionField> subscriptionField = SubscriptionField.of(type);
        assertTrue(subscriptionField.isPresent());
    }
    @Test
    public void getByNameSubscriptionField_whenStatus_IsNotEmpty() {
        String type = "SUBSCRIPTION_STATUS";
        Optional<SubscriptionField> subscriptionField = SubscriptionField.of(type);
        assertTrue(subscriptionField.isPresent());
    }
}