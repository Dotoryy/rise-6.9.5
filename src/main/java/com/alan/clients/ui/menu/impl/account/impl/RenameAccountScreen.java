package com.alan.clients.ui.menu.impl.account.impl;

import com.alan.clients.Client;
import com.alan.clients.ui.menu.impl.account.AccountManagerScreen;
import com.alan.clients.ui.menu.impl.account.display.AccountViewModel;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.ui.menu.component.button.MenuButton;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.gui.textbox.TextAlign;
import com.alan.clients.util.gui.textbox.TextBox;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class RenameAccountScreen extends GuiScreen implements InstanceAccess {
    private static final Font FONT_RENDERER = FontManager.MAIN.a(36, FontWeight.BOLD);
    private final MenuButton[] menuButtons = new MenuButton[3];
    private static TextBox usernameBox;
    private static GuiScreen reference;
    private AccountViewModel<?> accountViewModel;
    private Animation animation;
    private static final Runnable TEXT_BOX_RUNNABLE = () -> usernameBox.setSelected(true);
    private final Runnable CANCEL_RUNNABLE = () -> aEg.displayGuiScreen(new AccountManagerScreen(reference));
    private final Runnable UPDATE_RUNNABLE = () -> {
        String s = usernameBox.text;
        if (this.validate(s)) {
            System.out.println("Updating username to " + s);
            this.accountViewModel.getAccount().setName(s);
            this.accountViewModel.getAccount().se();
            Client.a.getAltManager().update();
            this.CANCEL_RUNNABLE.run();
            System.out.println("Write");
        }
    };
    private static final Runnable BACKGROUND_RUNNABLE = () -> {
        ScaledResolution scaledresolution = new ScaledResolution(aEg);
        RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK);
    };

    public RenameAccountScreen(AccountViewModel<?> accountViewModel) {
        this.accountViewModel = accountViewModel;
        reference = this;
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        this.animation.Q(0.0);
        RiseShaders.aPL.a(ShaderRenderType.OVERLAY, var3, null);
        this.b(ShaderQueueType.BLUR).c(BACKGROUND_RUNNABLE);
        FONT_RENDERER.drawString("Update your username", this.width / 2, this.height / 2 - 64 + this.animation.getValue(), Color.WHITE.getRGB());
        this.accountViewModel.draw();
        MenuButton[] aadh = this.menuButtons;
        int i = aadh.length;

        for (int j = 0; j < i; j++) {
            aadh[j].draw(var1, var2, var3);
        }

        usernameBox.draw();
    }

    @Override
    public void mouseClicked(int var1, int var2, int var3) {
        usernameBox.click(var1, var2, var3);

        for (MenuButton menuButton : this.menuButtons) {
            if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.oM(), menuButton.da(), var1, var2)) {
                menuButton.runAction();
                break;
            }
        }
    }

    @Override
    protected void keyTyped(char var1, int var2) {
        if (usernameBox.isSelected()) {
            usernameBox.key(var1, var2);
        }
    }

    @Override
    public void initGui() {
        short short1 = 200;
        byte b0 = 24;
        byte b1 = 4;
        float f = 196 / 2.0F;
        Vector2d vector2d = new Vector2d(this.width / 2 - 100, this.height / 2 - 32);
        this.accountViewModel = new AccountViewModel<>(this.accountViewModel.getAccount(), (float)vector2d.x, (float)vector2d.y, 200.0F, 40.0F);
        this.accountViewModel.setScreenHeight(this.height);
        Vector2d vector2d1 = new Vector2d(this.width / 2 - 100, this.height / 2 + 32);
        usernameBox = new TextBox(vector2d1.offset(100, 8.0), FontManager.MAIN.a(24, FontWeight.REGULAR), Color.WHITE, TextAlign.CENTER, "Username", short1);
        usernameBox.bW(this.accountViewModel.getAccount().getName());
        this.menuButtons[0] = new MenuTextButton(vector2d1.x, vector2d1.y, short1, b0, TEXT_BOX_RUNNABLE, "");
        this.menuButtons[1] = new MenuTextButton(vector2d1.x, vector2d1.y + b0 + b1, f, b0, this.UPDATE_RUNNABLE, "Update");
        this.menuButtons[2] = new MenuTextButton(vector2d1.x + f + b1, vector2d1.y + b0 + b1, f, b0, this.CANCEL_RUNNABLE, "Back");
        this.animation = new Animation(Easing.EASE_OUT_QUINT, 600L);
        this.animation.setStartValue(-200.0);
    }

    private boolean validate(String var1) {
        if (var1.length() >= 3 && var1.length() <= 16) {
            for (char c0 : var1.toCharArray()) {
                if (!Character.isLetterOrDigit(c0) && c0 != '_') {
                    return false;
                }
            }

            return true;
        }
        return false;
    }
}
