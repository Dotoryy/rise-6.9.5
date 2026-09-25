package rip.vantage.commons.packet.impl.server.protection;

import org.json.JSONObject;

public class S2CPacketServerJoin extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final String ip;
    private final int port;

    public S2CPacketServerJoin(String var1, int var2) {
        super((byte)3);
        this.ip = var1;
        this.port = var2;
    }

    public S2CPacketServerJoin(JSONObject json) {
        super((byte)3);
        this.ip = json.getString("a");
        this.port = json.getInt("b");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ip);
        jsonobject.put("b", this.port);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }
}
