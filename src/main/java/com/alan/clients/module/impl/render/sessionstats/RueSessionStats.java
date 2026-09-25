package com.alan.clients.module.impl.render.sessionstats;

import com.alan.clients.module.impl.render.SessionStats;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.KillEvent;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.render.sessionstats.RueSessionStatsData;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.StringUtils;

public final class RueSessionStats extends Mode<SessionStats> {
    private final DragValue position = this.getParent().getPosition();
    private RueSessionStatsData session = new RueSessionStatsData(0, 0, 0, 0, 0.0, 0.0);
    private String time = "0 seconds";
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.ticksExisted % 20 == 0) {
            long i = System.currentTimeMillis() - this.session.startTime;
            long j = TimeUnit.MILLISECONDS.toHours(i);
            long k = TimeUnit.MILLISECONDS.toMinutes(i) % 60L;
            long l = TimeUnit.MILLISECONDS.toSeconds(i) % 60L;
            String s = "";
            if (j > 0L) {
                s = s + j + " " + (j == 1L ? Localization.ce("ui.sessionstats.hour") : Localization.ce("ui.sessionstats.hours")) + (k == 0L ? "" : " ");
            }

            if (k > 0L) {
                s = s + k + " " + (k == 1L ? Localization.ce("ui.sessionstats.minute") : Localization.ce("ui.sessionstats.minutes")) + (l != 0L && j <= 0L ? " " : "");
            }

            if (l > 0L && j == 0L) {
                s = s + l + " " + (l == 1L ? Localization.ce("ui.sessionstats.second") : Localization.ce("ui.sessionstats.seconds"));
            }

            this.time = s;
        }
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        double d0 = 8.0;
        this.position.aHe = new Vector2d(130.0, 55.0);
        if (!aEg.gameSettings.bJf) {
            this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.roundedRectangle(this.position.apP.x, this.position.apP.y, this.position.aHe.x, this.position.aHe.y, 11.0, Color.BLACK));
            this.b(ShaderQueueType.REGULAR, 1)
                .c(
                    () -> {
                        RenderUtil.roundedRectangle(this.position.apP.x, this.position.apP.y, this.position.aHe.x, this.position.aHe.y, 11.0, ColorUtil.withAlpha(Color.black, 100));
                        RenderUtil.roundedOutlineGradientRectangle(
                            this.position.apP.x, this.position.apP.y, this.position.aHe.x, this.position.aHe.y, 11.0, 0.5, ColorUtil.withAlpha(this.rz().rA(), 200), ColorUtil.withAlpha(this.rz().rB(), 200)
                        );
                        FontManager.MAIN
                            .a(24, FontWeight.REGULAR)
                            .drawString(Localization.ce("ui.sessionstats.name"), this.position.apP.x + this.position.aHe.x / 2.0, this.position.apP.y + d0, this.rz().rD().getRGB());
                        FontManager.MAIN
                            .a(18, FontWeight.REGULAR)
                            .drawString(this.time, this.position.apP.x + this.position.aHe.x / 2.0, this.position.apP.y + d0 + 19.0, new Color(255, 255, 255, 200).getRGB());
                        FontManager.MAIN
                            .a(18, FontWeight.REGULAR)
                            .drawString(
                                Localization.ce("ui.sessionstats.kills").toLowerCase() + " " + this.session.kills,
                                this.position.apP.x + 35.0,
                                this.position.apP.y + d0 + 32.0,
                                new Color(255, 255, 255, 200).getRGB()
                            );
                        FontManager.MAIN
                            .a(18, FontWeight.REGULAR)
                            .drawString(
                                Localization.ce("ui.sessionstats.wins").toLowerCase() + " " + this.session.wins,
                                this.position.apP.x + 95.0,
                                this.position.apP.y + d0 + 32.0,
                                new Color(255, 255, 255, 200).getRGB()
                            );
                    }
                );
        }
    };
    @EventLink
    public final Listener<KillEvent> onKill = var1x -> this.session.kills++;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle s45packettitle = (S45PacketTitle)var1x.getPacket();
            if (s45packettitle.getMessage() == null) {
                return;
            }

            if (StringUtils.stripControlCodes(s45packettitle.getMessage().getUnformattedText()).equals("VICTORY!")) {
                this.session.wins++;
            }
        }
    };
    @EventLink
    public final Listener<ServerJoinEvent> onServerJoin = var1x -> this.session = new RueSessionStatsData(0, 0, 0, 0, 0.0, 0.0);

    public RueSessionStats(String var1, SessionStats sessionStats) {
        super(var1, sessionStats);
    }
}
