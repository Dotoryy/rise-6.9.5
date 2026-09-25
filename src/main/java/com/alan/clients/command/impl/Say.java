package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;

public final class Say extends Command {
    public Say() {
        super("command.say.description", "say", "chat");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length <= 1) {
            this.error(String.format(".%s <message>", var1[0]));
        } else {
            ChatUtil.send(String.join(" ", var1).substring(3).trim());
            ChatUtil.b("command.say.sent");
        }
    }
}
