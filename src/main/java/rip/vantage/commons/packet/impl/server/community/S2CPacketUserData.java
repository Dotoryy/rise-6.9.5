package rip.vantage.commons.packet.impl.server.community;

import org.json.JSONObject;

public class S2CPacketUserData extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final String message;

    public S2CPacketUserData(String var1) {
        super((byte)6);
        this.message = var1;
    }

    public S2CPacketUserData(JSONObject json) {
        super((byte)6);
        this.message = json.getString("a");
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
}
