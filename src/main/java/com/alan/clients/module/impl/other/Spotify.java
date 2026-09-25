package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.value.impl.SubMode;
import com.google.gson.Gson;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.file.FileManager;
import com.alan.clients.util.font.impl.minecraft.FontRenderer;
import com.alan.clients.util.render.ScissorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.api.DevelopmentFeature;
import com.alan.clients.module.impl.other.spotify.SurplusWidthComparator;
import com.alan.clients.module.impl.other.spotify.RemainderWidthComparator;
import com.alan.clients.module.impl.other.spotify.LyricLine;
import com.alan.clients.module.impl.other.spotify.ParsedLyricLine;
import com.alan.clients.module.impl.other.spotify.LyricWord;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeScript;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.s;
import org.json.JSONArray;
import org.json.JSONObject;

@DevelopmentFeature
@ModuleInfo(aliases = "module.other.spotify.name", description = "module.other.spotify.description", category = Category.RENDER)
public class Spotify extends Module {
    private final ModeValue musicService = new ModeValue("Music Service", this).add(new SubMode("Cider")).add(new SubMode("Spotify")).setDefault("Cider");
    public final DragValue positionValue = new DragValue("Position", this, new Vector2d(200.0, 200.0));
    private final NumberValue refreshTicks = new NumberValue("Refresh Ticks", this, 10, 1, 20, 1);
    private final BooleanValue debug = new BooleanValue("Debug", this, false);
    private final BooleanValue showLyricsSpotify = new BooleanValue("Show Lyrics (Spotify)", this, true);
    private final NumberValue lyricLines = new NumberValue("Lyric Lines", this, 1, 1, 12, 1);
    private final ModeValue lyricsProvider = new ModeValue("Lyrics Provider", this)
        .add(new SubMode("LRCLIB"))
        .add(new SubMode("TTML (Apple-style)"))
        .add(new SubMode("Enhanced LRC (word-timed)"))
        .add(new SubMode("Custom"))
        .setDefault("LRCLIB");
    private final StringValue lyricsEndpointURL = new StringValue(
        "Lyrics Endpoint URL", this, "", () -> !"Spotify".equals(this.musicService.wo().getName()) || "LRCLIB".equals(this.lyricsProvider.wo().getName())
    );
    private final StringValue lyricsEndpointHeader = new StringValue(
        "Lyrics Endpoint Header", this, "", () -> !"Spotify".equals(this.musicService.wo().getName()) || "LRCLIB".equals(this.lyricsProvider.wo().getName())
    );
    private final BooleanValue karaokeFill = new BooleanValue("Karaoke Fill", this, true);
    private final NumberValue karaokeSpeedX = new NumberValue("Karaoke Speed (x)", this, 1.25, 0.25, 3.0, 0.05);
    public String Wd = "";
    public String song = "Loading...";
    public String artist = "Loading...";
    public String artworkUrl;
    public String progress = "1:43";
    public String length = "2:56";
    public String songAtEnable;
    private String trackId = "";
    public int durationMs = 1;
    public int progressMs = 1;
    public int ticks = 0;
    public int artworkTexture = -1;
    public FontManager fontManager = FontManager.MAIN;
    public static boolean Wp = false;
    public final File dataDirectory = new File(FileManager.DIRECTORY, "data");
    private static final String CREDENTIALS_FILE_NAME = "spotify.json";
    private float marqueeOffset = 0.0F;
    private final float textWidthLimit = 105.0F;
    private boolean songOverflows = false;
    private boolean artistOverflows = false;
    private volatile boolean lyricsAvailable = false;
    private volatile boolean syncedLyrics = false;
    private volatile List<LyricLine> syncedLines = new ArrayList<>();
    private volatile List<String> plainLines = new ArrayList<>();
    private int currentLineIndex = -1;
    private long progressTimestamp = System.currentTimeMillis();
    private int previousLineIndex = -1;
    private long lineAnimStart = 0L;
    private long lastLineSwitchTime = 0L;
    private static final int LINE_ANIM_MS = 260;
    private static final int LINE_SWITCH_MIN_MS = 120;
    private static final int WH = 0;
    private static final int LYRIC_FONT_SIZE_MIN = 11;
    private static final int LYRIC_FONT_SIZE = 13;
    private static final int LYRIC_FONT_SIZE_ACTIVE = 14;
    private float scrollTarget = 0.0F;
    private float scrollOffset = 0.0F;
    private long lastFrameTime = System.currentTimeMillis();
    private static final float TITLE_FONT_SIZE = 20.0F;
    private static final int WP = 0;
    private float targetHeight;
    private float animatedHeight;
    private float heightVelocity;
    private boolean firstFrame = true;
    private static final float SPRING_STIFFNESS = 25.0F;
    private static final float SPRING_DAMPING = 6.5F;
    private long heightChangeTime = -1L;
    private float heightJumpAmount = 0.0F;
    private static final int HEIGHT_ANIM_MS = 520;
    private volatile boolean pollerRunning = false;
    private Thread pollerThread;
    private volatile boolean authServerRunning = false;
    private volatile String clientId = "";
    private volatile String clientSecret = "";
    private volatile long credentialsLastModified = 0L;
    private volatile long tokenExpiredAt = 0L;
    private final Minecraft minecraft = Minecraft.getMinecraft();
    private static final int CONNECT_TIMEOUT_MS = 4000;
    private static final int READ_TIMEOUT_MS = 6000;
    private static final String USER_AGENT = "RiseClient/SpotifyModule";
    private volatile String fetchingArtworkUrl = "";
    private volatile boolean artworkFetching = false;
    private volatile BufferedImage pendingArtwork = null;
    private volatile String pendingArtworkUrl = "";
    private volatile long pendingArtworkToken = -1L;
    private volatile long trackGeneration = 0L;
    private volatile long lastPlayingTime = 0L;
    private volatile int noContentCount = 0;
    private static final long NO_CONTENT_GRACE_MS = 1500L;
    private static final int NO_CONTENT_LIMIT = 3;
    private volatile long lyricsToken = -1L;
    private volatile long artworkToken = -1L;
    private volatile String requestedArtworkUrl = "";
    private float fillWidth = 0.0F;
    private int fillLineIndex = -1;
    private long lastFillFrameTime = System.currentTimeMillis();
    private int lastFillTime = 0;
    private static final int FILL_SEEK_BACK_MS = 600;
    private static final float FILL_SNAP_PX = 2.0F;
    private static final float FILL_JUMP_PX = 6.0F;
    private static final int FILL_BOOST_MS = 260;
    private static final float FILL_BOOST_MULTIPLIER = 8.0F;
    private long fillBoostUntil = 0L;
    private float lastFillTarget = 0.0F;
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (this.debug.wo()) {
            ChatUtil.b("Song: " + this.song);
        }

        int i = this.getEstimatedProgressMs();
        byte b0 = 0;
        if (this.syncedLyrics && this.currentLineIndex >= 0 && this.currentLineIndex < this.syncedLines.size()) {
            b0 = (byte)(this.syncedLines.get(this.currentLineIndex).wordTimed ? 60 : 90);
        }

        int j = i + b0;
        this.updateCurrentLine(j);
        this.progress = String.format("%d:%02d", i / 1000 / 60, i / 1000 % 60);
        this.length = String.format("%d:%02d", this.durationMs / 1000 / 60, this.durationMs / 1000 % 60);
        boolean flag = this.showLyricsSpotify.wo() && this.lyricsAvailable;
        boolean flag1 = flag && this.currentLineIndex < 0;
        float f = flag1 ? 18.0F : 18.0F;
        double d0 = this.positionValue.aHe.getX() - 25.0;
        int k;
        if (flag && this.currentLineIndex >= 0 && d0 > 0.0) {
            byte b1 = 17;
            float f1 = 1.0F;
            List list = this.wrapText(this.getLineText(this.currentLineIndex), d0, b1, f1);
            k = Math.max(0, list.size() - 1);
        } else {
            k = 0;
        }

        float f2 = flag1 ? 56.0F : 65.0F;
        int l = Math.max(2, this.lyricLines.wo().intValue());
        float f3 = this.getLyricLineHeight();
        int i1 = 0;
        if (flag) {
            int j1 = this.getLineCount();
            int k1 = l + k;
            i1 = Math.min(Math.max(1, j1), k1);
        }

        float f4 = flag ? 14.0F + f3 * i1 + f : 0.0F;
        float f5 = f2 + f4;
        long l1 = System.currentTimeMillis();
        if (this.firstFrame) {
            this.targetHeight = this.animatedHeight = f5;
            this.firstFrame = false;
        }

        if (Math.abs(f5 - this.targetHeight) > 0.5F) {
            this.targetHeight = f5;
            this.heightChangeTime = l1;
            this.heightJumpAmount = this.clampInt((int)(Math.abs(this.targetHeight - this.animatedHeight) * 0.25F), 6, 22);
        }

        float f6 = (float)(l1 - this.lastFrameTime) / 1000.0F;
        this.lastFrameTime = l1;
        float f7 = 25.0F * (this.targetHeight - this.animatedHeight) - 6.5F * this.heightVelocity;
        this.heightVelocity += f7 * f6;
        this.animatedHeight = this.animatedHeight + this.heightVelocity * f6;
        this.positionValue.n(new Vector2d(180.0, this.animatedHeight));
        double d1 = this.positionValue.apP.getX() + 65.0;
        Color color = this.rz().rE();
        this.b(ShaderQueueType.BLUR)
            .c(
                () -> RenderUtil.roundedRectangle(
                    this.positionValue.apP.getX(),
                    this.positionValue.apP.getY(),
                    this.positionValue.aHe.getX(),
                    this.positionValue.aHe.getY(),
                    10.0,
                    Color.BLACK
                )
            );
        this.b(ShaderQueueType.BLOOM)
            .c(
                () -> {
                    RenderUtil.a(
                        this.positionValue.apP.getX(),
                        this.positionValue.apP.getY(),
                        this.positionValue.aHe.getX(),
                        this.positionValue.aHe.getY(),
                        11.0,
                        color,
                        color,
                        false
                    );
                    RenderUtil.a(d1, this.positionValue.apP.getY() + 45.0, i * 100.0 / Math.max(1, this.durationMs), 6.0, 3.0, this.rz().rA(), this.rz().rB(), true);
                }
            );
        this.b(ShaderQueueType.REGULAR)
            .c(
                () -> {
                    float f8 = 10.0F;
                    if (this.heightChangeTime > 0L) {
                        float f9 = (float)(l1 - this.heightChangeTime) / 520.0F;
                        if (f9 < 1.0F) {
                            f8 += (1.0F - this.easeOutCubic(f9)) * 2.0F;
                        }
                    }

                    double d13 = this.positionValue.apP.getX();
                    double d14 = this.positionValue.apP.getY();
                    double d15 = this.positionValue.aHe.getX();
                    double d16 = this.positionValue.aHe.getY();
                    double d17 = f8;
                    this.rz();
                    Color color1 = Themes.rK();
                    this.rz();
                    RenderUtil.a(d13, d14, d15, d16, d17, color1, Themes.rK(), false);
                    float f10 = this.getStringWidth(this.song, 20);
                    float f11 = this.getStringWidth(this.artist, 16);
                    this.songOverflows = f10 > 105.0F;
                    this.artistOverflows = f11 > 105.0F;
                    if (!this.songOverflows && !this.artistOverflows) {
                        this.marqueeOffset = 0.0F;
                    } else {
                        this.marqueeOffset += 0.1F;
                        float f12 = Math.max(f10, f11);
                        if (this.marqueeOffset > f12) {
                            this.marqueeOffset = -100.0F;
                        }
                    }

                    this.drawScrollingString(this.song, d1, this.positionValue.apP.getY() + 15.0, 20, this.songOverflows);
                    this.drawScrollingString(this.artist, d1, this.positionValue.apP.getY() + 30.0, 16, this.artistOverflows);
                    this.uploadPendingArtwork();
                    if (this.artworkTexture == -1 && this.artworkUrl != null && !this.artworkUrl.isEmpty()) {
                        this.fetchArtwork(this.artworkUrl, this.trackGeneration);
                    }

                    if (this.artworkTexture != -1) {
                        RenderUtil.a(
                            this.artworkTexture, (float)this.positionValue.apP.getX() + 12.5F, (float)(this.positionValue.apP.getY() + 10.0), 42.5F, 42.5F, Color.WHITE
                        );
                    }

                    d14 = this.positionValue.apP.getY() + 45.0;
                    d15 = this.positionValue.aHe.getX() - 75.0;
                    this.rz();
                    RenderUtil.roundedRectangle(d1, d14, d15, 6.0, 3.0, Themes.rK());
                    RenderUtil.a(
                        d1,
                        this.positionValue.apP.getY() + 45.0,
                        i * (this.positionValue.aHe.getX() - 75.0) / Math.max(1, this.durationMs),
                        6.0,
                        3.0,
                        this.rz().getAccentColor(new Vector2d(d1, 0.0)),
                        this.rz().getAccentColor(new Vector2d(d1 + this.positionValue.aHe.getX() - 150.0, 0.0)),
                        false
                    );
                    double d2 = d1 + this.positionValue.aHe.getX() - 75.0;
                    double d3 = this.getStringWidth(this.length, 13);
                    this.a(this.progress + " / ", d2 - d3, this.positionValue.apP.getY() + 38.5, 13, new Color(255, 255, 255, 128).getRGB());
                    this.a(this.length, d2, this.positionValue.apP.getY() + 38.5, 13, new Color(255, 255, 255, 48).getRGB());
                    if (flag) {
                        double d4 = this.positionValue.apP.getX() + 12.5;
                        double d5 = this.positionValue.apP.getX() + this.positionValue.aHe.getX() - 12.5 - d4;
                        double d6 = this.positionValue.apP.getY() + 10.0 + 42.5;
                        double d7 = this.positionValue.apP.getY() + 45.0 + 6.0;
                        double d8 = Math.max(d6, d7) + (flag1 ? 8.0F : 12.0F);
                        float f13 = 1.0F;
                        if (this.heightChangeTime > 0L) {
                            float f14 = (float)(l1 - this.heightChangeTime) / 520.0F;
                            if (f14 < 1.0F) {
                                f13 = this.easeOutCubic(Math.max(0.0F, Math.min(1.0F, f14)));
                            }
                        }

                        int i2 = (int)(128.0F * f13);
                        this.drawString("Lyrics", (float)d4, (float)d8, 12, new Color(255, 255, 255, i2).getRGB());
                        int j2 = this.getLineCount();
                        int k2 = Math.max(2, this.lyricLines.wo().intValue());
                        int l2 = Math.max(0, j2 - k2);
                        int i3 = Math.max(0, (k2 - 1) / 2);
                        int j3 = this.currentLineIndex >= 0 ? this.clampInt(this.currentLineIndex - i3, 0, l2) : 0;
                        this.scrollTarget = j3;
                        float f15 = Math.min(1.0F, f6 * 20.0F);
                        this.scrollOffset = this.scrollOffset + (this.scrollTarget - this.scrollOffset) * f15;
                        int k3 = Math.max(0, Math.min(Math.max(0, j2 - 1), (int)Math.floor(this.scrollOffset)));
                        float f16 = this.scrollOffset - k3;
                        float f17 = (float)(d8 + (flag1 ? 12.0F : 12.0F));
                        float f18 = this.getLyricLineHeight();
                        int l3 = k2 + k;
                        double d9 = d4;
                        double d10 = f17 - 2.0F;
                        double d11 = d5;
                        double d12 = f18 * l3 + 2.0F + 4.0F + (flag1 ? 3.0F : 5.0F);
                        ScissorUtil.hK();
                        RenderUtil.g(d9, d10, d11, d12);
                        float f19 = f17 - f16 * f18;
                        int i4 = k2 + 2;
                        float f20 = this.getLineAnimProgress();

                        for (int j4 = k3; j4 < j2 && i4 > 0; j4++) {
                            boolean flag2 = this.currentLineIndex >= 0 && j4 == this.currentLineIndex;
                            boolean flag3 = this.previousLineIndex >= 0 && j4 == this.previousLineIndex;
                            int k4 = flag2 ? 14 : 13;
                            if (flag2) {
                                byte b2 = 17;
                            } else {
                                byte b3 = 16;
                            }

                            float f21 = k4;
                            int l4 = 160;
                            float f22 = 0.0F;
                            if (flag2) {
                                f21 = 13.0F + 4 * f20;
                                l4 = (int)(180.0F + 75.0F * f20);
                                f22 = (1.0F - f20) * 2.0F;
                            } else if (flag3) {
                                f21 = 14.0F + -1.0F * f20;
                                l4 = (int)(255.0F - 95.0F * f20);
                                f22 = f20 * 2.0F;
                            }

                            int i5 = this.clampInt((int)(l4 * f13), 0, 255);
                            String s = this.getLineText(j4);
                            int j5 = Math.max(11, Math.round(f21));
                            float f23 = f21 / Math.max(1, j5);
                            List list1 = this.wrapText(s, d5, j5, f23);
                            if (flag2) {
                                float f24 = 0.0F;

                                for (String s1 : (Iterable<String>)list1) {
                                    f24 += this.getScaledWidth(s1, j5, f23);
                                }

                                LyricLine sl = this.syncedLyrics && j4 < this.syncedLines.size() ? this.w(j4) : null;
                                float f25;
                                if (this.karaokeFill.wo() && sl != null) {
                                    f25 = this.getFilledWidth(sl, j4, i, j5, f23, f24);
                                } else {
                                    f25 = 0.0F;
                                }

                                float f26 = this.animateFill(j4, f25, f24, i);

                                for (String s2 : (Iterable<String>)list1) {
                                    float f27 = this.getScaledWidth(s2, j5, f23);
                                    float f28 = this.b(s2, j5, f23);
                                    int k5 = new Color(255, 255, 255, Math.max(100, (int)(140.0F * Math.max(f20, 0.6F)))).getRGB();
                                    this.drawScaledString(s2, (float)d4, f19 + f22, j5, f23, k5);
                                    float f29 = Math.max(0.0F, Math.min(f26, f27));
                                    if (f29 > 0.0F) {
                                        int l5 = new Color(255, 255, 255, 255).getRGB();
                                        RenderUtil.g(d4, f19 + f22 - 2.0F, f29, f28 + 4.0F);
                                        this.drawScaledString(s2, (float)d4, f19 + f22, j5, f23, l5);
                                        RenderUtil.g(d9, d10, d11, d12);
                                    } else if (!this.karaokeFill.wo()) {
                                        int i6 = new Color(255, 255, 255, 255).getRGB();
                                        this.drawScaledString(s2, (float)d4, f19 + f22, j5, f23, i6);
                                    }

                                    f26 -= f27;
                                    f19 += f28 + 2.0F;
                                }

                                i4--;
                            } else {
                                for (String s3 : (Iterable<String>)list1) {
                                    float f30 = this.b(s3, j5, f23);
                                    this.drawScaledString(s3, (float)d4, f19 + f22, j5, f23, new Color(255, 255, 255, i5).getRGB());
                                    f19 += f30 + 2.0F;
                                }

                                i4--;
                            }

                            if (f19 > d10 + d12) {
                                break;
                            }
                        }

                        ScissorUtil.disable();
                    }

                    if (this.musicService.wo().getName().equals("Spotify") && (this.Wd == null || this.Wd.isEmpty())) {
                        ;
                    }
                }
            );
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> this.ticks++;

    public Spotify() {
    }

    private File getCredentialsFile() {
        return new File(this.dataDirectory, "spotify.json");
    }

    private Map<String, String> loadCredentials() {
        File file1 = this.getCredentialsFile();
        Map map = new HashMap();
        if (file1.exists()) {
            try (FileReader filereader = new FileReader(file1)) {
                map = new Gson().fromJson(filereader, Map.class);
            } catch (IOException ioexception) {
                ioexception.printStackTrace();
            }
        }

        return map;
    }

    private float getLyricLineHeight() {
        float f = this.getStringHeight("Ay", 13) + 3.0F;
        float f1 = this.getStringHeight("Ay", 17) + 0.0F;
        return Math.max(f, f1);
    }

    private void reloadCredentials() {
        File file1 = this.getCredentialsFile();
        long i = file1.exists() ? file1.lastModified() : 0L;
        if (i != this.credentialsLastModified) {
            this.credentialsLastModified = i;
            String s = this.clientId;
            String s1 = this.clientSecret;
            Map map = this.loadCredentials();
            this.clientId = String.valueOf(map.getOrDefault("client_id", ""));
            this.clientSecret = String.valueOf(map.getOrDefault("client_secret", ""));
            boolean flag = !Objects.equals(s, this.clientId) || !Objects.equals(s1, this.clientSecret);
            if (flag) {
                ChatUtil.b("Spotify credentials updated. Re-auth will be requested.");
                this.Wd = "";
            }
        }
    }

    private boolean hasCredentials() {
        return this.clientId != null && !this.clientId.isEmpty() && this.clientSecret != null && !this.clientSecret.isEmpty();
    }

    private void ensureAuthorized() {
        if ("Spotify".equals(this.musicService.wo().getName())) {
            if (!this.authServerRunning) {
                boolean flag = this.Wd == null || this.Wd.isEmpty() || this.tokenExpiredAt > 0L;
                if (flag && this.hasCredentials()) {
                    this.startAuthFlow();
                    this.tokenExpiredAt = 0L;
                }
            }
        }
    }

    private float getSmallLineHeight() {
        return this.getStringHeight("Ay", 13) + 2.0F;
    }

    private boolean needsFallbackFont(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            for (int i = 0; i < var1.length(); i += Character.charCount(var1.codePointAt(i))) {
                int j = var1.codePointAt(i);
                if (j > 127) {
                    UnicodeScript unicodescript = UnicodeScript.of(j);
                    if (unicodescript != UnicodeScript.HAN
                        && unicodescript != UnicodeScript.CYRILLIC
                        && unicodescript != UnicodeScript.HIRAGANA
                        && unicodescript != UnicodeScript.KATAKANA
                        && unicodescript != UnicodeScript.HANGUL
                        && unicodescript != UnicodeScript.LATIN
                        && unicodescript != UnicodeScript.COMMON
                        && unicodescript != UnicodeScript.INHERITED) {
                        return true;
                    }
                }
            }

            return false;
        }
        return false;
    }

    private float v(int var1) {
        return var1 / 9.0F;
    }

    private float getStringWidth(String var1, int var2) {
        return this.needsFallbackFont(var1) ? this.minecraft.fontRendererObj.getStringWidth(var1) * this.v(var2) : FontManager.MAIN.a(var2, FontWeight.BOLD).getStringWidth(var1);
    }

    private float getStringHeight(String var1, int var2) {
        return this.needsFallbackFont(var1) ? 9 * this.v(var2) : FontManager.MAIN.a(var2, FontWeight.BOLD).height();
    }

    private void drawString(String var1, float var2, double var3, int var5, int var6) {
        if (this.needsFallbackFont(var1)) {
            FontRenderer fontRendererObj = this.minecraft.fontRendererObj;
            GlStateManager.pushMatrix();
            GlStateManager.translate(var2, (float)var3, 0.0F);
            float f = this.v(var5 - 2);
            GlStateManager.scale(f, f, 1.0F);
            fontRendererObj.b(var1, 0.0, 0.0, var6, true);
            GlStateManager.popMatrix();
        } else {
            FontManager.MAIN.a(var5, FontWeight.BOLD).a(var1, var2, (float)var3, var6);
        }
    }

    private void a(String var1, double var2, double var4, int var6, int var7) {
        float f = this.getStringWidth(var1, var6);
        this.drawString(var1, (float)(var2 - f), var4, var6, var7);
    }

    private void drawScrollingString(String var1, double var2, double var4, int var6, boolean var7) {
        float f = this.getStringHeight(var1, var6);
        if (var7) {
            ScissorUtil.hK();
            RenderUtil.g(this.positionValue.apP.getX() + 65.0, var4 - 2.0, 105.0, f + 2.0F);
            this.drawString(var1, (float)(var2 - this.marqueeOffset), var4, var6, new Color(255, 255, 255, var6 == 20 ? 255 : 128).getRGB());
            ScissorUtil.disable();
        } else {
            this.drawString(var1, (float)var2, var4, var6, new Color(255, 255, 255, var6 == 20 ? 255 : 128).getRGB());
        }
    }

    private float getWidth(String var1, int var2) {
        return this.getStringWidth(var1, var2);
    }

    private float getHeight(String var1, int var2) {
        return this.getStringHeight(var1, var2);
    }

    private float getScaledWidth(String var1, int var2, float var3) {
        return this.getWidth(var1, var2) * var3;
    }

    private float b(String var1, int var2, float var3) {
        return this.getHeight(var1, var2) * var3;
    }

    private List<String> wrapText(String var1, double var2, int var4, float var5) {
        ArrayList arraylist = new ArrayList();
        String[] astring = var1.split(" ");
        String s = "";

        for (String s1 : astring) {
            String s2 = s.isEmpty() ? s1 : s + " " + s1;
            if (this.getScaledWidth(s2, var4, var5) <= var2) {
                s = s2;
            } else {
                if (!s.isEmpty()) {
                    arraylist.add(s);
                }

                s = s1;
            }
        }

        if (!s.isEmpty()) {
            arraylist.add(s);
        }

        return arraylist;
    }

    private void drawScaledString(String var1, float var2, float var3, int var4, float var5, int var6) {
        if (this.needsFallbackFont(var1)) {
            FontRenderer fontRendererObj = this.minecraft.fontRendererObj;
            GlStateManager.pushMatrix();
            GlStateManager.translate(var2, var3, 0.0F);
            float f = this.v(var4) * var5 / 1.5F;
            GlStateManager.scale(f, f, 1.0F);
            fontRendererObj.b(var1, 0.0, 0.0, var6, true);
            GlStateManager.popMatrix();
        } else {
            GlStateManager.pushMatrix();
            GlStateManager.translate(var2, var3, 0.0F);
            GlStateManager.scale(var5, var5, 1.0F);
            FontManager.MAIN.a(var4, FontWeight.BOLD).a(var1, 0.0, 0.0, var6);
            GlStateManager.popMatrix();
        }
    }

    private float getLineProgress(int var1, int var2) {
        if (this.syncedLyrics && var1 >= 0 && var1 < this.syncedLines.size()) {
            int i = this.syncedLines.get(var1).startTime;
            int j = var1 + 1 < this.syncedLines.size() ? this.syncedLines.get(var1 + 1).startTime : this.durationMs;
            if (j <= i) {
                return 1.0F;
            }
            float f = (float)(var2 - i) / (j - i);
            if (f < 0.0F) {
                return 0.0F;
            }
            return f > 1.0F ? 1.0F : f;
        }
        return -1.0F;
    }

    private LyricLine w(int var1) {
        if (this.syncedLyrics && var1 >= 0 && var1 < this.syncedLines.size()) {
            LyricLine sl = this.syncedLines.get(var1);
            if (sl.words == null || sl.words.isEmpty()) {
                int i = var1 + 1 < this.syncedLines.size() ? this.syncedLines.get(var1 + 1).startTime : this.durationMs;
                sl.words = this.estimateWordTimings(sl.text, sl.startTime, i);
            }

            return sl;
        }
        return null;
    }

    private float getFilledWidth(LyricLine var1, int var2, int var3, int var4, float var5, float var6) {
        if (var1 == null) {
            return 0.0F;
        }

        if (var1.words != null && !var1.words.isEmpty()) {
            float f = 0.0F;

            for (int i = 0; i < var1.words.size(); i++) {
                LyricWord sn = var1.words.get(i);
                float f1 = this.getScaledWidth(sn.text, var4, var5);
                if (var3 < sn.endTime) {
                    if (var3 > sn.startTime) {
                        int j = Math.max(1, sn.endTime - sn.startTime);
                        float f2 = (float)(var3 - sn.startTime) / j;
                        f += f1 * Math.max(0.0F, Math.min(1.0F, f2));
                    }
                    break;
                }

                f += f1;
            }

            if (f < 0.0F) {
                f = 0.0F;
            }

            if (f > var6) {
                f = var6;
            }

            return f;
        }
        return var6 * this.getLineProgress(var2, var3);
    }

    private float animateFill(int var1, float var2, float var3, int var4) {
        long now = System.currentTimeMillis();
        if (var1 != this.fillLineIndex) {
            this.fillLineIndex = var1;
            this.fillWidth = 0.0F;
            this.lastFillFrameTime = now;
            this.lastFillTime = var4;
            this.fillBoostUntil = now + 260L;
            this.lastFillTarget = var2;
            return this.clamp(var2, 0.0F, var3);
        }

        if (var4 + 600 < this.lastFillTime) {
            this.fillWidth = this.clamp(var2, 0.0F, var3);
            this.lastFillFrameTime = now;
            this.lastFillTime = var4;
            this.fillBoostUntil = now + 260L;
            this.lastFillTarget = var2;
            return this.fillWidth;
        }

        float f = Math.max(0.0F, (float)(now - this.lastFillFrameTime) / 1000.0F);
        this.lastFillFrameTime = now;
        if (var2 - this.lastFillTarget > 6.0F) {
            this.fillBoostUntil = now + 260L;
        }

        this.lastFillTarget = var2;
        float f1 = this.getFillSpeed(var1, var3) * this.karaokeSpeedX.wo().floatValue();
        if (now < this.fillBoostUntil) {
            f1 *= 8.0F;
        }

        if (var2 > this.fillWidth) {
            float f2 = f1 * f;
            if (var2 - this.fillWidth >= var3 * 0.25F) {
                this.fillWidth = var2;
            } else if (var2 - this.fillWidth <= 2.0F) {
                this.fillWidth = var2;
            } else {
                this.fillWidth = Math.min(var2, this.fillWidth + Math.max(0.75F, f2));
            }
        }

        this.fillWidth = this.clamp(this.fillWidth, 0.0F, var3);
        this.lastFillTime = var4;
        return this.fillWidth;
    }

    private float getFillSpeed(int var1, float var2) {
        int i = this.x(var1);
        int j = this.y(var1);
        int k = Math.max(60, j - i);
        return var2 * 1000.0F / k;
    }

    private int x(int var1) {
        if (this.syncedLyrics && var1 >= 0 && var1 < this.syncedLines.size()) {
            return this.syncedLines.get(var1).startTime;
        }

        int i = Math.max(1, this.plainLines.size());
        double d0 = (double)var1 / i;
        return (int)Math.round(this.durationMs * d0);
    }

    private int y(int var1) {
        if (this.syncedLyrics) {
            return var1 + 1 < this.syncedLines.size() ? this.syncedLines.get(var1 + 1).startTime : this.durationMs;
        }

        int i = Math.max(1, this.plainLines.size());
        double d0 = (double)(var1 + 1) / i;
        return (int)Math.round(this.durationMs * d0);
    }

    private float clamp(float var1, float var2, float var3) {
        return Math.max(var2, Math.min(var3, var1));
    }

    public void pollCider() {
        try {
            long now = System.nanoTime();
            HttpURLConnection httpurlconnection = (HttpURLConnection)new URL("http://localhost:10767/api/v1/playback/now-playing").openConnection();
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.setRequestProperty("Content-Type", "application/json");
            httpurlconnection.setRequestProperty("User-Agent", "RiseClient/SpotifyModule");
            httpurlconnection.setConnectTimeout(4000);
            httpurlconnection.setReadTimeout(6000);

            try (Scanner scanner = new Scanner(httpurlconnection.getInputStream()).useDelimiter("\\A")) {
                JSONObject jsonobject = new JSONObject(scanner.hasNext() ? scanner.next() : "");
                if (jsonobject.getString("status").equals("ok")) {
                    JSONObject jsonobject1 = jsonobject.getJSONObject("info");
                    this.song = jsonobject1.getString("name");
                    this.artist = jsonobject1.getString("artistName");
                    this.durationMs = jsonobject1.getInt("durationInMillis");
                    this.artworkUrl = jsonobject1.getJSONObject("artwork").getString("url").replace("{w}x{h}", "600x600");
                    this.progressMs = (int)(this.durationMs - jsonobject1.getDouble("remainingTime") * 1000.0);
                    long j = (System.nanoTime() - now) / 1000000L;
                    long k = Math.min(150L, Math.max(0L, j / 2L));
                    this.progressTimestamp = System.currentTimeMillis() - k;
                }
            }
        } catch (SocketException socketexception) {
            if (this.debug.wo()) {
                ChatUtil.c("Cider connection issue: " + socketexception.getMessage());
            }
        } catch (Exception exception) {
        }
    }

    public void pollSpotify() {
        try {
            long now = System.nanoTime();
            HttpURLConnection httpurlconnection = (HttpURLConnection)new URL("https://api.spotify.com/v1/me/player?market=US").openConnection();
            httpurlconnection.setRequestMethod("GET");
            httpurlconnection.addRequestProperty("Authorization", "Bearer " + this.Wd);
            httpurlconnection.setRequestProperty("User-Agent", "RiseClient/SpotifyModule");
            httpurlconnection.setConnectTimeout(4000);
            httpurlconnection.setReadTimeout(6000);
            int j = httpurlconnection.getResponseCode();
            long k = System.currentTimeMillis();
            long l = (System.nanoTime() - now) / 1000000L;
            if (j == 200) {
                this.noContentCount = 0;
                this.lastPlayingTime = k;

                try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()))) {
                    JSONObject jsonobject = new JSONObject(bufferedreader.lines().reduce("", String::concat));
                    if (jsonobject.has("item")) {
                        JSONObject jsonobject1 = jsonobject.getJSONObject("item");
                        this.song = jsonobject1.getString("name");
                        this.artist = jsonobject1.getJSONArray("artists")
                            .toList()
                            .stream()
                            .map(var0 -> ((Map)var0).get("name").toString())
                            .reduce((var0, var1) -> var0 + ", " + var1)
                            .orElse("");
                        this.artworkUrl = jsonobject1.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
                        this.durationMs = jsonobject1.getInt("duration_ms");
                        this.progressMs = jsonobject.getInt("progress_ms");
                        long i1 = jsonobject.optLong("timestamp", k);
                        long j1 = Math.max(0L, k - i1);
                        long k1 = Math.max(0L, l / 2L);
                        long l1 = Math.min(300L, Math.max(k1, j1 / 2L));
                        this.progressTimestamp = k - l1;
                        String s = jsonobject1.optString("id", "");
                        boolean flag = !s.equals(this.trackId);
                        boolean flag1 = !this.lyricsAvailable || this.getLineCount() == 0;
                        boolean flag2 = this.artworkTexture == -1 && this.artworkUrl != null && !this.artworkUrl.isEmpty();
                        if (flag) {
                            this.trackId = s;
                            long i2 = ++this.trackGeneration;
                            this.resetArtwork(i2);
                            this.lyricsToken = i2;
                            this.a(this.song, this.artist, this.trackId, i2);
                            if (this.artworkUrl != null && !this.artworkUrl.isEmpty()) {
                                this.artworkToken = i2;
                                this.requestedArtworkUrl = this.artworkUrl;
                                this.fetchArtwork(this.artworkUrl, i2);
                            }
                        } else {
                            long j2 = this.trackGeneration;
                            if (flag1 && this.lyricsToken != j2) {
                                this.lyricsToken = j2;
                                this.a(this.song, this.artist, this.trackId, j2);
                            }

                            if (flag2 && (this.artworkToken != j2 || !Objects.equals(this.requestedArtworkUrl, this.artworkUrl))) {
                                this.artworkToken = j2;
                                this.requestedArtworkUrl = this.artworkUrl;
                                this.fetchArtwork(this.artworkUrl, j2);
                            }
                        }
                    }
                }
            } else if (j == 204) {
                this.noContentCount++;
                long k2 = this.lastPlayingTime == 0L ? Long.MAX_VALUE : k - this.lastPlayingTime;
                boolean flag3 = k2 >= 1500L && this.noContentCount >= 3;
                if (flag3) {
                    this.artist = this.song = "No data";
                    this.artworkUrl = "";
                    this.progressMs = this.durationMs = 999;
                    this.lyricsAvailable = false;
                    this.syncedLines = new ArrayList<>();
                    this.plainLines = new ArrayList<>();
                    this.trackId = "";
                    long l2 = ++this.trackGeneration;
                    this.resetArtwork(l2);
                }
            } else if (j == 401) {
                this.tokenExpiredAt = System.currentTimeMillis();
                this.Wd = "";
            }
        } catch (SocketException socketexception) {
            if (this.debug.wo()) {
                ChatUtil.c("Spotify connection issue: " + socketexception.getMessage());
            }
        } catch (Exception exception) {
            if (this.debug.wo()) {
                exception.printStackTrace();
            }
        }
    }

    private void a(String var1, String var2, String var3, long var4) {
        this.lyricsAvailable = false;
        this.syncedLines = new ArrayList<>();
        this.plainLines = new ArrayList<>();
        this.syncedLyrics = false;
        this.currentLineIndex = -1;
        this.previousLineIndex = -1;
        new Thread(() -> this.b(var1, var2, var3, var4), "LyricsFetch-" + var4).start();
    }

    private void b(String var1, String var2, String var3, long var4) {
        try {
            if (var4 != this.trackGeneration) {
                return;
            }

            String s = var2.contains(",") ? var2.split(",")[0].trim() : var2.trim();
            if (!"LRCLIB".equals(this.lyricsProvider.wo().getName())) {
                boolean flag = this.tryLyricsEndpoint(var1, var2, s, var3, var4);
                if (flag) {
                    return;
                }
            }

            JSONObject jsonobject = this.a(var1, s, this.durationMs);
            if (jsonobject == null) {
                JSONObject jsonobject1 = this.b(var1, s, this.durationMs);
                if (jsonobject1 != null) {
                    if (jsonobject1.optString("syncedLyrics", "").isEmpty() && jsonobject1.optString("plainLyrics", "").isEmpty()) {
                        jsonobject = this.a(jsonobject1.optString("trackName", var1), jsonobject1.optString("artistName", s), this.durationMs);
                    } else {
                        jsonobject = jsonobject1;
                    }
                }
            }

            if (jsonobject != null) {
                String s1 = jsonobject.optString("syncedLyrics", "");
                String s2 = jsonobject.optString("plainLyrics", "");
                if (!s1.isEmpty()) {
                    List list = this.parseLRC(s1);
                    if (!list.isEmpty()) {
                        if (var4 != this.trackGeneration) {
                            return;
                        }

                        this.syncedLines = list;
                        this.syncedLyrics = true;
                        this.lyricsAvailable = true;
                        this.currentLineIndex = this.getCurrentLineIndex();
                        return;
                    }
                }

                if (!s2.isEmpty()) {
                    if (var4 != this.trackGeneration) {
                        return;
                    }

                    this.plainLines = Arrays.asList(s2.split("\\r?\\n"));
                    this.syncedLyrics = false;
                    this.lyricsAvailable = !this.plainLines.isEmpty();
                    this.currentLineIndex = this.getCurrentLineIndex();
                }
            } else if (this.debug.wo()) {
                ChatUtil.c("LRCLIB: no lyrics for " + var1 + " - " + s);
            }
        } catch (Throwable throwable) {
            if (this.debug.wo()) {
                ChatUtil.c("Lyrics fetch error: " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
            }
        }
    }

    private boolean tryLyricsEndpoint(String var1, String var2, String var3, String var4, long var5) {
        try {
            String s = this.lyricsProvider.wo().getName();
            String s1 = this.lyricsEndpointURL.wo();
            if (s1 != null && !s1.trim().isEmpty()) {
                String s2 = this.a(s1, var1, var3, var2, var4);
                String lyricsEndpointHeader = this.lyricsEndpointHeader.wo();
                String s4 = this.h(s2, lyricsEndpointHeader);
                if (s4 == null) {
                    return false;
                }

                if (var5 != this.trackGeneration) {
                    return true;
                }

                String s5 = s4.trim();
                if (s5.startsWith("{")) {
                    JSONObject jsonobject = new JSONObject(s5);
                    String s6 = jsonobject.optString("syncedLyrics", "");
                    String s7 = jsonobject.optString("plainLyrics", "");
                    if (!s6.isEmpty()) {
                        List list = this.parseByProvider(s, s6);
                        if (!list.isEmpty()) {
                            if (var5 != this.trackGeneration) {
                                return true;
                            }

                            this.syncedLines = list;
                            this.syncedLyrics = true;
                            this.lyricsAvailable = true;
                            this.currentLineIndex = this.getCurrentLineIndex();
                            return true;
                        }
                    }

                    if (s7.isEmpty()) {
                        String s8 = jsonobject.optString("lyrics", "");
                        if (!s8.isEmpty()) {
                            List list1 = this.parseByProvider(s, s8);
                            if (!list1.isEmpty()) {
                                if (var5 != this.trackGeneration) {
                                    return true;
                                }

                                this.syncedLines = list1;
                                this.syncedLyrics = true;
                                this.lyricsAvailable = true;
                                this.currentLineIndex = this.getCurrentLineIndex();
                                return true;
                            }
                        }

                        return false;
                    }
                    if (var5 != this.trackGeneration) {
                        return true;
                    }

                    this.plainLines = Arrays.asList(s7.split("\\r?\\n"));
                    this.syncedLyrics = false;
                    this.lyricsAvailable = !this.plainLines.isEmpty();
                    this.currentLineIndex = this.getCurrentLineIndex();
                    return this.lyricsAvailable;
                }
                List list2 = this.parseByProvider(s, s5);
                if (list2.isEmpty()) {
                    return false;
                }

                if (var5 != this.trackGeneration) {
                    return true;
                }

                this.syncedLines = list2;
                this.syncedLyrics = true;
                this.lyricsAvailable = true;
                this.currentLineIndex = this.getCurrentLineIndex();
                return true;
            }
            return false;
        } catch (Throwable throwable) {
            if (this.debug.wo()) {
                ChatUtil.c("Lyrics endpoint provider error: " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
            }

            return false;
        }
    }

    private List<LyricLine> parseByProvider(String var1, String var2) {
        if (var2 == null) {
            return new ArrayList<>();
        }
        String s = var2.trim();
        if (s.isEmpty()) {
            return new ArrayList<>();
        } else if ("Custom".equals(var1)) {
            return this.parseLyrics(s);
        } else if ("TTML (Apple-style)".equals(var1)) {
            return !this.isTTML(s) ? new ArrayList<>() : this.parseTTML(s);
        }
        return "Enhanced LRC (word-timed)".equals(var1) ? this.parseLRC(s) : this.parseLyrics(s);
    }

    private String a(String var1, String var2, String var3, String var4, String var5) throws java.io.UnsupportedEncodingException {
        String s = var2 == null ? "" : var2;
        String s1 = var3 == null ? "" : var3;
        String s2 = var4 == null ? "" : var4;
        String s3 = var5 == null ? "" : var5;
        return var1.replace("{title}", URLEncoder.encode(s, "UTF-8"))
            .replace("{artist}", URLEncoder.encode(s1, "UTF-8"))
            .replace("{artists}", URLEncoder.encode(s2, "UTF-8"))
            .replace("{spotifyId}", URLEncoder.encode(s3, "UTF-8"));
    }

    private String h(String var1, String var2) {
        HttpURLConnection httpurlconnection = null;
        boolean flag = false ;

        Object object2;
        label222: {
            Object object1;
            label223: {
                Object object;
                label224: {
                    String s4;
                    try {
                        flag = true;
                        httpurlconnection = (HttpURLConnection)new URL(var1).openConnection();
                        httpurlconnection.setRequestMethod("GET");
                        httpurlconnection.setRequestProperty("User-Agent", "RiseClient/SpotifyModule");
                        httpurlconnection.setConnectTimeout(4000);
                        httpurlconnection.setReadTimeout(6000);
                        if (var2 != null) {
                            String s = var2.trim();
                            if (!s.isEmpty()) {
                                int i = s.indexOf(58);
                                if (i > 0 && i + 1 < s.length()) {
                                    String s1 = s.substring(0, i).trim();
                                    String s2 = s.substring(i + 1).trim();
                                    if (!s1.isEmpty() && !s2.isEmpty()) {
                                        httpurlconnection.setRequestProperty(s1, s2);
                                    }
                                }
                            }
                        }

                        int j = httpurlconnection.getResponseCode();
                        InputStream inputstream = j >= 200 && j < 300 ? httpurlconnection.getInputStream() : httpurlconnection.getErrorStream();
                        if (inputstream == null) {
                            object = null;
                            flag = false;
                            break label224;
                        }

                        try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream))) {
                            StringBuilder stringbuilder = new StringBuilder();

                            String s3;
                            while ((s3 = bufferedreader.readLine()) != null) {
                                stringbuilder.append(s3).append('\n');
                            }

                            s4 = stringbuilder.toString();
                        }

                        flag = false;
                    } catch (SocketException socketexception) {
                        if (this.debug.wo()) {
                            ChatUtil.c("Custom lyrics connection issue: " + socketexception.getMessage());
                        }

                        object1 = null;
                        flag = false;
                        break label223;
                    } catch (Throwable throwable6) {
                        if (this.debug.wo()) {
                            ChatUtil.c("Custom lyrics HTTP error: " + throwable6.getClass().getSimpleName() + ": " + throwable6.getMessage());
                        }

                        object2 = null;
                        flag = false;
                        break label222;
                    } finally {
                        if (flag) {
                            if (httpurlconnection != null) {
                                try {
                                    httpurlconnection.disconnect();
                                } catch (Throwable throwable) {
                                }
                            }
                        }
                    }

                    if (httpurlconnection != null) {
                        try {
                            httpurlconnection.disconnect();
                        } catch (Throwable throwable4) {
                        }
                    }

                    return s4;
                }

                if (httpurlconnection != null) {
                    try {
                        httpurlconnection.disconnect();
                    } catch (Throwable throwable3) {
                    }
                }

                return (String)object;
            }

            if (httpurlconnection != null) {
                try {
                    httpurlconnection.disconnect();
                } catch (Throwable throwable2) {
                }
            }

            return (String)object1;
        }

        if (httpurlconnection != null) {
            try {
                httpurlconnection.disconnect();
            } catch (Throwable throwable1) {
            }
        }

        return (String)object2;
    }

    private List<LyricLine> parseLyrics(String var1) {
        if (var1 == null) {
            return new ArrayList<>();
        }
        String s = var1.trim();
        if (s.isEmpty()) {
            return new ArrayList<>();
        }
        return this.isTTML(s) ? this.parseTTML(s) : this.parseLRC(s);
    }

    private boolean isTTML(String var1) {
        String s = var1.trim();
        return s.startsWith("<") && (s.contains("<tt") || s.contains("http://www.w3.org/ns/ttml"));
    }

    private List<LyricLine> parseTTML(String var1) {
        ArrayList arraylist = new ArrayList();
        if (var1 == null) {
            return arraylist;
        }

        Matcher matcher = Pattern.compile("(?is)<p\\b([^>]*)>(.*?)</p>").matcher(var1);

        while (matcher.find()) {
            String s = matcher.group(1);
            String s1 = matcher.group(2);
            Integer integer = this.parseTimestamp(this.getAttribute(s, "begin"));
            Integer integer1 = this.parseTimestamp(this.getAttribute(s, "end"));
            if (integer != null) {
                ArrayList arraylist1 = new ArrayList();
                boolean flag = false;
                Matcher matcher1 = Pattern.compile("(?is)<span\\b([^>]*)>(.*?)</span>").matcher(s1);
                StringBuilder stringbuilder = new StringBuilder();

                while (matcher1.find()) {
                    String s2 = matcher1.group(1);
                    String s3 = matcher1.group(2);
                    String s4 = this.stripTags(this.unescapeHtml(s3));
                    Integer integer2 = this.parseTimestamp(this.getAttribute(s2, "begin"));
                    Integer integer3 = this.parseTimestamp(this.getAttribute(s2, "end"));
                    if (s4 == null) {
                        s4 = "";
                    }

                    if (stringbuilder.length() > 0) {
                        char c0 = stringbuilder.charAt(stringbuilder.length() - 1);
                        if (!Character.isWhitespace(c0) && !s4.isEmpty() && !Character.isWhitespace(s4.charAt(0))) {
                            stringbuilder.append(' ');
                        }
                    }

                    stringbuilder.append(s4);
                    if (integer2 != null) {
                        flag = true;
                        LyricWord sn = new LyricWord(s4.isEmpty() ? "" : s4, integer2);
                        sn.endTime = integer3 != null ? integer3 : integer2;
                        arraylist1.add(sn);
                    }
                }

                String s5;
                if (stringbuilder.length() > 0) {
                    s5 = stringbuilder.toString().trim();
                } else {
                    s5 = this.stripTags(this.unescapeHtml(s1)).trim();
                }

                if (flag && !arraylist1.isEmpty()) {
                    for (int i = 0; i < arraylist1.size(); i++) {
                        LyricWord snx = (LyricWord)arraylist1.get(i);
                        if (i + 1 < arraylist1.size()) {
                            snx.endTime = Math.max(snx.startTime, Math.min(snx.endTime, ((LyricWord)arraylist1.get(i + 1)).startTime));
                            if (snx.endTime == snx.startTime) {
                                snx.endTime = ((LyricWord)arraylist1.get(i + 1)).startTime;
                            }
                        } else if (integer1 != null) {
                            snx.endTime = Math.max(snx.startTime, integer1);
                        }
                    }
                }

                arraylist.add(new LyricLine(integer, s5, arraylist1, flag));
            }
        }

        arraylist.sort(Comparator.comparingInt(var0 -> ((LyricLine)var0).startTime));

        for (int j = 0; j < arraylist.size(); j++) {
            LyricLine sl = (LyricLine)arraylist.get(j);
            int k = j + 1 < arraylist.size() ? ((LyricLine)arraylist.get(j + 1)).startTime : this.durationMs;
            if (sl.words == null || sl.words.isEmpty()) {
                sl.words = this.estimateWordTimings(sl.text, sl.startTime, k);
            }
        }

        return arraylist;
    }

    private String getAttribute(String var1, String var2) {
        if (var1 == null) {
            return null;
        }

        Matcher matcher = Pattern.compile("(?i)\\b" + Pattern.quote(var2) + "\\s*=\\s*\"([^\"]+)\"").matcher(var1);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String stripTags(String var1) {
        return var1 == null ? "" : var1.replaceAll("(?is)<[^>]+>", "");
    }

    private String unescapeHtml(String var1) {
        return var1 == null ? "" : var1.replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&apos;", "'");
    }

    private Integer parseTimestamp(String var1) {
        if (var1 == null) {
            return null;
        }

        String s = var1.trim();
        if (s.isEmpty()) {
            return null;
        }

        try {
            if (s.endsWith("s")) {
                double d0 = Double.parseDouble(s.substring(0, s.length() - 1));
                return (int)Math.round(d0 * 1000.0);
            }

            String[] astring = s.split(":");
            double d1;
            if (astring.length == 3) {
                int i = Integer.parseInt(astring[0]);
                int j = Integer.parseInt(astring[1]);
                d1 = Double.parseDouble(astring[2]) + j * 60.0 + i * 3600.0;
            } else if (astring.length == 2) {
                int k = Integer.parseInt(astring[0]);
                d1 = Double.parseDouble(astring[1]) + k * 60.0;
            } else {
                d1 = Double.parseDouble(astring[0]);
            }

            return (int)Math.round(d1 * 1000.0);
        } catch (Throwable throwable) {
            return null;
        }
    }

    private JSONObject a(String var1, String var2, int var3) {
        for (String s : this.getTitleVariants(var1)) {
            try {
                String s1 = "https://lrclib.net/api/get?track_name=" + URLEncoder.encode(s, "UTF-8") + "&artist_name=" + URLEncoder.encode(var2, "UTF-8");
                if (var3 > 0) {
                    s1 = s1 + "&duration=" + Math.max(1, Math.round(var3 / 1000.0F));
                }

                JSONObject jsonobject = this.requestJsonObject(s1);
                if (this.hasLyrics(jsonobject)) {
                    return jsonobject;
                }
            } catch (Exception exception) {
                if (this.debug.wo()) {
                    ChatUtil.c("LRCLIB exact lookup failed: " + exception.getClass().getSimpleName());
                }
            }
        }

        return null;
    }

    private JSONObject b(String var1, String var2, int var3) {
        JSONObject jsonobject = null;
        double d0 = Double.MAX_VALUE;

        for (String s : this.buildSearchUrls(var1, var2)) {
            try {
                JSONArray jsonarray = this.requestJsonArray(s);
                if (jsonarray != null) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject jsonobject1 = jsonarray.optJSONObject(i);
                        if (this.hasLyrics(jsonobject1)) {
                            double d1 = this.score(jsonobject1, var1, var2, var3);
                            if (d1 < d0) {
                                d0 = d1;
                                jsonobject = jsonobject1;
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                if (this.debug.wo()) {
                    ChatUtil.c("LRCLIB search failed: " + exception.getClass().getSimpleName());
                }
            }
        }

        if (this.debug.wo() && jsonobject != null) {
            ChatUtil.c(
                "LRCLIB matched: "
                    + jsonobject.optString("trackName", "?")
                    + " - "
                    + jsonobject.optString("artistName", "?")
                    + " score="
                    + Math.round(d0 * 100.0) / 100.0
            );
        }

        return jsonobject;
    }

    private List<String> buildSearchUrls(String var1, String var2) {
        ArrayList arraylist = new ArrayList();

        try {
            for (String s : this.getTitleVariants(var1)) {
                arraylist.add("https://lrclib.net/api/search?track_name=" + URLEncoder.encode(s, "UTF-8") + "&artist_name=" + URLEncoder.encode(var2, "UTF-8"));
            }

            arraylist.add("https://lrclib.net/api/search?q=" + URLEncoder.encode((var1 == null ? "" : var1) + " " + (var2 == null ? "" : var2), "UTF-8"));
        } catch (UnsupportedEncodingException unsupportedencodingexception) {
            if (this.debug.wo()) {
                ChatUtil.c("LRCLIB URL encoding failed: " + unsupportedencodingexception.getMessage());
            }
        }

        return arraylist;
    }

    private List<String> getTitleVariants(String var1) {
        ArrayList arraylist = new ArrayList();
        String s = var1 == null ? "" : var1.trim();
        this.addUnique(arraylist, s);
        String s1 = s.replaceAll(
                "(?i)\\s*-\\s*(remaster(?:ed)?|\\d{4}\\s*remaster(?:ed)?|radio edit|single version|album version|explicit|clean|mono|stereo|live|sped up|slowed|nightcore).*$",
                ""
            )
            .replaceAll(
                "(?i)\\s*\\((?:[^)]*(remaster|remastered|radio edit|single version|album version|explicit|clean|mono|stereo|live|sped up|slowed|nightcore|feat\\.?|ft\\.)[^)]*)\\)",
                ""
            )
            .replaceAll(
                "(?i)\\s*\\[(?:[^]]*(remaster|remastered|radio edit|single version|album version|explicit|clean|mono|stereo|live|sped up|slowed|nightcore|feat\\.?|ft\\.)[^]]*)]",
                ""
            )
            .trim();
        this.addUnique(arraylist, s1);
        return arraylist;
    }

    private void addUnique(List<String> var1, String var2) {
        if (var2 != null) {
            String s = var2.trim();
            if (!s.isEmpty()) {
                Iterator iterator = var1.iterator();

                while (iterator.hasNext()) {
                    if (((String)iterator.next()).equalsIgnoreCase(s)) {
                        return;
                    }
                }

                var1.add(s);
            }
        }
    }

    private boolean hasLyrics(JSONObject json) {
        return json != null && (!json.optString("syncedLyrics", "").isEmpty() || !json.optString("plainLyrics", "").isEmpty());
    }

    private double score(JSONObject json, String var2, String var3, int var4) {
        double d0 = 0.0;
        String s = this.normalize(var2);
        String s1 = this.normalize(var3);
        String s2 = this.normalize(json.optString("trackName", json.optString("name", "")));
        String s3 = this.normalize(json.optString("artistName", ""));
        if (!s2.equals(s)) {
            d0 += !s2.contains(s) && !s.contains(s2) ? 20.0 : 6.0;
        }

        if (!s3.equals(s1)) {
            d0 += !s3.contains(s1) && !s1.contains(s3) ? 12.0 : 4.0;
        }

        if (var4 > 0 && json.has("duration")) {
            double d1 = var4 / 1000.0;
            d0 += Math.min(30.0, Math.abs(json.optDouble("duration", d1) - d1) * 0.35);
        }

        if (!json.optString("syncedLyrics", "").isEmpty()) {
            d0 -= 2.0;
        }

        return d0;
    }

    private String normalize(String var1) {
        return var1 == null
            ? ""
            : var1.toLowerCase(Locale.ROOT)
                .replaceAll("&", "and")
                .replaceAll("(?i)\\b(feat|ft)\\.?\\b.*", "")
                .replaceAll("[\\p{Punct}]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private JSONObject requestJsonObject(String var1) throws java.io.IOException {
        HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(var1).openConnection();
        httpurlconnection.setRequestMethod("GET");
        httpurlconnection.setRequestProperty("User-Agent", "RiseClient/SpotifyModule");
        httpurlconnection.setConnectTimeout(4000);
        httpurlconnection.setReadTimeout(6000);
        int i = httpurlconnection.getResponseCode();
        if (i != 200) {
            if (this.debug.wo()) {
                ChatUtil.c("LRCLIB object HTTP " + i);
            }

            return null;
        }
        try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()))) {
            String s = bufferedreader.lines().reduce("", (var0, var1x) -> var0 + var1x);
            return new JSONObject(s);
        } finally {
            httpurlconnection.disconnect();
        }
    }

    private JSONArray requestJsonArray(String var1) throws java.io.IOException {
        HttpURLConnection httpurlconnection = (HttpURLConnection)new URL(var1).openConnection();
        httpurlconnection.setRequestMethod("GET");
        httpurlconnection.setRequestProperty("User-Agent", "RiseClient/SpotifyModule");
        httpurlconnection.setConnectTimeout(4000);
        httpurlconnection.setReadTimeout(6000);
        int i = httpurlconnection.getResponseCode();
        if (i != 200) {
            if (this.debug.wo()) {
                ChatUtil.c("LRCLIB search HTTP " + i);
            }

            return null;
        }
        try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()))) {
            String s = bufferedreader.lines().reduce("", (var0, var1x) -> var0 + var1x);
            return new JSONArray(s);
        } finally {
            httpurlconnection.disconnect();
        }
    }

    private List<LyricLine> parseLRC(String var1) {
        ArrayList arraylist = new ArrayList();
        BufferedReader bufferedreader = new BufferedReader(new StringReader(var1));
        Pattern pattern = Pattern.compile("\\[(\\d{1,2}):(\\d{1,2})(?:\\.(\\d{1,3}))?]");

        String s;
        try {
            while ((s = bufferedreader.readLine()) != null) {
                Matcher matcher = pattern.matcher(s);
                String s1 = s.replaceAll("\\[[^\\]]+]", "");
                ArrayList arraylist1 = new ArrayList();

                while (matcher.find()) {
                    int i = this.parseInt(matcher.group(1), 0);
                    int j = this.parseInt(matcher.group(2), 0);
                    int l = 0;
                    if (matcher.group(3) != null) {
                        String s2 = matcher.group(3);
                        l = s2.length() == 2 ? this.parseInt(s2, 0) * 10 : this.parseInt(s2, 0);
                    }

                    int k = i * 60000 + j * 1000 + l;
                    arraylist1.add(k);
                }

                if (!arraylist1.isEmpty()) {
                    boolean flag = s1.contains("<") && s1.contains(">");
                    if (flag) {
                        ParsedLyricLine sm = this.h(s1.trim(), (Integer)arraylist1.get(0));

                        for (int l : (Iterable<Integer>)arraylist1) {
                            LyricLine slx = new LyricLine(l, sm.text, this.copyWords(sm.words), true);
                            arraylist.add(slx);
                            ChatUtil.c("Enhanced line parsed: [" + l + "ms] \"" + slx.text + "\" | words=" + slx.words.size());
                        }
                    } else {
                        String s3 = s1.trim();

                        for (int l1 : (Iterable<Integer>)arraylist1) {
                            LyricLine sl1 = new LyricLine(l1, s3, new ArrayList(), false);
                            arraylist.add(sl1);
                        }
                    }
                }
            }
        } catch (IOException ioexception) {
        }

        for (int i1 = 0; i1 < arraylist.size(); i1++) {
            LyricLine sl = (LyricLine)arraylist.get(i1);
            int j1 = i1 + 1 < arraylist.size() ? ((LyricLine)arraylist.get(i1 + 1)).startTime : this.durationMs;
            if (!sl.words.isEmpty()) {
                for (int k1 = 0; k1 < sl.words.size(); k1++) {
                    if (k1 + 1 < sl.words.size()) {
                        sl.words.get(k1).endTime = sl.words.get(k1 + 1).startTime;
                    } else {
                        sl.words.get(k1).endTime = j1;
                    }
                }
            } else {
                sl.words = this.estimateWordTimings(sl.text, sl.startTime, j1);
            }
        }

        arraylist.sort(Comparator.comparingInt(var0 -> ((LyricLine)var0).startTime));
        return arraylist;
    }

    private ParsedLyricLine h(String var1, int var2) {
        ArrayList arraylist = new ArrayList();
        StringBuilder stringbuilder = new StringBuilder();
        int i = 0;
        int j = var2;
        boolean flag = false;
        StringBuilder stringbuilder1 = new StringBuilder();

        while (i < var1.length()) {
            char c0 = var1.charAt(i);
            if (c0 == '<') {
                int k = var1.indexOf(62, i);
                if (k > i) {
                    if (stringbuilder1.length() > 0) {
                        String s = stringbuilder1.toString();
                        arraylist.add(new LyricWord(s, j));
                        stringbuilder.append(s);
                        stringbuilder1.setLength(0);
                    }

                    String s1 = var1.substring(i + 1, k);
                    Integer integer = this.parseWordTimestamp(s1);
                    if (integer != null) {
                        j = integer;
                        flag = true;
                    }

                    i = k + 1;
                    continue;
                }
            }

            stringbuilder1.append(c0);
            i++;
        }

        if (stringbuilder1.length() > 0) {
            String s2 = stringbuilder1.toString();
            arraylist.add(new LyricWord(s2, j));
            stringbuilder.append(s2);
        }

        arraylist.removeIf(var0 -> ((LyricWord)var0).text == null || ((LyricWord)var0).text.isEmpty());
        if (!flag) {
            arraylist.clear();
        }

        return new ParsedLyricLine(stringbuilder.toString(), arraylist);
    }

    private List<LyricWord> copyWords(List<LyricWord> var1) {
        ArrayList arraylist = new ArrayList();

        for (LyricWord sn : var1) {
            arraylist.add(new LyricWord(sn.text, sn.startTime));
        }

        return arraylist;
    }

    private Integer parseWordTimestamp(String var1) {
        try {
            String[] astring = var1.split(":");
            if (astring.length != 2) {
                return null;
            }

            int i = Integer.parseInt(astring[0]);
            String[] astring1 = astring[1].split("\\.");
            int j = Integer.parseInt(astring1[0]);
            int k = 0;
            if (astring1.length > 1) {
                String s = astring1[1];
                k = s.length() == 2 ? Integer.parseInt(s) * 10 : Integer.parseInt(s);
            }

            return i * 60000 + j * 1000 + k;
        } catch (Exception exception) {
            return null;
        }
    }

    private List<LyricWord> estimateWordTimings(String var1, int var2, int var3) {
        ArrayList arraylist = new ArrayList();
        if (var1 == null) {
            return arraylist;
        }

        int i = Math.max(0, var3 - var2);
        if (i == 0) {
            LyricWord snx = new LyricWord(var1, var2);
            snx.endTime = var2;
            arraylist.add(snx);
            return arraylist;
        }

        ArrayList arraylist1 = new ArrayList();
        ArrayList arraylist2 = new ArrayList();
        StringBuilder stringbuilder = new StringBuilder();
        int j = -1;

        for (int k = 0; k < var1.length(); k++) {
            char c0 = var1.charAt(k);
            boolean flag = Character.isWhitespace(c0);
            boolean flag1 = !flag && (Character.isLetterOrDigit(c0) || c0 == '\'' || c0 == 8217);
            int l = flag ? 0 : (flag1 ? 1 : 2);
            if (l != j && j != -1) {
                arraylist1.add(stringbuilder.toString());
                arraylist2.add(j);
                stringbuilder.setLength(0);
            }

            stringbuilder.append(c0);
            j = l;
        }

        if (stringbuilder.length() > 0) {
            arraylist1.add(stringbuilder.toString());
            arraylist2.add(j);
        }

        if (arraylist1.isEmpty()) {
            LyricWord snxxx = new LyricWord(var1, var2);
            snxxx.endTime = var3;
            arraylist.add(snxxx);
            return arraylist;
        }

        int i1 = -1;

        for (int j1 = arraylist1.size() - 1; j1 >= 0; j1--) {
            if ((Integer)arraylist2.get(j1) == 1) {
                i1 = j1;
                break;
            }
        }

        if (i1 < 0) {
            LyricWord snxx = new LyricWord(var1, var2);
            snxx.endTime = var3;
            arraylist.add(snxx);
            return arraylist;
        }

        ArrayList arraylist3 = new ArrayList();
        ArrayList arraylist4 = new ArrayList();

        for (int k1 = 0; k1 < i1; k1++) {
            arraylist3.add((String)arraylist1.get(k1));
            arraylist4.add((Integer)arraylist2.get(k1));
        }

        StringBuilder stringbuilder1 = new StringBuilder();

        for (int l1 = i1; l1 < arraylist1.size(); l1++) {
            stringbuilder1.append((String)arraylist1.get(l1));
        }

        arraylist3.add(stringbuilder1.toString());
        arraylist4.add(1);
        int size = arraylist3.size();
        double[] adouble = new double[size];
        double d0 = 0.0;

        for (int j2 = 0; j2 < size; j2++) {
            String s = (String)arraylist3.get(j2);
            int k2 = (Integer)arraylist4.get(j2);
            double d2;
            if (k2 != 1) {
                if (k2 == 2) {
                    d2 = this.getPunctuationWeight(s);
                } else {
                    d2 = Math.max(0.12, 0.1 * s.length());
                }
            } else {
                String s1 = s.replaceAll("[^\\p{L}\\p{M}\\p{Nd}'’]+", "");
                int l2 = this.countSyllables(s1);
                double d1 = 0.0;

                for (int i3 = 0; i3 < s.length(); i3++) {
                    char c1 = s.charAt(i3);
                    if (!Character.isWhitespace(c1)) {
                        boolean flag2 = Character.isUpperCase(c1);
                        d1 += 1.0 + (flag2 ? 0.1 : 0.0);
                    }
                }

                d2 = Math.max(1.0, 0.6 * Math.max(1, l2) + 0.4 * Math.max(1.0, d1));
                if (s1.matches(".*([aAeEiIoOuUyY])\\1{2,}.*")) {
                    d2 += 0.6;
                }

                if (s1.length() >= 3 && s1.equals(s1.toUpperCase())) {
                    d2 += 0.5;
                }
            }

            if (j2 == size - 1) {
                d2 *= 1.45;
            }

            adouble[j2] = d2;
            d0 += d2;
        }

        if (d0 <= 0.0) {
            Arrays.fill(adouble, 1.0);
            d0 = size;
        }

        int[] aint = new int[size];

        for (int j3 = 0; j3 < size; j3++) {
            int k3 = (Integer)arraylist4.get(j3);
            if (k3 == 1) {
                aint[j3] = 45;
            } else if (k3 == 2) {
                aint[j3] = 60;
            } else {
                aint[j3] = Math.min(40, 10 * Math.max(1, ((String)arraylist3.get(j3)).length()));
            }
        }

        int l3 = Math.min(Math.max(100, (int)Math.round(0.22 * i)), i);
        aint[size - 1] = Math.max(aint[size - 1], l3);
        double[] adouble1 = new double[size];

        for (int i4 = 0; i4 < size; i4++) {
            adouble1[i4] = i * (adouble[i4] / d0);
        }

        int[] aint1 = new int[size];
        int j4 = 0;
        double[] adouble2 = new double[size];

        for (int k4 = 0; k4 < size; k4++) {
            int l4 = (int)Math.floor(adouble1[k4]);
            if (l4 < aint[k4]) {
                l4 = aint[k4];
            }

            aint1[k4] = l4;
            j4 += l4;
            adouble2[k4] = adouble1[k4] - Math.floor(adouble1[k4]);
        }

        if (j4 > i) {
            int i5 = j4 - i;
            Integer[] ainteger = new Integer[size];

            for (int j5 = 0; j5 < size; j5++) {
                ainteger[j5] = j5;
            }

            Arrays.sort(ainteger, new SurplusWidthComparator(this, size, aint1, aint, arraylist4));
            Integer[] ainteger1 = ainteger;
            int length = ainteger1.length;

            for (int l5 = 0; l5 < length; l5++) {
                for (int i6 = ainteger1[l5]; i5 > 0 && aint1[i6] > aint[i6]; i5--) {
                    aint1[i6]--;
                }

                if (i5 == 0) {
                    break;
                }
            }
        } else if (j4 < i) {
            int l6 = i - j4;
            Integer[] ainteger2 = new Integer[size];

            for (int i7 = 0; i7 < size; i7++) {
                ainteger2[i7] = i7;
            }

            Arrays.sort(ainteger2, new RemainderWidthComparator(this, size, adouble2, arraylist4));

            for (int j7 = 0; l6-- > 0; j7++) {
                aint1[ainteger2[j7 % size]]++;
            }
        }

        int j6 = 0;

        for (int k6 : aint1) {
            j6 += k6;
        }

        if (j6 != i) {
            aint1[size - 1] = aint1[size - 1] + (i - j6);
        }

        int k7 = var2;

        for (int l7 = 0; l7 < size; l7++) {
            int i8 = k7;
            int j8 = l7 == size - 1 ? var3 : Math.min(var3, i8 + Math.max(1, aint1[l7]));
            LyricWord sn = new LyricWord((String)arraylist3.get(l7), i8);
            sn.endTime = j8;
            arraylist.add(sn);
            k7 = j8;
        }

        return arraylist;
    }

    private int countSyllables(String var1) {
        if (var1 == null) {
            return 1;
        }

        String s = var1.toLowerCase(Locale.ROOT).replaceAll("[^a-zA-Z]", "");
        if (s.isEmpty()) {
            return 1;
        }

        String s1 = s.replaceAll("(?i)[^aeiouy]+", " ").trim();
        int i = s1.isEmpty() ? 0 : s1.split("\\s+").length;
        if (s.endsWith("e") && i > 1) {
            i--;
        }

        return Math.max(1, i);
    }

    private double getPunctuationWeight(String var1) {
        String s = var1;
        int i = s.length();
        if (s.matches("\\.+")) {
            return 0.9 + 0.15 * (i - 1);
        } else if (s.matches("[!?]+")) {
            return 0.8 + 0.1 * (i - 1);
        } else if (s.matches("[,;:]+")) {
            return 0.55 + 0.08 * (i - 1);
        } else if (s.matches("[—–-]+")) {
            return 0.55 + 0.05 * (i - 1);
        }
        return s.matches("[()\\[\\]\"“”]+") ? 0.35 : 0.3;
    }

    private int parseInt(String var1, int var2) {
        try {
            return Integer.parseInt(var1);
        } catch (Exception exception) {
            return var2;
        }
    }

    private int z(int var1) {
        if (!this.lyricsAvailable) {
            return -1;
        }

        if (this.syncedLyrics && !this.syncedLines.isEmpty()) {
            if (var1 < this.syncedLines.get(0).startTime + 0) {
                return -1;
            }

            int i = 0;
            int j = this.syncedLines.size() - 1;
            int k = -1;

            while (i <= j) {
                int l = i + j >>> 1;
                if (this.syncedLines.get(l).startTime <= var1) {
                    k = l;
                    i = l + 1;
                } else {
                    j = l - 1;
                }
            }

            return k;
        } else if (!this.syncedLyrics && !this.plainLines.isEmpty() && this.durationMs > 0) {
            int i1 = (int)Math.floor((double)var1 / this.durationMs * this.plainLines.size());
            return Math.max(0, Math.min(this.plainLines.size() - 1, i1));
        }
        return -1;
    }

    private int getCurrentLineIndex() {
        return this.z(this.progressMs);
    }

    private int getLineCount() {
        return this.syncedLyrics ? this.syncedLines.size() : this.plainLines.size();
    }

    private String getLineText(int var1) {
        if (this.syncedLyrics) {
            return var1 >= 0 && var1 < this.syncedLines.size() ? this.syncedLines.get(var1).text : "";
        }
        return var1 >= 0 && var1 < this.plainLines.size() ? this.plainLines.get(var1) : "";
    }

    private String requestAccessToken(String var1) {
        Map map = this.loadCredentials();
        String s = (String)map.get("client_id");
        String s1 = (String)map.get("client_secret");
        if (s == null) {
            s = "";
        }

        if (s1 == null) {
            s1 = "";
        }

        try {
            URL url = new URL("https://accounts.spotify.com/api/token");
            HttpURLConnection httpurlconnection = (HttpURLConnection)url.openConnection();
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setDoOutput(true);
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setRequestProperty("User-Agent", "RiseClient/SpotifyModule");
            httpurlconnection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((s + ":" + s1).getBytes()));
            httpurlconnection.setConnectTimeout(4000);
            httpurlconnection.setReadTimeout(6000);

            try (OutputStream outputstream = httpurlconnection.getOutputStream()) {
                outputstream.write(("grant_type=authorization_code&code=" + var1 + "&redirect_uri=http://127.0.0.1:8888/callback").getBytes());
            }

            try (BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()))) {
                String s2 = bufferedreader.lines().reduce("", String::concat);
                String s3 = s2.split("\"access_token\":\"")[1].split("\"")[0];
                this.Wd = s3;
                return this.Wd;
            }
        } catch (SocketException socketexception) {
            if (this.debug.wo()) {
                ChatUtil.c("Spotify token connection issue: " + socketexception.getMessage());
            }

            return null;
        } catch (Exception exception) {
            if (this.debug.wo()) {
                exception.printStackTrace();
            }

            return null;
        } finally {
            this.authServerRunning = false;
        }
    }

    private void startServer() throws IOException {
        ServerSocket serversocket = new ServerSocket();
        serversocket.setReuseAddress(true);
        serversocket.bind(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 8888));
        serversocket.setSoTimeout(500);

        try (ServerSocket serversocket1 = serversocket) {
            while (this.authServerRunning) {

                Socket socket;
                try {
                    socket = serversocket1.accept();
                } catch (SocketTimeoutException sockettimeoutexception) {
                    continue;
                }

                try (
                    Socket socket1 = socket;
                    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
                ) {
                    socket1.setSoTimeout(6000);
                    String s = null;

                    String s1;
                    while ((s1 = bufferedreader.readLine()) != null && !s1.isEmpty()) {
                        if (s1.startsWith("GET") && s1.contains("code=")) {
                            s = s1.split("code=")[1].split(" ")[0];
                            break;
                        }
                    }

                    try (OutputStream outputstream = socket1.getOutputStream()) {
                        String s2 = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: 107\r\n\r\n<html><body><h2>Authorization successful!</h2><p>You can now re-enable the Music Player module.</p></body></html>";
                        outputstream.write(s2.getBytes());
                    } catch (SocketException socketexception) {
                        if (this.debug.wo()) {
                            ChatUtil.c("Auth callback connection issue: " + socketexception.getMessage());
                        }
                    }

                    if (s != null) {
                        this.requestAccessToken(s);
                        break;
                    }
                } catch (SocketException socketexception1) {
                    if (this.debug.wo()) {
                        ChatUtil.c("Auth callback connection issue: " + socketexception1.getMessage());
                    }
                } catch (Throwable throwable) {
                    if (this.debug.wo()) {
                        throwable.printStackTrace();
                    }
                }
            }
        }
    }

    public int uploadTexture(String var1) {
        try {
            BufferedImage bufferedimage = this.downloadImage(var1);
            return bufferedimage == null ? -1 : TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), bufferedimage, true, false);
        } catch (Exception exception) {
            return -1;
        }
    }

    private BufferedImage downloadImage(String var1) {
        try {
            URLConnection urlconnection = new URL(var1).openConnection();
            urlconnection.setRequestProperty("User-Agent", "RiseClient/SpotifyModule");
            urlconnection.setConnectTimeout(4000);
            urlconnection.setReadTimeout(6000);

            try (InputStream inputstream = urlconnection.getInputStream()) {
                return ImageIO.read(inputstream);
            }
        } catch (Throwable throwable1) {
            if (this.debug.wo()) {
                ChatUtil.c("Artwork fetch issue: " + throwable1.getClass().getSimpleName() + ": " + throwable1.getMessage());
            }

            return null;
        }
    }

    private void fetchArtwork(String var1, long var2) {
        if (var1 != null && !var1.isEmpty()) {
            if (var2 == this.trackGeneration) {
                if (!this.artworkFetching || !Objects.equals(var1, this.fetchingArtworkUrl)) {
                    if (this.pendingArtwork == null || !Objects.equals(var1, this.pendingArtworkUrl) || this.pendingArtworkToken != var2) {
                        this.fetchingArtworkUrl = var1;
                        this.artworkFetching = true;
                        new Thread(() -> {
                            try {
                                BufferedImage bufferedimage = this.downloadImage(var1);
                                if (bufferedimage != null && var2 == this.trackGeneration) {
                                    this.pendingArtworkUrl = var1;
                                    this.pendingArtworkToken = var2;
                                    this.pendingArtwork = bufferedimage;
                                }
                            } finally {
                                this.artworkFetching = false;
                            }
                        }, "SpotifyArtworkFetch-" + var2).start();
                    }
                }
            }
        }
    }

    private void uploadPendingArtwork() {
        BufferedImage bufferedimage = this.pendingArtwork;
        if (bufferedimage != null) {
            if (this.pendingArtworkToken == this.trackGeneration && this.artworkUrl != null && !this.artworkUrl.isEmpty() && Objects.equals(this.artworkUrl, this.pendingArtworkUrl)) {
                try {
                    this.artworkTexture = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), bufferedimage, true, false);
                } catch (Throwable throwable) {
                    if (this.debug.wo()) {
                        ChatUtil.c("Artwork upload issue: " + throwable.getClass().getSimpleName() + ": " + throwable.getMessage());
                    }
                } finally {
                    this.pendingArtwork = null;
                    this.pendingArtworkUrl = "";
                    this.pendingArtworkToken = -1L;
                }
            } else {
                this.pendingArtwork = null;
                this.pendingArtworkUrl = "";
                this.pendingArtworkToken = -1L;
            }
        }
    }

    private void resetArtwork(long var1) {
        this.artworkTexture = -1;
        this.pendingArtwork = null;
        this.pendingArtworkUrl = "";
        this.pendingArtworkToken = -1L;
        this.lyricsToken = -1L;
        this.artworkToken = -1L;
        this.requestedArtworkUrl = "";
    }

    private void startPoller() {
        if (!this.pollerRunning) {
            this.pollerRunning = true;
            this.pollerThread = new Thread(() -> {
                while (this.pollerRunning) {
                    try {
                        this.reloadCredentials();
                        this.ensureAuthorized();
                        String s = this.musicService.wo().getName();
                        if ("Cider".equals(s)) {
                            this.pollCider();
                        } else if (this.Wd != null && !this.Wd.isEmpty()) {
                            this.pollSpotify();
                        }
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    long i = Math.max(25L, this.refreshTicks.wo().intValue() * 50L);

                    try {
                        Thread.sleep(i);
                    } catch (InterruptedException interruptedexception) {
                        break;
                    }
                }
            }, "SpotifyPoller");
            this.pollerThread.setDaemon(true);
            this.pollerThread.start();
        }
    }

    private void stopPoller() {
        this.pollerRunning = false;
        if (this.pollerThread != null) {
            try {
                this.pollerThread.interrupt();
            } catch (Throwable throwable) {
            }

            this.pollerThread = null;
        }
    }

    public void startAuthFlow() {
        if (!this.authServerRunning) {
            this.authServerRunning = true;
            new Thread(() -> {
                try {
                    this.startServer();
                } catch (IOException ioexception) {
                    ioexception.printStackTrace();
                    this.authServerRunning = false;
                }
            }, "SpotifyAuthServer").start();

            try {
                this.reloadCredentials();
                if (!this.hasCredentials()) {
                    ChatUtil.b("Spotify client ID/secret missing. Use .spotify (clientid) / .spotify (clientsecret). Redirect URI: http://127.0.0.1:8888/callback");
                }

                Desktop.getDesktop()
                    .browse(
                        new URI(
                            "https://accounts.spotify.com/authorize?response_type=code&client_id="
                                + URLEncoder.encode(this.clientId, "UTF-8")
                                + "&redirect_uri="
                                + URLEncoder.encode("http://127.0.0.1:8888/callback", "UTF-8")
                                + "&scope="
                                + URLEncoder.encode("user-read-playback-state", "UTF-8")
                        )
                    );
            } catch (Exception exception) {
                exception.printStackTrace();
                this.authServerRunning = false;
            }
        }
    }

    @Override
    public void onEnable() {
        ChatUtil.b(
            "do .spotify (clientid) for the client ID and .spotify (clientsecret) for the client secret and use http://127.0.0.1:8888/callback instead of localhost"
        );
        String sxx = "Tutorial:";
        s sx = new s(sxx + " ");
        s sxxOnEnable = new s("https://youtu.be/-wbsOmDxBqk");
        ChatStyle chatstyle = new ChatStyle()
            .setChatClickEvent(new ClickEvent(Action.OPEN_URL, "https://youtu.be/-wbsOmDxBqk"))
            .setUnderlined(true)
            .setColor(this.rz().getChatAccentColor());
        sxxOnEnable.setChatStyle(chatstyle);
        sx.appendSibling(sxxOnEnable);
        this.minecraft.thePlayer.addChatMessage(sx);
        this.reloadCredentials();
        String s1 = this.musicService.wo().getName();
        if ("Spotify".equals(s1)) {
            if (!this.hasCredentials()) {
                ChatUtil.b("Spotify: Missing client credentials. Set them before connecting.");
            } else if (this.Wd.isEmpty()) {
                this.startAuthFlow();
            }
        }

        if ("Cider".equals(s1)) {
            this.pollCider();
        } else if (!this.Wd.isEmpty()) {
            this.pollSpotify();
        }

        this.songAtEnable = this.song;
        this.artworkTexture = -1;
        this.uploadPendingArtwork();
        if (this.artworkUrl != null && !this.artworkUrl.isEmpty()) {
            this.fetchArtwork(this.artworkUrl, this.trackGeneration);
        }

        this.startPoller();
    }

    @Override
    public void onDisable() {
        this.stopPoller();
    }

    private int getEstimatedProgressMs() {
        long now = System.currentTimeMillis() - this.progressTimestamp;
        int j = this.progressMs + (int)now;
        if (j < 0) {
            j = 0;
        }

        if (this.durationMs > 0 && j > this.durationMs) {
            j = this.durationMs;
        }

        return j;
    }

    private float easeOutCubic(float var1) {
        if (var1 <= 0.0F) {
            return 0.0F;
        }

        if (var1 >= 1.0F) {
            return 1.0F;
        }

        float f = 1.0F - var1;
        return 1.0F - f * f * f;
    }

    private float getLineAnimProgress() {
        float f = (float)(System.currentTimeMillis() - this.lineAnimStart) / 260.0F;
        return this.easeOutCubic(Math.min(1.0F, Math.max(0.0F, f)));
    }

    private void updateCurrentLine(int var1) {
        if (this.lyricsAvailable) {
            int i = this.z(var1);
            if (i < 0 && this.currentLineIndex >= 0 && this.syncedLyrics && !this.syncedLines.isEmpty()) {
                int j = this.syncedLines.get(0).startTime + 0;
                if (var1 >= j) {
                    return;
                }
            }

            if (this.currentLineIndex < 0 || i < 0 || i >= this.currentLineIndex || System.currentTimeMillis() - this.lastLineSwitchTime >= 120L) {
                if (i != this.currentLineIndex) {
                    long now = System.currentTimeMillis();
                    boolean flag = this.currentLineIndex < 0 || i > this.currentLineIndex;
                    this.previousLineIndex = this.currentLineIndex;
                    this.currentLineIndex = i;
                    this.lineAnimStart = flag ? now + 0L : now;
                    this.lastLineSwitchTime = now;
                    this.fillLineIndex = -1;
                    this.fillWidth = 0.0F;
                    this.fillBoostUntil = now + 260L;
                }
            }
        }
    }

    private int clampInt(int var1, int var2, int var3) {
        return Math.max(var2, Math.min(var3, var1));
    }
}
