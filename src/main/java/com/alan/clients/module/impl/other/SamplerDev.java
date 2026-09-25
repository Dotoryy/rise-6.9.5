package com.alan.clients.module.impl.other;

import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.util.vector.Vector2f;
import com.alan.clients.util.chat.ChatUtil;
import com.alan.clients.util.vector.Vector3d;
import com.alan.clients.component.impl.combat.TargetComponent;
import com.alan.clients.module.impl.other.sampler.SampleKey;
import java.util.HashMap;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;

@ModuleInfo(aliases = "Sampler (Dev)", description = "module.other.antiafk.description", category = Category.PLAYER)
public final class SamplerDev extends Module {
    private final HashMap<String, Vector2f> samples = new HashMap<>();
    private Vector2f lastRotation;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1 -> {
        EntityLivingBase entitylivingbase = TargetComponent.e(6.0);
        EntityPlayerSP entityplayersp = aEg.thePlayer;
        Vector2f vector2f = new Vector2f(entityplayersp.pl % 360.0F, entityplayersp.rotationPitch);
        if (entitylivingbase != null && this.lastRotation != null) {
            SampleKey sg = new SampleKey(
                new Vector3d(entitylivingbase.posX - entityplayersp.posX, entitylivingbase.posY - entityplayersp.posY, entitylivingbase.posZ - entityplayersp.posZ),
                new Vector2f(vector2f.getX(), vector2f.getY())
            );
            String s = sg.toKey();
            if (this.samples.containsKey(s)) {
                Vector2f vector2f1 = this.samples.get(sg.toKey());
                aEg.thePlayer.pl = aEg.thePlayer.pl + vector2f1.getX();
                aEg.thePlayer.rotationPitch = aEg.thePlayer.rotationPitch + vector2f1.getY();
                ChatUtil.b("Contained: " + s);
                ChatUtil.b("Yaw: " + vector2f1.getX() + " Pitch: " + vector2f1.getY());
            } else {
                ChatUtil.b("None " + s);
            }
        }

        this.lastRotation = new Vector2f(vector2f.getX(), vector2f.getY());
    };

    public SamplerDev() {
    }

    @Override
    public void onEnable() {
        if (aEg.gameSettings.keyBindSneak.isKeyDown()) {
            this.samples.clear();
        }
    }
}
