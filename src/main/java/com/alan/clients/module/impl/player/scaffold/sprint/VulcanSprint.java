package com.alan.clients.module.impl.player.scaffold.sprint;

import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class VulcanSprint extends Mode<Scaffold> {
    private final NumberValue speed = new NumberValue("Speed", this, 1.3, 0.9, 1.3, 0.05);
    private int time;
    private int block;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1x -> {
        aEg.thePlayer.setSprinting(false);
        double d0 = this.speed.wo().doubleValue();
        if (!aEg.gameSettings.keyBindJump.isKeyDown() && d0 > 0.9 && aEg.thePlayer.cqL >= 2 && this.block <= 10) {
            MoveUtil.strafe(MoveUtil.getAllowedHorizontalDistance() * d0);
            aEg.thePlayer.jump();
            aEg.thePlayer.motionY = 0.012500047683714;
        }

        if (aEg.thePlayer.onGround) {
            MoveUtil.strafe();
        }

        this.time++;
        this.block++;
        switch (this.time) {
            case 1:
                PacketUtil.send(new C0BPacketEntityAction(aEg.thePlayer, Action.START_SNEAKING));
                break;
            case 10:
                this.time = 0;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        Packet packet = var1x.dq();
        if (packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)packet).getPlacedBlockDirection() != 255) {
            this.block = 0;
        }
    };

    public VulcanSprint(String var1, Scaffold scaffold) {
        super(var1, scaffold);
    }

    @Override
    public void onDisable() {
        if (this.time == 1) {
            PacketUtil.send(new C0BPacketEntityAction(aEg.thePlayer, Action.STOP_SNEAKING));
        }
    }
}
