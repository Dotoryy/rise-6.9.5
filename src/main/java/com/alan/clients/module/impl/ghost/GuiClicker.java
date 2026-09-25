package com.alan.clients.module.impl.ghost;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Mouse;

@ModuleInfo(aliases = "module.ghost.guiclicker.name", description = "module.ghost.guiclicker.description", category = Category.GHOST)
public class GuiClicker extends Module {
    public int mouseDownTicks;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var1 -> {
        if (aEg.currentScreen instanceof GuiContainer guicontainer) {
            int i = Mouse.getEventX() * guicontainer.width / aEg.displayWidth;
            int j = guicontainer.height - Mouse.getEventY() * guicontainer.height / aEg.displayHeight - 1;

            try {
                if (Mouse.isButtonDown(0)) {
                    this.mouseDownTicks++;
                    if (this.mouseDownTicks > 2 && Math.random() > 0.1) {
                        guicontainer.mouseClicked(i, j, 0);
                    }
                } else if (Mouse.isButtonDown(1)) {
                    this.mouseDownTicks++;
                    if (this.mouseDownTicks > 2 && Math.random() > 0.1) {
                        guicontainer.mouseClicked(i, j, 1);
                    }
                } else {
                    this.mouseDownTicks = 0;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    };

    public GuiClicker() {
    }
}
