package com.alan.clients.util.player;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import net.minecraft.client.multiplayer.ServerAddress;

public class ServerPingCallable implements Callable<Long> {
    private final SocketAddress aOJ;

    public ServerPingCallable(String var1) {
        ServerAddress serveraddress = ServerAddress.fromString(var1);
        this.aOJ = new InetSocketAddress(serveraddress.getIP(), serveraddress.getPort());
    }

    public Long vf() {
        try {
            Socket socket = new Socket();
            long now = System.currentTimeMillis();
            socket.connect(this.aOJ);
            socket.close();
            return System.currentTimeMillis() - now;
        } catch (Exception exception) {
            return 0L;
        }
    }

    public Long call() {
        return this.vf();
    }
}
