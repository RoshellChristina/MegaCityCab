package org.megacitycab.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

public class ImageUtil {

    public static String convertToBase64(InputStream inputStream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns a Base64 encoded default image (a 1x1 transparent PNG)
    public static String getDefaultImage() {
        // This Base64 string represents a minimal transparent PNG.
        return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAA1JREFUGFdj+P///P8ACfsD/6TJ3O8AAAAASUVORK5CYII=";
    }


    public static byte[] convertToBytes(InputStream inputStream) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Returns default image bytes (here, a minimal transparent PNG)
    public static byte[] getDefaultImageBytes() {
        String base64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAAAXNSR0IArs4c6QAAAA1JREFUGFdj+P///P8ACfsD/6TJ3O8AAAAASUVORK5CYII=";
        return Base64.getDecoder().decode(base64);
    }
}

