package com.alan.clients.security.impl;

import com.alan.clients.security.CrashCheck;
import com.alan.clients.util.packet.PacketUtil;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C19PacketResourcePackStatus.Action;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S48PacketResourcePackSend;

public final class ResourcePackCheck extends CrashCheck {
    public ResourcePackCheck() {
        super("Resource Pack Check", "Server attempted to view files on the computer");
    }

    @Override
    public boolean handle(Packet<?> packet) {
        if (packet instanceof S48PacketResourcePackSend s48packetresourcepacksend) {
            String s = s48packetresourcepacksend.getURL();
            String hash = s48packetresourcepacksend.getHash();
            if (s.toLowerCase().startsWith("level://")) {
                return this.check(s, hash);
            }
        }

        return false;
    }

    private boolean check(String var1, String var2) {
        try {
            String s = new URI(var1).getScheme();
            boolean flag = "level".equals(s);
            if (!"http".equals(s) && !"https".equals(s) && !flag) {
                throw new URISyntaxException(var1, "Wrong protocol");
            }

            String s1 = URLDecoder.decode(var1.substring("level://".length()), StandardCharsets.UTF_8.toString());
            if (!flag || !s1.contains("..") && s1.endsWith("/resources.zip")) {
                return false;
            }

            System.out.println("Server tried to access the path: " + s1);
            throw new URISyntaxException(s1, "Invalid levelstorage resource pack path");
        } catch (Exception exception) {
            PacketUtil.sendNoEvent(new C19PacketResourcePackStatus(var2, Action.FAILED_DOWNLOAD));
            return true;
        }
    }
}
