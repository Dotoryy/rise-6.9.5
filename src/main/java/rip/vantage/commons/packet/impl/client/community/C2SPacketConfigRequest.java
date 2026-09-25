package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public class C2SPacketConfigRequest extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final String configID;

    public C2SPacketConfigRequest(String var1) {
        super((byte)10);
        this.configID = var1;
    }

    public C2SPacketConfigRequest(JSONObject json) {
        super((byte)10);
        this.configID = json.getString("a");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.configID);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getConfigID() {
        return this.configID;
    }
}
