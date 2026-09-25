package rip.vantage.commons.util.encryption;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AesUtil {
    public AesUtil() {
    }

    public static SecretKey getSecretKey() throws java.security.NoSuchAlgorithmException {
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
        keygenerator.init(256);
        return keygenerator.generateKey();
    }

    public static SecretKeySpec getKeyFromPassword(String var0) throws java.security.NoSuchAlgorithmException, java.security.spec.InvalidKeySpecException {
        SecureRandom securerandom = SecureRandom.getInstanceStrong();
        byte[] abyte = new byte[100];
        securerandom.nextBytes(abyte);
        SecretKeyFactory secretkeyfactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        PBEKeySpec pbekeyspec = new PBEKeySpec(var0.toCharArray(), abyte, 65536, 256);
        return new SecretKeySpec(secretkeyfactory.generateSecret(pbekeyspec).getEncoded(), "AES");
    }

    public static IvParameterSpec generateIv() {
        byte[] abyte = new byte[16];
        new SecureRandom().nextBytes(abyte);
        return new IvParameterSpec(abyte);
    }

    public static String encrypt(String var0, SecretKey secretKey, IvParameterSpec ivParameterSpec) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, secretKey, ivParameterSpec);
        byte[] abyte = cipher.doFinal(var0.getBytes());
        return Base64.getEncoder().encodeToString(abyte);
    }

    public static String decrypt(String var0, SecretKey secretKey, IvParameterSpec ivParameterSpec) throws java.security.InvalidAlgorithmParameterException, java.security.InvalidKeyException, java.security.NoSuchAlgorithmException, javax.crypto.BadPaddingException, javax.crypto.IllegalBlockSizeException, javax.crypto.NoSuchPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(2, secretKey, ivParameterSpec);
        byte[] abyte = cipher.doFinal(Base64.getDecoder().decode(var0));
        return new String(abyte);
    }
}
