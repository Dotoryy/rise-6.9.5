package com.alan.clients.ui.menu.component.button;

import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.ui.menu.component.MenuComponent;
import com.alan.clients.util.MouseUtil;

public class MenuButton extends MenuComponent {
    private final Runnable runnable;
    private final Animation animation = new Animation(Easing.EASE_OUT_QUINT, 500L);
    private final Animation hoverAnimation = new Animation(Easing.EASE_OUT_SINE, 250L);

    public MenuButton(double var1, double var3, double var5, double var7, Runnable runnable) {
        super(var1, var3, var5, var7);
        this.runnable = runnable;
    }

    public void draw(int var1, int var2, float var3) {
        this.hoverAnimation.Q(MouseUtil.isHovered(this.getX(), this.getY(), this.oM(), this.da(), var1, var2) ? 100.0 : 45.0);
    }

    public void runAction() {
        this.runnable.run();
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public Animation getHoverAnimation() {
        return this.hoverAnimation;
    }
}
