package com.alan.clients.newevent.impl.render;

import com.alan.clients.newevent.Event;

public final class FramebufferResizeEvent implements Event {
    private final int width;
    private final int height;

    public FramebufferResizeEvent(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
