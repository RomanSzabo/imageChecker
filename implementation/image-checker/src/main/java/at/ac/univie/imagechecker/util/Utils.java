package at.ac.univie.imagechecker.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static boolean isNotNull(Object o) {
        return !(o == null);
    }

    public static byte[] createFingerprint(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(string.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] createFingerprint(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(bytes);
    }


}
