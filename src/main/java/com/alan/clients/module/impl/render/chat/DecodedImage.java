package com.alan.clients.module.impl.render.chat;

import java.awt.image.BufferedImage;

class DecodedImage {
    final BufferedImage[] frames;
    final int[] delays;
    final int frameCount;

    DecodedImage(BufferedImage[] images, int[] var2, int var3) {
        this.frames = images;
        this.delays = var2;
        this.frameCount = var3;
    }
}
