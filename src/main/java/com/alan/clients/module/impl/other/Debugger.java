package com.alan.clients.module.impl.other;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.interfaces.ExecutorAccess;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.component.impl.render.ESPComponent;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.util.EnumChatFormatting;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.other.debugger.name", category = Category.PLAYER, description = "module.other.debugger.description")
public final class Debugger extends Module implements ExecutorAccess {
    private final BooleanValue transaction = new BooleanValue("Transactions", this, true);
    private final BooleanValue keepAlive = new BooleanValue("Keep Alive", this, true);
    private final BooleanValue teleport = new BooleanValue("Teleport", this, true);
    private final BooleanValue velocity = new BooleanValue("Velocity", this, true);
    private final BooleanValue abilities = new BooleanValue("Abilities", this, true);
    private final BooleanValue displayAll = new BooleanValue("Display All", this, true);
    private final BooleanValue blacklist = new BooleanValue("Blacklist", this, true, () -> !this.displayAll.wo());
    private final BooleanValue timeSinceMove = new BooleanValue("Time Since Move", this, true, () -> !this.displayAll.wo());
    private final BooleanValue devPanel = new BooleanValue("Dev Panel", this, false);
    private final BooleanValue eventCalls = new BooleanValue("Event Calls", this, false);
    private final DragValue position = new DragValue("", this, new Vector2d(200.0, 200.0), true);
    private final DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final ArrayList<String> debugLines = new ArrayList<>();
    public static HashMap<String, Integer> calls = new HashMap<>();
    private long threadLag;
    private boolean measuring;
    private StopWatch stopWatch = new StopWatch();
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1 -> {};
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceiveEvent = var1 -> {
        Packet packet = var1.getPacket();
        if (this.transaction.wo() && packet instanceof S32PacketConfirmTransaction s32packetconfirmtransaction) {
            ChatUtil.b(
                EnumChatFormatting.RED + " Transaction " + EnumChatFormatting.RESET + " (ID: %s)   (WindowID: %s)",
                s32packetconfirmtransaction.actionNumber,
                s32packetconfirmtransaction.windowId
            );
        } else if (this.keepAlive.wo() && packet instanceof net.minecraft.network.play.server.a a) {
            ChatUtil.b(EnumChatFormatting.GREEN + " Keep Alive " + EnumChatFormatting.RESET + " (ID: %s)", a.func_149134_c());
        } else if (this.teleport.wo() && packet instanceof S08PacketPlayerPosLook s08packetplayerposlook) {
            ChatUtil.b(
                EnumChatFormatting.BLUE + " Server Teleport " + EnumChatFormatting.RESET + " (Position: %s)",
                MathUtil.round(s08packetplayerposlook.x, 3) + " " + MathUtil.round(s08packetplayerposlook.y, 3) + " " + MathUtil.round(s08packetplayerposlook.z, 3)
            );
        } else if (this.velocity.wo() && packet instanceof S12PacketEntityVelocity s12packetentityvelocity) {
            if (s12packetentityvelocity.getEntityID() == aEg.thePlayer.getEntityId()) {
                ChatUtil.b(
                    EnumChatFormatting.LIGHT_PURPLE + " Velocity " + EnumChatFormatting.RESET + " (DeltaX: %s) (DeltaY: %s)  (DeltaZ: %s) ",
                    s12packetentityvelocity.motionX / 8000.0,
                    s12packetentityvelocity.motionY / 8000.0,
                    s12packetentityvelocity.motionZ / 8000.0
                );
            }
        } else if (this.velocity.wo() && packet instanceof S27PacketExplosion s27packetexplosion) {
            ChatUtil.b(
                EnumChatFormatting.LIGHT_PURPLE + " Explosion (Velocity) " + EnumChatFormatting.RESET + " (DeltaX: %s) (DeltaY: %s)  (DeltaZ: %s) ",
                s27packetexplosion.func_149149_c(),
                s27packetexplosion.func_149144_d(),
                s27packetexplosion.func_149147_e()
            );
        } else if (this.abilities.wo() && packet instanceof S39PacketPlayerAbilities s39packetplayerabilities) {
            ChatUtil.b(
                EnumChatFormatting.YELLOW
                    + " Abilities "
                    + s39packetplayerabilities.getFlySpeed()
                    + " "
                    + s39packetplayerabilities.isAllowFlying()
                    + " "
                    + s39packetplayerabilities.isFlying()
            );
        }
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (this.devPanel.wo()) {
            double d0 = 10.0;
            this.position.aHe = new Vector2d(180.0, 207.0);
            this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                double d1 = this.position.apP.x;
                double d2 = this.position.apP.y;
                double d3 = this.position.aHe.x;
                double d4 = this.position.aHe.y;
                double d5 = this.rz().getRound();
                this.rz();
                RenderUtil.roundedRectangle(d1, d2, d3, d4, d5, Themes.rK());
            });
            this.b(ShaderQueueType.BLUR)
                .c(
                    () -> RenderUtil.roundedRectangle(
                        this.position.apP.x, this.position.apP.y, this.position.aHe.x, this.position.aHe.y, this.rz().getRound(), Color.BLACK
                    )
                );
            this.b(ShaderQueueType.BLOOM)
                .c(
                    () -> RenderUtil.roundedRectangle(
                        this.position.apP.x, this.position.apP.y, this.position.aHe.x, this.position.aHe.y, this.rz().getRound(), this.rz().rE()
                    )
                );
            this.b(ShaderQueueType.REGULAR, 1)
                .c(
                    () -> {
                        aEg.fontRendererObj
                            .b(
                                Client.b + " 6 INDEV " + this.date.format(LocalDateTime.now()),
                                this.position.apP.x + d0,
                                this.position.apP.y + d0,
                                new Color(255, 255, 0).getRGB()
                            );
                        aEg.fontRendererObj
                            .b(
                                "FPS: " + Minecraft.getDebugFPS() + " [target " + aEg.getLimitFramerate() + "]",
                                this.position.apP.x + d0,
                                this.position.apP.y + d0 * 2.0,
                                new Color(255, 255, 0).getRGB()
                            );
                        aEg.fontRendererObj.a("Debugger", this.position.apP.x + d0, this.position.apP.y + d0 * 4.0, this.rz().rA().hashCode());
                        aEg.fontRendererObj
                            .a("Hidden due to not in dev mode", this.position.apP.x + d0, this.position.apP.y + d0 * 5.0, Color.WHITE.hashCode());
                        aEg.fontRendererObj
                            .a("Hidden due to not in dev mode", this.position.apP.x + d0, this.position.apP.y + d0 * 6.0, Color.WHITE.hashCode());
                        aEg.fontRendererObj.a("ESPs Amount: " + ESPComponent.esps.size(), this.position.apP.x + d0, this.position.apP.y + d0 * 8.0, Color.WHITE.hashCode());
                        aEg.fontRendererObj.a("Performance", this.position.apP.x + d0, this.position.apP.y + d0 * 9.0, this.rz().rA().hashCode());
                        aEg.fontRendererObj
                            .a("ThreadLag: " + this.threadLag, this.position.apP.x + d0, this.position.apP.y + d0 * 16.0, Color.WHITE.hashCode());
                        aEg.fontRendererObj.a("Other", this.position.apP.x + d0, this.position.apP.y + d0 * 18.0, this.rz().rA().hashCode());
                        aEg.fontRendererObj.a("Timer: " + aEg.timer.dzD, this.position.apP.x + d0, this.position.apP.y + d0 * 19.0, Color.WHITE.hashCode());
                    }
                );
        }
    };
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (!this.measuring) {
            long i = System.currentTimeMillis();
            this.measuring = true;
            boolean flag = aEg.thePlayer.ticksExisted % 100 == 0 && this.eventCalls.wo();
            aMR.execute(() -> {
                this.threadLag = System.currentTimeMillis() - i;
                this.measuring = false;
                if (flag) {
                    ChatUtil.b("Displaying Calls: ");

                    for (String s : calls.keySet()) {
                        ChatUtil.b(s + ": " + calls.get(s));
                    }

                    calls.clear();
                }
            });
        }
    };

    public Debugger() {
    }

    @Override
    public void onEnable() {
        this.debugLines.clear();
    }
}
