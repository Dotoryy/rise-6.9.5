package rip.vantage.commons.util.encryption;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RsaUtil {
    private static final String CIPHER_INSTANCE = "RSA/ECB/PKCS1Padding";

    public RsaUtil() {
    }

    public static KeyPair generateRSAKeyPair() throws java.security.NoSuchAlgorithmException {
        SecureRandom securerandom = new SecureRandom();
        KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
        keypairgenerator.initialize(2048, securerandom);
        return keypairgenerator.generateKeyPair();
    }

    public static String encrypt(String var0, String var1) {
        try {
            byte[] abyte = var0.getBytes();
            byte[] abyte1 = var1.getBytes();
            PublicKey publickey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(abyte1)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(1, publickey);
            byte[] abyte2 = Base64.getEncoder().encode(cipher.doFinal(abyte));
            return new String(abyte2);
        } catch (Exception exception) {
            return null;
        }
    }

    public static String decrypt(String var0, String var1) {
        try {
            byte[] abyte = Base64.getDecoder().decode(var0);
            byte[] abyte1 = var1.getBytes();
            PrivateKey privatekey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(abyte1)));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(2, privatekey);
            byte[] abyte2 = cipher.doFinal(abyte);
            return new String(abyte2, StandardCharsets.UTF_8);
        } catch (Exception exception) {
            return null;
        }
    }
}
