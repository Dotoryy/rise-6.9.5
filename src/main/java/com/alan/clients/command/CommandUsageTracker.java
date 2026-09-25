package com.alan.clients.command;

import com.alan.clients.Client;
import com.alan.clients.command.Command;
import com.alan.clients.module.Module;
import com.alan.clients.util.file.config.ConfigFile;
import com.alan.clients.util.file.data.DataManager;
import com.alan.clients.util.interfaces.Bindable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Map;

public final class CommandUsageTracker {
    private static final CommandUsageTracker aGM = new CommandUsageTracker();
    private final Map<String, Long> aGN = new HashMap<>();
    private final Map<String, Long> aGO = new HashMap<>();
    private final Map<String, Long> aGP = new HashMap<>();
    private final long aGQ = System.currentTimeMillis();
    private long aGR;
    private long aGS;
    private final Map<String, String> aGT = new HashMap<>();
    private String aGU;
    private String aGV;
    private String aGW;
    private String aGX;
    private long aGY;
    private long aGZ;
    private String aHa;
    private boolean loaded;

    public static CommandUsageTracker sJ() {
        return aGM;
    }

    private CommandUsageTracker() {
    }

    private File sK() {
        return new File(DataManager.DATA_DIRECTORY, "command_usage.json");
    }

    private synchronized void sL() {
        if (!this.loaded) {
            this.loaded = true;
            this.load();
        }
    }

    public long bz(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            this.sL();
            synchronized (this) {
                return this.aGN.getOrDefault(var1, 0L);
            }
        } else {
            return 0L;
        }
    }

    public String sM() {
        this.sL();
        synchronized (this) {
            return this.aGU;
        }
    }

    public String sN() {
        this.sL();
        synchronized (this) {
            return this.aGV;
        }
    }

    public String sO() {
        this.sL();
        synchronized (this) {
            return this.aGX;
        }
    }

    public long sP() {
        this.sL();
        synchronized (this) {
            return this.aGY;
        }
    }

    public long sQ() {
        this.sL();
        synchronized (this) {
            return this.aGZ;
        }
    }

    public String sR() {
        this.sL();
        synchronized (this) {
            return this.aHa;
        }
    }

    public void bA(String var1) {
        this.sL();
        synchronized (this) {
            String s = this.bB(var1);
            if (s != null) {
                String s1 = this.bB(this.aHa);
                if (s1 == null || !s.equalsIgnoreCase(s1) || this.aGZ < this.aGQ) {
                    this.aGZ = System.currentTimeMillis();
                    this.aHa = s;
                    this.aGP.put("event:serverJoin", this.aGZ);
                    this.su();
                }
            }
        }
    }

    private String bB(String var1) {
        if (var1 == null) {
            return null;
        }

        String s = var1.trim();
        if (s.isEmpty()) {
            return null;
        }

        if (s.startsWith("http://")) {
            s = s.substring("http://".length());
        }

        if (s.startsWith("https://")) {
            s = s.substring("https://".length());
        }

        int i = s.indexOf(58);
        if (i > 0) {
            s = s.substring(0, i);
        }

        String s1 = s.trim();
        return s1.isEmpty() ? null : s1.toLowerCase(Locale.ROOT);
    }

    public void bC(String var1) {
        if (var1 != null) {
            String s = var1.trim();
            if (!s.isEmpty()) {
                if (!"latest".equalsIgnoreCase(s)) {
                    this.sL();

                    try {
                        JsonObject jsonobject = ConfigFile.b(false, false);
                        String s1 = this.a(jsonobject);
                        synchronized (this) {
                            this.aGV = s;
                            this.aGX = s1;
                            this.aGY = System.currentTimeMillis();
                            this.aGT.clear();
                            this.aGT.putAll(this.d(jsonobject));
                        }

                        this.su();
                    } catch (Throwable throwable) {
                    }
                }
            }
        }
    }

    public void sS() {
        synchronized (this) {
            this.aGS = this.aGR;
            this.aGR = System.currentTimeMillis();
        }
    }

    public long sT() {
        synchronized (this) {
            return this.aGS > 0L && this.aGR > 0L ? Math.max(0L, this.aGR - this.aGS) : -1L;
        }
    }

    public int sU() {
        try {
            JsonObject jsonobject = ConfigFile.b(false, false);
            Map map = this.d(jsonobject);
            synchronized (this) {
                if (this.aGT.isEmpty()) {
                    return -1;
                }

                int i = 0;

                for (Entry entry : this.aGT.entrySet()) {
                    String s = (String)entry.getKey();
                    String s1 = (String)entry.getValue();
                    String s2 = (String)map.get(s);
                    if (s2 != null && s1 != null && !s2.equals(s1)) {
                        i++;
                    }
                }

                return i;
            }
        } catch (Throwable throwable) {
            return -1;
        }
    }

    public String sV() {
        this.sL();
        synchronized (this) {
            return this.aGW;
        }
    }

    public void bD(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            this.sL();
            synchronized (this) {
                this.aGN.put(var1, this.aGN.getOrDefault(var1, 0L) + 1L);
                this.su();
            }
        }
    }

    public boolean c(String var1, long var2) {
        if (var1 == null || var1.isEmpty()) {
            return false;
        }

        if (var2 <= 0L) {
            return false;
        }

        this.sL();
        synchronized (this) {
            long i = this.aGP.getOrDefault(var1, 0L);
            return i > 0L && System.currentTimeMillis() - i <= var2;
        }
    }

    public String a(String var1, String[] var2) {
        if (var1 != null && !var1.trim().isEmpty()) {
            if (var2 != null && var2.length != 0) {
                this.sL();
                String s = var1.trim().toLowerCase(Locale.ROOT);
                synchronized (this) {
                    String s1 = null;
                    long i = -1L;
                    long j = -1L;
                    int k = Integer.MAX_VALUE;

                    for (String s2 : var2) {
                        if (s2 != null) {
                            String s3 = s2.trim();
                            if (!s3.isEmpty()) {
                                String s4 = s3.toLowerCase(Locale.ROOT);
                                String s5 = "cmd.alias:" + s + ":" + s4;
                                long l = this.aGN.getOrDefault(s5, 0L);
                                long i1 = this.aGP.getOrDefault(s5, 0L);
                                int j1 = s3.length();
                                if (l > i || l == i && i1 > j || l == i && i1 == j && j1 < k) {
                                    s1 = s3;
                                    i = l;
                                    j = i1;
                                    k = j1;
                                }
                            }
                        }
                    }

                    if (i <= 0L) {
                        s1 = null;
                        int k1 = Integer.MAX_VALUE;

                        for (String s6 : var2) {
                            if (s6 != null) {
                                String s7 = s6.trim();
                                if (!s7.isEmpty()) {
                                    int l1 = s7.length();
                                    if (l1 < k1) {
                                        k1 = l1;
                                        s1 = s7;
                                    }
                                }
                            }
                        }
                    }

                    return s1;
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    private void bE(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            this.sL();
            synchronized (this) {
                long now = System.currentTimeMillis();
                this.aGP.put(var1, now);
                this.aGN.put(var1, this.aGN.getOrDefault(var1, 0L) + 1L);
                this.su();
            }
        }
    }

    public List<String> j(String var1, int var2) {
        if (var1 == null || var1.isEmpty()) {
            return Collections.emptyList();
        }

        if (var2 <= 0) {
            return Collections.emptyList();
        }

        this.sL();
        synchronized (this) {
            ArrayList arraylist = new ArrayList();

            for (Entry entry : this.aGN.entrySet()) {
                String s = (String)entry.getKey();
                Long olong = (Long)entry.getValue();
                if (s != null && olong != null && s.startsWith(var1) && olong > 0L) {
                    arraylist.add(entry);
                }
            }

            arraylist.sort(Comparator.<Entry>comparingLong(var0 -> (Long)var0.getValue()).reversed());
            ArrayList arraylist1 = new ArrayList();

            for (int i = 0; i < Math.min(var2, arraylist.size()); i++) {
                arraylist1.add((String)((Entry)arraylist.get(i)).getKey());
            }

            return arraylist1;
        }
    }

    public boolean bF(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            this.sL();
            synchronized (this) {
                long now = System.currentTimeMillis();
                long j = this.aGO.getOrDefault(var1, 0L);
                return now >= j;
            }
        } else {
            return true;
        }
    }

    public void bG(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            this.sL();
            synchronized (this) {
                this.aGN.put("suggest.shown:" + var1, this.aGN.getOrDefault("suggest.shown:" + var1, 0L) + 1L);
                this.su();
            }
        }
    }

    public void bH(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            this.sL();
            synchronized (this) {
                long now = System.currentTimeMillis();
                this.aGO.put(var1, Math.max(this.aGO.getOrDefault(var1, 0L), now + 60000L));
                this.aGN.put("suggest.accepted:" + var1, this.aGN.getOrDefault("suggest.accepted:" + var1, 0L) + 1L);
                this.su();
            }
        }
    }

    public void bI(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            this.sL();
            synchronized (this) {
                long now = System.currentTimeMillis();
                String s = "suggest.ignored:" + var1;
                long j = this.aGN.getOrDefault(s, 0L) + 1L;
                this.aGN.put(s, j);
                long k = Math.min(4L, j - 1L);
                long l;
                if (var1.startsWith("config.save.dirty:")) {
                    l = Math.min(1800000L, 300000L * (1L << (int)k));
                } else {
                    l = Math.min(600000L, 30000L * (1L << (int)k));
                }

                this.aGO.put(var1, Math.max(this.aGO.getOrDefault(var1, 0L), now + l));
                this.su();
            }
        }
    }

    public void a(Command command, String[] var2) {
        if (command != null) {
            if (var2 != null && var2.length != 0) {
                try {
                    String s = String.join(" ", var2).trim();
                    if (!s.isEmpty()) {
                        this.sL();
                        synchronized (this) {
                            this.aGU = "." + s;
                        }
                    }
                } catch (Throwable throwable1) {
                }

                String[] astring = command.getExpressions();
                String s1 = astring != null && astring.length != 0 ? astring[0] : null;
                if (s1 == null || s1.trim().isEmpty()) {
                    s1 = var2[0];
                }

                if (s1 != null && !s1.trim().isEmpty()) {
                    String s2 = s1.trim().toLowerCase(Locale.ROOT);
                    this.bE("cmd:" + s2);

                    try {
                        String s3 = var2[0] == null ? "" : var2[0].trim();
                        if (!s3.isEmpty()) {
                            this.bE("cmd.alias:" + s2 + ":" + s3.toLowerCase(Locale.ROOT));
                        }
                    } catch (Throwable throwable) {
                    }

                    for (int i = 1; i < var2.length; i++) {
                        String s4 = var2[i];
                        if (s4 != null) {
                            String s5 = s4.trim();
                            if (!s5.isEmpty()) {
                                String s6 = s5.toLowerCase(Locale.ROOT);
                                int j = i - 1;
                                this.bE("arg:" + s2 + ":" + j + ":" + s6);
                                String s7 = this.bJ(s5);
                                if (s7 != null) {
                                    String s8 = s7.toLowerCase(Locale.ROOT);
                                    if (!s8.isEmpty() && !s8.equals(s6)) {
                                        this.bE("arg:" + s2 + ":" + j + ":" + s8);
                                    }
                                }
                            }
                        }
                    }

                    if (s2.equals("config")) {
                        this.d(var2);
                    } else if (s2.equals("bind")) {
                        this.e(var2);
                    }
                }
            }
        }
    }

    private void d(String[] var1) {
        if (var1.length >= 3) {
            String s = var1[1];
            String s1 = var1[2];
            if (s != null && s1 != null) {
                if (s.equalsIgnoreCase("load")) {
                    String s2 = s1.trim();
                    if (!s2.isEmpty()) {
                        this.bE("config.load:" + s2.toLowerCase(Locale.ROOT));

                        try {
                            synchronized (this) {
                                if (!"latest".equalsIgnoreCase(s2)) {
                                    this.aGV = s2;
                                    this.aGX = null;
                                    this.aGY = 0L;
                                }
                            }

                            this.su();
                        } catch (Throwable throwable) {
                        }
                    }
                }
            }
        }
    }

    private void e(String[] var1) {
        if (var1.length >= 2) {
            String s = var1[1];
            if (s != null) {
                String s1 = s.trim();
                if (!s1.isEmpty()) {
                    this.bE("bind.target:" + s1.toLowerCase(Locale.ROOT));

                    try {
                        synchronized (this) {
                            this.aGW = s1.replace(" ", "");
                        }

                        this.su();
                    } catch (Throwable throwable) {
                    }
                }
            }
        }
    }

    private String bJ(String var1) {
        try {
            String s = var1 == null ? "" : var1.trim();
            if (s.isEmpty()) {
                return null;
            }

            try {
                Bindable p = Client.a.t().a(s);
                if (p != null) {
                    return this.b(p.getAliases(), p.getName());
                }
            } catch (Throwable throwable1) {
            }

            try {
                Module module = Client.a.g().get(s);
                if (module != null) {
                    return this.b(module.getAliases(), module.getName());
                }
            } catch (Throwable throwable) {
            }
        } catch (Throwable throwable2) {
        }

        return null;
    }

    private String b(String[] var1, String var2) {
        String s = null;
        if (var1 != null) {
            for (String s1 : var1) {
                if (s1 != null) {
                    String s2 = s1.trim();
                    if (!s2.isEmpty()) {
                        String s3 = s2.replace(" ", "");
                        if (!s3.isEmpty()) {
                            if (!s2.contains(" ")) {
                                return s3;
                            }

                            if (s == null) {
                                s = s3;
                            }
                        }
                    }
                }
            }
        }

        if (s != null) {
            return s;
        }

        if (var2 == null) {
            return null;
        }

        String s4 = var2.trim().replace(" ", "");
        return s4.isEmpty() ? null : s4;
    }

    private void load() {
        try {
            File file1 = this.sK();
            if (file1.exists()) {
                try (BufferedReader bufferedreader = new BufferedReader(new FileReader(file1))) {
                    JsonObject jsonobject = Client.a.A().fromJson(bufferedreader, JsonObject.class);
                    if (jsonobject == null) {
                        return;
                    }

                    if (jsonobject.has("lastExecuted") && jsonobject.get("lastExecuted").isJsonPrimitive()) {
                        try {
                            String s = jsonobject.get("lastExecuted").getAsString();
                            if (s != null && !s.trim().isEmpty()) {
                                this.aGU = s.trim();
                            }
                        } catch (Throwable throwable6) {
                        }
                    }

                    if (jsonobject.has("lastLoadedConfig") && jsonobject.get("lastLoadedConfig").isJsonPrimitive()) {
                        try {
                            String s1 = jsonobject.get("lastLoadedConfig").getAsString();
                            if (s1 != null && !s1.trim().isEmpty()) {
                                this.aGV = s1.trim();
                            }
                        } catch (Throwable throwable5) {
                        }
                    }

                    if (jsonobject.has("lastLoadedConfigFingerprint") && jsonobject.get("lastLoadedConfigFingerprint").isJsonPrimitive()) {
                        try {
                            String s2 = jsonobject.get("lastLoadedConfigFingerprint").getAsString();
                            if (s2 != null && !s2.trim().isEmpty()) {
                                this.aGX = s2.trim();
                            }
                        } catch (Throwable throwable4) {
                        }
                    }

                    if (jsonobject.has("lastLoadedConfigAt") && jsonobject.get("lastLoadedConfigAt").isJsonPrimitive()) {
                        try {
                            this.aGY = jsonobject.get("lastLoadedConfigAt").getAsLong();
                        } catch (Throwable throwable3) {
                        }
                    }

                    if (jsonobject.has("lastBindTarget") && jsonobject.get("lastBindTarget").isJsonPrimitive()) {
                        try {
                            String s3 = jsonobject.get("lastBindTarget").getAsString();
                            if (s3 != null && !s3.trim().isEmpty()) {
                                this.aGW = s3.trim();
                            }
                        } catch (Throwable throwable2) {
                        }
                    }

                    if (jsonobject.has("lastServerJoinAt") && jsonobject.get("lastServerJoinAt").isJsonPrimitive()) {
                        try {
                            this.aGZ = jsonobject.get("lastServerJoinAt").getAsLong();
                        } catch (Throwable throwable1) {
                        }
                    }

                    if (jsonobject.has("lastServerHost") && jsonobject.get("lastServerHost").isJsonPrimitive()) {
                        try {
                            String s4 = jsonobject.get("lastServerHost").getAsString();
                            if (s4 != null && !s4.trim().isEmpty()) {
                                this.aHa = s4.trim();
                            }
                        } catch (Throwable throwable) {
                        }
                    }

                    if (jsonobject.has("suggestionNextEligibleAt") && jsonobject.get("suggestionNextEligibleAt").isJsonObject()) {
                        try {
                            JsonObject jsonobject1 = jsonobject.getAsJsonObject("suggestionNextEligibleAt");

                            for (Entry entry : jsonobject1.entrySet()) {
                                String s5 = (String)entry.getKey();
                                JsonElement jsonelement = (JsonElement)entry.getValue();
                                if (s5 != null
                                    && !s5.isEmpty()
                                    && jsonelement != null
                                    && jsonelement.isJsonPrimitive()
                                    && jsonelement.getAsJsonPrimitive().isNumber()) {
                                    long asLong = jsonelement.getAsLong();
                                    if (asLong > 0L) {
                                        this.aGO.put(s5, asLong);
                                    }
                                }
                            }
                        } catch (Throwable throwable7) {
                        }
                    }

                    JsonObject jsonobject2 = jsonobject.has("counts") && jsonobject.get("counts").isJsonObject() ? jsonobject.getAsJsonObject("counts") : null;
                    if (jsonobject2 != null) {
                        for (Entry entry1 : jsonobject2.entrySet()) {
                            String s6 = (String)entry1.getKey();
                            JsonElement jsonelement1 = (JsonElement)entry1.getValue();
                            if (s6 != null
                                && !s6.isEmpty()
                                && jsonelement1 != null
                                && jsonelement1.isJsonPrimitive()
                                && jsonelement1.getAsJsonPrimitive().isNumber()) {
                                long j = jsonelement1.getAsLong();
                                if (j > 0L) {
                                    this.aGN.put(s6, j);
                                }
                            }
                        }

                        return;
                    }
                }
            }
        } catch (Throwable throwable9) {
        }
    }

    private void su() {
        try {
            File file1 = this.sK();
            File file2 = file1.getParentFile();
            if (file2 != null && !file2.exists()) {
                file2.mkdirs();
            }

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("version", "6.9.5");
            if (this.aGU != null && !this.aGU.trim().isEmpty()) {
                jsonobject.addProperty("lastExecuted", this.aGU.trim());
            }

            if (this.aGV != null && !this.aGV.trim().isEmpty()) {
                jsonobject.addProperty("lastLoadedConfig", this.aGV.trim());
            }

            if (this.aGX != null && !this.aGX.trim().isEmpty()) {
                jsonobject.addProperty("lastLoadedConfigFingerprint", this.aGX.trim());
            }

            if (this.aGY > 0L) {
                jsonobject.addProperty("lastLoadedConfigAt", this.aGY);
            }

            if (this.aGW != null && !this.aGW.trim().isEmpty()) {
                jsonobject.addProperty("lastBindTarget", this.aGW.trim());
            }

            if (this.aGZ > 0L) {
                jsonobject.addProperty("lastServerJoinAt", this.aGZ);
            }

            if (this.aHa != null && !this.aHa.trim().isEmpty()) {
                jsonobject.addProperty("lastServerHost", this.aHa.trim());
            }

            JsonObject jsonobject1 = new JsonObject();

            for (Entry entry : this.aGO.entrySet()) {
                String s = (String)entry.getKey();
                Long olong = (Long)entry.getValue();
                if (s != null && !s.isEmpty() && olong != null && olong > 0L) {
                    jsonobject1.addProperty(s, olong);
                }
            }

            jsonobject.add("suggestionNextEligibleAt", jsonobject1);
            JsonObject jsonobject2 = new JsonObject();

            for (Entry entry1 : this.aGN.entrySet()) {
                String s1 = (String)entry1.getKey();
                Long olong1 = (Long)entry1.getValue();
                if (s1 != null && !s1.isEmpty() && olong1 != null && olong1 > 0L) {
                    jsonobject2.addProperty(s1, olong1);
                }
            }

            jsonobject.add("counts", jsonobject2);

            try (BufferedWriter bufferedwriter = new BufferedWriter(new FileWriter(file1))) {
                Client.a.A().toJson(jsonobject, bufferedwriter);
            }
        } catch (Throwable throwable1) {
        }
    }

    private String a(JsonObject json) {
        try {
            if (json == null) {
                return null;
            }

            JsonObject jsonobject = json.deepCopy();
            jsonobject.remove("Metadata");
            jsonobject.remove("theme");
            return aO(Client.a.A().toJson(jsonobject));
        } catch (Throwable throwable) {
            return null;
        }
    }

    private Map<String, String> d(JsonObject json) {
        HashMap hashmap = new HashMap();

        try {
            if (json == null) {
                return hashmap;
            }

            for (Entry entry : json.entrySet()) {
                String s = (String)entry.getKey();
                JsonElement jsonelement = (JsonElement)entry.getValue();
                if (s != null
                    && !s.isEmpty()
                    && jsonelement != null
                    && !"Metadata".equalsIgnoreCase(s)
                    && !"theme".equalsIgnoreCase(s)
                    && jsonelement.isJsonObject()) {
                    String s1 = aO(Client.a.A().toJson(jsonelement));
                    if (s1 != null) {
                        hashmap.put(s, s1);
                    }
                }
            }
        } catch (Throwable throwable) {
        }

        return hashmap;
    }

    private static String aO(String var0) {
        if (var0 == null) {
            return null;
        }

        try {
            MessageDigest messagedigest = MessageDigest.getInstance("SHA-256");
            byte[] abyte = var0.getBytes(StandardCharsets.UTF_8);
            byte[] abyte1 = messagedigest.digest(abyte);
            StringBuilder stringbuilder = new StringBuilder(abyte1.length * 2);

            for (byte b0 : abyte1) {
                stringbuilder.append(String.format("%02x", b0));
            }

            return stringbuilder.toString();
        } catch (Throwable throwable) {
            return null;
        }
    }
}
