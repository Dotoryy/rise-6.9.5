package com.alan.clients.script.api.wrapper.impl.packet;

import net.minecraft.network.Packet;
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

public class ScriptPacketFactory {
    public ScriptPacketFactory() {
    }

    public static ScriptPacket<?> wrap(Packet<?> packet) {
        if (packet == null) {
            return null;
        } else if (packet instanceof a) {
            return new ScriptPacketKeepAlive((a)packet);
        } else if (packet instanceof C01PacketChatMessage) {
            return new ScriptPacketChatMessage((C01PacketChatMessage)packet);
        } else if (packet instanceof C02PacketUseEntity) {
            return new ScriptPacketUseEntity((C02PacketUseEntity)packet);
        } else if (packet instanceof C06PacketPlayerPosLook) {
            return new ScriptPacketPosLook((C06PacketPlayerPosLook)packet);
        } else if (packet instanceof C05PacketPlayerLook) {
            return new ScriptPacketLook((C05PacketPlayerLook)packet);
        } else if (packet instanceof C04PacketPlayerPosition) {
            return new ScriptPacketPosition((C04PacketPlayerPosition)packet);
        } else if (packet instanceof C03PacketPlayer) {
            return new ScriptPacketPlayer((C03PacketPlayer)packet);
        } else if (packet instanceof C07PacketPlayerDigging) {
            return new ScriptPacketDigging((C07PacketPlayerDigging)packet);
        } else if (packet instanceof C08PacketPlayerBlockPlacement) {
            return new ScriptPacketBlockPlacement((C08PacketPlayerBlockPlacement)packet);
        } else if (packet instanceof l) {
            return new ScriptPacketHeldItem((l)packet);
        } else if (packet instanceof m) {
            return new ScriptPacketAnimation((m)packet);
        } else if (packet instanceof C0BPacketEntityAction) {
            return new ScriptPacketEntityAction((C0BPacketEntityAction)packet);
        } else if (packet instanceof C0CPacketInput) {
            return new ScriptPacketInput((C0CPacketInput)packet);
        } else if (packet instanceof q) {
            return new ScriptPacketCloseWindow((q)packet);
        } else if (packet instanceof C0EPacketClickWindow) {
            return new ScriptPacketClickWindow((C0EPacketClickWindow)packet);
        } else if (packet instanceof C0FPacketConfirmTransaction) {
            return new ScriptPacketTransaction((C0FPacketConfirmTransaction)packet);
        } else if (packet instanceof t) {
            return new ScriptPacketCreativeAction((t)packet);
        } else if (packet instanceof u) {
            return new ScriptPacketEnchantItem((u)packet);
        } else if (packet instanceof v) {
            return new ScriptPacketUpdateSign((v)packet);
        } else if (packet instanceof C13PacketPlayerAbilities) {
            return new ScriptPacketAbilities((C13PacketPlayerAbilities)packet);
        } else if (packet instanceof C14PacketTabComplete) {
            return new ScriptPacketTabComplete((C14PacketTabComplete)packet);
        } else if (packet instanceof C15PacketClientSettings) {
            return new ScriptPacketClientSettings((C15PacketClientSettings)packet);
        } else if (packet instanceof C16PacketClientStatus) {
            return new ScriptPacketClientStatus((C16PacketClientStatus)packet);
        } else if (packet instanceof C17PacketCustomPayload) {
            return new ScriptPacketCustomPayload((C17PacketCustomPayload)packet);
        } else if (packet instanceof C18PacketSpectate) {
            return new ScriptPacketSpectate((C18PacketSpectate)packet);
        } else if (packet instanceof C19PacketResourcePackStatus) {
            return new ScriptPacketResourcePackStatus((C19PacketResourcePackStatus)packet);
        } else if (packet instanceof net.minecraft.network.play.server.a) {
            return new ScriptPacketServerKeepAlive((net.minecraft.network.play.server.a)packet);
        } else if (packet instanceof S01PacketJoinGame) {
            return new ScriptPacketJoinGame((S01PacketJoinGame)packet);
        } else if (packet instanceof c) {
            return new ScriptPacketChat((c)packet);
        } else if (packet instanceof S03PacketTimeUpdate) {
            return new ScriptPacketTimeUpdate((S03PacketTimeUpdate)packet);
        } else if (packet instanceof e) {
            return new ScriptPacketEntityEquipment((e)packet);
        } else if (packet instanceof f) {
            return new ScriptPacketSpawnPosition((f)packet);
        } else if (packet instanceof S06PacketUpdateHealth) {
            return new ScriptPacketUpdateHealth((S06PacketUpdateHealth)packet);
        } else if (packet instanceof S07PacketRespawn) {
            return new ScriptPacketRespawn((S07PacketRespawn)packet);
        } else if (packet instanceof S08PacketPlayerPosLook) {
            return new ScriptPacketPlayerPosLook((S08PacketPlayerPosLook)packet);
        } else if (packet instanceof k) {
            return new ScriptPacketServerHeldItem((k)packet);
        } else if (packet instanceof net.minecraft.network.play.server.l) {
            return new ScriptPacketUseBed((net.minecraft.network.play.server.l)packet);
        } else if (packet instanceof net.minecraft.network.play.server.m) {
            return new ScriptPacketServerAnimation((net.minecraft.network.play.server.m)packet);
        } else if (packet instanceof n) {
            return new ScriptPacketSpawnPlayer((n)packet);
        } else if (packet instanceof o) {
            return new ScriptPacketCollectItem((o)packet);
        } else if (packet instanceof S0EPacketSpawnObject) {
            return new ScriptPacketSpawnObject((S0EPacketSpawnObject)packet);
        } else if (packet instanceof net.minecraft.network.play.server.q) {
            return new ScriptPacketSpawnMob((net.minecraft.network.play.server.q)packet);
        } else if (packet instanceof S10PacketSpawnPainting) {
            return new ScriptPacketSpawnPainting((S10PacketSpawnPainting)packet);
        } else if (packet instanceof s) {
            return new ScriptPacketSpawnXPOrb((s)packet);
        } else if (packet instanceof S12PacketEntityVelocity) {
            return new ScriptPacketVelocity((S12PacketEntityVelocity)packet);
        } else if (packet instanceof S13PacketDestroyEntities) {
            return new ScriptPacketDestroyEntities((S13PacketDestroyEntities)packet);
        } else if (packet instanceof S14PacketEntity) {
            return new ScriptPacketEntity((S14PacketEntity)packet);
        } else if (packet instanceof z) {
            return new ScriptPacketEntityTeleport((z)packet);
        } else if (packet instanceof aa) {
            return new ScriptPacketEntityHeadLook((aa)packet);
        } else if (packet instanceof ab) {
            return new ScriptPacketEntityStatus((ab)packet);
        } else if (packet instanceof ac) {
            return new ScriptPacketEntityAttach((ac)packet);
        } else if (packet instanceof ad) {
            return new ScriptPacketEntityMetadata((ad)packet);
        } else if (packet instanceof ae) {
            return new ScriptPacketEntityEffect((ae)packet);
        } else if (packet instanceof af) {
            return new ScriptPacketRemoveEntityEffect((af)packet);
        } else if (packet instanceof ag) {
            return new ScriptPacketSetExperience((ag)packet);
        } else if (packet instanceof S20PacketEntityProperties) {
            return new ScriptPacketEntityProperties((S20PacketEntityProperties)packet);
        } else if (packet instanceof S21PacketChunkData) {
            return new ScriptPacketChunkData((S21PacketChunkData)packet);
        } else if (packet instanceof S22PacketMultiBlockChange) {
            return new ScriptPacketMultiBlockChange((S22PacketMultiBlockChange)packet);
        } else if (packet instanceof S23PacketBlockChange) {
            return new ScriptPacketBlockChange((S23PacketBlockChange)packet);
        } else if (packet instanceof S24PacketBlockAction) {
            return new ScriptPacketBlockAction((S24PacketBlockAction)packet);
        } else if (packet instanceof ap) {
            return new ScriptPacketBlockBreakAnim((ap)packet);
        } else if (packet instanceof S26PacketMapChunkBulk) {
            return new ScriptPacketMapChunkBulk((S26PacketMapChunkBulk)packet);
        } else if (packet instanceof S27PacketExplosion) {
            return new ScriptPacketExplosion((S27PacketExplosion)packet);
        } else if (packet instanceof S28PacketEffect) {
            return new ScriptPacketEffect((S28PacketEffect)packet);
        } else if (packet instanceof S29PacketSoundEffect) {
            return new ScriptPacketSoundEffect((S29PacketSoundEffect)packet);
        } else if (packet instanceof S2APacketParticles) {
            return new ScriptPacketParticles((S2APacketParticles)packet);
        } else if (packet instanceof S2BPacketChangeGameState) {
            return new ScriptPacketChangeGameState((S2BPacketChangeGameState)packet);
        } else if (packet instanceof S2CPacketSpawnGlobalEntity) {
            return new ScriptPacketSpawnGlobalEntity((S2CPacketSpawnGlobalEntity)packet);
        } else if (packet instanceof S2DPacketOpenWindow) {
            return new ScriptPacketOpenWindow((S2DPacketOpenWindow)packet);
        } else if (packet instanceof ay) {
            return new ScriptPacketServerCloseWindow((ay)packet);
        } else if (packet instanceof az) {
            return new ScriptPacketSetSlot((az)packet);
        } else if (packet instanceof S30PacketWindowItems) {
            return new ScriptPacketWindowItems((S30PacketWindowItems)packet);
        } else if (packet instanceof S31PacketWindowProperty) {
            return new ScriptPacketWindowProperty((S31PacketWindowProperty)packet);
        } else if (packet instanceof S32PacketConfirmTransaction) {
            return new ScriptPacketServerTransaction((S32PacketConfirmTransaction)packet);
        } else if (packet instanceof bd) {
            return new ScriptPacketServerUpdateSign((bd)packet);
        } else if (packet instanceof S34PacketMaps) {
            return new ScriptPacketMaps((S34PacketMaps)packet);
        } else if (packet instanceof S35PacketUpdateTileEntity) {
            return new ScriptPacketUpdateTileEntity((S35PacketUpdateTileEntity)packet);
        } else if (packet instanceof bg) {
            return new ScriptPacketSignEditorOpen((bg)packet);
        } else if (packet instanceof S37PacketStatistics) {
            return new ScriptPacketStatistics((S37PacketStatistics)packet);
        } else if (packet instanceof S38PacketPlayerListItem) {
            return new ScriptPacketPlayerListItem((S38PacketPlayerListItem)packet);
        } else if (packet instanceof S39PacketPlayerAbilities) {
            return new ScriptPacketServerAbilities((S39PacketPlayerAbilities)packet);
        } else if (packet instanceof S3APacketTabComplete) {
            return new ScriptPacketServerTabComplete((S3APacketTabComplete)packet);
        } else if (packet instanceof S3BPacketScoreboardObjective) {
            return new ScriptPacketScoreboardObjective((S3BPacketScoreboardObjective)packet);
        } else if (packet instanceof S3CPacketUpdateScore) {
            return new ScriptPacketUpdateScore((S3CPacketUpdateScore)packet);
        } else if (packet instanceof bq) {
            return new ScriptPacketDisplayScoreboard((bq)packet);
        } else if (packet instanceof S3EPacketTeams) {
            return new ScriptPacketTeams((S3EPacketTeams)packet);
        } else if (packet instanceof S3FPacketCustomPayload) {
            return new ScriptPacketServerCustomPayload((S3FPacketCustomPayload)packet);
        } else if (packet instanceof bt) {
            return new ScriptPacketDisconnect((bt)packet);
        } else if (packet instanceof S41PacketServerDifficulty) {
            return new ScriptPacketServerDifficulty((S41PacketServerDifficulty)packet);
        } else if (packet instanceof S42PacketCombatEvent) {
            return new ScriptPacketCombatEvent((S42PacketCombatEvent)packet);
        } else if (packet instanceof bx) {
            return new ScriptPacketCamera((bx)packet);
        } else if (packet instanceof S44PacketWorldBorder) {
            return new ScriptPacketWorldBorder((S44PacketWorldBorder)packet);
        } else if (packet instanceof S45PacketTitle) {
            return new ScriptPacketTitle((S45PacketTitle)packet);
        } else if (packet instanceof cc) {
            return new ScriptPacketSetCompressionLevel((cc)packet);
        } else if (packet instanceof cd) {
            return new ScriptPacketPlayerListHeaderFooter((cd)packet);
        } else if (packet instanceof S48PacketResourcePackSend) {
            return new ScriptPacketResourcePackSend((S48PacketResourcePackSend)packet);
        } else {
            return packet instanceof S49PacketUpdateEntityNBT
                ? new ScriptPacketUpdateEntityNBT((S49PacketUpdateEntityNBT)packet)
                : new ScriptPacketFactory.ScriptPacketGeneric(packet);
        }
    }

    public static class ScriptPacketGeneric extends ScriptPacket<Packet<?>> {
        public ScriptPacketGeneric(Packet<?> packet) {
            super(packet);
        }
    }
}
