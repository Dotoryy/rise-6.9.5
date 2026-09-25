package com.alan.clients.util.ime;

import java.awt.Canvas;
import java.awt.Window;
import java.awt.im.InputMethodRequests;

class InputMethodCanvas extends Canvas {
    private final InputMethodRequests aJG;
    final Window aJH;
    final InputMethodBridge aJI;

    InputMethodCanvas(InputMethodBridge var1, Window window) {
        this.aJI = var1;
        this.aJH = window;
        this.aJG = new InputMethodRequestHandler(this);
    }

    public InputMethodRequests uj() {
        return this.aJG;
    }
}
