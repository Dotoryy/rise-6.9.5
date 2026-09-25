package rip.vantage.commons.util.encryption;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class XorCipherFake13 {
    private static final String KEY = "かにかふふいぎんぁふぃふじざ";

    public XorCipherFake13() {
    }

    public static String encrypt(String var0) {
        byte[] abyte = "かにかふふいぎんぁふぃふじざ".getBytes(StandardCharsets.UTF_8);
        byte[] abyte1 = var0.getBytes(StandardCharsets.UTF_8);
        byte[] abyte2 = new byte[abyte1.length];

        for (int i = 0; i < abyte1.length; i++) {
            abyte2[i] = (byte)(abyte1[i] ^ abyte[i % abyte.length]);
        }

        return Base64.getEncoder().encodeToString(abyte2);
    }

    public static String decrypt(String var0) {
        byte[] abyte = "かにかふふいぎんぁふぃふじざ".getBytes(StandardCharsets.UTF_8);
        byte[] abyte1 = Base64.getDecoder().decode(var0);
        byte[] abyte2 = new byte[abyte1.length];

        for (int i = 0; i < abyte1.length; i++) {
            abyte2[i] = (byte)(abyte1[i] ^ abyte[i % abyte.length]);
        }

        return new String(abyte2, StandardCharsets.UTF_8);
    }
}
