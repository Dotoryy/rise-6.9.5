package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONObject;

public class S2CPacketStartRecording extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    public int ePV;
    public String ePU;

    public String ajm() {
        return this.ePU;
    }

    public int aJV() {
        return this.ePV;
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ePU);
        jsonobject.put("b", this.ePV);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public S2CPacketStartRecording(String var1, int var2) {
        super((byte)22);
        this.ePU = var1;
        this.ePV = var2;
    }

    static {
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    public S2CPacketStartRecording(JSONObject json) {
        super((byte)22);
        this.ePU = json.getString("a");
        this.ePV = json.optInt("b", 600);
    }
}
