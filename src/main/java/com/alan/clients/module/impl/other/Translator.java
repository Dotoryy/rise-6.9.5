package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.packet.PacketReceiveEvent;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.SubMode;
import com.alan.clients.util.NetworkUtil;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.c;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.s;
import org.json.JSONArray;

@ModuleInfo(aliases = "module.other.translator.name", description = "Translates your chat, might not work with some VPNs", category = Category.RENDER)
public class Translator extends Module {
    Executor translatorThread = Executors.newFixedThreadPool(1);
    private final ModeValue mode = new ModeValue("Mode", this).add(new SubMode("Delay")).add(new SubMode("Resend")).setDefault("Delay");
    @EventLink
    public final Listener<PacketReceiveEvent> onPacketReceive = var1 -> {
        if (aEg.theWorld != null && aEg.thePlayer != null) {
            Packet packet = var1.getPacket();
            if (packet instanceof c) {
                String s = ((c)packet).getChatComponent().getFormattedText();
                if (s.contains("\n")) {
                    return;
                }

                {
                    String s1 = this.mode.wo().getName();
                    switch (s1) {
                        case "Delay":
                            var1.setCancelled();
                            this.translate(s);
                            return;
                        case "Resend":
                            break;
                        default:
                            return;
                    }
                }

                this.translate(s);
            }
        }
    };

    public Translator() {
    }

    public void translate(String var1) {
        this.translatorThread
            .execute(
                () -> {
                    JSONArray jsonarray = new JSONArray(
                        NetworkUtil.requestLine("https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=en&dt=t&q=" + URLEncoder.encode(var1), "GET")
                    );
                    String sxx = jsonarray.getJSONArray(0).getJSONArray(0).getString(0);
                    s sx = new s(sxx);
                    String s1 = new Locale(jsonarray.getString(2)).getDisplayLanguage(Locale.ENGLISH);
                    if (!sxx.equals(var1)) {
                        sx.appendText(" ");
                        s sxx2 = new s(this.rz().getChatAccentColor() + "[T]");
                        ChatStyle chatstyle = new ChatStyle();
                        chatstyle.setChatHoverEvent(new HoverEvent(Action.SHOW_TEXT, new s("Translated from " + s1 + "\n" + var1)));
                        sxx2.setChatStyle(chatstyle);
                        sx.appendSibling(sxx2);
                    }

                    aEg.thePlayer.addChatMessage(sx);
                }
            );
    }
}
