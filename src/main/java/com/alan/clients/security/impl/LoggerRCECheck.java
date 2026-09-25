package com.alan.clients.security.impl;

import com.alan.clients.security.CrashCheck;
import java.util.regex.Pattern;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.c;
import net.minecraft.util.IChatComponent;

public final class LoggerRCECheck extends CrashCheck {
    private static final Pattern PATTERN = Pattern.compile(".*\\$\\{[^}]*}.*");

    public LoggerRCECheck() {
        super("Log4J RCE Check", "Someone attempted to utilize the Log4J exploit");
    }

    @Override
    public boolean handle(Packet<?> packet) {
        if (packet instanceof S29PacketSoundEffect) {
            String s = ((S29PacketSoundEffect)packet).getSoundName();
            return PATTERN.matcher(s).matches();
        }

        if (!(packet instanceof c)) {
            return false;
        }

        IChatComponent ichatcomponent = ((c)packet).getChatComponent();
        return PATTERN.matcher(ichatcomponent.getUnformattedText()).matches() || PATTERN.matcher(ichatcomponent.getFormattedText()).matches();
    }
}
