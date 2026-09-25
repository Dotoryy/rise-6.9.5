package com.alan.clients.module.impl.render.chat;

import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ChatImage {
    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 200;
    private final String url;
    private final int updateCounter;
    private final int chatLineId;
    private final long createdTime;
    private volatile BufferedImage[] frames;
    private volatile int[] textureIds = new int[0];
    private volatile int[] frameDelays = new int[]{100};
    private volatile int width;
    private volatile int height;
    private volatile boolean loaded;
    private volatile boolean failed;
    private volatile boolean pendingUpload;
    private volatile long animationStartTime = System.currentTimeMillis();

    public ChatImage(String var1, int var2, int var3) {
        this.url = var1;
        this.updateCounter = var2;
        this.chatLineId = var3;
        this.createdTime = System.currentTimeMillis();
    }

    public String getUrl() {
        return this.url;
    }

    public int getUpdateCounter() {
        return this.updateCounter;
    }

    public int getChatLineId() {
        return this.chatLineId;
    }

    public long getCreatedTime() {
        return this.createdTime;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public boolean isFailed() {
        return this.failed;
    }

    public int[] getTextureIds() {
        return this.textureIds;
    }

    public synchronized void setImage(BufferedImage image) {
        this.a(new BufferedImage[]{image}, new int[]{100});
    }

    public synchronized void a(BufferedImage[] images, int[] var2) {
        this.frames = images;
        this.width = images[0].getWidth();
        this.height = images[0].getHeight();
        this.loaded = true;
        this.failed = false;
        this.pendingUpload = true;
        this.textureIds = new int[images.length];
        Arrays.fill(this.textureIds, -1);
        this.frameDelays = var2 != null && var2.length == images.length ? var2 : new int[images.length];

        for (int i = 0; i < this.frameDelays.length; i++) {
            if (this.frameDelays[i] <= 0) {
                this.frameDelays[i] = 100;
            }
        }

        this.animationStartTime = System.currentTimeMillis();
    }

    public synchronized boolean needsUpload() {
        return this.pendingUpload && this.frames != null && this.textureIds.length > 0 && this.textureIds[0] < 0;
    }

    public synchronized BufferedImage[] getFrames() {
        return this.frames;
    }

    public synchronized void setTextureIds(int[] var1) {
        this.textureIds = var1;
        this.pendingUpload = false;
        this.frames = null;
    }

    public synchronized void markFailed() {
        this.failed = true;
        this.loaded = false;
        this.pendingUpload = false;
        this.frames = null;
        this.textureIds = new int[0];
    }

    public int getCurrentTextureId() {
        int[] aint = this.textureIds;
        if (aint != null && aint.length != 0) {
            if (aint.length == 1) {
                return aint[0];
            }

            int[] aint1 = this.frameDelays;
            int i = 0;

            for (int j : aint1) {
                i += Math.max(j, 20);
            }

            if (i <= 0) {
                return aint[0];
            }

            long k = (System.currentTimeMillis() - this.animationStartTime) % i;

            for (int l = 0; l < aint.length; l++) {
                k -= Math.max(aint1[l], 20);
                if (k < 0L) {
                    return aint[l];
                }
            }

            return aint[0];
        }
        return -1;
    }

    public int getRenderWidth(float var1) {
        if (this.width > 0 && this.height > 0) {
            float f = Math.min(var1, 300.0F);
            float f1 = (float)this.width / this.height;
            int i = Math.min(this.width, (int)f);
            int j = Math.max(1, Math.round(i / f1));
            if (j > 200) {
                short short1 = 200;
                i = Math.max(1, Math.round(short1 * f1));
            }

            return Math.max(1, i);
        }
        return Math.min((int)var1, 300);
    }

    public int getRenderHeight(float var1) {
        if (this.width > 0 && this.height > 0) {
            float f = (float)this.width / this.height;
            int i = Math.min(this.width, Math.min((int)var1, 300));
            return Math.min(Math.max(1, Math.round(i / f)), 200);
        }
        return 0;
    }
}
