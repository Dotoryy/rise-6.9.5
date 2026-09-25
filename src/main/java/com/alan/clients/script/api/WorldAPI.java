package com.alan.clients.script.api;

import com.alan.clients.Client;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.script.api.wrapper.impl.ScriptBlockPos;
import com.alan.clients.script.api.wrapper.impl.ScriptEntityLiving;
import com.alan.clients.script.api.wrapper.impl.ScriptWorld;
import com.alan.clients.component.impl.combat.TargetComponent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;

public class WorldAPI extends ScriptWorld {
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> {
        if (this.wrapped == null) {
            this.wrapped = MC.theWorld;
        }
    };

    public WorldAPI() {
        super(MC.theWorld);
        Client.a.e().b(this);
    }

    public ScriptEntityLiving[] getEntities() {
        Object[] aobject = MC.theWorld.loadedEntityList.stream().filter(var0 -> var0 instanceof EntityLivingBase).toArray();
        ScriptEntityLiving[] ascriptentityliving = new ScriptEntityLiving[aobject.length];

        for (int i = 0; i < aobject.length; i++) {
            ascriptentityliving[i] = new ScriptEntityLiving((EntityLivingBase)aobject[i]);
        }

        return ascriptentityliving;
    }

    public ScriptEntityLiving getTargetEntity(int var1) {
        EntityLivingBase entitylivingbase = TargetComponent.e(var1);
        return entitylivingbase != null ? new ScriptEntityLiving(entitylivingbase) : null;
    }

    public void removeEntity(int entity) {
        MC.theWorld.removeEntityFromWorld(entity);
    }

    public void removeEntity(ScriptEntityLiving entity) {
        this.removeEntity(entity.getEntityId());
    }

    public ScriptBlockPos newBlockPos(int var1, int var2, int var3) {
        return new ScriptBlockPos(new BlockPos(var1, var2, var3));
    }

    public String getBlockName(ScriptBlockPos scriptBlockPos) {
        return scriptBlockPos.getBlock().getName();
    }
}
