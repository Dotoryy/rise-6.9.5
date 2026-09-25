package com.alan.clients.module.impl.movement;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

@ModuleInfo(aliases = "module.movement.autostuck.name", description = "module.movement.autostuck.description", category = Category.MOVEMENT)
public final class AutoStuck extends Module {
    private final NumberValue pulseTicks = new NumberValue("Pulse Ticks", this, 1.0, 1.0, 10.0, 1.0);
    private final BooleanValue usePearl = new BooleanValue("Use Pearl", this, true);
    private final BooleanValue requireVoid = new BooleanValue("Require Void", this, true);
    private final BooleanValue lockMovement = new BooleanValue("Lock Movement", this, true);
    private final NumberValue minFallDistance = new NumberValue("Min Fall Distance", this, 5.0, 0.0, 50.0, 0.5);
    private final NumberValue minVoidDepth = new NumberValue("Min Void Depth", this, 5.0, 1.0, 200.0, 1.0);
    private final NumberValue searchRadius = new NumberValue("Search Radius", this, 10.0, 2.0, 40.0, 1.0);
    private ExecutorService executor;
    private Future<?> searchFuture;
    private boolean falling;
    private boolean searching;
    private boolean hasTarget;
    private int searchAttempts;
    private static final int MAX_ATTEMPTS = 3;
    private Vector2f targetRotations;
    private int rotationTicks;
    private int pearlSlot = -1;
    private int slotDelay = 0;
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> {
        this.cancelSearch();
        this.disableStuck();
        this.falling = false;
        this.hasTarget = false;
        this.searching = false;
        this.searchAttempts = 0;
        this.rotationTicks = 0;
        this.targetRotations = null;
        this.pearlSlot = -1;
        this.slotDelay = 0;
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            boolean flag = this.findPearlSlot() != -1;
            if (this.requireVoid.wo() ? this.isOverVoid() : this.isFalling()) {
                if (!this.falling) {
                    this.falling = true;
                    this.searchAttempts = 0;
                }

                if (aEg.thePlayer.ticksExisted % this.pulseTicks.wo().intValue() != 0) {
                    this.enableStuck();
                } else {
                    this.disableStuck();
                }

                if (this.usePearl.wo() && flag) {
                    this.startSearch();
                }
            } else if (this.falling) {
                this.falling = false;
                this.hasTarget = false;
                this.rotationTicks = 0;
                this.cancelSearch();
                this.disableStuck();
                this.pearlSlot = -1;
                this.slotDelay = 0;
            }

            if (this.usePearl.wo() && !flag) {
                this.hasTarget = false;
                this.rotationTicks = 0;
                this.pearlSlot = -1;
                this.slotDelay = 0;
            }

            if (this.falling && this.hasTarget && this.targetRotations != null) {
                RotationComponent.setRotations(this.targetRotations, 10.0, MovementFix.OFF);
                if (this.rotationTicks++ >= 1) {
                    this.rotationTicks = 0;
                    this.hasTarget = false;
                    this.preparePearl();
                }
            }

            if (this.pearlSlot != -1) {
                if (this.slotDelay > 0) {
                    this.slotDelay--;
                } else {
                    this.throwPearl(this.pearlSlot);
                    this.pearlSlot = -1;
                }
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var1 -> {
        if (this.lockMovement.wo() && this.falling) {
            var1.setForward(0.0F);
            var1.setStrafe(0.0F);
            var1.setJump(false);
            var1.setSneak(false);
        }
    };

    public AutoStuck() {
    }

    @Override
    public void onEnable() {
        this.falling = false;
        this.searching = false;
        this.hasTarget = false;
        this.searchAttempts = 0;
        this.rotationTicks = 0;
        this.targetRotations = null;
        this.pearlSlot = -1;
        this.slotDelay = 0;
        if (this.executor == null || this.executor.isShutdown()) {
            this.executor = Executors.newSingleThreadExecutor();
        }
    }

    @Override
    public void onDisable() {
        this.cancelSearch();
        this.disableStuck();
        this.falling = false;
        this.hasTarget = false;
        this.searching = false;
        this.searchAttempts = 0;
        this.rotationTicks = 0;
        this.targetRotations = null;
        this.pearlSlot = -1;
        this.slotDelay = 0;
        if (this.executor != null) {
            this.executor.shutdownNow();
        }
    }

    private void cancelSearch() {
        if (this.searchFuture != null && !this.searchFuture.isDone()) {
            this.searchFuture.cancel(true);
        }

        this.searchFuture = null;
    }

    private void enableStuck() {
        Stuck stuck = this.e(Stuck.class);
        if (stuck != null && !stuck.isEnabled()) {
            stuck.setEnabled(true);
        }
    }

    private void disableStuck() {
        Stuck stuck = this.e(Stuck.class);
        if (stuck != null && stuck.isEnabled()) {
            stuck.setEnabled(false);
        }
    }

    private boolean isFalling() {
        return aEg.thePlayer != null && !aEg.thePlayer.onGround && aEg.thePlayer.fallDistance >= this.minFallDistance.wo().floatValue();
    }

    private boolean isOverVoid() {
        if (!this.isFalling()) {
            return false;
        }

        BlockPos blockpos = new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
        BlockPos blockpos1 = blockpos.down();
        if (!aEg.theWorld.isAirBlock(blockpos1)) {
            return false;
        }

        int i = 0;
        int j = this.minVoidDepth.wo().intValue();

        for (int kx = blockpos1.getY(); kx > 0 && i < j + 256; kx--) {
            BlockPos blockpos2 = new BlockPos(blockpos.getX(), kx, blockpos.getZ());
            if (!aEg.theWorld.isAirBlock(blockpos2)) {
                break;
            }

            if (++i >= j) {
                return true;
            }
        }

        return i >= j;
    }

    private int findPearlSlot() {
        if (aEg.thePlayer == null) {
            return -1;
        }

        for (int i = 0; i < aEg.thePlayer.inventory.mainInventory.length; i++) {
            ItemStack itemstack = aEg.thePlayer.inventory.mainInventory[i];
            if (itemstack != null && itemstack.getItem() == Items.ender_pearl) {
                return i;
            }
        }

        return -1;
    }

    private void startSearch() {
        if (!this.searching && this.searchAttempts < 3) {
            this.searching = true;
            this.searchAttempts++;
            Vec3 positionVector = aEg.thePlayer.getPositionVector();
            this.searchFuture = this.executor.submit(() -> {
                try {
                    Optional optional = this.findLandingSpot(positionVector, (int)this.searchRadius.wo().doubleValue());
                    if (optional.isPresent()) {
                        this.targetRotations = this.getThrowRotation((Vec3)optional.get());
                        this.hasTarget = this.targetRotations != null;
                    }
                } catch (Throwable throwable) {
                } finally {
                    this.searching = false;
                }
            });
        }
    }

    private Optional<Vec3> findLandingSpot(Vec3 vec, int var2) {
        if (aEg.theWorld == null) {
            return Optional.empty();
        }

        int i = (int)Math.floor(vec.xCoord);
        int j = (int)Math.floor(vec.yCoord);
        int floorZCoord = (int)Math.floor(vec.zCoord);
        Vec3 vec3 = null;
        double d0 = Double.MAX_VALUE;

        for (int l = -var2; l <= var2; l++) {
            for (int i1 = -var2; i1 <= var2; i1++) {
                for (int j1 = -5; j1 <= 5; j1++) {
                    BlockPos blockpos = new BlockPos(i + l, j + j1, floorZCoord + i1);
                    if (!aEg.theWorld.isAirBlock(blockpos) && aEg.theWorld.isAirBlock(blockpos.up()) && aEg.theWorld.isAirBlock(blockpos.up(2))) {
                        Vec3 vec31 = new Vec3(blockpos.getX() + 0.5, blockpos.getY() + 1.0, blockpos.getZ() + 0.5);
                        double d1 = vec31.squareDistanceTo(vec);
                        if (d1 < d0) {
                            d0 = d1;
                            vec3 = vec31;
                        }
                    }
                }
            }
        }

        return vec3 == null ? Optional.empty() : Optional.of(vec3);
    }

    private void preparePearl() {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            if (this.usePearl.wo()) {
                int i = this.findPearlSlot();
                if (i != -1) {
                    if (i <= 8) {
                        int j = i;
                        this.pearlSlot = j;
                        this.slotDelay = 0;
                    } else {
                        int kx = this.findEmptyHotbarSlot();
                        if (kx == -1) {
                            kx = 0;
                        }

                        int l = aEg.thePlayer.inventoryContainer.windowId;
                        int i1 = i;
                        aEg.playerController.windowClick(l, i1, kx, 2, aEg.thePlayer);
                        this.pearlSlot = kx;
                        this.slotDelay = 1;
                    }
                }
            }
        }
    }

    private int findEmptyHotbarSlot() {
        for (int i = 0; i < 9; i++) {
            if (aEg.thePlayer.inventory.mainInventory[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private void throwPearl(int var1) {
        if (aEg.thePlayer != null && aEg.theWorld != null) {
            int i = aEg.thePlayer.inventory.currentItem;
            aEg.thePlayer.inventory.currentItem = var1;
            aEg.playerController.syncCurrentPlayItem();
            ItemStack itemstack = aEg.thePlayer.getHeldItem();
            if (itemstack != null && itemstack.getItem() == Items.ender_pearl) {
                aEg.playerController.sendUseItem(aEg.thePlayer, aEg.theWorld, itemstack);
            }

            aEg.thePlayer.inventory.currentItem = i;
            aEg.playerController.syncCurrentPlayItem();
        }
    }

    private Vector2f getThrowRotation(Vec3 vec) {
        if (aEg.thePlayer == null) {
            return null;
        }

        Vec3 vec3 = new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY + aEg.thePlayer.getEyeHeight(), aEg.thePlayer.posZ);
        double d0 = vec.xCoord - vec3.xCoord;
        double d1 = vec.zCoord - vec3.zCoord;
        double d2 = vec.yCoord - vec3.yCoord;
        double d3 = Math.sqrt(d0 * d0 + d1 * d1);
        if (d3 < 0.001) {
            return null;
        }

        float f = (float)(Math.toDegrees(Math.atan2(d1, d0)) - 90.0);
        double d4 = 5.0625 - 0.03 * (0.03 * d3 * d3 + 2.0 * d2 * 2.25);
        double d6;
        if (d4 > 0.0) {
            double d5 = Math.sqrt(d4);
            d6 = -Math.toDegrees(Math.atan((2.25 - d5) / (0.03 * d3)));
        } else {
            d6 = -45.0;
        }

        double d7 = d6 - 4.0;
        double d8 = Math.max(-89.0, Math.min(-1.0, d7));
        return new Vector2f(f, (float)d8);
    }
}
