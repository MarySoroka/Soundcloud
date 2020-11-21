package com.soundcloud.builder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * Builder util class
 */
public class BuilderUtil {
    private BuilderUtil() {

    }

    /**
     * method for converting from input stream to base 64 format
     *
     * @param inputStream image input stream
     * @return base 64 format image
     * @throws IOException if writing to input stream has exception
     */
    public static String getBase64(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
            byte[] buffer = new byte[inputStream.available()];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}
