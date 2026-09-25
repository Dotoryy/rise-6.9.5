package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;

public final class Insults extends Command {
    public Insults() {
        super("command.insults.description", "insults", "killinsults", "insult");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length >= 3) {
            String s = var1[1].toLowerCase();
            String s1 = var1[2];
            if (s.equals("create")) {
                this.rN().getInsultManager().set(s1);
                ChatUtil.b("command.insults.created", s1);
            } else if (s.equals("delete")) {
                this.rN().getInsultManager().delete(s1);
                ChatUtil.b("command.insults.removed", s1);
            }
        } else {
            ChatUtil.b("command.insults.help1");
            ChatUtil.b("command.insults.help2");
        }
    }
}
