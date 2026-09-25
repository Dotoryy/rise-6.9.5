package com.alan.clients.util.ime;

import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;

class InputMethodWindowListener implements InputMethodListener {
    final InputMethodBridge aJK;

    InputMethodWindowListener(InputMethodBridge var1) {
        this.aJK = var1;
    }

    public void b(InputMethodEvent event) {
        try {
            this.aJK.a(event);
        } finally {
            event.consume();
        }
    }

    public void c(InputMethodEvent event) {
    }
    public void inputMethodTextChanged(InputMethodEvent event) { }
    public void caretPositionChanged(InputMethodEvent event) { }

}
