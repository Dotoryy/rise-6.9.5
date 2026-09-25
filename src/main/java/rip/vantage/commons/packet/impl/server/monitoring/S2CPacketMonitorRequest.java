package rip.vantage.commons.packet.impl.server.monitoring;

import org.json.JSONArray;
import org.json.JSONObject;

public class S2CPacketMonitorRequest extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    public JSONArray ePS;
    public String ePT;

    public JSONArray aJU() {
        return this.ePS;
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ePS);
        jsonobject.put("b", this.ePT);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.b(this);
    }

    public String getVersion() {
        return this.ePT;
    }

    static {
    }


    public S2CPacketMonitorRequest(JSONObject json) {
        super((byte)27);
        this.ePS = json.optJSONArray("a");
        this.ePT = json.optString("b", "1");
    }
}
