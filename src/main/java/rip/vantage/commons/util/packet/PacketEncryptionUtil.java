package rip.vantage.commons.util.packet;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import rip.vantage.commons.packet.api.interfaces.VantagePacket;

public class PacketEncryptionUtil {
    public PacketEncryptionUtil() {
    }

    public static String exportEncrypted(VantagePacket<?> var0, SecretKey secretKey, IvParameterSpec ivParameterSpec) {
        try {
            return rip.vantage.commons.util.encryption.AesUtil.encrypt(var0.aJk(), secretKey, ivParameterSpec);
        } catch (Exception exception) {
            System.out.println("Failed to encrypt packet, usually this is because Rise doesn't have access to the internet.");
            return "";
        }
    }
}
