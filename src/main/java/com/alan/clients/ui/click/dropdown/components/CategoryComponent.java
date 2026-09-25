package com.alan.clients.ui.click.dropdown.components;

import com.alan.clients.module.api.Category;
import com.alan.clients.ui.click.standard.UIColors;
import com.alan.clients.ui.click.standard.components.ModuleComponent;
import com.alan.clients.ui.click.standard.components.value.ValueComponent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.gui.GUIUtil;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.localization.Localization;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.lwjgl.input.Mouse;

public class CategoryComponent implements InstanceAccess {
    private final Category category;
    private final List<ModuleComponent> moduleComponents;
    private boolean axv = false;
    private final Animation axw = new Animation(Easing.EASE_OUT_QUINT, 300L);
    private final Animation axx = new Animation(Easing.LINEAR, 100L);
    private double x;
    private double y;
    private double axy;
    private double axz = 0.8;
    private static final double axA = 22.0;
    private static final double axB = 34.0;
    private static final double axC = 1.0;
    private static final double axD = 18.0;
    private final Map<Value<?>, Boolean> axE = new HashMap<>();
    private NumberValue axF = null;
    private double axG = 0.0;
    private double axH = 0.0;

    public CategoryComponent(Category category, List<ModuleComponent> moduleComponents) {
        this.category = category;
        this.moduleComponents = moduleComponents;
    }

    public void a(double var1, double var3, double var5, int var7, int var8, float var9, double var10) {
        this.axy = var5;
        this.axz = var10;
        double d0 = var1;
        double d1 = var3;
        if (this.axF != null) {
            if (Mouse.isButtonDown(0)) {
                double d2 = var7 - this.axG;
                double d3 = this.axF.getMax().doubleValue() - this.axF.getMin().doubleValue();
                double d4 = d3 / (var5 * 0.8);
                double d5 = this.axH + d2 * d4;
                double d6 = Math.max(this.axF.getMin().doubleValue(), Math.min(this.axF.getMax().doubleValue(), d5));
                int i = this.axF.getDecimalPlaces().intValue();
                double d7;
                if (i == 0) {
                    d7 = Math.round(d6);
                } else {
                    double d10 = Math.pow(10.0, i);
                    d7 = Math.round(d6 * d10) / d10;
                }

                this.axF.n(d7);
            } else {
                this.axF = null;
            }
        }

        double d8 = 0.0;
        if (this.axv) {
            for (ModuleComponent abdx : this.moduleComponents) {
                double d9 = d8 + 34.0 * var10;
                d8 = d9 + 1.0 * var10;
                if (abdx.isExpanded()) {
                    for (Value value : abdx.getModule().getAllValues()) {
                        if (this.d(value)) {
                            d8 += 18.0 * var10;
                            if (value instanceof ModeValue && this.axE.getOrDefault(value, false)) {
                                ModeValue modevalue = (ModeValue)value;
                                d8 += modevalue.getModes().size() * 18.0 * var10;
                            }
                        }
                    }

                    d8 += 1.0 * var10;
                }
            }

            d8 += 4.0 * var10;
        }

        this.axw.Q(this.axv ? d8 : 0.0);
        double d11 = this.axw.getValue();
        double d12 = 22.0 + d11;
        double d13 = 22.0 * var10;
        boolean flag = GUIUtil.c(d0, d1, var5, d13, var7, var8);
        this.axx.Q(flag ? 30.0 : 0.0);
        Color color = this.rz().rA();
        Color color1 = new Color(
            Math.min(255, UIColors.BACKGROUND.pV().getRed() + color.getRed() / 26),
            Math.min(255, UIColors.BACKGROUND.pV().getGreen() + color.getGreen() / 26),
            Math.min(255, UIColors.BACKGROUND.pV().getBlue() + color.getBlue() / 26),
            245
        );
        RenderUtil.a(d0, d1, var5, d13, 6.0 * var10, ColorUtil.withAlpha(UIColors.SECONDARY.pV(), 180), true, true, !this.axv, !this.axv);
        if (this.axx.getValue() > 0.0) {
            RenderUtil.a(d0, d1, var5, d13, 6.0 * var10, ColorUtil.withAlpha(Color.WHITE, (int)this.axx.getValue()), true, true, !this.axv, !this.axv);
        }

        String s = Localization.ce(this.category.getName());
        FontManager.MAIN.a((int)(16.0 * var10), FontWeight.BOLD).a(s, (float)(d0 + 8.0 * var10), (float)(d1 + 7.0 * var10), this.rz().rD().getRGB());
        String s1 = this.axv ? "▲" : "▼";
        FontManager.MAIN.a((int)(12.0 * var10), FontWeight.REGULAR).a(s1, (float)(d0 + var5 - 16.0 * var10), (float)(d1 + 7.0 * var10), UIColors.TRINARY_TEXT.pW());
        String s2 = "(" + this.moduleComponents.size() + ")";
        FontManager.MAIN.a((int)(12.0 * var10), FontWeight.REGULAR).drawCenteredString(s2, (float)(d0 + var5 - 26.0 * var10), (float)(d1 + 8.0 * var10), UIColors.TRINARY_TEXT.pW());
        if (d11 > 1.0) {
            if (this.axv) {
                RenderUtil.d(d0, d1 + d13 - 4.0 * var10, var5, 4.0 * var10, ColorUtil.withAlpha(UIColors.SECONDARY.pV(), 180));
            }

            RenderUtil.a(d0, d1 + d13, var5, d11, 6.0 * var10, color1, false, false, true, true);
            double d14 = d1 + d13 + 3.0 * var10;

            for (ModuleComponent moduleComponent : this.moduleComponents) {
                double d15 = 34.0;
                if (moduleComponent.isExpanded()) {
                    for (Value value1 : moduleComponent.getModule().getAllValues()) {
                        if (this.d(value1)) {
                            d15 += 18.0;
                            if (value1 instanceof ModeValue && this.axE.getOrDefault(value1, false)) {
                                ModeValue modevalue1 = (ModeValue)value1;
                                d15 += modevalue1.getModes().size() * 18.0;
                            }
                        }
                    }

                    d15 += 2.0;
                }

                if (!(d14 + d15 < d1) && !(d14 > d1 + d12)) {
                    double d17 = 34.0 * var10;
                    boolean flag1 = GUIUtil.c(d0 + 6.0 * var10, d14, var5 - 12.0 * var10, d17 - 2.0 * var10, var7, var8);
                    if (flag1) {
                        RenderUtil.roundedRectangle(d0 + 6.0 * var10, d14, var5 - 12.0 * var10, d17 - 2.0 * var10, 4.0 * var10, ColorUtil.withAlpha(Color.WHITE, 15));
                    }

                    if (moduleComponent.getModule().isEnabled()) {
                        RenderUtil.roundedRectangle(d0 + 6.0 * var10, d14, 2.0 * var10, d17 - 2.0 * var10, 1.0 * var10, this.rz().rD());
                    }

                    double d18 = d0 + 12.0 * var10;
                    double d19 = d14 + 4.0 * var10;
                    FontManager.MAIN
                        .a((int)(13.0 * var10), FontWeight.REGULAR)
                        .a(moduleComponent.getModule().getName(), (float)d18, (float)d19, moduleComponent.getModule().isEnabled() ? this.rz().rD().getRGB() : UIColors.TEXT.pW());
                    if (!moduleComponent.isExpanded()) {
                        String s3 = "(" + Localization.ce(moduleComponent.getModule().getModuleInfo().category().getName()) + ")";
                        FontManager.MAIN
                            .a((int)(10.0 * var10), FontWeight.REGULAR)
                            .a(
                                s3,
                                (float)(d18 + FontManager.MAIN.a((int)(13.0 * var10), FontWeight.REGULAR).getStringWidth(moduleComponent.getModule().getName()) + 4.0 * var10),
                                (float)(d19 + 1.0 * var10),
                                UIColors.TRINARY_TEXT.pW()
                            );
                    }

                    if (!moduleComponent.isExpanded()) {
                        String description = Localization.ce(moduleComponent.getModule().getModuleInfo().description());
                        int j = (int)(9.0 * var10);
                        float f = (float)(var5 - 24.0 * var10);
                        String[] astring = description.split(" ");
                        StringBuilder stringbuilder = new StringBuilder();
                        StringBuilder stringbuilder1 = new StringBuilder();
                        float f1 = 0.0F;
                        boolean flag2 = false;
                        String[] astring1 = astring;
                        int k = astring1.length;

                        for (byte b0 = 0; b0 < k; b0 += 1) {
                            String s5 = astring1[b0];
                            float f2 = FontManager.MAIN.a(j, FontWeight.REGULAR).getStringWidth(s5 + " ");
                            if (!flag2 && f1 + f2 > f && stringbuilder.length() > 0) {
                                flag2 = true;
                                f1 = 0.0F;
                            }

                            if (!flag2) {
                                stringbuilder.append(s5).append(" ");
                                f1 += f2;
                            } else {
                                if (!(f1 + f2 <= f)) {
                                    break;
                                }

                                stringbuilder1.append(s5).append(" ");
                                f1 += f2;
                            }
                        }

                        FontManager.MAIN.a(j, FontWeight.REGULAR).a(stringbuilder.toString().trim(), (float)d18, (float)(d19 + 11.0 * var10), UIColors.TRINARY_TEXT.pW());
                        if (stringbuilder1.length() > 0) {
                            FontManager.MAIN.a(j, FontWeight.REGULAR).a(stringbuilder1.toString().trim(), (float)d18, (float)(d19 + 19.0 * var10), UIColors.TRINARY_TEXT.pW());
                        }
                    }

                    double d20 = d14 + d17;
                    d14 = d20 + 1.0 * var10;
                    if (moduleComponent.isExpanded()) {
                        for (Value value2 : moduleComponent.getModule().getAllValues()) {
                            if (this.d(value2)) {
                                double d21 = 18.0 * var10;
                                boolean flag3 = GUIUtil.c(d0 + 10.0 * var10, d14, var5 - 20.0 * var10, d21, var7, var8);
                                if (flag3) {
                                    RenderUtil.roundedRectangle(
                                        d0 + 10.0 * var10, d14, var5 - 20.0 * var10, d21 - 2.0 * var10, 3.0 * var10, ColorUtil.withAlpha(Color.WHITE, 10)
                                    );
                                }

                                double d22 = d0 + 14.0 * var10;
                                double d23 = d14 + 4.0 * var10;
                                if (value2 instanceof BooleanValue booleanvalue) {
                                    String s6 = Localization.ce(value2.getName());
                                    FontManager.MAIN.a((int)(12.0 * var10), FontWeight.REGULAR).a(s6, (float)d22, (float)d23, UIColors.TEXT.pW());
                                    double d24 = d0 + var5 - 28.0 * var10;
                                    double d25 = d14 + 4.0 * var10;
                                    RenderUtil.roundedRectangle(d24, d25, 12.0 * var10, 12.0 * var10, 3.0 * var10, ColorUtil.withAlpha(UIColors.SECONDARY.pV(), 150));
                                    if (booleanvalue.wo()) {
                                        RenderUtil.roundedRectangle(d24 + 2.0 * var10, d25 + 2.0 * var10, 8.0 * var10, 8.0 * var10, 2.0 * var10, this.rz().rD());
                                    }
                                } else if (value2 instanceof ModeValue modevalue3) {
                                    String s7 = Localization.ce(value2.getName());
                                    FontManager.MAIN.a((int)(12.0 * var10), FontWeight.REGULAR).a(s7, (float)d22, (float)d23, UIColors.TEXT.pW());
                                    String s8 = modevalue3.wo().getName();
                                    FontManager.MAIN.a((int)(11.0 * var10), FontWeight.REGULAR).drawCenteredString(s8, (float)(d0 + var5 - 18.0 * var10), (float)d23, this.rz().rD().getRGB());
                                    String s9 = this.axE.getOrDefault(value2, false) ? "▲" : "▼";
                                    FontManager.MAIN.a((int)(10.0 * var10), FontWeight.REGULAR).a(s9, (float)(d0 + var5 - 16.0 * var10), (float)d23, UIColors.TRINARY_TEXT.pW());
                                } else if (value2 instanceof NumberValue numbervalue) {
                                    String s10 = Localization.ce(value2.getName());
                                    FontManager.MAIN.a((int)(12.0 * var10), FontWeight.REGULAR).a(s10, (float)d22, (float)d23, UIColors.TEXT.pW());
                                    double d26 = d0 + 12.0 * var10;
                                    double d27 = d14 + 18.0 * var10 - 6.0 * var10;
                                    double d28 = var5 - 24.0 * var10;
                                    double d29 = 2.0 * var10;
                                    RenderUtil.roundedRectangle(d26, d27, d28, d29, 1.5 * var10, ColorUtil.withAlpha(UIColors.SECONDARY.pV(), 100));
                                    double d30 = (numbervalue.wo().doubleValue() - numbervalue.getMin().doubleValue())
                                        / (numbervalue.getMax().doubleValue() - numbervalue.getMin().doubleValue());
                                    if (d30 > 0.0) {
                                        RenderUtil.roundedRectangle(d26, d27, d28 * d30, d29, 1.5 * var10, this.rz().rD());
                                    }

                                    String s11 = String.format("%." + numbervalue.getDecimalPlaces().intValue() + "f", numbervalue.wo().doubleValue());
                                    FontManager.MAIN
                                        .a((int)(11.0 * var10), FontWeight.REGULAR)
                                        .drawCenteredString(s11, (float)(d0 + var5 - 14.0 * var10), (float)(d23 - 2.0 * var10), UIColors.TRINARY_TEXT.pW());
                                }

                                d14 += d21;
                                if (value2 instanceof ModeValue && this.axE.getOrDefault(value2, false)) {
                                    ModeValue modevalue2 = (ModeValue)value2;

                                    for (Mode mode : modevalue2.getModes()) {
                                        boolean flag4 = GUIUtil.c(d0 + 16.0 * var10, d14, var5 - 32.0 * var10, d21, var7, var8);
                                        if (flag4) {
                                            RenderUtil.roundedRectangle(
                                                d0 + 16.0 * var10, d14, var5 - 32.0 * var10, d21 - 2.0 * var10, 3.0 * var10, ColorUtil.withAlpha(Color.WHITE, 12)
                                            );
                                        }

                                        boolean flag5 = modevalue2.wo() == mode;
                                        if (flag5) {
                                            RenderUtil.roundedRectangle(
                                                d0 + 16.0 * var10, d14, 2.0 * var10, d21 - 2.0 * var10, 1.0 * var10, this.rz().rD()
                                            );
                                        }
                                        FontManager.MAIN
                                            .a((int)(11.0 * var10), FontWeight.REGULAR)
                                            .a(
                                                mode.getName(),
                                                (float)(d0 + 22.0 * var10),
                                                (float)(d14 + 6.0 * var10),
                                                flag5 ? this.rz().rD().getRGB() : UIColors.TEXT.pW()
                                            );
                                        d14 += d21;
                                    }
                                }
                            }
                        }

                        d14 += 2.0 * var10;
                    }
                } else {
                    double d16 = d14 + d15;
                    d14 = d16 + 1.0;
                }
            }
        }
    }

    public void i(double var1, double var3, double var5) {
        String s = Localization.ce(this.category.getName());
        FontManager.MAIN.a((int)(16.0 * var5), FontWeight.BOLD).a(s, (float)(var1 + 8.0 * var5), (float)(var3 + 7.0 * var5), ColorUtil.withAlpha(this.rz().rA(), 200).getRGB());
        if (this.axv && this.axw.getValue() > 1.0) {
            double d0 = var3 + 22.0 * var5 + 3.0 * var5;

            for (ModuleComponent moduleComponent : this.moduleComponents) {
                if (moduleComponent.getModule().isEnabled()) {
                    RenderUtil.roundedRectangle(var1 + 6.0 * var5, d0, 2.0 * var5, 34.0 * var5 - 2.0 * var5, 1.0 * var5, ColorUtil.withAlpha(this.rz().rA(), 150));
                    FontManager.MAIN
                        .a((int)(13.0 * var5), FontWeight.REGULAR)
                        .a(moduleComponent.getModule().getName(), (float)(var1 + 12.0 * var5), (float)(d0 + 4.0 * var5), ColorUtil.withAlpha(this.rz().rA(), 120).getRGB());
                    if (moduleComponent.isExpanded()) {
                        moduleComponent.ci();
                    }
                }

                d0 += 34.0 * var5 + 1.0 * var5;
                if (moduleComponent.isExpanded()) {
                    for (Value value : moduleComponent.getModule().getAllValues()) {
                        if (this.d(value)) {
                            d0 += 18.0 * var5;
                            if (value instanceof ModeValue && this.axE.getOrDefault(value, false)) {
                                ModeValue modevalue = (ModeValue)value;
                                d0 += modevalue.getModes().size() * 18.0 * var5;
                            }
                        }
                    }

                    d0 += 2.0 * var5;
                }
            }
        }
    }

    public void a(double var1, double var3, int var5, int var6, int var7) {
        double d0 = this.axz;
        double d1 = 22.0 * d0;
        double d2 = 34.0 * d0;
        double d3 = 18.0 * d0;
        if (GUIUtil.c(var1, var3, this.axy, d1, var5, var6)) {
            if (var7 == 1) {
                this.axv = !this.axv;
            }
        } else {
            if (this.axv && this.axw.getValue() > 1.0) {
                double d4 = var3 + d1 + 3.0 * d0;

                for (ModuleComponent moduleComponent : this.moduleComponents) {
                    if (GUIUtil.c(var1 + 6.0 * d0, d4, this.axy - 12.0 * d0, d2, var5, var6)) {
                        if (var7 == 0) {
                            moduleComponent.getModule().toggle();
                        } else if (var7 == 1) {
                            moduleComponent.expanded = !moduleComponent.expanded;
                        }

                        return;
                    }

                    d4 += d2 + 1.0 * d0;
                    if (moduleComponent.isExpanded()) {
                        for (Value value : moduleComponent.getModule().getAllValues()) {
                            if (this.d(value)) {
                                if (GUIUtil.c(var1 + 10.0 * d0, d4, this.axy - 20.0 * d0, d3, var5, var6)) {
                                    this.a(value, var7);
                                    return;
                                }

                                d4 += d3;
                                if (value instanceof ModeValue && this.axE.getOrDefault(value, false)) {
                                    ModeValue modevalue = (ModeValue)value;

                                    for (Mode mode : modevalue.getModes()) {
                                        if (GUIUtil.c(var1 + 16.0 * d0, d4, this.axy - 32.0 * d0, d3, var5, var6)) {
                                            if (var7 == 0) {
                                                modevalue.n(mode);
                                            }

                                            return;
                                        }

                                        d4 += d3;
                                    }
                                }
                            }
                        }

                        d4 += 2.0 * d0;
                    }
                }
            }
        }
    }

    private void a(Value<?> var1, int var2) {
        if (var1 instanceof BooleanValue && var2 == 0) {
            BooleanValue booleanvalue = (BooleanValue)var1;
            booleanvalue.setValue(!booleanvalue.wo());
        } else if (var1 instanceof ModeValue && var2 == 0) {
            this.axE.put(var1, !this.axE.getOrDefault(var1, false));
        } else if (var1 instanceof NumberValue && var2 == 0) {
            NumberValue numbervalue = (NumberValue)var1;
            this.axF = numbervalue;
            this.axG = Mouse.getEventX() * aEg.currentScreen.width / aEg.displayWidth;
            this.axH = numbervalue.wo().doubleValue();
        }
    }

    private boolean d(Value<?> var1) {
        return var1.getHideIf() != null && var1.getHideIf().getAsBoolean() ? false : var1.getBooleanSupplier() == null || !var1.getBooleanSupplier().getAsBoolean();
    }

    public void oG() {
        this.axF = null;
        if (this.axv) {
            Iterator iterator = this.moduleComponents.iterator();

            while (iterator.hasNext()) {
                ((ModuleComponent)iterator.next()).pz();
            }
        }
    }

    public void a(char var1, int var2) {
        if (this.axv) {
            for (ModuleComponent moduleComponent : this.moduleComponents) {
                if (moduleComponent.isExpanded()) {
                    Iterator iterator = moduleComponent.getValueList().iterator();

                    while (iterator.hasNext()) {
                        ((ValueComponent)iterator.next()).key(var1, var2);
                    }
                }
            }
        }
    }

    public double da() {
        return 22.0 + this.axw.getValue();
    }

    public void i(double var1, double var3) {
        this.x = var1;
        this.y = var3;
    }

    @Generated
    public Category getCategory() {
        return this.category;
    }

    @Generated
    public List<ModuleComponent> getModuleComponents() {
        return this.moduleComponents;
    }

    @Generated
    public boolean oJ() {
        return this.axv;
    }

    @Generated
    public Animation oK() {
        return this.axw;
    }

    @Generated
    public Animation oL() {
        return this.axx;
    }

    @Generated
    public double getX() {
        return this.x;
    }

    @Generated
    public double getY() {
        return this.y;
    }

    @Generated
    public double oM() {
        return this.axy;
    }

    @Generated
    public double oN() {
        return this.axz;
    }

    @Generated
    public Map<Value<?>, Boolean> oO() {
        return this.axE;
    }

    @Generated
    public NumberValue getNumbervalue() {
        return this.axF;
    }

    @Generated
    public double oQ() {
        return this.axG;
    }

    @Generated
    public double oR() {
        return this.axH;
    }
}
