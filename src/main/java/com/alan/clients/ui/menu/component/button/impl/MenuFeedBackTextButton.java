package com.alan.clients.ui.menu.component.button.impl;

import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;

public class MenuFeedBackTextButton extends MenuTextButton {
    public String feedback;
    public String originalName;

    public MenuFeedBackTextButton(double var1, double var3, double var5, double var7, Runnable var9, String var10, String var11) {
        super(var1, var3, var5, var7, var9, var10);
        this.feedback = var11;
        this.originalName = var10;
    }

    @Override
    public void draw(int var1, int var2, float var3) {
        super.draw(var1, var2, var3);
        if (!MouseUtil.isHovered(this.getX(), this.getY(), this.oM(), this.da(), var1, var2)) {
            this.name = this.originalName;
        }
    }

    @Override
    public void runAction() {
        super.runAction();
        this.name = this.feedback;
    }
}
