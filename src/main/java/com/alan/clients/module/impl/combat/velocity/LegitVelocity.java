package com.alan.clients.module.impl.combat.velocity;

import com.alan.clients.module.impl.combat.Velocity;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public final class LegitVelocity extends Mode<Velocity> {
    public final NumberValue chance = new NumberValue("Chance", this, 100, 0, 100, 1);
    public final BooleanValue legitTiming = new BooleanValue("Legit Timing", this, false);
    private boolean jump;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> this.jump = false;
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {
        if (!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) {
            if (this.jump && Math.random() * 100.0 < this.chance.wo().doubleValue()) {
                var1x.setJump(true);
                ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_21_2);
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if ((!this.getParent().onSwing.wo() || aEg.thePlayer.isSwingInProgress) && !var1x.isCancelled()) {
            if (aEg.thePlayer.onGround) {
                if (var1x.getPacket() instanceof S12PacketEntityVelocity s12packetentityvelocity
                    && s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()
                    && s12packetentityvelocity.motionY > 0
                    && (!this.legitTiming.wo() || aEg.thePlayer.ae <= 14 || aEg.thePlayer.cqL <= 1)) {
                    this.jump = true;
                }
            }
        }
    };

    public LegitVelocity(String var1, Velocity velocity) {
        super(var1, velocity);
    }
}
