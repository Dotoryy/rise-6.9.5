package com.alan.clients.module.impl.movement.flight;

import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.player.DamageUtil;
import com.alan.clients.util.player.DamageType;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;

public class VerusDamageNewFlight extends Mode<Flight> {
    private int It;
    private int ticks;
    private boolean flying;
    private final NumberValue speed = new NumberValue("Speed", this, 1, 0.1, 9.5, 0.1);
    private double y;
    @EventLink
    public final Listener<TeleportEvent> onTeleport = var0 -> {};
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (!this.flying && aEg.thePlayer.onGround) {
            aEg.thePlayer.jump();
        }

        if (this.flying) {
            this.y = Math.floor(aEg.thePlayer.posY);
            aEg.thePlayer.motionY = 0.0 + (aEg.gameSettings.keyBindJump.isKeyDown() ? 1.0 : 0.0) - (aEg.gameSettings.keyBindSneak.isKeyDown() ? 1.0 : 0.0);
            if (aEg.thePlayer.getDistance(aEg.thePlayer.lastReportedPosX, aEg.thePlayer.lastReportedPosY, aEg.thePlayer.lastReportedPosZ) <= 8.5) {
                var1x.setCancelled();
            } else {
                this.ticks++;
                if (this.ticks >= 20) {
                    this.flying = false;
                    ChatUtil.b("s");
                    MoveUtil.stop();
                }
            }
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (this.flying) {
            float f = this.speed.wo().floatValue();
            var1x.setSpeed(f);
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var1x -> {
        if (var1x.getBlock() instanceof BlockAir
            && !aEg.gameSettings.keyBindSneak.isKeyDown()
            && (aEg.thePlayer.posY < this.y + 1.0 || aEg.gameSettings.keyBindJump.isKeyDown())) {
            double d0 = var1x.getBlockPos().getX();
            double d1 = var1x.getBlockPos().getY();
            double d2 = var1x.getBlockPos().getZ();
            if (d1 < aEg.thePlayer.posY) {
                var1x.setBoundingBox(AxisAlignedBB.fromBounds(-15.0, -1.0, -15.0, 15.0, 1.0, 15.0).offset(d0, d1, d2));
            }
        }
    };

    public VerusDamageNewFlight(String var1, Flight flight) {
        super(var1, flight);
    }

    @Override
    public void onEnable() {
        DamageUtil.damagePlayer(DamageType.POSITION, 3.42F, 1, false, false);
        this.ticks = 0;
        this.It = 2;
        this.flying = true;
    }

    @Override
    public void onDisable() {
        MoveUtil.stop();
    }
}
