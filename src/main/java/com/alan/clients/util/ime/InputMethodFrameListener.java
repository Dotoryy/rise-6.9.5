package com.alan.clients.util.ime;

import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

class InputMethodFrameListener implements InputMethodListener {
    final InputMethodBridge aJF;

    InputMethodFrameListener(InputMethodBridge var1) {
        this.aJF = var1;
    }

    public void b(InputMethodEvent event) {
        try {
            this.aJF.a(event);
        } finally {
            event.consume();
        }
    }

    public void c(InputMethodEvent event) {
    }
    public void inputMethodTextChanged(InputMethodEvent event) { }
    public void caretPositionChanged(InputMethodEvent event) { }

}
