package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import rip.vantage.commons.packet.impl.client.community.C2SPacketTitle;
import rip.vantage.network.core.VantageNetwork;

public class Title extends Command {
    public Title() {
        super("command.title.description", "title");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length != 7) {
            this.error(".title <message> <fadeInTime> <displayTime> <fadeOutTime> <color> <group/user>");
        } else {
            VantageNetwork.aKB().aKK().sendMessage(new C2SPacketTitle(var1[1], Integer.parseInt(var1[2]), Integer.parseInt(var1[3]), Integer.parseInt(var1[4]), var1[5], var1[6]).aJk());
        }
    }
}
