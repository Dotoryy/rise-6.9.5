package com.alan.clients.ui.palette;

import com.alan.clients.util.interfaces.Bindable;

record PaletteCommandRow(Bindable aBe, String aBf, String aBg, String aBh) {

    public Bindable rf() {
        return this.aBe;
    }

    public String rg() {
        return this.aBf;
    }

    public String rh() {
        return this.aBg;
    }

    public String ri() {
        return this.aBh;
    }
}
