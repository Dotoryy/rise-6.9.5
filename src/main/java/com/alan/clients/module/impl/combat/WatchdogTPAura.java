package com.alan.clients.module.impl.combat;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.rotation.RotationUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.m;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MathHelper;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases={"Watchdog TP Aura", "WatchdogTPAura"}, description="Teleport-based aura for Hypixel Watchdog", category=Category.COMBAT)
public final class WatchdogTPAura
extends Module {
    private final NumberValue range = new NumberValue("Range", this, (Number)20, (Number)6, (Number)20, (Number)1);
    private final BooleanValue players = new BooleanValue("Players", (Module)this, (Boolean)true);
    private final BooleanValue hostile = new BooleanValue("Hostile", (Module)this, (Boolean)false);
    private final BooleanValue teammates = new BooleanValue("Teammates", (Module)this, (Boolean)false);
    private final BooleanValue invisibles = new BooleanValue("Invisibles", (Module)this, (Boolean)true);
    private boolean skipNextTick = true;
    private boolean blinking = false;
    private EntityLivingBase target;
    private EntityLivingBase pendingAttackTarget;
    private int blinkTicks;
    private final StopWatch blinkTimer = new StopWatch();
    private final ConcurrentLinkedQueue<Packet<?>> heldPackets = new ConcurrentLinkedQueue();
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = preMotionEvent -> {
        if (this.skipNextTick) {
            this.skipNextTick = false;
            return;
        }
        this.updateTarget();
        if (this.target == null) {
            return;
        }
        if (this.blinking) {
            preMotionEvent.setPosX(this.target.posX);
            preMotionEvent.setPosY(this.target.posY);
            preMotionEvent.setPosZ(this.target.posZ);
            this.pendingAttackTarget = this.target;
            Vector2f vector2f = RotationUtil.y((Entity)this.target);
            preMotionEvent.setYaw(vector2f.x);
            preMotionEvent.setPitch(vector2f.y);
        } else {
            preMotionEvent.setPosX(0.0);
            preMotionEvent.setPosY(0.0);
            preMotionEvent.setPosZ(0.0);
        }
        WatchdogTPAura.aEg.thePlayer.positionUpdateTicks = 20;
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = packetReceiveEvent -> {
        if (packetReceiveEvent.getPacket() instanceof S08PacketPlayerPosLook && !this.blinking) {
            this.startBlink();
        }
    };
    @EventLink(value=0)
    public final Listener<PacketSendEvent> onPacketSend = packetSendEvent -> {
        if (this.blinking && !packetSendEvent.isCancelled()) {
            Packet<?> packet = packetSendEvent.dq();
            this.heldPackets.add(packet);
            packetSendEvent.setCancelled();
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = worldChangeEvent -> {
        if (this.blinking) {
            this.stopBlink();
        }
        this.heldPackets.clear();
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = preUpdateEvent -> {
        if (this.pendingAttackTarget != null && this.blinking) {
            WatchdogTPAura.aEg.playerController.syncCurrentPlayItem();
            AttackEvent attackEvent = new AttackEvent(this.pendingAttackTarget);
            Client.a.e().d(attackEvent);
            if (!attackEvent.isCancelled()) {
                PacketUtil.send(new m());
                ChatUtil.c("attacked", new Object[0]);
                PacketUtil.send(new C02PacketUseEntity((Entity)this.pendingAttackTarget, C02PacketUseEntity.Action.ATTACK));
            }
            this.pendingAttackTarget = null;
        }
        if (this.blinking && ++this.blinkTicks > 1) {
            this.stopBlink();
        }
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        if (this.target != null && this.blinking) {
            ScaledResolution scaledResolution = new ScaledResolution(aEg);
            String string = this.target.getName();
            float f2 = this.target.getHealth();
            float f3 = this.target.getMaxHealth();
            int n2 = (int)(f2 / f3 * 100.0f);
            String string2 = String.format("Target: %s [%d%%]", string, n2);
            int n3 = WatchdogTPAura.aEg.fontRendererObj.getStringWidth(string2);
            WatchdogTPAura.aEg.fontRendererObj.b(string2, (float)scaledResolution.getScaledWidth() / 2.0f - (float)n3 / 2.0f, (float)scaledResolution.getScaledHeight() / 2.0f + 20.0f, -1);
        }
    };

    private void updateTarget() {
        List<EntityLivingBase> list = TargetComponent.a(((Number)this.range.wo()).doubleValue(), (boolean)((Boolean)this.players.wo()), (boolean)((Boolean)this.invisibles.wo()), false, (boolean)((Boolean)this.hostile.wo()), (Boolean)this.teammates.wo());
        if (list.isEmpty()) {
            this.target = null;
            return;
        }
        list.sort(Comparator.comparingDouble(entityLivingBase -> {
            Vector2f vector2f = RotationUtil.y((Entity)entityLivingBase);
            float f2 = Math.abs(MathHelper.wrapAngleTo180_float((float)(vector2f.x - WatchdogTPAura.aEg.thePlayer.pl)));
            float f3 = Math.abs(MathHelper.wrapAngleTo180_float((float)(vector2f.y - WatchdogTPAura.aEg.thePlayer.rotationPitch)));
            return Math.sqrt(f2 * f2 + f3 * f3);
        }));
        this.target = list.get(0);
    }

    private void startBlink() {
        if (!this.blinking) {
            this.blinking = true;
            this.blinkTicks = 0;
            this.heldPackets.clear();
            this.blinkTimer.aX();
            ChatUtil.c("Started blinking", new Object[0]);
        }
    }

    private void stopBlink() {
        if (this.blinking) {
            this.blinking = false;
            ChatUtil.c("Dispatching " + this.heldPackets.size() + " packets", new Object[0]);
            while (!this.heldPackets.isEmpty()) {
                PacketUtil.sendNoEvent(this.heldPackets.poll());
            }
            this.skipNextTick = false;
            ChatUtil.c("Stopped blinking", new Object[0]);
        }
    }

    @Override
    public void onEnable() {
        ChatUtil.b("CREDIT TO https://youtube.com/@authh FOR THIS GOD BYPASS", new Object[0]);
        this.skipNextTick = true;
        this.blinking = false;
        this.target = null;
        this.pendingAttackTarget = null;
        this.blinkTicks = 0;
        this.heldPackets.clear();
    }

    @Override
    public void onDisable() {
        if (this.blinking) {
            this.stopBlink();
        }
        this.heldPackets.clear();
        this.target = null;
        this.pendingAttackTarget = null;
        this.blinkTicks = 0;
        this.skipNextTick = true;
    }
}
