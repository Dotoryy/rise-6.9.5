package rip.vantage.commons.packet.impl.client.general;

import org.json.JSONObject;

public class C2SPacketKeepAlive extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    public C2SPacketKeepAlive() {
        super((byte)0);
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }
}
