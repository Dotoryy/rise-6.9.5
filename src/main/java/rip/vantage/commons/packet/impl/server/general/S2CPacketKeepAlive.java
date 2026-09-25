package rip.vantage.commons.packet.impl.server.general;

import org.json.JSONObject;
import rip.vantage.commons.handler.api.S2CPacketHandler;
import rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket;

public class S2CPacketKeepAlive extends AbstractS2CPacket {
    long a;

    public S2CPacketKeepAlive() {
        super((byte)0);
        this.a = System.currentTimeMillis();
    }

    public S2CPacketKeepAlive(JSONObject json) {
        super((byte)0);
        this.a = json.getLong("a");
    }

    @Override
    public void handle(S2CPacketHandler handler) {
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("id", this.getId());
        jsonobject.put("a", this.a);
        return jsonobject.toString();
    }

    public long getA() {
        return this.a;
    }
}
