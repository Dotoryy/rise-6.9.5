package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONObject;

public class S2CPacketCaptureRequest extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    public String ePW;
    public int ePX;
    public String ePY;

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ePW);
        jsonobject.put("b", this.ePX);
        jsonobject.put("d", this.ePY);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public S2CPacketCaptureRequest(JSONObject json) {
        super((byte)24);
        this.ePW = json.getString("a");
        this.ePX = json.optInt("b", 300);
        this.ePY = json.optString("d", "screenshot");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    public S2CPacketCaptureRequest(String var1, int var2, String var3) {
        super((byte)24);
        this.ePW = var1;
        this.ePX = var2;
        this.ePY = var3;
    }

    public String ajm() {
        return this.ePW;
    }


    public int aJV() {
        return this.ePX;
    }

    public String aJW() {
        return this.ePY;
    }

    static {
    }
}
