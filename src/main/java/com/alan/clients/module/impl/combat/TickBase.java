package com.alan.clients.module.impl.combat;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.PingSpoofComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.player.MoveUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.player.BadPacketsComponent;
import com.alan.clients.component.impl.combat.TargetComponent;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.opengl.GL11;

@ModuleInfo(aliases = "module.combat.tickbase.name", description = "module.combat.legitreach.description", category = Category.COMBAT)
public final class TickBase extends Module {
    private ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Post")).setDefault("Legit");
    private Vector3d trackedPosition = new Vector3d(0.0, 0.0, 0.0);
    private int cooldownTicks = 0;
    private int ticksToSkip;
    private int skipDelayTicks;
    @EventLink
    Listener<TickEvent> onTick = var1 -> {
        {
            String s = this.mode.wo().getName();
            switch (s) {
                case "Post":
                    if (aEg.thePlayer.ticksExisted <= 20) {
                        aEg.timer.dzD = 0.5F;
                    }

                    this.cooldownTicks--;
                    if (this.ticksToSkip > 0) {
                        if (this.skipDelayTicks > 0) {
                            this.skipDelayTicks--;
                        } else {
                            var1.setCancelled();
                            aEg.timer.cancel();
                            this.ticksToSkip--;
                        }
                    }

                    List list = TargetComponent.f(6.0);
                    if (list.isEmpty()) {
                        return;
                    }

                    EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(0);
                    this.trackedPosition = this.predictPosition(entitylivingbase);
                    if (aEg.thePlayer.getDistance(this.trackedPosition.x, this.trackedPosition.y, this.trackedPosition.z) < 8.0 && PlayerUtil.v(entitylivingbase) > 3.0 && this.cooldownTicks < 0) {
                        int i = this.getTicksToReach(entitylivingbase);
                        if (i == -1) {
                            return;
                        }

                        aEg.timer.elapsedTicks += i;
                        this.ticksToSkip += i;
                        this.skipDelayTicks += i + 5;
                        this.cooldownTicks = 10;
                    }

                    return;
                case "Legit":
                    break;
                default:
                    return;
            }
        }

        EntityLivingBase entitylivingbase1 = TargetComponent.e(8.0);
        if (entitylivingbase1 != null
            && !BadPacketsComponent.bad(false, true, false, false, false)
            && this.trackedPosition.g(entitylivingbase1.Ty()) >= aEg.thePlayer.Ty().g(entitylivingbase1.Ty())) {
            BlinkComponent.blink();
        }
    };
    @EventLink
    Listener<PacketSendEvent> onPacketSend = var1 -> {
        Packet packet = var1.dq();
        if (!var1.isCancelled() && packet instanceof C03PacketPlayer) {
            this.trackedPosition = new Vector3d(((C03PacketPlayer)packet).getX(), ((C03PacketPlayer)packet).getY(), ((C03PacketPlayer)packet).getZ());
        }
    };
    @EventLink
    Listener<Render3DEvent> onRender3D = var1 -> {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDepthMask(false);
        RenderUtil.color(ColorUtil.withAlpha(this.rz().rA(), 50));
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GL11.glDepthMask(true);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    };

    public TickBase() {
    }

    int getTicksToReach(Entity entity) {
        byte b0 = 10;
        Vector3d aka = new Vector3d(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ);

        int i;
        for (i = 0; i < b0 && entity.getDistance(aka.x, aka.y, aka.z) > 4.4; i++) {
            aka = MoveUtil.a(
                aka,
                new Vector3d(aEg.thePlayer.motionX, 0.0, aEg.thePlayer.motionZ),
                (float)Math.toDegrees(MoveUtil.direction()),
                new Vector2f(aEg.thePlayer.moveStrafing, aEg.thePlayer.moveForward)
            );
        }

        return i == b0 ? -1 : i;
    }

    Vector3d predictPosition(Entity entity) {
        Vector3d aka = new Vector3d(entity.posX, entity.posY, entity.posZ);

        for (int i = 0; i <= PingSpoofComponent.getPing() / 50L; i++) {
            double dx = entity.posX - entity.lastTickPosX;
            double dz = entity.posZ - entity.lastTickPosZ;
            int j = dz == 0.0 && dx == 0.0 ? 0 : 1;
            aka = MoveUtil.a(aka, new Vector3d(dx, 0.0, dz), entity.pl, new Vector2f(0.0F, j));
        }

        return aka;
    }
}
