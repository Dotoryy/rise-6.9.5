package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public final class Stuck extends Command {
    public Stuck() {
        super("command.stuck.description", "stuck");
    }

    @Override
    public void execute(String[] var1) {
        PacketUtil.sendNoEvent(new C04PacketPlayerPosition(aEg.thePlayer.posX, -1.0, aEg.thePlayer.posZ, false));
        ChatUtil.b("command.stuck.sent");
    }
}
