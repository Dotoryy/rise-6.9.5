package com.alan.clients.module.impl.player.antivoid;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.longjump.Watchdog2LongJump;
import com.alan.clients.module.impl.player.AntiVoid;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.PacketQueueComponent;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.a;
import net.minecraft.util.BlockPos;

public class WatchdogAntiVoid extends Mode<AntiVoid> {
    private Vector3d lastSafePosition;
    private Vector3d lastSafeMotion;
    private boolean blinking;
    private boolean positionSent;
    private int voidTicks;
    private boolean moving;
    private final NumberValue distance = new NumberValue("Distance", this, 3, 0, 10, 0.1);
    @EventLink(value = 1)
    public final Listener<MoveEvent> onMove = var1x -> {
        if (aEg.thePlayer.ticksExisted > 75) {
            if (!this.e(Flight.class).isEnabled()) {
                boolean flag = false;
                if (MoveUtil.enoughMovementForSprinting()) {
                    this.moving = true;
                } else {
                    this.moving = false;
                }

                for (int i = 0; i <= this.distance.wo().doubleValue() * 40.0; i++) {
                    WorldClient worldclient = PlayerUtil.aEg.theWorld;
                    Vector3d aka = MoveUtil.a(aEg.thePlayer, new Vector2f(aEg.thePlayer.moveStrafing, aEg.thePlayer.moveForward), i, true);
                    BlockPos blockpos = new BlockPos(aka.x, aka.y - i, aka.z);
                    if (worldclient.getBlockState(blockpos).getBlock() != Blocks.air || aEg.thePlayer.onGround) {
                        flag = false;
                        break;
                    }

                    flag = true;
                }

                if (flag) {
                    this.voidTicks++;
                } else if (aEg.thePlayer.onGround) {
                    this.voidTicks = 0;
                }

                if (!flag
                    || this.lastSafePosition == null
                    || this.lastSafeMotion == null
                    || !(this.voidTicks < 30.0 + this.distance.wo().doubleValue() * 30.0)
                    || this.e(LongJump.class).isEnabled()
                        && (
                            Client.a.g().c(LongJump.class).mode.wo().getName().equals("Watchdog Fire Ball")
                                || Client.a.g().c(LongJump.class).mode.wo().getName().equals("Watchdog Fire Ball 2")
                        )
                    || this.e(LongJump.class).isEnabled() && Watchdog2LongJump.launched
                    || this.e(Flight.class).isEnabled()
                    || aEg.thePlayer.inventory.getStackInSlot(0) != null && aEg.thePlayer.inventory.getStackInSlot(0).getItem() == Items.compass
                    || this.e(Scaffold.class).isEnabled()) {
                    this.positionSent = false;
                    if (this.blinking) {
                        BlinkComponent.dispatch();
                        this.blinking = false;
                    }

                    this.lastSafeMotion = new Vector3d(aEg.thePlayer.motionX, aEg.thePlayer.motionY, aEg.thePlayer.motionZ);
                    this.lastSafePosition = new Vector3d(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
                } else if (!this.positionSent) {
                    this.blinking = true;
                    BlinkComponent.blink();
                    PacketQueueComponent.a(C0FPacketConfirmTransaction.class, a.class, C01PacketChatMessage.class);
                    if (FallDistanceComponent.cY > this.distance.wo().doubleValue() || this.positionSent) {
                        PacketUtil.sendNoEvent(new C04PacketPlayerPosition(this.lastSafePosition.x, this.lastSafePosition.y - -0.09800000190735147, this.lastSafePosition.z, false));
                        PacketQueueComponent.cQ.clear();
                        FallDistanceComponent.cY = 0.0F;
                        this.positionSent = true;
                    }
                } else {
                    BlinkComponent.dispatch();
                }
            }
        }
    };

    public WatchdogAntiVoid(String var1, AntiVoid antiVoid) {
        super(var1, antiVoid);
    }
}
