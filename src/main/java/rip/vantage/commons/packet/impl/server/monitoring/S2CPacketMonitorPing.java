package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONObject;

public class S2CPacketMonitorPing extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {

    public S2CPacketMonitorPing(JSONObject json) {
        super((byte)29);
    }


    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", true);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    static {
    }
}
