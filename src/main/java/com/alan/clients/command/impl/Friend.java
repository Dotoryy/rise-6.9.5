package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.social.FriendManager;
import net.minecraft.entity.player.EntityPlayer;

public final class Friend extends Command {
    public Friend() {
        super("command.friend.description", "friend", "setfriend", "f");
    }

    @Override
    public void execute(String[] var1) {
        if (var1.length != 3) {
            this.error(".f <add/remove> <player>");
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
                                FriendManager.j(entityplayer.getName());
                                ChatUtil.b(String.format("Added %s to friends list", s1));
                                flag = true;
                                break label36;
                            case "remove":
                                break;
                            default:
                                break label36;
                        }
                    }

                    FriendManager.k(entityplayer.getName());
                    ChatUtil.b(String.format("Removed %s from friends list", s1));
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
