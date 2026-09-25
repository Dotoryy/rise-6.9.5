package rip.vantage.commons.packet.impl.client.protection;

import org.json.JSONObject;
import rip.vantage.commons.handler.api.PacketHandler;
import rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket;

public class C2SPacketAuthentication extends AbstractC2SPacket
{
    private final String username;
    private final String hazeid;
    private final String hwid;
    private final int product;

    public String aJt() {
        return null;
    }

    public C2SPacketAuthentication(final String eor, final String eot, final String eos, final int eou) {
        super((byte)1);
        this.username = eor;
        this.hwid = eot;
        this.hazeid = eos;
        this.product = eou;
    }

    public C2SPacketAuthentication(final JSONObject jsonObject) {
        super((byte)1);
        this.username = jsonObject.getString("a");
        this.hwid = jsonObject.getString("b");
        this.product = jsonObject.getInt("c");
        this.hazeid = jsonObject.getString("d");
    }


    public String aJk() {
        return null;
    }

    public String bX() {
        return null;
    }

    public String aJu() {
        return null;
    }

    public int getProduct() {
        return 0;
    }

    public void handle(final rip.vantage.commons.handler.api.C2SPacketHandler handler) {
    }
}
