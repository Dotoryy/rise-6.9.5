package com.alan.clients.util.gui.textbox;

import com.alan.clients.util.gui.textbox.TextAlign;

class TextBoxSwitchMap {
    static final int[] aJw = new int[TextAlign.values().length];

    static {
        try {
            aJw[TextAlign.CENTER.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }

        try {
            aJw[TextAlign.LEFT.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
    }
}
