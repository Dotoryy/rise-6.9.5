package com.alan.clients.command.impl;

import com.alan.clients.command.Command;
import com.alan.clients.util.file.config.ConfigFile;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.command.CommandUsageTracker;
import com.alan.clients.util.file.config.ConfigManager;
import com.alan.clients.util.localization.Localization;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.s;
import rip.vantage.commons.packet.impl.client.community.C2SPacketConfigRequest;
import rip.vantage.network.core.VantageNetwork;

public final class Config extends Command {
    public Config() {
        super("command.config.description", "config", "configs", "cfg", "settings", "c");
    }

    @Override
    public void execute(String[] var1) {
        ConfigManager configManager = this.rN().getConfigManager();
        String s = var1[1].toLowerCase();
        switch (var1.length) {
            case 2:
                {
                    String s3 = s;
                    switch (s3) {
                        case "list":
                            ChatUtil.b("command.config.selectload");
                            configManager.update();
                            configManager.forEach(
                                var1x -> {
                                    String s4 = var1x.getFile().getName().replace(".json", "");
                                    String s5 = ".config load " + s4;
                                    String s6 = this.rz().getChatAccentColor().toString();
                                    s sx = new s(s6 + "> " + s4);
                                    s sxx = new s(String.format(Localization.ce("command.config.loadhover"), s4));
                                    sx.getChatStyle()
                                        .setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, s5))
                                        .setChatHoverEvent(new HoverEvent(net.minecraft.event.HoverEvent.Action.SHOW_TEXT, sxx));
                                    aEg.thePlayer.addChatMessage(sx);
                                }
                            );
                            return;
                        case "folder":
                            break;
                        default:
                            ChatUtil.b("command.config.actions");
                            return;
                    }
                }

                try {
                    Desktop desktop = Desktop.getDesktop();
                    File file1 = new File(String.valueOf(configManager.CONFIG_DIRECTORY));
                    desktop.open(file1);
                    ChatUtil.b("command.config.folder");
                } catch (IllegalArgumentException | IOException illegalargumentexception) {
                    ChatUtil.b("command.config.notfound");
                }
                break;
            case 3:
                String s1;
                {
                    s1 = var1[2];
                    String s2 = s;
                    switch (s2) {
                        case "load":
                            configManager.update();
                            ConfigFile configfile = configManager.get(s1);
                            if (configfile != null) {
                                CompletableFuture.runAsync(() -> {
                                    if (configfile.te()) {
                                        ChatUtil.b("command.config.loaded", s1);
                                        if (!s1.equalsIgnoreCase("latest")) {
                                            ChatUtil.b("command.config.accident");
                                        }

                                        try {
                                            CommandUsageTracker.sJ().bC(s1);
                                        } catch (Throwable throwable) {
                                        }
                                    } else {
                                        VantageNetwork.aKB().aKK().sendMessage(new C2SPacketConfigRequest(s1).aJk());
                                    }
                                });
                            } else {
                                VantageNetwork.aKB().aKK().sendMessage(new C2SPacketConfigRequest(s1).aJk());
                            }

                            return;
                        case "create":
                        case "save":
                            break;
                        default:
                            ChatUtil.b("command.config.usage");
                            return;
                    }
                }

                if (s1.equalsIgnoreCase("latest")) {
                    ChatUtil.b("command.config.reserved");
                    return;
                }

                CompletableFuture.runAsync(() -> {
                    configManager.d(s1, false);
                    ChatUtil.b("command.config.saved");
                    ChatUtil.b("command.config.reminder");
                });
                break;
            default:
                ChatUtil.b("command.config.actions");
        }
    }
}
