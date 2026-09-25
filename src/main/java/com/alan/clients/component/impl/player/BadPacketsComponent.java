package com.alan.clients.component.impl.player;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.C16PacketClientStatus.EnumState;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.m;

public final class BadPacketsComponent extends Component {
    private static boolean cI;
    private static boolean cJ;
    private static boolean cK;
    private static boolean cL;
    private static boolean cM;
    private static boolean cN;
    @EventLink(value = 4)
    public final Listener<PacketSendEvent> onPacketSend = var0 -> {
        Packet packet = var0.dq();
        if (packet instanceof net.minecraft.network.play.client.l) {
            cI = true;
        } else if (packet instanceof m) {
            cK = true;
        } else if (packet instanceof C02PacketUseEntity) {
            cJ = true;
        } else if (packet instanceof C08PacketPlayerBlockPlacement) {
            cL = true;
        } else if (packet instanceof C07PacketPlayerDigging) {
            cL = true;
            cN = true;
        } else if (packet instanceof C0EPacketClickWindow
            || packet instanceof C16PacketClientStatus && ((C16PacketClientStatus)packet).getStatus() == EnumState.OPEN_INVENTORY_ACHIEVEMENT
            || packet instanceof net.minecraft.network.play.client.q) {
            cM = true;
        } else if (packet instanceof C03PacketPlayer) {
            reset();
        }
    };

    public BadPacketsComponent() {
    }

    public static boolean aW() {
        return a(true, true, true, true, true, false);
    }

    public static boolean bad(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4) {
        return a(var0, var1, var2, var3, var4, false);
    }

    public static boolean a(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5) {
        return cI && var0 || cJ && var1 || cK && var2 || cL && var3 || cM && var4 || cN && var5;
    }

    public static void reset() {
        cI = false;
        cK = false;
        cJ = false;
        cL = false;
        cM = false;
        cN = false;
    }
}
