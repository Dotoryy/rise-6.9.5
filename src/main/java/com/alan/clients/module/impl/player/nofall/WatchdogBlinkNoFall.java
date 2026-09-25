package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.component.impl.player.BlinkComponent;
import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.newevent.impl.render.Render2DEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;

public final class WatchdogBlinkNoFall extends Mode<NoFall> {
    public int blinkTicks = 0;
    public boolean blinking;
    public final BooleanValue packet = new BooleanValue("Packet", this, true);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        if (PlayerUtil.vh() && !this.e(Scaffold.class).isEnabled()) {
            if (aEg.thePlayer.tR == 1 && aEg.thePlayer.motionY < 0.0 && PlayerUtil.vh() && !PlayerUtil.ad(3.0)) {
                this.blinking = true;
            }

            if (this.blinking) {
                BlinkComponent.a(99999, true, false, false, false, true);
                var1x.setOnGround(true);
                this.blinkTicks++;
            }

            if (this.blinking && aEg.thePlayer.onGround) {
                BlinkComponent.dispatch();
                this.blinking = false;
                this.blinkTicks = 0;
            }

            if (this.blinkTicks <= 0 && FallDistanceComponent.cY > 2.9 && !this.e(Scaffold.class).isEnabled()) {
                var1x.setPosY(var1x.getPosY() + 1.0E-13);
                PacketUtil.send(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 0, new ItemStack(Items.water_bucket, 1), 0.5F, 0.5F, 0.5F));
                aEg.timer.dzD = 0.5F;
                PacketUtil.send(new C03PacketPlayer(true));
                FallDistanceComponent.cY = 0.0F;
            }
        }
    };
    @EventLink
    public final Listener<Render2DEvent> onRender2D = var1x -> {
        if (this.blinkTicks > 0) {
            aEg.fontRendererObj.drawString("Blinking: " + this.blinkTicks, aEg.jY.getScaledWidth() / 2.0, aEg.jY.getScaledHeight() / 2.0 + 20.0, -1);
        }
    };

    public WatchdogBlinkNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
