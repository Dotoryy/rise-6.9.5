package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.player.ServerPingCallable;
import com.alan.clients.component.impl.render.NotificationComponent;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import rip.vantage.commons.util.time.StopWatch;

public final class PingSpoofComponent extends Component {
    private static long dH = 250L;
    private static final Executor dI = Executors.newFixedThreadPool(1);
    private static final StopWatch dJ = new StopWatch();
    private static final StopWatch dK = new StopWatch();
    private static final long dL = 120000L;
    private static final long dM = 10000L;
    private static final long dN = 250L;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (dJ.T(10000L) && !dK.T(120000L)) {
            a(false);
            dJ.aX();
        }
    };

    public PingSpoofComponent() {
    }

    public static long getPing() {
        if (dK.T(120000L)) {
            a(true);
            dK.aX();
            return 250L;
        }
        dK.aX();
        return dH;
    }

    private static void a(boolean var0) {
        if (aEg.isIntegratedServerRunning()) {
            dH = 0L;
        } else {
            dI.execute(() -> {
                dJ.aX();
                if (var0) {
                    NotificationComponent.a("Ping", "Please wait whilst Rise analyses your ping.", 7000);
                }

                dH = new ServerPingCallable(LastConnectionComponent.ip).vf();
                if (var0) {
                    NotificationComponent.a("Success", "Successfully analysed ping, your features are ready to use.", 7000);
                }
            });
        }
    }
}
