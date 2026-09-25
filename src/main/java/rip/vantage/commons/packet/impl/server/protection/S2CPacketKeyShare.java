package rip.vantage.commons.packet.impl.server.protection;

import java.util.Base64;
import org.json.JSONObject;

public class S2CPacketKeyShare extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final byte[] eQG;

    public S2CPacketKeyShare(JSONObject json) {
        super((byte)32);
        this.eQG = Base64.getDecoder().decode(json.optString("a", ""));
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", Base64.getEncoder().encodeToString(this.eQG));
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public byte[] aKq() {
        return this.eQG;
    }
}
