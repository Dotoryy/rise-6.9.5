package com.alan.clients.component.impl.monitoring;

import com.alan.clients.compat.RevivedPaths;
import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.mojang.authlib.GameProfile;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S08PacketPlayerPosLook.EnumFlags;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity.S15PacketEntityRelMove;
import net.minecraft.network.play.server.S14PacketEntity.S16PacketEntityLook;
import net.minecraft.network.play.server.S14PacketEntity.S17PacketEntityLookMove;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S38PacketPlayerListItem.AddPlayerData;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
import net.minecraft.network.play.server.S3CPacketUpdateScore;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S44PacketWorldBorder.Action;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.network.play.server.ad;
import net.minecraft.network.play.server.ag;
import net.minecraft.network.play.server.e;
import net.minecraft.network.play.server.f;
import net.minecraft.network.play.server.k;
import net.minecraft.network.play.server.m;
import net.minecraft.network.play.server.z;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.s;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings.GameType;
import net.minecraft.world.chunk.Chunk;
import rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStopRecording;
import rip.vantage.network.core.VantageNetwork;

public class ReplayRecordingComponent extends Component {
    public int eX;
    public Method spawnPacketMethod;
    public boolean eM;
    public long startTime;
    public DataOutputStream output;
    public int pendingSize;
    public boolean eH;
    public volatile boolean recording;
    public Object ex = new Object();
    public int lastX;
    public byte lastFlags;
    public boolean selfSpawned;
    public boolean eW;
    public ByteArrayOutputStream buffer;
    public boolean eG;
    public static int ew;
    public String serverIp;
    public boolean eK;
    public static UUID ev;
    @EventLink
    public Listener<TickEvent> onTick;
    public boolean eJ;
    @EventLink
    public Listener<WorldChangeEvent> onWorldChange;
    public byte lastHeadYaw;
    public long eI;
    public List<byte[]> pending;
    public ItemStack[] lastEquipment;
    @EventLink
    public Listener<BackendS2CEvent> fg;
    public byte lastPitch;
    public int fd;
    public Field eA;
    public int lastZ;
    public byte lastYaw;
    public Object ey = new Object();
    public String name;
    public Set<UUID> seenPlayers;
    public int lastY;
    public Map<UUID, GameProfile> profiles;
    public boolean eL;
    public int duration;
    public ItemStack eV;

    public void uploadRecording(String var1, byte[] var2, String var3) {

        try {
            if (var1 == null || VantageNetwork.aKB().aKK() == null) {
                return;
            }

            String s3 = escapeJson(var1);
            int l = var2.length;
            String s = escapeJson(var3);
            int l2 = l;
            String s1 = s3;
            String s2 = "{\"id\":30,\"a\":\"" + s1 + "\",\"b\":" + l2 + ",\"c\":\"" + s + "\"}";
            VantageNetwork.aKB().aKK().sendMessage(s2);
            VantageNetwork.aKB().aKK().o(var2);
        } catch (Exception exception) {
        }
    }

    public byte[] buildReplayFile(byte[] var1, String var2, long var3, long var5) throws java.io.IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        ZipOutputStream zipoutputstream = new ZipOutputStream(bytearrayoutputstream);
        CRC32 crc32 = new CRC32();
        crc32.update(var1);
        zipoutputstream.putNextEntry(new ZipEntry("recording.tmcpr"));
        zipoutputstream.write(var1);
        zipoutputstream.closeEntry();
        zipoutputstream.putNextEntry(new ZipEntry("metaData.json"));
        String s = this.buildPlayerUuidJson();
        String s4 = escapeJson(var2);
        String s1 = s;
        long k = var5;
        long l = var3;
        String s2 = s4;
        String s3 = "{\"singleplayer\":false,\"serverName\":\""
            + s2
            + "\",\"duration\":"
            + l
            + ",\"date\":"
            + k
            + ",\"mcversion\":\"1.8.9\",\"fileFormat\":\"MCPR\",\"fileFormatVersion\":14,\"protocol\":47,\"generator\":\"rise\",\"selfId\":-1,\"players\":"
            + s1
            + "}";
        zipoutputstream.write(s3.getBytes(StandardCharsets.UTF_8));
        zipoutputstream.closeEntry();
        zipoutputstream.putNextEntry(new ZipEntry("markers.json"));
        zipoutputstream.write("[]".getBytes(StandardCharsets.UTF_8));
        zipoutputstream.closeEntry();
        zipoutputstream.putNextEntry(new ZipEntry("mods.json"));
        zipoutputstream.write(("{\"requiredMods\":[]}").getBytes(StandardCharsets.UTF_8));
        zipoutputstream.closeEntry();
        zipoutputstream.putNextEntry(new ZipEntry("recording.tmcpr.crc32"));
        zipoutputstream.write(Long.toString(crc32.getValue()).getBytes(StandardCharsets.UTF_8));
        zipoutputstream.closeEntry();
        zipoutputstream.close();
        return bytearrayoutputstream.toByteArray();
    }

    public void a(WorldClient world, EntityPlayer player, Set<UUID> uuids) {
        if (world != null && player != null) {
            ArrayList arraylist = new ArrayList();
            this.bB();
            this.a(new S41PacketServerDifficulty(world.getDifficulty(), false), EnumConnectionState.PLAY);
            this.a(new f(world.getSpawnPoint()), EnumConnectionState.PLAY);
            this.a(new S44PacketWorldBorder(world.getWorldBorder(), Action.INITIALIZE), EnumConnectionState.PLAY);
            this.a(
                new S03PacketTimeUpdate(world.getTotalWorldTime(), world.getWorldTime(), world.getGameRules().getBoolean("doDaylightCycle")),
                EnumConnectionState.PLAY
            );
            this.a(
                new S06PacketUpdateHealth(player.getHealth(), player.getFoodStats().getFoodLevel(), player.getFoodStats().getSaturationLevel()),
                EnumConnectionState.PLAY
            );
            this.a(new k(player.inventory.currentItem), EnumConnectionState.PLAY);
            this.a(new S39PacketPlayerAbilities(player.capabilities), EnumConnectionState.PLAY);
            this.a(new ag(player.experience, player.experienceTotal, player.experienceLevel), EnumConnectionState.PLAY);
            this.b(player);
            if (!this.a(world.getScoreboard())) {
                this.flushPending();
            }

            this.a(world, player);
            this.a(world, player, uuids, arraylist);
            this.b(arraylist);
            this.a(
                new S08PacketPlayerPosLook(player.posX, player.posY, player.posZ, player.pl, player.rotationPitch, EnumSet.noneOf(EnumFlags.class)),
                EnumConnectionState.PLAY
            );
        }
    }

    public String buildPlayerUuidJson() {
        if (aEg.getNetHandler() == null && aEg.theWorld == null) {
            return "[]";
        }

        StringBuilder stringbuilder = new StringBuilder("[");
        LinkedHashSet linkedhashset = new LinkedHashSet();
        int k_hi = 1;
        if (aEg.getNetHandler() != null) {
            Iterator iterator = aEg.getNetHandler().getPlayerInfoMap().iterator();

            while (iterator.hasNext()) {
                NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)iterator.next();
                if (networkplayerinfo != null && networkplayerinfo.getGameProfile() != null && networkplayerinfo.getGameProfile().getId() != null) {
                    linkedhashset.add(networkplayerinfo.getGameProfile().getId());
                }
            }
        }

        if (aEg.theWorld != null) {
            Iterator iterator1 = aEg.theWorld.playerEntities.iterator();

            while (iterator1.hasNext()) {
                EntityPlayer entityplayer = (EntityPlayer)iterator1.next();
                if (entityplayer != null && entityplayer.getGameProfile() != null && entityplayer.getGameProfile().getId() != null) {
                    linkedhashset.add(entityplayer.getGameProfile().getId());
                }
            }
        }

        for (Iterator iterator2 = linkedhashset.iterator(); iterator2.hasNext(); k_hi = 0) {
            UUID uuid = (UUID)iterator2.next();
            if (k_hi == 0) {
                stringbuilder.append(',');
            }

            stringbuilder.append('"').append(uuid.toString()).append('"');
        }

        stringbuilder.append((char)('A' ^ '\u001c'));
        return stringbuilder.toString();
    }

    public void bB() {
        if (aEg.thePlayer != null) {
            this.eM = true;
            this.a(new net.minecraft.network.play.server.n(aEg.thePlayer), EnumConnectionState.PLAY);
            this.a(new ad(aEg.thePlayer.getEntityId(), aEg.thePlayer.getDataWatcher(), true), EnumConnectionState.PLAY);
            this.a(
                new net.minecraft.network.play.server.aa(aEg.thePlayer, (byte)(aEg.thePlayer.getRotationYawHead() * 256.0F / 360.0F)), EnumConnectionState.PLAY
            );
            this.a(
                new S16PacketEntityLook(
                    aEg.thePlayer.getEntityId(),
                    (byte)(aEg.thePlayer.renderYawOffset * 256.0F / 360.0F),
                    (byte)(aEg.thePlayer.rotationPitch * 256.0F / 360.0F),
                    aEg.thePlayer.onGround
                ),
                EnumConnectionState.PLAY
            );
            BaseAttributeMap baseattributemap = aEg.thePlayer.getAttributeMap();
            if (baseattributemap != null) {
                this.a(new S20PacketEntityProperties(aEg.thePlayer.getEntityId(), baseattributemap.getAllAttributes()), EnumConnectionState.PLAY);
            }

            this.c(aEg.thePlayer);

            for (int i = 0; i < 5; i++) {
                ItemStack itemstack = aEg.thePlayer.getEquipmentInSlot(i);
                if (itemstack != null) {
                    this.a(new e(aEg.thePlayer.getEntityId(), i, itemstack), EnumConnectionState.PLAY);
                }

                this.lastEquipment[i] = ItemStack.copyItemStack(itemstack);
            }

            Iterator iterator = aEg.thePlayer.getActivePotionEffects().iterator();

            while (iterator.hasNext()) {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                this.a(new net.minecraft.network.play.server.ae(aEg.thePlayer.getEntityId(), potioneffect), EnumConnectionState.PLAY);
            }

            this.lastX = (int)Math.floor(aEg.thePlayer.posX * 32.0);
            this.lastY = (int)Math.floor(aEg.thePlayer.posY * 32.0);
            this.lastZ = (int)Math.floor(aEg.thePlayer.posZ * 32.0);
            this.lastYaw = (byte)(aEg.thePlayer.renderYawOffset * 256.0F / 360.0F);
            this.lastPitch = (byte)(aEg.thePlayer.rotationPitch * 256.0F / 360.0F);
            this.lastHeadYaw = (byte)(aEg.thePlayer.getRotationYawHead() * 256.0F / 360.0F);
            this.lastFlags = aEg.thePlayer.getDataWatcher().getWatchableObjectByte(0);
            this.eV = ItemStack.copyItemStack(aEg.thePlayer.inventory.getCurrentItem());
            this.eW = aEg.thePlayer.isSwingInProgress;
            this.eX = aEg.thePlayer.swingProgressInt;
            this.selfSpawned = true;
        }
    }

    public Packet<?> b(Entity entity) {
        try {
            if (entity instanceof EntityPlayer) {
                return new net.minecraft.network.play.server.n((EntityPlayer)entity);
            }

            if (this.spawnPacketMethod == null) {
                this.spawnPacketMethod = EntityTrackerEntry.class.getDeclaredMethod("createSpawnPacket");
                this.spawnPacketMethod.setAccessible(true);
            }

            return (Packet<?>)this.spawnPacketMethod.invoke(new EntityTrackerEntry(entity, 0, 0, true));
        } catch (Exception exception) {
            return null;
        }
    }

    public void bw() {
        if (this.canRecord()) {
            WorldClient worldclient = aEg.theWorld;
            NetHandlerPlayClient nethandlerplayclient = aEg.getNetHandler();
            HashSet hashset = new HashSet();
            this.fd = Math.max(0, (int)(System.currentTimeMillis() - this.startTime));

            try {
                this.a(new S02PacketLoginSuccess(new GameProfile(ev, "Player")), EnumConnectionState.LOGIN);
                this.a(worldclient, nethandlerplayclient, hashset);
                this.a(this.bG(), EnumConnectionState.PLAY);
                this.a(this.buildJoinGamePacket(), EnumConnectionState.PLAY);
                this.a(this.buildRespawnPacket(), EnumConnectionState.PLAY);
                this.a(worldclient, aEg.thePlayer, hashset);
            } catch (Throwable throwable) {
                this.fd = -1;
                throw throwable;
            }

            this.fd = -1;
        }
    }

    public void a(GameProfile profile, int var2, GameType gameType, IChatComponent chat) {
        if (this.isValidProfile(profile)) {
            this.b(profile);
            this.seenPlayers.add(profile.getId());
            S38PacketPlayerListItem s38packetplayerlistitem = new S38PacketPlayerListItem(
                net.minecraft.network.play.server.S38PacketPlayerListItem.Action.ADD_PLAYER, Collections.emptyList()
            );
            List list = s38packetplayerlistitem.getEntries();
            S38PacketPlayerListItem s38packetplayerlistitem1 = s38packetplayerlistitem;
            Objects.requireNonNull(s38packetplayerlistitem);
            list.add(new AddPlayerData(s38packetplayerlistitem1, profile, var2, gameType != null ? gameType : GameType.SURVIVAL, chat));
            this.a(s38packetplayerlistitem, EnumConnectionState.PLAY);
        }
    }

    public boolean a(Scoreboard scoreboard) {
        if (scoreboard == null) {
            return false;
        }

        int i1_hi = 0;
        HashSet hashset = new HashSet();
        Iterator iterator2 = scoreboard.getScoreObjectives().iterator();

        while (iterator2.hasNext()) {
            ScoreObjective scoreobjective = (ScoreObjective)iterator2.next();
            if (scoreobjective != null && scoreobjective.getName() != null && hashset.add(scoreobjective.getName())) {
                this.a(new S3BPacketScoreboardObjective(scoreobjective, 0), EnumConnectionState.PLAY);
                i1_hi = 1;
            }
        }

        for (int i = 0; i < 19; i = i + 1) {
            ScoreObjective scoreobjective1 = scoreboard.getObjectiveInDisplaySlot(i);
            if (scoreobjective1 != null) {
                this.a(new net.minecraft.network.play.server.bq(i, scoreobjective1), EnumConnectionState.PLAY);
                i1_hi = 1;
            }
        }

        Iterator iterator = scoreboard.getScores().iterator();

        while (iterator.hasNext()) {
            Score score = (Score)iterator.next();
            if (score != null && score.getObjective() != null) {
                this.a(new S3CPacketUpdateScore(score), EnumConnectionState.PLAY);
                i1_hi = 1;
            }
        }

        Iterator iterator1 = scoreboard.getTeams().iterator();

        while (iterator1.hasNext()) {
            ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)iterator1.next();
            if (scoreplayerteam != null) {
                this.a(new S3EPacketTeams(scoreplayerteam, 0), EnumConnectionState.PLAY);
                i1_hi = 1;
            }
        }

        return (i1_hi) != 0;
    }

    public void b(Packet<?> packet, EnumConnectionState enumConnectionState, byte[] var3) {
        if (enumConnectionState == EnumConnectionState.PLAY && packet != null) {
            if (packet instanceof S01PacketJoinGame) {
                this.clearPending();
            } else if (packet instanceof S3BPacketScoreboardObjective
                || packet instanceof S3CPacketUpdateScore
                || packet instanceof net.minecraft.network.play.server.bq
                || packet instanceof S3EPacketTeams) {
                byte[] abyte = var3 != null && var3.length > 0 ? var3 : this.b(packet, enumConnectionState);
                if (abyte != null && abyte.length != 0) {
                    synchronized (this.ey) {
                        this.pending.add(abyte);
                        this.pendingSize += abyte.length;

                        while (this.pendingSize > 262144 && !this.pending.isEmpty()) {
                            byte[] abyte1 = this.pending.remove(0);
                            this.pendingSize -= abyte1.length;
                        }
                    }
                }
            }
        }
    }

    public S07PacketRespawn buildRespawnPacket() {
        WorldClient worldclient = aEg.theWorld;
        EntityPlayerSP entityplayersp = aEg.thePlayer;
        return new S07PacketRespawn(entityplayersp.dimension, worldclient.getDifficulty(), worldclient.getWorldType(), this.getGameType(worldclient));
    }

    public void d(Packet<?> packet, EnumConnectionState enumConnectionState) {
        if (enumConnectionState == EnumConnectionState.PLAY && packet instanceof S38PacketPlayerListItem s38packetplayerlistitem) {
            net.minecraft.network.play.server.S38PacketPlayerListItem.Action action = s38packetplayerlistitem.getAction();
            Iterator iterator = s38packetplayerlistitem.getEntries().iterator();

            while (iterator.hasNext()) {
                AddPlayerData addplayerdata = (AddPlayerData)iterator.next();
                if (addplayerdata != null && addplayerdata.getProfile() != null && addplayerdata.getProfile().getId() != null) {
                    if (action == net.minecraft.network.play.server.S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                        this.seenPlayers.remove(addplayerdata.getProfile().getId());
                    } else {
                        if (action == net.minecraft.network.play.server.S38PacketPlayerListItem.Action.ADD_PLAYER && this.isValidProfile(addplayerdata.getProfile())) {
                            this.b(addplayerdata.getProfile());
                        }

                        this.seenPlayers.add(addplayerdata.getProfile().getId());
                    }
                }
            }
        }
    }

    public GameType getGameType(World world) {
        if (aEg.playerController != null && aEg.playerController.getCurrentGameType() != null) {
            return aEg.playerController.getCurrentGameType();
        }
        return world != null && world.getWorldInfo() != null && world.getWorldInfo().getGameType() != null
            ? world.getWorldInfo().getGameType()
            : GameType.SURVIVAL;
    }

    public void b(List<GameProfile> profiles) {
        if (profiles != null && !profiles.isEmpty()) {
            HashSet hashset = new HashSet();
            Iterator iterator = profiles.iterator();

            while (iterator.hasNext()) {
                GameProfile gameprofile = (GameProfile)iterator.next();
                if (this.isValidProfile(gameprofile) && hashset.add(gameprofile.getId())) {
                    this.seenPlayers.remove(gameprofile.getId());
                    S38PacketPlayerListItem s38packetplayerlistitem = new S38PacketPlayerListItem(
                        net.minecraft.network.play.server.S38PacketPlayerListItem.Action.REMOVE_PLAYER, Collections.emptyList()
                    );
                    List list = s38packetplayerlistitem.getEntries();
                    S38PacketPlayerListItem s38packetplayerlistitem1 = s38packetplayerlistitem;
                    Objects.requireNonNull(s38packetplayerlistitem);
                    list.add(new AddPlayerData(s38packetplayerlistitem1, gameprofile, 0, GameType.SURVIVAL, null));
                    this.a(s38packetplayerlistitem, EnumConnectionState.PLAY);
                }
            }
        }
    }

    public void clearProfileCache() {
        this.seenPlayers.clear();
        this.profiles.clear();
    }

    public void b(EntityPlayer player) {
        if (player != null && player.inventoryContainer != null) {
            this.a(new S30PacketWindowItems(player.inventoryContainer.windowId, player.inventoryContainer.getInventory()), EnumConnectionState.PLAY);
        }
    }

    public void a(S38PacketPlayerListItem packet) {
        if (packet != null) {
            net.minecraft.network.play.server.S38PacketPlayerListItem.Action action = packet.getAction();
            Iterator iterator = packet.getEntries().iterator();

            while (iterator.hasNext()) {
                AddPlayerData addplayerdata = (AddPlayerData)iterator.next();
                if (addplayerdata != null && addplayerdata.getProfile() != null && addplayerdata.getProfile().getId() != null) {
                    if (action == net.minecraft.network.play.server.S38PacketPlayerListItem.Action.ADD_PLAYER) {
                        this.b(addplayerdata.getProfile());
                        this.seenPlayers.add(addplayerdata.getProfile().getId());
                    } else if (!this.seenPlayers.contains(addplayerdata.getProfile().getId())) {
                        GameProfile gameprofile = this.a(addplayerdata.getProfile().getId(), addplayerdata.getProfile().getName());
                        if (gameprofile != null) {
                            this.a(
                                gameprofile,
                                0,
                                this.getGameType(aEg.theWorld),
                                action
                                        == net.minecraft.network.play.server.S38PacketPlayerListItem.Action.UPDATE_DISPLAY_NAME
                                    ? addplayerdata.getDisplayName()
                                    : null
                            );
                        }
                    }
                }
            }
        }
    }

    public void flushPending() {
        ArrayList arraylist;
        synchronized (this.ey) {
            if (this.pending.isEmpty()) {
                return;
            }

            arraylist = new ArrayList<>(this.pending);
        }

        for (byte[] abyte : (Iterable<byte[]>)arraylist) {
            this.writeEntry(abyte);
        }
    }

    public void stopRecording() {
        long k;
        byte[] abyte;
        String s;
        String s1;
        long l;
        synchronized (this.ex) {
            if (!this.recording || this.buffer == null) {
                return;
            }

            this.recording = false;
            k = System.currentTimeMillis() - this.startTime;
            abyte = this.buffer.toByteArray();
            s = this.name;
            s1 = this.serverIp;
            l = this.startTime;

            try {
                this.output.close();
            } catch (IOException ioexception) {
            }

            this.output = null;
            this.buffer = null;
            this.name = null;
            this.eG = false;
            this.eH = false;
            this.eI = 0L;
            this.eK = false;
            this.eL = false;
            this.clearProfileCache();
            this.eM = false;
            this.selfSpawned = false;
            Arrays.fill(this.lastEquipment, null);
            this.eV = null;
            this.eW = false;
            this.eX = 0;
            this.eJ = false;
            this.fd = -1;
        }

        if (abyte.length != 0) {
            Thread thread = new Thread(() -> {

                try {
                    byte[] abytex = this.buildReplayFile(abyte, s, k, l);
                    this.uploadRecording(s1, abytex, s);
                } catch (IOException ioexceptionx) {
                }
            }, "ReplayUpload");
            thread.setDaemon((93 + -92) != 0);
            thread.start();
        }
    }

    public byte[] b(Packet<?> packet, EnumConnectionState enumConnectionState) {
        if (packet != null && enumConnectionState != null) {
            Integer integer = enumConnectionState.getPacketId(EnumPacketDirection.CLIENTBOUND, packet);
            if (integer == null) {
                return null;
            }

            PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());

            byte[] abyte1;
            label41: {
                Object object4;
                try {
                    try {
                        packetbuffer.writeVarIntToBuffer(integer);
                        packet.writePacketData(packetbuffer);
                        byte[] abyte = new byte[packetbuffer.readableBytes()];
                        packetbuffer.readBytes(abyte);
                        abyte1 = abyte;
                        break label41;
                    } catch (Exception exception) {
                        object4 = null;
                    }
                } catch (Throwable throwable) {
                    packetbuffer.release();
                    throw throwable;
                }

                packetbuffer.release();
                return (byte[])object4;
            }

            packetbuffer.release();
            return abyte1;
        }
        return null;
    }

    public void a(Packet<?> packet, EnumConnectionState enumConnectionState) {
        byte[] abyte = this.b(packet, enumConnectionState);
        if (abyte != null && abyte.length != 0) {
            this.writeEntry(abyte);
        }
    }

    static {

        ev = UUID.fromString("d41d8cd9-8f00-3204-a980-0998ecf8427e");
    }

    public void b(GameProfile profile) {
        if (this.isValidProfile(profile)) {
            this.profiles.put(profile.getId(), profile);
        }
    }

    public void bD() {
        if (aEg.thePlayer != null) {
            if (!this.selfSpawned) {
                this.a(new z(aEg.thePlayer), EnumConnectionState.PLAY);
            } else {
                int floor2 = (int)Math.floor(aEg.thePlayer.posX * 32.0);
                int floor3 = (int)Math.floor(aEg.thePlayer.posY * 32.0);
                int floor4 = (int)Math.floor(aEg.thePlayer.posZ * 32.0);
                int renderYawOffset2 = (byte)(aEg.thePlayer.renderYawOffset * 256.0F / 360.0F);
                int rotationPitch2 = (byte)(aEg.thePlayer.rotationPitch * 256.0F / 360.0F);
                int rotationYawHead = (byte)(aEg.thePlayer.getRotationYawHead() * 256.0F / 360.0F);
                int dJ4 = floor2 - this.lastX;
                int i5_lo = floor3 - this.lastY;
                int dK4 = floor4 - this.lastZ;
                int notFlag = dJ4 == 0 && i5_lo == 0 && dK4 == 0 ? 0 : 1;
                int notFlag2 = renderYawOffset2 == this.lastYaw && rotationPitch2 == this.lastPitch ? 0 : 1;
                if (dJ4 < -128 || dJ4 > 127 || i5_lo < -128 || i5_lo > 127 || dK4 < -128 || dK4 > 127) {
                    this.a(new z(aEg.thePlayer), EnumConnectionState.PLAY);
                } else if (notFlag != 0 && notFlag2 != 0) {
                    this.a(
                        new S17PacketEntityLookMove(
                            aEg.thePlayer.getEntityId(),
                            (byte)dJ4,
                            (byte)i5_lo,
                            (byte)dK4,
                            (byte)(renderYawOffset2),
                            (byte)(rotationPitch2),
                            aEg.thePlayer.onGround
                        ),
                        EnumConnectionState.PLAY
                    );
                } else if (notFlag != 0) {
                    this.a(
                        new S15PacketEntityRelMove(aEg.thePlayer.getEntityId(), (byte)dJ4, (byte)i5_lo, (byte)dK4, aEg.thePlayer.onGround),
                        EnumConnectionState.PLAY
                    );
                } else if (notFlag2 != 0) {
                    this.a(
                        new S16PacketEntityLook(aEg.thePlayer.getEntityId(), (byte)(renderYawOffset2), (byte)(rotationPitch2), aEg.thePlayer.onGround),
                        EnumConnectionState.PLAY
                    );
                }

                if (rotationYawHead != this.lastHeadYaw) {
                    this.a(new net.minecraft.network.play.server.aa(aEg.thePlayer, (byte)(rotationYawHead)), EnumConnectionState.PLAY);
                }

                this.lastX = floor2;
                this.lastY = floor3;
                this.lastZ = floor4;
                this.lastYaw = (byte)(renderYawOffset2);
                this.lastPitch = (byte)(rotationPitch2);
                this.lastHeadYaw = (byte)(rotationYawHead);
            }
        }
    }

    public List<GameProfile> c(Packet<?> packet, EnumConnectionState enumConnectionState) {
        if (enumConnectionState != EnumConnectionState.PLAY || packet == null) {
            return Collections.emptyList();
        }

        if (packet instanceof S38PacketPlayerListItem) {
            this.a((S38PacketPlayerListItem)packet);
            return Collections.emptyList();
        }

        if (packet instanceof net.minecraft.network.play.server.n) {
            GameProfile gameprofile = this.a((net.minecraft.network.play.server.n)packet);
            if (gameprofile != null) {
                return Collections.singletonList(gameprofile);
            }
        }

        return Collections.emptyList();
    }

    public void a(WorldClient world, EntityPlayer player, Set<UUID> uuids, List<GameProfile> profiles) {
        HashSet hashset = new HashSet();
        Iterator iterator = world.loadedEntityList.iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            this.a(entity, player, hashset, uuids, profiles);
        }

        Iterator iterator1 = world.playerEntities.iterator();

        while (iterator1.hasNext()) {
            EntityPlayer entityplayer = (EntityPlayer)iterator1.next();
            this.a(entityplayer, player, hashset, uuids, profiles);
        }
    }

    public static String escapeJson(String var0) {
        return var0 == null ? "" : var0.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public net.minecraft.network.play.server.cd bG() {
        net.minecraft.network.play.server.cd cd = new net.minecraft.network.play.server.cd(new s(""));

        try {
            Field field = net.minecraft.network.play.server.cd.class.getDeclaredField("bqe");
            field.setAccessible(true);
            field.set(cd, new s(""));
        } catch (Exception exception) {
        }

        while (true) {
            try {
                return cd;
            } catch (Exception exception1) {
            }
        }
    }

    public void bC() {
        if (aEg.thePlayer != null && this.selfSpawned) {
            int watchableObjectByte = aEg.thePlayer.getDataWatcher().getWatchableObjectByte(0);
            if (watchableObjectByte != this.lastFlags) {
                this.a(new ad(aEg.thePlayer.getEntityId(), aEg.thePlayer.getDataWatcher(), true), EnumConnectionState.PLAY);
                this.lastFlags = (byte)(watchableObjectByte);
            }

            for (int i = 0; i < this.lastEquipment.length; i++) {
                ItemStack itemstack = aEg.thePlayer.getEquipmentInSlot(i);
                if (!ItemStack.areItemStacksEqual(itemstack, this.lastEquipment[i])) {
                    this.a(new e(aEg.thePlayer.getEntityId(), i, itemstack), EnumConnectionState.PLAY);
                    this.lastEquipment[i] = ItemStack.copyItemStack(itemstack);
                }
            }

            ItemStack itemstack1 = aEg.thePlayer.inventory.getCurrentItem();
            this.eV = ItemStack.copyItemStack(itemstack1);
            int swingProgressInt2 = aEg.thePlayer.swingProgressInt;
            if (aEg.thePlayer.isSwingInProgress && (!this.eW || swingProgressInt2 < this.eX)) {
                this.a(new m(aEg.thePlayer, 0), EnumConnectionState.PLAY);
            }

            this.eW = aEg.thePlayer.isSwingInProgress;
            this.eX = swingProgressInt2;
        }
    }

    public boolean canRecord() {
        return aEg.theWorld != null && aEg.thePlayer != null && aEg.getNetHandler() != null && aEg.getNetHandler().doneLoadingTerrain;
    }


    public GameProfile a(net.minecraft.network.play.server.n var1) {
        if (var1 != null && var1.agh() != null && !this.seenPlayers.contains(var1.agh())) {
            GameProfile gameprofile = this.a(var1.agh(), null);
            if (gameprofile == null) {
                return null;
            }

            this.a(gameprofile, 0, this.getGameType(aEg.theWorld), null);
            return aEg.getNetHandler() != null && aEg.getNetHandler().getPlayerInfo(gameprofile.getId()) != null ? null : gameprofile;
        }
        return null;
    }

    public void c(Entity entity) {
        if (entity != null) {
            if (entity.motionX != 0.0 || entity.motionY != 0.0 || entity.motionZ != 0.0) {
                this.a(new S12PacketEntityVelocity(entity), EnumConnectionState.PLAY);
            }
        }
    }

    public void a(Entity entity, EntityPlayer player, Set<Integer> var3, Set<UUID> uuids, List<GameProfile> profiles) {
        if (entity != null && entity != player && entity.isEntityAlive() && var3.add(entity.getEntityId())) {
            if (entity instanceof EntityPlayer entityplayer && this.isValidProfile(entityplayer.getGameProfile()) && uuids.add(entityplayer.getGameProfile().getId())) {
                this.a(entityplayer.getGameProfile(), -94 - -34 - -60, this.getGameType(entity.worldObj), null);
                profiles.add(entityplayer.getGameProfile());
            }

            Packet packet = this.b(entity);
            if (packet != null) {
                this.a(packet, EnumConnectionState.PLAY);
                this.a(new ad(entity.getEntityId(), entity.getDataWatcher(), true), EnumConnectionState.PLAY);
                this.c(entity);
                if (entity instanceof EntityLivingBase entitylivingbase) {
                    this.a(
                        new net.minecraft.network.play.server.aa(entitylivingbase, (byte)(entitylivingbase.getRotationYawHead() * 256.0F / 360.0F)),
                        EnumConnectionState.PLAY
                    );
                    BaseAttributeMap baseattributemap = entitylivingbase.getAttributeMap();
                    if (baseattributemap != null) {
                        this.a(new S20PacketEntityProperties(entity.getEntityId(), baseattributemap.getAllAttributes()), EnumConnectionState.PLAY);
                    }

                    for (int i = 0; i < 5; i++) {
                        if (entitylivingbase.getEquipmentInSlot(i) != null) {
                            this.a(new e(entity.getEntityId(), i, entitylivingbase.getEquipmentInSlot(i)), EnumConnectionState.PLAY);
                        }
                    }

                    Iterator iterator = entitylivingbase.getActivePotionEffects().iterator();

                    while (iterator.hasNext()) {
                        PotionEffect potioneffect = (PotionEffect)iterator.next();
                        this.a(new net.minecraft.network.play.server.ae(entity.getEntityId(), potioneffect), EnumConnectionState.PLAY);
                    }
                }
            }
        }
    }

    public void writeEntry(byte[] var1) {
        if (var1 == null || var1.length == 0) {
            return;
        }

        int currentTimeMillis2 = this.fd >= 0 ? this.fd : (int)(System.currentTimeMillis() - this.startTime);

        try {
            synchronized (this.ex) {
                if (!this.recording || this.output == null || this.buffer == null) {
                    return;
                }

                this.output.writeInt(currentTimeMillis2);
                this.output.writeInt(var1.length);
                this.output.write(var1);
            }
        } catch (IOException ioexception) {
        }

        if (currentTimeMillis2 > this.duration) {
            this.stopRecording();
        }
    }

    public void clearPending() {
        synchronized (this.ey) {
            this.pending.clear();
            this.pendingSize = 0;
        }
    }

    public GameProfile a(UUID uuid, String var2) {
        if (uuid == null) {
            return null;
        }

        GameProfile gameprofile2 = this.profiles.get(uuid);
        if (this.isValidProfile(gameprofile2)) {
            return gameprofile2;
        }

        if (aEg.getNetHandler() != null) {
            NetworkPlayerInfo networkplayerinfo = aEg.getNetHandler().getPlayerInfo(uuid);
            if (networkplayerinfo != null && this.isValidProfile(networkplayerinfo.getGameProfile())) {
                GameProfile gameprofile3 = networkplayerinfo.getGameProfile();
                this.b(gameprofile3);
                return gameprofile3;
            }
        }

        if (aEg.theWorld != null) {
            EntityPlayer entityplayer = aEg.theWorld.getPlayerEntityByUUID(uuid);
            if (entityplayer != null && this.isValidProfile(entityplayer.getGameProfile())) {
                GameProfile gameprofile = entityplayer.getGameProfile();
                this.b(gameprofile);
                return gameprofile;
            }
        }

        String s = this.truncateName(var2 != null ? var2 : this.b(uuid));
        if (s == null) {
            return null;
        }

        GameProfile gameprofile1 = new GameProfile(uuid, s);
        this.b(gameprofile1);
        return gameprofile1;
    }

    public void c(String var1, int var2) {
        if (this.recording) {
            this.stopRecording();
        }

        this.name = var1;
        this.duration = var2 * 1000;
        this.serverIp = aEg.getCurrentServerData() != null ? aEg.getCurrentServerData().serverIP : "unknown";
        this.buffer = new ByteArrayOutputStream();
        this.output = new DataOutputStream(this.buffer);
        this.startTime = System.currentTimeMillis();
        this.recording = true;
        this.eG = !this.canRecord();
        this.eH = this.eG;
        this.eI = this.eH ? System.currentTimeMillis() : 0L;
        this.eK = false;
        this.eL = !this.eG;
        this.clearProfileCache();
        this.eM = false;
        this.selfSpawned = false;
        Arrays.fill(this.lastEquipment, null);
        this.eV = null;
        this.eW = false;
        this.eX = 0;
        boolean flag1;
        if (!this.eG) {
            flag1 = true;
        } else {
            boolean flag = false;
            flag1 = flag;
        }

        this.eJ = flag1;
        if (!this.eG) {
            this.bw();
            this.eJ = false;
        }
    }

    public void a(WorldClient world, EntityPlayer player) {
        long i1 = -5999452370710010159L;
        long j1 = -8132256309763312687L;
        long k1 = 1975227560856581487L;
        HashSet hashset = new HashSet();
        ArrayList arraylist = new ArrayList();

        label100: {
            //add code
            // ChunkProviderClient.chunkListing was bCw in the shipped client, so the lookup
            // below threw on its first instruction and this whole block was skipped -- and the
            // block is not an if/else with the loop underneath, it is an addition to it. Skipped,
            // the recording is the render-distance square and nothing else, which is what every
            // shipped build produced. Entered, it is every loaded chunk plus that square, which
            // is what the author wrote first and plainly meant. Off by default; the same
            // break label100 the shipped catch took, so nothing in here runs at all.
            if (!RevivedPaths.replayAllLoadedChunks()) {
                break label100;
            }

            label95: {
                try {
                    if (this.eA != null) {
                        break label95;
                    }
                } catch (Exception exception8) {
                    break label100;
                }

                try {
                    this.eA = world.getChunkProvider().getClass().getDeclaredField("chunkListing");
                    this.eA.setAccessible(true);
                } catch (Exception exception7) {
                    break label100;
                }
            }

            List list;
            try {
                list = (List)this.eA.get(world.getChunkProvider());
                if (list == null) {
                    break label100;
                }
            } catch (Exception exception6) {
                break label100;
            }

            Iterator iterator;
            try {
                iterator = list.iterator();
            } catch (Exception exception3) {
                break label100;
            }

            while (true) {
                try {
                    if (!iterator.hasNext()) {
                        break;
                    }
                } catch (Exception exception2) {
                    break;
                }

                Chunk chunk;
                try {
                    chunk = (Chunk)iterator.next();
                    if (chunk == null) {
                        continue;
                    }
                } catch (Exception exception5) {
                    break;
                }

                try {
                    if (!hashset.add((long)chunk.xPosition << 32 ^ chunk.zPosition & 4294967295L)) {
                        continue;
                    }
                } catch (Exception exception4) {
                    break;
                }

                try {
                    arraylist.add(chunk);
                } catch (Exception exception1) {
                    break;
                }
            }
        }

        while (true) {
            long j2 = (long)Math.min(aEg.gameSettings.renderDistanceChunks + 1, 8) << 32;
            long i2 = j1;
            j2 ^= j1;

            try {
                j1 = i2 ^ j2 & -1L << 32;

                for (i1 ^= ((long)(player.chunkCoordX - (int)(j1 >>> 32)) << 32 ^ i1) & -1L << 32;
                    (int)(i1 >>> 32) <= player.chunkCoordX + (int)(j1 >>> 32);
                    i1 += 4294967296L
                ) {
                    for (k1 ^= ((long)(player.chunkCoordZ - (int)(j1 >>> 32)) << 32 ^ k1) & -1L << 32;
                        (int)(k1 >>> 32) <= player.chunkCoordZ + (int)(j1 >>> 32);
                        k1 += 4294967296L
                    ) {
                        long l1 = (long)((int)(i1 >>> 32)) << 32 ^ (int)(k1 >>> 32) & 4294967295L;
                        if (hashset.add(l1)) {
                            Chunk chunk1 = world.getChunkFromChunkCoords((int)(i1 >>> 32), (int)(k1 >>> 32));
                            if (chunk1 != null) {
                                arraylist.add(chunk1);
                            }
                        }
                    }
                }

                if (arraylist.isEmpty()) {
                    return;
                }

                i1 ^= (274877906944L ^ i1) & -1L << 32;

                for (k1 ^= (0L ^ k1) & -1L << 32; (int)(k1 >>> 32) < arraylist.size(); k1 += 274877906944L) {
                    k1 ^= (Math.min((int)(k1 >>> 32) + 64, arraylist.size()) ^ k1) & -1L >>> 32;
                    this.a(new S26PacketMapChunkBulk(arraylist.subList((int)(k1 >>> 32), (int)k1)), EnumConnectionState.PLAY);
                }

                return;
            } catch (Exception exception) {
            }
        }
    }

    public ReplayRecordingComponent() {
        this.pending = new ArrayList<>();
        this.seenPlayers = new HashSet<>();
        this.profiles = new HashMap<>();
        this.lastEquipment = new ItemStack[5];
        this.fd = -1;
        this.fg = var1 -> {
            if (var1.dd() instanceof rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStartRecording) {
                rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStartRecording a = (rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStartRecording)var1.dd();
                this.c(a.ajm(), a.aJV());
            } else if (var1.dd() instanceof S2CPacketStopRecording) {
                S2CPacketStopRecording h = (S2CPacketStopRecording)var1.dd();
                if (this.recording && h.ajm().equals(this.name)) {
                    this.stopRecording();
                }
            }
        };
        this.onTick = var1 -> {
            if (this.eG && this.canRecord() && this.eH && System.currentTimeMillis() - this.eI > 1500L) {
                this.eJ = true;
                this.bw();
                this.eJ = false;
                this.eH = false;
                this.eG = false;
                this.eK = false;
                this.eL = true;
            }

            if (this.recording && aEg.thePlayer != null) {
                if ("unknown".equals(this.serverIp) && aEg.getCurrentServerData() != null && aEg.getCurrentServerData().serverIP != null) {
                    this.serverIp = aEg.getCurrentServerData().serverIP;
                }

                if (this.canRecord() && !this.eM) {
                    this.bB();
                }

                this.bC();
                this.bD();
            }
        };
        this.onWorldChange = var1 -> {
            this.clearPending();
            this.clearProfileCache();
            if (this.recording) {
                this.eM = false;
                this.selfSpawned = false;
                Arrays.fill(this.lastEquipment, null);
                this.eV = null;
                this.eW = false;
                this.eX = 0;
            }
        };
    }

    public void a(Packet<?> packet, EnumConnectionState enumConnectionState, byte[] var3) {
        this.b(packet, enumConnectionState, var3);
        if (this.recording && enumConnectionState != null && !this.eJ) {
            if (this.eH) {
                if (enumConnectionState != EnumConnectionState.LOGIN || !(packet instanceof S02PacketLoginSuccess)) {
                    return;
                }

                this.eH = false;
                this.eL = true;
                synchronized (this.ex) {
                    if (this.buffer != null && this.buffer.size() == 0) {
                        this.startTime = System.currentTimeMillis();
                    }
                }
            }

            if (enumConnectionState == EnumConnectionState.LOGIN || enumConnectionState == EnumConnectionState.PLAY) {
                if (enumConnectionState != EnumConnectionState.LOGIN || packet instanceof S02PacketLoginSuccess) {
                    if (enumConnectionState == EnumConnectionState.PLAY && packet instanceof S01PacketJoinGame) {
                        this.eM = false;
                        this.selfSpawned = false;
                        Arrays.fill(this.lastEquipment, null);
                        this.eV = null;
                        this.eW = false;
                        this.eX = 0;
                    }

                    if (packet instanceof net.minecraft.network.play.server.n
                        && aEg.thePlayer != null
                        && aEg.thePlayer.getGameProfile() != null
                        && ((net.minecraft.network.play.server.n)packet).agh().equals(aEg.thePlayer.getGameProfile().getId())) {
                        this.eM = true;
                    }

                    List list = this.c(packet, enumConnectionState);
                    if (var3 != null && var3.length > 0) {
                        this.writeEntry(var3);
                    } else {
                        this.a(packet, enumConnectionState);
                    }

                    this.d(packet, enumConnectionState);
                    this.b(list);
                }
            }
        }
    }

    public String truncateName(String var1) {
        if (var1 != null && !var1.isEmpty()) {
            return var1.length() > 16 ? var1.substring(0, 16) : var1;
        }
        return null;
    }

    public S01PacketJoinGame buildJoinGamePacket() {
        WorldClient worldclient = aEg.theWorld;
        NetHandlerPlayClient nethandlerplayclient = aEg.getNetHandler();
        EntityPlayerSP entityplayersp = aEg.thePlayer;
        return new S01PacketJoinGame(
            entityplayersp.getEntityId(),
            aEg.playerController != null ? aEg.playerController.getCurrentGameType() : worldclient.getWorldInfo().getGameType(),
            worldclient.getWorldInfo().isHardcoreModeEnabled(),
            entityplayersp.dimension,
            worldclient.getDifficulty(),
            nethandlerplayclient.currentServerMaxPlayers,
            worldclient.getWorldType(),
            false
        );
    }

    public String b(UUID uuid) {
        String s = uuid.toString().replace("-", "");
        String s1 = s.substring(0, Math.min(14, s.length()));
        return "p_" + s1;
    }

    public boolean isValidProfile(GameProfile profile) {
        return profile != null && profile.getId() != null && profile.getName() != null;
    }

    public void a(WorldClient world, NetHandlerPlayClient netHandlerPlayClient, Set<UUID> uuids) {
        if (netHandlerPlayClient != null) {
            GameType gametype = this.getGameType(world);
            Iterator iterator = netHandlerPlayClient.getPlayerInfoMap().iterator();

            while (iterator.hasNext()) {
                NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo)iterator.next();
                if (networkplayerinfo != null && this.isValidProfile(networkplayerinfo.getGameProfile()) && uuids.add(networkplayerinfo.getGameProfile().getId())) {
                    this.a(
                        networkplayerinfo.getGameProfile(),
                        networkplayerinfo.getResponseTime(),
                        networkplayerinfo.getGameType() != null ? networkplayerinfo.getGameType() : gametype,
                        networkplayerinfo.getDisplayName()
                    );
                }
            }

            if (aEg.thePlayer != null && this.isValidProfile(aEg.thePlayer.getGameProfile()) && uuids.add(aEg.thePlayer.getGameProfile().getId())) {
                this.a(aEg.thePlayer.getGameProfile(), 73 + -73, gametype, null);
            }
        }
    }
}
