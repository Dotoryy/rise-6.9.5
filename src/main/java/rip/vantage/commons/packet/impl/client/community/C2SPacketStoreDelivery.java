package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public final class C2SPacketStoreDelivery extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    public static final byte eOz = 31;
    private final String eOA;
    private final String eOB;
    private final int eOC;
    private final int eOD;
    private final String eOE;
    private final boolean eOF;

    public C2SPacketStoreDelivery(String var1, String var2, int var3, int var4, String var5, boolean var6) {
        super((byte)31);
        this.eOA = var1;
        this.eOB = var2;
        this.eOC = var3;
        this.eOD = var4;
        this.eOE = var5;
        this.eOF = var6;
    }

    public C2SPacketStoreDelivery(JSONObject json) {
        super((byte)31);
        this.eOA = json.getString("a");
        this.eOB = json.getString("b");
        this.eOC = json.getInt("c");
        this.eOD = json.getInt("d");
        this.eOE = json.optString("e", "");
        this.eOF = json.optBoolean("f", false);
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.eOA);
        jsonobject.put("b", this.eOB);
        jsonobject.put("c", this.eOC);
        jsonobject.put("d", this.eOD);
        jsonobject.put("e", this.eOE);
        jsonobject.put("f", this.eOF);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String bX() {
        return this.eOA;
    }

    public String aJn() {
        return this.eOB;
    }

    public int aJo() {
        return this.eOC;
    }

    public int getAmount() {
        return this.eOD;
    }

    public String sp() {
        return this.eOE;
    }

    public boolean aJp() {
        return this.eOF;
    }
}
