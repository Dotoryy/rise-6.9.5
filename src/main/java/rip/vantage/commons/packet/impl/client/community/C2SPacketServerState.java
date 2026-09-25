package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public class C2SPacketServerState extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String serverIp;

    public C2SPacketServerState(String var1) {
        super((byte)5);
        this.serverIp = var1;
    }

    public C2SPacketServerState(JSONObject json) {
        super((byte)5);
        this.serverIp = json.getString("a");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.serverIp);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getServerIp() {
        return this.serverIp;
    }
}
