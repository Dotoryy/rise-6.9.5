package rip.vantage.commons.packet.impl.server.protection;

import java.util.Arrays;
import java.util.Base64;
import org.json.JSONObject;

public final class S2CPacketJdkUnlockGrant extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    public static final byte eQu = 34;
    public static final int eQv = 1;
    public static final int eQw = 64;
    public static final int eQx = 4096;
    private final int eQy;
    private final byte[] eQz;
    private final byte[] eQA;

    public S2CPacketJdkUnlockGrant(byte[] var1, byte[] var2) {
        this(1, var1, var2);
    }

    private S2CPacketJdkUnlockGrant(int var1, byte[] var2, byte[] var3) {
        super((byte)34);
        if (var1 != 1) {
            throw new IllegalArgumentException("unsupported JDK unlock protocol version: " + var1);
        }

        if (var2 == null || var2.length == 0 || var2.length > 4096) {
            throw new IllegalArgumentException("capability must contain 1-4096 bytes");
        }

        if (var3 != null && var3.length == 64) {
            this.eQy = var1;
            this.eQz = Arrays.copyOf(var2, var2.length);
            this.eQA = Arrays.copyOf(var3, var3.length);
        } else {
            throw new IllegalArgumentException("Ed25519 signature must be exactly 64 bytes");
        }
    }

    public S2CPacketJdkUnlockGrant(JSONObject json) {
        this(json.getInt("v"), a(json, "a"), a(json, "b"));
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("v", this.eQy);
        jsonobject.put("a", Base64.getEncoder().encodeToString(this.eQz));
        jsonobject.put("b", Base64.getEncoder().encodeToString(this.eQA));
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public int aJE() {
        return this.eQy;
    }

    public byte[] aKo() {
        return Arrays.copyOf(this.eQz, this.eQz.length);
    }

    public byte[] aKp() {
        return Arrays.copyOf(this.eQA, this.eQA.length);
    }

    private static byte[] a(JSONObject json, String var1) {
        try {
            return Base64.getDecoder().decode(json.getString(var1));
        } catch (IllegalArgumentException illegalargumentexception) {
            throw new IllegalArgumentException("invalid base64 field: " + var1, illegalargumentexception);
        }
    }
}
