package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONObject;

public class S2CPacketMonitorCommand extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    public String ePQ;
    public String ePP;

    public String aJS() {
        return this.ePP;
    }

    public String aJT() {
        return this.ePQ;
    }


    static {
    }

    public S2CPacketMonitorCommand(JSONObject json) {
        super((byte)28);
        this.ePP = json.getString("a");
        this.ePQ = json.getString("b");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.b(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ePP);
        jsonobject.put("b", this.ePQ);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }
}
