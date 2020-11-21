package com.soundcloud.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SecurityUtilTest {
    @Test
    public void isPasswordEncodingIsRight() throws PasswordEncodingException {
        String encodePassword = SecurityUtil.encode("123");
        String encodePasswordAnother = SecurityUtil.encode("12");
        String encodePasswordSame = SecurityUtil.encode("123");

        Assert.assertEquals(encodePassword,encodePasswordSame);
        Assert.assertNotEquals(encodePassword,encodePasswordAnother);
    }
}
