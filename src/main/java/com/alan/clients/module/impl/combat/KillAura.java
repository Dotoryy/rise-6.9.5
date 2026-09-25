package com.alan.clients.module.impl.combat;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.component.impl.player.PingSpoofComponent;
import com.alan.clients.component.impl.player.RotationComponent;
import com.alan.clients.component.impl.player.SlotComponent;
import com.alan.clients.component.impl.player.rotationcomponent.MovementFix;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.combat.velocity.GrimReduceVelocity;
import com.alan.clients.module.impl.combat.velocity.GrimVelocity;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.movement.speed.GrimSpeed;
import com.alan.clients.module.impl.player.Breaker;
import com.alan.clients.module.impl.player.Manager;
import com.alan.clients.module.impl.player.OldManager;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.module.impl.player.Stealer;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.ClickEvent;
import com.alan.clients.newevent.impl.motion.HitSlowDownEvent;
import com.alan.clients.newevent.impl.motion.JumpEvent;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.SlowDownEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.newevent.impl.render.MouseOverEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.newevent.impl.render.RenderItemEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.ListValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_18_2to1_19.packet.ServerboundPackets1_19;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.type.EvictingList;
import com.alan.clients.util.RayCastUtil;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.ServerUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.player.PacketQueueComponent;
import com.alan.clients.component.impl.player.GUIDetectionComponent;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.util.social.FriendManager;
import com.alan.clients.component.impl.render.ESPComponent;
import com.alan.clients.component.impl.render.espcomponent.impl.AboveBox;
import com.alan.clients.component.impl.render.espcomponent.impl.FullBox;
import com.alan.clients.component.impl.render.espcomponent.impl.SigmaRing;
import com.alan.clients.newevent.impl.input.RightClickEvent;
import com.alan.clients.newevent.impl.motion.SprintEvent;
import com.alan.clients.module.impl.combat.KillAuraSwitchMap;
import com.alan.clients.module.impl.combat.KnockbackSample;
import com.alan.clients.module.impl.combat.RotationSnapshot;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.item.bw;
import net.minecraft.item.cn;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.l;
import net.minecraft.network.play.client.m;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Tuple;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = {"module.combat.killaura.name", "Aura", "Force Field"}, description = "module.combat.killaura.description", category = Category.COMBAT)
public class KillAura extends Module {
   public MovingObjectPosition movingObjectPosition;
   public BooleanValue blockSlowdown;
   public BooleanValue rayCast;
   public int blockSlot;
   public int triggerEntityId;
   public int lastTargetId;
   public NumberValue fOV;
   public int rightClickTick;
   public NumberValue advancedOvershootMax;
   public NumberValue randomizeFactor;
   public BooleanValue advancedSwing;
   public BooleanValue badPacketsCheck;
   public int of;
   public List<EntityLivingBase> espTargets;
   public NumberValue advancedDampedDistance;
   public boolean rightClickHandled;
   public BooleanValue showTargets;
   public boolean ob;
   public BooleanValue coloredSigmaRing;
   public BooleanValue extra;
   public boolean rightClickDown;
   public NumberValue advancedTriggerReaction;
   public EntityLivingBase jE;
   public BooleanValue advancedFlickGuard;
   @EventLink
   public Listener<Render2DEvent> onRender2D;
   public BooleanValue keepSprint;
   public ModeValue lV;
   public NumberValue advancedGravity;
   @EventLink
   public Listener<SlowDownEvent> onSlowDown;
   public BooleanValue fireAspect;
   public boolean blockQueued;
   public static boolean inReach;
   public BooleanValue oldPredictionKeepSprint;
   public BooleanValue axes;
   public static boolean blockPacketSent;
   public long triggerReadyTime;
   public static boolean canAttack;
   public BooleanValue oldMovefixBoost;
   public int lastBlockAttackTick;
   public boolean oc;
   public int lastVelocityBoostTick;
   @EventLink
   public Listener<PreUpdateEvent> onPreUpdate;
   public float burstStrength;
   public ModeValue mode;
   public boolean fakeBlocked;
   public boolean shortBlockCycle;
   public boolean warnedNoSecondSword;
   public BooleanValue hideSecondSword;
   public double cachedRotationsRange;
   public BoundsNumberValue switchDelay;
   public float stepSize;
   public int holdTicks;
   @EventLink
   public Listener<PostMotionEvent> onPostMotion;
   public BooleanValue showFOVCircle;
   public int nZ;
   public BooleanValue rightClickOnly;
   public float velocityY;
   public int attackPending;
   public NumberValue advancedMinStep;
   public NumberValue advancedTriggerReactionJitter;
   public StopWatch swingTimer;
   public BooleanValue silentRotations;
   public NumberValue advancedFlickMax;
   public ModeValue sorting;
   public BooleanValue noSwing;
   public BooleanValue velocityBoost;
   @EventLink
   public Listener<RightClickEvent> onAutoBlock;
   public Vector3d playerMotionAverage;
   public int lastDebugTick;
   public boolean pendingDisable;
   public NumberValue advancedPrediction;
   public NumberValue advancedDeadzone;
   public KnockbackSample knockbackPlan;
   public float ok;
   public List<EntityLivingBase> targets;
   public int ticksSinceAttack;
   public StopWatch attackTimer;
   public int lastSlotFlickTick;
   public BooleanValue attackWhilstScaffolding;
   public Entity target;
   public float windX;
   public BooleanValue subtickRaycast;
   public NumberValue advancedPaceJitter;
   public NumberValue advancedAnchor;
   public BooleanValue preventServersideBlocking;
   public BooleanValue newYouNeedThisToggledOnCurreFake;
   public Vector3d aimPoint;
   public long attackInterval;
   public BooleanValue sharpness;
   @EventLink
   public Listener<WorldChangeEvent> onWorldChange;
   public Map<Entity, Integer> lastHitTicks;
   @EventLink(value = 3)
   public Listener<PacketSendEvent> onPacketSend;
   public BooleanValue knockbackDisplacementDebug;
   public BooleanValue playerTeammates;
   public Queue<Packet<?>> heldPackets;
   @EventLink
   public Listener<MouseOverEvent> onMouseOver;
   public static float SQRT3;
   public int cachedRotationsTick;
   public NumberValue advancedOvershootScale;
   public Random random;
   public EvictingList<EntityLivingBase> switchHistory;
   public int overshootTicks;
   public Animation fovCircleYaw;
   public BooleanValue swords;
   public NumberValue advancedWind;
   public boolean triggerArmed;
   public BoundsNumberValue cps;
   public Vector2f cachedRotations;
   public NumberValue advancedAimReactionJitter;
   public ModeValue boxMode;
   public BooleanValue weapons;
   public int oe;
   public boolean cachedRotationsThroughWalls;
   @EventLink
   public Listener<HitSlowDownEvent> onHitSlowDown;
   public NumberValue advancedMaxStep;
   @EventLink
   public Listener<SprintEvent> onKeepSprintCancel;
   public BooleanValue throughWalls;
   public BooleanValue fallbackToWatchdog;
   @EventLink(value = 3)
   public Listener<RenderItemEvent> onRenderItem;
   public boolean watchdogFallbackActive;
   public NumberValue advancedAimReaction;
   public static boolean blocking;
   public BooleanValue animals;
   public BooleanValue knockbackDisplacement;
   public BooleanValue invisibles;
   public NumberValue advancedHoldTicks;
   public BooleanValue fist;
   public BooleanValue mobs;
   public boolean sprintCancelled;
   public String lastDebugLine;
   public int attack;
   public NumberValue advancedGaussian;
   public BooleanValue newUniversalKeepSprint;
   public BooleanValue advanced;
   public BoundsNumberValue rotationSpeed;
   public NumberValue advancedOvershootChance;
   public ListValue<MovementFix> movementCorrection;
   public Vector3d targetMotionAverage;
   public NumberValue rotationRange;
   public ModeValue rotationMode;
   public BooleanValue autoDisable;
   public BooleanValue showMovementArc;
   @EventLink
   public Listener<ClickEvent> onClick;
   public NumberValue range;
   public BooleanValue randomize19Speed;
   public int hV;
   public int burstTicks;
   public BooleanValue player;
   public Animation fovCircleRadius;
   @EventLink
   public Listener<JumpEvent> onJump;
   public Vector2f grimRotations;
   public NumberValue advancedCruiseFloor;
   public int blockStage;
   public NumberValue boostTicks;
   public String nE;
   public ModeValue clickMode;
   public NumberValue advancedAccuracy;
   public float windY;
   public Vector2f on;
   public NumberValue advancedBurstStrength;
   @EventLink
   public Listener<PreMotionEvent> onPreMotionEvent;
   public long nextAimUpdate;
   public static boolean attacking;
   public NumberValue advancedBurstChance;
   @EventLink(value = 3)
   public Listener<PreUpdateEvent> onPreUpdateHigh;
   public BooleanValue knockback;
   public int expandRange;
   public ModeValue espMode;
   public static float SQRT5;

   public Tuple<Boolean, Double> getAttackDelay() {
      Object var52 = null;
      double var53 = 0.0;
      Object var55 = null;
      double var64 = 0.0;
      var64 = -1.0;
      int var50_lo = 0;
      var52 = this.clickMode.wo().getName();
      int var50_hi = -1;
      switch (((String)var52).hashCode()) {
         case 1505775:
            if (((String)var52).equals("1.9+")) {
               var50_hi = 1;
            }
            break;
         case 1934158813:
            if (((String)var52).equals("1.9+ With 1.8 Animations")) {
               var50_hi = 0;
            }
      }

      switch (var50_hi) {
         case 0:
         case 1:
            if (this.clickMode.wo().getName().equals("1.9+ With 1.8 Animations") && Math.random() > 0.2) {
               RenderUtil.E(this.jE);
            }

            var53 = 4.0;
            if (aEg.thePlayer.getHeldItem() != null) {
               var55 = aEg.thePlayer.getHeldItem().getItem();
               if ((Item)var55 instanceof ItemSword) {
                  var53 = 1.6;
               } else if ((Item)var55 instanceof cn) {
                  var53 = 1.0;
               } else if ((Item)var55 instanceof bw) {
                  var53 = 1.2;
               } else if ((Item)var55 instanceof ItemAxe) {
                  switch (((ItemAxe)((Item)var55)).getToolMaterial()) {
                     case WOOD:
                     case STONE:
                        var53 = 0.8;
                        break;
                     case IRON:
                        var53 = 0.9;
                        break;
                     default:
                        var53 = 1.0;
                  }
               } else if ((Item)var55 instanceof ItemHoe) {
                  switch (((ItemHoe)((Item)var55)).getToolMaterial()) {
                     case WOOD:
                     case GOLD:
                        var53 = 1.0;
                        break;
                     case STONE:
                        var53 = 2.0;
                        break;
                     case IRON:
                        var53 = 3.0;
                  }
               }
            }

            if (this.randomize19Speed.wo()) {
               var53 -= Math.random() * this.randomizeFactor.wo().doubleValue();
            }

            var64 = 1.0 / var53 * 20.0 - 1.0;
         default:
            var64 = this.adjustAttackDelay(var64);
            return new Tuple<>(Boolean.valueOf((var50_lo) != 0), var64);
      }
   }

   public boolean hasTwoSwords() {
      return aEg.thePlayer != null && this.findSwordSlots()[1] != -1;
   }

   public void c(MovingObjectPosition hit) {
      if (!aEg.playerController.isPlayerRightClickingOnEntity(aEg.thePlayer, hit.entityHit, hit)) {
         aEg.playerController.interactWithEntitySendPacket(aEg.thePlayer, hit.entityHit);
      }
   }

   public boolean isTriggerReactionElapsed(EntityLivingBase living, MovingObjectPosition hit, double var3) {
      long var88 = 0L;
      long var92 = 0L;
      if (!this.isAdvancedRotations()) {
         return true;
      } else if (living == null) {
         this.triggerEntityId = Integer.MIN_VALUE;
         this.triggerArmed = false;
         this.triggerReadyTime = 0L;
         return false;
      }
      int notDistanceToEntity = (this.rayCast.wo() || !(aEg.thePlayer.getDistanceToEntity(living) <= var3)) && (hit == null || hit.entityHit != living) ? 0 : 1;
      if (notDistanceToEntity == 0) {
         this.triggerArmed = false;
         this.triggerEntityId = Integer.MIN_VALUE;
         this.triggerReadyTime = 0L;
         return false;
      }
      int entityId = living.getEntityId();
      var92 = System.currentTimeMillis();
      if (this.triggerArmed && this.triggerEntityId == entityId) {
         return var92 >= this.triggerReadyTime;
      }
      this.triggerEntityId = entityId;
      this.triggerArmed = true;
      var88 = this.jitteredDelay(this.advancedTriggerReaction.wo().doubleValue(), this.advancedTriggerReactionJitter.wo().doubleValue(), 0L, 450L);
      this.triggerReadyTime = var92 + var88;
      return var88 <= 0L;
   }

   public boolean handleRightClickBlock() {
      int keyDown = (int)(aEg.gameSettings.cgI.isKeyDown() ? 1L : 0L);
      if (!this.isDualSword() || !this.rightClickOnly.wo()) {
         this.rightClickTick = -1;
         this.rightClickDown = (keyDown) != 0;
         return false;
      } else if (this.rightClickTick != -1) {
         this.rightClickHandled = true;
         canAttack = false;
         if (aEg.thePlayer.ticksExisted != this.rightClickTick) {
            this.releaseBlock(true);
            this.rightClickTick = -1;
         }

         this.rightClickDown = (keyDown) != 0;
         return true;
      } else if (this.rightClickDown && keyDown == 0) {
         this.rightClickTick = aEg.thePlayer.ticksExisted;
         this.rightClickHandled = true;
         canAttack = false;
         this.rightClickDown = false;
         return true;
      }
      this.rightClickDown = (keyDown) != 0;
      return false;
   }

   public Vector2f computeRotations(EntityLivingBase target, double var2, boolean var4) {
      Object var37 = null;
      float var44 = 0.0F;
      Object var46 = null;
      if (target == null) {
         return new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
      }
      int ticksExisted2 = aEg.thePlayer.ticksExisted;
      if (this.cachedRotations != null && this.cachedRotationsTick == ticksExisted2 && this.target == target && this.cachedRotationsRange == var2 && this.cachedRotationsThroughWalls == var4) {
         return this.cachedRotations;
      }
      var37 = target.getEntityBoundingBox();
      if ((AxisAlignedBB)var37 != null && !((AxisAlignedBB)var37).hasNaN()) {
         var44 = this.getHitBoxExpand();
         var37 = ((AxisAlignedBB)var37).expand(var44, var44, var44);
         var46 = RotationUtil.a(target, (AxisAlignedBB)var37, true, var2, var4, var44);
      } else {
         var46 = RotationUtil.calculate(target, true, var2);
      }

      this.cachedRotations = (Vector2f)var46;
      this.cachedRotationsTick = ticksExisted2;
      this.target = target;
      this.cachedRotationsRange = var2;
      this.cachedRotationsThroughWalls = var4;
      return (Vector2f)var46;
   }

   public Vector2f b(Vector2f vec2) {
      Object var147 = null;
      float var153 = 0.0F;
      float var154 = 0.0F;
      float var155 = 0.0F;
      float var160 = 0.0F;
      float var163 = 0.0F;
      float var166 = 0.0F;
      float var173 = 0.0F;
      float var175 = 0.0F;
      float var177 = 0.0F;
      float var180 = 0.0F;
      float var181 = 0.0F;
      float var187 = 0.0F;
      float var188 = 0.0F;
      float var189 = 0.0F;
      float var192 = 0.0F;
      Object var197 = null;
      float var200 = 0.0F;
      float var203 = 0.0F;
      float var204 = 0.0F;
      Object var205 = null;
      float var206 = 0.0F;
      float var208 = 0.0F;
      float var209 = 0.0F;
      float var210 = 0.0F;
      float var212 = 0.0F;
      float var213 = 0.0F;
      float var214 = 0.0F;
      float var215 = 0.0F;
      float var216 = 0.0F;
      if (!this.isAdvancedRotations()) {
         return vec2;
      }
      var147 = this.getServerRotations();
      if (this.jE == null) {
         return vec2;
      }
      var153 = this.advancedMaxStep.wo().floatValue();
      var154 = this.advancedDampedDistance.wo().floatValue();
      var181 = Math.max(0.25F, this.advancedMinStep.wo().floatValue());
      var192 = MathHelper.clamp_float(this.advancedAccuracy.wo().floatValue() / 100.0F, 0.4F, 1.0F);
      if (this.stepSize <= 0.001F) {
         this.stepSize = var153;
      }

      int entityId = this.jE.getEntityId();
      if (entityId != this.lastTargetId) {
         this.windX = 0.0F;
         this.windY = 0.0F;
         this.ok = 0.0F;
         this.velocityY = 0.0F;
         this.stepSize = var153;
         this.on = null;
         this.overshootTicks = 0;
         this.aimPoint = null;
         this.holdTicks = 0;
         this.nextAimUpdate = 0L;
         this.triggerEntityId = Integer.MIN_VALUE;
         this.triggerArmed = false;
         this.triggerReadyTime = 0L;
         this.burstTicks = 0;
         this.burstStrength = 0.0F;
         this.lastTargetId = entityId;
         this.planOvershoot((Vector2f)var147, vec2);
      }

      var205 = vec2;
      int var184_hi = 0;
      if (this.on != null) {
         var180 = MathHelper.wrapAngleTo180_float(this.on.x - ((Vector2f)var147).x);
         var203 = this.on.y - ((Vector2f)var147).y;
         var160 = (float)Math.hypot(var180, var203);
         if (!(var160 < 1.15F) && this.overshootTicks-- > 0) {
            var205 = this.on;
            var184_hi = 1;
         } else {
            this.on = null;
         }
      }

      var180 = MathHelper.wrapAngleTo180_float(((Vector2f)var205).x - ((Vector2f)var147).x);
      var203 = ((Vector2f)var205).y - ((Vector2f)var147).y;
      var160 = (float)Math.hypot(var180, var203);
      var213 = Math.max(0.001F, var160);
      var155 = this.advancedDeadzone.wo().floatValue();
      var197 = RayCastUtil.rayCast((Vector2f)var147, this.range.wo().doubleValue() + 0.15, this.getHitBoxExpand(), aEg.thePlayer, this.canHitThroughWalls());
      int flag = (MovingObjectPosition)var197 != null && ((MovingObjectPosition)var197).typeOfHit == MovingObjectType.ENTITY && ((MovingObjectPosition)var197).entityHit == this.jE ? 1 : 0;
      if (var184_hi == 0 && flag != 0 && var160 <= var155) {
         this.holdTicks = Math.max(this.holdTicks, this.advancedHoldTicks.wo().intValue());
      }

      if (var184_hi == 0 && this.holdTicks > 0 && flag != 0 && var160 <= var155 * 1.35F) {
         this.holdTicks--;
         this.ok *= 0.42F;
         this.velocityY *= 0.42F;
         return RotationUtil.applySensitivityPatch(new Vector2f(((Vector2f)var147).x, ((Vector2f)var147).y), (Vector2f)var147);
      }
      if (this.holdTicks > 0) {
         this.holdTicks--;
      }

      if (var160 < 0.001F) {
         this.ok *= 0.6F;
         this.velocityY *= 0.6F;
         return RotationUtil.applySensitivityPatch(new Vector2f(((Vector2f)var147).x, MathHelper.clamp_float(((Vector2f)var147).y, -90.0F, 90.0F)), (Vector2f)var147);
      }
      var204 = Math.min(this.advancedWind.wo().floatValue(), var160);
      if (var160 >= var154) {
         this.windX = this.windX / SQRT3 + this.b(var204 / SQRT5, var204);
         this.windY = this.windY / SQRT3 + this.b(var204 / SQRT5, var204);
         var206 = var153 * (0.78F + this.random.nextFloat() * 0.32F);
         this.stepSize = Math.max(this.stepSize, var206);
      } else {
         this.windX = this.windX / SQRT3;
         this.windY = this.windY / SQRT3;
         if (this.stepSize < var181) {
            this.stepSize = var181 + this.random.nextFloat() * 0.7F;
         } else {
            this.stepSize = Math.max(var181, this.stepSize / SQRT5);
         }
      }

      var206 = this.advancedGravity.wo().floatValue();
      if (var160 < 3.5F) {
         var206 *= 0.82F + var192 * 0.12F;
      }

      this.ok = this.ok + (this.windX + var206 * var180 / var213);
      this.velocityY = this.velocityY + (this.windY + var206 * var203 / var213);
      var212 = 1.0F;
      var175 = this.advancedPaceJitter.wo().floatValue();
      if (var175 > 1.0E-4F) {
         var212 += this.b(var175 * 0.42F, var175);
      }

      if (var184_hi == 0 && var160 > var155 * 1.6F && var160 < var154 * 1.45F) {
         var187 = this.advancedBurstChance.wo().floatValue() / 100.0F;
         if (this.burstTicks <= 0 && var187 > 1.0E-4F && this.random.nextFloat() < var187 * 0.12F) {
            this.burstTicks = 1 + this.random.nextInt(2);
            this.burstStrength = this.advancedBurstStrength.wo().floatValue() * (0.65F + this.random.nextFloat() * 0.55F);
         }
      }

      if (this.burstTicks > 0) {
         var212 *= 1.0F + this.burstStrength;
         this.burstTicks--;
      } else {
         this.burstStrength *= 0.55F;
      }

      var212 = MathHelper.clamp_float(var212, 0.72F, 1.55F);
      var187 = Math.max(var181, this.stepSize * var212);
      var173 = (float)Math.hypot(this.ok, this.velocityY);
      if (var173 > var187) {
         var189 = var187 * (0.52F + this.random.nextFloat() * 0.48F);
         this.ok = this.ok / var173 * var189;
         this.velocityY = this.velocityY / var173 * var189;
      }

      if (var160 < 2.0F) {
         var189 = 0.86F + var192 * 0.08F;
         this.ok *= var189;
         this.velocityY *= var189;
      } else if (var160 < 5.0F) {
         this.ok *= 0.94F;
         this.velocityY *= 0.94F;
      }

      var189 = this.advancedCruiseFloor.wo().floatValue();
      if (var184_hi == 0 && var189 > 0.01F && var160 > var155 * 1.25F && var160 < 14.0F) {
         var216 = var189 * (0.86F + this.random.nextFloat() * 0.24F);
         var166 = (float)Math.hypot(this.ok, this.velocityY);
         if (var166 < var216) {
            var177 = (var216 - var166) * (0.72F + this.random.nextFloat() * 0.36F);
            this.ok += var180 / var213 * var177;
            this.velocityY += var203 / var213 * var177;
         }
      }

      var216 = MathHelper.clamp_float(var160 / 45.0F, 0.0F, 1.0F);
      var166 = (0.16F + var216 * 0.42F) * (0.55F + var192 * 0.45F);
      var166 *= MathHelper.clamp_float(0.88F + (var212 - 1.0F) * 0.5F, 0.74F, 1.18F);
      if (this.burstStrength > 0.02F) {
         var166 *= 1.0F + this.burstStrength * 0.35F;
      }

      if (var184_hi != 0) {
         var166 *= 0.55F;
      }

      var177 = ((Vector2f)var147).x + this.ok + var180 * var166;
      var214 = MathHelper.clamp_float(((Vector2f)var147).y + this.velocityY + var203 * (var166 * 0.85F), -89.9F, 89.9F);
      if (var184_hi == 0 && var160 < 1.65F) {
         var210 = (0.56F + var192 * 0.28F) * (0.94F + this.random.nextFloat() * 0.1F);
         var177 = ((Vector2f)var147).x + var180 * var210;
         var214 = MathHelper.clamp_float(((Vector2f)var147).y + var203 * var210, -89.9F, 89.9F);
         this.ok *= 0.55F;
         this.velocityY *= 0.55F;
      }

      var210 = this.advancedGaussian.wo().floatValue();
      if (var210 > 0.0F && var160 > 0.35F) {
         var209 = MathHelper.clamp_float(var160 / 16.0F, 0.25F, 1.0F);
         var209 *= 1.05F - var192 * 0.35F;
         if (var184_hi != 0) {
            var209 *= 0.8F;
         }

         var177 += this.b(var210 * var209, var210 * 2.2F * var209);
         var214 += this.b(var210 * 0.45F * var209, var210 * 1.5F * var209);
         var214 = MathHelper.clamp_float(var214, -89.9F, 89.9F);
      }

      if (this.advancedFlickGuard.wo() && var184_hi == 0) {
         var209 = MathHelper.wrapAngleTo180_float(var177 - ((Vector2f)var147).x);
         var208 = var214 - ((Vector2f)var147).y;
         var188 = (float)Math.hypot(var209, var208);
         if (var188 > 0.001F) {
            var215 = this.advancedFlickMax.wo().floatValue();
            var163 = MathHelper.clamp_float(var160 / 24.0F, 0.0F, 1.0F);
            var215 *= 0.85F + var163 * 0.4F;
            var215 *= 0.92F + this.random.nextFloat() * 0.2F;
            if (var188 > var215) {
               var200 = var215 / var188;
               var177 = ((Vector2f)var147).x + var209 * var200;
               var214 = MathHelper.clamp_float(((Vector2f)var147).y + var208 * var200, -89.9F, 89.9F);
               this.ok *= 0.82F;
               this.velocityY *= 0.82F;
            }
         }
      }

      return RotationUtil.applySensitivityPatch(new Vector2f(var177, var214), (Vector2f)var147);
   }

   public void resetAdvancedState() {
      this.windX = 0.0F;
      this.windY = 0.0F;
      this.ok = 0.0F;
      this.velocityY = 0.0F;
      this.stepSize = this.advancedMaxStep.wo().floatValue();
      this.on = null;
      this.overshootTicks = 0;
      this.aimPoint = null;
      this.targetMotionAverage = new Vector3d(0.0, 0.0, 0.0);
      this.playerMotionAverage = new Vector3d(0.0, 0.0, 0.0);
      this.holdTicks = 0;
      this.nextAimUpdate = 0L;
      this.triggerEntityId = Integer.MIN_VALUE;
      this.triggerArmed = false;
      this.triggerReadyTime = 0L;
      this.burstTicks = 0;
      this.burstStrength = 0.0F;
      this.lastTargetId = Integer.MIN_VALUE;
   }

   public boolean eX() {
      if (!this.rightClickOnly.wo() || aEg.gameSettings.cgI.isKeyDown()) {
         SlotComponent var10000 = this.d(SlotComponent.class);
         if (SlotComponent.getItemStack() != null) {
            var10000 = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack().getItem() instanceof ItemSword && (!this.isDualSword() || this.canBlock())) {
               return true;
            }
         }
      }

      return false;
   }

   public KillAura() {
      super();
      int var822 = 0;
      var822 = -1696500114;
      this.mode = new ModeValue("Attack Mode", this).add(new SubMode("Single")).add(new SubMode("Switch")).add(new SubMode("Multiple")).setDefault("Single");
      this.switchDelay = new BoundsNumberValue("Switch Delay", this, 0, 0, 0, 10, 1, () -> !this.mode.wo().getName().equals("Switch"));
      this.lV = new ModeValue("Auto Block", this)
         .add(new SubMode("None"))
         .add(new SubMode("Fake"))
         .add(new SubMode("Vanilla"))
         .add(new SubMode("NCP"))
         .add(new SubMode("Legit"))
         .add(new SubMode("Grim"))
         .add(new SubMode("Intave"))
         .add(new SubMode("Old Intave"))
         .add(new SubMode("Imperfect Vanilla"))
         .add(new SubMode("Vanilla ReBlock"))
         .add(new SubMode("Watchdog 1.12"))
         .add(new SubMode("New NCP"))
         .add(new SubMode("Universal"))
         .add(new SubMode("Watchdog"))
         .add(new SubMode("Dual Sword"))
         .add(new SubMode("Watchdog 1.8"))
         .setDefault("None");
      this.newYouNeedThisToggledOnCurreFake = new BooleanValue("New (you need this toggled on curreFake", this, false, () -> !this.lV.wo().getName().equals("Watchdog"));
      this.fallbackToWatchdog = new BooleanValue("Fallback to Watchdog", this, false, () -> !this.isDualSwordEngaged());
      this.hideSecondSword = new BooleanValue("Hide Second Sword", this, true, () -> (!this.isDualSwordEngaged() ? 1 : 73 ^ 82 ^ 27) != 0);
      this.rightClickOnly = new BooleanValue(
         "Right Click Only", this, false, () -> this.lV.wo().getName().equals("None") || this.lV.wo().getName().equals("Fake")
      );
      this.preventServersideBlocking = new BooleanValue(
         "Prevent Serverside Blocking",
         this,
         false,
         () -> (!this.lV.wo().getName().equals("None") && !this.lV.wo().getName().equals("Fake") ? 1 : 105 - 105) != 0
      );
      this.blockSlowdown = new BooleanValue(
         "Block Slowdown", this, false, () -> this.lV.wo().getName().equals("None") || this.lV.wo().getName().equals("Fake")
      );
      this.sorting = new ModeValue("Sorting", this).add(new SubMode("Distance")).add(new SubMode("Health")).add(new SubMode("Hurt Time")).setDefault("Distance");
      this.clickMode = new ModeValue("Click Delay Mode", this)
         .add(new SubMode("Normal"))
         .add(new SubMode("Hit Select"))
         .add(new SubMode("1.9+"))
         .add(new SubMode("1.9+ With 1.8 Animations"))
         .setDefault("Normal");
      this.randomize19Speed = new BooleanValue("Randomize 1.9+ Speed", this, false, () -> (!this.clickMode.wo().getName().contains("1.9+") ? 1 : (-57 ^ 17) - -42) != 0);
      this.randomizeFactor = new NumberValue("Randomize Factor", this, 0.2, 0.05, 1.0, 0.05, () -> !this.randomize19Speed.wo() || !this.clickMode.wo().getName().contains("1.9+"));
      this.range = new NumberValue("Range", this, 3, 3, 6, 0.1);
      this.rotationRange = new NumberValue("Rotation Range", this, 3, 0, 6, 0.1);
      this.rotationSpeed = new BoundsNumberValue("Rotation speed", this, 5, 10, 0, 10, 1);
      this.fOV = new NumberValue("FOV", this, 360, 0, 360, 1);
      this.showFOVCircle = new BooleanValue("Show FOV Circle", this, false, () -> (this.fOV.wo().doubleValue() >= 360.0 ? 1 : -109 - -109) != 0);
      this.cps = new BoundsNumberValue("CPS", this, 10, 15, 1, 20, 1);
      this.velocityBoost = new BooleanValue("Velocity Boost", this, false);
      this.boostTicks = new NumberValue("Boost Ticks", this, 4, 1, 10, 1, () -> {
         boolean var10000;
         if (!this.velocityBoost.wo()) {
            var10000 = true;
         } else {
            byte var4x = 11;
            var4x = -109;
            boolean var6 = false;
            var10000 = var6;
         }

         return var10000;
      });
      this.knockbackDisplacement = new BooleanValue("Knockback Displacement", this, true);
      this.knockbackDisplacementDebug = new BooleanValue("Knockback Displacement Debug", this, false, () -> (!this.isDisplacementEnabled() ? 1 : -94 ^ -94) != 0);
      this.silentRotations = new BooleanValue("Silent Rotations", this, true);
      this.movementCorrection = new ListValue<>("Movement correction", this);
      this.showMovementArc = new BooleanValue(
         "Show Movement Arc", this, false, () -> (this.movementCorrection.wo() == MovementFix.OFF ? 1 : -71 - -57 ^ -14) != 0
      );
      this.keepSprint = new BooleanValue("Keep sprint", this, false);
      this.oldPredictionKeepSprint = new BooleanValue("Old Prediction Keep sprint", this, false);
      this.oldMovefixBoost = new BooleanValue("Old Movefix Boost", this, false);
      this.newUniversalKeepSprint = new BooleanValue("New Universal Keep sprint", this, false);
      this.attackPending = 0;
      this.fakeBlocked = false;
      this.lastVelocityBoostTick = -1;
      this.sprintCancelled = false;
      this.lastDebugLine = "";
      this.lastDebugTick = -1;
      this.cachedRotationsTick = -1;
      this.espMode = new ModeValue("Target ESP Mode", this).add(new SubMode("Ring")).add(new SubMode("Box")).add(new SubMode("None")).setDefault("Ring");
      this.coloredSigmaRing = new BooleanValue("Colored Sigma Ring", this, true, () -> !this.espMode.wo().getName().equals("Ring"));
      this.boxMode = new ModeValue("Box Mode", this, () -> (!this.espMode.wo().getName().equals("Box") ? 1 : 60 ^ 60) != 0)
         .add(new SubMode("Above"))
         .add(new SubMode("Full"))
         .setDefault("Ring");
      this.rayCast = new BooleanValue("Ray cast", this, false);
      this.subtickRaycast = new BooleanValue("Subtick Raycast", this, true, () -> !this.rayCast.wo());
      this.throughWalls = new BooleanValue("Through Walls", this, false, () -> {
         boolean var10000;
         if (!this.rayCast.wo()) {
            var10000 = true;
         } else {
            byte var4x = -74;
            var4x = 12;
            boolean var6 = false;
            var10000 = var6;
         }

         return var10000;
      });
      this.advanced = new BooleanValue("Advanced", this, false);
      this.rotationMode = new ModeValue("Rotation Mode", this, () -> !this.advanced.wo())
         .add(new SubMode("Legit/Normal"))
         .add(new SubMode("Snap"))
         .add(new SubMode("NCP"))
         .add(new SubMode("Autistic AntiCheat"))
         .add(new SubMode("Advanced"))
         .add(new SubMode("Grim"))
         .setDefault("Legit/Normal");
      this.advancedGravity = new NumberValue("Advanced Gravity", this, 9, 1, 20, 0.1, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedWind = new NumberValue("Advanced Wind", this, 6, 0, 10, 0.1, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedDampedDistance = new NumberValue("Advanced Damped Distance", this, 12, 1, 45, 1, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedMaxStep = new NumberValue("Advanced Max Step", this, 15, 3, 60, 0.5, () -> (!this.rotationMode.wo().getName().equals("Advanced") ? 1 : 104 + -104) != 0);
      this.advancedOvershootChance = new NumberValue("Advanced Overshoot Chance", this, 77, 0, 100, 1, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedOvershootScale = new NumberValue("Advanced Overshoot Scale", this, 0.0, 0.0, 0.6, 0.01, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedOvershootMax = new NumberValue("Advanced Overshoot Max", this, 17, 2, 45, 0.5, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedGaussian = new NumberValue("Advanced Gaussian", this, 0.0, 0, 0.6, 0.01, () -> {
         boolean var10000;
         if (!this.rotationMode.wo().getName().equals("Advanced")) {
            var10000 = true;
         } else {
            byte var6 = 104;
            var6 = -43;
            boolean var8 = false;
            var10000 = var8;
         }

         return var10000;
      });
      this.advancedAccuracy = new NumberValue("Advanced Accuracy", this, 40, 40, 100, 1, () -> (!this.rotationMode.wo().getName().equals("Advanced") ? 1 : -88 - -88) != 0);
      this.advancedMinStep = new NumberValue("Advanced Min Step", this, 0, 0.0, 8.0, 0.1, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedPrediction = new NumberValue("Advanced Prediction", this, 1.0, 0, 3.5, 0.05, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedDeadzone = new NumberValue("Advanced Deadzone", this, 1.0, 0.25, 4.5, 0.05, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedAnchor = new NumberValue(
         "Advanced Anchor", this, 0.0, 0.0, 0.7, 0.01, () -> (!this.rotationMode.wo().getName().equals("Advanced") ? 1 : (28 ^ -53) - -41) != 0
      );
      this.advancedHoldTicks = new NumberValue("Advanced Hold Ticks", this, 2, 0, 8, 1, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedCruiseFloor = new NumberValue("Advanced Cruise Floor", this, 1.0, 0.0, 5.0, 0.05, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedPaceJitter = new NumberValue("Advanced Pace Jitter", this, 0.0, 0.0, 0.8, 0.01, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedBurstChance = new NumberValue(
         "Advanced Burst Chance", this, 21, 0, 100, 1, () -> (!this.rotationMode.wo().getName().equals("Advanced") ? 1 : -18 + -54 - -72) != 0
      );
      this.advancedBurstStrength = new NumberValue("Advanced Burst Strength", this, 0.0, 0.0, 1.0, 0.01, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedFlickGuard = new BooleanValue("Advanced Flick Guard", this, true, () -> {
         int var10000;
         if (!this.rotationMode.wo().getName().equals("Advanced")) {
            var10000 = 1;
         } else {
            int var6 = -56;
            var6 += -22;
            var6 -= -78;
            var10000 = var6;
         }

         return var10000 != 0;
      });
      this.advancedFlickMax = new NumberValue("Advanced Flick Max", this, 29, 4, 60, 0.5, () -> (!this.rotationMode.wo().getName().equals("Advanced") ? 1 : -9 + 9) != 0);
      this.advancedSwing = new BooleanValue("Advanced Swing", this, true, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedAimReaction = new NumberValue("Advanced Aim Reaction", this, 180, 30, 450, 5, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.advancedAimReactionJitter = new NumberValue("Advanced Aim Reaction Jitter", this, 44, 0, 220, 1, () -> {
         boolean var10000;
         if (!this.rotationMode.wo().getName().equals("Advanced")) {
            var10000 = true;
         } else {
            byte var6 = -105;
            var6 = 10;
            boolean var8 = false;
            var10000 = var8;
         }

         return var10000;
      });
      this.advancedTriggerReaction = new NumberValue(
         "Advanced Trigger Reaction", this, 95, 0, 300, 5, () -> (!this.rotationMode.wo().getName().equals("Advanced") ? 1 : 123 - 123) != 0
      );
      this.advancedTriggerReactionJitter = new NumberValue("Advanced Trigger Reaction Jitter", this, 30, 0, 140, 1, () -> !this.rotationMode.wo().getName().equals("Advanced"));
      this.grimRotations = null;
      this.attackWhilstScaffolding = new BooleanValue("Attack whilst Scaffolding", this, false, () -> {
         boolean var10000;
         if (!this.advanced.wo()) {
            var10000 = true;
         } else {
            byte var4x = 105;
            var4x = -41;
            boolean var6 = false;
            var10000 = var6;
         }

         return var10000;
      });
      this.noSwing = new BooleanValue("No swing", this, false, () -> !this.advanced.wo());
      this.autoDisable = new BooleanValue("Auto disable", this, false, () -> !this.advanced.wo());
      this.badPacketsCheck = new BooleanValue("BadPackets check", this, true, () -> (!this.advanced.wo() ? 1 : 175 - 54 + -121) != 0);
      this.showTargets = new BooleanValue("Targets", this, false);
      this.player = new BooleanValue("Player", this, true, () -> !this.showTargets.wo());
      this.invisibles = new BooleanValue("Invisibles", this, false, () -> !this.showTargets.wo());
      this.animals = new BooleanValue("Animals", this, false, () -> !this.showTargets.wo());
      this.mobs = new BooleanValue("Mobs", this, false, () -> {
         boolean var10000;
         if (!this.showTargets.wo()) {
            var10000 = true;
         } else {
            byte var4x = -29;
            var4x = -23;
            boolean var6 = false;
            var10000 = var6;
         }

         return var10000;
      });
      this.playerTeammates = new BooleanValue("Player Teammates", this, true, () -> !this.showTargets.wo());
      this.nE = null;
      this.weapons = new BooleanValue("Weapons", this, false);
      this.fist = new BooleanValue("Fist", this, false, () -> (!this.weapons.wo() ? 1 : -92 - -92) != 0);
      this.swords = new BooleanValue("Swords", this, true, () -> !this.weapons.wo());
      this.axes = new BooleanValue("Axes", this, false, () -> (!this.weapons.wo() ? 1 : (103 ^ 74) + -45) != 0);
      this.extra = new BooleanValue("Extra", this, false, () -> {
         int var10000;
         if (!this.weapons.wo()) {
            var10000 = 1;
         } else {
            int var4x = 38;
            var4x ^= -115;
            var4x -= -85;
            var10000 = var4x;
         }

         return var10000 != 0;
      });
      this.sharpness = new BooleanValue("Sharpness", this, false, () -> (!this.weapons.wo() ? 1 : -13 - -13) != 0);
      this.knockback = new BooleanValue("Knockback", this, false, () -> !this.weapons.wo());
      this.fireAspect = new BooleanValue("Fire aspect", this, false, () -> !this.weapons.wo());
      this.heldPackets = new ConcurrentLinkedQueue<>();
      this.attackTimer = new StopWatch();
      this.swingTimer = new StopWatch();
      this.shortBlockCycle = false;
      this.random = new Random();
      this.oe = 100;
      this.of = 0;
      this.lastTargetId = Integer.MIN_VALUE;
      this.targetMotionAverage = new Vector3d(0.0, 0.0, 0.0);
      this.playerMotionAverage = new Vector3d(0.0, 0.0, 0.0);
      this.triggerEntityId = Integer.MIN_VALUE;
      this.lastSlotFlickTick = -1;
      this.blockSlot = -1;
      this.lastBlockAttackTick = -1;
      this.rightClickTick = -1;
      this.switchHistory = new EvictingList<>(9);
      this.lastHitTicks = new HashMap<>();
      this.espTargets = new ArrayList<>();
      this.fovCircleRadius = new Animation(Easing.EASE_OUT_CUBIC, 300L);
      this.fovCircleYaw = new Animation(Easing.EASE_OUT_CUBIC, 250L);
      this.onPreMotionEvent = var1x -> {
         Object var136 = null;
         Object var144 = null;
         Object var151 = null;
         Object var160 = null;
         int nextInt2;
         Object var168 = null;
         if (!this.rightClickHandled) {
            label122: {
               this.heldPackets.forEach(PacketUtil::sendNoEvent);
               this.heldPackets.clear();
               this.ticksSinceAttack++;
               SlotComponent var10000 = this.d(SlotComponent.class);
               if (SlotComponent.getItemStack() != null) {
                  var10000 = this.d(SlotComponent.class);
                  if (SlotComponent.getItemStack().getItem() instanceof ItemSword) {
                     break label122;
                  }
               }

               blocking = false;
            }

            if (!GUIDetectionComponent.inGUI()) {
               if (this.jE == null || aEg.thePlayer.isDead || this.e(Scaffold.class).isEnabled()) {
                  if (this.isDualSword()) {
                     this.releaseBlock(true);
                  } else if (!this.lV.wo().getName().equals("Watchdog 1.12")) {
                     if (this.lV.wo().getName().equals("Watchdog") && !this.fakeBlocked && blocking && !SlotComponent.dj) {
                        if (this.flickSlot()) {
                           this.fakeBlocked = true;
                        }
                     } else if (!BadPacketsComponent.aW()) {
                        this.unblock(false);
                     }
                  } else if (!BadPacketsComponent.aW()) {
                     int bCP2 = aEg.playerController.bCP;

                     do {
                        nextInt2 = ThreadLocalRandom.current().nextInt(8);
                     } while (bCP2 == nextInt2);

                     if (blocking && !SlotComponent.dj) {
                        aEg.getNetHandler().addToSendQueue(new l(nextInt2));
                        aEg.playerController.bCP = nextInt2;
                        aEg.getNetHandler().addToSendQueue(new l(bCP2));
                        aEg.playerController.bCP = bCP2;
                        blocking = false;
                     }
                  }

                  this.jE = null;
               }

               if (this.jE != null) {
                  if (!this.espMode.wo().getName().equals("None")) {
                     this.updateTargets();
                     List var2;
                     if (this.mode.wo().getName().equals("Single")) {
                        var2 = new ArrayList();
                        if (this.jE != null) {
                           var2.add(this.jE);
                        }
                     } else {
                        var2 = this.targets;
                     }

                     var168 = var2.iterator();

                     while (((Iterator)var168).hasNext()) {
                        var136 = (EntityLivingBase)((Iterator)var168).next();
                        var144 = new com.alan.clients.component.impl.render.espcomponent.api.ESPColor(this.rz().rA(), this.rz().rB(), this.rz().rA());
                        if (!this.coloredSigmaRing.wo()) {
                           var144 = new com.alan.clients.component.impl.render.espcomponent.api.ESPColor(Color.WHITE, Color.WHITE, Color.WHITE);
                        }

                        var151 = this.espMode.wo().getName();
                        int var117_hi = -1;
                        switch (((String)var151).hashCode()) {
                           case 66987:
                              if (((String)var151).equals("Box")) {
                                 var117_hi = 1;
                              }
                              break;
                           case 2547280:
                              if (((String)var151).equals("Ring")) {
                                 var117_hi = 0;
                              }
                        }

                        switch (var117_hi) {
                           case 0:
                              ESPComponent.add(new SigmaRing((EntityLivingBase)var136, (com.alan.clients.component.impl.render.espcomponent.api.ESPColor)var144));
                              break;
                           case 1:
                              var160 = this.boxMode.wo().getName();
                              int var152_hi = -1;
                              switch (((String)var160).hashCode()) {
                                 case 2201263:
                                    if (((String)var160).equals("Full")) {
                                       var152_hi = 0;
                                    }
                                    break;
                                 case 63058813:
                                    if (((String)var160).equals("Above")) {
                                       var152_hi = 1;
                                    }
                              }

                              switch (var152_hi) {
                                 case 0:
                                    ESPComponent.add(new FullBox((EntityLivingBase)var136, (com.alan.clients.component.impl.render.espcomponent.api.ESPColor)var144));
                                    break;
                                 case 1:
                                    ESPComponent.add(new AboveBox((EntityLivingBase)var136, (com.alan.clients.component.impl.render.espcomponent.api.ESPColor)var144));
                              }
                        }
                     }
                  }
               }
            }
         }
      };
      this.onWorldChange = var1x -> {
         this.blockSlot = -1;
         this.lastBlockAttackTick = -1;
         this.rightClickTick = -1;
         this.rightClickHandled = false;
         this.resetAdvancedState();
         if (this.autoDisable.wo()) {
            this.toggle();
         }
      };
      this.onPreUpdateHigh = var1x -> {
         Object var220 = null;
         Object var221 = null;
         Object var249 = null;
         this.blockQueued = false;
         this.rightClickHandled = false;
         if (!this.handleRightClickBlock()) {
            if (this.pendingDisable) {
               this.applyPendingDisable();
            } else {
               this.updateWatchdogFallback();
               this.warnMissingSecondSword();
               if (aEg.thePlayer.isSprinting() || this.jE == null || !this.newUniversalKeepSprint.wo()) {
                  this.sprintCancelled = false;
               }

               if (!RotationComponent.bK()) {
                  if (this.attackPending > 0) {
                     this.attackPending--;
                  }

                  attacking = this.attackPending > 0;
                  if (this.lV.wo().getName().equals("Watchdog 1.12")
                     && !ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13)
                     && LastConnectionComponent.ip != null
                     && LastConnectionComponent.ip.contains("hypixel")
                     && aEg.thePlayer.ticksExisted % 5 == 0) {
                     ChatUtil.b("USE THIS AUTOBLOCK CONFIG ON 1.20 NOT 1.8 instead use Watchdog 1.8 Autoblock on 1.8");
                  }

                  if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13)
                     && LastConnectionComponent.ip != null
                     && LastConnectionComponent.ip.contains("hypixel")
                     && aEg.thePlayer.ticksExisted % 5 == 0
                     && this.lV.wo().getName().equals("Watchdog 1.8")) {
                     ChatUtil.b("USE THIS AUTOBLOCK CONFIG ON 1.8 NOT 1.20 instead use Watchdog 1.12 Autoblock on 1.20");
                  }

                  if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13)
                     && LastConnectionComponent.ip != null
                     && LastConnectionComponent.ip.contains("hypixel")
                     && aEg.thePlayer.ticksExisted % 5 == 0
                     && this.lV.wo().getName().equals("Watchdog")) {
                     ChatUtil.b("USE THIS AUTOBLOCK CONFIG ON 1.8 NOT 1.20 instead use Watchdog 1.12 Autoblock on 1.20");
                  }

                  if (this.rotationMode.wo().getName().equals("Grim") && aEg.thePlayer.ticksExisted % 20 == 0) {
                     int newerThan2 = ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17) && !ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_20_5) ? 1 : 0;
                     if (newerThan2 == 0) {
                        ChatUtil.b("OnTick rotation only works correctly on versions 1.17-1.20.6. Please switch to a version in that range.");
                     }
                  }

                  aEg.entityRenderer.getMouseOver(1.0F);
                  canAttack = !BadPacketsComponent.bad(false, false, false, true, true);
                  if (aEg.thePlayer.getHealth() <= 0.0 && this.autoDisable.wo()) {
                     this.toggle();
                  }

                  if (!this.e(Scaffold.class).isEnabled() || this.attackWhilstScaffolding.wo()) {
                     if (Breaker.breakingBed && this.e(Breaker.class).isEnabled() && !this.e(Breaker.class).attackWhileBreaking.wo()) {
                        this.jE = null;
                        this.updateBlockForMode();
                     } else if (!this.isWeaponAllowed()) {
                        this.jE = null;
                        this.updateBlockForMode();
                     } else {
                        this.attack = Math.max(Math.min(this.attack, this.attack - 2), 0);
                        var220 = this.e(Manager.class);
                        var249 = this.e(OldManager.class);
                        var221 = this.e(Stealer.class);
                        int jJ2 = (Manager)var220 != null && ((Manager)var220).isEnabled() && ((Manager)var220).jJ() ? 1 : 0;
                        int jJ3 = (OldManager)var249 != null && ((OldManager)var249).isEnabled() && ((OldManager)var249).isSorting() ? 1 : 0;
                        int enabled = (Stealer)var221 != null && ((Stealer)var221).isEnabled() && aEg.currentScreen instanceof GuiChest ? 1 : 0;
                        int notFlag = !GUIDetectionComponent.inGUI() && !aEg.gameSettings.cgI.isKeyDown() && !BadPacketsComponent.a(true, false, false, false, true, false) && jJ3 == 0 && enabled == 0 ? 0 : 1;
                        if (aEg.thePlayer.ticksExisted % 20 == 0 && !this.lV.wo().getName().equals("Watchdog 1.8")) {
                           this.expandRange = (int)(this.rotationRange.wo().doubleValue() + Math.random() * 0.5);
                        }

                        if (aEg.thePlayer.ticksExisted % 2 == 0 && this.lV.wo().getName().equals("Watchdog 1.8") && notFlag == 0) {
                           this.expandRange = (int)(5.0 + Math.random() * 0.5);
                        }

                        if (notFlag != 0 && this.lV.wo().getName().equals("Watchdog 1.8")) {
                           this.expandRange = (int)(this.rotationRange.wo().doubleValue() + Math.random() * 0.5);
                        }

                        if (!GUIDetectionComponent.inGUI()) {
                           this.updateTargets();
                           if (this.targets.isEmpty()) {
                              this.jE = null;
                              this.resetAdvancedState();
                              this.updateBlockForMode();
                           } else {
                              this.jE = this.targets.get(0);
                              if (this.jE != null && !aEg.thePlayer.isDead) {
                                 if (this.eX()) {
                                    this.updateAutoBlock();
                                 } else {
                                    this.updateBlockForMode();
                                 }

                                 this.updateKnockbackPlan();
                                 this.updateRotations();
                                 this.espTargets = this.lastHitTicks
                                    .entrySet()
                                    .stream()
                                    .filter(var0 -> aEg.thePlayer.ticksExisted - var0.getValue() <= 5)
                                    .map(Entry::getKey)
                                    .map(EntityLivingBase.class::cast)
                                    .collect(Collectors.toList());
                                 if (this.mode.wo().getName().equals("Single") && this.jE != null) {
                                    this.espTargets.clear();
                                    this.espTargets.add(this.jE);
                                 }
                              } else {
                                 this.resetAdvancedState();
                                 this.updateBlockForMode();
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      };
      this.onPreUpdate = var1x -> {
         Object var35 = null;
         if (!this.pendingDisable && !this.rightClickHandled) {
            if (this.jE != null && !aEg.thePlayer.isDead) {
               if (this.isAttackReady()) {
                  this.attackPending = 2;
               }

               var35 = this.lV.wo().getName();
               int var29_hi = -1;
               switch (((String)var35).hashCode()) {
                  case -1336727224:
                     if (((String)var35).equals("Watchdog 1.8")) {
                        var29_hi = 0;
                     }
                  default:
                     switch (var29_hi) {
                        case 0:
                        default:
                           this.doAttack(this.targets);
                           if (this.eX()) {
                              this.postAttackBlock();
                           }
                     }
               }
            }
         }
      };
      this.onMouseOver = var1x -> var1x.setRange(var1x.dA() + this.range.wo().doubleValue() - 3.0);
      this.onPostMotion = var1x -> {
         if (!this.rightClickHandled) {
            if (this.jE != null && this.eX()) {
               this.postMotionBlock();
            }
         }
      };
      this.onRenderItem = var1x -> {
         if (this.jE != null && !this.lV.wo().getName().equals("None") && this.eX()) {
            var1x.setEnumAction(EnumAction.BLOCK);
            var1x.setUseItem(true);
         }
      };
      this.hV = 0;
      this.onPacketSend = var1x -> {
         Object var16 = null;
         if (!var1x.isCancelled()) {
            var16 = var1x.dq();
            if ((Packet)var16 instanceof m) {
               blockPacketSent = true;
            } else if ((Packet)var16 instanceof C03PacketPlayer) {
               blockPacketSent = false;
            }

            this.packetBlock(var1x);
         }
      };
      this.onAutoBlock = var1x -> {
         Object var41 = null;
         if (this.jE != null) {
            SlotComponent var10000 = this.d(SlotComponent.class);
            if (SlotComponent.getItemStack() != null) {
               var10000 = this.d(SlotComponent.class);
               if (SlotComponent.getItemStack().getItem() instanceof ItemSword) {
                  var41 = this.lV.wo().getName();
                  int var39_hi = -1;
                  switch (((String)var41).hashCode()) {
                     case 2182005:
                        if (((String)var41).equals("Fake")) {
                           var39_hi = 0;
                        }
                        break;
                     case 2433880:
                        if (((String)var41).equals("None")) {
                           var39_hi = 1;
                        }
                        break;
                     case 73298841:
                        if (((String)var41).equals("Legit")) {
                           var39_hi = 2;
                        }
                  }

                  switch (var39_hi) {
                     case 0:
                     case 1:
                        if (!this.preventServersideBlocking.wo()) {
                           return;
                        }

                        var10000 = this.d(SlotComponent.class);
                        if (SlotComponent.getItemStack() == null) {
                           return;
                        }

                        var10000 = this.d(SlotComponent.class);
                        if (!(SlotComponent.getItemStack().getItem() instanceof ItemSword)) {
                           return;
                        }

                        var1x.setCancelled();
                     case 2:
                        break;
                     default:
                        var1x.setCancelled();
                  }

                  return;
               }
            }
         }
      };
      this.onClick = var1x -> {
         if (this.lV.wo().getName().equals("Watchdog") && this.jE != null && this.blockStage == 2) {
            var1x.setCancelled();
         }
      };
      this.onJump = var1x -> {
         if (this.newUniversalKeepSprint.wo() && this.sprintCancelled && !aEg.thePlayer.isSprinting()) {
            var1x.setCancelled();
         } else {
            if (!this.lV.wo().getName().equals("Watchdog") || this.rightClickOnly.wo() && !aEg.gameSettings.cgI.isKeyDown()) {
               if (this.oldPredictionKeepSprint.wo() && aEg.thePlayer.ticksExisted % 2 == 0 && attacking && this.jE != null && PlayerUtil.v(this.jE) <= 3.0 + MoveUtil.speed()) {
                  var1x.setCancelled();
               }
            } else if (!this.newYouNeedThisToggledOnCurreFake.wo() && this.oldPredictionKeepSprint.wo() && this.blockStage == 2 && attacking && this.jE != null && PlayerUtil.v(this.jE) <= 3.0 + MoveUtil.speed()) {
               var1x.setCancelled();
            }
         }
      };
      this.onHitSlowDown = var1x -> {
         if (this.lV.wo().getName().equals("Watchdog")
            && this.jE != null
            && PlayerUtil.v(this.jE) <= 3.0 + MoveUtil.speed()
            && this.oldPredictionKeepSprint.wo()
            && this.newYouNeedThisToggledOnCurreFake.wo()
            && aEg.thePlayer.ae >= 7) {
            var1x.setSlowDown(1.0);
         }
      };
      this.onKeepSprintCancel = var1x -> {
         if (this.lV.wo().getName().equals("Watchdog")
            && this.jE != null
            && PlayerUtil.v(this.jE) <= 3.0 + MoveUtil.speed()
            && this.oldPredictionKeepSprint.wo()
            && this.newYouNeedThisToggledOnCurreFake.wo()
            && aEg.thePlayer.ae > 7) {
            aEg.thePlayer.setSprinting(false);
         }

         if (!this.lV.wo().getName().equals("Watchdog") || this.rightClickOnly.wo() && !aEg.gameSettings.cgI.isKeyDown()) {
            if (this.oldPredictionKeepSprint.wo()
               && aEg.thePlayer.ticksExisted % 2 == 0
               && attacking
               && this.jE != null
               && PlayerUtil.v(this.jE) <= 3.0 + MoveUtil.speed()
               && (aEg.thePlayer.ae >= 7 || this.isGrimSpeedActive())) {
               aEg.thePlayer.setSprinting(false);
            }
         } else if (!this.newYouNeedThisToggledOnCurreFake.wo()
            && this.oldPredictionKeepSprint.wo()
            && this.blockStage > 1
            && attacking
            && this.jE != null
            && PlayerUtil.v(this.jE) <= 3.0 + MoveUtil.speed()
            && (aEg.thePlayer.ae >= 7 || this.isGrimSpeedActive())) {
            aEg.thePlayer.setSprinting(false);
         }
      };
      this.onSlowDown = var1x -> {
         Object var72 = null;
         var72 = this.lV.wo().getName();
         int var66_hi = -1;
         switch (((String)var72).hashCode()) {
            case -1885322919:
               if (((String)var72).equals("Dual Sword")) {
                  var66_hi = 2;
               }
               break;
            case -1336727224:
               if (((String)var72).equals("Watchdog 1.8")) {
                  var66_hi = 3;
               }
               break;
            case 73298841:
               if (((String)var72).equals("Legit")) {
                  var66_hi = 0;
               }
               break;
            case 609795629:
               if (((String)var72).equals("Watchdog")) {
                  var66_hi = 1;
               }
               break;
            case 1511128849:
               if (((String)var72).equals("Watchdog 1.12")) {
                  var66_hi = 4;
               }
         }

         switch (var66_hi) {
            case 0:
            default:
               break;
            case 1:
               if (this.jE != null && aEg.thePlayer.getHeldItem() != null && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                  var1x.setCancelled();
               }
               break;
            case 2:
               if (this.jE != null && aEg.thePlayer.getHeldItem() != null && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                  var1x.setCancelled();
               }
               break;
            case 3:
               if (this.jE != null && aEg.thePlayer.getHeldItem() != null && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                  var1x.setCancelled();
               }
               break;
            case 4:
               if (this.jE != null && aEg.thePlayer.getHeldItem() != null && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                  var1x.setCancelled();
               }
         }

         if (this.blockSlowdown.wo() && blocking && this.eX()) {
            var1x.setCancelled(false);
            var1x.k(true);
         }
      };
      this.onRender2D = var1x -> {
         float var121 = 0.0F;
         double var122 = 0.0;
         Object var126 = null;
         Object var127 = null;
         double var132 = 0.0;
         float var135 = 0.0F;
         Object var138 = null;
         Object var140 = null;
         Object var145 = null;
         Object var146 = null;
         float var147 = 0.0F;
         float var149 = 0.0F;
         double var150 = 0.0;
         Object var152 = null;
         float var157 = 0.0F;
         float var161 = 0.0F;
         double var163 = 0.0;
         double var165 = 0.0;
         double var168 = 0.0;
         Object var171 = null;
         double var172 = 0.0;
         float var176 = 0.0F;
         double var179 = 0.0;
         double var181 = 0.0;
         double var183 = 0.0;
         double var186 = 0.0;
         Object var189 = null;
         if (this.showFOVCircle.wo() && !(this.fOV.wo().doubleValue() >= 360.0) || this.showMovementArc.wo()) {
            var181 = this.fOV.wo().doubleValue();
            int scaledWidth = var1x.getScaledResolution().getScaledWidth();
            int scaledHeight = var1x.getScaledResolution().getScaledHeight();
            var149 = scaledWidth / 2.0F;
            var121 = scaledHeight / 2.0F;
            var127 = this.jE;
            var183 = 0.0;
            int var143_hi = 0;
            if ((EntityLivingBase)var127 == null) {
               var145 = Double.MAX_VALUE;
               var152 = this.range.wo().doubleValue() + 3.0;
               var189 = aEg.theWorld.loadedEntityList.iterator();

               while (((Iterator)var189).hasNext()) {
                  var126 = (Entity)((Iterator)var189).next();
                  if ((Entity)var126 instanceof EntityLivingBase && (Entity)var126 != aEg.thePlayer) {
                     var138 = (EntityLivingBase)((Entity)var126);
                     if (!((EntityLivingBase)var138).isDead && (!((EntityLivingBase)var138).isInvisible() || this.invisibles.wo())) {
                        var140 = (double)aEg.thePlayer.getDistanceToEntity((EntityLivingBase)var138);
                        if ((Double)var140 < (Double)var145 && (Double)var140 <= (Double)var152) {
                           var145 = (Double)var140;
                           var127 = (EntityLivingBase)var138;
                        }
                     }
                  }
               }
            }

            if ((EntityLivingBase)var127 != null) {
               var145 = RotationUtil.y((EntityLivingBase)var127);
               var157 = MathHelper.wrapAngleTo180_float(((Vector2f)var145).x - aEg.thePlayer.pl);
               var183 = var157;
               if (this.jE != null && !this.targets.isEmpty()) {
                  if (var181 >= 360.0) {
                     var143_hi = 1;
                  } else {
                     var143_hi = Math.abs(var157) <= var181 / 2.0 ? 1 : 0;
                  }
               }
            }

            var145 = var143_hi != 0 ? 16.0 : 10.0;
            this.fovCircleRadius.Q((Double)var145);
            var152 = (float)this.fovCircleRadius.getValue();
            var163 = (EntityLivingBase)var127 != null ? var183 : 0.0;
            this.fovCircleYaw.Q(var163);
            var126 = this.fovCircleYaw.getValue();
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GL11.glLineWidth(1.5F);
            if (this.showFOVCircle.wo() && var181 < 360.0) {
               var140 = var181 / 2.0;
               var186 = 270.0 + (Double)var126;
               var132 = var186 - (Double)var140;
               var179 = var186 + (Double)var140;
               GL11.glBegin(3);
               var146 = this.rz().rA();
               var171 = var143_hi != 0 ? 1.0F : 0.5F;
               GL11.glColor4f(((Color)var146).getRed() / 255.0F, ((Color)var146).getGreen() / 255.0F, ((Color)var146).getBlue() / 255.0F, (Float)var171);

               for (double var217 = var132; var217 <= var179; var217 += 2.0) {
                  var172 = Math.toRadians(var217);
                  var176 = var149 + (float)(Math.cos(var172) * ((Float)var152).floatValue());
                  var161 = var121 + (float)(Math.sin(var172) * ((Float)var152).floatValue());
                  GL11.glVertex2f(var176, var161);
               }

               GL11.glEnd();
            }

            if (this.showMovementArc.wo() && this.movementCorrection.wo() != MovementFix.OFF && this.jE != null && RotationComponent.fk != null) {
               var140 = RotationComponent.fk.x;
               var150 = MathHelper.wrapAngleTo180_float((Float)var140 - aEg.thePlayer.pl);
               var122 = 270.0 + var150 - 45.0;
               var165 = 270.0 + var150 + 45.0;
               var147 = (Float)var152 * 0.75F;
               GL11.glBegin(3);
               var146 = this.rz().rB();
               GL11.glColor4f(((Color)var146).getRed() / 255.0F, ((Color)var146).getGreen() / 255.0F, ((Color)var146).getBlue() / 255.0F, 0.6F);

               for (Double var223 = var122; var223 <= var165; var223 = var223 + 2.0) {
                  var168 = Math.toRadians(var223);
                  var135 = var149 + (float)(Math.cos(var168) * var147);
                  var176 = var121 + (float)(Math.sin(var168) * var147);
                  GL11.glVertex2f(var135, var176);
               }

               GL11.glEnd();
            }

            GlStateManager.disableBlend();
            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glPopMatrix();
            GlStateManager.resetColor();
         }
      };
      MovementFix[] movementFixes = MovementFix.values();
      var822 = movementFixes.length;

      for (int i2 = 0; i2 < var822; i2++) {
         MovementFix movementFix = movementFixes[i2];
         this.movementCorrection.add(movementFix);
      }

      this.movementCorrection.setDefault(MovementFix.OFF);
   }

   public boolean isOnSwordSlot() {
      Object var55 = null;
      var55 = this.findSwordSlots();
      if (((int[])var55)[1] == -1) {
         return false;
      }
      if (this.blockSlot != ((int[])var55)[0] && this.blockSlot != ((int[])var55)[1]) {
         SlotComponent var10000 = this.d(SlotComponent.class);
         int bQ2 = SlotComponent.bQ();
         if (bQ2 != ((int[])var55)[0] && bQ2 != ((int[])var55)[1]) {
            return false;
         }

         this.blockSlot = bQ2;
      }

      SlotComponent var59 = this.d(SlotComponent.class);
      if (SlotComponent.bQ() != this.blockSlot) {
         SlotComponent.b(this.blockSlot, false);
      }

      return true;
   }

   public float computePitch(EntityLivingBase living, float var2) {
      double var23 = 0.0;
      Object var25 = null;
      double var26 = 0.0;
      double var28 = 0.0;
      Object var30 = null;
      double var31 = 0.0;
      double var33 = 0.0;
      double var35 = 0.0;
      if (living == null) {
         return aEg.thePlayer.rotationPitch;
      }
      var25 = aEg.thePlayer.getPositionEyes(1.0F);
      var30 = new Vec3(living.posX, living.posY + living.getEyeHeight() * 0.9, living.posZ);
      var28 = ((Vec3)var30).xCoord - ((Vec3)var25).xCoord;
      var33 = ((Vec3)var30).yCoord - ((Vec3)var25).yCoord;
      var35 = ((Vec3)var30).zCoord - ((Vec3)var25).zCoord;
      var26 = Math.toRadians(var2);
      var31 = -var28 * Math.sin(var26) + var35 * Math.cos(var26);
      var23 = Math.toDegrees(Math.atan2(-var33, var31));
      var23 = Math.max(-90.0, Math.min(90.0, var23));
      return (float)var23;
   }

   public long jitteredDelay(double var1, double var3, long var5, long var7) {
      double var13 = 0.0;
      var13 = var1;
      if (var3 > 1.0E-4) {
         var13 = var1 + this.random.nextGaussian() * var3;
      }

      if (var13 < var5) {
         var13 = var5;
      }

      if (var13 > var7) {
         var13 = var7;
      }

      return Math.round(var13);
   }

   public void b(KnockbackSample var1) {
      if (var1 != null && aEg.thePlayer != null) {
         this.getServerPitch();
         float var10000 = aEg.thePlayer.pl + MathHelper.wrapAngleTo180_float(var1.yaw - aEg.thePlayer.pl);
      }
   }

   public void planOvershoot(Vector2f vec2, Vector2f var2) {
      float var35 = 0.0F;
      float var36 = 0.0F;
      float var37 = 0.0F;
      float var38 = 0.0F;
      float var39 = 0.0F;
      float var40 = 0.0F;
      float var41 = 0.0F;
      float var42 = 0.0F;
      float var45 = 0.0F;
      float var47 = 0.0F;
      float var49 = 0.0F;
      float var50 = 0.0F;
      float var51 = 0.0F;
      float var52 = 0.0F;
      float var53 = 0.0F;
      float var54 = 0.0F;
      var37 = this.advancedOvershootChance.wo().floatValue();
      if (!(var37 <= 0.0F)) {
         var52 = MathHelper.clamp_float(this.advancedAccuracy.wo().floatValue() / 100.0F, 0.4F, 1.0F);
         var40 = var37 * (1.15F - var52 * 0.35F);
         var51 = MathHelper.wrapAngleTo180_float(var2.x - vec2.x);
         var39 = var2.y - vec2.y;
         var42 = (float)Math.hypot(var51, var39);
         if (!(var42 < 9.0F) && !(this.random.nextFloat() * 100.0F > var40)) {
            var45 = this.advancedOvershootScale.wo().floatValue();
            var54 = Math.min(this.advancedOvershootMax.wo().floatValue(), Math.max(1.5F, var42 * var45));
            var54 *= 0.9F + (1.0F - var52) * 0.35F;
            var47 = Math.max(0.001F, var42);
            var49 = var51 / var47;
            var36 = var39 / var47;
            var53 = -var36;
            var50 = var54 + Math.abs(this.b(var54 * 0.25F, var54));
            var41 = this.b(var54 * 0.35F, var54);
            var38 = var2.x + var49 * var50 + var53 * var41;
            var35 = MathHelper.clamp_float(var2.y + var36 * var50 + var49 * var41 * 0.65F, -89.0F, 89.0F);
            this.on = new Vector2f(var38, var35);
            this.overshootTicks = 8 + this.random.nextInt(6);
         }
      }
   }

   public boolean ey() {
      return false;
   }

   public boolean flickSlot() {
      if (aEg.thePlayer == null || SlotComponent.dj) {
         return false;
      } else if (this.lastSlotFlickTick == aEg.thePlayer.ticksExisted) {
         return false;
      }
      SlotComponent var10000 = this.d(SlotComponent.class);
      int bQ2 = SlotComponent.bQ();
      int nextInt2 = ThreadLocalRandom.current().nextInt(9);

      while (nextInt2 == bQ2) {
         nextInt2 = ThreadLocalRandom.current().nextInt(9);
      }

      BlinkComponent.dispatch();
      if (ServerUtil.vn()) {
         if (!this.blockSlowdown.wo()) {
            SlotComponent.setSlot(nextInt2);
            SlotComponent.setSlot(bQ2);
         }

         this.unblock(true);
      } else {
         this.unblock(true);
         if (!this.blockSlowdown.wo()) {
            SlotComponent.setSlot(nextInt2);
            SlotComponent.setSlot(bQ2);
         }
      }

      this.lastSlotFlickTick = aEg.thePlayer.ticksExisted;
      return true;
   }

   public boolean usesWatchdogBlock() {
      return this.lV.wo().getName().equals("Watchdog") || this.isDualSword();
   }

   public void updateBlockForMode() {
      Object var50 = null;
      var50 = this.lV.wo().getName();
      int var43_hi = -1;
      switch (((String)var50).hashCode()) {
         case -1885322919:
            if (((String)var50).equals("Dual Sword")) {
               var43_hi = 2;
            }
            break;
         case 609795629:
            if (((String)var50).equals("Watchdog")) {
               var43_hi = 1;
            }
            break;
         case 1594433067:
            if (((String)var50).equals("Universal")) {
               var43_hi = 0;
            }
      }

      switch (var43_hi) {
         case 0:
            this.blockStage = -1;
         case 1:
         default:
            break;
         case 2:
            this.releaseBlock(true);
      }
   }

   public void b(MovingObjectPosition hit) {
      if (hit != null && hit.typeOfHit == MovingObjectType.ENTITY) {
         aEg.objectMouseOver = hit;
         aEg.pointedEntity = hit.entityHit;
      }
   }

   public double findSurfaceBelow(AxisAlignedBB box, int var2) {
      Object var125 = null;
      Object var133 = null;
      double var140 = 0.0;
      Object var185 = null;
      int floor_double2 = MathHelper.floor_double(box.minX + 1.0E-4);
      int floor_double3 = MathHelper.floor_double(box.maxX - 1.0E-4);
      int floor_double4 = MathHelper.floor_double(box.minZ + 1.0E-4);
      int floor_double5 = MathHelper.floor_double(box.maxZ - 1.0E-4);
      int floor_double6 = MathHelper.floor_double(box.minY) - 1;
      int max2 = Math.max(0, floor_double6 - var2);
      double var11 = Double.longBitsToDouble(9218868437227405312L);

      for (int i = floor_double2; i <= floor_double3; i++) {
         for (int j = floor_double4; j <= floor_double5; j++) {
            int var154_lo = 0;

            for (int k = floor_double6; k >= max2; k--) {
               var185 = new BlockPos(i, k, j);
               var133 = aEg.theWorld.getBlockState((BlockPos)var185);
               var125 = ((IBlockState)var133).getBlock().getCollisionBoundingBox(aEg.theWorld, (BlockPos)var185, (IBlockState)var133);
               if ((AxisAlignedBB)var125 != null) {
                  var140 = Math.max(0.0, box.minY - ((AxisAlignedBB)var125).maxY);
                  var11 = Math.min(var11, var140);
                  var154_lo = 1;
                  break;
               }
            }

            if (var154_lo == 0) {
               return Double.longBitsToDouble(6568169346052289630L ^ -1957144748560059298L);
            }
         }
      }

      return var11 == Double.longBitsToDouble(9218868437227405312L) ? -1.0 : var11;
   }

   public boolean stopSprinting() {
      if (aEg.thePlayer.cqL == 1) {
         return true;
      } else if (aEg.thePlayer.isSprinting()) {
         aEg.thePlayer.setSprinting(false);
         aEg.gameSettings.cgG.setPressed(false);
         this.sprintCancelled = true;
         return true;
      }
      return false;
   }

   public boolean isGrimVelocityActive() {
      Object var15 = null;
      var15 = this.e(Velocity.class);
      return (Velocity)var15 != null && ((Velocity)var15).isEnabled() && ((Velocity)var15).mode.wo().getName().equals("Grim") && GrimVelocity.rotating;
   }

   public void updateAutoBlock() {
      double var464 = 0.0;
      int nextInt2;
      Object var486 = null;
      var486 = this.lV.wo().getName();
      int var441_hi = -1;
      switch (((String)var486).hashCode()) {
         case -2099899231:
            if (((String)var486).equals("Intave")) {
               var441_hi = 2;
            }
            break;
         case -1885322919:
            if (((String)var486).equals("Dual Sword")) {
               var441_hi = 9;
            }
            break;
         case -1558462246:
            if (((String)var486).equals("Old Intave")) {
               var441_hi = 6;
            }
            break;
         case -1336727224:
            if (((String)var486).equals("Watchdog 1.8")) {
               var441_hi = 10;
            }
            break;
         case -786683237:
            if (((String)var486).equals("New NCP")) {
               var441_hi = 5;
            }
            break;
         case 77115:
            if (((String)var486).equals("NCP")) {
               var441_hi = 1;
            }
            break;
         case 2228079:
            if (((String)var486).equals("Grim")) {
               var441_hi = 3;
            }
            break;
         case 73298841:
            if (((String)var486).equals("Legit")) {
               var441_hi = 0;
            }
            break;
         case 609795629:
            if (((String)var486).equals("Watchdog")) {
               var441_hi = 8;
            }
            break;
         case 1511128849:
            if (((String)var486).equals("Watchdog 1.12")) {
               var441_hi = 4;
            }
            break;
         case 1594433067:
            if (((String)var486).equals("Universal")) {
               var441_hi = 7;
            }
      }

      switch (var441_hi) {
         case 0:
            var464 = PlayerUtil.v(this.jE);
            aEg.gameSettings.cgI.setPressed(var464 < 3.0 && this.ticksSinceAttack <= 5 && aEg.thePlayer.ae >= 5);
            this.blockStage++;
            if (aEg.gameSettings.cgI.isPressed() || aEg.thePlayer.isUsingItem()) {
               this.blockStage = 0;
            }

            canAttack = this.blockStage >= 2;
            break;
         case 1:
         case 2:
            canAttack = true;
            break;
         case 3:
            SlotComponent var536 = this.d(SlotComponent.class);
            PacketUtil.send(new l(SlotComponent.bQ() % 8 + 1));
            var536 = this.d(SlotComponent.class);
            PacketUtil.send(new l(SlotComponent.bQ()));
            this.block(false, false);
            break;
         case 4:
            int bCP2 = aEg.playerController.bCP;

            do {
               nextInt2 = ThreadLocalRandom.current().nextInt(8);
            } while (bCP2 == nextInt2);

            if (blocking && !SlotComponent.dj) {
               aEg.getNetHandler().addToSendQueue(new l(nextInt2));
               aEg.playerController.bCP = nextInt2;
               aEg.getNetHandler().addToSendQueue(new l(bCP2));
               aEg.playerController.bCP = bCP2;
               blocking = false;
            }

            if (!BadPacketsComponent.a(false, false, false, false, true, true)) {
               canAttack = true;
            } else {
               canAttack = false;
            }
            break;
         case 5:
            if (blocking) {
               SlotComponent var534 = this.d(SlotComponent.class);
               PacketUtil.send(new l(SlotComponent.bQ() % 8 + 1));
               var534 = this.d(SlotComponent.class);
               PacketUtil.send(new l(SlotComponent.bQ()));
               blocking = false;
            }
            break;
         case 6:
            if (aEg.thePlayer.isUsingItem()) {
               SlotComponent var10002 = this.d(SlotComponent.class);
               PacketUtil.send(new l(SlotComponent.bQ() % 8 + 1));
               var10002 = this.d(SlotComponent.class);
               PacketUtil.send(new l(SlotComponent.bQ()));
            }
            break;
         case 7:
            if (aEg.playerController.curBlockDamageMP != 0.0F && aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
               this.blockStage = 0;
               return;
            }

            this.blockStage++;
            if (this.blockStage > 5) {
               this.blockStage = 2;
            }

            BlinkComponent.a(99999, false, false, false, false, true);
            switch (this.blockStage) {
               case 2:
                  this.block(false, true);
                  return;
               case 3:
                  this.unblock(false);
                  return;
               default:
                  return;
            }
         case 8:
            if (aEg.playerController.curBlockDamageMP != 0.0F && aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
               this.blockStage = 0;
            }

            this.blockStage++;
            if (this.blockStage >= (this.shortBlockCycle ? 3 : 4)) {
               this.blockStage = 1;
            }

            switch (this.blockStage) {
               case 1:
                  this.shortBlockCycle = !this.newYouNeedThisToggledOnCurreFake.wo();
                  if (!blocking) {
                     ;
                  }

                  SlotComponent var532 = this.d(SlotComponent.class);
                  int bQ2 = SlotComponent.bQ();
                  int nextInt3 = ThreadLocalRandom.current().nextInt(9);

                  while (nextInt3 == bQ2) {
                     nextInt3 = ThreadLocalRandom.current().nextInt(9);
                  }

                  if (ServerUtil.vn() && Math.random() > 0.5 && !this.blockSlowdown.wo() && !this.shortBlockCycle) {
                     SlotComponent.setSlot(nextInt3);
                     SlotComponent.setSlot(bQ2);
                     this.unblock(false);
                  }

                  BlinkComponent.blink();
                  return;
               case 2:
                  if (!ServerUtil.vn()) {
                     this.unblock(false);
                  }

                  if (!blocking) {
                     ;
                  }

                  if (this.shortBlockCycle) {
                     SlotComponent var531 = this.d(SlotComponent.class);
                     int bQ3 = SlotComponent.bQ();
                     int nextInt4 = ThreadLocalRandom.current().nextInt(9);

                     while (nextInt4 == bQ3) {
                        nextInt4 = ThreadLocalRandom.current().nextInt(9);
                     }

                     canAttack = false;
                     BlinkComponent.bf();
                     if (!this.blockSlowdown.wo()) {
                        SlotComponent.setSlot(nextInt4);
                        SlotComponent.setSlot(bQ3);
                     }

                     this.unblock(true);
                     this.fakeBlocked = true;
                  }

                  return;
               case 3:
                  if (!this.fakeBlocked && blocking) {
                     ;
                  }

                  if (!this.shortBlockCycle) {
                     SlotComponent var10000 = this.d(SlotComponent.class);
                     int bQ4 = SlotComponent.bQ();
                     int nextInt5 = ThreadLocalRandom.current().nextInt(9);

                     while (nextInt5 == bQ4) {
                        nextInt5 = ThreadLocalRandom.current().nextInt(9);
                     }

                     canAttack = false;
                     BlinkComponent.bf();
                     if (ServerUtil.vn()) {
                        if (!this.blockSlowdown.wo()) {
                           SlotComponent.setSlot(nextInt5);
                           SlotComponent.setSlot(bQ4);
                        }

                        this.unblock(true);
                     } else {
                        this.unblock(true);
                        if (!this.blockSlowdown.wo()) {
                           SlotComponent.setSlot(nextInt5);
                           SlotComponent.setSlot(bQ4);
                        }
                     }

                     this.fakeBlocked = true;
                  }

                  return;
               default:
                  return;
            }
         case 9:
            canAttack = this.isOnSwordSlot() && !BadPacketsComponent.bad(false, false, false, true, true) && !ServerUtil.vn() || this.isOnSwordSlot() && !BadPacketsComponent.bad(false, false, false, true, true) && Math.random() < 0.6;
            break;
         case 10:
            BlinkComponent.bf();
            this.block(false, true);
      }
   }

   public int[] findSwordSlots() {
      Object var61 = null;
      Object var68 = null;
      var68 = new int[]{-1, -1};
      if (aEg.thePlayer == null) {
         return (int[])var68;
      }
      int var69_hi = 0;

      for (int i = 0; i < 9 && var69_hi < ((int[])var68).length; i++) {
         var61 = aEg.thePlayer.inventory.getStackInSlot(i);
         if ((ItemStack)var61 != null && ((ItemStack)var61).getItem() instanceof ItemSword) {
            int[] var10000 = (int[])var68;
            int var10001 = var69_hi;
            var69_hi++;
            var10000[var10001] = i;
         }
      }

      return (int[])var68;
   }

   public boolean isAttackReady() {
      Object var36 = null;
      double var38 = 0.0;
      var36 = this.getAttackDelay();
      var38 = (Double)((Tuple)var36).getSecond();
      int booleanValue2 = (int)(((Boolean)((Tuple)var36).getFirst()).booleanValue() ? 1L : 0L);
      return this.attackTimer.T(this.attackInterval - 50L)
         && this.jE != null
         && (this.swingTimer.T((long)(var38 * 50.0) - 50L) || booleanValue2 != 0)
         && (!this.clickMode.wo().getName().equals("Hit Select") || this.jE.hurtTime <= PingSpoofComponent.getPing() / 50L - 1L || aEg.thePlayer.ae <= 11)
         && canAttack;
   }

   public void updateKnockbackPlan() {
      if (this.isDisplacementEnabled() && this.jE != null && this.hasKnockbackSource()) {
         this.knockbackPlan = this.sampleKnockback(this.jE);
      } else {
         this.knockbackPlan = null;
      }
   }

   public int getOtherSwordSlot() {
      Object var89 = null;
      var89 = this.findSwordSlots();
      if (((int[])var89)[1] == -1) {
         return -1;
      }
      int oB2 = this.blockSlot;
      if (oB2 != ((int[])var89)[0] && oB2 != ((int[])var89)[1]) {
         SlotComponent var10000 = this.d(SlotComponent.class);
         oB2 = SlotComponent.bQ();
      }

      if (oB2 != ((int[])var89)[0] && oB2 != ((int[])var89)[1]) {
         return -1;
      }
      return oB2 == ((int[])var89)[0] ? ((int[])var89)[1] : ((int[])var89)[0];
   }

   @Override
   public void onDisable() {
      int nextInt2;
      this.pendingDisable = false;
      this.rightClickTick = -1;
      this.rightClickHandled = false;
      this.fakeBlocked = false;
      this.heldPackets.forEach(PacketUtil::sendNoEvent);
      this.heldPackets.clear();
      PacketQueueComponent.dispatch();
      this.jE = null;
      this.knockbackPlan = null;
      this.lastDebugLine = "";
      this.lastDebugTick = -1;
      this.resetAdvancedState();
      if (this.lV.wo().getName().equals("Watchdog 1.8") && blocking) {
         ChatUtil.c("for Autoblock to work best keep Killaura enabled unless it's necessary to turn off");
      }

      if (this.isDualSword()) {
         this.releaseBlock(true);
      } else if (this.lV.wo().getName().equals("Watchdog 1.12")) {
         if (!BadPacketsComponent.aW()) {
            int bCP2 = aEg.playerController.bCP;

            do {
               nextInt2 = ThreadLocalRandom.current().nextInt(8);
            } while (bCP2 == nextInt2);

            if (blocking && !SlotComponent.dj) {
               aEg.getNetHandler().addToSendQueue(new l(nextInt2));
               aEg.playerController.bCP = nextInt2;
               aEg.getNetHandler().addToSendQueue(new l(bCP2));
               aEg.playerController.bCP = bCP2;
               blocking = false;
            }
         }
      } else if (this.lV.wo().getName().equals("Watchdog") && !this.fakeBlocked && !SlotComponent.dj && blocking) {
         this.flickSlot();
      } else if (!BadPacketsComponent.aW()) {
         this.unblock(false);
      }

      aEg.gameSettings.cgI.setPressed(false);
      PacketQueueComponent.cR = false;
      this.lastHitTicks.clear();
      this.espTargets.clear();
      if (this.watchdogFallbackActive) {
         this.lV.co("Dual Sword");
         this.watchdogFallbackActive = false;
      }
   }

   public boolean shouldUseKnockbackPlan(EntityLivingBase living, KnockbackSample var2) {
      if (living == null || var2 == null) {
         return false;
      } else if (this.h(living)) {
         return (-67 - 23 - -90) != 0;
      }
      return living.hurtTime > 0 ? true : var2.score >= 120.0 || aEg.thePlayer.getDistanceToEntity(living) <= 2.6F;
   }

   public boolean h(EntityLivingBase living) {
      return this.canCriticalHit(living) && aEg.thePlayer.motionY < 0.0;
   }

   public float getHitBoxExpand() {
      return 0.0F;
   }

   public double adjustAttackDelay(double var1) {
      Object var115 = null;
      float var125 = 0.0F;
      var115 = this.lV.wo().getName();
      int var135_hi = -1;
      switch (((String)var115).hashCode()) {
         case -1336727224:
            if (((String)var115).equals("Watchdog 1.8")) {
               var135_hi = 2;
            }
            break;
         case 609795629:
            if (((String)var115).equals("Watchdog")) {
               var135_hi = 1;
            }
            break;
         case 1511128849:
            if (((String)var115).equals("Watchdog 1.12")) {
               var135_hi = 3;
            }
            break;
         case 1594433067:
            if (((String)var115).equals("Universal")) {
               var135_hi = 0;
            }
      }

      switch (var135_hi) {
         case 0:
            var1 = this.blockStage >= 4 ? -1.0 : 500.0;
            break;
         case 1:
            if (aEg.thePlayer.getHeldItem() != null && aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
               if (this.blockStage == 1 && Math.random() > 0.2 || this.blockStage == 2 || this.rightClickOnly.wo() && !aEg.gameSettings.cgI.isKeyDown()) {
                  var1 = -1.0;
                  if (this.oldPredictionKeepSprint.wo() && !this.eX()) {
                     if (aEg.thePlayer.ticksExisted % 2 != 1 && this.oldPredictionKeepSprint.wo() && aEg.thePlayer.ae >= 7) {
                        var1 = 500.0;
                     } else {
                        var1 = -1.0;
                     }
                  }
               } else {
                  var1 = 500.0;
               }
            } else if (this.jE != null && aEg.thePlayer.getHeldItem() != null) {
               var1 = 0.0;
            }
            break;
         case 2:
            if (aEg.thePlayer.ticksExisted % 2 != 1 && this.oldPredictionKeepSprint.wo() && aEg.thePlayer.ae >= 131 - 124) {
               var1 = 500.0;
            } else {
               var1 = -1.0;
            }

            if ((aEg.thePlayer.getHeldItem() == null || !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword))
               && this.jE != null
               && aEg.thePlayer.getHeldItem() != null) {
            }
            break;
         case 3:
            if (aEg.thePlayer.ticksExisted % 2 != 1 && this.oldPredictionKeepSprint.wo() && (aEg.thePlayer.ae >= 7 || this.isGrimSpeedActive())) {
               var1 = 500.0;
            } else {
               var1 = -1.0;
            }

            if ((aEg.thePlayer.getHeldItem() == null || !(aEg.thePlayer.getHeldItem().getItem() instanceof ItemSword))
               && (this.jE != null && aEg.thePlayer.getHeldItem() != null || aEg.thePlayer.getHeldItem() == null)) {
               this.blockStage = -1;
            }
      }

      if (this.clickMode.wo().getName().equals("Normal") && this.oldPredictionKeepSprint.wo()) {
         var115 = this.c(this.jE);
         if (!this.lV.wo().getName().equals("Watchdog")) {
            if (aEg.thePlayer.ticksExisted % 2 != 1 && this.oldPredictionKeepSprint.wo() && aEg.thePlayer.ae >= 7) {
               var1 = 500.0;
            } else {
               var1 = -1.0;
               if (aEg.thePlayer.cqL < 3 && aEg.thePlayer.onGround && this.oldMovefixBoost.wo()) {
                  RotationComponent.d(false);
                  var125 = this.computePitch(this.jE, aEg.thePlayer.pl);
                  RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, var125), 10.0, MovementFix.NORMAL);
               }
            }
         } else if (aEg.thePlayer.cqL < 3 && aEg.thePlayer.onGround && this.oldMovefixBoost.wo() && this.blockStage == 1 && aEg.thePlayer.ae > 7) {
            RotationComponent.d(false);
            var125 = this.computePitch(this.jE, aEg.thePlayer.pl);
            RotationComponent.setRotations(new Vector2f(aEg.thePlayer.pl, var125), 10.0, MovementFix.NORMAL);
         }
      }

      return var1;
   }

   public long getAimReactionDelay(boolean var1) {
      double var8 = 0.0;
      double var10 = 0.0;
      var10 = this.advancedAimReaction.wo().doubleValue();
      var8 = this.advancedAimReactionJitter.wo().doubleValue();
      if (var1) {
         var10 *= 0.6;
         var8 *= 0.5;
      }

      return this.jitteredDelay(var10, var8, 20L, 700L);
   }

   public MovingObjectPosition rayCastTarget(Vector2f vec2, double var2) {
      this.movingObjectPosition = null;
      if (this.subtickRaycast.wo()) {
         aEg.entityRenderer.getMouseOver(1.0F);
         if (this.isHitOn(aEg.objectMouseOver, this.jE)) {
            this.movingObjectPosition = aEg.objectMouseOver;
            return this.movingObjectPosition;
         }
      }

      this.b(vec2, var2);
      if (!this.isHitOn(this.movingObjectPosition, this.jE)) {
         this.b(RotationComponent.fk, var2);
      }

      if (!this.isHitOn(this.movingObjectPosition, this.jE)) {
         this.b(RotationComponent.fm, var2);
      }

      if (this.isHitOn(this.movingObjectPosition, this.jE)) {
         this.b(this.movingObjectPosition);
         return this.movingObjectPosition;
      }
      return this.movingObjectPosition != null && this.movingObjectPosition.typeOfHit == MovingObjectType.ENTITY ? this.movingObjectPosition : null;
   }

   public boolean hasKnockbackSource() {
      return (aEg.thePlayer == null || !aEg.thePlayer.isSprinting() && EnchantmentHelper.getKnockbackModifier(aEg.thePlayer) <= 0 ? 104 + -104 : 1) != 0;
   }

   public Vector2f getPredictedRotations() {
      Object var73 = null;
      double var74 = 0.0;
      double var76 = 0.0;
      Object var84 = null;
      long var86 = 0L;
      double var89 = 0.0;
      Object var95 = null;
      Object var96 = null;
      double var98 = 0.0;
      double var101 = 0.0;
      Object var103 = null;
      if (!this.isAdvancedRotations()) {
         return this.jE == null ? new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch) : this.c(this.jE);
      } else if (this.jE == null) {
         return new Vector2f(aEg.thePlayer.pl, aEg.thePlayer.rotationPitch);
      }
      var95 = this.jE.getEntityBoundingBox();
      if ((AxisAlignedBB)var95 != null && !((AxisAlignedBB)var95).hasNaN()) {
         this.updateMotionAverages();
         var73 = this.getPredictedAimPoint();
         var86 = System.currentTimeMillis();
         var74 = aEg.thePlayer.getDistanceToEntity(this.jE);
         if (this.aimPoint == null) {
            this.aimPoint = (Vector3d)var73;
            this.nextAimUpdate = var86 + this.getAimReactionDelay(false);
         } else {
            var103 = ((Vector3d)var73).subtract(this.aimPoint);
            var84 = 0.028 + Math.min(0.2, var74 * 0.006);
            var101 = ((Vector3d)var103).wg();
            var76 = 0.36 + Math.min(0.85, var74 * 0.07);
            int flag = var101 > var76 ? 1 : 0;
            int flag2 = var86 >= this.nextAimUpdate ? 1 : 0;
            if (var101 > (Double)var84 && (flag2 != 0 || flag != 0)) {
               var89 = this.advancedAnchor.wo().doubleValue();
               var98 = Math.min(0.7, Math.max(0.08, var89 + var101 * 0.32));
               var96 = ((AxisAlignedBB)var95).expand(0.12, 0.12, 0.12);
               this.aimPoint = this.clampToBox(this.aimPoint.e(((Vector3d)var103).ag(var98)), (AxisAlignedBB)var96);
               this.nextAimUpdate = var86 + this.getAimReactionDelay((flag) != 0);
            }
         }

         var103 = (AxisAlignedBB)var95;
         var84 = new Vec3(this.aimPoint.x, this.aimPoint.y, this.aimPoint.z);
         return RotationUtil.h(RotationUtil.a(this.jE, (AxisAlignedBB)var103, (Vec3)var84, this.range.wo().doubleValue(), this.canHitThroughWalls(), this.getHitBoxExpand()));
      }
      return RotationUtil.y(this.jE);
   }

   public void b(Vector2f vec2, double var2) {
      Object var10 = null;
      if (vec2 != null) {
         var10 = RayCastUtil.rayCast(vec2, var2, this.getHitBoxExpand(), aEg.thePlayer, this.canHitThroughWalls());
         if ((MovingObjectPosition)var10 != null && ((MovingObjectPosition)var10).typeOfHit == MovingObjectType.ENTITY) {
            this.movingObjectPosition = (MovingObjectPosition)var10;
         }
      }
   }

   public boolean containsBlockClass(AxisAlignedBB box, Class<? extends Block> type) {
      return this.containsBlock(box, type::isInstance);
   }

   public void warnMissingSecondSword() {
      if (!this.isDualSword()) {
         this.warnedNoSecondSword = false;
      } else {
         if (!this.hasTwoSwords()) {
            if (!this.warnedNoSecondSword) {
               ChatUtil.b("Dual Sword Auto Block requires two swords in your hotbar. Get a second sword.");
               this.warnedNoSecondSword = true;
            }
         } else {
            this.warnedNoSecondSword = false;
         }
      }
   }

   public boolean b(AxisAlignedBB box) {
      return this.containsBlock(box, var0 -> var0 == Blocks.fire || var0 == Blocks.flowing_lava || var0 == Blocks.lava);
   }

   public KnockbackSample evaluateKnockbackCandidate(EntityLivingBase living, AxisAlignedBB box, double var3, double var5, double var7) {
      double var47 = 0.0;
      float var49 = 0.0F;
      Object var50 = null;
      Object var56 = null;
      Object var59 = null;
      var59 = box.contract(0.05, 0.0, 0.05);
      var50 = ((AxisAlignedBB)var59).offset(0.0, -0.35, 0.0);
      String var11 = null;
      var47 = Double.longBitsToDouble(-4503599627370496L);
      if (this.containsMaterial((AxisAlignedBB)var59, Material.lava) || this.containsMaterial((AxisAlignedBB)var50, Material.lava)) {
         var11 = "Lava";
         var47 = 150.0 - var7 * 8.0;
      } else if (this.containsBlockClass((AxisAlignedBB)var59, BlockWeb.class) || this.containsBlockClass((AxisAlignedBB)var50, BlockWeb.class)) {
         var11 = "Web";
         var47 = 125.0 - var7 * 7.0;
      } else if (!this.b((AxisAlignedBB)var59) && !this.b((AxisAlignedBB)var50)) {
         if (!this.containsBlockEqual((AxisAlignedBB)var59, Blocks.cactus) && !this.containsBlockEqual((AxisAlignedBB)var50, Blocks.cactus)) {
            var56 = this.findSurfaceBelow((AxisAlignedBB)var50, 24);
            if ((Double)var56 < 0.0) {
               var11 = ((AxisAlignedBB)var50).minY <= 8.0 ? "Void" : "Deep Drop";
               var47 = (((AxisAlignedBB)var50).minY <= 8.0 ? Double.longBitsToDouble(-9072756156650891733L ^ -4434576306040613333L) : 108.0) - var7 * 7.0;
            } else if ((Double)var56 >= 4.0) {
               var11 = "Ditch";
               var47 = 88.0 + Math.min((Double)var56, 10.0) * 3.5 - var7 * 6.0;
            } else if (this.containsMaterial((AxisAlignedBB)var59, Material.water) || this.containsMaterial((AxisAlignedBB)var50, Material.water)) {
               var11 = "Water";
               var47 = 58.0 + Math.max(0.0, (Double)var56) * 2.0 - var7 * 5.0;
            }
         } else {
            var11 = "Cactus";
            var47 = 96.0 - var7 * 6.0;
         }
      } else {
         var11 = "Fire";
         var47 = 100.0 - var7 * 7.0;
      }

      if (var11 == null) {
         return null;
      }
      var56 = this.yawFromDelta(var3, var5);
      var49 = this.computePitch(living, (Float)var56);
      return new KnockbackSample(living.getEntityId(), (Float)var56, var49, var7, var47, var11);
   }

   @Override
   public void setEnabled(boolean enabled) {
      if (!enabled && this.isEnabled() && this.isDualSword() && aEg.thePlayer != null && aEg.theWorld != null && aEg.getNetHandler() != null) {
         this.pendingDisable = true;
      } else {
         if (enabled) {
            this.pendingDisable = false;
         }

         super.setEnabled(enabled);
      }
   }

   public Vector3d getPredictedAimPoint() {
      Object var15 = null;
      double var16 = 0.0;
      double var18 = 0.0;
      double var20 = 0.0;
      Object var23 = null;
      Object var25 = null;
      var16 = aEg.thePlayer.Ty().v(0.0, aEg.thePlayer.getEyeHeight(), 0.0).g(new Vector3d(this.jE.posX, this.jE.posY + this.jE.height * 0.75, this.jE.posZ));
      var20 = Math.min(3.5, Math.max(0.0, this.advancedPrediction.wo().doubleValue() + var16 * 0.017));
      var23 = this.targetMotionAverage.subtract(this.playerMotionAverage);
      var18 = Math.max(0.35, Math.min(this.jE.height * 0.82, this.jE.height - 0.12));
      var25 = (new Vector3d(this.jE.posX, this.jE.posY + var18, this.jE.posZ)).e(((Vector3d)var23).ag(var20));
      var15 = this.jE.getEntityBoundingBox().expand(0.18, 0.1, 0.18);
      var25 = this.clampToBox((Vector3d)var25, (AxisAlignedBB)var15);
      return (Vector3d)var25;
   }

   public void switchToOtherSword() {
      if (this.isDualSword() && blocking) {
         int eJ2 = this.getOtherSwordSlot();
         if (eJ2 == -1) {
            canAttack = false;
         } else {
            SlotComponent.b(eJ2, false);
            this.blockSlot = eJ2;
            blocking = false;
         }
      }
   }

   public Vector3d clampToBox(Vector3d var1, AxisAlignedBB box) {
      double var13 = 0.0;
      double var15 = 0.0;
      double var17 = 0.0;
      var15 = Math.max(box.minX, Math.min(var1.x, box.maxX));
      var13 = Math.max(box.minY, Math.min(var1.y, box.maxY));
      var17 = Math.max(box.minZ, Math.min(var1.z, box.maxZ));
      return new Vector3d(var15, var13, var17);
   }

   public boolean canDisplaceKnockback(EntityLivingBase living) {
      boolean var10000;
      if (this.getDisplacementRejection(living) == null) {
         var10000 = true;
      } else {
         byte var5 = -70;
         var5 = 55;
         boolean var7 = false;
         var10000 = var7;
      }

      return var10000;
   }

   @Override
   public void onEnable() {
      this.fakeBlocked = false;
      this.blockSlot = -1;
      this.lastBlockAttackTick = -1;
      this.blockQueued = false;
      this.warnedNoSecondSword = false;
      this.pendingDisable = false;
      this.watchdogFallbackActive = false;
      this.rightClickTick = -1;
      this.rightClickDown = aEg.gameSettings.cgI.isKeyDown();
      this.rightClickHandled = false;
      this.attack = 0;
      this.blockStage = 0;
      this.attackInterval = 0L;
      this.lastVelocityBoostTick = -1;
      this.knockbackPlan = null;
      this.lastDebugLine = "";
      this.lastDebugTick = -1;
      this.resetAdvancedState();
      if (this.rightClickOnly.wo() && Math.random() > 0.7) {
         ChatUtil.b("hold right click to autoblock or turn off right click to autoblock");
      }
   }

   public void applyPendingDisable() {
      if (this.pendingDisable) {
         this.pendingDisable = false;
         this.releaseBlock(true);
         super.setEnabled(false);
      }
   }

   public boolean containsBlockEqual(AxisAlignedBB box, Block var2) {
      return this.containsBlock(box, var1x -> {
         boolean var10000;
         if (var1x == var2) {
            var10000 = true;
         } else {
            byte var5 = 45;
            var5 = -26;
            boolean var7 = false;
            var10000 = var7;
         }

         return var10000;
      });
   }

   public boolean shouldSkipKeepSprint() {
      return aEg.thePlayer.ae < 8 && !this.isGrimSpeedActive();
   }

   public float yawFromDelta(double var1, double var3) {
      return (float)Math.toDegrees(Math.atan2(-var1, var3)) - 90.0F;
   }

   public boolean isWithinAttackCooldown() {
      Object var26 = null;
      var26 = this.getAttackDelay();
      int notBooleanValue = !this.attackTimer.T(this.attackInterval) || !this.swingTimer.T((long)((Double)((Tuple)var26).getSecond() * 50.0)) && !((Boolean)((Tuple)var26).getFirst()).booleanValue() ? 0 : 1;
      return notBooleanValue != 0 && aEg.thePlayer.getDistanceToEntity(this.jE) <= this.range.wo().doubleValue() + 0.5;
   }

   public void doAttack(List<EntityLivingBase> livings) {
      Object var258 = null;
      Object var259 = null;
      double var277 = 0.0;
      Object var304 = null;
      Object var316 = null;
      Object var355 = null;
      Object var361 = null;
      Object var363 = null;
      if (!this.newUniversalKeepSprint.wo() || this.jE == null || this.shouldSkipKeepSprint() || !this.isWithinAttackCooldown() || !this.stopSprinting()) {
         int eR2 = (int)(this.isAdvancedRotations() ? 1L : 0L);
         if (eR2 == 0) {
            this.triggerEntityId = Integer.MIN_VALUE;
            this.triggerArmed = false;
            this.triggerReadyTime = 0L;
         }

         if (!this.rotationMode.wo().getName().equals("Grim") || !this.isGrimVelocityActive()) {
            if (!this.isDualSword() && this.velocityBoost.wo() && this.jE != null && (canAttack || !this.badPacketsCheck.wo())) {
               int ae2 = aEg.thePlayer.ae;
               int intValue2 = this.boostTicks.wo().intValue();
               if (ae2 < intValue2 && aEg.thePlayer.ticksExisted != this.lastVelocityBoostTick) {
                  this.lastVelocityBoostTick = aEg.thePlayer.ticksExisted;
                  int equals2 = this.rotationMode.wo().getName().equals("Grim") && this.grimRotations != null ? 1 : 0;
                  if (equals2 != 0) {
                     var258 = aEg.thePlayer.pl + MathHelper.wrapAngleTo180_float(this.grimRotations.getX() - aEg.thePlayer.pl);
                     PacketUtil.send(
                        new C06PacketPlayerPosLook(
                           aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, (Float)var258, this.grimRotations.getY(), aEg.thePlayer.onGround
                        )
                     );
                  }

                  var258 = this.mode.wo().getName();
                  int var260_hi = -1;
                  switch (((String)var258).hashCode()) {
                     case -1818398616:
                        if (((String)var258).equals("Single")) {
                           var260_hi = 1;
                        }
                        break;
                     case -1805606060:
                        if (((String)var258).equals("Switch")) {
                           var260_hi = 0;
                        }
                        break;
                     case 718473776:
                        if (((String)var258).equals("Multiple")) {
                           var260_hi = 2;
                        }
                  }

                  switch (var260_hi) {
                     case 0:
                     case 1:
                        var259 = this.rayCastTarget(equals2 != 0 ? this.grimRotations : RotationComponent.fk, this.range.wo().doubleValue());
                        if ((MovingObjectPosition)var259 != null) {
                           this.b((MovingObjectPosition)var259);
                        } else if (!this.rayCast.wo()) {
                           this.spoofMouseOverTo(this.jE);
                        }

                        this.attack(this.jE);
                        break;
                     case 2:
                        var363 = this.range.wo().doubleValue();
                        final double var363a = (Double)var363;
                        livings.stream().filter(var2 -> {
                           boolean var10000;
                           if (aEg.thePlayer.getDistanceToEntity(var2) <= var363a) {
                              var10000 = true;
                           } else {
                              byte var6 = 111;
                              var6 = -105;
                              boolean var8 = false;
                              var10000 = var8;
                           }

                           return var10000;
                        }).forEach(var1x -> {
                           if (!this.rayCast.wo()) {
                              this.spoofMouseOverTo(var1x);
                           }

                           this.attack(var1x);
                        });
                  }

                  if (equals2 != 0) {
                     PacketUtil.send(
                        new C06PacketPlayerPosLook(
                           aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, aEg.thePlayer.onGround
                        )
                     );
                  }

                  this.attackTimer.aX();
                  return;
               }
            }

            int eA2 = (int)(this.isDualSword() ? 1L : 0L);
            Tuple var4 = eA2 != 0 ? null : this.getAttackDelay();
            var277 = eA2 != 0 ? -1.0 : (Double)var4.getSecond();
            int booleanValue2 = eA2 == 0 && ((Boolean)var4.getFirst()).booleanValue() ? 1 : 0;
            int t = eA2 != 0 ? (this.lastBlockAttackTick != -1 && aEg.thePlayer.ticksExisted - this.lastBlockAttackTick < 2 ? 0 : 1) : (!this.attackTimer.T(this.attackInterval) || !this.swingTimer.T((long)(var277 * 50.0)) && booleanValue2 == 0 ? 0 : 1);
            if (t != 0 && this.jE != null) {
               if (eA2 == 0) {
                  var363 = (long)(this.cps.wv().longValue() * 1.5);
                  this.attackInterval = 1000L / (Long)var363;
               }

               if ((eA2 != 0 || Math.sin(this.attackInterval) + 1.0 > Math.random() || this.attackTimer.T(this.attackInterval + 500L) || Math.random() > 0.5)
                  && (canAttack || !this.badPacketsCheck.wo())) {
                  var363 = this.range.wo().doubleValue();
                  int equals3 = this.rotationMode.wo().getName().equals("Grim") && this.grimRotations != null ? 1 : 0;
                  var355 = equals3 != 0 ? this.grimRotations : RotationComponent.fk;
                  var361 = this.rayCastTarget((Vector2f)var355, (Double)var363);
                  if (equals3 != 0) {
                     var304 = aEg.thePlayer.pl + MathHelper.wrapAngleTo180_float(this.grimRotations.getX() - aEg.thePlayer.pl);
                     PacketUtil.send(
                        new C06PacketPlayerPosLook(
                           aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, (Float)var304, this.grimRotations.getY(), aEg.thePlayer.onGround
                        )
                     );
                  }

                  var304 = this.mode.wo().getName();
                  int var331_hi = -1;
                  switch (((String)var304).hashCode()) {
                     case -1818398616:
                        if (((String)var304).equals("Single")) {
                           var331_hi = 1;
                        }
                        break;
                     case -1805606060:
                        if (((String)var304).equals("Switch")) {
                           var331_hi = 0;
                        }
                        break;
                     case 718473776:
                        if (((String)var304).equals("Multiple")) {
                           var331_hi = 2;
                        }
                  }

                  switch (var331_hi) {
                     case 0:
                     case 1:
                        int notWo = (!(aEg.thePlayer.getDistanceToEntity(this.jE) <= (Double)var363) || this.rayCast.wo()) && ((MovingObjectPosition)var361 == null || ((MovingObjectPosition)var361).entityHit != this.jE) ? 0 : 1;
                        if (notWo != 0) {
                           if (eR2 == 0 || this.isTriggerReactionElapsed(this.jE, (MovingObjectPosition)var361, (Double)var363)) {
                              if ((MovingObjectPosition)var361 != null) {
                                 this.b((MovingObjectPosition)var361);
                              } else {
                                 this.spoofMouseOverTo(this.jE);
                              }

                              this.attack(this.jE);
                           }
                        } else if ((MovingObjectPosition)var361 == null || ((MovingObjectPosition)var361).typeOfHit != MovingObjectType.ENTITY) {
                           var316 = this.clickMode.wo().getName();
                           int var308_hi = -1;
                           switch (((String)var316).hashCode()) {
                              case -1955878649:
                                 if (((String)var316).equals("Normal")) {
                                    var308_hi = 0;
                                 }
                                 break;
                              case -957532567:
                                 if (((String)var316).equals("Hit Select")) {
                                    var308_hi = 1;
                                 }
                           }

                           switch (var308_hi) {
                              case 0:
                              case 1:
                                 if (aEg.playerController.curBlockDamageMP != 0.0F) {
                                    return;
                                 }

                                 if (eR2 != 0) {
                                    this.swingAdvanced();
                                 }
                           }
                        } else if (((MovingObjectPosition)var361).entityHit instanceof EntityLivingBase) {
                           this.b((MovingObjectPosition)var361);
                           this.attack((EntityLivingBase)((MovingObjectPosition)var361).entityHit);
                        }
                        break;
                     case 2:
                        final double var363b = (Double)var363;
                        livings.removeIf(var2 -> aEg.thePlayer.getDistanceToEntity(var2) > var363b);
                        if (!livings.isEmpty()) {
                           livings.forEach(var1x -> {
                              if (!this.rayCast.wo()) {
                                 this.spoofMouseOverTo(var1x);
                              }

                              this.attack(var1x);
                           });
                        }
                  }

                  if (equals3 != 0) {
                     PacketUtil.send(
                        new C06PacketPlayerPosLook(
                           aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, aEg.thePlayer.pl, aEg.thePlayer.rotationPitch, aEg.thePlayer.onGround
                        )
                     );
                  }

                  this.attackTimer.aX();
               }
            }
         }
      }
   }

   public void postAttackBlock() {
      Object var148 = null;
      var148 = this.lV.wo().getName();
      int var167_hi = -1;
      switch (((String)var148).hashCode()) {
         case -2099899231:
            if (((String)var148).equals("Intave")) {
               var167_hi = 1;
            }
            break;
         case -1885322919:
            if (((String)var148).equals("Dual Sword")) {
               var167_hi = 7;
            }
            break;
         case -1844299644:
            if (((String)var148).equals("Imperfect Vanilla")) {
               var167_hi = 4;
            }
            break;
         case -1336727224:
            if (((String)var148).equals("Watchdog 1.8")) {
               var167_hi = 3;
            }
            break;
         case 73298841:
            if (((String)var148).equals("Legit")) {
               var167_hi = 0;
            }
            break;
         case 341887541:
            if (((String)var148).equals("Vanilla ReBlock")) {
               var167_hi = 5;
            }
            break;
         case 609795629:
            if (((String)var148).equals("Watchdog")) {
               var167_hi = 6;
            }
            break;
         case 1897755483:
            if (((String)var148).equals("Vanilla")) {
               var167_hi = 2;
            }
      }

      switch (var167_hi) {
         case 0:
         case 1:
         default:
            break;
         case 2:
            if (this.ticksSinceAttack != 0) {
               this.block(false, true);
            }
            break;
         case 3:
            canAttack = false;
            this.blockStage++;
            BlinkComponent.blink();
            int var10000 = this.blockStage % 2;
            if (aEg.playerController.curBlockDamageMP != 0.0F && aEg.objectMouseOver.typeOfHit == MovingObjectType.BLOCK) {
            }
            break;
         case 4:
            if (this.ticksSinceAttack == 1 && aEg.thePlayer.isSwingInProgress && Math.random() > 0.1) {
               this.block(false, true);
            }
            break;
         case 5:
            if (this.ticksSinceAttack == 1) {
               this.block(false, true);
            }
            break;
         case 6:
            if (!this.shortBlockCycle) {
               if (this.blockStage == 2) {
                  canAttack = false;
                  this.block(true, false);
                  BlinkComponent.blink();
                  this.fakeBlocked = false;
                  blocking = true;
               }
            } else if (this.blockStage == 1) {
               canAttack = false;
               this.block(true, false);
               BlinkComponent.blink();
               this.fakeBlocked = false;
               blocking = true;
            }
            break;
         case 7:
            if (this.blockQueued) {
               this.block(true, false);
            }
      }
   }

   public float getServerPitch() {
      return RotationComponent.fk != null ? RotationComponent.fk.getY() : aEg.thePlayer.rotationPitch;
   }

   public void block(boolean var1, boolean var2) {
      Object var14 = null;
      Object var16 = null;
      Object var17 = null;
      if (!blocking || !var1) {
         var14 = RayCastUtil.c(RotationComponent.fl, 3.0);
         if (var2 && (MovingObjectPosition)var14 != null && ((MovingObjectPosition)var14).typeOfHit == MovingObjectType.ENTITY) {
            this.c((MovingObjectPosition)var14);
         }

         if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_19) && !BlinkComponent.enabled) {
            var17 = Via.getManager().getConnectionManager().getConnections().iterator().next();
            var16 = PacketWrapper.create(ServerboundPackets1_19.USE_ITEM, (UserConnection)var17);
            ((PacketWrapper)var16).write(Types.VAR_INT, 0);
            ((PacketWrapper)var16).write(Types.VAR_INT, aEg.playerController.GZ());
         }

         SlotComponent var10002 = this.d(SlotComponent.class);
         PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
         blocking = true;
      }
   }

   public void spoofMouseOverTo(EntityLivingBase living) {
      if (living != null) {
         this.b(new MovingObjectPosition(living, living.getPositionEyes(1.0F)));
      }
   }

   public boolean isGrimSpeedActive() {
      Object var11 = null;
      var11 = this.e(Speed.class);
      return ((Speed)var11).isEnabled() && ((Speed)var11).getMode().wo() instanceof GrimSpeed && ((GrimSpeed)((Speed)var11).getMode().wo()).fastFall.wo();
   }

   public boolean containsBlock(AxisAlignedBB box, Predicate<Block> predicate) {
      Object var132 = null;
      int floor_double2 = MathHelper.floor_double(box.minX + 1.0E-4);
      int floor_double3 = MathHelper.floor_double(box.maxX - 1.0E-4);
      int floor_double4 = MathHelper.floor_double(box.minY + 1.0E-4);
      int floor_double5 = MathHelper.floor_double(box.maxY - 1.0E-4);
      int floor_double6 = MathHelper.floor_double(box.minZ + 1.0E-4);
      int floor_double7 = MathHelper.floor_double(box.maxZ - 1.0E-4);

      for (int i = floor_double2; i <= floor_double3; i++) {
         for (int j = floor_double4; j <= floor_double5; j++) {
            for (int k = floor_double6; k <= floor_double7; k++) {
               var132 = aEg.theWorld.getBlockState(new BlockPos(i, j, k)).getBlock();
               if (predicate.test((Block)var132)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void postMotionBlock() {
      Object var82 = null;
      var82 = this.lV.wo().getName();
      int var79_hi = -1;
      switch (((String)var82).hashCode()) {
         case 609795629:
            if (((String)var82).equals("Watchdog")) {
               var79_hi = 0;
            }
            break;
         case 1511128849:
            if (((String)var82).equals("Watchdog 1.12")) {
               var79_hi = 2;
            }
            break;
         case 1594433067:
            if (((String)var82).equals("Universal")) {
               var79_hi = 1;
            }
      }

      switch (var79_hi) {
         case 0:
         default:
            break;
         case 1:
            if (this.blockStage == 2) {
               BlinkComponent.dispatch();
            }
            break;
         case 2:
            this.blockStage++;
            if (this.blockStage > 0
               && !BadPacketsComponent.a((-9 - 4 ^ -13) != 0, true, false, false, false, true)
               && !this.e(LongJump.class).isEnabled()
               && !SlotComponent.dj
               && (aEg.thePlayer.ticksExisted % 2 == 0 || !this.oldPredictionKeepSprint.wo() || aEg.thePlayer.ae < 7 && !this.isGrimSpeedActive())) {
               this.block(true, false);
            }
      }
   }

   public boolean isAttackDue() {
      double var26 = 0.0;
      Object var35 = null;
      var35 = this.getAttackDelay();
      var26 = (Double)((Tuple)var35).getSecond();
      int booleanValue2 = (int)(((Boolean)((Tuple)var35).getFirst()).booleanValue() ? 1L : 0L);
      return this.attackTimer.T(this.attackInterval - 1L)
         && this.jE != null
         && (this.swingTimer.T((long)(var26 * 50.0) - 50L) || booleanValue2 != 0)
         && (!this.clickMode.wo().getName().equals("Hit Select") || this.jE.hurtTime <= PingSpoofComponent.getPing() / 50L - 1L || aEg.thePlayer.ae <= 11)
         && canAttack;
   }

   public void updateMotionAverages() {
      Object var5 = null;
      Object var6 = null;
      var5 = new Vector3d(this.jE.posX - this.jE.lastTickPosX, this.jE.posY - this.jE.lastTickPosY, this.jE.posZ - this.jE.lastTickPosZ);
      var6 = new Vector3d(
         aEg.thePlayer.posX - aEg.thePlayer.lastTickPosX, aEg.thePlayer.posY - aEg.thePlayer.lastTickPosY, aEg.thePlayer.posZ - aEg.thePlayer.lastTickPosZ
      );
      this.targetMotionAverage = this.targetMotionAverage.ag(0.72).e(((Vector3d)var5).ag(0.28));
      this.playerMotionAverage = this.playerMotionAverage.ag(0.76).e(((Vector3d)var6).ag(0.24));
   }

   public boolean containsMaterial(AxisAlignedBB box, Material material) {
      return this.containsBlock(box, var1x -> var1x.getMaterial() == material);
   }

   public boolean canBlock() {
      Object var13 = null;
      if (aEg.thePlayer == null) {
         return false;
      }
      SlotComponent var10000 = this.d(SlotComponent.class);
      var13 = SlotComponent.getItemStack();
      return (ItemStack)var13 != null && ((ItemStack)var13).getItem() instanceof ItemSword && this.hasTwoSwords();
   }

   static {

      attacking = false;
      SQRT3 = (float)Math.sqrt(3.0);
      SQRT5 = (float)Math.sqrt(5.0);
   }

   public void attack(EntityLivingBase living) {
      Object var66 = null;
      Object var74 = null;
      Object var79 = null;
      var74 = new AttackEvent(living);
      Client.a.e().d((AttackEvent)var74);
      if (!((AttackEvent)var74).isCancelled() && ((AttackEvent)var74).getLiving() != null) {
         var79 = ((AttackEvent)var74).getLiving();
         KnockbackSample var4 = this.canDisplaceKnockback((EntityLivingBase)var79) ? this.knockbackPlan : null;
         int flag = var4 != null && this.shouldUseKnockbackPlan((EntityLivingBase)var79, var4) ? 1 : 0;
         RotationSnapshot var6 = flag != 0 ? this.snapshotRotations(var4) : null;
         if (this.eX()) {
            this.switchToOtherSword();
         }

         this.attackPending = Math.max(this.attackPending, 1);
         attacking = true;
         if (!this.noSwing.wo() && !ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            aEg.thePlayer.swingItem();
         }

         if (flag != 0 && !this.rotationMode.wo().getName().equals("Grim")) {
            this.b(var4);
            this.debugKnockback("forced", var4);
         }

         if (this.keepSprint.wo()
               && (
                  aEg.thePlayer.ae >= 7
                     || !this.e(Velocity.class).isEnabled()
                     || !this.e(Velocity.class).mode.wo().getName().equals("Grim Reduce")
                     || this.e(Velocity.class).mode.wo().getName().equals("Grim Reduce") && !((GrimReduceVelocity)this.e(Velocity.class).mode.wo()).stopSprint.wo()
               )
            || !this.newYouNeedThisToggledOnCurreFake.wo() && this.oldPredictionKeepSprint.wo() && (aEg.thePlayer.ae >= 7 || this.isGrimSpeedActive()) && living != null) {
            aEg.playerController.syncCurrentPlayItem();
            PacketUtil.send(new C02PacketUseEntity((EntityLivingBase)var79, Action.ATTACK));
            if (this.usesWatchdogBlock() && this.eX()) {
               var66 = this.getHitVec((EntityLivingBase)var79);
               PacketUtil.send(new C02PacketUseEntity((EntityLivingBase)var79, (Vec3)var66));
               PacketUtil.send(new C02PacketUseEntity((EntityLivingBase)var79, Action.INTERACT));
            }

            if (aEg.thePlayer.fallDistance > 0.0F
               && !aEg.thePlayer.onGround
               && !aEg.thePlayer.isOnLadder()
               && !aEg.thePlayer.isInWater()
               && !aEg.thePlayer.isPotionActive(Potion.blindness)
               && aEg.thePlayer.ridingEntity == null) {
               aEg.thePlayer.onCriticalHit((EntityLivingBase)var79);
            }
         } else {
            aEg.playerController.attackEntity(aEg.thePlayer, (EntityLivingBase)var79);
            if (this.usesWatchdogBlock() && this.eX()) {
               var66 = this.getHitVec((EntityLivingBase)var79);
               PacketUtil.send(new C02PacketUseEntity((EntityLivingBase)var79, (Vec3)var66));
               PacketUtil.send(new C02PacketUseEntity((EntityLivingBase)var79, Action.INTERACT));
            }
         }

         if (this.isDualSword()) {
            this.blockQueued = true;
            this.lastBlockAttackTick = aEg.thePlayer.ticksExisted;
         }

         if (var6 != null) {
            var6.restore();
         }

         if (!this.noSwing.wo() && ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            aEg.thePlayer.swingItem();
         }

         this.swingTimer.aX();
         this.ticksSinceAttack = 0;
         this.lastHitTicks.put((EntityLivingBase)var79, aEg.thePlayer.ticksExisted);
      }
   }

   public boolean canCriticalHit(EntityLivingBase living) {
      return living != null
         && aEg.thePlayer.fallDistance > 0.0F
         && !aEg.thePlayer.onGround
         && !aEg.thePlayer.isOnLadder()
         && !aEg.thePlayer.isInWater()
         && !aEg.thePlayer.isPotionActive(Potion.blindness)
         && aEg.thePlayer.ridingEntity == null;
   }

   public String getDisplacementRejection(EntityLivingBase living) {
      if (!this.isDisplacementEnabled()) {
         return "disabled";
      } else if (living == null) {
         return "no-target";
      } else if (this.knockbackPlan == null) {
         return "no-plan";
      } else if (!this.hasKnockbackSource()) {
         return "no-kb-source";
      } else if (this.knockbackPlan.targetId != living.getEntityId()) {
         return "target-swap";
      } else if (this.attackPending <= 0 && !this.isAttackDue()) {
         return "no-attack-window";
      }
      return this.h(living) ? "crit-priority" : null;
   }

   public void debugKnockback(String var1, KnockbackSample var2) {
      Object var51 = null;
      Object var52 = null;
      Object var69 = null;
      if (this.knockbackDisplacementDebug.wo() && aEg.thePlayer != null) {
         if (var2 == null) {
            var51 = String.format("%s hurt=%d", var1, this.jE == null ? -1 : this.jE.hurtTime);
         } else {
            var51 = String.format(
               "%s %s score=%.1f yaw=%.1f pitch=%.1f dist=%.2f hurt=%d",
               var1,
               var2.hazard,
               var2.score,
               var2.yaw,
               var2.pitch,
               var2.distance,
               this.jE == null ? -1 : this.jE.hurtTime
            );
         }

         if (!((String)var51).equals(this.lastDebugLine) || aEg.thePlayer.ticksExisted - this.lastDebugTick >= 8) {
            this.lastDebugLine = (String)var51;
            this.lastDebugTick = aEg.thePlayer.ticksExisted;
            String prefix = ChatUtil.getPrefix();
            var69 = (String)var51;
            var52 = prefix;
            ChatUtil.c((String)var52 + "[KD] " + (String)var69);
         }
      }
   }

   public boolean isWithinYaw(EntityLivingBase living, double var2) {
      return Math.abs(MathHelper.wrapAngleTo180_float(RotationUtil.y(living).x - aEg.thePlayer.pl)) <= var2;
   }

   public boolean isWeaponAllowed() {
      Object var29 = null;
      Object var37 = null;
      if (!this.weapons.wo()) {
         return true;
      }
      var37 = aEg.thePlayer.getHeldItem();
      if ((ItemStack)var37 == null) {
         return this.fist.wo();
      }
      var29 = ((ItemStack)var37).getItem();
      if ((Item)var29 instanceof ItemSword && this.swords.wo()) {
         return true;
      } else if ((Item)var29 instanceof ItemAxe && this.axes.wo()) {
         return true;
      } else if (!this.extra.wo() || !((Item)var29 instanceof ItemTool) && !((Item)var29 instanceof ItemHoe)) {
         if (this.sharpness.wo() && EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, (ItemStack)var37) > 0) {
            return true;
         }
         return this.knockback.wo() && EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, (ItemStack)var37) > 0
            ? true
            : this.fireAspect.wo() && EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, (ItemStack)var37) > 0;
      } else {
         return true;
      }
   }

   public Vector2f getServerRotations() {
      return new Vector2f(RotationComponent.bH());
   }

   public void updateWatchdogFallback() {
      Object var58 = null;
      var58 = this.findSwordSlots();
      int flag = ((int[])var58)[0] != -1 && ((int[])var58)[1] == -1 ? 1 : 0;
      if (this.watchdogFallbackActive) {
         if (!this.lV.wo().getName().equals("Watchdog")) {
            this.watchdogFallbackActive = false;
         } else if (flag == 0 || !this.fallbackToWatchdog.wo()) {
            if (!blocking || this.flickSlot()) {
               this.fakeBlocked = false;
               this.watchdogFallbackActive = false;
               this.lV.getModes().stream().filter(var0 -> var0.getName().equals("Dual Sword")).findFirst().ifPresent(this.lV::update);
               int var10000 = ((int[])var58)[1];
            }
         }
      } else if (this.isDualSword() && this.fallbackToWatchdog.wo() && flag != 0) {
         this.releaseBlock(true);
         this.warnedNoSecondSword = false;
         this.lV.getModes().stream().filter(var0 -> var0.getName().equals("Watchdog")).findFirst().ifPresent(this.lV::update);
         this.watchdogFallbackActive = true;
      }
   }

   public RotationSnapshot snapshotRotations(KnockbackSample var1) {
      Object var7 = null;
      float var8 = 0.0F;
      if (var1 != null && aEg.thePlayer != null) {
         var7 = new RotationSnapshot(
            aEg.thePlayer.pl,
            aEg.thePlayer.rotationPitch,
            aEg.thePlayer.rotationYawHead,
            aEg.thePlayer.po,
            aEg.thePlayer.pp,
            aEg.thePlayer.pq,
            aEg.thePlayer.pr
         );
         var8 = this.getServerPitch();
         aEg.thePlayer.pl = var1.yaw;
         aEg.thePlayer.rotationPitch = var8;
         aEg.thePlayer.rotationYawHead = var1.yaw;
         aEg.thePlayer.po = var8;
         aEg.thePlayer.pp = var1.yaw;
         aEg.thePlayer.pq = var1.yaw;
         aEg.thePlayer.pr = var1.yaw;
         return (RotationSnapshot)var7;
      }
      return null;
   }

   public boolean canHitThroughWalls() {
      Object var12 = null;
      var12 = this.e(Piercing.class);
      int var10000;
      if (!this.rayCast.wo() || !this.throughWalls.wo() && ((Piercing)var12 == null || !((Piercing)var12).isEnabled())) {
         int var5 = 112;
         var5 = (byte)-70;
         var5 -= -70;
         var10000 = var5;
      } else {
         var10000 = 1;
      }

      return var10000 != 0;
   }

   public void unblock(boolean var1) {
      if (blocking && (!var1 || !blockPacketSent)) {
         PacketUtil.send(new C07PacketPlayerDigging(net.minecraft.network.play.client.C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
         blocking = false;
      }

      if (aEg.gameSettings.cgI.isKeyDown()) {
         SlotComponent var10000 = this.d(SlotComponent.class);
         if (SlotComponent.getItemStack() != null) {
            var10000 = this.d(SlotComponent.class);
            boolean var3 = SlotComponent.getItemStack().getItem() instanceof ItemSword;
         }
      }
   }

   public Vector2f c(EntityLivingBase living) {
      return this.computeRotations(living, this.range.wo().doubleValue(), this.canHitThroughWalls());
   }

   public void packetBlock(PacketSendEvent event) {
      Object var36 = null;
      Object var39 = null;
      var39 = event.dq();
      var36 = this.lV.wo().getName();
      int var41_hi = -1;
      switch (((String)var36).hashCode()) {
         case -2099899231:
            if (((String)var36).equals("Intave")) {
               var41_hi = 0;
            }
         default:
            switch (var41_hi) {
               case 0:
                  if ((Packet)var39 instanceof C03PacketPlayer && this.jE != null) {
                     event.setCancelled();
                     this.unblock(false);
                     PacketUtil.sendNoEvent((Packet<?>)var39);
                     this.block(false, (130 + -20 ^ 111) != 0);
                     this.unblock(false);
                  }
            }
      }
   }

   public boolean shouldHideSwordSlot(int var1) {
      Object var24 = null;
      if (this.isDualSword() && this.hideSecondSword.wo()) {
         var24 = this.findSwordSlots();
         return ((int[])var24)[1] != -1 && var1 == ((int[])var24)[1];
      }
      return false;
   }

   public void sortFriendsFirst() {
      this.targets.sort((var0, var1) -> {
         int name = (int)(FriendManager.n(var0.getName()) ? 1L : 0L);
         int name2 = (int)(FriendManager.n(var1.getName()) ? 1L : 0L);
         if (name != 0 && name2 == 0) {
            return -1;
         }
         return name == 0 && name2 != 0 ? -65 - -89 ^ 25 : 0;
      });
   }

   public boolean isDualSword() {
      return this.lV.wo().getName().equals("Dual Sword");
   }

   public void releaseBlock(boolean var1) {
      if (aEg.thePlayer == null) {
         this.blockSlot = -1;
         blocking = false;
      } else {
         int currentItem2 = aEg.thePlayer.inventory.currentItem;
         int flag = var1 && this.blockSlot != -1 && this.blockSlot != currentItem2 && !SlotComponent.dj ? 1 : 0;
         if (flag != 0) {
            SlotComponent.b(currentItem2, false);
            blocking = false;
         } else if (blocking) {
            this.unblock(false);
         }

         this.blockSlot = -1;
      }
   }

   public boolean isDisplacementEnabled() {
      return this.knockbackDisplacement.wo();
   }

   public void swingAdvanced() {
      if (this.isAdvancedRotations()) {
         if (this.advancedSwing.wo() && !this.noSwing.wo()) {
            aEg.thePlayer.swingItem();
            this.swingTimer.aX();
         }
      }
   }

   public void updateTargets() {
      double var37 = 0.0;
      Object var43 = null;
      var37 = this.range.wo().doubleValue();
      this.targets = TargetComponent.f(var37);
      if (this.mode.wo().getName().equals("Switch")) {
         this.targets.removeAll(this.switchHistory);
      }

      if (this.targets.isEmpty()) {
         this.switchHistory.clear();
         this.targets = TargetComponent.f(var37 + this.expandRange);
      }

      if (this.fOV.wo().doubleValue() < 360.0) {
         var43 = this.fOV.wo().doubleValue() / 2.0;
         final double var43a = (Double)var43;
         this.targets.removeIf(var3 -> !this.isWithinYaw(var3, var43a));
      }

      var43 = this.sorting.wo().getName();
      int var31_hi = -1;
      switch (((String)var43).hashCode()) {
         case -2137395588:
            if (((String)var43).equals("Health")) {
               var31_hi = 0;
            }
            break;
         case -2087977922:
            if (((String)var43).equals("Hurt Time")) {
               var31_hi = 1;
            }
      }

      switch (var31_hi) {
         case 0:
            this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
            this.sortFriendsFirst();
            break;
         case 1:
            this.targets.sort(Comparator.comparingDouble(var0 -> var0.hurtTime));
            this.sortFriendsFirst();
      }
   }

   public boolean isDualSwordEngaged() {
      return (!this.isDualSword() && !this.watchdogFallbackActive ? -189 + 97 - -92 : 1) != 0;
   }

   public Vec3 getHitVec(EntityLivingBase living) {
      double var12 = 0.0;
      Object var14 = null;
      Object var15 = null;
      Object var16 = null;
      Object var17 = null;
      if (living == null) {
         return new Vec3(0.0, 0.0, 0.0);
      }
      var16 = aEg.thePlayer.getPositionEyes(1.0F);
      var15 = aEg.thePlayer.getVectorForRotation(RotationComponent.fk.getY(), RotationComponent.fk.getX());
      var12 = this.range.wo().doubleValue();
      var17 = ((Vec3)var16).addVector(((Vec3)var15).xCoord * var12, ((Vec3)var15).yCoord * var12, ((Vec3)var15).zCoord * var12);
      var14 = living.getEntityBoundingBox().expand(0.1, 0.1, 0.1).calculateIntercept((Vec3)var16, (Vec3)var17);
      return (MovingObjectPosition)var14 != null && ((MovingObjectPosition)var14).hitVec != null
         ? ((MovingObjectPosition)var14).hitVec.subtract(new Vec3(living.posX, living.posY, living.posZ))
         : new Vec3(0.0, living.getEyeHeight() * 0.5, 0.0);
   }

   public float b(float var1, float var2) {
      float var10 = 0.0F;
      float var11 = 0.0F;
      if (var1 <= 0.0F) {
         return 0.0F;
      }
      var10 = Math.max(1.0E-4F, Math.abs(var2));
      if ((float)(this.random.nextGaussian() * var1) > var10) {
         var11 = var10;
      }

      if (var11 < -var10) {
         var11 = -var10;
      }

      return var11;
   }

   public boolean isHitOn(MovingObjectPosition hit, EntityLivingBase living) {
      return hit != null && hit.typeOfHit == MovingObjectType.ENTITY && hit.entityHit == living;
   }

   public void updateRotations() {
      Object var133 = null;
      Object var134 = null;
      Object var143 = null;
      Object var144 = null;
      Object var162 = null;
      Object var170 = null;
      Object var171 = null;
      float var179 = 0.0F;
      Object var181 = null;
      Object var184 = null;
      Object var186 = null;
      var179 = this.rotationSpeed.wv().floatValue();
      if (!this.isAdvancedRotations()) {
         this.resetAdvancedState();
      }

      var133 = this.rotationMode.wo().getName();
      int var167_hi = -1;
      switch (((String)var133).hashCode()) {
         case -1631405611:
            if (((String)var133).equals("Autistic AntiCheat")) {
               var167_hi = 3;
            }
            break;
         case -654193598:
            if (((String)var133).equals("Advanced")) {
               var167_hi = 4;
            }
            break;
         case 77115:
            if (((String)var133).equals("NCP")) {
               var167_hi = 1;
            }
            break;
         case 2228079:
            if (((String)var133).equals("Grim")) {
               var167_hi = 5;
            }
            break;
         case 2581482:
            if (((String)var133).equals("Snap")) {
               var167_hi = 2;
            }
            break;
         case 1951303741:
            if (((String)var133).equals("Legit/Normal")) {
               var167_hi = 0;
            }
      }

      switch (var167_hi) {
         case 0:
            var171 = this.c(this.jE);
            var171 = this.applyKnockbackDisplacement(this.jE, (Vector2f)var171);
            if (var179 != 0.0F) {
               RotationComponent.a(
                  (Vector2f)var171, var179, this.movementCorrection.wo() == MovementFix.OFF ? MovementFix.OFF : this.movementCorrection.wo(), var1 -> {
                     inReach = RotationUtil.a(var1, this.jE, this.range.wo().doubleValue(), this.canHitThroughWalls(), this.getHitBoxExpand());
                     return inReach;
                  }, this.silentRotations.wo()
               );
            }
            break;
         case 1:
            int random2 = (int)(Math.random() * 1.0);
            var143 = new Vector3d(this.jE.posX, this.jE.posY, this.jE.posZ);
            var144 = new Vector3d(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);
            var162 = MoveUtil.a(this.jE, new Vector2f(0.0F, 1.0F), random2, aEg.thePlayer.isSprinting());
            this.jE.setPosition(((Vector3d)var162).x, ((Vector3d)var162).y, ((Vector3d)var162).z);
            aEg.thePlayer
               .setPosition(
                  aEg.thePlayer.posX + aEg.thePlayer.motionX * random2,
                  aEg.thePlayer.posY + (aEg.thePlayer.motionY + 0.17) * random2,
                  aEg.thePlayer.posZ + aEg.thePlayer.motionZ * random2
               );
            var181 = RotationUtil.m(this.c(this.jE));
            this.jE.setPosition(((Vector3d)var143).x, ((Vector3d)var143).y, ((Vector3d)var143).z);
            aEg.thePlayer.setPosition(((Vector3d)var144).x, ((Vector3d)var144).y, ((Vector3d)var144).z);
            var181 = this.applyKnockbackDisplacement(this.jE, (Vector2f)var181);
            if (var179 != 0.0F) {
               if (Math.random() > 0.1) {
                  RotationComponent.a(
                     (Vector2f)var181,
                     var179,
                     this.movementCorrection.wo() == MovementFix.OFF ? MovementFix.OFF : this.movementCorrection.wo(),
                     null,
                     this.silentRotations.wo()
                  );
               } else {
                  RotationComponent.a(
                     (Vector2f)var181,
                     var179,
                     this.movementCorrection.wo() == MovementFix.OFF ? MovementFix.OFF : this.movementCorrection.wo(),
                     null,
                     this.silentRotations.wo()
                  );
               }
            }
            break;
         case 2:
            var184 = this.applyKnockbackDisplacement(this.jE, this.c(this.jE));
            if (var179 != 0.0F && this.isAttackDue()) {
               RotationComponent.d(false);
               RotationComponent.a(
                  (Vector2f)var184,
                  var179,
                  this.movementCorrection.wo() == MovementFix.OFF ? MovementFix.OFF : this.movementCorrection.wo(),
                  null,
                  this.silentRotations.wo()
               );
            } else {
               RotationComponent.d(false);
            }
            break;
         case 3:
            var170 = this.applyKnockbackDisplacement(this.jE, this.c(this.jE));
            var134 = RayCastUtil.rayCast((Vector2f)var170, this.range.wo().floatValue(), this.getHitBoxExpand(), aEg.thePlayer, this.canHitThroughWalls());
            int flag = (MovingObjectPosition)var134 != null && ((MovingObjectPosition)var134).entityHit == this.jE ? 1 : 0;
            int var155_hi = 1;
            if (this.rayCast.wo()) {
               var186 = RayCastUtil.rayCast((Vector2f)var170, this.range.wo().floatValue(), this.getHitBoxExpand(), aEg.thePlayer, this.canHitThroughWalls());
               var155_hi = (MovingObjectPosition)var186 != null && ((MovingObjectPosition)var186).entityHit == this.jE ? 1 : 0;
            }

            if (var179 != 0.0F && this.isAttackDue() && flag != 0 && var155_hi != 0 && this.rayCast.wo()) {
               RotationComponent.d(false);
               RotationComponent.a(
                  (Vector2f)var170,
                  var179,
                  this.movementCorrection.wo() == MovementFix.OFF ? MovementFix.OFF : this.movementCorrection.wo(),
                  null,
                  this.silentRotations.wo()
               );
            } else {
               RotationComponent.d(false);
               RotationComponent.a(
                  new Vector2f(RotationComponent.fk.x + var179 * 10.0F, 90.0F),
                  var179 * 10.0F / 18.0F,
                  this.movementCorrection.wo() == MovementFix.OFF ? MovementFix.OFF : this.movementCorrection.wo(),
                  null,
                  this.silentRotations.wo()
               );
            }
            break;
         case 4:
            var186 = this.applyKnockbackDisplacement(this.jE, this.getPredictedRotations());
            if (var179 != 0.0F) {
               RotationComponent.a(
                  this.b((Vector2f)var186),
                  var179,
                  this.movementCorrection.wo() == MovementFix.OFF ? MovementFix.OFF : this.movementCorrection.wo(),
                  null,
                  this.silentRotations.wo()
               );
            }
            break;
         case 5:
            this.grimRotations = this.applyKnockbackDisplacement(this.jE, this.c(this.jE));
      }
   }

   public Vector2f applyKnockbackDisplacement(EntityLivingBase living, Vector2f vec2) {
      Object var14 = null;
      Object var15 = null;
      var14 = this.getDisplacementRejection(living);
      if ((String)var14 != null) {
         var15 = (String)var14;
         this.debugKnockback("rejected:" + (String)var15, this.knockbackPlan);
         return vec2;
      }
      this.debugKnockback("applied", this.knockbackPlan);
      return new Vector2f(this.knockbackPlan.yaw, vec2.getY());
   }

   public KnockbackSample sampleKnockback(EntityLivingBase living) {
      double var35 = 0.0;
      Object var40 = null;
      Object var44 = null;
      double var47 = 0.0;
      Object var49 = null;
      double var52 = 0.0;
      var44 = living.getEntityBoundingBox();
      KnockbackSample var3 = null;

      for (int i = 0; i < 32; i++) {
         var52 = (Math.PI * 2) * i / 32.0;
         var35 = -Math.sin(var52);
         var47 = Math.cos(var52);

         for (double var59 = 0.8; var59 <= 5.0; var59 += 0.35) {
            var49 = ((AxisAlignedBB)var44).offset(var35 * var59, 0.0, var47 * var59);
            var40 = this.evaluateKnockbackCandidate(living, (AxisAlignedBB)var49, var35, var47, var59);
            if ((KnockbackSample)var40 != null && (var3 == null || ((KnockbackSample)var40).score > var3.score)) {
               var3 = (KnockbackSample)var40;
            }

            if (!aEg.theWorld.getCollidingBoundingBoxes(living, ((AxisAlignedBB)var49).contract(0.02, 0.0, 0.02)).isEmpty()) {
               break;
            }
         }
      }

      return var3 != null && var3.score >= 45.0 ? var3 : null;
   }

   public boolean isAdvancedRotations() {
      boolean var10000;
      if (this.rotationMode.wo() != null && "Advanced".equals(this.rotationMode.wo().getName())) {
         var10000 = true;
      } else {
         byte var6 = -123;
         var6 = -124;
         boolean var8 = false;
         var10000 = var8;
      }

      return var10000;
   }
}
