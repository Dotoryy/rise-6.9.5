package com.alan.clients.module.impl.movement.longjump;

import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public final class VulcanLongJump extends Mode<LongJump> {
    private boolean ignore;
    private int ticks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        this.ticks++;
        if (aEg.thePlayer.fallDistance > 0.0F && this.ticks % 2 == 0) {
            double d0;
            int i = (d0 = aEg.thePlayer.fallDistance - 2.2) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
        }

        switch (this.ticks) {
            case 1:
                aEg.thePlayer.jump();
            case 2:
            case 3:
            case 74:
            case 119:
            default:
                break;
            case 4:
                aEg.thePlayer.motionX *= 1.3;
                aEg.thePlayer.motionZ *= 1.3;
                break;
            case 5:
                aEg.thePlayer.motionX *= 1.05;
                aEg.thePlayer.motionZ *= 1.05;
                break;
            case 10:
                aEg.thePlayer.motionY = 2.0;
                break;
            case 11:
                aEg.thePlayer.motionY += 0.04;
                break;
            case 40:
                ChatUtil.b(aEg.thePlayer.motionY);
                aEg.thePlayer.motionY = -0.09800000190735147;
                break;
            case 62:
                ChatUtil.b(aEg.thePlayer.motionY);
                var1x.setOnGround(true);
                aEg.thePlayer.onGround = true;
                aEg.thePlayer.jump();
        }

        if (aEg.thePlayer.motionY < -1.53 && aEg.thePlayer.motionY > -1.54) {
            var1x.setOnGround(true);
            aEg.thePlayer.onGround = true;
            aEg.thePlayer.jump();
        }

        if (aEg.thePlayer.motionY < -0.51 && aEg.thePlayer.motionY > -0.52) {
            ChatUtil.b("if near void and the ghost block handler is on this may flag");
            aEg.thePlayer.motionY = 2.0;
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1x -> {
        if (var1x.getPacket() instanceof S08PacketPlayerPosLook && this.ignore) {
            var1x.setCancelled();
            this.ignore = false;
        }
    };

    public VulcanLongJump(String var1, LongJump longJump) {
        super(var1, longJump);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        if (!aEg.thePlayer.onGround) {
            this.toggle();
        }

        this.ignore = false;
        this.ticks = 0;
    }
}
