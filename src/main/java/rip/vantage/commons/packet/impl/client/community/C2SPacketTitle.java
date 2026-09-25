package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public class C2SPacketTitle extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String message;
    private final int b;
    private final int c;
    private final int d;
    private final String e;
    private final String f;

    public C2SPacketTitle(String var1, int var2, int var3, int var4, String var5, String var6) {
        super((byte)9);
        this.message = var1;
        this.b = var2;
        this.c = var3;
        this.d = var4;
        this.e = var5;
        this.f = var6;
    }

    public C2SPacketTitle(JSONObject json) {
        super((byte)9);
        this.message = json.getString("a");
        this.b = json.getInt("b");
        this.c = json.getInt("c");
        this.d = json.getInt("d");
        this.e = json.getString("e");
        this.f = json.getString("f");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.message);
        jsonobject.put("b", this.b);
        jsonobject.put("c", this.c);
        jsonobject.put("d", this.d);
        jsonobject.put("e", this.e);
        jsonobject.put("f", this.f);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getMessage() {
        return this.message;
    }
}
