package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public class C2SPacketUserLookup extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String message;

    public C2SPacketUserLookup(String var1) {
        super((byte)6);
        this.message = var1;
    }

    public C2SPacketUserLookup(JSONObject json) {
        super((byte)6);
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
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getMessage() {
        return this.message;
    }
}
