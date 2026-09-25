package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.Client;
import com.alan.clients.component.impl.render.ParticleComponent;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.KillEvent;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.font.impl.minecraft.FontRenderer;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.render.particle.Particle;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.render.interfaces.ModernArrayListColorModeValue;
import com.alan.clients.module.impl.render.interfaces.ArrayListFontModeValue;
import com.alan.clients.module.impl.render.interfaces.ShaderEffectModeValue;
import com.alan.clients.module.impl.render.interfaces.BackGroundModeValue;
import com.alan.clients.module.impl.render.interfaces.ArrayListEntry;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import rip.vantage.commons.util.time.StopWatch;

public class ModernInterface
extends Mode<Interface> {
    private final Font medium36Font = FontManager.MAIN.a(36, FontWeight.MEDIUM);
    private final Font regular20Font = FontManager.MAIN.a(20, FontWeight.REGULAR);
    private final Font medium18Font = FontManager.MAIN.a(18, FontWeight.MEDIUM);
    private final Font regular18Font = FontManager.MAIN.a(18, FontWeight.REGULAR);
    Font arrayListFont = FontManager.MAIN.a(18, FontWeight.REGULAR);
    private final StopWatch particleStopWatch = new StopWatch();
    private final ModeValue arrayListColorMode = new ModernArrayListColorModeValue(this, "ArrayList Color Mode", this);
    private final ModeValue arrayListFontMode = new ArrayListFontModeValue(this, "ArrayList Font", this);
    private final StringValue customInstalledFont = new StringValue("Custom Installed Font", (Mode<?>)this, "Arial", () -> {
        if (((Mode)this.arrayListFontMode.wo()).getName().equals("Custom")) return false;
        return true;
    });
    private final ModeValue shaderEffect = new ShaderEffectModeValue(this, "Shader Effect", this);
    private final BooleanValue dropShadow = new BooleanValue("Drop Shadow", (Mode<?>)this, (Boolean)true);
    private final BooleanValue sidebar = new BooleanValue("Sidebar", (Mode<?>)this, (Boolean)true);
    private final BooleanValue particles = new BooleanValue("Particles on Kill", (Mode<?>)this, (Boolean)true);
    private final ModeValue background = new BackGroundModeValue(this, "BackGround", this);
    private final StringValue customClientName = new StringValue("Custom Client Name", (Mode<?>)this, "");
    private boolean glow;
    private boolean shadow;
    private boolean normalBackGround;
    private String coordinates;
    private float userWidth;
    private float xyzWidth;
    private Color logoColor = new Color(0);
    private final StopWatch particleCycleStopWatch = new StopWatch();
    @EventLink
    public final Listener<Render2DEvent> onRender2D = render2DEvent -> {
        double d2;
        if (aEg == null || ModernInterface.aEg.gameSettings.bJf || ModernInterface.aEg.theWorld == null || ModernInterface.aEg.thePlayer == null) {
            return;
        }
        boolean bl = this.arrayListFont == FontManager.MINECRAFT.dM();
        ((Interface)this.getParent()).n(this.arrayListFont.height() + (float)(bl ? 2 : 0));
        ((Interface)this.getParent()).setWidthComparator(this.arrayListFont);
        ((Interface)this.getParent()).setEdgeOffset(10.0f);
        float f2 = render2DEvent.getScaledResolution().getScaledWidth();
        float f3 = (float)render2DEvent.getScaledResolution().getScaledHeight() - this.arrayListFont.height() - 1.0f;
        double d3 = d2 = bl ? 3.5 : 2.0;
        if (this.glow || this.shadow) {
            this.b(ShaderQueueType.BLOOM).c(() -> {
                for (ArrayListEntry zc2 : ((Interface)this.getParent()).getActiveEntries()) {
                    if (zc2.animationTime == 0.0f) continue;
                    double d32 = zc2.getPosition().getX();
                    double d4 = zc2.getPosition().getY();
                    Color color = zc2.getColor();
                    if (this.normalBackGround) {
                        if (!bl) {
                            RenderUtil.d(d32 - d2, d4 - 3.0, (double)(zc2.nameWidth + zc2.tagWidth + 3.0f) + d2, ((Interface)this.getParent()).moduleSpacing, this.glow ? ColorUtil.withAlpha(color, 255) : this.rz().rE());
                        } else {
                            RenderUtil.d(d32 - d2 + 0.5, d4 - 3.0, (double)(zc2.nameWidth + zc2.tagWidth + 3.0f) + d2, ((Interface)this.getParent()).moduleSpacing, this.glow ? ColorUtil.withAlpha(color, 255) : this.rz().rE());
                        }
                    } else if (this.glow) {
                        this.drawEntry(zc2, d32 + 0.5, d4, color.getRGB());
                    } else if (this.shadow) {
                        this.arrayListFont.a(zc2.getDisplayName(), d32, d4, Color.BLACK.getRGB());
                        if (zc2.isHasTag()) {
                            this.arrayListFont.a(zc2.getDisplayTag(), d32 + (double)zc2.getNameWidth() + 3.0, d4, Color.BLACK.getRGB());
                        }
                    }
                    if (!((Boolean)this.sidebar.wo()).booleanValue()) continue;
                    RenderUtil.roundedRectangle(d32 + (double)zc2.getNameWidth() + (double)zc2.getTagWidth() + 2.0, d4 - 1.5, 2.0, 9.0, 1.0, color);
                }
                if (((Mode)this.arrayListFontMode.wo()).getName().equals("Minecraft")) {
                    FontRenderer unused0 = ModernInterface.aEg.fontRendererObj;
                    float f4 = 16.0f / (float)9;
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(f4, f4, f4);
                    ModernInterface.aEg.fontRendererObj.b(Client.b, 6.0f / f4, 6.0f / f4, this.rz().rA().getRGB());
                    GlStateManager.popMatrix();
                } else {
                    ColorUtil.a(this.medium36Font, Client.b, 6.0, 6.0, true);
                    this.medium18Font.b("", 39.0, 6.0, ColorUtil.withAlpha(Color.WHITE, 170).getRGB());
                }
                if (!((String)this.customClientName.wo()).isEmpty()) {
                    this.medium18Font.a((String)this.customClientName.wo(), (double)(6 + this.medium36Font.getStringWidth(Client.b) + 2), 6.0, this.rz().rB().getRGB());
                }
                Font agc2 = FontManager.MAIN.a(18, FontWeight.MEDIUM);
                Font agc3 = FontManager.MAIN.a(18, FontWeight.BOLD);
                String string = "6.9.5";
                String string2 = rip.vantage.network.core.VantageNetwork.aKB().bX();
                String string3 = "User:";
                float f5 = agc2.getStringWidth("Version:\u2009\u2009\u2009\u2009\u2009\u2009");
                float f6 = agc3.getStringWidth(string);
                float f7 = this.regular20Font.getStringWidth(string3);
                float f8 = this.regular20Font.getStringWidth(string2);
                float f9 = f2 - (f5 + f6 + f7 + f8) - 6.0f;
                this.regular18Font.b("Version:\u2009\u2009\u2009\u2009\u2009\u2009", f9, f3, -3355444);
                agc3.b(string, f9 + f5, f3, -3355444);
                float f10 = f9 + f5 + f6 + 5.0f;
                this.regular18Font.b(string3, f10, f3, -3355444);
                this.medium18Font.b(string2, f10 + f7, f3, -3355444);
                this.regular18Font.b("XYZ:", 5.0, f3, -3355444);
                this.medium18Font.b(this.coordinates, 5.0f + this.xyzWidth, f3, -3355444);
                FontManager.MAIN.a(16, FontWeight.MEDIUM);
                Font agc4 = FontManager.MAIN.a(16, FontWeight.BOLD);
                Collection<PotionEffect> collection = ModernInterface.aEg.thePlayer.getActivePotionEffects();
                if (!collection.isEmpty()) {
                    String string4;
                    Font agc5 = this.regular18Font;
                    ArrayList<String[]> arrayList = new ArrayList<String[]>();
                    for (PotionEffect potionEffect : collection) {
                        if (potionEffect.getDuration() > 24000) continue;
                        string4 = this.toRomanNumeral(potionEffect.getAmplifier() + 1);
                        String string5 = I18n.format(potionEffect.getEffectName(), new Object[0]) + " " + string4;
                        String string6 = Potion.getDurationString(potionEffect);
                        arrayList.add(new String[]{string5, string6});
                    }
                    arrayList.sort((stringArray, stringArray2) -> Float.compare(agc4.getStringWidth(stringArray2[0]) + agc4.getStringWidth(" " + stringArray2[1]), agc4.getStringWidth(stringArray[0]) + agc4.getStringWidth(" " + stringArray[1])));
                    for (int i = 0; i < arrayList.size(); ++i) {
                        String string7 = ((String[])arrayList.get(i))[0];
                        string4 = ((String[])arrayList.get(i))[1];
                        float f11 = agc4.getStringWidth(string7) + agc5.getStringWidth(" " + string4);
                        float f12 = f2 - f11 - 5.0f;
                        float f13 = f3 - (agc5.height() + 5.0f) * (float)(i + 1);
                        agc4.b(string7, f12, f13, -3355444);
                        agc4.b(" " + string4, f12 + (float)agc4.getStringWidth(string7), f13, -3355444);
                    }
                }
            });
        }
        for (ArrayListEntry zc2 : ((Interface)this.getParent()).getActiveEntries()) {
            if (zc2.animationTime == 0.0f) continue;
            double d4 = zc2.getPosition().getX();
            double d5 = zc2.getPosition().getY();
            Color color2 = zc2.getColor();
            if (this.normalBackGround) {
                Consumer<Color> consumer = color -> {
                    if (!bl) {
                        RenderUtil.d(d4 - d2, d5 - 3.0, (double)(zc2.nameWidth + zc2.tagWidth + 3.0f) + d2, ((Interface)this.getParent()).moduleSpacing, color);
                        return;
                    }
                    RenderUtil.d(d4 - d2 + 0.5, d5 - 3.0, (double)(zc2.nameWidth + zc2.tagWidth + 3.0f) + d2, ((Interface)this.getParent()).moduleSpacing, color);
                };
                this.b(ShaderQueueType.BLUR).c(() -> consumer.accept(Color.BLACK));
                this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                    this.rz();
                    consumer.accept(Themes.rK());
                });
            }
            this.b(ShaderQueueType.REGULAR, 1).c(() -> this.drawEntry(zc2, d4, d5 - 0.5, color2.getRGB()));
            if (!((Boolean)this.sidebar.wo()).booleanValue()) continue;
            RenderUtil.roundedRectangle(d4 + (double)zc2.getNameWidth() + (double)zc2.getTagWidth() + 2.0, d5 - 1.5, 2.0, 9.0, 1.0, color2);
        }
        if (this.coordinates == null) {
            return;
        }
        if (!this.particleStopWatch.T(2000L)) {
            this.b(ShaderQueueType.BLOOM).c(ParticleComponent::cj);
        }
        String string = rip.vantage.network.core.VantageNetwork.aKB().bX();
        String string2 = "User:";
        String string3 = "6.9.5";
        Font agc2 = FontManager.MAIN.a(18, FontWeight.MEDIUM);
        Font agc3 = FontManager.MAIN.a(18, FontWeight.BOLD);
        float f4 = agc2.getStringWidth("Version:\u2009\u2009\u2009\u2009\u2009\u2009");
        float f5 = agc3.getStringWidth(string3);
        float f6 = this.regular20Font.getStringWidth(string2);
        float f7 = this.regular20Font.getStringWidth(string);
        float f8 = f2 - (f4 + f5 + f6 + f7) - 6.0f;
        this.regular18Font.b("Version:\u2009\u2009\u2009\u2009\u2009\u2009", f8, f3, -3355444);
        agc3.b(string3, f8 + f4, f3, -3355444);
        float f9 = f8 + f4 + f5 + 5.0f;
        this.regular18Font.b(string2, f9, f3, -3355444);
        this.medium18Font.b(string, f9 + f6, f3, -3355444);
        this.userWidth = f6 + f7;
        this.regular18Font.b("XYZ:", 5.0, f3, -3355444);
        this.medium18Font.b(this.coordinates, 5.0f + this.xyzWidth, f3, -3355444);
        FontManager.MAIN.a(16, FontWeight.MEDIUM);
        Font agc4 = FontManager.MAIN.a(16, FontWeight.BOLD);
        Collection<PotionEffect> collection = ModernInterface.aEg.thePlayer.getActivePotionEffects();
        if (!collection.isEmpty()) {
            Font agc5 = this.regular18Font;
            ArrayList<String[]> arrayList = new ArrayList<String[]>();
            for (PotionEffect potionEffect2 : collection) {
                if (potionEffect2.getDuration() > 24000) continue;
                String string5 = this.toRomanNumeral(potionEffect2.getAmplifier() + 1);
                String string6 = I18n.format(potionEffect2.getEffectName(), new Object[0]) + " " + string5;
                String string4 = Potion.getDurationString(potionEffect2);
                arrayList.add(new String[]{string6, string4});
            }
            arrayList.sort((stringArray, stringArray2) -> Float.compare(agc4.getStringWidth(stringArray2[0]) + agc4.getStringWidth(" " + stringArray2[1]), agc4.getStringWidth(stringArray[0]) + agc4.getStringWidth(" " + stringArray[1])));
            float f10 = 10.0f;
            float f11 = -3.7f;
            float f12 = 9.0f;
            for (int i = 0; i < arrayList.size(); ++i) {
                String string4 = ((String[])arrayList.get(i))[0];
                String string7 = ((String[])arrayList.get(i))[1];
                PotionEffect potionEffect3 = collection.stream().filter(potionEffect -> {
                    String string22 = this.toRomanNumeral(potionEffect.getAmplifier() + 1);
                    return I18n.format(potionEffect.getEffectName(), new Object[0]).equals(string4.replace(" " + string22, ""));
                }).findFirst().orElse(null);
                if (potionEffect3 == null) continue;
                float f13 = f2 - (float)agc4.getStringWidth(string4) - (float)agc5.getStringWidth(" " + string7) - f10 - f12 - 5.0f;
                float f14 = f3 - (agc5.height() + 5.0f) * (float)(i + 1);
                this.drawPotionIcon(potionEffect3.getPotionID(), f13 + 4.0f, f14 + f11, 13.0f);
                agc4.b(string4, f13 + f10 + f12, f14, -3355444);
                agc4.b(" " + string7, f13 + f10 + f12 + (float)agc4.getStringWidth(string4), f14, -3355444);
            }
        }
        if (((Mode)this.arrayListFontMode.wo()).getName().equals("Minecraft")) {
            FontRenderer unused0 = ModernInterface.aEg.fontRendererObj;
            float f15 = 16.0f / (float)9;
            GlStateManager.pushMatrix();
            GlStateManager.scale(f15, f15, f15);
            ModernInterface.aEg.fontRendererObj.b(Client.b, 6.0f / f15, 6.0f / f15, this.rz().rA().getRGB());
            GlStateManager.popMatrix();
        } else {
            ColorUtil.a(this.medium36Font, Client.b, 6.0, 6.0, true);
            this.medium18Font.b("", 39.0, 6.0, ColorUtil.withAlpha(Color.WHITE, 170).getRGB());
        }
        this.medium18Font.b("", 39.0, 6.0, ColorUtil.withAlpha(Color.WHITE, 170).getRGB());
        if (!((String)this.customClientName.wo()).isEmpty()) {
            this.medium18Font.a((String)this.customClientName.wo(), (double)(6 + this.medium36Font.getStringWidth(Client.b) + 2), 6.0, this.rz().rB().getRGB());
        }
        if (this.particleCycleStopWatch.T(7500L)) {
            this.particleStopWatch.aX();
            this.particleCycleStopWatch.aX();
        }
    };
    @EventLink
    public final Listener<KillEvent> onKill = killEvent -> {
        if (!this.particleStopWatch.T(2000L) && ((Boolean)this.particles.wo()).booleanValue()) {
            for (int i = 0; i <= 10; ++i) {
                ParticleComponent.a(new Particle(new Vector2f(0.0f, 0.0f), new Vector2f((float)Math.random(), (float)Math.random())));
            }
        }
        this.particleStopWatch.aX();
    };
    @EventLink
    public final Listener<TickEvent> onTick = tickEvent -> {
        if (ModernInterface.aEg.thePlayer == null || !ModernInterface.aEg.getNetHandler().doneLoadingTerrain) {
            return;
        }
        try {
            if (((Mode)this.arrayListFontMode.wo()).getName().equals("Custom") && this.arrayListFont != FontManager.CUSTOM.o(18)) {
                this.arrayListFont = FontManager.CUSTOM.o(18);
            }
        } catch (Exception exception) {}
        aMR.execute(() -> {
            block105: {
                block104: {
                    {
                        this.glow = ((Mode)this.shaderEffect.wo()).getName().equals("Glow");
                        this.shadow = ((Mode)this.shaderEffect.wo()).getName().equals("Shadow");
                        this.userWidth = this.regular20Font.getStringWidth("riseclient.com") + 2;
                        this.coordinates = (int)Math.floor(ModernInterface.aEg.thePlayer.posX) + ", " + (int)Math.floor(ModernInterface.aEg.thePlayer.posY) + ", " + (int)Math.floor(ModernInterface.aEg.thePlayer.posZ);
                        this.xyzWidth = this.medium18Font.getStringWidth("XYZ:") + 2;
                        this.logoColor = this.rz().rA();
                        this.normalBackGround = ((Mode)this.background.wo()).getName().equals("Normal");
                        String string = ((Mode)this.arrayListFontMode.wo()).getName();
                        switch (string) {
                            case "Apple UI": {
                                Font agc2 = FontManager.MAIN.a(18, FontWeight.REGULAR);
                                if (!this.arrayListFont.equals(agc2)) {
                                    this.arrayListFont = agc2;
                                }
                                break block105;
                            }
                            case "Minecraft": {
                                break;
                            }
                            case "Custom": {
                                break block104;
                            }
                            default: {
                                break block105;
                            }
                        }
                    }
                    Font agc3 = FontManager.MINECRAFT.dM();
                    if (!this.arrayListFont.equals(agc3)) {
                        this.arrayListFont = agc3;
                    }
                    break block105;
                }
                String string2 = (String)this.customInstalledFont.wo();
                if (Math.random() > 0.95) {
                    Optional<String> optional = FontManager.dN().stream().filter(string3 -> ModernInterface.fontNameMatches(string2, string3)).findFirst();
                    if (optional.isPresent() && !FontManager.CUSTOM.getName().equals(optional.get())) {
                        FontManager.CUSTOM.setName((String)optional.get());
                        FontManager.CUSTOM.dO().clear();
                    }
                }
            }
            for (ArrayListEntry zc2 : ((Interface)this.getParent()).getActiveEntries()) {
                if (zc2.animationTime == 0.0f) continue;
                String string4;
                String string5;
                Color color;
                block80: {
                    {
                        zc2.y(!zc2.getTag().isEmpty() && ((Boolean)((Interface)this.getParent()).suffix.wo()).booleanValue());
                        string4 = (((Boolean)((Interface)this.getParent()).lowercase.wo()).booleanValue() ? zc2.getTranslatedName().toLowerCase() : zc2.getTranslatedName()).replace(((Boolean)((Interface)this.getParent()).getRemoveSpaces().wo()).booleanValue() ? " " : "", "");
                        string5 = (((Boolean)((Interface)this.getParent()).lowercase.wo()).booleanValue() ? zc2.getTag().toLowerCase() : zc2.getTag()).replace(((Boolean)((Interface)this.getParent()).getRemoveSpaces().wo()).booleanValue() ? " " : "", "");
                        color = this.rz().rA();
                        String string6 = ((Mode)this.arrayListColorMode.wo()).getName();
                        switch (string6) {
                            case "Breathe": {
                                double d2 = this.rz().getBlendFactor(new Vector2d(0.0, 0.0));
                                color = ColorUtil.a(color, this.rz().rB(), d2);
                                break block80;
                            }
                            case "Fade": {
                                break;
                            }
                            default: {
                                break block80;
                            }
                        }
                    }
                    color = this.rz().getAccentColor(new Vector2d(0.0, zc2.getPosition().getY()));
                }
                zc2.setColor(color);
                zc2.setNameWidth(this.arrayListFont.getStringWidth(string4));
                zc2.u(zc2.isHasTag() ? this.arrayListFont.getStringWidth(string5) + 3 : 0.0f);
                zc2.setDisplayName(string4);
                zc2.setDisplayTag(string5);
            }
        });
    };

    public ModernInterface(String string, Interface interface_) {
        super(string, interface_);
    }

    private String toRomanNumeral(int n2) {
        if (n2 < 1) {
            return String.valueOf(n2);
        }
        if (n2 > 255) {
            n2 = 255;
        }
        StringBuilder stringBuilder = new StringBuilder();
        int[] nArray = new int[]{100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] stringArray = new String[]{"C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        while (n2 >= 100) {
            stringBuilder.append("C");
            n2 -= 100;
        }
        for (int i = 0; i < nArray.length; ++i) {
            while (n2 >= nArray[i]) {
                stringBuilder.append(stringArray[i]);
                n2 -= nArray[i];
            }
        }
        return stringBuilder.toString();
    }

    private void drawPotionIcon(int n2, float f2, float f3, float f4) {
        Potion potion = Potion.potionTypes[n2];
        if (potion == null || !potion.hasStatusIcon()) {
            return;
        }
        ResourceLocation resourceLocation = GuiContainer.inventoryBackground;
        int statusIconIndex = potion.getStatusIconIndex();
        int n4 = statusIconIndex % 8 * 18;
        int n5 = 198 + statusIconIndex / 8 * 19;
        RenderUtil.a(resourceLocation, f2, f3, n4, n5, 18, 18, 256, 256, f4, f4);
    }

    public static String lettersOnly(String string) {
        return string.replaceAll("[^a-zA-Z]", "");
    }

    private void drawEntry(ArrayListEntry zc2, double d2, double d3, int n2) {
        if (((Boolean)this.dropShadow.wo()).booleanValue()) {
            this.arrayListFont.b(zc2.getDisplayName(), d2, d3, n2);
            if (!zc2.isHasTag()) return;
            this.arrayListFont.b(zc2.getDisplayTag(), d2 + (double)zc2.getNameWidth() + 3.0, d3, -3355444);
            return;
        }
        this.arrayListFont.a(zc2.getDisplayName(), d2, d3, n2);
        if (!zc2.isHasTag()) return;
        this.arrayListFont.a(zc2.getDisplayTag(), d2 + (double)zc2.getNameWidth() + 3.0, d3, -3355444);
    }

    private static  boolean fontNameMatches(String string, String string2) {
        return ModernInterface.lettersOnly(string2).toLowerCase().contains(ModernInterface.lettersOnly(string).toLowerCase());
    }
}
