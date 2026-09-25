package rip.vantage.commons.packet.impl.client.protection;

import java.util.Base64;
import org.json.JSONObject;

public class C2SPacketProofOfWorkSolution extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    public byte[] proof;
    public int checksumCount;
    public long timestamp;

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", Base64.getEncoder().encodeToString(this.proof));
        jsonobject.put("b", this.timestamp);
        jsonobject.put("c", this.checksumCount);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public C2SPacketProofOfWorkSolution(byte[] var1, long var2, int var4) {
        super((byte)20);
        this.proof = var1;
        this.timestamp = var2;
        this.checksumCount = var4;
    }


    public byte[] getProof() {
        return this.proof;
    }

    public C2SPacketProofOfWorkSolution(JSONObject json) {
        super((byte)20);
        this.proof = Base64.getDecoder().decode(json.getString("a"));
        this.timestamp = json.getLong("b");
        this.checksumCount = json.getInt("c");
    }

    static {
    }

    public int getChecksumCount() {
        return this.checksumCount;
    }
}
