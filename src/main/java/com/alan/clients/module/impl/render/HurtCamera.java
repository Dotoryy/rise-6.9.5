package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.value.ConstantManager;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.MathHelper;

@ModuleInfo(aliases = "module.render.hurtcamera.name", description = "module.render.hurtcamera.description", category = Category.RENDER)
public final class HurtCamera extends Module {
    public final NumberValue intensity = new NumberValue("Intensity", this, 1, 0, 1, 0.1);
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var0 -> {
        if (var0.getPacket() instanceof S12PacketEntityVelocity s12packetentityvelocity && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
            double d0 = s12packetentityvelocity.motionX / 8000.0;
            double d1 = s12packetentityvelocity.motionZ / 8000.0;
            aEg.thePlayer.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * 180.0 / ConstantManager.aHb - aEg.thePlayer.pl);
        }
    };

    public HurtCamera() {
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.attackedAtYaw = 0.0F;
    }
}
