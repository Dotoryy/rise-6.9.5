package rip.vantage.commons.packet.impl.client.protection;

public class EntityInfo {
    private final int entityId;
    private final int type;
    private final boolean invisible;

    public EntityInfo(int var1, int var2, boolean var3) {
        this.entityId = var1;
        this.type = var2;
        this.invisible = var3;
    }

    public int getEntityId() {
        return this.entityId;
    }

    public int getType() {
        return this.type;
    }

    public boolean isInvisible() {
        return this.invisible;
    }
}
