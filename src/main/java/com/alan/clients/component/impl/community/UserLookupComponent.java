package com.alan.clients.component.impl.community;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.ServerJoinEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.s;
import org.json.JSONArray;
import org.json.JSONObject;
import rip.vantage.commons.packet.impl.client.community.C2SPacketUserLookup;
import rip.vantage.commons.packet.impl.server.community.S2CPacketUserData;
import rip.vantage.network.core.VantageNetwork;

public class UserLookupComponent extends Component implements InstanceAccess {
    public static HashMap<String, ClientUser> dc = new HashMap<>();
    public static List<String> known = new ArrayList<>();
    public static List<String> pending = new ArrayList<>();
    @EventLink
    public final Listener<ServerJoinEvent> onServerJoin = var0 -> {
        dc = new HashMap<>();
        known = new ArrayList<>();
    };
    @EventLink
    public final Listener<TickEvent> onTick = var0 -> {
        CapeComponent.loadTextures();
        CapeComponent.updateFrames();

        for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
            if (!known.contains(entityplayer.getGameProfile().getName())) {
                known.add(entityplayer.getGameProfile().getName());
                pending.add(entityplayer.getGameProfile().getName());
            }
        }

        aZ();
    };
    @EventLink
    public final Listener<BackendS2CEvent> dh = var0 -> {
        if (var0.dd() instanceof S2CPacketUserData) {
            String s = ((S2CPacketUserData)var0.dd()).getMessage();
            JSONObject jsonobject = new JSONObject(s);

            for (String s1 : jsonobject.keySet()) {
                JSONObject jsonobject1 = new JSONObject(jsonobject.get(s1).toString());
                String s2 = jsonobject1.getString("f");
                ClientUser by = new ClientUser(s1);
                if (!s2.equals("") && !s2.equals(" ")) {
                    by.setTag(s2);
                    by.setRank(UserRank.Regular);
                    if (jsonobject1.getBoolean("h")) {
                        by.setRank(UserRank.Developer);
                    }

                    if (jsonobject1.getBoolean("g")) {
                        by.setRank(UserRank.Admin);
                    }

                    if (jsonobject1.getBoolean("i")) {
                        by.setRank(UserRank.Gato);
                    }

                    JSONArray jsonarray = jsonobject1.getJSONArray("j");
                    String[] astring = new String[jsonarray.length()];

                    for (int i = 0; i < jsonarray.length(); i++) {
                        astring[i] = jsonarray.getString(i);
                    }

                    by.setCapeUrls(astring);
                    dc.put(s1, by);
                }
            }
        }
    };
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var0 -> {
        if (var0.getPacket() instanceof net.minecraft.network.play.server.c c) {
            for (IChatComponent ichatcomponent : new ArrayList<>(c.getChatComponent().getSiblings())) {
                if (ichatcomponent instanceof s) {
                    String s = ichatcomponent.getFormattedText();
                    c.getChatComponent().getSiblings().remove(ichatcomponent);

                    for (Entry entry : dc.entrySet()) {
                        s = c(s, (String)entry.getKey());
                    }

                    if (s.equals(ichatcomponent.getFormattedText())) {
                        c.getChatComponent().getSiblings().add(ichatcomponent);
                    } else {
                        c.getChatComponent().getSiblings().add(new s(s));
                    }
                }
            }

            var0.setPacket(c);
        }
    };

    public UserLookupComponent() {
    }

    public static String c(String var0, String var1) {
        ClientUser by = dc.get(var1);
        if (by != null) {
            String s = " §7(" + var1 + "§7)§r";
            String s1 = "§" + by.getColorCode() + by.getTag() + " §7(";
            if (!var0.contains(s1)) {
                int i = var0.indexOf(var1);
                if (i > 1 && var0.toCharArray()[i - 2] == 167) {
                    s = " §7(§" + var0.toCharArray()[i - 1] + var1 + "§7)§r";
                }

                return var0.replaceAll(var1, "§" + by.getColorCode() + by.getTag() + s);
            }
        } else if (!known.contains(var1)) {
            known.add(var1);
            if (!var1.isBlank()) {
                pending.add(var1);
            }
        }

        return var0;
    }

    public static void aZ() {
        if (!pending.isEmpty() && pending.get(0) != "") {
            StringBuilder stringbuilder = new StringBuilder();
            stringbuilder.append("[");

            for (String s : pending) {
                stringbuilder.append("\"").append(s).append("\"");
            }

            stringbuilder.append("]");
            String s1 = "[\"" + String.join("\", \"", pending) + "\"]";
            VantageNetwork.aKB().aKK().sendMessage(new C2SPacketUserLookup(s1).aJk());
            pending = new ArrayList<>();
        }
    }

    public static String getChatPrefix(String var0, String var1) {
        String s = Client.a.getThemeManager().getTheme().getChatAccentColor().toString();
        return EnumChatFormatting.BOLD + var1 + var0 + EnumChatFormatting.RESET + s + " » " + EnumChatFormatting.RESET;
    }
}
