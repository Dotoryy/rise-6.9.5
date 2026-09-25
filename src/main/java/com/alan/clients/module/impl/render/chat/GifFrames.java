package com.alan.clients.module.impl.render.chat;

import java.awt.image.BufferedImage;

class GifFrames {
    final BufferedImage[] images;
    final int[] delays;

    GifFrames(BufferedImage[] images, int[] var2) {
        this.images = images;
        this.delays = var2;
    }
}
