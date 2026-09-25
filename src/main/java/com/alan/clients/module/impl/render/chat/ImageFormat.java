package com.alan.clients.module.impl.render.chat;

enum ImageFormat {
    PNG,
    JPEG,
    GIF,
    BMP,
    WEBP,
    UNKNOWN;

    private static final ImageFormat[] $VALUES = createValues();

    ImageFormat() {
    }

    private static ImageFormat[] createValues() {
        return new ImageFormat[]{PNG, JPEG, GIF, BMP, WEBP, UNKNOWN};
    }
}
