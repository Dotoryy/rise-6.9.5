package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.CancellableEvent;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.packet.TimedPacket;
import com.alan.clients.component.impl.player.BlinkTicket;
import com.alan.clients.component.impl.player.BlinkChannel;
import com.alan.clients.component.impl.player.BlinkPacket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.client.C03PacketPlayer.C05PacketPlayerLook;
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.client.l;
import net.minecraft.network.play.client.m;
import net.minecraft.network.play.client.q;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity.S15PacketEntityRelMove;
import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.aa;
import net.minecraft.network.play.server.ad;
import net.minecraft.network.play.server.k;
import net.minecraft.network.play.server.z;
import net.minecraft.util.Tuple;
import rip.vantage.commons.util.time.StopWatch;

public final class BlinkComponent extends Component {
    public static ConcurrentLinkedQueue<TimedPacket> packets = new ConcurrentLinkedQueue<>();
    static StopWatch dP = new StopWatch();
    public static boolean enabled;
    static long dQ;
    private static final Object dR = new Object();
    private static final Map<String, BlinkChannel> dS = new LinkedHashMap<>();
    private static final long dT = 100L;
    public static final AtomicLong dU = new AtomicLong();
    public static Tuple<Class[], Boolean> dV = new Tuple<>(
        new Class[]{C0FPacketConfirmTransaction.class, net.minecraft.network.play.client.a.class, ad.class}, false
    );
    public static Tuple<Class[], Boolean> dW = new Tuple<>(new Class[]{S12PacketEntityVelocity.class, S27PacketExplosion.class}, false);
    public static Tuple<Class[], Boolean> dX = new Tuple<>(new Class[]{S08PacketPlayerPosLook.class, S39PacketPlayerAbilities.class, k.class}, false);
    public static Tuple<Class[], Boolean> dY = new Tuple<>(
        new Class[]{
            S13PacketDestroyEntities.class,
            S14PacketEntity.class,
            S16PacketEntityLook.class,
            S15PacketEntityRelMove.class,
            S17PacketEntityLookMove.class,
            z.class,
            S20PacketEntityProperties.class,
            aa.class
        },
        false
    );
    public static Tuple<Class[], Boolean> dZ = new Tuple<>(
        new Class[]{
            C02PacketUseEntity.class,
            q.class,
            C0EPacketClickWindow.class,
            C0CPacketInput.class,
            C0BPacketEntityAction.class,
            C08PacketPlayerBlockPlacement.class,
            C07PacketPlayerDigging.class,
            l.class,
            C13PacketPlayerAbilities.class,
            C15PacketClientSettings.class,
            C16PacketClientStatus.class,
            C17PacketCustomPayload.class,
            C18PacketSpectate.class,
            C19PacketResourcePackStatus.class,
            C03PacketPlayer.class,
            C04PacketPlayerPosition.class,
            C05PacketPlayerLook.class,
            C06PacketPlayerPosLook.class,
            m.class
        },
        false
    );
    public static Tuple<Class[], Boolean> ea = new Tuple<>(
        new Class[]{C03PacketPlayer.class, C04PacketPlayerPosition.class, C05PacketPlayerLook.class, C06PacketPlayerPosLook.class, m.class}, false
    );
    public static Tuple<Class[], Boolean> eb = new Tuple<>(
        new Class[]{
            C02PacketUseEntity.class,
            q.class,
            C0EPacketClickWindow.class,
            C0BPacketEntityAction.class,
            C08PacketPlayerBlockPlacement.class,
            C07PacketPlayerDigging.class,
            C13PacketPlayerAbilities.class,
            C15PacketClientSettings.class,
            C17PacketCustomPayload.class,
            C18PacketSpectate.class,
            C19PacketResourcePackStatus.class,
            C03PacketPlayer.class,
            C04PacketPlayerPosition.class,
            C05PacketPlayerLook.class,
            C06PacketPlayerPosLook.class,
            m.class
        },
        false
    );
    public static Tuple<Class[], Boolean>[] ec = new Tuple[]{dV, dW, dX, dY, dZ, ea, eb};
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1 -> var1.setCancelled(this.a(var1.dq(), var1).isCancelled());
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> var1.setCancelled(this.a(var1.getPacket(), var1).isCancelled());
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var0 -> bl();
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var0 -> {
        if (aEg.currentScreen instanceof GuiDownloadTerrain) {
            bl();
        } else {
            long i = System.currentTimeMillis();
            synchronized (dR) {
                b(i);
            }

            bk();
            a(i);
            synchronized (dR) {
                dS.values().forEach(BlinkChannel::br);
                bm();
            }
        }
    };

    public BlinkComponent() {
    }

    public CancellableEvent a(Packet<?> packet, CancellableEvent cancellableEvent) {
        if (!cancellableEvent.isCancelled() && enabled) {
            boolean flag;
            synchronized (dR) {
                Set<BlinkChannel> set = dS.values()
                    .stream()
                    .filter(BlinkChannel::bd)
                    .filter(var1x -> var1x.d(packet))
                    .collect(Collectors.toCollection(HashSet::new));
                Set<BlinkTicket> set1 = set.stream().map(var0 -> var0.ek).collect(Collectors.toCollection(HashSet::new));
                flag = !set1.isEmpty();
                if (flag) {
                    set.forEach(BlinkChannel::bq);
                    packets.add(new BlinkPacket(packet, set1));
                }
            }

            if (flag) {
                cancellableEvent.setCancelled();
            }

            return cancellableEvent;
        }
        return cancellableEvent;
    }

    public static void dispatch() {
        e(bn());
    }

    public static void bf() {
        f(bn());
    }

    public static int bg() {
        return g(bn());
    }

    public static boolean bh() {
        return bg() > 0;
    }

    public static void d(int var0) {
        a(bn(), var0);
    }

    public static void bi() {
        e(1);
    }

    public static void e(int var0) {
        b(bn(), var0);
    }

    public static void f(int var0) {
        e(var0);
    }

    public static void bj() {
        bi();
    }

    private static void e(String var0) {
        BlinkTicket bn;
        synchronized (dR) {
            BlinkChannel bp = dS.get(var0);
            bn = bp == null ? null : bp.bp();
        }

        if (bn != null) {
            a(bn, Integer.MAX_VALUE);
        }
    }

    public static void disable() {
        BlinkTicket bn;
        synchronized (dR) {
            BlinkChannel bp = dS.get(bn());
            bn = bp == null ? null : bp.bp();
            bm();
        }

        if (bn != null) {
            a(bn, Integer.MAX_VALUE);
        }

        synchronized (dR) {
            bm();
        }

        dP.setMillis(dP.millis - 999999999L);
    }

    public static void a(int var0, boolean var1, boolean var2, boolean var3, boolean var4) {
        a(var0, var1, var2, var3, var4, false, false, false);
    }

    public static void a(int var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5) {
        a(var0, var1, var2, var3, var4, var5, false, false);
    }

    public static void a(int var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6) {
        a(var0, var1, var2, var3, var4, var5, var6, false);
    }

    public static void a(int var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
        dP.aX();
        synchronized (dR) {
            dS.computeIfAbsent(bn(), BlinkChannel::new).a(var0, var1, var2, var3, var4, var5, var6, var7);
            bm();
        }

        dV.k(var1);
        dW.k(var2);
        dX.k(var3);
        dY.k(var4);
        dZ.k(var5);
        ea.k(var6);
        eb.k(var7);
        dQ = var0;
    }

    public static void blink() {
        a(9999999, true, false, false, false, true);
    }

    private static void f(String var0) {
        BlinkTicket bn;
        synchronized (dR) {
            BlinkChannel bp = dS.get(var0);
            bn = bp == null ? null : bp.bp();
            if (bp != null) {
                bp.bo();
            }

            bm();
        }

        if (bn != null) {
            a(bn, Integer.MAX_VALUE);
        }
    }

    private static int g(String var0) {
        synchronized (dR) {
            BlinkChannel bp = dS.get(var0);
            if (bp != null && bp.ek != null) {
                int i = 0;

                for (TimedPacket timedPacket : packets) {
                    if (timedPacket instanceof BlinkPacket && ((BlinkPacket)timedPacket).b(bp.ek)) {
                        i++;
                    }
                }

                return i;
            }
            return 0;
        }
    }

    private static void a(String var0, int var1) {
        BlinkTicket bn;
        synchronized (dR) {
            BlinkChannel bp = dS.get(var0);
            bn = bp == null ? null : bp.ek;
        }

        if (bn != null) {
            b(bn, var1);
        }
    }

    private static void b(String var0, int var1) {
        if (var1 > 0) {
            BlinkTicket bn;
            int i;
            synchronized (dR) {
                BlinkChannel bp = dS.get(var0);
                if (bp == null || bp.ek == null) {
                    return;
                }

                bp.br();
                bn = bp.ek;
                i = bp.g(var1);
            }

            if (i > 0) {
                b(bn, i);
            }
        }
    }

    private static void a(BlinkTicket var0, int var1) {
        ArrayList arraylist = new ArrayList();
        synchronized (dR) {
            var0.dj = false;
            int i = 0;
            Iterator iterator = packets.iterator();

            while (iterator.hasNext() && i < var1) {
                TimedPacket timedPacket = (TimedPacket)iterator.next();
                if (timedPacket instanceof BlinkPacket br) {
                    if (br.bt()) {
                        break;
                    }

                    i++;
                    iterator.remove();
                    arraylist.add(br.getPacket());
                }
            }

            bm();
        }

        a(arraylist);
    }

    private static void b(BlinkTicket var0, int var1) {
        ArrayList arraylist = new ArrayList();
        synchronized (dR) {
            int i = 0;
            Iterator iterator = packets.iterator();

            while (iterator.hasNext() && i < var1) {
                TimedPacket timedPacket = (TimedPacket)iterator.next();
                if (timedPacket instanceof BlinkPacket br) {
                    if (!br.b(var0)) {
                        break;
                    }

                    br.a(var0);
                    i++;
                    if (br.bt()) {
                        break;
                    }

                    iterator.remove();
                    arraylist.add(br.getPacket());
                }
            }

            bm();
        }

        a(arraylist);
    }

    private static void bk() {
        ArrayList arraylist = new ArrayList();
        synchronized (dR) {
            a(arraylist, Integer.MAX_VALUE);
            bm();
        }

        a(arraylist);
    }

    private static void bl() {
        ArrayList arraylist = new ArrayList();
        synchronized (dR) {
            for (TimedPacket timedPacket : packets) {
                arraylist.add(timedPacket.getPacket());
            }

            packets.clear();
            dS.values().forEach(BlinkChannel::bp);
            bm();
        }

        a(arraylist);
    }

    private static void a(long var0) {
        ArrayList arraylist = new ArrayList();
        synchronized (dR) {
            Iterator iterator = packets.iterator();

            while (iterator.hasNext()) {
                TimedPacket timedPacket = (TimedPacket)iterator.next();
                if (!(timedPacket instanceof BlinkPacket br)) {
                    if (timedPacket.getTime() + dQ >= var0) {
                        break;
                    }

                    iterator.remove();
                    arraylist.add(timedPacket.getPacket());
                } else {
                    br.c(var0);
                    if (br.bt()) {
                        break;
                    }

                    iterator.remove();
                    arraylist.add(br.getPacket());
                }
            }
        }

        a(arraylist);
    }

    private static void a(List<Packet<?>> var0, int var1) {
        int i = 0;

        for (Iterator iterator = packets.iterator(); iterator.hasNext() && i < var1; i++) {
            TimedPacket timedPacket = (TimedPacket)iterator.next();
            if (timedPacket instanceof BlinkPacket && ((BlinkPacket)timedPacket).bt()) {
                break;
            }

            iterator.remove();
            var0.add(timedPacket.getPacket());
        }
    }

    private static void a(List<Packet<?>> var0) {
        if (!var0.isEmpty()) {
            boolean flag = enabled;
            enabled = false;
            var0.forEach(PacketUtil::queue);
            enabled = flag;
        }
    }

    private static void b(long var0) {
        if (aEg.currentScreen instanceof GuiDownloadTerrain) {
            dS.values().forEach(BlinkChannel::bp);
            bm();
        } else {
            dS.values().stream().filter(var2 -> var2.bd() && var0 - var2.el > 100L).forEach(BlinkChannel::bp);
            bm();
        }
    }

    private static void bm() {
        enabled = dS.values().stream().anyMatch(BlinkChannel::bd);
    }

    private static String bn() {
        String s = BlinkComponent.class.getName();
        StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();
        int i = astacktraceelement.length;

        for (int j = 0; j < i; j++) {
            String className = astacktraceelement[j].getClassName();
            if (!className.equals(s) && !className.equals(Thread.class.getName())) {
                return className;
            }
        }

        return s;
    }
}
