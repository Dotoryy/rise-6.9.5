package com.alan.clients.component.impl.render.espcomponent.api;

import com.alan.clients.module.impl.combat.KillAura;
import com.alan.clients.module.impl.combat.TeleportAura;
import com.alan.clients.util.interfaces.InstanceAccess;
import java.awt.Color;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class ESP implements InstanceAccess {
    public ESPColor espColor;
    public Entity target;
    public int tick;
    public static KillAura gj;
    public static TeleportAura hR;

    public ESP(ESPColor espColor) {
        this.espColor = espColor;
        this.tick = aEg.thePlayer.ticksExisted;
    }

    public void co() {
    }

    public void render3D() {
    }

    public void a(EntityPlayer player, ModelBiped modelBiped) {
    }

    public void cq() {
        if (gj == null) {
            gj = this.e(KillAura.class);
        }

        if (hR == null) {
            hR = this.e(TeleportAura.class);
        }

        if (gj.jE != null) {
            this.target = gj.jE;
        } else if (hR.target != null) {
            this.target = hR.target;
        } else {
            this.target = null;
        }
    }

    public Color getColor(EntityLivingBase living) {
        Color color = this.espColor.getColor();
        if (living == null) {
            return color;
        }

        if (living.hurtTime > 0) {
            color = this.espColor.cs();
        } else if (this.target == living) {
            color = this.espColor.ct();
        }

        return color;
    }
}
