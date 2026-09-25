package com.alan.clients.security;

import com.alan.clients.Client;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.ChatInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.interfaces.ExecutorAccess;
import com.alan.clients.security.ChatMessageObserver;
import com.alan.clients.security.impl.DebugOrPacketCommandCheck;
import com.alan.clients.packetlog.impl.HostsFileCheck;
import com.alan.clients.security.impl.HypixelBrandAddressMismatchCheck;
import com.alan.clients.security.impl.HypixelIpNoScoreboardCheck;
import com.alan.clients.security.impl.StackProbeCheck;
import com.alan.clients.security.impl.AcCommandCheck;
import com.alan.clients.security.impl.NativeWorldScanCheck;
import com.alan.clients.security.impl.SusChatCheck;
import com.alan.clients.security.impl.LowActivityWorldCheck;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.network.play.client.C01PacketChatMessage;
import org.apache.commons.lang3.StringUtils;

public class SecurityFeatureManager implements InstanceAccess, ExecutorAccess {
    public rip.vantage.commons.util.time.StopWatch avt;
    @EventLink
    public Listener<ChatInputEvent> onChatInput;
    public static long avp;
    public String avu;
    public List<SecurityFeature> avr;
    public long avv;
    public List<SecurityFeature> avq = new ArrayList<>();
    public static boolean avo;
    @EventLink
    public Listener<PreMotionEvent> onPreMotion;
    public boolean avw;
    @EventLink
    public Listener<PacketSendEvent> onPacketSend;
    public Set<String> avs;

    public void a(SecurityFeature securityFeature) {
        this.avq.add(securityFeature);
        Client.a.e().b(securityFeature);
    }

    @Generated
    public List<SecurityFeature> nI() {
        return this.avq;
    }

    @Generated
    public List<SecurityFeature> nJ() {
        return this.avr;
    }

    public void b(SecurityFeature securityFeature) {
        this.avr.add(securityFeature);
        Client.a.e().b(securityFeature);
    }

    @Generated
    public long nM() {
        return this.avv;
    }

    static {
    }

    @Generated
    public String nL() {
        return this.avu;
    }

    public void at(String var1) {
        this.c(var1, true);
    }

    public SecurityFeatureManager() {
        this.avr = new ArrayList<>();
        this.avs = ConcurrentHashMap.newKeySet();
        this.avt = new rip.vantage.commons.util.time.StopWatch();
        this.avu = "";
        this.onPreMotion = var1 -> {
            if (!this.nH()) {
                this.avw = false;
            } else {
                if (this.avt.T(10000L)) {
                    aMR.execute(() -> {
                        Object object = null;
                        int k_hi = 0;
                        Iterator iterator = this.avq.iterator();

                        while (iterator.hasNext()) {
                            SecurityFeature securityfeature = (SecurityFeature)iterator.next();
                            if (securityfeature.run()) {
                                k_hi = 1;
                                this.c(securityfeature.getReason(), false);
                                break;
                            }
                        }

                        Iterator iterator1 = this.avr.iterator();

                        while (iterator1.hasNext()) {
                            SecurityFeature securityfeature1 = (SecurityFeature)iterator1.next();
                            if (securityfeature1.run()) {
                                this.c(securityfeature1.getReason(), true);
                            }
                        }

                        this.avw = (k_hi) != 0;
                    });
                    this.avt.aX();
                }
            }
        };
        this.onChatInput = var1 -> {
            if (this.nH()) {
                this.au(var1.getMessage());
            }
        };
        this.onPacketSend = var1 -> {
            Object object = null;
            if (this.nH()) {
                if (var1.dq() instanceof C01PacketChatMessage) {
                    C01PacketChatMessage c01packetchatmessage = (C01PacketChatMessage)var1.dq();
                    this.au(c01packetchatmessage.getMessage());
                }
            }
        };
    }

    @Generated
    public Listener<ChatInputEvent> getOnChatInput() {
        return this.onChatInput;
    }

    @Generated
    public Set<String> nK() {
        return this.avs;
    }

    public void c(String var1, boolean var2) {
        if (this.nH()) {
            if (!StringUtils.isBlank(var1) && this.avs.add(var1)) {
                this.as(var1);
            }
        }
    }

    @Generated
    public boolean nN() {
        return this.avw;
    }

    public void au(String var1) {
        String s = StringUtils.trimToEmpty(var1);
        if (!s.isEmpty()) {
            long now = System.currentTimeMillis();
            if (!s.equals(this.avu) || now - this.avv > 750L) {
                this.avu = s;
                this.avv = now;
                Iterator iterator = this.avr.iterator();

                while (iterator.hasNext()) {
                    SecurityFeature securityfeature = (SecurityFeature)iterator.next();
                    if (securityfeature instanceof ChatMessageObserver) {
                        ((ChatMessageObserver)securityfeature).ar(s);
                    }
                }
            }
        }
    }


    public void init() {
        Client.a.e().b(this);
        if (!this.nH()) {
            this.avw = false;
        } else {
            this.a(new HostsFileCheck());
            this.a(new NativeWorldScanCheck());
            this.b(new HypixelIpNoScoreboardCheck());
            this.b(new HypixelBrandAddressMismatchCheck());
            this.b(new AcCommandCheck());
            this.b(new DebugOrPacketCommandCheck());
            this.b(new SusChatCheck());
            this.b(new LowActivityWorldCheck());
            this.b(new StackProbeCheck());
        }
    }

    @Generated
    public Listener<PreMotionEvent> getOnPreMotion() {
        return this.onPreMotion;
    }

    @Generated
    public Listener<PacketSendEvent> getOnPacketSend() {
        return this.onPacketSend;
    }

    public void as(String var1) {
        if (this.nH()) {
            String s = rip.vantage.util.NativeBridge.kU(var1);
            if (s != null) {
                rip.vantage.util.NativeBridge.aN(s, var1);
            }
        }
    }

    public boolean nH() {
        return true;
    }

    @Generated
    public rip.vantage.commons.util.time.StopWatch mQ() {
        return this.avt;
    }
}
