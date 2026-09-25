package com.alan.clients.module.impl.player;

import com.alan.clients.Client;
import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.Module;
import com.alan.clients.module.api.Category;
import com.alan.clients.module.api.ModuleInfo;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PostMotionEvent;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.other.WorldChangeEvent;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.value.impl.BoundsNumberValue;
import com.alan.clients.util.math.MathUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;

@ModuleInfo(aliases = "module.player.blink.name", description = "module.player.blink.description", category = Category.PLAYER)
public class Blink extends Module {
    public BooleanValue pulse = new BooleanValue("Pulse", this, false);
    public BoundsNumberValue delay = new BoundsNumberValue("Delay", this, 2, 2, 2, 40, 1, () -> !this.pulse.wo());
    public int next;
    private EntityOtherPlayerMP blinkEntity;
    @EventLink
    public final Listener<PreMotionEvent> onPreMotionEvent = var0 -> BlinkComponent.blink();
    @EventLink
    public final Listener<PostMotionEvent> onPostMotion = var1 -> {
        if (aEg.thePlayer.ticksExisted > this.next && this.pulse.wo()) {
            this.gi();
            BlinkComponent.dispatch();
            this.deSpawnEntity();
        }
    };
    @EventLink
    public final Listener<WorldChangeEvent> onWorldChange = var1 -> this.gi();

    public Blink() {
    }

    @Override
    public void onEnable() {
        this.gi();
    }

    @Override
    public void onDisable() {
        this.deSpawnEntity();
    }

    public void gi() {
        if (aEg.thePlayer != null) {
            this.next = aEg.thePlayer.ticksExisted + (int)MathUtil.l(this.delay.wo().intValue(), this.delay.wA().intValue());
        }
    }

    public void deSpawnEntity() {
        if (this.blinkEntity != null) {
            Client.a.getBotManager().c(this, this.blinkEntity);
            aEg.theWorld.removeEntityFromWorld(this.blinkEntity.getEntityId());
            this.blinkEntity = null;
        }
    }
}
