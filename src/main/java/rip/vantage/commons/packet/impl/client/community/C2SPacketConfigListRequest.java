package rip.vantage.commons.packet.impl.client.community;

import org.json.JSONObject;

public class C2SPacketConfigListRequest extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    public C2SPacketConfigListRequest() {
        super((byte)11);
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }
}
