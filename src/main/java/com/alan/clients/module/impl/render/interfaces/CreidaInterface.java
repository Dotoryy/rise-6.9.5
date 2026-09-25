package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.Client;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.module.impl.render.interfaces.ArrayListEntry;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.minecraft.client.renderer.GlStateManager;

public final class CreidaInterface extends Mode<Interface> {
    private final BooleanValue font = new BooleanValue("Font", this, true);
    private static final Font minecraftFont = FontManager.MINECRAFT.dM();
    private static final double rowHeight = minecraftFont.height() + 2.0F;
    private static final int LINE_WIDTH = 1;
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        for (ArrayListEntry zc : this.getParent().getActiveEntries()) {
            float f = this.font.wo() ? 11.0F : 10.0F;
            this.getParent().n(aEg.fontRendererObj.height() + 3.0F);
            float f1;
            int i = (f1 = this.getParent().moduleSpacing - f) == 0.0F ? 0 : (f1 < 0.0F ? -1 : 1);
            this.getParent().setEdgeOffset(3.0F);
            double d0 = zc.getPosition().getX() - 1.0 - 2.0;
            double d1 = zc.getPosition().getY() + 1.0;
            double d2 = zc.nameWidth + zc.tagWidth + 2.0F + 3.0F;
            this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.d(d0 - 1.0, d1 - 2.0, d2, rowHeight + 1.0, new Color(0, 0, 0, 255)));
            this.b(ShaderQueueType.BLOOM).c(() -> RenderUtil.d(d0 - 1.0, d1 - 2.0, d2, rowHeight + 1.0, new Color(0, 0, 0, 255)));
            this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                RenderUtil.d(d0 - 1.0, d1 - 2.0, d2, rowHeight + 1.0, new Color(0, 0, 0, 110));
                RenderUtil.d(d0 + zc.nameWidth + zc.tagWidth + 4.0, d1 - 2.0, 1.0, rowHeight + 1.0, this.rz().getAccentColor(new Vector2d(zc.getPosition().getX(), zc.getPosition().getY() / 1.5)));
                this.drawEntry(zc, d0 + 1.0, d1, this.rz().getAccentColor(new Vector2d(zc.getPosition().getX(), zc.getPosition().getY() / 1.5)).getRGB());
            });
        }

        this.b(ShaderQueueType.REGULAR).c(() -> {
            Date date = new Date();
            String s = new SimpleDateFormat("HH:mm:ss").format(date);
            String s1 = Client.b + " Client | 6.9.5 | " + aEg.thePlayer.getName() + " | " + s;
            RenderUtil.roundedRectangle(2.0, 3.0, (this.font.wo() ? 0 : 8) + this.getFont().getStringWidth(s1), 15.0, 4.0, new Color(0, 0, 0, 100));
            GlStateManager.resetColor();
            int j = 0;

            for (int k = 0; k < s1.length(); k++) {
                char c0 = s1.charAt(k);
                this.getFont().b(String.valueOf(c0), 6 + j, 8.0, this.rz().getAccentColor(new Vector2d(k, k)).getRGB());
                j += this.getFont().getStringWidth(String.valueOf(c0));
            }
        });
        this.b(ShaderQueueType.BLUR).c(() -> {
            Date date = new Date();
            String s = new SimpleDateFormat("HH:mm:ss").format(date);
            String s1 = Client.b + " Client | 6.9.5 | " + aEg.thePlayer.getName() + " | " + s;
            RenderUtil.roundedRectangle(2.0, 3.0, (this.font.wo() ? 0 : 8) + this.getFont().getStringWidth(s1), 15.0, 4.0, new Color(0, 0, 0, 255));
        });
        this.b(ShaderQueueType.BLOOM).c(() -> {
            Date date = new Date();
            String s = new SimpleDateFormat("HH:mm:ss").format(date);
            String s1 = Client.b + " Client | 6.9.5 | " + aEg.thePlayer.getName() + " | " + s;
            RenderUtil.roundedRectangle(2.0, 3.0, (this.font.wo() ? 0 : 8) + this.getFont().getStringWidth(s1) - 1, 15.0, 5.0, new Color(0, 0, 0, 255));
        });
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> {
        for (ArrayListEntry zc : this.getParent().getActiveEntries()) {
            if (zc.animationTime != 0.0F) {
                zc.y(!zc.getTag().isEmpty() && this.getParent().suffix.wo());
                String s = (this.getParent().lowercase.wo() ? zc.getTranslatedName().toLowerCase() : zc.getTranslatedName()).replace(this.getParent().getRemoveSpaces().wo() ? " " : "", "");
                String s1 = (this.getParent().lowercase.wo() ? zc.getTag().toLowerCase() : zc.getTag()).replace(this.getParent().getRemoveSpaces().wo() ? " " : "", "");
                zc.setNameWidth(this.getFont().getStringWidth(s));
                zc.u(zc.isHasTag() ? this.getFont().getStringWidth(s1) + 1 + 3 : 1.0F);
                zc.setDisplayName(s);
                zc.setDisplayTag(s1);
            }
        }
    };

    public CreidaInterface(String var1, Interface var2) {
        super(var1, var2);
    }

    private Font getFont() {
        return this.font.wo() ? FontManager.MAIN.a(24, FontWeight.REGULAR) : FontManager.MINECRAFT.dM();
    }

    private void drawEntry(ArrayListEntry var1, double var2, double var4, int var6) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        this.getFont().b(var1.getDisplayName(), var2, var4, var6);
        if (var1.isHasTag()) {
            this.getFont().b(var1.getDisplayTag(), var2 + var1.getNameWidth() + 1.0 + 3.0, var4, -3355444);
        }

        GlStateManager.disableBlend();
    }
}
