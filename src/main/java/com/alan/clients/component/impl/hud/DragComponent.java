package com.alan.clients.component.impl.hud;

import com.alan.clients.Client;
import com.alan.clients.component.Component;
import com.alan.clients.module.Module;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.GuiClickEvent;
import com.alan.clients.newevent.impl.input.GuiMouseReleaseEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.util.vector.Vector2d;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.util.MouseUtil;
import com.alan.clients.util.gui.GUIUtil;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.component.impl.hud.DragComponentSwitchMap;
import com.alan.clients.component.impl.hud.SnapAxis;
import com.alan.clients.component.impl.hud.SnapLine;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import rip.vantage.commons.util.time.StopWatch;

public class DragComponent extends Component {
    private static DragValue selectedValue = null;
    private static Vector2d offset;
    private static final ArrayList<Module> modules = new ArrayList<>();
    private static final Animation animationAlpha = new Animation(Easing.LINEAR, 600L);
    public static final StopWatch bV = new StopWatch();
    public static final StopWatch bW = new StopWatch();
    public static ArrayList<SnapLine> snaps = new ArrayList<>();
    public static SnapLine bY;
    @EventLink(value = -2)
    public final Listener<Render2DEvent> onRender2D = var0 -> {
        try {
            ScaledResolution scaledresolution = aEg.jY;
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            boolean flag = aEg.currentScreen instanceof GuiChat;
            if (!flag) {
                selectedValue = null;
            } else {
                bV.aX();
            }

            animationAlpha.setEasing(Easing.LINEAR);
            animationAlpha.setDuration(300L);
            animationAlpha.Q(flag ? 100.0 : 0.0);
            if (animationAlpha.getValue() <= 0.0 && bV.T(0L)) {
                selectedValue = null;
            }

            modules.clear();
            Client.a
                .g()
                .getAll()
                .stream()
                .filter(var0x -> var0x.isEnabled() && var0x.getValues().stream().anyMatch(var0xx -> var0xx instanceof DragValue))
                .forEach(modules::add);
            if (selectedValue != null) {
                Vector2d vector2d = MouseUtil.rU();
                double d0 = vector2d.x + offset.x;
                double d1 = vector2d.y + offset.y;
                selectedValue.atg = new Vector2d(d0, d1);
                snaps.clear();
                double d2 = Client.a.getThemeManager().getTheme().qd();
                snaps.add(new SnapLine(i / 2.0F, 5.0, SnapAxis.HORIZONTAL, true, true, true));
                snaps.add(new SnapLine(j / 2.0F, 5.0, SnapAxis.VERTICAL, true, true, true));
                snaps.add(new SnapLine(j - d2, 5.0, SnapAxis.VERTICAL, false, false, true));
                snaps.add(new SnapLine(d2, 5.0, SnapAxis.VERTICAL, false, true, false));
                snaps.add(new SnapLine(i - d2, 5.0, SnapAxis.HORIZONTAL, false, false, true));
                snaps.add(new SnapLine(d2, 5.0, SnapAxis.HORIZONTAL, false, true, false));
                Iterator iterator = modules.iterator();

                while (iterator.hasNext()) {
                    Optional optional = ((Module)iterator.next()).getValues().stream().filter(var0x -> var0x instanceof DragValue).findFirst();
                    DragValue dragvalue = (DragValue)optional.get();
                    if (dragvalue != selectedValue) {
                        snaps.add(new SnapLine(dragvalue.apP.x + dragvalue.aHe.x + d2, 5.0, SnapAxis.HORIZONTAL, false, true, false));
                        snaps.add(new SnapLine(dragvalue.apP.x - d2, 5.0, SnapAxis.HORIZONTAL, false, false, true));
                        snaps.add(new SnapLine(dragvalue.apP.y, 5.0, SnapAxis.VERTICAL, false, false, true));
                        snaps.add(new SnapLine(dragvalue.apP.y + dragvalue.aHe.y, 5.0, SnapAxis.VERTICAL, false, true, false));
                    }
                }

                bY = null;
                Color color = ColorUtil.withAlpha(Color.WHITE, 60);

                for (SnapLine av : snaps) {
                    switch (av.cm) {
                        case VERTICAL:
                            double d3 = Double.MAX_VALUE;
                            double d4 = -selectedValue.aHe.y;

                            for (; d4 <= 0.0; d4 += selectedValue.aHe.y / 2.0) {
                                if ((d4 != -selectedValue.aHe.y / 2.0 || av.cn) && (d4 != -selectedValue.aHe.y || av.cp) && (d4 != 0.0 || av.co)) {
                                    double d5 = Math.abs(selectedValue.atg.y - (av.ck + d4));
                                    if (d5 < av.cl && d5 < d3) {
                                        d3 = d5;
                                        selectedValue.atg.y = av.ck + d4;
                                        bY = av;
                                        RenderUtil.d(0.0, bY.ck, scaledresolution.getScaledWidth(), 0.5, color);
                                    }
                                }
                            }
                            break;
                        case HORIZONTAL:
                            double d6 = Double.MAX_VALUE;

                            for (double d7 = -selectedValue.aHe.x; d7 <= 0.0; d7 += selectedValue.aHe.x / 2.0) {
                                if ((d7 != -selectedValue.aHe.x / 2.0 || av.cn) && (d7 != -selectedValue.aHe.x || av.cp) && (d7 != 0.0 || av.co)) {
                                    double d8 = Math.abs(selectedValue.atg.x - (av.ck + d7));
                                    if (d8 < av.cl && d8 < d6) {
                                        d6 = d8;
                                        selectedValue.atg.x = av.ck + d7;
                                        bY = av;
                                        RenderUtil.d(bY.ck, 0.0, 0.5, scaledresolution.getScaledHeight(), color);
                                    }
                                }
                            }
                    }
                }
            }

            for (Module module : modules) {
                DragValue dragvalue1 = (DragValue)module.getValues().stream().filter(var0x -> var0x instanceof DragValue).findFirst().get();
                float f = Client.a.getThemeManager().getTheme().qd();
                dragvalue1.apP.x = Math.max(f, dragvalue1.apP.x);
                dragvalue1.apP.x = Math.min(i - dragvalue1.aHe.x - f, dragvalue1.apP.x);
                dragvalue1.apP.y = Math.max(f, dragvalue1.apP.y);
                dragvalue1.apP.y = Math.min(j - dragvalue1.aHe.y - f, dragvalue1.apP.y);
                dragvalue1.atg.x = Math.max(f, dragvalue1.atg.x);
                dragvalue1.atg.x = Math.min(i - dragvalue1.aHe.x - f, dragvalue1.atg.x);
                dragvalue1.atg.y = Math.max(f, dragvalue1.atg.y);
                dragvalue1.atg.y = Math.min(j - dragvalue1.aHe.y - f, dragvalue1.atg.y);
                dragvalue1.apP = new Vector2d(Math.min(i - dragvalue1.aHe.x - f, dragvalue1.atg.x), Math.min(j - dragvalue1.aHe.y - f, dragvalue1.atg.y));
            }

            bW.aX();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("exception");
        }
    };
    @EventLink
    public final Listener<GuiClickEvent> onGuiClick = var0 -> {
        if (var0.cN() == 0) {
            if (aEg.currentScreen instanceof GuiChat) {
                Iterator iterator = modules.iterator();

                while (iterator.hasNext()) {
                    for (Value value : ((Module)iterator.next()).getValues()) {
                        if (value instanceof DragValue dragvalue) {
                            Vector2d vector2d = dragvalue.apP;
                            Vector2d vector2d1 = dragvalue.aHe;
                            float f = var0.getMouseX();
                            float f1 = var0.getMouseY();
                            if (!dragvalue.aRb && GUIUtil.mouseOver(vector2d, vector2d1, f, f1)) {
                                selectedValue = dragvalue;
                                offset = new Vector2d(vector2d.x - f, vector2d.y - f1);
                            }
                        }
                    }
                }
            }
        }
    };
    @EventLink
    public final Listener<GuiMouseReleaseEvent> onGuiMouseRelease = var0 -> selectedValue = null;

    public DragComponent() {
    }
}
