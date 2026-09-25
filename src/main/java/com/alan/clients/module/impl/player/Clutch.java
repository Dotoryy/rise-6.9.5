package com.alan.clients.module.impl.player;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.EnumFacingOffset;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.m;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

@ModuleInfo(aliases = "module.player.clutch.name", description = "module.player.clutch.description", category = Category.PLAYER)
public class Clutch extends Module {
    private final BoundsNumberValue rotationSpeed = new BoundsNumberValue("Rotation Speed", this, 5, 10, 0, 10, 1);
    private final BoundsNumberValue placeDelay = new BoundsNumberValue("Place Delay", this, 0, 0, 0, 1, 1);
    private Vec3 placePosition;
    private EnumFacingOffset facing;
    private BlockPos placeBlock;
    private float yaw;
    private float pitch;
    private int placeTicks;
    private int activeTicks;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        if (aEg.thePlayer.Zl > 15
            && aEg.thePlayer.ticksExisted > 50
            && !BadPacketsComponent.aW()
            && !this.e(Scaffold.class).isEnabled()
            && aEg.gameSettings.keyBindSneak.isKeyDown()) {
            if (aEg.thePlayer.tR > 3 && !PlayerUtil.ad(10.0)) {
                this.activeTicks = 10;
            }

            if (this.activeTicks-- >= 0) {
                SlotComponent slotcomponent = this.d(SlotComponent.class);
                SlotComponent.setSlot(SlotUtil.vx());
                Vec3i vec3i = new Vec3i(0, 0, 0);
                if (PlayerUtil.p(vec3i.getX(), -1 + vec3i.getY(), vec3i.getZ()).isReplaceable(aEg.theWorld, new BlockPos(aEg.thePlayer).down())) {
                    this.placeTicks++;
                } else {
                    this.placeTicks = 0;
                }

                this.placePosition = PlayerUtil.getPlacePossibility(vec3i.getX(), vec3i.getY(), vec3i.getZ());
                if (this.placePosition != null) {
                    this.facing = PlayerUtil.getEnumFacing(this.placePosition);
                    if (this.facing != null) {
                        BlockPos blockpos = new BlockPos(this.placePosition.xCoord, this.placePosition.yCoord, this.placePosition.zCoord);
                        this.placeBlock = blockpos.add(this.facing.getOffset().xCoord, this.facing.getOffset().yCoord, this.facing.getOffset().zCoord);
                        if (this.placeBlock != null && this.facing != null) {
                            this.updateRotations();
                            if (this.placePosition != null && this.facing != null && this.placeBlock != null) {
                                int i = aEg.thePlayer.inventory.cIT;
                                SlotComponent slotcomponent1 = this.d(SlotComponent.class);
                                if (i == SlotComponent.bQ()) {
                                    if (!BadPacketsComponent.bad(false, true, false, false, true)
                                        && this.placeTicks > MathUtil.l(this.placeDelay.wo().intValue(), this.placeDelay.wA().intValue())
                                        && RayCastUtil.overBlock(this.facing.getEnumFacing(), this.placeBlock, true)) {
                                        Vec3 vec3 = RayCastUtil.c(RotationComponent.fk, aEg.playerController.getBlockReachDistance()).hitVec;
                                        PlayerControllerMP playercontrollermp = aEg.playerController;
                                        EntityPlayerSP entityplayersp = aEg.thePlayer;
                                        WorldClient worldclient = aEg.theWorld;
                                        SlotComponent slotcomponent3 = this.d(SlotComponent.class);
                                        if (playercontrollermp.onPlayerRightClick(
                                            entityplayersp, worldclient, SlotComponent.getItemStack(), this.placeBlock, this.facing.getEnumFacing(), vec3
                                        )) {
                                            PacketUtil.send(new m());
                                        }

                                        aEg.rightClickDelayTimer = 0;
                                        this.placeTicks = 0;
                                        slotcomponent = this.d(SlotComponent.class);
                                        if (!assertionsDisabled && SlotComponent.getItemStack() == null) {
                                            throw new AssertionError();
                                        }

                                        slotcomponent = this.d(SlotComponent.class);
                                        if (SlotComponent.getItemStack() != null) {
                                            slotcomponent = this.d(SlotComponent.class);
                                            if (SlotComponent.getItemStack().stackSize == 0) {
                                                ItemStack[] aitemstack = aEg.thePlayer.inventory.mainInventory;
                                                slotcomponent1 = this.d(SlotComponent.class);
                                                aitemstack[SlotComponent.bQ()] = null;
                                            }
                                        }
                                    } else if (Math.random() > 0.92 && aEg.rightClickDelayTimer <= 0) {
                                        SlotComponent slotcomponent2 = this.d(SlotComponent.class);
                                        PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
                                        aEg.rightClickDelayTimer = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };
    static final boolean assertionsDisabled = !Clutch.class.desiredAssertionStatus();

    public Clutch() {
    }

    @Override
    public void onEnable() {
        this.yaw = aEg.thePlayer.pl - 180.0F;
        this.pitch = 90.0F;
        this.placePosition = null;
    }

    public void updateRotations() {
        if (this.placeTicks > 0 && !RayCastUtil.a(RotationComponent.fk, this.facing.getEnumFacing(), this.placeBlock, true)) {
            this.findRotations(0);
        }

        double rotationSpeed = this.rotationSpeed.wo().doubleValue();
        double d1 = this.rotationSpeed.wA().doubleValue();
        float f = (float)MathUtil.l(rotationSpeed, d1);
        if (f != 0.0F) {
            RotationComponent.setRotations(new Vector2f(this.yaw, this.pitch), f, MovementFix.NORMAL);
        }
    }

    public void findRotations(int var1) {
        EntityPlayerSP entityplayersp = aEg.thePlayer;
        double d0 = entityplayersp.posY + entityplayersp.getEyeHeight() - this.placePosition.yCoord - 0.1 - Math.random() * 0.8;

        for (int i = -180 + var1; i <= 180; i += 45) {
            entityplayersp.setPosition(entityplayersp.posX, entityplayersp.posY - d0, entityplayersp.posZ);
            MovingObjectPosition movingobjectposition = RayCastUtil.c(new Vector2f(entityplayersp.pl + i, 0.0F), 4.5);
            entityplayersp.setPosition(entityplayersp.posX, entityplayersp.posY + d0, entityplayersp.posZ);
            if (movingobjectposition != null
                && new BlockPos(this.placeBlock).equals(movingobjectposition.getBlockPos())
                && this.facing.getEnumFacing() == movingobjectposition.sideHit) {
                Vector2f vector2f = RotationUtil.h(movingobjectposition.hitVec);
                this.yaw = vector2f.x;
                this.pitch = vector2f.y;
                return;
            }
        }

        Vector2f vector2f1 = RotationUtil.a(new Vector3d(this.placeBlock.getX(), this.placeBlock.getY(), this.placeBlock.getZ()), this.facing.getEnumFacing());
        this.yaw = vector2f1.x;
        this.pitch = vector2f1.y;
    }
}
