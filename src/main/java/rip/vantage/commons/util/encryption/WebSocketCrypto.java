package rip.vantage.commons.util.encryption;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class WebSocketCrypto {
    public static final int eRc = 32;
    private static final int eRd = 12;
    private static final int eRe = 128;
    private static final byte[] eRf = "rise-ws-v1".getBytes(StandardCharsets.UTF_8);
    private static final SecureRandom eRg = new SecureRandom();

    private WebSocketCrypto() {
    }

    private static byte[] aKv() {
        return new byte[]{
            56,
            -48,
            104,
            40,
            -91,
            -6,
            -24,
            25,
            54,
            -74,
            -18,
            6,
            -86,
            27,
            -110,
            -17,
            -16,
            -31,
            -91,
            -123,
            -38,
            22,
            6,
            118,
            -76,
            48,
            -47,
            125,
            -44,
            -62,
            -98,
            -39
        };
    }

    public static byte[] aKw() {
        byte[] abyte = new byte[32];
        eRg.nextBytes(abyte);
        return abyte;
    }

    public static byte[] b(byte[] var0, byte[] var1) throws java.security.InvalidKeyException, java.security.NoSuchAlgorithmException {
        byte[] abyte = new byte[var0.length + var1.length];
        System.arraycopy(var0, 0, abyte, 0, var0.length);
        System.arraycopy(var1, 0, abyte, var0.length, var1.length);
        byte[] abyte1 = e(abyte, aKv());
        byte[] abyte2 = new byte[eRf.length + 1];
        System.arraycopy(eRf, 0, abyte2, 0, eRf.length);
        abyte2[eRf.length] = 1;
        return e(abyte1, abyte2);
    }

    public static byte[] c(byte[] var0, byte[] var1) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.NoSuchPaddingException {
        byte[] abyte = new byte[12];
        eRg.nextBytes(abyte);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(1, new SecretKeySpec(var0, "AES"), new GCMParameterSpec(128, abyte));
        byte[] abyte1 = cipher.doFinal(var1);
        byte[] abyte2 = new byte[12 + abyte1.length];
        System.arraycopy(abyte, 0, abyte2, 0, 12);
        System.arraycopy(abyte1, 0, abyte2, 12, abyte1.length);
        return abyte2;
    }

    public static byte[] d(byte[] var0, byte[] var1) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.NoSuchPaddingException {
        if (var1.length < 28) {
            throw new IllegalArgumentException("envelope too short");
        }

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(2, new SecretKeySpec(var0, "AES"), new GCMParameterSpec(128, var1, 0, 12));
        return cipher.doFinal(var1, 12, var1.length - 12);
    }

    public static String a(byte[] var0, String var1) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.NoSuchPaddingException {
        return Base64.getEncoder().encodeToString(c(var0, var1.getBytes(StandardCharsets.UTF_8)));
    }

    public static String b(byte[] var0, String var1) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.NoSuchPaddingException {
        return new String(d(var0, Base64.getDecoder().decode(var1)), StandardCharsets.UTF_8);
    }

    private static byte[] e(byte[] var0, byte[] var1) throws java.security.InvalidKeyException, java.security.NoSuchAlgorithmException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(var0, "HmacSHA256"));
        return mac.doFinal(var1);
    }
}
