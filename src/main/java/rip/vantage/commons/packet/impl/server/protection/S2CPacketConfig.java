package rip.vantage.commons.packet.impl.server.protection;

import org.json.JSONObject;

public class S2CPacketConfig extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final String config;

    public S2CPacketConfig(String var1) {
        super((byte)2);
        this.config = var1;
    }

    public S2CPacketConfig(JSONObject json) {
        super((byte)2);
        this.config = json.getString("a");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.config);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getConfig() {
        return this.config;
    }
}
