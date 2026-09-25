package com.alan.clients.util.render;

import com.alan.clients.util.interfaces.InstanceAccess;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class StencilUtil implements InstanceAccess {
    public StencilUtil() {
    }

    private static void recreate(Framebuffer framebuffer) {
        GL30.glDeleteRenderbuffers(framebuffer.depthBuffer);
        int i = GL30.glGenRenderbuffers();
        GL30.glBindRenderbuffer(36161, i);
        GL30.glRenderbufferStorage(36161, 34041, aEg.displayWidth, aEg.displayHeight);
        GL30.glFramebufferRenderbuffer(36160, 36128, 36161, i);
        GL30.glFramebufferRenderbuffer(36160, 36096, 36161, i);
    }

    public static void c(Framebuffer framebuffer) {
        if (framebuffer != null && framebuffer.depthBuffer > -1) {
            recreate(framebuffer);
            framebuffer.depthBuffer = -1;
        }
    }

    public static void initStencil() {
        initStencil(aEg.getFramebuffer());
    }

    public static void initStencil(Framebuffer framebuffer) {
        framebuffer.bindFramebuffer(false);
        c(framebuffer);
        GL11.glClear(1280);
        GL11.glEnable(2960);
    }

    public static void bindWriteStencilBuffer() {
        GL11.glStencilFunc(519, 1, 1);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glColorMask(false, false, false, false);
    }

    public static void bindReadStencilBuffer(int var0) {
        GL11.glColorMask(true, true, true, true);
        GL11.glStencilFunc(514, var0, 1);
        GL11.glStencilOp(7680, 7680, 7680);
    }

    public static void uninitStencilBuffer() {
        GL11.glDisable(2960);
    }
}
