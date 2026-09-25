package com.alan.clients.component.impl.hud.dragcomponent.api;

import com.alan.clients.module.Module;
import com.alan.clients.ui.click.standard.components.value.impl.PositionValueComponent;
import com.alan.clients.util.animation.Animation;
import com.alan.clients.util.animation.Easing;
import com.alan.clients.util.interfaces.InstanceAccess;
import com.alan.clients.util.render.RenderUtil;
import com.alan.clients.value.Value;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.value.impl.ColorValue;
import com.alan.clients.value.impl.DragValue;
import com.alan.clients.value.impl.ListValue;
import com.alan.clients.value.impl.ModeValue;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.value.impl.StringValue;
import com.alan.clients.ui.click.standard.components.value.ValueComponent;
import com.alan.clients.ui.theme.Themes;
import com.alan.clients.util.render.ColorUtil;
import com.alan.clients.util.shader.ShaderQueueType;
import com.alan.clients.ui.click.standard.components.value.impl.BooleanValueComponent;
import com.alan.clients.ui.click.standard.components.value.impl.BoundsNumberValueComponent;
import com.alan.clients.ui.click.standard.components.value.impl.ColorValueComponent;
import com.alan.clients.ui.click.standard.components.value.impl.ListValueComponent;
import com.alan.clients.ui.click.standard.components.value.impl.ModeValueComponent;
import com.alan.clients.ui.click.standard.components.value.impl.NumberValueComponent;
import com.alan.clients.ui.click.standard.components.value.impl.StringValueComponent;
import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.client.renderer.GlStateManager;

public class SettingsRenderer implements InstanceAccess {
    public Module module;
    public DragValue positionValue;
    public Animation animation = new Animation(Easing.EASE_OUT_ELASTIC, 300L);
    public boolean close;
    public ArrayList<ValueComponent> valueList = new ArrayList<>();

    public SettingsRenderer(Module module, DragValue positionValue) {
        this.module = module;
        this.positionValue = positionValue;

        for (Value value : module.getAllValues()) {
            if (value instanceof ModeValue) {
                this.valueList.add(new ModeValueComponent(value));
            } else if (value instanceof BooleanValue) {
                this.valueList.add(new BooleanValueComponent(value));
            } else if (value instanceof StringValue) {
                this.valueList.add(new StringValueComponent(value));
            } else if (value instanceof NumberValue) {
                this.valueList.add(new NumberValueComponent(value));
            } else if (value instanceof BoundsNumberValue) {
                this.valueList.add(new BoundsNumberValueComponent(value));
            } else if (value instanceof DragValue) {
                this.valueList.add(new PositionValueComponent(value));
            } else if (value instanceof ListValue) {
                this.valueList.add(new ListValueComponent(value));
            } else if (value instanceof ColorValue) {
                this.valueList.add(new ColorValueComponent(value));
            }
        }
    }

    public void render(int var1, int var2, float var3) {
        double d0 = 100.0;
        double d1 = 100.0;
        this.animation.setDuration(300L);
        this.animation.setEasing(this.close ? Easing.EASE_IN_EXPO : Easing.EASE_OUT_EXPO);
        this.animation.Q(this.close ? 0.0 : 1.0);
        double d2 = this.animation.getValue();
        if (!(d2 <= 1.0E-4)) {
            this.b(ShaderQueueType.REGULAR).c(() -> {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.positionValue.apP.x * (1.0 - d2), (this.positionValue.apP.y + this.positionValue.aHe.y / 2.0) * (1.0 - d2), 0.0);
                GlStateManager.scale(d2, d2, 1.0);
                double d3 = this.positionValue.apP.x - d0 - 10.0;
                double d4 = this.positionValue.apP.y + this.positionValue.aHe.y / 2.0 - d1 / 2.0;
                double round = this.rz().getRound();
                this.rz();
                Color color = Themes.rK();
                double d6 = this.animation.getValue();
                this.rz();
                RenderUtil.roundedRectangle(d3, d4, d0, d1, round, ColorUtil.withAlpha(color, (int)(d6 * Themes.rK().getAlpha())));

                for (ValueComponent valueComponent : this.valueList) {
                    if (valueComponent.getValue() != null && valueComponent.getValue().getHideIf() != null && valueComponent.getValue().getHideIf().getAsBoolean()) {
                    }
                }

                GlStateManager.popMatrix();
            });
            this.b(ShaderQueueType.BLUR).c(() -> {
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.positionValue.apP.x * (1.0 - d2), (this.positionValue.apP.y + this.positionValue.aHe.y / 2.0) * (1.0 - d2), 0.0);
                GlStateManager.scale(d2, d2, 1.0);
                RenderUtil.roundedRectangle(this.positionValue.apP.x - d0 - 10.0, this.positionValue.apP.y + this.positionValue.aHe.y / 2.0 - d1 / 2.0, d0, d1, this.rz().getRound(), Color.BLACK);
                GlStateManager.popMatrix();
            });
        }
    }

    public void close() {
        this.close = true;
    }
}
