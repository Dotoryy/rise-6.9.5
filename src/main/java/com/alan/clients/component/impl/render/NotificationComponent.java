package com.alan.clients.component.impl.render;

import com.alan.clients.component.Component;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.font.Font;
import com.alan.clients.util.font.FontManager;
import com.alan.clients.util.font.FontWeight;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.util.tuples.Triple;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.util.type.EvictingList;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import rip.vantage.commons.util.time.StopWatch;

public class NotificationComponent extends Component {
    private static final EvictingList<Triple<String, String, Integer>> queue = new EvictingList<>(5);
    private static final StopWatch time = new StopWatch();
    private static Triple<String, String, Integer> current;
    private static final Animation animation = new Animation(Easing.EASE_OUT_EXPO, 900L);
    private static final Vector2d SCALE = new Vector2d(140.0, 30.0);
    private static final Vector2d ICON_SCALE = new Vector2d(20.0, 20.0);
    private static final Vector2d POSITION = new Vector2d(5.0, 126.0);
    private static final double SPACER = (SCALE.y - ICON_SCALE.y) / 2.0;
    private static final Font bold = FontManager.MAIN.a(15, FontWeight.BOLD);
    private static final Font light = FontManager.MAIN.a(15, FontWeight.LIGHT);
    @EventLink(value = 4)
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (current != null) {
            boolean flag = time.T(current.getThird().intValue());
            animation.Q(flag ? 1.1 : 1.0);
            animation.setDuration(500L);
            animation.setEasing(Easing.EASE_OUT_EXPO);
            double d0 = animation.getValue();
            double d1 = 1.0 - 10.0 * Math.abs(1.0 - animation.getValue());
            if (!animation.isFinished() || !flag) {
                this.b(ShaderQueueType.REGULAR, 1).c(() -> {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((POSITION.x + SCALE.x / 2.0) * (1.0 - d0), (POSITION.y + SCALE.y / 2.0) * (1.0 - d0), 0.0);
                    GlStateManager.scale(d0, d0, 0.0);
                    double d2 = POSITION.x;
                    double d3 = POSITION.y;
                    double d4 = SCALE.x;
                    double d5 = SCALE.y;
                    this.rz();
                    Color color = Themes.rK();
                    this.rz();
                    RenderUtil.roundedRectangle(d2, d3, d4, d5, 10.0, ColorUtil.withAlpha(color, (int)(Themes.rK().getAlpha() * d1)));
                    RenderUtil.roundedRectangle(POSITION.x + SPACER, POSITION.y + SPACER, ICON_SCALE.x, ICON_SCALE.y, 6.0, ColorUtil.withAlpha(Color.WHITE, (int)(255.0 * d1)));
                    bold.b(current.getFirst(), POSITION.x + SPACER + ICON_SCALE.x + SPACER, POSITION.y + SPACER + 3.0, ColorUtil.withAlpha(this.rz().rA(), (int)(255.0 * d1)).getRGB());
                    light.b(current.getSecond(), POSITION.x + SPACER + ICON_SCALE.x + SPACER, POSITION.y + SPACER + 0.5 + SPACER * 0.7 + bold.height(), ColorUtil.withAlpha(Color.WHITE, (int)(255.0 * d1)).getRGB());
                    GlStateManager.popMatrix();
                });
                this.b(ShaderQueueType.BLOOM)
                    .c(
                        () -> {
                            GlStateManager.pushMatrix();
                            GlStateManager.translate((POSITION.x + SCALE.x / 2.0) * (1.0 - d0), (POSITION.y + SCALE.y / 2.0) * (1.0 - d0), 0.0);
                            GlStateManager.scale(d0, d0, 0.0);
                            RenderUtil.roundedRectangle(
                                POSITION.x + 0.5, POSITION.y + 0.5, SCALE.x - 1.0, SCALE.y - 1.0, 11.0, ColorUtil.withAlpha(this.rz().rE(), (int)(this.rz().rE().getAlpha() * d1))
                            );
                            GlStateManager.popMatrix();
                        }
                    );
                this.b(ShaderQueueType.BLUR).c(() -> {
                    if (!(Math.abs(animation.getValue() - 1.0) > 0.045)) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((POSITION.x + SCALE.x / 2.0) * (1.0 - d0), (POSITION.y + SCALE.y / 2.0) * (1.0 - d0), 0.0);
                        GlStateManager.scale(d0, d0, 0.0);
                        RenderUtil.roundedRectangle(POSITION.x, POSITION.y, SCALE.x, SCALE.y, 10.0, ColorUtil.withAlpha(Color.BLACK, (int)(255.0 * d1)));
                        GlStateManager.popMatrix();
                    }
                });
            }
        }
    };
    @EventLink(value = 4)
    public final Listener<PreMotionEvent> onPreMotion = var0 -> {
        if (aEg.thePlayer.ticksExisted % 5 == 0) {
            if (!queue.isEmpty() && (current == null || time.T(current.getThird() + 200))) {
                if (current != null) {
                    queue.remove(current);
                }

                if (!queue.isEmpty()) {
                    current = queue.get(0);
                    time.aX();
                }

                SCALE.x = Math.max(140.0, light.getStringWidth(current.getSecond()) + SPACER * 3.0 + ICON_SCALE.x + 2.0);
            }
        }
    };

    public NotificationComponent() {
    }

    public static void e(String var0, String var1) {
        a(var0, var1, 3000);
    }

    public static void a(String var0, String var1, Integer var2) {
        queue.add(new Triple<>(var0, var1, var2));
    }
}
