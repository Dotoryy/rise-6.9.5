package com.alan.clients.module.impl.movement;

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
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.SlotUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.component.impl.render.NotificationComponent;
import com.alan.clients.module.impl.movement.AutoMLGSwitchMap;
import com.alan.clients.module.impl.movement.ScaffoldState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.m;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@ModuleInfo(aliases = "module.movement.automlg.name", description = "module.movement.automlg.description", category = Category.MOVEMENT)
public class AutoMLG extends Module {
    private final NumberValue fallDistance = new NumberValue("Fall Distance", this, 4.0, 3.0, 20.0, 0.5);
    private final NumberValue pickupDelay = new NumberValue("Pickup Delay", this, 4, 1, 20, 1);
    private final BooleanValue autoPickup = new BooleanValue("Auto Pickup", this, true);
    private final BooleanValue autoDisable = new BooleanValue("Auto Disable", this, false);
    private ScaffoldState state = ScaffoldState.IDLE;
    private int bucketSlot = -1;
    private int stateTicks;
    private BlockPos waterPos;
    @EventLink(value = 3)
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        if (aEg.thePlayer != null && aEg.theWorld != null && !aEg.thePlayer.isDead) {
            if (this.state != ScaffoldState.IDLE && this.bucketSlot != -1) {
                SlotComponent.b(this.bucketSlot, true);
            }

            switch (this.state) {
                case IDLE:
                    this.handleIdle();
                    break;
                case ROTATING:
                    this.handleRotating();
                    break;
                case PLACED:
                    this.handlePlaced();
                    break;
                case PICKUP:
                    this.handlePickup();
            }
        }
    };

    public AutoMLG() {
    }

    @Override
    public void onEnable() {
        this.reset();
    }

    @Override
    public void onDisable() {
        this.state = ScaffoldState.IDLE;
        this.bucketSlot = -1;
        this.waterPos = null;
    }

    private void reset() {
        this.state = ScaffoldState.IDLE;
        this.bucketSlot = -1;
        this.stateTicks = 0;
        this.waterPos = null;
    }

    private boolean collidesAtOffset(double var1) {
        AxisAlignedBB axisalignedbb = aEg.thePlayer.getEntityBoundingBox().offset(0.0, var1, 0.0);
        return !aEg.theWorld.getCollidingBoundingBoxes(aEg.thePlayer, axisalignedbb).isEmpty();
    }

    private Vec3 getHitVec(BlockPos pos, EnumFacing facing, MovingObjectPosition hit) {
        Vec3 vec3 = new Vec3(pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random());
        switch (facing) {
            case DOWN:
                vec3.yCoord = pos.getY();
                break;
            case UP:
                vec3.yCoord = pos.getY() + 1;
                break;
            case NORTH:
                vec3.zCoord = pos.getZ();
                break;
            case SOUTH:
                vec3.zCoord = pos.getZ() + 1;
                break;
            case WEST:
                vec3.xCoord = pos.getX();
                break;
            case EAST:
                vec3.xCoord = pos.getX() + 1;
        }

        if (hit != null && hit.hitVec != null && hit.getBlockPos() != null && hit.getBlockPos().equals(pos) && hit.sideHit == facing) {
            vec3 = hit.hitVec;
        }

        return vec3;
    }

    private BlockPos getPlacedPos(MovingObjectPosition hit) {
        if (hit != null && hit.getBlockPos() != null && hit.sideHit != null) {
            BlockPos blockpos = hit.getBlockPos();
            BlockPos blockpos1 = blockpos.offset(hit.sideHit);
            Block block = aEg.theWorld.getBlockState(blockpos1).getBlock();
            return !block.isReplaceable(aEg.theWorld, blockpos1) && !block.getMaterial().isLiquid() ? blockpos : blockpos1;
        }
        return null;
    }

    private boolean e(BlockPos pos) {
        if (pos == null) {
            return false;
        }

        IBlockState iblockstate = aEg.theWorld.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return (block == Blocks.water || block == Blocks.flowing_water) && block instanceof BlockLiquid && iblockstate.getValue(BlockLiquid.LEVEL) == 0;
    }

    private BlockPos findNearestWater() {
        BlockPos blockpos = new BlockPos(aEg.thePlayer);
        BlockPos blockpos1 = null;
        double d0 = Double.MAX_VALUE;

        for (int i = -3; i <= 3; i++) {
            for (int j = -2; j <= 2; j++) {
                for (int k = -3; k <= 3; k++) {
                    BlockPos blockpos2 = blockpos.add(i, j, k);
                    if (this.e(blockpos2)) {
                        double d1 = aEg.thePlayer.getDistanceSq(blockpos2.getX() + 0.5, blockpos2.getY() + 0.5, blockpos2.getZ() + 0.5);
                        if (d1 < d0) {
                            d0 = d1;
                            blockpos1 = blockpos2;
                        }
                    }
                }
            }
        }

        return blockpos1;
    }

    private BlockPos getWaterPos() {
        if (this.e(this.waterPos)) {
            return this.waterPos;
        }

        BlockPos blockpos = this.findNearestWater();
        if (blockpos != null) {
            this.waterPos = blockpos;
        }

        return this.waterPos;
    }

    private boolean pickupWater(ItemStack stack, BlockPos pos) {
        if (stack != null && stack.getItem() == Items.bucket && pos != null) {
            Vector2f vector2f = RotationUtil.h(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
            float f = aEg.thePlayer.pl;
            float rotationPitch = aEg.thePlayer.rotationPitch;
            aEg.thePlayer.pl = vector2f.x;
            aEg.thePlayer.rotationPitch = vector2f.y;
            boolean flag = aEg.playerController.sendUseItem(aEg.thePlayer, aEg.theWorld, stack);
            aEg.thePlayer.pl = f;
            aEg.thePlayer.rotationPitch = rotationPitch;
            if (flag) {
                aEg.rightClickDelayTimer = 0;
            }

            return flag;
        }
        return false;
    }

    private void handleIdle() {
        if (!(aEg.thePlayer.fallDistance < this.fallDistance.wo().floatValue())
            && !(aEg.thePlayer.motionY >= 0.0)
            && !aEg.thePlayer.isInWater()
            && !aEg.thePlayer.isInLava()
            && !aEg.thePlayer.onGround
            && !aEg.thePlayer.capabilities.isFlying) {
            int i = SlotUtil.findItem(Items.water_bucket);
            if (i != -1) {
                this.bucketSlot = i;
                SlotComponent.b(this.bucketSlot, true);
                this.state = ScaffoldState.ROTATING;
                this.stateTicks = 0;
            }
        }
    }

    private void handleRotating() {
        this.stateTicks++;
        if (aEg.thePlayer.onGround || aEg.thePlayer.isInWater() || aEg.thePlayer.isDead || this.stateTicks > 100) {
            NotificationComponent.e("Auto MLG", "Failed - couldn't place water in time");
            this.reset();
        } else if (aEg.thePlayer.motionY > 0.5) {
            this.reset();
        } else {
            ItemStack itemstack = SlotComponent.getItemStack();
            if (itemstack != null && itemstack.getItem() == Items.water_bucket) {
                RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 90.0F), 10.0, MovementFix.NORMAL);
                double d0 = Math.max(aEg.thePlayer.motionY * 2.0, -4.5);
                if (this.collidesAtOffset(d0)) {
                    MovingObjectPosition movingobjectposition = RayCastUtil.c(RotationComponent.fk, aEg.playerController.getBlockReachDistance());
                    if (movingobjectposition != null
                        && movingobjectposition.typeOfHit == MovingObjectType.BLOCK
                        && movingobjectposition.sideHit != null
                        && movingobjectposition.getBlockPos() != null
                        && !(movingobjectposition.getBlockPos().getY() >= aEg.thePlayer.posY)) {
                        this.getHitVec(movingobjectposition.getBlockPos(), movingobjectposition.sideHit, movingobjectposition);
                        ItemStack itemstack1 = SlotComponent.getItemStack();
                        if (itemstack1 != null && itemstack1.getItem() == Items.water_bucket) {
                            PacketUtil.send(new m());
                            PacketUtil.send(new C08PacketPlayerBlockPlacement(itemstack1));
                            boolean flag1 = true;
                            if (flag1) {
                                this.waterPos = this.getPlacedPos(movingobjectposition);
                                aEg.rightClickDelayTimer = 0;
                                this.state = ScaffoldState.PLACED;
                                this.stateTicks = 0;
                            }
                        } else {
                            NotificationComponent.e("Auto MLG", "Failed - no water bucket in hand");
                            this.reset();
                        }
                    }
                }
            } else {
                NotificationComponent.e("Auto MLG", "Failed - no water bucket in hand");
                this.reset();
            }
        }
    }

    private void handlePlaced() {
        this.stateTicks++;
        RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 90.0F), 10.0, MovementFix.NORMAL);
        if (!aEg.thePlayer.onGround && !aEg.thePlayer.isInWater()) {
            if (this.stateTicks > 60) {
                NotificationComponent.e("Auto MLG", "Failed - landing timed out");
                this.reset();
            }
        } else {
            if (this.autoPickup.wo()) {
                this.state = ScaffoldState.PICKUP;
                this.stateTicks = 0;
            } else {
                NotificationComponent.e("Auto MLG", "Water placed successfully!");
                this.reset();
                if (this.autoDisable.wo()) {
                    this.toggle();
                }
            }
        }
    }

    private void handlePickup() {
        this.stateTicks++;
        ItemStack itemstack = SlotComponent.getItemStack();
        if (itemstack != null && itemstack.getItem() == Items.water_bucket) {
            NotificationComponent.e("Auto MLG", "MLG successful!");
            this.reset();
            if (this.autoDisable.wo()) {
                this.toggle();
            }
        } else {
            BlockPos blockpos = this.getWaterPos();
            if (blockpos != null) {
                RotationComponent.setRotations(RotationUtil.h(new Vec3(blockpos.getX() + 0.5, blockpos.getY() + 0.5, blockpos.getZ() + 0.5)), 30.0, MovementFix.NORMAL);
            } else {
                RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, 90.0F), 10.0, MovementFix.NORMAL);
            }

            if (this.stateTicks >= this.pickupDelay.wo().intValue()) {
                ItemStack itemstack1 = SlotComponent.getItemStack();
                if (itemstack1 != null && itemstack1.getItem() == Items.bucket && blockpos != null && this.pickupWater(itemstack1, blockpos)) {
                    PacketUtil.send(new m());
                    NotificationComponent.e("Auto MLG", "MLG successful!");
                    this.reset();
                    if (this.autoDisable.wo()) {
                        this.toggle();
                    }
                }
            }

            if (this.stateTicks > this.pickupDelay.wo().intValue() + 40) {
                NotificationComponent.e("Auto MLG", "Pickup timed out, resetting");
                this.reset();
            }
        }
    }
}
