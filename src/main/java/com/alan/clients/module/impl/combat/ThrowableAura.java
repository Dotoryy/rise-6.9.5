package com.alan.clients.module.impl.combat;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.ghost.AimBacktrack;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.player.SlotUtil;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

@ModuleInfo(aliases = "module.combat.throwableaura.name", description = "module.combat.throwableaura.description", category = Category.COMBAT)
public class ThrowableAura extends Module {
    public int delayTicks;
    public BooleanValue players;
    public int previousSlot;
    public EntityLivingBase target;
    public BooleanValue prediction;
    public NumberValue fOV;
    public boolean throwing;
    public BooleanValue playerTeammates;
    public BooleanValue moduleCheck;
    public NumberValue minimumRange;
    public NumberValue range;
    public BooleanValue autoSwitch;
    @EventLink
    public Listener<PreUpdateEvent> onPreUpdate;
    public BooleanValue eggs;
    public BooleanValue snowballs;
    public BooleanValue throughWalls;
    public NumberValue throwDelay;
    public NumberValue rotationRange = new NumberValue("Rotation Range", this, 8.0, 1.0, 15.0, 0.1);
    public BooleanValue mobs;
    public BooleanValue antiBot;

    @Override
    public void onEnable() {
        this.delayTicks = 0;
        this.previousSlot = -1;
        this.throwing = false;
        this.target = null;
    }


    public Vec3 getPredictedPosition(Entity entity) {
        Vec3 vec3 = entity.getPositionVector().addVector(0.0, entity.getEyeHeight() * 0.5, 0.0);
        double d1 = aEg.thePlayer.getDistanceToEntity(entity) / 1.5;
        return vec3.addVector(entity.motionX * d1, entity.motionY * d1, entity.motionZ * d1);
    }

    @Override
    public void onDisable() {
        if (this.autoSwitch.wo() && this.previousSlot != -1 && aEg.thePlayer != null) {
            aEg.thePlayer.inventory.currentItem = this.previousSlot;
        }

        this.throwing = false;
        this.target = null;
        this.previousSlot = -1;
    }

    public boolean isFacing(EntityLivingBase living) {
        Vec3 vec3 = this.prediction.wo() ? this.getPredictedPosition(living) : living.getPositionVector().addVector(0.0, living.getEyeHeight() * 0.5, 0.0);
        Vec3 vec = aEg.thePlayer.getPositionEyes(1.0F);
        Vec3 vec32 = aEg.thePlayer.getLookVec().normalize();
        Vec3 vec33 = vec3.subtract(vec).normalize();
        return vec32.dotProduct(vec33) > 0.9;
    }

    public void throwAt(Entity entity) {
        if (aEg.playerController != null && entity != null) {
            float f2 = aEg.thePlayer.pl;
            float rotationPitch = aEg.thePlayer.rotationPitch;
            this.h(entity);
            ItemStack itemstack = aEg.thePlayer.getHeldItem();
            if (itemstack != null) {
                aEg.thePlayer.swingItem();
                aEg.playerController.sendUseItem(aEg.thePlayer, aEg.theWorld, itemstack);
                aEg.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(itemstack));
            }

            aEg.thePlayer.pl = f2;
            aEg.thePlayer.rotationPitch = rotationPitch;
        }
    }

    static {
    }

    public boolean canThrow() {
        if (!this.moduleCheck.wo()) {
            return true;
        }

        AimBacktrack aimbacktrack = this.e(AimBacktrack.class);
        return aimbacktrack == null || !aimbacktrack.isEnabled();
    }

    public void h(Entity entity) {
        Vec3 vec3 = this.prediction.wo() ? this.getPredictedPosition(entity) : entity.getPositionVector().addVector(0.0, entity.getEyeHeight() * 0.5, 0.0);
        Vec3 vec = aEg.thePlayer.getPositionEyes(1.0F);
        double d6 = vec3.xCoord - vec.xCoord;
        double d7 = vec3.yCoord - vec.yCoord;
        double d8 = vec3.zCoord - vec.zCoord;
        double d9 = Math.atan2(d8, d6) * 180.0 / Math.PI - 90.0;
        double d10 = Math.sqrt(d6 * d6 + d8 * d8);
        double d11 = -(Math.atan2(d7, d10) * 180.0 / Math.PI);
        aEg.thePlayer.pl = (float)d9;
        aEg.thePlayer.rotationPitch = (float)d11;
    }

    public boolean shouldClearAuraTarget(KillAura killAura) {
        return killAura != null && killAura.isEnabled() && killAura.jE != null ? !this.isAuraTargetInRange(killAura) : false;
    }

    public ThrowableAura() {
        this.range = new NumberValue("Range", this, 6.0, 1.0, 12.0, 0.1);
        this.minimumRange = new NumberValue("Minimum Range", this, 3.0, 1.0, 10.0, 0.1);
        this.throwDelay = new NumberValue("Throw Delay", this, 5.0, 0.0, 100.0, 1.0);
        this.fOV = new NumberValue("FOV", this, 180.0, 30.0, 360.0, 1.0);
        this.playerTeammates = new BooleanValue("Player Teammates", this, true);
        this.players = new BooleanValue("Players", this, true);
        this.mobs = new BooleanValue("Mobs", this, true);
        this.autoSwitch = new BooleanValue("Auto Switch", this, true);
        this.prediction = new BooleanValue("Prediction", this, true);
        this.moduleCheck = new BooleanValue("Module Check", this, true);
        this.antiBot = new BooleanValue("AntiBot", this, true);
        this.snowballs = new BooleanValue("Snowballs", this, true);
        this.eggs = new BooleanValue("Eggs", this, true);
        this.throughWalls = new BooleanValue("Through Walls", this, true);
        this.previousSlot = -1;
        this.onPreUpdate = var1 -> {
            if (aEg.thePlayer != null && aEg.theWorld != null && !this.throwing) {
                if (this.canThrow()) {
                    if (this.delayTicks < this.throwDelay.wo().intValue()) {
                        this.delayTicks++;
                    } else {
                        this.delayTicks = 0;
                        this.target = this.getTarget();
                        if (this.target != null) {
                            double d1 = aEg.thePlayer.getDistanceToEntity(this.target);
                            if (!(d1 > this.range.wo().doubleValue()) && !(d1 <= this.minimumRange.wo().doubleValue())) {
                                int gu2 = this.findThrowableSlot();
                                if (gu2 != -1) {
                                    if (this.isFacing(this.target) && this.canHit(this.target)) {
                                        int currentItem2 = aEg.thePlayer.inventory.currentItem;
                                        if (this.autoSwitch.wo() && currentItem2 != gu2) {
                                            if (this.previousSlot == -1) {
                                                this.previousSlot = currentItem2;
                                            }

                                            aEg.thePlayer.inventory.currentItem = gu2;
                                        }

                                        ItemStack itemstack = aEg.thePlayer.getHeldItem();
                                        if (!this.isThrowable(itemstack)) {
                                            if (this.autoSwitch.wo() && this.previousSlot != -1) {
                                                aEg.thePlayer.inventory.currentItem = this.previousSlot;
                                                this.previousSlot = -1;
                                            }
                                        } else {
                                            KillAura killaura = this.e(KillAura.class);
                                            EntityLivingBase entitylivingbase = this.shouldClearAuraTarget(killaura) ? killaura.jE : null;
                                            if (entitylivingbase != null) {
                                                killaura.jE = null;
                                            }

                                            this.throwing = true;
                                            this.throwAt(this.target);
                                            this.throwing = false;
                                            if (entitylivingbase != null && killaura != null) {
                                                killaura.jE = entitylivingbase;
                                            }

                                            if (this.autoSwitch.wo() && this.previousSlot != -1) {
                                                aEg.thePlayer.inventory.currentItem = this.previousSlot;
                                                this.previousSlot = -1;
                                            }

                                            this.target = null;
                                        }
                                    }
                                }
                            } else {
                                this.target = null;
                            }
                        }
                    }
                }
            }
        };
    }

    public boolean canHit(EntityLivingBase living) {
        if (aEg.objectMouseOver != null && aEg.objectMouseOver.entityHit == living) {
            return true;
        }

        MovingObjectPosition movingobjectposition = RayCastUtil.rayCast(new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch), this.range.wo().doubleValue(), 0.1F);
        return movingobjectposition != null && movingobjectposition.entityHit == living;
    }

    public int findThrowableSlot() {
        if (this.snowballs.wo()) {
            int e2 = SlotUtil.findItem(Items.snowball);
            if (e2 != -1) {
                return e2;
            }
        }

        if (this.eggs.wo()) {
            int e3 = SlotUtil.findItem(Items.egg);
            if (e3 != -1) {
                return e3;
            }
        }

        return -1;
    }

    public boolean isThrowable(ItemStack stack) {
        return stack == null ? false : this.snowballs.wo() && stack.getItem() == Items.snowball || this.eggs.wo() && stack.getItem() == Items.egg;
    }

    public boolean isAuraTargetInRange(KillAura killAura) {
        EntityLivingBase entitylivingbase = killAura.jE;
        if (entitylivingbase == null) {
            return false;
        }

        double d1 = killAura.range.wo().doubleValue();
        MovingObjectPosition movingobjectposition = RayCastUtil.c(RotationComponent.fk, d1);
        return movingobjectposition != null && movingobjectposition.entityHit == entitylivingbase
            ? true ^ true
            : aEg.thePlayer.getDistanceToEntity(entitylivingbase) <= d1;
    }

    public EntityLivingBase getTarget() {
        return aEg.theWorld.loadedEntityList.stream().filter(EntityLivingBase.class::isInstance).map(EntityLivingBase.class::cast).filter(var1 -> {
            if (var1 != aEg.thePlayer && var1.isEntityAlive()) {
                double d1 = aEg.thePlayer.getDistanceToEntity(var1);
                if (d1 > this.rotationRange.wo().doubleValue() || d1 <= this.minimumRange.wo().doubleValue()) {
                    return true ^ true;
                } else if (!this.isInFOV(var1)) {
                    return false;
                } else if (this.throughWalls.wo() && !aEg.thePlayer.canEntityBeSeen(var1)) {
                    return false;
                } else if (this.antiBot.wo() && Client.a.getBotManager().a(var1)) {
                    return false;
                } else if (var1 instanceof EntityPlayer) {
                    return !this.players.wo() ? false : !this.playerTeammates.wo() || !PlayerUtil.sameTeam(var1);
                }
                return var1 instanceof IMob ? this.mobs.wo() : false;
            }
            return false;
        }).min(Comparator.comparingDouble(var0 -> aEg.thePlayer.getDistanceSqToEntity(var0))).orElse(null);
    }

    public boolean isInFOV(EntityLivingBase living) {
        if (this.fOV.wo().doubleValue() >= 360.0) {
            return true;
        }

        Vec3 vec3 = aEg.thePlayer.getLookVec().normalize();
        Vec3 vec = living.getPositionVector().addVector(0.0, living.getEyeHeight() * 0.5, 0.0).subtract(aEg.thePlayer.getPositionEyes(1.0F)).normalize();
        return Math.toDegrees(Math.acos(MathHelper.clamp_double(vec3.dotProduct(vec), -1.0, 1.0))) <= this.fOV.wo().doubleValue() / 2.0;
    }
}
