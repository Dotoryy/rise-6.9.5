package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import java.util.ArrayList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.a;

public class GrimServer19NoFall extends Mode<NoFall> {
    private boolean shouldNoFall;
    public static boolean holdingPackets = false;
    public static boolean releasing;
    private int tR;
    private final ArrayList<Packet<?>> heldPackets = new ArrayList<>();
    private float ub;
    private double vB;
    private double vC;
    private final BooleanValue newestGrimMayFlagTheAnticheat = new BooleanValue("Newest Grim, may flag the anticheat", this, false);
    private boolean shouldJump;
    @EventLink(value = 2)
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (!releasing && aEg.thePlayer.ticksExisted >= 10 && !aEg.thePlayer.isCollidedHorizontally) {
            switch (var1x.getPacket()) {
                case S12PacketEntityVelocity s12packetentityvelocity:
                    if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId() && !var1x.isCancelled()) {
                        if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId() && s12packetentityvelocity.motionY > 0
                            || aEg.thePlayer.ae <= 14
                            || aEg.thePlayer.cqL <= 1) {
                            this.shouldJump = true;
                        }

                        if (!aEg.thePlayer.onGround && this.shouldNoFall) {
                            holdingPackets = true;
                        }
                    }
                    break;
                case S32PacketConfirmTransaction s32packetconfirmtransaction:
                    if (holdingPackets) {
                        return;
                    }
                    break;
                case a a:
                    if (holdingPackets) {
                        return;
                    }
                    break;
                default:
            }
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (!aEg.thePlayer.isCollidedHorizontally) {
            float f = FallDistanceComponent.cY;
            if (aEg.thePlayer.motionY > 0.1) {
                this.shouldNoFall = false;
            }

            if (f > 3.0F) {
                this.shouldNoFall = true;
            }

            if (this.shouldNoFall && aEg.thePlayer.onGround) {
                if (!this.newestGrimMayFlagTheAnticheat.wo()) {
                    var1x.setCancelled(true);
                } else {
                    PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + 0.01, aEg.thePlayer.posZ, true));
                }

                var1x.setCancelled(true);
                PacketUtil.send(new C03PacketPlayer(true));
                aEg.gameSettings.keyBindJump.setPressed(false);
                f = 0.0F;
            }

            if (this.shouldNoFall) {
                aEg.gameSettings.keyBindJump.setPressed(false);
            }

            if (PlayerUtil.vi() > 5.0 && aEg.thePlayer.tR == 9 && aEg.thePlayer.Zl > 200 && this.newestGrimMayFlagTheAnticheat.wo()) {
                aEg.thePlayer.motionY = MoveUtil.predictedMotion(aEg.thePlayer.motionY, 10);
            }

            FallDistanceComponent.cY = f;
        }
    };
    @EventLink(value = 2)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        if (holdingPackets && aEg.thePlayer.onGround) {
            releasing = true;
            holdingPackets = false;
            this.heldPackets.forEach(PacketUtil::receive);
            this.heldPackets.clear();
            releasing = false;
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1x -> {
        if (!aEg.thePlayer.isCollidedHorizontally) {
            if (this.shouldJump && this.shouldNoFall) {
                var1x.setJump(true);
                this.shouldJump = false;
            } else if (this.shouldNoFall) {
            }
        }
    };

    public GrimServer19NoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }

    @Override
    public void onEnable() {
        this.shouldNoFall = false;
    }
}
