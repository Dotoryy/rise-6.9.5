package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONObject;

public class S2CPacketCaptureCancel extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    public String eQa;

    public String ajm() {
        return this.eQa;
    }

    static {
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    public S2CPacketCaptureCancel(JSONObject json) {
        super((byte)25);
        this.eQa = json.getString("a");
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.eQa);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public S2CPacketCaptureCancel(String var1) {
        super((byte)25);
        this.eQa = var1;
    }

}
