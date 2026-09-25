package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.player.PlayerUtil;
import net.minecraft.client.gui.GuiScreen;

public final class Name extends Command {
    public Name() {
        super("command.name.description", "name", "ign", "username", "nick", "nickname");
    }

    @Override
    public void execute(String[] var1) {
        String s = PlayerUtil.name();
        GuiScreen.setClipboardString(s);
        ChatUtil.b("command.name.copied", s);
    }
}
