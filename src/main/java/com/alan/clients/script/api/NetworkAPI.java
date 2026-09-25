package com.alan.clients.script.api;

import com.alan.clients.util.chat.ChatUtil;
import java.util.Arrays;
import javax.script.ScriptException;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C01PacketChatMessage;
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
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.client.C18PacketSpectate;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.client.a;
import net.minecraft.network.play.client.l;
import net.minecraft.network.play.client.m;
import net.minecraft.network.play.client.q;
import net.minecraft.network.play.client.t;
import net.minecraft.network.play.client.u;
import net.minecraft.network.play.client.v;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S10PacketSpawnPainting;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S28PacketEffect;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S34PacketMaps;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.play.server.S37PacketStatistics;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3APacketTabComplete;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.server.S49PacketUpdateEntityNBT;
import net.minecraft.network.play.server.aa;
import net.minecraft.network.play.server.ab;
import net.minecraft.network.play.server.ac;
import net.minecraft.network.play.server.ad;
import net.minecraft.network.play.server.ae;
import net.minecraft.network.play.server.af;
import net.minecraft.network.play.server.ag;
import net.minecraft.network.play.server.ap;
import net.minecraft.network.play.server.ay;
import net.minecraft.network.play.server.az;
import net.minecraft.network.play.server.bd;
import net.minecraft.network.play.server.bg;
import net.minecraft.network.play.server.bq;
import net.minecraft.network.play.server.bt;
import net.minecraft.network.play.server.bx;
import net.minecraft.network.play.server.c;
import net.minecraft.network.play.server.cc;
import net.minecraft.network.play.server.cd;
import net.minecraft.network.play.server.e;
import net.minecraft.network.play.server.f;
import net.minecraft.network.play.server.k;
import net.minecraft.network.play.server.n;
import net.minecraft.network.play.server.o;
import net.minecraft.network.play.server.s;
import net.minecraft.network.play.server.z;

public class NetworkAPI extends API {
    public static final Class<Packet<INetHandlerPlayServer>>[] serverbound = new Class[]{
        a.class,
        C01PacketChatMessage.class,
        C02PacketUseEntity.class,
        C03PacketPlayer.class,
        C04PacketPlayerPosition.class,
        C05PacketPlayerLook.class,
        C06PacketPlayerPosLook.class,
        C07PacketPlayerDigging.class,
        C08PacketPlayerBlockPlacement.class,
        l.class,
        m.class,
        C0BPacketEntityAction.class,
        C0CPacketInput.class,
        q.class,
        C0EPacketClickWindow.class,
        C0FPacketConfirmTransaction.class,
        t.class,
        u.class,
        v.class,
        C13PacketPlayerAbilities.class,
        C14PacketTabComplete.class,
        C15PacketClientSettings.class,
        C16PacketClientStatus.class,
        C17PacketCustomPayload.class,
        C18PacketSpectate.class,
        C19PacketResourcePackStatus.class
    };
    public static final Class<Packet<INetHandlerPlayClient>>[] clientbound = new Class[]{
        net.minecraft.network.play.server.a.class,
        S01PacketJoinGame.class,
        c.class,
        S03PacketTimeUpdate.class,
        e.class,
        f.class,
        S06PacketUpdateHealth.class,
        S07PacketRespawn.class,
        S08PacketPlayerPosLook.class,
        k.class,
        net.minecraft.network.play.server.l.class,
        net.minecraft.network.play.server.m.class,
        n.class,
        o.class,
        S0EPacketSpawnObject.class,
        net.minecraft.network.play.server.q.class,
        S10PacketSpawnPainting.class,
        s.class,
        S12PacketEntityVelocity.class,
        S13PacketDestroyEntities.class,
        S14PacketEntity.class,
        z.class,
        aa.class,
        ab.class,
        ac.class,
        ad.class,
        ae.class,
        af.class,
        ag.class,
        S20PacketEntityProperties.class,
        S21PacketChunkData.class,
        S22PacketMultiBlockChange.class,
        S23PacketBlockChange.class,
        S24PacketBlockAction.class,
        ap.class,
        S26PacketMapChunkBulk.class,
        S27PacketExplosion.class,
        S28PacketEffect.class,
        S29PacketSoundEffect.class,
        S2APacketParticles.class,
        S2BPacketChangeGameState.class,
        S2CPacketSpawnGlobalEntity.class,
        S2DPacketOpenWindow.class,
        ay.class,
        az.class,
        S30PacketWindowItems.class,
        S31PacketWindowProperty.class,
        S32PacketConfirmTransaction.class,
        bd.class,
        S34PacketMaps.class,
        S35PacketUpdateTileEntity.class,
        bg.class,
        S37PacketStatistics.class,
        S38PacketPlayerListItem.class,
        S39PacketPlayerAbilities.class,
        S3APacketTabComplete.class,
        S3BPacketScoreboardObjective.class,
        S3CPacketUpdateScore.class,
        bq.class,
        S3EPacketTeams.class,
        S3FPacketCustomPayload.class,
        bt.class,
        S41PacketServerDifficulty.class,
        S42PacketCombatEvent.class,
        bx.class,
        S44PacketWorldBorder.class,
        S45PacketTitle.class,
        cc.class,
        cd.class,
        S48PacketResourcePackSend.class,
        S49PacketUpdateEntityNBT.class
    };

    public NetworkAPI() {
    }

    private static Packet<?> instantiatePacket(EnumPacketDirection enumPacketDirection, int var1, Object... var2) throws ScriptException {
        try {
            Packet packet = null;

            try {
                if (enumPacketDirection == EnumPacketDirection.CLIENTBOUND) {
                    packet = (Packet)Arrays.stream(clientbound[var1].getConstructors())
                        .filter(var1x -> var1x.getParameterCount() == var2.length)
                        .findFirst()
                        .get()
                        .newInstance(var2);
                } else if (enumPacketDirection == EnumPacketDirection.SERVERBOUND) {
                    packet = (Packet)Arrays.stream(serverbound[var1].getConstructors())
                        .filter(var1x -> var1x.getParameterCount() == var2.length)
                        .findFirst()
                        .get()
                        .newInstance(var2);
                }
            } catch (Exception exception) {
                ChatUtil.b("Failed to instantiate packet!");
                throw new ScriptException(exception);
            }

            return packet;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    public void sendPacket(int var1, Object... var2) throws ScriptException {
        Packet packet = instantiatePacket(EnumPacketDirection.SERVERBOUND, var1, var2);
        MC.getNetHandler().addToSendQueue(packet);
    }

    public void receivePacket(int var1, Object... var2) throws ScriptException {
        Packet packet = instantiatePacket(EnumPacketDirection.CLIENTBOUND, var1, var2);
        MC.getNetHandler().v(packet);
    }

    public boolean isSingleplayer() {
        return MC.isSingleplayer();
    }

    public boolean isMultiplayer() {
        return !MC.isSingleplayer() && MC.getCurrentServerData() != null;
    }

    public String getServerIP() {
        return MC.getCurrentServerData() != null ? MC.getCurrentServerData().serverIP : null;
    }

    public String getServerName() {
        return MC.getCurrentServerData() != null ? MC.getCurrentServerData().serverName : null;
    }

    public String getServerMOTD() {
        return MC.getCurrentServerData() != null ? MC.getCurrentServerData().serverMOTD : null;
    }
}
