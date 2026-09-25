package com.alan.clients.util.ime;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Window.Type;
import java.awt.Window;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.im.InputContext;
import java.text.AttributedCharacterIterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public final class InputMethodBridge {
    private static final InputMethodBridge aJx = new InputMethodBridge();
    private volatile boolean Mc;
    private volatile boolean dj;
    private Component aJy;
    private InputContext aJz;
    private final Queue<String> aJA = new ConcurrentLinkedQueue<>();
    private volatile String aJB = "";
    volatile int aJC = 12;
    private volatile Frame aJD;
    private volatile Window aJE;

    public static InputMethodBridge ua() {
        return aJx;
    }

    private InputMethodBridge() {
    }

    public void ub() {
        if (!this.Mc) {
            try {
                Canvas canvas = Display.getParent();
                if (canvas != null) {
                    canvas.enableInputMethods(true);
                    canvas.addInputMethodListener(new InputMethodFrameListener(this));
                    this.aJy = canvas;
                    this.aJz = canvas.getInputContext();
                    this.Mc = true;
                    return;
                }
            } catch (Throwable throwable1) {
            }

            try {
                EventQueue.invokeAndWait(() -> {
                    Frame frame = new Frame();
                    frame.setUndecorated(true);
                    frame.setFocusableWindowState(false);
                    frame.setAutoRequestFocus(false);
                    frame.setType(Type.UTILITY);
                    frame.setLocation(-10000, -10000);
                    frame.setSize(1, 1);
                    frame.addNotify();
                    Window window = new Window(frame);
                    window.setFocusableWindowState(false);
                    window.setAutoRequestFocus(false);
                    window.setAlwaysOnTop(true);

                    try {
                        window.setBackground(new Color(0, 0, 0, 0));
                    } catch (Throwable throwable2) {
                    }

                    InputMethodCanvas agr = new InputMethodCanvas(this, window);
                    agr.setFocusable(false);
                    agr.enableInputMethods(true);
                    agr.setSize(1, 1);
                    window.add(agr);
                    window.setSize(1, 1);
                    window.addNotify();
                    agr.addNotify();
                    agr.addInputMethodListener(new InputMethodWindowListener(this));
                    this.aJD = frame;
                    this.aJE = window;
                    this.aJy = agr;
                    this.aJz = agr.getInputContext();
                    this.Mc = true;
                    window.setVisible(false);
                });
            } catch (Throwable throwable) {
                this.Mc = false;
                this.aJy = null;
                this.aJz = null;
                this.aJD = null;
                this.aJE = null;
            }
        }
    }

    public void c(boolean var1) {
        this.dj = var1;
        if (!var1) {
            this.aJB = "";
        }

        if (this.Mc) {
            if (this.aJD == null) {
                ;
            }

            Window window = this.aJE;
            if (window != null) {
                try {
                    EventQueue.invokeLater(() -> {
                        try {
                            window.setVisible(var1);
                        } catch (Throwable throwable1) {
                        }
                    });
                } catch (Throwable throwable) {
                }
            }
        }
    }

    public boolean uc() {
        String s = this.aJB;
        return s != null && !s.isEmpty();
    }

    public String ud() {
        return this.aJB == null ? "" : this.aJB;
    }

    public void g(int var1, int var2, int var3) {
        if (var3 > 0) {
            this.aJC = var3;
        }

        if (this.Mc && this.dj) {
            Window window = this.aJE;
            if (window != null) {
                int i = ug() + var1;
                int j = uh() + var2;

                try {
                    EventQueue.invokeLater(() -> {
                        try {
                            window.setLocation(i, j);
                            if (!window.isVisible()) {
                                window.setVisible(true);
                            }
                        } catch (Throwable throwable1) {
                        }
                    });
                } catch (Throwable throwable) {
                }
            }
        }
    }

    public void a(int var1, char var2) {
        if (this.Mc && this.dj) {
            if (this.aJz != null && this.aJy != null) {
                long now = System.currentTimeMillis();
                int j = uf();
                int k = b(var1, var2);
                KeyEvent keyevent = new KeyEvent(this.aJy, 401, now, j, k, '\uffff');
                this.aJz.dispatchEvent(keyevent);
                if (var2 != 0 && !Character.isISOControl(var2)) {
                    KeyEvent keyevent1 = new KeyEvent(this.aJy, 400, now, j, 0, var2);
                    this.aJz.dispatchEvent(keyevent1);
                }
            }
        }
    }

    public String ue() {
        String s = this.aJA.poll();
        if (s == null) {
            return null;
        }

        StringBuilder stringbuilder = new StringBuilder(s);

        String s1;
        while ((s1 = this.aJA.poll()) != null) {
            stringbuilder.append(s1);
        }

        return stringbuilder.toString();
    }

    void a(InputMethodEvent event) {
        AttributedCharacterIterator attributedcharacteriterator = event.getText();
        int i = event.getCommittedCharacterCount();
        if (attributedcharacteriterator == null) {
            this.aJB = "";
        } else {
            String s = a(attributedcharacteriterator);
            int j = Math.max(0, Math.min(i, s.length()));
            if (j > 0) {
                String s1 = s.substring(0, j);
                if (!s1.isEmpty()) {
                    this.aJA.add(s1);
                }
            }

            String s2 = s.substring(j);
            this.aJB = s2 == null ? "" : s2;
        }
    }

    private static String a(AttributedCharacterIterator var0) {
        StringBuilder stringbuilder = new StringBuilder();

        for (char c0 = var0.first(); c0 != '\uffff'; c0 = var0.next()) {
            stringbuilder.append(c0);
        }

        return stringbuilder.toString();
    }

    private static int uf() {
        short short1 = 0;
        if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
            short1 |= 64;
        }

        if (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) {
            short1 |= 128;
        }

        if (Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184)) {
            short1 |= 512;
        }

        if (Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220)) {
            short1 |= 256;
        }

        return short1;
    }

    private static int b(int var0, char var1) {
        switch (var0) {
            case 1:
                return 27;
            case 14:
                return 8;
            case 15:
                return 9;
            case 28:
                return 10;
            case 57:
                return 32;
            case 200:
                return 38;
            case 203:
                return 37;
            case 205:
                return 39;
            case 208:
                return 40;
            default:
                if (var1 >= 'A' && var1 <= 'Z') {
                    return 65 + (var1 - 65);
                } else if (var1 >= 'a' && var1 <= 'z') {
                    return 65 + (var1 - 97);
                }
                return var1 >= 48 && var1 <= 57 ? 48 + (var1 - 48) : 0;
        }
    }

    private static int ug() {
        try {
            return Display.getX();
        } catch (Throwable throwable) {
            return 0;
        }
    }

    private static int uh() {
        try {
            return Display.getY();
        } catch (Throwable throwable) {
            return 0;
        }
    }
}
