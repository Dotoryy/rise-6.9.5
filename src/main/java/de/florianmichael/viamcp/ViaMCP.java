package de.florianmichael.viamcp;

import com.mojang.authlib.GameProfile;
import com.viaversion.viabackwards.protocol.v1_17to1_16_4.Protocol1_17To1_16_4;
import com.viaversion.viabackwards.protocol.v1_20_3to1_20_2.Protocol1_20_3To1_20_2;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.protocols.v1_16_1to1_16_2.packet.ClientboundPackets1_16_2;
import com.viaversion.viaversion.protocols.v1_16_1to1_16_2.packet.ServerboundPackets1_16_2;
import com.viaversion.viaversion.protocols.v1_16_4to1_17.packet.ClientboundPackets1_17;
import com.viaversion.viaversion.protocols.v1_16_4to1_17.packet.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_8;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ClientboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.gui.AsyncVersionSlider;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import net.minecraft.client.Minecraft;

public class ViaMCP {
    public static final int NATIVE_VERSION = 47;
    public static ViaMCP INSTANCE;
    public UserConnection user;
    private AsyncVersionSlider asyncVersionSlider;

    public static void create() {
        INSTANCE = new ViaMCP();
    }

    public ViaMCP() {
        ViaLoadingBase.ViaLoadingBaseBuilder.create().runDirectory(new File("ViaMCP")).nativeVersion(47).onProtocolReload(var1 -> {
            if (this.getAsyncVersionSlider() != null) {
                this.getAsyncVersionSlider().setVersion(var1.getVersion());
            }
        }).build();
        Protocol1_20_3To1_20_2 protocol1_20_3to1_20_2 = Via.getManager().getProtocolManager().getProtocol(Protocol1_20_3To1_20_2.class);
        protocol1_20_3to1_20_2.registerServerbound(State.LOGIN, ServerboundLoginPackets.LOGIN_ACKNOWLEDGED, var1 -> this.user = var1.user());
        protocol1_20_3to1_20_2.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO, var0 -> {
            var0.cancel();
            PacketWrapper packetwrapper = PacketWrapper.create(ServerboundLoginPackets.HELLO, var0.user());
            GameProfile gameprofile = Minecraft.getMinecraft().getSession().getProfile();
            packetwrapper.write(Types.STRING, gameprofile.getName());
            UUID uuid = gameprofile.getId();
            if (uuid != null) {
                System.out.println("online");
                packetwrapper.write(Types.UUID, uuid);
            } else {
                System.out.println("offline");
                packetwrapper.write(Types.UUID, UUID.nameUUIDFromBytes(gameprofile.getName().getBytes(StandardCharsets.UTF_8)));
            }

            packetwrapper.sendToServer(Protocol1_20_3To1_20_2.class);
        });
        this.fixTransactions();
        Via.getManager()
            .getProtocolManager()
            .getProtocol(Protocol1_9To1_8.class)
            .registerClientbound(ClientboundPackets1_9.PLAYER_POSITION, ClientboundPackets1_8.PLAYER_POSITION, var0 -> {}, true);
    }

    private void fixTransactions() {
        Protocol1_17To1_16_4 protocol1_17to1_16_4 = Via.getManager().getProtocolManager().getProtocol(Protocol1_17To1_16_4.class);
        protocol1_17to1_16_4.registerClientbound(ClientboundPackets1_17.PING, ClientboundPackets1_16_2.CONTAINER_ACK, var0 -> {}, true);
        protocol1_17to1_16_4.registerServerbound(ServerboundPackets1_16_2.CONTAINER_ACK, ServerboundPackets1_17.PONG, var0 -> {}, true);
    }

    public void initAsyncSlider() {
        this.initAsyncSlider(5, 5, 110, 20);
    }

    public void initAsyncSlider(int var1, int var2, int var3, int var4) {
        this.asyncVersionSlider = new AsyncVersionSlider(-1, var1, var2, Math.max(var3, 110), var4);
    }

    public AsyncVersionSlider getAsyncVersionSlider() {
        return this.asyncVersionSlider;
    }
}
