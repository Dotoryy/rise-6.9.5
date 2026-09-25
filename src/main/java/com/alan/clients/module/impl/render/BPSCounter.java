package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostStrafeEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.math.MathUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.shader.ShaderQueueType;
import java.awt.Color;

@ModuleInfo(aliases={"module.render.bpscounter.name"}, description="module.render.bpscounter.description", category=Category.RENDER)
public class BPSCounter
extends Module {
    public Vector3d lastPosition;
    public Vector2f scale;
    @EventLink
    public Listener<Render2DEvent> onRender2D;
    @EventLink
    public Listener<PostStrafeEvent> onPostStrafe;
    public DragValue position;
    public BooleanValue showTitle = new BooleanValue("Title", (Module)this, (Boolean)false);
    public String speed = "";


    static {
    }

    public BPSCounter() {
        this.position = new DragValue("Position", (Module)this, new Vector2d(200.0, 200.0));
        this.scale = new Vector2f(22.0f, 22.0f);
        this.lastPosition = new Vector3d(0.0, 0.0, 0.0);
        this.onPostStrafe = postStrafeEvent -> {
            this.speed = String.valueOf(MathUtil.round(new Vector3d(BPSCounter.aEg.thePlayer.posX, 0.0, BPSCounter.aEg.thePlayer.posZ).g(this.lastPosition) * 20.0 * (double)BPSCounter.aEg.timer.dzD, 2));
            this.lastPosition = new Vector3d(BPSCounter.aEg.thePlayer.posX, 0.0, BPSCounter.aEg.thePlayer.posZ);
        };
        this.onRender2D = render2DEvent -> {
            Vector2d vector2d = this.position.apP;
            String string = (Boolean)this.showTitle.wo() != false ? "BPS " : "";
            String string2 = this.speed;
            float f3 = FontManager.MAIN.a(20, FontWeight.BOLD).getStringWidth(string);
            this.scale.x = f3 + (float)FontManager.MAIN.a(20, FontWeight.REGULAR).getStringWidth(string2);
            this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                double d4 = vector2d.x;
                double d5 = vector2d.y;
                double d6 = this.scale.x + 6.0f;
                double d7 = this.scale.y - 1.0f;
                double round = this.rz().getRound();
                this.rz();
                RenderUtil.roundedRectangle(d4, d5, d6, d7, round, Themes.rK());
                this.position.n(new Vector2d(this.scale.x + 6.0f, this.scale.y - 1.0f));
                double d9 = vector2d.x + 3.0;
                double d10 = vector2d.y + (double)(this.scale.y / 2.05f) - (double)(FontManager.MAIN.a(20, FontWeight.REGULAR).height() / 4.0f);
                FontManager.MAIN.a(20, FontWeight.BOLD).b(string, d9, d10, this.rz().rA().getRGB());
                FontManager.MAIN.a(20, FontWeight.REGULAR).b(string2, d9 + (double)f3, d10, Color.WHITE.getRGB());
            });
            this.b(ShaderQueueType.BLUR).c(() -> RenderUtil.roundedRectangle(vector2d.x, vector2d.y, this.scale.x + 6.0f, this.scale.y - 1.0f, this.rz().getRound(), Color.BLACK));
            this.b(ShaderQueueType.BLOOM).c(() -> RenderUtil.roundedRectangle(vector2d.x + 0.5, vector2d.y + 0.5, this.scale.x + 6.0f - 1.0f, this.scale.y - 2.0f, this.rz().getRound() + 1, this.rz().rE()));
        };
    }
}
