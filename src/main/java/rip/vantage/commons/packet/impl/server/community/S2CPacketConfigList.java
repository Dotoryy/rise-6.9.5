package rip.vantage.commons.packet.impl.server.community;

import org.json.JSONArray;
import org.json.JSONObject;

public class S2CPacketConfigList extends rip.vantage.commons.packet.api.abstracts.AbstractS2CPacket {
    private JSONArray configs;
    private JSONArray scripts;
    private final String type;

    public S2CPacketConfigList(JSONObject json) {
        super((byte)11);

        try {
            this.configs = json.getJSONArray("a");
        } catch (Exception exception1) {
            this.configs = new JSONArray();
        }

        try {
            this.scripts = json.getJSONArray("b");
        } catch (Exception exception) {
            this.scripts = new JSONArray();
        }

        this.type = json.getString("c");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.S2CPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        jsonobject.put("a", this.configs);
        jsonobject.put("b", this.scripts);
        jsonobject.put("c", this.type);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public JSONArray getConfigs() {
        return this.configs;
    }

    public JSONArray getScripts() {
        return this.scripts;
    }

    public String getType() {
        return this.type;
    }
}
