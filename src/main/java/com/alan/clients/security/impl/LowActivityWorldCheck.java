package com.alan.clients.security.impl;

import com.alan.clients.Client;
import com.alan.clients.compat.ProtectionToggles;
import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.newevent.impl.other.ServerKickEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.security.SecurityFeature;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.alan.clients.util.player.ServerUtil;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Normalizer.Form;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.c;
import org.apache.commons.lang3.StringUtils;

public class LowActivityWorldCheck extends SecurityFeature {
    public static long awf;
    public int awu;
    public static String avX;
    public static Pattern awb;
    public String awy;
    public static Pattern awe;
    @EventLink
    public Listener<WorldChangeEvent> onWorldChange;
    public static Pattern awc;
    public static String avW;
    public static long avS;
    public long awt;
    public int awv;
    public static long awg;
    public boolean avH;
    public static long awh;
    public static Pattern awj;
    public static Pattern awm;
    public Map<Integer, TrackedPosition> awn = new HashMap<>();
    @EventLink
    public Listener<ServerKickEvent> onServerKick;
    public static Pattern awa;
    @EventLink
    public Listener<PacketReceiveEvent> onPacketReceive;
    public static Pattern awl;
    public long aws;
    public Set<String> awp;
    public static Pattern[] avY;
    public Object awq;
    public static Pattern awd;
    public long awr;
    public static double avT;
    @EventLink
    public Listener<TickEvent> onTick;
    public static SecurityPattern[] awi;
    public Set<Integer> awo = new HashSet<>();
    public Map<String, SecurityCacheEntry> awz;
    public static int avU;
    public static int avV;
    @EventLink
    public Listener<ServerJoinEvent> onServerJoin;
    public boolean awx;
    public static Pattern awk;
    public static Pattern[] avZ;
    public Object theWorld;
    public long aww;

    public void aD(String var1) {
        if (awj.matcher(var1).find()) {
            if (this.awp.add("numeric_countdown_combined")) {
                this.aE("numeric_countdown_combined");
            }
        } else if (var1.matches("^\\d+$")) {
            int parseInt2;
            try {
                parseInt2 = Integer.parseInt(var1);
            } catch (NumberFormatException numberformatexception) {
                return;
            }

            if (parseInt2 >= 1 && parseInt2 <= 5) {
                Long olong = System.currentTimeMillis();
                if (this.awu == parseInt2 + 1 && olong - this.aww <= 4000L) {
                    this.awv++;
                } else {
                    this.awv = 1;
                }

                this.awu = parseInt2;
                this.aww = olong;
                if (this.awv >= 3 && this.awp.add("numeric_countdown_sequence")) {
                    this.aE("numeric_countdown_sequence");
                }
            }
        }
    }

    public String aC(String var1) {
        String s = StringUtils.trimToEmpty(var1);
        int indexOf2 = s.indexOf(58);
        return indexOf2 >= 0 ? s.substring(0, indexOf2) : s;
    }

    public String p(String var1, String var2) throws java.io.IOException, java.net.MalformedURLException, java.net.ProtocolException {
        HttpsURLConnection httpsurlconnection = (HttpsURLConnection)new URL(var1).openConnection();
        httpsurlconnection.setRequestMethod("GET");
        httpsurlconnection.setConnectTimeout(3000);
        httpsurlconnection.setReadTimeout(3000);
        httpsurlconnection.setUseCaches(false);

        String s1;
        label95: {
            String s2;
            try {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(httpsurlconnection.getInputStream()));

                label91: {
                    try {
                        StringBuilder stringbuilder = new StringBuilder();

                        String s;
                        while ((s = bufferedreader.readLine()) != null) {
                            stringbuilder.append(s);
                        }

                        JsonObject jsonobject = new JsonParser().parse(stringbuilder.toString()).getAsJsonObject();
                        if (jsonobject.has(var2) && !jsonobject.get(var2).isJsonNull()) {
                            s2 = StringUtils.trimToEmpty(jsonobject.get(var2).getAsString()).toLowerCase(Locale.ROOT);
                            break label91;
                        }

                        s1 = "";
                    } catch (Throwable throwable1) {
                        try {
                            bufferedreader.close();
                        } catch (Throwable throwable) {
                            throwable1.addSuppressed(throwable);
                        }

                        throw throwable1;
                    }

                    bufferedreader.close();
                    break label95;
                }

                bufferedreader.close();
            } catch (Throwable throwable2) {
                httpsurlconnection.disconnect();
                throw throwable2;
            }

            httpsurlconnection.disconnect();
            return s2;
        }

        httpsurlconnection.disconnect();
        return s1;
    }

    public void nT() {
        if (aEg != null && !aEg.isIntegratedServerRunning() && aEg.theWorld != null && aEg.thePlayer != null) {
            String s = StringUtils.trimToEmpty(LastConnectionComponent.ip);
            if (!this.az(s)) {
                if (this.awq != aEg.theWorld) {
                    this.av("new_world_detected");
                    this.f(aEg.theWorld);
                }
            }
        }
    }

    public void nU() {
        this.awq = null;
        this.awr = 0L;
        this.awn.clear();
        this.awo.clear();
        this.awp.clear();
        this.aws = 0L;
        this.awt = 0L;
        this.awu = -1;
        this.awv = 0;
        this.aww = 0L;
        this.awx = false;
        this.awy = "";
    }


    public boolean a(c var1, String var2) {
        if (var1 == null) {
            return true;
        } else if (var1.getType() != 0) {
            return false;
        }
        String s = this.ax(var2);
        if (s.isEmpty()) {
            return true;
        } else if (awa.matcher(s).find()) {
            return false;
        }
        return awm.matcher(s).matches() ? false : awk.matcher(s).matches() || awl.matcher(s).matches();
    }

    public void nW() {
        if (!this.awx && this.awq != null && this.awq != this.theWorld && aEg != null && aEg.theWorld != null && aEg.thePlayer != null) {
            long totalWorldTime = aEg.theWorld.getTotalWorldTime();
            if (totalWorldTime >= this.awt) {
                this.awt = totalWorldTime + 5L;
                String s = ServerUtil.b(avZ);
                if (s != null && this.awp.add("scoreboard_mode_marker")) {
                    String s1 = s;
                    this.aE("scoreboard:" + s1);
                }
            }
        }
    }

    public void aE(String var1) {
        if (!this.awx) {
            this.awx = true;
            this.awy = StringUtils.abbreviate(StringUtils.defaultString(var1), 160);
        }
    }

    public void aw(String var1) {
        if (!this.awx && this.awq != null && this.awq != this.theWorld) {
            if (var1 != null && this.ay(var1) && aEg != null && aEg.theWorld != null) {
                this.theWorld = aEg.theWorld;
                this.avH = true;
            } else {
                String s = this.ax(var1);
                if (!s.isEmpty()) {
                    Matcher matcher = awa.matcher(s);
                    if (matcher.find() && this.awp.add("global_exempt_survival_smp_minehut")) {
                        String group = matcher.group();
                        this.aE("chat:" + group);
                    } else {
                        this.aD(s);
                        if (!this.awx) {
                            SecurityPattern[] aaag = awi;
                            int count = aaag.length;

                            for (int i = 0; i < count; i++) {
                                SecurityPattern aag = aaag[i];
                                Matcher matcher1 = aag.awG.matcher(s);
                                if (matcher1.find() && this.awp.add(aag.awF)) {
                                    String s4 = aag.awF;
                                    String s2 = matcher1.group();
                                    String s3 = s4;
                                    this.aE(s3 + ":" + s2);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean aA(String var1) {
        long k = 2208356211311774096L;
        String s = this.aB(var1);
        if (s == null) {
            return false;
        }

        long l = System.currentTimeMillis();
        SecurityCacheEntry aah = this.awz.get(var1);
        if (aah != null && aah.awJ > l) {
            return aah.awH || aah.awI;
        }

        SecurityCacheEntry aahx = new SecurityCacheEntry();
        aahx.awH = true;
        aahx.awI = false;
        aahx.awJ = l + 300000L;
        this.awz.put(var1, aahx);
        long i1 = k ^ ((awc.matcher(var1).matches() ? 1L : 0L) << 32 ^ k) & -1L << 32;
        String s4;
        if ((int)(i1 >>> 32) != 0) {
            String s1 = s;
            s4 = "https://redacted.invalid/lookup-route/" + s1;
        } else {
            String s3 = s;
            s4 = "https://api.liquidbounce.net/api/v2/proxy/lookup-route/" + s3;
        }

        String s2 = s4;
        Thread thread = new Thread(() -> {
            int j2_hi = 0;
            long l1 = 30000L;

            try {
                String s5 = this.p(s2, (int)(i1 >>> 32) != 0 ? "target" : "domain");
                j2_hi = s5 != null && awe.matcher(s5).find() ? 1 : 0;
                l1 = 300000L;
            } catch (Exception exception) {
            }

            SecurityCacheEntry aahxx = new SecurityCacheEntry();
            aahxx.awH = false;
            aahxx.awI = (j2_hi) != 0;
            aahxx.awJ = System.currentTimeMillis() + l1;
            this.awz.put(var1, aahxx);
        }, "WorldActivityProxyLookup");
        thread.setDaemon(true);
        thread.start();
        return true;
    }

    public LowActivityWorldCheck() {
        this.awp = new HashSet<>();
        this.awu = -1;
        this.awy = "";
        this.awz = new ConcurrentHashMap<>();
        this.onTick = var1 -> {
            this.nT();
            this.nW();
            this.nV();
        };
        this.onPacketReceive = var1 -> {
            if (var1.getPacket() instanceof c) {
                c c = (c)var1.getPacket();
                String s = c.getChatComponent() != null ? c.getChatComponent().getUnformattedText() : "";
                int flag = !this.a(c, s) ? 1 : 0;
                String s1 = this.ax(s);
                if (flag != 0
                    && s != null
                    && (s1.contains("you were spawned in limbo.") || s1.contains("you are afk, move around to return from afk."))
                    && aEg != null
                    && aEg.theWorld != null) {
                    this.theWorld = aEg.theWorld;
                    this.avH = true;
                    return;
                }

                if (s != null && this.ay(s) && aEg != null && aEg.theWorld != null) {
                    this.theWorld = aEg.theWorld;
                    this.avH = true;
                    return;
                }

                if (flag != 0) {
                    this.aw(s);
                }
            }
        };
        this.onWorldChange = var1 -> {
            this.av("world_change");
            if (this.avH) {
                this.theWorld = aEg != null ? aEg.theWorld : null;
                this.avH = false;
            } else {
                this.theWorld = null;
            }
        };
        this.onServerKick = var1 -> this.av("server_kick");
        this.onServerJoin = var1 -> this.av("server_join");
    }

    public String aB(String var1) {
        Matcher matcher = awc.matcher(var1);
        if (matcher.matches()) {
            return matcher.group(1);
        }

        Matcher matcher1 = awd.matcher(var1);
        return matcher1.matches() ? matcher1.group(1) : null;
    }

    public void av(String var1) {
        if (this.awq != null) {
            if (this.awq != this.theWorld && !this.avH) {
                long l = System.currentTimeMillis() - this.awr;
                int size2 = this.awo.size();
                int empty = !this.awp.isEmpty() ? 1 : 0;
                if (l >= 30000L && size2 < 5 && !this.awx && empty == 0) {
                    Client.a.getSecurityManager().at(this.getReason());
                }

                this.nU();
            } else {
                this.nU();
            }
        }
    }

    public String ax(String var1) {
        String s = StringUtils.trimToEmpty(var1).toLowerCase(Locale.ROOT);
        return s.isEmpty()
            ? ""
            : Normalizer.normalize(s, Form.NFD).replaceAll("\\p{M}+", "").replaceAll("\\s+", " ");
    }

    public boolean ay(String var1) {
        String s = this.ax(var1);
        if (s.isEmpty()) {
            return false;
        }

        Pattern[] apattern = avY;
        int count = apattern.length;

        for (int i = 0; i < count; i++) {
            if (apattern[i].matcher(s).find()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getReason() {
        return "lowactivityworld";
    }

    public void f(Object var1) {
        this.awq = var1;
        this.awr = System.currentTimeMillis();
        this.awn.clear();
        this.awo.clear();
        this.awp.clear();
        this.aws = 0L;
        this.awt = 0L;
        this.awu = -1;
        this.awv = 0;
        this.aww = 0L;
        this.awx = false;
        this.awy = "";
    }

    public void nV() {
        if (!this.awx && this.awq != null && this.awq != this.theWorld && aEg != null && aEg.theWorld != null && aEg.thePlayer != null) {
            if (this.awo.size() < 5) {
                long totalWorldTime = aEg.theWorld.getTotalWorldTime();
                if (totalWorldTime >= this.aws) {
                    this.aws = totalWorldTime + 5L;
                    Iterator iterator = aEg.theWorld.playerEntities.iterator();

                    while (iterator.hasNext()) {
                        EntityPlayer entityplayer = (EntityPlayer)iterator.next();
                        if (entityplayer != null && entityplayer != aEg.thePlayer) {
                            int entityId = entityplayer.getEntityId();
                            TrackedPosition aai = this.awn.computeIfAbsent(entityId, var1 -> new TrackedPosition(entityplayer.posX, entityplayer.posY, entityplayer.posZ));
                            double d3 = entityplayer.posX - aai.at;
                            double d4 = entityplayer.posY - aai.au;
                            double d5 = entityplayer.posZ - aai.av;
                            aai.awK = aai.awK + Math.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                            aai.at = entityplayer.posX;
                            aai.au = entityplayer.posY;
                            aai.av = entityplayer.posZ;
                            if (aai.awK > 10.0 && this.awo.add(entityId) && this.awo.size() >= 5) {
                                int size2 = this.awo.size();
                                this.aE("moving_players:" + size2);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    static {

        avY = new Pattern[]{
            Pattern.compile("\\b(?:register|registration|signup|sign\\s*up|login|log\\s*in|signin|sign\\s*in|authenticate|authentication|authme|password|passwd)\\b", 66),
            Pattern.compile("/(?:register|reg|login|l|signup|signin|auth|captcha)\\b", 66),
            Pattern.compile("\\b(?:registrate|registrarse|registrar|registro|inicia\\s+sesion|iniciar\\s+sesion|inicie\\s+sesion|logueate|loguearse|ingresa|ingresar|contrasena|clave)\\b", 66),
            Pattern.compile("\\b(?:registre|inscris(?:-toi)?|inscription|connecte(?:z|r)?|connexion|mot\\s+de\\s+passe)\\b", 66),
            Pattern.compile("\\b(?:registrati|registrazione|accedi|accedere|accesso|password)\\b", 66),
            Pattern.compile("\\b(?:registre-se|registrar|registrado|logar|entrar|acessar|acesso|senha)\\b", 66),
            Pattern.compile("\\b(?:anmelden|anmeldung|einloggen|einlogg?en|passwort|registrieren)\\b", 66),
            Pattern.compile("\\b(?:registreer|registreren|aanmelden|inloggen|wachtwoord)\\b", 66),
            Pattern.compile("\\b(?:zarejestruj|rejestruj|rejestracja|zaloguj|logowanie|haslo)\\b", 66),
            Pattern.compile("\\b(?:registrace|registrovat|prihlas(?:it|eni)|heslo)\\b", 66),
            Pattern.compile("\\b(?:registrati-va|registrati|autentificare|autentifica-te|parola)\\b", 66),
            Pattern.compile("\\b(?:kayit\\s*ol|kaydol|giris\\s*yap|sifre|parola|dogrula)\\b", 66),
            Pattern.compile("\\b(?:dang\\s*ky|dang\\s*nhap|mat\\s*khau|xac\\s*thuc)\\b", 66),
            Pattern.compile("\\b(?:daftar|masuk|kata\\s*sandi|sandi|otentikasi)\\b", 66),
            Pattern.compile("\\b(?:rekisteroidy|rekisterointi|kirjaudu|salasana)\\b", 66),
            Pattern.compile("\\b(?:\u03b5\u03b3\u03b3\u03c1\u03b1\u03c6\u03b7|\u03c3\u03c5\u03bd\u03b4\u03b5\u03c3\u03b7|\u03ba\u03c9\u03b4\u03b9\u03ba\u03bf\u03c2|\u03ba\u03c9\u03b4\u03b9\u03ba\u03bf\u03c3|\u03c0\u03b9\u03c3\u03c4\u03bf\u03c0\u03bf\u03b9\u03b7\u03c3\u03b7)\\b", 66),
            Pattern.compile("\\b(?:\u0440\u0435\u0433\u0438\u0441\u0442(?:\u0440\u0430\u0446\u0438\u044f|\u0440\u0438\u0440\u0443\u0439\u0441\u044f|\u0440\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c)|\u0437\u0430\u0440\u0435\u0433\u0438\u0441\u0442\u0440(?:\u0438\u0440\u0443\u0439\u0441\u044f|\u0438\u0440\u0443\u0439\u0442\u0435\u0441\u044c)|\u0432(?:\u043e\u0438|\u043e\u0439)\u0434\u0438(?:\u0442\u0435)?|\u0432\u0445\u043e\u0434|\u0430\u0432\u0442\u043e\u0440\u0438\u0437(?:\u0443\u0439\u0441\u044f|\u0443\u0439\u0442\u0435\u0441\u044c|\u0430\u0446\u0438\u044f)|\u043f\u0430\u0440\u043e\u043b\u044c)\\b", 66),
            Pattern.compile("\\b(?:\u0440\u0435\u0454\u0441\u0442\u0440(?:\u0430\u0446\u0456\u044f|\u0443\u0439\u0441\u044f|\u0443\u0439\u0442\u0435\u0441\u044f)|\u0437\u0430\u0440\u0435\u0454\u0441\u0442\u0440(?:\u0443\u0439\u0441\u044f|\u0443\u0439\u0442\u0435\u0441\u044f)|\u0443\u0432\u0456\u0439\u0434(?:\u0438|\u0456\u0442\u044c)|\u0432\u0445\u0456\u0434|\u0430\u0432\u0442\u043e\u0440\u0438\u0437(?:\u0443\u0439\u0441\u044f|\u0443\u0439\u0442\u0435\u0441\u044f|\u0430\u0446\u0456\u044f)|\u043f\u0430\u0440\u043e\u043b\u044c)\\b", 66),
            Pattern.compile("(?:\u6ce8\u518c|\u8a3b\u518a|\u767b\u5f55|\u767b\u5165|\u5bc6[\u7801\u78bc]|\u8ba4\u8bc1|\u9a57\u8b49)", 66),
            Pattern.compile("(?:\u767b\u9332|\u30ed\u30b0\u30a4\u30f3|\u30d1\u30b9\u30ef\u30fc\u30c9|\u8a8d\u8a3c)", 66),
            Pattern.compile("(?:\ud68c\uc6d0\uac00\uc785|\ub85c\uadf8\uc778|\ube44\ubc00\ubc88\ud638|\uc778\uc99d)", 66),
            Pattern.compile("(?:\u062a\u0633\u062c\u064a\u0644|\u062f\u062e\u0648\u0644|\u0643\u0644\u0645\u0629\\s*(?:\u0627\u0644\u0645\u0631\u0648\u0631|\u0627\u0644\u0633\u0631)|\u062a\u0623\u0643\u064a\u062f|\u062a\u062d\u0642\u0642)", 66),
            Pattern.compile("(?:\u0e25\u0e07\u0e17\u0e30\u0e40\u0e1a\u0e35\u0e22\u0e19|\u0e40\u0e02\u0e49\u0e32\u0e2a\u0e39\u0e48\u0e23\u0e30\u0e1a\u0e1a|\u0e23\u0e2b\u0e31\u0e2a\u0e1c\u0e48\u0e32\u0e19|\u0e22\u0e37\u0e19\u0e22\u0e31\u0e19\u0e15\u0e31\u0e27\u0e15\u0e19)", 66),
            Pattern.compile("(?:\u092a\u0902\u091c\u0940\u0915\u0930\u0923|\u0930\u091c\u093f\u0938\u094d\u091f\u0930|\u0932\u0949\u0917\u093f\u0928|\u092a\u093e\u0938\u0935\u0930\u094d\u0921|\u092a\u094d\u0930\u092e\u093e\u0923\u0940\u0915\u0930\u0923)", 66),
            Pattern.compile("\\b(?:captcha|verify|verification|verifying|verified|security\\s*check|auth\\s*code|pin\\s*code|access\\s*code|enter\\s+the\\s+code|type\\s+the\\s+code|enter\\s+the\\s+image\\s+code|image\\s+code|code\\s+from\\s+the\\s+image|code\\s+in\\s+chat|please\\s+wait|please\\s+verify|verify\\s+your\\s+client|verify\\s+your\\s+account|anti\\s*bot|human\\s*verification|robot\\s*check|complete\\s+the\\s+captcha)\\b", 66),
            Pattern.compile("\\b(?:verificando\\s+tu\\s+cliente|verifica(?:ndo|cion)?\\s+tu\\s+cliente|por\\s+favor\\s+espera|espera(?:\\s+un\\s+momento)?|introduce\\s+el\\s+codigo\\s+de\\s+la\\s+imagen|introduzca\\s+el\\s+codigo\\s+de\\s+la\\s+imagen|codigo\\s+de\\s+la\\s+imagen|codigo\\s+en\\s+el\\s+chat|verificacion\\s+humana|completa\\s+el\\s+captcha|cliente\\s+verificado)\\b", 66),
            Pattern.compile("\\b(?:verificando\\s+seu\\s+cliente|verifique\\s+seu\\s+cliente|por\\s+favor\\s+aguarde|aguarde(?:\\s+um\\s+momento)?|digite\\s+o\\s+codigo\\s+da\\s+imagem|codigo\\s+da\\s+imagem|codigo\\s+no\\s+chat|verificacao\\s+humana|complete\\s+o\\s+captcha)\\b", 66),
            Pattern.compile("\\b(?:verification\\s+de\\s+votre\\s+client|veuillez\\s+patienter|entrez\\s+le\\s+code\\s+de\\s+l'?image|code\\s+de\\s+l'?image|code\\s+dans\\s+le\\s+chat|verification\\s+humaine|completez\\s+le\\s+captcha)\\b", 66),
            Pattern.compile("\\b(?:verifiziere\\s+deinen\\s+client|bitte\\s+warten|gib\\s+den\\s+code\\s+aus\\s+dem\\s+bild\\s+ein|bildcode|code\\s+im\\s+chat|menschliche\\s+verifizierung|captcha\\s+abschliessen)\\b", 66),
            Pattern.compile("\\b(?:verifica(?:zione)?\\s+del\\s+client|attendi(?:\\s+un\\s+momento)?|inserisci\\s+il\\s+codice\\s+dell'?immagine|codice\\s+dell'?immagine|codice\\s+nella\\s+chat|verifica\\s+umana|completa\\s+il\\s+captcha)\\b", 66),
            Pattern.compile("\\b(?:weryfikacj(?:a|e)\\s+klienta|prosze\\s+czekac|wpisz\\s+kod\\s+z\\s+obrazka|kod\\s+z\\s+obrazka|kod\\s+na\\s+czacie|weryfikacja\\s+czlowieka|uzupelnij\\s+captcha)\\b", 66),
            Pattern.compile("\\b(?:\u043f\u0440\u043e\u0432\u0435\u0440(?:\u043a\u0430|\u044f\u0435\u043c)\\s+\u043a\u043b\u0438\u0435\u043d\u0442|\u043f\u043e\u0436\u0430\u043b\u0443\u0439\u0441\u0442\u0430\\s+\u043f\u043e\u0434\u043e\u0436\u0434\u0438\u0442\u0435|\u0432\u0432\u0435\u0434\u0438\u0442\u0435\\s+\u043a\u043e\u0434\\s+\u0441\\s+\u0438\u0437\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u0438\u044f|\u043a\u043e\u0434\\s+\u0441\\s+\u043a\u0430\u0440\u0442\u0438\u043d\u043a\u0438|\u043a\u043e\u0434\\s+\u0432\\s+\u0447\u0430\u0442|\u0447\u0435\u043b\u043e\u0432\u0435\u0447\u0435\u0441\u043a\u0430\u044f\\s+\u043f\u0440\u043e\u0432\u0435\u0440\u043a\u0430|\u043a\u0430\u043f\u0447\u0430)\\b", 66),
            Pattern.compile("\\b(?:\u043f\u0435\u0440\u0435\u0432\u0456\u0440(?:\u043a\u0430|\u044f\u0454\u043c\u043e)\\s+\u043a\u043b\u0456\u0454\u043d\u0442|\u0431\u0443\u0434\u044c\\s+\u043b\u0430\u0441\u043a\u0430\\s+\u0437\u0430\u0447\u0435\u043a\u0430\u0439\u0442\u0435|\u0432\u0432\u0435\u0434\u0456\u0442\u044c\\s+\u043a\u043e\u0434\\s+\u0437\\s+\u0437\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u043d\u044f|\u043a\u043e\u0434\\s+\u0456\u0437\\s+\u0437\u043e\u0431\u0440\u0430\u0436\u0435\u043d\u043d\u044f|\u043a\u043e\u0434\\s+\u0432\\s+\u0447\u0430\u0442|\u043f\u0435\u0440\u0435\u0432\u0456\u0440\u043a\u0430\\s+\u043b\u044e\u0434\u0438\u043d\u0438|\u043a\u0430\u043f\u0447\u0430)\\b", 66),
            Pattern.compile("(?:\u9a57\u8b49\u4f60\u7684\u5ba2\u6236\u7aef|\u9a8c\u8bc1\u4f60\u7684\u5ba2\u6237\u7aef|\u6b63\u5728\u9a8c\u8bc1\u4f60\u7684\u5ba2\u6237\u7aef|\u8acb\u7a0d\u5019|\u8bf7\u7a0d\u5019|\u8acb\u8f38\u5165\u5716\u50cf\u4e2d\u7684\u4ee3\u78bc|\u8bf7\u8f93\u5165\u56fe\u50cf\u4e2d\u7684\u4ee3\u7801|\u5716\u7247\u4ee3\u78bc|\u56fe\u50cf\u4ee3\u7801|\u804a\u5929\u4e2d\u7684\u4ee3\u78bc|\u804a\u5929\u4e2d\u7684\u4ee3\u7801|\u4eba\u5de5\u9a57\u8b49|\u4eba\u5de5\u9a8c\u8bc1|\u5b8c\u6210\u9a57\u8b49\u78bc|\u5b8c\u6210\u9a8c\u8bc1\u7801|captcha)", 66),
            Pattern.compile("(?:\u30af\u30e9\u30a4\u30a2\u30f3\u30c8\u3092\u78ba\u8a8d|\u78ba\u8a8d\u4e2d|\u3057\u3070\u3089\u304f\u304a\u5f85\u3061\u304f\u3060\u3055\u3044|\u753b\u50cf\u306e\u30b3\u30fc\u30c9\u3092\u5165\u529b|\u753b\u50cf\u30b3\u30fc\u30c9|\u30c1\u30e3\u30c3\u30c8\u306b\u30b3\u30fc\u30c9\u3092\u5165\u529b|\u4eba\u9593\u78ba\u8a8d|\u30ad\u30e3\u30d7\u30c1\u30e3|captcha)", 66),
            Pattern.compile("(?:\ud074\ub77c\uc774\uc5b8\ud2b8\ub97c\\s*\ud655\uc778|\ud655\uc778\\s*\uc911|\uc7a0\uc2dc\ub9cc\\s*\uae30\ub2e4\ub824|\uc774\ubbf8\uc9c0\\s*\ucf54\ub4dc\ub97c\\s*\uc785\ub825|\uc774\ubbf8\uc9c0\\s*\ucf54\ub4dc|\ucc44\ud305\uc5d0\\s*\ucf54\ub4dc\\s*\uc785\ub825|\uc0ac\ub78c\\s*\ud655\uc778|\ucea1\ucc28|captcha)", 66),
            Pattern.compile("(?:\u0627\u0644\u062a\u062d\u0642\u0642\\s*\u0645\u0646\\s*\u0627\u0644\u0639\u0645\u064a\u0644|\u062c\u0627\u0631(?:\u064d|\u064a)\\s*\u0627\u0644\u062a\u062d\u0642\u0642|\u064a\u0631\u062c\u0649\\s*\u0627\u0644\u0627\u0646\u062a\u0638\u0627\u0631|\u0627\u062f\u062e\u0644\\s*\u0631\u0645\u0632\\s*\u0627\u0644\u0635\u0648\u0631\u0629|\u0631\u0645\u0632\\s*\u0627\u0644\u0635\u0648\u0631\u0629|\u0627\u062f\u062e\u0644\\s*\u0627\u0644\u0631\u0645\u0632\\s*\u0641\u064a\\s*\u0627\u0644\u062f\u0631\u062f\u0634\u0629|\u062a\u062d\u0642\u0642\\s*\u0628\u0634\u0631\u064a|\u0643\u0627\u0628\u062a\u0634\u0627|captcha)", 66),
            Pattern.compile("(?:\u0e01\u0e23\u0e38\u0e13\u0e32\u0e23\u0e2d|\u0e01\u0e33\u0e25\u0e31\u0e07\u0e15\u0e23\u0e27\u0e08\u0e2a\u0e2d\u0e1a\u0e44\u0e04\u0e25\u0e40\u0e2d\u0e19\u0e15\u0e4c|\u0e43\u0e2a\u0e48\u0e23\u0e2b\u0e31\u0e2a\u0e08\u0e32\u0e01\u0e23\u0e39\u0e1b\u0e20\u0e32\u0e1e|\u0e23\u0e2b\u0e31\u0e2a\u0e23\u0e39\u0e1b\u0e20\u0e32\u0e1e|\u0e43\u0e2a\u0e48\u0e23\u0e2b\u0e31\u0e2a\u0e43\u0e19\u0e41\u0e0a\u0e17|\u0e22\u0e37\u0e19\u0e22\u0e31\u0e19\u0e27\u0e48\u0e32\u0e40\u0e1b\u0e47\u0e19\u0e21\u0e19\u0e38\u0e29\u0e22\u0e4c|\u0e41\u0e04\u0e1b\u0e0a\u0e32|captcha)", 66),
            Pattern.compile("(?:\u0915\u0943\u092a\u092f\u093e\\s*\u092a\u094d\u0930\u0924\u0940\u0915\u094d\u0937\u093e\\s*\u0915\u0930\u0947\u0902|\u0915\u094d\u0932\u093e\u0907\u0902\u091f\\s*\u0938\u0924\u094d\u092f\u093e\u092a\u093f\u0924|\u091b\u0935\u093f\\s*\u0915\u094b\u0921\\s*\u0926\u0930\u094d\u091c\\s*\u0915\u0930\u0947\u0902|\u091a\u093f\u0924\u094d\u0930\\s*\u0915\u094b\u0921|\u091a\u0948\u091f\\s*\u092e\u0947\u0902\\s*\u0915\u094b\u0921|\u092e\u093e\u0928\u0935\\s*\u0938\u0924\u094d\u092f\u093e\u092a\u0928|\u0915\u0948\u092a\u094d\u091a\u093e|captcha)", 66)
        };
        avZ = new Pattern[]{
            Pattern.compile("\\b(?:survival|smp|minehut)\\b", 66),
            Pattern.compile("\\b(?:ranked|unranked)\\b", 66),
            Pattern.compile("\\b(?:clasificado|clasificada|sin\\s+clasificar|classe|classee|non\\s+classe|classificata|classificato|non\\s+classificato|ranqueada|ranqueado|sem\\s+rank|besorolt|nem\\s+besorolt)\\b", 66),
            Pattern.compile("\\b(?:duel|duels)\\b", 66),
            Pattern.compile("\\b(?:bedwars|skywars|skyblock|factions|prison|lifesteal|practice|kitpvp|eggwars|oneblock|survival\\s+games|hungergames|hunger\\s+games|anarchy|towny|creative)\\b", 66),
            Pattern.compile("\\bbuild\\s*uhc\\b", 66),
            Pattern.compile("\\bclasico\\b|\\bclassico\\b|\\bclassic\\b", 66),
            Pattern.compile("\\barena\\s*pvp\\b", 66),
            Pattern.compile("\\barenapvp\\b", 66),
            Pattern.compile("\\belo\\b", 66),
            Pattern.compile("\\belo\\s+global\\b|\\branking\\s*\\(?elo\\)?\\b|\\btemporada\\s+ranked\\b", 66),
            Pattern.compile("\\b(?:coins?|tokens?|credits?|eco(?:nomy)?|dollars?)\\b", 66),
            Pattern.compile("\\b(?:creditos?|cr[e\u00e9]ditos?|economia|econom[i\u00ed]a)\\b", 66),
            Pattern.compile("\\b(?:creditos?|cr[e\u00e9]ditos?|economia|grana)\\b", 66),
            Pattern.compile("\\b(?:credits?|cr[e\u00e9]dits|economie|[\u00e9e]conomie)\\b", 66),
            Pattern.compile("\\b(?:muenzen|m\u00fcnzen|wirtschaft)\\b", 66),
            Pattern.compile("\\bwins?\\b", 66),
            Pattern.compile("\\b(?:victorias|victoria|vitorias|vitoria|victoires|victoire|siege|sieg|wygrane|wygrana|wygra|gyozelmek|gyozelem|castiguri|castig|vittorie|vittoria|thang|\u80dc\u5229|\u52dd\u5229|\uc2b9\ub9ac|\u52dd|\u043f\u043e\u0431\u0435\u0434)\\b", 66),
            Pattern.compile("\\bloss(?:es)?\\b", 66),
            Pattern.compile("\\b(?:derrotas|derrota|defeats?|defaites|defaite|perdidas|perdida|porazky|sconfitte|sconfitta|veszteseg|vesztesegek|infrangeri|thua|\u043f\u043e\u0440\u0430\u0436\u0435\u043d\u0438|\u8d25\u5317|\u6557\u5317|\ud328\ubc30|niederlage|niederlagen)\\b", 66),
            Pattern.compile("\\bwinstreak\\b|\\bwin\\s*streak\\b|\\bws\\b", 66),
            Pattern.compile("\\b(?:racha|racha\\s+de\\s+victorias|serie\\s+de\\s+vitorias|serie\\s+de\\s+vitto?rie|serie\\s+de\\s+victoires|sorozat|\u043f\u043e\u0431\u0435\u0434\u043d|\u80dc\u573a|\u9023\u52dd|\uc5f0\uc2b9)\\b", 66),
            Pattern.compile("\\bkit\\b|\\bmap\\b|\\bmapa\\b|\\bmappa\\b|\\bcarte\\b|\\bgegner\\b|\\badversaire\\b|\\bavversario\\b|\\bopponent\\b|\\boponente\\b|\\brivals?\\b|\\brounds?\\b|\\brondas?\\b|\\bmanches?\\b|\\bgoals?\\b|\\bgoles?\\b|\\bbuts?\\b|\\bconectados\\b|\\bconnected\\b|\\bplayers?\\b|\\bspieler\\b|\\bverbunden\\b|\\bwarteschlange\\b", 66),
            Pattern.compile("\\btiempo\\s+vivo\\b|\\basesinatos\\s+finales\\b|\\basesinatos\\b|\\bflechas\\b|\\bgolpes\\b|\\binformacion\\b|\\btemporada\\b|\\bglobal\\b|\\branking\\b|\\brangliste\\b|\\bsaison\\b", 66),
            Pattern.compile("\\b(?:cola|queue|buscando\\s+oponente|matchmaking|oponente|adversario|adversaire|gegner|avversario|encontrado|encontrei|trouve|gefunden|talalt|talalva|contra|versus|vs\\b|warteschlange|gegner\\s+gefunden|\u5f00\u59cb|\u958b\u59cb|\uc2dc\uc791|\u5bfe\u6226\u76f8\u624b|\uc0c1\ub300)\\b", 66),
            Pattern.compile("\\b(?:claim|claims|land|lands|chunk|chunks|home|homes|sethome|warp|warps|shop|shops|auction|auctions|auctionhouse|ah\\b|tpa|tpahere|teleport|teleport\\s+request|spawner|spawners|crate|crates)\\b", 66),
            Pattern.compile("\\b(?:reclamo|reclamos|terreno|terrenos|hogar|hogares|casa|casas|tienda|tiendas|subasta|subastas|teletransporte|proteccion|protecci\u00f3n|warp|warps|tpa|tpahere)\\b", 66),
            Pattern.compile("\\b(?:terreno|terrenos|casa|casas|loja|lojas|leilao|leil\u00e3o|teleporte|warp|warps|tpa|tpahere)\\b", 66),
            Pattern.compile("\\b(?:terrain|terrains|maison|maisons|boutique|boutiques|enchere|ench\u00e8re|encheres|ench\u00e8res|teleportation|warp|warps|tpa|tpahere)\\b", 66),
            Pattern.compile("\\b(?:grundstuck|grundst\u00fcck|home|homes|shop|shops|auktion|auktionen|teleport|warp|warps|tpa|tpahere)\\b", 66),
            Pattern.compile("\\b(?:\uc5f0\uacb0\ub428|\uc811\uc18d\uc911|\ud50c\ub808\uc774\uc5b4|\uc2b9\ub9ac|\ud328\ubc30|\ub7ad\ud06c|\ub7ad\ud0b9|\uc5d8\ub85c|\uc0c1\ub300|\uc2dc\uc98c|\ub300\uae30\uc5f4|\ub4c0\uc5bc|\uc2a4\uce74\uc774\uc6cc\uc988|\ubca0\ub4dc\uc6cc\uc988)\\b", 66),
            Pattern.compile("\\b(?:\u73a9\u5bb6|\u5728\u7ebf|\u9023\u7dda|\u8fde\u63a5|\u52dd\u5229|\u5931\u8d25|\u6557\u5317|\u6392\u540d|\u8d5b\u5b63|\u8cfd\u5b63|\u5c0d\u624b|\u5bf9\u624b|\u6c7a\u9b25|\u51b3\u6597|\u7a7a\u5c9b\u6218\u4e89|\u8d77\u5e8a\u6218\u4e89)\\b", 66),
            Pattern.compile("\\b(?:\u30d7\u30ec\u30a4\u30e4\u30fc|\u52dd\u5229|\u6557\u5317|\u30e9\u30f3\u30ad\u30f3\u30b0|\u30ec\u30fc\u30c8|\u5bfe\u6226\u76f8\u624b|\u30b7\u30fc\u30ba\u30f3|\u30c7\u30e5\u30a8\u30eb|\u30b9\u30ab\u30a4\u30a6\u30a9\u30fc\u30ba|\u30d9\u30c3\u30c9\u30a6\u30a9\u30fc\u30ba)\\b", 66),
            Pattern.compile("\\b(?:\u0433\u0440\u0430\u0432\u0435\u0446\u044c|\u043f\u0435\u0440\u0435\u043c\u043e\u0433\u0430|\u043f\u043e\u0440\u0430\u0437\u043a\u0430|\u0440\u0435\u0439\u0442\u0438\u043d\u0433|\u0441\u0435\u0437\u043e\u043d|\u0441\u0443\u043f\u0435\u0440\u043d\u0438\u043a|\u0447\u0435\u0440\u0433\u0430|\u0434\u0443\u0435\u043b\u044c|\u0441\u043a\u0430\u0439\u0432\u0430\u0440\u0441|\u0431\u0435\u0434\u0432\u0430\u0440\u0441)\\b", 66),
            Pattern.compile("\\b(?:\u03c0\u03b1\u03af\u03ba\u03c4\u03b7\u03c2|\u03bd\u03af\u03ba\u03b7|\u03ae\u03c4\u03c4\u03b1|\u03ba\u03b1\u03c4\u03ac\u03c4\u03b1\u03be\u03b7|\u03c3\u03b5\u03b6\u03cc\u03bd|\u03b1\u03bd\u03c4\u03af\u03c0\u03b1\u03bb\u03bf\u03c2|\u03bf\u03c5\u03c1\u03ac|\u03bc\u03bf\u03bd\u03bf\u03bc\u03b1\u03c7\u03af\u03b1)\\b", 66),
            Pattern.compile("\\b(?:\u0644\u0627\u0639\u0628|\u0641\u0648\u0632|\u062e\u0633\u0627\u0631\u0629|\u062a\u0631\u062a\u064a\u0628|\u0645\u0648\u0633\u0645|\u062e\u0635\u0645|\u0637\u0627\u0628\u0648\u0631|\u0645\u0628\u0627\u0631\u0632\u0629|\u0633\u0643\u0627\u064a\\s*\u0648\u0627\u0631\u0632|\u0628\u062f\\s*\u0648\u0627\u0631\u0632)\\b", 66),
            Pattern.compile("\\b(?:\u0916\u093f\u0932\u093e\u0921\u093c\u0940|\u091c\u0940\u0924|\u0939\u093e\u0930|\u0930\u0948\u0902\u0915|\u0930\u0948\u0902\u0915\u093f\u0902\u0917|\u0938\u0940\u091c\u0928|\u092e\u094c\u0938\u092e|\u092a\u094d\u0930\u0924\u093f\u0926\u094d\u0935\u0902\u0926\u0940|\u0935\u093f\u092a\u0915\u094d\u0937\u0940|\u0915\u0924\u093e\u0930|\u0921\u094d\u092f\u0942\u0932|\u092c\u0947\u0921\u0935\u093e\u0930\u094d\u0938|\u0938\u094d\u0915\u093e\u0908\u0935\u093e\u0930\u094d\u0938)\\b", 66),
            Pattern.compile("\\b(?:\u0e1c\u0e39\u0e49\u0e40\u0e25\u0e48\u0e19|\u0e0a\u0e19\u0e30|\u0e41\u0e1e\u0e49|\u0e2d\u0e31\u0e19\u0e14\u0e31\u0e1a|\u0e24\u0e14\u0e39\u0e01\u0e32\u0e25|\u0e04\u0e39\u0e48\u0e15\u0e48\u0e2d\u0e2a\u0e39\u0e49|\u0e04\u0e34\u0e27|\u0e14\u0e27\u0e25|\u0e2a\u0e01\u0e32\u0e22\u0e27\u0e2d\u0e23\u0e4c|\u0e40\u0e1a\u0e14\u0e27\u0e2d\u0e23\u0e4c)\\b", 66),
            Pattern.compile("\\b(?:classic|clasico|classico|nodebuff|no\\s*debuff|boxing|sumo|bridge|uhc|build\\s*uhc|skywars|bedwars|skyblock|factions|prison|lifesteal|practice|kitpvp|eggwars|oneblock|op|combo|blitz|megawalls|bow|parkour|tnt|gapple|arcade|arcade\\s*games?|warlords?|turbo\\s*kart\\s*racers?|tkr|blitzsg|blitz\\s*sg|blitz\\s*survival|survival\\s*games?|cops\\s*(?:and|&)\\s*criminals?|cnc|battleground|battlegrounds?|vampirez|the\\s*walls|mega\\s*walls|walls|mcgo|murder|murderer?|murder\\s*mystery|smash\\s*heroes?|paintball|quake|the\\s*tnt\\s*games?|tnt\\s*games?|rumble|pvp\\s*run|speed\\s*uhc|wool\\s*wars|party\\s*games|hole\\s*ladder|farm\\s*hunt|build\\s*battle|pixel\\s*paintball|boulevard|pit|pit\\s*xp?|housing|hybrid|ranked|unranked|mega|ult|ultimate|hive|cubecraft|mineville)\\b", 66),
            Pattern.compile("\\b(?:clasificado|clasificada|sin\\s*clasificar|ranqueada|ranqueado|rango| clasificacion|ranked)\\b", 66),
            Pattern.compile("\\b(?:juegos\\s*de\\s*arcade|arcade|warlords?|guerra\\s*de\\s*clanes?|batalla|criminoso|criminales|policias|policias\\s*y\\s*criminales|bsr|copia|en\\s*caja|box\\s*in|box|in\\s*the\\s*box|uno\\s*en\\s*caja|cops?|survival|supervivencia|juegos\\s*de\\s*supervivencia|bedwars|skywars|skyblock|facciones|prision|robo\\s*de\\s*vida|eggwars|isla\\s*unica|partida\\s*rapida|anarquia|pvp\\s*clasico|misterio\\s*de\\s*asesinato|asesinato|herores\\s*de\\s*golpe|paintball|quake|tnt|carreras\\s*de\\s*kart|wool\\s*wars|juegos\\s*de\\s*fiesta|escalera\\s*del\\s*agujero|caza\\s*de\\s*granjas|batalla\\s*de\\s*construccion|bola\\s*de\\s*pintura|pit|vivienda|h\u00edbrido|escarbar|lucha\\s*de\\s*cerdos|tag\\s*l\u00e1ser|esponja|soccer|futbol|scrimmage|duelo|duelos|lobby|vida\\s*robada|equipo\\s*rojo|equipo\\s*azul|jugador|partida|torneo)\\b", 66),
            Pattern.compile("\\b(?:class\u00e9|d\u00e9class\u00e9|non\\s*class\u00e9|ranked|non\\s*rank\u00e9)\\b", 66),
            Pattern.compile("\\b(?:arcade|jeux\\s*d'arcade|war lords?|guerre|flics?|criminels?|flics\\s*et\\s*criminels?|survie|survival|jeux\\s*de\\s*survie|bedwars|skywars|skyblock|factions|prison|vol\\s*de\\s*vie|eggwars|une\\s*seule\\s*ile|anarchie|practice|classique|myst\u00e8re\\s*d'|meurtre|meurtrier|h\u00e9ros?|paintball|quake|tnt|wool\\s*wars|jeux\\s*de\\s*f\u00eate|\u00e9chelle|chasse\\s*\u00e0\\s*la\\s*ferme|bataille\\s*de\\s*construction|peinture|trou|housing|carr\u00e9|h\u00edbrido|h\u00e9t\u00e9ro|hybrid|cochons?|laser\\s*tag|soccer|football|rugby|bot\\s*fight)\\b", 66),
            Pattern.compile("\\b(?:ranqueado|n\u00e3o\\s*ranqueado|rankeado|classificado)\\b", 66),
            Pattern.compile("\\b(?:arcade| jogos\\s*de\\s*arcade|war\\s*lords?|pol\u00edcia|criminoso|survival|sobrevivencia|bedwars|skywars|skyblock|factions|pris\u00e3o|assalto\\s*\u00e0\\s*vida|eggwars|um\\s*bloco|anarquia|pr\u00e1tica|mist\u00e9rio\\s*de\\s*assass\u00ednio|her\u00f3is\\s*de\\s*golpe|paintball|quake|tnt|corrida\\s*de\\s*kart|wool\\s*wars|jogos\\s*de\\s*festa|escada\\s*do\\s*buraco|ca\u00e7a\\s*\u00e0\\s*fazenda|bat\\s*de\\s*constru\u00e7\u00e3o|pit|vida\\s*h\u00edbrida|heterodoxo|hybrid)\\b", 66),
            Pattern.compile("\\b(?:ranglisten|gerankt|unranked|klassiert|rangliste)\\b", 66),
            Pattern.compile("\\b(?:klassiker|arcade|hallenspiel|war\\s*lords?|polizist|kriminelle|survival|\u00fcberleben|bedwars|skywars|skyblock|fraktionen|gef\u00e4ngnis|heben\\s*stehlen|eggwars|ein\\s*block|anarchie|praxis|klassisch|mord|ermordung|r\u00e4tsel|smash|helden|paintball|quake|tnt|kart\\s*fahren|wool\\s*wars|party\\s*spiele|leiter|loch|bau\\s*schlacht|pit|wohnung|hybrid|schildkr\u00f6ten|scheinwerfer)\\b", 66),
            Pattern.compile("\\b(?:ranked|unranked|klasyfikacja|klasy|turniej|ranga)\\b", 66),
            Pattern.compile("\\b(?:arcade|klasyka|wojna|gliniany|morderstwo|zab\u00f3jstwo|survival|surwiwal|bedwars|skywars|skyblock|frakcje|wiezienie|kradziez|eggwars|anarchia|practice|mistyczny|quake|tnt|wool\\s*wars|mini\\s*gry|pit|housing|hybrid)\\b", 66),
            Pattern.compile("\\b(?:\u0440\u0435\u0439\u0442\u0438\u043d\u0433|\u0440\u0430\u043d\u0433|\u0431\u0435\u0437\\s*\u0440\u0435\u0439\u0442\u0438\u043d\u0433\u0430|\u043a\u043b\u0430\u0441\u0441|\u0440\u0430\u043d\u0436\u0438\u0440\u043e\u0432\u0430\u043d\u043d\u044b\u0439)\\b", 66),
            Pattern.compile("\\b(?:\u0430\u0440\u043a\u0430\u0434\u0430|\u043a\u043b\u0430\u0441\u0441\u0438\u043a\u0430|\u0432\u043e\u0439\u043d\u0430|\u0432\u043e\u0440\u043b\u0434\u0441|\u0431\u0430\u043d\u0434\u0438\u0442\u044b|\u043f\u043e\u043b\u0438\u0446\u0438\u044f|\u0432\u044b\u0436\u0438\u0432\u0430\u043d\u0438\u0435|bedwars|skywars|skyblock|\u0444\u0440\u0430\u043a\u0446\u0438\u0438|\u0442\u044e\u0440\u044c\u043c\u0430|\u043a\u0440\u0430\u0436\u0430\\s*\u0436\u0438\u0437\u043d\u0438|eggwars|\u043e\u0434\u0438\u043d\\s*\u0431\u043b\u043e\u043a|\u0430\u043d\u0430\u0440\u0445\u0438\u044f|\u043f\u0440\u0430\u043a\u0442\u0438\u043a\u0430|\u0443\u0431\u0438\u0439\u0441\u0442\u0432\u043e|\u0443\u0431\u0438\u0439\u0446\u0430|\u043c\u0438\u0441\u0442\u0435\u0440\u0438|\u043a\u0432\u0435\u0441\u0442|quake|tnt|wool\\s*wars|\u0432\u0435\u0447\u0435\u0440\u0438\u043d\u043a\u0430|\u043c\u0438\u043d\u0438\\s*\u0438\u0433\u0440\u044b|pit|\u0436\u0438\u043b\u044c\u0435|\u0433\u0438\u0431\u0440\u0438\u0434|\u0447\u0435\u0440\u0435\u043f\u0430\u0445\u0438|\u043b\u0430\u0437\u0435\u0440\\s*\u0442\u0430\u0433|soccer|football)\\b", 66),
            Pattern.compile("\\b(?:s\u0131ral\u0131|s\u0131ras\u0131z|ranked|r\u00fctbe|klasi| klasman)\\b", 66),
            Pattern.compile("\\b(?:klasik|arcade|sava\u015f|b\u00f6lge|hayatta\\s*kalma|bedwars|skywars|skyblock|cephe|cezaevi|ya\u011fma|eggwars|tek\\s*blok|anarsi|pratik|macerac\u0131|katil|\u00f6ld\u00fcrme|gizem|quake|tnt|wool\\s*wars|parti\\s*oyunlar\u0131|pit|konut|hibrit|kaplumba\u011fa)\\b", 66),
            Pattern.compile("\\b(?:arcade| klasik|war\\s*lords?|polisi|kriminal|survival|bedwars|skywars|skyblock|faksi|tahanan|eggwars|satu\\s*blok|anarki|latihan|pembantaian|pembunuh|teka\\s*teki|quake|tnt|wool\\s*wars|party\\s*games|pit|housing|hybrid)\\b", 66),
            Pattern.compile("\\b(?:x\u1ebfp\\s*h\u1ea1ng|kh\u00f4ng\\s*x\u1ebfp\\s*h\u1ea1ng|h\u1ea1ng|ranked)\\b", 66),
            Pattern.compile("\\b(?:arcade|c\u1ed5\\s*\u0111i\u1ec3n|war\\s*lords?|c\u01b0\u1edbp|c\u1ea3nh\\s*s\u00e1t|sinh\\s*t\u1ed3n|bedwars|skywars|skyblock|chi\u1ebfn\\s*tranh|t\u00f9|eggwars|m\u1ed9t\\s*kh\u1ed1i|v\u00f4\\s*ch\u00ednh\\s*ph\u1ee7|th\u1ef1c\\s*h\u00e0nh|nghi\\s*can|th\u1ee7\\s*ph\u1ea1m|quake|tnt|wool\\s*wars|tr\u00f2\\s*ch\u01a1i\\s*ti\u1ec7c\\s*\u00edch|pit|nh\u00e0\\s*\u1edf|hybird|con\\s*r\u00f9a)\\b", 66),
            Pattern.compile("\\b(?:\uc21c\uc704|\ub7ad\ud06c|\ube44\uc21c\uc704|\ub7ad\ud0b9|\ub9ac\ub354\ubcf4\ub4dc)\\b", 66),
            Pattern.compile("\\b(?:\uc544\ucf00\uc774\ub4dc|\uace0\uc804|\uc804\uc7c1|\uc6cc\ub85c\ub4dc|\uc878\uc5c5|\ubc94\uc8c4|\uc11c\ubc14\uc774\ubc8c|bedwars|skywars|skyblock|\ud30c\ubc8c|\uad50\ub3c4\uc18c|eggwars|\uc6d0\ube14\ub85d|\ubb34\u653f\u5e9c|\uad00\ud589|\uc5f0\uc2b5|\uc0b4\uc778|\ubbf8\uc2a4\ud130\ub9ac|quake|tnt|\uc591\\s*\uc804\uc7c1|\ud30c\ud2f0\\s*\uac8c\uc784|pit|\uc8fc\uac70|\ud558\uc774\ube0c\ub9ac\ub4dc|\uac70\ubd81\uc774)\\b", 66),
            Pattern.compile("\\b(?:\u30e9\u30f3\u30af|\u30ec\u30fc\u30c8|\u975e\u30e9\u30f3\u30af|\u30e9\u30f3\u30ad\u30f3\u30b0)\\b", 66),
            Pattern.compile("\\b(?:\u30a2\u30fc\u30b1\u30fc\u30c9|\u30af\u30e9\u30b7\u30c3\u30af|\u30a6\u30a9\u30fc\u30ba|\u6226\u58eb|\u8b66\u5bdf|\u72af\u4eba|\u30b5\u30d0\u30a4\u30d0\u30eb|bedwars|skywars|skyblock|\u6d3e\u95a5|\u5211\u52d9\u6240|eggwars|\u30ef\u30f3\u30d6\u30ed\u30c3\u30af|\u7121\u653f\u5e9c|\u7df4\u7fd2|\u6bba\u4eba|\u30df\u30b9\u30c6\u30ea\u30fc|quake|tnt|\u30a6\u30fc\u30eb\u30a6\u30a9\u30fc\u30ba|\u30d1\u30fc\u30c6\u30a3\u30fc\u30b2\u30fc\u30e0|pit|\u4f4f\u5b85|\u30cf\u30a4\u30d6\u30ea\u30c3\u30c9|\u4e80)\\b", 66),
            Pattern.compile("\\b(?:\u6392\u540d|\u65e0\u6392\u540d|\u6bb5\u4f4d|\u5929\u68af|\u6392\u4f4d|\u5929\u68af\u8d5b)\\b", 66),
            Pattern.compile("\\b(?:\u8857\u673a|\u7ecf\u5178|\u6218\u4e89|\u6218\u9738|\u751f\u5b58|\u8d77\u5e8a\u6218\u4e89|\u7a7a\u5c9b\u6218\u4e89|\u7a7a\u5c9b|\u9635\u8425|\u76d1\u72f1|\u8d77\u5e8a\u6218\u4e89|\u5355\u65b9\u5757|\u65e0\u653f\u5e9c|\u7ec3\u4e60|\u8c0b\u6740|\u8c1c\u9898|quake|tnt|\u7f8a\u6bdb\u6218\u4e89|\u6d3e\u5bf9\u6e38\u620f|\u6d1e\u68af|\u730e\u573a|\u5efa\u7b51\u6218|\u5c0f\u7403|\u732a\u732a|\u6fc0\u5149\u67aa|\u8db3\u7403|football|\u7bee\u7403)\\b", 66),
            Pattern.compile("\\b(?:\u0930\u0948\u0902\u0915|\u0930\u0948\u0902\u0915\u093f\u0902\u0917|\u0905\u0928\u0930\u0948\u0902\u0915|\u0926\u0930\u094d\u091c\u093e)\\b", 66),
            Pattern.compile("\\b(?:\u0906\u0930\u094d\u0915\u0947\u0921|\u0915\u094d\u0932\u093e\u0938\u093f\u0915|\u0935\u093e\u0930\\s*\u0932\u0949\u0930\u094d\u0921\u094d\u0938?|\u092a\u0941\u0932\u093f\u0938|\u0905\u092a\u0930\u093e\u0927\u0940|\u0938\u0930\u094d\u0935\u093e\u0907\u0935\u0932|bedwars|skywars|skyblock|\u0917\u0941\u091f|\u0915\u093e\u0930\u093e\u0917\u093e\u0930|eggwars|\u090f\u0915\\s*\u092c\u094d\u0932\u0949\u0915|\u0905\u0930\u093e\u091c\u0915\u0924\u093e|\u0905\u092d\u094d\u092f\u093e\u0938|\u0939\u0924\u094d\u092f\u093e|\u0939\u0924\u094d\u092f\u093e\u0930\u093e|\u0930\u0939\u0938\u094d\u092f|quake|tnt|\u090a\u0928\\s*\u092f\u0941\u0926\u094d\u0927|\u092a\u093e\u0930\u094d\u091f\u0940\\s*\u0916\u0947\u0932|pit|\u0906\u0935\u093e\u0938|\u0939\u093e\u0907\u092c\u094d\u0930\u093f\u0921|\u0915\u091b\u0941\u0906)\\b", 66),
            Pattern.compile("\\b(?:\u0645\u0631\u062a\u0628|\u062a\u0631\u062a\u064a\u0628|\u0644\u0627\u0639\u0628|\u0644\u0639\u0628\u0629|\u062a\u0635\u0646\u064a\u0641|\u0628\u062f\u0648\u0646\\s*\u062a\u0631\u062a\u064a\u0628)\\b", 66),
            Pattern.compile("\\b(?:\u0627\u0631\u0643\u064a\u062f|\u0643\u0644\u0627\u0633\u064a\u0643|\u062d\u0631\u0628|\u0628\u0648\u0644\u064a\u0633|\u062c\u0627\u0646\u0627|\u0628\u0642\u0627\u0621|bedwars|skywars|skyblock|\u0641\u0635\u064a\u0644|\u0633\u062c\u0646|eggwars|\u062c\u0632\u064a\u0631\u0629\\s*\u0648\u0627\u062d\u062f\u0629|\u0641\u0648\u0636\u0648\u064a\u0629|\u0627\u0644\u0642\u062a\u0644| \u0627\u0644\u0642\u0627\u062a\u0644|\u0627\u0644\u063a\u0645\u0648\u0636|quake|tnt|\u0635\u0648\u0641\\s*\u062d\u0631\u0628|\u062d\u0641\u0644\u0629\\s*\u0627\u0644\u0639\u0627\u0628|pit|\u0633\u0643\u0646|\u0647\u062c\u064a\u0646)\\b", 66),
            Pattern.compile("\\b(?:\u0e2d\u0e31\u0e19\u0e14\u0e31\u0e1a|\u0e44\u0e21\u0e48\u0e21\u0e35\u0e2d\u0e31\u0e19\u0e14\u0e31\u0e1a|\u0e41\u0e23\u0e07\u0e04\u0e4c|\u0e40\u0e14\u0e47\u0e14\u0e02\u0e32\u0e14)\\b", 66),
            Pattern.compile("\\b(?:\u0e2d\u0e32\u0e23\u0e4c\u0e40\u0e04\u0e14|\u0e04\u0e25\u0e32\u0e2a\u0e2a\u0e34\u0e01|\u0e2a\u0e07\u0e04\u0e23\u0e32\u0e21|\u0e15\u0e33\u0e23\u0e27\u0e08|\u0e2d\u0e32\u0e0a\u0e0d\u0e32\u0e01\u0e23|\u0e40\u0e2d\u0e32\u0e15\u0e34\u0e14\u0e15\u0e31\u0e27|bedwars|skywars|skyblock|\u0e1d\u0e48\u0e32\u0e22|\u0e04\u0e38\u0e01|eggwars|\u0e2b\u0e19\u0e36\u0e48\u0e07\\s*\u0e1a\u0e25\u0e47\u0e2d\u0e01|\u0e1b\u0e48\u0e32\u0e40\u0e16\u0e37\u0e48\u0e2d\u0e19|\u0e1d\u0e36\u0e01\u0e1d\u0e19|\u0e06\u0e32\u0e15\u0e01\u0e23|\u0e15\u0e31\u0e27\u0e25\u0e30\u0e04\u0e23|quake|tnt|\u0e02\u0e19\u0e41\u0e01\u0e30\\s*\u0e2a\u0e07\u0e04\u0e23\u0e32\u0e21|\u0e1b\u0e32\u0e23\u0e4c\u0e15\u0e35\u0e49\\s*\u0e40\u0e01\u0e21|pit|\u0e17\u0e35\u0e48\u0e2d\u0e22\u0e39\u0e48\u0e2d\u0e32\u0e28\u0e31\u0e22|\u0e44\u0e2e\u0e1a\u0e23\u0e34\u0e14|\u0e40\u0e15\u0e48\u0e32)\\b", 66),
            Pattern.compile("\\b(?:\u03ba\u03b1\u03c4\u03ac\u03c4\u03b1\u03be\u03b7|ranted|\u03c7\u03c9\u03c1\u03af\u03c2\\s*\u03ba\u03b1\u03c4\u03ac\u03c4\u03b1\u03be\u03b7|elo)\\b", 66),
            Pattern.compile("\\b(?:\u03b1\u03c1\u03ba\u03ac\u03bd\u03c4|\u03ba\u03bb\u03b1\u03c3\u03b9\u03ba\u03ac|war\\s*lords?|\u03b1\u03c3\u03c4\u03c5\u03bd\u03bf\u03bc\u03af\u03b1|\u03b5\u03b3\u03ba\u03bb\u03b7\u03bc\u03b1\u03c4\u03af\u03b5\u03c2|\u03b5\u03c0\u03b9\u03b2\u03af\u03c9\u03c3\u03b7|bedwars|skywars|skyblock|\u03c0\u03b1\u03c1\u03b1\u03c4\u03ac\u03be\u03b5\u03b9\u03c2|\u03c6\u03c5\u03bb\u03b1\u03ba\u03ae|eggwars|\u03ad\u03bd\u03b1\\s*\u03bc\u03c0\u03bb\u03bf\u03ba|\u03b1\u03bd\u03b1\u03c1\u03c7\u03af\u03b1|\u03c0\u03c1\u03b1\u03ba\u03c4\u03b9\u03ba\u03ae|\u03b4\u03bf\u03bb\u03bf\u03c6\u03bf\u03bd\u03af\u03b1|\u03b4\u03bf\u03bb\u03bf\u03c6\u03cc\u03bd\u03bf\u03c2|\u03bc\u03c5\u03c3\u03c4\u03ae\u03c1\u03b9\u03bf|quake|tnt|\u03bc\u03ac\u03bb\u03bb\u03b9\u03bd\u03bf\u03b9\\s*\u03c0\u03cc\u03bb\u03b5\u03bc\u03bf\u03b9|\u03c0\u03ac\u03c1\u03c4\u03b9\\s*\u03c0\u03b1\u03b9\u03c7\u03bd\u03af\u03b4\u03b9\u03b1|pit|\u03c3\u03c0\u03af\u03c4\u03b9|\u03c5\u03b2\u03c1\u03af\u03b4\u03b9\u03bf)\\b", 66),
            Pattern.compile("\\b(?:ranglista|ranked|rangl\u00f3|szint)\\b", 66),
            Pattern.compile("\\b(?:arcade|klasszikus|war\\s*lords?|rend\u0151r|b\u0171n\u00f6z\u0151|t\u00fal\u00e9l\u00e9s|bedwars|skywars|skyblock|t\u00f6bbs\u00e9g|bt\u00f6n|eggwars|egy\\s*blokk|anarchia|gyakorlat|gyilkoss\u00e1g|gyilkos|rejt\u00e9ly|quake|tnt|gyapj\u00fa\\s*h\u00e1bor\u00fa|p\u00e1rty\\s*j\u00e1t\u00e9kok|pit|sz\u00e1ll\u00e1s|hibrid)\\b", 66),
            Pattern.compile("\\b(?:ranked|neranked|po\u0159ad\u00ed|\u017eeb\u0159\u00ed\u010dek|klasifikace)\\b", 66),
            Pattern.compile("\\b(?:arcade|klasika|war\\s*lords?|policie|zlo\u010dinec|p\u0159e\u017eit\u00ed|bedwars|skywars|skyblock|strana|v\u011bzen\u00ed|eggwars|jeden\\s*blok|anarchie|praxe|vra\u017eda|vrah|tajemstv\u00ed|quake|tnt|vln\u011bn\u00e9\\s*v\u00e1lky|strana\\s*hry|pit|bydlen\u00ed|hybrid)\\b", 66),
            Pattern.compile("\\b(?:ranked|unranked|klassement|ranglijst)\\b", 66),
            Pattern.compile("\\b(?:arcade|klassiek|war\\s*lords?|politie|criminelen|overleving|bedwars|skywars|skyblock|fractie|gevangenis|eggwars|een\\s*blok|anarchie|oefening|moord|moordenaar|raadsel|quake|tnt|woon|hybrid)\\b", 66),
            Pattern.compile("\\b(?:clasat|neclasat|rang|ranked|clasament)\\b", 66),
            Pattern.compile("\\b(?:arcad\u0103|clasic|war\\s*lords?|poli\u021bie|criminal|supravie\u021buire|bedwars|skywars|skyblock|fac\u021biuni|\u00eenchisoare|eggwars|un\\s*bloc|anarhie|practic\u0103|uciga\u0219|omor| mister|quake|tnt|vat\u0103\\s*r\u0103zboi|petrecere\\s*jocuri|pit|locuin\u021b\u0103|hibrid)\\b", 66),
            Pattern.compile("\\b(?:classificato|non\\s*classificato|ranked|graduatoria|rango)\\b", 66),
            Pattern.compile("\\b(?:arcade|classico|war\\s*lords?|polizia|criminali|sopravvivenza|bedwars|skywars|skyblock|fazioni|prigione|eggwars|un\\s*blocco|anarchia|pratica|assassino|omicidio|mistero|quake|tnt|guerra\\s*lana|party\\s*games|pit|casa|ibrido)\\b", 66)
        };
        awa = Pattern.compile("\\b(?:survival|smp|minehut)\\b", 66);
        awb = Pattern.compile("\\b(?:eu\\.loyisa\\.cn|mc\\.loyisa\\.cn|tcpshield\\.anticheat-test\\.com|spectrum\\.anticheat-test\\.com|(?:[a-z0-9-]+\\.)*blocksmc\\.(?:com|net|club|org))\\b", 2);
        awc = Pattern.compile("^([a-z0-9]+)\\.[a-z0-9-]+\\.[a-z0-9-]+\\.[a-z0-9-]+\\.fisx\\.uk(?::\\d+)?$", 2);
        awd = Pattern.compile("^([A-Za-z0-9]+)(?:\\.[a-z0-9-]+)*\\.liquidproxy\\.net(?::\\d+)?$", 2);
        awe = Pattern.compile("\\b(?:[a-z0-9-]+\\.)*(?:loyisa\\.cn|anticheat-test\\.com)\\b", 2);
        awi = new SecurityPattern[]{
            new SecurityPattern("game_start_en", "(?:game|match|round)\\s+(?:is\\s+)?(?:starting|beginning)|(?:game|match|round)\\s+starts?\\s+in|starting\\s+in|begins?\\s+in|countdown|waiting\\s+for\\s+players"),
            new SecurityPattern("player_join_en", "player\\s+joined|joined\\s+the\\s+game|has\\s+joined|has\\s+entered|joined\\s+the\\s+lobby|has\\s+joined\\s*\\("),
            new SecurityPattern("duels_mode_en", "\\b(?:ranked|unranked)\\b|\\bbuild\\s*uhc\\b|\\b(?:duel|duels)\\b.*\\b(?:classic|nodebuff|no\\s*debuff|boxing|sumo|bridge|uhc|skywars|op|combo|blitz|megawalls|bow|parkour|tnt|gapple|mega|ult|ultimate)\\b|\\b(?:classic|nodebuff|no\\s*debuff|boxing|sumo|bridge|uhc|build\\s*uhc|skywars|op|combo|blitz|megawalls|bow|parkour|tnt|gapple|mega|ult|ultimate)\\b.*\\b(?:duel|duels)\\b|\\b(?:classic|nodebuff|boxing|sumo|bridge|uhc|build\\s*uhc|skywars|op|combo|blitz|megawalls|bow|parkour|tnt|gapple|mega|ult|ultimate)\\s+(?:duel|duels)\\b"),
            new SecurityPattern("minigame_mode_en", "\\b(?:skyblock|bedwars|skywars|factions|prison|lifesteal|practice|kitpvp|eggwars|oneblock|survival\\s+games|hunger\\s+games|hungergames|anarchy|towny|creative|arcade|warlords?|turbo\\s*kart\\s*racers?|tkr|blitzsg|blitz\\s*sg|cops\\s*(?:and|&)\\s*criminals?|cnc|battleground|battlegrounds?|vampirez|the\\s*walls|mega\\s*walls|walls|mcgo|murder\\s*(?:mystery)?|smash\\s*heroes?|paintball|quake|the\\s*tnt\\s*games?|tnt\\s*games?|rumble|pvp\\s*run|speed\\s*uhc|wool\\s*wars|party\\s*games|hole\\s*ladder|farm\\s*hunt|build\\s*battle|pixel\\s*paintball|boulevard|pit|housing|hybrid|turtles?|pig\\s*fight|laser\\s*tag|ender\\s*spleef|soccer|football|scrimmage|hive|cubecraft|lunar|mineville|oneninja|drop|instant\\s*respawn|mega|ult|ultimate)\\b"),
            new SecurityPattern("economy_smp_en", "\\b(?:coins?|tokens?|credits?|eco(?:nomy)?|claim|claims|land|lands|chunk|chunks|home|homes|sethome|warp|warps|shop|shops|auction|auction\\s+house|ah\\b|tpa|tpahere|teleport\\s+request|spawner|crate|survival|smp|anarchy|pit|pit\\s*xp?|housing)\\b"),
            new SecurityPattern("game_mode_es", "\\b(?:clasificado|clasificada|sin\\s*clasificar|ranqueado|ranked|arcade|juegos\\s*de\\s*arcade|warlords?|batalla|criminoso|polic\u00edas|survival|supervivencia|bedwars|skywars|skyblock|facciones|prisi\u00f3n|robo\\s*de\\s*vida|eggwars|isla\\s*unica|anarqu\u00eda|duelo|duelos|misterio|asesinato|lobby|vida\\s*robada|torneo)\\b"),
            new SecurityPattern("game_mode_fr", "\\b(?:class\u00e9|d\u00e9class\u00e9|non\\s*class\u00e9|ranked|arcade|survie|bedwars|skywars|skyblock|factions|prison|vol\\s*de\\s*vie|eggwars|une\\s*seule\\s*\u00eele|anarchie|duel|duels?|meurtrier|myst\u00e8re|bot\\s*fight|spawn|instant\\s*respawn|drop)\\b"),
            new SecurityPattern("game_mode_de", "\\b(?:gerankt|ranglisten|klassiert|rangliste|arcade|\u00fcberleben|bedwars|skywars|skyblock|fraktionen|gef\u00e4ngnis|anarchie|kampf|duell|hive|cubecraft|lunar)\\b"),
            new SecurityPattern("game_mode_pl", "\\b(?:ranked|unranked|klasyfikacja|arcade|surwiwal|bedwars|skywars|skyblock|frakcje|wiezienie|anarchia|gra|duel|hive)\\b"),
            new SecurityPattern("game_mode_ru", "\\b(?:\u0440\u0435\u0439\u0442\u0438\u043d\u0433|\u0431\u0435\u0437\\s*\u0440\u0435\u0439\u0442\u0438\u043d\u0433\u0430|\u0430\u0440\u043a\u0430\u0434\u0430|\u0432\u044b\u0436\u0438\u0432\u0430\u043d\u0438\u0435|bedwars|skywars|skyblock|\u0444\u0440\u0430\u043a\u0446\u0438\u0438|\u0442\u044e\u0440\u044c\u043c\u0430|\u0430\u043d\u0430\u0440\u0445\u0438\u044f|\u0434\u0443\u044d\u043b\u044c|\u0443\u0431\u0438\u0439\u0441\u0442\u0432\u043e)\\b"),
            new SecurityPattern("game_mode_tr", "\\b(?:s\u0131ral\u0131|s\u0131ras\u0131z|arcade|hayatta\\s*kalma|bedwars|skywars|skyblock|cephe|cezaevi|anarsi|duello|\u00f6ld\u00fcrme)\\b"),
            new SecurityPattern("game_mode_pt", "\\b(?:ranqueado|n\u00e3o\\s*ranqueado|arcade|sobrevivencia|bedwars|skywars|skyblock|fra\u00e7\u00f5es|pris\u00e3o|anarquia|duelo|assassinato)\\b"),
            new SecurityPattern("game_mode_it", "\\b(?:classificato|non\\s*classificato|arcade|sopravvivenza|bedwars|skywars|skyblock|fazioni|prigione|anarchia|duello|omicidio)\\b"),
            new SecurityPattern("game_mode_nl", "\\b(?:ranked|unranked|arcade|overleving|bedwars|skywars|skyblock|fractie|gevangenis|anarchie|gevecht|duel)\\b"),
            new SecurityPattern("game_mode_ro", "\\b(?:clasat|neclasat|arcad\u0103|supravie\u021buire|bedwars|skywars|skyblock|fac\u021biuni|\u00eenchisoare|anarchie|duel|uciga\u0219)\\b"),
            new SecurityPattern("game_mode_cs_sk", "\\b(?:ranked|neranked|arcade|p\u0159e\u017eit\u00ed|bedwars|skywars|skyblock|strany|v\u011bzen\u00ed|anarchie|souboj|duel|vra\u017eda)\\b"),
            new SecurityPattern("game_mode_hu", "\\b(?:ranglista|ranked|arcade|t\u00fal\u00e9l\u00e9s|bedwars|skywars|skyblock|t\u00f6bbs\u00e9g|b\u00f6rt\u00f6n|anarchia|harc|k\u00fczdelem|gyilkoss\u00e1g)\\b"),
            new SecurityPattern("game_mode_id_ms", "\\b(?:arcade|keramat|survival|bedwars|skywars|skyblock|faksi|tahanan|anarki|pertempuran|duel|pembantaian)\\b"),
            new SecurityPattern("game_mode_vi", "\\b(?:x\u1ebfp\\s*h\u1ea1ng|kh\u00f4ng\\s*x\u1ebfp\\s*h\u1ea1ng|arcade|sinh\\s*t\u1ed3n|bedwars|skywars|skyblock|chi\u1ebfn\\s*tranh|t\u00f9|anarchy|nhatt\\s*\u0111\u1ea5u|gi\u1ebft)\\b"),
            new SecurityPattern("game_mode_ru_translit", "\\b(?:reiting|bez\\s*reitinga|arkada|vyzhivanie|bedwars|skywars|skyblock|fraktsii|tyurma|anarkhiya|duel|ubiystvo)\\b"),
            new SecurityPattern("game_mode_zh", "\\b(?:\u6392\u540d|\u65e0\u6392\u540d|\u8857\u673a|\u751f\u5b58|\u8d77\u5e8a\u6218\u4e89|\u7a7a\u5c9b\u6218\u4e89|\u7a7a\u5c9b|\u9635\u8425|\u76d1\u72f1|\u65e0\u653f\u5e9c|\u51b3\u6597|\u8c0b\u6740)\\b"),
            new SecurityPattern("game_mode_ja", "\\b(?:\u30e9\u30f3\u30af|\u975e\u30e9\u30f3\u30af|\u30a2\u30fc\u30b1\u30fc\u30c9|\u751f\u5b58|\u30d9\u30c3\u30c9\u30a6\u30a9\u30fc\u30ba|\u30b9\u30ab\u30a4\u30a6\u30a9\u30fc\u30ba|\u30b9\u30ab\u30a4\u30d6\u30ed\u30c3\u30af|\u6d3e\u95a5|\u5211\u52d9\u6240|\u7121\u653f\u5e9c|\u6c7a\u95d8|\u6bba\u4eba)\\b"),
            new SecurityPattern("game_mode_ko", "\\b(?:\uc21c\uc704|\ube44\uc21c\uc704|\uc544\ucf00\uc774\ub4dc|\uc11c\ubc14\uc774\ubc8c|bedwars|skywars|skyblock|\ud30c\ubc8c|\uad50\ub3c4\uc18c|\ubb34\uc815\ubd80|\uacb0\ud22c|\uc0b4\uc778)\\b"),
            new SecurityPattern("game_mode_el", "\\b(?:\u03ba\u03b1\u03c4\u03ac\u03c4\u03b1\u03be\u03b7|\u03c7\u03c9\u03c1\u03af\u03c2\\s*\u03ba\u03b1\u03c4\u03ac\u03c4\u03b1\u03be\u03b7|\u03b1\u03c1\u03ba\u03ac\u03bd\u03c4|\u03b5\u03c0\u03b9\u03b2\u03af\u03c9\u03c3\u03b7|bedwars|skywars|skyblock|\u03c0\u03b1\u03c1\u03b1\u03c4\u03ac\u03be\u03b5\u03b9\u03c2|\u03c6\u03c5\u03bb\u03b1\u03ba\u03ae|\u03b1\u03bd\u03b1\u03c1\u03c7\u03af\u03b1|\u03bc\u03ac\u03c7\u03b7|\u03b4\u03bf\u03bb\u03bf\u03c6\u03bf\u03bd\u03af\u03b1)\\b"),
            new SecurityPattern("game_mode_ar", "\\b(?:\u062a\u0635\u0646\u064a\u0641|\u0628\u062f\u0648\u0646\\s*\u062a\u0631\u062a\u064a\u0628|\u0627\u0631\u0643\u064a\u062f|\u0628\u0642\u0627\u0621|\u0633\u0631\u064a\u0631|\u0633\u0643\u0627\u064a|\u0641\u0635\u064a\u0644|\u0633\u062c\u0646|\u0641\u0648\u0636\u0648\u064a\u0629|\u0642\u062a\u0627\u0644| \u0443\u0431\u0438\u0644)\\b"),
            new SecurityPattern("game_mode_hi", "\\b(?:\u0930\u0948\u0902\u0915|\u0905\u0928\u0930\u0948\u0902\u0915|\u0906\u0930\u094d\u0915\u0947\u0921|\u092c\u091a\u093e\u0935|bedwars|skywars|skyblock|\u0917\u0941\u091f|\u0915\u093e\u0930\u093e\u0917\u093e\u0930|\u0905\u0930\u093e\u091c\u0915\u0924\u093e|\u0932\u0921\u093c\u093e\u0908|\u0939\u0924\u094d\u092f\u093e)\\b"),
            new SecurityPattern("game_mode_th", "\\b(?:\u0e2d\u0e31\u0e19\u0e14\u0e31\u0e1a|\u0e44\u0e21\u0e48\u0e21\u0e35\u0e2d\u0e31\u0e19\u0e14\u0e31\u0e1a|\u0e2d\u0e32\u0e23\u0e4c\u0e40\u0e04\u0e14|\u0e40\u0e2d\u0e32\u0e15\u0e34\u0e14\u0e15\u0e31\u0e27|bedwars|skywars|skyblock|\u0e1d\u0e48\u0e32\u0e22|\u0e04\u0e38\u0e01|\u0e1b\u0e48\u0e32\u0e40\u0e16\u0e37\u0e48\u0e2d\u0e19|\u0e15\u0e48\u0e2d\u0e2a\u0e39\u0e49|\u0e06\u0e32\u0e15\u0e01\u0e23)\\b"),
            new SecurityPattern("game_mode_uk", "\\b(?:\u0440\u0435\u0439\u0442\u0438\u043d\u0433|\u0431\u0435\u0437\\s*\u0440\u0435\u0439\u0442\u0438\u043d\u0433\u0443|\u0430\u0440\u043a\u0430\u0434\u0430|\u0432\u0438\u0436\u0438\u0432\u0430\u043d\u043d\u044f|bedwars|skywars|skyblock|\u0444\u0440\u0430\u043a\u0446\u0456\u0457|\u0432'\u044f\u0437\u043d\u0438\u0446\u044f|\u0430\u043d\u0430\u0440\u0445\u0456\u044f|\u0434\u0443\u0435\u043b\u044c|\u0432\u0431\u0438\u0432\u0441\u0442\u0432\u043e)\\b"),
            new SecurityPattern("game_start_pl", "gra\\s+rozpocz(?:yna|nie)|gra\\s+rozpoczyna\\s*sie\\s+za|rozpocznie\\s*sie\\s+za|start\\s+za|odliczan"),
            new SecurityPattern("player_join_pl", "gracz\\s+do[l\u0142]acz(?:yl|y[\u0142l])|do[l\u0142]acz(?:yl|y[\u0142l])\\s+do\\s+gry|wchodzi\\s+do\\s+gry"),
            new SecurityPattern("game_start_de", "spiel\\s+startet|das\\s+spiel\\s+startet\\s+in|startet\\s+in|beginnt\\s+in|countdown"),
            new SecurityPattern("player_join_de", "spieler\\s+ist\\s+beigetreten|spieler\\s+hat\\s+das\\s+spiel\\s+betreten|ist\\s+beigetreten|hat\\s+das\\s+spiel\\s+betreten"),
            new SecurityPattern("economy_smp_de", "\\b(?:geld|kontostand|konto|bank|claim|claims?|grundstuck|grundst\u00fcck|home|homes|shop|shops|auktion|auktionshaus|survival|anarchie|smp)\\b"),
            new SecurityPattern("game_start_tr", "oyun\\s+basliyor|oyun\\s+baslamasina|baslangica|geri\\s+sayim"),
            new SecurityPattern("player_join_tr", "oyuncu\\s+katildi|oyuna\\s+katildi|katildi|giris\\s+yapti|oyuna\\s+girdi"),
            new SecurityPattern("game_start_es", "la\\s+partida\\s+(?:empieza|comienza)|empieza\\s+en|comienza\\s+en|cuenta\\s+regresiva|esperando\\s+jugadores|anadido\\s+a\\s+la\\s+cola|aniadido\\s+a\\s+la\\s+cola|agregado\\s+a\\s+la\\s+cola|buscando\\s+oponente|has\\s+encontrado\\s+a|ha?s\\s+ganado|oponente:|informacion:"),
            new SecurityPattern("player_join_es", "jugador\\s+se\\s+ha\\s+unido|se\\s+ha\\s+unido\\s+al\\s+juego|entro\\s+al\\s+juego|se\\s+unio|ha\\s+entrado\\s+al\\s+servidor|ha\\s+entrado\\s+al\\s+lobby|joined\\s+the\\s+lobby"),
            new SecurityPattern("game_state_es", "asesinatos\\s+finales|tiempo\\s+vivo|flechas:|golpes:|cps:|salido\\s+de\\s+la\\s+cola|deseas\\s+salirte\\s+de\\s+la\\s+arena|verificando\\s+tu\\s+cliente|introduce\\s+el\\s+codigo\\s+de\\s+la\\s+imagen|has\\s+pasado\\s+el\\s+test\\s+antibot|eres\\s+jugador\\s+con\\s+cuenta\\s+premium|verificando\\s+cuenta\\s+en\\s+minecraft\\.net|te\\s+has\\s+registrado\\s+correctamente|registrate"),
            new SecurityPattern("economy_smp_es", "\\b(?:saldo|dinero|banco|monedas?|economia|econom[i\u00ed]a|reclamo|reclamos|terreno|terrenos|hogar|hogares|casa|casas|tienda|tiendas|subasta|subastas|teletransporte|proteccion|protecci\u00f3n|survival|anarquia|anarqu[i\u00ed]a|smp)\\b"),
            new SecurityPattern("game_start_pt", "o\\s+jogo\\s+comeca|o\\s+jogo\\s+comeca|o\\s+jogo\\s+come\u00e7a|comeca\\s+em|comeca\\s+em|come\u00e7a\\s+em|iniciando\\s+em|contagem\\s+regressiva|aguardando\\s+jogadores"),
            new SecurityPattern("player_join_pt", "jogador\\s+entrou|entrou\\s+no\\s+jogo|se\\s+juntou|juntou-se"),
            new SecurityPattern("economy_smp_pt", "\\b(?:saldo|dinheiro|banco|moedas?|economia|terreno|terrenos|casa|casas|loja|lojas|leilao|leil\u00e3o|teleporte|survival|anarquia|smp)\\b"),
            new SecurityPattern("game_start_fr", "la\\s+partie\\s+commence|la\\s+partie\\s+commence\\s+dans|commence\\s+dans|debut\\s+dans|d\u00e9but\\s+dans|compte\\s+a\\s+rebours|compte\\s+\u00e0\\s+rebours|en\\s+attente\\s+de\\s+joueurs"),
            new SecurityPattern("player_join_fr", "joueur\\s+a\\s+rejoint|a\\s+rejoint\\s+la\\s+partie|a\\s+rejoint|est\\s+entre\\s+dans\\s+la\\s+partie|est\\s+entr\u00e9\\s+dans\\s+la\\s+partie"),
            new SecurityPattern("economy_smp_fr", "\\b(?:argent|solde|banque|claim|claims|terrain|terrains|maison|maisons|boutique|boutiques|enchere|ench\u00e8re|survie|anarchie|smp)\\b"),
            new SecurityPattern("game_start_it", "la\\s+partita\\s+inizia|la\\s+partita\\s+inizia\\s+tra|inizia\\s+tra|comincia\\s+tra|conto\\s+alla\\s+rovescia|in\\s+attesa\\s+di\\s+giocatori"),
            new SecurityPattern("player_join_it", "giocatore\\s+entrato|si\\s+e\\s+unito\\s+alla\\s+partita|si\\s+e\\s+unito|e\\s+entrato\\s+nel\\s+gioco"),
            new SecurityPattern("game_start_nl", "spel\\s+begint|spel\\s+begint\\s+over|begint\\s+over|aftellen|wachten\\s+op\\s+spelers"),
            new SecurityPattern("player_join_nl", "speler\\s+is\\s+toegetreden|is\\s+toegetreden|heeft\\s+de\\s+game\\s+betreden"),
            new SecurityPattern("game_start_cs_sk", "hra\\s+zacina|hra\\s+zacne\\s+za|zacina\\s+za|odpocet|odpoctavani|caka\\s+sa\\s+na\\s+hracov"),
            new SecurityPattern("player_join_cs_sk", "hrac\\s+se\\s+pripojil|hrac\\s+sa\\s+pripojil|pripojil\\s+se\\s+do\\s+hry|sa\\s+pripojil\\s+do\\s+hry"),
            new SecurityPattern("game_start_ro", "jocul\\s+incepe|jocul\\s+incepe\\s+in|incepe\\s+in|numaratoare\\s+inversa|asteptam\\s+jucatori"),
            new SecurityPattern("player_join_ro", "jucatorul\\s+s-a\\s+alaturat|s-a\\s+alaturat\\s+jocului|a\\s+intrat\\s+in\\s+joc"),
            new SecurityPattern("game_start_hu", "jatek\\s+kezdodik|jatek\\s+kezdodik\\s+ennyi\\s+ido\\s+mulva|kezdes\\s+ennyi\\s+ido\\s+mulva|visszaszamlalas|jatekosokra\\s+varunk"),
            new SecurityPattern("player_join_hu", "jatekos\\s+csatlakozott|csatlakozott\\s+a\\s+jatekhoz|belepett\\s+a\\s+jatekba"),
            new SecurityPattern("game_start_id_ms", "permainan\\s+dimulai|game\\s+dimulai|mulai\\s+dalam|hitung\\s+mundur|menunggu\\s+pemain"),
            new SecurityPattern("player_join_id_ms", "pemain\\s+bergabung|bergabung\\s+ke\\s+permainan|masuk\\s+ke\\s+permainan"),
            new SecurityPattern("game_start_vi", "tran\\s+dau\\s+bat\\s+dau|bat\\s+dau\\s+sau|dem\\s+nguoc|dang\\s+doi\\s+nguoi\\s+choi"),
            new SecurityPattern("player_join_vi", "nguoi\\s+choi\\s+da\\s+tham\\s+gia|da\\s+vao\\s+tran|tham\\s+gia\\s+tro\\s+choi"),
            new SecurityPattern("game_start_ru_translit", "igra\\s+nachinaetsya|igra\\s+nachnetsya|igra\\s+nachnetsa|nachalo\\s+cherez|start\\s+cherez|otschet|obratnyy\\s+otschet|ozhidanie\\s+igrokov"),
            new SecurityPattern("player_join_ru_translit", "igrok\\s+prisoedinilsya|igrok\\s+voshel|voshyol\\s+v\\s+igru|zashel\\s+v\\s+igru"),
            new SecurityPattern("game_start_ru", "\u0438\u0433\u0440\u0430\\s+\u043d\u0430\u0447\u0438\u043d\u0430\u0435\u0442\u0441\u044f|\u0438\u0433\u0440\u0430\\s+\u043d\u0430\u0447\u043d[\u0435\u0451]\u0442\u0441\u044f|\u043d\u0430\u0447\u0430\u043b\u043e\\s+\u0447\u0435\u0440\u0435\u0437|\u0441\u0442\u0430\u0440\u0442\\s+\u0447\u0435\u0440\u0435\u0437|\u043e\u0442\u0441\u0447[\u0435\u0451]\u0442|\u043e\u0431\u0440\u0430\u0442\u043d\u044b\u0439\\s+\u043e\u0442\u0441\u0447[\u0435\u0451]\u0442|\u043e\u0436\u0438\u0434\u0430\u043d\u0438\u0435\\s+\u0438\u0433\u0440\u043e\u043a\u043e\u0432"),
            new SecurityPattern("player_join_ru", "\u0438\u0433\u0440\u043e\u043a\\s+\u043f\u0440\u0438\u0441\u043e\u0435\u0434\u0438\u043d\u0438\u043b\u0441\u044f|\u0438\u0433\u0440\u043e\u043a\\s+\u0432\u043e\u0448[\u0435\u0451]\u043b|\u0432\u043e\u0448[\u0435\u0451]\u043b\\s+\u0432\\s+\u0438\u0433\u0440\u0443|\u0437\u0430\u0448[\u0435\u0451]\u043b\\s+\u0432\\s+\u0438\u0433\u0440\u0443"),
            new SecurityPattern("game_start_zh", "\u6e38\u620f\u5373\u5c06\u5f00\u59cb|\u6e38\u620f\u5f00\u59cb\u5012\u8ba1\u65f6|\u5c06\u5728\\s*\\d+\\s*\u79d2\u540e\u5f00\u59cb|\u5012\u8ba1\u65f6|\u7b49\u5f85\u73a9\u5bb6|\u6bd4\u8d5b\u5f00\u59cb"),
            new SecurityPattern("player_join_zh", "\u73a9\u5bb6\u5df2\u52a0\u5165|\u52a0\u5165\u4e86\u6e38\u620f|\u8fdb\u5165\u4e86\u6e38\u620f|\u52a0\u5165\u5927\u5385|\u8fdb\u5165\u5927\u5385"),
            new SecurityPattern("game_start_ja", "\u30b2\u30fc\u30e0\u958b\u59cb|\u30b2\u30fc\u30e0\u306f.*\u79d2\u5f8c\u306b\u958b\u59cb|\u958b\u59cb\u307e\u3067|\u30ab\u30a6\u30f3\u30c8\u30c0\u30a6\u30f3|\u30d7\u30ec\u30a4\u30e4\u30fc\u3092\u5f85\u3063\u3066\u3044\u307e\u3059"),
            new SecurityPattern("player_join_ja", "\u30d7\u30ec\u30a4\u30e4\u30fc\u304c\u53c2\u52a0|\u30b2\u30fc\u30e0\u306b\u53c2\u52a0\u3057\u307e\u3057\u305f|\u30ed\u30d3\u30fc\u306b\u53c2\u52a0\u3057\u307e\u3057\u305f"),
            new SecurityPattern("game_start_ko", "\uac8c\uc784\\s*\uc2dc\uc791|\uac8c\uc784\uc774\\s*.*\ucd08\\s*\ud6c4\uc5d0\\s*\uc2dc\uc791|\uce74\uc6b4\ud2b8\ub2e4\uc6b4|\ud50c\ub808\uc774\uc5b4\ub97c\\s*\uae30\ub2e4\ub9ac\ub294\\s*\uc911"),
            new SecurityPattern("player_join_ko", "\ud50c\ub808\uc774\uc5b4\uac00\\s*\ucc38\uac00\ud588\uc2b5\ub2c8\ub2e4|\uac8c\uc784\uc5d0\\s*\ucc38\uac00\ud588\uc2b5\ub2c8\ub2e4|\ub85c\ube44\uc5d0\\s*\ucc38\uac00\ud588\uc2b5\ub2c8\ub2e4"),
            new SecurityPattern("game_start_uk", "\u0433\u0440\u0430\\s+\u043f\u043e\u0447\u0438\u043d\u0430\u0454\u0442\u044c\u0441\u044f|\u0433\u0440\u0430\\s+\u043f\u043e\u0447\u043d\u0435\u0442\u044c\u0441\u044f\\s+\u0447\u0435\u0440\u0435\u0437|\u043f\u043e\u0447\u0430\u0442\u043e\u043a\\s+\u0447\u0435\u0440\u0435\u0437|\u0432\u0456\u0434\u043b\u0456\u043a|\u043e\u0447\u0456\u043a\u0443\u0432\u0430\u043d\u043d\u044f\\s+\u0433\u0440\u0430\u0432\u0446\u0456\u0432"),
            new SecurityPattern("player_join_uk", "\u0433\u0440\u0430\u0432\u0435\u0446\u044c\\s+\u043f\u0440\u0438\u0454\u0434\u043d\u0430\u0432\u0441\u044f|\u043f\u0440\u0438\u0454\u0434\u043d\u0430\u0432\u0441\u044f\\s+\u0434\u043e\\s+\u0433\u0440\u0438|\u0443\u0432\u0456\u0439\u0448\u043e\u0432\\s+\u0443\\s+\u0433\u0440\u0443"),
            new SecurityPattern("game_start_el", "\u03c4\u03bf\\s+\u03c0\u03b1\u03b9\u03c7\u03bd\u03b9\u03b4\u03b9\\s+\u03be\u03b5\u03ba\u03b9\u03bd\u03b1|\u03c4\u03bf\\s+\u03c0\u03b1\u03b9\u03c7\u03bd\u03b9\u03b4\u03b9\\s+\u03b1\u03c1\u03c7\u03b9\u03b6\u03b5\u03b9|\u03be\u03b5\u03ba\u03b9\u03bd\u03b1\\s+\u03c3\u03b5|\u03b1\u03bd\u03c4\u03b9\u03c3\u03c4\u03c1\u03bf\u03c6\u03b7\\s+\u03bc\u03b5\u03c4\u03c1\u03b7\u03c3\u03b7|\u03b1\u03bd\u03b1\u03bc\u03bf\u03bd\u03b7\\s+\u03c0\u03b1\u03b9\u03ba\u03c4\u03c9\u03bd"),
            new SecurityPattern("player_join_el", "\u03bf\\s+\u03c0\u03b1\u03b9\u03ba\u03c4\u03b7\u03c2\\s+\u03bc\u03c0\u03b7\u03ba\u03b5|\u03bf\\s+\u03c0\u03b1\u03b9\u03ba\u03c4\u03b7\u03c2\\s+\u03c3\u03c5\u03bd\u03b4\u03b5\u03b8\u03b7\u03ba\u03b5|\u03bc\u03c0\u03b7\u03ba\u03b5\\s+\u03c3\u03c4\u03bf\\s+\u03c0\u03b1\u03b9\u03c7\u03bd\u03b9\u03b4\u03b9"),
            new SecurityPattern("game_start_ar", "\u062a\u0628\u062f\u0623\\s+\u0627\u0644\u0644\u0639\u0628\u0629|\u0627\u0644\u0644\u0639\u0628\u0629\\s+\u062a\u0628\u062f\u0623\\s+\u062e\u0644\u0627\u0644|\u062a\u0628\u062f\u0623\\s+\u062e\u0644\u0627\u0644|\u0627\u0644\u0639\u062f\\s+\u0627\u0644\u062a\u0646\u0627\u0632\u0644\u064a|\u0628\u0627\u0646\u062a\u0638\u0627\u0631\\s+\u0627\u0644\u0644\u0627\u0639\u0628\u064a\u0646"),
            new SecurityPattern("player_join_ar", "\u0627\u0646\u0636\u0645\\s+\u0627\u0644\u0644\u0627\u0639\u0628|\u062f\u062e\u0644\\s+\u0627\u0644\u0644\u0627\u0639\u0628|\u0627\u0646\u0636\u0645\\s+\u0627\u0644\u0649\\s+\u0627\u0644\u0644\u0639\u0628\u0629|\u062f\u062e\u0644\\s+\u0627\u0644\u0649\\s+\u0627\u0644\u0644\u0639\u0628\u0629"),
            new SecurityPattern("game_start_hi", "\u0917\u0947\u092e\\s+\u0936\u0941\u0930\u0942|\u0916\u0947\u0932\\s+\u0936\u0941\u0930\u0942|\u0936\u0941\u0930\u0942\\s+\u0939\u094b\u0928\u0947\\s+\u092e\u0947\u0902|\u0909\u0932\u091f\u0940\\s+\u0917\u093f\u0928\u0924\u0940|\u0916\u093f\u0932\u093e\u0921\u093c\u093f\u092f\u094b\u0902\\s+\u0915\u0940\\s+\u092a\u094d\u0930\u0924\u0940\u0915\u094d\u0937\u093e"),
            new SecurityPattern("player_join_hi", "\u0916\u093f\u0932\u093e\u0921\u093c\u0940\\s+\u091c\u0941\u0921\u093c|\u0916\u0947\u0932\\s+\u092e\u0947\u0902\\s+\u0936\u093e\u092e\u093f\u0932|\u0917\u0947\u092e\\s+\u092e\u0947\u0902\\s+\u0936\u093e\u092e\u093f\u0932"),
            new SecurityPattern("game_start_th", "\u0e40\u0e01\u0e21\u0e40\u0e23\u0e34\u0e48\u0e21|\u0e08\u0e30\u0e40\u0e23\u0e34\u0e48\u0e21\u0e43\u0e19|\u0e19\u0e31\u0e1a\u0e16\u0e2d\u0e22\u0e2b\u0e25\u0e31\u0e07|\u0e01\u0e33\u0e25\u0e31\u0e07\u0e23\u0e2d\u0e1c\u0e39\u0e49\u0e40\u0e25\u0e48\u0e19"),
            new SecurityPattern("player_join_th", "\u0e1c\u0e39\u0e49\u0e40\u0e25\u0e48\u0e19\u0e40\u0e02\u0e49\u0e32\u0e23\u0e48\u0e27\u0e21|\u0e40\u0e02\u0e49\u0e32\u0e23\u0e48\u0e27\u0e21\u0e40\u0e01\u0e21|\u0e40\u0e02\u0e49\u0e32\u0e25\u0e47\u0e2d\u0e1a\u0e1a\u0e35\u0e49")
        };
        awj = Pattern.compile("(?:\\b5\\b\\D*\\b4\\b\\D*\\b3\\b)|(?:\\b4\\b\\D*\\b3\\b\\D*\\b2\\b\\D*\\b1\\b)|(?:\\b3\\b\\D*\\b2\\b\\D*\\b1\\b)", 66);
        awk = Pattern.compile("^(?:\\[[^\\]]+\\]\\s*){0,5}[\\p{L}0-9_]{1,24}\\s*:\\s+.+$", 66);
        awl = Pattern.compile("^<[^>]{1,24}>\\s+.+$", 66);
        awm = Pattern.compile("^(?:opponent|oponente|adversaire|adversario|avversario|gegner|informacion|information|info|cuenta|account|uc|pit|fiesta|party|sistema|system|server)\\s*[:\u00bb].*$", 66);
    }

    @Override
    public boolean run() {
        return false;
    }

    public boolean az(String var1) {
        String s = StringUtils.trimToEmpty(this.aC(var1)).toLowerCase(Locale.ROOT);
        if (s.isEmpty()) {
            return false;
        }
        //add code
        return awb.matcher(s).find() ? true : ProtectionToggles.proxyLookup() && this.aA(s);
    }
}
