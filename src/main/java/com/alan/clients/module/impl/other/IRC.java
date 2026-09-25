package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.ChatInputEvent;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.StringUtils;
import rip.vantage.commons.packet.impl.client.community.C2SPacketChatMessage;
import rip.vantage.commons.util.vantage.VantageClient;

@ModuleInfo(aliases={"module.other.irc.name"}, description="module.other.irc.description", category=Category.RENDER, autoEnabled=true)
public final class IRC
extends Module {
    private static final String CHAT_PREFIX = "[Rise] ";
    private static final String[] IMAGE_EXTENSIONS = new String[]{".png", ".jpg", ".jpeg", ".gif", ".webp", ".bmp"};
    @EventLink
    public final Listener<ChatInputEvent> onChatInput = chatInputEvent -> {
        String string = chatInputEvent.getMessage();
        if (string.startsWith("#") && string.length() > 1) {
            chatInputEvent.setCancelled();
            String string2 = StringUtils.normalizeSpace(string.substring(1));
            rip.vantage.network.core.VantageNetwork.aKB().aKK().sendMessage(new C2SPacketChatMessage(string2).aJk());
        }
    };
    @EventLink
    public final Listener<BackendS2CEvent> onVantagePacketReceive = er2 -> {
        if (!(er2.dd() instanceof rip.vantage.commons.packet.impl.server.community.S2CPacketChatMessage)) {
            return;
        }
        rip.vantage.commons.packet.impl.server.community.S2CPacketChatMessage b2 = (rip.vantage.commons.packet.impl.server.community.S2CPacketChatMessage)er2.dd();
        VantageClient unused0 = VantageClient.values()[b2.getProduct()];
        String string = b2.getMessage();
        String string2 = "\u00a7" + b2.getAuthor();
        String string3 = String.valueOf(EnumChatFormatting.GRAY) + string;
        if (this.isTruncatedAttachment(string)) {
            ChatUtil.d(ChatUtil.getPrefix() + String.valueOf(EnumChatFormatting.RED) + "Discord attachment URL arrived truncated before the client. The bridge/backend must send the full link.", new Object[0]);
        }
        Object object = Math.random() > 0.9 ? String.valueOf(EnumChatFormatting.GRAY) + " Start your msg with # to chat" : "";
        ChatUtil.d(CHAT_PREFIX + string2 + String.valueOf(EnumChatFormatting.GRAY) + ": " + string3 + (String)object, new Object[0]);
    };

    private boolean isTruncatedAttachment(String string) {
        if (string == null) {
            return false;
        }
        String string2 = string.toLowerCase();
        if (!string2.contains("discordapp.com/attachments/") && !string2.contains("discord.com/attachments/")) {
            return false;
        }
        for (String string3 : IMAGE_EXTENSIONS) {
            if (!string2.contains(string3)) continue;
            return false;
        }
        return true;
    }
}
