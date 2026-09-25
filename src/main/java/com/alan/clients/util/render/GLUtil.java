package com.alan.clients.util.render;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class GLUtil {
    public GLUtil() {
    }

    public static void tB() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
    }

    public static void tC() {
        GlStateManager.disableBlend();
    }

    public static void ap(int var0) {
        GL11.glBindTexture(3553, var0);
    }

    public static void m(int var0, int var1) {
        GL13.glActiveTexture(var1);
        GL11.glBindTexture(3553, var0);
    }
}
