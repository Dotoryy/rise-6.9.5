package rip.vantage.commons.packet.impl.client.protection;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class C2SPacketEntityList extends rip.vantage.commons.packet.api.abstracts.AbstractC2SPacket {
    private final List<EntityInfo> entityList;
    private final boolean players;
    private final boolean invisibles;
    private final boolean animals;
    private final boolean mobs;
    private final int uid;

    public C2SPacketEntityList(List<EntityInfo> var1, boolean var2, boolean var3, boolean var4, boolean var5, int var6) {
        super((byte)8);
        this.entityList = var1;
        this.players = var2;
        this.invisibles = var3;
        this.animals = var4;
        this.mobs = var5;
        this.uid = var6;
    }

    public C2SPacketEntityList(JSONObject json) {
        super((byte)8);
        JSONArray jsonarray = json.getJSONArray("a");
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            arraylist.add(new EntityInfo(jsonobject.getInt("a"), jsonobject.getInt("b"), jsonobject.getBoolean("c")));
        }

        this.entityList = arraylist;
        this.players = json.getBoolean("b");
        this.invisibles = json.getBoolean("c");
        this.animals = json.getBoolean("d");
        this.mobs = json.getBoolean("e");
        this.uid = json.getInt("f");
    }

    @Override
    public void handle(rip.vantage.commons.handler.api.C2SPacketHandler handler) {
        handler.handle(this);
    }

    @Override
    public String aJk() {
        JSONObject jsonobject = new JSONObject();
        JSONArray jsonarray = new JSONArray();

        for (EntityInfo g : this.entityList) {
            JSONObject jsonobject1 = new JSONObject();
            jsonobject1.put("a", g.getEntityId());
            jsonobject1.put("b", g.getType());
            jsonobject1.put("c", g.isInvisible());
            jsonarray.put(jsonobject1);
        }

        jsonobject.put("a", jsonarray);
        jsonobject.put("b", this.players);
        jsonobject.put("c", this.invisibles);
        jsonobject.put("d", this.animals);
        jsonobject.put("e", this.mobs);
        jsonobject.put("f", this.uid);
        jsonobject.put("id", this.getId());
        return jsonobject.toString();
    }

    public List<EntityInfo> getEntityList() {
        return this.entityList;
    }

    public boolean isPlayers() {
        return this.players;
    }

    public boolean isInvisibles() {
        return this.invisibles;
    }

    public boolean isAnimals() {
        return this.animals;
    }

    public boolean isMobs() {
        return this.mobs;
    }

    public int getUid() {
        return this.uid;
    }
}
