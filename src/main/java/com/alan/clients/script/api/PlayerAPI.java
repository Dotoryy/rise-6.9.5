package com.alan.clients.script.api;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.script.api.wrapper.impl.ScriptEntity;
import com.alan.clients.script.api.wrapper.impl.ScriptEntityLiving;
import com.alan.clients.script.api.wrapper.impl.ScriptInventory;
import com.alan.clients.script.api.wrapper.impl.ScriptItemStack;
import com.alan.clients.script.api.wrapper.impl.vector.ScriptVector2f;
import com.alan.clients.script.api.wrapper.impl.vector.ScriptVector3d;
import com.alan.clients.ui.click.standard.RiseClickGUI;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.player.DamageUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.ItemDamageComponent;
import com.alan.clients.component.impl.player.PacketlessDamageComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class PlayerAPI extends ScriptEntityLiving {
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> {
        if (this.wrapped == null || this.wrapped != MC.thePlayer) {
            this.wrapped = this.wrappedLiving = MC.thePlayer;
        }
    };

    public PlayerAPI() {
        super(MC.thePlayer);
        Client.a.e().b(this);
    }

    public String getName() {
        return MC.getSession().getUsername();
    }

    public String getPlayerID() {
        return MC.getSession().getPlayerID();
    }

    public boolean isOnGround() {
        return MC.thePlayer.onGround;
    }

    public boolean isMoving() {
        return MoveUtil.isMoving();
    }

    public void jump() {
        MC.thePlayer.jump();
    }

    public void strafe() {
        MoveUtil.strafe();
    }

    public void strafe(double var1) {
        MoveUtil.strafe(var1);
    }

    public float getForward() {
        return MC.thePlayer.moveForward;
    }

    public float getStrafe() {
        return MC.thePlayer.moveStrafing;
    }

    public double getSpeed() {
        return MoveUtil.speed();
    }

    public void stop() {
        MoveUtil.stop();
    }

    public void setPosition(double var1, double var3, double var5) {
        MC.thePlayer.setPosition(var1, var3, var5);
    }

    public void setPosition(ScriptVector3d position) {
        this.setPosition(position.getX(), position.getY(), position.getZ());
    }

    public void setMotion(double var1, double var3, double var5) {
        MC.thePlayer.motionX = var1;
        MC.thePlayer.motionY = var3;
        MC.thePlayer.motionZ = var5;
    }

    public int getUseItemProgress() {
        return MC.thePlayer.getItemInUseDuration();
    }

    @Override
    public void setMotionY(double var1) {
        MC.thePlayer.motionY = var1;
    }

    @Override
    public void setMotionX(double var1) {
        MC.thePlayer.motionX = var1;
    }

    @Override
    public void setMotionZ(double var1) {
        MC.thePlayer.motionZ = var1;
    }

    @Override
    public void setMotion(ScriptVector3d motion) {
        this.setMotion(motion.getX(), motion.getY(), motion.getZ());
    }

    public void leftClick() {
        MC.Ay();
    }

    public void rightClick() {
        MC.Az();
    }

    public void attackEntity(ScriptEntityLiving scriptEntityLiving) {
        MC.playerController.attackEntity(MC.thePlayer, MC.theWorld.getEntityByID(scriptEntityLiving.getEntityId()));
    }

    public void swingItem() {
        MC.thePlayer.swingItem();
    }

    public void message(String var1) {
        if (MC.thePlayer != null && MC.theWorld != null) {
            MC.thePlayer.sendChatMessage(var1);
        }
    }

    public void setRotation(ScriptVector2f scriptVector2f, double var2, boolean var4) {
        RotationComponent.setRotations(new Vector2f(scriptVector2f.getX(), scriptVector2f.getY()), var2, var4 ? MovementFix.NORMAL : MovementFix.OFF);
    }

    public void setRotation(float var1, float var2) {
        RotationComponent.setRotations(new Vector2f(var1, var2), 10.0, MovementFix.NORMAL);
    }

    public void setHeldItem(int var1, boolean var2) {
        SlotComponent slotcomponent = Client.a.h().b(SlotComponent.class);
        SlotComponent.setSlot(var1);
    }

    public double[] getLerpedPosition() {
        return new double[]{
            MC.thePlayer.lastTickPosX + (MC.thePlayer.posX - MC.thePlayer.lastTickPosX) * MC.timer.bWm,
            MC.thePlayer.lastTickPosY + (MC.thePlayer.posY - MC.thePlayer.lastTickPosY) * MC.timer.bWm,
            MC.thePlayer.lastTickPosZ + (MC.thePlayer.posZ - MC.thePlayer.lastTickPosZ) * MC.timer.bWm
        };
    }

    public void setSlot(int slot) {
        MC.thePlayer.inventory.currentItem = slot;
    }

    public void setHeldItem(int heldItem) {
        SlotComponent slotcomponent = Client.a.h().b(SlotComponent.class);
        SlotComponent.setSlot(heldItem);
    }

    @Override
    public ScriptItemStack getHeldItemStack() {
        SlotComponent slotcomponent = Client.a.h().b(SlotComponent.class);
        return new ScriptItemStack(SlotComponent.getItemStack());
    }

    public ScriptItemStack getClientHeldItemStack() {
        return new ScriptItemStack(MC.thePlayer.getHeldItem());
    }

    public int getClientHeldItemSlot() {
        return MC.thePlayer.inventory.currentItem;
    }

    public void itemDamage() {
        ItemDamageComponent.damage(true);
    }

    public void damage(boolean var1, float var2) {
        if (!var1) {
            PacketlessDamageComponent.setActive(var2);
        } else {
            DamageUtil.damagePlayer(0.5);
        }
    }

    @Override
    public int getHurtTime() {
        return MC.thePlayer.hurtTime;
    }

    public void damage(boolean var1) {
        this.damage(var1, 1.0F);
    }

    public void fakeDamage() {
        PlayerUtil.fakeDamage();
    }

    public boolean isUsingItem() {
        return Minecraft.getMinecraft().thePlayer.isUsingItem();
    }

    public boolean isHoldingSword() {
        ItemStack itemstack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        return itemstack != null && itemstack.getItem() instanceof ItemSword;
    }

    public boolean isHoldingTool() {
        ItemStack itemstack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        return itemstack != null && itemstack.getItem() instanceof ItemTool;
    }

    public boolean isHoldingBlock() {
        ItemStack itemstack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        return itemstack != null && itemstack.getItem() instanceof ItemBlock;
    }

    public boolean isHoldingFood() {
        ItemStack itemstack = Minecraft.getMinecraft().thePlayer.getHeldItem();
        return itemstack != null && itemstack.getItem() instanceof ItemFood;
    }

    public ScriptVector2f calculateRotations(ScriptVector3d scriptVector3d) {
        Vector2f vector2f = RotationUtil.d(new Vector3d(scriptVector3d.getX(), scriptVector3d.getY(), scriptVector3d.getZ()));
        return new ScriptVector2f(vector2f.x, vector2f.y);
    }

    public ScriptVector2f calculateRotations(ScriptEntity scriptEntity) {
        ScriptVector3d scriptvector3d = scriptEntity.getPosition();
        scriptvector3d.add(new ScriptVector3d(0.0, 1.8, 0.0));
        return this.calculateRotations(scriptvector3d);
    }

    public boolean mouseOverEntity(ScriptEntity scriptEntity, int var2) {
        MovingObjectPosition movingobjectposition = RayCastUtil.c(RotationComponent.fk, var2);
        return movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectType.ENTITY
            ? movingobjectposition.entityHit != null && movingobjectposition.entityHit.getEntityId() == scriptEntity.getEntityId()
            : false;
    }

    public ScriptInventory getInventory() {
        return new ScriptInventory(MC.thePlayer.inventory);
    }

    public double getBPS() {
        return MC.thePlayer.getDistance(MC.thePlayer.lastTickPosX, MC.thePlayer.posY, MC.thePlayer.lastTickPosZ) * 20.0;
    }

    public void setSprinting(boolean sprinting) {
        MC.thePlayer.setSprinting(sprinting);
    }

    public void setClientRotation(float var1, float var2) {
        MC.thePlayer.pl = var1;
        MC.thePlayer.rotationPitch = var2;
    }

    public float getFallDistance() {
        return MC.thePlayer.fallDistance;
    }

    public float getHunger() {
        return MC.thePlayer.getFoodStats().getFoodLevel();
    }

    public float getAbsorption() {
        return MC.thePlayer.getAbsorptionAmount();
    }

    public int getFacing() {
        return MC.thePlayer.getHorizontalFacing().getIndex();
    }

    public float getEyeHeight() {
        return MC.thePlayer.getEyeHeight();
    }

    public boolean isInWater() {
        return MC.thePlayer.isInWater();
    }

    public boolean isInLava() {
        return MC.thePlayer.isInLava();
    }

    public void setSneaking(boolean sneaking) {
        MC.thePlayer.setSneaking(sneaking);
    }

    public boolean isInWeb() {
        return MC.thePlayer.isInWeb;
    }

    public boolean isOnLadder() {
        return MC.thePlayer.isOnLadder();
    }

    public boolean isCollided() {
        return MC.thePlayer.isCollided;
    }

    public boolean isCollidedHorizontally() {
        return MC.thePlayer.isCollidedHorizontally;
    }

    public boolean isCollidedVertically() {
        return MC.thePlayer.isCollidedVertically;
    }

    public boolean isPotionActive(int var1) {
        return MC.thePlayer.isPotionActive(var1);
    }

    public void placeBlock(ScriptItemStack scriptItemStack, ScriptVector3d scriptVector3d, int var3, ScriptVector3d var4) {
        MC.playerController
            .onPlayerRightClick(
                MC.thePlayer,
                MC.theWorld,
                scriptItemStack.getWrapped(),
                new BlockPos(scriptVector3d.getX(), scriptVector3d.getY(), scriptVector3d.getZ()),
                EnumFacing.getFront(var3),
                new Vec3(var4.getX(), var4.getY(), var4.getZ())
            );
    }

    public String getGUI() {
        if (MC.currentScreen == null) {
            return "none";
        } else if (MC.currentScreen instanceof GuiChest) {
            return "chest";
        } else if (MC.currentScreen instanceof RiseClickGUI) {
            return "clickgui";
        } else if (MC.currentScreen instanceof GuiChat) {
            return "chat";
        }
        return MC.currentScreen instanceof GuiInventory ? "inventory" : "undefined";
    }

    public int getTicksSinceVelocity() {
        return MC.thePlayer.ae;
    }

    public int getTicksSinceTeleport() {
        return MC.thePlayer.Zl;
    }
}
