package com.alan.clients.util.font.impl.minecraft;

import com.alan.clients.util.font.Font;
import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.src.a;
import net.minecraft.util.ResourceLocation;
import net.optifine.render.f;
import net.optifine.s;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;

public class FontRenderer extends Font implements IResourceManagerReloadListener {
    private static final ResourceLocation[] unicodePageLocations = new ResourceLocation[256];
    private final int[] charWidth = new int[256];
    public static int aHU = 9;
    public Random aHV = new Random();
    private final byte[] glyphWidth = new byte[65536];
    private final int[] colorCode = new int[32];
    private ResourceLocation locationFontTexture;
    private final TextureManager renderEngine;
    private float aIa;
    private float aIb;
    private boolean unicodeFlag;
    private boolean bidiFlag;
    private float red;
    private float blue;
    private float green;
    private float alpha;
    private int textColor;
    private boolean randomStyle;
    private boolean boldStyle;
    private boolean italicStyle;
    private boolean underlineStyle;
    private boolean strikethroughStyle;
    public GameSettings gameSettings;
    public ResourceLocation locationFontTextureBase;
    public float offsetBold = 1.0F;
    private final float[] charWidthFloat = new float[256];
    private boolean blend = false;
    private final f blendState = new f();

    public FontRenderer(GameSettings gameSettings, ResourceLocation location, TextureManager renderEngine, boolean var4) {
        this.gameSettings = gameSettings;
        this.locationFontTextureBase = location;
        this.locationFontTexture = location;
        this.renderEngine = renderEngine;
        this.unicodeFlag = var4;
        this.locationFontTexture = net.optifine.util.l.R(this.locationFontTextureBase);
        this.bindTexture(this.locationFontTexture);

        for (int i = 0; i < 32; i++) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;
            if (i == 6) {
                k += 85;
            }

            if (gameSettings.anaglyph) {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            this.colorCode[i] = (k & 0xFF) << 16 | (l & 0xFF) << 8 | i1 & 0xFF;
        }

        this.readGlyphSizes();
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        this.locationFontTexture = net.optifine.util.l.R(this.locationFontTextureBase);

        for (int i = 0; i < unicodePageLocations.length; i++) {
            unicodePageLocations[i] = null;
        }

        this.readFontTexture();
        this.readGlyphSizes();
    }

    private void readFontTexture() {
        BufferedImage bufferedimage;
        try {
            bufferedimage = TextureUtil.readBufferedImage(this.getResourceInputStream(this.locationFontTexture));
        } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        }

        Properties properties = net.optifine.util.l.Q(this.locationFontTexture);
        this.blend = net.optifine.util.l.b(properties, "blend", false);
        int i = bufferedimage.getWidth();
        int j = bufferedimage.getHeight();
        int k = i / 16;
        int l = j / 16;
        float f = i / 128.0F;
        float f1 = a.F(f, 1.0F, 2.0F);
        this.offsetBold = 1.0F / f1;
        float f2 = net.optifine.util.l.a(properties, "offsetBold", -1.0F);
        if (f2 >= 0.0F) {
            this.offsetBold = f2;
        }

        int[] aint = new int[i * j];
        bufferedimage.getRGB(0, 0, i, j, aint, 0, i);

        for (int i1 = 0; i1 < 256; i1++) {
            int j1 = i1 % 16;
            int k1 = i1 / 16;
            int l1 = 0;

            for (l1 = k - 1; l1 >= 0; l1--) {
                int i2 = j1 * k + l1;
                boolean flag = true;

                for (int j2 = 0; j2 < l && flag; j2++) {
                    int k2 = (k1 * l + j2) * i;
                    if ((aint[i2 + k2] >> 24 & 0xFF) > 16) {
                        flag = false;
                        break;
                    }
                }

                if (!flag) {
                    break;
                }
            }

            if (i1 == 65) {
                i1 = i1;
            }

            if (i1 == 32) {
                if (k <= 8) {
                    l1 = (int)(2.0F * f);
                } else {
                    l1 = (int)(1.5F * f);
                }
            }

            this.charWidthFloat[i1] = (l1 + 1) / f + 1.0F;
        }

        net.optifine.util.l.a(properties, this.charWidthFloat);

        for (int l2 = 0; l2 < this.charWidth.length; l2++) {
            this.charWidth[l2] = Math.round(this.charWidthFloat[l2]);
        }
    }

    private void readGlyphSizes() {
        InputStream inputstream = null;

        try {
            inputstream = this.getResourceInputStream(new ResourceLocation("font/glyph_sizes.bin"));
            inputstream.read(this.glyphWidth);
        } catch (IOException ioexception) {
            throw new RuntimeException(ioexception);
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }

    private float renderChar(char var1, boolean var2) {
        if (var1 != ' ' && var1 != 160) {
            int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
                .indexOf(var1);
            return i != -1 && !this.unicodeFlag ? this.renderDefaultChar(i, var2) : this.renderUnicodeChar(var1, var2);
        }
        return !this.unicodeFlag ? this.charWidthFloat[var1] : 4.0F;
    }

    private float renderDefaultChar(int var1, boolean var2) {
        int i = var1 % 16 * 8;
        int j = var1 / 16 * 8;
        int k = var2 ? 1 : 0;
        this.bindTexture(this.locationFontTexture);
        float f = this.charWidthFloat[var1];
        GL11.glBegin(5);
        GL11.glTexCoord2f(i / 128.0F, j / 128.0F);
        GL11.glVertex3f(this.aIa + k, this.aIb, 0.0F);
        GL11.glTexCoord2f(i / 128.0F, (j + 7.99F) / 128.0F);
        GL11.glVertex3f(this.aIa - k, this.aIb + 7.99F, 0.0F);
        GL11.glTexCoord2f((i + 7.99F - 1.0F) / 128.0F, j / 128.0F);
        GL11.glVertex3f(this.aIa + 7.99F - 1.0F + k, this.aIb, 0.0F);
        GL11.glTexCoord2f((i + 7.99F - 1.0F) / 128.0F, (j + 7.99F) / 128.0F);
        GL11.glVertex3f(this.aIa + 7.99F - 1.0F - k, this.aIb + 7.99F, 0.0F);
        GL11.glEnd();
        return f;
    }

    private ResourceLocation getUnicodePageLocation(int var1) {
        if (unicodePageLocations[var1] == null) {
            unicodePageLocations[var1] = new ResourceLocation(String.format("textures/font/unicode_page_%02x.png", var1));
            unicodePageLocations[var1] = net.optifine.util.l.R(unicodePageLocations[var1]);
        }

        return unicodePageLocations[var1];
    }

    private void loadGlyphTexture(int var1) {
        this.bindTexture(this.getUnicodePageLocation(var1));
    }

    private float renderUnicodeChar(char var1, boolean var2) {
        if (this.glyphWidth[var1] == 0) {
            return 0.0F;
        }

        int i = var1 / 256;
        this.loadGlyphTexture(i);
        int j = this.glyphWidth[var1] >>> 4;
        int k = this.glyphWidth[var1] & 15;
        float f = j;
        float f1 = k + 1;
        float f2 = var1 % 16 * 16 + f;
        float f3 = (var1 & 255) / 16 * 16;
        float f4 = f1 - f - 0.02F;
        float f5 = var2 ? 1.0F : 0.0F;
        GL11.glBegin(5);
        GL11.glTexCoord2f(f2 / 256.0F, f3 / 256.0F);
        GL11.glVertex3f(this.aIa + f5, this.aIb, 0.0F);
        GL11.glTexCoord2f(f2 / 256.0F, (f3 + 15.98F) / 256.0F);
        GL11.glVertex3f(this.aIa - f5, this.aIb + 7.99F, 0.0F);
        GL11.glTexCoord2f((f2 + f4) / 256.0F, f3 / 256.0F);
        GL11.glVertex3f(this.aIa + f4 / 2.0F + f5, this.aIb, 0.0F);
        GL11.glTexCoord2f((f2 + f4) / 256.0F, (f3 + 15.98F) / 256.0F);
        GL11.glVertex3f(this.aIa + f4 / 2.0F - f5, this.aIb + 7.99F, 0.0F);
        GL11.glEnd();
        return (f1 - f) / 2.0F + 1.0F;
    }

    @Override
    public int b(String var1, double var2, double var4, int var6) {
        return this.b(var1, var2, var4, var6, true);
    }

    @Override
    public int a(String var1, double var2, double var4, int var6) {
        return this.b(var1, var2, var4, var6, false);
    }

    @Override
    public int b(String var1, double var2, double var4, int var6, boolean var7) {
        var4--;
        this.enableAlpha();
        if (this.blend) {
            GlStateManager.b(this.blendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
        }

        this.resetStyles();
        int i;
        if (var7) {
            i = this.renderString(var1, (float)var2 + 1.0F, (float)var4 + 1.0F, var6, true);
            i = Math.max(i, this.renderString(var1, (float)var2, (float)var4, var6, false));
        } else {
            i = this.renderString(var1, (float)var2, (float)var4, var6, false);
        }

        if (this.blend) {
            GlStateManager.c(this.blendState);
        }

        return i;
    }

    private String bidiReorder(String var1) {
        try {
            Bidi bidi = new Bidi(new ArabicShaping(8).shape(var1), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        } catch (ArabicShapingException arabicshapingexception) {
            return var1;
        }
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private void renderStringAtPos(String var1, boolean var2) {
        for (int i = 0; i < var1.length(); i++) {
            char c0 = var1.charAt(i);
            if (c0 == 167 && i + 1 < var1.length()) {
                int j = "0123456789abcdefklmnor".indexOf(var1.toLowerCase(Locale.ENGLISH).charAt(i + 1));
                if (j < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (j < 0) {
                        j = 15;
                    }

                    if (var2) {
                        j += 16;
                    }

                    int k = this.colorCode[j];
                    if (a.aoh()) {
                        k = s.bB(j, k);
                    }

                    this.textColor = k;
                    this.setColor((k >> 16) / 255.0F, (k >> 8 & 0xFF) / 255.0F, (k & 0xFF) / 255.0F, this.alpha);
                } else if (j == 17) {
                    this.boldStyle = true;
                } else if (j == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }

                i++;
            } else {
                float f = !this.unicodeFlag ? this.offsetBold : 0.5F;
                boolean flag = (c0 == 0 || this.unicodeFlag) && var2;
                if (flag) {
                    this.aIa -= f;
                    this.aIb -= f;
                }

                float f1 = this.renderChar(c0, this.italicStyle);
                if (flag) {
                    this.aIa += f;
                    this.aIb += f;
                }

                if (this.boldStyle) {
                    this.aIa += f;
                    if (flag) {
                        this.aIa -= f;
                        this.aIb -= f;
                    }

                    this.renderChar(c0, this.italicStyle);
                    this.aIa -= f;
                    if (flag) {
                        this.aIa += f;
                        this.aIb += f;
                    }

                    f1 += f;
                }

                this.doDraw(f1);
            }
        }
    }

    protected void doDraw(float var1) {
        if (this.underlineStyle) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION);
            int i = this.underlineStyle ? -1 : 0;
            worldrenderer.pos(this.aIa + i, this.aIb + 9, 0.0).endVertex();
            worldrenderer.pos(this.aIa + var1, this.aIb + 9, 0.0).endVertex();
            worldrenderer.pos(this.aIa + var1, this.aIb + 9 - 1.0F, 0.0).endVertex();
            worldrenderer.pos(this.aIa + i, this.aIb + 9 - 1.0F, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }

        this.aIa += var1;
    }

    private int renderStringAligned(String var1, int var2, int var3, int var4, int var5, boolean var6) {
        if (this.bidiFlag) {
            int i = this.getStringWidth(this.bidiReorder(var1));
            var2 = var2 + var4 - i;
        }

        return this.renderString(var1, (float)var2, (float)var3, var5, var6);
    }

    private int renderString(String var1, float var2, float var3, int var4, boolean var5) {
        if (var1 == null) {
            return 0;
        }

        if (this.bidiFlag) {
            var1 = this.bidiReorder(var1);
        }

        if ((var4 & -67108864) == 0) {
            var4 |= -16777216;
        }

        if (var5) {
            var4 = (var4 & 16579836) >> 2 | var4 & 0xFF000000;
        }

        this.red = (var4 >> 16 & 0xFF) / 255.0F;
        this.blue = (var4 >> 8 & 0xFF) / 255.0F;
        this.green = (var4 & 0xFF) / 255.0F;
        this.alpha = (var4 >> 24 & 0xFF) / 255.0F;
        this.setColor(this.red, this.blue, this.green, this.alpha);
        this.aIa = var2;
        this.aIb = var3;
        this.renderStringAtPos(var1, var5);
        return (int)this.aIa;
    }

    @Override
    public int getStringWidth(String var1) {
        if (var1 == null) {
            return 0;
        }

        float f = 0.0F;
        boolean flag = false;

        for (int i = 0; i < var1.length(); i++) {
            char c0 = var1.charAt(i);
            float f1 = this.getCharWidthFloat(c0);
            if (f1 < 0.0F && i < var1.length() - 1) {
                c0 = var1.charAt(++i);
                if (c0 == 'l' || c0 == 'L') {
                    flag = true;
                } else if (c0 == 'r' || c0 == 'R') {
                    flag = false;
                }

                f1 = 0.0F;
            }

            f += f1;
            if (flag && f1 > 0.0F) {
                f += this.unicodeFlag ? 1.0F : this.offsetBold;
            }
        }

        return Math.round(f);
    }

    @Override
    public int drawString(String var1, double var2, double var4, int var6) {
        return this.b(var1, var2 - (this.getStringWidth(var1) >> 1), var4, var6, false);
    }

    @Override
    public int drawCenteredString(String var1, double var2, double var4, int var6) {
        return this.b(var1, var2 - this.getStringWidth(var1), var4, var6, false);
    }

    @Override
    public float height() {
        return 9;
    }

    @Override
    public void a(char var1, int var2, int var3, Color color) {
        this.b(String.valueOf(var1), var2, var3, color.getRGB());
    }

    public int b(char var1) {
        return Math.round(this.getCharWidthFloat(var1));
    }

    private float getCharWidthFloat(char var1) {
        if (var1 == 167) {
            return -1.0F;
        }

        if (var1 != ' ' && var1 != 160) {
            int i = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000"
                .indexOf(var1);
            if (var1 > 0 && i != -1 && !this.unicodeFlag) {
                return this.charWidthFloat[i];
            }

            if (this.glyphWidth[var1] != 0) {
                int j = this.glyphWidth[var1] >>> 4;
                int k = this.glyphWidth[var1] & 15;
                if (k > 7) {
                    k = 15;
                    j = 0;
                }

                k++;
                return (k - j) / 2 + 1;
            }
            return 0.0F;
        } else {
            return this.charWidthFloat[32];
        }
    }

    public String trimStringToWidth(String var1, int var2) {
        return this.trimStringToWidth(var1, var2, false);
    }

    public String trimStringToWidth(String var1, int var2, boolean var3) {
        StringBuilder stringbuilder = new StringBuilder();
        float f = 0.0F;
        int i = var3 ? var1.length() - 1 : 0;
        int j = var3 ? -1 : 1;
        boolean flag = false;
        boolean flag1 = false;

        for (int k = i; k >= 0 && k < var1.length() && f < var2; k += j) {
            char c0 = var1.charAt(k);
            float f1 = this.getCharWidthFloat(c0);
            if (flag) {
                flag = false;
                if (c0 == 'l' || c0 == 'L') {
                    flag1 = true;
                } else if (c0 == 'r' || c0 == 'R') {
                    flag1 = false;
                }
            } else if (f1 < 0.0F) {
                flag = true;
            } else {
                f += f1;
                if (flag1) {
                    f++;
                }
            }

            if (f > var2) {
                break;
            }

            if (var3) {
                stringbuilder.insert(0, c0);
            } else {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

    private String trimStringNewline(String var1) {
        while (var1 != null && var1.endsWith("\n")) {
            var1 = var1.substring(0, var1.length() - 1);
        }

        return var1;
    }

    public void a(String var1, int var2, int var3, int var4, int var5) {
        if (this.blend) {
            GlStateManager.b(this.blendState);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(770, 771);
        }

        this.resetStyles();
        this.textColor = var5;
        var1 = this.trimStringNewline(var1);
        this.renderSplitString(var1, var2, var3, var4, false);
        if (this.blend) {
            GlStateManager.c(this.blendState);
        }
    }

    private void renderSplitString(String var1, int var2, int var3, int var4, boolean var5) {
        for (String s : this.listFormattedStringToWidth(var1, var4)) {
            this.renderStringAligned(s, var2, var3, var4, this.textColor, var5);
            var3 += 9;
        }
    }

    public int l(String var1, int var2) {
        return 9 * this.listFormattedStringToWidth(var1, var2).size();
    }

    public void C(boolean var1) {
        this.unicodeFlag = var1;
    }

    public boolean tu() {
        return this.unicodeFlag;
    }

    public void D(boolean var1) {
        this.bidiFlag = var1;
    }

    public List<String> listFormattedStringToWidth(String var1, int var2) {
        return Arrays.asList(this.wrapFormattedStringToWidth(var1, var2).split("\n"));
    }

    String wrapFormattedStringToWidth(String var1, int var2) {
        if (var1.length() <= 1) {
            return var1;
        }

        int i = this.sizeStringToWidth(var1, var2);
        if (var1.length() <= i) {
            return var1;
        }

        String s = var1.substring(0, i);
        char c0 = var1.charAt(i);
        boolean flag = c0 == ' ' || c0 == '\n';
        String s1 = getFormatFromString(s) + var1.substring(i + (flag ? 1 : 0));
        return s + "\n" + this.wrapFormattedStringToWidth(s1, var2);
    }

    private int sizeStringToWidth(String var1, int var2) {
        int i = var1.length();
        float f = 0.0F;
        int j = 0;
        int k = -1;
        boolean flag = false;

        while (j < i) {
            char c0 = var1.charAt(j);
            switch (c0) {
                case '\n':
                    j--;
                    break;
                case ' ':
                    k = j;
                default:
                    f += this.b(c0);
                    if (flag) {
                        f++;
                    }
                    break;
                case '§':
                    if (j < i - 1) {
                        char c1 = var1.charAt(++j);
                        if (c1 == 'l' || c1 == 'L') {
                            flag = true;
                        } else if (c1 == 'r' || c1 == 'R' || isFormatColor(c1)) {
                            flag = false;
                        }
                    }
            }

            if (c0 == '\n') {
                k = ++j;
                break;
            }

            if (Math.round(f) > var2) {
                break;
            }

            j++;
        }

        return j != i && k != -1 && k < j ? k : j;
    }

    private static boolean isFormatColor(char var0) {
        return var0 >= '0' && var0 <= '9' || var0 >= 'a' && var0 <= 'f' || var0 >= 'A' && var0 <= 'F';
    }

    private static boolean isFormatSpecial(char var0) {
        return var0 >= 'k' && var0 <= 'o' || var0 >= 'K' && var0 <= 'O' || var0 == 'r' || var0 == 'R';
    }

    public static String getFormatFromString(String var0) {
        StringBuilder stringbuilder = new StringBuilder();
        int i = -1;
        int j = var0.length();

        while ((i = var0.indexOf(167, i + 1)) != -1) {
            if (i < j - 1) {
                char c0 = var0.charAt(i + 1);
                if (isFormatColor(c0)) {
                    stringbuilder = new StringBuilder("§" + c0);
                } else if (isFormatSpecial(c0)) {
                    stringbuilder.append("§").append(c0);
                }
            }
        }

        return stringbuilder.toString();
    }

    public boolean tv() {
        return this.bidiFlag;
    }

    public int getColorCode(char var1) {
        int i = "0123456789abcdef".indexOf(var1);
        if (i >= 0 && i < this.colorCode.length) {
            int j = this.colorCode[i];
            if (a.aoh()) {
                j = s.bB(i, j);
            }

            return j;
        }
        return 16777215;
    }

    protected void setColor(float var1, float var2, float var3, float var4) {
        GlStateManager.color(var1, var2, var3, var4);
    }

    protected void enableAlpha() {
        GlStateManager.enableAlpha();
    }

    protected void bindTexture(ResourceLocation location) {
        this.renderEngine.bindTexture(location);
    }

    protected InputStream getResourceInputStream(ResourceLocation location) throws IOException {
        return Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream();
    }
}
