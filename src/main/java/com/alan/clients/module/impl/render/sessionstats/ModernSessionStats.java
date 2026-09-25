package com.alan.clients.module.impl.render.sessionstats;

import com.alan.clients.module.impl.render.Interface;
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
import com.alan.clients.ui.click.standard.UIColors;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.render.sessionstats.ModernSessionStatsData;
import java.awt.Color;
import java.util.concurrent.TimeUnit;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.StringUtils;

public final class ModernSessionStats extends Mode<SessionStats> {
    private final DragValue position = this.getParent().getPosition();
    private ModernSessionStatsData data = new ModernSessionStatsData(0, 0, 0, 0, 0.0, 0.0);
    private String sessionTime = "0 seconds";
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (aEg.thePlayer.ticksExisted % 20 == 0) {
            long i = System.currentTimeMillis() - this.data.startTime;
            long j = TimeUnit.MILLISECONDS.toHours(i);
            long k = TimeUnit.MILLISECONDS.toMinutes(i) % 60L;
            long l = TimeUnit.MILLISECONDS.toSeconds(i) % 60L;
            String s = "";
            String s1 = s + j + Localization.ce("h ");
            String s2 = s1 + k + Localization.ce("m ");
            String s3 = s2 + l + Localization.ce("s ");
            this.sessionTime = s3;
        }
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        double d0 = 10.0;
        this.position.aHe = new Vector2d(130.0, 50.0);
        if (!aEg.gameSettings.bJf) {
            if (this.e(Interface.class).aoc.wo()) {
                this.b(ShaderQueueType.BLUR).c(() -> {
                    RenderUtil.a(this.position.apP.x, this.position.apP.y - 14.0, this.position.aHe.x, 14.0, 6.0, UIColors.SECONDARY.pV(), true, true, false, false);
                    RenderUtil.a(this.position.apP.x, this.position.apP.y, this.position.aHe.x, this.position.aHe.y, 6.0, UIColors.BACKGROUND.pV(), false, false, true, true);
                });
                this.b(ShaderQueueType.BLOOM).c(() -> {
                    RenderUtil.roundedRectangle(this.position.apP.x, this.position.apP.y - 14.0, this.position.aHe.x, this.position.aHe.y + 14.0, 7.0, this.rz().rE());
                    double d1 = this.position.apP.x + this.position.aHe.x / 2.0 - (FontManager.MAIN.a(18, FontWeight.REGULAR).getStringWidth("Information") / 2.0 + 1.0);
                    FontManager.MAIN.a(18, FontWeight.BOLD).drawString(Localization.ce("session"), d1, this.position.apP.y - 9.0, ColorUtil.withAlpha(this.rz().rA(), 200).getRGB());
                });
            }

            this.b(ShaderQueueType.REGULAR, 1)
                .c(
                    () -> {
                        Color color = this.rz().rA();
                        Color color1 = new Color(
                            Math.min(255, UIColors.BACKGROUND.pV().getRed() + color.getRed() / 26),
                            Math.min(255, UIColors.BACKGROUND.pV().getGreen() + color.getGreen() / 26),
                            Math.min(255, UIColors.BACKGROUND.pV().getBlue() + color.getBlue() / 26),
                            245
                        );
                        RenderUtil.a(this.position.apP.x, this.position.apP.y - 14.0, this.position.aHe.x, 14.0, 6.0, ColorUtil.withAlpha(UIColors.SECONDARY.pV(), 170), true, true, false, false);
                        RenderUtil.a(this.position.apP.x, this.position.apP.y, this.position.aHe.x, this.position.aHe.y, 6.0, color1, false, false, true, true);
                        double d1 = this.position.apP.x + this.position.aHe.x / 2.0 - (FontManager.MAIN.a(18, FontWeight.REGULAR).getStringWidth("Information") / 2.0 + 1.0);
                        FontManager.MAIN.a(18, FontWeight.BOLD).drawString(Localization.ce("session"), d1, this.position.apP.y - 9.0, this.rz().rD().getRGB());
                        FontManager.MAIN
                            .a(18, FontWeight.REGULAR)
                            .drawString(
                                Localization.ce("Information"),
                                this.position.apP.x + this.position.aHe.x / 2.0 + (FontManager.MAIN.a(18, FontWeight.BOLD).getStringWidth("session") / 2.0 + 1.0),
                                this.position.apP.y - 9.0,
                                UIColors.TEXT.pW()
                            );
                        FontManager.MAIN.a(24, FontWeight.BOLD).a(this.sessionTime, this.position.apP.x + d0, this.position.apP.y + d0 - 1.8, UIColors.TEXT.pW());
                        FontManager.MAIN
                            .a(16, FontWeight.REGULAR)
                            .a(
                                Localization.ce("You have gotten ") + this.data.kills + " kills",
                                this.position.apP.x + d0,
                                this.position.apP.y + d0 + FontManager.MAIN.a(24, FontWeight.BOLD).height(),
                                UIColors.TRINARY_TEXT.pW()
                            );
                        FontManager.MAIN
                            .a(16, FontWeight.REGULAR)
                            .a(
                                Localization.ce("You have won " + this.data.wins) + " games",
                                this.position.apP.x + d0,
                                this.position.apP.y + d0 + FontManager.MAIN.a(24, FontWeight.BOLD).height() + FontManager.MAIN.a(16, FontWeight.REGULAR).height() + 2.0,
                                UIColors.TRINARY_TEXT.pW()
                            );
                    }
                );
        }
    };
    @EventLink
    public final Listener<KillEvent> onKill = var1x -> this.data.kills++;
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1x -> {
        if (var1x.getPacket() instanceof S45PacketTitle) {
            S45PacketTitle s45packettitle = (S45PacketTitle)var1x.getPacket();
            if (s45packettitle.getMessage() == null) {
                return;
            }

            if (StringUtils.stripControlCodes(s45packettitle.getMessage().getUnformattedText()).equals("VICTORY!")) {
                this.data.wins++;
            }
        }
    };
    @EventLink
    public final Listener<ServerJoinEvent> onServerJoin = var1x -> this.data = new ModernSessionStatsData(0, 0, 0, 0, 0.0, 0.0);

    public ModernSessionStats(String var1, SessionStats sessionStats) {
        super(var1, sessionStats);
    }
}
