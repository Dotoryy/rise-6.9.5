package rip.vantage.commons.packet.impl.client.protection;

import org.json.JSONObject;

public class C2SPacketConfig extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String config;

    public C2SPacketConfig(String var1) {
        super((byte)2);
        this.config = var1;
    }

    public C2SPacketConfig(JSONObject json) {
        super((byte)2);
        this.config = json.getString("a");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
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
