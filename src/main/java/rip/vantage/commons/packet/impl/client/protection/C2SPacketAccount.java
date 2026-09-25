package rip.vantage.commons.packet.impl.client.protection;

import org.json.JSONObject;

public class C2SPacketAccount extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String refreshToken;
    private String altSkin;

    public C2SPacketAccount(String var1) {
        super((byte)4);
        this.refreshToken = var1;
    }

    public C2SPacketAccount(JSONObject json) {
        super((byte)4);
        this.refreshToken = json.getString("a");
        if (json.has("b")) {
            this.altSkin = new AltSkin(json.getString("b")).toString();
        }
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.refreshToken);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getAltSkin() {
        return this.altSkin;
    }
}
