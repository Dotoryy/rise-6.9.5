package com.alan.clients.util.packet;

import com.alan.clients.util.packet.EntityPositionSnapshot;
import java.util.Map;
import java.util.function.BiFunction;
import lombok.Generated;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.aa;
import net.minecraft.network.play.server.q;
import net.minecraft.network.play.server.z;

public enum EntityPositionPacket {
    SPAWN_MOB(q.class, (packet, map) -> {
        q q2 = (q)packet;
        int n = q2.getEntityID();
        double d = (double)q2.we() / 32.0;
        double d2 = (double)q2.wf() / 32.0;
        double d3 = (double)q2.wi() / 32.0;
        float f = (float)(q2.agi() * 360) / 256.0f;
        float f2 = (float)(q2.agj() * 360) / 256.0f;
        EntityPositionSnapshot cd2 = new EntityPositionSnapshot(d, d2, d3, f, f2);
        map.put(n, cd2);
        return map;
    }),
    ENTITY_TELEPORT(z.class, (packet, map) -> {
        z z2 = (z)packet;
        int n = z2.getEntityId();
        double d = (double)z2.we() / 32.0;
        double d2 = (double)z2.wf() / 32.0;
        double d3 = (double)z2.wi() / 32.0;
        float f = (float)(z2.agi() * 360) / 256.0f;
        float f2 = (float)(z2.agj() * 360) / 256.0f;
        EntityPositionSnapshot cd2 = new EntityPositionSnapshot(d, d2, d3, f, f2);
        map.put(n, cd2);
        return map;
    }),
    ENTITY_REL_MOVE(S14PacketEntity.S15PacketEntityRelMove.class, (packet, map) -> {
        S14PacketEntity.S15PacketEntityRelMove s15PacketEntityRelMove = (S14PacketEntity.S15PacketEntityRelMove)packet;
        int n = s15PacketEntityRelMove.entityId;
        EntityPositionSnapshot cd2 = (EntityPositionSnapshot)map.get(n);
        if (cd2 != null) {
            double d = cd2.gW + (double)s15PacketEntityRelMove.agC() / 32.0;
            double d2 = cd2.gX + (double)s15PacketEntityRelMove.agD() / 32.0;
            double d3 = cd2.gY + (double)s15PacketEntityRelMove.agE() / 32.0;
            float f = cd2.gZ;
            float f2 = cd2.ha;
            EntityPositionSnapshot cd3 = new EntityPositionSnapshot(d, d2, d3, f, f2);
            map.put(n, cd3);
            return map;
        }
        return null;
    }),
    ENTITY_LOOK_MOVE(S14PacketEntity.S17PacketEntityLookMove.class, (packet, map) -> {
        S14PacketEntity.S17PacketEntityLookMove s17PacketEntityLookMove = (S14PacketEntity.S17PacketEntityLookMove)packet;
        int n = s17PacketEntityLookMove.getEntityId();
        double d = (double)s17PacketEntityLookMove.agC() / 32.0;
        double d2 = (double)s17PacketEntityLookMove.agD() / 32.0;
        double d3 = (double)s17PacketEntityLookMove.agE() / 32.0;
        float f = (float)(s17PacketEntityLookMove.agi() * 360) / 256.0f;
        float f2 = (float)(s17PacketEntityLookMove.agj() * 360) / 256.0f;
        EntityPositionSnapshot cd2 = (EntityPositionSnapshot)map.get(n);
        if (cd2 != null) {
            double d4 = cd2.gW + d;
            double d5 = cd2.gX + d2;
            double d6 = cd2.gY + d3;
            EntityPositionSnapshot cd3 = new EntityPositionSnapshot(d4, d5, d6, f, f2);
            map.put(n, cd3);
            return map;
        }
        return null;
    }),
    ENTITY_LOOK(S14PacketEntity.S16PacketEntityLook.class, (packet, map) -> {
        S14PacketEntity.S16PacketEntityLook s16PacketEntityLook = (S14PacketEntity.S16PacketEntityLook)packet;
        int n = s16PacketEntityLook.getEntityId();
        float f = (float)(s16PacketEntityLook.agi() * 360) / 256.0f;
        float f2 = (float)(s16PacketEntityLook.agj() * 360) / 256.0f;
        EntityPositionSnapshot cd2 = (EntityPositionSnapshot)map.get(n);
        if (cd2 != null) {
            EntityPositionSnapshot cd3 = new EntityPositionSnapshot(cd2.gW, cd2.gX, cd2.gY, f, f2);
            map.put(n, cd3);
            return map;
        }
        return null;
    }),
    DESTROY_ENTITY(S13PacketDestroyEntities.class, (packet, map) -> {
        for (int n : ((S13PacketDestroyEntities)packet).getEntityIDs()) {
            map.remove(n);
        }
        return map;
    }),
    ENTITY_HEAD_LOOK(aa.class, (packet, map) -> {
        aa aa2 = (aa)packet;
        int n = aa2.getEntityId();
        float f = (float)(aa2.agi() * 360) / 256.0f;
        EntityPositionSnapshot cd2 = (EntityPositionSnapshot)map.get(n);
        if (cd2 != null) {
            EntityPositionSnapshot cd3 = new EntityPositionSnapshot(cd2.gW, cd2.gX, cd2.gY, f, cd2.ha);
            map.put(n, cd3);
            return map;
        }
        return null;
    });

    private final Class<? extends Packet<?>> clazz;
    private final BiFunction<Packet<?>, Map<Integer, EntityPositionSnapshot>, Map<Integer, EntityPositionSnapshot>> biFunction;
    private static final EntityPositionPacket[] $VALUES = EntityPositionPacket.cf();



    private EntityPositionPacket(Class<? extends Packet<?>> clazz, BiFunction<Packet<?>, Map<Integer, EntityPositionSnapshot>, Map<Integer, EntityPositionSnapshot>> biFunction) {
        this.clazz = clazz;
        this.biFunction = biFunction;
    }

    public Map<Integer, EntityPositionSnapshot> a(Packet<?> packet, Map<Integer, EntityPositionSnapshot> map) {
        return this.biFunction.apply(packet, map);
    }

    @Generated
    public Class<? extends Packet<?>> getClazz() {
        return this.clazz;
    }

    private static EntityPositionPacket[] cf() {
        return new EntityPositionPacket[]{SPAWN_MOB, ENTITY_TELEPORT, ENTITY_REL_MOVE, ENTITY_LOOK_MOVE, ENTITY_LOOK, DESTROY_ENTITY, ENTITY_HEAD_LOOK};
    }
}
