package com.alan.clients.module.impl.combat;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.combat.velocity.WatchdogPredictionVelocity;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.SubMode;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.component.impl.render.ProgressBarComponent;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases={"module.combat.lagbreak.name", "Lag Range", "Fake Lag"}, description="module.combat.lagbreak.description", category=Category.COMBAT)
public class LagBreak
extends Module {
    public int ps;
    public int blinkTicks;
    private static final int RELEASE_TICKS = 6;
    private EntityOtherPlayerMP fakePlayer;
    private boolean pendingDispatch;
    private int blinkStartTick;
    private int releaseTicks;
    private int lastReleaseTick;
    private float releaseStartProgress;
    private float radius;
    private float lastRadius;
    private final NumberValue maxLagTicks = new NumberValue("Max Lag Ticks", this, (Number)9, (Number)1, (Number)40, (Number)1);
    private final NumberValue range = new NumberValue("Range", this, (Number)10, (Number)8, (Number)15, (Number)0.1);
    private final NumberValue minimumRange = new NumberValue("Minimum Range", this, (Number)0, (Number)0, (Number)3, (Number)0.1);
    private final ModeValue visualMode = new ModeValue("Visual Mode", this).add(new Mode[]{new SubMode("Percentage")}).add(new Mode[]{new SubMode("Circle")}).add(new Mode[]{new SubMode("Text")}).setDefault("Percentage");
    private final NumberValue circleRadius = new NumberValue("Circle Radius", this, (Number)16, (Number)8, (Number)40, (Number)1, () -> {
        if (this.isCircleMode()) return false;
        return true;
    });
    private final NumberValue circleThickness = new NumberValue("Circle Thickness", this, (Number)1.5, (Number)0.5, (Number)5, (Number)0.1, () -> {
        if (this.isCircleMode()) return false;
        return true;
    });
    private final BooleanValue dispatchOnAttack = new BooleanValue("Dispatch On Attack", (Module)this, (Boolean)true);
    private final BooleanValue dispatchOnHurtime = new BooleanValue("Dispatch On Hurtime", (Module)this, (Boolean)false);
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = preUpdateEvent -> {
        if (LagBreak.aEg.gameSettings.cgI.isKeyDown()) {
            this.cancelBlink();
            return;
        }
        if (this.blinkTicks > 0) {
            BlinkComponent.blink();
        }
        if (this.blinkTicks <= 0) {
            if (this.releaseTicks <= 0) return;
        }
        float f2 = this.getDisplayProgress();
        if (((Mode)this.visualMode.wo()).getName().equals("Percentage")) {
            ProgressBarComponent.a(f2, 0.75f, false, true, 10);
            return;
        }
        if (!this.isCircleMode()) return;
        this.updateRadius(f2);
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = preMotionEvent -> {
        EntityLivingBase entityLivingBase;
        if (LagBreak.aEg.gameSettings.cgI.isKeyDown()) {
            this.cancelBlink();
            return;
        }
        if (LagBreak.aEg.thePlayer == null || LagBreak.aEg.theWorld == null) {
            return;
        }
        this.blinkTicks = LagBreak.aEg.thePlayer.ticksExisted - this.blinkStartTick - 1;
        if (this.blinkTicks > 0 && this.releaseTicks > 0) {
            this.releaseTicks = 0;
            this.releaseStartProgress = 0.0f;
            this.lastReleaseTick = 0;
            ProgressBarComponent.cl();
        }
        boolean bl = (entityLivingBase = this.getNearestTarget(((Number)this.range.wo()).doubleValue())) != null && PlayerUtil.v((Entity)entityLivingBase) <= 3.0 + MoveUtil.speed() / 2.0;
        boolean bl2 = entityLivingBase != null && PlayerUtil.v((Entity)entityLivingBase) <= 5.0 && this.isLookingAtPlayer(entityLivingBase, 12.0f, 20.0f);
        int n2 = entityLivingBase != null ? (int)MathUtil.l(9.0, 16.0) : (int)MathUtil.l(10.0, 18.0);
        boolean bl3 = false;
        if (this.blinkTicks >= ((Number)this.maxLagTicks.wo()).intValue() || this.e(Scaffold.class).isEnabled() || LagBreak.aEg.gameSettings.cgI.isKeyDown() || !MoveUtil.isMoving() || entityLivingBase == null || PlayerUtil.v((Entity)entityLivingBase) > ((Number)this.range.wo()).doubleValue() || WatchdogPredictionVelocity.dj || LagBreak.aEg.thePlayer.Zl < 2) {
            bl3 = true;
        }
        if (!((Boolean)this.dispatchOnAttack.wo()).booleanValue()) {
            if (!bl3 && bl) {
                bl3 = true;
            }
            if (!bl3 && this.blinkTicks >= n2 && !bl2) {
                bl3 = true;
            }
        }
        if (bl3) {
            this.dispatchBlink();
        }
        if (entityLivingBase != null && PlayerUtil.v((Entity)entityLivingBase) < ((Number)this.minimumRange.wo()).doubleValue()) {
            this.dispatchBlink();
        }
        if (LagBreak.aEg.thePlayer.ae < 2 && ((Boolean)this.dispatchOnHurtime.wo()).booleanValue()) {
            this.dispatchBlink();
        }
        if (this.pendingDispatch) {
            this.dispatchBlink();
            this.pendingDispatch = false;
        }
    };
    @EventLink(value=1)
    public final Listener<PacketSendEvent> onPacketSend = packetSendEvent -> {
        if (!((Boolean)this.dispatchOnAttack.wo()).booleanValue() || this.blinkTicks <= 0 || LagBreak.aEg.thePlayer == null) {
            return;
        }
        Packet<?> packet = packetSendEvent.dq();
        if (packet instanceof C02PacketUseEntity && ((C02PacketUseEntity)packet).getAction() == C02PacketUseEntity.Action.ATTACK) {
            this.pendingDispatch = true;
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = worldChangeEvent -> {
        this.gi();
        this.releaseTicks = 0;
        this.lastReleaseTick = 0;
        this.releaseStartProgress = 0.0f;
        if (LagBreak.aEg.thePlayer != null) {
            this.blinkStartTick = LagBreak.aEg.thePlayer.ticksExisted;
        }
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        if (LagBreak.aEg.gameSettings.cgI.isKeyDown()) {
            this.cancelBlink();
            return;
        }
        if (this.blinkTicks <= 0) {
            if (this.releaseTicks <= 0) return;
        }
        block4: {
            float f2;
            block3: {
                {
                    f2 = this.getDisplayProgress();
                    String string = ((Mode)this.visualMode.wo()).getName();
                    switch (string) {
                        case "Percentage": {
                            ProgressBarComponent.a(f2, 0.75f, false, true, 10);
                            break block4;
                        }
                        case "Circle": {
                            break block3;
                        }
                        case "Text": {
                            break;
                        }
                        default: {
                            break block4;
                        }
                    }
                }
                this.drawBlinkText();
                break block4;
            }
            this.drawCircle(render2DEvent, f2);
        }
        if (this.releaseTicks > 0 && LagBreak.aEg.thePlayer != null && this.lastReleaseTick != LagBreak.aEg.thePlayer.ticksExisted) {
            this.lastReleaseTick = LagBreak.aEg.thePlayer.ticksExisted;
            --this.releaseTicks;
        }
    };

    @Override
    public void onEnable() {
        if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_13) && LastConnectionComponent.ip != null) {
            LastConnectionComponent.ip.contains("hypixel");
        }
        this.gi();
        this.releaseTicks = 0;
        this.lastReleaseTick = 0;
        this.releaseStartProgress = 0.0f;
        this.lastRadius = this.radius = ((Number)this.circleRadius.wo()).floatValue();
        if (LagBreak.aEg.thePlayer != null) {
            this.blinkStartTick = LagBreak.aEg.thePlayer.ticksExisted;
        }
    }

    @Override
    public void onDisable() {
        this.removeFakePlayer();
        BlinkComponent.dispatch();
    }

    public boolean isBlinking() {
        if (this.blinkTicks <= 0) return false;
        return true;
    }

    private void gi() {
        if (LagBreak.aEg.thePlayer == null) {
            return;
        }
    }

    private void removeFakePlayer() {
        if (this.fakePlayer != null) {
            Client.a.getBotManager().c(this, (Entity)this.fakePlayer);
            LagBreak.aEg.theWorld.removeEntityFromWorld(this.fakePlayer.getEntityId());
            this.fakePlayer = null;
        }
    }

    private void dispatchBlink() {
        if (this.blinkTicks > 0) {
            this.releaseStartProgress = this.getLagProgress();
            this.releaseTicks = 6;
            this.lastReleaseTick = 0;
        }
        BlinkComponent.dispatch();
        BlinkComponent.disable();
        if (LagBreak.aEg.thePlayer != null) {
            this.blinkStartTick = LagBreak.aEg.thePlayer.ticksExisted;
        }
        this.blinkTicks = 0;
        this.removeFakePlayer();
    }

    private void cancelBlink() {
        if (this.blinkTicks > 0) {
            BlinkComponent.dispatch();
            BlinkComponent.disable();
        }
        this.releaseTicks = 0;
        this.releaseStartProgress = 0.0f;
        this.lastReleaseTick = 0;
        this.blinkTicks = 0;
        this.pendingDispatch = false;
        if (LagBreak.aEg.thePlayer != null) {
            this.blinkStartTick = LagBreak.aEg.thePlayer.ticksExisted;
        }
        ProgressBarComponent.stop();
    }

    private boolean isCircleMode() {
        return ((Mode)this.visualMode.wo()).getName().equals("Circle");
    }

    private float getLagProgress() {
        int n2 = Math.max(1, ((Number)this.maxLagTicks.wo()).intValue() - 1);
        return MathHelper.clamp_float((float)((float)this.blinkTicks / (float)n2), (float)0.0f, (float)1.0f);
    }

    private float getDisplayProgress() {
        if (this.releaseTicks <= 0) {
            if (this.blinkTicks <= 0) return 0.0f;
            float f2 = this.getLagProgress();
            return f2;
        }
        float f3 = 1.0f - (float)(this.releaseTicks - 1) / 6.0f;
        float f4 = 1.0f - (float)Math.pow(1.0f - f3, 3.0);
        return MathHelper.clamp_float((float)(this.releaseStartProgress + (1.0f - this.releaseStartProgress) * f4), (float)0.0f, (float)1.0f);
    }

    private void drawBlinkText() {
        float f2 = (float)LagBreak.aEg.jY.getScaledWidth() / 2.0f;
        float f3 = (float)LagBreak.aEg.jY.getScaledHeight() / 2.0f;
        int n2 = this.rz().rA().getRGB();
        int n3 = new Color(0, 0, 0, 200).getRGB();
        FontManager.MAIN.a(17, FontWeight.LIGHT).drawString("Blinking: " + this.blinkTicks, f2 + 1.0f, f3 + 10.0f + 1.0f, n3);
        FontManager.MAIN.a(17, FontWeight.LIGHT).drawString("Blinking: " + this.blinkTicks, f2, f3 + 10.0f, n2);
    }

    private void drawCircle(Render2DEvent render2DEvent, float f2) {
        int scaledWidth = render2DEvent.getScaledResolution().getScaledWidth();
        int scaledHeight = render2DEvent.getScaledResolution().getScaledHeight();
        float f3 = (float)scaledWidth / 2.0f;
        float f4 = (float)scaledHeight / 2.0f;
        float f5 = MathUtil.lerp(this.lastRadius, this.radius, LagBreak.aEg.timer.bWm);
        double d2 = 360.0 * (double)f2;
        double d3 = 270.0 + d2;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate((int)770, (int)771, (int)1, (int)0);
        GL11.glLineWidth(((Number)this.circleThickness.wo()).floatValue());
        this.drawArc(f3, f4, f5, 270.0, d3, this.rz().rA(), 1.0f);
        GlStateManager.disableBlend();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
        GlStateManager.resetColor();
    }

    private void updateRadius(float f2) {
        float f3 = ((Number)this.circleRadius.wo()).floatValue() + f2 * 5.0f;
        this.lastRadius = this.radius;
        this.radius = MathUtil.lerp(this.radius, f3, 0.35f);
    }

    private void drawArc(float f2, float f3, float f4, double d2, double d3, Color color, float f5) {
        GL11.glBegin(3);
        GL11.glColor4f((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, f5);
        double d4 = d2 <= d3 ? 0.75 : -0.75;
        double d5 = d2;
        while (d4 > 0.0 ? d5 <= d3 : d5 >= d3) {
            double radians = Math.toRadians(d5);
            float f6 = f2 + (float)(Math.cos(radians) * (double)f4);
            float f7 = f3 + (float)(Math.sin(radians) * (double)f4);
            GL11.glVertex2f(f6, f7);
            d5 += d4;
        }
        double d7 = Math.toRadians(d3);
        GL11.glVertex2f(f2 + (float)(Math.cos(d7) * (double)f4), f3 + (float)(Math.sin(d7) * (double)f4));
        GL11.glEnd();
    }

    private EntityLivingBase getNearestTarget(double d2) {
        try {
            List<EntityLivingBase> list = TargetComponent.f(d2);
            if (list == null || list.isEmpty()) {
                return null;
            }
            list.sort(Comparator.comparingDouble(entityLivingBase -> PlayerUtil.v((Entity)entityLivingBase)));
            return list.get(0);
        }
        catch (Throwable throwable) {
            return null;
        }
    }

    private boolean isLookingAtPlayer(EntityLivingBase entityLivingBase, float f2, float f3) {
        double dx = LagBreak.aEg.thePlayer.posX - entityLivingBase.posX;
        double dz = LagBreak.aEg.thePlayer.posZ - entityLivingBase.posZ;
        double d4 = LagBreak.aEg.thePlayer.posY + (double)LagBreak.aEg.thePlayer.getEyeHeight() - (entityLivingBase.posY + (double)entityLivingBase.getEyeHeight());
        double d5 = Math.sqrt(dx * dx + dz * dz);
        float f4 = (float)(Math.toDegrees(Math.atan2(dz, dx)) - 90.0);
        float f5 = (float)(-Math.toDegrees(Math.atan2(d4, d5)));
        float f6 = Math.abs(MathHelper.wrapAngleTo180_float((float)(entityLivingBase.pl - f4)));
        float f7 = Math.abs(MathHelper.wrapAngleTo180_float((float)(entityLivingBase.rotationPitch - f5)));
        if (!(f6 <= f2)) return false;
        if (!(f7 <= f3)) return false;
        return true;
    }
}
