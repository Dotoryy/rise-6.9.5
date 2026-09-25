package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.social.FriendManager;
import net.minecraft.entity.player.EntityPlayer;

public final class Target extends Command {
    public Target() {
        super("command.target.description", "target", "settarget");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length != 3) {
            this.error(".target <add/remove> <player>");
        } else {
            String s = var1[1].toLowerCase();
            String s1 = var1[2];
            boolean flag = false;

            label36:
            for (EntityPlayer entityplayer : aEg.theWorld.playerEntities) {
                if (entityplayer.getName().equalsIgnoreCase(s1)) {
                    {
                        String s2 = s;
                        switch (s2) {
                            case "add":
                                FriendManager.l(entityplayer.getName());
                                ChatUtil.b(String.format("Added %s to target list", s1));
                                flag = true;
                                break label36;
                            case "remove":
                                break;
                            default:
                                break label36;
                        }
                    }

                    FriendManager.m(entityplayer.getName());
                    ChatUtil.b(String.format("Removed %s from target list", s1));
                    flag = true;
                    break;
                }
            }

            if (!flag) {
                ChatUtil.b("That user could not be found.");
            }
        }
    }
}
