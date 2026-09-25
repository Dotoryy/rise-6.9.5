package com.alan.clients.module.impl.combat;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.ClickEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.viaversion.viabackwards.protocol.v1_21_2to1_21.Protocol1_21_2To1_21;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.protocols.v1_21to1_21_2.packet.ServerboundPackets1_21_2;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.pathfinding.unlegit.MainPathFinder;
import com.alan.clients.util.pathfinding.unlegit.Vec3;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.module.impl.combat.TeleportAuraSwitchMap;
import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.item.bw;
import net.minecraft.item.cn;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.potion.Potion;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(
    aliases = {"module.combat.teleportaura.name", "Infinite Aura", "Infinite", "TP Aura"},
    description = "module.combat.teleportaura.description",
    category = Category.COMBAT
)
public final class TeleportAura extends Module {
    private final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Single")).add(new SubMode("Multiple")).setDefault("Single");
    private final NumberValue range = new NumberValue("Range", this, 32, 3, 100, 0.1);
    private final BoundsNumberValue cps = new BoundsNumberValue("CPS", this, 10, 15, 1, 20, 1);
    private final BooleanValue cooldown19 = new BooleanValue("1.9 Cooldown", this, false);
    private final BooleanValue render = new BooleanValue("Render", this, true);
    private final StopWatch clickStopWatch = new StopWatch();
    private KillAura killAura;
    private List<Vec3> path;
    public EntityLivingBase target;
    private long nextSwing;
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = var1 -> {
        if (this.killAura == null) {
            this.killAura = this.e(KillAura.class);
        }

        List list = TargetComponent.a(this.range.wo().doubleValue(), this.killAura.player.wo(), this.killAura.invisibles.wo(), this.killAura.animals.wo(), this.killAura.mobs.wo(), this.killAura.playerTeammates.wo());
        if (list.isEmpty()) {
            this.target = null;
        } else {
            list.sort(Comparator.comparingDouble(var0 -> aEg.thePlayer.getDistanceToEntity((Entity)var0)));
            this.target = (EntityLivingBase)list.get(0);
            if (this.target != null && !aEg.thePlayer.isDead) {
                this.doAttack(list);
            }
        }
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        if (this.render.wo() && this.path != null && this.target != null) {
            Vec3 ahyx = null;

            for (Vec3 ahyx2 : this.path) {
                if (ahyx != null) {
                    RenderUtil.drawLine(ahyx.getX(), ahyx.getY() + 0.01, ahyx.getZ(), ahyx2.getX(), ahyx2.getY() + 0.01, ahyx2.getZ(), Color.WHITE, 1.0F);
                }

                ahyx = ahyx2;
            }
        }
    };

    public TeleportAura() {
    }

    @Override
    public void onDisable() {
        this.target = null;
    }

    private void doAttack(List<EntityLivingBase> livings) {
        boolean flag;
        if (this.cooldown19.wo()) {
            double d0 = 4.0;
            if (aEg.thePlayer.getHeldItem() != null) {
                Item item = aEg.thePlayer.getHeldItem().getItem();
                if (item instanceof ItemSword) {
                    d0 = 1.6;
                } else if (item instanceof cn) {
                    d0 = 1.0;
                } else if (item instanceof bw) {
                    d0 = 1.2;
                } else if (item instanceof ItemAxe) {
                    switch (((ItemAxe)item).getToolMaterial()) {
                        case WOOD:
                        case STONE:
                            d0 = 0.8;
                            break;
                        case IRON:
                            d0 = 0.9;
                            break;
                        case EMERALD:
                        default:
                            d0 = 1.0;
                    }
                } else if (item instanceof ItemHoe) {
                    switch (((ItemHoe)item).getToolMaterial()) {
                        case WOOD:
                        case GOLD:
                            d0 = 1.0;
                            break;
                        case STONE:
                            d0 = 2.0;
                            break;
                        case IRON:
                            d0 = 3.0;
                            break;
                        case EMERALD:
                        default:
                            d0 = 4.0;
                    }
                }
            }

            double d1 = 1.0 / d0 * 20.0 - 1.0;
            flag = this.clickStopWatch.T((long)(d1 * 50.0));
        } else {
            flag = this.clickStopWatch.T(this.nextSwing);
        }

        if (flag && this.target != null && !aEg.gameSettings.cgK.isKeyDown() && !aEg.gameSettings.cgI.isKeyDown()) {
            if (!this.cooldown19.wo()) {
                long i = Math.round(MathUtil.l(this.cps.wo().intValue(), this.cps.wA().intValue()));
                this.nextSwing = 1000L / i;
            }

            label66: {
                double d2;
                {
                    d2 = this.range.wo().doubleValue();
                    String s = this.mode.wo().getName();
                    switch (s) {
                        case "Single":
                            if (aEg.thePlayer.getDistanceToEntity(this.target) <= d2) {
                                this.attack(this.target);
                            }
                            break label66;
                        case "Multiple":
                            break;
                        default:
                            break label66;
                    }
                }

                livings.removeIf(var2 -> aEg.thePlayer.getDistanceToEntity(var2) > d2);
                if (!livings.isEmpty()) {
                    livings.forEach(this::attack);
                }
            }

            this.clickStopWatch.aX();
        }
    }

    private void attack(EntityLivingBase living) {
        aEg.playerController.syncCurrentPlayItem();
        AttackEvent attackevent = new AttackEvent(living);
        Client.a.e().d(attackevent);
        if (!attackevent.isCancelled()) {
            EntityLivingBase entitylivingbase = attackevent.getLiving();
            this.path = MainPathFinder.a(
                new Vec3(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ),
                new Vec3(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ),
                true
            );
            if (this.path != null) {
                for (Vec3 ahy : this.path) {
                    PacketUtil.sendNoEvent(new C04PacketPlayerPosition(ahy.getX(), ahy.getY(), ahy.getZ(), true));
                    if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_21_2)
                        && aEg.thePlayer != null
                        && aEg.theWorld != null
                        && Via.getManager().getConnectionManager() != null
                        && !Via.getManager().getConnectionManager().getConnections().isEmpty()) {
                        PacketWrapper.create(
                                ServerboundPackets1_21_2.CLIENT_TICK_END, null, Via.getManager().getConnectionManager().getConnections().iterator().next()
                            )
                            .sendToServer(Protocol1_21_2To1_21.class);
                    }
                }

                if (ViaLoadingBase.getInstance().getTargetVersion().olderThanOrEqualTo(ProtocolVersion.v1_8)) {
                    Client.a.e().d(new ClickEvent());
                    aEg.thePlayer.swingItem();
                }

                PacketUtil.sendNoEvent(new C02PacketUseEntity(entitylivingbase, Action.ATTACK));
                if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
                    Client.a.e().d(new ClickEvent());
                    aEg.thePlayer.swingItem();
                }

                Collections.reverse(this.path);

                for (Vec3 ahyx : this.path) {
                    PacketUtil.sendNoEvent(new C04PacketPlayerPosition(ahyx.getX(), ahyx.getY(), ahyx.getZ(), true));
                    if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_21_2)
                        && aEg.thePlayer != null
                        && aEg.theWorld != null
                        && Via.getManager().getConnectionManager() != null
                        && !Via.getManager().getConnectionManager().getConnections().isEmpty()) {
                        PacketWrapper.create(
                                ServerboundPackets1_21_2.CLIENT_TICK_END, null, Via.getManager().getConnectionManager().getConnections().iterator().next()
                            )
                            .sendToServer(Protocol1_21_2To1_21.class);
                    }
                }

                if (this.e(Criticals.class).isEnabled()
                    || aEg.thePlayer.fallDistance > 0.0F
                        && !aEg.thePlayer.onGround
                        && !aEg.thePlayer.isOnLadder()
                        && !aEg.thePlayer.isInWater()
                        && !aEg.thePlayer.isPotionActive(Potion.blindness)
                        && aEg.thePlayer.ridingEntity == null) {
                    aEg.thePlayer.onCriticalHit(entitylivingbase);
                }
            }
        }
    }
}
