package com.soundcloud.security;

import com.soundcloud.application.ApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    private static final Logger LOGGER = LogManager.getLogger(SecurityUtil.class);

    private SecurityUtil() {
    }

    /**
     * method encode password
     *
     * @param password password
     * @return encoding password
     * @throws PasswordEncodingException if we have troubles with encoding
     */
    public static String encode(String password) throws PasswordEncodingException {
        try {
            MessageDigest digest = MessageDigest.getInstance(ApplicationConstants.ENCODING_ALGORITHM);
            byte[] encodePassword = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String result = ApplicationConstants.PASSWORD_ENCODE_STATE.replace("0", "was successfully");
            LOGGER.info(result);
            return bytesToHex(encodePassword);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(ApplicationConstants.PASSWORD_ENCODE_STATE.replace("0", "wasn't"));
            throw new PasswordEncodingException(e.getMessage());
        }
    }

    /**
     * method convert bytes to hex
     *
     * @param hash bytes hash
     * @return hex representation of bytes
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
