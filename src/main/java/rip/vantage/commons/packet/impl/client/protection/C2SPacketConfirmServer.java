package rip.vantage.commons.packet.impl.client.protection;

import org.json.JSONObject;

public class C2SPacketConfirmServer extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String ip;
    private final int port;
    private final String username;

    public C2SPacketConfirmServer(String var1, int var2, String var3) {
        super((byte)3);
        this.ip = var1;
        this.port = var2;
        this.username = var3;
    }

    public C2SPacketConfirmServer(JSONObject json) {
        super((byte)3);
        this.ip = json.getString("a");
        this.port = json.getInt("b");
        this.username = json.getString("c");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ip);
        jsonobject.put("b", this.port);
        jsonobject.put("c", this.username);
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
