package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;
import net.minecraft.client.Minecraft;

@ModuleInfo(aliases = "module.render.fpscounter.name", description = "module.render.fpscounter.description", category = Category.RENDER)
public final class FPSCounter extends Module {
    private final BooleanValue showTitle = new BooleanValue("Title", this, false);
    private final DragValue position = new DragValue("Position", this, new Vector2d(200.0, 200.0));
    private final Vector2f scale = new Vector2f(22.0F, 22.0F);
    private int lastFps;
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        Vector2d vector2d = this.position.apP;
        String s = this.showTitle.wo() ? "FPS " : "";
        String s1 = Minecraft.getDebugFPS() + "";
        float f = FontManager.MAIN.a(20, FontWeight.BOLD).getStringWidth(s);
        if (Minecraft.getDebugFPS() != this.lastFps) {
            this.scale.x = f + FontManager.MAIN.a(20, FontWeight.REGULAR).getStringWidth(s1);
        }

        this.lastFps = Minecraft.getDebugFPS();
        this.b(ShaderQueueType.REGULAR, 1).c(() -> {
            double d2 = vector2d.x;
            double d3 = vector2d.y;
            double d4 = this.scale.x + 6.0F;
            double d5 = this.scale.y - 1.0F;
            double d6 = this.rz().getRound();
            this.rz();
            RenderUtil.roundedRectangle(d2, d3, d4, d5, d6, Themes.rK());
            this.position.n(new Vector2d(this.scale.x + 6.0F, this.scale.y - 1.0F));
            double d0 = vector2d.x + 3.0;
            double d1 = vector2d.y + this.scale.y / 2.05F - FontManager.MAIN.a(20, FontWeight.REGULAR).height() / 4.0F;
            FontManager.MAIN.a(20, FontWeight.BOLD).b(s, d0, d1, this.rz().rA().getRGB());
            FontManager.MAIN.a(20, FontWeight.REGULAR).b(s1, d0 + f, d1, Color.WHITE.getRGB());
        });
        this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.roundedRectangle(vector2d.x, vector2d.y, this.scale.x + 6.0F, this.scale.y - 1.0F, this.rz().getRound() + 1, Color.BLACK));
        this.b(ShaderQueueType.BLOOM)
            .c(
                () -> RenderUtil.roundedRectangle(
                    vector2d.x + 0.5, vector2d.y + 0.5, this.scale.x + 6.0F - 1.0F, this.scale.y - 2.0F, this.rz().getRound() + 1, this.rz().rE()
                )
            );
    };

    public FPSCounter() {
    }
}
