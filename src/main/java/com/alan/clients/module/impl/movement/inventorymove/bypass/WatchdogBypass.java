package com.alan.clients.module.impl.movement.inventorymove.bypass;

import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.impl.movement.InventoryMove;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.MoveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.q;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WatchdogBypass
extends Mode<InventoryMove> {
    private boolean speedSuppressed = false;
    private boolean inputDelayPassed = false;
    private long inputBlockStart = 0L;
    private float forward;
    private float strafe;
    public static boolean inventoryClicking;
    public static boolean chestOpenConfirmed;
    int dm;
    int pressedKeyCount;
    private final KeyBinding[] movementKeys;
    public final BooleanValue predictionMode;
    public final NumberValue ticks;
    public final BooleanValue measureChestOpen;
    private int JW;
    private int openSentTick;
    private int openLatencyTicks;
    private int chestOpenTick;
    private BlockPos lastChestPos;
    private BlockPos pendingChestPos;
    private String lastOpenSource;
    private String pendingOpenSource;
    private boolean openPending;
    private boolean awaitingChestGui;
    @EventLink
    private final Listener<PreMotionEvent> onPreMotion;
    @EventLink
    private final Listener<PacketSendEvent> onPacketSend;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionMedium;
    @EventLink(value=3)
    Listener<MoveInputEvent> onMoveInputHigh;
    @EventLink(value=1)
    Listener<SprintEvent> onSprint;
    @EventLink(value=1)
    Listener<PreUpdateEvent> onPreUpdate;
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput;
    @EventLink
    public final Listener<MoveEvent> onMove;

    public WatchdogBypass(String string, InventoryMove inventoryMove) {
        super(string, inventoryMove);
        this.movementKeys = new KeyBinding[]{WatchdogBypass.aEg.gameSettings.keyBindForward, WatchdogBypass.aEg.gameSettings.keyBindBack, WatchdogBypass.aEg.gameSettings.keyBindRight, WatchdogBypass.aEg.gameSettings.keyBindLeft, WatchdogBypass.aEg.gameSettings.keyBindJump};
        this.predictionMode = new BooleanValue("Prediction Mode", (Mode<?>)this, (Boolean)false);
        this.ticks = new NumberValue("Ticks", this, (Number)1, (Number)1, (Number)20, (Number)1);
        this.measureChestOpen = new BooleanValue("Measure Chest Open", (Mode<?>)this, (Boolean)true);
        this.openSentTick = -1;
        this.openLatencyTicks = -1;
        this.chestOpenTick = -1;
        this.lastOpenSource = "unknown";
        this.pendingOpenSource = "unknown";
        this.onPreMotion = preMotionEvent -> {
            if (WatchdogBypass.aEg.currentScreen == null || WatchdogBypass.aEg.currentScreen instanceof GuiChat || WatchdogBypass.aEg.currentScreen == this.getStandardClickGUI()) {
                return;
            }
            boolean unused0 = WatchdogBypass.aEg.currentScreen instanceof GuiChest;
            for (KeyBinding keyBinding : this.movementKeys) {
                keyBinding.setPressed(GameSettings.isKeyDown(keyBinding));
            }
            if (chestOpenConfirmed) {
                int unused1 = WatchdogBypass.aEg.thePlayer.ticksExisted % 2;
            }
        };
        this.onPacketSend = packetSendEvent -> {
            Packet<?> packet = packetSendEvent.dq();
            if (!packetSendEvent.isCancelled() && WatchdogBypass.aEg.thePlayer != null && WatchdogBypass.aEg.theWorld != null && ((Boolean)this.measureChestOpen.wo()).booleanValue() && packet instanceof C08PacketPlayerBlockPlacement) {
                C08PacketPlayerBlockPlacement c08PacketPlayerBlockPlacement = (C08PacketPlayerBlockPlacement)packet;
                BlockPos blockPos = c08PacketPlayerBlockPlacement.getPosition();
                if (!(WatchdogBypass.aEg.currentScreen instanceof GuiChest) && this.h(blockPos)) {
                    this.pendingChestPos = blockPos;
                    this.pendingOpenSource = "C08 at " + String.valueOf(blockPos);
                    this.openPending = true;
                }
            } else if (!packetSendEvent.isCancelled() && WatchdogBypass.aEg.thePlayer != null && WatchdogBypass.aEg.theWorld != null && ((Boolean)this.measureChestOpen.wo()).booleanValue() && packet instanceof C02PacketUseEntity) {
                C02PacketUseEntity c02PacketUseEntity = (C02PacketUseEntity)packet;
                Entity entity = c02PacketUseEntity.getEntityFromWorld((World)WatchdogBypass.aEg.theWorld);
                if (!(WatchdogBypass.aEg.currentScreen instanceof GuiChest) && c02PacketUseEntity.getAction() != C02PacketUseEntity.Action.ATTACK && this.isNpcEntity(entity)) {
                    this.pendingChestPos = null;
                    this.pendingOpenSource = "C02 at " + entity.getName() + " (" + entity.getEntityId() + ")";
                    this.openPending = true;
                }
            }
            if (packet instanceof C0EPacketClickWindow) {
                C0EPacketClickWindow c0EPacketClickWindow = (C0EPacketClickWindow)packet;
                if (WatchdogBypass.aEg.currentScreen instanceof GuiInventory && c0EPacketClickWindow.getMode() < 1 && c0EPacketClickWindow.getClickedItem() != null) {
                    inventoryClicking = true;
                }
            }
        };
        this.onPreMotionMedium = preMotionEvent -> {
            GuiScreen guiScreen;
            if (!(WatchdogBypass.aEg.currentScreen instanceof GuiInventory)) {
                inventoryClicking = false;
            }
            if ((guiScreen = WatchdogBypass.aEg.currentScreen) instanceof GuiInventory) {
                GuiInventory guiInventory = (GuiInventory)guiScreen;
                if (!inventoryClicking && !BadPacketsComponent.bad(false, false, false, false, true)) {
                    WatchdogBypass.aEg.thePlayer.sendQueue.u((Packet)new q(guiInventory.inventorySlots.windowId));
                }
            }
            if ((WatchdogBypass.aEg.currentScreen instanceof GuiChest || inventoryClicking) && !((Boolean)this.predictionMode.wo()).booleanValue()) {
                if (this.e(Speed.class).isEnabled() && !this.speedSuppressed) {
                    WatchdogBypass.aEg.thePlayer.motionZ *= -0.1;
                    WatchdogBypass.aEg.thePlayer.motionX *= -0.1;
                    this.speedSuppressed = true;
                }
                if (WatchdogBypass.aEg.thePlayer.cqL < 10 && !(Math.abs(WatchdogBypass.aEg.thePlayer.posY - (double)Math.round(WatchdogBypass.aEg.thePlayer.posY)) > 0.03) && !(WatchdogBypass.aEg.currentScreen instanceof GuiChest)) {
                    MoveUtil.strafe(0.0365);
                } else if (!WatchdogBypass.aEg.thePlayer.onGround) {
                    MoveUtil.stop();
                } else if (WatchdogBypass.aEg.thePlayer.isPotionActive(Potion.moveSpeed) && 1 + WatchdogBypass.aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() > 1) {
                    MoveUtil.strafe(0.0185 * (double)(1 + WatchdogBypass.aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()));
                } else if (WatchdogBypass.aEg.thePlayer.isPotionActive(Potion.moveSpeed) && 1 + WatchdogBypass.aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() == 1) {
                    MoveUtil.strafe(0.0635 * (double)(1 + WatchdogBypass.aEg.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier()));
                } else {
                    MoveUtil.strafe(0.09);
                }
                if (WatchdogBypass.aEg.thePlayer.isJumping && WatchdogBypass.aEg.thePlayer.onGround) {
                    MoveUtil.stop();
                } else if (WatchdogBypass.aEg.thePlayer.isJumping) {
                    MoveUtil.stop();
                }
                MoveUtil.preventDiagonalSpeed();
            } else if (this.speedSuppressed) {
                this.e(Speed.class).setEnabled(true);
                this.speedSuppressed = false;
            }
            if ((WatchdogBypass.aEg.currentScreen instanceof GuiChest || inventoryClicking) && ((Boolean)this.predictionMode.wo()).booleanValue() && !WatchdogBypass.aEg.thePlayer.isPotionActive(Potion.moveSpeed)) {
                KeyBinding[] keyBindingArray;
                preMotionEvent.setSprinting(false);
                WatchdogBypass.aEg.gameSettings.cgG.setPressed(false);
                WatchdogBypass.aEg.gameSettings.keyBindJump.setPressed(false);
                KeyBinding[] keyBindingArray2 = keyBindingArray = new KeyBinding[]{WatchdogBypass.aEg.gameSettings.keyBindForward, WatchdogBypass.aEg.gameSettings.keyBindRight, WatchdogBypass.aEg.gameSettings.keyBindBack, WatchdogBypass.aEg.gameSettings.keyBindLeft};
                int n2 = keyBindingArray2.length;
                int n3 = 0;
                while (n3 < n2) {
                    if (keyBindingArray2[n3].isKeyDown()) {
                        ++this.pressedKeyCount;
                    }
                    ++n3;
                }
                return;
            }
            if (!(WatchdogBypass.aEg.currentScreen instanceof GuiChest)) {
                if (!inventoryClicking) return;
            }
            if ((Boolean)this.predictionMode.wo() == false) return;
            preMotionEvent.setSprinting(false);
        };
        this.onMoveInputHigh = moveInputEvent -> {
            this.forward = moveInputEvent.getForward();
            this.strafe = moveInputEvent.getStrafe();
        };
        this.onSprint = en2 -> {
            if (WatchdogBypass.aEg.currentScreen instanceof GuiChest || inventoryClicking) {
                WatchdogBypass.aEg.thePlayer.setSprinting(false);
            }
        };
        this.onPreUpdate = preUpdateEvent -> {
            if (WatchdogBypass.aEg.thePlayer == null || WatchdogBypass.aEg.theWorld == null || WatchdogBypass.aEg.thePlayer.ticksExisted < 50) {
                return;
            }
            this.beginOpenMeasurement();
            this.finishOpenMeasurement();
            this.updateChestOpenState();
            KeyBinding[] keyBindingArray = new KeyBinding[]{WatchdogBypass.aEg.gameSettings.keyBindForward, WatchdogBypass.aEg.gameSettings.keyBindRight, WatchdogBypass.aEg.gameSettings.keyBindBack, WatchdogBypass.aEg.gameSettings.keyBindLeft};
            int n2 = 0;
            KeyBinding[] keyBindingArray2 = keyBindingArray;
            int n3 = keyBindingArray2.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                if (!keyBindingArray2[i2].isKeyDown()) continue;
                ++n2;
            }
            if ((WatchdogBypass.aEg.currentScreen instanceof GuiChest || inventoryClicking) && n2 > 1) {
                RotationComponent.setRotations(new Vector2f((float)Math.toDegrees(MoveUtil.g(this.forward, this.strafe)), WatchdogBypass.aEg.thePlayer.rotationPitch), 10.0, MovementFix.NORMAL);
            }
        };
        this.onMoveInput = moveInputEvent -> {
            KeyBinding[] keyBindingArray = new KeyBinding[]{WatchdogBypass.aEg.gameSettings.keyBindForward, WatchdogBypass.aEg.gameSettings.keyBindRight, WatchdogBypass.aEg.gameSettings.keyBindBack, WatchdogBypass.aEg.gameSettings.keyBindLeft};
            int n2 = 0;
            KeyBinding[] keyBindingArray2 = keyBindingArray;
            int n3 = keyBindingArray2.length;
            for (int i2 = 0; i2 < n3; ++i2) {
                if (!keyBindingArray2[i2].isKeyDown()) continue;
                ++n2;
            }
            if (chestOpenConfirmed && WatchdogBypass.aEg.thePlayer.ticksExisted % 5 != 0 && WatchdogBypass.aEg.currentScreen instanceof GuiChest) {
                moveInputEvent.setStrafe(0.0f);
                moveInputEvent.setForward(0.0f);
            }
            if (!(WatchdogBypass.aEg.currentScreen instanceof GuiChest)) {
                chestOpenConfirmed = false;
            }
            if ((WatchdogBypass.aEg.currentScreen instanceof GuiChest || inventoryClicking) && ((Boolean)this.predictionMode.wo()).booleanValue() && (WatchdogBypass.aEg.thePlayer.isPotionActive(Potion.moveSpeed) || !WatchdogBypass.aEg.thePlayer.onGround)) {
                moveInputEvent.setStrafe(0.0f);
                moveInputEvent.setForward(0.0f);
            } else if (inventoryClicking && !this.inputDelayPassed) {
                moveInputEvent.setStrafe(0.0f);
                moveInputEvent.setForward(0.0f);
                if (this.inputBlockStart == 0L) {
                    this.inputBlockStart = System.currentTimeMillis();
                }
            } else {
                this.inputBlockStart = 0L;
            }
            if (this.inputBlockStart != 0L && System.currentTimeMillis() - this.inputBlockStart >= 60L) {
                this.inputDelayPassed = true;
                this.inputBlockStart = 0L;
            }
            if (!(WatchdogBypass.aEg.currentScreen instanceof GuiChest) && !inventoryClicking) {
                this.inputDelayPassed = false;
            }
        };
        this.onMove = moveEvent -> {
            if ((WatchdogBypass.aEg.currentScreen instanceof GuiChest || inventoryClicking) && !((Boolean)this.predictionMode.wo()).booleanValue() && !WatchdogBypass.aEg.thePlayer.onGround) {
                moveEvent.setPosZ(0.0);
                moveEvent.setPosX(0.0);
            }
        };
    }

    private void finishOpenMeasurement() {
        if (!this.awaitingChestGui) {
            return;
        }
        if (WatchdogBypass.aEg.currentScreen instanceof GuiChest) {
            int n2;
            this.openLatencyTicks = n2 = WatchdogBypass.aEg.thePlayer.ticksExisted - this.openSentTick;
            if (this.chestOpenTick == -1) {
                this.chestOpenTick = WatchdogBypass.aEg.thePlayer.ticksExisted;
            }
            this.awaitingChestGui = false;
            return;
        }
        if (WatchdogBypass.aEg.thePlayer.ticksExisted - this.openSentTick > 40) {
            this.awaitingChestGui = false;
        }
    }

    private void beginOpenMeasurement() {
        if (!this.openPending) {
            return;
        }
        this.openSentTick = WatchdogBypass.aEg.thePlayer.ticksExisted;
        this.lastChestPos = this.pendingChestPos;
        this.lastOpenSource = this.pendingOpenSource;
        this.openPending = false;
        this.awaitingChestGui = true;
    }

    private void updateChestOpenState() {
        if (!(WatchdogBypass.aEg.currentScreen instanceof GuiChest)) {
            this.chestOpenTick = -1;
            chestOpenConfirmed = false;
            return;
        }
        if (this.chestOpenTick == -1) {
            this.chestOpenTick = WatchdogBypass.aEg.thePlayer.ticksExisted;
        }
        if (!chestOpenConfirmed && this.openLatencyTicks >= 0 && WatchdogBypass.aEg.thePlayer.ticksExisted - this.chestOpenTick >= this.openLatencyTicks - ((Number)this.ticks.wo()).intValue()) {
            chestOpenConfirmed = true;
        }
    }

    private boolean h(BlockPos blockPos) {
        if (blockPos == null) return false;
        if (WatchdogBypass.aEg.theWorld == null) return false;
        if (blockPos.equals((Object)new BlockPos(-1, -1, -1))) {
            return false;
        }
        Block block = WatchdogBypass.aEg.theWorld.getBlockState(blockPos).getBlock();
        if (block == Blocks.chest) return true;
        if (block == Blocks.trapped_chest) return true;
        if (block != Blocks.ender_chest) return false;
        return true;
    }

    private boolean isNpcEntity(Entity entity) {
        if (entity == null) return false;
        if (entity == WatchdogBypass.aEg.thePlayer) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            if (aEg.getNetHandler().getPlayerInfo(((EntityPlayer)entity).getUniqueID()) != null) return false;
            return true;
        }
        if (entity.adr) return false;
        if (!this.isStationary(entity)) return false;
        return true;
    }

    private boolean isStationary(Entity entity) {
        if (!(Math.abs(entity.posX - entity.lastTickPosX) < 0.03)) return false;
        if (!(Math.abs(entity.posY - entity.lastTickPosY) < 0.03)) return false;
        if (!(Math.abs(entity.posZ - entity.lastTickPosZ) < 0.03)) return false;
        if (!(Math.abs(entity.motionX) < 0.03)) return false;
        if (!(Math.abs(entity.motionY) < 0.03)) return false;
        if (!(Math.abs(entity.motionZ) < 0.03)) return false;
        return true;
    }
}
