package com.alan.clients.component.impl.monitoring;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.newevent.impl.other.BackendS2CEvent;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ScreenCaptureComponent extends Component {
    public static int fF;
    public long fa;
    public int fK;
    public IntBuffer fN;
    public static int fB;
    public static int fD;
    public BufferedImage fP;
    public static int fG;
    @EventLink
    public Listener<BackendS2CEvent> fR;
    public String fH;
    public List<Integer> fM;
    public int fb;
    @EventLink
    public Listener<WorldChangeEvent> onWorldChange;
    public String fc;
    public static float fE;
    @EventLink
    public Listener<Render2DEvent> onRender2D;
    public static int fC;
    public String eZ;
    public volatile boolean eF;
    public int[] fO;
    public List<byte[]> fL = new ArrayList<>();
    public long fI;
    public int fJ;
    public BufferedImage fQ;

    public void bL() {
    }

    public byte[] a(BufferedImage image) {
        return null;
    }

    public void a(List var1, List var2, String var3, String var4, String var5, int var6) {
    }

    public synchronized void a(String var1, int var2, String var3) {
    }

    public void a(String var1, byte[] var2, String var3, String var4, int var5) {
    }

    public synchronized void bv() {
    }

    public ScreenCaptureComponent() {
        this.fM = new ArrayList<>();
        this.fR = var1 -> {};
        this.onRender2D = var1 -> {};
        this.onWorldChange = var1 -> {};
    }

    public byte[] a(List<byte[]> var1, List<Integer> var2) {
        return null;
    }

    public static String h(String var0) {
        return null;
    }
}
