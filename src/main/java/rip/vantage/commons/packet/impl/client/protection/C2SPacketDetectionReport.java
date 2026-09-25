package rip.vantage.commons.packet.impl.client.protection;

import org.json.JSONObject;

public class C2SPacketDetectionReport extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    public String ePj;
    public String ePk;
    public String ePi;

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.ePi);
        jsonobject.put("b", this.ePj);
        jsonobject.put("c", this.ePk);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getReason() {
        return this.ePk;
    }

    public String si() {
        return this.ePj;
    }

    public C2SPacketDetectionReport(String var1, String var2) {
        super((byte)30);
        this.ePi = var1;
        this.ePj = "";
        this.ePk = var2;
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    public C2SPacketDetectionReport(JSONObject json) {
        super((byte)30);
        this.ePi = json.getString("a");
        this.ePj = json.optString("b", "");
        this.ePk = json.optString("c", "");
    }

    static {
    }


    public String aJD() {
        return this.ePi;
    }
}
