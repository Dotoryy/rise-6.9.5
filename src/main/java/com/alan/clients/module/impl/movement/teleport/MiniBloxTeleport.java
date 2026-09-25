package com.alan.clients.module.impl.movement.teleport;

import com.alan.clients.module.impl.movement.Teleport;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreUpdateEvent;
import com.alan.clients.newevent.impl.motion.PushOutOfBlockEvent;
import com.alan.clients.newevent.impl.other.BlockAABBEvent;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.pathfinding.unlegit.MainPathFinder;
import com.alan.clients.util.pathfinding.unlegit.Vec3;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.module.api.DevelopmentFeature;
import java.awt.Color;
import java.util.List;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.opengl.GL11;

@DevelopmentFeature
public final class MiniBloxTeleport
extends Mode<Teleport> {
    private Vec3 targetPosition = new Vec3(0.0, 0.0, 0.0);
    @EventLink
    public final Listener<TeleportEvent> onTeleport = teleportEvent -> this.toggle();
    @EventLink
    public final Listener<PushOutOfBlockEvent> onPushOutOfBlock = CancellableEvent::setCancelled;
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> FontManager.MAIN.a(17, FontWeight.LIGHT).drawString("hold sneak to teleport", (float)MiniBloxTeleport.aEg.jY.getScaledWidth() / 2.0f, (float)MiniBloxTeleport.aEg.jY.getScaledHeight() / 2.0f + 30.0f, this.rz().rA().getRGB());
    @EventLink
    public final Listener<Render3DEvent> onRender3D = render3DEvent -> {
        float f2 = MiniBloxTeleport.aEg.timer.bWm;
        EntityPlayerSP entityPlayerSP = MiniBloxTeleport.aEg.thePlayer;
        if (entityPlayerSP == null) {
            return;
        }
        double d2 = Math.toRadians(entityPlayerSP.prevRotationYaw + (entityPlayerSP.pl - entityPlayerSP.prevRotationYaw) * f2);
        double d3 = (double)(MiniBloxTeleport.aEg.thePlayer.rotationPitch + 2.0f) * 1.8;
        double d4 = entityPlayerSP.prevPosX + (entityPlayerSP.posX - entityPlayerSP.prevPosX) * (double)f2 - Math.sin(d2) * d3;
        double d5 = entityPlayerSP.prevPosY + (entityPlayerSP.posY - entityPlayerSP.prevPosY) * (double)f2;
        double d6 = entityPlayerSP.prevPosZ + (entityPlayerSP.posZ - entityPlayerSP.prevPosZ) * (double)f2 + Math.cos(d2) * d3;
        this.targetPosition = new Vec3(d4, d5, d6);
        Color color = ColorUtil.withAlpha(this.rz().rA(), 100);
        if (color.getAlpha() <= 0) {
            return;
        }
        RenderUtil.color(color);
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GL11.glDepthMask(false);
        double d7 = 0.14;
        RenderUtil.drawBoundingBox(entityPlayerSP.getEntityBoundingBox().offset(-entityPlayerSP.posX, -entityPlayerSP.posY, -entityPlayerSP.posZ).offset(d4, d5, d6).expand(d7, d7, d7));
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GL11.glDepthMask(true);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
        RenderHelper.disableStandardItemLighting();
        MiniBloxTeleport.aEg.entityRenderer.IU();
    };
    @EventLink
    public final Listener<PreUpdateEvent> onPreUpdate = preUpdateEvent -> {
        if (!MiniBloxTeleport.aEg.gameSettings.keyBindSneak.isKeyDown()) {
            return;
        }
        List<Vec3> list = MainPathFinder.a(new Vec3(MiniBloxTeleport.aEg.thePlayer.posX, MiniBloxTeleport.aEg.thePlayer.posY, MiniBloxTeleport.aEg.thePlayer.posZ), this.targetPosition, true);
        if (list == null || list.isEmpty()) {
            return;
        }
        if (MiniBloxTeleport.aEg.thePlayer.onGround) {
            MiniBloxTeleport.aEg.thePlayer.setPosition(MiniBloxTeleport.aEg.thePlayer.posX, MiniBloxTeleport.aEg.thePlayer.posY - 1.0E-4, MiniBloxTeleport.aEg.thePlayer.posZ);
        }
        for (Vec3 ahy2 : list) {
            PacketUtil.sendNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(ahy2.getX(), ahy2.getY(), ahy2.getZ(), true));
        }
    };
    @EventLink
    public final Listener<BlockAABBEvent> onBlockAABB = blockAABBEvent -> {
        if (blockAABBEvent.getBlock() instanceof BlockAir) {
            double unused1 = blockAABBEvent.getBlockPos().getX();
            double d2 = blockAABBEvent.getBlockPos().getY();
            double unused2 = blockAABBEvent.getBlockPos().getZ();
            double d3 = d2 - MiniBloxTeleport.aEg.thePlayer.posY;
            int unused3 = d3 == 0.0 ? 0 : (d3 < 0.0 ? -1 : 1);
        }
    };

    public MiniBloxTeleport(String string, Teleport teleport) {
        super(string, teleport);
    }
}
