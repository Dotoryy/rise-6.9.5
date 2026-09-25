package rip.vantage.commons.packet.impl.server.protection;

import java.util.Base64;
import org.json.JSONObject;

public class S2CPacketProofOfWorkChallenge extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final byte[] challenge;
    private final long timestamp;

    public S2CPacketProofOfWorkChallenge(byte[] var1, long var2) {
        super((byte)21);
        this.challenge = var1;
        this.timestamp = var2;
    }

    public S2CPacketProofOfWorkChallenge(JSONObject json) {
        super((byte)21);
        this.challenge = Base64.getDecoder().decode(json.getString("a"));
        this.timestamp = json.getLong("b");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", Base64.getEncoder().encodeToString(this.challenge));
        jsonobject.put("b", this.timestamp);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public byte[] getChallenge() {
        return this.challenge;
    }

    public long getTimestamp() {
        return this.timestamp;
    }
}
