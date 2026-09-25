package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public final class C2SPacketTelemetry extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    public static final byte eOv = 33;
    private final String eOw;
    private final String eOx;
    private final String eOy;

    public C2SPacketTelemetry(String var1, String var2, String var3) {
        super((byte)33);
        this.eOw = var1;
        this.eOx = var2;
        this.eOy = var3;
    }

    public C2SPacketTelemetry(JSONObject json) {
        super((byte)33);
        this.eOw = json.getString("a");
        this.eOx = json.getString("b");
        this.eOy = json.optString("c", "");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.eOw);
        jsonobject.put("b", this.eOx);
        jsonobject.put("c", this.eOy);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String bX() {
        return this.eOw;
    }

    public String aJm() {
        return this.eOx;
    }

    public String mY() {
        return this.eOy;
    }
}
