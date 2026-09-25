package com.alan.clients.command.impl;

import com.alan.clients.Client;
import com.alan.clients.command.Command;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.interfaces.Bindable;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.s;
import org.lwjgl.input.Keyboard;

public final class Bind extends Command
{
    public Bind() {
        super("command.bind.description", new String[] { "bind", "binds", "keybind", "b" });
    }

    @Override
    public void execute(final String[] array) {
        if (array.length == 3) {
            final Bindable a = Client.a.t().a(array[1]);
            if (a == null) {
                ChatUtil.b("command.bind.invalidmodule", new Object[0]);
                return;
            }
            final int d = d(array[2]);
            a.setKey(d);
            ChatUtil.b("Bound " + a.getName() + " to " + b(d), new Object[0]);
            ChatUtil.b("Remember you can bind configs by doing .bind configname key", new Object[0]);
        }
        else if (array.length == 2 && array[1].equalsIgnoreCase("list")) {
            ChatUtil.b("Displaying all active binds", new Object[0]);
            Client.a.t().aP().forEach(p -> {
                if (p.getKey() != 0) {
                    final s s = new s(this.rz().getChatAccentColor().toString() + "> " + p.getAliases()[0] + "§f " + b(p.getKey()));
                    s.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ".bind " + p.getName().replace((CharSequence)" ", (CharSequence)"") + " none")).setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (IChatComponent)new s("Click to remove " + p.getAliases()[0] + " bind")));
                    Bind.aEg.thePlayer.addChatMessage((IChatComponent)s);
                }
            });
        }
        else {
            this.error(".bind <list/module/config> (KEY)");
        }
    }

    public static int d(final String s) {
        if (s == null) {
            return 0;
        }
        final String upperCase = s.trim().toUpperCase();
        if (upperCase.isEmpty()) {
            return 0;
        }
        final String s2 = upperCase;
        switch (s2) {
            case "NONE":
            case "UNBOUND": {
                return 0;
            }
            case "LEFTCLICK":
            case "LEFTMOUSE":
            case "MOUSE1":
            case "MB1": {
                return -100;
            }
            case "RIGHTCLICK":
            case "RIGHTMOUSE":
            case "MOUSE2":
            case "MB2": {
                return -99;
            }
            case "MIDDLECLICK":
            case "MIDDLEMOUSE":
            case "MOUSE3":
            case "MB3": {
                return -98;
            }
            default: {
                if (upperCase.startsWith("MOUSE") || upperCase.startsWith("MB")) {
                    final String s3 = upperCase.startsWith("MOUSE") ? upperCase.substring(5) : upperCase.substring(2);
                    try {
                        final int int1 = Integer.parseInt(s3);
                        if (int1 >= 1) {
                            return int1 - 101;
                        }
                    }
                    catch (final NumberFormatException ex) {}
                }
                break;
            }
        }
        return Keyboard.getKeyIndex(upperCase);
    }

    public static String b(final int n) {
        switch (n) {
            case -100: {
                return "Left Click";
            }
            case -99: {
                return "Right Click";
            }
            case -98: {
                return "Middle Click";
            }
            default: {
                final String keyDisplayString = GameSettings.getKeyDisplayString(n);
                return (keyDisplayString == null || keyDisplayString.trim().isEmpty()) ? "NONE" : keyDisplayString;
            }
        }
    }

    public static String c(final int n) {
        if (n == 0) {
            return "NONE";
        }
        if (n < 0) {
            return "MOUSE" + (n + 101);
        }
        final String keyName = Keyboard.getKeyName(n);
        return (keyName == null || keyName.trim().isEmpty()) ? "NONE" : keyName.toUpperCase();
    }
}
