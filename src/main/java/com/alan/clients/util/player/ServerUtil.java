package com.alan.clients.util.player;

import com.alan.clients.Client;
import com.alan.clients.compat.ProtectionToggles;
import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.util.OS;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.alan.clients.util.OSUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import javax.net.ssl.HttpsURLConnection;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.init.Blocks;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.apache.commons.lang3.StringUtils;

public class ServerUtil implements InstanceAccess
{
    public static long aOR;
    public static Object aPd;
    public static Map<String, Boolean> aPg;
    public static boolean aPf;
    public static Pattern aOP;
    public static Object aOX;
    public static int aPb;
    public static Pattern aOL;
    public static Pattern aOM;
    public static Map<String, CachedServer> aOS;
    public static String aPc;
    public static Pattern aOO;
    public static boolean aOZ;
    public static long aOQ;
    public static Object aPa;
    public static int aPe;
    public static int aOT;
    public static int aOW;
    public static String aOV;
    public static int aOY;
    public static Pattern aON;
    public static boolean aOU;
    public static OldServerPinger pinger;

    public static boolean vv() {
        final ScoreObjective objective = vw();
        if (objective == null) {
            return false;
        }
        final List<Object> list = (List<Object>)((Collection)objective.getScoreboard().getSortedScores(objective)).stream().filter(score2 -> ((Score)score2).getPlayerName() != null && !((Score)score2).getPlayerName().startsWith("#")).collect((Collector<? super Object, ?, List<Object>>)Collectors.toList());
        final ArrayList list2 = (ArrayList)((list.size() > 15) ? Lists.newArrayList(Iterables.skip((Iterable)list, list.size() - 15)) : list);
        final StringBuilder sb = new StringBuilder();
        final Iterator iterator = list2.iterator();
        while (iterator.hasNext()) {
            final Score score = (Score)iterator.next();
            final String textWithoutFormattingCodes = EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName((ScorePlayerTeam)objective.getScoreboard().getPlayersTeam(score.getPlayerName()), score.getPlayerName()));
            if (textWithoutFormattingCodes != null && !textWithoutFormattingCodes.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(textWithoutFormattingCodes);
            }
        }
        return ServerUtil.aOP.matcher(sb).find();
    }

    public static ServerData isOnline(final String s, final int n, final int n2) {
        try {
            final String string = (Object)s + ":" + n;
            final ServerData serverData = new ServerData(string, string, false);
            ServerUtil.pinger.ping(serverData, n2);
            return serverData;
        }
        catch (final Exception ex) {
            return null;
        }
    }

    public static boolean a(final Pattern[] array) {
        return b(array) != null;
    }

    public static boolean a(final ScoreObjective scoreObjective) {
        if (scoreObjective == null) {
            return false;
        }
        final Iterator iterator = scoreObjective.getScoreboard().getSortedScores(scoreObjective).iterator();
        while (iterator.hasNext()) {
            final Score score = (Score)iterator.next();
            if (score != null && score.getPlayerName() != null && !score.getPlayerName().startsWith("#")) {
                return true;
            }
        }
        return false;
    }

    public static String vu() {
        if (ServerUtil.aEg == null || ServerUtil.aEg.theWorld == null || ServerUtil.aEg.thePlayer == null) {
            return "";
        }
        int totalWorldTime = (int)((int)(ServerUtil.aEg.theWorld.getTotalWorldTime() / 5L));
        if (ServerUtil.aPd == ServerUtil.aEg.theWorld && ServerUtil.aPb == totalWorldTime) {
            return ServerUtil.aPc;
        }
        final ScoreObjective objective = vw();
        if (objective == null) {
            ServerUtil.aPd = ServerUtil.aEg.theWorld;
            ServerUtil.aPb = totalWorldTime;
            return ServerUtil.aPc = "";
        }
        final List<Object> list = (List<Object>)((Collection)objective.getScoreboard().getSortedScores(objective)).stream().filter(score2 -> {
            boolean b;
            if (((Score)score2).getPlayerName() != null && !((Score)score2).getPlayerName().startsWith("#")) {
                b = true;
            }
            else {
                b = (9 - 9 != 0);
            }
            return b;
        }).collect((Collector<? super Object, ?, List<Object>>)Collectors.toList());
        final ArrayList list2 = (ArrayList)((list.size() > 15) ? Lists.newArrayList(Iterables.skip((Iterable)list, list.size() - 15)) : list);
        final StringBuilder sb = new StringBuilder();
        final Iterator iterator = list2.iterator();
        while (iterator.hasNext()) {
            final Score score = (Score)iterator.next();
            final String textWithoutFormattingCodes = EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName((ScorePlayerTeam)objective.getScoreboard().getPlayersTeam(score.getPlayerName()), score.getPlayerName()));
            if (textWithoutFormattingCodes != null && !textWithoutFormattingCodes.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append(' ');
                }
                sb.append(textWithoutFormattingCodes);
            }
        }
        ServerUtil.aPd = ServerUtil.aEg.theWorld;
        ServerUtil.aPb = totalWorldTime;
        return ServerUtil.aPc = cj(sb.toString());
    }

    public static boolean vm() {
        if (ServerUtil.aEg == null || ServerUtil.aEg.theWorld == null || ServerUtil.aEg.thePlayer == null) {
            return false;
        }
        int totalWorldTime = (int)((int)(ServerUtil.aEg.theWorld.getTotalWorldTime() / 5L));
        if (ServerUtil.aPa == ServerUtil.aEg.theWorld && ServerUtil.aOY == totalWorldTime) {
            return ServerUtil.aOZ;
        }
        ServerUtil.aOZ = (vv() || ci("www.hypixel"));
        ServerUtil.aOY = totalWorldTime;
        ServerUtil.aPa = ServerUtil.aEg.theWorld;
        return false;
    }

    public static ScoreObjective vw() {
        if (ServerUtil.aEg == null || ServerUtil.aEg.theWorld == null || ServerUtil.aEg.thePlayer == null) {
            return null;
        }
        final Scoreboard scoreboard = ServerUtil.aEg.theWorld.getScoreboard();
        if (scoreboard == null) {
            return null;
        }
        final ScoreObjective objectiveInDisplaySlot = scoreboard.getObjectiveInDisplaySlot(1);
        ScoreObjective objectiveInDisplaySlot2 = null;
        final ScorePlayerTeam playersTeam = scoreboard.getPlayersTeam(ServerUtil.aEg.thePlayer.getName());
        if (playersTeam != null) {
            final EnumChatFormatting chatFormat = playersTeam.getChatFormat();
            if (chatFormat != null) {
                int colorIndex = chatFormat.getColorIndex();
                if (colorIndex > -1) {
                    objectiveInDisplaySlot2 = scoreboard.getObjectiveInDisplaySlot(3 + colorIndex);
                }
            }
        }
        if (a(objectiveInDisplaySlot2)) {
            return objectiveInDisplaySlot2;
        }
        if (a(objectiveInDisplaySlot)) {
            return objectiveInDisplaySlot;
        }
        return (objectiveInDisplaySlot2 != null) ? objectiveInDisplaySlot2 : objectiveInDisplaySlot;
    }

    public static String b(final Pattern[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        final String vu = vu();
        if (vu.isEmpty()) {
            return null;
        }
        int limit = array.length;
        for (int i = 0; i < limit; i++) {
            final Pattern pattern = array[i];
            if (pattern != null) {
                final Matcher matcher = pattern.matcher(vu);
                if (matcher.find()) {
                    return matcher.group();
                }
            }
        }
        return null;
    }


    public static String cj(final String s) {
        final String lowerCase = StringUtils.trimToEmpty(s).toLowerCase(Locale.ROOT);
        if (lowerCase.isEmpty()) {
            return "";
        }
        return ((String)Normalizer.normalize(lowerCase, Normalizer.Form.NFD).replaceAll("\\p{M}+", "")).replaceAll("\\s+", " ");
    }

    public static boolean nS() {
        if (ServerUtil.aEg == null || ServerUtil.aEg.thePlayer == null || ServerUtil.aEg.theWorld == null) {
            return false;
        }
        int totalWorldTime = (int)ServerUtil.aEg.theWorld.getTotalWorldTime();
        if (ServerUtil.aPe == totalWorldTime) {
            return ServerUtil.aPf;
        }
        int n11_hi = 0;
        int floor_double2 = MathHelper.floor_double(ServerUtil.aEg.thePlayer.posX);
        int floor_double3 = MathHelper.floor_double(ServerUtil.aEg.thePlayer.posZ);
        for (int i2 = -2; i2 <= 2; i2++) {
            for (int j = -2; j <= 2; j++) {
                if (i(floor_double2 + i2, floor_double3 + j)) {
                    n11_hi++;
                }
            }
        }
        ServerUtil.aPf = (((n11_hi >= 16) ? 1 : 0) != 0);
        ServerUtil.aPe = totalWorldTime;
        return false;
    }

    public static String aC(final String s) {
        final String trimToEmpty = StringUtils.trimToEmpty(s);
        int indexOf2 = trimToEmpty.indexOf(58);
        return (indexOf2 >= 0) ? trimToEmpty.substring(0, indexOf2) : trimToEmpty;
    }

    static {
        ServerUtil.aOL = Pattern.compile("^(?:[a-zA-Z0-9-]+\\.)*(?:hypixel\\.net|hypixel\\.io|technoblade\\.club)(?:\\.)?$", 2);
        ServerUtil.aOM = Pattern.compile("^([a-zA-Z0-9-]+)\\.[a-zA-Z0-9-]+\\.[a-zA-Z0-9-]+\\.fisx\\.uk$", 2);
        ServerUtil.aON = Pattern.compile("^([a-zA-Z0-9-]+)(?:\\.[a-zA-Z0-9-]+)*\\.liquidproxy\\.net$", 2);
        ServerUtil.aOO = Pattern.compile("Hypixel BungeeCord \\(.+\\) <- .+");
        ServerUtil.aOP = Pattern.compile("[^\\s/]{1,4}/[^\\s/]{1,4}/[^\\s/]{1,8}");
        ServerUtil.aOS = new ConcurrentHashMap<String, CachedServer>();
        ServerUtil.aOT = Integer.MIN_VALUE;
        ServerUtil.aOV = "";
        ServerUtil.aOW = Integer.MIN_VALUE;
        ServerUtil.aOY = Integer.MIN_VALUE;
        ServerUtil.aPb = Integer.MIN_VALUE;
        ServerUtil.aPc = "";
        ServerUtil.aPe = Integer.MIN_VALUE;
        ServerUtil.aPg = new HashMap<String, Boolean>();
        ServerUtil.pinger = new OldServerPinger();
    }

    public static boolean ci(final String s) {
        final ScoreObjective objective = vw();
        if (objective == null) {
            return false;
        }
        final List<Object> list = (List<Object>)((Collection)objective.getScoreboard().getSortedScores(objective)).stream().filter(score2 -> ((Score)score2).getPlayerName() != null && !((Score)score2).getPlayerName().startsWith("#")).collect((Collector<? super Object, ?, List<Object>>)Collectors.toList());
        final Iterator iterator = ((list.size() > 15) ? Lists.newArrayList(Iterables.skip((Iterable)list, list.size() - 15)) : list).iterator();
        while (iterator.hasNext()) {
            final Score score = (Score)iterator.next();
            if (StringUtils.containsIgnoreCase((CharSequence)EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName((ScorePlayerTeam)objective.getScoreboard().getPlayersTeam(score.getPlayerName()), score.getPlayerName())), (CharSequence)s)) {
                return true;
            }
        }
        return false;
    }

    public static boolean cg(final String s) {
        if (Client.a.getSecurityManager().nN()) {
            return false;
        }
        if (ServerUtil.aPg.containsKey(s)) {
            return Boolean.valueOf(ServerUtil.aPg.get((Object)s));
        }
        int containsIgnoreCase2 = (!ServerUtil.aEg.isIntegratedServerRunning() && StringUtils.containsIgnoreCase((CharSequence)LastConnectionComponent.ip, (CharSequence)s)) ? 1 : 0;
        ServerUtil.aPg.put(s, Boolean.valueOf((boolean)(containsIgnoreCase2 != 0)));
        return containsIgnoreCase2 != 0;
    }

    public static String ch(final String s) {
        //add code
        if (!ProtectionToggles.proxyLookup()) {
            return null;
        }

        final Matcher matcher = ServerUtil.aOM.matcher(s);
        if (matcher.matches()) {
            return c(s, "https://redacted.invalid/lookup-route/" + (Object)matcher.group(1), "target");
        }
        final Matcher matcher2 = ServerUtil.aON.matcher(s);
        if (matcher2.matches()) {
            return c(s, "https://api.liquidbounce.net/api/v2/proxy/lookup-route/" + (Object)matcher2.group(1), "domain");
        }
        return null;
    }

    public static boolean vn() {
        if (ServerUtil.aEg == null || ServerUtil.aEg.thePlayer == null || ServerUtil.aEg.theWorld == null) {
            return false;
        }
        int ticksExisted2 = ServerUtil.aEg.thePlayer.ticksExisted / 20;
        final String trimToEmpty = StringUtils.trimToEmpty(LastConnectionComponent.ip);
        int port2 = LastConnectionComponent.port;
        if (ServerUtil.aOX == ServerUtil.aEg.theWorld && ServerUtil.aOT == ticksExisted2 && ServerUtil.aOW == port2 && StringUtils.equals((CharSequence)ServerUtil.aOV, (CharSequence)trimToEmpty)) {
            return ServerUtil.aOU;
        }
        ServerUtil.aOU = vo();
        ServerUtil.aOT = ticksExisted2;
        ServerUtil.aOV = trimToEmpty;
        ServerUtil.aOW = port2;
        ServerUtil.aOX = ServerUtil.aEg.theWorld;
        return ServerUtil.aOU;
    }

    public static String c(final String s, final String spec, final String s2) {
        final long currentTimeMillis = System.currentTimeMillis();
        final CachedServer aij = (CachedServer)ServerUtil.aOS.get(s);
        if (aij != null && aij.awJ > currentTimeMillis) {
            return aij.aPi;
        }
        final CachedServer aij2 = new CachedServer();
        try {
            final HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL(spec).openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setConnectTimeout(2500);
            httpsURLConnection.setReadTimeout(2500);
            httpsURLConnection.setUseCaches(false);
            try {
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
                try {
                    final StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        sb.append(line);
                    }
                    final JsonObject asJsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();
                    aij2.aPi = ((asJsonObject.has(s2) && !asJsonObject.get(s2).isJsonNull()) ? aC(StringUtils.trimToEmpty(asJsonObject.get(s2).getAsString()).toLowerCase(Locale.ENGLISH)) : "");
                    aij2.awJ = currentTimeMillis + 300000L;
                    bufferedReader.close();
                }
                catch (final Throwable t) {
                    try {
                        bufferedReader.close();
                    }
                    catch (final Throwable e) {
                        t.addSuppressed(e);
                    }
                    throw t;
                }
                httpsURLConnection.disconnect();
            }
            finally {
                httpsURLConnection.disconnect();
            }
        }
        catch (final Exception ex) {
            aij2.aPi = "";
            aij2.awJ = currentTimeMillis + 30000L;
        }
        ServerUtil.aOS.put(s, aij2);
        return aij2.aPi;
    }

    public static boolean vr() {
        if (ServerUtil.aEg == null || ServerUtil.aEg.thePlayer == null) {
            return false;
        }
        final String clientBrand = ServerUtil.aEg.thePlayer.getClientBrand();
        return ((!StringUtils.isBlank((CharSequence)clientBrand) && ServerUtil.aOO.matcher(clientBrand).matches()) ? 1 : 0) != 0;
    }

    public static Path vt() {
        final OS rv = OSUtil.rV();
        if (rv == OS.WINDOWS) {
            final String getenv = System.getenv("WinDir");
            if (StringUtils.isBlank((CharSequence)getenv)) {
                return null;
            }
            final String first = getenv;
            final String[] more = { "System32", "drivers", null, null };
            more[2] = "etc";
            more[3] = "hosts";
            return Paths.get(first, more);
        }
        if (rv == OS.LINUX || rv == OS.MACOS || rv == OS.SOLARIS) {
            return Paths.get("/etc/hosts", new String[0]);
        }
        return null;
    }

    public static Block c(final int n, final int n2, final int n3) {
        return ServerUtil.aEg.theWorld.getBlockState(new BlockPos(n, n2, n3)).getBlock();
    }

    public static boolean vp() {
        final String ac = aC(StringUtils.trimToEmpty(LastConnectionComponent.ip).toLowerCase(Locale.ENGLISH));
        if (ac.isEmpty()) {
            return false;
        }
        if (ac.equals("localhost") || ac.startsWith("127.") || ac.equals("::1") || ac.equals("0:0:0:0:0:0:0:1")) {
            return false;
        }
        final Matcher matcher = ServerUtil.aOM.matcher(ac);
        if (matcher.matches()) {
            final String c = c(ac, "https://redacted.invalid/lookup-route/" + (Object)matcher.group(1), "target");
            return c.isEmpty() || ServerUtil.aOL.matcher(c).matches();
        }
        final Matcher matcher2 = ServerUtil.aON.matcher(ac);
        if (matcher2.matches()) {
            final String c2 = c(ac, "https://api.liquidbounce.net/api/v2/proxy/lookup-route/" + (Object)matcher2.group(1), "domain");
            return c2.isEmpty() || ServerUtil.aOL.matcher(c2).matches();
        }
        return ServerUtil.aOL.matcher(ac).matches() && LastConnectionComponent.port == 25565;
    }

    @Generated
    ServerUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean vs() {
        try {
            final Path path = vt();
            if (path == null || !Files.exists(path, new LinkOption[0]) || !Files.isReadable(path)) {
                return false;
            }
            final String lowerCase = new String(Files.readAllBytes(path)).toLowerCase(Locale.ENGLISH);
            return lowerCase.contains((CharSequence)"riseclient.com") || lowerCase.contains((CharSequence)"vantage") || lowerCase.contains((CharSequence)"hypixel.net") || lowerCase.contains((CharSequence)"www.hypixel.net") || lowerCase.contains((CharSequence)"hypixel");
        }
        catch (final Exception ex) {
            return false;
        }
    }

    public static boolean vq() {
        final String clientBrand = ServerUtil.aEg.thePlayer.getClientBrand();
        return StringUtils.isBlank((CharSequence)clientBrand) || ServerUtil.aOO.matcher(clientBrand).matches();
    }

    public static boolean a(final Pattern pattern) {
        if (pattern == null) {
            return false;
        }
        final String ac = aC(StringUtils.trimToEmpty(LastConnectionComponent.ip).toLowerCase(Locale.ENGLISH));
        if (ac.isEmpty()) {
            return false;
        }
        if (pattern.matcher(ac).find()) {
            return true;
        }
        final String ch = ch(ac);
        return ch != null && !ch.isEmpty() && pattern.matcher(ch).find();
    }

    public static boolean vo() {
        if (Client.a.getSecurityManager().nN()) {
            return false;
        }
        if (ServerUtil.aEg == null || ServerUtil.aEg.thePlayer == null || ServerUtil.aEg.theWorld == null || ServerUtil.aEg.isIntegratedServerRunning()) {
            return false;
        }
        return !StringUtils.containsIgnoreCase((CharSequence)LastConnectionComponent.ip, (CharSequence)"test") && !vs() && !nS() && vp() && vq() && vm();
    }

    public static boolean i(final int n, final int n2) {
        return c(n, 0, n2) == Blocks.bedrock && c(n, 1, n2) == Blocks.dirt && c(n, 2, n2) == Blocks.dirt && c(n, 3, n2) == Blocks.grass && c(n, 4, n2) == Blocks.air && c(n, 5, n2) == Blocks.air;
    }
}
