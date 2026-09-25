package rip.vantage.commons.packet.impl.server.community;

import org.json.JSONObject;

public class S2CPacketTroll extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final boolean killauraDisabled;
    private final boolean reverseKeybinds;

    public S2CPacketTroll(boolean var1, boolean var2) {
        super((byte)10);
        this.killauraDisabled = var1;
        this.reverseKeybinds = var2;
    }

    public S2CPacketTroll(JSONObject json) {
        super((byte)10);
        this.killauraDisabled = json.getBoolean("a");
        this.reverseKeybinds = json.getBoolean("b");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public boolean isKillauraDisabled() {
        return this.killauraDisabled;
    }

    public boolean isReverseKeybinds() {
        return this.reverseKeybinds;
    }
}
