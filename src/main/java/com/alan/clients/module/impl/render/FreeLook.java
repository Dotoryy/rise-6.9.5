package com.alan.clients.module.impl.render;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.TeleportEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.value.impl.BooleanValue;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

@ModuleInfo(aliases = "module.render.freelook.name", description = "module.render.freelook.description", category = Category.RENDER)
public final class FreeLook extends Module {
    public BooleanValue invertPitch = new BooleanValue("Invert Pitch", this, false);
    private int previousPerspective;
    public float originalYaw;
    public float originalPitch;
    public float Il;
    public float Im;
    @EventLink(value = 1)
    public final Listener<Render2DEvent> onRender2D = var1 -> {
        if (this.getKey() != 0 && Keyboard.isKeyDown(this.getKey())) {
            aEg.bgr.atf();
            float f = aEg.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = (float)(f * f * f * 1.5);
            this.Il = this.Il + aEg.bgr.dyD * f1;
            this.Im = this.Im - aEg.bgr.dyE * f1;
            this.Im = MathHelper.clamp_float(this.Im, -90.0F, 90.0F);
            aEg.gameSettings.thirdPersonView = 1;
        } else {
            this.setEnabled(false);
        }
    };
    @EventLink(value = 1)
    public final Listener<TeleportEvent> onTeleport = var1 -> {
        this.originalYaw = var1.getYaw();
        this.originalPitch = var1.getPitch();
    };

    public FreeLook() {
    }

    @Override
    public void onEnable() {
        this.previousPerspective = aEg.gameSettings.thirdPersonView;
        this.originalYaw = this.Il = aEg.thePlayer.pl;
        this.originalPitch = this.Im = aEg.thePlayer.rotationPitch;
        if (this.invertPitch.wo()) {
            this.Im *= -1.0F;
        }

        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void onDisable() {
        aEg.thePlayer.pl = this.originalYaw;
        aEg.thePlayer.rotationPitch = this.originalPitch;
        aEg.gameSettings.thirdPersonView = this.previousPerspective;
    }
}
