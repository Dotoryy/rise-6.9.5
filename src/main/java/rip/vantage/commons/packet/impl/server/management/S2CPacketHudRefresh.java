package rip.vantage.commons.packet.impl.server.management;

import org.json.JSONObject;
import rip.vantage.commons.handler.api.S2CPacketHandler;
import rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket;

public class S2CPacketHudRefresh extends AbstractS2CPacket {
    public S2CPacketHudRefresh() {
        super((byte)5);
    }

    @Override
    public void handle(S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }
}
