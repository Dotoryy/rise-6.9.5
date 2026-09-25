package com.alan.clients.ui.menu.impl.account;

import com.alan.clients.Client;
import com.alan.clients.ui.menu.impl.account.display.AccountViewModel;
import com.alan.clients.ui.menu.impl.account.impl.AddAccountScreen;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.menu.component.button.MenuButton;
import com.alan.clients.ui.menu.component.button.impl.MenuTextButton;
import com.alan.clients.ui.menu.impl.main.MainMenu;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.account.AltAccount;
import com.alan.clients.util.file.alt.AltManager;
import com.alan.clients.util.gui.ScrollUtil;
import com.alan.clients.util.render.ScissorUtil;
import com.alan.clients.util.shader.RiseShaders;
import com.alan.clients.util.shader.base.ShaderRenderType;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class AccountManagerScreen extends GuiScreen implements InstanceAccess {
    private static final AltManager ALT_MANAGER = Client.a.getAltManager();
    private static final Runnable ADD_ACCOUNT_RUNNABLE = () -> aEg.displayGuiScreen(new AddAccountScreen());
    private static final Runnable CANCEL_RUNNABLE = () -> aEg.displayGuiScreen(new MainMenu());
    private static final Runnable BACKGROUND_RUNNABLE = () -> {
        ScaledResolution scaledresolution = new ScaledResolution(aEg);
        RenderUtil.d(0.0, 0.0, scaledresolution.getScaledWidth(), scaledresolution.getScaledHeight(), Color.BLACK);
    };
    private static final Animation SLIDE_ANIMATION = new Animation(Easing.EASE_OUT_CIRC, 350L);
    private static final MenuButton[] MENU_BUTTONS = new MenuButton[2];
    private static final List<AccountViewModel<?>> ACCOUNT_DISPLAY_LIST = new ArrayList<>();
    private static GuiScreen prevScreen;
    private boolean updateMarker;
    private static int screenWidth;
    private static int screenHeight;
    ScrollUtil scrollUtil = new ScrollUtil();
    private static int accountsInRow;

    public AccountManagerScreen(GuiScreen screen) {
        prevScreen = screen;
    }

    @Override
    public void drawScreen(int var1, int var2, float var3) {
        RiseShaders.aPL.a(ShaderRenderType.OVERLAY, var3, null);
        this.b(ShaderQueueType.BLUR).c(BACKGROUND_RUNNABLE);
        GL11.glPushMatrix();
        ScissorUtil.hK();
        this.b(ShaderQueueType.BLOOM).c(() -> GuiScreen.drawRect(0, 0, 0, 0, 0));
        this.scrollUtil.qx();
        if (!ACCOUNT_DISPLAY_LIST.isEmpty()) {
            int i = (int)ACCOUNT_DISPLAY_LIST.get(0).getHeight();
            byte b0 = 5;
            int j = (int)Math.ceil((double)ACCOUNT_DISPLAY_LIST.size() / accountsInRow);
            int k = j * (i + b0) + 16;
            int l = this.height - 48;
            int i1 = Math.min(0, -(k - l));
            this.scrollUtil.V(i1);
        } else {
            this.scrollUtil.V(0.0);
        }

        for (int j1 = 0; j1 < ACCOUNT_DISPLAY_LIST.size(); j1++) {
            AccountViewModel accountviewmodel = ACCOUNT_DISPLAY_LIST.get(j1);
            accountviewmodel.setScroll(this.scrollUtil.tE());
            accountviewmodel.draw();
        }

        ScissorUtil.scissor(new ScaledResolution(aEg), 0.0, 0.0, this.width, this.height - 48);
        ScissorUtil.disable();
        GL11.glPopMatrix();

        for (MenuButton menuButton : MENU_BUTTONS) {
            if (menuButton != null) {
                menuButton.draw(var1, var2, var3);
            }
        }
    }

    @Override
    public void updateScreen() {
        if (this.updateMarker) {
            this.updateMarker = false;
            ArrayList arraylist = new ArrayList();

            for (AccountViewModel accountviewmodel : ACCOUNT_DISPLAY_LIST) {
                if (accountviewmodel.isRemovable()) {
                    ALT_MANAGER.getAccounts().remove(accountviewmodel.getAccount());
                    arraylist.add(accountviewmodel);
                }
            }

            ACCOUNT_DISPLAY_LIST.removeAll(arraylist);
            ALT_MANAGER.update();
            this.reorderViewModels();
        }
    }

    @Override
    public void mouseClicked(int var1, int var2, int var3) {
        for (MenuButton menuButton : MENU_BUTTONS) {
            if (MouseUtil.isHovered(menuButton.getX(), menuButton.getY(), menuButton.oM(), menuButton.da(), var1, var2)) {
                menuButton.runAction();
                return;
            }
        }

        for (AccountViewModel accountviewmodel : ACCOUNT_DISPLAY_LIST) {
            if (accountviewmodel.mouseClicked(var1, var2, var3)) {
                this.updateMarker = true;
                return;
            }
        }
    }

    @Override
    protected void mouseReleased(int var1, int var2, int var3) {
        super.mouseReleased(var1, var2, var3);
    }

    @Override
    public void initGui() {
        super.initGui();
        screenWidth = this.width;
        screenHeight = this.height;
        int i = MENU_BUTTONS.length;
        byte b0 = 100;
        byte b1 = 24;
        byte b2 = 5;
        int j = this.width / 2 - ((i & 1) == 0 ? 105 * (i / 2) - 2 : (b0 + b2) * (i / 2) + b0 / 2);
        MENU_BUTTONS[0] = new MenuTextButton(0.0, 0.0, 0.0, 0.0, ADD_ACCOUNT_RUNNABLE, "Add Account");
        MENU_BUTTONS[1] = new MenuTextButton(0.0, 0.0, 0.0, 0.0, CANCEL_RUNNABLE, "Cancel");

        for (MenuButton menuButton : MENU_BUTTONS) {
            menuButton.setX(j);
            menuButton.setY(this.height - b1 - b2 * 2);
            menuButton.P(b0);
            menuButton.h(b1);
            j += b0 + b2;
        }

        if (prevScreen instanceof AccountManagerScreen) {
            this.reorderViewModels();
        } else {
            ACCOUNT_DISPLAY_LIST.clear();
            ALT_MANAGER.tk();

            for (AltAccount altAccount : ALT_MANAGER.getAccounts()) {
                addDisplay(altAccount);
            }

            this.b(ShaderQueueType.REGULAR).c(BACKGROUND_RUNNABLE);
        }
    }

    public static void addAccount(AltAccount account) {
        addDisplay(account);
        ALT_MANAGER.getAccounts().add(account);
        ALT_MANAGER.update();
    }

    private static void addDisplay(AltAccount display) {
        short short1 = 172;
        byte b0 = 40;
        byte b1 = 5;
        accountsInRow = Math.max(1, Math.min(3, (screenWidth - 57) / 177));
        int i = 16 + 45 * (ACCOUNT_DISPLAY_LIST.size() / accountsInRow);
        int j = screenWidth / 2 - ((accountsInRow & 1) == 0 ? 177 * (accountsInRow / 2) - 2 : (short1 + b1) * (accountsInRow / 2) + short1 / 2);
        int k = j + (short1 + b1) * (ACCOUNT_DISPLAY_LIST.size() % accountsInRow);
        AccountViewModel accountviewmodel = new AccountViewModel<>(display, k, i, short1, b0);
        accountviewmodel.setScreenHeight(screenHeight);
        ACCOUNT_DISPLAY_LIST.add(accountviewmodel);
    }

    private void reorderViewModels() {
        ArrayList arraylist = new ArrayList<>(ACCOUNT_DISPLAY_LIST);
        ACCOUNT_DISPLAY_LIST.clear();
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            addDisplay(((AccountViewModel)iterator.next()).getAccount());
        }
    }

    @Override
    public void onResize(Minecraft mc, int var2, int var3) {
        prevScreen = this;
        super.onResize(mc, var2, var3);
    }

    @Override
    public void onGuiClosed() {
        ACCOUNT_DISPLAY_LIST.clear();
        Arrays.fill(MENU_BUTTONS, null);
    }
}
