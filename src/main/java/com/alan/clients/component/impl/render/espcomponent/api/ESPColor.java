package com.alan.clients.component.impl.render.espcomponent.api;

import java.awt.Color;
import lombok.Generated;

public class ESPColor {
    private Color hS;
    private Color hT;
    private Color hU;

    @Generated
    public ESPColor(Color color, Color var2, Color var3) {
        this.hS = color;
        this.hT = var2;
        this.hU = var3;
    }

    @Generated
    public Color getColor() {
        return this.hS;
    }

    @Generated
    public Color cs() {
        return this.hT;
    }

    @Generated
    public Color ct() {
        return this.hU;
    }
}
