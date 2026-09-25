package com.alan.clients.module.impl.ghost;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.module.impl.ghost.autoclicker.DragClickSimulationsAutoClicker;
import com.alan.clients.module.impl.ghost.autoclicker.NormalAutoClicker;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.input.ClickEvent;
import com.alan.clients.newevent.impl.render.Render3DEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.ModeValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import rip.vantage.commons.util.time.StopWatch;

@ModuleInfo(aliases = "module.ghost.autoclicker.name", description = "module.ghost.autoclicker.description", category = Category.GHOST)
public class AutoClicker extends Module {
    private final ModeValue mode = new ModeValue("Mode", this)
        .add(new NormalAutoClicker("Normal", this))
        .add(new DragClickSimulationsAutoClicker("Drag Click Simulations", this))
        .setDefault("Normal");
    private final BooleanValue jitter = new BooleanValue("Jitter", this, false);
    private final StopWatch stopWatch = new StopWatch();
    private double directionX;
    private double directionY;
    @EventLink
    public final Listener<ClickEvent> onClick = var1 -> {
        this.stopWatch.aX();
        this.directionX = (Math.random() - 0.5) * 4.0;
        this.directionY = (Math.random() - 0.5) * 4.0;
    };
    @EventLink
    public final Listener<Render3DEvent> onRender3D = var1 -> {
        aEg.leftClickCounter = -1;
        if (!this.stopWatch.T(100L) && this.jitter.wo() && aEg.gameSettings.cgK.isKeyDown()) {
            EntityRenderer.bIp = (float)((Math.random() - 0.5) * 400.0 / Minecraft.getDebugFPS() * this.directionX);
            EntityRenderer.bIq = (float)((Math.random() - 0.5) * 400.0 / Minecraft.getDebugFPS() * this.directionY);
        }
    };

    public AutoClicker() {
    }
}
