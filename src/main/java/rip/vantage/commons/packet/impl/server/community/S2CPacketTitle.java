package rip.vantage.commons.packet.impl.server.community;

import org.json.JSONObject;

public class S2CPacketTitle extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final String message;
    private final int b;
    private final int c;
    private final int d;
    private final String e;

    public S2CPacketTitle(String var1, int var2, int var3, int var4, String var5) {
        super((byte)9);
        this.message = var1;
        this.b = var2;
        this.c = var3;
        this.d = var4;
        this.e = var5;
    }

    public S2CPacketTitle(JSONObject json) {
        super((byte)9);
        this.message = json.getString("a");
        this.b = json.getInt("b");
        this.c = json.getInt("c");
        this.d = json.getInt("d");
        this.e = json.getString("e");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.message);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getMessage() {
        return this.message;
    }

    public int getFadeInTime() {
        return this.b;
    }

    public int getDisplayTime() {
        return this.c;
    }

    public int getFadeOutTime() {
        return this.d;
    }

    public String getColor() {
        return this.e;
    }
}
