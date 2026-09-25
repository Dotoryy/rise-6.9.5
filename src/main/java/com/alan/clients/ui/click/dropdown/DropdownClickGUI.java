package com.alan.clients.ui.click.dropdown;

import com.alan.clients.Client;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.ui.click.dropdown.components.CategoryComponent;
import com.alan.clients.ui.click.standard.components.ModuleComponent;
import com.alan.clients.ui.click.standard.UIColors;
import com.alan.clients.util.gui.GUIUtil;
import com.alan.clients.util.interfaces.ExecutorAccess;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class DropdownClickGUI
extends GuiScreen
implements ExecutorAccess {
    private final Map<Category, CategoryComponent> axh = new HashMap<Category, CategoryComponent>();
    private final List<Category> axi = new ArrayList<Category>();
    private double axj = 0.0;
    private double axk = 0.0;
    private final Animation axl = new Animation(Easing.EASE_OUT_EXPO, 300L);
    private CategoryComponent axm = null;
    private double axn;
    private double axo;
    private double axp = 0.8;
    private final double axq = 129.0;
    private final double axr = 10.0;
    private final double axs = 3.0;
    private boolean Mc = false;

    public void om() {
        this.axh.clear();
        this.axi.clear();
        this.Mc = false;
        if (aEg != null && DropdownClickGUI.aEg.currentScreen == this) {
            this.initGui();
        }
    }

    private double on() {
        return 129.0 * this.axp;
    }

    private double oo() {
        return 10.0 * this.axp;
    }

    public void initGui() {
        if (!this.Mc) {
            this.oq();
            this.Mc = true;
            this.op();
        }
        this.axj = 0.0;
        this.axk = 0.0;
        this.axl.setValue(0.0);
        Keyboard.enableRepeatEvents(true);
    }

    private void op() {
        double d2 = 20.0;
        double d3 = 20.0;
        int n2 = 0;
        int n3 = 0;
        for (Category category : this.axi) {
            CategoryComponent abb2 = this.axh.get((Object)category);
            if (abb2 == null) continue;
            double d4 = d2 + (double)n2 * (this.on() + this.oo());
            double d5 = d3 + (double)(n3 * 28);
            abb2.i(d4, d5);
            if (!((double)(++n2) >= 3.0)) continue;
            n2 = 0;
            ++n3;
        }
    }

    private void E(double d2) {
        this.axp = Math.max(0.5, Math.min(1.5, d2));
        this.op();
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.axj = 0.0;
        this.axk = 0.0;
        this.axl.setValue(0.0);
    }

    private void oq() {
        this.axi.add(Category.COMBAT);
        this.axi.add(Category.GHOST);
        this.axi.add(Category.MOVEMENT);
        this.axi.add(Category.RENDER);
        this.axi.add(Category.PLAYER);
        this.axi.add(Category.EXPLOIT);
        for (Category category : this.axi) {
            ArrayList<ModuleComponent> arrayList = new ArrayList<ModuleComponent>();
            for (Module module : Client.a.g().getAll()) {
                if (module.getModuleInfo().category() != category) continue;
                arrayList.add(new ModuleComponent(module));
            }
            arrayList.sort((abd2, abd3) -> Collator.getInstance().compare(abd2.getModule().getName(), abd3.getModule().getName()));
            this.axh.put(category, new CategoryComponent(category, arrayList));
        }
    }

    public void drawScreen(int n2, int n3, float f2) {
        DropdownClickGUI.drawRect((int)0, (int)0, (int)this.width, (int)this.height, (int)new Color(0, 0, 0, 100).getRGB());
        boolean bl = this.axh.values().stream().anyMatch(CategoryComponent::oJ);
        if (bl) {
            int dWheel = Mouse.getDWheel();
            if (dWheel != 0) {
                double d2 = (dWheel > 0 ? -1 : 1) * 15;
                this.axk += d2;
                double d3 = this.or();
                this.axk = d3 <= 0.0 ? 0.0 : Math.max(0.0, Math.min(d3, this.axk));
            }
        } else {
            this.axk = 0.0;
            this.axj = 0.0;
            this.axl.setValue(0.0);
        }
        this.axl.Q(this.axk);
        this.axj = this.axl.getValue();
        if (this.axm != null) {
            double d4 = (double)n3 + this.axo + this.axj;
            this.axm.i((double)n2 + this.axn, d4);
        }
        if (((Boolean)Client.a.g().c(Interface.class).aoc.wo()).booleanValue()) {
            this.b(ShaderQueueType.BLUR).c(() -> {
                for (Category category : this.axi) {
                    CategoryComponent abb2 = this.axh.get((Object)category);
                    if (abb2 == null) continue;
                    double d2 = abb2.getX();
                    double d3 = abb2.getY() - this.axj;
                    if (d3 + abb2.da() < 0.0 || d3 > (double)this.height) continue;
                    RenderUtil.a(d2, d3, this.on(), 24.0 * this.axp, 6.0 * this.axp, UIColors.SECONDARY.pV(), true, true, false, false);
                    if (!abb2.oJ() || !(abb2.oK().getValue() > 1.0)) continue;
                    RenderUtil.a(d2, d3 + 24.0 * this.axp, this.on(), abb2.oK().getValue(), 6.0 * this.axp, UIColors.BACKGROUND.pV(), false, false, true, true);
                }
            });
            this.b(ShaderQueueType.BLOOM).c(() -> {
                for (Category category : this.axi) {
                    CategoryComponent abb2 = this.axh.get((Object)category);
                    if (abb2 == null) continue;
                    double d2 = abb2.getX();
                    double d3 = abb2.getY() - this.axj;
                    if (d3 + abb2.da() < 0.0 || d3 > (double)this.height) continue;
                    abb2.i(d2, d3, this.axp);
                }
            });
        }
        this.b(ShaderQueueType.REGULAR, 1).c(() -> {
            for (Category category : this.axi) {
                CategoryComponent abb2 = this.axh.get((Object)category);
                if (abb2 == null) continue;
                double d2 = abb2.getX();
                double d3 = abb2.getY() - this.axj;
                if (d3 + abb2.da() < 0.0 || d3 > (double)this.height) continue;
                abb2.a(d2, d3, this.on(), n2, n3, f2, this.axp);
            }
        });
        super.drawScreen(n2, n3, f2);
    }

    public void mouseClicked(int n2, int n3, int n4) {
        for (Category category : this.axi) {
            double d2;
            CategoryComponent abb2 = this.axh.get((Object)category);
            if (abb2 == null) continue;
            double d3 = abb2.getX();
            if (GUIUtil.c(d3, d2 = abb2.getY() - this.axj, this.on(), 24.0 * this.axp, n2, n3) && DropdownClickGUI.isShiftKeyDown() && n4 == 0) {
                this.axm = abb2;
                this.axn = d3 - (double)n2;
                this.axo = d2 - (double)n3;
                break;
            }
            if (!GUIUtil.c(d3, d2, this.on(), abb2.da(), n2, n3)) continue;
            abb2.a(d3, d2, n2, n3, n4);
            break;
        }
        super.mouseClicked(n2, n3, n4);
    }

    protected void mouseReleased(int n2, int n3, int n4) {
        this.axm = null;
        for (CategoryComponent abb2 : this.axh.values()) {
            abb2.oG();
        }
        super.mouseReleased(n2, n3, n4);
    }

    protected void keyTyped(char c2, int n2) {
        if (n2 == 1) {
            aEg.displayGuiScreen(null);
            return;
        }
        boolean ctrlKeyDown = DropdownClickGUI.isCtrlKeyDown();
        if (ctrlKeyDown) {
            if (n2 == 13 || n2 == 78) {
                this.E(this.axp + 0.1);
                return;
            }
            if (n2 == 12 || n2 == 74) {
                this.E(this.axp - 0.1);
                return;
            }
            if (n2 == 11) {
                this.E(0.8);
                return;
            }
        }
        for (CategoryComponent abb2 : this.axh.values()) {
            abb2.a(c2, n2);
        }
        super.keyTyped(c2, n2);
    }

    public boolean doesGuiPauseGame() {
        return false;
    }

    public void cj() {
        if (DropdownClickGUI.aEg.currentScreen != this) {
            return;
        }
    }

    public void ci() {
        if (DropdownClickGUI.aEg.currentScreen != this) {
            return;
        }
    }

    private double or() {
        double d2 = 0.0;
        double d3 = Double.MAX_VALUE;
        for (CategoryComponent abb2 : this.axh.values()) {
            double d4 = abb2.getY();
            double d5 = abb2.getY() + abb2.da();
            if (d4 < d3) {
                d3 = d4;
            }
            if (!(d5 > d2)) continue;
            d2 = d5;
        }
        double d6 = d2 - d3 + 40.0;
        return Math.max(0.0, d6 - (double)this.height);
    }

    @Generated
    public Map<Category, CategoryComponent> os() {
        return this.axh;
    }

    @Generated
    public List<Category> ot() {
        return this.axi;
    }

    @Generated
    public double ou() {
        return this.axj;
    }

    @Generated
    public double ov() {
        return this.axk;
    }

    @Generated
    public Animation ow() {
        return this.axl;
    }

    @Generated
    public CategoryComponent ox() {
        return this.axm;
    }

    @Generated
    public double oy() {
        return this.axn;
    }

    @Generated
    public double oz() {
        return this.axo;
    }

    @Generated
    public double oA() {
        return this.axp;
    }

    @Generated
    public double oB() {
        return this.axq;
    }

    @Generated
    public double oC() {
        return this.axr;
    }

    @Generated
    public double oD() {
        return this.axs;
    }

    @Generated
    public boolean oi() {
        return this.Mc;
    }
}
