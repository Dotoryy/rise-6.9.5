package com.alan.clients.util.chat;

import com.alan.clients.Client;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.packet.PacketUtil;
import lombok.Generated;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.s;

public final class ChatUtil implements InstanceAccess {
    public static void b(Object var0, Object... var1) {
        if (aEg.thePlayer != null) {
            String s = String.format(Localization.ce(var0.toString()), var1);
            aEg.thePlayer.addChatMessage(new s(getPrefix() + s));
            System.out.println(var0);
        }
    }

    public static void c(Object var0, Object... var1) {
    }

    public static void d(Object var0, Object... var1) {
        if (aEg.thePlayer != null) {
            String s = var1 != null && var1.length != 0 ? String.format(var0.toString(), var1) : var0.toString();
            aEg.thePlayer.addChatMessage(new s(s));
        }
    }

    public static void send(Object var0) {
        if (aEg.thePlayer != null) {
            PacketUtil.send(new C01PacketChatMessage(var0.toString()));
        }
    }

    public static String getPrefix() {
        return Client.b + " » ";
    }

    @Generated
    private ChatUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
