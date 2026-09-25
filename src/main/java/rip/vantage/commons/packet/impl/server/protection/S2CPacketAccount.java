package rip.vantage.commons.packet.impl.server.protection;

import org.json.JSONObject;

public class S2CPacketAccount extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final String username;
    private final String uuid;
    private final String accessToken;
    private final String refreshToken;
    private String altSkin;

    public S2CPacketAccount(String var1, String var2, String var3, String var4) {
        super((byte)4);
        this.username = var1;
        this.uuid = var2;
        this.accessToken = var3;
        this.refreshToken = var4;
    }

    public S2CPacketAccount(JSONObject json) {
        super((byte)4);
        this.username = json.getString("a");
        this.uuid = json.getString("b");
        this.accessToken = json.getString("c");
        this.refreshToken = json.getString("d");
        if (json.has("e")) {
            this.altSkin = new rip.vantage.commons.packet.impl.client.protection.AltSkin(json.getString("e")).toString();
        }
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.username);
        jsonobject.put("b", this.uuid);
        jsonobject.put("c", this.accessToken);
        jsonobject.put("d", this.refreshToken);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getUsername() {
        return this.username;
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getAltSkin() {
        return this.altSkin;
    }
}
