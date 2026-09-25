package rip.vantage.network.handler;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.impl.render.ClickGUI;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.ColorValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.ListValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.interfaces.ExecutorAccess;
import com.alan.clients.util.localization.Locale;
import com.alan.clients.util.localization.Localization;
import com.alan.clients.util.vantage.MachineFingerprint;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import java.awt.Color;
import java.util.HashMap;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.Session;
import rip.vantage.commons.packet.impl.client.protection.C2SPacketConfirmServer;
import rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStopRecording;
import rip.vantage.commons.packet.impl.server.protection.S2CPacketServerJoin;
import rip.vantage.commons.packet.impl.server.protection.S2CPacketConfig;
import rip.vantage.commons.packet.impl.server.protection.S2CPacketProofOfWorkChallenge;
import rip.vantage.security.IntegrityGuard;

public final class BackendPacketHandler implements rip.vantage.commons.handler.api.S2CPacketHandler {
    private final rip.vantage.commons.util.time.StopWatch eRz = new rip.vantage.commons.util.time.StopWatch();

    public BackendPacketHandler() {
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketAuthentication packet) {
        String s = packet.getExpectedHwid();
        if (s != null && !s.isEmpty()) {
            String s1 = MachineFingerprint.vW();
            if (!IntegrityGuard.aL(s1, s)) {
                System.out.println("EC57");
                rip.vantage.util.NativeBridge.a(packet, boolean.class, false);
                rip.vantage.util.NativeBridge.kQ(s1);
            }
        }

        long i = IntegrityGuard.a(packet, packet.isSuccess(), packet.getB(), packet.getC(), packet.getD());
        if (!IntegrityGuard.V(i)) {
            System.out.println("EC74");
            rip.vantage.util.NativeBridge.kF(1);
            rip.vantage.util.NativeBridge.kG(1);
            throw new SecurityException("EC74");
        }

        Client.a.e().d(new BackendS2CEvent(packet));
        this.eRz.aX();
        NetHandlerPlayClient nethandlerplayclient = InstanceAccess.aEg.getNetHandler();
        if (nethandlerplayclient != null) {
            NetworkManager networkmanager = nethandlerplayclient.getNetworkManager();
            String s2 = networkmanager.getRemoteAddress().toString().split(":")[0];
            int j = Integer.parseInt(networkmanager.getRemoteAddress().toString().split(":")[1]);
            rip.vantage.network.core.VantageNetwork.aKB().aKK().sendMessage(new C2SPacketConfirmServer(s2, j, InstanceAccess.aEg.getSession().getUsername()).aJk());
        }
    }

    @Override
    public void handle(S2CPacketConfig packet) {
        ExecutorAccess.aMR
            .execute(
                () -> {
                    HashMap hashmap = new HashMap();

                    for (Module module : Client.a.g().getAll()) {
                        module.setEnabled(false);
                        String s = module.getModuleInfo().aliases()[0];
                        hashmap.put(s, module);

                        for (Locale locale : Locale.values()) {
                            String s1 = Localization.a(s, locale);
                            if (s1 != null && !s1.isEmpty() && !s1.equals(s)) {
                                hashmap.putIfAbsent(s1, module);
                            }
                        }

                        String[] astring = rip.vantage.util.NativeBridge.n(module);
                        if (astring != null) {
                            for (String s2 : astring) {
                                if (s2 != null && !s2.isEmpty()) {
                                    hashmap.putIfAbsent(s2, module);
                                }
                            }
                        }

                        for (Value value : module.getAllValues()) {
                            value.setValueAsObject(value.getDefaultValue());
                        }
                    }

                    boolean flag = false;

                    for (String s3 : packet.getConfig().split("\n")) {
                        if (!flag) {
                            String[] astring1 = s3.split("th_");
                            if (astring1.length > 1) {
                                rip.vantage.util.NativeBridge.a(() -> Client.a.getThemeManager().a(Themes.valueOf(astring1[1])));
                            }

                            flag = true;
                        } else {
                            String s4 = s3.split("_")[0].trim();
                            if (hashmap.containsKey(s4)) {
                                Module module1 = (Module)hashmap.get(s4);
                                String s5 = s3.split("_")[1].split("_")[0];
                                String s6 = s5;
                                String s7;
                                int i;
                                switch (s6) {
                                    case "e1":
                                    case "kc":
                                        continue;
                                    default:
                                        s7 = s3.split("_" + Pattern.quote(s5) + "_")[1].split("_")[0];
                                        i = 0;
                                }

                                for (Value value1 : module1.getAllValues()) {
                                    i++;
                                    String s8 = value1.getName()
                                        + " in "
                                        + (
                                            value1.wq() != null
                                                ? (
                                                    value1.wq() instanceof Module
                                                        ? ((Module)value1.wq()).getModuleInfo().aliases()[0] + " Module"
                                                        : ((Mode)value1.wq()).getName() + " Mode"
                                                )
                                                : "Unknown"
                                        );
                                    if (s5.contains("*")) {
                                        s8 = value1.getName() + "*" + i;
                                    }

                                    if (s8.equalsIgnoreCase(s5)) {
                                        if (value1 instanceof ModeValue modevalue) {
                                            modevalue.co(s3.split("_" + Pattern.quote(s7) + "_")[1]);
                                        } else if (value1 instanceof BooleanValue booleanvalue) {
                                            booleanvalue.setValue(Boolean.parseBoolean(s3.split("_" + Pattern.quote(s7) + "_")[1]));
                                        } else if (value1 instanceof StringValue stringvalue) {
                                            if (s3.contains("_" + Pattern.quote(s7) + "_")) {
                                                stringvalue.n(s3.split("_" + Pattern.quote(s7) + "_")[1].replaceAll("<percentsign>", "%"));
                                            }
                                        } else if (value1 instanceof NumberValue numbervalue) {
                                            numbervalue.n(Double.parseDouble(s3.split("_" + Pattern.quote(s7) + "_")[1]));
                                        } else if (value1 instanceof BoundsNumberValue boundsnumbervalue) {
                                            double d0;
                                            {
                                                d0 = Double.parseDouble(s3.split("_" + Pattern.quote(s7) + "_")[1]);
                                                String s9 = s7;
                                                switch (s9) {
                                                    case "first":
                                                        boundsnumbervalue.n(d0);
                                                        continue;
                                                    case "second":
                                                        break;
                                                    default:
                                                        continue;
                                                }
                                            }

                                            boundsnumbervalue.a(d0);
                                        } else if (value1 instanceof ColorValue colorvalue) {
                                            Color color;
                                            int j;
                                            label220: {
                                                label219: {
                                                    {
                                                        color = colorvalue.wo();
                                                        j = Integer.parseInt(s3.split("_" + Pattern.quote(s7) + "_")[1]);
                                                        String s10 = s7;
                                                        switch (s10) {
                                                            case "red":
                                                                colorvalue.n(new Color(j, color.getGreen(), color.getBlue(), color.getAlpha()));
                                                                continue;
                                                            case "green":
                                                                break label220;
                                                            case "blue":
                                                                break label219;
                                                            case "alpha":
                                                                break;
                                                            default:
                                                                continue;
                                                        }
                                                    }

                                                    colorvalue.n(new Color(color.getRed(), color.getGreen(), color.getBlue(), j));
                                                    continue;
                                                }

                                                colorvalue.n(new Color(color.getRed(), color.getGreen(), j, color.getAlpha()));
                                                continue;
                                            }

                                            colorvalue.n(new Color(color.getRed(), j, color.getBlue(), color.getAlpha()));
                                        } else if (!(value1 instanceof DragValue dragvalue)) {
                                            if (value1 instanceof ListValue listvalue) {
                                                for (Object object : listvalue.getModes()) {
                                                    if (object.toString().equalsIgnoreCase(s3.split("_" + Pattern.quote(s7) + "_")[1])) {
                                                        listvalue.setValueAsObject(object);
                                                    }
                                                }
                                            }
                                        } else {
                                            double d1;
                                            label241: {
                                                label240: {
                                                    {
                                                        d1 = Double.parseDouble(s3.split("_" + Pattern.quote(s7) + "_")[1]);
                                                        String s11 = s7;
                                                        switch (s11) {
                                                            case "positionX":
                                                                dragvalue.h(new Vector2d(d1, dragvalue.apP.y));
                                                                dragvalue.i(new Vector2d(d1, dragvalue.atg.y));
                                                                continue;
                                                            case "positionY":
                                                                break label241;
                                                            case "scaleX":
                                                                break label240;
                                                            case "scaleY":
                                                                break;
                                                            default:
                                                                continue;
                                                        }
                                                    }

                                                    dragvalue.n(new Vector2d(dragvalue.aHe.x, d1));
                                                    continue;
                                                }

                                                dragvalue.n(new Vector2d(d1, dragvalue.aHe.y));
                                                continue;
                                            }

                                            dragvalue.h(new Vector2d(dragvalue.apP.x, d1));
                                            dragvalue.i(new Vector2d(dragvalue.atg.x, d1));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (String s12 : packet.getConfig().split("\n")) {
                        String s13 = s12.split("_")[0];
                        if (hashmap.containsKey(s13)) {
                            Module module2;
                            {
                                module2 = (Module)hashmap.get(s13);
                                String s14 = s12.split("_")[1].split("_")[0];
                                String s15 = s14;
                                switch (s15) {
                                    case "e1":
                                        if (!(module2 instanceof ClickGUI)) {
                                            module2.setEnabled(Boolean.parseBoolean(s12.split("_e1_")[1]));
                                        }
                                        continue;
                                    case "kc":
                                        break;
                                    default:
                                        continue;
                                }
                            }

                            module2.setKey(Integer.parseInt(s12.split("_kc_")[1]));
                        }
                    }
                }
            );
    }

    @Override
    public void handle(S2CPacketServerJoin packet) {
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketAccount packet) {
        Minecraft.getMinecraft().session = new Session(packet.getUsername(), packet.getUuid(), packet.getAccessToken(), "microsoft");
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.management.S2CPacketHudRefresh packet) {
        Minecraft.getMinecraft().ingameGUI.lastSystemTime = -50L;
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketChatMessage packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketUserData packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketEntityListRequest packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketTitle packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketConfigList packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.community.S2CPacketTroll var1) {
        Client.a.e().d(new BackendS2CEvent(var1));
    }

    public void handle(rip.vantage.commons.packet.impl.server.general.S2CPacketKeepAlive var1) {
        BackendWebSocket.eRC.aX();
        this.eRz.aX();
    }

    @Override
    public void handle(S2CPacketProofOfWorkChallenge packet) {
        byte[] abyte = packet.getChallenge();
        if (abyte != null && abyte.length == 32) {
            long i = packet.getTimestamp();
            if (i > 0L && Math.abs(System.currentTimeMillis() - i) > 60000L) {
                System.out.println("EC152");
            }

            if (!IntegrityGuard.aMs()) {
                System.out.println("EC40 - ProofOfWork integrity failed");
                rip.vantage.util.NativeBridge.kF(1);
                rip.vantage.util.NativeBridge.kG(1);
                throw new SecurityException("EC40");
            }

            rip.vantage.network.core.VantageNetwork.aKB().n(abyte);
        } else {
            System.out.println("EC151");
            rip.vantage.util.NativeBridge.kF(1);
            rip.vantage.util.NativeBridge.kG(1);
            throw new SecurityException("EC151");
        }
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketStartRecording packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(S2CPacketStopRecording packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketCaptureRequest packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketCaptureCancel packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void b(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorConsent packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void b(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorRequest packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void b(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorCommand packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.monitoring.S2CPacketMonitorPing packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketKeyShare var1) {
    }

    @Override
    public void handle(rip.vantage.commons.packet.impl.server.protection.S2CPacketJdkUnlockGrant packet) {
        Client.a.e().d(new BackendS2CEvent(packet));
    }
}
