package com.alan.clients.module.impl.player;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;

@ModuleInfo(aliases = "module.player.fastuse.name", description = "module.player.fastuse.description", category = Category.PLAYER)
public class FastUse extends Module {
    private final NumberValue speed = new NumberValue("Speed", this, 1, 1, 24, 1);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        if (aEg.thePlayer.isUsingItem()) {
            for (int i = 0; i <= this.speed.wo().intValue(); i++) {
                PacketUtil.send(
                    new C06PacketPlayerPosLook(
                        aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, aEg.thePlayer.onGround
                    )
                );
            }
        }
    };

    public FastUse() {
    }
}
