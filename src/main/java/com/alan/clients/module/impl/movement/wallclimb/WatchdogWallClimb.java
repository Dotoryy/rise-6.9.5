package com.alan.clients.module.impl.movement.wallclimb;

import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.module.impl.movement.WallClimb;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.StrafeEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.chat.ChatUtil;
import java.util.Objects;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class WatchdogWallClimb extends Mode<WallClimb> {
    private double jumpMotion = 0.42;
    private boolean active;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer != null) {
            if (aEg.thePlayer.motionY < 0.0) {
                this.active = true;
            }

            if (this.active) {
                float f = aEg.thePlayer.pl;
                float f1 = aEg.thePlayer.movementInput.moveForward;
                if (f1 != 0.0F) {
                    f += f1 < 0.0F ? 180.0F : 0.0F;
                    f1 = f1 < 0.0F ? -0.5F : 0.5F;
                }

                float f2 = aEg.thePlayer.movementInput.moveStrafe;
                if (f2 != 0.0F) {
                    f += (f2 < 0.0F ? 90.0F : -90.0F) * f1;
                }

                double d0 = Math.toRadians(f);
                var1x.setYaw(f);
                if (!Objects.equals(LastConnectionComponent.port, "35565") && !Objects.equals(LastConnectionComponent.ip, "testnet.hypixel.net")) {
                    var1x.setPosX(var1x.getPosX() - Math.sin(d0) * 1.0E-8);
                    var1x.setPosZ(var1x.getPosZ() + Math.cos(d0) * 1.0E-8);
                }

                this.active = false;
            }

            if (aEg.thePlayer.isCollidedHorizontally && aEg.thePlayer.onGround && !aEg.gameSettings.keyBindJump.isKeyDown()) {
                aEg.thePlayer.jump();
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> {
        if (aEg.thePlayer.isCollidedHorizontally) {
            var0.setStrafe(0.0F);
        }
    };
    @EventLink
    public final Listener<StrafeEvent> onStrafe = var1x -> {
        if (aEg.thePlayer != null) {
            if (aEg.thePlayer.tR == 5) {
                double d0;
                int j = (d0 = MoveUtil.speed() - 0.0) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
            }

            if (aEg.thePlayer.tR == 6) {
                double d1;
                int i = (d1 = MoveUtil.speed() - 0.0) == 0.0 ? 0 : (d1 < 0.0 ? -1 : 1);
            }

            if (aEg.thePlayer.isCollidedHorizontally && aEg.gameSettings.keyBindJump.isKeyDown() && this.jumpMotion > 0.42) {
                ;
            }
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = var0 -> {
        if (aEg.thePlayer != null) {
            BlockPos blockpos = var0.getBlockPos();
            if (blockpos != null) {
                if (var0.getBlock() instanceof BlockAir) {
                    if (!(blockpos.getY() >= aEg.thePlayer.posY)) {
                        if (aEg.thePlayer.isCollidedHorizontally) {
                            if (!aEg.thePlayer.isOnLadder()) {
                                double d0 = (int)aEg.thePlayer.posY - 1.0;
                                AxisAlignedBB axisalignedbb = AxisAlignedBB.fromBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
                                    .offset(aEg.thePlayer.posX, d0, aEg.thePlayer.posZ);
                                AxisAlignedBB axisalignedbb1 = axisalignedbb.expand(-1.0E-7, 0.0, -1.0E-7);
                                var0.setBoundingBox(axisalignedbb1);
                            }
                        }
                    }
                }
            }
        }
    };

    public WatchdogWallClimb(String var1, WallClimb wallClimb) {
        super(var1, wallClimb);
    }

    @Override
    public void onEnable() {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)
            && LastConnectionComponent.ip != null
            && LastConnectionComponent.ip.contains("hypixel")) {
            ChatUtil.b("this will only work 1.8 on hypixel");
        } else {
            ChatUtil.b("don't move horizontally while you spider and only spider on full blocks");
        }

        this.active = false;
    }

    @Override
    public void onDisable() {
        aEg.timer.dzD = 1.0F;
        this.active = false;
    }
}
