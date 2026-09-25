package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public class C2SPacketChatMessage extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String message;
    private static final String productRise = "63d0f9bc46ca6bf7ad9572b7";

    public C2SPacketChatMessage(String var1) {
        super((byte)4);
        this.message = var1;
    }

    public C2SPacketChatMessage(JSONObject json) {
        super((byte)4);
        this.message = json.getString("a");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.message);
        jsonobject.put("b", "63d0f9bc46ca6bf7ad9572b7");
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getMessage() {
        return this.message;
    }
}
