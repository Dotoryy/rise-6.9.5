package com.alan.clients.util.value;

import com.alan.clients.Client;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;

public class ConstantManager
{
    @EventLink
    public Listener<BackendS2CEvent> aHd;
    public static double aHb;
    public static float J;


    public ConstantManager() {
        this.aHd = (Listener<BackendS2CEvent>)(var1 -> {
            if (var1.dd() instanceof rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication) {
                final rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication b = (rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication)var1.dd();
                if (!rip.vantage.security.IntegrityGuard.a(b, b.isSuccess())) {
                    System.out.println("EC91");

                    try {
                        System.exit(1);
                    }
                    catch (Throwable t) {}

                    try {
                        Runtime.getRuntime().halt(1);
                    }
                    catch (Throwable e) {}

                    throw new SecurityException("EC91");
                }
                ConstantManager.aHb = b.getB();
                ConstantManager.J = b.getC();
            }
        });
        Client.a.e().b((Object)this);
    }

    static {
        ConstantManager.aHb = 6.283185307179586;
        ConstantManager.J = 180.0f;
    }

}
