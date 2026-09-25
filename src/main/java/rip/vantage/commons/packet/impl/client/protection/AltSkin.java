package rip.vantage.commons.packet.impl.client.protection;

import java.awt.geom.Dimension2D;
import org.apache.batik.swing.JSVGCanvas;

public class AltSkin {
    private final String skin;

    public AltSkin(String var1) {
        this.skin = var1;
    }

    public Dimension2D getSize() {
        if (this.skin == null) {
            return null;
        }

        JSVGCanvas jsvgcanvas = new JSVGCanvas();
        jsvgcanvas.loadSVGDocument(this.skin);
        return jsvgcanvas.getSVGDocumentSize();
    }

    @Override
    public String toString() {
        Dimension2D dimension2d = this.getSize();
        return "[" + dimension2d.getWidth() + ", " + dimension2d.getHeight() + "]";
    }
}
