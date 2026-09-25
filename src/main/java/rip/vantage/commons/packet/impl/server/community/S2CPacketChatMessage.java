package rip.vantage.commons.packet.impl.server.community;

import org.json.JSONObject;

public class S2CPacketChatMessage extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private final String author;
    private final int product;
    private final String message;

    public S2CPacketChatMessage(String var1, int var2, String var3) {
        super((byte)4);
        this.author = var1;
        this.product = var2;
        this.message = var3;
    }

    public S2CPacketChatMessage(JSONObject json) {
        super((byte)4);
        this.author = json.getString("a");
        this.product = json.getInt("b");
        this.message = json.getString("c");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.author);
        jsonobject.put("b", this.product);
        jsonobject.put("c", this.message);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public String getAuthor() {
        return this.author;
    }

    public int getProduct() {
        return this.product;
    }

    public String getMessage() {
        return this.message;
    }
}
