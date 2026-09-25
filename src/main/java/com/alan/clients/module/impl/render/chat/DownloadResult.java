package com.alan.clients.module.impl.render.chat;

final class DownloadResult {
    final byte[] data;
    final String resolvedUrl;
    final String contentType;
    final int statusCode;

    DownloadResult(byte[] var1, String var2, String var3, int var4) {
        this.data = var1;
        this.resolvedUrl = var2;
        this.contentType = var3;
        this.statusCode = var4;
    }
}
