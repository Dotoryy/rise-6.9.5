package rip.vantage.commons.packet.impl.client.protection;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import org.json.JSONObject;

public final class C2SPacketJdkUnlockRequest extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    public static final byte ePl = 34;
    public static final int ePm = 1;
    public static final int ePn = 32;
    public static final int ePo = 32;
    public static final int ePp = 32;
    public static final int ePq = 64;
    private final int ePr;
    private final byte[] ePs;
    private final byte[] ePt;
    private final String ePu;
    private final byte[] ePv;

    public C2SPacketJdkUnlockRequest(byte[] var1, byte[] var2, String var3, byte[] var4) {
        this(1, var1, var2, var3, var4);
    }

    private C2SPacketJdkUnlockRequest(int var1, byte[] var2, byte[] var3, String var4, byte[] var5) {
        super((byte)34);
        kD(var1);
        a("challenge", var2, 32);
        a("clientPublicKey", var3, 32);
        kf(var4);
        a("hwidHash", var5, 32);
        this.ePr = var1;
        this.ePs = Arrays.copyOf(var2, var2.length);
        this.ePt = Arrays.copyOf(var3, var3.length);
        this.ePu = var4;
        this.ePv = Arrays.copyOf(var5, var5.length);
    }

    public C2SPacketJdkUnlockRequest(JSONObject json) {
        this(json.getInt("v"), a(json, "a"), a(json, "b"), json.getString("c"), a(json, "d"));
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("v", this.ePr);
        jsonobject.put("a", Base64.getEncoder().encodeToString(this.ePs));
        jsonobject.put("b", Base64.getEncoder().encodeToString(this.ePt));
        jsonobject.put("c", this.ePu);
        jsonobject.put("d", Base64.getEncoder().encodeToString(this.ePv));
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public int aJE() {
        return this.ePr;
    }

    public byte[] aJF() {
        return Arrays.copyOf(this.ePs, this.ePs.length);
    }

    public byte[] aJG() {
        return Arrays.copyOf(this.ePt, this.ePt.length);
    }

    public String aJH() {
        return this.ePu;
    }

    public byte[] aJI() {
        return Arrays.copyOf(this.ePv, this.ePv.length);
    }

    private static byte[] a(JSONObject json, String var1) {
        try {
            return Base64.getDecoder().decode(json.getString(var1));
        } catch (IllegalArgumentException illegalargumentexception) {
            throw new IllegalArgumentException("invalid base64 field: " + var1, illegalargumentexception);
        }
    }

    private static void kD(int var0) {
        if (var0 != 1) {
            throw new IllegalArgumentException("unsupported JDK unlock protocol version: " + var0);
        }
    }

    private static void a(String var0, byte[] var1, int var2) {
        if (var1 == null || var1.length != var2) {
            throw new IllegalArgumentException(var0 + " must be exactly " + var2 + " bytes");
        }
    }

    private static void kf(String var0) {
        if (var0 != null && !var0.isEmpty()) {
            if (var0.getBytes(StandardCharsets.UTF_8).length > 64) {
                throw new IllegalArgumentException("buildId exceeds 64 UTF-8 bytes");
            }
        } else {
            throw new IllegalArgumentException("buildId must not be empty");
        }
    }
}
