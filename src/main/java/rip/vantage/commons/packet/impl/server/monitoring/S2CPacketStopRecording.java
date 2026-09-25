package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONObject;

public class S2CPacketStopRecording extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final String ePZ;

    public S2CPacketStopRecording(String var1) {
        super((byte)23);
        this.ePZ = var1;
    }

    public S2CPacketStopRecording(JSONObject json) {
        super((byte)23);
        this.ePZ = json.getString("a");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ePZ);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String ajm() {
        return this.ePZ;
    }
}
