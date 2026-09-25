package com.alan.clients.util.sound;

import com.alan.clients.util.interfaces.InstanceAccess;
import lombok.Generated;

public final class SoundUtil implements InstanceAccess {
    private static int ticksExisted;

    public static void toggleSound(boolean var0) {
        if (aEg.thePlayer != null && aEg.thePlayer.ticksExisted != ticksExisted) {
            if (var0) {
                cm("rise.toggle.enable");
            } else {
                cm("rise.toggle.disable");
            }

            ticksExisted = aEg.thePlayer.ticksExisted;
        }
    }

    public static void cm(String var0) {
        playSound(var0, 1.0F, 1.0F);
    }

    public static void playSound(String var0, float var1, float var2) {
        aEg.theWorld.playSound(aEg.thePlayer.posX, aEg.thePlayer.posY, aEg.thePlayer.posZ, var0, var1, var2, false);
    }

    @Generated
    private SoundUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
