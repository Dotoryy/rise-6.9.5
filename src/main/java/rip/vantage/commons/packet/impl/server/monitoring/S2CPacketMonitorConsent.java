package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONObject;

public class S2CPacketMonitorConsent extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    public boolean ePR;

    static {
    }

    public S2CPacketMonitorConsent(JSONObject json) {
        super((byte)26);
        this.ePR = json.optBoolean("a", false);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ePR);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.b(this);
    }

    public boolean isAccepted() {
        return this.ePR;
    }

}
