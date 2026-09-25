package com.alan.clients.component.impl.viamcp;

import com.alan.clients.Client;
import com.alan.clients.compat.ProtectionToggles;
import com.alan.clients.component.Component;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.component.impl.player.LastConnectionComponent;
import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.Breaker;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.MoveInputEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketSendEvent;
import com.alan.clients.util.player.MoveUtil;
import com.viaversion.viabackwards.protocol.v1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viarewind.protocol.v1_9to1_8.Protocol1_9To1_8;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.BlockPosition;
import com.viaversion.viaversion.api.minecraft.item.StructuredItem;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocol.packet.PacketWrapperImpl;
import com.viaversion.viaversion.protocols.v1_16_4to1_17.packet.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.v1_18_2to1_19.packet.ServerboundPackets1_19;
import com.viaversion.viaversion.protocols.v1_8to1_9.packet.ServerboundPackets1_9;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.vialoadingbase.netty.handler.VLBViaDecodeHandler;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.newevent.impl.packet.PacketEncodeEvent;
import java.awt.Container;
import java.awt.Window;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Locale;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.client.m;
import rip.vantage.network.core.VantageNetwork;

public final class ProtocolFixComponent extends Component {
    private boolean iq = false;
    private boolean ir = false;
    private int is = 0;
    private static final int it = 1;
    private static boolean iu = false;
    private static long iv = 0L;
    private static final long iw = 2000L;
    @EventLink(value = 1)
    public final Listener<PacketEncodeEvent> onPacketEncode = var0 -> {};
    @EventLink
    public final Listener<TickEvent> onTick = var1 -> {
        this.is = 0;
        String s = VantageNetwork.aKB().bX();
        s.equals("zaikoyano");
        "true".equals(System.getProperty("rise.lag.active"));
        long i = System.currentTimeMillis();
        //add code
        if (ProtectionToggles.antiCrackScan() && i - iv > 2000L) {
            iu = cC();
            iv = i;
        }

        if (Math.random() > 0.5) {
            ;
        }

        if (aEg.thePlayer.ticksExisted < 10) {
            this.ir = false;
        }

        if (s != null
            && !s.isBlank()
            && (Objects.equals(LastConnectionComponent.port, "35565") || Objects.equals(LastConnectionComponent.ip, "testnet.hypixel.net") || Client.a.getSecurityManager().nN())
            && aEg.thePlayer.ticksExisted > 100
            && (this.e(KillAura.class).isEnabled() || this.e(Speed.class).isEnabled() || this.e(Scaffold.class).isEnabled())) {
            double d0;
            int k = (d0 = Math.random() - 0.001) == 0.0 ? 0 : (d0 < 0.0 ? -1 : 1);
        }

        if (s != null) {
            s.isBlank();
        }

        if ((s == null || s.isBlank()) && aEg.thePlayer.ticksExisted % 100 == 0 && this.ir && !this.e(Speed.class).isEnabled()) {
            ;
        }

        double d1;
        int j = (d1 = Math.random() - 1.0E-5) == 0.0 ? 0 : (d1 < 0.0 ? -1 : 1);
    };
    @EventLink
    public final Listener<PacketSendEvent> onPacketSend = var1 -> {
        String s = VantageNetwork.aKB().bX();
        if (Breaker.targetBlock == null && !this.e(KillAura.class).isEnabled()) {
            KillAura.inReach = false;
        }

        if (ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_8)) {
            if (aEg.isSingleplayer()) {
                return;
            }

            UserConnection userconnection = Via.getManager().getConnectionManager().getConnections().iterator().next();
            ProtocolInfo protocolinfo = userconnection.getProtocolInfo();
            if (protocolinfo.getPipeline().contains(Protocol1_9To1_8.class)) {
                PacketWrapper packetwrapper = PacketWrapper.create(ServerboundPackets1_9.SWING, userconnection);
                packetwrapper.write(Types.VAR_INT, 0);
                Packet packet = var1.dq();
                if ((packet instanceof m && s != null && !s.isBlank() || packet instanceof m && Math.random() < 0.2) && !BlinkComponent.enabled) {
                    var1.setCancelled();
                    packetwrapper.sendToServer(Protocol1_9To1_8.class);
                    return;
                }
            } else {
                ChatUtil.b("Â§cConnection does not contain Protocol1_8To1_9.");
            }

            if (var1.dq() instanceof C0EPacketClickWindow c0epacketclickwindow
                && c0epacketclickwindow.getMode() < 5
                && ViaLoadingBase.getInstance().getTargetVersion().newerThan(ProtocolVersion.v1_18_2)
                && (!this.e(Disabler.class).isGrimInventoryMove() || aEg.currentScreen instanceof GuiChest || aEg.currentScreen instanceof GuiInventory)) {
                Class<Protocol1_19To1_18_2> oclass = Protocol1_19To1_18_2.class;
                ProtocolVersion protocolversion = ViaLoadingBase.getInstance().getTargetVersion();
                PacketWrapperImpl packetwrapperimpl = (PacketWrapperImpl)PacketWrapper.create(ServerboundPackets1_19.CONTAINER_CLICK, userconnection);
                int i = c0epacketclickwindow.getWindowId();
                int j = c0epacketclickwindow.getSlotId();
                int k = c0epacketclickwindow.getUsedButton();
                int l = c0epacketclickwindow.getMode();
                if (j < -1 || k < 0 || l < 0 || l > 6) {
                    return;
                }

                if (c0epacketclickwindow == null || protocolinfo == null || userconnection == null) {
                    return;
                }

                var1.setCancelled(true);
                int i1 = VLBViaDecodeHandler.stateId;
                if (i1 < 0) {
                    i1 = 0;
                }

                if (j == -1) {
                    j = -999;
                }

                if (l < 0 || l > 6) {
                    l = 0;
                }

                if (k < 0) {
                    k = 0;
                }

                packetwrapperimpl.write(Types.BYTE, (byte)i);
                packetwrapperimpl.write(Types.VAR_INT, i1);
                packetwrapperimpl.write(Types.SHORT, (short)j);
                packetwrapperimpl.write(Types.BYTE, (byte)k);
                packetwrapperimpl.write(Types.VAR_INT, l);
                packetwrapperimpl.write(Types.VAR_INT, 0);
                StructuredItem structureditem = new StructuredItem(0, 0, null);
                packetwrapperimpl.write(Types.ITEM1_13_2, structureditem);
                if (protocolinfo.getPipeline().contains(oclass)) {
                    packetwrapperimpl.sendToServer(oclass);
                }
            }
        }

        if (ViaLoadingBase.getInstance().getTargetVersion().newerThanOrEqualTo(ProtocolVersion.v1_17)) {
            UserConnection userconnection1 = Via.getManager().getConnectionManager().getConnections().iterator().next();
            if (var1.dq() instanceof C07PacketPlayerDigging c07packetplayerdigging
                && (c07packetplayerdigging.getStatus() == Action.START_DESTROY_BLOCK || c07packetplayerdigging.getStatus() == Action.STOP_DESTROY_BLOCK)) {
                PacketWrapper packetwrapper1 = PacketWrapper.create(ServerboundPackets1_17.PLAYER_ACTION, userconnection1);
                WorldClient worldclient = Minecraft.getMinecraft().theWorld;
                int j1 = Minecraft.getMinecraft().theWorld.GZ();
                packetwrapper1.write(Types.VAR_INT, c07packetplayerdigging.getStatus().ordinal());
                packetwrapper1.write(
                    Types.BLOCK_POSITION1_14,
                    new BlockPosition(
                        c07packetplayerdigging.getPosition().getX(), c07packetplayerdigging.getPosition().getY(), c07packetplayerdigging.getPosition().getZ()
                    )
                );
                packetwrapper1.write(Types.UNSIGNED_BYTE, (short)c07packetplayerdigging.getFacing().getIndex());
                packetwrapper1.write(Types.VAR_INT, j1);
            }
        }
    };
    @EventLink
    public final Listener<MoveInputEvent> onMoveInput = var0 -> MoveUtil.enoughMovementForSprinting();
    @EventLink(value = 1)
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (cB() && aEg.thePlayer != null) {
            ;
        }
    };

    public ProtocolFixComponent() {
    }

    private static boolean cB() {
        return false;
    }

    private static boolean cC() {
        try {
            String[] astring = new String[]{
                "cracked",
                "crack",
                "qreaj",
                "patcher",
                "discord.gg",
                "ws://127.0.0.1",
                "ws://localhost",
                "bypassed",
                "keygen",
                "CopeHarder",
                "premium bypass",
                "free version",
                "patched by",
                "nulled",
                "leaked",
                "cracker",
                "encryption key",
                "reis free",
                "billionare bypass",
                "cooking agent"
            };
            Window[] awindow = Window.getWindows();
            //add code
            if (ProtectionToggles.nameProbes() && awindow != null && awindow.length > 0) {
                for (Window window : awindow) {
                    if (window != null) {
                        try {
                            String s = window.getName();
                            if (s != null) {
                                String s1 = s.toLowerCase(Locale.ROOT);

                                for (String s2 : astring) {
                                    if (s1.contains(s2.toLowerCase(Locale.ROOT))) {
                                        return true;
                                    }
                                }
                            }

                            if (window.getClass().getName().contains("Dialog") && a(window.getComponents(), astring)) {
                                ChatUtil.c("s");
                                return true;
                            }
                        } catch (Throwable throwable) {
                        }
                    }
                }
            }

            if (System.getProperty("crack.version") != null || System.getProperty("qreaj.loaded") != null || System.getProperty("bypass.enabled") != null) {
                ChatUtil.c("s");
                return true;
            }

            StackTraceElement[] astacktraceelement = Thread.currentThread().getStackTrace();

            //add code
            if (ProtectionToggles.nameProbes()) {
                for (StackTraceElement stacktraceelement : astacktraceelement) {
                    String s3 = stacktraceelement.getClassName().toLowerCase(Locale.ROOT);
                    if ((
                            s3.contains("crack")
                                || s3.contains("qreaj")
                                || s3.contains("copeharder")
                                || s3.contains("bypass")
                                || s3.contains("agentthatcooks")
                                || s3.contains("cookingagent")
                                || s3.contains("riseagenttransformer")
                                || s3.contains("reisutil")
                                || s3.contains("cc.fish")
                        )
                        && !s3.startsWith("com.alan.clients")
                        && !s3.startsWith("rip.vantage")) {
                        return true;
                    }
                }
            }

            try {
                String s4 = System.getProperty("sun.java.command", "");
                String s5 = System.getProperty("java.class.path", "");
                if (s4.contains("idea_rt.jar") || s5.contains("idea_rt.jar")) {
                    boolean flag = System.getProperty("idea.config.path") != null
                        || System.getProperty("idea.system.path") != null
                        || System.getProperty("idea.plugins.path") != null;
                    if (!flag) {
                        return true;
                    }
                }
            } catch (Throwable throwable3) {
            }

            try {
                //add code
                if (ProtectionToggles.nameProbes()) {
                    for (Thread thread : Thread.getAllStackTraces().keySet()) {
                        String s6 = thread.getName();
                        if (s6 != null && s6.equals("agent")) {
                            return true;
                        }
                    }
                }
            } catch (Throwable throwable2) {
            }

            try {
                RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();

                for (String s7 : runtimemxbean.getInputArguments()) {
                    if (s7.startsWith("-javaagent:")) {
                        String s8 = s7.substring("-javaagent:".length()).toLowerCase(Locale.ROOT);
                        if (s8.contains("crack") || s8.contains("patch") || s8.contains("bypass")) {
                            return true;
                        }

                        if (s8.contains("idea_rt.jar")) {
                            boolean flag1 = System.getProperty("idea.config.path") != null || System.getProperty("idea.system.path") != null;
                            if (!flag1) {
                                return true;
                            }
                        }
                    }
                }
            } catch (Throwable throwable1) {
            }
        } catch (Throwable throwable4) {
        }

        return false;
    }

    private static boolean a(java.awt.Component[] components, String[] var1) {
        if (components != null && components.length != 0) {
            try {
                for (java.awt.Component component : components) {
                    if (component != null) {
                        try {
                            String s = (String)component.getClass().getMethod("getText").invoke(component);
                            if (s != null) {
                                String s1 = s.toLowerCase(Locale.ROOT);

                                for (String s2 : var1) {
                                    if (s1.contains(s2.toLowerCase(Locale.ROOT))) {
                                        return true;
                                    }
                                }

                                if (s1.matches(".*ws://127\\.0\\.0\\.1.*") || s1.matches(".*ws://localhost.*")) {
                                    return true;
                                }
                            }
                        } catch (NoSuchMethodException nosuchmethodexception) {
                        }

                        try {
                            if (component instanceof Container && a(((Container)component).getComponents(), var1)) {
                                return true;
                            }
                        } catch (Throwable throwable) {
                        }
                    }
                }
            } catch (Throwable throwable1) {
            }

            return false;
        }
        return false;
    }
}
