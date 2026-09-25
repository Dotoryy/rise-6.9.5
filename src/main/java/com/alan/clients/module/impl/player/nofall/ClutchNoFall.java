package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.module.impl.player.nofall.ClutchState;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import rip.vantage.commons.util.time.StopWatch;

public class ClutchNoFall extends Mode<NoFall> {
    private ClutchState state = ClutchState.IDLE;
    private BlockPos landingPos = null;
    private BlockPos waterPos = null;
    private StopWatch stateTimer = new StopWatch();
    private StopWatch placeTimer = new StopWatch();
    private int waterSlot = -1;
    private int bucketSlot = -1;
    private boolean placed = false;
    private boolean pendingPlace = false;
    private boolean pendingPickup = false;
    private Vector2f rotations = null;
    @EventLink(value = 2)
    public final Listener<PreUpdateEvent> onPreUpdate = var1x -> {
        switch (this.state) {
            case IDLE:
                if (aEg.thePlayer.onGround || aEg.thePlayer.motionY >= 0.0 || aEg.thePlayer.fallDistance < 2.0F) {
                    return;
                }

                this.waterSlot = com.alan.clients.util.player.SlotUtil.findItem(Items.water_bucket);
                if (this.waterSlot == -1) {
                    return;
                }

                this.state = ClutchState.PREDICT;
                this.stateTimer.aX();
                break;
            case PREDICT:
                this.landingPos = this.predictLandingPos();
                if (this.landingPos == null) {
                    return;
                }

                double d0 = aEg.thePlayer.posY - this.landingPos.getY();
                double d1 = 8.0;
                if (d0 < d1 && d0 > 3.0) {
                    this.state = ClutchState.ROTATE;
                    this.stateTimer.aX();
                } else if (this.stateTimer.T(50L)) {
                    this.state = ClutchState.IDLE;
                }
                break;
            case ROTATE:
                SlotComponent slotcomponent = this.d(SlotComponent.class);
                SlotComponent.setSlot(this.waterSlot);
                RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 90.0F), 5.0, MovementFix.NORMAL);
                if (Math.abs(aEg.thePlayer.rotationPitch - 90.0F) < 15.0F || this.stateTimer.T(200L)) {
                    this.pendingPlace = true;
                    this.rotations = new Vector2f(aEg.thePlayer.pl, 90.0F);
                    this.state = ClutchState.PLACE;
                    this.stateTimer.aX();
                }
                break;
            case PLACE:
                this.pendingPlace = true;
                break;
            case WAIT_LAND:
                if (aEg.thePlayer.onGround || this.stateTimer.T(3000L)) {
                    if (this.waterPos != null) {
                        this.bucketSlot = com.alan.clients.util.player.SlotUtil.findItem(Items.bucket);
                        ChatUtil.b("S");
                        if (this.bucketSlot != -1) {
                            this.state = ClutchState.PICKUP;
                            this.stateTimer.aX();
                        } else {
                            this.reset();
                        }
                    } else {
                        this.reset();
                    }
                }
                break;
            case PICKUP:
                if (this.waterPos == null) {
                    this.reset();
                } else if (aEg.thePlayer.getDistance(this.waterPos.getX() + 0.5, this.waterPos.getY() + 0.5, this.waterPos.getZ() + 0.5) < 5.0) {
                    SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                    SlotComponent.setSlot(this.bucketSlot);
                    aEg.thePlayer.inventory.currentItem = this.bucketSlot;
                    Vec3 vec3 = new Vec3(this.waterPos.getX() + 0.5, this.waterPos.getY() + 0.5, this.waterPos.getZ() + 0.5);
                    this.rotations = RotationUtil.h(vec3);
                    RotationComponent.setRotations(this.rotations, 10.0, MovementFix.NORMAL);
                    this.pendingPickup = true;
                } else {
                    this.reset();
                }
        }
    };
    @EventLink(value = 4)
    public final Listener<PreMotionEvent> aim = var1x -> {
        if (this.state == ClutchState.PLACE && this.pendingPlace) {
            RotationComponent.setRotations(this.rotations, 10.0, MovementFix.NORMAL);
            float f = aEg.thePlayer.rotationPitch;
            aEg.thePlayer.pl = this.rotations.x;
            aEg.thePlayer.rotationPitch = this.rotations.y;
            aEg.entityRenderer.getMouseOver(1.0F);
            SlotComponent slotcomponent = this.d(SlotComponent.class);
            SlotComponent.setSlot(this.waterSlot);
            aEg.rightClickDelayTimer = 0;
            aEg.Az();
            aEg.thePlayer.rotationPitch = f;
            this.waterPos = new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY - 1.0, aEg.thePlayer.posZ);
            this.placed = true;
            this.placeTimer.aX();
            ChatUtil.b("MLG water placed at distance: " + (aEg.thePlayer.posY - this.landingPos.getY()));
            this.state = ClutchState.WAIT_LAND;
            this.stateTimer.aX();
            this.pendingPlace = false;
        } else if (this.state == ClutchState.PICKUP && this.pendingPickup) {
            float f1 = aEg.thePlayer.rotationPitch;
            aEg.entityRenderer.getMouseOver(1.0F);
            aEg.rightClickDelayTimer = 0;
            ChatUtil.b("Picked up water MLG");
            this.reset();
            this.pendingPickup = false;
        }
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1x -> {
        if (this.state == ClutchState.PLACE && this.pendingPlace) {
            var1x.setCancelled(true);
            this.pendingPlace = false;
        } else if (this.state == ClutchState.PICKUP && this.pendingPickup) {
            var1x.setCancelled(true);
            this.pendingPickup = false;
        }
    };

    public ClutchNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }

    private BlockPos predictLandingPos() {
        double d0 = aEg.thePlayer.posX;
        double d1 = aEg.thePlayer.posY;
        double d2 = aEg.thePlayer.posZ;
        double d3 = aEg.thePlayer.motionX;
        double d4 = aEg.thePlayer.motionY;
        double d5 = aEg.thePlayer.motionZ;
        AxisAlignedBB axisalignedbb = aEg.thePlayer.getEntityBoundingBox();
        int i = 0;

        while (d1 > 0.0 && i++ < 200) {
            float f = aEg.thePlayer.movementInput.moveStrafe;
            float moveForward = aEg.thePlayer.movementInput.moveForward;
            float f2 = aEg.thePlayer.pl;
            float f3 = f * f + moveForward * moveForward;
            if (f3 >= 1.0E-4F) {
                float f4 = MathHelper.sqrt_float(f3);
                if (f4 < 1.0F) {
                    f4 = 1.0F;
                }

                float f5 = 0.02F / f4;
                float f6 = f * f5;
                float f7 = moveForward * f5;
                float sin = MathHelper.sin(f2 * (float) Math.PI / 180.0F);
                float cos = MathHelper.cos(f2 * (float) Math.PI / 180.0F);
                d3 += f6 * cos - f7 * sin;
                d5 += f7 * cos + f6 * sin;
            }

            double d6 = d4 - 0.08;
            double d7 = d6 * 0.98F;
            double d8 = d3 * 0.91F;
            double d9 = d5 * 0.91F;
            double d10 = d8;
            double d11 = d7;
            double d12 = d9;
            AxisAlignedBB axisalignedbb1 = axisalignedbb.addCoord(d10, 0.0, 0.0);
            Iterator iterator = aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb1).iterator();

            while (iterator.hasNext()) {
                d10 = ((AxisAlignedBB)iterator.next()).calculateXOffset(axisalignedbb, d10);
            }

            AxisAlignedBB axisalignedbb2 = axisalignedbb.offset(d10, 0.0, 0.0);
            d0 += d10;
            double d13 = d11;
            AxisAlignedBB axisalignedbb3 = axisalignedbb2.addCoord(0.0, d11, 0.0);
            Iterator iterator1 = aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb3).iterator();

            while (iterator1.hasNext()) {
                d11 = ((AxisAlignedBB)iterator1.next()).calculateYOffset(axisalignedbb2, d11);
            }

            AxisAlignedBB axisalignedbb4 = axisalignedbb2.offset(0.0, d11, 0.0);
            d1 += d11;
            AxisAlignedBB axisalignedbb5 = axisalignedbb4.addCoord(0.0, 0.0, d12);
            Iterator iterator2 = aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb5).iterator();

            while (iterator2.hasNext()) {
                d12 = ((AxisAlignedBB)iterator2.next()).calculateZOffset(axisalignedbb4, d12);
            }

            axisalignedbb = axisalignedbb4.offset(0.0, 0.0, d12);
            d2 += d12;
            if (d11 != d13 && d13 < 0.0) {
                return new BlockPos(d0, d1, d2);
            }

            if (Math.abs(d8) < 0.003) {
            }

            if (Math.abs(d7) < 0.003) {
            }

            if (Math.abs(d9) < 0.003) {
            }

            d3 = d10;
            d4 = d11;
            d5 = d12;
        }

        return null;
    }

    private void reset() {
        this.state = ClutchState.IDLE;
        this.landingPos = null;
        this.waterPos = null;
        this.waterSlot = -1;
        this.bucketSlot = -1;
        this.placed = false;
        this.stateTimer.aX();
        this.placeTimer.aX();
        this.pendingPlace = false;
        this.pendingPickup = false;
        this.rotations = null;
    }
}
