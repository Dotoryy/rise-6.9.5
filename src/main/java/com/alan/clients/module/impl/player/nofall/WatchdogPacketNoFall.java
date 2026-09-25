package com.alan.clients.module.impl.player.nofall;

import com.alan.clients.Client;
import com.alan.clients.module.impl.exploit.Disabler;
import com.alan.clients.module.impl.movement.Flight;
import com.alan.clients.module.impl.movement.LongJump;
import com.alan.clients.module.impl.movement.Speed;
import com.alan.clients.module.impl.player.NoFall;
import com.alan.clients.module.impl.player.Scaffold;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.motion.PreMotionEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.BooleanValue;
import com.alan.clients.util.packet.PacketUtil;
import com.alan.clients.util.player.PlayerUtil;
import com.alan.clients.component.impl.player.FallDistanceComponent;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class WatchdogPacketNoFall extends Mode<NoFall> {
    public final BooleanValue prediction = new BooleanValue("Prediction", this, false);
    @EventLink
    public final Listener<PreMotionEvent> onPreMotion = var1x -> {
        boolean flag = false;

        for (int i = 0; i <= 200; i++) {
            WorldClient worldclient = PlayerUtil.aEg.theWorld;
            BlockPos blockpos = new BlockPos(aEg.thePlayer.posX, aEg.thePlayer.posY - i, aEg.thePlayer.posZ);
            if (worldclient.getBlockState(blockpos).getBlock() != Blocks.air || aEg.thePlayer.onGround) {
                flag = false;
                break;
            }

            flag = true;
        }

        if (FallDistanceComponent.cY > 3.1 + aEg.thePlayer.motionY
            && !flag
            && !this.e(Scaffold.class).isEnabled()
            && !this.e(LongJump.class).isEnabled()
            && (!this.e(Flight.class).isEnabled() || !this.prediction.wo())
            && (!this.e(Speed.class).isEnabled() || !this.prediction.wo())) {
            if (!this.e(Disabler.class).watchdogFly.wo() || !this.e(Disabler.class).isEnabled()) {
                var1x.setPosY(var1x.getPosY() + 1.0E-13);
            }

            PacketUtil.send(new C03PacketPlayer(true));
            aEg.timer.dzD = 0.5F;
            FallDistanceComponent.cY = 0.0F;
        } else if (FallDistanceComponent.cY > 3.1 + aEg.thePlayer.motionY
            && !this.e(Scaffold.class).isEnabled()
            && (!this.e(Flight.class).isEnabled() || !this.prediction.wo())
            && (!this.e(LongJump.class).isEnabled() || !Client.a.g().c(LongJump.class).mode.wo().getName().equals("Watchdog 2"))) {
            if (!flag || this.e(LongJump.class).isEnabled() && Client.a.g().c(LongJump.class).mode.wo().getName().equals("Watchdog Fire Ball 2")) {
                if (!this.e(Disabler.class).watchdogFly.wo() || !this.e(Disabler.class).isEnabled()) {
                    var1x.setPosY(var1x.getPosY() + 1.0E-13);
                }

                PacketUtil.send(new C03PacketPlayer(true));
                aEg.timer.dzD = 0.5F;
                FallDistanceComponent.cY = 0.0F;
            }
        } else if (Client.a.g().c(LongJump.class).mode.wo().getName().equals("Watchdog 2")
            && this.e(LongJump.class).isEnabled()
            && FallDistanceComponent.cY > 4.0 + aEg.thePlayer.motionY) {
            var1x.setPosY(var1x.getPosY() + 1.0E-13);
            PacketUtil.send(new C03PacketPlayer(true));
            aEg.timer.dzD = 0.5F;
            FallDistanceComponent.cY = 0.0F;
        }

        if (FallDistanceComponent.cY > 3.1 + aEg.thePlayer.motionY
            && !flag
            && this.e(Speed.class).isEnabled()
            && this.prediction.wo()
            && (!this.e(Disabler.class).watchdogFly.wo() || !this.e(Disabler.class).isEnabled())) {
            var1x.setPosY(var1x.getPosY() + 1.0E-13);
            PacketUtil.send(new C03PacketPlayer(true));
            aEg.timer.dzD = 0.5F;
            FallDistanceComponent.cY = 0.0F;
        }
    };

    public WatchdogPacketNoFall(String var1, NoFall noFall) {
        super(var1, noFall);
    }
}
