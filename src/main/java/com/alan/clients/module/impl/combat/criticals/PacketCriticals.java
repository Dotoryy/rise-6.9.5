package com.alan.clients.module.impl.combat.criticals;

import com.alan.clients.module.impl.combat.Criticals;
import com.alan.clients.newevent.Listener;
import com.alan.clients.newevent.annotations.EventLink;
import com.alan.clients.newevent.impl.other.AttackEvent;
import com.alan.clients.value.Mode;
import com.alan.clients.value.impl.NumberValue;
import com.alan.clients.util.packet.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import rip.vantage.commons.util.time.StopWatch;

public final class PacketCriticals extends Mode<Criticals> {
    private final NumberValue delay = new NumberValue("Delay", this, 500, 0, 1000, 1);
    private final double[] offsets = new double[]{0.42, 0.0};
    private final StopWatch stopwatch = new StopWatch();
    @EventLink
    public final Listener<AttackEvent> onAttack = var1x -> {
        if (this.stopwatch.T(this.delay.wo().longValue()) && aEg.thePlayer.cqL > 0) {
            for (double d0 : this.offsets) {
                PacketUtil.send(new C04PacketPlayerPosition(aEg.thePlayer.posX, aEg.thePlayer.posY + d0, aEg.thePlayer.posZ, false));
            }

            aEg.thePlayer.onCriticalHit(var1x.getLiving());
            this.stopwatch.aX();
        }
    };

    public PacketCriticals(String var1, Criticals criticals) {
        super(var1, criticals);
    }
}
