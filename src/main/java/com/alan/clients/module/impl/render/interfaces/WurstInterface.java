package com.alan.clients.module.impl.render.interfaces;

import com.alan.clients.Client;
import com.alan.clients.module.impl.render.Interface;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TickEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Mode;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.localization.Locale;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.module.impl.render.interfaces.ArrayListEntry;
import java.awt.Color;
import net.minecraft.util.ResourceLocation;

public class WurstInterface extends Mode<Interface> {
    private Font arrayListFont;
    private final ResourceLocation resourceLocation = new ResourceLocation("rise/logo/wurst.png");
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        if (aEg != null && !aEg.gameSettings.bJf && aEg.theWorld != null && aEg.thePlayer != null) {
            this.getParent().n(this.arrayListFont.height() + 1.0F);
            this.getParent().setWidthComparator(this.arrayListFont);
            this.getParent().setEdgeOffset(4.0F);

            for (ArrayListEntry zc : this.getParent().getActiveEntries()) {
                if (zc.animationTime != 0.0F) {
                    double d0 = zc.getPosition().getX();
                    double d1 = zc.getPosition().getY();
                    Color color = Color.WHITE;
                    this.arrayListFont.b(zc.getTranslatedName(), d0, d1, color.getRGB());
                }
            }

            RenderUtil.d(0.0, 10.0, 185.0, 12.0, ColorUtil.withAlpha(Color.WHITE, 100));
            RenderUtil.image(this.resourceLocation, 2.0, 5.5, 89.17647F, 22.588236F);
            this.arrayListFont.a("v6 MC 1.8.9", 95.0, 14.0, Color.BLACK.getRGB());
        }
    };
    @EventLink
    public final Listener<TickEvent> onTick = var1x -> aMR.execute(() -> {
        for (ArrayListEntry zc : this.getParent().getActiveEntries()) {
            if (zc.animationTime != 0.0F) {
                String s = zc.getTranslatedName();
                zc.setNameWidth(this.arrayListFont.getStringWidth(s));
                zc.u(0.0F);
                zc.an("");
            }
        }
    });

    public WurstInterface(String var1, Interface var2) {
        super(var1, var2);
        this.arrayListFont = Client.a.getLocale() == Locale.ZH_ZH ? FontManager.MAIN.a(18, FontWeight.REGULAR) : aEg.fontRendererObj;
    }
}
