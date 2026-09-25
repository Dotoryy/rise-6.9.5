package rip.vantage.network.core;

import com.alan.clients.Client;
import com.alan.clients.auth.AuthHook;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.GameEvent;
import java.security.SecureRandom;
import lombok.Generated;
import rip.vantage.network.handler.BackendPacketHandler;
import rip.vantage.network.handler.BackendWebSocket;

public class VantageNetwork {
    @EventLink
    public Listener<GameEvent> onGame;
    public static String eRr;
    public String aCj;
    public BackendPacketHandler eRs;
    public volatile BackendWebSocket dcF;
    public static volatile long eRw;
    public static volatile VantageNetwork eRq;
    public static SecureRandom eRu;
    public rip.vantage.commons.util.time.StopWatch eRt = new rip.vantage.commons.util.time.StopWatch();
    public static volatile boolean eRv;
    public static volatile boolean eRx;

    public void init() {
    }

    public BackendPacketHandler aKJ() {
        return null;
    }

    public void aKE() {
    }

    //add code
    private static boolean creating;

    public static VantageNetwork aKB() {
        VantageNetwork instance = eRq;
        if (instance != null) {
            return instance;
        }
        synchronized (VantageNetwork.class) {
            if (eRq == null && !creating) {
                creating = true;
                try {
                    eRq = new VantageNetwork();
                } finally {
                    creating = false;
                }
            }
            return eRq;
        }
    }

    public static void aKC() {
    }

    public void aKG() {
    }

    @Generated
    public void f(rip.vantage.commons.util.time.StopWatch var1) {
    }

    public void n(byte[] var1) {
    }

    public void aKF() {
    }

    public void aKM() {
    }

    public void aKN() {
    }

    public void aKI() {
    }

    //add code
    @Generated
    public String bX() {
        return AuthHook.provider().username();
    }

    @Generated
    public rip.vantage.commons.util.time.StopWatch aKL() {
        return null;
    }

    public void aKH() {
    }

    public void a(GameEvent event) {
    }

    public VantageNetwork() {
        this.onGame = this::a;
        Client.a.e().b(this);
    }

    public void aKD() {
    }

    //add code
    public BackendWebSocket aKK() {
        BackendWebSocket transport = this.dcF;
        if (transport == null) {
            synchronized (this) {
                transport = this.dcF;
                if (transport == null) {
                    transport = this.dcF = new BackendWebSocket();
                }
            }
        }
        return transport;
    }

    //add code
    @Generated
    public void kj(String var1) {
        if (!AuthHook.provider().login(var1)) {
            return;
        }
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getMinecraft();
        if (mc != null) {
            mc.displayGuiScreen(new com.alan.clients.ui.menu.impl.main.MainMenu());
        }
        Client.a.getConfigManager().tn();
    }
}
